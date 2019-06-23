package com.kristurek.polskatv.ui.event;

import com.kristurek.polskatv.ui.arch.Event;
import com.kristurek.polskatv.ui.epgs.model.EpgType;

public class FindCurrentEpgEvent implements Event {

    private int channelId;
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
        final StringBuilder sb = new StringBuilder("FindCurrentEpgEvent{");
        sb.append("channelId=").append(channelId);
        sb.append(", epgBeginTime=").append(epgBeginTime);
        sb.append(", epgEndTime=").append(epgEndTime);
        sb.append(", epgCurrentTime=").append(epgCurrentTime);
        sb.append(", epgType=").append(epgType);
        sb.append('}');
        return sb.toString();
    }
}
