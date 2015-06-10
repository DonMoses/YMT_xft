package com.ymt.demo1.beams;

/**
 * Created by Dan on 2015/6/9
 */
public class HubPlate {
    private int fid;            //  论坛id
    private int fup;            //	上级论坛id
    private String type;        //	类型(group:分类forum:普通论坛sub:子论坛)
    private String name;        //	名称
    private String lastpost;    //	最后发表

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getFup() {
        return fup;
    }

    public void setFup(int fup) {
        this.fup = fup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastpost() {
        return lastpost;
    }

    public void setLastpost(String lastpost) {
        this.lastpost = lastpost;
    }
}
