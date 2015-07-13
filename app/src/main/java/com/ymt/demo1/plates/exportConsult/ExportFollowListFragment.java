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
import com.ymt.demo1.adapter.ExportFollowAdapter;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.beams.expert_consult.FollowedExpert;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportFollowListFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ExportFollowFragment";
    private RequestQueue mQueue;
    private int start;
    private int pageSize;
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
        start = 1;
        pageSize = 10;
        followedExperts = new ArrayList<>();
//        Bundle bundle = getArguments();
//        String emptyInfo = bundle.getString("empty_info");
    }

    /**
     * 初始化关注界面
     */
    protected void initFollowList(View view) {
        chatListView = (PullToRefreshListView) view.findViewById(R.id.followed_list_view);
        mQueue.add(getFollowedList(start, pageSize, AppContext.now_session_id));
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
                Toast.makeText(getActivity(), "export " + String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

        chatListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = 1;
                followedExperts.clear();
                followAdapter.setList(followedExperts);
                mQueue.add(getFollowedList(start, pageSize, AppContext.now_session_id));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start++;
                mQueue.add(getFollowedList(start, pageSize, AppContext.now_session_id));
            }
        });


    }


    /**
     * 获取关注
     */
    private StringRequest getFollowedList(int start, int pageSize, String sId) {
        return new StringRequest(BaseURLUtil.followedExpertList(start, pageSize, sId), new Response.Listener<String>() {
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
                            expert.setFk_expert_id(obj.optString("fk_expert_id"));
                            expert.setThe_id(obj.optString("id"));
                            expert.setExpert_type(obj.optString("expert_type"));
                            expert.setExpert_head_pic(BaseURLUtil.BASE_URL + obj.optString("expert_head_pic"));
                            expert.setCreate_time(obj.optString("create_time"));
                            expert.setFk_user_id(obj.optString("fk_user_id"));
                            expert.setExpert_experience(obj.optString("expert_experience"));
                            expert.setExpert_name(obj.optString("expert_name"));
                            followedExperts.add(expert);
                            followAdapter.setList(followedExperts);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                chatListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                chatListView.onRefreshComplete();
            }
        });
    }


}
