package com.kristurek.polskatv.iptv.core.dto;


import com.kristurek.polskatv.iptv.core.dto.common.enumeration.SettingType;

public class SettingsRequest {

    private SettingType type;
    private String oldValue;
    private String newValue;

    public SettingsRequest() {
    }

    public SettingsRequest(SettingType type, String oldValue, String newValue) {
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public SettingType getType() {
        return type;
    }

    public void setType(SettingType type) {
        this.type = type;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SettingsRequest{");
        sb.append("type=").append(type);
        sb.append(", oldValue='").append(oldValue).append('\'');
        sb.append(", newValue='").append(newValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
