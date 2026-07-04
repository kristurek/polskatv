package com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit;

import com.google.gson.GsonBuilder;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels.ChannelsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.currentepgs.CurrentEpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.epgs.EpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login.LoginRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.logout.LogoutRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.settings.SettingsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.url.UrlRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.endpoint.PolskaTelewizjaUsaApi;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PolskaTelewizjaUsaApiFactory {

    private static volatile OkHttpClient okHttpClient;

    public static PolskaTelewizjaUsaApi create() {
        return getClient(PolskaTelewizjaUsaApi.SERVICE_ENDPOINT).create(PolskaTelewizjaUsaApi.class);
    }

    public static PolskaTelewizjaUsaApi mockCreate(String url) {
        return getClient(url).create(PolskaTelewizjaUsaApi.class);
    }

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (PolskaTelewizjaUsaApiFactory.class) {
                if (okHttpClient == null) {
                    CookieManager cookieManager = new CookieManager();
                    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                    CookieJar cookieJar = new JavaNetCookieJar(cookieManager);

                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .cookieJar(cookieJar)
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    private static Retrofit getClient(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(getOkHttpClient())
                .addConverterFactory(createGsonConverterFactory())
                .build();
    }

    private static CustomGsonConverterFactory createGsonConverterFactory() {
        return new CustomGsonConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LoginRetrofitResponse.class, new CustomJsonDeserializer<LoginRetrofitResponse>())
                .registerTypeAdapter(LogoutRetrofitResponse.class, new CustomJsonDeserializer<LogoutRetrofitResponse>())
                .registerTypeAdapter(SettingsRetrofitResponse.class, new CustomJsonDeserializer<SettingsRetrofitResponse>())
                .registerTypeAdapter(ChannelsRetrofitResponse.class, new CustomJsonDeserializer<ChannelsRetrofitResponse>())
                .registerTypeAdapter(EpgsRetrofitResponse.class, new CustomJsonDeserializer<EpgsRetrofitResponse>())
                .registerTypeAdapter(CurrentEpgsRetrofitResponse.class, new CustomJsonDeserializer<CurrentEpgsRetrofitResponse>())
                .registerTypeAdapter(UrlRetrofitResponse.class, new CustomJsonDeserializer<UrlRetrofitResponse>())
                .create()));
    }

}
