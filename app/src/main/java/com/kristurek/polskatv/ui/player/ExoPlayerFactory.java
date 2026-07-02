package com.kristurek.polskatv.ui.player;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.trackselection.TrackSelector;

@UnstableApi
public class ExoPlayerFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ExoPlayer player;

    public static ExoPlayer createInstance(Context context, TrackSelector trackSelector) {
        if (player == null) {
            synchronized (ExoPlayer.class) {
                if (player == null) {
                    player = new ExoPlayer.Builder(context).setTrackSelector(trackSelector).build();
                }
            }
        }
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
