package com.kristurek.polskatv.iptv.polskatelewizjausa.endpoint;

import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.channels.ChannelsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.currentepgs.CurrentEpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.epgs.EpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login.LoginRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.logout.LogoutRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.settings.SettingsRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.url.UrlRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit.TargetClass;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PolskaTelewizjaUsaApi {

    String SERVICE_ENDPOINT = "http://core.iptv1world.com";

    @TargetClass(clazz = LoginRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/iptv/api/v1/json/login")
    @Headers({
            "User-Agent: iptv1world.com 2.65 - Windows, built at Jul 30 2013",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> login(@Field("login") String login,
                                     @Field("pass") String pass,
                                     @Field("with_acc") int with_acc,
                                     @Field("with_cfg") int with_cfg);

    @TargetClass(clazz = LogoutRetrofitResponse.class)
    @POST("/iptv/api/v1/json/logout")
    @Headers({
            "User-Agent: iptv1world.com 2.65 - Windows, built at Jul 30 2013",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> logout();

    @TargetClass(clazz = SettingsRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/iptv/api/v1/json/set")
    @Headers({
            "User-Agent: iptv1world.com 2.65 - Windows, built at Jul 30 2013",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> saveSettings(@Field("var") String var,
                                            @Field("val") String val);

    @TargetClass(clazz = SettingsRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/iptv/api/v1/json/set")
    @Headers({
            "User-Agent: iptv1world.com 2.65 - Windows, built at Jul 30 2013",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> saveSettings(@Field("var") String var,
                                            @Field("val") String val,
                                            @Field("protect_code") String protectCode);

    @TargetClass(clazz = ChannelsRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/iptv/api/v1/json/get_list_tv")
    @Headers({
            "User-Agent: iptv1world.com 2.65 - Windows, built at Jul 30 2013",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getChannels(@Field("with_epg") int withEpg,
                                           @Field("time_shift") int timeShift,
                                           @Field("mode") int mode);

    @TargetClass(clazz = EpgsRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/iptv/api/v1/json/get_epg")
    @Headers({
            "User-Agent: iptv1world.com 2.65 - Windows, built at Jul 30 2013",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getEpgs(@Field("cid") String cids,
                                       @Field("from_uts") long fromUts,
                                       @Field("hours") int hours,
                                       @Field("time_shift") int timeShift);

    @TargetClass(clazz = CurrentEpgsRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/iptv/api/v1/json/get_epg_current")
    @Headers({
            "User-Agent: iptv1world.com 2.65 - Windows, built at Jul 30 2013",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getCurrentEpgs(@Field("cid") String cids,
                                              @Field("time_shift") int timeShift);


    @TargetClass(clazz = UrlRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/iptv/api/v1/json/get_url_tv")
    @Headers({
            "User-Agent: iptv1world.com 2.65 - Windows, built at Jul 30 2013",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getLiveUrl(@Field("cid") int cid,
                                          @Field("time_shift") int timeShift,
                                          @Field("protect_code") String protectCode);

    @TargetClass(clazz = UrlRetrofitResponse.class)
    @FormUrlEncoded
    @POST("/iptv/api/v1/json/get_url_tv")
    @Headers({
            "User-Agent: iptv1world.com 2.65 - Windows, built at Jul 30 2013",
            "Connection: close",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept-Language: en-US,*"
    })
    Call<BaseRetrofitResponse> getArchiveUrl(@Field("cid") int cid,
                                             @Field("uts") long uts,
                                             @Field("protect_code") String protectCode);
}
