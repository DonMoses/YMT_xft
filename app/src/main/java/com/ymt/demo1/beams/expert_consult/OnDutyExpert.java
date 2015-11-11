package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DonMoses on 2015/9/15
 */
public class OnDutyExpert implements Parcelable {
    private int theId;
    private String dutyTime;
    private String fkUserId;
    private String exName;
    private String ondutyDate;

    public int getTheId() {
        return theId;
    }

    public void setTheId(int theId) {
        this.theId = theId;
    }

    public String getDutyTime() {
        return dutyTime;
    }

    public void setDutyTime(String dutyTime) {
        this.dutyTime = dutyTime;
    }

    public String getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public String getOndutyDate() {
        return ondutyDate;
    }

    public void setOndutyDate(String ondutyDate) {
        this.ondutyDate = ondutyDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.theId);
        dest.writeString(this.dutyTime);
        dest.writeString(this.fkUserId);
        dest.writeString(this.exName);
        dest.writeString(this.ondutyDate);
    }

    public OnDutyExpert() {
    }

    protected OnDutyExpert(Parcel in) {
        this.theId = in.readInt();
        this.dutyTime = in.readString();
        this.fkUserId = in.readString();
        this.exName = in.readString();
        this.ondutyDate = in.readString();
    }

    public static final Creator<OnDutyExpert> CREATOR = new Creator<OnDutyExpert>() {
        public OnDutyExpert createFromParcel(Parcel source) {
            return new OnDutyExpert(source);
        }

        public OnDutyExpert[] newArray(int size) {
            return new OnDutyExpert[size];
        }
    };
}
