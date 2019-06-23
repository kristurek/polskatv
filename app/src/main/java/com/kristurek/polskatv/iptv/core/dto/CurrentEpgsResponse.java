package com.kristurek.polskatv.iptv.core.dto;

import com.kristurek.polskatv.iptv.core.dto.common.Channel;

import java.util.List;

public class CurrentEpgsResponse {

    List<Channel> channels;

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public String toString() {
        return "CurrentEpgsRetrofitResponse{" +
                "channels=" + channels +
                '}';
    }
}
