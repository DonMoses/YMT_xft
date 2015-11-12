package com.ymt.demo1.plates.exportConsult.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.plates.exportConsult.MyConsultActivity;

/**
 * Created by Dan on 2015/5/14
 */
public class ConsultChatActivity extends BaseActivity {
    private int cId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_consult_chat);
        cId = getIntent().getIntExtra("cId", 0);

        initTitle();
        initView();

    }

    private MyTitle title;
    private String titleStr;

    protected void initTitle() {
        //todo 调用网络接口，获取聊天记录
        title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        titleStr = getIntent().getStringExtra("title");
        if (titleStr != null) {
            title.updateCenterTitle(titleStr);
        }

        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    private void initView() {
        ViewPager contentPager = (ViewPager) findViewById(R.id.content_pager);
        ChatContentAdapter contentAdapter = new ChatContentAdapter(getSupportFragmentManager());
        contentPager.setAdapter(contentAdapter);
        contentPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    if (titleStr != null) {
                        title.updateCenterTitle(titleStr);
                    }
                } else {
                    title.updateCenterTitle("会话成员");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ConsultChatActivity.this, MyConsultActivity.class));
        super.onBackPressed();
    }

    class ChatContentAdapter extends FragmentStatePagerAdapter {

        public ChatContentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = ChatContentFragment.getInstance(cId);
                    break;
                case 1:
                    fragment = ChatMemberFragment.getInstance(cId);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}