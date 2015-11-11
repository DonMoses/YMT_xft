package com.ymt.demo1.beams.edu.exam;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/7/17
 */
public class Opt implements Parcelable {
    private String content;

    public String getThe_id() {
        return the_id;
    }

    public void setThe_id(String the_id) {
        this.the_id = the_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getAnswer_analysis() {
        return answer_analysis;
    }

    public void setAnswer_analysis(String answer_analysis) {
        this.answer_analysis = answer_analysis;
    }

    public boolean is_corrent() {
        return is_correct;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }

    public String getFk_que_id() {
        return fk_que_id;
    }

    public void setFk_que_id(String fk_que_id) {
        this.fk_que_id = fk_que_id;
    }

    private String the_id;
    private int sort;
    private String answer_analysis;
    private boolean is_correct;
    private String fk_que_id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.the_id);
        dest.writeInt(this.sort);
        dest.writeString(this.answer_analysis);
        dest.writeByte(is_correct ? (byte) 1 : (byte) 0);
        dest.writeString(this.fk_que_id);
    }

    public Opt() {
    }

    protected Opt(Parcel in) {
        this.content = in.readString();
        this.the_id = in.readString();
        this.sort = in.readInt();
        this.answer_analysis = in.readString();
        this.is_correct = in.readByte() != 0;
        this.fk_que_id = in.readString();
    }

    public static final Parcelable.Creator<Opt> CREATOR = new Parcelable.Creator<Opt>() {
        public Opt createFromParcel(Parcel source) {
            return new Opt(source);
        }

        public Opt[] newArray(int size) {
            return new Opt[size];
        }
    };
}