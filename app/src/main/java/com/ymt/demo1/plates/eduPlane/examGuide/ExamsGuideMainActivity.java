package com.ymt.demo1.plates.eduPlane.examGuide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.edu.ExamsGuideExpandListAdapter;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.main.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/4/9
 */
public class ExamsGuideMainActivity extends BaseFloatActivity {
    private ExamsGuideExpandListAdapter examsGuideExpandListAdapter;
    private List<String> parentList;
    private List<List<String>> childList;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_guide_main);
        initTitle();
        initView();

    }

    /**
     * 初始化title 和 action事件
     */
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
                //打开搜索界面
                startActivity(new Intent(ExamsGuideMainActivity.this, SearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    protected void initView() {

        //todo 从网络接口获取列表信息
        //一级列表
        parentList = new ArrayList<>();
        parentList.add("一级消防工程师");
        parentList.add("二级消防工程师");
        parentList.add("初级消防工程师");
        parentList.add("中级消防工程师");
        //二级列表
        childList = new ArrayList<>();
        List<String> list001 = new ArrayList<>();
        List<String> list002 = new ArrayList<>();
        List<String> list003 = new ArrayList<>();
        List<String> list004 = new ArrayList<>();

        childList.add(list001);
        childList.add(list002);
        childList.add(list003);
        childList.add(list004);
        //todo

        //一级列表控件
        expandableListView = (ExpandableListView) findViewById(R.id.exams_guide_expandable_list_view);
        ProgressBar progressBar = new ProgressBar(this);
        expandableListView.setEmptyView(progressBar);

        examsGuideExpandListAdapter = new ExamsGuideExpandListAdapter(this);
        expandableListView.setAdapter(examsGuideExpandListAdapter);

        //列表item点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //todo 根据关键字搜索，跳转到咨询搜索界面
              return true;
            }
        });

        updateList();

    }

    protected void updateList() {
        examsGuideExpandListAdapter.setList(parentList, childList);
        //默认选中第一栏
        expandableListView.expandGroup(0);

    }


}
