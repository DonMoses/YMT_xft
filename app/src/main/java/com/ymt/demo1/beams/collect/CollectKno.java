package com.ymt.demo1.beams.collect;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DonMoses on 2015/11/10
 */
public class CollectKno implements Parcelable {
    private String collectionTime;
    private String user_name;
    private int avr_scor;
    private String docTitle;
    private String knowId;

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getAvr_scor() {
        return avr_scor;
    }

    public void setAvr_scor(int avr_scor) {
        this.avr_scor = avr_scor;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getKnowId() {
        return knowId;
    }

    public void setKnowId(String knowId) {
        this.knowId = knowId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.collectionTime);
        dest.writeString(this.user_name);
        dest.writeInt(this.avr_scor);
        dest.writeString(this.docTitle);
        dest.writeString(this.knowId);
    }

    public CollectKno() {
    }

    protected CollectKno(Parcel in) {
        this.collectionTime = in.readString();
        this.user_name = in.readString();
        this.avr_scor = in.readInt();
        this.docTitle = in.readString();
        this.knowId = in.readString();
    }

    public static final Parcelable.Creator<CollectKno> CREATOR = new Parcelable.Creator<CollectKno>() {
        public CollectKno createFromParcel(Parcel source) {
            return new CollectKno(source);
        }

        public CollectKno[] newArray(int size) {
            return new CollectKno[size];
        }
    };
}
