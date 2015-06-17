package com.ymt.demo1.beams.expert_consult;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/11
 */
public class QQChatInfo extends DataSupport {
    private String msg_title;

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    /**
     * qq_id
     */
    private String qq_id;
    /**
     * 会话时间
     */
    private String msg_time;
    /**
     * 用户id
     */
    private String fk_user_id;

    private String status;
    /**
     * 期望专家id
     */
    private String hoping_pro_id;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 合作厂商id
     */
    private String fk_company_id;
    /**
     * 电子报价
     */
    private String elec_price;
    /**
     * 资深专家id（当前会话专家id）
     */
    private String fk_pro_id;
    /**
     * 编号
     */
    private int msg_num;
    /**
     * 合同文件id
     */
    private String fk_contract_id;

    public String getQq_id() {
        return qq_id;
    }

    public void setQq_id(String id) {
        this.qq_id = id;
    }

    public String getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(String msg_time) {
        this.msg_time = msg_time;
    }

    public String getFk_user_id() {
        return fk_user_id;
    }

    public void setFk_user_id(String fk_user_id) {
        this.fk_user_id = fk_user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHoping_pro_id() {
        return hoping_pro_id;
    }

    public void setHoping_pro_id(String hoping_pro_id) {
        this.hoping_pro_id = hoping_pro_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFk_company_id() {
        return fk_company_id;
    }

    public void setFk_company_id(String fk_company_id) {
        this.fk_company_id = fk_company_id;
    }

    public String getElec_price() {
        return elec_price;
    }

    public void setElec_price(String elec_price) {
        this.elec_price = elec_price;
    }

    public String getFk_pro_id() {
        return fk_pro_id;
    }

    public void setFk_pro_id(String fk_pro_id) {
        this.fk_pro_id = fk_pro_id;
    }

    public int getMsg_num() {
        return msg_num;
    }

    public void setMsg_num(int msg_num) {
        this.msg_num = msg_num;
    }

    public String getFk_contract_id() {
        return fk_contract_id;
    }

    public void setFk_contract_id(String fk_contract_id) {
        this.fk_contract_id = fk_contract_id;
    }
}
