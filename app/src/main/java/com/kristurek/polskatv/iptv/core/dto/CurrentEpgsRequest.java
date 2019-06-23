package com.kristurek.polskatv.iptv.core.dto;

import java.util.Collection;

public class CurrentEpgsRequest {

    private Collection<Integer> channelIds;

    public CurrentEpgsRequest() {
    }

    public CurrentEpgsRequest(Collection<Integer> channelIds) {
        this.channelIds = channelIds;
    }

    public Collection<Integer> getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(Collection<Integer> channelIds) {
        this.channelIds = channelIds;
    }

    @Override
    public String toString() {
        return "CurrentEpgsRequest{" +
                "channelIds=" + channelIds +
                '}';
    }
}
