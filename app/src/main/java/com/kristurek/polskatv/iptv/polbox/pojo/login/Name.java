
package com.kristurek.polskatv.iptv.polbox.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Name {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("val")
    @Expose
    private String val;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "Name{" +
                "title='" + title + '\'' +
                ", val='" + val + '\'' +
                '}';
    }
}
