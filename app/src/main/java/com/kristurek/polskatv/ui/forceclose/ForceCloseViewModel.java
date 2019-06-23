package com.kristurek.polskatv.ui.forceclose;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.service.DiagnosticService;
import com.kristurek.polskatv.service.LoggerService;
import com.kristurek.polskatv.service.RemoteServerService;
import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.ui.forceclose.interactor.GenerateAndUploadLogsInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ForceCloseViewModel extends AbstractViewModel {

    private MutableLiveData<String> error = new MutableLiveData<>();

    private RemoteServerService remoteService;
    private LoggerService logService;
    private DiagnosticService diagService;
    private Context context;

    public MutableLiveData<String> getError() {
        return error;
    }

    public ForceCloseViewModel(Context context,
                               RemoteServerService remoteService,
                               LoggerService logService,
                               DiagnosticService diagService) {
        this.remoteService = remoteService;
        this.logService = logService;
        this.diagService = diagService;
        this.context = context;
    }

    public void initialize(String errorMsg) {
        error.postValue(errorMsg);

        disposables.add(new GenerateAndUploadLogsInteractor(context, remoteService, logService, diagService)
                .execute(errorMsg)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> notifyMessage(R.string.msg_12),
                        throwable -> notifyException(throwable))
        );
    }
}
