
package com.kristurek.polskatv.iptv.polbox.pojo.epgs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Epg {

    @SerializedName("progname")
    @Expose
    private String progname;
    @SerializedName("t_start")
    @Expose
    private String tStart;
    @SerializedName("ut_start")
    @Expose
    private Integer utStart;

    public String getProgname() {
        return progname;
    }

    public void setProgname(String progname) {
        this.progname = progname;
    }

    public String getTStart() {
        return tStart;
    }

    public void setTStart(String tStart) {
        this.tStart = tStart;
    }

    public Integer getUtStart() {
        return utStart;
    }

    public void setUtStart(Integer utStart) {
        this.utStart = utStart;
    }

    @Override
    public String toString() {
        return "Epg{" +
                " utStart=" + utStart +
                ", tStart='" + tStart + '\'' +
                ", progname='" + progname + '\'' +
                '}';
    }
}
