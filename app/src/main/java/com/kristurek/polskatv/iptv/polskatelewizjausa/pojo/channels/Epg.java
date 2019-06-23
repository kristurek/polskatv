
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Epg {

    @SerializedName("time_shift")
    @Expose
    private Integer timeShift;
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("next")
    @Expose
    private Next next;

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
        final StringBuilder sb = new StringBuilder("Epg{");
        sb.append("timeShift=").append(timeShift);
        sb.append(", current=").append(current);
        sb.append(", next=").append(next);
        sb.append('}');
        return sb.toString();
    }
}
