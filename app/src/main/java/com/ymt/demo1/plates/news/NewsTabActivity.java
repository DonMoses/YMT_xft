package com.ymt.demo1.plates.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.customViews.obsScrollview.CacheFragmentStatePagerAdapter;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.plates.knowledge.KnowledgePagerTabParentFragment;

/**
 * Created by Dan on 2015/6/8
 * 新闻tab列表界面
 */
public class NewsTabActivity extends BaseFloatActivity {
    public int tabPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_news);
        tabPosition = getIntent().getIntExtra("tab_position", 0);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(PagerTabParentFragment.FRAGMENT_TAG) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment, PagerTabParentFragment.newInstanceWithPosition(tabPosition),
                    PagerTabParentFragment.FRAGMENT_TAG);
            ft.commit();
            fm.executePendingTransactions();
        }
        initTitle();
    }

    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
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
