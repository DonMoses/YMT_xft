package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/11
 */
public class QQChatInfo extends DataSupport implements Parcelable {
    private String title;
    private String time;
    private int isEnd;
    private int consultId;
    private String userName;
    private int replays;
    private int user_id;
    private int unReadCount;

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(int isEnd) {
        this.isEnd = isEnd;
    }

    public int getConsultId() {
        return consultId;
    }

    public void setConsultId(int consultId) {
        this.consultId = consultId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getReplays() {
        return replays;
    }

    public void setReplays(int replays) {
        this.replays = replays;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public QQChatInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.time);
        dest.writeInt(this.isEnd);
        dest.writeInt(this.consultId);
        dest.writeString(this.userName);
        dest.writeInt(this.replays);
        dest.writeInt(this.user_id);
        dest.writeInt(this.unReadCount);
    }

    protected QQChatInfo(Parcel in) {
        this.title = in.readString();
        this.time = in.readString();
        this.isEnd = in.readInt();
        this.consultId = in.readInt();
        this.userName = in.readString();
        this.replays = in.readInt();
        this.user_id = in.readInt();
        this.unReadCount = in.readInt();
    }

    public static final Creator<QQChatInfo> CREATOR = new Creator<QQChatInfo>() {
        public QQChatInfo createFromParcel(Parcel source) {
            return new QQChatInfo(source);
        }

        public QQChatInfo[] newArray(int size) {
            return new QQChatInfo[size];
        }
    };
}
