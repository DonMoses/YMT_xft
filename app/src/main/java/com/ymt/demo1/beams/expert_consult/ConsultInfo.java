package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by DonMoses on 2015/11/3
 */
public class ConsultInfo extends DataSupport implements Parcelable {
    private int cid;
    private String title;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ConsultInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cid);
        dest.writeString(this.title);
        dest.writeString(this.type);
    }

    protected ConsultInfo(Parcel in) {
        this.cid = in.readInt();
        this.title = in.readString();
        this.type = in.readString();
    }

    public static final Creator<ConsultInfo> CREATOR = new Creator<ConsultInfo>() {
        public ConsultInfo createFromParcel(Parcel source) {
            return new ConsultInfo(source);
        }

        public ConsultInfo[] newArray(int size) {
            return new ConsultInfo[size];
        }
    };
}
