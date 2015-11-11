package com.ymt.demo1.beams.collect;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DonMoses on 2015/11/10
 */
public class CollectCon implements Parcelable {
    private String time;
    private String title;
    private int views;
    private int userId;
    private int consult_id;

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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getConsult_id() {
        return consult_id;
    }

    public void setConsult_id(int consult_id) {
        this.consult_id = consult_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time);
        dest.writeString(this.title);
        dest.writeInt(this.views);
        dest.writeInt(this.userId);
        dest.writeInt(this.consult_id);
    }

    public CollectCon() {
    }

    protected CollectCon(Parcel in) {
        this.time = in.readString();
        this.title = in.readString();
        this.views = in.readInt();
        this.userId = in.readInt();
        this.consult_id = in.readInt();
    }

    public static final Parcelable.Creator<CollectCon> CREATOR = new Parcelable.Creator<CollectCon>() {
        public CollectCon createFromParcel(Parcel source) {
            return new CollectCon(source);
        }

        public CollectCon[] newArray(int size) {
            return new CollectCon[size];
        }
    };
}
