package com.ymt.demo1.beams;

/**
 * Created by Dan on 2015/5/4
 */
public class SearchedConsultInfo {
    private String consultTitle;
    private String consultAnswer;
    private int collectedCount;
    private int commentCount;
    private boolean hasCollected;
    private boolean hasCommented;

    public boolean isHasCollected() {
        return hasCollected;
    }

    public void setHasCollected(boolean hasCollected) {
        this.hasCollected = hasCollected;
    }

    public boolean isHasCommented() {
        return hasCommented;
    }

    public void setHasCommented(boolean hasCommented) {
        this.hasCommented = hasCommented;
    }

    public String getConsultTitle() {
        return consultTitle;
    }

    public void setConsultTitle(String consultTitle) {
        this.consultTitle = consultTitle;
    }

    public String getConsultAnswer() {
        return consultAnswer;
    }

    public void setConsultAnswer(String consultAnswer) {
        this.consultAnswer = consultAnswer;
    }

    public int getCollectedCount() {
        return collectedCount;
    }

    public void setCollectedCount(int collectedCount) {
        this.collectedCount = collectedCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
