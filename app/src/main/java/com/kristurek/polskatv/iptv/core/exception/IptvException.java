package com.kristurek.polskatv.iptv.core.exception;

import com.kristurek.polskatv.iptv.core.exception.common.ExceptionModel;

public class IptvException extends Exception { //DOTO rename to IptvException

    private ExceptionModel exception;

    public IptvException() {
        super();
    }

    public IptvException(ExceptionModel exception) {
        super("Error message=[" + exception.getMessage() + "] code=[" + exception.getCode() + "]");
        this.exception = exception;
    }

    public IptvException(int code, String message) {
        super("Error message=[" + message + "] code=[" + code + "]");
    }

    public IptvException(String message) {
        super(message);
    }

    public IptvException(Throwable cause) {
        super(cause);
    }

    public IptvException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionModel getException() {
        return exception;
    }

    public void setException(ExceptionModel exception) {
        this.exception = exception;
    }

}