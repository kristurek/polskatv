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

        String url;

        if(!response.getUrl().contains("protected")) {
            url = "http://".concat(response.getUrl().split("//", 2)[1]);
            url = url.split(" ", 2)[0];
        } else {
            url = response.getUrl();
        }

        responseDTO.setUrl(url);
        responseDTO.setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) polbox.tv/1.4.3 Chrome/89.0.4389.128 Electron/12.0.9 Safari/537.36");

        Log.d(Tag.API, "UrlConverter.convert(" + responseDTO + ")");
        return responseDTO;
    }
}
