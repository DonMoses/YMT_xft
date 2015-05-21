/*
    历年真题界面。
 */
package com.ymt.demo1.plates.eduPlane;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.TestsItemAdapter;
import com.ymt.demo1.beams.XXFExam;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.SearchActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/9
 */
public class OverYearsExamsActivityCopy extends BaseFloatActivity
//        implements View.OnClickListener
{

//    private View bottomTab;
//    private View tabDiView;
//    private ListView overYearsList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edu_over_exams);
//        initTitle();
//        initView();
//
//    }
//
//    protected void initTitle() {
//        MyTitle title = (MyTitle) findViewById(R.id.my_title);
//        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
//        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
//            @Override
//            public void onClick() {
//                finish();
//            }
//        });
//
//        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
//            @Override
//            public void onRightLClick() {
//                startActivity(new Intent(OverYearsExamsActivityCopy.this, SearchActivity.class));
//            }
//
//            @Override
//            public void onRightRClick() {
//                //todo 设置按钮Action
//            }
//        });
//    }
//
//    protected void initView() {
//        /*
//        底部tab菜单及其item 、 事件
//         */
//        bottomTab = findViewById(R.id.test_bottom_tab_view);
//        tabDiView = findViewById(R.id.tab_divider_view);
//
//        View tabMenu = bottomTab.findViewById(R.id.tab_test_menu);
//        View tabCollect = bottomTab.findViewById(R.id.tab_test_collect);
//        View tabSetting = bottomTab.findViewById(R.id.tab_test_setting);
//        tabMenu.setOnClickListener(this);
//        tabCollect.setOnClickListener(this);
//        tabSetting.setOnClickListener(this);
//
//        /*
//        历年真题列表
//         */
//        overYearsList = (ListView) findViewById(R.id.tests_list_view);
//        overYearsList.setEmptyView(findViewById(R.id.empty_pro_bar));           //无数据时显示progress bar
//
//         /*
//        历年真题列表滑动监听
//            此处根据listView的滑动动态改变底部Tab 的显示和隐藏
//         */
//        overYearsList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                switch (scrollState) {
//                    case SCROLL_STATE_IDLE:                 //停止时
//                        showBottomTab();    //直接显示
//                        break;
//                    case SCROLL_STATE_FLING:                //滚动中
//                    case SCROLL_STATE_TOUCH_SCROLL:         //触摸中
//                        hideBottomTab();    //直接隐藏
//                        break;
//                    default:
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });
//
//         /*
//        历年真题列表item 点击事件
//            打开对应试题
//         */
//        overYearsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(OverYearsExamsActivityCopy.this, ((XXFExam) parent.getItemAtPosition(position)).getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        /*
//        设置数据适配器
//         */
//        TestsItemAdapter adapter = new TestsItemAdapter(this);
//        overYearsList.setAdapter(adapter);
//
//        //todo 这里加入网络获取历年真题数据
//        ArrayList<XXFExam> mData = new ArrayList<>();
//        //模拟加入历年真题数据
//        for (int i = 0; i < 20; i++) {
//            XXFExam test = new XXFExam();
//            test.setTitle("2015年一级消防工程师《消防工程项目管理》考试真题");
//            test.setCount(String.valueOf(125));
//            test.setTotalTime(String.valueOf(120));
//            test.setTotalScore(String.valueOf(150));
//            test.setWatchedCount(String.valueOf(3678));
//            test.setCollectedCount(String.valueOf(1830));
//            mData.add(test);
//            adapter.setList(mData);
//        }
//
//    }
//
//    /**
//     * 显示底部tab
//     */
//    protected void showBottomTab() {
//        bottomTab.setVisibility(View.VISIBLE);
//        tabDiView.setVisibility(View.VISIBLE);
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_tab_auto_show);
//        bottomTab.setAnimation(animation);
//        tabDiView.setAnimation(animation);
//    }
//
//    /**
//     * 滑动时隐藏底部tab
//     */
//
//    protected void hideBottomTab() {
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_tab_auto_hide);
//        bottomTab.setAnimation(animation);
//        tabDiView.setAnimation(animation);
//        bottomTab.setVisibility(View.INVISIBLE);
//        tabDiView.setVisibility(View.INVISIBLE);
//    }
//
//    /**
//     * 重写View的点击事件。
//     */
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.tab_test_menu:
//                //todo 点击选项tab
//                Toast.makeText(OverYearsExamsActivityCopy.this, "全部题目", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, ListOverYearsTestsActivity.class);
//                ArrayList<String> mTests = new ArrayList<>();   // 键tests_years
//
//                /*
//                将历年真题 年份，传送到下一个界面中显示
//                 */
//                TestsItemAdapter iAdapter = (TestsItemAdapter) overYearsList.getAdapter();
//                mTests.add("全部");
//                for (int i = 0; i < iAdapter.getCount(); i++) {
//                    XXFExam eduTestInfo = (XXFExam) iAdapter.getItem(i);
//                    String title = eduTestInfo.getTitle();
//                    /*
//                    这里只截取年份。 如 2015，1989等
//                     */
//                    String year = title.substring(0, 4);
//                    mTests.add(year);
//                }
//
//                /*
//                 传送年份到下一个界面
//                  */
//                intent.putStringArrayListExtra("tests_years", mTests);
//                startActivity(intent);
//
//                break;
//            case R.id.tab_test_collect:
//                //todo 点击收藏tab
//                Toast.makeText(OverYearsExamsActivityCopy.this, "我的收藏", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.tab_test_setting:
//                //todo 点击设置tab
//                Toast.makeText(OverYearsExamsActivityCopy.this, "练习设置", Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                break;
//        }
//    }
//
//
//
//    static class ShowTabHandler extends Handler {
//        private WeakReference<OverYearsExamsActivityCopy> overYearsTestActivityWeakReference;
//
//        public ShowTabHandler(OverYearsExamsActivityCopy overYearsTestActivity) {
//            overYearsTestActivityWeakReference = new WeakReference<>(overYearsTestActivity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            OverYearsExamsActivityCopy overYearsTestActivity = overYearsTestActivityWeakReference.get();
//            if (overYearsTestActivity != null) {
//                //todo 通过外部类的引用，操操作外部类的成员和方法
//                switch (msg.what) {
//                    case 0:
//
//                        break;
//                    default:
//                        break;
//
//                }
//            }
//        }
//    }
//
//
}
