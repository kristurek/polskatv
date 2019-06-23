package com.kristurek.polskatv.ui.arch;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.kristurek.polskatv.ui.event.DumyEvent;
import com.kristurek.polskatv.util.Tag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class AbstractDialogFragment extends DialogFragment implements AbstractViewModel.EventBus {

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receive(DumyEvent event) {
        Log.d(Tag.UI, String.valueOf(event));
    }

    public void post(Event event) {
        Log.d(Tag.EVENT, "AbstractDialogFragment.post()[" + event + "]");
        EventBus.getDefault().post(event);
    }

    protected void handleException(Throwable th) {
        Log.d(Tag.UI, th.getMessage(), th);

        if (getActivity() != null)
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), th.getMessage(), Toast.LENGTH_LONG).show());
    }

    protected void handleMessage(int resId) {
        Log.d(Tag.UI, getString(resId));

        if (getActivity() != null)
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), getString(resId), Toast.LENGTH_LONG).show());
    }
}
