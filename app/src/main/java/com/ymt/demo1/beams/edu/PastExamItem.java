package com.ymt.demo1.beams.edu;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/4/9
 * 试题信息
 */
public class PastExamItem implements Parcelable {
    private String exam_year;
    private String the_id;
    private String files;
    private String article_title;
    private String level;
    private String status;
    private String subject;
    private String create_time;
    private String hitnum;
    private String meta_keys;
    private String fk_create_user_id;
    private String pdf_id;
    private String downcount;

    public String getExam_year() {
        return exam_year;
    }

    public void setExam_year(String exam_year) {
        this.exam_year = exam_year;
    }

    public String getThe_id() {
        return the_id;
    }

    public void setThe_id(String the_id) {
        this.the_id = the_id;
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

    public String getHitnum() {
        return hitnum;
    }

    public void setHitnum(String hitnum) {
        this.hitnum = hitnum;
    }

    public String getMeta_keys() {
        return meta_keys;
    }

    public void setMeta_keys(String meta_keys) {
        this.meta_keys = meta_keys;
    }

    public String getFk_create_user_id() {
        return fk_create_user_id;
    }

    public void setFk_create_user_id(String fk_create_user_id) {
        this.fk_create_user_id = fk_create_user_id;
    }

    public String getPdf_id() {
        return pdf_id;
    }

    public void setPdf_id(String pdf_id) {
        this.pdf_id = pdf_id;
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
        dest.writeString(this.exam_year);
        dest.writeString(this.the_id);
        dest.writeString(this.files);
        dest.writeString(this.article_title);
        dest.writeString(this.level);
        dest.writeString(this.status);
        dest.writeString(this.subject);
        dest.writeString(this.create_time);
        dest.writeString(this.hitnum);
        dest.writeString(this.meta_keys);
        dest.writeString(this.fk_create_user_id);
        dest.writeString(this.pdf_id);
        dest.writeString(this.downcount);
    }

    public PastExamItem() {
    }

    protected PastExamItem(Parcel in) {
        this.exam_year = in.readString();
        this.the_id = in.readString();
        this.files = in.readString();
        this.article_title = in.readString();
        this.level = in.readString();
        this.status = in.readString();
        this.subject = in.readString();
        this.create_time = in.readString();
        this.hitnum = in.readString();
        this.meta_keys = in.readString();
        this.fk_create_user_id = in.readString();
        this.pdf_id = in.readString();
        this.downcount = in.readString();
    }

    public static final Parcelable.Creator<PastExamItem> CREATOR = new Parcelable.Creator<PastExamItem>() {
        public PastExamItem createFromParcel(Parcel source) {
            return new PastExamItem(source);
        }

        public PastExamItem[] newArray(int size) {
            return new PastExamItem[size];
        }
    };
}
