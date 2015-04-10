package com.ymt.demo1.plates.eduPlane;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.AppGuide;
import com.ymt.demo1.customViews.AppGuideButton;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/9
 */
public class ApplicationGuideActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_guide);
        initView();
    }

    protected void initView() {

        /*
        报考指南项目GridView视图
         */
        GridView guideGridView = (GridView) findViewById(R.id.app_guide_gridView);
        guideGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppGuide clickedItem = (AppGuide) parent.getAdapter().getItem(position);
                String title = clickedItem.getTitle();
                Toast.makeText(ApplicationGuideActivity.this, title, Toast.LENGTH_SHORT).show();
            }
        });

        AppGuideBtnAdapter adapter = new AppGuideBtnAdapter(this);
        guideGridView.setAdapter(adapter);

        ArrayList<AppGuide> mData = new ArrayList<>();
        mData.add(addAppGuide(Color.RED, "报名时间"));
        adapter.setList(mData);
        mData.add(addAppGuide(Color.BLUE, "报名条件"));
        adapter.setList(mData);
        mData.add(addAppGuide(Color.RED, "考试时间"));
        adapter.setList(mData);
        mData.add(addAppGuide(Color.GREEN, "考试科目"));
        adapter.setList(mData);
        mData.add(addAppGuide(Color.RED, "考试时长"));
        adapter.setList(mData);
        mData.add(addAppGuide(Color.GRAY, "免考规定"));
        adapter.setList(mData);
        mData.add(addAppGuide(Color.BLUE, "成绩管理"));
        adapter.setList(mData);
        mData.add(addAppGuide(Color.BLUE, "证书"));
        adapter.setList(mData);
        mData.add(addAppGuide(Color.GREEN, "执业范围"));
        adapter.setList(mData);
        mData.add(addAppGuide(Color.BLUE, "考核认定"));

        /*
        title 返回按钮
         */
        View backView = findViewById(R.id.merge_title_layout);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected AppGuide addAppGuide(int bgColor, String title) {
        AppGuide appGuide = new AppGuide();
        appGuide.setBgColor(bgColor);
        appGuide.setTitle(title);
        return appGuide;

    }

    class AppGuideBtnAdapter extends BaseAdapter {
        ArrayList<AppGuide> mList = new ArrayList<>();
        Context context;
        LayoutInflater inflater;

        public AppGuideBtnAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        public void setList(ArrayList<AppGuide> mList) {
            this.mList = mList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppGuideButton guideButton;
            if (convertView == null) {
                convertView = new AppGuideButton(ApplicationGuideActivity.this);
                guideButton = (AppGuideButton) convertView;
                convertView.setTag(guideButton);
            } else {
                guideButton = (AppGuideButton) convertView.getTag();
            }
            guideButton.setColor(((AppGuide) getItem(position)).getBgColor());
            guideButton.setText(((AppGuide) getItem(position)).getTitle());
            return convertView;
        }
    }
}
