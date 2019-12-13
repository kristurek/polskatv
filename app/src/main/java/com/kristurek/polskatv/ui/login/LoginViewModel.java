package com.kristurek.polskatv.ui.login;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.common.base.Strings;
import com.kristurek.polskatv.R;
import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.service.DiagnosticService;
import com.kristurek.polskatv.service.LoggerService;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.service.RemoteServerService;
import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.ui.login.interactor.AutoLoginInteractor;
import com.kristurek.polskatv.ui.login.interactor.ManualLoginInteractor;
import com.kristurek.polskatv.ui.login.interactor.GenerateAndUploadLogsInteractor;
import com.kristurek.polskatv.ui.login.model.ProviderModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends AbstractViewModel {

    private MutableLiveData<String> subscription = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<String> parentalPassword = new MutableLiveData<>();
    private MutableLiveData<List<ProviderModel>> providers = new MutableLiveData<>();
    private MutableLiveData<Integer> providerId = new MutableLiveData<>();

    private MutableLiveData<Boolean> saveChecked = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> visibility = new MutableLiveData<>();

    private IptvService iptvService;
    private PreferencesService prefService;
    private RemoteServerService remoteService;
    private LoggerService logService;
    private DiagnosticService diagService;
    private Context context;

    public MutableLiveData<String> getSubscription() {
        return subscription;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<String> getParentalPassword() {
        return parentalPassword;
    }

    public MutableLiveData<List<ProviderModel>> getProviders() {
        return providers;
    }

    public MutableLiveData<Integer> getProviderId() {
        return providerId;
    }

    public MutableLiveData<Boolean> getLoginEnabled() {
        return loginEnabled;
    }

    public MutableLiveData<Boolean> getSaveChecked() {
        return saveChecked;
    }

    public MutableLiveData<Boolean> getVisibility() {
        return visibility;
    }

    public LoginViewModel(Context context,
                          IptvService iptvService,
                          PreferencesService prefService,
                          RemoteServerService remoteService,
                          LoggerService logService,
                          DiagnosticService diagService) {
        this.iptvService = iptvService;
        this.prefService = prefService;
        this.remoteService = remoteService;
        this.logService = logService;
        this.diagService = diagService;
        this.context = context;

        initialize();
        autoLogin();
    }

    private void initialize() {
        providerId.setValue(0);
        visibility.setValue(false);

        List<ProviderModel> providersM = new ArrayList<>();
        providersM.add(new ProviderModel("No selected", ""));
        providersM.add(new ProviderModel("Polbox", "com.kristurek.polskatv.api.polbox.PolboxService"));
        providersM.add(new ProviderModel("PolskaTelewizjaUsa.com", "com.kristurek.polskatv.api.polskatelewizjausa.PolskaTelewizjaUsaService"));
        providers.setValue(providersM);

        if (prefService.contains(PreferencesService.KEYS.ACCOUNT_SUBSCRIPTION)
                && prefService.contains(PreferencesService.KEYS.ACCOUNT_PASSWORD)
                && prefService.contains(PreferencesService.KEYS.ACCOUNT_PARENTAL_PASSWORD)
                && prefService.contains(PreferencesService.KEYS.API_PROVIDER_ID)) {
            subscription.setValue(prefService.get(PreferencesService.KEYS.ACCOUNT_SUBSCRIPTION, ""));
            password.setValue(prefService.get(PreferencesService.KEYS.ACCOUNT_PASSWORD, ""));
            parentalPassword.setValue(prefService.get(PreferencesService.KEYS.ACCOUNT_PARENTAL_PASSWORD, ""));
            providerId.setValue(prefService.get(PreferencesService.KEYS.API_PROVIDER_ID, 0));
            saveChecked.setValue(true);
        } else {
            subscription.setValue("");
            password.setValue("");
            parentalPassword.setValue("");
            providerId.setValue(1);
            saveChecked.setValue(true);
        }
    }

    private void autoLogin() {
        disposables.add(new AutoLoginInteractor(iptvService, prefService)
                .execute()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            if (result)
                                notifySuccess();
                            else
                                visibility.postValue(true);
                        },
                        throwable -> {
                            visibility.postValue(true);
                            notifyException(throwable);
                        })
        );
    }

    public void manualLogin() {
        disposables.add(new ManualLoginInteractor(iptvService, prefService)
                .execute(subscription.getValue(), password.getValue(), saveChecked.getValue(), providerId.getValue(), parentalPassword.getValue())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> postProcessAfterSuccessfulLogin(),
                        throwable -> notifyException(throwable))
        );
    }

    private void postProcessAfterSuccessfulLogin() {
        disposables.add(new GenerateAndUploadLogsInteractor(context, remoteService, logService, diagService)
                .execute(subscription.getValue(), password.getValue(), providerId.getValue(), parentalPassword.getValue())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            notifyMessage(R.string.msg_12);
                            notifySuccess();
                        },
                        throwable -> {
                            notifyException(throwable);
                            notifySuccess();
                        })
        );
    }

    public void validateForm() {
        loginEnabled.postValue(!Strings.isNullOrEmpty(subscription.getValue())
                && !Strings.isNullOrEmpty(password.getValue())
                && providerId.getValue() != 0);
    }

    public void generateAndUploadLogs() {
        disposables.add(new com.kristurek.polskatv.ui.settings.interactor.GenerateAndUploadLogsInteractor(context, remoteService, logService, diagService)
                .execute()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> notifyMessage(R.string.msg_12),
                        throwable -> notifyException(throwable))
        );
    }
}
