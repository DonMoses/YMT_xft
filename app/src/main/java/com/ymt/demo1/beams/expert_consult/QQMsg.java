package com.ymt.demo1.beams.expert_consult;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/12
 */
public class QQMsg extends DataSupport {
    private String msg_id;
    private String content;
    private String pro_expert_user_id;
    private String status;
    private String fk_reply_user_id;
    private String reply_time;
    private String type;
    private String reply_role;
    private String reply_user_name;
    private String fk_qq_id;

    public String getId() {
        return msg_id;
    }

    public void setId(String id) {
        this.msg_id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPro_expert_user_id() {
        return pro_expert_user_id;
    }

    public void setPro_expert_user_id(String pro_expert_user_id) {
        this.pro_expert_user_id = pro_expert_user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFk_reply_user_id() {
        return fk_reply_user_id;
    }

    public void setFk_reply_user_id(String fk_reply_user_id) {
        this.fk_reply_user_id = fk_reply_user_id;
    }

    public String getReply_time() {
        return reply_time;
    }

    public void setReply_time(String reply_time) {
        this.reply_time = reply_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReply_role() {
        return reply_role;
    }

    public void setReply_role(String reply_role) {
        this.reply_role = reply_role;
    }

    public String getReply_user_name() {
        return reply_user_name;
    }

    public void setReply_user_name(String reply_user_name) {
        this.reply_user_name = reply_user_name;
    }

    public String getFk_qq_id() {
        return fk_qq_id;
    }

    public void setFk_qq_id(String fk_qq_id) {
        this.fk_qq_id = fk_qq_id;
    }
}
