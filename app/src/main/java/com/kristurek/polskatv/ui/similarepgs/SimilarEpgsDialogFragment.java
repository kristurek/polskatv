package com.kristurek.polskatv.ui.similarepgs;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.SimilarEpgsDialogFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractDialogFragment;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;
import com.kristurek.polskatv.ui.similarepgs.adapter.SimilarEpgsAdapter;
import com.kristurek.polskatv.ui.view.XListView;
import com.kristurek.polskatv.util.FontHelper;
import com.kristurek.polskatv.util.Tag;

public class SimilarEpgsDialogFragment extends AbstractDialogFragment implements AdapterView.OnItemClickListener {

    private SimilarEpgsViewModel viewModel;
    private SimilarEpgsAdapter adapter;
    private XListView list;

    private RelativeLayout loadingView;
    private RelativeLayout noResultView;

    public static SimilarEpgsDialogFragment newInstanceManyChannels(String title) {
        SimilarEpgsDialogFragment frag = new SimilarEpgsDialogFragment();
        Bundle args = new Bundle();

        args.putSerializable("many_channels", true);
        args.putSerializable("title", title);

        frag.setArguments(args);
        return frag;
    }

    public static SimilarEpgsDialogFragment newInstanceOneChannel(String title, Integer id, String name) {
        SimilarEpgsDialogFragment frag = new SimilarEpgsDialogFragment();
        Bundle args = new Bundle();

        args.putSerializable("many_channels", false);
        args.putSerializable("title", title);
        args.putSerializable("id", id);
        args.putSerializable("name", name);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        adapter = new SimilarEpgsAdapter(getActivity());

        viewModel = obtainViewModel();
        viewModel.initializeEmpty();
        viewModel.initializeEventBus(this);

        SimilarEpgsDialogFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.similar_epgs_dialog_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        list = binding.similarEpgsList;
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        list.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
                list.smoothScrollToPosition(list.getSelectedItemPosition());
            return false;
        });

        loadingView = binding.similarEpgsLoading;
        noResultView = binding.similarEpgsNoResults;

        FontHelper.setFont(getActivity(), binding.similarEpgsHeader, FontHelper.Header.H_1);
        FontHelper.setFont(getActivity(), binding.similarEpgsNoResultsText, FontHelper.Header.H_1);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getExceptionNotifier().observe(this, this::handleException);
        viewModel.getMessageNotifier().observe(this, this::handleMessage);

        viewModel.getEpgs().observe(this, (list) -> adapter.setList(list));
        viewModel.getFocusedEpg().observe(this, model -> {
            Log.d(Tag.UI, "SimilarEpgsFragment.getFocusEpg().observe()[" + model + "]");
            list.setSelection(adapter.getPosition(model));
            list.requestFocus();
        });
        viewModel.getNoResults().observe(this, noResult -> {
            Log.d(Tag.UI, "SimilarEpgsFragment.getNoResults().observe()[" + noResult + "]");
            if (noResult)
                noResultView.setVisibility(View.VISIBLE);
            else
                noResultView.setVisibility(View.GONE);
        });
        viewModel.getLoading().observe(this, loading -> {
            Log.d(Tag.UI, "SimilarEpgsFragment.getLoading().observe()[" + loading + "]");
            if (loading)
                loadingView.setVisibility(View.VISIBLE);
            else
                loadingView.setVisibility(View.GONE);
        });

        String title = (String) getArguments().getSerializable("title");
        String name = (String) getArguments().getSerializable("name");
        Integer id = (Integer) getArguments().getSerializable("id");
        Boolean manyChannels = (Boolean) getArguments().getSerializable("many_channels");

        loadingView.setVisibility(View.VISIBLE);
        viewModel.initialize(id, title, manyChannels, name);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @NonNull
    public SimilarEpgsViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance();

        return ViewModelProviders.of(getActivity(), factory).get(SimilarEpgsViewModel.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        viewModel.selectEpg(adapter.getItem(position));
        dismiss();
    }
}