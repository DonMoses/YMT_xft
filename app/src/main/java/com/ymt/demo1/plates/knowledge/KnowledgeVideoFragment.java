package com.ymt.demo1.plates.knowledge;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.VideoListAdapter;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.knowledge.KnowledgeVideo;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/6/16
 */
public class KnowledgeVideoFragment extends BaseFragment {
    public static final String KNOWLEDGE_SPZL = "spzl";    //视频资料
    public static final String TAG = "KnowledgeVideoFragment";
    private List<KnowledgeVideo> videoList;
    private RequestQueue mQueue;
    private VideoListAdapter videoListAdapter;
    private PullToRefreshListView videoListView;
    private int pageSize;
    private int startIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledge_video, container, false);
        initView(view);
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageSize = 12;
        startIndex = 1;
        videoList = new ArrayList<>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mQueue = ((KnowledgeMainActivity) activity).mQueue;
    }

    protected void initView(View view) {
        videoListView = (PullToRefreshListView) view.findViewById(R.id.video_grid_view);
        videoListAdapter = new VideoListAdapter(getActivity(), AppContext.screenWidth);
        videoListView.setAdapter(videoListAdapter);
        videoList.addAll(DataSupport.findAll(KnowledgeVideo.class));
        videoListAdapter.setVideos(videoList);
        videoListView.onRefreshComplete();

        videoListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                startIndex = 1;
                videoList.clear();
                DataSupport.deleteAll(KnowledgeVideo.class);
                videoListAdapter.setVideos(videoList);
                mQueue.add(getKnowledgeVideo(pageSize, startIndex, ""));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                startIndex++;
                mQueue.add(getKnowledgeVideo(pageSize, startIndex, ""));
            }
        });
    }

    /**
     * 知识平台-视频
     */
    protected StringRequest getKnowledgeVideo(int pageSize, int start, String searchWhat) {
        return new StringRequest(BaseURLUtil.doGetKnowledgeAction(KnowledgeVideoFragment.KNOWLEDGE_SPZL, pageSize, start, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            KnowledgeVideo video = new KnowledgeVideo();
                            String id = obj.optString("id");
                            video.setCreate_time(obj.optString("create_time"));
                            video.setArticle_title(obj.optString("article_title"));
                            video.setContent(obj.optString("content"));
                            video.setDowncount(obj.optString("downcount"));
                            video.setFiles(obj.optString("files"));
                            video.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            video.setHitnum(obj.optString("hitnum"));
                            video.setMeta_keys(obj.optString("meta_keys"));
                            video.setScore(obj.optString("score"));
                            video.setStatus(obj.optString("status"));
                            video.setThe_id(id);
                            int size = DataSupport.where("the_id = ?", id).find(KnowledgeVideo.class).size();
                            if (size == 0) {
                                video.save();
                            } else {
                                video.updateAll("the_id = ?", id);
                            }
                        }

                        videoList.clear();
                        videoList.addAll(DataSupport.findAll(KnowledgeVideo.class));
                        videoListAdapter.setVideos(videoList);
                        videoListView.onRefreshComplete();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }
}
