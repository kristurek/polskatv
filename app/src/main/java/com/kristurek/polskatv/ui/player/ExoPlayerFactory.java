package com.kristurek.polskatv.ui.player;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.media3.exoplayer.ExoPlayer;

public class ExoPlayerFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ExoPlayer player;

    public static ExoPlayer createInstance(Context context) {
        if (player == null) {
            synchronized (ExoPlayer.class) {
                if (player == null) {
                    player = new ExoPlayer.Builder(context).build();
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
