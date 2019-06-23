package com.kristurek.polskatv.ui.event;

import com.kristurek.polskatv.ui.arch.Event;

public class SelectedEpgEvent extends SeekToTimeEvent implements Event {

    private String channelName;
    private String epgTitle;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getEpgTitle() {
        return epgTitle;
    }

    public void setEpgTitle(String epgTitle) {
        this.epgTitle = epgTitle;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SelectedEpgEvent{");
        sb.append("channelName='").append(channelName).append('\'');
        sb.append(", epgTitle='").append(epgTitle).append('\'');
        sb.append(", super=").append(super.toString());
        sb.append("}");
        return sb.toString();
    }
}
