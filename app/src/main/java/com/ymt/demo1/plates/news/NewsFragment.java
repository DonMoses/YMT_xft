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

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.NewsSummaryAdapter;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.NewsItem;
import com.ymt.demo1.beams.NewsSummary;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollView;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollViewCallbacks;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;


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
    private String news_type_id = "xf_article_h_news";  //新闻类型标识, 此处表示普通新闻
    private final Handler mHandler = new MyHandler(this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrollview_ultra_list_view, container, false);
        mQueue = Volley.newRequestQueue(getActivity());
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
//        //todo data
//        for (int i = 0; i < 20; i++) {
//            NewsSummary item = new NewsSummary();
//            item.setArticle_title(i + 1 + "深圳市出台《消防产品监督管理工作考核办法》");
//            item.setCreate_time("2015.05.23 11:23:08");
//            testArray.add(item);
//        }

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
                String[] actionList = new String[]{"分享", "收藏", "删除"};
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
                            case "删除":
                                Toast.makeText(getActivity(), "删除", Toast.LENGTH_SHORT).show();
//                              //属性动画
                                ObjectAnimator animator = ObjectAnimator
                                        .ofFloat(view, "rotationX", 0.0F, 360.0F)//
                                        .setDuration(1000);
                                animator.start();
                                animator.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        testArray.remove(position - 1);     //因为此listView包含header，所以 -1
                                        summaryAdapter.setList(testArray);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
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
                Intent intent = new Intent(getActivity(), NewsDownloadDetailActivity.class);
                intent.putExtra("title", ((NewsItem) parent.getAdapter().getItem(position)).getTitle());
                intent.putExtra("content", ((NewsItem) parent.getAdapter().getItem(position)).getContentTxt());
                startActivity(intent);
            }
        });

    }

    private static class MyHandler extends Handler {
        private final WeakReference<NewsFragment> mNewsContentReference;

        public MyHandler(NewsFragment newsContentFragment) {
            this.mNewsContentReference = new WeakReference<>(newsContentFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            NewsFragment newsContentFragment = mNewsContentReference.get();
            if (newsContentFragment != null) {
                switch (msg.what) {
                    case 1:

                        break;
                    default:
                        break;
                }
            }
        }
    }

    private StringRequest summaryRequest(String urlStr) {
        StringRequest stringRequest = new StringRequest(urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("TAG", "content info>>>>>>>>>>>>>>" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray summaryArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                    int length = summaryArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject object = summaryArray.getJSONObject(i);
                        NewsSummary summary = new NewsSummary();
                        summary.setCreate_time(object.getString("create_time"));
                        summary.setArticle_title(object.getString("article_title"));
                        summary.setHitnum(object.getString("hitnum"));
                        summary.setId(object.getString("id"));
                        summary.setStatus(object.getString("status"));
                        mNews.add(summary);
                        summaryAdapter.setList(mNews);
                    }
                    // when refresh completed
                    listView.onRefreshComplete();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "网络错误，请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        });

        return stringRequest;

    }

    private static final String loginUrl = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.LoginAction&loginname=admin&pwd=admin&t=app";

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

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mQueue.add(new StringRequest(loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("TAG", "login info>>>>>>>>>>>>>>" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String sId = jsonObject.getString("sId");
                    mQueue.add(summaryRequest("http://120.24.172.105:8000/fw?controller=com.xfsm.action.ArticleAction&" + sId + "&m=list&type=" + news_type_id + "&order=new&start=1"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }));
    }
}
