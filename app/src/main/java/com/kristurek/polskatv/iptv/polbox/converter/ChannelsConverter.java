package com.kristurek.polskatv.iptv.polbox.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.core.dto.ChannelsResponse;
import com.kristurek.polskatv.iptv.core.dto.common.Channel;
import com.kristurek.polskatv.iptv.core.dto.common.Group;
import com.kristurek.polskatv.iptv.core.dto.common.enumeration.ChannelType;
import com.kristurek.polskatv.iptv.polbox.pojo.channels.ChannelsRetrofitResponse;
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

        List<com.kristurek.polskatv.iptv.polbox.pojo.channels.Group> groups = response.getGroups();
        for (com.kristurek.polskatv.iptv.polbox.pojo.channels.Group group : groups) {
            List<Channel> channelsDTO = new ArrayList<>();
            List<com.kristurek.polskatv.iptv.polbox.pojo.channels.Channel> channels = group.getChannels();
            for (com.kristurek.polskatv.iptv.polbox.pojo.channels.Channel channel : channels) {
                if (channel.getIsVideo().equals("1")) {
                    Channel channelDTO = new Channel();

                    channelDTO.setId(Integer.valueOf(channel.getId()));
                    channelDTO.setName(channel.getName().replace("\\n", " ").replaceAll("\\p{Cntrl}", ""));

                    String pattern = "/dune/polbox/images_v3/";
                    channelDTO.setIcon("polbox/" + channel.getBigIcon().substring(pattern.length()));

                    channelDTO.setType(Integer.valueOf(channel.getHaveArchive()) == 0 ? ChannelType.LIVE_CHANNEL : ChannelType.ARCHIVE_CHANNEL);
                    if (channel.getEpgProgname() != null) {
                        String sString[] = channel.getEpgProgname().split("\\.", 2);

                        channelDTO.setLiveEpgTitle(sString[0].replace("\\n", " ").replaceAll("\\p{Cntrl}", "").trim());
                        channelDTO.setLiveEpgDescription(sString[1].replace("\\n", " ").replaceAll("\\p{Cntrl}", "").trim());
                        channelDTO.setLiveEpgBeginTime(Long.valueOf(channel.getEpgStart()));
                        channelDTO.setLiveEpgEndTime(Long.valueOf(channel.getEpgEnd()));
                    }

                    channelsDTO.add(channelDTO);
                }
            }

            Group groupDTO = new Group();

            groupDTO.setId(Integer.valueOf(group.getId()));
            groupDTO.setTitle(group.getName());

            if (!channelsDTO.isEmpty())
                groupsDTO.put(groupDTO, channelsDTO);
        }

        responseDTO.setGroups(groupsDTO);

        Log.d(Tag.API, "ChannelsConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
