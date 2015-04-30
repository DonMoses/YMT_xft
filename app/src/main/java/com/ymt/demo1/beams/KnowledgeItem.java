package com.ymt.demo1.beams;

import com.ymt.demo1.plates.knowledge.KnowledgeItemType;

/**
 * Created by Dan on 2015/4/29
 */
public class KnowledgeItem {
    private String title;
    private int iconSrc;
    private String contentTxt;
    private KnowledgeItemType knowledgeItemType;

    public KnowledgeItemType getKnowledgeItemType() {
        return knowledgeItemType;
    }

    public void setKnowledgeItemType(KnowledgeItemType knowledgeItemType) {
        this.knowledgeItemType = knowledgeItemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconSrc() {
        return iconSrc;
    }

    public void setIconSrc(int iconSrc) {
        this.iconSrc = iconSrc;
    }

    public String getContentTxt() {
        return contentTxt;
    }

    public void setContentTxt(String contentTxt) {
        this.contentTxt = contentTxt;
    }
}
