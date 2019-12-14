package com.kristurek.polskatv.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.preference.DialogPreference;

import com.kristurek.polskatv.R;

public class XPasswordPreference extends DialogPreference {//TODO impl old pass and new pass in one dialog box

    private EditText newPassword;

    public XPasswordPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected View onCreateDialogView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.x_password_preference, null);
        newPassword = v.findViewById(R.id.x_password_preference_new_password);
        return v;
    }

    public String getOldPassword() {
        return getPersistedString("");
    }


    public String getNewPassword() {
        return newPassword.getText().toString();
    }
}
