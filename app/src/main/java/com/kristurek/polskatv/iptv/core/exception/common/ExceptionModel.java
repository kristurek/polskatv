package com.kristurek.polskatv.iptv.core.exception.common;

public class ExceptionModel {

    private String message;
    private String code;

    public ExceptionModel() {
        super();
    }

    public ExceptionModel(String message, String code) {
        super();
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ExceptionModel [message=" + message + ", code=" + code + "]";
    }
}
