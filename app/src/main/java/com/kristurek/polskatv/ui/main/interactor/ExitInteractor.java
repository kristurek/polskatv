package com.kristurek.polskatv.ui.main.interactor;

import android.content.Context;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.LogoutRequest;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.VoidParamAbstractInteractor;

public class ExitInteractor extends VoidParamAbstractInteractor<Boolean> {

    private Context context;
    private IptvService iptvService;
    private PreferencesService prefService;

    public ExitInteractor(Context context,
                          IptvService iptvService,
                          PreferencesService prefService) {
        this.iptvService = iptvService;
        this.prefService = prefService;
        this.context = context;
    }

    @Override
    protected Boolean process() throws Exception {

        iptvService.logout(new LogoutRequest());

        return true;
    }
}

