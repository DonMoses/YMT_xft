package com.ymt.demo1.main.sign;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.dbTables.Account;
import com.ymt.demo1.mainStyles.CircleMenuActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Dan on 2015/4/2
 */
public class SignInActivity extends Activity {
    String account;
    String psw;
    private EditText accountETxt;
    private EditText pswETxt;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
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
        final Button signUpBtn = (Button) findViewById(R.id.jump_sign_up);
        final Button foundPswBtn = (Button) findViewById(R.id.jump_found_psw);
        accountETxt = (EditText) findViewById(R.id.sign_in_account_text);
        pswETxt = (EditText) findViewById(R.id.sign_in_psw_text);

          /*
        从sharedPreference获取保存到本地的账号信息
         */
        sharedPreferences = getSharedPreferences("saved_account", MODE_PRIVATE);
        String savedAccount = sharedPreferences.getString("account", "");
        String savedPsw = sharedPreferences.getString("password", "");
        accountETxt.setText(savedAccount);
        pswETxt.setText(savedPsw);

        //登录按钮
        final Button signInBtn = (Button) findViewById(R.id.sign_in_btn);
        final ImageButton wechatBtn = (ImageButton) findViewById(R.id.sign_in_wechat);
        final ImageButton qqBtn = (ImageButton) findViewById(R.id.sign_in_qq);
        final ImageButton sinaBtn = (ImageButton) findViewById(R.id.sign_in_sina);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 获取网络账号密码信息
                //获取本地储存的账号信息
                List<Account> accountList = DataSupport.findAll(Account.class);
                switch (v.getId()) {
                    case R.id.jump_sign_up:
                        //跳转到注册界面
                        startActivityForResult(new Intent(SignInActivity.this, SignUpActivity.class), 0);
                        break;
                    case R.id.jump_found_psw:
                        //找回密码逻辑
//                        foundPswBtn.setBackgroundColor(Color.BLUE);
//                        foundPswBtn.setTextColor(Color.WHITE);
                        startActivity(new Intent(SignInActivity.this, FoundPswActivity.class));
                        break;
                    case R.id.sign_in_btn:
                        /*获取用户名和密码，匹配则登录（跳转到个人界面），不匹配弹出提示框*/
                        account = accountETxt.getText().toString();
                        psw = pswETxt.getText().toString();
                        Account account1 = new Account();
                        account1.setAccountName(account);
                        account1.setPassword(psw);

                        if (accountList == null || accountList.size() == 0) {
                            Toast.makeText(SignInActivity.this, R.string.should_sign_up_1st, Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < accountList.size(); i++) {
                                if (accountList.get(i).getAccountName().equals(account) &&
                                        accountList.get(i).getPassword().equals(psw)) {
                                    Toast.makeText(SignInActivity.this, R.string.sign_in_ok, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignInActivity.this, CircleMenuActivity.class));
                                    finish();
                                    return;
                                }
                            }
                            Toast.makeText(SignInActivity.this, R.string.account_psw_wrong, Toast.LENGTH_SHORT).show();

                        }
                        break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //这里不能调用super的方法，如果调用则下面代码失效
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {      //从注册界面返回的账号和密码
                    accountETxt.setText(data.getStringExtra("account"));
                    pswETxt.setText(data.getStringExtra("password"));
                    Log.e("TAG", "account>>>" + data.getStringExtra("account"));
                    Log.e("TAG", "password>>>" + data.getStringExtra("password"));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        //保存成功的信息到本地sharedPreference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account", account);
        editor.putString("password", psw);
        editor.apply();
        super.onPause();
    }
}
