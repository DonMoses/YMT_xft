package com.ymt.demo1.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.ymt.demo1.R;

import java.lang.ref.WeakReference;

/**
 * Created by Dan on 2015/6/29
 */
public class MyCheckView extends ImageView {

    private Drawable bg_icon;
    private Drawable check_icon;
    private boolean isChecked;

    public MyCheckView(Context context) {
        super(context);
        this.isChecked = false;
        initView();
    }


    public MyCheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isChecked = false;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCheckView);
        bg_icon = a.getDrawable(R.styleable.MyCheckView_backgroundIcon);
        check_icon = a.getDrawable(R.styleable.MyCheckView_checkIcon);
        a.recycle();
        initView();
    }

    /**
     * 初始化控件
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void initView() {
        this.setBackground(bg_icon);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAction(isChecked);
                isChecked = !isChecked;
//                Log.e("TAG", ">>>>>>>>>>>>>>>>>>>>>>.");
            }
        });
    }

    private MyHandler handler = new MyHandler(this);

    /**
     * 绘制中间的check图标
     */
    protected void checkAction(boolean checked) {
        if (checked) {
            //选中 ————> 不选中   【图标消失】

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int level = 10000;
                    while (level > 0) {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.arg1 = level;
                        handler.sendMessage(msg);
                        level = level - 50;
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            //不选中 ————> 选中   【图标显示】
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int tLevel = 10000;
                    int level = 0;
                    while (level < tLevel) {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.arg1 = level;
                        handler.sendMessage(msg);
                        level = level + 50;
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void updateCheckedStadu(int level) {
        final ClipDrawable drawable = new ClipDrawable(check_icon, Gravity.START, ClipDrawable.HORIZONTAL);
        drawable.setLevel(level);
        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);
        this.setImageBitmap(bitmap);
    }

    static class MyHandler extends Handler {
        private WeakReference<MyCheckView> reference;

        public MyHandler(MyCheckView myCheckView) {
            reference = new WeakReference<>(myCheckView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyCheckView myCheckView = reference.get();
            if (myCheckView != null) {
                switch (msg.what) {
                    case 0:
                        int level = msg.arg1;
                        myCheckView.updateCheckedStadu(level);
//                        Log.e("TAG", ">>>>>>>>handle level>>>>>>>>>>>>>>." + level);
                        break;
                }
            }
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public Drawable getBg_icon() {
        return bg_icon;
    }

    public void setBg_icon(Drawable bg_icon) {
        this.bg_icon = bg_icon;
    }

    public Drawable getCheck_icon() {
        return check_icon;
    }

    public void setCheck_icon(Drawable check_icon) {
        this.check_icon = check_icon;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
