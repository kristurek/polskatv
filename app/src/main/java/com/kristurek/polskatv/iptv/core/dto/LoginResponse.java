package com.kristurek.polskatv.iptv.core.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoginResponse {

    private int restOfDay;
    private String mediaServerId;
    private int timeShift;
    private int timeZone;
    private String parentalPass;
    private String interfaceLang;
    private Map<String, String> mediaServers = new LinkedHashMap<>();

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

    @Override
    public String toString() {
        return "LoginRetrofitResponse{" +
                " restOfDay=" + restOfDay +
                ", mediaServerId=" + mediaServerId +
                ", timeShift=" + timeShift +
                ", timeZone=" + timeZone +
                ", parentalPass=" + parentalPass +
                ", interfaceLang='" + interfaceLang + '\'' +
                ", mediaServers=" + mediaServers +
                '}';
    }
}
