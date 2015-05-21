package com.ymt.demo1.beams;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/4/14
 */
public class EduDialogueInfo implements Parcelable {

    private String question;
    private String answer;
    private int watchedCount;
    private int collectedCount;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getWatchedCount() {
        return watchedCount;
    }

    public void setWatchedCount(int watchedCount) {
        this.watchedCount = watchedCount;
    }

    public int getCollectedCount() {
        return collectedCount;
    }

    public void setCollectedCount(int collectedCount) {
        this.collectedCount = collectedCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeString(this.answer);
        dest.writeInt(this.watchedCount);
        dest.writeInt(this.collectedCount);
    }

    public EduDialogueInfo() {
    }

    private EduDialogueInfo(Parcel in) {
        this.question = in.readString();
        this.answer = in.readString();
        this.watchedCount = in.readInt();
        this.collectedCount = in.readInt();
    }

    public static final Parcelable.Creator<EduDialogueInfo> CREATOR = new Parcelable.Creator<EduDialogueInfo>() {
        public EduDialogueInfo createFromParcel(Parcel source) {
            return new EduDialogueInfo(source);
        }

        public EduDialogueInfo[] newArray(int size) {
            return new EduDialogueInfo[size];
        }
    };
}
