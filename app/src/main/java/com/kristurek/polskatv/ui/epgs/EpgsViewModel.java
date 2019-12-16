package com.kristurek.polskatv.ui.epgs;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.kristurek.polskatv.R;
import com.kristurek.polskatv.iptv.FactoryService;
import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.ui.epgs.interactor.InitializeEpgsInteractor;
import com.kristurek.polskatv.ui.epgs.model.EpgModel;
import com.kristurek.polskatv.ui.epgs.model.EpgType;
import com.kristurek.polskatv.ui.event.FindCurrentEpgEvent;
import com.kristurek.polskatv.ui.event.QuietPausePlayerEvent;
import com.kristurek.polskatv.ui.event.RecreateAppEvent;
import com.kristurek.polskatv.ui.event.SelectedChannelEvent;
import com.kristurek.polskatv.ui.event.SelectedEpgEvent;
import com.kristurek.polskatv.ui.event.StopPlayerEvent;
import com.kristurek.polskatv.util.DateTimeHelper;
import com.kristurek.polskatv.util.Tag;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EpgsViewModel extends AbstractViewModel {

    private MutableLiveData<List<EpgModel>> epgs = new MutableLiveData<>();
    private MutableLiveData<EpgModel> selectedEpg = new MutableLiveData<>();
    private MutableLiveData<EpgModel> focusedEpg = new MutableLiveData<>();
    private MutableLiveData<List<LocalDate>> days = new MutableLiveData<>();
    private MutableLiveData<LocalDate> selectedDay = new MutableLiveData<>();
    private MutableLiveData<Boolean> needRefresh = new MutableLiveData<>();

    private Integer selectedChannelId;
    private String selectedChannelName;

    private IptvService iptvService;
    private PreferencesService prefService;

    private Timer timer;

    public MutableLiveData<List<EpgModel>> getEpgs() {
        return epgs;
    }

    public MutableLiveData<EpgModel> getSelectedEpg() {
        return selectedEpg;
    }

    public MutableLiveData<EpgModel> getFocusedEpg() {
        return focusedEpg;
    }

    public MutableLiveData<List<LocalDate>> getDays() {
        return days;
    }

    public MutableLiveData<LocalDate> getSelectedDay() {
        return selectedDay;
    }

    public MutableLiveData<Boolean> getNeedRefresh() {
        return needRefresh;
    }

    public EpgsViewModel(PreferencesService prefService) {
        this.iptvService = FactoryService.SERVICE.getInstance();
        this.prefService = prefService;

        epgs.setValue(new ArrayList<>());
        needRefresh.setValue(false);

        initializeDays();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (timer != null)
            timer.cancel();
    }

    public void startUpdateProcess() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshEpgs();
            }
        }, 60000, 20000);
    }

    private void refreshEpgs() {
        long currentUnixTime = DateTimeHelper.localDateTimeToUnixTime(DateTimeHelper.getCurrentTimeSelectedTimeZone());

        for (EpgModel epg : epgs.getValue()) {
            if (epg.getType().equals(EpgType.LIVE_EPG) && currentUnixTime > epg.getEndTime()) {
                epg.setType(EpgType.ARCHIVE_EPG);
                epg.setIcon(R.drawable.ic_play);
            }
            if (epg.getType().equals(EpgType.NOT_AVAILABLE) && currentUnixTime >= epg.getBeginTime() && currentUnixTime < epg.getEndTime()) {
                epg.setType(EpgType.LIVE_EPG);
                epg.setIcon(R.drawable.ic_wait);
            }
        }
        needRefresh.postValue(true);
    }

    private void initializeDays() {
        days.setValue(DateTimeHelper.generateDays());

        LocalDate selectedDate = null;
        if (selectedDay.getValue() == null)
            if (prefService.get(PreferencesService.KEYS.APPLICATION_USE_CUSTOM_TIME_AND_PARTIAL_TIME_ZONE_IN_EPGS, true))
                selectedDate = DateTimeHelper.getCurrentDayDeviceTimeZone();
            else
                selectedDate = DateTimeHelper.getCurrentDaySelectedTimeZone();
        else
            selectedDate = selectedDay.getValue();

        selectedDay.setValue(selectedDate);
    }

    public void selectDay(int position) {
        Log.d(Tag.UI, "EpgsViewModel.selectDay()[" + position + ", " + days.getValue().get(position) + "]");

        selectedDay.postValue(days.getValue().get(position));

        if (selectedChannelId != null)
            initializeEpgs(selectedChannelId, days.getValue().get(position), selectedChannelName);
    }

    public void initializeEpgs(SelectedChannelEvent event) {
        Log.d(Tag.UI, "EpgsViewModel.initializeEpgs()[" + event + "]");

        selectedChannelId = event.getChannelId();
        selectedChannelName = event.getChannelName();

        initializeEpgs(event.getChannelId(), selectedDay.getValue(), selectedChannelName);
    }

    private void initializeEpgs(Integer channelId, LocalDate day, String channelName) {
        Log.d(Tag.UI, "EpgsViewModel.initializeEpgs()[" + channelId + ", " + day + "]");

        disposables.add(new InitializeEpgsInteractor(iptvService)
                .execute(channelId, day, channelName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> postProcessAfterInitializeEpgs(result), throwable -> notifyException(throwable)));
    }

    private void postProcessAfterInitializeEpgs(List<EpgModel> result) {
        epgs.postValue(result);
        selectedEpg.postValue(null);

        if (!result.isEmpty())
            if (DateTimeHelper.getCurrentDayDeviceTimeZone().isEqual(selectedDay.getValue()))
                if (prefService.get(PreferencesService.KEYS.APPLICATION_USE_CUSTOM_TIME_AND_PARTIAL_TIME_ZONE_IN_EPGS, true)) {
                    String customTime = prefService.get(PreferencesService.KEYS.APPLICATION_CUSTOM_TIME_TO_FIND_DEFAULT_POSITION_IN_EPGS, "17:00");
                    long customDateTime = DateTimeHelper.customDateTimeToUnixTime(selectedDay.getValue(), customTime);

                    int compare = DateTimeHelper.compareCurrentDayBetweenTimeZones();
                    if (compare == 1)
                        focusedEpg.postValue(findEpg(result, customDateTime, true));
                    else if (compare == -1)
                        focusedEpg.postValue(findEpg(result, customDateTime, true));
                    else
                        focusedEpg.postValue(findEpg(result, 0, false));
                } else
                    focusedEpg.postValue(findEpg(result, 0, false));
            else
                focusedEpg.postValue(result.get(0));
    }

    private EpgModel findEpg(List<EpgModel> result, long customTime, boolean enabledCustomTime) {
        EpgModel model = Iterables.find(result, input -> enabledCustomTime && (customTime >= input.getBeginTime() && customTime < input.getEndTime()), null);
        if (model != null)
            return model;
        else {
            model = Iterables.find(result, input -> input.getType().equals(EpgType.LIVE_EPG), null);
            if (model != null)
                return model;
            else {
                model = Iterables.find(Lists.reverse(result), input -> input.getType().equals(EpgType.ARCHIVE_EPG), null);
                if (model != null)
                    return model;
                else
                    return result.get(0);
            }
        }
    }

    public void selectEpg(EpgModel item) {
        Log.d(Tag.UI, "EpgsViewModel.selectEpg()[" + item + "]");

        if (!item.getType().equals(EpgType.NOT_AVAILABLE)) {
            selectedEpg.postValue(item);
            focusedEpg.postValue(item);

            SelectedEpgEvent event = new SelectedEpgEvent();
            event.setEpgTitle(item.getTitle());
            event.setEpgBeginTime(item.getBeginTime());
            event.setEpgEndTime(item.getEndTime());

            event.setEpgType(item.getType());
            event.setChannelName(item.getChannelName());
            event.setChannelId(item.getChannelId());

            if (item.getType().equals(EpgType.LIVE_EPG))
                event.setEpgCurrentTime(DateTimeHelper.localDateTimeToUnixTime(DateTimeHelper.getCurrentTimeSelectedTimeZone()));
            else
                event.setEpgCurrentTime(item.getBeginTime());

            getEventBus().post(new QuietPausePlayerEvent());
            getEventBus().post(event);
        } else {
            selectedEpg.postValue(null);
            getMessageNotifier().postValue(R.string.msg_11);
        }
    }

    public void selectNextDay() {
        LocalDate nextDay = DateTimeHelper.getNextDay(selectedDay.getValue());

        if (nextDay != null && selectedChannelId != null) {
            selectedDay.postValue(nextDay);
            initializeEpgs(selectedChannelId, nextDay, selectedChannelName);
        } else
            getMessageNotifier().postValue(R.string.msg_10);
    }

    public void resolveCurrentEpg(FindCurrentEpgEvent event) {
        Log.d(Tag.EVENT, "EpgsViewModel.resolveCurrentEpg()[" + event + "]");

        LocalDate currentDay = DateTimeHelper.unixTimeToLocalDate(event.getEpgCurrentTime());

        disposables.add(new InitializeEpgsInteractor(iptvService)
                .execute(event.getChannelId(), currentDay, event.getChannelName())
                .flatMap(epgModels -> {
                            if (!epgModels.isEmpty()) {
                                if (event.getEpgCurrentTime() < epgModels.iterator().next().getBeginTime())
                                    return new InitializeEpgsInteractor(iptvService).execute(event.getChannelId(), DateTimeHelper.getPreviousDay(currentDay), event.getChannelName());
                                if (event.getEpgCurrentTime() > Iterables.getLast(epgModels).getEndTime())
                                    return new InitializeEpgsInteractor(iptvService).execute(event.getChannelId(), DateTimeHelper.getNextDay(currentDay), event.getChannelName());
                            }
                            return Single.just(epgModels);
                        }
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> postProcessAfterInitializeEpgs(result, event.getEpgCurrentTime()), throwable -> notifyException(throwable)));
    }

    public void recreateEpgs(RecreateAppEvent event) {
        Log.d(Tag.UI, "EpgsViewModel.recreateEpgs()[" + event + "]");

        selectedChannelId = event.getChannelId();
        selectedChannelName = event.getChannelName();
        LocalDate currentDay = DateTimeHelper.unixTimeToLocalDate(event.getEpgBeginTime());

        disposables.add(new InitializeEpgsInteractor(iptvService)
                .execute(event.getChannelId(), currentDay, event.getChannelName())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> postProcessAfterInitializeEpgs(result, event.getEpgCurrentTime()), throwable -> notifyException(throwable)));
    }

    private void postProcessAfterInitializeEpgs(List<EpgModel> result,
                                                long epgCurrentTime) {
        epgs.postValue(result);
        selectedEpg.postValue(null);

        EpgModel epg = Iterables.find(result, input -> epgCurrentTime >= input.getBeginTime() && epgCurrentTime < input.getEndTime() && !input.getType().equals(EpgType.NOT_AVAILABLE), null);

        if (epg != null) {
            SelectedEpgEvent selectedEpgEvent = new SelectedEpgEvent();
            selectedDay.postValue(DateTimeHelper.unixTimeToLocalDate(epg.getBeginTime()));
            selectedEpg.postValue(epg);
            focusedEpg.postValue(epg);

            selectedEpgEvent.setEpgTitle(epg.getTitle());
            selectedEpgEvent.setEpgBeginTime(epg.getBeginTime());
            selectedEpgEvent.setEpgEndTime(epg.getEndTime());
            selectedEpgEvent.setEpgType(epg.getType());
            selectedEpgEvent.setChannelName(epg.getChannelName());
            selectedEpgEvent.setChannelId(epg.getChannelId());

            if (epg.getType().equals(EpgType.LIVE_EPG))
                selectedEpgEvent.setEpgCurrentTime(DateTimeHelper.localDateTimeToUnixTime(DateTimeHelper.getCurrentTimeSelectedTimeZone()));
            else
                selectedEpgEvent.setEpgCurrentTime(epgCurrentTime);

            getEventBus().post(new QuietPausePlayerEvent());
            getEventBus().post(selectedEpgEvent);
        } else {
            getEventBus().post(new StopPlayerEvent());
        }
    }

    public boolean enableRightFocus() {
        return prefService.get(PreferencesService.KEYS.APPLICATION_SHOW_VOLUME, false);
    }
}