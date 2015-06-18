package com.ymt.demo1.beams.edu;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Dan on 2015/4/9
 * 试题信息
 */
public class XXFExam implements Parcelable, Serializable {
    public static final String TITLE_TYPE_1 = "一级消防工程师";
    public static final String TITLE_TYPE_2 = "二级消防工程师";
    public static final String TITLE_TYPE_3 = "初级消防构建员";
    //试题年份
    private int examYear;
    //名称
    private String title;
    //对应职称类型（一级、二级、初级等）
    private String titleType;
    //考试科目
    private String examSubject;
    //是否是真题（或测试题）
    private boolean isActual;
    //包含题目数
    private String count;
    //答题时间
    private String totalTime;
    //试卷总分
    private String totalScore;
    //被查看次数统计
    private String watchedCount;
    //被收藏次数统计
    private String collectedCount;

    public String getTitleType() {
        return titleType;
    }

    public String getExamSubject() {
        return examSubject;
    }

    public void setExamSubject(String examSubject) {
        this.examSubject = examSubject;
    }

    public int getExamYear() {
        return examYear;
    }

    public void setExamYear(int examYear) {
        this.examYear = examYear;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActual() {
        return isActual;
    }

    public void setIsActual(boolean isActual) {
        this.isActual = isActual;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getWatchedCount() {
        return watchedCount;
    }

    public void setWatchedCount(String watchedCount) {
        this.watchedCount = watchedCount;
    }

    public String getCollectedCount() {
        return collectedCount;
    }

    public void setCollectedCount(String collectedCount) {
        this.collectedCount = collectedCount;
    }


    public XXFExam() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.examYear);
        dest.writeString(this.title);
        dest.writeString(this.titleType);
        dest.writeString(this.examSubject);
        dest.writeByte(isActual ? (byte) 1 : (byte) 0);
        dest.writeString(this.count);
        dest.writeString(this.totalTime);
        dest.writeString(this.totalScore);
        dest.writeString(this.watchedCount);
        dest.writeString(this.collectedCount);
    }

    private XXFExam(Parcel in) {
        this.examYear = in.readInt();
        this.title = in.readString();
        this.titleType = in.readString();
        this.examSubject = in.readString();
        this.isActual = in.readByte() != 0;
        this.count = in.readString();
        this.totalTime = in.readString();
        this.totalScore = in.readString();
        this.watchedCount = in.readString();
        this.collectedCount = in.readString();
    }

    public static final Creator<XXFExam> CREATOR = new Creator<XXFExam>() {
        public XXFExam createFromParcel(Parcel source) {
            return new XXFExam(source);
        }

        public XXFExam[] newArray(int size) {
            return new XXFExam[size];
        }
    };


}
