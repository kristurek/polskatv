
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;

public class LoginRetrofitResponse extends BaseRetrofitResponse {

    @SerializedName("sid")
    @Expose
    private String sid;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("account")
    @Expose
    private Account account;
    @SerializedName("settings")
    @Expose
    private Settings settings;

    public String getSid() {
        return sid;
    }

    public String getVersion() {
        return version;
    }

    public Account getAccount() {
        return account;
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LoginRetrofitResponse{");
        sb.append("sid='").append(sid).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", account=").append(account);
        sb.append(", settings=").append(settings);
        sb.append(", ").append(super.toString());
        sb.append('}');

        return sb.toString();
    }
}
