package com.ymt.demo1.launchpages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.main.floatWindow.FloatWindowService;
import com.ymt.demo1.mainStyles.CircleMenuActivity;
import com.ymt.demo1.mainStyles.NavigationMenuActivity;
import com.ymt.demo1.mainStyles.TabMenuActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.tablemanager.Connector;

/**
 * Created by Dan on 2015/5/18
 * 一个无界面的，用于判断启动方式的Activity
 */
public class MainActivity extends Activity {
    public static final String SETTING_PREFERENCES = "setting_preferences";
    public static final String LAUNCH_STYLE_KEY = "launch_style_key";
    public static final int LAUNCH_STYLE_CIRCLE_MODE = 0;
    public static final int LAUNCH_STYLE_SLIDE_MODE = 1;
    public static final int LAUNCH_STYLE_TAB_MODE = 2;
    public static final String FIRST_LAUNCH_KEY = "first_launch_key";

    private SharedPreferences sharedPreferences;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connector.getDatabase();
        SharedPreferences preferences = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE);
        boolean isFirstLaunch = preferences.getBoolean(MainActivity.FIRST_LAUNCH_KEY, true);
        if (isFirstLaunch) {
            startActivity(new Intent(this, GuideActivity.class));       //第一次启动
            finish();
        } else {
            chooseLaunchStyle();                                        //常规启动
        }

        startService(new Intent(this, FloatWindowService.class));

        sharedPreferences = AppContext.getSaveAccountPreferences(this);
        mQueue = Volley.newRequestQueue(this);

        //获取账号密码信息自动登录
        String admin = sharedPreferences.getString("account", "");
        String psw = sharedPreferences.getString("password", "");
        if (!(TextUtils.isEmpty(admin) || TextUtils.isEmpty(psw))) {
            //将等路请求加入Volley队列
            mQueue.add(signInRequest(admin, psw));
//            Log.e("TAG", "userName>>>>>>>>>>>." + admin);
        }

        getVersion();           //版本号

    }

    /**
     * 版本号
     */
    protected String getVersion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info;
        String version = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        AppContext.version = version;
        return "版本号" + version;
    }

    protected void chooseLaunchStyle() {
        //屏幕尺寸这这里保存到AppContext供全局使用
        SharedPreferences sharedPreferences =
                getSharedPreferences(MainActivity.SETTING_PREFERENCES, MODE_PRIVATE);
        int style = sharedPreferences.getInt(MainActivity.LAUNCH_STYLE_KEY, MainActivity.LAUNCH_STYLE_SLIDE_MODE);
        switch (style) {
            case MainActivity.LAUNCH_STYLE_CIRCLE_MODE:
                startActivity(new Intent(this, CircleMenuActivity.class));
                finish();
                break;
            case MainActivity.LAUNCH_STYLE_SLIDE_MODE:
                startActivity(new Intent(this, NavigationMenuActivity.class));
                finish();
                break;
            case MainActivity.LAUNCH_STYLE_TAB_MODE:
                startActivity(new Intent(this, TabMenuActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 自动登录
     */
    protected StringRequest signInRequest(final String account, final String psw) {
        String url = BaseURLUtil.doSignIn(account, psw);
//        Log.e("TAG", " login in url:  " + url);

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("result").equals("N")) {

                        Toast.makeText(MainActivity.this, "登陆失败！", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("account", account);
                        editor.putString("password", psw);
                        editor.putString("now_user_id", "");
                        editor.putString("now_session_id", "");
                        editor.putString("user_type", "");

                        editor.apply();

                        AppContext.now_session_id = "";
                        AppContext.now_user_id = 0;
                        AppContext.now_user_name = "";
                        AppContext.user_type = "";

                    } else if (jsonObject.getString("result").equals("Y")) {

                        //登录成功
                        Toast.makeText(MainActivity.this, R.string.sign_in_ok, Toast.LENGTH_SHORT).show();
                        JSONObject listData = jsonObject.getJSONObject("datas").getJSONObject("listData");
                        int userId = listData.optInt("uid");
                        String userSId = listData.optString("sId");
                        String user_type = listData.optString("userType");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("account", account);
                        editor.putString("password", psw);
                        editor.putInt("now_user_id", userId);
                        editor.putString("now_session_id", userSId);
                        editor.putString("user_type", user_type);
                        editor.apply();

                        AppContext.now_session_id = userSId;
                        AppContext.now_user_id = userId;
                        AppContext.now_user_name = account;
                        AppContext.user_type = user_type;

                        mQueue.add(AppContext.getHeader(jsonObject.optString("headPic")));

                        MainActivity.this.finish();
                    }

                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(8 * 1000, 1, 1));
        return request;
    }

}
