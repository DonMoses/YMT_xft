package com.ymt.demo1.plates.hub;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ymt.demo1.R;

/**
 * Created by Dan on 2015/4/8
 */
public class PostActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initView();

    }

    protected void initView() {
        //设置顶部title及其事件
        View title = findViewById(R.id.merge_title_layout);
        final Button backBtn = (Button) title.findViewById(R.id.merge_title_back);
        title.setOnTouchListener(new View.OnTouchListener() {
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

        //输入框
        EditText titleTxt = (EditText) findViewById(R.id.post_title);
        EditText infoTxt = (EditText) findViewById(R.id.post_info);

        //点击退出当前活动，回到主界面
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostActivity.this, "post done!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
