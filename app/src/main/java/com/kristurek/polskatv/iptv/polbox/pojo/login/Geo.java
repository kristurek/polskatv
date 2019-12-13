
package com.kristurek.polskatv.iptv.polbox.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geo {

    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("region")
    @Expose
    private Region region;

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Geo{" +
                "countryId=" + countryId +
                ", ip='" + ip + '\'' +
                ", region=" + region +
                '}';
    }
}
