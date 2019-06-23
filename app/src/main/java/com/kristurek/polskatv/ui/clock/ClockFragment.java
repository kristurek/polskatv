package com.kristurek.polskatv.ui.clock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.ClockFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;

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

        viewModel.getTime().observe(this, (item) -> clock.setText(item));

        viewModel.initializeClock();
    }

    @NonNull
    public ClockViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance();

        return ViewModelProviders.of(getActivity(), factory).get(ClockViewModel.class);
    }
}
