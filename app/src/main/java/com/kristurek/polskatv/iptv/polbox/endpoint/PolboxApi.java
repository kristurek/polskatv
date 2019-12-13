package com.kristurek.polskatv.iptv.polbox.endpoint;

import com.kristurek.polskatv.iptv.polbox.pojo.channels.ChannelsRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.common.BaseRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.currentepgs.CurrentEpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.epgs.EpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.login.LoginRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.logout.LogoutRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.settings.SettingsRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.url.UrlRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.retrofit.TargetClass;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PolboxApi {

    String SERVICE_ENDPOINT = "http://online.polbox.tv";

    @TargetClass(clazz = LoginRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/api/json/login")
    @Headers({
            "User-Agent: Polbox.TV 3.0.0B - Windows, built at Jul 18 2016",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> login(@Field("login") String login,
                                     @Field("pass") String pass,
                                     @Field("settings") String settings,
                                     @Field("softid") String softId,
                                     @Field("cli_serial") String cliSerial,
                                     @Field("lang") String lang);

    @TargetClass(clazz = LogoutRetrofitResponse.class)
    @GET("/api/json/logout")
    @Headers({
            "User-Agent: Polbox.TV 3.0.0B - Windows, built at Jul 18 2016",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> logout();

    @TargetClass(clazz = ChannelsRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/api/json/channel_list")
    @Headers({
            "User-Agent: Polbox.TV 3.0.0B - Windows, built at Jul 18 2016",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getChannels(@Field("icon") int icon);

    @TargetClass(clazz = EpgsRetrofitResponse.class)
    @GET("/api/json/epg")
    @Headers({
            "User-Agent: Polbox.TV 3.0.0B - Windows, built at Jul 18 2016",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getEpgs(@Query("cid") String cid, @Query("day") String day);

    @TargetClass(clazz = CurrentEpgsRetrofitResponse.class)
    @GET("/api/json/epg_current")
    @Headers({
            "User-Agent: Polbox.TV 3.0.0B - Windows, built at Jul 18 2016",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getCurrentEpgs(@Query("cids") String cids, @Query("epg") int epg, @Query("fixtime") int fixTime);


    @TargetClass(clazz = UrlRetrofitResponse.class)
    @GET("/api/json/get_url")
    @Headers({
            "User-Agent: Polbox.TV 3.0.0B - Windows, built at Jul 18 2016",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getLiveUrl(@Query("cid") int cid,
                                          @Query("protect_code") String protectCode);

    @TargetClass(clazz = UrlRetrofitResponse.class)
    @GET("/api/json/get_url")
    @Headers({
            "User-Agent: Polbox.TV 3.0.0B - Windows, built at Jul 18 2016",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getArchiveUrl(@Query("cid") int cid,
                                             @Query("gmt") long gmt,
                                             @Query("protect_code") String protectCode);


    @TargetClass(clazz = SettingsRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/api/json/settings_set")
    @Headers({
            "User-Agent: Polbox.TV 3.0.0B - Windows, built at Jul 18 2016",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> saveSettings(@Field("var") String var,
                                            @Field("val") String val);

    @TargetClass(clazz = SettingsRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/api/json/settings_set")
    @Headers({
            "User-Agent: Polbox.TV 3.0.0B - Windows, built at Jul 18 2016",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> saveSettingsParentalPass(@Field("var") String var,
                                                        @Field("old_code") String oldCode,
                                                        @Field("new_code") String newCode,
                                                        @Field("confirm_code") String confirmCode);
}
