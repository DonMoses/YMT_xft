package com.ymt.demo1.main.sign;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;


/**
 * Created by Moses on 2015
 */
public class ChangePswActivity extends Activity {
    private RequestQueue queue;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connector.getDatabase();
        sharedPreferences = AppContext.getSaveAccountPrefecences(this);
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_change_psw);
        initTitle();
        initView();
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }

    protected void initView() {
        final EditText account = (EditText) findViewById(R.id.account);
        account.setText(getIntent().getStringExtra("loginName"));
        final EditText oldPswTxt = (EditText) findViewById(R.id.old_psw);
        final EditText newPswTxt = (EditText) findViewById(R.id.new_psw);
        final EditText reNewPswTxt = (EditText) findViewById(R.id.re_new_psw);
        Button subBtn = (Button) findViewById(R.id.do_change_btn);
        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPsw = oldPswTxt.getText().toString();
                String newPsw = newPswTxt.getText().toString();
                String reNewPsw = reNewPswTxt.getText().toString();
                if (oldPsw.equals("") || newPsw.equals("") || reNewPsw.equals("")) {
                    Toast.makeText(ChangePswActivity.this, "输入有误，请重新输入...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!reNewPsw.equals(newPsw)) {
                    Toast.makeText(ChangePswActivity.this, "新密码输入不一致，请重新输入...", Toast.LENGTH_SHORT).show();
                } else {
                    queue.add(changePswRequest(account.getText().toString(), oldPsw, newPsw));
                }
            }
        });
    }

    /**
     * 发起请求 修改密码
     */
    protected StringRequest changePswRequest(final String loginName, String oldPsw, final String newPsw) {
        return new StringRequest(BaseURLUtil.getChangePswUrl(loginName, oldPsw, newPsw), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", ">>>>>>>>>>>>>>.s>>" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String result = jsonObject.getString("result");
                    if (result.equals("Y")) {
                        //修改密码成功，跳转到登录界面
                        Toast.makeText(ChangePswActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();

                        //退出当前账号
                        //跳转到登录界面
                        sharedPreferences.edit().clear().apply();
                        AppContext.headerPic = null;
                        AppContext.now_user_name = "";
                        AppContext.now_user_id = "";
                        AppContext.now_session_id = "";
                        DataSupport.deleteAll(QQMsg.class);
                        DataSupport.deleteAll(QQChatInfo.class);
                        queue.add(signOutAction(AppContext.now_session_id));

                        //跳转登录界面
                        Intent intent = new Intent(ChangePswActivity.this, SignInActivity.class);
                        intent.putExtra("change", true);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("account", loginName);
                        editor.putString("password", newPsw);
                        editor.apply();

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);                //清楚前面所有Activity
                        startActivity(intent);
                        //结束本活动
                        finish();
                    } else {
                        //修改不成功，弹出提示框
                        Toast.makeText(ChangePswActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }


    /**
     * 退出账号操作
     */
    protected StringRequest signOutAction(String sId) {
        return new StringRequest(BaseURLUtil.signOutAct(sId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("TAG", ">>>>>>>>>..s>>." + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

}
