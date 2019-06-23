package com.kristurek.polskatv.iptv.polskatelewizjausa.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.dto.EpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.common.Epg;
import com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.epgs.Channel;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.epgs.EpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.List;

public class EpgsConverter implements Converter<EpgsRetrofitResponse, EpgsResponse> {
    private final long fromBeginTime;

    public EpgsConverter(long fromBeginTime) {
        this.fromBeginTime = fromBeginTime;
    }

    @Override
    public EpgsResponse convert(EpgsRetrofitResponse response) {
        Log.d(Tag.API, "EpgsConverter.convert(" + response + ")");

        EpgsResponse responseDTO = new EpgsResponse();
        List<Epg> epgsDTO = new ArrayList<>();

        for (Channel channel : response.getChannels())
            for (com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.epgs.Epg epg : channel.getEpg()) {
                if (epg.getBegin() >= fromBeginTime) {
                    Epg epgDTO = new Epg();

                    epgDTO.setChannelId(channel.getId());
                    epgDTO.setChannelName(channel.getName().replace("\\n", " ").replaceAll("\\p{Cntrl}", ""));
                    epgDTO.setBeginTime(epg.getBegin());
                    epgDTO.setEndTime(epg.getEnd());
                    epgDTO.setDescription(epg.getInfo().replace("\\n", " ").replaceAll("\\p{Cntrl}", ""));
                    epgDTO.setTitle(epg.getTitle().replace("\\n", " ").replaceAll("\\p{Cntrl}", ""));
                    epgDTO.setType(determineEpgType(epg.getWait(), epg.getHasArchive(), epg.getBegin(), epg.getEnd()));

                    epgsDTO.add(epgDTO);
                }
            }

        responseDTO.setEpgs(epgsDTO);

        Log.d(Tag.API, "EpgsConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }

    private EpgType determineEpgType(Integer wait, Integer hasArchive, Integer begin, Integer end) {
        if (isLiveStream(begin, end)) {
            return EpgType.LIVE_EPG;
        } else if (wait == null && hasArchive == null) {
            return EpgType.NOT_AVAILABLE;
        } else if (wait == null && hasArchive.equals(0)) {
            return EpgType.LIVE_EPG;
        } else if (wait.equals(0) && hasArchive.equals(1)) {
            return EpgType.ARCHIVE_EPG;
        } else if (wait.equals(0) && hasArchive.equals(0)) {
            return EpgType.ARCHIVE_EPG;
        } else if (wait.equals(1) && hasArchive.equals(1)) {
            return EpgType.LIVE_EPG;
        } else {
            return EpgType.NOT_AVAILABLE;
        }
    }

    private static boolean isLiveStream(Integer begin, Integer end) {
        long now = Duration.millis(DateTime.now().getMillis()).getStandardSeconds();

        return now >= begin && now <= end;
    }
}
