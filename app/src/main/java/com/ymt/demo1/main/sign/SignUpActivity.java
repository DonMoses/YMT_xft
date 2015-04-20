package com.ymt.demo1.main.sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.dbTables.Account;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Moses on 2015
 */
public class SignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initTitle();
        initAccount();
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
    protected void initAccount() {

        //todo 账号输入栏失去焦点时，请求网络，判断是否可以注册。
        /*
        本地模拟
         */
        //初始化控件
        final EditText phoneTxt = (EditText) findViewById(R.id.input_phone_num);
        final EditText accountTxt = (EditText) findViewById(R.id.input_account);
        final EditText pswTxt = (EditText) findViewById(R.id.input_psw);
        final EditText rePswTxt = (EditText) findViewById(R.id.re_input_psw);

        //注册按钮事件
        Button signUpBtn = (Button) findViewById(R.id.do_sign_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            //四个输入组件的内容
            String phoneNum;
            String account;
            String psw;
            String rePsw;

            @Override
            public void onClick(View v) {
                //todo 获取网络账号密码信息
                //获取本地储存的账号信息
                List<Account> accountList = DataSupport.findAll(Account.class);
                phoneNum = phoneTxt.getText().toString();
                account = accountTxt.getText().toString();
                psw = pswTxt.getText().toString();
                rePsw = rePswTxt.getText().toString();

                if (accountList == null || accountList.size() == 0) {
                    jumpToSignIn(phoneNum, account, psw);
                } else {

                    for (int i = 0; i < accountList.size(); i++) {
                        if (accountList.get(i).getPhoneNum().equals(phoneNum)) {
                            Toast.makeText(SignUpActivity.this, "该手机号已经注册!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (accountList.get(i).getAccountName().equals(account)) {
                            Toast.makeText(SignUpActivity.this, "该用户名已经注册!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (!psw.equals(rePsw) || psw == null || psw.equals("")) {
                            Toast.makeText(SignUpActivity.this, "密码输入错误!", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        jumpToSignIn(phoneNum, account, psw);
                    }
                }

            }
        });
    }

    protected void jumpToSignIn(String phone, String account, String psw) {
        Account account1 = new Account();
        account1.setPhoneNum(phone);
        account1.setAccountName(account);
        account1.setPassword(psw);
        account1.save();
        Toast.makeText(SignUpActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("account", account);
        intent.putExtra("password", psw);
        setResult(RESULT_OK, intent);

        finish();
    }

}
