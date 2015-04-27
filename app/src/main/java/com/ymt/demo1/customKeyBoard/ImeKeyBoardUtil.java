package com.ymt.demo1.customKeyBoard;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.ymt.demo1.R;

/**
 * Created by Dan on 2015/4/23
 */
public class ImeKeyBoardUtil {
    private static ImeKeyBoardUtil imeKeyBoardUtil = null;
    private LayoutInflater inflater;
    private ConsultActivity mActivity;
    private View inputLayout;
    //底层视图
    private FrameLayout rootView;
    //弹出视图
    private View popContent;

    private ImeKeyBoardUtil(Context context, View inputLayout) {
        inflater = LayoutInflater.from(context);
        this.mActivity = (ConsultActivity) context;
        this.inputLayout = inputLayout;
        initPopContent();
    }

    public static ImeKeyBoardUtil getInstance(Context context, View inputLayout) {
        if (imeKeyBoardUtil == null) {
            imeKeyBoardUtil = new ImeKeyBoardUtil(context, inputLayout);
        }
        return imeKeyBoardUtil;
    }

    protected void initPopContent() {
        //弹出View
        popContent = inflater.inflate(R.layout.layout_ime_pop_keyboard, null);
        //加入表情fragment
        PopEmoJUtil popEmoJUtil = PopEmoJUtil.getInstance(mActivity);
        popEmoJUtil.doAdapter(popContent);
        rootView = (FrameLayout) inputLayout.getRootView();
    }

    public void showPopKeyBoard(int height, int titleBarHeight) {
        rootView.removeView(popContent);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, height, Gravity.BOTTOM);
        layoutParams.bottomMargin = titleBarHeight;
        //向底层视图中加入view(弹出键盘)
        rootView.addView(popContent, layoutParams);
    }

    public void hidePopKeyBoard() {
        rootView.removeView(popContent);

    }

}
