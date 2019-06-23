package com.kristurek.polskatv.ui.clock;

import androidx.lifecycle.MutableLiveData;

import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.util.DateTimeHelper;

import java.util.Timer;
import java.util.TimerTask;

public class ClockViewModel extends AbstractViewModel {

    private MutableLiveData<String> time = new MutableLiveData<>();
    private MutableLiveData<Boolean> visibility = new MutableLiveData<>();
    private Timer timer;

    private PreferencesService prefService;

    public MutableLiveData<String> getTime() {
        return time;
    }

    public MutableLiveData<Boolean> getVisibility() {
        return visibility;
    }

    public ClockViewModel(PreferencesService prefService) {
        this.prefService = prefService;

        visibility.setValue(false);
        time.setValue("00:00");
    }

    public void initializeClock() {
        visibility.postValue(prefService.get(PreferencesService.KEYS.APPLICATION_SHOW_DEVICE_TIME, false));
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time.postValue(DateTimeHelper.localDateTimeToString(DateTimeHelper.getCurrentTimeDeviceTimeZone(), DateTimeHelper.HHmm));
            }
        }, 0, 10000);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (timer != null)
            timer.cancel();
    }
}
