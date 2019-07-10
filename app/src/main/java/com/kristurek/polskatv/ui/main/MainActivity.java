package com.kristurek.polskatv.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.databinding.MainActivityBinding;
import com.kristurek.polskatv.ui.arch.AbstractActivity;
import com.kristurek.polskatv.ui.arch.ViewModelFactory;
import com.kristurek.polskatv.ui.channels.ChannelsFragment;
import com.kristurek.polskatv.ui.console.ConsoleFragment;
import com.kristurek.polskatv.ui.epgs.EpgsFragment;
import com.kristurek.polskatv.ui.event.InitializeChannelsEvent;
import com.kristurek.polskatv.ui.event.RecreateAppEvent;
import com.kristurek.polskatv.ui.login.LoginActivity;
import com.kristurek.polskatv.ui.settings.SettingsActivity;
import com.kristurek.polskatv.ui.volume.VolumeFragment;
import com.kristurek.polskatv.util.Focus;
import com.kristurek.polskatv.util.Tag;

public class MainActivity extends AbstractActivity {

    public static final int ID = 6789;

    private MainViewModel viewModel;

    private ConsoleFragment consoleFragment;
    private EpgsFragment epgsFragment;
    private ChannelsFragment channelsFragment;
    private VolumeFragment volumeFragment;

    private Focus savedFocus = Focus.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = obtainViewModel();
        viewModel.initializeEventBus(this);

        MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        consoleFragment = (ConsoleFragment) getSupportFragmentManager().findFragmentById(R.id.console_fragment);
        epgsFragment = (EpgsFragment) getSupportFragmentManager().findFragmentById(R.id.epgs_fragment);
        channelsFragment = (ChannelsFragment) getSupportFragmentManager().findFragmentById(R.id.channels_fragment);
        volumeFragment = (VolumeFragment) getSupportFragmentManager().findFragmentById(R.id.volume_fragment);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initializeFragments();
    }

    private void initializeFragments() {
        Log.d(Tag.UI, "MainActivity.restoreFragments()");

        RecreateAppEvent event = null;

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object object = bundle.getSerializable("RecreateApp");
                if (object instanceof RecreateAppEvent) {
                    event = (RecreateAppEvent) object;
                    intent.removeExtra("RecreateApp");
                }
            }
        }

        if (event != null)
            post(event);
        else
            post(new InitializeChannelsEvent());
    }

    @NonNull
    public MainViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance();

        return ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (viewModel.onTouchEvent())
            return true;
        else
            return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (viewModel.onTouchEvent()) {
                    if (savedFocus.equals(Focus.CHANNELS))
                        channelsFragment.requestFocus(savedFocus);
                    else if (savedFocus.equals(Focus.EPGS))
                        epgsFragment.requestFocus(savedFocus);
                    else if (savedFocus.equals(Focus.DAYS))
                        epgsFragment.requestFocus(savedFocus);
                    else if (savedFocus.equals(Focus.VOLUME))
                        volumeFragment.requestFocus(savedFocus);

                    return true;
                }
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                consoleFragment.onPlayPauseClick();
                return true;
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                event.startTracking();
                return true;
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                event.startTracking();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((event.getFlags() & KeyEvent.FLAG_CANCELED_LONG_PRESS) == 0) {
            if (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                consoleFragment.onBackwardClick();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
                consoleFragment.onForwardClick();
                return true;
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
            consoleFragment.onFastBackwardClick();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            consoleFragment.onFastForwardClick();
            return true;
        }

        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (viewModel.onBackPressed()) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.msg_13)
                    .setCancelable(false)
                    .setPositiveButton(R.string.msg_2, (dialog, id) -> {
                        viewModel.onExitSelected();
                        MainActivity.super.onBackPressed();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    })
                    .setNegativeButton(R.string.msg_3, null)
                    .show();
        } else {
            if (channelsFragment.hasFocus())
                savedFocus = Focus.CHANNELS;
            else if (epgsFragment.hasFocus(Focus.EPGS))
                savedFocus = Focus.EPGS;
            else if (epgsFragment.hasFocus(Focus.DAYS))
                savedFocus = Focus.DAYS;
            else if (volumeFragment.hasFocus())
                savedFocus = Focus.VOLUME;
            else
                savedFocus = Focus.NONE;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Intent intent = getIntent();
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("RecreateApp", consoleFragment.onSettings());
            intent.putExtras(bundle);
            startActivityForResult(intent, MainActivity.ID);

            return true;
        } else if (itemId == R.id.menu_logout) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.msg_1)
                    .setCancelable(false)
                    .setPositiveButton(R.string.msg_2, (dialog, id) -> {
                        viewModel.onLogoutSelected();

                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);

                        finish();
                    })
                    .setNegativeButton(R.string.msg_3, null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
