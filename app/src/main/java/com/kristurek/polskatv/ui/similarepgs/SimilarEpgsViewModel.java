package com.kristurek.polskatv.ui.similarepgs;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.ui.epgs.model.EpgModel;
import com.kristurek.polskatv.ui.event.QuietPausePlayerEvent;
import com.kristurek.polskatv.ui.event.RecreateAppEvent;
import com.kristurek.polskatv.ui.similarepgs.interactor.FindSimilarEpgsInteractor;
import com.kristurek.polskatv.util.Tag;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SimilarEpgsViewModel extends AbstractViewModel {

    private MutableLiveData<List<EpgModel>> epgs = new MutableLiveData<>();
    private MutableLiveData<EpgModel> focusedEpg = new MutableLiveData<>();
    private MutableLiveData<Boolean> noResults = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private IptvService iptvService;
    private PreferencesService prefService;

    public MutableLiveData<List<EpgModel>> getEpgs() {
        return epgs;
    }

    public MutableLiveData<EpgModel> getFocusedEpg() {
        return focusedEpg;
    }

    public MutableLiveData<Boolean> getNoResults() {
        return noResults;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public SimilarEpgsViewModel(IptvService iptvService, PreferencesService prefService) {
        this.iptvService = iptvService;
        this.prefService = prefService;

        epgs.setValue(new ArrayList<>());
        noResults.setValue(false);
        loading.setValue(false);
    }

    public void initialize(Integer channelId, String title, Boolean manyChannels) {
        Log.d(Tag.UI, "SimilarEpgsViewModel.initializeEpgs()[" + channelId + ", " + title + "]");

        loading.postValue(true);

        disposables.add(new FindSimilarEpgsInteractor(iptvService, prefService)
                .execute(channelId, title, manyChannels)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> postProcessAfterInitializationEpgs(result), throwable -> notifyException(throwable)));
    }

    private void postProcessAfterInitializationEpgs(List<EpgModel> result) {
        epgs.postValue(result);

        loading.postValue(false);

        if (result.isEmpty())
            noResults.postValue(true);
        else {
            noResults.postValue(false);
            focusedEpg.postValue(result.get(0));
        }
    }

    public void selectEpg(EpgModel item) {
        Log.d(Tag.UI, "SimilarEpgsViewModel.selectEpg()[" + item + "]");

        focusedEpg.postValue(item);

        RecreateAppEvent event = new RecreateAppEvent();
        event.setEpgBeginTime(item.getBeginTime());
        event.setEpgCurrentTime(item.getBeginTime());
        event.setChannelId(item.getChannelId());

        getEventBus().post(new QuietPausePlayerEvent());
        getEventBus().post(event);
    }

    protected void initializeEmpty() {
        epgs.setValue(new ArrayList<>());
        noResults.setValue(false);
        loading.setValue(false);
    }
}
