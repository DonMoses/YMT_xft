package com.ymt.demo1.beams.expert_consult;

import android.graphics.Bitmap;

import com.ymt.demo1.plates.news.NewsItemType;

/**
 * Created by Dan on 2015/4/29
 */
public class ConsultItem {
    private String title;
    private int img;
    private String contentTxt;
    private Bitmap bitmapIcon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getContentTxt() {
        return contentTxt;
    }

    public void setContentTxt(String contentTxt) {
        this.contentTxt = contentTxt;
    }

    public Bitmap getBitmapIcon() {
        return bitmapIcon;
    }

    public void setBitmapIcon(Bitmap bitmapIcon) {
        this.bitmapIcon = bitmapIcon;
    }
}
