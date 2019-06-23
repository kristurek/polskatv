package com.kristurek.polskatv.ui.epg.model;

public class EpgModel {

    private long epgBeginTime;
    private long epgEndTime;
    private long epgCurrentTime;

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

    public void setEpgCurrentTime(long epgCurrentTime) {
        this.epgCurrentTime = epgCurrentTime;
    }

    public long getEpgCurrentTime() {
        return epgCurrentTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EpgModel{");
        sb.append(", epgBeginTime=").append(epgBeginTime);
        sb.append(", epgEndTime=").append(epgEndTime);
        sb.append(", epgCurrentTime=").append(epgCurrentTime);
        sb.append('}');
        return sb.toString();
    }

}
