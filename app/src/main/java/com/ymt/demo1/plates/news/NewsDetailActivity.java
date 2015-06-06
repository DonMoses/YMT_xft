package com.ymt.demo1.plates.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SimpleTxtItemAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dan on 2015/4/29
 */
public class NewsDetailActivity extends BaseActivity {
    private WebView contentView;
    private String title;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String id = getIntent().getStringExtra("news_id");
        title = getIntent().getStringExtra("news_title");
        String fullTime = getIntent().getStringExtra("news_time");
        time = (String) fullTime.subSequence(0, fullTime.length() - 2);
        setContentView(R.layout.activity_news_detail);
        initTitle();
        initView();
        String baseContentUrl = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.ArticleAction&m=show&id=";
        mQueue.add(newsContentRequest(baseContentUrl + id));
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);

        title.updateCenterTitle(getIntent().getStringExtra("title"));     //设置title
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //弹出分享界面
                //todo 分享内容
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "消防咨询-" + getIntent().getStringExtra("title"));
                intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("content"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    protected void initView() {
        //热门话题GridView
        final GridView hotCommentGrid = (GridView) findViewById(R.id.hot_comment_grid_view);
        final TextView newsTitle = (TextView) findViewById(R.id.news_title);
        final TextView newsTime = (TextView) findViewById(R.id.news_time);
        newsTitle.setText(title);
        newsTime.setText(time);

        /*
        内容textView
         */
        contentView = (WebView) findViewById(R.id.content);
        //设置底部热点话题内容
        setDataToHotGird(hotCommentGrid);

        /*
        点击 “写点评”
         */
        View view = findViewById(R.id.write_comment_layout);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 写点评
                Toast.makeText(NewsDetailActivity.this, "写点评...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 为底部热点话题设置数据
     */
    protected void setDataToHotGird(GridView gridView) {
        ArrayList<String> list = new ArrayList<>();
        String[] hotArray = new String[]{"消防部门", "规范组", "建委",
                "科研机构", "设计院", "开发商", "设备商", "服务商"};
        Collections.addAll(list, hotArray);
        SimpleTxtItemAdapter adapter = new SimpleTxtItemAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setColor(Color.WHITE, getResources().getColor(R.color.bg_view_blue));
        adapter.setList(list);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getAdapter().getItem(position).toString();
                //todo
                Toast.makeText(NewsDetailActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private StringRequest newsContentRequest(String urlStr) {
        return new StringRequest(urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String result = jsonObject.getString("result");
                    if (result.equals("Y")) {
                        //TODO 显示内容
                        contentView.loadDataWithBaseURL(null, jsonObject.getString("centent"), "text/html", "utf-8", null);
                    } else {
                        Toast.makeText(NewsDetailActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(NewsDetailActivity.this, "网络错误，请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
