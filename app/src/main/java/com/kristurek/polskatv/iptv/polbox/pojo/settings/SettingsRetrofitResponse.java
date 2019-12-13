
package com.kristurek.polskatv.iptv.polbox.pojo.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kristurek.polskatv.iptv.polbox.pojo.common.BaseRetrofitResponse;

public class SettingsRetrofitResponse extends BaseRetrofitResponse {

    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("messages")
    @Expose
    private Integer messages;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Integer getMessages() {
        return messages;
    }

    public void setMessages(Integer messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "SettingsRetrofitResponse{" +
                "message=" + message +
                ", messages=" + messages +
                '}';
    }
}
