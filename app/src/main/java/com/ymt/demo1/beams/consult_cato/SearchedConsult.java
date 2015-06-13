package com.ymt.demo1.beams.consult_cato;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/5/4
 */
public class SearchedConsult extends DataSupport {
    private String article_id;
    private String article_title;
    private String article_content;
    private String fk_expert_id;
    private String create_time;
    private String hitnum;
    private String consult_key_word;

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_content() {
        return article_content;
    }

    public void setArticle_content(String article_content) {
        this.article_content = article_content;
    }

    public String getFk_expert_id() {
        return fk_expert_id;
    }

    public void setFk_expert_id(String fk_expert_id) {
        this.fk_expert_id = fk_expert_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getHitnum() {
        return hitnum;
    }

    public void setHitnum(String hitnum) {
        this.hitnum = hitnum;
    }

    public String getConsult_key_word() {
        return consult_key_word;
    }

    public void setConsult_key_word(String consult_key_word) {
        this.consult_key_word = consult_key_word;
    }
}
