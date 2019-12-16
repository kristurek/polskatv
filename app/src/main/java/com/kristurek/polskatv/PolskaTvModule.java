package com.kristurek.polskatv;

import android.content.Context;

import com.kristurek.polskatv.iptv.ServiceProvider;
import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.polbox.PolboxService;
import com.kristurek.polskatv.iptv.polbox.retrofit.PolboxApiFactory;
import com.kristurek.polskatv.iptv.polskatelewizjausa.PolskaTelewizjaUsaService;
import com.kristurek.polskatv.iptv.polskatelewizjausa.PolskaTelewizjaUsaServiceMock;
import com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit.PolskaTelewizjaUsaApiFactory;
import com.kristurek.polskatv.service.DiagnosticService;
import com.kristurek.polskatv.service.LoggerService;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.service.RemoteServerService;
import com.kristurek.polskatv.service.impl.PolskaTvDiagnosticService;
import com.kristurek.polskatv.service.impl.PolskaTvLoggerService;
import com.kristurek.polskatv.service.impl.PolskaTvPreferencesService;
import com.kristurek.polskatv.service.impl.PolskaTvRemoteServerService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PolskaTvModule {

    private Context context;

    public PolskaTvModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    PreferencesService providePrefService() {
        return new PolskaTvPreferencesService(context);
    }

    @Singleton
    @Provides
    RemoteServerService provideRemoteServerService() {
        return new PolskaTvRemoteServerService();
    }

    @Singleton
    @Provides
    LoggerService provideLoggerService() {
        return new PolskaTvLoggerService();
    }

    @Singleton
    @Provides
    DiagnosticService provideDiagnosticService() {
        return new PolskaTvDiagnosticService(context, providePrefService());
    }

}
