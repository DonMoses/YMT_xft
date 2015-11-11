package com.ymt.demo1.plates.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.news.NewsSummary;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by Dan on 2015/4/29
 */
public class NewsDetailActivity extends BaseActivity {
    private String title;
    private String time;
    private NewsSummary summary;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        summary = getIntent().getParcelableExtra("summary");
        title = summary.getArticleTitle();
        time = summary.getCreateTime();
        setContentView(R.layout.activity_news_detail);
        initTitle();
        initView();
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);

        type = getIntent().getStringExtra("type");
        if (type != null) {
            switch (type) {
                case "hitnum":
                    title.updateCenterTitle("热点资讯");
                    break;
                case "time":
                    title.updateCenterTitle("最新资讯");
                    break;
                case "notice":
                    title.updateCenterTitle("消防公告");
                    break;
            }
        }

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
                intent.putExtra(Intent.EXTRA_SUBJECT, "消防资讯-" + getIntent().getStringExtra("title"));
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
//        final GridView hotCommentGrid = (GridView) findViewById(R.id.hot_comment_grid_view);
        final TextView newsTitle = (TextView) findViewById(R.id.news_title);
        final TextView newsTime = (TextView) findViewById(R.id.news_time);
        newsTitle.setText(title);
        String author = summary.getAuthor();
        String editor = summary.getEditor();
        String source = summary.getSource();
        if (!TextUtils.isEmpty(editor)) {
            newsTime.setText(source + "-" + author + "  编辑-" + editor + "  " + time);
        } else {
            newsTime.setText(source + "-" + author + "  " + time);
        }

        /*
        内容textView
         */
        WebView contentView = (WebView) findViewById(R.id.content);
        contentView.loadDataWithBaseURL(null, summary.getContent(), "text/html", "UTF-8", null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (type == null) {
            return;
        }
        switch (type) {
            case "hitnum":
            case "time":
                Intent intentR = new Intent(this, FireHRActivity.class);
                intentR.putExtra("type", type);
                startActivity(intentR);
                this.finish();
                break;
        }
    }
}
