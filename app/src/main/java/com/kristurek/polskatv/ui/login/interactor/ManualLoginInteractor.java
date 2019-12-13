package com.kristurek.polskatv.ui.login.interactor;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.LoginRequest;
import com.kristurek.polskatv.iptv.core.dto.LoginResponse;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.ArrayParamAbstractInteractor;

import java.util.LinkedHashMap;


public class ManualLoginInteractor extends ArrayParamAbstractInteractor<Boolean, Object> {

    private IptvService iptvService;
    private PreferencesService prefService;

    public ManualLoginInteractor(IptvService iptvService, PreferencesService prefService) {
        this.iptvService = iptvService;
        this.prefService = prefService;
    }

    @Override
    protected Boolean process(Object... param) throws Exception {
        String subscription = (String) param[0];
        String password = (String) param[1];
        Boolean save = (Boolean) param[2];
        Integer providerId = (Integer) param[3];
        String parentalPassword = (String) param[4];

        LoginResponse response = iptvService.login(new LoginRequest(subscription, password));

        if (save) {
            prefService.save(PreferencesService.KEYS.ACCOUNT_SUBSCRIPTION, subscription);
            prefService.save(PreferencesService.KEYS.ACCOUNT_PASSWORD, password);
            prefService.save(PreferencesService.KEYS.API_PROVIDER_ID, providerId);
            prefService.save(PreferencesService.KEYS.ACCOUNT_PARENTAL_PASSWORD, parentalPassword);
        } else {
            prefService.clear(PreferencesService.KEYS.ACCOUNT_SUBSCRIPTION);
            prefService.clear(PreferencesService.KEYS.ACCOUNT_PASSWORD);
            prefService.clear(PreferencesService.KEYS.API_PROVIDER_ID);
            prefService.clear(PreferencesService.KEYS.ACCOUNT_PARENTAL_PASSWORD);
        }

        prefService.save(PreferencesService.KEYS.ACCOUNT_LANGUAGE, response.getInterfaceLang());
        prefService.save(PreferencesService.KEYS.ACCOUNT_MEDIA_SERVERS, (LinkedHashMap<String, String>) response.getMediaServers());
        prefService.save(PreferencesService.KEYS.ACCOUNT_MEDIA_SERVER_ID, response.getMediaServerId());
        prefService.save(PreferencesService.KEYS.ACCOUNT_REST_OF_DAY, response.getRestOfDay());
        prefService.save(PreferencesService.KEYS.ACCOUNT_TIME_SHIFT, response.getTimeShift());
        prefService.save(PreferencesService.KEYS.ACCOUNT_TIME_ZONE, response.getTimeZone());

        return true;
    }
}
