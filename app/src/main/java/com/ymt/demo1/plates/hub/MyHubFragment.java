/*
 * Copyright 2014 Don Moses
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ymt.demo1.plates.hub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.hub.MyHubPostAdapter;
import com.ymt.demo1.adapter.hub.MyHubReplyAdapter;
import com.ymt.demo1.adapter.hub.MyHubSysInfoAdapter;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.hub.MyHubPost;
import com.ymt.demo1.beams.hub.MyHubReply;
import com.ymt.demo1.beams.hub.MyHubSysInfo;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollView;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollViewCallbacks;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for ViewPagerTabFragmentActivity.
 * ScrollView callbacks are handled by its parent fragment, not its parent activity.
 */
public class MyHubFragment extends BaseFragment {
    private ObservableScrollView scrollView;
    private PullToRefreshListView pullToRefreshListView;
    public static final String MY_POST = "post";
    public static final String MY_REPLIES = "reply";
    public static final String MY_SYS_INFO = "sys";
    private String type;
    private RequestQueue mQueue;
    private int index = 1;
    //我的发帖
    private MyHubPostAdapter hubPostAdapter;
    private List<MyHubPost> postList;
    //我的回帖
    private MyHubReplyAdapter hubReplyAdapter;
    private List<MyHubReply> replyList;
    //系统消息
    private MyHubSysInfoAdapter hubSysInfoAdapter;
    private List<MyHubSysInfo> sysInfoList;
    //监听
    private static HubReplyPostCountGetter countGetter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrollview_ultra_list_view, container, false);

        scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            scrollView.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }

        initView(view);
        switch (type) {
            case MY_POST:       //我的发帖
                postList = new ArrayList<>();
                mQueue.add(getHubMyPost(AppContext.now_user_name, index));
                pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                break;
            case MY_REPLIES:    //我的回帖
                replyList = new ArrayList<>();
                mQueue.add(getHubMyReplies(AppContext.now_user_name, index));
                pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                break;
            case MY_SYS_INFO:   //系统消息
                sysInfoList = new ArrayList<>();
                mQueue.add(getHubSysInfo(AppContext.now_user_name));
                pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                break;
            default:
                break;
        }

        return view;
    }

    public static MyHubFragment getInstance(String myHubItemType, HubReplyPostCountGetter countGetter) {
        MyHubFragment.countGetter = countGetter;

        MyHubFragment fragment = new MyHubFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", myHubItemType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        mQueue = Volley.newRequestQueue(getActivity());
    }

    protected void initView(View view) {

        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_list_view);

        scrollView.setListView(pullToRefreshListView);            //测量、监听listView的变化
        switch (type) {
            case MY_POST:       //我的发帖
                hubPostAdapter = new MyHubPostAdapter(getActivity());
                pullToRefreshListView.setAdapter(hubPostAdapter);
                break;
            case MY_REPLIES:    //我的回帖
                hubReplyAdapter = new MyHubReplyAdapter(getActivity());
                pullToRefreshListView.setAdapter(hubReplyAdapter);
                break;
            case MY_SYS_INFO:   //系统消息
                hubSysInfoAdapter = new MyHubSysInfoAdapter(getActivity());
                pullToRefreshListView.setAdapter(hubSysInfoAdapter);
                break;
            default:
                break;
        }

        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type) {
                    case MY_POST:       //我的发帖
                        Intent intent1 = new Intent(getActivity(), PostContentActivity.class);
                        intent1.putExtra("tid", ((MyHubPost) parent.getAdapter().getItem(position)).getTid());
                        intent1.putExtra("author", ((MyHubPost) (parent.getAdapter().getItem(position))).getAuthor());
                        intent1.putExtra("subject", ((MyHubPost) (parent.getAdapter().getItem(position))).getSubject());
                        startActivity(intent1);
                        break;
                    case MY_REPLIES:    //我的回帖
                        Intent intent2 = new Intent(getActivity(), PostContentActivity.class);
                        intent2.putExtra("tid", ((MyHubReply) parent.getAdapter().getItem(position)).getTid());
                        intent2.putExtra("author", ((MyHubReply) (parent.getAdapter().getItem(position))).getLastposter());
                        intent2.putExtra("subject", ((MyHubReply) (parent.getAdapter().getItem(position))).getSubject());
                        startActivity(intent2);
                        break;
                    case MY_SYS_INFO:   //系统消息
                        //todo 系统消息
                        break;
                    default:
                        break;
                }
            }
        });

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 1;
                switch (type) {
                    case MY_POST:       //我的发帖
                        postList.clear();
                        hubPostAdapter.setSubjects(postList);
                        mQueue.add(getHubMyPost(AppContext.now_user_name, index));
                        break;
                    case MY_REPLIES:    //我的回帖
                        replyList.clear();
                        hubReplyAdapter.setSubjects(replyList);
                        mQueue.add(getHubMyReplies(AppContext.now_user_name, index));
                        break;
                    case MY_SYS_INFO:   //系统消息
                        mQueue.add(getHubSysInfo(AppContext.now_user_name));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                switch (type) {
                    case MY_POST:       //我的发帖
                        mQueue.add(getHubMyPost(AppContext.now_user_name, index));
                        break;
                    case MY_REPLIES:    //我的回帖
                        mQueue.add(getHubMyReplies(AppContext.now_user_name, index));
                        break;
                    case MY_SYS_INFO:   //系统消息
                        mQueue.add(getHubSysInfo(AppContext.now_user_name));
                        break;
                    default:
                        break;
                }
            }
        });

    }

    /**
     * 我的发帖
     */
    protected StringRequest getHubMyPost(String userName, int index) {
        return new StringRequest(BaseURLUtil.getHubMyPost(userName, index), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("retCode") == 0) {
                        countGetter.getPostCount(jsonObject.getInt("count"));

                        JSONArray array = jsonObject.getJSONArray("data");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject object = array.getJSONObject(i);
                            MyHubPost post = new MyHubPost();
                            post.setFid(object.optInt("fid"));
                            post.setReplies(object.optInt("replies"));
                            post.setViews(object.optInt("views"));
                            post.setTid(object.optInt("tid"));
                            post.setAuthor(object.optString("author"));
                            post.setLastposter(object.optString("lastposter"));
                            post.setSubject(object.optString("subject"));
                            post.setName(object.optString("name"));
                            post.setLastpost(object.optString("lastpost"));
                            post.setDateline(object.optString("dateline"));
                            postList.add(post);

                        }
                        hubPostAdapter.setSubjects(postList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pullToRefreshListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pullToRefreshListView.onRefreshComplete();
            }
        });
    }

    /**
     * 我的回帖
     */
    protected StringRequest getHubMyReplies(String userName, int index) {
        return new StringRequest(BaseURLUtil.getHubMyReplies(userName, index), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("retCode") == 0) {
                        countGetter.getReplyCount(jsonObject.getInt("count"));

                        JSONArray array = jsonObject.getJSONArray("data");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject object = array.getJSONObject(i);
                            MyHubReply reply = new MyHubReply();
                            reply.setMessage(object.optString("message"));
                            reply.setFid(object.optInt("fid"));
                            reply.setLastposter(object.optString("lastposter"));
                            reply.setReplies(object.optInt("replies"));
                            reply.setViews(object.optInt("views"));
                            reply.setSubject(object.optString("subject"));
                            reply.setName(object.optString("name"));
                            reply.setPid(object.optInt("pid"));
                            reply.setDateline(object.optString("dateline"));
                            reply.setTid(object.optInt("tid"));
                            replyList.add(reply);

                        }
                        hubReplyAdapter.setSubjects(replyList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pullToRefreshListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pullToRefreshListView.onRefreshComplete();
            }
        });
    }

    /**
     * 系统消息
     */
    protected StringRequest getHubSysInfo(String userName) {
        return new StringRequest(BaseURLUtil.getHubSysInfo(userName), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("retCode") == 0) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject object = array.getJSONObject(i);
                            MyHubSysInfo sysInfo = new MyHubSysInfo();
                            sysInfo.setAuthor(object.optInt("author"));
                            sysInfo.setAuthorid(object.optInt("authorid"));
                            sysInfo.setCategory(object.optInt("category"));
                            sysInfo.setDateline(object.optInt("dateline"));
                            sysInfo.setFrom_id(object.optInt("from_id"));
                            sysInfo.setFrom_idtype(object.optString("from_idtype"));
                            sysInfo.setFrom_num(object.optInt("from_num"));
                            sysInfo.setThe_id(object.optInt("id"));
                            sysInfo.setNew_(object.optInt("new_"));
                            sysInfo.setNote(object.optString("note"));
                            sysInfo.setType(object.optString("type"));
                            sysInfo.setUid(object.optInt("uid"));
                            sysInfoList.add(sysInfo);

                        }
                        hubSysInfoAdapter.setSubjects(sysInfoList);
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

    public interface HubReplyPostCountGetter {
        void getReplyCount(int replyCount);

        void getPostCount(int postCount);
    }

}
