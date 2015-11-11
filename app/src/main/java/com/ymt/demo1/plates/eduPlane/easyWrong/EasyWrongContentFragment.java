package com.ymt.demo1.plates.eduPlane.easyWrong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.edu.EasyWrongTopicAdapter;
import com.ymt.demo1.beams.edu.EasyWrongTopic;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DonMoses on 2015/11/9
 */
public class EasyWrongContentFragment extends Fragment {
    private static EasyWrongContentFragment easyWrongContentFragment;
    private static String TAG = "EasyWrongContentFragment";
    private String type;
    private String level;
    private int index;
    private int pageNum;
    private PullToRefreshListView listView;
    private List<EasyWrongTopic> easyWrongTopics;
    private RequestQueue mQueue;
    private EasyWrongTopicAdapter easyWrongTopicAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_collect_and_easy_wrong_content_list, container, false);
        initView(view);
        return view;
    }

    public static EasyWrongContentFragment getInstance(String type) {
        if (easyWrongContentFragment == null) {
            easyWrongContentFragment = new EasyWrongContentFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        easyWrongContentFragment.setArguments(bundle);
        return easyWrongContentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getActivity());
        easyWrongTopics = new ArrayList<>();
        index = 1;
        pageNum = 10;
        type = getArguments().getString("type", "collect");
        switch (type) { //易错题类型
            case "1":
                level = "一级注册消防工程师";
                break;
            case "2":
                level = "二级注册消防工程师";
                break;
            case "3":
                level = "初级建(构)筑物消防员";
                break;
            case "4":
                level = "中级建(构)筑物消防员";
                break;
            default:
                break;
        }
        if (!AppContext.internetAvialable()) {
            easyWrongTopics.addAll(DataSupport.where("level = ?", level).find(EasyWrongTopic.class));
        }
        new TitleTypeAct((EasyWrongActivity) getActivity()).setCollectType(type);
    }

    private void initView(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.collect_and_easy_wrong_list_view);
        ProgressBar progressBar = new ProgressBar(getActivity());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(AppContext.screenWidth / 8, AppContext.screenWidth / 8);
        progressBar.setLayoutParams(layoutParams);
        listView.setEmptyView(progressBar);
        easyWrongTopicAdapter = new EasyWrongTopicAdapter(getActivity());
        listView.setAdapter(easyWrongTopicAdapter);
        easyWrongTopicAdapter.setList(easyWrongTopics);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EasyWrongDetailActivity.class);
                intent.putExtra("bankId", ((EasyWrongTopic) parent.getAdapter().getItem(position)).getBankId());
                startActivity(intent);
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    index = 1;
                    topicStr = null;
                    easyWrongTopics.clear();
                    easyWrongTopicAdapter.notifyDataSetChanged();
                    DataSupport.deleteAll(EasyWrongTopic.class, "level = ?", level);
                    mQueue.add(getEasyWrongTopic(index, pageNum, level));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    index++;
                    mQueue.add(getEasyWrongTopic(index, pageNum, level));
                }
            }
        });

        mQueue.add(getEasyWrongTopic(index, pageNum, level));
    }

    private static class TitleTypeAct {
        private WeakReference<EasyWrongActivity> weakReference;

        private TitleTypeAct(EasyWrongActivity easyWrongActivity) {
            weakReference = new WeakReference<>(easyWrongActivity);
        }

        //// TODO: 2015/11/11 此处有Fragment动画未结束时退出Activity有内存溢出bug【待修复】
        private void setCollectType(String type) {
            EasyWrongActivity easyWrongActivity = weakReference.get();
            if (easyWrongActivity != null) {
                easyWrongActivity.setTitleType(type);
            }
        }
    }

    //获取易错题[根据考试类型]
    private String topicStr = null;

    private StringRequest getEasyWrongTopic(int index, int pageNum, final String level) {
        return new StringRequest(BaseURLUtil.getEasyWrongList(index, pageNum, level), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (TextUtils.isEmpty(topicStr) || ((!TextUtils.isEmpty(topicStr)) && (!topicStr.equals("Y")))) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.optString("result").equals("Y")) {
                            JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                EasyWrongTopic wrongTopic = new EasyWrongTopic();
                                wrongTopic.setLevel(object.optString("level"));
                                wrongTopic.setTopicType(object.optInt("topicType"));
                                wrongTopic.setSubjects(object.optString("subjects"));
                                wrongTopic.setProblem(object.optString("problem"));
                                wrongTopic.setBankId(object.optInt("bankId"));

                                if (DataSupport.where("bankId = ?", String.valueOf(wrongTopic.getBankId())).find(EasyWrongTopic.class)
                                        .size() == 0) {
                                    wrongTopic.save();
                                } else {
                                    wrongTopic.updateAll("bankId = ?", String.valueOf(wrongTopic.getBankId()));
                                }
                            }
                            easyWrongTopics.clear();
                            easyWrongTopics.addAll(DataSupport.where("level = ?", level).find(EasyWrongTopic.class));
                            easyWrongTopicAdapter.notifyDataSetChanged();
                        }
                        topicStr = s;
                    } catch (JSONException e) {
                        AppContext.toastBadJson();
                    }
                }

                listView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                listView.onRefreshComplete();
            }
        });
    }

}
