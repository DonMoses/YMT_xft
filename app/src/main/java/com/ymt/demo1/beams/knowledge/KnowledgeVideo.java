package com.ymt.demo1.beams.knowledge;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/6/16
 */
public class KnowledgeVideo extends DataSupport {
    private String the_id;
    private String content;
    private String files;
    private String article_title;
    private String status;
    private String create_time;
    private String meta_keys;
    private String score;
    private String fk_create_user_id;
    private String hitnum;
    private String downcount;

    public String getThe_id() {
        return the_id;
    }

    public void setThe_id(String the_id) {
        this.the_id = the_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getMeta_keys() {
        return meta_keys;
    }

    public void setMeta_keys(String meta_keys) {
        this.meta_keys = meta_keys;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getFk_create_user_id() {
        return fk_create_user_id;
    }

    public void setFk_create_user_id(String fk_create_user_id) {
        this.fk_create_user_id = fk_create_user_id;
    }

    public String getHitnum() {
        return hitnum;
    }

    public void setHitnum(String hitnum) {
        this.hitnum = hitnum;
    }

    public String getDowncount() {
        return downcount;
    }

    public void setDowncount(String downcount) {
        this.downcount = downcount;
    }
}
