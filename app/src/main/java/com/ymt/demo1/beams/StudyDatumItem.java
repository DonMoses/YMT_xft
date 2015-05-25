package com.ymt.demo1.beams;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Dan on 2015/5/22
 */
public class StudyDatumItem extends DataSupport implements Parcelable, Serializable {

    public enum TypeO {
        WORD, PPT, PDF, MP3
    }

    private TypeO typeO;        //type
    private String title;       //标题
    private String content;     //内容
    private float fileSize;     //文件大小
    private int requiredIntegral;       //所需积分

    public float getFileSize() {
        return fileSize;
    }

    public void setFileSize(float fileSize) {
        this.fileSize = fileSize;
    }

    public int getRequiredIntegral() {
        return requiredIntegral;
    }

    public void setRequiredIntegral(int requiredIntegral) {
        this.requiredIntegral = requiredIntegral;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TypeO getTypeO() {
        return typeO;
    }

    public void setTypeO(TypeO typeO) {
        this.typeO = typeO;
    }


    public StudyDatumItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.typeO == null ? -1 : this.typeO.ordinal());
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeFloat(this.fileSize);
        dest.writeInt(this.requiredIntegral);
    }

    private StudyDatumItem(Parcel in) {
        int tmpTypeO = in.readInt();
        this.typeO = tmpTypeO == -1 ? null : TypeO.values()[tmpTypeO];
        this.title = in.readString();
        this.content = in.readString();
        this.fileSize = in.readFloat();
        this.requiredIntegral = in.readInt();
    }

    public static final Creator<StudyDatumItem> CREATOR = new Creator<StudyDatumItem>() {
        public StudyDatumItem createFromParcel(Parcel source) {
            return new StudyDatumItem(source);
        }

        public StudyDatumItem[] newArray(int size) {
            return new StudyDatumItem[size];
        }
    };
}
