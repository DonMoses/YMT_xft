package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.expertConsult.ExportFollowAdapter;
import com.ymt.demo1.beams.expert_consult.FollowedExpert;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportFollowListFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ExportFollowFragment";
    private RequestQueue mQueue;
    private List<FollowedExpert> followedExperts;
    private PullToRefreshListView chatListView;
    private ExportFollowAdapter followAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export_follow, container, false);
        initFollowList(view);
        return view;
    }

    public static ExportFollowListFragment newInstance(String emptyInfo) {
        ExportFollowListFragment exportFollowFragment = new ExportFollowListFragment();
        Bundle args = new Bundle();
        args.putString("empty_info", emptyInfo);
        exportFollowFragment.setArguments(args);
        return exportFollowFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = ((MyConsultActivity) getActivity()).mQueue;
        followedExperts = new ArrayList<>();
    }

    /**
     * 初始化关注界面
     */
    protected void initFollowList(View view) {
        chatListView = (PullToRefreshListView) view.findViewById(R.id.followed_list_view);
        chatListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mQueue.add(getFollowedList(AppContext.now_session_id));
        followAdapter = new ExportFollowAdapter(getActivity());
        chatListView.setAdapter(followAdapter);
        followAdapter.setList(followedExperts);
        chatListView.onRefreshComplete();

        /*
        todo 关注列表 点击事件
         */
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入关注详情界面
                Intent intent1 = new Intent(getActivity(), ExpertInfoActivity.class);
                intent1.putExtra("id", ((FollowedExpert) parent.getAdapter().getItem(position)).getFkExpertId());
                startActivity(intent1);
            }
        });

        chatListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    followedExperts.clear();
                    followAdapter.setList(followedExperts);
                    mQueue.add(getFollowedList(AppContext.now_session_id));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });


    }


    /**
     * 获取关注
     */
    private StringRequest getFollowedList(String sId) {
        return new StringRequest(BaseURLUtil.getFollowedExpertList(sId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject object = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = object.getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            FollowedExpert expert = new FollowedExpert();
                            expert.setFkExpertId(obj.optInt("fkExpertId"));
                            expert.setHeadPic(obj.optString("headPic"));
                            expert.setMajorWorks(obj.optString("majorWorks"));
                            expert.setUserName(obj.optString("userName"));

                            if (DataSupport.where("fkExpertId = ?", String.valueOf(expert.getFkExpertId()))
                                    .find(FollowedExpert.class)
                                    .size() != 0) {
                                expert.updateAll("fkExpertId = ?", String.valueOf(expert.getFkExpertId()));
                            } else {
                                expert.save();
                            }
                            //// TODO: 2015/11/3
                            followedExperts.add(expert);
                            followAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }

                chatListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();

                chatListView.onRefreshComplete();
            }
        });
    }


}
