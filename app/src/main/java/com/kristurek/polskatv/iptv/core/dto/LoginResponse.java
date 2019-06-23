package com.kristurek.polskatv.iptv.core.dto;

import org.joda.time.LocalDate;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoginResponse {

    private String sid;
    private LocalDate beginDate;
    private LocalDate endDate;
    private int restOfDay;
    private int mediaServerId;
    private int timeShift;
    private int timeZone;
    private String parentalPass;
    private String interfaceLang;
    private Map<Integer, String> mediaServers = new LinkedHashMap<>();

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getRestOfDay() {
        return restOfDay;
    }

    public void setRestOfDay(int restOfDay) {
        this.restOfDay = restOfDay;
    }

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

    public Map<Integer, String> getMediaServers() {
        return mediaServers;
    }

    public void setMediaServers(Map<Integer, String> mediaServers) {
        this.mediaServers = mediaServers;
    }

    @Override
    public String toString() {
        return "LoginRetrofitResponse{" +
                "sid='" + sid + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", restOfDay=" + restOfDay +
                ", mediaServerId=" + mediaServerId +
                ", timeShift=" + timeShift +
                ", timeZone=" + timeZone +
                ", parentalPass=" + parentalPass +
                ", interfaceLang='" + interfaceLang + '\'' +
                ", mediaServers=" + mediaServers +
                '}';
    }
}
