
package com.kristurek.polskatv.iptv.polbox.pojo.currentepgs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Epg {

    @SerializedName("cid")
    @Expose
    private Integer cid;
    @SerializedName("epg")
    @Expose
    private List<Epg_> epg = null;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public List<Epg_> getEpg() {
        return epg;
    }

    public void setEpg(List<Epg_> epg) {
        this.epg = epg;
    }

    @Override
    public String toString() {
        return "Epg{" +
                "cid=" + cid +
                ", epg=" + epg +
                '}';
    }
}
