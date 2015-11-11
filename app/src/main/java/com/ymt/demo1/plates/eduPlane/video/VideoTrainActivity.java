package com.ymt.demo1.plates.eduPlane.video;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by DonMoses on 2015/9/6
 */
public class VideoTrainActivity extends BaseFloatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_wrong_train);
        manager = getSupportFragmentManager();
        initTitle();
        initView();

    }

    private FragmentManager manager;
    private MyTitle title;

    protected void initTitle() {
        title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                Fragment curFragment = manager.findFragmentById(R.id.content);
                if (curFragment instanceof VideoTrainContentFragment) {
                    initView();
                } else if (curFragment instanceof VideoTrainTypeListFragment) {
                    finish();
                }
            }
        });

    }

    private void initView() {
        title.updateCenterTitle("培训视频");
        FragmentTransaction ft = manager.beginTransaction();
        if (manager.findFragmentById(R.id.content) != null) {
            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
            ft.replace(R.id.content, VideoTrainTypeListFragment.getInstance());
        } else {
            ft.add(R.id.content, VideoTrainTypeListFragment.getInstance());
        }
        ft.commit();
    }

    public void setTitleType(String type) {
        String tStr = null;
        switch (type) {
            case "1":
                tStr = "一级注册消防工程师-培训视频";
                break;
            case "2":
                tStr = "二级注册消防工程师-培训视频";
                break;
            case "3":
                tStr = "初级建(构)筑物消防员-培训视频";
                break;
            case "4":
                tStr = "中级建(构)筑物消防员-培训视频";
                break;
            default:
                break;
        }
        title.updateCenterTitle(tStr);
    }

    @Override
    public void onBackPressed() {
        Fragment curFragment = manager.findFragmentById(R.id.content);
        if (curFragment instanceof VideoTrainContentFragment) {
            initView();
        } else if (curFragment instanceof VideoTrainTypeListFragment) {
            super.onBackPressed();
        }
    }
}
