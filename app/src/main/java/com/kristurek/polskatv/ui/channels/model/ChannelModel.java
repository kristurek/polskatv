package com.kristurek.polskatv.ui.channels.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class ChannelModel implements Serializable {

    private int id;
    private String name;
    private String time;
    private Drawable icon;
    private ChannelType type;
    private boolean protectedContent;

    private String liveEpgTitle;
    private long liveEpgBeginTime;
    private long liveEpgEndTime;
    private int liveEpgProgress;
    private String liveEpgDescription;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }

    public boolean isProtectedContent() {
        return protectedContent;
    }

    public void setProtectedContent(boolean protectedContent) {
        this.protectedContent = protectedContent;
    }

    public String getLiveEpgTitle() {
        return liveEpgTitle;
    }

    public void setLiveEpgTitle(String liveEpgTitle) {
        this.liveEpgTitle = liveEpgTitle;
    }

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

    public int getLiveEpgProgress() {
        return liveEpgProgress;
    }

    public void setLiveEpgProgress(int liveEpgProgress) {
        this.liveEpgProgress = liveEpgProgress;
    }

    public String getLiveEpgDescription() {
        return liveEpgDescription;
    }

    public void setLiveEpgDescription(String liveEpgDescription) {
        this.liveEpgDescription = liveEpgDescription;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChannelModel{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", time='").append(time).append('\'');
        sb.append(", icon=").append(icon);
        sb.append(", type=").append(type);
        sb.append(", protectedContent=").append(protectedContent);
        sb.append(", liveEpgTitle='").append(liveEpgTitle).append('\'');
        sb.append(", liveEpgBeginTime=").append(liveEpgBeginTime);
        sb.append(", liveEpgEndTime=").append(liveEpgEndTime);
        sb.append(", liveEpgProgress=").append(liveEpgProgress);
        sb.append(", liveEpgDescription='").append(liveEpgDescription).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
