package com.kristurek.polskatv.iptv.polskatelewizjausa;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.error.ErrorRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login.LoginRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.url.UrlRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit.PolskaTelewizjaUsaApiFactory;
import com.kristurek.polskatv.iptv.polskatelewizjausa.endpoint.PolskaTelewizjaUsaApi;
import com.kristurek.polskatv.iptv.polskatelewizjausa.util.Md5HashGenerator;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class JUnitTest {

    @Test
    public void testLoginAndLogoutMethod() throws IOException {
        PolskaTelewizjaUsaApi service = PolskaTelewizjaUsaApiFactory.create();

        String subscription = "1100324";// actual
        String password = "19944984";// actual

        //String subscription = "3472673";// expired
        //String password = "33541271";// expired

        password = Md5HashGenerator.md5(Md5HashGenerator.md5(subscription) + Md5HashGenerator.md5(password));

        Call<BaseRetrofitResponse> call = service.login(subscription, password, 1, 1);
        Response<BaseRetrofitResponse> response = call.execute();

        if (response.body() instanceof ErrorRetrofitResponse)
            Log.d("JunitTest", response.body().toString());
        if (response.body() instanceof LoginRetrofitResponse)
            Log.d("JunitTest", response.body().toString());
//if(true)return;
        //Call<BaseRetrofitResponse> call2 = service.logout();
        //Response<BaseRetrofitResponse> response2 = call2.execute();

        //Call<BaseRetrofitResponse> channelsCall = service.getChannels(1, 0, 1);
        //Response<BaseRetrofitResponse> responseChannelsCall = channelsCall.execute();
        /*ChannelsRetrofitResponse channelsResponse = (ChannelsRetrofitResponse) responseChannelsCall.body();
        StringBuilder cidsSB = new StringBuilder();
        for(Group group : channelsResponse.getGroups())
            for(Channel channel : group.getChannels())
                cidsSB.append(channel.getId()).append(",");
        String cids = cidsSB.toString().substring(0, cidsSB.toString().length() - 1);
        log.debug("CIDS", cids);
        long day = Duration.millis(DateTime.now().withTimeAtStartOfDay().getMillis()).getStandardSeconds();
        log.debug("DAY", String.valueOf(day) + " ");*/
        String cids = "";
        //Call<BaseRetrofitResponse> call3 = service.getLiveUrl(2134,0,1111);
        Call<BaseRetrofitResponse> call3 = service.getArchiveUrl(2134, 1555425562, "1111");
        Response<BaseRetrofitResponse> response3 = call3.execute();

        if (response3.body() instanceof ErrorRetrofitResponse)
            Log.d("JunitTest", response3.body().toString());
        if (response3.body() instanceof UrlRetrofitResponse)
            Log.d("JunitTest", response3.body().toString());


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        String responseString = gson.toJson(response3.body());
        //gson.toJson(responseString, new FileWriter("test2.json"));
        //log.debug("JunitTest", responseString);
    }

    @Test
    public void test2() {

    }
}