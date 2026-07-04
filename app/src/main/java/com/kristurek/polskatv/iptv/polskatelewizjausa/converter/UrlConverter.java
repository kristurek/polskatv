package com.kristurek.polskatv.iptv.polskatelewizjausa.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.core.dto.UrlResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.url.UrlRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;

public class UrlConverter implements Converter<UrlRetrofitResponse, UrlResponse> {
    @Override
    public UrlResponse convert(UrlRetrofitResponse response) {
        Log.d(Tag.API, "UrlConverter.convert(" + response + ")");

        UrlResponse responseDTO = new UrlResponse();
        responseDTO.setUrl(response.getUrl());

        Log.d(Tag.API, "UrlConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
