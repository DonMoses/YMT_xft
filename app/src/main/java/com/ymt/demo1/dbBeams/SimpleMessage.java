package com.ymt.demo1.dbBeams;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dan on 2015/5/15
 * 用于测试的简单消息
 */
public class SimpleMessage extends DataSupport implements Serializable, Parcelable {

    private String type;
    private String msgTxt;
    private Chat chat;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsgTxt() {
        return msgTxt;
    }

    public void setMsgTxt(String msgTxt) {
        this.msgTxt = msgTxt;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public SimpleMessage() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.msgTxt);
        dest.writeParcelable(this.chat, 0);
    }

    private SimpleMessage(Parcel in) {
        this.type = in.readString();
        this.msgTxt = in.readString();
        this.chat = in.readParcelable(Chat.class.getClassLoader());
    }

    public static final Creator<SimpleMessage> CREATOR = new Creator<SimpleMessage>() {
        public SimpleMessage createFromParcel(Parcel source) {
            return new SimpleMessage(source);
        }

        public SimpleMessage[] newArray(int size) {
            return new SimpleMessage[size];
        }
    };
}
