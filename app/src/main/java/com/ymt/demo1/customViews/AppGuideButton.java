package com.ymt.demo1.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Dan on 2015/4/9
 */
public class AppGuideButton extends View {
    private int color;
    private String text;

    public AppGuideButton(Context context) {
        super(context);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AppGuideButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.color = attrs.getAttributeIntValue(4, Color.GRAY);
        this.text = attrs.getAttributeValue(3);
//        Log.e("TAG", "attr count>>>>>>" + attrs.getAttributeCount());

    }

    public AppGuideButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    Paint paint = new Paint();
    int widthSize;
    int heightSize;
    Canvas canvas;

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        this.canvas = canvas;
        RectF rectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());

        paint.setColor(color);
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, 12, 12, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(14 * getResources().getDisplayMetrics().density);
        float textWidth = paint.measureText(text);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float textHeight = fontMetrics.ascent + fontMetrics.descent;

        canvas.drawText(text, (getMeasuredWidth() - textWidth) / 2f, (getMeasuredHeight() - textHeight) / 2f, paint);

//        Log.e("TAG", "getMeasuredWidth>>>>>>  " + getMeasuredWidth() + "   getMeasuredHeight>>>>>  " + getMeasuredHeight());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //todo 这里存在一个问题，heightMeasureSpec的值为0， 目前不理解——需要进一步学习View的自定义。
//        Log.e("TAG", "widthMeasureSpec>>>>>>  " + widthMeasureSpec + "   heightMeasureSpec>>>>>  " + heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);     //根据mode 确定视图尺寸
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heighttMode = MeasureSpec.getMode(heightMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, 140);    //这里指定后，则在getMeasuredWidth() 和 getMeasuredHeight() 时分别获得到widthSize 和 120.

    }

}