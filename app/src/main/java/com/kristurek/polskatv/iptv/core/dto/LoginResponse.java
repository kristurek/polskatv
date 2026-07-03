package com.kristurek.polskatv.iptv.core.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoginResponse {

    private int restOfDay;
    private String mediaServerId;
    private String bitrateId;
    private String httpCachingId;
    private int timeShift;
    private int timeZone;
    private String parentalPass;
    private String interfaceLang;
    private Map<String, String> mediaServers = new LinkedHashMap<>();
    private Map<String, String> bitrates = new LinkedHashMap<>();
    private Map<String, String> httpCachings = new LinkedHashMap<>();

    public int getRestOfDay() {
        return restOfDay;
    }

    public void setRestOfDay(int restOfDay) {
        this.restOfDay = restOfDay;
    }

    public String getMediaServerId() {
        return mediaServerId;
    }

    public void setMediaServerId(String mediaServerId) {
        this.mediaServerId = mediaServerId;
    }

    public String getBitrateId() {
        return bitrateId;
    }

    public void setBitrateId(String bitrateId) {
        this.bitrateId = bitrateId;
    }

    public String getHttpCachingId() {
        return httpCachingId;
    }

    public void setHttpCachingId(String httpCachingId) {
        this.httpCachingId = httpCachingId;
    }

    public int getTimeShift() {
        return timeShift;
    }

    public void setTimeShift(int timeShift) {
        this.timeShift = timeShift;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public String getParentalPass() {
        return parentalPass;
    }

    public void setParentalPass(String parentalPass) {
        this.parentalPass = parentalPass;
    }

    public String getInterfaceLang() {
        return interfaceLang;
    }

    public void setInterfaceLang(String interfaceLang) {
        this.interfaceLang = interfaceLang;
    }

    public Map<String, String> getMediaServers() {
        return mediaServers;
    }

    public void setMediaServers(Map<String, String> mediaServers) {
        this.mediaServers = mediaServers;
    }

    public Map<String, String> getBitrates() {
        return bitrates;
    }

    public void setBitrates(Map<String, String> bitrates) {
        this.bitrates = bitrates;
    }

    public Map<String, String> getHttpCachings() {
        return httpCachings;
    }

    public void setHttpCachings(Map<String, String> httpCachings) {
        this.httpCachings = httpCachings;
    }

    @Override
    public String toString() {
        return "LoginRetrofitResponse{" +
                " restOfDay=" + restOfDay +
                ", mediaServerId=" + mediaServerId +
                ", bitrateId=" + bitrateId +
                ", httpCachingId=" + httpCachingId +
                ", timeShift=" + timeShift +
                ", timeZone=" + timeZone +
                ", parentalPass=" + parentalPass +
                ", interfaceLang='" + interfaceLang + '\'' +
                ", mediaServers=" + mediaServers +
                ", bitrates=" + bitrates +
                ", httpCachings=" + httpCachings +
                '}';
    }
}
