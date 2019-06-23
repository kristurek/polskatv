
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.currentepgs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Icons {

    @SerializedName("default")
    @Expose
    private String _default;
    @SerializedName("w40h30")
    @Expose
    private String w40h30;
    @SerializedName("playback")
    @Expose
    private String playback;
    @SerializedName("small")
    @Expose
    private String small;

    public String getDefault() {
        return _default;
    }

    public String getW40h30() {
        return w40h30;
    }

    public String getPlayback() {
        return playback;
    }

    public String getSmall() {
        return small;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Icons{");
        sb.append("_default='").append(_default).append('\'');
        sb.append(", w40h30='").append(w40h30).append('\'');
        sb.append(", playback='").append(playback).append('\'');
        sb.append(", small='").append(small).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
