/*
    历年真题界面。
 */
package com.ymt.demo1.plates.eduPlane;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.TestInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/9
 */
public class OverYearsTestActivity extends Activity implements View.OnClickListener {
    final int SIMPLE_TYPE = 0;
    private View bottomTab;
    private View tabDiView;
    private ListView overYearsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_years_test);
        initView();
    }

    protected void initView() {
        /*
        底部tab菜单及其item 、 事件
         */
        bottomTab = findViewById(R.id.test_bottom_tab_view);
        tabDiView = findViewById(R.id.tab_divider_view);

        View tabMenu = bottomTab.findViewById(R.id.tab_test_menu);
        View tabCollect = bottomTab.findViewById(R.id.tab_test_collect);
        View tabSetting = bottomTab.findViewById(R.id.tab_test_setting);
        tabMenu.setOnClickListener(this);
        tabCollect.setOnClickListener(this);
        tabSetting.setOnClickListener(this);

        /*
        title 上back 按钮和搜索按钮的点击事件
         */
        Button backBtn = (Button) findViewById(R.id.merge_title_back);
        ImageButton searchBtn = (ImageButton) findViewById(R.id.merge_search_btn);
        backBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);

        /*
        历年真题列表
         */
        overYearsList = (ListView) findViewById(R.id.tests_list_view);
        overYearsList.setEmptyView(findViewById(R.id.empty_pro_bar));           //无数据时显示progress bar

         /*
        历年真题列表滑动监听
            此处根据listView的滑动动态改变底部Tab 的显示和隐藏
         */
        overYearsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:                 //停止时
                        showBottomTab();    //直接显示
                        break;
                    case SCROLL_STATE_FLING:                //滚动中
                    case SCROLL_STATE_TOUCH_SCROLL:         //触摸中
                        hideBottomTab();    //直接隐藏
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

         /*
        历年真题列表item 点击事件
            打开对应试题
         */
        overYearsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(OverYearsTestActivity.this, ((TestInfo) parent.getItemAtPosition(position)).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        /*
        设置数据适配器
         */
        TestsItemAdapter adapter = new TestsItemAdapter(this);
        overYearsList.setAdapter(adapter);

        //todo 这里加入网络获取历年真题数据
        ArrayList<TestInfo> mData = new ArrayList<>();
        //模拟加入历年真题数据
        for (int i = 0; i < 20; i++) {
            TestInfo test = new TestInfo();
            test.setTitle("2015年一级消防工程师《消防工程项目管理》考试真题");
            test.setCount(String.valueOf(125));
            test.setTotalTime(String.valueOf(120));
            test.setTotalScore(String.valueOf(150));
            test.setWatchedCount(String.valueOf(3678));
            test.setCollectedCount(String.valueOf(1830));
            mData.add(test);
            adapter.setList(mData);
        }

    }

    /**
     * 显示底部tab
     */
    protected void showBottomTab() {
        bottomTab.setVisibility(View.VISIBLE);
        tabDiView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_tab_auto_show);
        bottomTab.setAnimation(animation);
        tabDiView.setAnimation(animation);
    }

    /**
     * 滑动时隐藏底部tab
     */

    protected void hideBottomTab() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_tab_auto_hide);
        bottomTab.setAnimation(animation);
        tabDiView.setAnimation(animation);
        bottomTab.setVisibility(View.INVISIBLE);
        tabDiView.setVisibility(View.INVISIBLE);
    }

    /**
     * 重写View的点击事件。
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.merge_title_back:

                finish();           //退出
                break;
            case R.id.merge_search_btn:         //搜索

                break;
            case R.id.tab_test_menu:
                //todo 点击选项tab
                Toast.makeText(OverYearsTestActivity.this, "全部题目", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ListOverYearsTestsActivity.class);
                ArrayList<String> mTests = new ArrayList<>();   // 键tests_years

                /*
                将历年真题 年份，传送到下一个界面中显示
                 */
                TestsItemAdapter iAdapter = (TestsItemAdapter) overYearsList.getAdapter();
                mTests.add("全部");
                for (int i = 0; i < iAdapter.getCount(); i++) {
                    TestInfo testInfo = (TestInfo) iAdapter.getItem(i);
                    String title = testInfo.getTitle();
                    /*
                    这里只截取年份。 如 2015，1989等
                     */
                    String year = title.substring(0, 4);
                    mTests.add(year);
                }

                /*
                 传送年份到下一个界面
                  */
                intent.putStringArrayListExtra("tests_years", mTests);
                startActivity(intent);

                break;
            case R.id.tab_test_collect:
                //todo 点击收藏tab
                Toast.makeText(OverYearsTestActivity.this, "我的收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tab_test_setting:
                //todo 点击设置tab
                Toast.makeText(OverYearsTestActivity.this, "练习设置", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ApplicationGuideActivity.class));
                break;
            default:
                break;
        }
    }

    /*
    定义listView 中item 的适配器
     */
    class TestsItemAdapter extends BaseAdapter {

        ArrayList<TestInfo> mList = new ArrayList<>();
        Context context;
        LayoutInflater inflater;

        public TestsItemAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        public void setList(ArrayList<TestInfo> list) {
            this.mList = list;
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
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return SIMPLE_TYPE;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            int type = getItemViewType(position);
            if (convertView == null) {
                switch (type) {
                    case SIMPLE_TYPE:
                        convertView = inflater.inflate(R.layout.layout_test_item, parent, false);
                        viewHolder = new ViewHolder();
                        viewHolder.title = (TextView) convertView.findViewById(R.id.test_title_txt);
                        viewHolder.count = (TextView) convertView.findViewById(R.id.tests_count_txt);
                        viewHolder.totalTime = (TextView) convertView.findViewById(R.id.tests_time_txt);
                        viewHolder.totalScore = (TextView) convertView.findViewById(R.id.tests_score_txt);
                        viewHolder.watchedCount = (TextView) convertView.findViewById(R.id.watched_count_txt);
                        viewHolder.collectedCount = (TextView) convertView.findViewById(R.id.collected_count_txt);
                        convertView.setTag(viewHolder);
                        break;
                    default:
                        break;
                }

            } else {
                switch (type) {
                    case SIMPLE_TYPE:
                        viewHolder = (ViewHolder) convertView.getTag();
                        break;
                    default:
                        break;
                }
            }

            /*
            设置TestInfo字段信息到列表的item中
             */
            switch (type) {
                case SIMPLE_TYPE:
                    TestInfo test = (TestInfo) getItem(position);
                    viewHolder.title.setText(test.getTitle());
                    viewHolder.count.setText(test.getCount());
                    viewHolder.totalTime.setText(test.getTotalTime());
                    viewHolder.totalScore.setText(test.getTotalScore());
                    viewHolder.watchedCount.setText(test.getWatchedCount());
                    viewHolder.collectedCount.setText(test.getCollectedCount());
                    break;

                default:
                    break;

            }
            return convertView;
        }

        /*
        包含TestInfo中内容字段的ViewHolder
         */
        class ViewHolder {
            TextView title;
            TextView count;
            TextView totalTime;
            TextView totalScore;
            TextView watchedCount;
            TextView collectedCount;
        }
    }

    static class ShowTabHandler extends Handler {
        private WeakReference<OverYearsTestActivity> overYearsTestActivityWeakReference;

        public ShowTabHandler(OverYearsTestActivity overYearsTestActivity) {
            overYearsTestActivityWeakReference = new WeakReference<>(overYearsTestActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            OverYearsTestActivity overYearsTestActivity = overYearsTestActivityWeakReference.get();
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
