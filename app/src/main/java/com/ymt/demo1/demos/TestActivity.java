package com.ymt.demo1.demos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;


/**
 * Created by Dan on 2015/4/28
 */
public class TestActivity extends BaseActivity {
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        view = findViewById(R.id.test_view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void initView() {

        float x1 = view.getX();
        float y1 = view.getY();
        Log.e("TAG1", "view.getX() = " + x1 + "    view.getY() = " + y1);

        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        int x2 = locations[0];
        int y2 = locations[1];
        Log.e("TAG1", "locations[0] = " + x2 + "    locations[1] = " + y2);

        int w1 = view.getWidth();
        int h1 = view.getHeight();
        Log.e("TAG1", "view.getWidth() = " + w1 + "    view.getHeight() = " + h1);

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        int w2 = view.getMeasuredWidth();
        int h2 = view.getMeasuredHeight();
        Log.e("TAG1", "view.getMeasuredWidth() = " + w2 + "    view.getMeasuredHeight() = " + h2);

    }

}
