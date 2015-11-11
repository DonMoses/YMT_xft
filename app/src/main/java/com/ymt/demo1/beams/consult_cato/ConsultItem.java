package com.ymt.demo1.beams.consult_cato;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/5/4
 */
public class ConsultItem extends DataSupport implements Parcelable {
    private String itContent;
    private String createTime;
    private String title;
    private int expertId;
    private int views;
    private String itTime;
    private int codeId;

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getItContent() {
        return itContent;
    }

    public void setItContent(String itContent) {
        this.itContent = itContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getExpertId() {
        return expertId;
    }

    public void setExpertId(int expertId) {
        this.expertId = expertId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getItTime() {
        return itTime;
    }

    public void setItTime(String itTime) {
        this.itTime = itTime;
    }

    private int cid;

    public ConsultItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itContent);
        dest.writeString(this.createTime);
        dest.writeString(this.title);
        dest.writeInt(this.expertId);
        dest.writeInt(this.views);
        dest.writeString(this.itTime);
        dest.writeInt(this.codeId);
        dest.writeInt(this.cid);
    }

    protected ConsultItem(Parcel in) {
        this.itContent = in.readString();
        this.createTime = in.readString();
        this.title = in.readString();
        this.expertId = in.readInt();
        this.views = in.readInt();
        this.itTime = in.readString();
        this.codeId = in.readInt();
        this.cid = in.readInt();
    }

    public static final Creator<ConsultItem> CREATOR = new Creator<ConsultItem>() {
        public ConsultItem createFromParcel(Parcel source) {
            return new ConsultItem(source);
        }

        public ConsultItem[] newArray(int size) {
            return new ConsultItem[size];
        }
    };
}
