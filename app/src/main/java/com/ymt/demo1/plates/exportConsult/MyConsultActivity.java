package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.SearchActivity;

/**
 * Created by Dan on 2015/5/12
 */
public class MyConsultActivity extends BaseFloatActivity {

    private TextView chatTxt;
    private TextView followTxt;
    private TextView bookingTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_consult);
        initTitle();
        initTab();

    }

    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                startActivity(new Intent(MyConsultActivity.this, SearchActivity.class));  //打开搜索界面
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    protected void initTab() {
        chatTxt = (TextView) findViewById(R.id.tab_chat);
        followTxt = (TextView) findViewById(R.id.tab_follow);
        bookingTxt = (TextView) findViewById(R.id.tab_booking);
        setTabSelection(0);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tab_chat:
                        //todo 切换到 “会话”
                        setTabSelection(0);
                        break;
                    case R.id.tab_follow:
                        //todo 切换到 “关注”
                        setTabSelection(1);
                        break;
                    case R.id.tab_booking:
                        //todo 切换到 “预约”
                        setTabSelection(2);
                        break;
                    default:
                        break;
                }
            }
        };
        chatTxt.setOnClickListener(onClickListener);
        followTxt.setOnClickListener(onClickListener);
        bookingTxt.setOnClickListener(onClickListener);

    }

    /**
     * 设置tab选中状态
     */
    protected void setTabSelection(int tabIndex) {
        FragmentManager fm = getSupportFragmentManager();

        switch (tabIndex) {
            case 0:
                chatTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_chat_on), null, null);
                followTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_follow_off), null, null);
                bookingTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_booking_off), null, null);
                chatTxt.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                followTxt.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                bookingTxt.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                /*
                会话fragment
                 */
                FragmentTransaction ft1 = fm.beginTransaction();
                if (fm.findFragmentByTag(ExportFollowFragment.FRAGMENT_TAG) != null) {
                    ft1.hide(fm.findFragmentByTag(ExportFollowFragment.FRAGMENT_TAG));
                }

                if (fm.findFragmentByTag(ExportChatFragment.FRAGMENT_TAG) == null) {
                    ft1.add(R.id.my_consult_content, ExportChatFragment.newInstance(""),
                            ExportChatFragment.FRAGMENT_TAG);
                    ft1.commit();
                    fm.executePendingTransactions();
                } else {
                    ft1.show(fm.findFragmentByTag(ExportChatFragment.FRAGMENT_TAG));
                    ft1.commit();
                }
                break;
            case 1:
                chatTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_chat_off), null, null);
                followTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_follow_on), null, null);
                bookingTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_booking_off), null, null);
                chatTxt.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                followTxt.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                bookingTxt.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                 /*
                关注fragment
                 */
                FragmentTransaction ft2 = fm.beginTransaction();
                if (fm.findFragmentByTag(ExportChatFragment.FRAGMENT_TAG) != null) {
                    ft2.hide(fm.findFragmentByTag(ExportChatFragment.FRAGMENT_TAG));
                }

                if (fm.findFragmentByTag(ExportFollowFragment.FRAGMENT_TAG) == null) {
                    ft2.add(R.id.my_consult_content, ExportFollowFragment.newInstance(""),
                            ExportFollowFragment.FRAGMENT_TAG);
                    ft2.commit();
                    fm.executePendingTransactions();
                } else {
                    ft2.show(fm.findFragmentByTag(ExportFollowFragment.FRAGMENT_TAG));
                    ft2.commit();
                }
                break;
            case 2:
                chatTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_chat_off), null, null);
                followTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_follow_off), null, null);
                bookingTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_booking_on), null, null);
                chatTxt.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                followTxt.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                bookingTxt.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                break;
            default:
                break;
        }
    }
}


