
package com.kristurek.polskatv.iptv.polbox.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("descr")
    @Expose
    private String descr;
    @SerializedName("ip")
    @Expose
    private String ip;

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "List{" +
                "descr='" + descr + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
