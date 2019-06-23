package com.kristurek.polskatv.ui.arch;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public abstract class AbstractViewModel extends ViewModel {

    protected MutableLiveData<Throwable> exceptionNotifier = new MutableLiveData<>();

    protected MutableLiveData<Boolean> successNotifier = new MutableLiveData<>();

    protected MutableLiveData<Integer> messageNotifier = new MutableLiveData<>();

    protected final CompositeDisposable disposables = new CompositeDisposable();

    private EventBus eventBus;

    //==============================================================================================

    public MutableLiveData<Throwable> getExceptionNotifier() {
        return exceptionNotifier;
    }

    public MutableLiveData<Boolean> getSuccessNotifier() {
        return successNotifier;
    }

    public MutableLiveData<Integer> getMessageNotifier() {
        return messageNotifier;
    }

    //==============================================================================================

    protected void notifySuccess() {
        successNotifier.postValue(true);
    }

    protected void notifyException(Throwable throwable) {
        exceptionNotifier.postValue(throwable);
    }

    protected void notifyMessage(int resId) {
        messageNotifier.postValue(resId);
    }

    public void initializeEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public EventBus getEventBus() {
        if (eventBus == null)
            throw new IllegalStateException("Event Bus doesn't initialized");

        return eventBus;
    }

    public interface EventBus {
        void post(Event event);
    }

    //==============================================================================================

    @Override
    protected void onCleared() {
        super.onCleared();

        disposables.clear();
    }
}
