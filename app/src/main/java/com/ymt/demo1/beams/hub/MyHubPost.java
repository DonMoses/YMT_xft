package com.ymt.demo1.beams.hub;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/7/28
 */
public class MyHubPost implements Parcelable {
    private int fid;
    private String author;
    private String lastposter;
    private int replies;
    private int views;
    private String subject;
    private String name;
    private String lastpost;
    private String dateline;
    private int tid;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getLastpost() {
        return lastpost;
    }

    public void setLastpost(String lastpost) {
        this.lastpost = lastpost;
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
        dest.writeInt(this.fid);
        dest.writeString(this.author);
        dest.writeString(this.lastposter);
        dest.writeInt(this.replies);
        dest.writeInt(this.views);
        dest.writeString(this.subject);
        dest.writeString(this.name);
        dest.writeString(this.lastpost);
        dest.writeString(this.dateline);
        dest.writeInt(this.tid);
    }

    public MyHubPost() {
    }

    protected MyHubPost(Parcel in) {
        this.fid = in.readInt();
        this.author = in.readString();
        this.lastposter = in.readString();
        this.replies = in.readInt();
        this.views = in.readInt();
        this.subject = in.readString();
        this.name = in.readString();
        this.lastpost = in.readString();
        this.dateline = in.readString();
        this.tid = in.readInt();
    }

    public static final Parcelable.Creator<MyHubPost> CREATOR = new Parcelable.Creator<MyHubPost>() {
        public MyHubPost createFromParcel(Parcel source) {
            return new MyHubPost(source);
        }

        public MyHubPost[] newArray(int size) {
            return new MyHubPost[size];
        }
    };
}
