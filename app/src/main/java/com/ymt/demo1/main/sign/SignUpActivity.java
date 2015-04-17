package com.ymt.demo1.main.sign;

import android.app.Activity;
import android.os.Bundle;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by Moses on 2015
 */
public class SignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initTitle();
        initView();
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }

    protected void initView() {

    }
}
