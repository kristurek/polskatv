
package com.kristurek.polskatv.iptv.polbox.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("packet_expire")
    @Expose
    private Integer packetExpire;
    @SerializedName("packet_id")
    @Expose
    private String packetId;
    @SerializedName("packet_name")
    @Expose
    private String packetName;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getPacketExpire() {
        return packetExpire;
    }

    public void setPacketExpire(Integer packetExpire) {
        this.packetExpire = packetExpire;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getPacketName() {
        return packetName;
    }

    public void setPacketName(String packetName) {
        this.packetName = packetName;
    }

    @Override
    public String toString() {
        return "Account{" +
                "login='" + login + '\'' +
                ", packetExpire=" + packetExpire +
                ", packetId='" + packetId + '\'' +
                ", packetName='" + packetName + '\'' +
                '}';
    }
}
