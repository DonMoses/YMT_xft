package com.ymt.demo1.plates.exportConsult;

import android.os.Bundle;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;

/**
 * Created by Dan on 2015/5/12
 */
public class ConsultNowActivity extends BaseFloatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_now);
        initTitle();
    }

    /**
     * 初始化title
     */
    protected void initTitle() {
        MyTitle myTitle = (MyTitle) findViewById(R.id.my_title);
        myTitle.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        myTitle.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }


}
