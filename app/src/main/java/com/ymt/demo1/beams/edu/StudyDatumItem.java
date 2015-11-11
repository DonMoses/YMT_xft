package com.ymt.demo1.beams.edu;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Dan on 2015/5/22
 */
public class StudyDatumItem extends DataSupport implements Parcelable {
    private int downNum;
    private String title;
    private String level;
    private String subjects;
    private int levelId;
    private int views;
    private String historyId;
    private int score;
    private int replays;
    private String yuer;
    private String date;
    private String descs;

    public int getDownNum() {
        return downNum;
    }

    public void setDownNum(int downNum) {
        this.downNum = downNum;
    }

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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getReplays() {
        return replays;
    }

    public void setReplays(int replays) {
        this.replays = replays;
    }

    public String getYuer() {
        return yuer;
    }

    public void setYuer(String yuer) {
        this.yuer = yuer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        dest.writeInt(this.downNum);
        dest.writeString(this.title);
        dest.writeString(this.level);
        dest.writeString(this.subjects);
        dest.writeInt(this.levelId);
        dest.writeInt(this.views);
        dest.writeString(this.historyId);
        dest.writeInt(this.score);
        dest.writeInt(this.replays);
        dest.writeString(this.yuer);
        dest.writeString(this.date);
        dest.writeString(this.descs);
    }

    public StudyDatumItem() {
    }

    protected StudyDatumItem(Parcel in) {
        this.downNum = in.readInt();
        this.title = in.readString();
        this.level = in.readString();
        this.subjects = in.readString();
        this.levelId = in.readInt();
        this.views = in.readInt();
        this.historyId = in.readString();
        this.score = in.readInt();
        this.replays = in.readInt();
        this.yuer = in.readString();
        this.date = in.readString();
        this.descs = in.readString();
    }

    public static final Creator<StudyDatumItem> CREATOR = new Creator<StudyDatumItem>() {
        public StudyDatumItem createFromParcel(Parcel source) {
            return new StudyDatumItem(source);
        }

        public StudyDatumItem[] newArray(int size) {
            return new StudyDatumItem[size];
        }
    };
}
