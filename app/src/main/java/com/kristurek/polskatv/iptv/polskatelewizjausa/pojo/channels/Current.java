
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("begin")
    @Expose
    private Integer begin;
    @SerializedName("end")
    @Expose
    private Integer end;

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public Integer getBegin() {
        return begin;
    }

    public Integer getEnd() {
        return end;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Current{");
        sb.append("title='").append(title).append('\'');
        sb.append(", info='").append(info).append('\'');
        sb.append(", begin=").append(begin);
        sb.append(", end=").append(end);
        sb.append('}');
        return sb.toString();
    }
}
