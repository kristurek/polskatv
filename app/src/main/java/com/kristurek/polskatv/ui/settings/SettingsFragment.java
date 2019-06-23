package com.kristurek.polskatv.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.kristurek.polskatv.BuildConfig;
import com.kristurek.polskatv.PolskaTvApplication;
import com.kristurek.polskatv.R;
import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.service.DiagnosticService;
import com.kristurek.polskatv.service.LoggerService;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.service.RemoteServerService;
import com.kristurek.polskatv.ui.settings.interactor.PersistSettingsInteractor;
import com.kristurek.polskatv.ui.settings.interactor.GenerateAndUploadLogsInteractor;
import com.kristurek.polskatv.ui.view.XTimeDialogPreference;
import com.kristurek.polskatv.ui.view.XTimePreferenceDialogFragmentCompat;
import com.kristurek.polskatv.util.DateTimeHelper;
import com.kristurek.polskatv.util.FontHelper;
import com.kristurek.polskatv.util.Tag;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_LANGUAGE;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_MEDIA_SERVERS;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_MEDIA_SERVER_ID;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_PARENTAL_PASSWORD;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_PASSWORD;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_REST_OF_DAY;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_SUBSCRIPTION;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_TIME_SHIFT;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_TIME_ZONE;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.APPLICATION_CUSTOM_TIME_TO_FIND_DEFAULT_POSITION_IN_EPGS;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.APPLICATION_FONT_SIZE;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.APPLICATION_HIDDEN_CHANNELS;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.APPLICATION_SEARCH_CHANNELS;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.APPLICATION_TIME_ZONE;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.APPLICATION_UPLOAD_LOGS;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.APPLICATION_VERSION;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.PLAYER_BACKWARD_MOVE;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.PLAYER_FAST_BACKWARD_MOVE;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.PLAYER_FAST_FORWARD_MOVE;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.PLAYER_FORWARD_MOVE;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    @Inject
    public PreferencesService prefService;
    @Inject
    public IptvService iptvService;
    @Inject
    public RemoteServerService remoteService;
    @Inject
    public LoggerService logService;
    @Inject
    public DiagnosticService diagService;
    @Inject
    public Context context;

    private final CompositeDisposable disposables = new CompositeDisposable();

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        disposables.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateSummary();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        PolskaTvApplication.getComponent().inject(this);

        setPreferencesFromResource(R.xml.settings, rootKey);

        Preference uploadLogsPref = findPreference(APPLICATION_UPLOAD_LOGS.getValue());
        uploadLogsPref.setOnPreferenceClickListener(preference -> uploadLogs());

        EditTextPreference accountParentalPasswordPref = (EditTextPreference) findPreference(ACCOUNT_PARENTAL_PASSWORD.getValue());
        accountParentalPasswordPref.setOnPreferenceChangeListener(this);

        ListPreference accountLanguagePref = (ListPreference) findPreference(ACCOUNT_LANGUAGE.getValue());
        accountLanguagePref.setEntries(new String[]{"PL", "EN", "DE", "RU"});
        accountLanguagePref.setEntryValues(new String[]{"pl", "en", "de", "ru"});
        accountLanguagePref.setOnPreferenceChangeListener(this);

        ListPreference accountMediaServerPref = (ListPreference) findPreference(ACCOUNT_MEDIA_SERVER_ID.getValue());
        Map<Integer, String> mediaServers = prefService.get(ACCOUNT_MEDIA_SERVERS, new LinkedHashMap<>());
        List<CharSequence> entries = new ArrayList<>();
        List<CharSequence> entryValues = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : mediaServers.entrySet()) {
            entries.add(entry.getValue());
            entryValues.add(entry.getKey().toString());
        }
        accountMediaServerPref.setEntries(entries.toArray(new CharSequence[0]));
        accountMediaServerPref.setEntryValues(entryValues.toArray(new CharSequence[0]));
        accountMediaServerPref.setOnPreferenceChangeListener(this);

        EditTextPreference accountTimeShiftPref = (EditTextPreference) findPreference(ACCOUNT_TIME_SHIFT.getValue());
        accountTimeShiftPref.setOnPreferenceChangeListener(this);

        EditTextPreference accountTimeZonePref = (EditTextPreference) findPreference(ACCOUNT_TIME_ZONE.getValue());
        accountTimeZonePref.setOnPreferenceChangeListener(this);

        ListPreference playerForwardPref = (ListPreference) findPreference(PLAYER_FORWARD_MOVE.getValue());
        playerForwardPref.setEntries(new String[]{"1", "2", "3", "4", "5"});
        playerForwardPref.setEntryValues(new String[]{"1", "2", "3", "4", "5"});

        ListPreference playerFastForwardPref = (ListPreference) findPreference(PLAYER_FAST_FORWARD_MOVE.getValue());
        playerFastForwardPref.setEntries(new String[]{"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"});
        playerFastForwardPref.setEntryValues(new String[]{"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"});

        ListPreference playerBackwardPref = (ListPreference) findPreference(PLAYER_BACKWARD_MOVE.getValue());
        playerBackwardPref.setEntries(new String[]{"1", "2", "3", "4", "5"});
        playerBackwardPref.setEntryValues(new String[]{"1", "2", "3", "4", "5"});

        ListPreference playerFastBackwardPref = (ListPreference) findPreference(PLAYER_FAST_BACKWARD_MOVE.getValue());
        playerFastBackwardPref.setEntries(new String[]{"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"});
        playerFastBackwardPref.setEntryValues(new String[]{"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"});

        ListPreference applicationTimeZonePref = (ListPreference) findPreference(APPLICATION_TIME_ZONE.getValue());
        applicationTimeZonePref.setEntries(DateTimeHelper.TIME_ZONE_IDS);
        applicationTimeZonePref.setEntryValues(DateTimeHelper.TIME_ZONE_IDS);
        applicationTimeZonePref.setOnPreferenceChangeListener(this);

        ListPreference applicationFontSizePref = (ListPreference) findPreference(APPLICATION_FONT_SIZE.getValue());
        applicationFontSizePref.setEntries(FontHelper.SIZES.names());
        applicationFontSizePref.setEntryValues(FontHelper.SIZES.names());
        applicationFontSizePref.setOnPreferenceChangeListener(this);

        LinkedHashMap<Integer, String> allChannels = (LinkedHashMap<Integer, String>) prefService.get(PreferencesService.KEYS.APPLICATION_ALL_CHANNELS, new LinkedHashMap<>());
        List<CharSequence> entriesAllChannels = new ArrayList<>();
        List<CharSequence> entryValuesAllChannels = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : allChannels.entrySet()) {
            entriesAllChannels.add(entry.getValue());
            entryValuesAllChannels.add(String.valueOf(entry.getKey()));
        }

        MultiSelectListPreference hiddenChannelsPref = (MultiSelectListPreference) findPreference(APPLICATION_HIDDEN_CHANNELS.getValue());
        hiddenChannelsPref.setEntries(entriesAllChannels.toArray(new CharSequence[entriesAllChannels.size()]));
        hiddenChannelsPref.setEntryValues(entryValuesAllChannels.toArray(new CharSequence[entryValuesAllChannels.size()]));

        MultiSelectListPreference searchChannelsPref = (MultiSelectListPreference) findPreference(APPLICATION_SEARCH_CHANNELS.getValue());
        searchChannelsPref.setEntries(entriesAllChannels.toArray(new CharSequence[entriesAllChannels.size()]));
        searchChannelsPref.setEntryValues(entryValuesAllChannels.toArray(new CharSequence[entryValuesAllChannels.size()]));

        updateSummary();
    }

    private void updateSummary() {

        Preference versionPref = findPreference(APPLICATION_VERSION.getValue());
        versionPref.setSummary(BuildConfig.VERSION_NAME);

        Preference subscriptionPref = findPreference(ACCOUNT_SUBSCRIPTION.getValue());
        subscriptionPref.setSummary(prefService.get(ACCOUNT_SUBSCRIPTION, "<No saved>"));

        Preference passwordPref = findPreference(ACCOUNT_PASSWORD.getValue());
        passwordPref.setSummary(prefService.get(ACCOUNT_PASSWORD, "<No saved>"));

        Preference restOfDayPref = findPreference(ACCOUNT_REST_OF_DAY.getValue());
        restOfDayPref.setSummary(prefService.get(ACCOUNT_REST_OF_DAY, 0) + getResources().getString(R.string.rest_of_day_summary_settings));

        EditTextPreference parentalPassPref = (EditTextPreference) findPreference(ACCOUNT_PARENTAL_PASSWORD.getValue());
        parentalPassPref.setSummary(prefService.get(ACCOUNT_PARENTAL_PASSWORD, "<No saved>"));

        ListPreference languagePref = (ListPreference) findPreference(ACCOUNT_LANGUAGE.getValue());
        languagePref.setSummary(prefService.get(ACCOUNT_LANGUAGE, "<No saved>"));

        ListPreference mediaServersPref = (ListPreference) findPreference(ACCOUNT_MEDIA_SERVER_ID.getValue());
        mediaServersPref.setSummary(mediaServersPref.getEntry());

        EditTextPreference timeShiftPref = (EditTextPreference) findPreference(ACCOUNT_TIME_SHIFT.getValue());
        timeShiftPref.setSummary(String.valueOf(prefService.get(ACCOUNT_TIME_SHIFT, 0)));

        EditTextPreference timeZonePref = (EditTextPreference) findPreference(ACCOUNT_TIME_ZONE.getValue());
        timeZonePref.setSummary(String.valueOf(prefService.get(ACCOUNT_TIME_ZONE, 0)));

        ListPreference forwardPref = (ListPreference) findPreference(PLAYER_FORWARD_MOVE.getValue());
        forwardPref.setSummary(prefService.get(PLAYER_FORWARD_MOVE, 0) + getResources().getString(R.string.forward_summary_settings));

        ListPreference fastForwardPref = (ListPreference) findPreference(PLAYER_FAST_FORWARD_MOVE.getValue());
        fastForwardPref.setSummary(prefService.get(PLAYER_FAST_FORWARD_MOVE, 0) + getResources().getString(R.string.fast_forward_summary_settings));

        ListPreference backwardPref = (ListPreference) findPreference(PLAYER_BACKWARD_MOVE.getValue());
        backwardPref.setSummary(prefService.get(PLAYER_BACKWARD_MOVE, 0) + getResources().getString(R.string.backward_summary_settings));

        ListPreference fastBackwardPref = (ListPreference) findPreference(PLAYER_FAST_BACKWARD_MOVE.getValue());
        fastBackwardPref.setSummary(prefService.get(PLAYER_FAST_BACKWARD_MOVE, 0) + getResources().getString(R.string.fast_backward_summary_settings));

        ListPreference applicationTimeZonePref = (ListPreference) findPreference(APPLICATION_TIME_ZONE.getValue());
        applicationTimeZonePref.setSummary(prefService.get(APPLICATION_TIME_ZONE, "<No saved>"));

        ListPreference fontSizePref = (ListPreference) findPreference(APPLICATION_FONT_SIZE.getValue());
        fontSizePref.setSummary(prefService.get(APPLICATION_FONT_SIZE, "<No saved>"));

        XTimeDialogPreference customTimeSizePref = (XTimeDialogPreference) findPreference(APPLICATION_CUSTOM_TIME_TO_FIND_DEFAULT_POSITION_IN_EPGS.getValue());
        customTimeSizePref.setSummary(prefService.get(APPLICATION_CUSTOM_TIME_TO_FIND_DEFAULT_POSITION_IN_EPGS, "17:00"));
    }

    private boolean uploadLogs() {
        disposables.add(new GenerateAndUploadLogsInteractor(context, remoteService, logService, diagService)
                .execute()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), R.string.msg_12, Toast.LENGTH_LONG).show());
                    }
                }, throwable -> {
                    Log.e(Tag.UI, throwable.getMessage(), throwable);
                    if (getActivity() != null)
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show());
                }));
        return true;
    }

    private void persistSetting(Preference preference, PreferencesService.KEYS key, String oldSettingValue, String newSettingValue) {
        disposables.add(new PersistSettingsInteractor(iptvService)
                .execute(key, oldSettingValue, newSettingValue)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (getActivity() != null) {
                        onPreferenceChanged(preference, newSettingValue);
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), R.string.msg_22, Toast.LENGTH_LONG).show());
                    }
                }, throwable -> {
                    Log.e(Tag.UI, throwable.getMessage(), throwable);
                    if (getActivity() != null)
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show());
                }));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (PreferencesService.KEYS.fromString(preference.getKey())) {
            case ACCOUNT_PARENTAL_PASSWORD:
                persistSetting(preference, ACCOUNT_PARENTAL_PASSWORD, ((EditTextPreference) preference).getText(), (String) newValue);
                return false;
            case ACCOUNT_LANGUAGE:
                persistSetting(preference, ACCOUNT_LANGUAGE, ((ListPreference) preference).getValue(), (String) newValue);
                return false;
            case ACCOUNT_MEDIA_SERVER_ID:
                persistSetting(preference, ACCOUNT_MEDIA_SERVER_ID, ((ListPreference) preference).getValue(), String.valueOf(newValue));
                return false;
            case ACCOUNT_TIME_SHIFT:
                persistSetting(preference, ACCOUNT_TIME_SHIFT, ((EditTextPreference) preference).getText(), String.valueOf(newValue));
                return false;
            case ACCOUNT_TIME_ZONE:
                persistSetting(preference, ACCOUNT_TIME_ZONE, ((EditTextPreference) preference).getText(), String.valueOf(newValue));
                return false;
            case APPLICATION_FONT_SIZE:
                FontHelper.setFontSize((String) newValue);
                break;
            case APPLICATION_TIME_ZONE:
                DateTimeHelper.setSelectedTimeZoneId((String) newValue);
                break;
            default:
                break;
        }

        return true;
    }

    private void onPreferenceChanged(Preference preference, Object newValue) {
        if (preference instanceof EditTextPreference)
            ((EditTextPreference) preference).setText((String) newValue);
        else if (preference instanceof ListPreference)
            ((ListPreference) preference).setValue((String) newValue);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if (preference instanceof XTimeDialogPreference) {
            dialogFragment = new XTimePreferenceDialogFragmentCompat();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", preference.getKey());
            dialogFragment.setArguments(bundle);
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
