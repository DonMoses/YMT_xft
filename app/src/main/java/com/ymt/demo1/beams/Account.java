package com.ymt.demo1.beams;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/4/20
 */
public class Account extends DataSupport implements Parcelable {
    public static final String NORMAL_USER = "001";
    public static final String EXPORT_USER = "002";
    public static final String MEMBER_USER = "003";
    public static final String MANAGER_USER = "004";

    private String roleDesc;
    private String id;
    private String phoneNum;
    private String loginname;
    private String password;
    private String role;        //001一般用户,002注册用户,003专家用户,004管理者等
    private String headPic;
    private String sId;
    private int attCount;
    private int fansCount;
    String email;
    String qqID;


    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getAttCount() {
        return attCount;
    }

    public void setAttCount(int attCount) {
        this.attCount = attCount;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQqID() {
        return qqID;
    }

    public void setQqID(String qqID) {
        this.qqID = qqID;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Account() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.roleDesc);
        dest.writeString(this.id);
        dest.writeString(this.phoneNum);
        dest.writeString(this.loginname);
        dest.writeString(this.password);
        dest.writeString(this.role);
        dest.writeString(this.headPic);
        dest.writeString(this.sId);
        dest.writeInt(this.attCount);
        dest.writeInt(this.fansCount);
        dest.writeString(this.email);
        dest.writeString(this.qqID);
    }

    protected Account(Parcel in) {
        this.roleDesc = in.readString();
        this.id = in.readString();
        this.phoneNum = in.readString();
        this.loginname = in.readString();
        this.password = in.readString();
        this.role = in.readString();
        this.headPic = in.readString();
        this.sId = in.readString();
        this.attCount = in.readInt();
        this.fansCount = in.readInt();
        this.email = in.readString();
        this.qqID = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}