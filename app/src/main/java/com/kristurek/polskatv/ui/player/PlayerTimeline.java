package com.kristurek.polskatv.ui.player;

import android.os.Handler;
import android.util.Log;

import com.google.android.exoplayer2.Player;
import com.kristurek.polskatv.util.Tag;

import java.util.concurrent.TimeUnit;

public class PlayerTimeline implements Player.Listener {

    private volatile long actualProgressLastFrame = 0L;
    private volatile long actualProgressTotal = 0L;
    private volatile boolean count = false;
    private volatile long startCount = 0L;

    private Integer period = 250;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (count)
                actualProgressLastFrame = TimeUnit.MILLISECONDS.toSeconds(TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - startCount);

            handler.postDelayed(this, period);
        }
    };

    public PlayerTimeline(long epgCurrentTime) {
        this.actualProgressTotal = epgCurrentTime;
    }

    public void start() {
        handler.postDelayed(runnable, period);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }

    public long getEpgCurrentTime() {
        Log.d(Tag.MASSIVE, "PlayerTimeline.getEpgCurrentTime()[" + (actualProgressTotal + actualProgressLastFrame) + "]");
        return actualProgressTotal + actualProgressLastFrame;
    }

    public boolean isActiveCounter() {
        return count;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playWhenReady && playbackState == Player.STATE_READY) {
            startCount = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
            actualProgressLastFrame = 0L;
            count = true;
        } else {
            count = false;
            actualProgressTotal = actualProgressTotal + actualProgressLastFrame;
            actualProgressLastFrame = 0L;
        }
    }
}
