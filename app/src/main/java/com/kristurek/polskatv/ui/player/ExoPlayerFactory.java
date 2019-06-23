package com.kristurek.polskatv.ui.player;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.TrackSelector;

public class ExoPlayerFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile SimpleExoPlayer player;

    public static SimpleExoPlayer createInstance(Context context, TrackSelector trackSelector) {
        if (player == null) {
            synchronized (SimpleExoPlayer.class) {
                if (player == null) {
                    player = com.google.android.exoplayer2.ExoPlayerFactory.newSimpleInstance(context, trackSelector);
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
