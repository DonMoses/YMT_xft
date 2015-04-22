package com.ymt.demo1.demos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.SearchActivity;

/**
 * Created by Dan on 2015/4/17
 */
public class TestActivity extends Activity {

    private InputMethodManager inputMethodManager;
    protected enum INPUT_STYLE {
        TEXT, TAP_VOICE, EMO, MORE_CATO
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initTitle();
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                startActivity(new Intent(TestActivity.this, SearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                //todo 设置按钮Action
                Toast.makeText(TestActivity.this, "设置按钮Action", Toast.LENGTH_SHORT).show();
            }
        });

        final LinearLayout inputParent = (LinearLayout) findViewById(R.id.input_parent);
        final EditText inputTxt = (EditText) findViewById(R.id.input_edit_text);

        inputListener(inputParent, inputTxt);           //输入监听
    }

    /**
     * 输入框文字改变监听,动态设定外层布局高度
     */
    protected void inputListener(final LinearLayout parent, final EditText inputTxt) {

         /*
        editText文字改变监听
         */
        inputTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //动态设置外层布局高度
                parent.setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        (int) (inputTxt.getHeight() + inputTxt.getTextSize()),
                        Gravity.BOTTOM));

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 点击监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        onHideSoftInput(event);
        return super.onTouchEvent(event);
    }

    /**
     * 点击空白处,关闭输入法软键盘
     */
    public void onHideSoftInput(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null)
            {
                inputMethodManager.hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


}
