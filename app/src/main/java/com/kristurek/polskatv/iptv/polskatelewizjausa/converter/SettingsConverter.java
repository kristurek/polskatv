package com.kristurek.polskatv.iptv.polskatelewizjausa.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.core.dto.SettingsResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.settings.SettingsRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;

public class SettingsConverter implements Converter<SettingsRetrofitResponse, SettingsResponse> {

    @Override
    public SettingsResponse convert(SettingsRetrofitResponse response) {
        Log.d(Tag.API, "SettingsConverter.convert(" + response + ")");

        SettingsResponse responseDTO = new SettingsResponse();

        Log.d(Tag.API, "SettingsConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
