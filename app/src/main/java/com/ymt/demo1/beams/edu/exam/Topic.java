package com.ymt.demo1.beams.edu.exam;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Dan on 2015/7/17
 */
public class Topic implements Parcelable {
    private String is_fallibility;
    private String diff;
    private String status;
    private String subject;
    private String answer;
    private String fk_que_id;
    private String user_answer;
    private String type;
    private String the_id;
    private String content;
    private String level;
    private String hot_ratio;
    private int que_score;
    private String is_hot;
    private String points;
    private String err_ratio;
    private String fk_exam_id;

    private List<Opt> ops;

    public String getIs_fallibility() {
        return is_fallibility;
    }

    public void setIs_fallibility(String is_fallibility) {
        this.is_fallibility = is_fallibility;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getFk_que_id() {
        return fk_que_id;
    }

    public void setFk_que_id(String fk_que_id) {
        this.fk_que_id = fk_que_id;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getHot_ratio() {
        return hot_ratio;
    }

    public void setHot_ratio(String hot_ratio) {
        this.hot_ratio = hot_ratio;
    }

    public int getQue_score() {
        return que_score;
    }

    public void setQue_score(int que_score) {
        this.que_score = que_score;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getErr_ratio() {
        return err_ratio;
    }

    public void setErr_ratio(String err_ratio) {
        this.err_ratio = err_ratio;
    }

    public String getFk_exam_id() {
        return fk_exam_id;
    }

    public void setFk_exam_id(String fk_exam_id) {
        this.fk_exam_id = fk_exam_id;
    }

    public List<Opt> getOps() {
        return ops;
    }

    public void setOps(List<Opt> ops) {
        this.ops = ops;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.is_fallibility);
        dest.writeString(this.diff);
        dest.writeString(this.status);
        dest.writeString(this.subject);
        dest.writeString(this.answer);
        dest.writeString(this.fk_que_id);
        dest.writeString(this.user_answer);
        dest.writeString(this.type);
        dest.writeString(this.the_id);
        dest.writeString(this.content);
        dest.writeString(this.level);
        dest.writeString(this.hot_ratio);
        dest.writeInt(this.que_score);
        dest.writeString(this.is_hot);
        dest.writeString(this.points);
        dest.writeString(this.err_ratio);
        dest.writeString(this.fk_exam_id);
        dest.writeTypedList(ops);
    }

    public Topic() {
    }

    protected Topic(Parcel in) {
        this.is_fallibility = in.readString();
        this.diff = in.readString();
        this.status = in.readString();
        this.subject = in.readString();
        this.answer = in.readString();
        this.fk_que_id = in.readString();
        this.user_answer = in.readString();
        this.type = in.readString();
        this.the_id = in.readString();
        this.content = in.readString();
        this.level = in.readString();
        this.hot_ratio = in.readString();
        this.que_score = in.readInt();
        this.is_hot = in.readString();
        this.points = in.readString();
        this.err_ratio = in.readString();
        this.fk_exam_id = in.readString();
        this.ops = in.createTypedArrayList(Opt.CREATOR);
    }

    public static final Parcelable.Creator<Topic> CREATOR = new Parcelable.Creator<Topic>() {
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
}