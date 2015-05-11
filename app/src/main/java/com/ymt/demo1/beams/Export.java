package com.ymt.demo1.beams;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/5/11
 */
public class Export extends DataSupport {
    private Bitmap icon;
    private String birthDay;
    private String name;
    private String major;

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
