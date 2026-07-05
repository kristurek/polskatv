package com.kristurek.polskatv.iptv.polbox;

import android.util.Log;

import com.google.common.base.Joiner;
import com.kristurek.polskatv.iptv.common.ExceptionHelper;
import com.kristurek.polskatv.iptv.common.ValidatorBean;
import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.ChannelsRequest;
import com.kristurek.polskatv.iptv.core.dto.ChannelsResponse;
import com.kristurek.polskatv.iptv.core.dto.CurrentEpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.CurrentEpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.EpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.EpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.LoginRequest;
import com.kristurek.polskatv.iptv.core.dto.LoginResponse;
import com.kristurek.polskatv.iptv.core.dto.LogoutRequest;
import com.kristurek.polskatv.iptv.core.dto.LogoutResponse;
import com.kristurek.polskatv.iptv.core.dto.SettingsRequest;
import com.kristurek.polskatv.iptv.core.dto.SettingsResponse;
import com.kristurek.polskatv.iptv.core.dto.SimilarEpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.SimilarEpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.UrlRequest;
import com.kristurek.polskatv.iptv.core.dto.UrlResponse;
import com.kristurek.polskatv.iptv.core.dto.common.Epg;
import com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType;
import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.iptv.core.exception.IptvSubscriptionExpiredException;
import com.kristurek.polskatv.iptv.core.exception.IptvValidatorException;
import com.kristurek.polskatv.iptv.polbox.converter.ChannelsConverter;
import com.kristurek.polskatv.iptv.polbox.converter.CurrentEpgsConverter;
import com.kristurek.polskatv.iptv.polbox.converter.EpgsConverter;
import com.kristurek.polskatv.iptv.polbox.converter.LoginConverter;
import com.kristurek.polskatv.iptv.polbox.converter.LogoutConverter;
import com.kristurek.polskatv.iptv.polbox.converter.SettingsConverter;
import com.kristurek.polskatv.iptv.polbox.converter.UnionEpgsConverter;
import com.kristurek.polskatv.iptv.polbox.converter.UrlConverter;
import com.kristurek.polskatv.iptv.polbox.endpoint.PolboxApi;
import com.kristurek.polskatv.iptv.polbox.pojo.epgs.EpgsRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.util.DateTimeHelper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PolboxService extends BasePolboxService implements IptvService {

    private PolboxApi api;

    private PreferencesService prefService;

    private LoginRequest reLoginRequest;

    private String parentalPass;

    public PolboxService(PolboxApi api, PreferencesService prefService) {
        this.api = api;
        this.prefService = prefService;
    }

    private void persistRequest(LoginRequest request) {
        reLoginRequest = request;
    }

    private String getSoftId() {
        String mode = prefService.get(PreferencesService.KEYS.PLAYER_COMPATIBILITY_MODE, "LINUX");
        Log.d(Tag.API, "PolboxService.getSoftId() mode: " + mode);
        switch (mode) {
            case "LEGACY":
                return "polwin-jo-001";
            case "WEB":
                return "react_smarttv_other";
            case "LINUX":
                return "react_linux";
            case "WINDOWS":
                return "react_win";
            default:
                return "react_linux";
        }
    }

    private String getUserAgent() {
        String mode = prefService.get(PreferencesService.KEYS.PLAYER_COMPATIBILITY_MODE, "LINUX");
        Log.d(Tag.API, "PolboxService.getUserAgent() mode: " + mode);
        return switch (mode) {
            case "LEGACY" -> "Polbox.TV 3.0.0B - Windows, built at Jul 18 2016";
            case "WEB" -> "Mozilla/5.0 (X11; Linux x86_64; rv:152.0) Gecko/20100101 Firefox/152.0";
            case "LINUX" ->
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) polbox.tv/1.4.3 Chrome/89.0.4389.128 Electron/12.0.9 Safari/537.36";
            case "WINDOWS" ->
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) polbox.tv/1.4.1 Chrome/89.0.4389.128 Electron/12.0.9 Safari/537.36";
            default ->
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) polbox.tv/1.4.3 Chrome/89.0.4389.128 Electron/12.0.9 Safari/537.36";
        };
    }

    @Override
    public LoginResponse login(LoginRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.login(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        persistRequest(request);

        String userAgent = getUserAgent();
        String softId = getSoftId();

        Log.d(Tag.API, "Polbox.login() userAgent: " + userAgent + ", softId: " + softId);

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", userAgent);

        Map<String, String> fields = new HashMap<>();
        fields.put("login", request.getLogin());
        fields.put("pass", request.getPass());
        fields.put("settings", "all");
        fields.put("softid", softId);
        fields.put("lang", "en");
        fields.put("device", "apple");

        if (softId.equals("react_smarttv_other")) {
            fields.put("userAgent", "react_web");
        } else {
            fields.put("cli_serial", "b9007bc2ca5768442a3fa4c41f14a4fb");
        }

        LoginResponse response = process(new LoginConverter(), () -> api.login(headers, fields));

        this.parentalPass = response.getParentalPass() != null ? response.getParentalPass() : request.getParentalPass();

        if (response.getRestOfDay() == 0)
            throw new IptvSubscriptionExpiredException(ExceptionHelper.SUBSCRIPTION_EXPIRED_MSG);

        return response;
    }

    @Override
    public LogoutResponse logout(LogoutRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.logout(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        return process(new LogoutConverter(), () -> api.logout(getUserAgent()));
    }

    @Override
    public SettingsResponse saveSettings(SettingsRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.saveSettings(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        String userAgent = getUserAgent();

        switch (request.getType()) {
            case HTTP_CACHING:
                return process(new SettingsConverter(), () -> api.saveSettings(userAgent, "http_caching", request.getNewValue()), () -> login(reLoginRequest));
            case BITRATE:
                return process(new SettingsConverter(), () -> api.saveSettings(userAgent, "bitrate", request.getNewValue()), () -> login(reLoginRequest));
            case STREAM_SERVER:
                return process(new SettingsConverter(), () -> api.saveSettings(userAgent, "stream_server", request.getNewValue()), () -> login(reLoginRequest));
            case TIME_SHIFT:
                return process(new SettingsConverter(), () -> api.saveSettings(userAgent, "timeshift", request.getNewValue()), () -> login(reLoginRequest));
            case TIME_ZONE:
                return new SettingsResponse();
            case PARENTAL_PASSWORD:
                SettingsResponse response = process(new SettingsConverter(), () -> api.saveSettingsParentalPass(userAgent, "pcode", request.getOldValue(), request.getNewValue(), request.getNewValue()), () -> login(reLoginRequest));
                this.parentalPass = request.getNewValue();
                return response;
            case LANGUAGE:
                return new SettingsResponse();
            default:
                throw new IptvException(ExceptionHelper.UNSUPPORTED_SETTINGS_TYPE_MSG);
        }
    }

    @Override
    public ChannelsResponse getChannels(ChannelsRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.getChannels(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        return process(new ChannelsConverter(), () -> api.getChannels(getUserAgent(), 1), () -> login(reLoginRequest));
    }

    @Override
    public EpgsResponse getEpgs(EpgsRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.getEpgs(" + request + ")");

        if (!ValidatorBean.validate(request) || request.getChannelIds().isEmpty())
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        String currentDay = DateTimeHelper.unixTimeToString(request.getFromBeginTime(), DateTimeHelper.ddMMyy);

        LocalDate previousDayLD = DateTimeHelper.getPreviousDay(DateTimeHelper.unixTimeToLocalDate(request.getFromBeginTime()));
        String previousDay = previousDayLD != null ? DateTimeHelper.localDateToString(previousDayLD, DateTimeHelper.ddMMyy) : null;

        LocalDate nextDayLD = DateTimeHelper.getNextDay(DateTimeHelper.unixTimeToLocalDate(request.getFromBeginTime()));
        String nextDay = nextDayLD != null ? DateTimeHelper.localDateToString(nextDayLD, DateTimeHelper.ddMMyy) : null;

        String cid = Joiner.on(",").join(request.getChannelIds());
        String userAgent = getUserAgent();

        EpgsRetrofitResponse response1 = null;
        if (previousDay != null)
            response1 = process(new EpgsConverter(), () -> api.getEpgs(userAgent, cid, previousDay), () -> login(reLoginRequest));

        EpgsRetrofitResponse response2 = process(new EpgsConverter(), () -> api.getEpgs(userAgent, cid, currentDay), () -> login(reLoginRequest));

        EpgsRetrofitResponse response3 = null;
        if (nextDay != null)
            response3 = process(new EpgsConverter(), () -> api.getEpgs(userAgent, cid, nextDay), () -> login(reLoginRequest));

        return new UnionEpgsConverter(request.getFromBeginTime()).convert(response1, response2, response3);
    }

    @Override
    public CurrentEpgsResponse getCurrentEpgs(CurrentEpgsRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.getCurrentEpgs(" + request + ")");

        if (!ValidatorBean.validate(request) || request.getChannelIds().isEmpty())
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        return process(new CurrentEpgsConverter(), () -> api.getCurrentEpgs(getUserAgent(), Joiner.on(",").join(request.getChannelIds()), 3, 1), () -> login(reLoginRequest));
    }

    @Override
    public UrlResponse getUrl(UrlRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.getUrl(" + request + ")");

        UrlResponse response = null;

        for (int i = 0; i < 2; i++) {
            response = tryGetUrl(request);

            if (!response.getUrl().contains("http://:/"))
                return response;
            else {
                Log.e(Tag.API, "Polbox.getUrl(" + request + ") found incorrect url, re-try");

                logout(new LogoutRequest());
                login(reLoginRequest);
            }
        }

        return response;
    }

    private UrlResponse tryGetUrl(UrlRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.tryGetUrl(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        UrlResponse response = null;
        String userAgent = getUserAgent();

        switch (request.getType()) {
            case LIVE_EPG:
                response = process(new UrlConverter(), () -> api.getLiveUrl(userAgent, request.getChannelId(), parentalPass), () -> login(reLoginRequest));
                break;
            case ARCHIVE_EPG:
                response = process(new UrlConverter(), () -> api.getArchiveUrl(userAgent, request.getChannelId(), request.getSeekToTime(), parentalPass), () -> login(reLoginRequest));
                break;
            default:
                throw new IptvException(ExceptionHelper.UNSUPPORTED_EPG_TYPE_MSG);
        }

        if (response.getUrl().equalsIgnoreCase("protected"))
            throw new IptvException(ExceptionHelper.PROTECTED_CONTENT_MSG);

        return response;
    }

    @Override
    public SimilarEpgsResponse getSimilarEpgs(SimilarEpgsRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.getSimilarEpgs(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        if (request.getChannelIds().size() > 1)
            throw new IptvException(ExceptionHelper.UNSUPPORTED_FUNCTIONALITY_MSG);

        List<LocalDate> days = DateTimeHelper.generateDays();

        List<Epg> results = new LinkedList<>();
        for (LocalDate day : days) {
            EpgsRequest epgsRequest = new EpgsRequest();
            epgsRequest.setChannelIds(request.getChannelIds());
            epgsRequest.setFromBeginTime(DateTimeHelper.localDateToUnixTime(day));

            EpgsResponse lResults = getEpgs(epgsRequest);
            results.addAll(lResults.getEpgs());
        }

        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();
        CollectionUtils.filter(results, epg -> {
            double result = jaroWinklerDistance.apply(request.getTitle(), epg.getTitle());
            return result > 0.80 && epg.getType().equals(EpgType.ARCHIVE_EPG);
        });

        Collections.reverse(results);

        SimilarEpgsResponse response = new SimilarEpgsResponse();
        response.setEpgs(results);

        Log.d(Tag.API, "Polbox.getSimilarEpgs(" + response + ")");
        return response;
    }


}
