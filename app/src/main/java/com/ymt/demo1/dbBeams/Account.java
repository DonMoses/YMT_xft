package com.ymt.demo1.dbBeams;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/4/20
 */
public class Account extends DataSupport {
    public static final String NORMAL_USER = "001";
    public static final String EXPORT_USER = "002";
    public static final String MEMBER_USER = "003";
    public static final String MANAGER_USER = "004";

    private String userId;
    private String phoneNum;
    private String accountName;
    private String password;
    private String userType;        //001一般用户,002注册用户,003专家用户,004管理者等
    private Bitmap header;
    String email;
    String qqID;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Bitmap getHeader() {
        return header;
    }

    public void setHeader(Bitmap header) {
        this.header = header;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
}