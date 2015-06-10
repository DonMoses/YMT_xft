package com.ymt.demo1.plates.news;

import android.os.Bundle;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;

/**
 * Created by Dan on 2015/6/8
 * 图片新闻列表界面
 */
public class NewsPicsActivity extends BaseFloatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_pics);
        initTitle();
    }

    protected void initTitle() {
        MyTitle myTitle = (MyTitle) findViewById(R.id.my_title);
        myTitle.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        myTitle.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    protected void initView() {
        PullToRefreshListView picsListView = (PullToRefreshListView) findViewById(R.id.pics_news_listview);
        //todo 适配图片新闻数据
    }
}
