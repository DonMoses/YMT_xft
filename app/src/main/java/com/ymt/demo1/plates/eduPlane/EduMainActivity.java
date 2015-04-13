package com.ymt.demo1.plates.eduPlane;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.ymt.demo1.R;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dan on 2015/4/13
 */
public class EduMainActivity extends Activity {
    private static final int DO_REFRESH = 0;
    TextView timeDay;
    TextView timeHour;
    TextView timeMinute;
    TextView timeSecond;
    /*
    假设在2015年7月3日上午的8：30考试
     */
    int year = 2015, month = 7, day = 20, hour = 8, min = 30, sec = 0;
    int showDay, showHour, showMin, showSec;

    private final TimeHandler timeHandler = new TimeHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_main);
        initView();
    }

    protected void initView() {
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
        SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                while (true) {
                    long curTime = System.currentTimeMillis();      //当前系统时间
                    if ((beginTime - curTime) <= 0) {
                        break;
                    }
                    final long gapTime = beginTime - curTime;
                    showDay = (int) (gapTime / (1000 * 3600 * 24));
                    showHour = (int) ((gapTime / (1000 * 3600)) % 24);
                    long restSec = gapTime / 1000 - showDay * 24 * 3600 - showHour * 3600;
                    showMin = (int) (restSec / 60);
                    showSec = (int) (restSec % 60);

                    timeHandler.sendEmptyMessage(DO_REFRESH);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    protected void freshTimer() {
        timeDay.setText(String.valueOf(showDay));
        timeHour.setText(String.valueOf(showHour));
        timeMinute.setText(String.valueOf(showMin));
        timeSecond.setText(String.valueOf(showSec));
    }

    static class TimeHandler extends Handler {
        private WeakReference<EduMainActivity> eduMainActivityWeakReference;

        public TimeHandler(EduMainActivity eduMainActivity) {
            eduMainActivityWeakReference = new WeakReference<>(eduMainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            EduMainActivity eduMainActivity = eduMainActivityWeakReference.get();
            if (eduMainActivity != null) {
                switch (msg.what) {
                    case DO_REFRESH:
                        eduMainActivity.freshTimer();
                        break;
                    default:
                        break;

                }

            }
        }
    }
}
