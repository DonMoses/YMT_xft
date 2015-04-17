package com.ymt.demo1.main;

import android.support.v7.app.ActionBarActivity;

/**
 * Created by Dan on 2015/4/17
 */
public class BaseFloatActivity extends ActionBarActivity {

    @Override
    protected void onPause() {
        AppContext.removeFromAppContext(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        AppContext.addToAppContext(this);
        super.onResume();
    }

}
