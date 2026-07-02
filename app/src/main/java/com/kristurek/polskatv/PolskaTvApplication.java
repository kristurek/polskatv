package com.kristurek.polskatv;

import android.util.Log;

import androidx.annotation.NonNull;

import com.kristurek.polskatv.service.DiagnosticService;
import com.kristurek.polskatv.service.LoggerService;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.service.RemoteServerService;
import com.kristurek.polskatv.ui.update.UpdateWorker;
import com.kristurek.polskatv.util.DateTimeHelper;
import com.kristurek.polskatv.util.FontHelper;
import com.kristurek.polskatv.util.Tag;

import androidx.work.Configuration;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import dagger.hilt.android.HiltAndroidApp;
import androidx.hilt.work.HiltWorkerFactory;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

@HiltAndroidApp
public class PolskaTvApplication extends android.app.Application implements Consumer<Throwable>, Configuration.Provider {

    @Inject
    public HiltWorkerFactory workerFactory;

    @Inject
    public PreferencesService prefService;
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

        DateTimeHelper.setSelectedTimeZoneId(prefService.get(PreferencesService.KEYS.APPLICATION_TIME_ZONE, DateTimeHelper.DEFAULT_TIME_ZONE_ID));
        FontHelper.setFontSize(prefService.get(PreferencesService.KEYS.APPLICATION_FONT_SIZE, FontHelper.DEFAULT_FONT_SIZE));

        WorkManager.getInstance(this).enqueue(OneTimeWorkRequest.from(UpdateWorker.class));
    }

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build();
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        Log.e(Tag.UI, throwable.getMessage(), throwable);
    }
}
