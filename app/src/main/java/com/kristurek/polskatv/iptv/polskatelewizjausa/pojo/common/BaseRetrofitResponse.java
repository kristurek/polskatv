package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseRetrofitResponse {

    @SerializedName("servertime")
    @Expose
    private Integer servertime;

    public Integer getServertime() {
        return servertime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseRetrofitResponse{");
        sb.append("servertime=").append(servertime);
        sb.append('}');

        return sb.toString();
    }
}
