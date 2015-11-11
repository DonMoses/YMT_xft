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
    private String articleTitle;
    private String source;
    private String createTime;
    private String hitnum;
    private String type;
    private String pic;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String name1;
    private String name2;

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

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHitnum() {
        return hitnum;
    }

    public void setHitnum(String hitnum) {
        this.hitnum = hitnum;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
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
        dest.writeString(this.articleTitle);
        dest.writeString(this.source);
        dest.writeString(this.createTime);
        dest.writeString(this.hitnum);
        dest.writeString(this.type);
        dest.writeString(this.pic);
        dest.writeString(this.name1);
        dest.writeString(this.name2);
    }

    protected NewsSummary(Parcel in) {
        this.the_id = in.readString();
        this.content = in.readString();
        this.author = in.readString();
        this.editor = in.readString();
        this.articleTitle = in.readString();
        this.source = in.readString();
        this.createTime = in.readString();
        this.hitnum = in.readString();
        this.type = in.readString();
        this.pic = in.readString();
        this.name1 = in.readString();
        this.name2 = in.readString();
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
