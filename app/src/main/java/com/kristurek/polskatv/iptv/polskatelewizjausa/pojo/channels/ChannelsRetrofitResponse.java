
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;

import java.util.List;

public class ChannelsRetrofitResponse extends BaseRetrofitResponse {

    @SerializedName("icons")
    @Expose
    private Icons icons;
    @SerializedName("groups")
    @Expose
    private List<Group> groups = null;

    public Icons getIcons() {
        return icons;
    }

    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChannelsRetrofitResponse{");
        sb.append("icons=").append(icons);
        sb.append(", groups=").append(groups);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
