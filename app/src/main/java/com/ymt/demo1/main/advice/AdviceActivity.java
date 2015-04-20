package com.ymt.demo1.main.advice;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by Moses on 2015
 */
public class AdviceActivity extends Activity {
    private int widthEdit;
    private int widthPro;
    private ProgressBar contextProBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        initView();
        initTitle();
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
        final Button subAdviceBtn = (Button) findViewById(R.id.do_sub_btn);
        final EditText editAdviceTxt = (EditText) findViewById(R.id.advice_edit_text);

         /*
                    测量控件大小，计算偏移以确定位置。
                     */
        ViewTreeObserver vtoEdit = editAdviceTxt.getViewTreeObserver();

        vtoEdit.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                            /*
                            这个方法，我们需要注册一个ViewTreeObserver的监听回调，这个监听回调，就是专门监听绘图的，
                            既然是监听绘图，那么我们自然可以获取测量值了，同时，我们在每次监听前remove前一次的监听，避免重复监听。
                             */
                editAdviceTxt.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                widthEdit = editAdviceTxt.getWidth();

            }
        });

        /*
        提交建议
         */
        subAdviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adviceTxt = editAdviceTxt.getText().toString();
                if (adviceTxt != null && !adviceTxt.equals("")) {
                    //模拟将建议发送到服务器，然后接受服务器返回结果。
                    final PopupWindow popupWindow = new PopupWindow(AdviceActivity.this);
                    contextProBar = new ProgressBar(AdviceActivity.this);
                    ViewTreeObserver vtoPro = contextProBar.getViewTreeObserver();

                    vtoPro.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            /*
                            这个方法，我们需要注册一个ViewTreeObserver的监听回调，这个监听回调，就是专门监听绘图的，
                            既然是监听绘图，那么我们自然可以获取测量值了，同时，我们在每次监听前remove前一次的监听，避免重复监听。
                             */
                            contextProBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            widthPro = contextProBar.getWidth();

                        }
                    });

                    contextProBar.setIndeterminate(true);
                    popupWindow.setContentView(contextProBar);
                    popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                    popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                    //计算x 方向偏移
                    int offsetX = (widthEdit - widthPro) / 2;
                    popupWindow.showAsDropDown(editAdviceTxt, offsetX, 0);
                    editAdviceTxt.setText("");
                    editAdviceTxt.clearFocus();

                    /*
                    模拟成功上传建议到服务器
                     */
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            popupWindow.dismiss();
                            Toast.makeText(AdviceActivity.this, "Done!Thanks for your advising.", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);

                } else {
                    Toast.makeText(AdviceActivity.this, "please enter you advice here then press the send button!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
