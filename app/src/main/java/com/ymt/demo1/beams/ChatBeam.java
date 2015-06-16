package com.ymt.demo1.beams;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dan on 2015/5/13
 * 每一个chatBeam 包含： 一个专家、过去的会话信息的array（专家in，用户out）、未读的会话信息的array（专家in）
 */
public class ChatBeam implements Serializable {
    private ExportTest exportTest;
    private ArrayList<ChatMessage> messages;

    public ExportTest getExportTest() {
        return exportTest;
    }

    public void setExportTest(ExportTest exportTest) {
        this.exportTest = exportTest;
    }

    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }
}
