package com.kristurek.polskatv.iptv.polbox.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.dto.LoginResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.login.List;
import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.polbox.pojo.login.LoginRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;
import com.kristurek.polskatv.util.DateTimeHelper;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class LoginConverter implements Converter<LoginRetrofitResponse, LoginResponse> {

    @Override
    public LoginResponse convert(LoginRetrofitResponse response) {
        Log.d(Tag.API, "LoginConverter.convert(" + response + ")");

        LoginResponse responseDTO = new LoginResponse();

        if (response.getAccount() != null && response.getAccount().getPacketExpire() != null)
            responseDTO.setRestOfDay(Days.daysBetween(LocalDate.now(), DateTimeHelper.unixTimeToLocalDate(response.getAccount().getPacketExpire())).getDays());

        responseDTO.setMediaServerId(response.getSettings().getStreamServer().getValue());
        responseDTO.setTimeShift(Integer.parseInt(response.getSettings().getTimeshift().getValue()));
        responseDTO.setTimeZone(Integer.parseInt(response.getSettings().getTimezone().getValue()));
        responseDTO.setParentalPass("1111");//TODO
        responseDTO.setInterfaceLang("en");
        for (List mediaServer : response.getSettings().getStreamServer().getList())
            responseDTO.getMediaServers().put(mediaServer.getIp(), mediaServer.getDescr());

        Log.d(Tag.API, "LoginConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
