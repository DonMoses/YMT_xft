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

package com.ymt.demo1.plates.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.ymt.demo1.adapter.NewsSummaryAdapter;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.news.NewsSummary;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollView;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollViewCallbacks;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Fragment for ViewPagerTabFragmentActivity.
 * ScrollView callbacks are handled by its parent fragment, not its parent activity.
 */
public class NewsFragment extends BaseFragment {
    private RequestQueue mQueue;
    private ObservableScrollView scrollView;
    private PullToRefreshListView listView;
    private ArrayList<NewsSummary> testArray;
    private NewsSummaryAdapter summaryAdapter;
    private PopActionListener actionListener;
    private ArrayList<NewsSummary> mNews = new ArrayList<>();       //获取新闻到集合
    private String news_type_id;
    private int startPosition;

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

    protected void initView(View view) {

        /*
         *将下拉刷新中的listView 加入到滚动布局中
         */
        listView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_list_view);
        ListView theListView = listView.getRefreshableView();
        testArray = new ArrayList<>();
        ProgressBar progressBar = new ProgressBar(getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBar.setLayoutParams(params);
        listView.setEmptyView(progressBar);

        /*
        设置数据源
         */
        summaryAdapter = new NewsSummaryAdapter(getActivity());
        listView.setAdapter(summaryAdapter);
        summaryAdapter.setList(testArray);
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
                //todo bug
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                NewsSummary summary = (NewsSummary) (parent.getAdapter()).getItem(position);
                intent.putExtra("summary", summary);
                startActivity(intent);
            }
        });

        /*
         * 上拉加载更多，下拉刷新更多
         */
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mNews.clear();
                startPosition = 1;
                mQueue.add(summaryRequest(BaseURLUtil.BASE_URL+"/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + news_type_id + "&order=new&start=" + String.valueOf(startPosition)));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                startPosition++;
                mQueue.add(summaryRequest(BaseURLUtil.BASE_URL+"/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + news_type_id + "&order=new&start=" + String.valueOf(startPosition)));
            }
        });

    }

    private StringRequest summaryRequest(String urlStr) {

        return new StringRequest(urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray summaryArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                    int length = summaryArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject object = summaryArray.getJSONObject(i);
                        NewsSummary summary = new NewsSummary();
                        summary.setContent(object.optString("content"));
                        summary.setCreate_time(object.optString("create_time"));
                        summary.setArticle_title(object.optString("article_title"));
                        summary.setHitnum(object.optString("hitnum"));
                        summary.setThe_id(object.optString("id"));
                        summary.setFk_create_user_id(object.optString("fk_create_user_id"));
                        summary.setSource(object.optString("source"));
                        summary.setEditor(object.optString("editor"));
                        summary.setAuthor(object.optString("author"));
                        summary.setStatus(object.optString("status"));
                        mNews.add(summary);
                    }
                    summaryAdapter.setList(mNews);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listView.onRefreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "网络错误，请稍后重试！", Toast.LENGTH_SHORT).show();
                listView.onRefreshComplete();
            }
        });

    }

    /**
     * get instance
     */
    public static NewsFragment newInstance(String news_type_id_in) {
        NewsFragment newsContentFragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString("news_type_id", news_type_id_in);
        newsContentFragment.setArguments(args);

        return newsContentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        news_type_id = bundle.getString("news_type_id");
        mQueue = Volley.newRequestQueue(getActivity());
        startPosition = 1;
        mQueue.add(summaryRequest(BaseURLUtil.BASE_URL+"/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + news_type_id + "&order=new&start=" + String.valueOf(startPosition)));

    }

}
