package com.kristurek.polskatv.ui.player;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.exoplayer2.ui.PlayerView;
import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.PlayerFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;
import com.kristurek.polskatv.ui.event.PausePlayerEvent;
import com.kristurek.polskatv.ui.event.QuietPausePlayerEvent;
import com.kristurek.polskatv.ui.event.ResumePlayerEvent;
import com.kristurek.polskatv.ui.event.SeekToTimeEvent;
import com.kristurek.polskatv.ui.event.StopPlayerEvent;
import com.kristurek.polskatv.ui.event.VolumePlayerEvent;
import com.kristurek.polskatv.util.Tag;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PlayerFragment extends AbstractFragment {

    private PlayerViewModel viewModel;
    private PlayerView playerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = obtainViewModel();
        viewModel.initializeEventBus(this);

        PlayerFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.player_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        playerView = binding.playerFragmentExoPlayerView;

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getExceptionNotifier().observe(this, this::handleException);
        viewModel.getMessageNotifier().observe(this, this::handleMessage);

        viewModel.getPlayer().observe(this, player -> playerView.setPlayer(player));
    }

    @NonNull
    public PlayerViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance();

        return ViewModelProviders.of(getActivity(), factory).get(PlayerViewModel.class);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
    public void receive(SeekToTimeEvent event) {
        Log.d(Tag.EVENT, "PlayerFragment.receive()[" + event + "]");
        viewModel.startPlayer(event);
    }

//    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
//    public void receive(SelectedEpgEvent event) {
//        Log.d(Tag.EVENT, "PlayerFragment.receive()[" + event + "]");
//        viewModel.startPlayer(event);
//    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(StopPlayerEvent event) {
        Log.d(Tag.EVENT, "PlayerFragment.receive()[" + event + "]");
        viewModel.stopPlayer(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(PausePlayerEvent event) {
        Log.d(Tag.EVENT, "PlayerFragment.receive()[" + event + "]");
        viewModel.pausePlayer(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(QuietPausePlayerEvent event) {
        Log.d(Tag.EVENT, "PlayerFragment.receive()[" + event + "]");
        viewModel.quietPausePlayer(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(ResumePlayerEvent event) {
        Log.d(Tag.EVENT, "PlayerFragment.receive()[" + event + "]");
        viewModel.resumePlayer(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(VolumePlayerEvent event) {
        Log.d(Tag.EVENT, "PlayerFragment.receive()[" + event + "]");
        viewModel.changeVolume(event);
    }
}