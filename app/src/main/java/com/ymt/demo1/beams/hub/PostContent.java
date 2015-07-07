package com.ymt.demo1.beams.hub;

/**
 * Created by Dan on 2015/7/1
 */
public class PostContent {
    private String message;
    private int pid;
    private String subject;
    private int tid;
    private String useip;
    private String tags;
    private String fid;
    private String author;
    private String anthorid;
    private int dateline;
    private int anonymous;
    private int attachment;
    private int bbcodeoff;
    private int comment;
    private int first;
    private int htmlon;
    private int invisible;
    private int parseurloff;
    private int port;
    private int position;
    private int rate;
    private int replycredit;
    private int smileyoff;
    private int status;
    private int userig;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUseip() {
        return useip;
    }

    public void setUseip(String useip) {
        this.useip = useip;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAnthorid() {
        return anthorid;
    }

    public void setAnthorid(String anthorid) {
        this.anthorid = anthorid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public int getDateline() {
        return dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }
}
