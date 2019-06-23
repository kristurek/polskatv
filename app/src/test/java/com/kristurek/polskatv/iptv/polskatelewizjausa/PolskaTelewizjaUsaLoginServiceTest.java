package com.kristurek.polskatv.iptv.polskatelewizjausa;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.LoginRequest;
import com.kristurek.polskatv.iptv.core.dto.LoginResponse;
import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.iptv.core.exception.IptvValidatorException;
import com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit.PolskaTelewizjaUsaApiFactory;
import com.kristurek.polskatv.iptv.polskatelewizjausa.util.ExceptionHelper;
import com.kristurek.polskatv.util.TestExceptionHelper;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
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

public class PolskaTelewizjaUsaLoginServiceTest {

    private static MockWebServer mockServer;
    private static IptvService service;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @BeforeClass
    public static void createServer() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
        HttpUrl baseUrl = mockServer.url("/iptv/api/v1/json/login");
        service = new PolskaTelewizjaUsaService(PolskaTelewizjaUsaApiFactory.mockCreate("http://" + baseUrl.host() + ":" + baseUrl.port()));
    }

    @AfterClass
    public static void downServer() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void should_throw_exception_when_dto_fields_is_null() throws IptvException {
        expectedEx.expect(IptvValidatorException.class);
        expectedEx.expectMessage(ExceptionHelper.VALIDATOR_MSG);

        service.login(new LoginRequest());
    }

    @Test
    public void should_throw_exception_when_dto_is_null() throws IptvException {
        expectedEx.expect(IptvValidatorException.class);
        expectedEx.expectMessage(ExceptionHelper.VALIDATOR_MSG);

        service.login(null);
    }

    @Test
    public void should_return_successful_response() throws IOException, IptvException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/login_success_response.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        mockServer.enqueue(mockedResponse);

        LoginResponse responseDTO = service.login(new LoginRequest("user", "password"));

        assertNotNull(responseDTO);
        assertEquals("3736223480e8b6e280640cc4d94b0204", responseDTO.getSid());
        assertEquals(LocalDate.parse("2019-04-14", DateTimeFormat.forPattern("yyyy-MM-dd")), responseDTO.getBeginDate());
        assertEquals(LocalDate.parse("2019-05-14", DateTimeFormat.forPattern("yyyy-MM-dd")), responseDTO.getEndDate());
        assertEquals("ru", responseDTO.getInterfaceLang());
        assertEquals(0, responseDTO.getMediaServerId());
        assertEquals("1111", responseDTO.getParentalPass());
        assertEquals(29, responseDTO.getRestOfDay());
        assertEquals(0, responseDTO.getTimeShift());
        assertEquals(120, responseDTO.getTimeZone());
    }

    @Test
    public void should_throw_exception_when_subscription_expired() throws IOException, IptvException {
        expectedEx.expect(IptvException.class);
        expectedEx.expectMessage(ExceptionHelper.SUBSCRIPTION_EXPIRED_MSG);

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/login_success_response_subscription_expired.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        HttpUrl baseUrl = mockServer.url("/iptv/api/v1/json/login");
        mockServer.enqueue(mockedResponse);

        PolskaTelewizjaUsaService service = new PolskaTelewizjaUsaService(PolskaTelewizjaUsaApiFactory.mockCreate("http://" + baseUrl.host() + ":" + baseUrl.port()));
        service.login(new LoginRequest("user", "password"));
    }

    @Test
    public void should_throw_exception_when_login_or_pass_is_wrong() throws IOException, IptvException {
        expectedEx.expect(IptvException.class);
        expectedEx.expectMessage(TestExceptionHelper.ACC_WRONG_MSG);

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/error_response_acc_wrong.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        mockServer.enqueue(mockedResponse);

        service.login(new LoginRequest("user", "password"));
    }

    @Test
    public void should_throw_exception_when_json_response_has_invalid_format_1() throws IOException, IptvException {
        expectedEx.expect(IptvException.class);
        expectedEx.expectMessage(ExceptionHelper.CONVERTER_MSG);

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/login_response_invalid_json_format_1.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        mockServer.enqueue(mockedResponse);

        service.login(new LoginRequest("user", "password"));
    }

    @Test
    public void should_throw_exception_when_json_response_has_invalid_format_2() throws IOException, IptvException {
        expectedEx.expect(IptvException.class);
        expectedEx.expectMessage("com.google.gson.stream.MalformedJsonException: Expected name at line 3 column 2 path $.TEST");

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/login_response_invalid_json_format_2.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        mockServer.enqueue(mockedResponse);

        service.login(new LoginRequest("user", "password"));
    }

    @Test
    public void should_throw_exception_when_json_response_has_invalid_format_3() throws IOException, IptvException {
        expectedEx.expect(IptvException.class);
        expectedEx.expectMessage("Not a JSON Object. [TEST]");

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/login_response_invalid_json_format_3.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        mockServer.enqueue(mockedResponse);

        service.login(new LoginRequest("user", "password"));
    }
}
