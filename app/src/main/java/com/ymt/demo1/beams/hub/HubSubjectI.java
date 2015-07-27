package com.ymt.demo1.beams.hub;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/6/9
 */
public class HubSubjectI implements Parcelable {
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 论坛id
     */
    private String forumFid;
    /**
     * 上级论坛id
     */
    private String forumFup;
    /**
     * 名称
     */
    private String forumName;
    /**
     * 类型(group:分类forum:普通论坛sub:子论坛)
     */
    private String forumType;
    /**
     * 上级论坛
     */
    private String threadFid;
    /**
     * 标题
     */
    private String threadSubject;
    /**
     * 主题id
     */
    private String threadTid;
    private int todayPosts;
    private int views;
    private int authorid;
    private int favtimes;
    private String lastpost;
    private String lastposter;
    private int rank;
    private int replies;
    private String dateline;

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public int getTodayPosts() {
        return todayPosts;
    }

    public void setTodayPosts(int todayPosts) {
        this.todayPosts = todayPosts;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public int getFavtimes() {
        return favtimes;
    }

    public void setFavtimes(int favtimes) {
        this.favtimes = favtimes;
    }

    public String getLastpost() {
        return lastpost;
    }

    public void setLastpost(String lastpost) {
        this.lastpost = lastpost;
    }

    public String getLastposter() {
        return lastposter;
    }

    public void setLastposter(String lastposter) {
        this.lastposter = lastposter;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public String getForumFid() {
        return forumFid;
    }

    public void setForumFid(String forumFid) {
        this.forumFid = forumFid;
    }

    public String getForumFup() {
        return forumFup;
    }

    public void setForumFup(String forumFup) {
        this.forumFup = forumFup;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getForumType() {
        return forumType;
    }

    public void setForumType(String forumType) {
        this.forumType = forumType;
    }

    public String getThreadFid() {
        return threadFid;
    }

    public void setThreadFid(String threadFid) {
        this.threadFid = threadFid;
    }

    public String getThreadSubject() {
        return threadSubject;
    }

    public void setThreadSubject(String threadSubject) {
        this.threadSubject = threadSubject;
    }

    public String getThreadTid() {
        return threadTid;
    }

    public void setThreadTid(String threadTid) {
        this.threadTid = threadTid;
    }

    public HubSubjectI() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.forumFid);
        dest.writeString(this.forumFup);
        dest.writeString(this.forumName);
        dest.writeString(this.forumType);
        dest.writeString(this.threadFid);
        dest.writeString(this.threadSubject);
        dest.writeString(this.threadTid);
        dest.writeInt(this.todayPosts);
        dest.writeInt(this.views);
        dest.writeInt(this.authorid);
        dest.writeInt(this.favtimes);
        dest.writeString(this.lastpost);
        dest.writeString(this.lastposter);
        dest.writeInt(this.rank);
        dest.writeInt(this.replies);
        dest.writeString(this.dateline);
    }

    protected HubSubjectI(Parcel in) {
        this.author = in.readString();
        this.forumFid = in.readString();
        this.forumFup = in.readString();
        this.forumName = in.readString();
        this.forumType = in.readString();
        this.threadFid = in.readString();
        this.threadSubject = in.readString();
        this.threadTid = in.readString();
        this.todayPosts = in.readInt();
        this.views = in.readInt();
        this.authorid = in.readInt();
        this.favtimes = in.readInt();
        this.lastpost = in.readString();
        this.lastposter = in.readString();
        this.rank = in.readInt();
        this.replies = in.readInt();
        this.dateline = in.readString();
    }

    public static final Creator<HubSubjectI> CREATOR = new Creator<HubSubjectI>() {
        public HubSubjectI createFromParcel(Parcel source) {
            return new HubSubjectI(source);
        }

        public HubSubjectI[] newArray(int size) {
            return new HubSubjectI[size];
        }
    };
}
