package com.kristurek.polskatv.ui.main.interactor;

import android.content.Context;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.LogoutRequest;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.VoidParamAbstractInteractor;
import com.kristurek.polskatv.util.CacheHelper;

public class LogoutInteractor extends VoidParamAbstractInteractor<Boolean> {

    private Context context;
    private IptvService iptvService;
    private PreferencesService prefService;

    public LogoutInteractor(Context context,
                            IptvService iptvService,
                            PreferencesService prefService) {
        this.iptvService = iptvService;
        this.prefService = prefService;
        this.context = context;
    }

    @Override
    protected Boolean process() throws Exception {
        CacheHelper.deleteCache(context);

        prefService.clear(PreferencesService.KEYS.ACCOUNT_SUBSCRIPTION);
        prefService.clear(PreferencesService.KEYS.ACCOUNT_PASSWORD);
        prefService.clear(PreferencesService.KEYS.ACCOUNT_PARENTAL_PASSWORD);
        prefService.clear(PreferencesService.KEYS.ACCOUNT_MEDIA_SERVER_ID);
        prefService.clear(PreferencesService.KEYS.ACCOUNT_MEDIA_SERVERS);
        prefService.clear(PreferencesService.KEYS.ACCOUNT_TIME_SHIFT);
        prefService.clear(PreferencesService.KEYS.ACCOUNT_TIME_ZONE);
        prefService.clear(PreferencesService.KEYS.ACCOUNT_REST_OF_DAY);
        prefService.clear(PreferencesService.KEYS.ACCOUNT_LANGUAGE);

        iptvService.logout(new LogoutRequest());

        return true;
    }
}
