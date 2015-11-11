package com.ymt.demo1.beams.edu;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/7/21
 */
public class MyExamsItem implements Parcelable {
    private String theId;
    private String otime;
    private int score;
    private String exaId;
    private String exaName;

    public String getTheId() {
        return theId;
    }

    public void setTheId(String theId) {
        this.theId = theId;
    }

    public String getOtime() {
        return otime;
    }

    public void setOtime(String otime) {
        this.otime = otime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getExaId() {
        return exaId;
    }

    public void setExaId(String exaId) {
        this.exaId = exaId;
    }

    public String getExaName() {
        return exaName;
    }

    public void setExaName(String exaName) {
        this.exaName = exaName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.theId);
        dest.writeString(this.otime);
        dest.writeInt(this.score);
        dest.writeString(this.exaId);
        dest.writeString(this.exaName);
    }

    public MyExamsItem() {
    }

    protected MyExamsItem(Parcel in) {
        this.theId = in.readString();
        this.otime = in.readString();
        this.score = in.readInt();
        this.exaId = in.readString();
        this.exaName = in.readString();
    }

    public static final Creator<MyExamsItem> CREATOR = new Creator<MyExamsItem>() {
        public MyExamsItem createFromParcel(Parcel source) {
            return new MyExamsItem(source);
        }

        public MyExamsItem[] newArray(int size) {
            return new MyExamsItem[size];
        }
    };
}
