package com.kristurek.polskatv.iptv.polskatelewizjausa.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.dto.UrlResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.url.UrlRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;

public class UrlConverter implements Converter<UrlRetrofitResponse, UrlResponse> {
    @Override
    public UrlResponse convert(UrlRetrofitResponse response) {
        Log.d(Tag.API, "UrlConverter.convert(" + response + ")");

        UrlResponse responseDTO = new UrlResponse();
        responseDTO.setUrl(response.getUrl());
        responseDTO.setUserAgent("iptv1world.com 2.65 - Windows, built at Jul 30 2013");//TODO delete and use in PlayerViewModel something like PolskaTV 1.21

        Log.d(Tag.API, "UrlConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
