package com.kristurek.polskatv.ui.arch;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.kristurek.polskatv.service.DiagnosticService;
import com.kristurek.polskatv.service.LoggerService;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.service.RemoteServerService;
import com.kristurek.polskatv.ui.channels.ChannelsViewModel;
import com.kristurek.polskatv.ui.clock.ClockViewModel;
import com.kristurek.polskatv.ui.console.ConsoleViewModel;
import com.kristurek.polskatv.ui.epg.EpgViewModel;
import com.kristurek.polskatv.ui.epgs.EpgsViewModel;
import com.kristurek.polskatv.ui.forceclose.ForceCloseViewModel;
import com.kristurek.polskatv.ui.login.LoginViewModel;
import com.kristurek.polskatv.ui.main.MainViewModel;
import com.kristurek.polskatv.ui.player.PlayerViewModel;
import com.kristurek.polskatv.ui.similarepgs.SimilarEpgsViewModel;
import com.kristurek.polskatv.ui.volume.VolumeViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final PreferencesService prefService;
    private final RemoteServerService remoteService;
    private final LoggerService logService;
    private final DiagnosticService diagService;
    private final Context context;

    public static ViewModelFactory initialize(Context context,
                                              PreferencesService prefService,
                                              RemoteServerService remoteService,
                                              LoggerService logService,
                                              DiagnosticService diagService) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(context, prefService, remoteService, logService, diagService);
                }
            }
        }
        return INSTANCE;
    }

    public static ViewModelFactory getInstance() {
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private ViewModelFactory(Context context,
                             PreferencesService prefService,
                             RemoteServerService remoteService,
                             LoggerService logService,
                             DiagnosticService diagService) {
        this.context = context;
        this.prefService = prefService;
        this.remoteService = remoteService;
        this.logService = logService;
        this.diagService = diagService;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(context, prefService, remoteService, logService, diagService);
        }
        if (modelClass.isAssignableFrom(ChannelsViewModel.class)) {
            return (T) new ChannelsViewModel(context, prefService);
        }
        if (modelClass.isAssignableFrom(EpgsViewModel.class)) {
            return (T) new EpgsViewModel(prefService);
        }
        if (modelClass.isAssignableFrom(EpgViewModel.class)) {
            return (T) new EpgViewModel();
        }
        if (modelClass.isAssignableFrom(ConsoleViewModel.class)) {
            return (T) new ConsoleViewModel(prefService);
        }
        if (modelClass.isAssignableFrom(PlayerViewModel.class)) {
            return (T) new PlayerViewModel(context, prefService);
        }
        if (modelClass.isAssignableFrom(VolumeViewModel.class)) {
            return (T) new VolumeViewModel(prefService);
        }
        if (modelClass.isAssignableFrom(ClockViewModel.class)) {
            return (T) new ClockViewModel(prefService);
        }
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(context, prefService);
        }
        if (modelClass.isAssignableFrom(SimilarEpgsViewModel.class)) {
            return (T) new SimilarEpgsViewModel(prefService);
        }
        if (modelClass.isAssignableFrom(ForceCloseViewModel.class)) {
            return (T) new ForceCloseViewModel(context, remoteService, logService, diagService);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
