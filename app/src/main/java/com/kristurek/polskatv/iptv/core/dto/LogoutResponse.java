package com.kristurek.polskatv.iptv.core.dto;

public class LogoutResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LogoutRetrofitResponse{" +
                "message='" + message + '\'' +
                '}';
    }

}
