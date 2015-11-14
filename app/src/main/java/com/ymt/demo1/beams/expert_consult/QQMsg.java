package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/12
 */
public class QQMsg extends DataSupport implements Parcelable {
    private int cId;
    private String cmdType;
    private String headImage;
    private int theId;
    private String msg;
    private String name;
    private String opTime;
    private String title;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public int getTheId() {
        return theId;
    }

    public void setTheId(int theId) {
        this.theId = theId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpTime() {
        return opTime;
    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    private int userId;
    private String userName;
    private String userType;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cId);
        dest.writeString(this.cmdType);
        dest.writeString(this.headImage);
        dest.writeInt(this.theId);
        dest.writeString(this.msg);
        dest.writeString(this.name);
        dest.writeString(this.opTime);
        dest.writeString(this.title);
        dest.writeInt(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.userType);
    }

    public QQMsg() {
    }

    protected QQMsg(Parcel in) {
        this.cId = in.readInt();
        this.cmdType = in.readString();
        this.headImage = in.readString();
        this.theId = in.readInt();
        this.msg = in.readString();
        this.name = in.readString();
        this.opTime = in.readString();
        this.title = in.readString();
        this.userId = in.readInt();
        this.userName = in.readString();
        this.userType = in.readString();
    }

    public static final Creator<QQMsg> CREATOR = new Creator<QQMsg>() {
        public QQMsg createFromParcel(Parcel source) {
            return new QQMsg(source);
        }

        public QQMsg[] newArray(int size) {
            return new QQMsg[size];
        }
    };
}
