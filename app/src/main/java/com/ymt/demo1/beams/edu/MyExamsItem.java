package com.ymt.demo1.beams.edu;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/7/21
 */
public class MyExamsItem implements Parcelable {
    private String status;
    private int total_item;
    private String subject;
    private int total_score;
    private int top_score;
    private String fk_user_id;
    private int remain_time;
    private String exam_title;
    private String type;
    private String the_id;
    private String create_time;
    private String start_time;
    private String fk_create_user_id;
    private int cor_score;
    private int exam_time;
    private String fk_exam_id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal_item() {
        return total_item;
    }

    public void setTotal_item(int total_item) {
        this.total_item = total_item;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public int getTop_score() {
        return top_score;
    }

    public void setTop_score(int top_score) {
        this.top_score = top_score;
    }

    public String getFk_user_id() {
        return fk_user_id;
    }

    public void setFk_user_id(String fk_user_id) {
        this.fk_user_id = fk_user_id;
    }

    public int getRemain_time() {
        return remain_time;
    }

    public void setRemain_time(int remain_time) {
        this.remain_time = remain_time;
    }

    public String getExam_title() {
        return exam_title;
    }

    public void setExam_title(String exam_title) {
        this.exam_title = exam_title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThe_id() {
        return the_id;
    }

    public void setThe_id(String the_id) {
        this.the_id = the_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getFk_create_user_id() {
        return fk_create_user_id;
    }

    public void setFk_create_user_id(String fk_create_user_id) {
        this.fk_create_user_id = fk_create_user_id;
    }

    public int getCor_score() {
        return cor_score;
    }

    public void setCor_score(int cor_score) {
        this.cor_score = cor_score;
    }

    public int getExam_time() {
        return exam_time;
    }

    public void setExam_time(int exam_time) {
        this.exam_time = exam_time;
    }

    public String getFk_exam_id() {
        return fk_exam_id;
    }

    public void setFk_exam_id(String fk_exam_id) {
        this.fk_exam_id = fk_exam_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeInt(this.total_item);
        dest.writeString(this.subject);
        dest.writeInt(this.total_score);
        dest.writeInt(this.top_score);
        dest.writeString(this.fk_user_id);
        dest.writeInt(this.remain_time);
        dest.writeString(this.exam_title);
        dest.writeString(this.type);
        dest.writeString(this.the_id);
        dest.writeString(this.create_time);
        dest.writeString(this.start_time);
        dest.writeString(this.fk_create_user_id);
        dest.writeInt(this.cor_score);
        dest.writeInt(this.exam_time);
        dest.writeString(this.fk_exam_id);
    }

    public MyExamsItem() {
    }

    protected MyExamsItem(Parcel in) {
        this.status = in.readString();
        this.total_item = in.readInt();
        this.subject = in.readString();
        this.total_score = in.readInt();
        this.top_score = in.readInt();
        this.fk_user_id = in.readString();
        this.remain_time = in.readInt();
        this.exam_title = in.readString();
        this.type = in.readString();
        this.the_id = in.readString();
        this.create_time = in.readString();
        this.start_time = in.readString();
        this.fk_create_user_id = in.readString();
        this.cor_score = in.readInt();
        this.exam_time = in.readInt();
        this.fk_exam_id = in.readString();
    }

    public static final Parcelable.Creator<MyExamsItem> CREATOR = new Parcelable.Creator<MyExamsItem>() {
        public MyExamsItem createFromParcel(Parcel source) {
            return new MyExamsItem(source);
        }

        public MyExamsItem[] newArray(int size) {
            return new MyExamsItem[size];
        }
    };
}
