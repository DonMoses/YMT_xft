package com.ymt.demo1.beams.edu;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by DonMoses on 2015/11/10
 */
public class EasyWrongTopic extends DataSupport implements Parcelable {
    private String level;
    private int topicType;
    private String subjects;
    private String problem;
    private int bankId;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getTopicType() {
        return topicType;
    }

    public void setTopicType(int topicType) {
        this.topicType = topicType;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.level);
        dest.writeInt(this.topicType);
        dest.writeString(this.subjects);
        dest.writeString(this.problem);
        dest.writeInt(this.bankId);
    }

    public EasyWrongTopic() {
    }

    protected EasyWrongTopic(Parcel in) {
        this.level = in.readString();
        this.topicType = in.readInt();
        this.subjects = in.readString();
        this.problem = in.readString();
        this.bankId = in.readInt();
    }

    public static final Creator<EasyWrongTopic> CREATOR = new Creator<EasyWrongTopic>() {
        public EasyWrongTopic createFromParcel(Parcel source) {
            return new EasyWrongTopic(source);
        }

        public EasyWrongTopic[] newArray(int size) {
            return new EasyWrongTopic[size];
        }
    };
}
