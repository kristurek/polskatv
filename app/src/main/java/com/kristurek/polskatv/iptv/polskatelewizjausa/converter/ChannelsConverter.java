package com.kristurek.polskatv.iptv.polskatelewizjausa.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.dto.ChannelsResponse;
import com.kristurek.polskatv.iptv.core.dto.common.Channel;
import com.kristurek.polskatv.iptv.core.dto.common.Group;
import com.kristurek.polskatv.iptv.core.dto.common.enumeration.ChannelType;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels.ChannelsRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChannelsConverter implements Converter<ChannelsRetrofitResponse, ChannelsResponse> {
    @Override
    public ChannelsResponse convert(ChannelsRetrofitResponse response) {
        Log.d(Tag.API, "ChannelsConverter.convert(" + response + ")");

        ChannelsResponse responseDTO = new ChannelsResponse();
        Map<Group, List<Channel>> groupsDTO = new LinkedHashMap<>();

        List<com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels.Group> groups = response.getGroups();
        for (com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels.Group group : groups) {
            List<Channel> channelsDTO = new ArrayList<>();
            List<com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels.Channel> channels = group.getChannels();
            for (com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels.Channel channel : channels) {
                Channel channelDTO = new Channel();

                channelDTO.setId(channel.getId());
                channelDTO.setName(channel.getName().replace("\\n", " ").replaceAll("\\p{Cntrl}", ""));
                channelDTO.setIcon("polskatelewizjausa/" + channel.getIcon().replace('-', '_') + ".png");
                channelDTO.setType(channel.getHasArchive() == 0 ? ChannelType.LIVE_CHANNEL : ChannelType.ARCHIVE_CHANNEL);
                channelDTO.setProtectedContent(channel.getProtected() == 1);
                channelDTO.setGroupId(group.getId());
                if (channel.getEpg() != null && channel.getEpg().getCurrent() != null) {
                    channelDTO.setLiveEpgTitle(channel.getEpg().getCurrent().getTitle().replace("\\n", " ").replaceAll("\\p{Cntrl}", ""));
                    channelDTO.setLiveEpgDescription(channel.getEpg().getCurrent().getInfo().replace("\\n", " ").replaceAll("\\p{Cntrl}", ""));
                    channelDTO.setLiveEpgBeginTime(channel.getEpg().getCurrent().getBegin());
                    channelDTO.setLiveEpgEndTime(channel.getEpg().getCurrent().getEnd());
                }

                channelsDTO.add(channelDTO);
            }

            Group groupDTO = new Group();

            groupDTO.setId(group.getId());
            groupDTO.setTitle(group.getUserTitle());

            groupsDTO.put(groupDTO, channelsDTO);
        }

        responseDTO.setGroups(groupsDTO);

        Log.d(Tag.API, "ChannelsConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
