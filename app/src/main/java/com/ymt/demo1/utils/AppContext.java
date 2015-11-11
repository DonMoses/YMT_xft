package com.ymt.demo1.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.ymt.demo1.R;
import com.ymt.demo1.launchpages.MainActivity;
import com.ymt.demo1.main.setting.VoiceSettingActivity;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Dan on 2015/4/16
 */
public class AppContext extends LitePalApplication implements VoiceSettingActivity.VoiceSettingListener {
    private static List<Activity> yActivities;
    private static Activity floatActivity;
    private static AppContext appContext;
    public static int now_user_id;
    public static String now_session_id;
    public static String now_user_name;
    public static int screenWidth;
    //    public static int sysKeyBoardWidth;
//    public static int sysKeyBoardHeight;
    public static Bitmap headerPic;
    public static String version;

    public static SharedPreferences getSaveAccountPreferences(Context context) {
        return context.getSharedPreferences("saved_account", MODE_PRIVATE);
    }

    public static AppContext getInstance() {
        return appContext;
    }

    @Override
    public final void onCreate() {
        super.onCreate();
        if (yActivities == null) {
            yActivities = new ArrayList<>();
        }
        appContext = this;

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;//屏幕宽度

        //消息设置
        sharedPreferences = getSharedPreferences(MainActivity.SETTING_PREFERENCES, MODE_PRIVATE);
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

    /**
     * 获取头像
     */
    public static ImageRequest getHeader(String headerUrl) {
        return new ImageRequest(BaseURLUtil.BASE_URL + headerUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                headerPic = bitmap;
            }
        }, 72, 72, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                headerPic = null;
            }
        });
    }

    /**
     * 消息声音等设置
     */
    private SharedPreferences sharedPreferences;

    protected void initVoiceSetting() {
        String[] array = getResources().getStringArray(R.array.voice_settings);
        for (String anArray : array) {
            switch (anArray) {
                case "通知消息内容显示":
                    if (sharedPreferences.getBoolean(anArray, false)) {
                        //todo
//                        Log.e("TAG", "setting " + anArray + "is" + sharedPreferences.getBoolean(anArray, false));
                    }
                    break;
                case "锁屏显示消息弹窗":
                    if (sharedPreferences.getBoolean(anArray, false)) {
                        //todo
//                        Log.e("TAG", "setting " + anArray + "is" + sharedPreferences.getBoolean(anArray, false));
                    }
                    break;
                case "退出后仍接受消息通知":
                    if (sharedPreferences.getBoolean(anArray, false)) {
                        //todo
//                        Log.e("TAG", "setting " + anArray + "is" + sharedPreferences.getBoolean(anArray, false));
                    }
                    break;
                case "群消息":
                    if (sharedPreferences.getBoolean(anArray, false)) {
                        //todo
//                        Log.e("TAG", "setting " + anArray + "is" + sharedPreferences.getBoolean(anArray, false));
                    }
                    break;
                case "声音":
                    if (sharedPreferences.getBoolean(anArray, false)) {
                        //todo
//                        Log.e("TAG", "setting " + anArray + "is" + sharedPreferences.getBoolean(anArray, false));
                    }
                    break;
                case "群振动":
                    if (sharedPreferences.getBoolean(anArray, false)) {
                        //todo
//                        Log.e("TAG", "setting " + anArray + "is" + sharedPreferences.getBoolean(anArray, false));
                    }
                    break;
                case "振动":
                    if (sharedPreferences.getBoolean(anArray, false)) {
                        //todo
//                        Log.e("TAG", "setting " + anArray + "is" + sharedPreferences.getBoolean(anArray, false));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onSettingDone() {
        initVoiceSetting();
    }


    /**
     * 当前是否有网络
     */
    public static boolean internetAvialable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 网络错误toast
     */
    public static void toastBadInternet() {
        Toast.makeText(appContext, "网络错误，请稍后重试!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 网络服务器解析toast
     */
    public static void toastBadJson() {
        Toast.makeText(appContext, "服务器故障，请稍后重试!", Toast.LENGTH_SHORT).show();
    }
}
