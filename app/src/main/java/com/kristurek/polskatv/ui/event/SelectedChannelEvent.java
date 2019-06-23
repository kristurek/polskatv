package com.kristurek.polskatv.ui.event;

import com.kristurek.polskatv.ui.arch.Event;

public class SelectedChannelEvent implements Event {

    private int channelId;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SelectedChannelEvent{");
        sb.append("channelId=").append(channelId);
        sb.append('}');
        return sb.toString();
    }
}
