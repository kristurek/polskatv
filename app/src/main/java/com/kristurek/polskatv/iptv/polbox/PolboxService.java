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
import com.kristurek.polskatv.util.DateTimeHelper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PolboxService extends BasePolboxService implements IptvService {

    private PolboxApi api;

    private LoginRequest reLoginRequest;

    public PolboxService(PolboxApi api) {
        this.api = api;
    }

    private void persistRequest(LoginRequest request) {
        reLoginRequest = request;
    }

    @Override
    public LoginResponse login(LoginRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.login(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        persistRequest(request);

        LoginResponse response = process(new LoginConverter(), () -> api.login(request.getLogin(), request.getPass(), "all", "polwin-jo-001", "b9007bc2ca5768442a3fa4c41f14a4fb", "en"));

        if (response.getRestOfDay() == 0)
            throw new IptvSubscriptionExpiredException(ExceptionHelper.SUBSCRIPTION_EXPIRED_MSG);

        return response;
    }

    @Override
    public LogoutResponse logout(LogoutRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.logout(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        return process(new LogoutConverter(), () -> api.logout());
    }

    @Override
    public SettingsResponse saveSettings(SettingsRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.saveSettings(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        switch (request.getType()) {
            case STREAM_SERVER:
                return process(new SettingsConverter(), () -> api.saveSettings("stream_server", request.getNewValue()), () -> login(reLoginRequest));
            case TIME_SHIFT:
                return process(new SettingsConverter(), () -> api.saveSettings("timeshift", request.getNewValue()), () -> login(reLoginRequest));
            case TIME_ZONE:
                return new SettingsResponse();
            case PARENTAL_PASSWORD:
                return process(new SettingsConverter(), () -> api.saveSettingsParentalPass("pcode", request.getOldValue(), request.getNewValue(), request.getNewValue()), () -> login(reLoginRequest));
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

        return process(new ChannelsConverter(), () -> api.getChannels(1), () -> login(reLoginRequest));
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

        EpgsRetrofitResponse response1 = null;
        if (previousDay != null)
            response1 = process(new EpgsConverter(), () -> api.getEpgs(cid, previousDay), () -> login(reLoginRequest));

        EpgsRetrofitResponse response2 = process(new EpgsConverter(), () -> api.getEpgs(cid, currentDay), () -> login(reLoginRequest));

        EpgsRetrofitResponse response3 = null;
        if (nextDay != null)
            response3 = process(new EpgsConverter(), () -> api.getEpgs(cid, nextDay), () -> login(reLoginRequest));

        return new UnionEpgsConverter(request.getFromBeginTime()).convert(response1, response2, response3);
    }

    @Override
    public CurrentEpgsResponse getCurrentEpgs(CurrentEpgsRequest request) throws IptvException {
        Log.d(Tag.API, "Polbox.getCurrentEpgs(" + request + ")");

        if (!ValidatorBean.validate(request) || request.getChannelIds().isEmpty())
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        return process(new CurrentEpgsConverter(), () -> api.getCurrentEpgs(Joiner.on(",").join(request.getChannelIds()), 3, 1), () -> login(reLoginRequest));
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

        switch (request.getType()) {
            case LIVE_EPG:
                response = process(new UrlConverter(), () -> api.getLiveUrl(request.getChannelId(), request.getProtectCode()), () -> login(reLoginRequest));
                break;
            case ARCHIVE_EPG:
                response = process(new UrlConverter(), () -> api.getArchiveUrl(request.getChannelId(), request.getSeekToTime(), request.getProtectCode()), () -> login(reLoginRequest));
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
