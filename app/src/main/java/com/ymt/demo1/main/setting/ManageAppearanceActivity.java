package com.ymt.demo1.main.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.launchpages.MainActivity;
import com.ymt.demo1.mainStyles.CircleMenuActivity;
import com.ymt.demo1.mainStyles.NavigationMenuActivity;

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
        TextView selectCircle = (TextView) findViewById(R.id.select_circle);
        SharedPreferences mSharedPreferences =
                getSharedPreferences(MainActivity.SETTING_PREFERENCES, MODE_PRIVATE);
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.select_slide:
                        Intent intent1 = new Intent();
                        intent1.setClass(ManageAppearanceActivity.this, NavigationMenuActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        editor.putInt(MainActivity.LAUNCH_STYLE_KEY, MainActivity.LAUNCH_STYLE_SLIDE_MODE);      //保存风格设置
                        editor.apply();
                        finish();
                        if (CircleMenuActivity.styleChangeListener != null) {
                            CircleMenuActivity.styleChangeListener.onStyleChanged();
                        }
                        break;
                    case R.id.select_circle:
                        Intent intent2 = new Intent();
                        intent2.setClass(ManageAppearanceActivity.this, CircleMenuActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        editor.putInt(MainActivity.LAUNCH_STYLE_KEY, MainActivity.LAUNCH_STYLE_CIRCLE_MODE);     //保存风格设置
                        editor.apply();
                        finish();
                        if (NavigationMenuActivity.styleChangeListener != null) {
                            NavigationMenuActivity.styleChangeListener.onStyleChanged();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        selectSlide.setOnClickListener(onClickListener);
        selectCircle.setOnClickListener(onClickListener);

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
