package com.ymt.demo1.beams;

/**
 * Created by Dan on 2015/4/9
 */
public class TestInfo {

    private boolean isNewest;
    private String title;
    private String count;
    private String totalTime;
    private String totalScore;
    private String watchedCount;
    private String collectedCount;

    public boolean isNewest() {
        return isNewest;
    }

    public void setNewest(boolean isNewest) {
        this.isNewest = isNewest;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getWatchedCount() {
        return watchedCount;
    }

    public void setWatchedCount(String watchedCount) {
        this.watchedCount = watchedCount;
    }

    public String getCollectedCount() {
        return collectedCount;
    }

    public void setCollectedCount(String collectedCount) {
        this.collectedCount = collectedCount;
    }
}
