package com.ymt.demo1.beams;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dan on 2015/5/11
 */
public class Export extends DataSupport implements Serializable, Parcelable {
    private String id;
    private Bitmap icon;
    private String birthDay;
    private String name;
    private String major;
    private boolean isFollowed;
    /**
     * 个人简介
     */
    private String selfResume;
    /**
     * 团队简介
     */
    private String teamResume;
    /**
     * 最近问题
     */
    private ArrayList<String> recentQuests;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

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

    public String getSelfResume() {
        return selfResume;
    }

    public void setSelfResume(String selfResume) {
        this.selfResume = selfResume;
    }

    public String getTeamResume() {
        return teamResume;
    }

    public void setTeamResume(String teamResume) {
        this.teamResume = teamResume;
    }

    public ArrayList<String> getRecentQuests() {
        return recentQuests;
    }

    public void setRecentQuests(ArrayList<String> recentQuests) {
        this.recentQuests = recentQuests;
    }

    public Export() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.icon, 0);
        dest.writeString(this.birthDay);
        dest.writeString(this.name);
        dest.writeString(this.major);
        dest.writeByte(isFollowed ? (byte) 1 : (byte) 0);
        dest.writeString(this.selfResume);
        dest.writeString(this.teamResume);
        dest.writeSerializable(this.recentQuests);
        dest.writeString(this.id);
    }

    private Export(Parcel in) {
        this.icon = in.readParcelable(Bitmap.class.getClassLoader());
        this.birthDay = in.readString();
        this.name = in.readString();
        this.major = in.readString();
        this.isFollowed = in.readByte() != 0;
        this.selfResume = in.readString();
        this.teamResume = in.readString();
        this.recentQuests = (ArrayList<String>) in.readSerializable();
        this.id = in.readString();
    }

    public static final Creator<Export> CREATOR = new Creator<Export>() {
        public Export createFromParcel(Parcel source) {
            return new Export(source);
        }

        public Export[] newArray(int size) {
            return new Export[size];
        }
    };
}
