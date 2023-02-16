package com.kristurek.polskatv.ui.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.kristurek.polskatv.iptv.FactoryService;
import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.ui.event.EpgCurrentTimeEvent;
import com.kristurek.polskatv.ui.event.PausePlayerEvent;
import com.kristurek.polskatv.ui.event.QuietPausePlayerEvent;
import com.kristurek.polskatv.ui.event.ResumePlayerEvent;
import com.kristurek.polskatv.ui.event.SeekToTimeEvent;
import com.kristurek.polskatv.ui.event.StopPlayerEvent;
import com.kristurek.polskatv.ui.event.StreamEndedEvent;
import com.kristurek.polskatv.ui.event.VolumePlayerEvent;
import com.kristurek.polskatv.ui.player.interactor.InitializeUrlInteractor;
import com.kristurek.polskatv.ui.player.model.PlayerModel;
import com.kristurek.polskatv.util.Tag;

import java.util.concurrent.atomic.AtomicBoolean;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlayerViewModel extends AbstractViewModel {

    private MutableLiveData<ExoPlayer> player = new MutableLiveData<>();
    private MutableLiveData<Integer> loading = new MutableLiveData<>();
    private MutableLiveData<Integer> paused = new MutableLiveData<>();

    private PlayerTimeline timeline;

    private static final int CONNECT_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 30000;

    private final Object lock = new Object();

    private Handler handler = new Handler();

    private IptvService iptvService;
    private PreferencesService prefService;
    private Context context;

    public MutableLiveData<ExoPlayer> getPlayer() {
        return player;
    }

    public MutableLiveData<Integer> getLoading() {
        return loading;
    }

    public MutableLiveData<Integer> getPaused() {
        return paused;
    }

    //==============================================================================================

    private Runnable refreshJob = new Runnable() {
        @Override
        public void run() {
            refresh();
            handler.postDelayed(this, 300);
        }
    };

    //==============================================================================================

    public PlayerViewModel(Context context,
                           PreferencesService prefService) {
        this.iptvService = FactoryService.SERVICE.getInstance();
        this.prefService = prefService;
        this.context = context;

        paused.setValue(View.GONE);
        loading.setValue(View.GONE);

        handler.postDelayed(refreshJob, 0);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacks(refreshJob);

        releasePlayer();
    }

    private void refresh() {
        if (timeline != null && timeline.isActiveCounter())
            getEventBus().post(new EpgCurrentTimeEvent(timeline.getEpgCurrentTime()));

        if (player.getValue() != null) {
            switch (player.getValue().getPlaybackState()) {
                case Player.STATE_BUFFERING:
                    Log.d(Tag.MASSIVE, "PlayerViewModel.refresh()[Player.STATE_BUFFERING]");
                    loading.postValue(View.VISIBLE);
                    break;
                case Player.STATE_ENDED:
                    Log.d(Tag.MASSIVE, "PlayerViewModel.refresh()[Player.STATE_ENDED]");

                    releasePlayer();
                    getEventBus().post(new StreamEndedEvent());
                    break;
                case Player.STATE_IDLE:
                    Log.d(Tag.MASSIVE, "PlayerViewModel.refresh()[Player.STATE_IDLE]");
                    loading.postValue(View.GONE);
                    break;
                case Player.STATE_READY:
                    Log.d(Tag.MASSIVE, "PlayerViewModel.refresh()[Player.STATE_READY]");
                    loading.postValue(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    //==============================================================================================

    private final AtomicBoolean locked = new AtomicBoolean(false);

    void startPlayer(SeekToTimeEvent event) {
        Log.d(Tag.EVENT, "PlayerViewModel.startPlayer()[" + event + "]");

        if (locked.compareAndSet(false, true)) {
            disposables.add(new InitializeUrlInteractor(iptvService, prefService)
                    .execute(event.getChannelId(), event.getEpgType(), event.getEpgCurrentTime())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                                locked.set(false);
                                postProcessAfterInitializationUrl(result);
                            }, throwable -> {
                                locked.set(false);
                                notifyException(throwable);
                            }
                    ));
        } else {
            Log.d(Tag.SERVICE, "PlayerViewModel.startPlayer()[ignored startPlayer action, another action is in progress]");
        }
    }

    void stopPlayer(StopPlayerEvent event) {
        Log.d(Tag.EVENT, "PlayerViewModel.stopPlayer()[" + event + "]");

        releasePlayer();

        loading.postValue(View.GONE);
        paused.postValue(View.GONE);
    }

    void pausePlayer(PausePlayerEvent event) {
        Log.d(Tag.EVENT, "PlayerViewModel.pausePlayer()[" + event + "]");

        if (player.getValue() != null) {
            player.getValue().setPlayWhenReady(false);
            paused.postValue(View.VISIBLE);
        }
    }

    void quietPausePlayer(QuietPausePlayerEvent event) {
        Log.d(Tag.EVENT, "PlayerViewModel.quietPausePlayer()[" + event + "]");

        if (player.getValue() != null) {
            player.getValue().setPlayWhenReady(false);
        }
    }

    void resumePlayer(ResumePlayerEvent event) {
        Log.d(Tag.EVENT, "PlayerViewModel.resumePlayer()[" + event + "]");

        if (player.getValue() != null) {
            player.getValue().setPlayWhenReady(true);
            paused.postValue(View.GONE);
        }
    }

    void changeVolume(VolumePlayerEvent event) {
        Log.d(Tag.EVENT, "PlayerViewModel.changeVolume()[" + event + "]");

        if (player.getValue() != null)
            player.getValue().setVolume(event.getVolume());
    }

    //==============================================================================================

    private void releasePlayer() {
        synchronized (lock) {
            Log.d(Tag.UI, "PlayerViewModel.release()[begin]");

            ExoPlayerFactory.destroyInstance();
            player.postValue(null);

            if (timeline != null) {
                timeline.stop();
                timeline = null;
            }

            loading.postValue(View.GONE);
            paused.postValue(View.GONE);

            Log.d(Tag.UI, "PlayerViewModel.release()[end]");
        }
    }

    private void postProcessAfterInitializationUrl(PlayerModel result) {
        releasePlayer();
        synchronized (lock) {
            Log.d(Tag.UI, "PlayerViewModel.postProcessAfterInitializationUrl()[begin]");
            Log.d(Tag.UI, "PlayerViewModel.postProcessAfterInitializationUrl()[" + result + "]");

            ExoTrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
            TrackSelector trackSelector = new DefaultTrackSelector(context, videoTrackSelectionFactory);

            ExoPlayer internalPlayer = ExoPlayerFactory.createInstance(context, trackSelector);

            Log.d(Tag.UI, "PlayerViewModel.postProcessAfterInitializationUrl() hash[" + internalPlayer.hashCode() + "]");

            Uri uri = Uri.parse(result.getUrl());
            //Uri uri = Uri.parse("/storage/emulated/0/download/sd.mp4");
            String userAgent = Util.getUserAgent(context, result.getUserAgent());
            HttpDataSource.Factory  httpDataSourceFactory= new DefaultHttpDataSource.Factory()
                    .setUserAgent(userAgent)
                    .setAllowCrossProtocolRedirects(true)
                    .setConnectTimeoutMs(CONNECT_TIMEOUT)
                    .setReadTimeoutMs(READ_TIMEOUT);

            //MediaSource source = new ProgressiveMediaSource.Factory(httpDataSourceFactory).createMediaSource(MediaItem.fromUri(uri));

            HlsMediaSource hlsMediaSource =
                    new HlsMediaSource.Factory(httpDataSourceFactory)
                            .createMediaSource(MediaItem.fromUri(uri));

            player.postValue(internalPlayer);
            internalPlayer.setMediaSource(hlsMediaSource);
            internalPlayer.prepare();

            timeline = new PlayerTimeline(result.getEpgCurrentTime());
            internalPlayer.addListener(timeline);
            internalPlayer.setVolume(prefService.get(PreferencesService.KEYS.PLAYER_VOLUME, 1f));

            timeline.start();
            internalPlayer.setPlayWhenReady(true);

            Log.d(Tag.UI, "PlayerViewModel.postProcessAfterInitializationUrl()[end]");
        }
    }
}
