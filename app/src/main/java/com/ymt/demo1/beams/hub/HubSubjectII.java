package com.ymt.demo1.beams.hub;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/6/9
 */
public class HubSubjectII implements Parcelable {
    private int fid;
    private String threadSubject;
    private String author;
    private String lastposter;
    private int authorid;
    private int replies;
    private int views;
    private int posttableid;
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

    public String getThreadSubject() {
        return threadSubject;
    }

    public void setThreadSubject(String threadSubject) {
        this.threadSubject = threadSubject;
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

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
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

    public int getPosttableid() {
        return posttableid;
    }

    public void setPosttableid(int posttableid) {
        this.posttableid = posttableid;
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
        dest.writeString(this.threadSubject);
        dest.writeString(this.author);
        dest.writeString(this.lastposter);
        dest.writeInt(this.authorid);
        dest.writeInt(this.replies);
        dest.writeInt(this.views);
        dest.writeInt(this.posttableid);
        dest.writeString(this.name);
        dest.writeString(this.lastpost);
        dest.writeString(this.dateline);
        dest.writeInt(this.tid);
    }

    public HubSubjectII() {
    }

    protected HubSubjectII(Parcel in) {
        this.fid = in.readInt();
        this.threadSubject = in.readString();
        this.author = in.readString();
        this.lastposter = in.readString();
        this.authorid = in.readInt();
        this.replies = in.readInt();
        this.views = in.readInt();
        this.posttableid = in.readInt();
        this.name = in.readString();
        this.lastpost = in.readString();
        this.dateline = in.readString();
        this.tid = in.readInt();
    }

    public static final Parcelable.Creator<HubSubjectII> CREATOR = new Parcelable.Creator<HubSubjectII>() {
        public HubSubjectII createFromParcel(Parcel source) {
            return new HubSubjectII(source);
        }

        public HubSubjectII[] newArray(int size) {
            return new HubSubjectII[size];
        }
    };
}
