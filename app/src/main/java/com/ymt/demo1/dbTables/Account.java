package com.ymt.demo1.dbTables;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/4/20
 */
public class Account extends DataSupport {
    private String phoneNum;
    private String accountName;
    private String password;
    String email;
    String qqID;

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
