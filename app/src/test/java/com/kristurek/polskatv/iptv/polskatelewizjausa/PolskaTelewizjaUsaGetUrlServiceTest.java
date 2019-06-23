package com.kristurek.polskatv.iptv.polskatelewizjausa;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.UrlRequest;
import com.kristurek.polskatv.iptv.core.dto.UrlResponse;
import com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType;
import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.iptv.core.exception.IptvValidatorException;
import com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit.PolskaTelewizjaUsaApiFactory;
import com.kristurek.polskatv.iptv.polskatelewizjausa.util.ExceptionHelper;
import com.kristurek.polskatv.util.TestExceptionHelper;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertNotNull;

public class PolskaTelewizjaUsaGetUrlServiceTest {

    private static MockWebServer mockServer;
    private static IptvService service;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @BeforeClass
    public static void createServer() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
        HttpUrl baseUrl = mockServer.url("/iptv/api/v1/json/get_url_tv");
        service = new PolskaTelewizjaUsaService(PolskaTelewizjaUsaApiFactory.mockCreate("http://" + baseUrl.host() + ":" + baseUrl.port()));
    }

    @AfterClass
    public static void downServer() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void should_throw_exception_when_dto_is_null() throws IptvException {
        expectedEx.expect(IptvValidatorException.class);
        expectedEx.expectMessage(ExceptionHelper.VALIDATOR_MSG);

        service.getUrl(null);
    }

    @Test
    public void should_throw_exception_when_dto_fields_is_null() throws IptvException {
        expectedEx.expect(IptvValidatorException.class);
        expectedEx.expectMessage(ExceptionHelper.VALIDATOR_MSG);

        service.getUrl(new UrlRequest());
    }

    @Test
    public void should_throw_exception_when_session_is_timeouted() throws IptvException, IOException {
        expectedEx.expect(IptvException.class);
        expectedEx.expectMessage(TestExceptionHelper.STIMEOUT_MSG);

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/error_response_session_outdated.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setResponseCode(200);
        mockedResponse.setBody(response);

        mockServer.enqueue(mockedResponse);//first attempt
        mockServer.enqueue(mockedResponse);//second attempt

        service.getUrl(new UrlRequest(2134, "1111", EpgType.LIVE_EPG));
    }

    @Test
    public void should_return_successful_response_live() throws IOException, IptvException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/url_success_response_live.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        mockServer.enqueue(mockedResponse);

        UrlResponse responseDTO = service.getUrl(new UrlRequest(2134, "1111", EpgType.LIVE_EPG));

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getUrl());
    }

    @Test
    public void should_return_successful_response_archive() throws IOException, IptvException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/url_success_response_archive.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        mockServer.enqueue(mockedResponse);

        UrlResponse responseDTO = service.getUrl(new UrlRequest(2134, 1555425562, "1111", EpgType.ARCHIVE_EPG));

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getUrl());
    }
}
