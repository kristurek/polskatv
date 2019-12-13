package com.kristurek.polskatv.iptv.polskatelewizjausa.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.core.dto.CurrentEpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.common.Channel;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.currentepgs.CurrentEpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;

import java.util.ArrayList;
import java.util.List;

public class CurrentEpgsConverter implements Converter<CurrentEpgsRetrofitResponse, CurrentEpgsResponse> {
    @Override
    public CurrentEpgsResponse convert(CurrentEpgsRetrofitResponse response) {
        Log.d(Tag.API, "CurrentEpgsConverter.convert(" + response + ")");

        CurrentEpgsResponse responseDTO = new CurrentEpgsResponse();
        List<Channel> channelsDTO = new ArrayList<>();

        for (com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.currentepgs.Channel channel : response.getChannels()) {
            Channel channelDTO = new Channel();

            channelDTO.setId(channel.getId());
            channelDTO.setName(channel.getName().replace("\\n", " ").replaceAll("\\p{Cntrl}", ""));
            channelDTO.setIcon("polskatelewizjausa/" + channel.getIcon().replace('-', '_') + ".png");
            if (channel.getCurrent() != null) {
                channelDTO.setLiveEpgTitle(channel.getCurrent().getTitle().replace("\\n", " ").replaceAll("\\p{Cntrl}", ""));
                channelDTO.setLiveEpgDescription(channel.getCurrent().getInfo().replace("\\n", " ").replaceAll("\\p{Cntrl}", ""));
                channelDTO.setLiveEpgBeginTime(channel.getCurrent().getBegin() != null ? channel.getCurrent().getBegin() : 0);
                channelDTO.setLiveEpgEndTime(channel.getCurrent().getEnd() != null ? channel.getCurrent().getEnd() : 0);
            }

            channelsDTO.add(channelDTO);
        }
        responseDTO.setChannels(channelsDTO);

        Log.d(Tag.API, "CurrentEpgsConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
