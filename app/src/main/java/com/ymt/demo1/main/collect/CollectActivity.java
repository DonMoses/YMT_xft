package com.ymt.demo1.main.collect;

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
public class CollectActivity extends BaseFloatActivity {

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
                if (curFragment instanceof CollectContentFragment) {
                    initView();
                } else if (curFragment instanceof CollectListFragment) {
                    finish();
                }
            }
        });

    }

    private void initView() {
        title.updateCenterTitle("我的收藏");
        FragmentTransaction ft = manager.beginTransaction();
        if (manager.findFragmentById(R.id.content) != null) {
            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
            ft.replace(R.id.content, CollectListFragment.getInstance());
        } else {
            ft.add(R.id.content, CollectListFragment.getInstance());
        }
        ft.commit();
    }

    public void setTitleType(String type) {
        String tStr = null;
        switch (type) {
            case "1":
                tStr = "咨询收藏";
                break;
            case "2":
                tStr = "知识收藏";
                break;
            case "3":
                tStr = "教育收藏";
                break;
//            case "4":
//                tStr = "商品收藏";
//                break;
            default:
                break;
        }
        title.updateCenterTitle(tStr);
    }

    @Override
    public void onBackPressed() {
        Fragment curFragment = manager.findFragmentById(R.id.content);
        if (curFragment instanceof CollectContentFragment) {
            initView();
        } else if (curFragment instanceof CollectListFragment) {
            super.onBackPressed();
        }
    }
}
