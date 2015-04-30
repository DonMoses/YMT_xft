package com.ymt.demo1.customKeyBoard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by Dan on 2015/4/24
 */
public class AScreenSizeUtil {
    public static AScreenSizeUtil AScreenSizeUtil;
    private int statusBarHeight = 0;
    private int titleBarHeight = 0;
    private int pureScreenHeight = 0;
    private int rootScreenHeight = 0;
    private int contentBarHeight = 0;
    private Context context;

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public int getTitleBarHeight() {
        return titleBarHeight;
    }

    public int getPureScreenHeight() {
        return pureScreenHeight;
    }

    public int getRootScreenHeight() {
        return rootScreenHeight;
    }

    private AScreenSizeUtil(Context context, int screenHeight, int contentBarHeight) {
        this.contentBarHeight = contentBarHeight;
        this.context = context;
        this.rootScreenHeight = screenHeight;
        initSize();
    }

    /**
     * @param context          ： 当前视图所在Context(一般是活动)
     * @param screenHeight     : 屏幕宽度（根据空间根布局获得）
     * @param contentBarHeight ： 状态栏和标题栏高度（通过软键盘弹出和关闭获得）
     */
    public static AScreenSizeUtil getInstance(Context context, int screenHeight, int contentBarHeight) {
        if (AScreenSizeUtil == null) {
            AScreenSizeUtil = new AScreenSizeUtil(context, screenHeight, contentBarHeight);
        }
        return AScreenSizeUtil;
    }

    private void setStatusBarHeight(Context context) {

        int statusHeight;

        Rect localRect = new Rect();

        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);

        statusHeight = localRect.top;

        if (0 == statusHeight) {

            Class<?> localClass;

            try {

                localClass = Class.forName("com.android.internal.R$dimen");

                Object localObject = localClass.newInstance();

                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());

                statusHeight = context.getResources().getDimensionPixelSize(i5);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        statusBarHeight = statusHeight;

    }

    /**
     * 底部标题栏高度
     */
    private void setTitleBarHeight() {
        titleBarHeight = contentBarHeight - statusBarHeight;
    }

    /**
     * 不包含的标题栏、状态栏的屏幕高度
     */
    private void setPureScreenHeight() {
        pureScreenHeight = rootScreenHeight - contentBarHeight;
    }

    /**
     * 打印屏幕高度信息
     */
    public void logScreenInfo() {
        Log.e("TAG", "statusBarHeight>>>>>" + statusBarHeight);//N5 本机状态栏高度为75
        Log.e("TAG", "titleBarHeight>>>>>" + titleBarHeight);//N5 本机状态栏高度为144
        Log.e("TAG", "rootScreenHeight>>>>>" + rootScreenHeight);//N5 本机状态栏高度为1920
        Log.e("TAG", "pureScreenHeight>>>>>" + pureScreenHeight);//N5 本机状态栏高度为1701
    }

    /**
     * 使用本类时，则测量屏幕及其组件高度
     */
    public void initSize() {
        setStatusBarHeight(context);
        setTitleBarHeight();
        setPureScreenHeight();
    }


}
