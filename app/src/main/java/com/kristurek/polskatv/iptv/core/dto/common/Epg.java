package com.kristurek.polskatv.iptv.core.dto.common;


import com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType;

public class Epg {

    private long beginTime;
    private long endTime;
    private EpgType type;
    private String description;
    private String title;

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public EpgType getType() {
        return type;
    }

    public void setType(EpgType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Epg{" +
                "beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
