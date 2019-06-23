
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.epgs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;

import java.util.List;

public class EpgsRetrofitResponse extends BaseRetrofitResponse {

    @SerializedName("icons")
    @Expose
    private Icons icons;
    @SerializedName("channels")
    @Expose
    private List<Channel> channels = null;

    public Icons getIcons() {
        return icons;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EpgsRetrofitResponse{");
        sb.append("icons=").append(icons);
        sb.append(", channels=").append(channels);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
