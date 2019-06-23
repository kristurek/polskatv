package com.kristurek.polskatv.ui.epgs.model;

import org.joda.time.LocalDate;

import java.io.Serializable;

public class EpgModel implements Serializable {

    private Integer channelId;
    private String channelName;

    private String description;
    private String time;
    private String title;
    private int icon;
    private Long beginTime;
    private Long endTime;
    private EpgType type;
    private LocalDate day;

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public EpgType getType() {
        return type;
    }

    public void setType(EpgType type) {
        this.type = type;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EpgModel{");
        sb.append("channelId=").append(channelId);
        sb.append(", channelName='").append(channelName).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", time='").append(time).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", icon=").append(icon);
        sb.append(", beginTime=").append(beginTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", type=").append(type);
        sb.append(", day=").append(day);
        sb.append('}');
        return sb.toString();
    }
}
