package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/16
 */
public class Expert extends DataSupport implements Parcelable {
   private String reportingMethods;
    private String workAddr;
    private String majorWorks;
    private int count;
    private String tel;
    private String goods;
    private String addr;
    private String education;
    private String userType;
    private String politics;
    private String qualification;
    private String username;
    private String level;
    private int fkUserId;
    private String codeValue;
    private String workName;
    private String capacity;
    private String experience;
    private int industry;
    private String proLife;
    private String honoraryTitle;
    private int waitCount;
    private String headPic;
    private String certificateNumber;
    private String degree;
    private String school;
    private String workExperience;
    private String positionTitle;

    public String getReportingMethods() {
        return reportingMethods;
    }

    public void setReportingMethods(String reportingMethods) {
        this.reportingMethods = reportingMethods;
    }

    public String getWorkAddr() {
        return workAddr;
    }

    public void setWorkAddr(String workAddr) {
        this.workAddr = workAddr;
    }

    public String getMajorWorks() {
        return majorWorks;
    }

    public void setMajorWorks(String majorWorks) {
        this.majorWorks = majorWorks;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(int fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
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

    public int getIndustry() {
        return industry;
    }

    public void setIndustry(int industry) {
        this.industry = industry;
    }

    public String getProLife() {
        return proLife;
    }

    public void setProLife(String proLife) {
        this.proLife = proLife;
    }

    public String getHonoraryTitle() {
        return honoraryTitle;
    }

    public void setHonoraryTitle(String honoraryTitle) {
        this.honoraryTitle = honoraryTitle;
    }

    public int getWaitCount() {
        return waitCount;
    }

    public void setWaitCount(int waitCount) {
        this.waitCount = waitCount;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reportingMethods);
        dest.writeString(this.workAddr);
        dest.writeString(this.majorWorks);
        dest.writeInt(this.count);
        dest.writeString(this.tel);
        dest.writeString(this.goods);
        dest.writeString(this.addr);
        dest.writeString(this.education);
        dest.writeString(this.userType);
        dest.writeString(this.politics);
        dest.writeString(this.qualification);
        dest.writeString(this.username);
        dest.writeString(this.level);
        dest.writeInt(this.fkUserId);
        dest.writeString(this.codeValue);
        dest.writeString(this.workName);
        dest.writeString(this.capacity);
        dest.writeString(this.experience);
        dest.writeInt(this.industry);
        dest.writeString(this.proLife);
        dest.writeString(this.honoraryTitle);
        dest.writeInt(this.waitCount);
        dest.writeString(this.headPic);
        dest.writeString(this.certificateNumber);
        dest.writeString(this.degree);
        dest.writeString(this.school);
        dest.writeString(this.workExperience);
        dest.writeString(this.positionTitle);
    }

    public Expert() {
    }

    protected Expert(Parcel in) {
        this.reportingMethods = in.readString();
        this.workAddr = in.readString();
        this.majorWorks = in.readString();
        this.count = in.readInt();
        this.tel = in.readString();
        this.goods = in.readString();
        this.addr = in.readString();
        this.education = in.readString();
        this.userType = in.readString();
        this.politics = in.readString();
        this.qualification = in.readString();
        this.username = in.readString();
        this.level = in.readString();
        this.fkUserId = in.readInt();
        this.codeValue = in.readString();
        this.workName = in.readString();
        this.capacity = in.readString();
        this.experience = in.readString();
        this.industry = in.readInt();
        this.proLife = in.readString();
        this.honoraryTitle = in.readString();
        this.waitCount = in.readInt();
        this.headPic = in.readString();
        this.certificateNumber = in.readString();
        this.degree = in.readString();
        this.school = in.readString();
        this.workExperience = in.readString();
        this.positionTitle = in.readString();
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
