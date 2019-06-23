package com.kristurek.polskatv.iptv.core.dto;


import com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType;

public class UrlRequest {

    private int channelId;
    private long seekToTime;
    private String protectCode;
    private EpgType type;

    public UrlRequest() {
    }

    public UrlRequest(int channelId, long seekToTime, String protectCode, EpgType type) {
        this.channelId = channelId;
        this.seekToTime = seekToTime;
        this.protectCode = protectCode;
        this.type = type;
    }

    public UrlRequest(int channelId, String protectCode, EpgType type) {
        this.channelId = channelId;
        this.protectCode = protectCode;
        this.type = type;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public long getSeekToTime() {
        return seekToTime;
    }

    public void setSeekToTime(long seekToTime) {
        this.seekToTime = seekToTime;
    }

    public String getProtectCode() {
        return protectCode;
    }

    public void setProtectCode(String protectCode) {
        this.protectCode = protectCode;
    }

    public EpgType getType() {
        return type;
    }

    public void setType(EpgType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UrlRequest{" +
                "channelId=" + channelId +
                ", seekToTime=" + seekToTime +
                ", protectCode=" + protectCode +
                ", type=" + type +
                '}';
    }
}
