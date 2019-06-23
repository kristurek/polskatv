package com.kristurek.polskatv.ui.volume;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.VolumeFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;
import com.kristurek.polskatv.ui.view.XVerticalSeekBar;
import com.kristurek.polskatv.util.Focus;
import com.kristurek.polskatv.util.FontHelper;
import com.kristurek.polskatv.util.Tag;

public class VolumeFragment extends AbstractFragment implements View.OnKeyListener, SeekBar.OnSeekBarChangeListener {

    private VolumeViewModel viewModel;

    private XVerticalSeekBar volumeSeekbar;
    private TextView volumeValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = obtainViewModel();
        viewModel.initializeEventBus(this);

        VolumeFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.volume_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        volumeSeekbar = binding.volumeFragmentSeekbar;
        volumeValue = binding.volumeFragmentValue;

        volumeSeekbar.setOnSeekBarChangeListener(this);
        volumeSeekbar.setOnKeyListener(this);
        volumeSeekbar.setMax(100);

        FontHelper.setFont(getActivity(), volumeValue, FontHelper.Header.H_1);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getExceptionNotifier().observe(this, this::handleException);
        viewModel.getMessageNotifier().observe(this, this::handleMessage);

        viewModel.getProgress().observe(this, progress -> {
            volumeSeekbar.setProgress(progress);
            volumeValue.setText(String.valueOf(progress));
        });
    }

    @NonNull
    public VolumeViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance();

        return ViewModelProviders.of(getActivity(), factory).get(VolumeViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(Tag.UI, "VolumeFragment.onResume()");

        viewModel.initialize();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(Tag.UI, "VolumeFragment.onPause()");

        viewModel.persistCurrentVolume();
    }

    //==============================================================================================

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(Tag.UI, "VolumeFragment.updateProgress()[" + progress + "]");

        viewModel.changeProgress(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            viewModel.volumeUp();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            viewModel.volumeDown();
            return true;
        } else
            return false;
    }

    public void requestFocus(Focus focus) {
        if (focus.equals(Focus.VOLUME))
            volumeSeekbar.requestFocus();
    }

    public boolean hasFocus() {
        return volumeSeekbar.hasFocus();
    }
}
