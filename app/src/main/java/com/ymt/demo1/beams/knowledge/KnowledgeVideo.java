package com.ymt.demo1.beams.knowledge;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/16
 */
public class KnowledgeVideo extends DataSupport implements Parcelable {

    private String content;
    private String the_id;
    private String classify;
    private String cover;
    private String article_title;
    private String status;
    private String create_time;
    private String hitnum;
    private String fk_create_user_id;
    private String attachment;

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

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getHitnum() {
        return hitnum;
    }

    public void setHitnum(String hitnum) {
        this.hitnum = hitnum;
    }

    public String getFk_create_user_id() {
        return fk_create_user_id;
    }

    public void setFk_create_user_id(String fk_create_user_id) {
        this.fk_create_user_id = fk_create_user_id;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.the_id);
        dest.writeString(this.classify);
        dest.writeString(this.cover);
        dest.writeString(this.article_title);
        dest.writeString(this.status);
        dest.writeString(this.create_time);
        dest.writeString(this.hitnum);
        dest.writeString(this.fk_create_user_id);
        dest.writeString(this.attachment);
    }

    public KnowledgeVideo() {
    }

    protected KnowledgeVideo(Parcel in) {
        this.content = in.readString();
        this.the_id = in.readString();
        this.classify = in.readString();
        this.cover = in.readString();
        this.article_title = in.readString();
        this.status = in.readString();
        this.create_time = in.readString();
        this.hitnum = in.readString();
        this.fk_create_user_id = in.readString();
        this.attachment = in.readString();
    }

    public static final Parcelable.Creator<KnowledgeVideo> CREATOR = new Parcelable.Creator<KnowledgeVideo>() {
        public KnowledgeVideo createFromParcel(Parcel source) {
            return new KnowledgeVideo(source);
        }

        public KnowledgeVideo[] newArray(int size) {
            return new KnowledgeVideo[size];
        }
    };
}
