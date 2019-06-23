package com.kristurek.polskatv.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.ui.arch.AbstractActivity;

import java.io.Serializable;

public class SettingsActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_activity, SettingsFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("RecreateApp", (Serializable) getIntent().getExtras().get("RecreateApp"));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();

        super.onBackPressed();
    }
}
