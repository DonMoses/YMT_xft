package com.ymt.demo1.beams.expert_consult;

/**
 * Created by DonMoses on 2015/9/15
 */
public class OnDutyExpert extends Expert {

    private String name;
    private String userId;
    private String flag;
    private String day;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
