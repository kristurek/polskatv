
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("user_title")
    @Expose
    private String userTitle;
    @SerializedName("channels")
    @Expose
    private List<Channel> channels = null;

    public Integer getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getAlias() {
        return alias;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Group{");
        sb.append("id=").append(id);
        sb.append(", number=").append(number);
        sb.append(", alias='").append(alias).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", color='").append(color).append('\'');
        sb.append(", userTitle='").append(userTitle).append('\'');
        sb.append(", channels=").append(channels);
        sb.append('}');
        return sb.toString();
    }
}
