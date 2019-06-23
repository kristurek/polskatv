package com.kristurek.polskatv.iptv.core.dto;

import java.util.HashMap;
import java.util.Map;

public class SettingsResponse {

    private int mediaServerId;
    private int timeShift;
    private int timeZone;
    private int parentalPass;
    private String interfaceLang;
    private Map<Integer, String> mediaServers = new HashMap<>();

    public int getMediaServerId() {
        return mediaServerId;
    }

    public void setMediaServerId(int mediaServerId) {
        this.mediaServerId = mediaServerId;
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

    public int getParentalPass() {
        return parentalPass;
    }

    public void setParentalPass(int parentalPass) {
        this.parentalPass = parentalPass;
    }

    public String getInterfaceLang() {
        return interfaceLang;
    }

    public void setInterfaceLang(String interfaceLang) {
        this.interfaceLang = interfaceLang;
    }

    public Map<Integer, String> getMediaServers() {
        return mediaServers;
    }

    public void setMediaServers(Map<Integer, String> mediaServers) {
        this.mediaServers = mediaServers;
    }

    @Override
    public String toString() {
        return "SettingsRetrofitResponse{" +
                ", mediaServerId=" + mediaServerId +
                ", timeShift=" + timeShift +
                ", timeZone=" + timeZone +
                ", parentalPass=" + parentalPass +
                ", interfaceLang='" + interfaceLang + '\'' +
                ", mediaServers=" + mediaServers +
                '}';
    }
}
