package com.kristurek.polskatv.ui.forceclose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.kristurek.polskatv.PolskaTvApplication;
import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.ForceCloseFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;
import com.kristurek.polskatv.ui.arch.ViewModelProviderFactory;

import javax.inject.Inject;

public class ForceCloseFragment extends AbstractFragment {

    @Inject
    public ViewModelProviderFactory factory;

    private ForceCloseViewModel viewModel;
    private TextView error;

    public static ForceCloseFragment newInstance() {
        return new ForceCloseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        PolskaTvApplication.getComponent().inject(this);

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

        viewModel.getExceptionNotifier().observe(getViewLifecycleOwner(), this::handleException);
        viewModel.getMessageNotifier().observe(getViewLifecycleOwner(), this::handleMessage);

        viewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> error.setText(errorMsg));

        viewModel.initialize(getActivity().getIntent().getStringExtra("error"));
    }

    @NonNull
    public ForceCloseViewModel obtainViewModel() {
        return new ViewModelProvider(getActivity(), factory).get(ForceCloseViewModel.class);
    }
}