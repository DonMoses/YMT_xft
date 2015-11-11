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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.hub.HubPlateAdapter;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.hub.HubPlate;
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
public class FireHubMainFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = "FireHubMainFragment";

    private HubPlateAdapter hubPlateAdapter;
    private List<HubPlate> plateList;
    List<HubPlate> parentList = new ArrayList<>();
    List<List<HubPlate>> childList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrollview_list_view, container, false);
        initView(view);
        return view;
    }

    protected void initView(View view) {
        //论坛版块二级列表
        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.hub_list_view);
        //加载时显示圆形进度条
        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setIndeterminate(true);
        expandableListView.setEmptyView(progressBar);

        hubPlateAdapter = new HubPlateAdapter(getActivity());
        expandableListView.setAdapter(hubPlateAdapter);

        //列表item点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //跳转到对应板块列表界面
                Intent intent = new Intent(getActivity(), SubjectsActivity.class);
                intent.putExtra("plate", childList.get(groupPosition).get(childPosition));
                intent.putExtra("group_name", parentList.get(groupPosition).getName());
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            }
        });

        ImageView doPostView = (ImageView) view.findViewById(R.id.hub_act_post);
//        ImageView topPostView = (ImageView) view.findViewById(R.id.hub_act_top);
        ImageView newPostView = (ImageView) view.findViewById(R.id.hub_act_new);
        ImageView hotPostView = (ImageView) view.findViewById(R.id.hub_act_hot);
        ImageView myPostView = (ImageView) view.findViewById(R.id.hub_act_my);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.hub_act_post:                                         //发帖
                        Intent intent1 = new Intent(getActivity(), DoPostActivity.class);
                        ArrayList<HubPlate> parents = new ArrayList<>();
                        for (HubPlate plate : parentList) {
                            parents.add(plate);
                        }
                        ArrayList<HubPlate> childs = new ArrayList<>();
                        for (List<HubPlate> pList : childList) {
                            for (HubPlate plate : pList) {
                                childs.add(plate);
                            }
                        }
                        intent1.putParcelableArrayListExtra("parent", parents);
                        intent1.putParcelableArrayListExtra("child", childs);
                        getActivity().startActivity(intent1);
                        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        break;
//                    case R.id.hub_act_top:                                          //精华帖子
//
//                        break;
                    case R.id.hub_act_new:                                          //最近帖子
                        Intent intentN = new Intent(getActivity(), HotNewPostActivity.class);
                        intentN.putExtra("type", HotNewPostActivity.TYPE_NEW);
                        startActivity(intentN);
                        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        break;
                    case R.id.hub_act_hot:                                          //热门帖子
                        Intent intentH = new Intent(getActivity(), HotNewPostActivity.class);
                        intentH.putExtra("type", HotNewPostActivity.TYPE_HOT);
                        startActivity(intentH);
                        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        break;
                    case R.id.hub_act_my:                                           //我的论坛
                        startActivity(new Intent(getActivity(), MyHubTabActivity.class));
                        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        break;
                }
            }
        };
        doPostView.setOnClickListener(onClickListener);
//        topPostView.setOnClickListener(onClickListener);
        newPostView.setOnClickListener(onClickListener);
        hotPostView.setOnClickListener(onClickListener);
        myPostView.setOnClickListener(onClickListener);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        //一级列表
        plateList = new ArrayList<>();
        if (AppContext.internetAvialable()) {
            mQueue.add(hubPlateRequest());
        }
    }


    /**
     * hub板块请求
     */
    public StringRequest hubPlateRequest() {
        return new StringRequest(BaseURLUtil.PLATE_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", ">>>>>>>>>>>>>.s  " + s);
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
                            hubPlate.setRank(object.optInt("rank"));
                            hubPlate.setThreads(object.optInt("threads"));

                            plateList.add(hubPlate);
                        }
                        update1List();

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

    protected void update1List() {
        for (HubPlate plate : plateList) {
            if (plate.getFup() == 0) {
                parentList.add(plate);
            }
        }

        for (int i = 0; i < parentList.size(); i++) {
            List<HubPlate> list = new ArrayList<>();
            for (HubPlate plate : plateList) {
                if (parentList.get(i).getFid() == plate.getFup()) {
                    list.add(plate);
                }
            }
            childList.add(list);
            hubPlateAdapter.setList(parentList, childList);
            hubPlateAdapter.onGroupExpanded(i);
        }

    }

}
