package com.ymt.demo1.beams.knowledge;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/4/29
 */
public class KnowledgeItemKYWX extends DataSupport implements Parcelable {
    private String the_id;
    private String content;
    private String files;
    private String article_title;
    private String status;
    private String create_time;
    private String meta_keys;
    private String score;
    private String fk_create_user_id;
    private String hitnum;
    private String jzxf;
    private String downcount;
    private String isFile;
    private String attribute;
    private String pdf_id;
    private String author;

    public String getPdf_id() {
        return pdf_id;
    }

    public void setPdf_id(String pdf_id) {
        this.pdf_id = pdf_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getIsFile() {
        return isFile;
    }

    public void setIsFile(String isFile) {
        this.isFile = isFile;
    }

    public String getThe_id() {
        return the_id;
    }

    public void setThe_id(String the_id) {
        this.the_id = the_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
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

    public String getMeta_keys() {
        return meta_keys;
    }

    public void setMeta_keys(String meta_keys) {
        this.meta_keys = meta_keys;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getFk_create_user_id() {
        return fk_create_user_id;
    }

    public void setFk_create_user_id(String fk_create_user_id) {
        this.fk_create_user_id = fk_create_user_id;
    }

    public String getHitnum() {
        return hitnum;
    }

    public void setHitnum(String hitnum) {
        this.hitnum = hitnum;
    }

    public String getJzxf() {
        return jzxf;
    }

    public void setJzxf(String jzxf) {
        this.jzxf = jzxf;
    }

    public String getDowncount() {
        return downcount;
    }

    public void setDowncount(String downcount) {
        this.downcount = downcount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.the_id);
        dest.writeString(this.content);
        dest.writeString(this.files);
        dest.writeString(this.article_title);
        dest.writeString(this.status);
        dest.writeString(this.create_time);
        dest.writeString(this.meta_keys);
        dest.writeString(this.score);
        dest.writeString(this.fk_create_user_id);
        dest.writeString(this.hitnum);
        dest.writeString(this.jzxf);
        dest.writeString(this.downcount);
        dest.writeString(this.isFile);
        dest.writeString(this.attribute);
        dest.writeString(this.pdf_id);
        dest.writeString(this.author);
    }

    public KnowledgeItemKYWX() {
    }

    protected KnowledgeItemKYWX(Parcel in) {
        this.the_id = in.readString();
        this.content = in.readString();
        this.files = in.readString();
        this.article_title = in.readString();
        this.status = in.readString();
        this.create_time = in.readString();
        this.meta_keys = in.readString();
        this.score = in.readString();
        this.fk_create_user_id = in.readString();
        this.hitnum = in.readString();
        this.jzxf = in.readString();
        this.downcount = in.readString();
        this.isFile = in.readString();
        this.attribute = in.readString();
        this.pdf_id = in.readString();
        this.author = in.readString();
    }

    public static final Parcelable.Creator<KnowledgeItemKYWX> CREATOR = new Parcelable.Creator<KnowledgeItemKYWX>() {
        public KnowledgeItemKYWX createFromParcel(Parcel source) {
            return new KnowledgeItemKYWX(source);
        }

        public KnowledgeItemKYWX[] newArray(int size) {
            return new KnowledgeItemKYWX[size];
        }
    };
}
