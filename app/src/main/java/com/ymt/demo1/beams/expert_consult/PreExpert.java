package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by DonMoses on 2015/11/2
 */
public class PreExpert extends DataSupport implements Parcelable {
    private String username;
    private String level;
    private int count;
    private int waitCount;
    private String headImage;

    public int getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(int fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWaitCount() {
        return waitCount;
    }

    public void setWaitCount(int waitCount) {
        this.waitCount = waitCount;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    private int fkUserId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.level);
        dest.writeInt(this.count);
        dest.writeInt(this.waitCount);
        dest.writeString(this.headImage);
        dest.writeInt(this.fkUserId);
    }

    public PreExpert() {
    }

    protected PreExpert(Parcel in) {
        this.username = in.readString();
        this.level = in.readString();
        this.count = in.readInt();
        this.waitCount = in.readInt();
        this.headImage = in.readString();
        this.fkUserId = in.readInt();
    }

    public static final Creator<PreExpert> CREATOR = new Creator<PreExpert>() {
        public PreExpert createFromParcel(Parcel source) {
            return new PreExpert(source);
        }

        public PreExpert[] newArray(int size) {
            return new PreExpert[size];
        }
    };
}
