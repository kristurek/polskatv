package com.kristurek.polskatv.ui.login;

import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.ui.arch.AbstractActivity;

public class LoginActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.login_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.login_activity, LoginFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        LoginActivity.super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
