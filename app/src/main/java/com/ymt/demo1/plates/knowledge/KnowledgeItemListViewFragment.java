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

package com.ymt.demo1.plates.knowledge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.KnowledgeItemAdapter;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.knowledge.KnowledgeItemBZGF;
import com.ymt.demo1.beams.knowledge.KnowledgeItemKYWX;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollView;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollViewCallbacks;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for ViewPagerTabFragmentActivity.
 * ScrollView callbacks are handled by its parent fragment, not its parent activity.
 */
public class KnowledgeItemListViewFragment extends BaseFragment {
    public static final String KNOWLEDGE_BZGF = "bzgf";    //标准规范
    public static final String KNOWLEDGE_SPZL = "spzl";    //视频资料
    public static final String KNOWLEDGE_KYWX = "kywx";    //科研文献
    private ObservableScrollView scrollView;
    PullToRefreshListView listView;
    ListView theListView;
    KnowledgeItemAdapter adapter;
    private PopActionListener actionListener;
    private String mKnowledgeType;
    private List<KnowledgeItemBZGF> knowledgeItemBZGFList;
    private List<KnowledgeItemKYWX> knowledgeItemKYWXList;
    private int pageSize;
    private int startIndex;
    private RequestQueue mQueue;

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
        return view;
    }

    public static KnowledgeItemListViewFragment getNewInstance(String knowledgeType) {
        KnowledgeItemListViewFragment fragment = new KnowledgeItemListViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("knowledge_type", knowledgeType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKnowledgeType = getArguments().getString("knowledge_type");
        pageSize = 9;
        startIndex = 1;
        switch (mKnowledgeType) {
            case KNOWLEDGE_BZGF:
                knowledgeItemBZGFList = new ArrayList<>();
                knowledgeItemBZGFList.addAll(DataSupport.findAll(KnowledgeItemBZGF.class));
                mQueue.add(getBzgfList(pageSize, startIndex, ""));
                break;
            case KNOWLEDGE_KYWX:
                knowledgeItemKYWXList = new ArrayList<>();
                knowledgeItemKYWXList.addAll(DataSupport.findAll(KnowledgeItemKYWX.class));
                mQueue.add(getKywxList(pageSize, startIndex, ""));
                break;
            case KNOWLEDGE_SPZL:

                break;
            default:

                break;

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mQueue = ((KnowledgeMainActivity) activity).mQueue;
    }

    protected void initView(View view) {

        /*
         *将下拉刷新中的listView 加入到滚动布局中
         */
        listView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_list_view);
        theListView = listView.getRefreshableView();

        /*
        设置数据源
         */
        switch (mKnowledgeType) {
            case KNOWLEDGE_BZGF:
                adapter = new KnowledgeItemAdapter(getActivity(), KNOWLEDGE_BZGF);
                listView.setAdapter(adapter);
                adapter.setBZGFList(knowledgeItemBZGFList);
                break;
            case KNOWLEDGE_KYWX:
                adapter = new KnowledgeItemAdapter(getActivity(), KNOWLEDGE_KYWX);
                listView.setAdapter(adapter);
                adapter.setKYWXList(knowledgeItemKYWXList);
                break;
            case KNOWLEDGE_SPZL:

                break;
            default:

                break;

        }

        scrollView.setListView(listView);            //测量、监听listView的变化

        /*
         *设置长按弹出菜单事件
         */
        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

            /*
            *初始化弹出菜单
            */
                String[] actionList = new String[]{"分享", "收藏"};
                PopActionUtil popActionUtil = PopActionUtil.getInstance(getActivity());
                actionListener = new PopActionListener() {
                    @Override
                    public void onAction(String action) {
                        switch (action) {
                            case "分享":
                                Toast.makeText(getActivity(), "分享", Toast.LENGTH_SHORT).show();
                                break;
                            case "收藏":
                                Toast.makeText(getActivity(), "收藏", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onDismiss() {

                    }
                };

                popActionUtil.setActionListener(actionListener);
                popActionUtil.setActions(actionList);
                final PopupWindow itemPopMenu = popActionUtil.getSimpleTxtPopActionMenu();

                itemPopMenu.showAsDropDown(view, view.getWidth() - 20, -view.getHeight());
                itemPopMenu.update();

                return true;
            }
        });

        /*
        listView 点击事件 。 跳转到详情界面
         */
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //传入内容
                Intent intent = new Intent(getActivity(), KnowledgeItemDetailActivity.class);
                switch (mKnowledgeType) {
                    case KNOWLEDGE_BZGF:
                        intent.putExtra("title", ((KnowledgeItemBZGF) parent.getAdapter().getItem(position)).getArticle_title());
                        intent.putExtra("content", ((KnowledgeItemBZGF) parent.getAdapter().getItem(position)).getContent());
                        break;
                    case KNOWLEDGE_KYWX:
                        intent.putExtra("title", ((KnowledgeItemKYWX) parent.getAdapter().getItem(position)).getArticle_title());
                        intent.putExtra("content", ((KnowledgeItemKYWX) parent.getAdapter().getItem(position)).getContent());
                        break;
                    case KNOWLEDGE_SPZL:

                        break;
                    default:

                        break;

                }
                startActivity(intent);
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                startIndex = 1;
                switch (mKnowledgeType) {
                    case KNOWLEDGE_BZGF:
                        DataSupport.deleteAll(KnowledgeItemBZGF.class);
                        knowledgeItemBZGFList.clear();
                        adapter.setBZGFList(knowledgeItemBZGFList);
                        mQueue.add(getBzgfList(pageSize, startIndex, ""));
                        break;
                    case KNOWLEDGE_KYWX:
                        DataSupport.deleteAll(KnowledgeItemKYWX.class);
                        knowledgeItemKYWXList.clear();
                        adapter.setKYWXList(knowledgeItemKYWXList);
                        mQueue.add(getKywxList(pageSize, startIndex, ""));
                        break;
                    case KNOWLEDGE_SPZL:

                        break;
                    default:

                        break;

                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                startIndex++;
                switch (mKnowledgeType) {
                    case KNOWLEDGE_BZGF:
                        mQueue.add(getBzgfList(pageSize, startIndex, ""));
                        break;
                    case KNOWLEDGE_KYWX:
                        mQueue.add(getKywxList(pageSize, startIndex, ""));
                        break;
                    case KNOWLEDGE_SPZL:

                        break;
                    default:

                        break;

                }
            }
        });
        listView.onRefreshComplete();


    }

    protected StringRequest getBzgfList(int pageSize, int startIndex, String searchWhat) {
//        Log.e("TAG", ">>>>>>>>>>>>>url>>>>>>>>>>" + BaseURLUtil.doGetKnowledgeAction(KNOWLEDGE_BZGF, pageSize, startIndex, searchWhat));
        return new StringRequest(BaseURLUtil.doGetKnowledgeAction(KNOWLEDGE_BZGF, pageSize, startIndex, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject object = new JSONObject(s);
//                    Log.e("TAG", ">>>>>>>>>>>>>s>>>>>>>>>>" + s);

                    if (object.getString("result").equals("Y")) {
                        JSONObject jsonObject = object.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            KnowledgeItemBZGF knowledgeItemBZGF = new KnowledgeItemBZGF();
                            knowledgeItemBZGF.setHitnum(obj.optString("hitnum"));
                            knowledgeItemBZGF.setCreate_time(obj.optString("create_time"));
                            knowledgeItemBZGF.setArticle_title(obj.optString("article_title"));
                            knowledgeItemBZGF.setContent(obj.optString("content"));
                            knowledgeItemBZGF.setDowncount(obj.optString("downcount"));
                            knowledgeItemBZGF.setFiles(obj.optString("files"));
                            knowledgeItemBZGF.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            knowledgeItemBZGF.setJzxf(obj.optString("jzxf"));
                            knowledgeItemBZGF.setMeta_keys(obj.optString("meta_keys"));
                            knowledgeItemBZGF.setStatus(obj.optString("status"));
                            String id = obj.optString("id");
                            knowledgeItemBZGF.setThe_id(id);
                            knowledgeItemBZGF.setScore(obj.optString("score"));

                            /*
                             * 判断数据库是否已经包含该item
                             */
                            int size = DataSupport.where("the_id = ?", id).find(KnowledgeItemBZGF.class).size();
                            int len = DataSupport.count(KnowledgeItemBZGF.class);
                            if (size == 0) {
                                if (len > 50) {
                                    DataSupport.delete(KnowledgeItemBZGF.class, 0);
                                }
                                knowledgeItemBZGF.save();
                            } else {
                                knowledgeItemBZGF.updateAll("the_id = ?", id);
                            }
                        }
                        knowledgeItemBZGFList = DataSupport.findAll(KnowledgeItemBZGF.class);
                        adapter.setBZGFList(knowledgeItemBZGFList);
                        listView.onRefreshComplete();

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

    protected StringRequest getKywxList(int pageSize, int startIndex, String searchWhat) {
        return new StringRequest(BaseURLUtil.doGetKnowledgeAction(KNOWLEDGE_KYWX, pageSize, startIndex, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
//                    Log.e("TAG", ">>>>>>>>>>>>>s>>>>>>>>>>" + s);

                    if (object.getString("result").equals("Y")) {
                        JSONObject jsonObject = object.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            KnowledgeItemKYWX knowledgeItemKYWX = new KnowledgeItemKYWX();
                            knowledgeItemKYWX.setHitnum(obj.optString("hitnum"));
                            knowledgeItemKYWX.setCreate_time(obj.optString("create_time"));
                            knowledgeItemKYWX.setArticle_title(obj.optString("article_title"));
                            knowledgeItemKYWX.setContent(obj.optString("content"));
                            knowledgeItemKYWX.setDowncount(obj.optString("downcount"));
                            knowledgeItemKYWX.setFiles(obj.optString("files"));
                            knowledgeItemKYWX.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            knowledgeItemKYWX.setJzxf(obj.optString("jzxf"));
                            knowledgeItemKYWX.setMeta_keys(obj.optString("meta_keys"));
                            knowledgeItemKYWX.setStatus(obj.optString("status"));
                            knowledgeItemKYWX.setAuthor(obj.optString("author"));
                            knowledgeItemKYWX.setPdf_id(obj.optString("pdf_id"));
                            knowledgeItemKYWX.setIsFile(obj.optString("isfile"));
                            knowledgeItemKYWX.setAttribute(obj.optString("attribute"));

                            String id = obj.optString("id");
                            knowledgeItemKYWX.setThe_id(id);
                            knowledgeItemKYWX.setScore(obj.optString("score"));

                            /*
                             * 判断数据库是否已经包含该item
                             */
                            int size = DataSupport.where("the_id = ?", id).find(KnowledgeItemKYWX.class).size();
                            int len = DataSupport.count(KnowledgeItemKYWX.class);
                            if (size == 0) {
                                if (len > 50) {
                                    DataSupport.delete(KnowledgeItemKYWX.class, 0);
                                }
                                knowledgeItemKYWX.save();
                            } else {
                                knowledgeItemKYWX.updateAll("the_id = ?", id);
                            }
                        }
                        knowledgeItemKYWXList = DataSupport.findAll(KnowledgeItemKYWX.class);
                        adapter.setKYWXList(knowledgeItemKYWXList);
                        listView.onRefreshComplete();

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

    protected StringRequest getSpzlList() {
        return new StringRequest("", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

    }
}
