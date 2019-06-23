package com.kristurek.polskatv;

import com.kristurek.polskatv.ui.channels.ChannelsFragment;
import com.kristurek.polskatv.ui.forceclose.ForceCloseActivity;
import com.kristurek.polskatv.ui.login.LoginFragment;
import com.kristurek.polskatv.ui.settings.SettingsFragment;
import com.kristurek.polskatv.ui.update.UpdateIntentService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = PolskaTvModule.class)
public interface PolskaTvComponent {

    void inject(PolskaTvApplication obj);

    void inject(LoginFragment obj);

    void inject(ChannelsFragment obj);

    void inject(SettingsFragment obj);

    void inject(UpdateIntentService updateIntentService);

    void inject(ForceCloseActivity forceCloseActivity);
}