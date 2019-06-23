package com.kristurek.polskatv.ui.epg;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.EpgFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;
import com.kristurek.polskatv.ui.epgs.model.EpgType;
import com.kristurek.polskatv.ui.event.EpgCurrentTimeEvent;
import com.kristurek.polskatv.ui.event.SelectedEpgEvent;
import com.kristurek.polskatv.ui.event.StopPlayerEvent;
import com.kristurek.polskatv.util.FontHelper;
import com.kristurek.polskatv.util.Tag;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EpgFragment extends AbstractFragment {

    private EpgViewModel viewModel;

    private ImageView typeImage;
    private TextView dateTime;
    private TextView name;
    private TextView title;
    private TextView type;
    private TextView currentTime;
    private TextView totalTime;
    private SeekBar seekBar;
    private LinearLayout seekBarTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = obtainViewModel();
        viewModel.initializeEventBus(this);

        EpgFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.epg_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        name = binding.epgFragmentName;
        title = binding.epgFragmentTitle;
        type = binding.epgFragmentType;
        typeImage = binding.epgFragmentTypeImage;
        dateTime = binding.epgFragmentDateTime;
        currentTime = binding.epgFragmentCurrentTime;
        totalTime = binding.epgFragmentTotalTime;
        seekBar = binding.epgFragmentSeekbar;
        seekBarTime = binding.epgFragmentSeekbarTime;

        seekBar.setOnTouchListener((v, event) -> true);

        FontHelper.setFont(getActivity(), dateTime, FontHelper.Header.H_3);
        FontHelper.setFont(getActivity(), name, FontHelper.Header.H_1);
        FontHelper.setFont(getActivity(), title, FontHelper.Header.H_1);
        FontHelper.setFont(getActivity(), type, FontHelper.Header.H_3);

        FontHelper.setFont(getActivity(), currentTime, FontHelper.Header.H_2);
        FontHelper.setFont(getActivity(), totalTime, FontHelper.Header.H_2);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getExceptionNotifier().observe(this, this::handleException);
        viewModel.getMessageNotifier().observe(this, this::handleMessage);

        viewModel.getName().observe(this, param -> name.setText(param));
        viewModel.getTitle().observe(this, param -> title.setText(param));
        viewModel.getDateTime().observe(this, param -> dateTime.setText(param));
        viewModel.getType().observe(this, param -> {
            if (param.equals(EpgType.ARCHIVE_EPG)) {
                type.setText("ARCHIVE");
                typeImage.setImageResource(R.drawable.ic_play);
                seekBarTime.setVisibility(View.VISIBLE);
            } else if (param.equals(EpgType.LIVE_EPG)) {
                type.setText("LIVE");
                typeImage.setImageResource(R.drawable.ic_live);
                seekBarTime.setVisibility(View.GONE);
            }
        });
        viewModel.getCurrentSecond().observe(this, param -> seekBar.setProgress(param));
        viewModel.getTotalSecond().observe(this, param -> seekBar.setMax(param));
        viewModel.getCurrentTime().observe(this, param -> currentTime.setText(param));
        viewModel.getTotalTime().observe(this, param -> totalTime.setText(param));
    }

    @NonNull
    public EpgViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance();

        return ViewModelProviders.of(getActivity(), factory).get(EpgViewModel.class);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(SelectedEpgEvent event) {
        Log.d(Tag.EVENT, "EpgFragment.receive()[" + event + "]");
        viewModel.initializeEpg(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(EpgCurrentTimeEvent event) {
        Log.d(Tag.MASSIVE, "EpgFragment.receive()[" + event + "]");
        viewModel.updateProgress(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(StopPlayerEvent event) {
        Log.d(Tag.EVENT, "EpgFragment.receive()[" + event + "]");
        viewModel.clearProgress();
    }
}
