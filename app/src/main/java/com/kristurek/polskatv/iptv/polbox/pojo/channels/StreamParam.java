
package com.kristurek.polskatv.iptv.polbox.pojo.channels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StreamParam {

    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("ts")
    @Expose
    private Integer ts;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Integer getTs() {
        return ts;
    }

    public void setTs(Integer ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "StreamParam{" +
                "rate='" + rate + '\'' +
                ", ts=" + ts +
                '}';
    }
}
