package com.ymt.demo1.beams.edu;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by DonMoses on 2015/11/11
 */
public class VideoTrainItem extends DataSupport implements Parcelable {
    private String columnType;
    private String descs;
    private int downNum;
    private String historyId;
    private int model;
    private String opTime;
    private int replays;
    private int score;
    private int subId;
    private String title;
    private String url;
    private String views;
    private String yuer;

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    public int getDownNum() {
        return downNum;
    }

    public void setDownNum(int downNum) {
        this.downNum = downNum;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getOpTime() {
        return opTime;
    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }

    public int getReplays() {
        return replays;
    }

    public void setReplays(int replays) {
        this.replays = replays;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getYuer() {
        return yuer;
    }

    public void setYuer(String yuer) {
        this.yuer = yuer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.columnType);
        dest.writeString(this.descs);
        dest.writeInt(this.downNum);
        dest.writeString(this.historyId);
        dest.writeInt(this.model);
        dest.writeString(this.opTime);
        dest.writeInt(this.replays);
        dest.writeInt(this.score);
        dest.writeInt(this.subId);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.views);
        dest.writeString(this.yuer);
    }

    public VideoTrainItem() {
    }

    protected VideoTrainItem(Parcel in) {
        this.columnType = in.readString();
        this.descs = in.readString();
        this.downNum = in.readInt();
        this.historyId = in.readString();
        this.model = in.readInt();
        this.opTime = in.readString();
        this.replays = in.readInt();
        this.score = in.readInt();
        this.subId = in.readInt();
        this.title = in.readString();
        this.url = in.readString();
        this.views = in.readString();
        this.yuer = in.readString();
    }

    public static final Parcelable.Creator<VideoTrainItem> CREATOR = new Parcelable.Creator<VideoTrainItem>() {
        public VideoTrainItem createFromParcel(Parcel source) {
            return new VideoTrainItem(source);
        }

        public VideoTrainItem[] newArray(int size) {
            return new VideoTrainItem[size];
        }
    };
}
