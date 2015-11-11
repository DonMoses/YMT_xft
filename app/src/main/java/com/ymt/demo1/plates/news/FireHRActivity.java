package com.ymt.demo1.plates.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.news.NewsSummaryAdapter;
import com.ymt.demo1.beams.knowledge.KnowledgeItem;
import com.ymt.demo1.beams.news.NewsSummary;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.plates.knowledge.KnowledgeItemListViewFragment;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/7/14
 * 热点新闻
 */
public class FireHRActivity extends BaseFloatActivity {
    private PullToRefreshListView newsListView;
    private RequestQueue mQueue;
    private NewsSummaryAdapter summaryAdapter;
    private ArrayList<NewsSummary> newsList;
    private String state = "001";
    private int start;
    private int pagesize;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        start = 1;
        pagesize = 15;
        type = getIntent().getStringExtra("type");
        setContentView(R.layout.activity_fire_hr);
        initTitle();
        initView();
        mQueue.add(summaryRequest(state, start, pagesize, type));

    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        if (type.equals("hitnum")) {
            title.updateCenterTitle("热点资讯");
        } else if (type.equals("time")) {
            title.updateCenterTitle("最新资讯");
        }
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    protected void initView() {
        newsListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_list_view);
        summaryAdapter = new NewsSummaryAdapter(this);
        newsList = new ArrayList<>();
        newsListView.setAdapter(summaryAdapter);
        newsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = 1;
                newsList.clear();
                summaryAdapter.setList(newsList);
                mQueue.add(summaryRequest(state, start, pagesize, type));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start++;
                mQueue.add(summaryRequest(state, start, pagesize, type));
            }
        });

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FireHRActivity.this, NewsDetailActivity.class);
                NewsSummary summary = (NewsSummary) (parent.getAdapter()).getItem(position);
                intent.putExtra("summary", summary);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
    }

    private StringRequest summaryRequest(String state, int start, int pagezie, String type) {

        return new StringRequest(BaseURLUtil.getNews(state, start, pagezie, type), new Response.Listener<String>() {
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
                        summary.setCreateTime(object.optString("createTime"));
                        summary.setArticleTitle(object.optString("articleTitle"));
                        summary.setHitnum(object.optString("hitnum"));
                        summary.setThe_id(object.optString("id"));
                        summary.setSource(object.optString("source"));
                        summary.setEditor(object.optString("editor"));
                        summary.setAuthor(object.optString("author"));
                        summary.setName1(object.optString("name1"));
                        summary.setName2(object.optString("name2"));
                        newsList.add(summary);
                        summaryAdapter.setList(newsList);
                    }
                    // when refresh completed
                    newsListView.onRefreshComplete();
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
                newsListView.onRefreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                newsListView.onRefreshComplete();
            }
        });

    }

}
