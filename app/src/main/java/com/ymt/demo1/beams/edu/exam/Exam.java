package com.ymt.demo1.beams.edu.exam;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Dan on 2015/7/17
 */
public class Exam implements Parcelable {
    private List<Topic> singles;
    private List<Topic> mutils;
    private List<Topic> fills;
    private List<Topic> judgments;
    private List<Topic> subjectives;
    private int judgmentCent;
    private int doneUsers;
    private int curQueIndex;
    private String status;
    private int total_item;
    private int fillCent;
    private String subject;
    private int total_score;
    private int top_score;
    private int singleCent;
    private int passScore;
    private int remain_time;
    private String exam_title;
    private String type;
    private String the_id;
    private int mutilCent;
    private String end_time;
    private int subjectiveCent;
    private String create_time;
    private String start_time;
    private String fk_create_user_id;
    private int cor_score;
    private String fk_exam_id;
    private int exam_time;

    public List<Topic> getSingles() {
        return singles;
    }

    public void setSingles(List<Topic> singles) {
        this.singles = singles;
    }

    public List<Topic> getMutils() {
        return mutils;
    }

    public void setMutils(List<Topic> mutils) {
        this.mutils = mutils;
    }

    public List<Topic> getFills() {
        return fills;
    }

    public void setFills(List<Topic> fills) {
        this.fills = fills;
    }

    public List<Topic> getJudgments() {
        return judgments;
    }

    public void setJudgments(List<Topic> judgments) {
        this.judgments = judgments;
    }

    public List<Topic> getSubjectives() {
        return subjectives;
    }

    public void setSubjectives(List<Topic> subjectives) {
        this.subjectives = subjectives;
    }

    public int getJudgmentCent() {
        return judgmentCent;
    }

    public void setJudgmentCent(int judgmentCent) {
        this.judgmentCent = judgmentCent;
    }

    public int getDoneUsers() {
        return doneUsers;
    }

    public void setDoneUsers(int doneUsers) {
        this.doneUsers = doneUsers;
    }

    public int getCurQueIndex() {
        return curQueIndex;
    }

    public void setCurQueIndex(int curQueIndex) {
        this.curQueIndex = curQueIndex;
    }

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

    public int getFillCent() {
        return fillCent;
    }

    public void setFillCent(int fillCent) {
        this.fillCent = fillCent;
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

    public int getSingleCent() {
        return singleCent;
    }

    public void setSingleCent(int singleScore) {
        this.singleCent = singleScore;
    }

    public int getPassScore() {
        return passScore;
    }

    public void setPassScore(int passScore) {
        this.passScore = passScore;
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

    public int getMutilCent() {
        return mutilCent;
    }

    public void setMutilCent(int mutilCent) {
        this.mutilCent = mutilCent;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getSubjectiveCent() {
        return subjectiveCent;
    }

    public void setSubjectiveCent(int subjectiveCent) {
        this.subjectiveCent = subjectiveCent;
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

    public String getFk_exam_id() {
        return fk_exam_id;
    }

    public void setFk_exam_id(String fk_exam_id) {
        this.fk_exam_id = fk_exam_id;
    }

    public int getExam_time() {
        return exam_time;
    }

    public void setExam_time(int exam_time) {
        this.exam_time = exam_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(singles);
        dest.writeTypedList(mutils);
        dest.writeTypedList(fills);
        dest.writeTypedList(judgments);
        dest.writeTypedList(subjectives);
        dest.writeInt(this.judgmentCent);
        dest.writeInt(this.doneUsers);
        dest.writeInt(this.curQueIndex);
        dest.writeString(this.status);
        dest.writeInt(this.total_item);
        dest.writeInt(this.fillCent);
        dest.writeString(this.subject);
        dest.writeInt(this.total_score);
        dest.writeInt(this.top_score);
        dest.writeInt(this.singleCent);
        dest.writeInt(this.passScore);
        dest.writeInt(this.remain_time);
        dest.writeString(this.exam_title);
        dest.writeString(this.type);
        dest.writeString(this.the_id);
        dest.writeInt(this.mutilCent);
        dest.writeString(this.end_time);
        dest.writeInt(this.subjectiveCent);
        dest.writeString(this.create_time);
        dest.writeString(this.start_time);
        dest.writeString(this.fk_create_user_id);
        dest.writeInt(this.cor_score);
        dest.writeString(this.fk_exam_id);
        dest.writeInt(this.exam_time);
    }

    public Exam() {
    }

    protected Exam(Parcel in) {
        this.singles = in.createTypedArrayList(Topic.CREATOR);
        this.mutils = in.createTypedArrayList(Topic.CREATOR);
        this.fills = in.createTypedArrayList(Topic.CREATOR);
        this.judgments = in.createTypedArrayList(Topic.CREATOR);
        this.subjectives = in.createTypedArrayList(Topic.CREATOR);
        this.judgmentCent = in.readInt();
        this.doneUsers = in.readInt();
        this.curQueIndex = in.readInt();
        this.status = in.readString();
        this.total_item = in.readInt();
        this.fillCent = in.readInt();
        this.subject = in.readString();
        this.total_score = in.readInt();
        this.top_score = in.readInt();
        this.singleCent = in.readInt();
        this.passScore = in.readInt();
        this.remain_time = in.readInt();
        this.exam_title = in.readString();
        this.type = in.readString();
        this.the_id = in.readString();
        this.mutilCent = in.readInt();
        this.end_time = in.readString();
        this.subjectiveCent = in.readInt();
        this.create_time = in.readString();
        this.start_time = in.readString();
        this.fk_create_user_id = in.readString();
        this.cor_score = in.readInt();
        this.fk_exam_id = in.readString();
        this.exam_time = in.readInt();
    }

    public static final Parcelable.Creator<Exam> CREATOR = new Parcelable.Creator<Exam>() {
        public Exam createFromParcel(Parcel source) {
            return new Exam(source);
        }

        public Exam[] newArray(int size) {
            return new Exam[size];
        }
    };
}