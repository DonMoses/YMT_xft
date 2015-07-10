package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/16
 */
public class Expert extends DataSupport implements Parcelable {
    private String id_number;

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    private String count;
    private String pro_life;
    private String head_pic;
    private String tel;
    private String fk_user_id;
    private String addr;
    private String education;
    private String reporting_methods;
    private String home_zip_code;
    private String the_id;
    private String politics;
    private String qualification;
    private String level;
    private String capacity;
    private String experience;
    private String industry;
    private String note;
    private String work_addr;
    private String others;
    private String home_addr;
    private String user_name;
    private String school;
    private String degree;
    private String major_works;
    private String work_zip_code;
    private String create_time;
    private String position_title;
    private String work_experience;
    private String work_name;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPro_life() {
        return pro_life;
    }

    public void setPro_life(String pro_life) {
        this.pro_life = pro_life;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFk_user_id() {
        return fk_user_id;
    }

    public void setFk_user_id(String fk_user_id) {
        this.fk_user_id = fk_user_id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getReporting_methods() {
        return reporting_methods;
    }

    public void setReporting_methods(String reporting_methods) {
        this.reporting_methods = reporting_methods;
    }

    public String getHome_zip_code() {
        return home_zip_code;
    }

    public void setHome_zip_code(String home_zip_code) {
        this.home_zip_code = home_zip_code;
    }

    public String getThe_id() {
        return the_id;
    }

    public void setThe_id(String the_id) {
        this.the_id = the_id;
    }

    public String getPolitics() {
        return politics;
    }

    public void setPolitics(String politics) {
        this.politics = politics;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getWork_addr() {
        return work_addr;
    }

    public void setWork_addr(String work_addr) {
        this.work_addr = work_addr;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getHome_addr() {
        return home_addr;
    }

    public void setHome_addr(String home_addr) {
        this.home_addr = home_addr;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor_works() {
        return major_works;
    }

    public void setMajor_works(String major_works) {
        this.major_works = major_works;
    }

    public String getWork_zip_code() {
        return work_zip_code;
    }

    public void setWork_zip_code(String work_zip_code) {
        this.work_zip_code = work_zip_code;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPosition_title() {
        return position_title;
    }

    public void setPosition_title(String position_title) {
        this.position_title = position_title;
    }

    public String getWork_experience() {
        return work_experience;
    }

    public void setWork_experience(String work_experience) {
        this.work_experience = work_experience;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public Expert() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id_number);
        dest.writeString(this.count);
        dest.writeString(this.pro_life);
        dest.writeString(this.head_pic);
        dest.writeString(this.tel);
        dest.writeString(this.fk_user_id);
        dest.writeString(this.addr);
        dest.writeString(this.education);
        dest.writeString(this.reporting_methods);
        dest.writeString(this.home_zip_code);
        dest.writeString(this.the_id);
        dest.writeString(this.politics);
        dest.writeString(this.qualification);
        dest.writeString(this.level);
        dest.writeString(this.capacity);
        dest.writeString(this.experience);
        dest.writeString(this.industry);
        dest.writeString(this.note);
        dest.writeString(this.work_addr);
        dest.writeString(this.others);
        dest.writeString(this.home_addr);
        dest.writeString(this.user_name);
        dest.writeString(this.school);
        dest.writeString(this.degree);
        dest.writeString(this.major_works);
        dest.writeString(this.work_zip_code);
        dest.writeString(this.create_time);
        dest.writeString(this.position_title);
        dest.writeString(this.work_experience);
        dest.writeString(this.work_name);
    }

    protected Expert(Parcel in) {
        this.id_number = in.readString();
        this.count = in.readString();
        this.pro_life = in.readString();
        this.head_pic = in.readString();
        this.tel = in.readString();
        this.fk_user_id = in.readString();
        this.addr = in.readString();
        this.education = in.readString();
        this.reporting_methods = in.readString();
        this.home_zip_code = in.readString();
        this.the_id = in.readString();
        this.politics = in.readString();
        this.qualification = in.readString();
        this.level = in.readString();
        this.capacity = in.readString();
        this.experience = in.readString();
        this.industry = in.readString();
        this.note = in.readString();
        this.work_addr = in.readString();
        this.others = in.readString();
        this.home_addr = in.readString();
        this.user_name = in.readString();
        this.school = in.readString();
        this.degree = in.readString();
        this.major_works = in.readString();
        this.work_zip_code = in.readString();
        this.create_time = in.readString();
        this.position_title = in.readString();
        this.work_experience = in.readString();
        this.work_name = in.readString();
    }

    public static final Creator<Expert> CREATOR = new Creator<Expert>() {
        public Expert createFromParcel(Parcel source) {
            return new Expert(source);
        }

        public Expert[] newArray(int size) {
            return new Expert[size];
        }
    };
}
