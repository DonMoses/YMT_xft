package com.ymt.demo1.utils;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by DonMoses on 2015/9/17 [用于图片剪切的类]
 */
public class BitmapCutUtil {
    //根据控件大小居中剪切图片
    public static Bitmap getBitmapCutByViewSize(View target, Bitmap bitmap) {
        Bitmap bitmap1;
        int viewW = target.getWidth();        //
        int viewH = target.getHeight();       //
        int imgW = bitmap.getWidth();           //
        int imgH = bitmap.getHeight();          //
        if ((float) viewW / viewH > (float) imgW / imgH) {        //基于宽
            bitmap1 = Bitmap.createBitmap(bitmap, 0, (imgH - imgW * viewH / viewW) / 2, imgW, imgW * viewH / viewW);
        } else {                                             //基于高
            bitmap1 = Bitmap.createBitmap(bitmap, (imgW - imgH * viewW / viewH) / 2, 0, imgH, imgH * viewW / viewH);
        }

        return bitmap1;
    }
}


/*
* 获得上面的bitmap
 ImageRequest request = new ImageRequest(urls, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Bitmap bitmap1 = BitmapCutUtil.getBitmapCutByViewSize(imgNewIII, bitmap);
                        imgNewIII.setImageBitmap(bitmap1);
                    }
                }, imgNewIII.getWidth(), imgNewIII.getHeight(), Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
 */
