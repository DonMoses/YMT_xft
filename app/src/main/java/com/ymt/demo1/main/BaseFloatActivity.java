package com.ymt.demo1.main;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();           //默认不现实actionBar
    }

    @Override
    protected void onDestroy() {
        AppContext.removeFromAppContext(this);
        super.onDestroy();
    }
}
