package com.kristurek.polskatv.ui.event;

import com.kristurek.polskatv.ui.arch.Event;

public class ReselectChannelEvent implements Event {

    private int channelId;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReselectChannelEvent{");
        sb.append("channelId=").append(channelId);
        sb.append('}');
        return sb.toString();
    }
}
