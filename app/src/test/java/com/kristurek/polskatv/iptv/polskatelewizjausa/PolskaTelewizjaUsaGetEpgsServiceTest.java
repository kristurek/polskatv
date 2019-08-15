package com.kristurek.polskatv.iptv.polskatelewizjausa;


import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.EpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.EpgsResponse;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PolskaTelewizjaUsaGetEpgsServiceTest {

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

        service.getEpgs(null);
    }

    @Test
    public void should_throw_exception_when_dto_fields_is_null() throws IptvException {
        expectedEx.expect(IptvValidatorException.class);
        expectedEx.expectMessage(ExceptionHelper.VALIDATOR_MSG);

        service.getEpgs(new EpgsRequest());
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

        service.getEpgs(new EpgsRequest(new HashSet<>(Arrays.asList(2134)), 1555352418));
    }

    @Test
    public void should_return_successful_response() throws IOException, IptvException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/epgs_success_response.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        mockServer.enqueue(mockedResponse);

        EpgsResponse responseDTO = service.getEpgs(new EpgsRequest(new HashSet<>(Arrays.asList(2134)), 1555352418));

        assertNotNull(responseDTO);
        assertEquals(58, responseDTO.getEpgs().size());
    }

    @Test
    public void should_return_successful_response_all_channels() throws IOException, IptvException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String response = new String(Files.readAllBytes(Paths.get(loader.getResource("polskatelewizjausa/epgs_success_response_all_channels.json").getPath())), Charset.defaultCharset());

        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setBody(response);
        mockedResponse.setResponseCode(200);

        mockServer.enqueue(mockedResponse);

        String cids = "521,2076,522,2077,545,520,2075,2129,524,579,2080,2131,525,544,580,543,698,586,583,582,2128,2134,2393,2455,2456,2457,2458,2461,2462,868,523,548,581,526,2030,485,170,44,588,587,527,528,535,866,551,970,2042,2132,171,2459,697,627,584,537,553,985,2056,547,536,867,534,589,533,623,624,625,862,863,555,552,864,861,2057,2058,2166,2167,2168,2169,2170,2177,2178,2453,2454,2460,699,700,701,538,585,546,865,2060,2165,2171,2172,2173,2174,2176,530,531,626,634,635,969,2072,2175,2183,2130,2396,2397,2398,2467,2468,554,550,978,2179,2181,2182,759,636,155,157,156,158,360,363,578,364,366";
        Set<String> cidsSetString = new HashSet<>(Arrays.asList(cids.split(",")));
        Set<Integer> cidsSet = cidsSetString.stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toSet());

        EpgsResponse responseDTO = service.getEpgs(new EpgsRequest(cidsSet, 1555390800));

        assertNotNull(responseDTO);
        assertEquals(3693, responseDTO.getEpgs().size());
    }
}

