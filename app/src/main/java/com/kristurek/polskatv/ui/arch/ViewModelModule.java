package com.kristurek.polskatv.ui.arch;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    public abstract ViewModel bindLoginViewModel(LoginViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChannelsViewModel.class)
    public abstract ViewModel bindChannelsViewModel(ChannelsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EpgViewModel.class)
    public abstract ViewModel bindEpgViewModel(EpgViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ClockViewModel.class)
    public abstract ViewModel bindClockViewModel(ClockViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ConsoleViewModel.class)
    public abstract ViewModel bindConsoleViewModel(ConsoleViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EpgsViewModel.class)
    public abstract ViewModel bindEpgsViewModel(EpgsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ForceCloseViewModel.class)
    public abstract ViewModel bindForceCloseViewModel(ForceCloseViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel.class)
    public abstract ViewModel bindPlayerViewModel(PlayerViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SimilarEpgsViewModel.class)
    public abstract ViewModel bindSimilarEpgsViewModel(SimilarEpgsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(VolumeViewModel.class)
    public abstract ViewModel bindVolumeViewModel(VolumeViewModel viewModel);
}
