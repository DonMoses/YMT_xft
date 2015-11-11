package com.ymt.demo1.beams.consult_cato;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/13
 */
public class ConsultCato extends DataSupport implements Parcelable {
    private int codeId;
    private String codeValue;
    private int codeType;

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public int getCodeType() {
        return codeType;
    }

    public void setCodeType(int codeType) {
        this.codeType = codeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.codeId);
        dest.writeString(this.codeValue);
        dest.writeInt(this.codeType);
    }

    public ConsultCato() {
    }

    protected ConsultCato(Parcel in) {
        this.codeId = in.readInt();
        this.codeValue = in.readString();
        this.codeType = in.readInt();
    }

    public static final Parcelable.Creator<ConsultCato> CREATOR = new Parcelable.Creator<ConsultCato>() {
        public ConsultCato createFromParcel(Parcel source) {
            return new ConsultCato(source);
        }

        public ConsultCato[] newArray(int size) {
            return new ConsultCato[size];
        }
    };
}
