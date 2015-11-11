package com.ymt.demo1.beams.knowledge;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/4/29
 */
public class KnowledgeItem extends DataSupport implements Parcelable {
    private String auditorId;
    private String author;
    private int avrScor;
    private String docBrief;
    private String docLoacl;
    private String docTitle;
    private String docType;
    private int downTimes;
    private int downVal;
    private String editor;
    private String fileName;
    private String keyWord;
    private String kind;
    private String knowId;
    private String netType;
    private String passTime;
    private String prtKind;
    private int readTimes;
    private String reason;
    private int scorTimes;
    private String source;
    private String stat;
    private String type;
    private String upDateTime;
    private String userid;
    private String cover;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAvrScor() {
        return avrScor;
    }

    public void setAvrScor(int avrScor) {
        this.avrScor = avrScor;
    }

    public String getDocBrief() {
        return docBrief;
    }

    public void setDocBrief(String docBrief) {
        this.docBrief = docBrief;
    }

    public String getDocLoacl() {
        return docLoacl;
    }

    public void setDocLoacl(String docLoacl) {
        this.docLoacl = docLoacl;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public int getDownTimes() {
        return downTimes;
    }

    public void setDownTimes(int downTimes) {
        this.downTimes = downTimes;
    }

    public int getDownVal() {
        return downVal;
    }

    public void setDownVal(int downVal) {
        this.downVal = downVal;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKnowId() {
        return knowId;
    }

    public void setKnowId(String knowId) {
        this.knowId = knowId;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public String getPrtKind() {
        return prtKind;
    }

    public void setPrtKind(String prtKind) {
        this.prtKind = prtKind;
    }

    public int getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(int readTimes) {
        this.readTimes = readTimes;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getScorTimes() {
        return scorTimes;
    }

    public void setScorTimes(int scorTimes) {
        this.scorTimes = scorTimes;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpDateTime() {
        return upDateTime;
    }

    public void setUpDateTime(String upDateTime) {
        this.upDateTime = upDateTime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public KnowledgeItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.auditorId);
        dest.writeString(this.author);
        dest.writeInt(this.avrScor);
        dest.writeString(this.docBrief);
        dest.writeString(this.docLoacl);
        dest.writeString(this.docTitle);
        dest.writeString(this.docType);
        dest.writeInt(this.downTimes);
        dest.writeInt(this.downVal);
        dest.writeString(this.editor);
        dest.writeString(this.fileName);
        dest.writeString(this.keyWord);
        dest.writeString(this.kind);
        dest.writeString(this.knowId);
        dest.writeString(this.netType);
        dest.writeString(this.passTime);
        dest.writeString(this.prtKind);
        dest.writeInt(this.readTimes);
        dest.writeString(this.reason);
        dest.writeInt(this.scorTimes);
        dest.writeString(this.source);
        dest.writeString(this.stat);
        dest.writeString(this.type);
        dest.writeString(this.upDateTime);
        dest.writeString(this.userid);
        dest.writeString(this.cover);
    }

    protected KnowledgeItem(Parcel in) {
        this.auditorId = in.readString();
        this.author = in.readString();
        this.avrScor = in.readInt();
        this.docBrief = in.readString();
        this.docLoacl = in.readString();
        this.docTitle = in.readString();
        this.docType = in.readString();
        this.downTimes = in.readInt();
        this.downVal = in.readInt();
        this.editor = in.readString();
        this.fileName = in.readString();
        this.keyWord = in.readString();
        this.kind = in.readString();
        this.knowId = in.readString();
        this.netType = in.readString();
        this.passTime = in.readString();
        this.prtKind = in.readString();
        this.readTimes = in.readInt();
        this.reason = in.readString();
        this.scorTimes = in.readInt();
        this.source = in.readString();
        this.stat = in.readString();
        this.type = in.readString();
        this.upDateTime = in.readString();
        this.userid = in.readString();
        this.cover = in.readString();
    }

    public static final Creator<KnowledgeItem> CREATOR = new Creator<KnowledgeItem>() {
        public KnowledgeItem createFromParcel(Parcel source) {
            return new KnowledgeItem(source);
        }

        public KnowledgeItem[] newArray(int size) {
            return new KnowledgeItem[size];
        }
    };
}
