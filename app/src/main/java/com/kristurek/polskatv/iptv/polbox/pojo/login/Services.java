
package com.kristurek.polskatv.iptv.polbox.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Services {

    @SerializedName("archive")
    @Expose
    private Integer archive;
    @SerializedName("vod")
    @Expose
    private Integer vod;

    public Integer getArchive() {
        return archive;
    }

    public void setArchive(Integer archive) {
        this.archive = archive;
    }

    public Integer getVod() {
        return vod;
    }

    public void setVod(Integer vod) {
        this.vod = vod;
    }

    @Override
    public String toString() {
        return "Services{" +
                "archive=" + archive +
                ", vod=" + vod +
                '}';
    }
}
