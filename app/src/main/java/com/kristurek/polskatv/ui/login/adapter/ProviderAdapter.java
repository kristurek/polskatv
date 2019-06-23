package com.kristurek.polskatv.ui.login.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.kristurek.polskatv.ui.login.model.ProviderModel;

import java.util.List;

public class ProviderAdapter extends ArrayAdapter<ProviderModel> {

    public ProviderAdapter(Context context, List<ProviderModel> list) {
        super(context, android.R.layout.simple_spinner_item, list);
        //setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
    public ProviderAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item);
        //setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
