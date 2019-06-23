package com.kristurek.polskatv.ui.arch;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewConfiguration;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.ui.event.DumyEvent;
import com.kristurek.polskatv.ui.forceclose.handler.ExceptionHandler;
import com.kristurek.polskatv.util.Tag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class AbstractActivity extends AppCompatActivity implements AbstractViewModel.EventBus{

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
        Log.d(Tag.EVENT, "AbstractFragment.post()[" + event + "]");
        EventBus.getDefault().post(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        loadTheme();
    }

    private void loadTheme() {
        Log.d(Tag.UI, "AbstractActivity.loadTheme(hasPermanentMenuKey)[" + ViewConfiguration.get(this).hasPermanentMenuKey() + "]");

        setTheme(ViewConfiguration.get(this).hasPermanentMenuKey() ? R.style.PolskaTv_Theme_Fullscreen_NoActionBar : R.style.PolskaTv_Theme_Fullscreen_ActionBar);
    }

    protected void handleException(Throwable th) {
        Log.d(Tag.UI, th.getMessage(), th);

        runOnUiThread(() -> Toast.makeText(this, th.getMessage(), Toast.LENGTH_LONG).show());
    }

    protected void handleMessage(String message) {
        Log.d(Tag.UI, message);

        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }
}
