package com.kristurek.polskatv.iptv.core;

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
import com.kristurek.polskatv.iptv.core.exception.IptvException;

public interface IptvService {

    LoginResponse login(LoginRequest request) throws IptvException;

    LogoutResponse logout(LogoutRequest request) throws IptvException;

    SettingsResponse saveSettings(SettingsRequest request) throws IptvException;

    ChannelsResponse getChannels(ChannelsRequest request) throws IptvException;

    EpgsResponse getEpgs(EpgsRequest request) throws IptvException;

    CurrentEpgsResponse getCurrentEpgs(CurrentEpgsRequest request) throws IptvException;

    UrlResponse getUrl(UrlRequest request) throws IptvException;

    SimilarEpgsResponse getSimilarEpgs(SimilarEpgsRequest request) throws IptvException;
}
