package com.ymt.demo1.beams.hub;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/7/28
 */
public class MyHubSysInfo implements Parcelable {
    private int author;
    private int authorid;
    private int category;
    private int dateline;
    private int from_id;
    private String from_idtype;
    private int from_num;
    private int the_id;
    private int new_;
    private String note;
    private String type;
    private int uid;

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getDateline() {
        return dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public String getFrom_idtype() {
        return from_idtype;
    }

    public void setFrom_idtype(String from_idtype) {
        this.from_idtype = from_idtype;
    }

    public int getFrom_num() {
        return from_num;
    }

    public void setFrom_num(int from_num) {
        this.from_num = from_num;
    }

    public int getThe_id() {
        return the_id;
    }

    public void setThe_id(int the_id) {
        this.the_id = the_id;
    }

    public int getNew_() {
        return new_;
    }

    public void setNew_(int new_) {
        this.new_ = new_;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.author);
        dest.writeInt(this.authorid);
        dest.writeInt(this.category);
        dest.writeInt(this.dateline);
        dest.writeInt(this.from_id);
        dest.writeString(this.from_idtype);
        dest.writeInt(this.from_num);
        dest.writeInt(this.the_id);
        dest.writeInt(this.new_);
        dest.writeString(this.note);
        dest.writeString(this.type);
        dest.writeInt(this.uid);
    }

    public MyHubSysInfo() {
    }

    protected MyHubSysInfo(Parcel in) {
        this.author = in.readInt();
        this.authorid = in.readInt();
        this.category = in.readInt();
        this.dateline = in.readInt();
        this.from_id = in.readInt();
        this.from_idtype = in.readString();
        this.from_num = in.readInt();
        this.the_id = in.readInt();
        this.new_ = in.readInt();
        this.note = in.readString();
        this.type = in.readString();
        this.uid = in.readInt();
    }

    public static final Parcelable.Creator<MyHubSysInfo> CREATOR = new Parcelable.Creator<MyHubSysInfo>() {
        public MyHubSysInfo createFromParcel(Parcel source) {
            return new MyHubSysInfo(source);
        }

        public MyHubSysInfo[] newArray(int size) {
            return new MyHubSysInfo[size];
        }
    };
}
