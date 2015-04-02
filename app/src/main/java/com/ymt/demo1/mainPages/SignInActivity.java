package com.ymt.demo1.mainPages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ymt.demo1.R;

/**
 * Created by Dan on 2015/4/2
 */
public class SignInActivity extends Activity {
    String account;
    String psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    protected void initView() {
        final Button signUpBtn = (Button) findViewById(R.id.jump_sign_up);
        final Button foundPswBtn = (Button) findViewById(R.id.jump_found_psw);
        final EditText accountETxt = (EditText) findViewById(R.id.sign_in_account_text);
        final EditText pswETxt = (EditText) findViewById(R.id.sign_in_psw_text);
        final Button signInBtn = (Button) findViewById(R.id.sign_in_btn);
        final ImageButton wechatBtn = (ImageButton) findViewById(R.id.sign_in_wechat);
        final ImageButton qqBtn = (ImageButton) findViewById(R.id.sign_in_qq);
        final ImageButton sinaBtn = (ImageButton) findViewById(R.id.sign_in_sina);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.jump_sign_up:
                        //跳转到注册界面
                        signUpBtn.setBackgroundColor(Color.BLUE);
                        signUpBtn.setTextColor(Color.WHITE);
                        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                        break;
                    case R.id.jump_found_psw:
                        //找回密码逻辑
                        foundPswBtn.setBackgroundColor(Color.BLUE);
                        foundPswBtn.setTextColor(Color.WHITE);

                        break;
                    case R.id.sign_in_btn:
                        /*获取用户名和密码，匹配则登录（跳转到个人界面），不匹配弹出提示框*/
                        account = accountETxt.getText().toString();
                        psw = pswETxt.getText().toString();

                    case R.id.sign_in_wechat:
                        //使用微信账号登录

                        break;
                    case R.id.sign_in_qq:
                        //使用qq账号登录

                        break;
                    case R.id.sign_in_sina:
                        //使用sina账号登录
                        
                        break;
                    default:
                        break;

                }
            }
        };

        signUpBtn.setOnClickListener(onClickListener);
        foundPswBtn.setOnClickListener(onClickListener);
        signInBtn.setOnClickListener(onClickListener);
        wechatBtn.setOnClickListener(onClickListener);
        qqBtn.setOnClickListener(onClickListener);
        sinaBtn.setOnClickListener(onClickListener);

    }
}
