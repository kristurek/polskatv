package com.kristurek.polskatv.iptv.polskatelewizjausa;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.SimilarEpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.SimilarEpgsResponse;
import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.iptv.core.exception.IptvValidatorException;
import com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit.PolskaTelewizjaUsaApiFactory;
import com.kristurek.polskatv.iptv.common.ExceptionHelper;
import com.kristurek.polskatv.util.TestExceptionHelper;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PolskaTelewizjaUsaGetSimilarEpgsServiceTest {

    private static MockWebServer mockServer;
    private static IptvService service;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @BeforeClass
    public static void createServer() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
        HttpUrl baseUrl = mockServer.url("/iptv/api/v1/json/get_epg");
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

        service.getSimilarEpgs(null);
    }

    @Test
    public void should_throw_exception_when_dto_fields_is_null() throws IptvException {
        expectedEx.expect(IptvValidatorException.class);
        expectedEx.expectMessage(ExceptionHelper.VALIDATOR_MSG);

        service.getSimilarEpgs(new SimilarEpgsRequest(new HashSet<>(), "", 0));
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

        service.getSimilarEpgs(new SimilarEpgsRequest(new HashSet<>(Arrays.asList(101, 21)), "Supernatural", 0));
    }

    @Test
    public void should_return_successful_response() throws IOException, IptvException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/similar_epgs_success_response_all_channels.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        mockServer.enqueue(mockedResponse);

        long beginArchive = Duration.millis(new DateTime(1555390800).withTimeAtStartOfDay().minusDays(13).getMillis()).getStandardSeconds();

        SimilarEpgsResponse responseDTO = service.getSimilarEpgs(new SimilarEpgsRequest(new HashSet<>(Arrays.asList(2455)), "Międzynarodowi poszukiwacze domów", beginArchive));

        assertNotNull(responseDTO);
        assertEquals(4, responseDTO.getEpgs().size());
    }
}
