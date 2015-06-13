package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

import com.ymt.demo1.beams.SimpleMessage;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/15
 * 专家咨询 会话内容class。
 */
public class Chat extends DataSupport implements Serializable, Parcelable {
    /**
     * 专家ID
     */
    private String exportId;
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 消息数组
     */
    private List<SimpleMessage> messages = new ArrayList<>();


    public String getExportId() {
        return exportId;
    }

    public void setExportId(String exportId) {
        this.exportId = exportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<SimpleMessage> getMessages() {
        return messages;
    }

    public List<SimpleMessage> getMessages(int id) {
        return DataSupport.where("chat_id=?", String.valueOf(id)).find(SimpleMessage.class);
    }

    public void setMessages(List<SimpleMessage> messages) {
        this.messages = messages;
    }

    public Chat() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.exportId);
        dest.writeString(this.userId);
        dest.writeTypedList(messages);
    }

    private Chat(Parcel in) {
        this.exportId = in.readString();
        this.userId = in.readString();
        in.readTypedList(messages, SimpleMessage.CREATOR);
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        public Chat createFromParcel(Parcel source) {
            return new Chat(source);
        }

        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}
