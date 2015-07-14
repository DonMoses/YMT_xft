package com.ymt.demo1.beams.news;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/5/22
 */
public class NewsSummary extends DataSupport implements Parcelable {
    private String the_id;
    private String content;
    private String author;
    private String editor;
    private String article_title;
    private String source;
    private String create_time;
    private String hitnum;
    private String fk_create_user_id;
    private String status;
    private String pic;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public NewsSummary() {
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
        dest.writeString(this.editor);
        dest.writeString(this.article_title);
        dest.writeString(this.source);
        dest.writeString(this.create_time);
        dest.writeString(this.hitnum);
        dest.writeString(this.fk_create_user_id);
        dest.writeString(this.status);
        dest.writeString(this.pic);
    }

    protected NewsSummary(Parcel in) {
        this.the_id = in.readString();
        this.content = in.readString();
        this.author = in.readString();
        this.editor = in.readString();
        this.article_title = in.readString();
        this.source = in.readString();
        this.create_time = in.readString();
        this.hitnum = in.readString();
        this.fk_create_user_id = in.readString();
        this.status = in.readString();
        this.pic = in.readString();
    }

    public static final Creator<NewsSummary> CREATOR = new Creator<NewsSummary>() {
        public NewsSummary createFromParcel(Parcel source) {
            return new NewsSummary(source);
        }

        public NewsSummary[] newArray(int size) {
            return new NewsSummary[size];
        }
    };
}
