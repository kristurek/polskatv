package com.kristurek.polskatv.iptv.core.dto;

import java.util.Set;

public class SimilarEpgsRequest {

    private Set<Integer> channelIds;
    private String title;

    public SimilarEpgsRequest() {
    }

    public SimilarEpgsRequest(Set<Integer> channelIds, String title) {
        this.channelIds = channelIds;
        this.title = title;
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

    @Override
    public String toString() {
        return "SimilarEpgsRequest{" +
                "channelIds=" + channelIds +
                ", title='" + title + '\'' +
                '}';
    }
}
