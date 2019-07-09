package com.kristurek.polskatv;

import android.content.Intent;
import android.util.Log;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.service.DiagnosticService;
import com.kristurek.polskatv.service.LoggerService;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.service.RemoteServerService;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;
import com.kristurek.polskatv.ui.update.UpdateIntentService;
import com.kristurek.polskatv.util.DateTimeHelper;
import com.kristurek.polskatv.util.FontHelper;
import com.kristurek.polskatv.util.Tag;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class PolskaTvApplication extends android.app.Application implements Consumer<Throwable> {

    private static PolskaTvComponent component;

    @Inject
    public PreferencesService prefService;
    @Inject
    public IptvService iptvService;
    @Inject
    public RemoteServerService remoteService;
    @Inject
    public LoggerService logService;
    @Inject
    public DiagnosticService diagService;

    @Override
    public void onCreate() {
        super.onCreate();

        RxJavaPlugins.setErrorHandler(this);

        component = DaggerPolskaTvComponent.builder().polskaTvModule(new PolskaTvModule(this)).build();

        PolskaTvApplication.getComponent().inject(this);

        ViewModelFactory.initialize(this, iptvService, prefService, remoteService, logService, diagService);

        DateTimeHelper.setSelectedTimeZoneId(prefService.get(PreferencesService.KEYS.APPLICATION_TIME_ZONE, DateTimeHelper.DEFAULT_TIME_ZONE_ID));
        FontHelper.setFontSize(prefService.get(PreferencesService.KEYS.APPLICATION_FONT_SIZE, FontHelper.DEFAULT_FONT_SIZE));

        startService(new Intent(this, UpdateIntentService.class));
    }

    public static PolskaTvComponent getComponent() {
        return component;
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        Log.e(Tag.UI, throwable.getMessage(), throwable);
    }
}
