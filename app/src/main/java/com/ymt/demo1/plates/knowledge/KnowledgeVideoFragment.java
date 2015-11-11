package com.ymt.demo1.plates.knowledge;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.knowledge.VideoListAdapter;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.knowledge.KnowledgeItem;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

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
    public static final String TAG = "KnowledgeVideoFragment";
    private List<KnowledgeItem> videoList;
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
        pageSize = 15;
        startIndex = 1;
        videoList = new ArrayList<>();

        if (AppContext.internetAvialable()) {
            mQueue.add(getKnowledgeVideo(pageSize, startIndex, ""));
        }
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
        videoListAdapter.setVideos(videoList);
        new AsyncTask<Void, Void, List<KnowledgeItem>>() {
            @Override
            protected List<KnowledgeItem> doInBackground(Void... params) {
                return DataSupport.where("type = ?", String.valueOf(KnowledgeItemListViewFragment.KNOWLEDGE_SPZL)).find(KnowledgeItem.class);
            }

            @Override
            protected void onPostExecute(List<KnowledgeItem> list) {
                super.onPostExecute(list);
                videoList.addAll(list);
                videoListAdapter.notifyDataSetChanged();
            }
        }.execute();

        videoListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                startIndex = 1;
                if (AppContext.internetAvialable()) {
                    videoList.clear();
                    DataSupport.deleteAll(KnowledgeItem.class, "type = ?", String.valueOf(KnowledgeItemListViewFragment.KNOWLEDGE_SPZL));
                    videoListAdapter.setVideos(videoList);
                    mQueue.add(getKnowledgeVideo(pageSize, startIndex, ""));
                } else {
                    videoListView.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                startIndex++;
                if (AppContext.internetAvialable()) {
                    mQueue.add(getKnowledgeVideo(pageSize, startIndex, ""));
                }
            }
        });

        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebVideoActivity.class);
                intent.putExtra("mp4_url", ((KnowledgeItem) parent.getAdapter().getItem(position)).getDocLoacl());
                startActivity(intent);
            }
        });
    }

    /**
     * 知识平台-视频
     */
    protected StringRequest getKnowledgeVideo(int pageSize, int start, String searchWhat) {
        String url = BaseURLUtil.doGetKnowledgeAction(KnowledgeItemListViewFragment.KNOWLEDGE_SPZL, pageSize, start, searchWhat);
//        Log.e("TAG", ">>>: KnowledgeItemListViewFragment url:  " + url);
        return new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", ">>>>>>>>>>>>>s>>>>>>>>>>" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            KnowledgeItem knowledgeItem = new KnowledgeItem();
                            knowledgeItem.setAuditorId(obj.optString("auditorId"));
                            knowledgeItem.setAuthor(obj.optString("author"));
                            knowledgeItem.setAvrScor(obj.optInt("avrScor"));
                            knowledgeItem.setDocBrief(obj.optString("docBrief"));
                            knowledgeItem.setDocTitle(obj.optString("docTitle"));
                            knowledgeItem.setDocType(obj.optString("docType"));
                            knowledgeItem.setDownTimes(obj.optInt("downTimes"));
                            knowledgeItem.setDownVal(obj.optInt("downVal"));
                            knowledgeItem.setEditor(obj.optString("editor"));
                            knowledgeItem.setFileName(obj.optString("fileName"));
                            knowledgeItem.setKeyWord(obj.optString("keyWord"));
                            knowledgeItem.setKind(obj.optString("kind"));
                            knowledgeItem.setCover(obj.optString("cover"));
                            String id = obj.optString("knowId");
                            knowledgeItem.setKnowId(id);
                            knowledgeItem.setNetType(obj.optString("netType"));
                            switch (knowledgeItem.getNetType()) {
                                case "1"://网络视频
                                    //knowledgeItem.setDocLoacl(obj.optString("docLoacl"));
                                    knowledgeItem.setDocLoacl(obj.optString("docLoacl")
                                            .replace("player.php/sid", "embed").replace("/v.swf", ""));
                                    break;
                                case "0"://系统视频
                                    knowledgeItem.setDocLoacl(BaseURLUtil.getKnowledgeStaticFile(obj.optString("docLoacl")));
//                                    Log.e("TAG", ">>>>>>>video: " + knowledgeItem.getDocLoacl());
                                    break;
                                default:
                                    break;
                            }
                            knowledgeItem.setPassTime(obj.optString("passTime"));
                            knowledgeItem.setPrtKind(obj.optString("prtKind"));
                            knowledgeItem.setReadTimes(obj.optInt("readTimes"));
                            knowledgeItem.setReason(obj.optString("reason"));
                            knowledgeItem.setScorTimes(obj.optInt("scorTimes"));
                            knowledgeItem.setSource(obj.optString("source"));
                            knowledgeItem.setStat(obj.optString("stat"));
                            knowledgeItem.setType(obj.optString("type"));
                            knowledgeItem.setUpDateTime(obj.optString("upDate"));
                            knowledgeItem.setUserid(obj.optString("userid"));
//                            Log.e("TAG",">>>>>>>>url>>>>>>>."+obj.optString("attachment"));
                            int size = DataSupport.where("knowId = ? and type = ?", id, String.valueOf(KnowledgeItemListViewFragment.KNOWLEDGE_SPZL)).find(KnowledgeItem.class).size();
                            if (size == 0) {
                                knowledgeItem.save();
                            } else {
                                knowledgeItem.updateAll("knowId = ? and type = ?", id, String.valueOf(KnowledgeItemListViewFragment.KNOWLEDGE_SPZL));
                            }
                        }

                        videoList.clear();
                        videoList.addAll(DataSupport.where("type = ?", String.valueOf(KnowledgeItemListViewFragment.KNOWLEDGE_SPZL)).find(KnowledgeItem.class));
                        videoListAdapter.setVideos(videoList);
                        videoListView.onRefreshComplete();

                    }
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
            }
        });
    }

}
