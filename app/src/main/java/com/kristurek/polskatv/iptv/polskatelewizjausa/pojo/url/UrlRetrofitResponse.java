package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.url;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;

public class UrlRetrofitResponse extends BaseRetrofitResponse {


    @SerializedName("url")
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UrlRetrofitResponse{");
        sb.append("url='").append(url).append('\'');
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
