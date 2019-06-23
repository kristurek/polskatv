package com.kristurek.polskatv.ui.settings.interactor;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.SettingsRequest;
import com.kristurek.polskatv.iptv.core.dto.common.enumeration.SettingType;
import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.ArrayParamAbstractInteractor;
import com.kristurek.polskatv.util.Tag;

public class PersistSettingsInteractor extends ArrayParamAbstractInteractor<Boolean, Object> {

    private IptvService iptvService;

    public PersistSettingsInteractor(IptvService iptvService) {
        this.iptvService = iptvService;
    }

    @Override
    protected Boolean process(Object... param) throws IptvException {
        Log.d(Tag.UI, "PersistSettingsInteractor.process()[" + param + "]");

        PreferencesService.KEYS key = (PreferencesService.KEYS) param[0];
        String oldValue = (String) param[1];
        String newValue = (String) param[2];
        SettingType type;

        switch (key) {
            case ACCOUNT_PARENTAL_PASSWORD:
                type = SettingType.PARENTAL_PASSWORD;
                break;
            case ACCOUNT_LANGUAGE:
                type = SettingType.LANGUAGE;
                break;
            case ACCOUNT_MEDIA_SERVER_ID:
                type = SettingType.STREAM_SERVER;
                break;
            case ACCOUNT_TIME_SHIFT:
                type = SettingType.TIME_SHIFT;
                break;
            case ACCOUNT_TIME_ZONE:
                type = SettingType.TIME_ZONE;
                break;
            default:
                type = null;
        }

        SettingsRequest request = new SettingsRequest();
        request.setType(type);
        request.setOldValue(oldValue);
        request.setNewValue(newValue);

        iptvService.saveSettings(request);

        return true;
    }
}
