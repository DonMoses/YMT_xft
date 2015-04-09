package com.ymt.demo1.styleTabCircle.advice;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ymt.demo1.R;

/**
 * Created by Moses on 2015
 */
public class AdviceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        initView();
    }

    protected void initView() {
        View mergeView = findViewById(R.id.merge_advice_title);
        View adviceTitle = mergeView.findViewById(R.id.merge_title_layout);
        final Button backBtn = (Button) adviceTitle.findViewById(R.id.merge_title_back);
        TextView titleTxt = (TextView) mergeView.findViewById(R.id.merge_title_text);
        titleTxt.setText("意见反馈");
        adviceTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    backBtn.setBackgroundResource(R.drawable.back_normal);
                }

                return false;
            }
        });

        adviceTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
