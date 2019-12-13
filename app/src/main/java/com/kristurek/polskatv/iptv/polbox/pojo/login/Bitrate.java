
package com.kristurek.polskatv.iptv.polbox.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bitrate {

    @SerializedName("list")
    @Expose
    private List<String> list = null;
    @SerializedName("names")
    @Expose
    private List<Name> names = null;
    @SerializedName("value")
    @Expose
    private String value;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Bitrate{" +
                "list=" + list +
                ", names=" + names +
                ", value='" + value + '\'' +
                '}';
    }
}
