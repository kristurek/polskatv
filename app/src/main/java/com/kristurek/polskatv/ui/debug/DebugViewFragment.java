package com.kristurek.polskatv.ui.debug;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.DebugViewFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
@UnstableApi
public class DebugViewFragment extends AbstractFragment {

    private DebugViewModel viewModel;
    private TextView content;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = obtainViewModel();

        DebugViewFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.debug_view_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        content = binding.debugViewFragmentContent;

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getContent().observe(getViewLifecycleOwner(), (item) -> content.setText(item));

        viewModel.initializeDebugView();
    }

    @NonNull
    public DebugViewModel obtainViewModel() {
        return new ViewModelProvider(this).get(DebugViewModel.class);
    }
}
