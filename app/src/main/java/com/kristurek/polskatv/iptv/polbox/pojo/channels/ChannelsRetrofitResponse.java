
package com.kristurek.polskatv.iptv.polbox.pojo.channels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kristurek.polskatv.iptv.polbox.pojo.common.BaseRetrofitResponse;

import java.util.List;

public class ChannelsRetrofitResponse extends BaseRetrofitResponse {

    @SerializedName("groups")
    @Expose
    private List<Group> groups = null;
    @SerializedName("messages")
    @Expose
    private Integer messages;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Integer getMessages() {
        return messages;
    }

    public void setMessages(Integer messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ChannelsRetrofitResponse{" +
                "groups=" + groups +
                ", messages=" + messages +
                '}';
    }
}
