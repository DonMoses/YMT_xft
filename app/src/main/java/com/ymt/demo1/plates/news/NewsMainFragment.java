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

package com.ymt.demo1.plates.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.NewsSummaryAdapter;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.news.NewsSummary;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.SearchViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This fragment manages ViewPager and its child Fragments.
 * Scrolling techniques are basically the same as ViewPagerTab2Activity.
 */
public class NewsMainFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = "fragment";
    private SearchViewUtil searchViewUtil;
    /**
     * 消防新闻、消防公告、教育资讯、图片资讯
     */
    private ArrayList<NewsSummary> newsSummaries;
    private ArrayList<NewsSummary> noticeSummaries;
    private ArrayList<NewsSummary> eduNewsSummaries;
    private ArrayList<NewsSummary> picsSummaries;
    private static final int PIC_NEWS = 1;
    private static final int FIRE_NEWS = 2;
    private static final int FIRE_NOTICE = 3;
    private static final int EDU_NEWS = 4;
    private NewsSummaryAdapter fireNewsAdapter;
    private NewsSummaryAdapter fireNoticeAdapter;
    private NewsSummaryAdapter eduNewsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_parent, container, false);
        //初始化搜索界面
        searchViewUtil = new SearchViewUtil();

        initView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        newsSummaries = new ArrayList<>();
        noticeSummaries = new ArrayList<>();
        eduNewsSummaries = new ArrayList<>();
        picsSummaries = new ArrayList<>();
        mQueue.add(stringRequest("http://120.24.172.105:8000/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + "xf_article_h_news_photo" + "&order=new&start=" + String.valueOf(1), PIC_NEWS));
        mQueue.add(stringRequest("http://120.24.172.105:8000/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + "xf_article_h_news" + "&order=new&start=" + String.valueOf(1), FIRE_NEWS));
        mQueue.add(stringRequest("http://120.24.172.105:8000/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + "xf_article_h_notice" + "&order=new&start=" + String.valueOf(1), FIRE_NOTICE));
        mQueue.add(stringRequest("http://120.24.172.105:8000/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + "xf_article_h_edu" + "&order=new&start=" + String.valueOf(1), EDU_NEWS));
    }

    protected void initView(View view) {
        TextView picsGate = (TextView) view.findViewById(R.id.pics_gate);
        TextView fireNewsGate = (TextView) view.findViewById(R.id.fire_news_gate);
        TextView fireNoticeGate = (TextView) view.findViewById(R.id.fire_notice_gate);
        TextView eduNewsGate = (TextView) view.findViewById(R.id.edu_news_gate);

        ListView fireNewsListView = (ListView) view.findViewById(R.id.main_news_listview);
        ListView fireNoticeListView = (ListView) view.findViewById(R.id.main_notice_listview);
        ListView eduNewsListView = (ListView) view.findViewById(R.id.main_edu_news_listview);
        fireNewsAdapter = new NewsSummaryAdapter(getActivity());
        fireNoticeAdapter = new NewsSummaryAdapter(getActivity());
        eduNewsAdapter = new NewsSummaryAdapter(getActivity());
        fireNewsListView.setAdapter(fireNewsAdapter);
        fireNoticeListView.setAdapter(fireNoticeAdapter);
        eduNewsListView.setAdapter(eduNewsAdapter);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.pics_gate:
                        startActivity(new Intent(getActivity(), NewsPicsActivity.class));
                        break;
                    case R.id.fire_news_gate:
                        Intent intent = new Intent(getActivity(), NewsTabActivity.class);
                        intent.putExtra("tab_position", 0);
                        startActivity(intent);
                        break;
                    case R.id.fire_notice_gate:
                        Intent intent1 = new Intent(getActivity(), NewsTabActivity.class);
                        intent1.putExtra("tab_position", 1);
                        startActivity(intent1);
                        break;
                    case R.id.edu_news_gate:
                        Intent intent2 = new Intent(getActivity(), NewsTabActivity.class);
                        intent2.putExtra("tab_position", 2);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        };
        picsGate.setOnClickListener(onClickListener);
        fireNewsGate.setOnClickListener(onClickListener);
        fireNoticeGate.setOnClickListener(onClickListener);
        eduNewsGate.setOnClickListener(onClickListener);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化搜索界面
        searchViewUtil.initSearchView(getActivity());
    }

    private StringRequest stringRequest(String urlStr, final int msgWhat) {

        return new StringRequest(urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray summaryArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                    int length = summaryArray.length();
                    ArrayList<NewsSummary> summaries = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        JSONObject object = summaryArray.getJSONObject(i);
                        NewsSummary summary = new NewsSummary();
                        summary.setCreate_time(object.getString("create_time"));
                        summary.setArticle_title(object.getString("article_title"));
                        summary.setHitnum(object.optString("hitnum"));
                        summary.setId(object.getString("id"));
                        summary.setStatus(object.getString("status"));
                        summary.setPic(AppContext.SERVICE_BASE_URL + object.optString("pic"));
                        summaries.add(summary);
                        if (summaries.size() == 5) {    //只取5条数据
                            break;
                        }
                    }

                    switch (msgWhat) {
                        case PIC_NEWS:
                            picsSummaries = summaries;
                            //todo 设置图片刀view
                            break;
                        case FIRE_NEWS:
                            newsSummaries = summaries;
                            fireNewsAdapter.setList(newsSummaries);
                            break;
                        case FIRE_NOTICE:
                            noticeSummaries = summaries;
                            fireNoticeAdapter.setList(noticeSummaries);
                            break;
                        case EDU_NEWS:
                            eduNewsSummaries = summaries;
                            eduNewsAdapter.setList(eduNewsSummaries);
                            break;
                        default:
                            break;

                    }
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

    }

}
