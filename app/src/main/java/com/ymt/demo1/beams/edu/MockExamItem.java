package com.ymt.demo1.beams.edu;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/7/17
 */
public class MockExamItem implements Parcelable {
    private String the_id;
    private String status;
    private String total_item;
    private String subject;
    private String total_score;
    private String top_score;
    private String create_time;
    private String fk_create_user_id;
    private String exam_time;
    private String type;
    private String exam_title;

    public String getThe_id() {
        return the_id;
    }

    public void setThe_id(String the_id) {
        this.the_id = the_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_item() {
        return total_item;
    }

    public void setTotal_item(String total_item) {
        this.total_item = total_item;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public String getTop_score() {
        return top_score;
    }

    public void setTop_score(String top_score) {
        this.top_score = top_score;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFk_create_user_id() {
        return fk_create_user_id;
    }

    public void setFk_create_user_id(String fk_create_user_id) {
        this.fk_create_user_id = fk_create_user_id;
    }

    public String getExam_time() {
        return exam_time;
    }

    public void setExam_time(String exam_time) {
        this.exam_time = exam_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExam_title() {
        return exam_title;
    }

    public void setExam_title(String exam_title) {
        this.exam_title = exam_title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.the_id);
        dest.writeString(this.status);
        dest.writeString(this.total_item);
        dest.writeString(this.subject);
        dest.writeString(this.total_score);
        dest.writeString(this.top_score);
        dest.writeString(this.create_time);
        dest.writeString(this.fk_create_user_id);
        dest.writeString(this.exam_time);
        dest.writeString(this.type);
        dest.writeString(this.exam_title);
    }

    public MockExamItem() {
    }

    protected MockExamItem(Parcel in) {
        this.the_id = in.readString();
        this.status = in.readString();
        this.total_item = in.readString();
        this.subject = in.readString();
        this.total_score = in.readString();
        this.top_score = in.readString();
        this.create_time = in.readString();
        this.fk_create_user_id = in.readString();
        this.exam_time = in.readString();
        this.type = in.readString();
        this.exam_title = in.readString();
    }

    public static final Parcelable.Creator<MockExamItem> CREATOR = new Parcelable.Creator<MockExamItem>() {
        public MockExamItem createFromParcel(Parcel source) {
            return new MockExamItem(source);
        }

        public MockExamItem[] newArray(int size) {
            return new MockExamItem[size];
        }
    };
}
