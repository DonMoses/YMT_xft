package com.ymt.demo1.beams.news;

import android.os.Parcel;

/**
 * Created by DonMoses on 2015/10/29
 */
public class ImageNewsSummary extends NewsSummary{
   private String cover;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.cover);
    }

    public ImageNewsSummary() {
    }

    protected ImageNewsSummary(Parcel in) {
        super(in);
        this.cover = in.readString();
    }

    public static final Creator<ImageNewsSummary> CREATOR = new Creator<ImageNewsSummary>() {
        public ImageNewsSummary createFromParcel(Parcel source) {
            return new ImageNewsSummary(source);
        }

        public ImageNewsSummary[] newArray(int size) {
            return new ImageNewsSummary[size];
        }
    };
}
