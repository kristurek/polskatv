package com.kristurek.polskatv.iptv.polskatelewizjausa;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.ChannelsRequest;
import com.kristurek.polskatv.iptv.core.dto.ChannelsResponse;
import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.iptv.core.exception.IptvConverterException;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PolskaTelewizjaUsaGetChannelsServiceTest {

    private static MockWebServer mockServer;
    private static IptvService service;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @BeforeClass
    public static void createServer() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
        HttpUrl baseUrl = mockServer.url("/iptv/api/v1/json/get_list_tv");
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

        service.getChannels(null);
    }

    @Test
    public void should_throw_exception_when_converter_failed() throws IptvException, IOException {
        expectedEx.expect(IptvConverterException.class);
        expectedEx.expectMessage(ExceptionHelper.CONVERTER_MSG);

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/channels_success_response_no_fill_fields.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setResponseCode(200);
        mockedResponse.setBody(response);

        mockServer.enqueue(mockedResponse);

        service.getChannels(new ChannelsRequest());
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

        service.getChannels(new ChannelsRequest());
    }

    @Test
    public void should_return_successful_response() throws IOException, IptvException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/channels_success_response.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        mockServer.enqueue(mockedResponse);

        ChannelsResponse responseDTO = service.getChannels(new ChannelsRequest());

        assertNotNull(responseDTO);
        assertEquals(10, responseDTO.getGroups().size());
    }

    @Test
    public void should_return_successful_response_after_auto_relogin() throws IOException, IptvException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();

        String response1 = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/error_response_session_outdated.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse1 = new MockResponse();
        mockedResponse1.setResponseCode(200);
        mockedResponse1.setBody(response1);

        mockServer.enqueue(mockedResponse1);

        String response2 = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/channels_success_response.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse2 = new MockResponse();
        mockedResponse2.setBody(response2);
        mockedResponse2.setResponseCode(200);

        mockServer.enqueue(mockedResponse2);

        ChannelsResponse responseDTO = service.getChannels(new ChannelsRequest());

        assertNotNull(responseDTO);
        assertEquals(10, responseDTO.getGroups().size());
    }
}
