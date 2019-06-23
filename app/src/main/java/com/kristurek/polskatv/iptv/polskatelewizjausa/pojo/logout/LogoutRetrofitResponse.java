package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.logout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;

public class LogoutRetrofitResponse extends BaseRetrofitResponse {

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LogoutRetrofitResponse{");
        sb.append("message='").append(message).append('\'');
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}

