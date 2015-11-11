package com.ymt.demo1.beams.edu;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/4/9
 * 试题信息
 */
public class PastExamItem extends DataSupport implements Parcelable {
    private String title;
    private String level;
    private String subjects;
    private int levelId;
    private String historyId;
    private int views;
    private String DATE;
    private String yuer;
    private String descs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getYuer() {
        return yuer;
    }

    public void setYuer(String yuer) {
        this.yuer = yuer;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.level);
        dest.writeString(this.subjects);
        dest.writeInt(this.levelId);
        dest.writeString(this.historyId);
        dest.writeInt(this.views);
        dest.writeString(this.DATE);
        dest.writeString(this.yuer);
        dest.writeString(this.descs);
    }

    public PastExamItem() {
    }

    protected PastExamItem(Parcel in) {
        this.title = in.readString();
        this.level = in.readString();
        this.subjects = in.readString();
        this.levelId = in.readInt();
        this.historyId = in.readString();
        this.views = in.readInt();
        this.DATE = in.readString();
        this.yuer = in.readString();
        this.descs = in.readString();
    }

    public static final Creator<PastExamItem> CREATOR = new Creator<PastExamItem>() {
        public PastExamItem createFromParcel(Parcel source) {
            return new PastExamItem(source);
        }

        public PastExamItem[] newArray(int size) {
            return new PastExamItem[size];
        }
    };
}
