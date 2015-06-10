package com.ymt.demo1.launchpages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.dbBeams.Account;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.mainStyles.CircleMenuActivity;
import com.ymt.demo1.mainStyles.NavigationMenuActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

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
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //从数据库获取账号密码信息自动登录
        Account account = DataSupport.find(Account.class, 0);
        if (account != null) {
            String userName = account.getLoginname();
            String psw = account.getPassword();
            //将等路请求加入Volley队列
            mQueue.add(signInRequest(userName, psw));
        } else {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    chooseLaunchStyle();
                }
            }, 2000);
        }

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

    /**
     * 自动登录
     */
    protected StringRequest signInRequest(final String userName, final String psw) {
        String loginBaseUrl = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.LoginAction";
        String url = loginBaseUrl + "&loginname=" + userName + "&pwd=" + psw + "&t=app";

        return new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {

                        //登录成功
                        Toast.makeText(LoadingPageActivity.this, R.string.sign_in_ok, Toast.LENGTH_SHORT).show();
                        //将账号Account信息加入到全局Application中
                        Account account = new Account();
                        account.setId(jsonObject.getString("id"));
                        account.setLoginname(jsonObject.getString("loginname"));
                        account.setHeadPic(jsonObject.getString("headPic"));
                        account.setRole(jsonObject.getString("role"));
                        account.setsId(jsonObject.getString("sId"));
                        account.setAttCount(jsonObject.getInt("attCount"));
                        account.setFansCount(jsonObject.getInt("fansCount"));
                        AppContext.myAccount = account;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                chooseLaunchStyle();        //成功登录，跳转到主界面
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                chooseLaunchStyle();        //未成功登录，仍调到主界面
            }
        });
    }

}
