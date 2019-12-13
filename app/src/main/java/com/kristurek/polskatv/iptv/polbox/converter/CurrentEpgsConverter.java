package com.kristurek.polskatv.iptv.polbox.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.core.dto.CurrentEpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.common.Channel;
import com.kristurek.polskatv.iptv.polbox.pojo.currentepgs.CurrentEpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;

import java.util.ArrayList;
import java.util.List;

public class CurrentEpgsConverter implements Converter<CurrentEpgsRetrofitResponse, CurrentEpgsResponse> {
    @Override
    public CurrentEpgsResponse convert(CurrentEpgsRetrofitResponse response) {
        Log.d(Tag.API, "CurrentEpgsConverter.convert(" + response + ")");

        CurrentEpgsResponse responseDTO = new CurrentEpgsResponse();
        List<Channel> channelsDTO = new ArrayList<>();

        for (com.kristurek.polskatv.iptv.polbox.pojo.currentepgs.Epg epg : response.getEpg()) {
            Channel channelDTO = new Channel();

            channelDTO.setId(epg.getCid());

            String sString[] = epg.getEpg().get(0).getProgname().split("\\n", 2);
            if (sString.length < 2)
                sString = epg.getEpg().get(0).getProgname().split(" ", 2);

            if (sString.length < 2)
                sString = new String[]{"None", "None"};

            channelDTO.setLiveEpgTitle(sString[0].replace("\\n", " ").replaceAll("\\p{Cntrl}", "").trim());
            channelDTO.setLiveEpgDescription(sString[1].replace("\\n", " ").replaceAll("\\p{Cntrl}", "").trim());
            channelDTO.setLiveEpgBeginTime(Integer.valueOf(epg.getEpg().get(0).getTs()));
            channelDTO.setLiveEpgEndTime(Integer.valueOf(epg.getEpg().get(1).getTs()));

            channelsDTO.add(channelDTO);
        }
        responseDTO.setChannels(channelsDTO);

        Log.d(Tag.API, "CurrentEpgsConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
