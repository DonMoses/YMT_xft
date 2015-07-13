package com.ymt.demo1.beams.expert_consult;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dan on 2015/7/13
 */
public class FollowedExpert implements Parcelable {
    private String fk_expert_id;
    private String the_id;
    private String expert_type;
    private String expert_head_pic;
    private String create_time;
    private String fk_user_id;
    private String expert_experience;
    private String expert_name;

    public String getFk_expert_id() {
        return fk_expert_id;
    }

    public void setFk_expert_id(String fk_expert_id) {
        this.fk_expert_id = fk_expert_id;
    }

    public String getThe_id() {
        return the_id;
    }

    public void setThe_id(String the_id) {
        this.the_id = the_id;
    }

    public String getExpert_type() {
        return expert_type;
    }

    public void setExpert_type(String expert_type) {
        this.expert_type = expert_type;
    }

    public String getExpert_head_pic() {
        return expert_head_pic;
    }

    public void setExpert_head_pic(String expert_head_pic) {
        this.expert_head_pic = expert_head_pic;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFk_user_id() {
        return fk_user_id;
    }

    public void setFk_user_id(String fk_user_id) {
        this.fk_user_id = fk_user_id;
    }

    public String getExpert_experience() {
        return expert_experience;
    }

    public void setExpert_experience(String expert_experience) {
        this.expert_experience = expert_experience;
    }

    public String getExpert_name() {
        return expert_name;
    }

    public void setExpert_name(String expert_name) {
        this.expert_name = expert_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fk_expert_id);
        dest.writeString(this.the_id);
        dest.writeString(this.expert_type);
        dest.writeString(this.expert_head_pic);
        dest.writeString(this.create_time);
        dest.writeString(this.fk_user_id);
        dest.writeString(this.expert_experience);
        dest.writeString(this.expert_name);
    }

    public FollowedExpert() {
    }

    protected FollowedExpert(Parcel in) {
        this.fk_expert_id = in.readString();
        this.the_id = in.readString();
        this.expert_type = in.readString();
        this.expert_head_pic = in.readString();
        this.create_time = in.readString();
        this.fk_user_id = in.readString();
        this.expert_experience = in.readString();
        this.expert_name = in.readString();
    }

    public static final Parcelable.Creator<FollowedExpert> CREATOR = new Parcelable.Creator<FollowedExpert>() {
        public FollowedExpert createFromParcel(Parcel source) {
            return new FollowedExpert(source);
        }

        public FollowedExpert[] newArray(int size) {
            return new FollowedExpert[size];
        }
    };
}
