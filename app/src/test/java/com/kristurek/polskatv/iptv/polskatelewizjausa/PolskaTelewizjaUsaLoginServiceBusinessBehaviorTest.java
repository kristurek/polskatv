package com.kristurek.polskatv.iptv.polskatelewizjausa;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.LoginRequest;
import com.kristurek.polskatv.iptv.core.dto.LoginResponse;
import com.kristurek.polskatv.iptv.core.exception.IptvConverterException;
import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.iptv.core.exception.IptvSubscriptionExpiredException;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login.LoginRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.service.PolskaTelewizjaUsaApi;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileReader;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PolskaTelewizjaUsaLoginServiceBusinessBehaviorTest {

    @Mock
    private PolskaTelewizjaUsaApi polskaTelewizjaUsaApi;

    @Mock
    private Call<BaseRetrofitResponse> call;

    private IptvService service;

    @Before
    public void setUp() {
        service = new PolskaTelewizjaUsaService(polskaTelewizjaUsaApi);
    }

    @Test
    public void should_not_be_null() {
        assertNotNull(polskaTelewizjaUsaApi);
    }

    @Test(expected = IptvException.class)
    public void should_throw_exception_when_http_failed() throws IOException, IptvException {

        when(polskaTelewizjaUsaApi.login(anyString(), anyString(), anyInt(), anyInt())).thenReturn(call);
        when(call.execute()).thenReturn(Response.error(500, ResponseBody.create(MediaType.get("text/xml;charset=utf-8; charset=utf-8"), "There was an error on the server and the request could not be completed")));

        service.login(new LoginRequest("user", "password"));
    }

    @Test(expected = IptvConverterException.class)
    public void should_throw_exception_when_converter_failed() throws IptvException, IOException {
        Response<BaseRetrofitResponse> response = Response.success(new LoginRetrofitResponse());

        when(polskaTelewizjaUsaApi.login(anyString(), anyString(), anyInt(), anyInt())).thenReturn(call);
        when(call.execute()).thenReturn(response);

        service.login(new LoginRequest("user", "password"));
    }

    @Test
    public void should_return_successful_response() throws IOException, IptvException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        JsonReader reader = new JsonReader(new FileReader(loader.getResource("polskatelewizjausa/login_success_response.json").getFile()));
        LoginRetrofitResponse loginResponse = gson.fromJson(reader, LoginRetrofitResponse.class);

        Response<BaseRetrofitResponse> response = Response.success(loginResponse);

        when(polskaTelewizjaUsaApi.login(anyString(), anyString(), anyInt(), anyInt())).thenReturn(call);
        when(call.execute()).thenReturn(response);

        LoginResponse loginResponseDTO = service.login(new LoginRequest("user", "pass"));

        assertEquals(loginResponse.getSid(), loginResponseDTO.getSid());
        assertEquals(LocalDate.parse(loginResponse.getAccount().getSubscriptions().get(0).getBeginDate(), DateTimeFormat.forPattern("yyyy-MM-dd")),
                loginResponseDTO.getBeginDate());
        assertEquals(LocalDate.parse(loginResponse.getAccount().getSubscriptions().get(0).getEndDate(), DateTimeFormat.forPattern("yyyy-MM-dd")),
                loginResponseDTO.getEndDate());
        assertEquals(loginResponse.getSettings().getInterfaceLng(), loginResponseDTO.getInterfaceLang());
        assertEquals(loginResponse.getSettings().getMediaServerId(), Integer.valueOf(loginResponseDTO.getMediaServerId()));
        assertEquals(loginResponse.getSettings().getParentalPass(), loginResponseDTO.getParentalPass());
        assertEquals(loginResponse.getAccount().getSubscriptions().get(0).getRestOfDays(), String.valueOf(loginResponseDTO.getRestOfDay()));
        assertEquals(loginResponse.getSettings().getTimeShift(), Integer.valueOf(loginResponseDTO.getTimeShift()));
        assertEquals(loginResponse.getSettings().getTimeZone(), Integer.valueOf(loginResponseDTO.getTimeZone()));

        verify(polskaTelewizjaUsaApi, times(1)).login(anyString(), anyString(), anyInt(), anyInt());
        verify(call, times(1)).execute();
    }

    @Test(expected = IptvSubscriptionExpiredException.class)
    public void should_throw_exception_when_subscription_expired() throws IOException, IptvException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        JsonReader reader = new JsonReader(new FileReader(loader.getResource("polskatelewizjausa/login_success_response_subscription_expired.json").getFile()));
        LoginRetrofitResponse loginResponse = gson.fromJson(reader, LoginRetrofitResponse.class);

        Response<BaseRetrofitResponse> response = Response.success(loginResponse);

        when(polskaTelewizjaUsaApi.login(anyString(), anyString(), anyInt(), anyInt())).thenReturn(call);
        when(call.execute()).thenReturn(response);

        service.login(new LoginRequest("user", "pass"));
    }
}
