package com.ymt.demo1.main.setting;

import android.app.Activity;
import android.os.Bundle;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by Dan on 2015/4/3
 */
public class ManageStoreActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_store);
        initTitle();
        initView();
    }

    protected void initView() {


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
}
