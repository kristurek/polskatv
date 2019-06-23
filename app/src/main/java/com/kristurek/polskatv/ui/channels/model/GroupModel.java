package com.kristurek.polskatv.ui.channels.model;

import java.io.Serializable;

public class GroupModel implements Serializable {

    private String title;

    public GroupModel() {
    }

    public GroupModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GroupModel{");
        sb.append("title='").append(title).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
