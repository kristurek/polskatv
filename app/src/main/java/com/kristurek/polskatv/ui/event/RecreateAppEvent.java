package com.kristurek.polskatv.ui.event;

import com.kristurek.polskatv.ui.arch.Event;

public class RecreateAppEvent implements Event {

    private int channelId;
    private String channelName;
    private long epgBeginTime;
    private long epgCurrentTime;

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

    public long getEpgCurrentTime() {
        return epgCurrentTime;
    }

    public void setEpgCurrentTime(long epgCurrentTime) {
        this.epgCurrentTime = epgCurrentTime;
    }

    public long getEpgBeginTime() {
        return epgBeginTime;
    }

    public void setEpgBeginTime(long epgBeginTime) {
        this.epgBeginTime = epgBeginTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RecreateAppEvent{");
        sb.append("channelId=").append(channelId);
        sb.append("channelName=").append(channelName);
        sb.append(", epgBeginTime=").append(epgBeginTime);
        sb.append(", epgCurrentTime=").append(epgCurrentTime);
        sb.append('}');
        return sb.toString();
    }
}
