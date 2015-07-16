package com.ymt.demo1.beams;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/7/16
 */
public class HeaderById extends DataSupport implements Parcelable {

    private String the_id;
    private String headerUrl;

    public String getThe_id() {
        return the_id;
    }

    public void setThe_id(String the_id) {
        this.the_id = the_id;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.the_id);
        dest.writeString(this.headerUrl);
    }

    public HeaderById() {
    }

    protected HeaderById(Parcel in) {
        this.the_id = in.readString();
        this.headerUrl = in.readString();
    }

    public static final Parcelable.Creator<HeaderById> CREATOR = new Parcelable.Creator<HeaderById>() {
        public HeaderById createFromParcel(Parcel source) {
            return new HeaderById(source);
        }

        public HeaderById[] newArray(int size) {
            return new HeaderById[size];
        }
    };
}
