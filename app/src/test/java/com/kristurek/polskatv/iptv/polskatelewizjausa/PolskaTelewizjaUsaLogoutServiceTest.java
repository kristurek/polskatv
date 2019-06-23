package com.kristurek.polskatv.iptv.polskatelewizjausa;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.LogoutRequest;
import com.kristurek.polskatv.iptv.core.dto.LogoutResponse;
import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit.PolskaTelewizjaUsaApiFactory;
import com.kristurek.polskatv.util.TestExceptionHelper;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class PolskaTelewizjaUsaLogoutServiceTest {

    private static MockWebServer mockServer;
    private static IptvService service;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @BeforeClass
    public static void createServer() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
        HttpUrl baseUrl = mockServer.url("/iptv/api/v1/json/logout");
        service = new PolskaTelewizjaUsaService(PolskaTelewizjaUsaApiFactory.mockCreate("http://" + baseUrl.host() + ":" + baseUrl.port()));
    }

    @AfterClass
    public static void downServer() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void should_throw_exception_when_bad_session_id() throws IptvException, IOException {
        expectedEx.expect(IptvException.class);
        expectedEx.expectMessage(TestExceptionHelper.BAD_SID_MSG);

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/error_response_bad_sid.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setResponseCode(200);
        mockedResponse.setBody(response);

        mockServer.enqueue(mockedResponse);

        service.logout(new LogoutRequest());
    }

    @Test
    public void should_throw_exception_when_no_such_session() throws IptvException, IOException {
        expectedEx.expect(IptvException.class);
        expectedEx.expectMessage(TestExceptionHelper.NO_SUCH_SESSION_MSG);

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/error_response_no_sush_session.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setResponseCode(200);
        mockedResponse.setBody(response);

        mockServer.enqueue(mockedResponse);

        service.logout(new LogoutRequest());
    }

    @Test
    public void should_throw_exception_when_session_is_outdated() throws IptvException, IOException {
        expectedEx.expect(IptvException.class);
        expectedEx.expectMessage(TestExceptionHelper.STIMEOUT_MSG);

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/error_response_session_outdated.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setResponseCode(200);
        mockedResponse.setBody(response);

        mockServer.enqueue(mockedResponse);

        service.logout(new LogoutRequest());
    }

    @Test
    public void should_return_successful_response() throws IptvException, IOException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/logout_success_response.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setResponseCode(200);
        mockedResponse.setBody(response);

        mockServer.enqueue(mockedResponse);

        LogoutResponse responseDTO = service.logout(new LogoutRequest());
        assertNotNull(responseDTO);
        assertEquals(responseDTO.getMessage(),"OK");
    }
}
