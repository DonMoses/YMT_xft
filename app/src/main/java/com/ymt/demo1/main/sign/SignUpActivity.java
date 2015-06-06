package com.ymt.demo1.main.sign;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.dbBeams.Account;
import com.ymt.demo1.customViews.MyTitle;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.tablemanager.Connector;

/**
 * Created by Moses on 2015
 */
public class SignUpActivity extends Activity {
    private RequestQueue queue;
    //&loginname=moses&pwd=123&phone=333&t=001(t表示用户类型，包括001，002，003，004)
    private boolean liscenseChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connector.getDatabase();
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_sign_up);
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

    /**
     * 初始化账号信息
     * 如果已经账号或手机号码已经注册，则提示已经注册，请登录或注册其他账号。
     */
    protected void initView() {

        //todo 账号输入栏失去焦点时，请求网络，判断是否可以注册。
        /*
        本地模拟
         */
        //初始化控件
        final EditText phoneTxt = (EditText) findViewById(R.id.input_phone_num);
        final EditText accountTxt = (EditText) findViewById(R.id.input_account);
        final EditText pswTxt = (EditText) findViewById(R.id.input_psw);
        final EditText rePswTxt = (EditText) findViewById(R.id.re_input_psw);
        final TextView licenseTxt = (TextView) findViewById(R.id.licence_txt);
        //todo 使用spannableString 设置富文本
        SpannableString spannableString = new SpannableString(getString(R.string.license));
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.material_blue_grey_800));
        spannableString.setSpan(span, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        licenseTxt.setText(spannableString);

        //注册按钮事件
        Button signUpBtn = (Button) findViewById(R.id.do_sign_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //todo 获取网络账号密码信息
                //四个输入组件的内容
                String phoneNum = phoneTxt.getText().toString();
                String account = accountTxt.getText().toString();
                String psw = pswTxt.getText().toString();
                String rePsw = rePswTxt.getText().toString();
                if (!TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(account) && !TextUtils.isEmpty(psw) && psw.equals(rePsw)) {
                    //输入正确，todo 请求注册
                    queue.add(signUpRequest(account, psw, phoneNum, Account.NORMAL_USER));
                } else if (TextUtils.isEmpty(phoneNum)) {
                    //提示手机号错误
                    Toast.makeText(SignUpActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(account)) {
                    //提示账号错误
                    Toast.makeText(SignUpActivity.this, "账号不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    //提示密码错误
                    Toast.makeText(SignUpActivity.this, "密码输入错误，请重新输入！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //switch(消防协议)
        ImageView checkView = (ImageView) findViewById(R.id.switch_liscense);
        if (liscenseChecked) {
            checkView.setImageResource(R.drawable.icon_checked);
        } else {
            checkView.setImageResource(R.drawable.icon_unchecked);
        }
        checkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liscenseChecked = !liscenseChecked;
            }
        });
    }

    protected void jumpToSignIn(String phone, String account, String userId, String psw, String userType) {
        Account account1 = new Account();
        account1.setPhoneNum(phone);
        account1.setAccountName(account);
        account1.setPassword(psw);
        account1.setUserId(userId);
        account1.setUserType(userType);
        account1.save();
        Toast.makeText(SignUpActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("account", account);
        intent.putExtra("password", psw);
        setResult(RESULT_OK, intent);

        finish();
    }

    /**
     * 发起请求 注册
     *
     * @param account   ：sign account
     * @param psw       : sign password
     * @param phone     : sign phone_num
     * @param user_type : sign type (normal_user,export_user,manager_user...)
     * @return : VOLLEY Request
     */
    protected StringRequest signUpRequest(final String account, final String psw, final String phone, final String user_type) {
        String loginBaseUrl = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.RegAction";
        String url = loginBaseUrl + "&loginname=" + account + "&pwd=" + psw + "&phone=" + phone + "&t=" + user_type;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        //sign successfully
                        jumpToSignIn(phone, account, jsonObject.getString("user_id"), psw, user_type);
                    } else {
                        //sign unsuccessfully
                        Toast.makeText(SignUpActivity.this, jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
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
        return request;
    }


}
