package com.ymt.demo1.customViews.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class MyScaleImageView extends ImageView {

    private static final String TAG = "MyImageView";

    private static final int SCALE_REDUCE_INIT = 0;
    private static final int SCALING = 1;
    private static final int SCALE_ADD_INIT = 6;

    /**
     * 控件的宽
     */
    private int mWidth;
    /**
     * 控件的高
     */
    private int mHeight;
    /**
     * 控件的宽1/2
     */
    private int mCenterWidth;
    /**
     * 控件的高 1/2
     */
    private int mCenterHeight;
    /**
     * 设置一个缩放的常量
     */
    private float mMinScale = 0.85f;
    /**
     * 缩放是否结束
     */
    private boolean isFinish = true;

    public MyScaleImageView(Context context) {
        this(context, null);
    }

    public MyScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 必要的初始化
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            mHeight = getHeight() - getPaddingTop() - getPaddingBottom();

            mCenterWidth = mWidth / 2;
            mCenterHeight = mHeight / 2;

            Drawable drawable = getDrawable();
            BitmapDrawable bd = (BitmapDrawable) drawable;
            bd.setAntiAlias(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float X = event.getX();
                float Y = event.getY();
                mScaleHandler.sendEmptyMessage(SCALE_REDUCE_INIT);
                break;
            case MotionEvent.ACTION_UP:
                mScaleHandler.sendEmptyMessage(SCALE_ADD_INIT);
                break;
        }
        return true;
    }

    /**
     * 控制缩放的Handler
     */
    private ScaleHandler mScaleHandler = new ScaleHandler(this);

    protected void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缩放
     */
    private synchronized void beginScale(Matrix matrix, float scale) {
        matrix.postScale(scale, scale, mCenterWidth, mCenterHeight);
        setImageMatrix(matrix);
    }

    /**
     * 回调接口
     */
    private OnViewClickListener mOnViewClickListener;

    public void setOnClickIntent(OnViewClickListener onViewClickListener) {
        this.mOnViewClickListener = onViewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(MyScaleImageView view);
    }


    protected static class ScaleHandler extends Handler {
        private Matrix matrix = new Matrix();
        private int count = 0;
        private float s;
        /**
         * 是否已经调用了点击事件
         */
        private boolean isClicked;

        private WeakReference<MyScaleImageView> myScaleImageViewWeakReference;

        public ScaleHandler(MyScaleImageView myScaleImageView) {
            myScaleImageViewWeakReference = new WeakReference<>(myScaleImageView);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyScaleImageView myScaleImageView = myScaleImageViewWeakReference.get();
            if (myScaleImageView != null) {
                matrix.set(myScaleImageView.getImageMatrix());
                switch (msg.what) {
                    case SCALE_REDUCE_INIT:
                        if (!myScaleImageView.isFinish) {
                            myScaleImageView.mScaleHandler.sendEmptyMessage(SCALE_REDUCE_INIT);
                        } else {
                            myScaleImageView.isFinish = false;
                            count = 0;
                            s = (float) Math.sqrt(Math.sqrt(myScaleImageView.mMinScale));
                            myScaleImageView.beginScale(matrix, s);
                            myScaleImageView.mScaleHandler.sendEmptyMessage(SCALING);
                        }
                        break;
                    case SCALING:
                        myScaleImageView.beginScale(matrix, s);
                        if (count < 4) {
                            myScaleImageView.mScaleHandler.sendEmptyMessage(SCALING);
                        } else {
                            myScaleImageView.isFinish = true;
                            if (myScaleImageView.mOnViewClickListener != null && !isClicked) {
                                isClicked = true;
                                myScaleImageView.mOnViewClickListener.onViewClick(myScaleImageView);
                            } else {
                                isClicked = false;
                            }
                        }
                        count++;

                        break;
                    case 6:
                        if (!myScaleImageView.isFinish) {
                            myScaleImageView.mScaleHandler.sendEmptyMessage(SCALE_ADD_INIT);
                        } else {
                            myScaleImageView.isFinish = false;
                            count = 0;
                            s = (float) Math.sqrt(Math.sqrt(1.0f / myScaleImageView.mMinScale));
                            myScaleImageView.beginScale(matrix, s);
                            myScaleImageView.mScaleHandler.sendEmptyMessage(SCALING);
                        }
                        break;
                }
            }

        }
    }

}
