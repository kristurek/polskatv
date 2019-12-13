
package com.kristurek.polskatv.iptv.polbox.pojo.channels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Channel {

    @SerializedName("big_icon")
    @Expose
    private String bigIcon;
    @SerializedName("epg_end")
    @Expose
    private String epgEnd;
    @SerializedName("epg_progname")
    @Expose
    private String epgProgname;
    @SerializedName("epg_start")
    @Expose
    private String epgStart;
    @SerializedName("have_archive")
    @Expose
    private String haveArchive;
    @SerializedName("hide")
    @Expose
    private Integer hide;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("is_video")
    @Expose
    private String isVideo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("stream_params")
    @Expose
    private List<StreamParam> streamParams = null;

    public String getBigIcon() {
        return bigIcon;
    }

    public void setBigIcon(String bigIcon) {
        this.bigIcon = bigIcon;
    }

    public String getEpgEnd() {
        return epgEnd;
    }

    public void setEpgEnd(String epgEnd) {
        this.epgEnd = epgEnd;
    }

    public String getEpgProgname() {
        return epgProgname;
    }

    public void setEpgProgname(String epgProgname) {
        this.epgProgname = epgProgname;
    }

    public String getEpgStart() {
        return epgStart;
    }

    public void setEpgStart(String epgStart) {
        this.epgStart = epgStart;
    }

    public String getHaveArchive() {
        return haveArchive;
    }

    public void setHaveArchive(String haveArchive) {
        this.haveArchive = haveArchive;
    }

    public Integer getHide() {
        return hide;
    }

    public void setHide(Integer hide) {
        this.hide = hide;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(String isVideo) {
        this.isVideo = isVideo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StreamParam> getStreamParams() {
        return streamParams;
    }

    public void setStreamParams(List<StreamParam> streamParams) {
        this.streamParams = streamParams;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "bigIcon='" + bigIcon + '\'' +
                ", epgEnd='" + epgEnd + '\'' +
                ", epgProgname='" + epgProgname + '\'' +
                ", epgStart='" + epgStart + '\'' +
                ", haveArchive='" + haveArchive + '\'' +
                ", hide=" + hide +
                ", icon='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", isVideo='" + isVideo + '\'' +
                ", name='" + name + '\'' +
                ", streamParams=" + streamParams +
                '}';
    }
}
