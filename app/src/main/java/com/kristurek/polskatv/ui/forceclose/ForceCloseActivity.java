package com.kristurek.polskatv.ui.forceclose;

import android.os.Bundle;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.ui.arch.AbstractActivity;

public class ForceCloseActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.force_close_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.force_close_activity, ForceCloseFragment.newInstance())
                    .commitNow();
        }
    }
}
