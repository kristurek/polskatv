package com.kristurek.polskatv.iptv.polskatelewizjausa;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.kristurek.polskatv.R;
import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.ChannelsRequest;
import com.kristurek.polskatv.iptv.core.dto.ChannelsResponse;
import com.kristurek.polskatv.iptv.core.dto.CurrentEpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.CurrentEpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.EpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.EpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.LoginRequest;
import com.kristurek.polskatv.iptv.core.dto.LoginResponse;
import com.kristurek.polskatv.iptv.core.dto.LogoutRequest;
import com.kristurek.polskatv.iptv.core.dto.LogoutResponse;
import com.kristurek.polskatv.iptv.core.dto.SettingsRequest;
import com.kristurek.polskatv.iptv.core.dto.SettingsResponse;
import com.kristurek.polskatv.iptv.core.dto.SimilarEpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.SimilarEpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.UrlRequest;
import com.kristurek.polskatv.iptv.core.dto.UrlResponse;
import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.iptv.polskatelewizjausa.converter.ChannelsConverter;
import com.kristurek.polskatv.iptv.polskatelewizjausa.converter.EpgsConverter;
import com.kristurek.polskatv.iptv.polskatelewizjausa.converter.LoginConverter;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels.ChannelsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.currentepgs.CurrentEpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.epgs.EpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login.LoginRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.logout.LogoutRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.settings.SettingsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.url.UrlRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit.CustomJsonDeserializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class PolskaTelewizjaUsaServiceMock implements IptvService {

    private Context mContext;

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .registerTypeAdapter(LoginRetrofitResponse.class, new CustomJsonDeserializer<LoginRetrofitResponse>())
            .registerTypeAdapter(LogoutRetrofitResponse.class, new CustomJsonDeserializer<LogoutRetrofitResponse>())
            .registerTypeAdapter(SettingsRetrofitResponse.class, new CustomJsonDeserializer<SettingsRetrofitResponse>())
            .registerTypeAdapter(ChannelsRetrofitResponse.class, new CustomJsonDeserializer<ChannelsRetrofitResponse>())
            .registerTypeAdapter(EpgsRetrofitResponse.class, new CustomJsonDeserializer<EpgsRetrofitResponse>())
            .registerTypeAdapter(CurrentEpgsRetrofitResponse.class, new CustomJsonDeserializer<CurrentEpgsRetrofitResponse>())
            .registerTypeAdapter(UrlRetrofitResponse.class, new CustomJsonDeserializer<UrlRetrofitResponse>())
            .create();

    public PolskaTelewizjaUsaServiceMock(Context context) {
        mContext = context;
    }

    private JsonReader prepareReader(int resource) {
        InputStream raw = mContext.getResources().openRawResource(resource);
        Reader rd = new BufferedReader(new InputStreamReader(raw));
        return new JsonReader(rd);
    }

    @Override
    public LoginResponse login(LoginRequest requestDTO) throws IptvException {
        LoginRetrofitResponse response = gson.fromJson(prepareReader(R.raw.login_response), LoginRetrofitResponse.class);
        LoginConverter converter = new LoginConverter();

        return converter.convert(response);
    }

    @Override
    public LogoutResponse logout(LogoutRequest requestDTO) throws IptvException {
        return null;
    }

    @Override
    public SettingsResponse saveSettings(SettingsRequest requestDTO) throws IptvException {
        return null;
    }

    @Override
    public ChannelsResponse getChannels(ChannelsRequest requestDTO) throws IptvException {
        ChannelsRetrofitResponse response = gson.fromJson(prepareReader(R.raw.channels_response), ChannelsRetrofitResponse.class);
        ChannelsConverter converter = new ChannelsConverter();

        return converter.convert(response);
    }

    @Override
    public EpgsResponse getEpgs(EpgsRequest requestDTO) throws IptvException {
        EpgsRetrofitResponse response = gson.fromJson(prepareReader(R.raw.epgs_response), EpgsRetrofitResponse.class);
        EpgsConverter converter = new EpgsConverter(0);

        return converter.convert(response);
    }

    @Override
    public CurrentEpgsResponse getCurrentEpgs(CurrentEpgsRequest requestDTO) throws IptvException {
        return new CurrentEpgsResponse();
    }

    @Override
    public UrlResponse getUrl(UrlRequest requestDTO) throws IptvException {
        UrlResponse response = new UrlResponse();
        response.setUserAgent("UserAgent");
        response.setUrl("http://37.221.172.231:8469/2134?587706569=5bbf6e548ff613e12b3de435f08a413cc19697ea14a7de8642e438bf23e50f6242414406247563701622767");

        return response;
    }

    @Override
    public SimilarEpgsResponse getSimilarEpgs(SimilarEpgsRequest requestDTO) throws IptvException {
        SimilarEpgsResponse response = new SimilarEpgsResponse();
        response.setEpgs(new ArrayList<>());

        return response;
    }
}
