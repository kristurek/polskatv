package com.kristurek.polskatv.iptv.polskatelewizjausa.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.dto.LoginResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login.LoginRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login.MediaServer;
import com.kristurek.polskatv.iptv.util.Tag;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class LoginConverter implements Converter<LoginRetrofitResponse, LoginResponse> {

    @Override
    public LoginResponse convert(LoginRetrofitResponse response) {
        Log.d(Tag.API, "LoginConverter.convert(" + response + ")");

        LoginResponse responseDTO = new LoginResponse();

        responseDTO.setSid(response.getSid());
        if (response.getAccount() != null
                && response.getAccount().getSubscriptions() != null
                && !response.getAccount().getSubscriptions().isEmpty()) {
            String beginDate = response.getAccount().getSubscriptions().get(0).getBeginDate();
            responseDTO.setBeginDate(LocalDate.parse(beginDate, DateTimeFormat.forPattern("yyyy-MM-dd")));
            String endDate = response.getAccount().getSubscriptions().get(0).getEndDate();
            responseDTO.setEndDate(LocalDate.parse(endDate, DateTimeFormat.forPattern("yyyy-MM-dd")));
            responseDTO.setRestOfDay(Integer.valueOf(response.getAccount().getSubscriptions().get(0).getRestOfDays()));
        }

        responseDTO.setMediaServerId(response.getSettings().getMediaServerId());
        responseDTO.setTimeShift(response.getSettings().getTimeShift());
        responseDTO.setTimeZone(response.getSettings().getTimeZone());
        responseDTO.setParentalPass(response.getSettings().getParentalPass());
        responseDTO.setInterfaceLang(response.getSettings().getInterfaceLng());
        for (MediaServer mediaServer : response.getSettings().getMediaServers())
            responseDTO.getMediaServers().put(Integer.valueOf(mediaServer.getId()), mediaServer.getTitle());

        Log.d(Tag.API, "LoginConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
