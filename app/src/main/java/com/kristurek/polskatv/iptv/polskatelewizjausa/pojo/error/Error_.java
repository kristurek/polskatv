
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error_ {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Error_{");
        sb.append("code='").append(code).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append('}');

        return sb.toString();
    }
}
