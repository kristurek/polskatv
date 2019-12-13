
package com.kristurek.polskatv.iptv.polbox.pojo.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("text")
    @Expose
    private String text;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "code=" + code +
                ", text='" + text + '\'' +
                '}';
    }
}
