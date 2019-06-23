package com.kristurek.polskatv.iptv.core.dto;

public class UrlResponse {

    private String url;
    private String userAgent;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UrlRetrofitResponse{");
        sb.append("url='").append(url).append('\'');
        sb.append(", userAgent='").append(userAgent).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
