package com.ymt.demo1.launchpages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connector.getDatabase();
        setContentView(R.layout.activity_loading_page);
        sharedPreferences = AppContext.getSaveAccountPrefecences(this);
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //获取账号密码信息自动登录
        AppContext.now_user_id = sharedPreferences.getString("now_user_id", "");
        AppContext.now_session_id = sharedPreferences.getString("now_session_id", "");
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
