package com.kristurek.polskatv.ui.channels;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.ChannelsFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;
import com.kristurek.polskatv.ui.channels.adapter.ChannelsAdapter;
import com.kristurek.polskatv.ui.event.InitializeChannelsEvent;
import com.kristurek.polskatv.ui.event.RecreateAppEvent;
import com.kristurek.polskatv.ui.view.XListView;
import com.kristurek.polskatv.util.Focus;
import com.kristurek.polskatv.util.FontHelper;
import com.kristurek.polskatv.util.Tag;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChannelsFragment extends AbstractFragment implements AdapterView.OnItemClickListener {

    private ChannelsViewModel viewModel;
    private ChannelsAdapter adapter;
    private XListView list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        adapter = new ChannelsAdapter(getActivity());

        viewModel = obtainViewModel();
        viewModel.initializeEventBus(this);

        ChannelsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.channels_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        list = binding.channelsFragmentList;
        list.setRestoreSelectionType(XListView.SELECTION_TYPE.CHECK);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        list.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
                list.smoothScrollToPosition(list.getSelectedItemPosition());
            return false;
        });

        FontHelper.setFont(getActivity(), binding.channelsFragmentTitle, FontHelper.Header.H_1);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getExceptionNotifier().observe(this, this::handleException);
        viewModel.getMessageNotifier().observe(this, this::handleMessage);

        viewModel.getChannels().observe(this, (items) -> adapter.setList(items));
        viewModel.getSelectedChannel().observe(this, (item) -> {
            list.setItemChecked(adapter.getPosition(item), true);
            list.requestFocus();
        });
        viewModel.getFocusedChannel().observe(this, (item) -> {
            list.setSelection(adapter.getPosition(item));
            list.requestFocus();
        });
        viewModel.getNeedRefresh().observe(this, (refresh) -> {
            if (refresh)
                adapter.notifyDataSetChanged();
        });

        viewModel.startUpdateProcess();
    }

    @NonNull
    public ChannelsViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance();

        return ViewModelProviders.of(getActivity(), factory).get(ChannelsViewModel.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        viewModel.selectChannel(adapter.getItem(position));
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(InitializeChannelsEvent event) {
        Log.d(Tag.EVENT, "ChannelsFragment.receive()[" + event + "]");
        viewModel.initializeChannels();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(RecreateAppEvent event) {
        Log.d(Tag.EVENT, "ChannelsFragment.receive()[" + event + "]");
        viewModel.recreateChannels(event);
    }

    public void requestFocus(Focus focus) {
        if (focus.equals(Focus.CHANNELS))
            list.requestFocus();
    }

    public boolean hasFocus() {
        return list.hasFocus();
    }
}