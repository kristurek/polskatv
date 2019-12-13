package com.kristurek.polskatv.iptv.polbox.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.dto.EpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.common.Epg;
import com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType;
import com.kristurek.polskatv.iptv.polbox.pojo.epgs.EpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;
import com.kristurek.polskatv.util.DateTimeHelper;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class UnionEpgsConverter {
    private final long fromBeginTime;

    public UnionEpgsConverter(long fromBeginTime) {
        this.fromBeginTime = fromBeginTime;
    }

    public EpgsResponse convert(EpgsRetrofitResponse response1, EpgsRetrofitResponse response2, EpgsRetrofitResponse response3) {
        Log.d(Tag.API, "EpgsConverter.convert(1-" + response1 + ")");
        Log.d(Tag.API, "EpgsConverter.convert(2-" + response2 + ")");
        Log.d(Tag.API, "EpgsConverter.convert(3-" + response3 + ")");

        EpgsResponse responseDTO = new EpgsResponse();
        List<Epg> epgsDTO = new ArrayList<>();

        List<com.kristurek.polskatv.iptv.polbox.pojo.epgs.Epg> response = new ArrayList<>();

        if (response1 != null)
            for (com.kristurek.polskatv.iptv.polbox.pojo.epgs.Epg epg : response1.getEpg())
                if (epg.getUtStart() > fromBeginTime)
                    response.add(epg);

        for (com.kristurek.polskatv.iptv.polbox.pojo.epgs.Epg epg : response2.getEpg())
            response.add(epg);

        if (response3 != null)
            for (com.kristurek.polskatv.iptv.polbox.pojo.epgs.Epg epg : response3.getEpg())
                response.add(epg);

        for (int i = 0; i < response.size() - 1; i++) {
            com.kristurek.polskatv.iptv.polbox.pojo.epgs.Epg epg = response.get(i);

            Epg epgDTO = new Epg();

            String[] sDesc = response.get(i).getProgname().split("\\.", 2);
            epgDTO.setDescription(sDesc[1].replace("\\n", " ").replaceAll("\\p{Cntrl}", "").trim());
            epgDTO.setTitle(sDesc[0].replace("\\n", " ").replaceAll("\\p{Cntrl}", "").trim());

            epgDTO.setBeginTime(response.get(i).getUtStart());
            epgDTO.setEndTime(response.get(i + 1).getUtStart());

            epgDTO.setType(determineEpgType(epgDTO.getBeginTime(), epgDTO.getEndTime()));

            LocalDate nextDayDate = DateTimeHelper.getNextDay(DateTimeHelper.unixTimeToLocalDate(fromBeginTime));
            long unixTimeNextDay = DateTimeHelper.localDateToUnixTime(nextDayDate);
            if (epg.getUtStart() <= unixTimeNextDay)
                epgsDTO.add(epgDTO);
        }

        responseDTO.setEpgs(epgsDTO);

        Log.d(Tag.API, "EpgsConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }

    private EpgType determineEpgType(long begin, long end) {
        if (isLiveStream(begin, end)) {
            return EpgType.LIVE_EPG;
        } else if (isArchiveStream(begin, begin)) {
            return EpgType.ARCHIVE_EPG;
        } else {
            return EpgType.NOT_AVAILABLE;
        }
    }

    private static boolean isLiveStream(long begin, long end) {
        long now = Duration.millis(DateTime.now().getMillis()).getStandardSeconds();

        return now >= begin && now <= end;
    }


    private static boolean isArchiveStream(long begin, long end) {
        long now = Duration.millis(DateTime.now().getMillis()).getStandardSeconds();

        return now >= begin && now > end;
    }
}