
package com.kristurek.polskatv.iptv.polbox.pojo.currentepgs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Epg_ {

    @SerializedName("progname")
    @Expose
    private String progname;
    @SerializedName("ts")
    @Expose
    private String ts;

    public String getProgname() {
        return progname;
    }

    public void setProgname(String progname) {
        this.progname = progname;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "Epg_{" +
                "progname='" + progname + '\'' +
                ", ts='" + ts + '\'' +
                '}';
    }
}
