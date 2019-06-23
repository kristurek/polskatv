package com.kristurek.polskatv.ui.player.interactor;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.UrlRequest;
import com.kristurek.polskatv.iptv.core.dto.UrlResponse;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.ArrayParamAbstractInteractor;
import com.kristurek.polskatv.ui.epgs.model.EpgType;
import com.kristurek.polskatv.ui.player.model.PlayerModel;
import com.kristurek.polskatv.util.Tag;

public class InitializeUrlInteractor extends ArrayParamAbstractInteractor<PlayerModel, Object> {

    private IptvService iptvService;
    private PreferencesService prefService;


    public InitializeUrlInteractor(IptvService iptvService, PreferencesService prefService) {
        this.iptvService = iptvService;
        this.prefService = prefService;
    }

    @Override
    protected PlayerModel process(Object... param) throws Exception {
        Log.d(Tag.UI, "InitializeUrlInteractor.process()[" + param + "]");
        Integer channelId = (Integer) param[0];
        EpgType epgType = (EpgType) param[1];
        Long epgCurrentTime = (Long) param[2];

        UrlRequest request = new UrlRequest();
        request.setProtectCode(prefService.get(PreferencesService.KEYS.ACCOUNT_PARENTAL_PASSWORD, ""));
        request.setChannelId(channelId);

        if (epgType.equals(EpgType.ARCHIVE_EPG)) {
            request.setSeekToTime(epgCurrentTime);
            request.setType(com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType.ARCHIVE_EPG);
        } else if (epgType.equals(EpgType.LIVE_EPG))
            request.setType(com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType.LIVE_EPG);
        else
            request.setType(com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType.NOT_AVAILABLE);

        UrlResponse response = iptvService.getUrl(request);

        PlayerModel model = new PlayerModel();
        model.setUrl(response.getUrl());
        model.setEpgCurrentTime(epgCurrentTime);
        model.setUserAgent(response.getUserAgent());

        return model;
    }
}
