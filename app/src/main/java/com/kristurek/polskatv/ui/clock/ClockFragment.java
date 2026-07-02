package com.kristurek.polskatv.ui.clock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.ClockFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ClockFragment extends AbstractFragment {

    private ClockViewModel viewModel;
    private TextView clock;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = obtainViewModel();

        ClockFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.clock_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        clock = binding.clockFragmentClock;

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getTime().observe(getViewLifecycleOwner(), (item) -> clock.setText(item));

        viewModel.initializeClock();
    }

    @NonNull
    public ClockViewModel obtainViewModel() {
        return new ViewModelProvider(this).get(ClockViewModel.class);
    }
}
