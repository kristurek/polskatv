
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.epgs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Epg {

    @SerializedName("wait")
    @Expose
    private Integer wait;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("has_archive")
    @Expose
    private Integer hasArchive;
    @SerializedName("begin")
    @Expose
    private Integer begin;
    @SerializedName("end")
    @Expose
    private Integer end;

    public Integer getWait() {
        return wait;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public Integer getHasArchive() {
        return hasArchive;
    }

    public Integer getBegin() {
        return begin;
    }

    public Integer getEnd() {
        return end;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Epg{");
        sb.append("wait=").append(wait);
        sb.append(", title='").append(title).append('\'');
        sb.append(", info='").append(info).append('\'');
        sb.append(", hasArchive=").append(hasArchive);
        sb.append(", begin=").append(begin);
        sb.append(", end=").append(end);
        sb.append('}');
        return sb.toString();
    }
}
