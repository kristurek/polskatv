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

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PolboxApi {

    String SERVICE_ENDPOINT = "http://online.polbox.tv";

    @TargetClass(clazz = LoginRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/api/json/login")
    @Headers({
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> login(@HeaderMap Map<String, String> headers,
                                     @FieldMap Map<String, String> fields);

    @TargetClass(clazz = LogoutRetrofitResponse.class)
    @GET("/api/json/logout")
    @Headers({
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> logout(@Header("User-Agent") String userAgent);

    @TargetClass(clazz = ChannelsRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/api/json/channel_list")
    @Headers({
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getChannels(@Header("User-Agent") String userAgent,
                                            @Field("icon") int icon);

    @TargetClass(clazz = EpgsRetrofitResponse.class)
    @GET("/api/json/epg")
    @Headers({
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getEpgs(@Header("User-Agent") String userAgent,
                                       @Query("cid") String cid,
                                       @Query("day") String day);

    @TargetClass(clazz = CurrentEpgsRetrofitResponse.class)
    @GET("/api/json/epg_current")
    @Headers({
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getCurrentEpgs(@Header("User-Agent") String userAgent,
                                              @Query("cids") String cids,
                                              @Query("epg") int epg,
                                              @Query("fixtime") int fixTime);


    @TargetClass(clazz = UrlRetrofitResponse.class)
    @GET("/api/json/get_url")
    @Headers({
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getLiveUrl(@Header("User-Agent") String userAgent,
                                          @Query("cid") int cid,
                                          @Query("protect_code") String protectCode);

    @TargetClass(clazz = UrlRetrofitResponse.class)
    @GET("/api/json/get_url")
    @Headers({
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getArchiveUrl(@Header("User-Agent") String userAgent,
                                             @Query("cid") int cid,
                                             @Query("gmt") long gmt,
                                             @Query("protect_code") String protectCode);


    @TargetClass(clazz = SettingsRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/api/json/settings_set")
    @Headers({
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> saveSettings(@Header("User-Agent") String userAgent,
                                            @Field("var") String var,
                                            @Field("val") String val);

    @TargetClass(clazz = SettingsRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/api/json/settings_set")
    @Headers({
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> saveSettingsParentalPass(@Header("User-Agent") String userAgent,
                                                        @Field("var") String var,
                                                        @Field("old_code") String oldCode,
                                                        @Field("new_code") String newCode,
                                                        @Field("confirm_code") String confirmCode);
}
