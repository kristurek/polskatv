package com.kristurek.polskatv.ui.console;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.ui.arch.Event;
import com.kristurek.polskatv.ui.console.model.ConsoleModel;
import com.kristurek.polskatv.ui.epgs.model.EpgType;
import com.kristurek.polskatv.ui.event.EpgCurrentTimeEvent;
import com.kristurek.polskatv.ui.event.FindCurrentEpgEvent;
import com.kristurek.polskatv.ui.event.PausePlayerEvent;
import com.kristurek.polskatv.ui.event.QuietPausePlayerEvent;
import com.kristurek.polskatv.ui.event.RecreateAppEvent;
import com.kristurek.polskatv.ui.event.ResumePlayerEvent;
import com.kristurek.polskatv.ui.event.SeekToTimeEvent;
import com.kristurek.polskatv.ui.event.SelectedEpgEvent;
import com.kristurek.polskatv.ui.event.StopPlayerEvent;
import com.kristurek.polskatv.util.Tag;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class ConsoleViewModel extends AbstractViewModel {

    private MutableLiveData<Boolean> paused = new MutableLiveData<>();
    private MutableLiveData<Boolean> played = new MutableLiveData<>();
    private MutableLiveData<EpgType> epgType = new MutableLiveData<>();
    private MutableLiveData<Boolean> visibility = new MutableLiveData<>();

    private ConsoleModel model;
    private PreferencesService prefService;

    public MutableLiveData<Boolean> getPaused() {
        return paused;
    }

    public MutableLiveData<Boolean> getPlayed() {
        return played;
    }

    public MutableLiveData<EpgType> getEpgType() {
        return epgType;
    }

    public MutableLiveData<Boolean> getVisibility() {
        return visibility;
    }

    public ConsoleViewModel(PreferencesService prefService) {
        this.prefService = prefService;

        this.paused.setValue(true);
        this.played.setValue(false);
        this.epgType.setValue(EpgType.NOT_AVAILABLE);
        this.visibility.setValue(prefService.get(PreferencesService.KEYS.APPLICATION_SHOW_CONSOLE, false));
    }

    //==============================================================================================

    public void selectPlayPause() {
        if (model != null && !model.getEpgType().equals(EpgType.NOT_AVAILABLE)) {
            if (getPlayed().getValue() && !getPaused().getValue()) {
                paused.postValue(true);
                played.postValue(false);

                getEventBus().post(new ResumePlayerEvent());
            } else {
                played.postValue(true);
                paused.postValue(false);

                getEventBus().post(new PausePlayerEvent());
            }
        }
    }

    public void selectForward() {
        if (model != null && model.getEpgType().equals(EpgType.ARCHIVE_EPG)) {
            if (!paused.getValue()) {
                played.postValue(false);
                paused.postValue(true);
            }
            Event event = determineIfNeedReloadArchiveEpg(Action.FORWARD_MOVE);
            if (event != null) {
                getEventBus().post(new QuietPausePlayerEvent());
                getEventBus().post(event);
            }
        }
    }

    public void selectFastForward() {
        if (model != null && model.getEpgType().equals(EpgType.ARCHIVE_EPG)) {
            if (!paused.getValue()) {
                played.postValue(false);
                paused.postValue(true);
            }
            Event event = determineIfNeedReloadArchiveEpg(Action.FAST_FORWARD_MOVE);
            if (event != null) {
                getEventBus().post(new QuietPausePlayerEvent());
                getEventBus().post(event);
            }
        }
    }

    public void selectBackward() {
        if (model != null && model.getEpgType().equals(EpgType.ARCHIVE_EPG)) {
            if (!paused.getValue()) {
                played.postValue(false);
                paused.postValue(true);
            }
            Event event = determineIfNeedReloadArchiveEpg(Action.BACKWARD_MOVE);
            if (event != null) {
                getEventBus().post(new QuietPausePlayerEvent());
                getEventBus().post(event);
            }
        }
    }

    public void selectFastBackward() {
        if (model != null && model.getEpgType().equals(EpgType.ARCHIVE_EPG)) {
            if (!paused.getValue()) {
                played.postValue(false);
                paused.postValue(true);
            }
            Event event = determineIfNeedReloadArchiveEpg(Action.FAST_BACKWARD_MOVE);
            if (event != null) {
                getEventBus().post(new QuietPausePlayerEvent());
                getEventBus().post(event);
            }
        }
    }

    //==============================================================================================

    public void updateProgress(EpgCurrentTimeEvent event) {
        Log.d(Tag.MASSIVE, "ConsoleViewModel.updateProgress()[" + event + "]");

        if (model != null) {
            model.setEpgCurrentTime(event.getEpgCurrentTime());

            Event newEvent;
            if (model.getEpgType().equals(EpgType.ARCHIVE_EPG))
                newEvent = determineIfNeedReloadArchiveEpg(Action.NATURAL_MOVE);
            else if (model.getEpgType().equals(EpgType.LIVE_EPG))
                newEvent = determineIfNeedReloadLiveEpg();
            else
                newEvent = null;

            if (newEvent != null) {
                getEventBus().post(new QuietPausePlayerEvent());
                getEventBus().post(newEvent);
            }
        }
    }

    public void selectEpg(SelectedEpgEvent event) {
        Log.d(Tag.EVENT, "ConsoleViewModel.selectEpg()[" + event + "]");

        epgType.postValue(event.getEpgType());

        paused.postValue(true);
        played.postValue(false);

        model = new ConsoleModel();
        model.setChannelId(event.getChannelId());
        model.setEpgType(event.getEpgType());
        model.setEpgBeginTime(event.getEpgBeginTime());
        model.setEpgEndTime(event.getEpgEndTime());
        model.setEpgCurrentTime(event.getEpgBeginTime());
    }

    public void stopPlayer(StopPlayerEvent event) {
        Log.d(Tag.EVENT, "ConsoleViewModel.onStopPlayer()[" + event + "]");

        epgType.postValue(EpgType.NOT_AVAILABLE);
        paused.postValue(true);
        played.postValue(false);

        model = null;
    }

    //==============================================================================================

    public Serializable openSettings() {
        if (model != null) {

            getEventBus().post(new QuietPausePlayerEvent());

            RecreateAppEvent event = new RecreateAppEvent();

            event.setChannelId(model.getChannelId());
            event.setEpgBeginTime(model.getEpgBeginTime());
            event.setEpgCurrentTime(model.getEpgCurrentTime());

            return event;
        } else
            return null;
    }

    //==============================================================================================

    private Event determineIfNeedReloadLiveEpg() {
        Log.d(Tag.MASSIVE, "ConsoleViewModel.determineLivePosition()");

        if (model.getEpgCurrentTime() > model.getEpgEndTime()) {
            FindCurrentEpgEvent event = new FindCurrentEpgEvent();
            event.setChannelId(model.getChannelId());
            event.setEpgCurrentTime(model.getEpgCurrentTime());
            event.setEpgBeginTime(model.getEpgBeginTime());
            event.setEpgEndTime(model.getEpgEndTime());
            event.setEpgType(model.getEpgType());

            return event;
        } else
            return null;
    }

    private Event determineIfNeedReloadArchiveEpg(Action action) {
        Log.d(Tag.MASSIVE, "ConsoleViewModel.determineIfNeedReloadArchiveEpg()[" + action + "]");

        long currentInSeconds = model.getEpgCurrentTime() - model.getEpgBeginTime();
        long stepInSeconds = getTimeStepInSeconds(action);
        long totalInSeconds = model.getEpgEndTime() - model.getEpgBeginTime();

        switch (action) {
            case NATURAL_MOVE: {
                if ((currentInSeconds + stepInSeconds) >= totalInSeconds) {
                    FindCurrentEpgEvent event = new FindCurrentEpgEvent();
                    event.setChannelId(model.getChannelId());
                    event.setEpgCurrentTime(model.getEpgCurrentTime() + stepInSeconds);
                    event.setEpgBeginTime(model.getEpgBeginTime());
                    event.setEpgEndTime(model.getEpgEndTime());
                    event.setEpgType(model.getEpgType());

                    return event;
                } else
                    return null;
            }
            case FORWARD_MOVE:
            case FAST_FORWARD_MOVE:
            case BACKWARD_MOVE:
            case FAST_BACKWARD_MOVE: {
                if ((currentInSeconds + stepInSeconds) >= totalInSeconds || (currentInSeconds + stepInSeconds) < 0) {
                    FindCurrentEpgEvent event = new FindCurrentEpgEvent();
                    event.setChannelId(model.getChannelId());
                    event.setEpgCurrentTime(model.getEpgCurrentTime() + stepInSeconds);
                    event.setEpgBeginTime(model.getEpgBeginTime());
                    event.setEpgEndTime(model.getEpgEndTime());
                    event.setEpgType(model.getEpgType());

                    return event;
                } else {
                    SeekToTimeEvent event = new SeekToTimeEvent();
                    event.setChannelId(model.getChannelId());
                    event.setEpgCurrentTime(model.getEpgCurrentTime() + stepInSeconds);
                    event.setEpgBeginTime(model.getEpgBeginTime());
                    event.setEpgEndTime(model.getEpgEndTime());
                    event.setEpgType(model.getEpgType());

                    return event;
                }
            }
            default:
                return null;
        }
    }

    private Long getTimeStepInSeconds(Action action) {
        switch (action) {
            case FORWARD_MOVE:
                return TimeUnit.MINUTES.toSeconds(prefService.get(PreferencesService.KEYS.PLAYER_FORWARD_MOVE, 0));
            case FAST_FORWARD_MOVE:
                return TimeUnit.MINUTES.toSeconds(prefService.get(PreferencesService.KEYS.PLAYER_FAST_FORWARD_MOVE, 0));
            case BACKWARD_MOVE:
                return -TimeUnit.MINUTES.toSeconds(prefService.get(PreferencesService.KEYS.PLAYER_BACKWARD_MOVE, 0));
            case FAST_BACKWARD_MOVE:
                return -TimeUnit.MINUTES.toSeconds(prefService.get(PreferencesService.KEYS.PLAYER_FAST_BACKWARD_MOVE, 0));
            case NATURAL_MOVE:
                return 0L;
            default:
                return 0L;
        }
    }

    private enum Action {
        NATURAL_MOVE,
        FORWARD_MOVE,
        FAST_FORWARD_MOVE,
        BACKWARD_MOVE,
        FAST_BACKWARD_MOVE;
    }
}