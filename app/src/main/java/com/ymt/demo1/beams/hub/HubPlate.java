package com.ymt.demo1.beams.hub;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/6/9
 */
public class HubPlate implements Parcelable {
    private int fid;            //  论坛id
    private int fup;            //	上级论坛id
    private String type;        //	类型(group:分类forum:普通论坛sub:子论坛)
    private String name;        //	名称
    private String lastpost;    //	最后发表
    private int rank;
    private int threads;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.fid);
        dest.writeInt(this.fup);
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeString(this.lastpost);
    }

    public HubPlate() {
    }

    protected HubPlate(Parcel in) {
        this.fid = in.readInt();
        this.fup = in.readInt();
        this.type = in.readString();
        this.name = in.readString();
        this.lastpost = in.readString();
    }

    public static final Parcelable.Creator<HubPlate> CREATOR = new Parcelable.Creator<HubPlate>() {
        public HubPlate createFromParcel(Parcel source) {
            return new HubPlate(source);
        }

        public HubPlate[] newArray(int size) {
            return new HubPlate[size];
        }
    };
}
