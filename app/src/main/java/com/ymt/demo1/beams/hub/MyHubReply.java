package com.ymt.demo1.beams.hub;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/7/28
 */
public class MyHubReply implements Parcelable {
    private String message;
    private int fid;
    private String lastposter;
    private int replies;
    private int views;
    private String subject;
    private String name;
    private int pid;
    private String dateline;
    private int tid;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getLastposter() {
        return lastposter;
    }

    public void setLastposter(String lastposter) {
        this.lastposter = lastposter;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeInt(this.fid);
        dest.writeString(this.lastposter);
        dest.writeInt(this.replies);
        dest.writeInt(this.views);
        dest.writeString(this.subject);
        dest.writeString(this.name);
        dest.writeInt(this.pid);
        dest.writeString(this.dateline);
        dest.writeInt(this.tid);
    }

    public MyHubReply() {
    }

    protected MyHubReply(Parcel in) {
        this.message = in.readString();
        this.fid = in.readInt();
        this.lastposter = in.readString();
        this.replies = in.readInt();
        this.views = in.readInt();
        this.subject = in.readString();
        this.name = in.readString();
        this.pid = in.readInt();
        this.dateline = in.readString();
        this.tid = in.readInt();
    }

    public static final Parcelable.Creator<MyHubReply> CREATOR = new Parcelable.Creator<MyHubReply>() {
        public MyHubReply createFromParcel(Parcel source) {
            return new MyHubReply(source);
        }

        public MyHubReply[] newArray(int size) {
            return new MyHubReply[size];
        }
    };
}
