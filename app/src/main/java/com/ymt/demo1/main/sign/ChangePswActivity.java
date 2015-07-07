package com.ymt.demo1.main.sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.tablemanager.Connector;


/**
 * Created by Moses on 2015
 */
public class ChangePswActivity extends Activity {
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connector.getDatabase();
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
                        Intent intent = new Intent(ChangePswActivity.this, SignInActivity.class);
                        intent.putExtra("change", true);
                        intent.putExtra("loginName", loginName);
                        intent.putExtra("psw", newPsw);
                        setResult(RESULT_OK, intent);
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


}
