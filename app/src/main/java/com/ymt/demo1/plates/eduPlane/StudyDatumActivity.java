package com.ymt.demo1.plates.eduPlane;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.SearchActivity;
import com.ymt.demo1.main.SearchViewUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Dan on 2015/4/9
 * 学习资料
 */
public class StudyDatumActivity extends BaseFloatActivity {
    private SearchViewUtil searchViewUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_study_datum);
        searchViewUtil = new SearchViewUtil();
        initTitle();
        initView();

    }

    protected void initTitle() {
        searchViewUtil.initSearchView(this);
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
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
                startActivity(new Intent(StudyDatumActivity.this, SearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                //todo

            }
        });
    }

    protected void initView() {


    }

    static class ShowTabHandler extends Handler {
        private WeakReference<StudyDatumActivity> overYearsTestActivityWeakReference;

        public ShowTabHandler(StudyDatumActivity overYearsTestActivity) {
            overYearsTestActivityWeakReference = new WeakReference<>(overYearsTestActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            StudyDatumActivity overYearsTestActivity = overYearsTestActivityWeakReference.get();
            if (overYearsTestActivity != null) {
                //todo 通过外部类的引用，操操作外部类的成员和方法
                switch (msg.what) {
                    case 0:

                        break;
                    default:
                        break;

                }
            }
        }
    }


}
