package com.kristurek.polskatv.ui.player.model;

public class PlayerModel {

    private String url;
    private long epgCurrentTime;
    private String userAgent;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getEpgCurrentTime() {
        return epgCurrentTime;
    }

    public void setEpgCurrentTime(long epgCurrentTime) {
        this.epgCurrentTime = epgCurrentTime;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlayerModel{");
        sb.append("url='").append(url).append('\'');
        sb.append(", epgCurrentTime=").append(epgCurrentTime);
        sb.append(", userAgent='").append(userAgent).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
