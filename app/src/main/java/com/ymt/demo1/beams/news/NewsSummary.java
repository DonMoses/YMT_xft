package com.ymt.demo1.beams.news;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/5/22
 */
public class NewsSummary extends DataSupport implements Parcelable {
    //新闻id
    private String id;
    //新闻title
    private String article_title;
    //新闻时间
    private String create_time;
    //统计
    private String hitnum;
    //状态
    private String status;
    //图片
    private String pic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public NewsSummary() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.article_title);
        dest.writeString(this.create_time);
        dest.writeString(this.hitnum);
        dest.writeString(this.status);
        dest.writeString(this.pic);
    }

    protected NewsSummary(Parcel in) {
        this.id = in.readString();
        this.article_title = in.readString();
        this.create_time = in.readString();
        this.hitnum = in.readString();
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
