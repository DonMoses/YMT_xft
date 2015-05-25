package com.ymt.demo1.launchpages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.ymt.demo1.R;
import com.ymt.demo1.mainStyles.CircleMenuActivity;
import com.ymt.demo1.mainStyles.NavigationMenuActivity;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dan on 2015/5/18
 * 加载界面。 根据sharedPreference中保存的设置来启动到相应主界面
 */
public class LoadingPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                chooseLaunchStyle();
            }
        }, 2000);
    }

    protected void chooseLaunchStyle() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(MainActivity.SETTING_PREFERENCES, MODE_PRIVATE);
        int style = sharedPreferences.getInt(MainActivity.LAUNCH_STYLE_KEY, 0);
        switch (style) {
            case MainActivity.LAUNCH_STYLE_CIRCLE_MODE:
                startActivity(new Intent(this, CircleMenuActivity.class));
                finish();
                break;
            case MainActivity.LAUNCH_STYLE_SLIDE_MODE:
                startActivity(new Intent(this, NavigationMenuActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

}
