package com.kristurek.polskatv.ui.epgs;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.EpgsFragmentBinding;
import com.kristurek.polskatv.ui.arch.AbstractFragment;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;
import com.kristurek.polskatv.ui.epgs.adapter.EpgsAdapter;
import com.kristurek.polskatv.ui.epgs.model.EpgModel;
import com.kristurek.polskatv.ui.event.FindCurrentEpgEvent;
import com.kristurek.polskatv.ui.event.RecreateAppEvent;
import com.kristurek.polskatv.ui.event.SelectedChannelEvent;
import com.kristurek.polskatv.ui.menuepgs.MenuEpgsDialogFragment;
import com.kristurek.polskatv.ui.view.XListView;
import com.kristurek.polskatv.ui.view.XTabLayout;
import com.kristurek.polskatv.util.DateTimeHelper;
import com.kristurek.polskatv.util.Focus;
import com.kristurek.polskatv.util.FontHelper;
import com.kristurek.polskatv.util.Tag;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EpgsFragment extends AbstractFragment implements XTabLayout.OnTabSelectedListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private EpgsViewModel viewModel;
    private EpgsAdapter adapter;
    private XListView epgsList;
    private TextView selectedDayText;
    private XTabLayout daysTabs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        adapter = new EpgsAdapter(getActivity());

        viewModel = obtainViewModel();
        viewModel.initializeEventBus(this);

        EpgsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.epgs_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        selectedDayText = binding.epgsFragmentSelectedDay;

        daysTabs = binding.epgsFragmentTabs;
        daysTabs.setTabMode(XTabLayout.MODE_SCROLLABLE);
        daysTabs.addOnTabSelectedListener(this);

        epgsList = binding.epgsFragmentList;
        epgsList.setRestoreSelectionType(XListView.SELECTION_TYPE.SELECT);
        epgsList.setAdapter(adapter);
        epgsList.addFooterView(inflater.inflate(R.layout.epgs_footer, epgsList, false), null, true);
        epgsList.setOnItemClickListener(this);
        epgsList.setOnItemLongClickListener(this);
        epgsList.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
                epgsList.smoothScrollToPosition(epgsList.getSelectedItemPosition());
            return false;
        });

        FontHelper.setFont(getActivity(), selectedDayText, FontHelper.Header.H_1);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getExceptionNotifier().observe(this, this::handleException);
        viewModel.getMessageNotifier().observe(this, this::handleMessage);

        viewModel.getEpgs().observe(this, (list) -> adapter.setList(list));
        viewModel.getDays().observe(this, (days) -> daysTabs.addTabs(days));
        viewModel.getSelectedDay().observe(this, (day) -> {
            Log.d(Tag.UI, "EpgsFragment.getSelectedDay().observe()[" + day + "]");
            daysTabs.selectWithoutTriggerListeners(day);
            selectedDayText.setText(day.toString(DateTimeHelper.EEEddMMyyyy));
        });
        viewModel.getFocusedEpg().observe(this, model -> {
            Log.d(Tag.UI, "EpgsFragment.getFocusEpg().observe()[" + model + "]");
            epgsList.setSelection(adapter.getPosition(model));
            epgsList.requestFocus();

        });
        viewModel.getSelectedEpg().observe(this, model -> {
            Log.d(Tag.UI, "EpgsFragment.getSelectedEpg().observe()[" + model + "]");
            epgsList.setItemChecked(adapter.getPosition(model), true);
            epgsList.requestFocus();
        });
        viewModel.getNeedRefresh().observe(this, (refresh) -> {
            if (refresh)
                adapter.notifyDataSetChanged();
        });

        viewModel.startUpdateProcess();
    }

    @NonNull
    public EpgsViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance();

        return ViewModelProviders.of(getActivity(), factory).get(EpgsViewModel.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(Tag.UI, "EpgsFragment.onItemClick()[" + position + "]");

        if (position == adapter.getCount())
            viewModel.selectNextDay();
        else
            viewModel.selectEpg(adapter.getItem(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != adapter.getCount()) {
            EpgModel item = adapter.getItem(position);

            MenuEpgsDialogFragment dialog = MenuEpgsDialogFragment.newInstance(item.getChannelId(), item.getChannelName(), item.getDescription(), item.getTitle());
            dialog.show(getFragmentManager(), "MenuEpgsDialogFragment");
            return true;
        }

        return false;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d(Tag.UI, "EpgsFragment.onTabSelected()[" + tab.getPosition() + ", " + tab.getText() + "]");

        viewModel.selectDay(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(SelectedChannelEvent event) {
        Log.d(Tag.EVENT, "EpgsFragment.receive()[" + event + "]");
        viewModel.initializeEpgs(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(FindCurrentEpgEvent event) {
        Log.d(Tag.EVENT, "EpgsFragment.receive()[" + event + "]");
        viewModel.resolveCurrentEpg(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void receive(RecreateAppEvent event) {
        Log.d(Tag.EVENT, "EpgsFragment.receive()[" + event + "]");
        viewModel.recreateEpgs(event);
    }

    public void requestFocus(Focus focus) {
        if (focus.equals(Focus.EPGS))
            epgsList.requestFocus();
        if (focus.equals(Focus.DAYS))
            daysTabs.requestFocus();
    }

    public boolean hasFocus(Focus focus) {
        if (focus.equals(Focus.EPGS))
            return epgsList.hasFocus();
        else if (focus.equals(Focus.DAYS))
            return daysTabs.hasFocus();
        else
            return false;
    }
}

