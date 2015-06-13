package com.ymt.demo1.beams.consult_cato;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/13
 */
public class ConsultCato extends DataSupport{
    private String code;
    private String note;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
