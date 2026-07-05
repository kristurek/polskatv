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
import com.kristurek.polskatv.iptv.util.Tag;

import android.util.Log;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PolboxApiFactory {

    private static volatile OkHttpClient okHttpClient;

    public static PolboxApi create() {
        return getClient(PolboxApi.SERVICE_ENDPOINT).create(PolboxApi.class);
    }

    public static PolboxApi mockCreate(String url) {
        return getClient(url).create(PolboxApi.class);
    }

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (PolboxApiFactory.class) {
                if (okHttpClient == null) {
                    CookieManager cookieManager = new CookieManager();
                    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                    CookieJar cookieJar = new JavaNetCookieJar(cookieManager);

                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .cookieJar(cookieJar)
                            .addInterceptor(chain -> {
                                Request request = chain.request();

                                String requestBodyString = "";
                                if (request.body() != null) {
                                    Buffer requestBuffer = new Buffer();
                                    request.body().writeTo(requestBuffer);
                                    requestBodyString = requestBuffer.readUtf8();
                                }

                                StringBuilder requestLog = new StringBuilder();
                                requestLog.append("\n\u250f\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501 RETROFIT REQUEST \u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\n");
                                requestLog.append("\u2503 URL: ").append(request.url()).append("\n");
                                requestLog.append("\u2503 Method: ").append(request.method()).append("\n");

                                if (request.headers().size() > 0) {
                                    requestLog.append("\u2503 Headers:\n");
                                    for (String name : request.headers().names()) {
                                        requestLog.append("\u2503   ").append(name).append(": ").append(request.header(name)).append("\n");
                                    }
                                }

                                if (!requestBodyString.isEmpty()) {
                                    requestLog.append("\u2503 Payload: ").append(requestBodyString).append("\n");
                                }

                                String cookies = cookieJar.loadForRequest(request.url()).toString();
                                if (!cookies.equals("[]")) {
                                    requestLog.append("\u2503 Cookies: ").append(cookies).append("\n");
                                }
                                requestLog.append("\u2517\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\n");

                                Log.d(Tag.API, requestLog.toString());

                                Response response = chain.proceed(request);

                                ResponseBody responseBody = response.body();
                                String bodyString = "";
                                if (responseBody != null) {
                                    BufferedSource source = responseBody.source();
                                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                                    Buffer buffer = source.getBuffer();
                                    Charset charset = StandardCharsets.UTF_8;
                                    bodyString = buffer.clone().readString(charset);
                                }

                                StringBuilder responseLog = new StringBuilder();
                                responseLog.append("\n\u250f\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501 RETROFIT RESPONSE \u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\n");
                                responseLog.append("\u2503 Code: ").append(response.code()).append("\n");
                                responseLog.append("\u2503 Message: ").append(response.message()).append("\n");
                                if (!bodyString.isEmpty()) {
                                    responseLog.append("\u2503 Payload: ").append(bodyString).append("\n");
                                }
                                responseLog.append("\u2517\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\n");

                                Log.d(Tag.API, responseLog.toString());

                                return response;
                            })
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
                .registerTypeAdapter(ChannelsRetrofitResponse.class, new CustomJsonDeserializer<ChannelsRetrofitResponse>())
                .registerTypeAdapter(EpgsRetrofitResponse.class, new CustomJsonDeserializer<EpgsRetrofitResponse>())
                .registerTypeAdapter(CurrentEpgsRetrofitResponse.class, new CustomJsonDeserializer<CurrentEpgsRetrofitResponse>())
                .registerTypeAdapter(UrlRetrofitResponse.class, new CustomJsonDeserializer<UrlRetrofitResponse>())
                .registerTypeAdapter(SettingsRetrofitResponse.class, new CustomJsonDeserializer<SettingsRetrofitResponse>())
                .create()));
    }

}
