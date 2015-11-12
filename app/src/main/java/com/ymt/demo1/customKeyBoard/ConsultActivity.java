package com.ymt.demo1.customKeyBoard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.search.FullSearchActivity;

/**
 * Created by Dan on 2015/4/17
 */
public class ConsultActivity extends BaseActivity {
    public EditText mInputText;
    private ImeKeyBoardUtil imeKeyBoardUtil;
    private InputMethodManager inputMethodManager;
    private ImeLayout imeLayout;
    private AScreenSizeUtil AScreenSizeUtil;
    private int tempLess = 0;
    private int tempMuch = 0;
    private int screenHeight;
    private int heightDifference;
    private boolean isPop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imeLayout = (ImeLayout) findViewById(R.id.root_ime_layout);
        imeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                Rect r = new Rect();
                imeLayout.getWindowVisibleDisplayFrame(r);
                screenHeight = imeLayout.getRootView().getHeight();

                heightDifference = screenHeight - (r.bottom - r.top);
                //heightDifference的较小值表示状态栏 + 标题栏高度，heightDifference的(较大值 - 较小值)表示弹出软键盘高度
                if (tempLess == 0) {
                    tempLess = heightDifference;        //较小值
                } else if (tempMuch == 0 && tempLess < heightDifference) {
                    tempMuch = heightDifference;        //较大值
                }

                int screenContentBarHeight = tempLess;
                AScreenSizeUtil = com.ymt.demo1.customKeyBoard.AScreenSizeUtil
                        .getInstance(ConsultActivity.this, screenHeight, screenContentBarHeight);
                AScreenSizeUtil.initSize();
            }
        });

        initTitle();
        initInputText();
        initEmoJInputView();
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
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
                startActivity(new Intent(ConsultActivity.this, FullSearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                //todo 设置按钮Action
                Toast.makeText(ConsultActivity.this, "设置按钮Action", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化输入框
     */
    protected void initInputText() {
        mInputText = (EditText) findViewById(R.id.input_edit_text);
        //输入框 ， 根据输入框高度动态该表父布局高度
        final LinearLayout inputParent = (LinearLayout) findViewById(R.id.input_parent);
        inputListener(inputParent, mInputText);           //输入监听
    }

    /**
     * 点击监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onHideSoftInput(event);
        return super.onTouchEvent(event);
    }

    /**
     * 点击空白处,关闭软键盘和pop键盘
     */
    public void onHideSoftInput(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            hidePopAndResetLayout();
            isPop = false;
        }
    }

    protected void showPopAndSetLayout() {
        imeLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                tempMuch - tempLess + AScreenSizeUtil.getTitleBarHeight()));
        imeKeyBoardUtil.showPopKeyBoard(
                tempMuch - tempLess, AScreenSizeUtil.getTitleBarHeight());
    }

    protected void hidePopAndResetLayout() {
        imeKeyBoardUtil.hidePopKeyBoard();
        imeLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        imeLayout.setBottom(AScreenSizeUtil.getTitleBarHeight());
    }

    protected void initEmoJInputView() {
        imeKeyBoardUtil = ImeKeyBoardUtil.getInstance(this, mInputText.getRootView());

        /*
        todo 这里应该重新梳理打开、关闭逻辑
         */
        final ImageView imageView = (ImageView) findViewById(R.id.change_btw_sys_emoJ);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputText.setFocusable(true);
                mInputText.requestFocus();
                /*
                图标背景变化
                 */
                if (isPop) {
                    imageView.setBackgroundColor(getResources().getColor(android.R.color.white));
                } else {
                    imageView.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                }
                /*
                ①输入栏在底部，则点击^_^图标时
                                    【设置底层布局到需要高度 - 弹出pop键盘】
                ②（输入栏不在底部）当前系统软键盘正在显示，则点击^_^图标时
                                【关闭软键盘 - 设置底层布局到需要高度 - 弹出pop键盘】
                ③（输入栏不在底部）当前系统软键盘没有显示（显示的是pop键盘），则点击^_^时
                            【关闭pop键盘 - 打开软键盘 - 设置底层布局到适应父类】
                 */
                if (imeLayout.getBottom() > AScreenSizeUtil.getTitleBarHeight() && !isPop) {
                    //
                    showPopAndSetLayout();
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);

                } else if (imeLayout.getBottom() > AScreenSizeUtil.getTitleBarHeight() && isPop) {
                    //关闭pop键盘，重新布局
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    hidePopAndResetLayout();
                } else if (imeLayout.getBottom() <= AScreenSizeUtil.getTitleBarHeight() && !isPop) {
                    //显示pop键盘，重新布局
                    showPopAndSetLayout();
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                }

                isPop = !isPop;
            }
        });

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
                parent.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT
                        , (int) (inputTxt.getHeight() + inputTxt.getTextSize())));

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
