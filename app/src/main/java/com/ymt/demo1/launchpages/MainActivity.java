package com.ymt.demo1.launchpages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.ymt.demo1.main.floatWindow.FloatWindowService;

/**
 * Created by Dan on 2015/5/18
 * 一个无界面的，用于判断启动方式的Activity
 */
public class MainActivity extends Activity {
    public static final String SETTING_PREFERENCES = "setting_preferences";
    public static final String LAUNCH_STYLE_KEY = "launch_style_key";
    public static final int LAUNCH_STYLE_CIRCLE_MODE = 0;
    public static final int LAUNCH_STYLE_SLIDE_MODE = 1;
    public static final String FIRST_LAUNCH_KEY = "first_launch_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, FloatWindowService.class));
        SharedPreferences sharedPreferences = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE);
        boolean isFirstLaunch = sharedPreferences.getBoolean(MainActivity.FIRST_LAUNCH_KEY, true);
        if (isFirstLaunch) {
            startActivity(new Intent(this, GuideActivity.class));
        } else {
            startActivity(new Intent(this, LoadingPageActivity.class));
        }
        finish();
    }
}
