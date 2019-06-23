package com.kristurek.polskatv.ui.volume;

import androidx.lifecycle.MutableLiveData;

import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.ui.event.VolumePlayerEvent;

public class VolumeViewModel extends AbstractViewModel {

    private MutableLiveData<Integer> progress = new MutableLiveData<>();
    private MutableLiveData<Boolean> visibility = new MutableLiveData<>();

    private PreferencesService prefService;

    public MutableLiveData<Integer> getProgress() {
        return progress;
    }

    public MutableLiveData<Boolean> getVisibility() {
        return visibility;
    }

    public VolumeViewModel(PreferencesService prefService) {
        this.prefService = prefService;
    }

    public void initialize() {
        visibility.postValue(prefService.get(PreferencesService.KEYS.APPLICATION_SHOW_VOLUME, false));
        progress.postValue((int) (prefService.get(PreferencesService.KEYS.PLAYER_VOLUME, 1.0f) * 100));

        getEventBus().post(new VolumePlayerEvent(prefService.get(PreferencesService.KEYS.PLAYER_VOLUME, 1.0f)));
    }

    public void persistCurrentVolume() {
        if (progress.getValue() != null)
            prefService.save(PreferencesService.KEYS.PLAYER_VOLUME, progress.getValue() / 100f);
    }

    public void changeProgress(int value) {
        progress.postValue(value);

        prefService.save(PreferencesService.KEYS.PLAYER_VOLUME, value / 100f);
        getEventBus().post(new VolumePlayerEvent(value / 100f));
    }

    public void volumeUp() {
        if (progress.getValue() + 1 <= 100) {
            int value = progress.getValue() + 1;
            progress.postValue(value);

            prefService.save(PreferencesService.KEYS.PLAYER_VOLUME, value / 100f);
            getEventBus().post(new VolumePlayerEvent(value / 100f));
        }
    }

    public void volumeDown() {
        if (progress.getValue() - 1 >= 0) {
            int value = progress.getValue() - 1;
            progress.postValue(value);

            prefService.save(PreferencesService.KEYS.PLAYER_VOLUME, value / 100f);
            getEventBus().post(new VolumePlayerEvent(value / 100f));
        }
    }
}
