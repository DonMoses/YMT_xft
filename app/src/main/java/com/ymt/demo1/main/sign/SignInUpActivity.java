package com.ymt.demo1.main.sign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.customViews.SignSwitchView;
import com.ymt.demo1.launchpages.MainActivity;
import com.ymt.demo1.mainStyles.CircleMenuActivity;
import com.ymt.demo1.mainStyles.NavigationMenuActivity;
import com.ymt.demo1.mainStyles.TabMenuActivity;

/**
 * Created by DonMoses on 2015/8/27
 */
public class SignInUpActivity extends BaseFloatActivity implements SignSwitchView.SignSwitchListener {
    private boolean isBackToMain;
    public boolean isSigned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isBackToMain = getIntent().getBooleanExtra("is_back_to_main", true);
        setContentView(R.layout.activity_sign_in_up);
        initBack();
        initView();
    }

    /**
     * 返回按钮
     */
    private void initBack() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    private void initView() {
        SignSwitchView switchView = (SignSwitchView) findViewById(R.id.sign_layout);
        //todo 这里注册切换的监听
        switchView.setSwitchListener(this);
        setSelect(0);
    }

    public void setSelect(int index) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        switch (index) {
            case 0:
                transaction.replace(R.id.content, new SignInFragment(), SignInFragment.FLAG);
                break;
            case 1:
                transaction.replace(R.id.content, new SignUpFragment(), SignUpFragment.FLAG);
                break;
            default:
                break;
        }
        transaction.commit();

    }

    @Override
    public void isSignIn(boolean isSignIn) {
        if (isSignIn) {
            setSelect(0);
        } else {
            setSelect(1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBackToMain) {
            chooseLaunchStyle();
        } else {
            if (isSigned) {
                setResult(RESULT_OK);
            } else {
                setResult(RESULT_CANCELED);
            }
        }
    }

    protected void chooseLaunchStyle() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(MainActivity.SETTING_PREFERENCES, Context.MODE_PRIVATE);
        int style = sharedPreferences.getInt(MainActivity.LAUNCH_STYLE_KEY, MainActivity.LAUNCH_STYLE_SLIDE_MODE);
        switch (style) {
            case MainActivity.LAUNCH_STYLE_CIRCLE_MODE:
                startActivity(new Intent(SignInUpActivity.this, CircleMenuActivity.class));
                finish();
                break;
            case MainActivity.LAUNCH_STYLE_SLIDE_MODE:
                startActivity(new Intent(SignInUpActivity.this, NavigationMenuActivity.class));
                finish();
                break;
            case MainActivity.LAUNCH_STYLE_TAB_MODE:
                startActivity(new Intent(SignInUpActivity.this, TabMenuActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
