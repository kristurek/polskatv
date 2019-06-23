package com.kristurek.polskatv.ui.channels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.ui.channels.interactor.InitializeChannelsInteractor;
import com.kristurek.polskatv.ui.channels.interactor.UpdateChannelsInteractor;
import com.kristurek.polskatv.ui.channels.model.ChannelModel;
import com.kristurek.polskatv.ui.event.RecreateAppEvent;
import com.kristurek.polskatv.ui.event.SelectedChannelEvent;
import com.kristurek.polskatv.util.DateTimeHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChannelsViewModel extends AbstractViewModel {

    private MutableLiveData<List<Serializable>> channels = new MutableLiveData<>();
    private MutableLiveData<Serializable> selectedChannel = new MutableLiveData<>();
    private MutableLiveData<Serializable> focusedChannel = new MutableLiveData<>();
    private MutableLiveData<Boolean> needRefresh = new MutableLiveData<>();

    public MutableLiveData<Serializable> getSelectedChannel() {
        return selectedChannel;
    }

    public MutableLiveData<Serializable> getFocusedChannel() {
        return focusedChannel;
    }

    public MutableLiveData<List<Serializable>> getChannels() {
        return channels;
    }

    public MutableLiveData<Boolean> getNeedRefresh() {
        return needRefresh;
    }

    private IptvService iptvService;
    private PreferencesService prefService;
    private Context context;
    private Timer timer;

    public ChannelsViewModel(Context context,
                             IptvService iptvService, PreferencesService prefService) {
        this.iptvService = iptvService;
        this.prefService = prefService;
        this.context = context;

        channels.setValue(new ArrayList<>());
        needRefresh.setValue(false);
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
                refreshProgress();
                refreshChannels();
            }
        }, 60000, 20000);
    }

    private void refreshProgress() {
        for (Serializable element : channels.getValue())
            if (element instanceof ChannelModel) {
                ChannelModel channel = (ChannelModel) element;
                channel.setLiveEpgProgress(DateTimeHelper.currentPercentBetweenUnixTime(channel.getLiveEpgBeginTime(), channel.getLiveEpgEndTime()));
            }
        needRefresh.postValue(true);
    }

    private void refreshChannels() {
        Collection<Serializable> filteredChannels = Collections2.filter(channels.getValue(), input -> input instanceof ChannelModel && ((ChannelModel) input).getLiveEpgProgress() == 100);
        Collection<Integer> channelIds = Collections2.transform(filteredChannels, input -> ((ChannelModel) input).getId());

        if (!channelIds.isEmpty())
            disposables.add(new UpdateChannelsInteractor(context, iptvService)
                    .execute(channelIds)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(results -> postProcessAfterUpdateChannels(results), throwable -> notifyException(throwable)));
    }

    private void postProcessAfterUpdateChannels(Collection<Serializable> results) {
        Iterable<Serializable> onlyChannels = Iterables.filter(channels.getValue(), input -> input instanceof ChannelModel);

        for (Serializable element : results) {
            ChannelModel model = (ChannelModel) element;

            Iterable<Serializable> filter = Iterables.filter(onlyChannels, input -> ((ChannelModel) input).getId() == model.getId());
            ChannelModel channelToUpdate = (ChannelModel) filter.iterator().next();

            channelToUpdate.setLiveEpgTitle(model.getLiveEpgTitle());
            channelToUpdate.setTime(model.getTime());
            channelToUpdate.setLiveEpgBeginTime(model.getLiveEpgBeginTime());
            channelToUpdate.setLiveEpgEndTime(model.getLiveEpgEndTime());
            channelToUpdate.setLiveEpgProgress(model.getLiveEpgProgress());
            channelToUpdate.setLiveEpgDescription(model.getLiveEpgDescription());
        }

        needRefresh.postValue(true);
    }

    public void selectChannel(Serializable item) {
        selectedChannel.postValue(item);
        focusedChannel.postValue(item);

        SelectedChannelEvent event = new SelectedChannelEvent();
        event.setChannelId(((ChannelModel) item).getId());

        getEventBus().post(event);
    }

    public void initializeChannels() {
        disposables.add(new InitializeChannelsInteractor(context, iptvService, prefService)
                .execute()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> postProcessAfterInitializeChannels(result), throwable -> notifyException(throwable)));
    }

    private void postProcessAfterInitializeChannels(List<Serializable> result) {
        Serializable item = Iterables.find(result, input -> input instanceof ChannelModel, null);
        channels.postValue(result);
        selectedChannel.postValue(item);

        SelectedChannelEvent event = new SelectedChannelEvent();
        event.setChannelId(((ChannelModel) item).getId());

        getEventBus().post(event);
    }

    public void recreateChannels(RecreateAppEvent event) {
        disposables.add(new InitializeChannelsInteractor(context, iptvService, prefService)
                .execute()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> postProcessAfterRecreateChannels(result, event.getChannelId()), throwable -> notifyException(throwable)));
    }

    private void postProcessAfterRecreateChannels(List<Serializable> result, int channelId) {
        Serializable item = Iterables.find(result, input -> input instanceof ChannelModel && ((ChannelModel) input).getId() == channelId, null);
        channels.postValue(result);
        selectedChannel.postValue(item);
    }
}
