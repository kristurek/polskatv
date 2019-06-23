package com.kristurek.polskatv.ui.event;

import com.kristurek.polskatv.ui.arch.Event;

public class EpgCurrentTimeEvent implements Event {

    private long epgCurrentTime;

    public EpgCurrentTimeEvent(long epgCurrentTime) {
        this.epgCurrentTime = epgCurrentTime;
    }

    public long getEpgCurrentTime() {
        return epgCurrentTime;
    }

    public void setEpgCurrentTime(long epgCurrentTime) {
        this.epgCurrentTime = epgCurrentTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EpgCurrentTimeEvent{");
        sb.append("epgCurrentTime=").append(epgCurrentTime);
        sb.append('}');
        return sb.toString();
    }
}
