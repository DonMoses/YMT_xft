package com.ymt.demo1.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import com.ymt.demo1.dbBeams.Account;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/4/16
 */
public class AppContext extends LitePalApplication {
    private static List<Activity> yActivities;
    private static Activity floatActivity;
    private static AppContext appContext;
    public static final String SERVICE_BASE_URL = "http://120.24.172.105:8000";
    public static Account myAccount;
    public static String now_user_id;
    public static String now_session_id;

    public static SharedPreferences getSaveAccountPrefecences(Context context) {
        return context.getSharedPreferences("saved_account", MODE_PRIVATE);
    }

    public static AppContext getInstance() {
        return appContext;
    }

    @Override
    public final void onCreate() {
        if (yActivities == null) {
            yActivities = new ArrayList<>();
        }
        appContext = this;
        myAccount = new Account();
    }

    /**
     * 判断是否显示浮动床
     */
    public static boolean mayFloatMenu() {
        return yActivities != null && yActivities.size() > 0;
    }

    /**
     * 将activity添加到自定义的全局application中
     */
    public static void addToAppContext(Activity activity) {
        if (!yActivities.contains(activity)) {           //先判断是否已经存在
            yActivities.add(activity);
        }
    }

    /**
     * 将activity从自定义的全局application中移除
     */
    public static void removeFromAppContext(Activity activity) {
        if (yActivities.contains(activity)) {           //先判断是否已经存在
            yActivities.remove(activity);
        }
    }

    public List<Activity> getyActivities() {
        return yActivities;
    }


    /**
     * 获得当前浮动窗所在的Activity，用于响应浮动窗中的事件
     */
    public static Activity getCurrActivity() {
        if (yActivities != null && yActivities.size() > 0) {
            return yActivities.get(yActivities.size() - 1);
        }
        return null;
    }

    /**
     * 获得FloatMenuActivity
     */
    public static Activity getFloatActivity() {
        return floatActivity;
    }

    /**
     * 设置FloatMenuActivity
     */
    public static void setFloatActivity(Activity activity) {
        floatActivity = activity;
    }

    /**
     * 清空所有Activity
     */
    public static void clearAllActivities() {
        for (Activity activity : yActivities) {
            activity.finish();
        }
        yActivities.clear();
    }
}
