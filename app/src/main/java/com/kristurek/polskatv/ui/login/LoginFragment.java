package com.kristurek.polskatv.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.kristurek.polskatv.PolskaTvApplication;
import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.LoginFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;
import com.kristurek.polskatv.ui.login.adapter.ProviderAdapter;
import com.kristurek.polskatv.ui.main.MainActivity;

public class LoginFragment extends AbstractFragment {

    private LoginViewModel viewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        PolskaTvApplication.getComponent().inject(this);

        viewModel = obtainViewModel();

        LoginFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.getViewModel().getSubscription().observe(this, r -> viewModel.validateForm());
        binding.getViewModel().getPassword().observe(this, r -> viewModel.validateForm());
        binding.getViewModel().getProviderId().observe(this, r -> viewModel.validateForm());
        binding.getViewModel().getSuccessNotifier().observe(this, r -> startMainActivity());
        binding.getViewModel().getExceptionNotifier().observe(this, this::handleException);
        binding.getViewModel().getMessageNotifier().observe(this, this::handleMessage);

        Spinner providerSpinner = binding.getRoot().findViewById(R.id.login_input_provider);
        providerSpinner.setAdapter(new ProviderAdapter(getContext()));

        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    @NonNull
    public LoginViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance();

        return ViewModelProviders.of(getActivity(), factory).get(LoginViewModel.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_upload_logs) {
            viewModel.generateAndUploadLogs();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

