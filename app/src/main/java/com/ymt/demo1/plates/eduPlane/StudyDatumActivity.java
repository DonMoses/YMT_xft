package com.ymt.demo1.plates.eduPlane;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.StudyDatumAdapter;
import com.ymt.demo1.beams.edu.StudyDatumItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.SearchViewUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

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
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //todo
            }

            @Override
            public void onRightRClick() {
                //todo

            }
        });
    }

    protected void initView() {
        PullToRefreshListView pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.studyDatum_list);
        ListView datumListView = pullToRefreshListView.getRefreshableView();
        StudyDatumAdapter studyDatumAdapter = new StudyDatumAdapter(this);
        datumListView.setAdapter(studyDatumAdapter);
        /*
        todo 学习资料数据源和适配器
         */
        ArrayList<StudyDatumItem> datumItems = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            StudyDatumItem item = new StudyDatumItem();
            item.setTitle(String.valueOf(i + 1) + " " + getString(R.string.study_datum_title_for_test));
            item.setContent(getString(R.string.study_datum_content_for_test));
            int random = new Random().nextInt(100);
            switch (random % 4) {
                case 0:
                    item.setTypeO(StudyDatumItem.TypeO.WORD);
                    break;
                case 1:
                    item.setTypeO(StudyDatumItem.TypeO.PDF);
                    break;
                case 2:
                    item.setTypeO(StudyDatumItem.TypeO.PPT);
                    break;
                case 3:
                    item.setTypeO(StudyDatumItem.TypeO.MP3);
                    break;
                default:
                    break;

            }
            item.setFileSize((float) (0.618 * random));
            item.setRequiredIntegral(random % 8);
            datumItems.add(item);
            studyDatumAdapter.setList(datumItems);
        }

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
