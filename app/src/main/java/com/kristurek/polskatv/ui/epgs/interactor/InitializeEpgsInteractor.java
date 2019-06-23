package com.kristurek.polskatv.ui.epgs.interactor;

import android.util.Log;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.EpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.EpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.common.Epg;
import com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType;
import com.kristurek.polskatv.ui.arch.ArrayParamAbstractInteractor;
import com.kristurek.polskatv.ui.epgs.model.EpgModel;
import com.kristurek.polskatv.util.DateTimeHelper;
import com.kristurek.polskatv.util.Tag;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class InitializeEpgsInteractor extends ArrayParamAbstractInteractor<List<EpgModel>, Object> {

    private IptvService iptvService;

    public InitializeEpgsInteractor(IptvService iptvService) {
        this.iptvService = iptvService;
    }

    @Override
    protected List<EpgModel> process(Object... param) throws Exception {
        Integer channelId = (Integer) param[0];
        LocalDate day = (LocalDate) param[1];

        Log.d(Tag.UI, "InitializeEpgsInteractor.process()[" + channelId + "," + day + "]");

        List<EpgModel> epgs = new ArrayList<>();

        EpgsRequest request = new EpgsRequest();
        request.setChannelIds(new HashSet<>(Arrays.asList(channelId)));
        request.setFromBeginTime(DateTimeHelper.localDateToUnixTime(day));

        EpgsResponse response = iptvService.getEpgs(request);

        for (Epg epgDTO : response.getEpgs()) {
            EpgModel epgModel = new EpgModel();

            epgModel.setChannelId(epgDTO.getChannelId());
            epgModel.setChannelName(epgDTO.getChannelName());
            epgModel.setTitle(epgDTO.getTitle());
            epgModel.setDescription(epgDTO.getDescription());
            epgModel.setTime(DateTimeHelper.unixTimeToString(epgDTO.getBeginTime(), DateTimeHelper.HHmm));
            epgModel.setBeginTime(epgDTO.getBeginTime());
            epgModel.setEndTime(epgDTO.getEndTime());
            epgModel.setDay(DateTimeHelper.unixTimeToLocalDate(epgDTO.getBeginTime()));
            if (epgDTO.getType().equals(EpgType.LIVE_EPG)) {
                epgModel.setIcon(R.drawable.ic_live);
                epgModel.setType(com.kristurek.polskatv.ui.epgs.model.EpgType.LIVE_EPG);
            } else if (epgDTO.getType().equals(EpgType.ARCHIVE_EPG)) {
                epgModel.setIcon(R.drawable.ic_play);
                epgModel.setType(com.kristurek.polskatv.ui.epgs.model.EpgType.ARCHIVE_EPG);
            } else {
                epgModel.setIcon(R.drawable.ic_wait);
                epgModel.setType(com.kristurek.polskatv.ui.epgs.model.EpgType.NOT_AVAILABLE);
            }

            epgs.add(epgModel);
        }

        return epgs;
    }
}
