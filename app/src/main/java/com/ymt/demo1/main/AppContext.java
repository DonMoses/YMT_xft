package com.ymt.demo1.main;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/4/16
 */
public class AppContext extends Application {
    private static Context context;
    private static List<Activity> yActivities;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        if (yActivities == null) {
            yActivities = new ArrayList<>();
        }
    }

    public static Context getContext() {
        return context;
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
}
