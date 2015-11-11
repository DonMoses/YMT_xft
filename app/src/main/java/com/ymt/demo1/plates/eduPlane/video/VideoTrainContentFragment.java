package com.ymt.demo1.plates.eduPlane.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.ymt.demo1.adapter.edu.VideoTrainAdapter;
import com.ymt.demo1.beams.edu.VideoTrainItem;
import com.ymt.demo1.plates.knowledge.WebVideoActivity;
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
public class VideoTrainContentFragment extends Fragment {
    private static VideoTrainContentFragment videoTrainContentFragment;
    private static String TAG = "EasyWrongContentFragment";
    private String type;        //UI的类型
    private String level;
    private PullToRefreshListView listView;
    private List<VideoTrainItem> videoTrainItems;
    private RequestQueue mQueue;
    private VideoTrainAdapter videoTrainAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_collect_and_easy_wrong_content_list, container, false);
        initView(view);
        return view;
    }

    public static VideoTrainContentFragment getInstance(String type) {
        if (videoTrainContentFragment == null) {
            videoTrainContentFragment = new VideoTrainContentFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        videoTrainContentFragment.setArguments(bundle);
        return videoTrainContentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getActivity());
        videoTrainItems = new ArrayList<>();
        type = getArguments().getString("type", "collect");
        switch (type) { //视频
            case "1":
                level = "一级注册消防工程师";    //1、2、3
                videoTrainItems.addAll(DataSupport.where("subId < ?", "4").find(VideoTrainItem.class));
                break;
            case "2":
                level = "二级注册消防工程师";    //4、5
                videoTrainItems.addAll(DataSupport.where("subId > ? and subId < ?", "3", "6").find(VideoTrainItem.class));
                break;
            case "3":
                level = "初级建(构)筑物消防员";  //6
                videoTrainItems.addAll(DataSupport.where("subId > ? and subId < ?", "5", "7").find(VideoTrainItem.class));
                break;
            case "4":
                level = "中级建(构)筑物消防员";  //7
                videoTrainItems.addAll(DataSupport.where("subId > ?", "6").find(VideoTrainItem.class));
                break;
            default:
                break;
        }

        new TitleTypeAct((VideoTrainActivity) getActivity()).setCollectType(type);
    }

    private void initView(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.collect_and_easy_wrong_list_view);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ProgressBar progressBar = new ProgressBar(getActivity());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(AppContext.screenWidth / 8, AppContext.screenWidth / 8);
        progressBar.setLayoutParams(layoutParams);
        listView.setEmptyView(progressBar);
        videoTrainAdapter = new VideoTrainAdapter(getActivity(), AppContext.screenWidth);
        listView.setAdapter(videoTrainAdapter);
        videoTrainAdapter.setVideos(videoTrainItems);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //播放视频
                Intent intent = new Intent(getActivity(), WebVideoActivity.class);
                intent.putExtra("mp4_url", BaseURLUtil.getEduTrainVideoUrl(((VideoTrainItem) parent.getAdapter().getItem(position)).getUrl()));
                startActivity(intent);
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    videoTrainItems.clear();
                    mQueue.add(getEasyWrongTopic(level));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        mQueue.add(getEasyWrongTopic(level));
    }

    private static class TitleTypeAct {
        private WeakReference<VideoTrainActivity> weakReference;

        private TitleTypeAct(VideoTrainActivity videoTrainActivity) {
            weakReference = new WeakReference<>(videoTrainActivity);
        }

        //// TODO: 2015/11/11 此处有Fragment动画未结束时退出Activity有内存溢出bug【待修复】
        private void setCollectType(String type) {
            VideoTrainActivity videoTrainActivity = weakReference.get();
            if (videoTrainActivity != null) {
                videoTrainActivity.setTitleType(type);
            }
        }
    }

    //获取易错题[根据考试类型]
    private StringRequest getEasyWrongTopic(final String level) {
        return new StringRequest(BaseURLUtil.getVideoTrainList(level), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.optString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            VideoTrainItem trainItem = new VideoTrainItem();
                            trainItem.setColumnType(object.optString("columnType"));
                            trainItem.setDescs(object.optString("descs"));
                            trainItem.setDownNum(object.optInt("downNum"));
                            trainItem.setHistoryId(object.optString("historyId"));
                            trainItem.setModel(object.optInt("model"));
                            trainItem.setOpTime(object.optString("opTime"));
                            trainItem.setReplays(object.optInt("replays"));
                            trainItem.setScore(object.optInt("score"));
                            trainItem.setSubId(object.optInt("subId"));
                            trainItem.setTitle(object.optString("title"));
                            trainItem.setUrl(object.optString("url"));
                            trainItem.setViews(object.optString("views"));
                            trainItem.setYuer(object.optString("yuer"));

                            if (DataSupport.where("historyId = ?", trainItem.getHistoryId()).find(VideoTrainItem.class)
                                    .size() == 0) {
                                trainItem.save();
                            } else {
                                trainItem.updateAll("historyId = ?", trainItem.getHistoryId());
                            }
                        }
                        videoTrainItems.clear();
                        switch (type) { //视频
                            case "1":
                                //1、2、3
                                videoTrainItems.addAll(DataSupport.where("subId < ?", "4").find(VideoTrainItem.class));
                                break;
                            case "2":
                                //4、5
                                videoTrainItems.addAll(DataSupport.where("subId > ? and subId < ?", "3", "6").find(VideoTrainItem.class));
                                break;
                            case "3":
                                //6
                                videoTrainItems.addAll(DataSupport.where("subId > ? and subId < ?", "5", "7").find(VideoTrainItem.class));
                                break;
                            case "4":
                                //7
                                videoTrainItems.addAll(DataSupport.where("subId > ?", "6").find(VideoTrainItem.class));
                                break;
                            default:
                                break;
                        }
                        videoTrainAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    AppContext.toastBadJson();
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
