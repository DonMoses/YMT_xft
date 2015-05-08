package com.ymt.demo1.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymt.demo1.R;

/**
 * Created by Dan on 2015/4/17
 * 创建自定义title ， 左边为返回按钮， 中间为title文字，右边为search 和action 按钮
 */
public class MyTitle extends LinearLayout {
    /*
    title监听事件
     */
    private OnLeftActionClickListener onLeftActionClickListener;
    private OnRightActionClickListener onRightActionClickListener;
    /*
     容器
     */
    private LinearLayout leftBackLayout;
    private LinearLayout rightActionLayout;
    /*
    title
     */
    private TextView centerTitleTxt;

    /*
     参数
     */
    private Drawable leftActIcon;
    private String centerTitle;
    private Drawable rightActIconL;
    private Drawable rightActIconR;

    /*
     用于标记title类型的向量
     */
    public enum TitleStyle {
        NO_ICON, LEFT_ICON, RIGHT_ICON_L, RIGHT_ICON_L_R
    }

    public MyTitle(Context context) {
        super(context);
        init(context);
    }

    public MyTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        //从xml 布局文件中读取属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyTitle);
        leftActIcon = a.getDrawable(R.styleable.MyTitle_setLeftActionIcon);
        centerTitle = a.getString(R.styleable.MyTitle_setTitleTxt);
        rightActIconL = a.getDrawable(R.styleable.MyTitle_setRightIconL);
        rightActIconR = a.getDrawable(R.styleable.MyTitle_setRightActIconR);

        init(context);
        a.recycle();
    }

    /**
     * 初始化自定义控件
     */
    public void init(Context context) {
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);
        initChildLayout(context);
    }

    /**
     * 设置title类型
     */
    public void setTitleStyle(TitleStyle style) {
        switch (style) {
            case NO_ICON:                   //无按钮样式
                setNoIconTitle();
                break;
            case LEFT_ICON:                 //左按钮样式
                setLeftTitle();
                break;
            case RIGHT_ICON_L:              //左按钮+右按钮1样式
                setLeftRightL();
                break;
            case RIGHT_ICON_L_R:            //默认情况下（左 + 右1 + 右2）样式
            default:
                break;

        }

    }

    /**
     * 当只有中间title文字，没有其他任何按钮时
     */
    protected void setNoIconTitle() {
        leftBackLayout.removeAllViews();
        rightActionLayout.removeAllViews();
        this.removeView(leftBackLayout);
        this.removeView(rightActionLayout);
    }

    /**
     * 左边icon  + 中间title
     */
    protected void setLeftTitle() {
        rightActionLayout.removeAllViews();
        rightActionLayout.setVisibility(INVISIBLE);
    }

    /**
     * 左边icon + 中间title + 右边iconL（左）
     */
    protected void setLeftRightL() {
        if (rightActionLayout.getChildCount() > 1) {
            rightActionLayout.removeViewAt(1);          //移除右边第二个按钮
        }

    }

    /**
     * 初始化各个child 布局
     */
    public void initChildLayout(final Context context) {
        /*
        初始化左边back布局
         */
        leftBackLayout = new LinearLayout(context);
        leftBackLayout.setLayoutParams(
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1));

        leftBackLayout.setGravity(Gravity.CENTER);
        if (leftActIcon != null) {
            ImageView leftIcon = new ImageView(context);
            leftIcon.setImageDrawable(leftActIcon);
            leftBackLayout.addView(leftIcon);
        }

        //设置监听
        leftBackLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLeftActionClickListener != null) {
                    //回调方法，调用onLeftActionClickListener接口实现类的方法
                    onLeftActionClickListener.onClick();
                }
            }
        });
        addView(leftBackLayout);

        /**
         * 初始化中间title文字
         */
        centerTitleTxt = new TextView(context);
        centerTitleTxt.setLayoutParams(
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 8));
        centerTitleTxt.setText(centerTitle);
        centerTitleTxt.setTextColor(Color.WHITE);
        centerTitleTxt.setSingleLine(true);
        centerTitleTxt.setMaxEms(6);
        centerTitleTxt.setEllipsize(TextUtils.TruncateAt.END);
        centerTitleTxt.setGravity(Gravity.CENTER);
        addView(centerTitleTxt);

        /**
         *
         */
        rightActionLayout = new LinearLayout(context);
        rightActionLayout.setLayoutParams(
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1));

        rightActionLayout.setOrientation(HORIZONTAL);
        rightActionLayout.setGravity(Gravity.CENTER);
        if (rightActIconL != null) {
            ImageView rightIconL = new ImageView(context);
            rightIconL.setImageDrawable(rightActIconL);
            rightActionLayout.addView(rightIconL);
            rightIconL.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //回调方法，调用onRightActionClickListener接口实现类的方法
                    if (onRightActionClickListener != null) {
                        onRightActionClickListener.onRightLClick();
                    }
                }
            });
        }

        if (rightActIconR != null) {
            ImageView rightIconR = new ImageView(context);
            rightIconR.setImageDrawable(rightActIconR);
            rightActionLayout.addView(rightIconR);
            rightIconR.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //回调方法，调用onRightActionClickListener接口实现类的方法
                    if (onRightActionClickListener != null) {
                        onRightActionClickListener.onRightRClick();
                    }
                }
            });
        }

        addView(rightActionLayout);

    }

    /**
     * 左键监听
     */
    public interface OnLeftActionClickListener {
        void onClick();
    }

    /**
     * 右键监听
     */
    public interface OnRightActionClickListener {
        void onRightLClick();

        void onRightRClick();
    }

    public void setOnLeftActionClickListener(OnLeftActionClickListener onLeftActionClickListener) {
        this.onLeftActionClickListener = onLeftActionClickListener;
    }

    public void setOnRightActionClickListener(OnRightActionClickListener onRightActionClickListener) {
        this.onRightActionClickListener = onRightActionClickListener;
    }


    public Drawable getRightActIconR() {
        return rightActIconR;
    }

    public void setRightActIconR(Drawable rightActIconR) {
        this.rightActIconR = rightActIconR;
    }

    public Drawable getLeftActIcon() {
        return leftActIcon;
    }

    public void setLeftActIcon(Drawable leftActIcon) {
        this.leftActIcon = leftActIcon;
    }

    public String getCenterTitle() {
        return centerTitle;
    }

    public void setCenterTitle(String centerTitle) {
        this.centerTitle = centerTitle;
    }

    public void updateCenterTitle(String centerTitle) {
        centerTitleTxt.setText(centerTitle);
        invalidate();
    }

    public Drawable getRightActIconL() {
        return rightActIconL;
    }

    public void setRightActIconL(Drawable rightActIconL) {
        this.rightActIconL = rightActIconL;
    }
}