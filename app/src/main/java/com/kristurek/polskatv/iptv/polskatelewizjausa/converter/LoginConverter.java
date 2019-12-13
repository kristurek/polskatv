package com.kristurek.polskatv.iptv.polskatelewizjausa.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.core.dto.LoginResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login.LoginRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login.MediaServer;
import com.kristurek.polskatv.iptv.util.Tag;

public class LoginConverter implements Converter<LoginRetrofitResponse, LoginResponse> {

    @Override
    public LoginResponse convert(LoginRetrofitResponse response) {
        Log.d(Tag.API, "LoginConverter.convert(" + response + ")");

        LoginResponse responseDTO = new LoginResponse();

        if (response.getAccount() != null
                && response.getAccount().getSubscriptions() != null
                && !response.getAccount().getSubscriptions().isEmpty())
            responseDTO.setRestOfDay(Integer.valueOf(response.getAccount().getSubscriptions().get(0).getRestOfDays()));

        responseDTO.setMediaServerId(String.valueOf(response.getSettings().getMediaServerId()));
        responseDTO.setTimeShift(response.getSettings().getTimeShift());
        responseDTO.setTimeZone(response.getSettings().getTimeZone());
        responseDTO.setParentalPass(response.getSettings().getParentalPass());
        responseDTO.setInterfaceLang(response.getSettings().getInterfaceLng());
        for (MediaServer mediaServer : response.getSettings().getMediaServers())
            responseDTO.getMediaServers().put(mediaServer.getId(), mediaServer.getTitle());

        Log.d(Tag.API, "LoginConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
