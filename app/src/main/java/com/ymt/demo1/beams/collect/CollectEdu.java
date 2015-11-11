package com.ymt.demo1.beams.collect;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DonMoses on 2015/11/10
 */
public class CollectEdu implements Parcelable {
    private String time;
    private String title;
    private String columnType;
    private String historyId;
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public CollectEdu() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time);
        dest.writeString(this.title);
        dest.writeString(this.columnType);
        dest.writeString(this.historyId);
        dest.writeInt(this.score);
    }

    protected CollectEdu(Parcel in) {
        this.time = in.readString();
        this.title = in.readString();
        this.columnType = in.readString();
        this.historyId = in.readString();
        this.score = in.readInt();
    }

    public static final Creator<CollectEdu> CREATOR = new Creator<CollectEdu>() {
        public CollectEdu createFromParcel(Parcel source) {
            return new CollectEdu(source);
        }

        public CollectEdu[] newArray(int size) {
            return new CollectEdu[size];
        }
    };
}
