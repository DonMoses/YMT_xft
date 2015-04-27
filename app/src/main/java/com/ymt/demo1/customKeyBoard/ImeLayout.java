package com.ymt.demo1.customKeyBoard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Dan on 2015/4/23
 */
public class ImeLayout extends LinearLayout {

    public ImeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mListener != null) {
            mListener.OnResizeRelative(w, h, oldw, oldh);
        }
    }

    // 监听接口
    private OnResizeRelativeListener mListener;

    public interface OnResizeRelativeListener {
        void OnResizeRelative(int w, int h, int oldw, int oldh);
    }

    public void setOnResizeRelativeListener(OnResizeRelativeListener l) {
        mListener = l;
    }
}
