
package com.ymt.demo1.plates.eduPlane;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.PandectExamsAdapter;
import com.ymt.demo1.beams.XXFExam;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.SearchActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/9
 * 历年真题界面
 */
public class OverYearsExamsActivity extends BaseFloatActivity implements View.OnClickListener {

    private ArrayList<XXFExam> exampleExams1;
    private ArrayList<XXFExam> exampleExams2;
    private ArrayList<XXFExam> exampleExams3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_over_exams);
        initTitle();
        initView();

    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //todo 显示全部真题
            }

            @Override
            public void onRightRClick() {
                startActivity(new Intent(OverYearsExamsActivity.this, SearchActivity.class));
            }
        });
    }

    protected void initView() {
        /*
        todo 模拟从网络获取试题信息
         */
        initExamsData();
        //设置数据到滑动控件
        setData2View();

    }

    /**
     * 获取试题信息数据
     */
    protected void initExamsData() {
        //eg 1
        exampleExams1 = new ArrayList<>();
        for (int i = 2015; i > 2005; i--) {
            XXFExam exam = new XXFExam();
            exam.setTitleType(XXFExam.TITLE_TYPE_1);
            exam.setExamYear(i);
            exam.setCount(String.valueOf(100));
            exam.setIsActual(true);
            exam.setExamSubject("消防工程项目管理1");
            exam.setWatchedCount(String.valueOf(200));
            exam.setCollectedCount(String.valueOf(89));
            exam.setTotalScore(String.valueOf(150));
            exam.setTotalTime(String.valueOf(120));
            exam.setTitle(String.valueOf(exam.getExamYear()) + exam.getTitleType() +
                    "《" + exam.getExamSubject() + "》考试真题");
            exampleExams1.add(exam);
        }
        //eg 2
        exampleExams2 = new ArrayList<>();
        for (int i = 2015; i > 2005; i--) {
            XXFExam exam = new XXFExam();
            exam.setTitleType(XXFExam.TITLE_TYPE_2);
            exam.setExamYear(i);
            exam.setCount(String.valueOf(100));
            exam.setIsActual(true);
            exam.setExamSubject("消防工程项目管理2");
            exam.setWatchedCount(String.valueOf(200));
            exam.setCollectedCount(String.valueOf(89));
            exam.setTotalScore(String.valueOf(150));
            exam.setTotalTime(String.valueOf(120));
            exam.setTitle(String.valueOf(exam.getExamYear()) + exam.getTitleType() +
                    "《" + exam.getExamSubject() + "》考试真题");
            exampleExams2.add(exam);
        }
        //eg 3
        exampleExams3 = new ArrayList<>();
        for (int i = 2015; i > 2005; i--) {
            XXFExam exam = new XXFExam();
            exam.setTitleType(XXFExam.TITLE_TYPE_3);
            exam.setExamYear(i);
            exam.setCount(String.valueOf(100));
            exam.setIsActual(true);
            exam.setExamSubject("消防工程项目管理3");
            exam.setWatchedCount(String.valueOf(200));
            exam.setCollectedCount(String.valueOf(89));
            exam.setTotalScore(String.valueOf(150));
            exam.setTotalTime(String.valueOf(120));
            exam.setTitle(String.valueOf(exam.getExamYear()) + exam.getTitleType() +
                    "《" + exam.getExamSubject() + "》考试真题");
            exampleExams3.add(exam);
        }
    }

    /**
     * 设置数据到空间s
     */
    protected void setData2View() {
        //列表控件
        ListView listView = (ListView) findViewById(R.id.content_list);
        //适配器（title / content）
        PandectExamsAdapter adapter = new PandectExamsAdapter(this);
        listView.setAdapter(adapter);
        //每一项最多添加5个
        int size1 = exampleExams1.size();
        int size2 = exampleExams2.size();
        int size3 = exampleExams3.size();
        int count1 = getEachCount(size1);
        int count2 = getEachCount(size2);
        int count3 = getEachCount(size3);
        ArrayList<XXFExam> list = new ArrayList<>();
        /*
        添加类型1--添加类型1对应content；
        添加类型2--添加类型2对应content；
        添加类型3--添加类型3对应content；
         */
        list.add(null);
        for (int i = 0; i < count1; i++) {
            XXFExam exam = exampleExams1.get(i);
            list.add(exam);
            adapter.setList(list);
        }
        list.add(null);
        for (int i = 0; i < count2; i++) {
            XXFExam exam = exampleExams2.get(i);
            list.add(exam);
            adapter.setList(list);
        }
        list.add(null);
        for (int i = 0; i < count3; i++) {
            XXFExam exam = exampleExams3.get(i);
            list.add(exam);
            adapter.setList(list);
        }

        /*
        listView点选事件
        ①显示全部，跳转到对应类型的题库       ②其它，跳转到试题界面
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getAdapter().getItemViewType(position) == PandectExamsAdapter.TYPE_CONTENT) {
                    //todo 试题界面（答题）
                    XXFExam exam = (XXFExam) parent.getAdapter().getItem(position);
                    Toast.makeText(OverYearsExamsActivity.this,
                            String.valueOf(exam.getExamYear()) +
                                    exam.getTitleType() + "《" +
                                    exam.getExamSubject() + "》考试真题", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * each最多5个
     */
    protected int getEachCount(int parentSize) {
        if (parentSize > 5) {
            return 5;
        } else {
            return parentSize;
        }
    }

    @Override
    public void onClick(View v) {

    }

    static class ShowTabHandler extends Handler {
        private WeakReference<OverYearsExamsActivity> overYearsTestActivityWeakReference;

        public ShowTabHandler(OverYearsExamsActivity overYearsTestActivity) {
            overYearsTestActivityWeakReference = new WeakReference<>(overYearsTestActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            OverYearsExamsActivity overYearsTestActivity = overYearsTestActivityWeakReference.get();
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
