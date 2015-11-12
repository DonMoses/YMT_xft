package com.ymt.demo1.plates.eduPlane;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.main.search.FullSearchActivity;
import com.ymt.demo1.plates.eduPlane.easyWrong.EasyWrongActivity;
import com.ymt.demo1.plates.eduPlane.myStudy.MyMockListActivity;
import com.ymt.demo1.plates.eduPlane.video.VideoTrainActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.plates.eduPlane.examGuide.ExamsGuideMainActivity;
import com.ymt.demo1.plates.eduPlane.mockExams.MockExamsMainActivity;
import com.ymt.demo1.plates.eduPlane.pastExams.PastExamsMainActivity;
import com.ymt.demo1.plates.eduPlane.studyDatum.StudyDatumActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private TextView examYear;
    private TextView examMonth;
    private TextView examDay;
    private TextView examName;

    private static boolean ALWAYS_ON = true;
    /*
    假设在2015年7月3日上午的8：30考试
     */
    int year = 0, month = 0, day = 0, hour = 0, min = 0, sec = 0;
    int showDay, showHour, showMin, showSec;
    private final MyHandler myHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_edu_main);
        initTitle();
        initView();
        mQueue.add(getExamInfo());
    }

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
                startActivity(new Intent(EduMainActivity.this, FullSearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                //todo 设置按钮Action
                Toast.makeText(EduMainActivity.this, "设置按钮Action", Toast.LENGTH_SHORT).show();

            }
        });
    }

    protected void initView() {
        initNextExamView();
        initEduItem();
    }

    /**
     * 初始化平台内容item   。 ”我的学习“，”历年真题“，”报考指南“等
     */
    protected void initEduItem() {

        ImageView overYearsTest = (ImageView) findViewById(R.id.pastExams);
        ImageView practiceTest = (ImageView) findViewById(R.id.anologExams);
        ImageView myStudy = (ImageView) findViewById(R.id.myStudy);
        ImageView studyDatum = (ImageView) findViewById(R.id.studyDatum);
        ImageView appGuide = (ImageView) findViewById(R.id.appGuide);
        ImageView trainVideo = (ImageView) findViewById(R.id.trainVideo);
        ImageView practicePaper = (ImageView) findViewById(R.id.examInterpret);
        ImageView eduMore = (ImageView) findViewById(R.id.eduMore);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.pastExams:
                        startActivity(new Intent(EduMainActivity.this, PastExamsMainActivity.class));    //历年真题
                        break;
                    case R.id.anologExams:
                        startActivity(new Intent(EduMainActivity.this, MockExamsMainActivity.class));    //模拟考试
                        break;
                    case R.id.myStudy:
                        startActivity(new Intent(EduMainActivity.this, MyMockListActivity.class));       //我的学习
                        break;
                    case R.id.studyDatum:
                        startActivity(new Intent(EduMainActivity.this, StudyDatumActivity.class));       //学习资料
                        break;
                    case R.id.appGuide:
                        startActivity(new Intent(EduMainActivity.this, ExamsGuideMainActivity.class));   //报考指南
                        break;
                    case R.id.trainVideo:
                        startActivity(new Intent(EduMainActivity.this, VideoTrainActivity.class));       //培训视频
                        break;
                    case R.id.examInterpret:
                        startActivity(new Intent(EduMainActivity.this, EasyWrongActivity.class));        //易错题分析
                        break;
                    case R.id.eduMore:
                        Toast.makeText(EduMainActivity.this, "更多", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }
        };

        myStudy.setOnClickListener(onClickListener);
        practicePaper.setOnClickListener(onClickListener);
        practiceTest.setOnClickListener(onClickListener);
        appGuide.setOnClickListener(onClickListener);
        eduMore.setOnClickListener(onClickListener);
        overYearsTest.setOnClickListener(onClickListener);
        studyDatum.setOnClickListener(onClickListener);
        trainVideo.setOnClickListener(onClickListener);
    }

    /**
     * 初始化考试时间倒计时提示控件
     */
    protected void initNextExamView() {
        examYear = (TextView) findViewById(R.id.next_exam_year);
        examMonth = (TextView) findViewById(R.id.next_exam_month);
        examDay = (TextView) findViewById(R.id.next_exam_day);
        examName = (TextView) findViewById(R.id.next_exam_name);

        timeDay = (TextView) findViewById(R.id.time_day);
        timeHour = (TextView) findViewById(R.id.time_hour);
        timeMinute = (TextView) findViewById(R.id.time_minute);
        timeSecond = (TextView) findViewById(R.id.time_second);

    }

    protected void examTimer() {
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
     * 刷新考试时间提示
     */
    protected void freshTimer() {
        timeDay.setText("还有 " + String.valueOf(showDay));
        timeHour.setText(String.valueOf(showHour));
        timeMinute.setText(String.valueOf(showMin));
        timeSecond.setText(String.valueOf(showSec));
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
//                        eduMainActivity.autoNextPage(msg.arg1);
                        break;
                    default:
                        break;

                }

            }
        }
    }

    protected StringRequest getExamInfo() {
        return new StringRequest(BaseURLUtil.EARLIEST_EXAM_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        JSONObject object = jsonArray.getJSONObject(0);

                        String time = object.optString("examTime");
                        String name = object.optString("examName");

                        if (time.length() >= 4) {
                            year = Integer.valueOf(time.substring(0, 4));
                        }
                        if (time.length() >= 7) {
                            month = Integer.valueOf(time.substring(5, 7));
                        }
                        if (time.length() >= 10) {
                            day = Integer.valueOf(time.substring(8, 10));
                        }

                        examYear.setText(String.valueOf(year));
                        examMonth.setText(String.valueOf(month));
                        examDay.setText(String.valueOf(day) + "日");
                        examName.setText(name);

                        examTimer();

                    }
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(EduMainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                AppContext.toastBadInternet();
            }
        });
    }


}
