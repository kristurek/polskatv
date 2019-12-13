package com.kristurek.polskatv.iptv.polbox.retrofit;

import com.google.gson.GsonBuilder;
import com.kristurek.polskatv.iptv.polbox.endpoint.PolboxApi;
import com.kristurek.polskatv.iptv.polbox.pojo.channels.ChannelsRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.currentepgs.CurrentEpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.epgs.EpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.login.LoginRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.logout.LogoutRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.settings.SettingsRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.url.UrlRetrofitResponse;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PolboxApiFactory {

    public static PolboxApi create() {
        return getClient(PolboxApi.SERVICE_ENDPOINT).create(PolboxApi.class);
    }

    public static PolboxApi mockCreate(String url) {
        return getClient(url).create(PolboxApi.class);
    }

    private static Retrofit getClient(String url) {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieJar cookieJar = new JavaNetCookieJar(cookieManager);

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .cookieJar(cookieJar);

        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(createGsonConverterFactory())
                .build();
    }

    private static CustomGsonConverterFactory createGsonConverterFactory() {
        return new CustomGsonConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LoginRetrofitResponse.class, new CustomJsonDeserializer<LoginRetrofitResponse>())
                .registerTypeAdapter(LogoutRetrofitResponse.class, new CustomJsonDeserializer<LogoutRetrofitResponse>())
                .registerTypeAdapter(ChannelsRetrofitResponse.class, new CustomJsonDeserializer<ChannelsRetrofitResponse>())
                .registerTypeAdapter(EpgsRetrofitResponse.class, new CustomJsonDeserializer<EpgsRetrofitResponse>())
                .registerTypeAdapter(CurrentEpgsRetrofitResponse.class, new CustomJsonDeserializer<CurrentEpgsRetrofitResponse>())
                .registerTypeAdapter(UrlRetrofitResponse.class, new CustomJsonDeserializer<UrlRetrofitResponse>())
                .registerTypeAdapter(SettingsRetrofitResponse.class, new CustomJsonDeserializer<SettingsRetrofitResponse>())
                .create()));
    }

}
