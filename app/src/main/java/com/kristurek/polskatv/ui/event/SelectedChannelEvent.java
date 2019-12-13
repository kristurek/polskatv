package com.kristurek.polskatv.ui.event;

import com.kristurek.polskatv.ui.arch.Event;

public class SelectedChannelEvent implements Event {

    private int channelId;
    private String channelName;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SelectedChannelEvent{");
        sb.append("channelId=").append(channelId);
        sb.append(" ,channelName=").append(channelName);
        sb.append('}');
        return sb.toString();
    }
}
