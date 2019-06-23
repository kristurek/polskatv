
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.currentepgs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("next")
    @Expose
    private Next next;

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

    public Current getCurrent() {
        return current;
    }

    public Next getNext() {
        return next;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Channel{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", icon='").append(icon).append('\'');
        sb.append(", timeShift=").append(timeShift);
        sb.append(", current=").append(current);
        sb.append(", next=").append(next);
        sb.append('}');
        return sb.toString();
    }
}
