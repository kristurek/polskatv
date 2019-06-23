package com.kristurek.polskatv.ui.forceclose;

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
import com.kristurek.polskatv.databinding.ForceCloseFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;

public class ForceCloseFragment extends AbstractFragment {

    private ForceCloseViewModel viewModel;
    private TextView error;

    public static ForceCloseFragment newInstance() {
        return new ForceCloseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = obtainViewModel();

        ForceCloseFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.force_close_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        error = binding.forceCloseError;

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getExceptionNotifier().observe(this, this::handleException);
        viewModel.getMessageNotifier().observe(this, this::handleMessage);

        viewModel.getError().observe(this, errorMsg -> error.setText(errorMsg));

        viewModel.initialize(getActivity().getIntent().getStringExtra("error"));
    }

    @NonNull
    public ForceCloseViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance();

        return ViewModelProviders.of(getActivity(), factory).get(ForceCloseViewModel.class);
    }
}