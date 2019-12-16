package com.kristurek.polskatv.iptv.polbox.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.dto.UrlResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.url.UrlRetrofitResponse;
import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.util.Tag;

public class UrlConverter implements Converter<UrlRetrofitResponse, UrlResponse> {
    @Override
    public UrlResponse convert(UrlRetrofitResponse response) {
        Log.d(Tag.API, "UrlConverter.convert(" + response + ")");

        UrlResponse responseDTO = new UrlResponse();

        String url = response.getUrl().replace("/ts","");
        url = url.split(" ", 2)[0];

        responseDTO.setUrl(url);
        responseDTO.setUserAgent("Polbox.TV 3.0.0B - Windows, built at Jul 18 2016");

        Log.d(Tag.API, "UrlConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
