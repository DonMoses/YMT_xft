package com.ymt.demo1.beams;

import android.graphics.Bitmap;

/**
 * Created by Dan on 2015/5/13
 */
public class ChatMessage {

    public enum ChatMsgType {
        TXT, IMG, MP3
    }

    private ChatMsgType msgType;
    private String msgTxt;
    private String msgAudioFilePath;
    private Bitmap msgImg;
    private boolean isRead;

    public ChatMsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(ChatMsgType msgType) {
        this.msgType = msgType;
    }

    public String getMsgTxt() {
        return msgTxt;
    }

    public void setMsgTxt(String msgTxt) {
        this.msgTxt = msgTxt;
    }

    public String getMsgAudio() {
        return msgAudioFilePath;
    }

    public void setMsgAudio(String msgAudioFilePath) {
        this.msgAudioFilePath = msgAudioFilePath;
    }

    public Bitmap getMsgImg() {
        return msgImg;
    }

    public void setMsgImg(Bitmap msgImg) {
        this.msgImg = msgImg;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
