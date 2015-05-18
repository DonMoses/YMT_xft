package com.ymt.demo1.dbBeams;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/4/20
 */
public class Account extends DataSupport {
    private String phoneNum;
    private String accountName;
    private String password;
    private Bitmap header;
    String email;
    String qqID;

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