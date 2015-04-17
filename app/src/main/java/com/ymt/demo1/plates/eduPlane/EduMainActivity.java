package com.ymt.demo1.plates.eduPlane;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.CyclePagerAdapter;
import com.ymt.demo1.customViews.IndicatorView;
import com.ymt.demo1.customViews.MyScaleImageView;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.SearchActivity;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dan on 2015/4/13
 */
public class EduMainActivity extends BaseFloatActivity {
    private static final int DO_REFRESH = 0;
    private static final int SHOW_NEXT_PAGE = 1;
    /*
    考试时间倒计时提示views
     */
    private TextView timeDay;
    private TextView timeHour;
    private TextView timeMinute;
    private TextView timeSecond;
    private static boolean ALWAYS_ON = true;
    //顶部ViewPager
    private ViewPager adViewPager;
    /*
    假设在2015年7月3日上午的8：30考试
     */
    int year = 2015, month = 7, day = 20, hour = 8, min = 30, sec = 0;
    int showDay, showHour, showMin, showSec;
    private final MyHandler myHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_main);
        initTitle();
        initView();
    }

    protected void initTitle() {
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
                startActivity(new Intent(EduMainActivity.this, SearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                //todo 设置按钮Action
//                Toast.makeText(EduMainActivity.this, "设置按钮Action", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initView() {
        initAdViewPager();
        initNextExamView();
        initEduItem();
        initTab();
    }

    /**
     * 初始化顶部广告ViewPager
     */
    protected void initAdViewPager() {
        adViewPager = (ViewPager) findViewById(R.id.ad_viewPager);
        /*
        设置适配器
         */
        final CyclePagerAdapter adPagerAdapter = new CyclePagerAdapter();
        adViewPager.setAdapter(adPagerAdapter);
        /*
        更新数据源（Views）
         */
        LayoutInflater inflater = LayoutInflater.from(this);
        ArrayList<View> views = new ArrayList<>();
        views.add(inflater.inflate(R.layout.edu_pager1, null));
        views.add(inflater.inflate(R.layout.edu_pager2, null));
        views.add(inflater.inflate(R.layout.edu_pager3, null));
        adPagerAdapter.setViews(views);
        //指示器Indicator
        final IndicatorView indicator = (IndicatorView) findViewById(R.id.myPointIndicator);
        indicator.updateTotal(adPagerAdapter.getCount());   //设置指示器显示item个数（适配adapter中元素个数）
        indicator.setCurr(0);
        /*
        pager 滑动事件
         */
        adViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setCurr(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:

                        break;
                    case ViewPager.SCROLL_STATE_IDLE:

                        break;
                    default:
                        break;

                }
            }
        });

        /*
        开启线程，让使Viewpager轮播
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ALWAYS_ON) {

                    if (adPagerAdapter.getCount() == 0) {
                        Toast.makeText(EduMainActivity.this, "这里应加入view", Toast.LENGTH_SHORT).show();
                        continue;
                    }

                    int toPosition;
                    int curPosition = adViewPager.getCurrentItem();
                    if (curPosition < adViewPager.getChildCount() - 1) {
                        toPosition = curPosition + 1;
                    } else {
                        toPosition = 0;             //从page尾跳到page头
                    }

                    Message msg = Message.obtain();
                    msg.what = SHOW_NEXT_PAGE;
                    msg.arg1 = toPosition;
                    myHandler.sendMessage(msg);

                    try {
                        Thread.sleep(6000);         //每6 s切换到下一page
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }

    /**
     * 初始化平台内容item   。 ”我的学习“，”历年真题“，”报考指南“等
     */
    protected void initEduItem() {

        MyScaleImageView overYearsTest = (MyScaleImageView) findViewById(R.id.overYearsTest);
        MyScaleImageView practiceTest = (MyScaleImageView) findViewById(R.id.practiceTest);
        MyScaleImageView myStudy = (MyScaleImageView) findViewById(R.id.myStudy);
        MyScaleImageView studyDatum = (MyScaleImageView) findViewById(R.id.studyDatum);
        MyScaleImageView appGuide = (MyScaleImageView) findViewById(R.id.appGuide);
        MyScaleImageView trainVideo = (MyScaleImageView) findViewById(R.id.trainVideo);
        MyScaleImageView dialogue = (MyScaleImageView) findViewById(R.id.dialogue);
        MyScaleImageView practicePaper = (MyScaleImageView) findViewById(R.id.practicePaper);
        MyScaleImageView eduMore = (MyScaleImageView) findViewById(R.id.eduMore);
        MyScaleImageView.OnViewClickListener eduItemClickListener = new MyScaleImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyScaleImageView view) {
                switch (view.getId()) {
                    case R.id.overYearsTest:
                        startActivity(new Intent(EduMainActivity.this, OverYearsTestActivity.class));    //历年真题
                        break;
                    case R.id.practiceTest:
                        Toast.makeText(EduMainActivity.this, "模拟考试", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.myStudy:
                        startActivity(new Intent(EduMainActivity.this, MyStudyActivity.class));             //我的学习
                        break;
                    case R.id.studyDatum:
                        startActivity(new Intent(EduMainActivity.this, StudyDatumActivity.class));          //学习资料
                        break;
                    case R.id.appGuide:
                        startActivity(new Intent(EduMainActivity.this, ApplicationGuideActivity.class));    //报考指南
                        break;
                    case R.id.trainVideo:
                        Toast.makeText(EduMainActivity.this, "培训视频", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.dialogue:
                        startActivity(new Intent(EduMainActivity.this, DialogueListActivity.class));         //问答
                        break;
                    case R.id.practicePaper:
                        Toast.makeText(EduMainActivity.this, "模拟试题", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.eduMore:
                        Toast.makeText(EduMainActivity.this, "更多", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }
        };

        myStudy.setOnClickIntent(eduItemClickListener);
        practicePaper.setOnClickIntent(eduItemClickListener);
        practiceTest.setOnClickIntent(eduItemClickListener);
        dialogue.setOnClickIntent(eduItemClickListener);
        appGuide.setOnClickIntent(eduItemClickListener);
        eduMore.setOnClickIntent(eduItemClickListener);
        overYearsTest.setOnClickIntent(eduItemClickListener);
        studyDatum.setOnClickIntent(eduItemClickListener);
        trainVideo.setOnClickIntent(eduItemClickListener);
    }

    /**
     * 初始化考试时间倒计时提示控件
     */
    protected void initNextExamView() {
        TextView examYear = (TextView) findViewById(R.id.next_exam_year);
        TextView examMonth = (TextView) findViewById(R.id.next_exam_month);
        TextView examDay = (TextView) findViewById(R.id.next_exam_day);
        examYear.setText(String.valueOf(year));
        examMonth.setText(String.valueOf(month));
        examDay.setText(String.valueOf(day));

        timeDay = (TextView) findViewById(R.id.time_day);
        timeHour = (TextView) findViewById(R.id.time_hour);
        timeMinute = (TextView) findViewById(R.id.time_minute);
        timeSecond = (TextView) findViewById(R.id.time_second);

        //String 先转成date
        String begin = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
        SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = sDate.parse(begin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //date转成毫秒
        assert date != null;
        final long beginTime = date.getTime();

        /*
        在到达指定事件之前，每隔1s 刷新timer
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ALWAYS_ON) {
                    long curTime = System.currentTimeMillis();      //当前系统时间
                    if ((beginTime - curTime) <= 0) {
                        continue;
                    }
                    final long gapTime = beginTime - curTime;
                    showDay = (int) (gapTime / (1000 * 3600 * 24));
                    showHour = (int) ((gapTime / (1000 * 3600)) % 24);
                    long restSec = gapTime / 1000 - showDay * 24 * 3600 - showHour * 3600;      //开始计算分、秒
                    showMin = (int) (restSec / 60);
                    showSec = (int) (restSec % 60);

                    myHandler.sendEmptyMessage(DO_REFRESH);
                    try {
                        Thread.sleep(1000);             //每1s 刷新一次控件
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * 初始化底部Tab。 ”我的收藏“，”切换题库“，”练习设置“等
     */
    protected void initTab() {
        View tabCollect = findViewById(R.id.edu_tab_my_collect);
        View tabChangeTest = findViewById(R.id.edu_tab_change_tests);
        View tabSetting = findViewById(R.id.edu_tab_practice_setting);
        View.OnClickListener tabClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.edu_tab_my_collect:
                        Toast.makeText(EduMainActivity.this, "edu_tab_my_collect", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.edu_tab_change_tests:
                        Toast.makeText(EduMainActivity.this, "edu_tab_change_tests", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.edu_tab_practice_setting:
                        Toast.makeText(EduMainActivity.this, "edu_tab_practice_setting", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }
        };
        tabCollect.setOnClickListener(tabClickListener);
        tabChangeTest.setOnClickListener(tabClickListener);
        tabSetting.setOnClickListener(tabClickListener);

    }

    /**
     * 刷新考试时间提示
     */
    protected void freshTimer() {
        timeDay.setText(String.valueOf(showDay));
        timeHour.setText(String.valueOf(showHour));
        timeMinute.setText(String.valueOf(showMin));
        timeSecond.setText(String.valueOf(showSec));
    }

    /**
     * ViewPager轮播
     */
    protected void autoNextPage(int toPosition) {
        adViewPager.setCurrentItem(toPosition);
    }

    /*
    Handler
     */
    static class MyHandler extends Handler {
        private WeakReference<EduMainActivity> eduMainActivityWeakReference;

        public MyHandler(EduMainActivity eduMainActivity) {
            eduMainActivityWeakReference = new WeakReference<>(eduMainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            EduMainActivity eduMainActivity = eduMainActivityWeakReference.get();
            if (eduMainActivity != null) {
                switch (msg.what) {
                    case DO_REFRESH:                //刷新考试时间提示
                        eduMainActivity.freshTimer();
                        break;
                    case SHOW_NEXT_PAGE:
                        eduMainActivity.autoNextPage(msg.arg1);
                        break;
                    default:
                        break;

                }

            }
        }
    }


}
