package com.ymt.demo1.beams.hub;

/**
 * Created by Dan on 2015/6/9
 */
public class HubSubject {
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
}
