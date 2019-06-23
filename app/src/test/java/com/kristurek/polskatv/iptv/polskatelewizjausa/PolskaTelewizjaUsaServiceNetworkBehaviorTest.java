package com.kristurek.polskatv.iptv.polskatelewizjausa;

import com.kristurek.polskatv.iptv.core.dto.LoginRequest;
import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit.PolskaTelewizjaUsaApiFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;

public class PolskaTelewizjaUsaServiceNetworkBehaviorTest {

    private static MockWebServer mockServer;

    @BeforeClass
    public static void createServer() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterClass
    public static void downServer() throws IOException {
        mockServer.shutdown();
    }

    @Test(expected = IptvException.class)
    public void should_throw_exception_when_http_timeout() throws IptvException {
        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setResponseCode(408);
        mockedResponse.setBody("Request Timeout");
        mockedResponse.setSocketPolicy(SocketPolicy.NO_RESPONSE);

        HttpUrl baseUrl = mockServer.url("/iptv/api/v1/json/login");
        mockServer.enqueue(mockedResponse);

        PolskaTelewizjaUsaService service = new PolskaTelewizjaUsaService(PolskaTelewizjaUsaApiFactory.mockCreate("http://" + baseUrl.host() + ":" + baseUrl.port()));
        service.login(new LoginRequest("user", "password"));
    }

    @Test(expected = IptvException.class)
    public void should_throw_exception_when_http_code_500() throws IptvException {
        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody("Internal Server Error");
        mockedResponse.setResponseCode(500);

        HttpUrl baseUrl = mockServer.url("/iptv/api/v1/json/login");
        mockServer.enqueue(mockedResponse);

        PolskaTelewizjaUsaService service = new PolskaTelewizjaUsaService(PolskaTelewizjaUsaApiFactory.mockCreate("http://" + baseUrl.host() + ":" + baseUrl.port()));
        service.login(new LoginRequest("user", "password"));
    }
}
