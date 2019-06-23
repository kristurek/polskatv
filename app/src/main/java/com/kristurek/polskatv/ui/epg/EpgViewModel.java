package com.kristurek.polskatv.ui.epg;

import androidx.lifecycle.MutableLiveData;

import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.ui.epg.model.EpgModel;
import com.kristurek.polskatv.ui.epgs.model.EpgType;
import com.kristurek.polskatv.ui.event.EpgCurrentTimeEvent;
import com.kristurek.polskatv.ui.event.SelectedEpgEvent;
import com.kristurek.polskatv.util.DateTimeHelper;

public class EpgViewModel extends AbstractViewModel {

    private MutableLiveData<String> dateTime = new MutableLiveData<>();
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<EpgType> type = new MutableLiveData<>();

    private MutableLiveData<String> currentTime = new MutableLiveData<>();
    private MutableLiveData<String> totalTime = new MutableLiveData<>();

    private MutableLiveData<Integer> currentSecond = new MutableLiveData<>();
    private MutableLiveData<Integer> totalSecond = new MutableLiveData<>();

    private MutableLiveData<Boolean> visibility = new MutableLiveData<>();

    private EpgModel model;

    //==============================================================================================

    public MutableLiveData<String> getDateTime() {
        return dateTime;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public MutableLiveData<EpgType> getType() {
        return type;
    }

    public MutableLiveData<String> getCurrentTime() {
        return currentTime;
    }

    public MutableLiveData<String> getTotalTime() {
        return totalTime;
    }

    public MutableLiveData<Integer> getCurrentSecond() {
        return currentSecond;
    }

    public MutableLiveData<Integer> getTotalSecond() {
        return totalSecond;
    }

    public MutableLiveData<Boolean> getVisibility() {
        return visibility;
    }

    //==============================================================================================

    public EpgViewModel() {
        visibility.setValue(false);
    }

    public void initializeEpg(SelectedEpgEvent event) {
        model = new EpgModel();
        model.setEpgBeginTime(event.getEpgBeginTime());
        model.setEpgEndTime(event.getEpgEndTime());
        model.setEpgCurrentTime(event.getEpgCurrentTime());

        name.postValue(event.getChannelName());
        title.postValue(event.getEpgTitle());
        type.postValue(event.getEpgType());
        dateTime.postValue(DateTimeHelper.rangeUnixTimeToString(event.getEpgBeginTime(), event.getEpgEndTime()));

        currentTime.postValue(DateTimeHelper.periodUnixTimeToString(event.getEpgBeginTime(), event.getEpgCurrentTime()));
        totalTime.postValue(DateTimeHelper.periodUnixTimeToString(event.getEpgBeginTime(), event.getEpgEndTime()));

        currentSecond.postValue((int) (event.getEpgCurrentTime() - event.getEpgBeginTime()));
        totalSecond.postValue((int) (event.getEpgEndTime() - event.getEpgBeginTime()));

        visibility.postValue(true);
    }

    public void updateProgress(EpgCurrentTimeEvent event) {
        if (model != null) {
            model.setEpgCurrentTime(event.getEpgCurrentTime());

            currentTime.postValue(DateTimeHelper.periodUnixTimeToString(model.getEpgBeginTime(), event.getEpgCurrentTime()));
            currentSecond.postValue((int) (event.getEpgCurrentTime() - model.getEpgBeginTime()));
        }
    }

    public void clearProgress() {
        model = null;
        visibility.setValue(false);

        name.postValue(null);
        title.postValue(null);
        type.postValue(EpgType.NOT_AVAILABLE);
        dateTime.postValue(null);

        currentTime.postValue(null);
        totalTime.postValue(null);

        currentSecond.postValue(0);
        totalSecond.postValue(0);
    }
}
