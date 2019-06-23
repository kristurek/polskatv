
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;

import java.util.List;

public class SettingsRetrofitResponse extends BaseRetrofitResponse {

    @SerializedName("time_zone")
    @Expose
    private Integer timeZone;
    @SerializedName("time_shift")
    @Expose
    private Integer timeShift;
    @SerializedName("interface_lng")
    @Expose
    private String interfaceLng;
    @SerializedName("parental_pass")
    @Expose
    private Integer parentalPass;
    @SerializedName("media_server_id")
    @Expose
    private Integer mediaServerId;
    @SerializedName("stb_volume")
    @Expose
    private Integer stbVolume;
    @SerializedName("stb_bright")
    @Expose
    private Integer stbBright;
    @SerializedName("stb_contrast")
    @Expose
    private Integer stbContrast;
    @SerializedName("stb_saturation")
    @Expose
    private Integer stbSaturation;
    @SerializedName("stb_buffer")
    @Expose
    private Integer stbBuffer;
    @SerializedName("media_servers")
    @Expose
    private List<MediaServer> mediaServers = null;

    public Integer getTimeZone() {
        return timeZone;
    }

    public Integer getTimeShift() {
        return timeShift;
    }

    public String getInterfaceLng() {
        return interfaceLng;
    }

    public Integer getParentalPass() {
        return parentalPass;
    }

    public Integer getMediaServerId() {
        return mediaServerId;
    }

    public Integer getStbVolume() {
        return stbVolume;
    }

    public Integer getStbBright() {
        return stbBright;
    }

    public Integer getStbContrast() {
        return stbContrast;
    }

    public Integer getStbSaturation() {
        return stbSaturation;
    }

    public Integer getStbBuffer() {
        return stbBuffer;
    }

    public List<MediaServer> getMediaServers() {
        return mediaServers;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SettingsRetrofitResponse{");
        sb.append("timeZone=").append(timeZone);
        sb.append(", timeShift=").append(timeShift);
        sb.append(", interfaceLng='").append(interfaceLng).append('\'');
        sb.append(", parentalPass=").append(parentalPass);
        sb.append(", mediaServerId=").append(mediaServerId);
        sb.append(", stbVolume=").append(stbVolume);
        sb.append(", stbBright=").append(stbBright);
        sb.append(", stbContrast=").append(stbContrast);
        sb.append(", stbSaturation=").append(stbSaturation);
        sb.append(", stbBuffer=").append(stbBuffer);
        sb.append(", mediaServers=").append(mediaServers);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
