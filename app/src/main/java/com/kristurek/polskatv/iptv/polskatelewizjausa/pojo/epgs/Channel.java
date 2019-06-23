
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.epgs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Channel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("time_shift")
    @Expose
    private Integer timeShift;
    @SerializedName("epg")
    @Expose
    private List<Epg> epg = null;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public Integer getTimeShift() {
        return timeShift;
    }

    public List<Epg> getEpg() {
        return epg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Channel{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", icon='").append(icon).append('\'');
        sb.append(", timeShift=").append(timeShift);
        sb.append(", epg=").append(epg);
        sb.append('}');
        return sb.toString();
    }
}
