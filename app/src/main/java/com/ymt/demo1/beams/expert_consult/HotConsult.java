package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/15
 */
public class HotConsult extends DataSupport implements Parcelable {
    private String content;
    private String the_id;
    private String fk_expert_id;
    private String article_title;
    private String fk_consult_user_id;
    private String status;
    private String gg;
    private String hitnum;
    private String create_time;
    private String fk_create_user_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThe_id() {
        return the_id;
    }

    public void setThe_id(String the_id) {
        this.the_id = the_id;
    }

    public String getFk_expert_id() {
        return fk_expert_id;
    }

    public void setFk_expert_id(String fk_expert_id) {
        this.fk_expert_id = fk_expert_id;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getFk_consult_user_id() {
        return fk_consult_user_id;
    }

    public void setFk_consult_user_id(String fk_consult_user_id) {
        this.fk_consult_user_id = fk_consult_user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getHitnum() {
        return hitnum;
    }

    public void setHitnum(String hitnum) {
        this.hitnum = hitnum;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFk_create_user_id() {
        return fk_create_user_id;
    }

    public void setFk_create_user_id(String fk_create_user_id) {
        this.fk_create_user_id = fk_create_user_id;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public String getJz() {
        return jz;
    }

    public void setJz(String jz) {
        this.jz = jz;
    }

    public String getGjc() {
        return gjc;
    }

    public void setGjc(String gjc) {
        this.gjc = gjc;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    private String ishot;
    private String jz;
    private String gjc;
    private String zy;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.the_id);
        dest.writeString(this.fk_expert_id);
        dest.writeString(this.article_title);
        dest.writeString(this.fk_consult_user_id);
        dest.writeString(this.status);
        dest.writeString(this.gg);
        dest.writeString(this.hitnum);
        dest.writeString(this.create_time);
        dest.writeString(this.fk_create_user_id);
        dest.writeString(this.ishot);
        dest.writeString(this.jz);
        dest.writeString(this.gjc);
        dest.writeString(this.zy);
    }

    public HotConsult() {
    }

    protected HotConsult(Parcel in) {
        this.content = in.readString();
        this.the_id = in.readString();
        this.fk_expert_id = in.readString();
        this.article_title = in.readString();
        this.fk_consult_user_id = in.readString();
        this.status = in.readString();
        this.gg = in.readString();
        this.hitnum = in.readString();
        this.create_time = in.readString();
        this.fk_create_user_id = in.readString();
        this.ishot = in.readString();
        this.jz = in.readString();
        this.gjc = in.readString();
        this.zy = in.readString();
    }

    public static final Parcelable.Creator<HotConsult> CREATOR = new Parcelable.Creator<HotConsult>() {
        public HotConsult createFromParcel(Parcel source) {
            return new HotConsult(source);
        }

        public HotConsult[] newArray(int size) {
            return new HotConsult[size];
        }
    };
}
