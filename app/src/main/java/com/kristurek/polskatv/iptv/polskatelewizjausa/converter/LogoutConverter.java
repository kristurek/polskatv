package com.kristurek.polskatv.iptv.polskatelewizjausa.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.dto.LogoutResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.logout.LogoutRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;

public class LogoutConverter implements Converter<LogoutRetrofitResponse, LogoutResponse> {

    @Override
    public LogoutResponse convert(LogoutRetrofitResponse response) {
        Log.d(Tag.API, "LogoutConverter.convert(" + response + ")");

        LogoutResponse responseDTO = new LogoutResponse();
        responseDTO.setMessage(response.getMessage());

        Log.d(Tag.API, "LogoutConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
