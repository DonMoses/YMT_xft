package com.ymt.demo1.main.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.mainStyles.CircleMenuActivity;

/**
 * Created by Dan on 2015/4/3
 */
public class ManageAppearanceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appearance);
        initTitle();
        initView();
    }

    protected void initView() {
        TextView selectSlide = (TextView) findViewById(R.id.select_slide);
        selectSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(ManageAppearanceActivity.this, SlidingMenuActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//                CircleMenuActivity.styleChangeListener.onStyleChanged();
            }
        });

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

    public interface StyleChangeListener {
        void onStyleChanged();
    }
}
