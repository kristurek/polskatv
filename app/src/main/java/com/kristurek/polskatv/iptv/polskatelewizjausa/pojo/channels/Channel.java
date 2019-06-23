
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Channel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("group_id")
    @Expose
    private Integer groupId;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_video")
    @Expose
    private Integer isVideo;
    @SerializedName("protected")
    @Expose
    private Integer _protected;
    @SerializedName("has_archive")
    @Expose
    private Integer hasArchive;
    @SerializedName("is_favorite")
    @Expose
    private Integer isFavorite;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("languages")
    @Expose
    private String languages;
    @SerializedName("audiotracks")
    @Expose
    private String audiotracks;
    @SerializedName("audiotrack_default")
    @Expose
    private String audiotrackDefault;
    @SerializedName("aspect_ratio")
    @Expose
    private String aspectRatio;
    @SerializedName("has_ams_archive")
    @Expose
    private Integer hasAmsArchive;
    @SerializedName("quality")
    @Expose
    private List<String> quality = null;
    @SerializedName("epg")
    @Expose
    private Epg epg;

    public Integer getId() {
        return id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public Integer getIsVideo() {
        return isVideo;
    }

    public Integer getProtected() {
        return _protected;
    }

    public Integer getHasArchive() {
        return hasArchive;
    }

    public Integer getIsFavorite() {
        return isFavorite;
    }

    public String getIcon() {
        return icon;
    }

    public String getLanguages() {
        return languages;
    }

    public String getAudiotracks() {
        return audiotracks;
    }

    public String getAudiotrackDefault() {
        return audiotrackDefault;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public Integer getHasAmsArchive() {
        return hasAmsArchive;
    }

    public List<String> getQuality() {
        return quality;
    }

    public Epg getEpg() {
        return epg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Channel{");
        sb.append("id=").append(id);
        sb.append(", groupId=").append(groupId);
        sb.append(", number=").append(number);
        sb.append(", name='").append(name).append('\'');
        sb.append(", isVideo=").append(isVideo);
        sb.append(", _protected=").append(_protected);
        sb.append(", hasArchive=").append(hasArchive);
        sb.append(", isFavorite=").append(isFavorite);
        sb.append(", icon='").append(icon).append('\'');
        sb.append(", languages='").append(languages).append('\'');
        sb.append(", audiotracks='").append(audiotracks).append('\'');
        sb.append(", audiotrackDefault='").append(audiotrackDefault).append('\'');
        sb.append(", aspectRatio='").append(aspectRatio).append('\'');
        sb.append(", hasAmsArchive=").append(hasAmsArchive);
        sb.append(", quality=").append(quality);
        sb.append(", epg=").append(epg);
        sb.append('}');
        return sb.toString();
    }
}
