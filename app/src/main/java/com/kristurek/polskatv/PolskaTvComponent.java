package com.kristurek.polskatv;

import com.kristurek.polskatv.ui.arch.ViewModelModule;
import com.kristurek.polskatv.ui.channels.ChannelsFragment;
import com.kristurek.polskatv.ui.clock.ClockFragment;
import com.kristurek.polskatv.ui.console.ConsoleFragment;
import com.kristurek.polskatv.ui.epg.EpgFragment;
import com.kristurek.polskatv.ui.epgs.EpgsFragment;
import com.kristurek.polskatv.ui.forceclose.ForceCloseActivity;
import com.kristurek.polskatv.ui.forceclose.ForceCloseFragment;
import com.kristurek.polskatv.ui.login.LoginFragment;
import com.kristurek.polskatv.ui.main.MainActivity;
import com.kristurek.polskatv.ui.player.PlayerFragment;
import com.kristurek.polskatv.ui.settings.SettingsFragment;
import com.kristurek.polskatv.ui.similarepgs.SimilarEpgsDialogFragment;
import com.kristurek.polskatv.ui.update.UpdateIntentService;
import com.kristurek.polskatv.ui.update.UpdateWorker;
import com.kristurek.polskatv.ui.volume.VolumeFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PolskaTvModule.class, ViewModelModule.class})
public interface PolskaTvComponent {

    void inject(PolskaTvApplication obj);

    void inject(MainActivity obj);

    void inject(LoginFragment obj);

    void inject(ChannelsFragment obj);

    void inject(EpgFragment obj);

    void inject(SettingsFragment obj);

    void inject(EpgsFragment obj);

    void inject(SimilarEpgsDialogFragment obj);

    void inject(VolumeFragment obj);

    void inject(ConsoleFragment obj);

    void inject(ClockFragment obj);

    void inject(PlayerFragment obj);

    void inject(ForceCloseFragment obj);

    void inject(UpdateIntentService updateIntentService);

    void inject(UpdateWorker updateWorker);

    void inject(ForceCloseActivity forceCloseActivity);
}