package com.ymt.demo1.styleTabCircle.sign;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.ymt.demo1.R;

/**
 * Created by Moses on 2015
 */
public class SignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
    }

    protected void initView() {
        View mergeView = findViewById(R.id.merge_sign_title);
        View signTitle = mergeView.findViewById(R.id.merge_title_layout);
        final Button backBtn = (Button) signTitle.findViewById(R.id.merge_title_back);
//        TextView titleTxt = (TextView) mergeView.findViewById(R.id.merge_title_text);
        signTitle.setOnTouchListener(new View.OnTouchListener() {
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

        signTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
