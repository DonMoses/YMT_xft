package com.ymt.demo1.launchpages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.mainStyles.CircleMenuActivity;
import com.ymt.demo1.mainStyles.NavigationMenuActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dan on 2015/5/18
 * 加载界面。 根据sharedPreference中保存的设置来启动到相应主界面
 */
public class LoadingPageActivity extends Activity {
    private SharedPreferences sharedPreferences;
    private TextView versionTxt;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connector.getDatabase();
        setContentView(R.layout.activity_loading_page);
        sharedPreferences = AppContext.getSaveAccountPrefecences(this);
        mQueue = Volley.newRequestQueue(this);

        //获取账号密码信息自动登录
        String admin = sharedPreferences.getString("account", "");
        String psw = sharedPreferences.getString("password", "");
        if (!(TextUtils.isEmpty(admin) || TextUtils.isEmpty(psw))) {
            //将等路请求加入Volley队列
            mQueue.add(signInRequest(admin, psw));
//            Log.e("TAG", "userName>>>>>>>>>>>." + admin);
        } else {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    chooseLaunchStyle();
                }
            }, 2000);
        }

        versionTxt = (TextView) findViewById(R.id.loading_version_text);
        versionTxt.setText(getVersion());

    }

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
        return "版本号" + version;
    }

    protected void chooseLaunchStyle() {
        //屏幕尺寸这这里保存到AppContext供全局使用
        AppContext.screenWidth = versionTxt.getRootView().getWidth();

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

    /**
     * 自动登录
     */
    protected StringRequest signInRequest(final String userName, final String psw) {
        String url = BaseURLUtil.doSignIn(userName, psw);

        return new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        //登录成功
                        Toast.makeText(LoadingPageActivity.this, R.string.sign_in_ok, Toast.LENGTH_SHORT).show();
                        //将账号Account信息加入到全局Application中
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("account", userName);
                        editor.putString("password", psw);
                        editor.putString("now_user_id", jsonObject.getString("id"));
                        editor.putString("now_session_id", jsonObject.getString("sId"));
                        editor.apply();

                         /*
                        如果更改账号登录，则删除前一账号的QQ信息
                         */
                        String savedUserId = sharedPreferences.getString("now_user_id", "");
                        if ((!TextUtils.isEmpty(savedUserId)) && (!savedUserId.equals(jsonObject.getString("id")))) {
                            DataSupport.deleteAll(QQChatInfo.class);
                            Log.e("TAG", "do deleteAll>>>>>>>QQ>>>>>>>>>>");
                        }

                        AppContext.now_session_id = jsonObject.getString("sId");
                        AppContext.now_user_id = jsonObject.getString("id");
                        AppContext.now_user_name = userName;
                        Intent intent = new Intent(LoadingPageActivity.this, QQUnreadMsgService.class);
                        startService(intent);

                        mQueue.add(AppContext.getHeader(jsonObject.optString("headPic")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        chooseLaunchStyle();        //成功登录，跳转到主界面
                    }
                }).start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        chooseLaunchStyle();        //未成功登录，仍调到主界面
                    }
                }).start();
            }
        });
    }


}
