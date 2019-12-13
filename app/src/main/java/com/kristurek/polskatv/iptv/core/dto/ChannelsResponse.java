package com.kristurek.polskatv.iptv.core.dto;


import com.kristurek.polskatv.iptv.core.dto.common.Channel;
import com.kristurek.polskatv.iptv.core.dto.common.Group;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChannelsResponse {

    private Map<Group, List<Channel>> groups;

    public Map<Group, List<Channel>> getGroups() {
        if(groups==null)
            return new LinkedHashMap<>();
        return groups;
    }

    public void setGroups(Map<Group, List<Channel>> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "ChannelsRetrofitResponse{" +
                "groups=" + groups +
                '}';
    }


}