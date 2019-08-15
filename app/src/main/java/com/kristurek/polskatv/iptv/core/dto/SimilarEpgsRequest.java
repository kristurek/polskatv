package com.kristurek.polskatv.iptv.core.dto;

import java.util.Set;

public class SimilarEpgsRequest {

    private Set<Integer> channelIds;
    private String title;
    private long fromBeginTime;

    public SimilarEpgsRequest() {
    }

    public SimilarEpgsRequest(Set<Integer> channelIds, String title, long fromBeginTime) {
        this.channelIds = channelIds;
        this.title = title;
        this.fromBeginTime = fromBeginTime;
    }

    public Set<Integer> getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(Set<Integer> channelIds) {
        this.channelIds = channelIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getFromBeginTime() {
        return fromBeginTime;
    }

    public void setFromBeginTime(long fromBeginTime) {
        this.fromBeginTime = fromBeginTime;
    }

    @Override
    public String toString() {
        return "SimilarEpgsRequest{" +
                "channelIds=" + channelIds +
                ", title='" + title + '\'' +
                ", fromBeginTime='" + fromBeginTime + '\'' +
                '}';
    }
}
