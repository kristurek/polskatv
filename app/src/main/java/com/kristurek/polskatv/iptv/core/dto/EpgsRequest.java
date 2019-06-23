package com.kristurek.polskatv.iptv.core.dto;

import java.util.Set;

public class EpgsRequest {

    private Set<Integer> channelIds;
    private long fromBeginTime;

    public EpgsRequest() {
    }

    public EpgsRequest(Set<Integer> channelIds, long fromBeginTime) {
        this.channelIds = channelIds;
        this.fromBeginTime = fromBeginTime;
    }

    public Set<Integer> getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(Set<Integer> channelIds) {
        this.channelIds = channelIds;
    }

    public long getFromBeginTime() {
        return fromBeginTime;
    }

    public void setFromBeginTime(long fromBeginTime) {
        this.fromBeginTime = fromBeginTime;
    }

    @Override
    public String toString() {
        return "EpgsRequest{" +
                "channelIds=" + channelIds +
                ", fromBeginTime=" + fromBeginTime +
                '}';
    }
}
