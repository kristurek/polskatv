package com.kristurek.polskatv.iptv.polbox.converter;

import android.util.Log;

import com.kristurek.polskatv.iptv.polbox.pojo.epgs.EpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.util.Tag;

public class EpgsConverter implements Converter<EpgsRetrofitResponse, EpgsRetrofitResponse> {

    @Override
    public EpgsRetrofitResponse convert(EpgsRetrofitResponse response) {
        Log.d(Tag.API, "EpgsConverter.convert(" + response + ")");
        return response;
    }
}
