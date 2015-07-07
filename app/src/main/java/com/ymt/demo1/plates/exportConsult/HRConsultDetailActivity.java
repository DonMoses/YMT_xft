package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.expert_consult.HotConsult;
import com.ymt.demo1.beams.expert_consult.RecentConsult;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by Dan on 2015/7/5
 */
public class HRConsultDetailActivity extends BaseActivity {
    private String type;
    private HotConsult hotConsult;
    private RecentConsult recConsult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_rec_consult_detail);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        switch (type) {
            case "hot":
                hotConsult = intent.getParcelableExtra("consult");
                break;
            case "rec":
                recConsult = intent.getParcelableExtra("consult");
                break;
            default:
                break;
        }

        initTitle();
        initView();

    }

    protected void initTitle() {
        MyTitle myTitle = (MyTitle) findViewById(R.id.my_title);
        myTitle.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        myTitle.updateCenterTitle("");
    }

    protected void initView() {
        TextView title = (TextView) findViewById(R.id.consult_title);
        TextView time = (TextView) findViewById(R.id.consult_time);
        WebView content = (WebView) findViewById(R.id.consult_content);
        switch (type) {
            case "hot":
                title.setText(hotConsult.getArticle_title());
                time.setText(hotConsult.getCreate_time());
                content.loadDataWithBaseURL(null, hotConsult.getContent(), "text/html", "utf-8", null);
                break;
            case "rec":
                title.setText(recConsult.getArticle_title());
                time.setText(recConsult.getCreate_time());
                content.loadDataWithBaseURL(null, recConsult.getContent(), "text/html", "utf-8", null);
                break;
            default:
                break;
        }
    }
}
