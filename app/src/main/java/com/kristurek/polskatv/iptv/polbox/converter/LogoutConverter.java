package com.kristurek.polskatv.iptv.polbox.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.dto.LogoutResponse;
import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.polbox.pojo.logout.LogoutRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;

public class LogoutConverter implements Converter<LogoutRetrofitResponse, LogoutResponse> {

    @Override
    public LogoutResponse convert(LogoutRetrofitResponse response) {
        Log.d(Tag.API, "LogoutConverter.convert(" + response + ")");

        LogoutResponse responseDTO = new LogoutResponse();

        Log.d(Tag.API, "LogoutConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
