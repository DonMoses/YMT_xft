package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/7/13
 */
public class FollowedExpert extends DataSupport implements Parcelable {
    private String majorWorks;
    private String userName;
    private String headPic;
    private int fkExpertId;

    public String getMajorWorks() {
        return majorWorks;
    }

    public void setMajorWorks(String majorWorks) {
        this.majorWorks = majorWorks;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getFkExpertId() {
        return fkExpertId;
    }

    public void setFkExpertId(int fkExpertId) {
        this.fkExpertId = fkExpertId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.majorWorks);
        dest.writeString(this.userName);
        dest.writeString(this.headPic);
        dest.writeInt(this.fkExpertId);
    }

    public FollowedExpert() {
    }

    protected FollowedExpert(Parcel in) {
        this.majorWorks = in.readString();
        this.userName = in.readString();
        this.headPic = in.readString();
        this.fkExpertId = in.readInt();
    }

    public static final Creator<FollowedExpert> CREATOR = new Creator<FollowedExpert>() {
        public FollowedExpert createFromParcel(Parcel source) {
            return new FollowedExpert(source);
        }

        public FollowedExpert[] newArray(int size) {
            return new FollowedExpert[size];
        }
    };
}
