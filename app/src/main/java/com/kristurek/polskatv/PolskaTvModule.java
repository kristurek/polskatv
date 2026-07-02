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
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class PolskaTvModule {

    @Singleton
    @Provides
    PreferencesService providePrefService(@ApplicationContext Context context) {
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
    DiagnosticService provideDiagnosticService(@ApplicationContext Context context, PreferencesService preferencesService) {
        return new PolskaTvDiagnosticService(context, preferencesService);
    }

}
