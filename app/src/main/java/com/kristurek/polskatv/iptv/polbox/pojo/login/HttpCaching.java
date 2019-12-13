
package com.kristurek.polskatv.iptv.polbox.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HttpCaching {

    @SerializedName("list")
    @Expose
    private List<Integer> list = null;
    @SerializedName("value")
    @Expose
    private String value;

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
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
        return "HttpCaching{" +
                "list=" + list +
                ", value='" + value + '\'' +
                '}';
    }
}
