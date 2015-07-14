package com.ymt.demo1.main.sign;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyCheckView;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by Dan on 2015/4/3
 */
public class FoundPswActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_psw);
        initTitle();
        initView();
    }

    private boolean isLighting = false;

    protected void initView() {
        Button lightBtn = (Button) findViewById(R.id.ligth_me);
        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);

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
}
