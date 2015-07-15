package com.ymt.demo1.beams.edu;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Dan on 2015/5/22
 */
public class StudyDatumItem extends DataSupport implements Parcelable {

//    public enum TypeO {
//        WORD, PPT, PDF, MP3
//    }

    private String the_id;
    private String content;
    private String author;
    private String time;
    private String article_title;
    private String level;
    private String status;
    private String subject;
    private String create_time;
    private String fk_create_user_id;
    private String hitnum;
    private String pdf_id;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public String getHitnum() {
        return hitnum;
    }

    public void setHitnum(String hitnum) {
        this.hitnum = hitnum;
    }

    public String getPdf_id() {
        return pdf_id;
    }

    public void setPdf_id(String pdf_id) {
        this.pdf_id = pdf_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.the_id);
        dest.writeString(this.content);
        dest.writeString(this.author);
        dest.writeString(this.time);
        dest.writeString(this.article_title);
        dest.writeString(this.level);
        dest.writeString(this.status);
        dest.writeString(this.subject);
        dest.writeString(this.create_time);
        dest.writeString(this.fk_create_user_id);
        dest.writeString(this.hitnum);
        dest.writeString(this.pdf_id);
    }

    public StudyDatumItem() {
    }

    protected StudyDatumItem(Parcel in) {
        this.the_id = in.readString();
        this.content = in.readString();
        this.author = in.readString();
        this.time = in.readString();
        this.article_title = in.readString();
        this.level = in.readString();
        this.status = in.readString();
        this.subject = in.readString();
        this.create_time = in.readString();
        this.fk_create_user_id = in.readString();
        this.hitnum = in.readString();
        this.pdf_id = in.readString();
    }

    public static final Parcelable.Creator<StudyDatumItem> CREATOR = new Parcelable.Creator<StudyDatumItem>() {
        public StudyDatumItem createFromParcel(Parcel source) {
            return new StudyDatumItem(source);
        }

        public StudyDatumItem[] newArray(int size) {
            return new StudyDatumItem[size];
        }
    };
}
