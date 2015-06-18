/*
 * Copyright 2014 DonMoses
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
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.HubExpandListAdapter;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.hub.HubPlate;
import com.ymt.demo1.beams.hub.HubSubject;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollView;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollViewCallbacks;
import com.ymt.demo1.plates.consultCato.CatoConsultListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Fragment for ViewPagerTabFragmentActivity.
 * ScrollView callbacks are handled by its parent fragment, not its parent activity.
 */
public class FireHubMainFragment extends BaseFragment {
    private static final String PLATE_REQUEST_URL = "http://120.24.172.105:8000/xxfintf/bbs/getForumList";
    private static final String SUBJECT_REQUEST_BASE_URL = "http://120.24.172.105:8000/xxfintf/bbs/getSubjectListByFid";
    private HubExpandListAdapter hubExpandListAdapter;
    private List<HubPlate> plateList;
    private LinkedList<List<HubSubject>> subjectList;
    private RequestQueue mQueue;
    private ExpandableListView expandableListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrollview_list_view, container, false);

        final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            scrollView.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }

        initView(view);

        return view;
    }

    protected void initView(View view) {
        //todo 从网络接口获取列表信息

        //一级列表控件
        expandableListView = (ExpandableListView) view.findViewById(R.id.hub_list_view);
        //加载时显示圆形进度条
        ProgressBar progressBar = new ProgressBar(getActivity());
        expandableListView.setEmptyView(progressBar);

        hubExpandListAdapter = new HubExpandListAdapter(getActivity());
        expandableListView.setAdapter(hubExpandListAdapter);

        //列表item点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //todo 根据关键字搜索，跳转到咨询搜索界面
                HubExpandListAdapter adapter = (HubExpandListAdapter) parent.getExpandableListAdapter();
                String txt = adapter.getChild(groupPosition, childPosition).toString();
                Intent intent = new Intent(getActivity(), CatoConsultListActivity.class);
                intent.putExtra("search_key_word", txt);
                startActivity(intent);
//                Toast.makeText(ConsultCatoMainActivity.this, txt, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getActivity());
        mQueue.add(hubPlateRequest());
        //一级列表
        plateList = new ArrayList<>();
        //二级列表
        subjectList = new LinkedList<>();
    }

    /**
     * hub板块请求
     */
    public StringRequest hubPlateRequest() {
        return new StringRequest(PLATE_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("retCode") == 0) {        //请求成功
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            HubPlate hubPlate = new HubPlate();
                            hubPlate.setFid(object.getInt("fid"));
                            hubPlate.setFup(object.getInt("fup"));
                            hubPlate.setLastpost(object.getString("lastpost"));
                            hubPlate.setName(object.getString("name"));
                            hubPlate.setType(object.getString("type"));
                            plateList.add(hubPlate);
                            subjectList.add(null);
                        }

                        for (int i = 0; i < plateList.size(); i++) {
                            mQueue.add(hubSubjectRequest(plateList.get(i).getFid(), i));
                        }
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

    /**
     * 主题请求
     */
    protected StringRequest hubSubjectRequest(final int fid, final int index) {

        return new StringRequest(SUBJECT_REQUEST_BASE_URL + "?fid=" + fid, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("retCode") == 0) {        //请求成功
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        int length = jsonArray.length();
                        List<HubSubject> hubSubjects = new ArrayList<>();
                        if (length == 0) {
                            HubSubject hubSubject = new HubSubject();
                            hubSubject.setThreadSubject("当前无主题...");
                            hubSubjects.add(hubSubject);
                        } else {
                            for (int i = 0; i < length; i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                HubSubject hubSubject = new HubSubject();
                                hubSubject.setForumFid(object.getString("forumFid"));
                                hubSubject.setForumFup(object.getString("forumFup"));
                                hubSubject.setForumName(object.getString("forumName"));
                                hubSubject.setForumType(object.getString("forumType"));
                                hubSubject.setThreadFid(object.getString("threadFid"));
                                hubSubject.setThreadSubject(object.getString("threadSubject"));
                                hubSubject.setThreadTid(object.getString("threadTid"));
                                hubSubjects.add(i, hubSubject);

                            }

                        }
                        subjectList.set(index, hubSubjects);        //将获取到的主题添加到集合
                        updateList();
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

    protected void updateList() {
        for (int i = 0; i < subjectList.size(); i++) {
            if (subjectList.get(i) == null) {
                //如果对应项是null，则new 一个无数据的项
                subjectList.set(i, new LinkedList<HubSubject>());
            }
        }
        hubExpandListAdapter.setList(plateList, subjectList);
        //默认展开所有
        for (int i = 0; i < plateList.size(); i++) {
            expandableListView.expandGroup(i);
        }

    }

}
