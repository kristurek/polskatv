package com.kristurek.polskatv.ui.login.interactor;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.LoginRequest;
import com.kristurek.polskatv.iptv.core.dto.LoginResponse;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.VoidParamAbstractInteractor;

import java.util.LinkedHashMap;

public class AutoLoginInteractor extends VoidParamAbstractInteractor<Boolean> {

    private IptvService iptvService;
    private PreferencesService prefService;

    public AutoLoginInteractor(IptvService iptvService, PreferencesService prefService) {
        this.iptvService = iptvService;
        this.prefService = prefService;
    }

    protected Boolean process() throws Exception {
        if (prefService.contains(PreferencesService.KEYS.ACCOUNT_SUBSCRIPTION)
                && prefService.contains(PreferencesService.KEYS.ACCOUNT_PASSWORD)
                && prefService.contains(PreferencesService.KEYS.API_PROVIDER_ID)) {

            String subscription = prefService.get(PreferencesService.KEYS.ACCOUNT_SUBSCRIPTION, (String) null);
            String password = prefService.get(PreferencesService.KEYS.ACCOUNT_PASSWORD, (String) null);

            LoginResponse response = iptvService.login(new LoginRequest(subscription, password));

            prefService.save(PreferencesService.KEYS.ACCOUNT_PARENTAL_PASSWORD, response.getParentalPass());
            prefService.save(PreferencesService.KEYS.ACCOUNT_LANGUAGE, response.getInterfaceLang());
            prefService.save(PreferencesService.KEYS.ACCOUNT_MEDIA_SERVERS, (LinkedHashMap<String, String>) response.getMediaServers());
            prefService.save(PreferencesService.KEYS.ACCOUNT_MEDIA_SERVER_ID, response.getMediaServerId());
            prefService.save(PreferencesService.KEYS.ACCOUNT_REST_OF_DAY, response.getRestOfDay());
            prefService.save(PreferencesService.KEYS.ACCOUNT_TIME_SHIFT, response.getTimeShift());
            prefService.save(PreferencesService.KEYS.ACCOUNT_TIME_ZONE, response.getTimeZone());

            return true;
        }

        return false;
    }
}