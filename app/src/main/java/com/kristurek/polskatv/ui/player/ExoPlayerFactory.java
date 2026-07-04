package com.kristurek.polskatv.ui.player;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;

import com.kristurek.polskatv.service.PreferencesService;

@UnstableApi
public class ExoPlayerFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ExoPlayer player;

    public static ExoPlayer createInstance(Context context, PreferencesService prefService) {
        if (player == null) {
            synchronized (ExoPlayer.class) {
                if (player == null) {
                    int buffer = prefService.get(PreferencesService.KEYS.PLAYER_BUFFER, 5) * 1000;
                    DefaultLoadControl loadControl = new DefaultLoadControl.Builder()
                            .setBufferDurationsMs(
                                    buffer, /* minBufferMs */
                                    Math.max(buffer * 4, 60000), /* maxBufferMs */
                                    Math.min(buffer / 2, 2500), /* bufferForPlaybackMs */
                                    Math.min(buffer, 5000)  /* bufferForPlaybackAfterRebufferMs */
                            )
                            .build();

                    player = new ExoPlayer.Builder(context)
                            .setLoadControl(loadControl)
                            .build();
                }
            }
        }
        return player;
    }

    public static ExoPlayer getInstance() {
        return player;
    }

    public static void destroyInstance() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop();
            player.release();

            player = null;
        }
    }
}
