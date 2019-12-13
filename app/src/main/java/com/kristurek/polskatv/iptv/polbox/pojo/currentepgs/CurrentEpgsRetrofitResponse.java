package com.kristurek.polskatv.iptv.polbox.pojo.currentepgs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kristurek.polskatv.iptv.polbox.pojo.common.BaseRetrofitResponse;

import java.util.List;

public class CurrentEpgsRetrofitResponse extends BaseRetrofitResponse {

    @SerializedName("epg")
    @Expose
    private List<Epg> epg = null;

    public List<Epg> getEpg() {
        return epg;
    }

    public void setEpg(List<Epg> epg) {
        this.epg = epg;
    }

    @Override
    public String toString() {
        return "CurrentEpgsRetrofitResponse{" +
                "epg=" + epg +
                '}';
    }
}
