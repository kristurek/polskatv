package com.kristurek.polskatv.ui.similarepgs.interactor;

import android.util.Log;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.SimilarEpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.SimilarEpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.common.Epg;
import com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.ArrayParamAbstractInteractor;
import com.kristurek.polskatv.ui.epgs.model.EpgModel;
import com.kristurek.polskatv.util.DateTimeHelper;
import com.kristurek.polskatv.util.Tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FindSimilarEpgsInteractor extends ArrayParamAbstractInteractor<List<EpgModel>, Object> {

    private IptvService iptvService;
    private PreferencesService prefService;

    public FindSimilarEpgsInteractor(IptvService iptvService, PreferencesService prefService) {
        this.iptvService = iptvService;
        this.prefService = prefService;
    }

    @Override
    protected List<EpgModel> process(Object... param) throws Exception {
        Integer channelId = (Integer) param[0];
        String title = (String) param[1];
        Boolean manyChannels = (Boolean) param[2];

        Log.d(Tag.UI, "FindSimilarEpgsInteractor.process()[" + channelId + "," + title + "," + manyChannels + "]");

        Set<String> hiddenChannels = prefService.get(PreferencesService.KEYS.APPLICATION_HIDDEN_CHANNELS, new HashSet<>());
        Set<String> searchChannels = prefService.get(PreferencesService.KEYS.APPLICATION_SEARCH_CHANNELS, new HashSet<>());
        Set<Integer> channelIds = new HashSet<>();

        if (manyChannels) {
            for (String channel : searchChannels)
                if (!hiddenChannels.contains(String.valueOf(channel)))
                    channelIds.add(Integer.valueOf(channel));
        } else
            channelIds.add(channelId);

        if (channelIds.isEmpty())
            return new ArrayList<>();

        List<EpgModel> epgs = new ArrayList<>();

        SimilarEpgsRequest request = new SimilarEpgsRequest();
        request.setChannelIds(channelIds);
        request.setTitle(title);
        request.setFromBeginTime(DateTimeHelper.unixTimeFromCurrentDayMinus13Days());

        SimilarEpgsResponse response = iptvService.getSimilarEpgs(request);

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
