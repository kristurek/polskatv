package com.kristurek.polskatv.iptv.core.dto.common;

import com.kristurek.polskatv.iptv.core.dto.common.enumeration.ChannelType;


public class Channel {

    private long liveEpgBeginTime;
    private long liveEpgEndTime;
    private String liveEpgDescription;
    private String liveEpgTitle;
    private int groupId;
    private ChannelType type;
    private String icon;
    private int id;
    private String name;
    private boolean protectedContent;

    public long getLiveEpgBeginTime() {
        return liveEpgBeginTime;
    }

    public void setLiveEpgBeginTime(long liveEpgBeginTime) {
        this.liveEpgBeginTime = liveEpgBeginTime;
    }

    public long getLiveEpgEndTime() {
        return liveEpgEndTime;
    }

    public void setLiveEpgEndTime(long liveEpgEndTime) {
        this.liveEpgEndTime = liveEpgEndTime;
    }

    public String getLiveEpgDescription() {
        return liveEpgDescription;
    }

    public void setLiveEpgDescription(String liveEpgDescription) {
        this.liveEpgDescription = liveEpgDescription;
    }

    public String getLiveEpgTitle() {
        return liveEpgTitle;
    }

    public void setLiveEpgTitle(String liveEpgTitle) {
        this.liveEpgTitle = liveEpgTitle;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getProtectedContent() {
        return protectedContent;
    }

    public void setProtectedContent(boolean protectedContent) {
        this.protectedContent = protectedContent;
    }

    @Override
    public String toString() {
        return "Channel{" +
                " id=" + id +
                ", name='" + name + '\'' +
                ", liveEpgBeginTime=" + liveEpgBeginTime +
                ", liveEpgEndTime=" + liveEpgEndTime +
                ", liveEpgTitle='" + liveEpgTitle + '\'' +
                ", groupId=" + groupId +
                ", type=" + type +
                ", icon=" + icon +
                ", protectedContent=" + protectedContent +
                ", liveEpgDescription='" + liveEpgDescription + '\'' +
                '}';
    }
}
