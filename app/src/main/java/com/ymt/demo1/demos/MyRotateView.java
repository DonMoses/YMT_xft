package com.ymt.demo1.demos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import com.ymt.demo1.R;

/**
 * Created by Dan on 2015/4/21
 */
public class MyRotateView extends View {

    private Bitmap bitmap;
    private int degreeL = 0;
    private Matrix matrix;

    public MyRotateView(Context context) {
        super(context);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.central_logo);
        matrix = new Matrix();
    }

    public MyRotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.central_logo);
        matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        matrix.setRotate(degreeL++, 160, 240);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(bitmap, 88, 169, null);
        //不停的刷新，不断调用onDraw();
        invalidate();

    }
}
