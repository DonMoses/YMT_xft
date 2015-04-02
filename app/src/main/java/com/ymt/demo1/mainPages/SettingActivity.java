package com.ymt.demo1.mainPages;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ymt.demo1.R;

/**
 * Created by Dan on 2015/4/2
 */
public class SettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        initView();
    }

    protected void initView() {
        View mergeView = findViewById(R.id.merge_setting_title);
        View adviceTitle = mergeView.findViewById(R.id.merge_title_layout);
        final Button backBtn = (Button) adviceTitle.findViewById(R.id.merge_title_back);
        TextView titleTxt = (TextView) mergeView.findViewById(R.id.merge_title_text);
        titleTxt.setText("设置");
        adviceTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    backBtn.setBackgroundResource(R.drawable.btn_back_detail_pressed);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    backBtn.setBackgroundResource(R.drawable.btn_back_detail_normal);
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

        ListView settingListView = (ListView) findViewById(R.id.common_quest_list_view);
        settingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //设置选项点击事件


            }
        });

        Button signOutBtn = (Button) findViewById(R.id.sign_out_btn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出当前账号

                
            }
        });
    }
}
