package com.kristurek.polskatv.ui.console.model;

import com.kristurek.polskatv.ui.epgs.model.EpgType;

public class ConsoleModel {

    private int channelId;
    private String channelName;
    private long epgBeginTime;
    private long epgEndTime;
    private long epgCurrentTime;
    private EpgType epgType;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public long getEpgBeginTime() {
        return epgBeginTime;
    }

    public void setEpgBeginTime(long epgBeginTime) {
        this.epgBeginTime = epgBeginTime;
    }

    public long getEpgEndTime() {
        return epgEndTime;
    }

    public void setEpgEndTime(long epgEndTime) {
        this.epgEndTime = epgEndTime;
    }

    public long getEpgCurrentTime() {
        return epgCurrentTime;
    }

    public void setEpgCurrentTime(long epgCurrentTime) {
        this.epgCurrentTime = epgCurrentTime;
    }

    public EpgType getEpgType() {
        return epgType;
    }

    public void setEpgType(EpgType epgType) {
        this.epgType = epgType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConsoleModel{");
        sb.append("channelId=").append(channelId);
        sb.append("channelName=").append(channelName);
        sb.append(", epgBeginTime=").append(epgBeginTime);
        sb.append(", epgEndTime=").append(epgEndTime);
        sb.append(", epgCurrentTime=").append(epgCurrentTime);
        sb.append(", epgType=").append(epgType);
        sb.append('}');
        return sb.toString();
    }
}
