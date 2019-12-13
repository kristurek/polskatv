
package com.kristurek.polskatv.iptv.polbox.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StreamServer {

    @SerializedName("list")
    @Expose
    private java.util.List<com.kristurek.polskatv.iptv.polbox.pojo.login.List> list = null;
    @SerializedName("value")
    @Expose
    private String value;

    public java.util.List<com.kristurek.polskatv.iptv.polbox.pojo.login.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.kristurek.polskatv.iptv.polbox.pojo.login.List> list) {
        this.list = list;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "StreamServer{" +
                "list=" + list +
                ", value='" + value + '\'' +
                '}';
    }
}
