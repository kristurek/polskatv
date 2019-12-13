
package com.kristurek.polskatv.iptv.polbox.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kristurek.polskatv.iptv.polbox.pojo.common.BaseRetrofitResponse;

public class LoginRetrofitResponse extends BaseRetrofitResponse {

    @SerializedName("account")
    @Expose
    private Account account;
    @SerializedName("geo")
    @Expose
    private Geo geo;
    @SerializedName("ktv_dune_data_url")
    @Expose
    private String ktvDuneDataUrl;
    @SerializedName("messages")
    @Expose
    private Integer messages;
    @SerializedName("services")
    @Expose
    private Services services;
    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("sid")
    @Expose
    private String sid;
    @SerializedName("sid_name")
    @Expose
    private String sidName;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public String getKtvDuneDataUrl() {
        return ktvDuneDataUrl;
    }

    public void setKtvDuneDataUrl(String ktvDuneDataUrl) {
        this.ktvDuneDataUrl = ktvDuneDataUrl;
    }

    public Integer getMessages() {
        return messages;
    }

    public void setMessages(Integer messages) {
        this.messages = messages;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSidName() {
        return sidName;
    }

    public void setSidName(String sidName) {
        this.sidName = sidName;
    }

    @Override
    public String toString() {
        return "LoginRetrofitResponse{" +
                "account=" + account +
                ", geo=" + geo +
                ", ktvDuneDataUrl='" + ktvDuneDataUrl + '\'' +
                ", messages=" + messages +
                ", services=" + services +
                ", settings=" + settings +
                ", sid='" + sid + '\'' +
                ", sidName='" + sidName + '\'' +
                '}';
    }
}
