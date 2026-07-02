package com.kristurek.polskatv.ui.debug;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import androidx.lifecycle.MutableLiveData;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.common.util.UnstableApi;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.AbstractViewModel;
import com.kristurek.polskatv.ui.player.ExoPlayerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
@UnstableApi
public class DebugViewModel extends AbstractViewModel {

    private MutableLiveData<String> content = new MutableLiveData<>();
    private MutableLiveData<Boolean> visibility = new MutableLiveData<>();
    private Timer timer;

    private PreferencesService prefService;
    private final int pid = Process.myPid();
    
    private String lastAudioLog = "";
    private String lastVideoLog = "";
    
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public MutableLiveData<String> getContent() {
        return content;
    }

    public MutableLiveData<Boolean> getVisibility() {
        return visibility;
    }

    @Inject
    public DebugViewModel(PreferencesService prefService) {
        this.prefService = prefService;

        visibility.setValue(false);
        content.setValue("");
    }

    public void initializeDebugView() {
        visibility.postValue(prefService.get(PreferencesService.KEYS.APPLICATION_DEBUG_VIEW, false));
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainHandler.post(() -> {
                    ExoPlayer player = ExoPlayerFactory.getInstance();
                    if (player == null || player.getPlaybackState() == ExoPlayer.STATE_IDLE) {
                        content.postValue("");
                        lastAudioLog = "";
                        lastVideoLog = "";
                    } else {
                        // Trigger log reading in background
                        new Thread(() -> {
                            readLogs();
                            StringBuilder sb = new StringBuilder();
                            if (!lastAudioLog.isEmpty()) sb.append(lastAudioLog).append("\n");
                            if (!lastVideoLog.isEmpty()) sb.append(lastVideoLog);
                            content.postValue(sb.toString());
                        }).start();
                    }
                });
            }
        }, 0, 1000);
    }

    private void readLogs() {
        try {
            java.lang.Process process = Runtime.getRuntime().exec("logcat -d -t 100 MediaCodecLogger:I *:S");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String searchPattern = pid + ".";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(searchPattern) && line.contains("bitrateInKbps")) {
                    int index = line.indexOf(searchPattern);
                    if (index != -1) {
                        String logPart = line.substring(index).trim();
                        if (logPart.contains(".audio.")) {
                            lastAudioLog = logPart;
                        } else if (logPart.contains(".video.")) {
                            lastVideoLog = logPart;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (timer != null)
            timer.cancel();
    }
}
