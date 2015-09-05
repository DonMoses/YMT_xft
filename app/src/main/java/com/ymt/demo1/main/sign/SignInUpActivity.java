package com.ymt.demo1.main.sign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.customViews.SignSwitchView;
import com.ymt.demo1.customViews.UnScrollableViewPager;

/**
 * Created by DonMoses on 2015/8/27
 */
public class SignInUpActivity extends BaseFloatActivity implements SignSwitchView.SignSwitchListener {
    private UnScrollableViewPager contentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        contentPager = (UnScrollableViewPager) findViewById(R.id.content_pager);
        SignSwitchView switchView = (SignSwitchView) findViewById(R.id.sign_layout);
        //todo 这里注册切换的监听
        switchView.setSwitchListener(this);
        FragmentManager fm = getSupportFragmentManager();
        SignFragmentAdapter adapter = new SignFragmentAdapter(fm);
        contentPager.setAdapter(adapter);
    }

    @Override
    public void isSignIn(boolean isSignIn) {
        if (isSignIn) {
            contentPager.setCurrentItem(0);
        } else {
            contentPager.setCurrentItem(1);
        }
    }

    class SignFragmentAdapter extends FragmentPagerAdapter {

        public SignFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new SignInFragment();
            } else {
                return new SignUpFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
