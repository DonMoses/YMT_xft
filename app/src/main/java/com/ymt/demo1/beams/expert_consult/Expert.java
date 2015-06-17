package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/16
 */
public class Expert extends DataSupport implements Parcelable {
    private String count;
    private String remark;
    private String head_pic;
    private String fk_user_id;
    private String good;
    private String social_part_time;
    private String user_name;
    private String resume;
    private String level;
    private String bio;
    private String major_works;
    private String create_time;
    private String capacity;
    private String experience;
    private String note;
    private String work_time;
    private String the_id;

    public String getThe_id() {
        return the_id;
    }

    public void setThe_id(String the_id) {
        this.the_id = the_id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getFk_user_id() {
        return fk_user_id;
    }

    public void setFk_user_id(String fk_user_id) {
        this.fk_user_id = fk_user_id;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getSocial_part_time() {
        return social_part_time;
    }

    public void setSocial_part_time(String social_part_time) {
        this.social_part_time = social_part_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getMajor_works() {
        return major_works;
    }

    public void setMajor_works(String major_works) {
        this.major_works = major_works;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getWork_time() {
        return work_time;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.count);
        dest.writeString(this.remark);
        dest.writeString(this.head_pic);
        dest.writeString(this.fk_user_id);
        dest.writeString(this.good);
        dest.writeString(this.social_part_time);
        dest.writeString(this.user_name);
        dest.writeString(this.resume);
        dest.writeString(this.level);
        dest.writeString(this.bio);
        dest.writeString(this.major_works);
        dest.writeString(this.create_time);
        dest.writeString(this.capacity);
        dest.writeString(this.experience);
        dest.writeString(this.note);
        dest.writeString(this.work_time);
        dest.writeString(this.the_id);
    }

    public Expert() {
    }

    protected Expert(Parcel in) {
        this.count = in.readString();
        this.remark = in.readString();
        this.head_pic = in.readString();
        this.fk_user_id = in.readString();
        this.good = in.readString();
        this.social_part_time = in.readString();
        this.user_name = in.readString();
        this.resume = in.readString();
        this.level = in.readString();
        this.bio = in.readString();
        this.major_works = in.readString();
        this.create_time = in.readString();
        this.capacity = in.readString();
        this.experience = in.readString();
        this.note = in.readString();
        this.work_time = in.readString();
        this.the_id = in.readString();
    }

    public static final Parcelable.Creator<Expert> CREATOR = new Parcelable.Creator<Expert>() {
        public Expert createFromParcel(Parcel source) {
            return new Expert(source);
        }

        public Expert[] newArray(int size) {
            return new Expert[size];
        }
    };
}
