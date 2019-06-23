package com.kristurek.polskatv.ui.main;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.ui.main.interactor.ExitInteractor;
import com.kristurek.polskatv.ui.main.interactor.LogoutInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends AbstractViewModel {

    private MutableLiveData<Boolean> visibility = new MutableLiveData<>();

    private Context context;
    private IptvService iptvService;
    private PreferencesService prefService;

    //==============================================================================================

    public MutableLiveData<Boolean> getVisibility() {
        return visibility;
    }

    //==============================================================================================

    public MainViewModel(Context context,
                         IptvService iptvService,
                         PreferencesService prefService) {
        this.iptvService = iptvService;
        this.prefService = prefService;
        this.context = context;

        visibility.setValue(true);
    }

    //==============================================================================================

    public boolean onTouchEvent() {
        if (!visibility.getValue()) {
            visibility.postValue(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean onBackPressed() {
        if (visibility.getValue()) {
            visibility.postValue(false);
            return false;
        } else
            return true;
    }

    public void onExitSelected() {
        disposables.add(new ExitInteractor(context, iptvService, prefService)
                .execute()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                }, throwable -> notifyException(throwable)));
    }

    public void onLogoutSelected() {
        disposables.add(new LogoutInteractor(context, iptvService, prefService)
                .execute()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                }, throwable -> notifyException(throwable)));
    }
}
