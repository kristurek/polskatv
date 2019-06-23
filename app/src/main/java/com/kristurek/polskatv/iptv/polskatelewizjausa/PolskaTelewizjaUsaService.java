package com.kristurek.polskatv.iptv.polskatelewizjausa;


import android.util.Log;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
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
import com.kristurek.polskatv.iptv.polskatelewizjausa.converter.ChannelsConverter;
import com.kristurek.polskatv.iptv.polskatelewizjausa.converter.CurrentEpgsConverter;
import com.kristurek.polskatv.iptv.polskatelewizjausa.converter.EpgsConverter;
import com.kristurek.polskatv.iptv.polskatelewizjausa.converter.LoginConverter;
import com.kristurek.polskatv.iptv.polskatelewizjausa.converter.LogoutConverter;
import com.kristurek.polskatv.iptv.polskatelewizjausa.converter.SettingsConverter;
import com.kristurek.polskatv.iptv.polskatelewizjausa.converter.UrlConverter;
import com.kristurek.polskatv.iptv.polskatelewizjausa.service.PolskaTelewizjaUsaApi;
import com.kristurek.polskatv.iptv.polskatelewizjausa.util.ExceptionHelper;
import com.kristurek.polskatv.iptv.polskatelewizjausa.util.Md5HashGenerator;
import com.kristurek.polskatv.iptv.polskatelewizjausa.validator.ValidatorBean;
import com.kristurek.polskatv.iptv.util.Tag;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class PolskaTelewizjaUsaService extends BasePolskaTelewizjaUsaService implements IptvService {

    private PolskaTelewizjaUsaApi api;

    private LoginRequest reLoginRequest;

    @Inject
    public PolskaTelewizjaUsaService(PolskaTelewizjaUsaApi api) {
        this.api = api;
    }

    private void persistRequest(LoginRequest request) {
        reLoginRequest = request;
    }

    @Override
    public LoginResponse login(LoginRequest request) throws IptvException {
        Log.d(Tag.API, "PolskaTelewizjaUsaService.login(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        persistRequest(request);

        LoginResponse response = process(new LoginConverter(), () -> api.login(request.getLogin(), Md5HashGenerator.md5(Md5HashGenerator.md5(request.getLogin()) + Md5HashGenerator.md5(request.getPass())), 1, 1));

        if (response.getRestOfDay() == 0)
            throw new IptvSubscriptionExpiredException(ExceptionHelper.SUBSCRIPTION_EXPIRED_MSG);

        return response;
    }

    @Override
    public LogoutResponse logout(LogoutRequest request) throws IptvException {
        Log.d(Tag.API, "PolskaTelewizjaUsaService.logout(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        return process(new LogoutConverter(), () -> api.logout());
    }

    @Override
    public SettingsResponse saveSettings(SettingsRequest request) throws IptvException {
        Log.d(Tag.API, "PolskaTelewizjaUsaService.saveSettings(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        switch (request.getType()) {
            case STREAM_SERVER:
                return process(new SettingsConverter(), () -> api.saveSettings("media_server_id", request.getNewValue()), () -> login(reLoginRequest));
            case TIME_SHIFT:
                return process(new SettingsConverter(), () -> api.saveSettings("time_shift", request.getNewValue()), () -> login(reLoginRequest));
            case TIME_ZONE:
                return process(new SettingsConverter(), () -> api.saveSettings("time_zone", request.getNewValue()), () -> login(reLoginRequest));
            case PARENTAL_PASSWORD:
                return process(new SettingsConverter(), () -> api.saveSettings("parental_pass", request.getNewValue(), request.getOldValue()), () -> login(reLoginRequest));
            case LANGUAGE:
                return process(new SettingsConverter(), () -> api.saveSettings("interface_lng", request.getNewValue()), () -> login(reLoginRequest));
            default:
                throw new IptvException(ExceptionHelper.UNSUPPORTED_SETTINGS_TYPE_MSG);
        }
    }

    @Override
    public ChannelsResponse getChannels(ChannelsRequest request) throws IptvException {
        Log.d(Tag.API, "PolskaTelewizjaUsaService.getChannels(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        return process(new ChannelsConverter(), () -> api.getChannels(1, 0, 1), () -> login(reLoginRequest));
    }

    @Override
    public EpgsResponse getEpgs(EpgsRequest request) throws IptvException {
        Log.d(Tag.API, "PolskaTelewizjaUsaService.getEpgs(" + request + ")");

        if (!ValidatorBean.validate(request) || request.getChannelIds().isEmpty())
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        return process(new EpgsConverter(request.getFromBeginTime()), () -> api.getEpgs(Joiner.on(",").join(request.getChannelIds()), request.getFromBeginTime(), 24, 0), () -> login(reLoginRequest));
    }

    @Override
    public CurrentEpgsResponse getCurrentEpgs(CurrentEpgsRequest request) throws IptvException {
        Log.d(Tag.API, "PolskaTelewizjaUsaService.getCurrentEpgs(" + request + ")");

        if (!ValidatorBean.validate(request) || request.getChannelIds().isEmpty())
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        return process(new CurrentEpgsConverter(), () -> api.getCurrentEpgs(Joiner.on(",").join(request.getChannelIds()), 0), () -> login(reLoginRequest));
    }

    @Override
    public UrlResponse getUrl(UrlRequest request) throws IptvException {
        Log.d(Tag.API, "PolskaTelewizjaUsaService.getUrl(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        switch (request.getType()) {
            case LIVE_EPG:
                return process(new UrlConverter(), () -> api.getLiveUrl(request.getChannelId(), 0, request.getProtectCode()), () -> login(reLoginRequest));
            case ARCHIVE_EPG:
                return process(new UrlConverter(), () -> api.getArchiveUrl(request.getChannelId(), request.getSeekToTime(), request.getProtectCode()), () -> login(reLoginRequest));
            default:
                throw new IptvException(ExceptionHelper.UNSUPPORTED_EPG_TYPE_MSG);
        }
    }

    @Override
    public SimilarEpgsResponse getSimilarEpgs(SimilarEpgsRequest request) throws IptvException {
        Log.d(Tag.API, "PolskaTelewizjaUsaService.getSimilarEpgs(" + request + ")");

        if (!ValidatorBean.validate(request))
            throw new IptvValidatorException(ExceptionHelper.VALIDATOR_MSG);

        long beginArchive = Duration.millis(DateTime.now().withTimeAtStartOfDay().minusDays(13).getMillis()).getStandardSeconds();
        int hours = 336; // liczba dni * 24h czyli (13+1)*24 (13 dni wstecz + dzien dzisiejszy razy 24h)
        List<List<Integer>> dividedChannelsIds = Lists.partition(new ArrayList<>(request.getChannelIds()), 40);

        Log.d(Tag.API, "PolskaTelewizjaUsaService.getSimilarEpgs()[beginArchive=" + beginArchive + ",hours=" + hours + ",channels.size()=" + request.getChannelIds().size() + "]");

        List<Epg> results = new LinkedList<>();
        for (List channels : dividedChannelsIds) {
            EpgsResponse lResults = process(new EpgsConverter(beginArchive), () -> api.getEpgs(Joiner.on(",").join(channels), beginArchive, hours, 0), () -> login(reLoginRequest));
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

        Log.d(Tag.API, "PolskaTelewizjaUsaService.getSimilarEpgs(" + response + ")");
        return response;
    }
}
