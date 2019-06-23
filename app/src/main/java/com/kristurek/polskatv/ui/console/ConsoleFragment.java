package com.kristurek.polskatv.ui.console;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.ConsoleFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;
import com.kristurek.polskatv.ui.event.EpgCurrentTimeEvent;
import com.kristurek.polskatv.ui.event.SelectedEpgEvent;
import com.kristurek.polskatv.ui.event.StopPlayerEvent;
import com.kristurek.polskatv.util.Tag;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

public class ConsoleFragment extends AbstractFragment {

    private ConsoleViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = obtainViewModel();
        viewModel.initializeEventBus(this);

        ConsoleFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.console_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getExceptionNotifier().observe(this, this::handleException);
        viewModel.getMessageNotifier().observe(this, this::handleMessage);
    }

    @NonNull
    public ConsoleViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance();

        return ViewModelProviders.of(getActivity(), factory).get(ConsoleViewModel.class);
    }

    public void onForwardClick() {
        Log.d(Tag.UI, "ConsoleFragmnet.onForwardClick()");

        viewModel.selectForward();
    }

    public void onBackwardClick() {
        Log.d(Tag.UI, "ConsoleFragmnet.onBackwardClick()");

        viewModel.selectBackward();
    }

    public void onFastForwardClick() {
        Log.d(Tag.UI, "ConsoleFragmnet.onFastForwardClick()");

        viewModel.selectFastForward();
    }

    public void onFastBackwardClick() {
        Log.d(Tag.UI, "ConsoleFragmnet.onFastBackwardClick()");

        viewModel.selectFastBackward();
    }

    public void onPlayPauseClick() {
        Log.d(Tag.UI, "ConsoleFragmnet.onPlayPauseClick()");

        viewModel.selectPlayPause();
    }

    public Serializable onSettings() {
        return viewModel.openSettings();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(SelectedEpgEvent event) {
        Log.d(Tag.EVENT, "ConsoleFragment.receive()[" + event + "]");

        viewModel.selectEpg(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(EpgCurrentTimeEvent event) {
        Log.d(Tag.MASSIVE, "ConsoleFragment.receive()[" + event + "]");
        viewModel.updateProgress(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(StopPlayerEvent event) {
        Log.d(Tag.EVENT, "ConsoleFragment.receive()[" + event + "]");

        viewModel.stopPlayer(event);
    }
}
