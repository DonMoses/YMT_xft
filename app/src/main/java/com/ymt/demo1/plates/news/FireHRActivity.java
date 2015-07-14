package com.ymt.demo1.plates.news;

import android.content.Intent;
import android.os.Bundle;
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
import com.ymt.demo1.adapter.NewsSummaryAdapter;
import com.ymt.demo1.beams.news.NewsSummary;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/7/14
 */
public class FireHRActivity extends BaseFloatActivity {
    private PullToRefreshListView newsListView;
    private int start;
    private RequestQueue mQueue;
    private NewsSummaryAdapter summaryAdapter;
    private ArrayList<NewsSummary> newsList;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        start = 1;
        type = getIntent().getStringExtra("type");
        setContentView(R.layout.activity_fire_hr);
        initTitle();
        initView();
        mQueue.add(summaryRequest(type, start));

    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        if (type.equals("hot")) {
            title.updateCenterTitle("热点资讯");
        } else if (type.equals("new")) {
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
                mQueue.add(summaryRequest(type, start));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start++;
                mQueue.add(summaryRequest(type, start));
            }
        });

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FireHRActivity.this, NewsDetailActivity.class);
                NewsSummary summary = (NewsSummary) (parent.getAdapter()).getItem(position);
                intent.putExtra("summary", summary);
                startActivity(intent);
            }
        });
    }

    private StringRequest summaryRequest(String type, int start) {

        return new StringRequest("http://120.24.172.105:8000/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + "xf_article_h_news" + "&order=" + type + "&start=" + String.valueOf(start), new Response.Listener<String>() {
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
                        newsList.add(summary);
                        summaryAdapter.setList(newsList);
                    }
                    // when refresh completed
                    newsListView.onRefreshComplete();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                newsListView.onRefreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(FireHRActivity.this, "网络错误，请稍后重试！", Toast.LENGTH_SHORT).show();
                newsListView.onRefreshComplete();
            }
        });

    }
}
