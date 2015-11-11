package com.ymt.demo1.beams.edu;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/7/17
 */
public class MockExamItem extends DataSupport implements Parcelable {
    private int bank_num;
    private int uids;
    private int scores;
    private String times;
    private String exaId;
    private String exaName;

    public int getBank_num() {
        return bank_num;
    }

    public void setBank_num(int bank_num) {
        this.bank_num = bank_num;
    }

    public int getUids() {
        return uids;
    }

    public void setUids(int uids) {
        this.uids = uids;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getExaId() {
        return exaId;
    }

    public void setExaId(String exaId) {
        this.exaId = exaId;
    }

    public String getExaName() {
        return exaName;
    }

    public void setExaName(String exaName) {
        this.exaName = exaName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bank_num);
        dest.writeInt(this.uids);
        dest.writeInt(this.scores);
        dest.writeString(this.times);
        dest.writeString(this.exaId);
        dest.writeString(this.exaName);
    }

    public MockExamItem() {
    }

    protected MockExamItem(Parcel in) {
        this.bank_num = in.readInt();
        this.uids = in.readInt();
        this.scores = in.readInt();
        this.times = in.readString();
        this.exaId = in.readString();
        this.exaName = in.readString();
    }

    public static final Creator<MockExamItem> CREATOR = new Creator<MockExamItem>() {
        public MockExamItem createFromParcel(Parcel source) {
            return new MockExamItem(source);
        }

        public MockExamItem[] newArray(int size) {
            return new MockExamItem[size];
        }
    };
}
