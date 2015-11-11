package com.ymt.demo1.main.collect;

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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.collect.CollectItemAdapter;
import com.ymt.demo1.beams.collect.CollectCon;
import com.ymt.demo1.beams.collect.CollectEdu;
import com.ymt.demo1.beams.collect.CollectKno;
import com.ymt.demo1.beams.knowledge.KnowledgeItem;
import com.ymt.demo1.main.sign.SignInUpActivity;
import com.ymt.demo1.plates.eduPlane.pastExams.PastExamDetailActivity;
import com.ymt.demo1.plates.exportConsult.ConsultDetailActivity;
import com.ymt.demo1.plates.knowledge.KnowledgeItemDetailActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DonMoses on 2015/11/9
 */
public class CollectContentFragment extends Fragment {
    private static CollectContentFragment collectContentFragment;
    private static String TAG = "CollectContentFragment";
    private String type;        //UI的类型
    private int index;
    private int pageNum;
    private PullToRefreshListView listView;
    private List<Object> collectItems;
    private RequestQueue mQueue;
    private CollectItemAdapter collectItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_collect_and_easy_wrong_content_list, container, false);
        initView(view);
        return view;
    }

    public static CollectContentFragment getInstance(String type) {
        if (collectContentFragment == null) {
            collectContentFragment = new CollectContentFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        collectContentFragment.setArguments(bundle);
        return collectContentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getActivity());
        index = 1;
        pageNum = 10;
        collectItems = new ArrayList<>();
        type = getArguments().getString("type", "collect");

        new TitleTypeAct((CollectActivity) getActivity()).setCollectType(type);
    }

    private void initView(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.collect_and_easy_wrong_list_view);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        ProgressBar progressBar = new ProgressBar(getActivity());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(AppContext.screenWidth / 8, AppContext.screenWidth / 8);
        progressBar.setLayoutParams(layoutParams);
        listView.setEmptyView(progressBar);

        collectItemAdapter = new CollectItemAdapter(getActivity(), Integer.valueOf(type));

        listView.setAdapter(collectItemAdapter);
        collectItemAdapter.setObjects(collectItems);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type) {
                    case "1": //咨询
                        Intent intent1 = new Intent(getActivity(), ConsultDetailActivity.class);
                        intent1.putExtra("cid", ((CollectCon) parent.getAdapter().getItem(position)).getConsult_id());
                        startActivity(intent1);
                        break;
                    case "2": //知识
                        Intent intent2 = new Intent(getActivity(), KnowledgeItemDetailActivity.class);
                        intent2.putExtra("knowId", ((CollectKno) parent.getAdapter().getItem(position)).getKnowId());
                        startActivity(intent2);
                        break;
                    case "3": //教育
                        Intent intent3 = new Intent(getActivity(), PastExamDetailActivity.class);
                        intent3.putExtra("historyId", ((CollectEdu) parent.getAdapter().getItem(position)).getHistoryId());
                        startActivity(intent3);
                        break;
                    default:
                        break;
                }
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    jsonStr = null;
                    index = 1;
                    collectItems.clear();
                    mQueue.add(getCollectItems(Integer.valueOf(type), AppContext.now_session_id, index, pageNum));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                mQueue.add(getCollectItems(Integer.valueOf(type), AppContext.now_session_id, index, pageNum));
            }
        });

        mQueue.add(getCollectItems(Integer.valueOf(type), AppContext.now_session_id, index, pageNum));
    }

    private static class TitleTypeAct {
        private WeakReference<CollectActivity> weakReference;

        private TitleTypeAct(CollectActivity collectActivity) {
            weakReference = new WeakReference<>(collectActivity);
        }

        //// TODO: 2015/11/11 此处有Fragment动画未结束时退出Activity有内存溢出bug【待修复】
        private void setCollectType(String type) {
            CollectActivity collectActivity = weakReference.get();
            if (collectActivity != null) {
                collectActivity.setTitleType(type);
            }
        }
    }

    //获取易错题[根据考试类型]
    private String jsonStr = null;

    private StringRequest getCollectItems(final int type, String sId, int index, int pageNum) {
        if (TextUtils.isEmpty(sId)) {
            Toast.makeText(getActivity(), "请先登录...", Toast.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(getActivity(), SignInUpActivity.class));
                    getActivity().finish();
                }
            }, 1000);
        }
        return new StringRequest(BaseURLUtil.getCollectItemList(type, sId, index, pageNum), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (TextUtils.isEmpty(jsonStr) || ((!TextUtils.isEmpty(jsonStr)) && (!jsonStr.equals(s)))) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.optString("result").equals("Y")) {
                            JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                switch (type) {
                                    case 1:     //咨询
                                        CollectCon con = new CollectCon();
                                        con.setTitle(object.optString("title"));
                                        con.setTime(object.optString("TIME"));
                                        con.setViews(object.optInt("views"));
                                        con.setUserId(object.optInt("userId"));
                                        con.setConsult_id(object.optInt("consult_id"));
                                        collectItems.add(con);
                                        break;
                                    case 2:     //知识
                                        CollectKno kno = new CollectKno();
                                        kno.setCollectionTime(object.optString("collectionTime"));
                                        kno.setUser_name(object.optString("user_name"));
                                        kno.setAvr_scor(object.optInt("avr_scor"));
                                        kno.setDocTitle(object.optString("docTitle"));
                                        kno.setKnowId(object.optString("knowId"));
                                        collectItems.add(kno);
                                        break;
                                    case 3:     //教育
                                        CollectEdu edu = new CollectEdu();
                                        edu.setTime(object.optString("time"));
                                        edu.setTitle(object.optString("title"));
                                        edu.setColumnType(object.optString("columnType"));
                                        edu.setHistoryId(object.optString("historyId"));
                                        edu.setScore(object.optInt("score"));
                                        collectItems.add(edu);
                                        break;
                                    default:
                                        break;
                                }
                                //// TODO: 2015/11/11
                                collectItemAdapter.notifyDataSetChanged();
                            }

                        }
                        jsonStr = s;

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
