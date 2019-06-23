package com.kristurek.polskatv.iptv.polskatelewizjausa.validator;

import com.kristurek.polskatv.iptv.core.dto.ChannelsRequest;
import com.kristurek.polskatv.iptv.core.dto.CurrentEpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.EpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.LoginRequest;
import com.kristurek.polskatv.iptv.core.dto.LogoutRequest;
import com.kristurek.polskatv.iptv.core.dto.SettingsRequest;
import com.kristurek.polskatv.iptv.core.dto.SimilarEpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.UrlRequest;
import com.kristurek.polskatv.iptv.core.dto.common.enumeration.EpgType;

public class ValidatorBean {

    public static boolean validate(ChannelsRequest dto) {
        return dto != null;
    }

    public static boolean validate(CurrentEpgsRequest dto) {
        if (dto == null)
            return false;

        return dto.getChannelIds() != null && !dto.getChannelIds().isEmpty() && !dto.getChannelIds().contains(0);
    }

    public static boolean validate(EpgsRequest dto) {
        if (dto == null)
            return false;
        if (dto.getChannelIds() == null || dto.getChannelIds().isEmpty() || dto.getChannelIds().contains(0))
            return false;

        return dto.getFromBeginTime() != 0;
    }

    public static boolean validate(LoginRequest dto) {
        if (dto == null)
            return false;
        if (dto.getLogin() == null || dto.getLogin().trim().length() == 0)
            return false;

        return dto.getPass() != null && dto.getPass().trim().length() != 0;
    }

    public static boolean validate(LogoutRequest dto) {
        return dto != null;
    }

    public static boolean validate(SettingsRequest dto) {
        if (dto == null)
            return false;
        if (dto.getType() == null)
            return false;
        if (dto.getOldValue() == null || dto.getOldValue().trim().length() == 0)
            return false;

        return dto.getNewValue() != null && dto.getNewValue().trim().length() != 0;
    }

    public static boolean validate(SimilarEpgsRequest dto) {
        if (dto == null)
            return false;
        if (dto.getTitle() == null || dto.getTitle().trim().length() == 0)
            return false;

        return dto.getChannelIds() != null && !dto.getChannelIds().isEmpty() && !dto.getChannelIds().contains(0);
    }

    public static boolean validate(UrlRequest dto) {
        if (dto == null)
            return false;
        if (dto.getType() == null || dto.getType().equals(EpgType.NOT_AVAILABLE))
            return false;
        if (dto.getType().equals(EpgType.LIVE_EPG))
            if (dto.getProtectCode() == null || dto.getProtectCode().trim().length() == 0 || dto.getChannelId() == 0 && dto.getSeekToTime() != 0)
                return false;
        if (dto.getType().equals(EpgType.ARCHIVE_EPG))
            return dto.getProtectCode() != null && dto.getProtectCode().trim().length() != 0 && dto.getChannelId() != 0 && dto.getSeekToTime() != 0;

        return true;
    }
}
