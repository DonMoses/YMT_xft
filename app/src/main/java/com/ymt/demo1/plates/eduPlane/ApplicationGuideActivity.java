package com.ymt.demo1.plates.eduPlane;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.AppGuideGridViewAdapter;
import com.ymt.demo1.beams.EduAppGuide;
import com.ymt.demo1.customViews.DragGridView;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/9
 */
public class ApplicationGuideActivity extends BaseFloatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_guide);
        initTitle();
        initView();

    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    protected void initView() {

        /*
        报考指南项目GridView视图
         */
        GridView guideGridView = (DragGridView) findViewById(R.id.app_guide_gridView);
        guideGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EduAppGuide clickedItem = (EduAppGuide) parent.getAdapter().getItem(position);
                String title = clickedItem.getTitle();
                Toast.makeText(ApplicationGuideActivity.this, title, Toast.LENGTH_SHORT).show();
            }
        });

        AppGuideGridViewAdapter adapter = new AppGuideGridViewAdapter(this);
        guideGridView.setAdapter(adapter);

        ArrayList<EduAppGuide> mData = new ArrayList<>();
        mData.add(addAppGuide(getResources().getColor(R.color.guide_bksj), "报名时间"));
        adapter.setList(mData);
        mData.add(addAppGuide(getResources().getColor(R.color.guide_bmtj), "报名条件"));
        adapter.setList(mData);
        mData.add(addAppGuide(getResources().getColor(R.color.guide_kssj), "考试时间"));
        adapter.setList(mData);
        mData.add(addAppGuide(getResources().getColor(R.color.guide_kskm), "考试科目"));
        adapter.setList(mData);
        mData.add(addAppGuide(getResources().getColor(R.color.guide_kssc), "考试时长"));
        adapter.setList(mData);
        mData.add(addAppGuide(getResources().getColor(R.color.guide_mkgd), "免考规定"));
        adapter.setList(mData);
        mData.add(addAppGuide(getResources().getColor(R.color.guide_cjgl), "成绩管理"));
        adapter.setList(mData);
        mData.add(addAppGuide(getResources().getColor(R.color.guide_hgzs), "证书"));
        adapter.setList(mData);
        mData.add(addAppGuide(getResources().getColor(R.color.guide_zyfw), "执业范围"));
        adapter.setList(mData);
        mData.add(addAppGuide(getResources().getColor(R.color.guide_khrd), "考核认定"));

    }

    protected EduAppGuide addAppGuide(int bgColor, String title) {
        EduAppGuide eduAppGuide = new EduAppGuide();
        eduAppGuide.setBgColor(bgColor);
        eduAppGuide.setTitle(title);
        return eduAppGuide;

    }

}
