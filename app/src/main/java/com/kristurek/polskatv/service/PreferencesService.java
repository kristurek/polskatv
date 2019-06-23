package com.kristurek.polskatv.service;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public interface PreferencesService {

    enum KEYS {
        ACCOUNT_SUBSCRIPTION("__ACCOUNT_SUBSCRIPTION__"),
        ACCOUNT_PASSWORD("__ACCOUNT_PASSWORD__"),
        ACCOUNT_PARENTAL_PASSWORD("__ACCOUNT_PARENTAL_PASS__"),
        ACCOUNT_MEDIA_SERVER_ID("__ACCOUNT_MEDIA_SERVER_ID__"),
        ACCOUNT_MEDIA_SERVERS("__ACCOUNT_MEDIA_SERVERS__"),
        ACCOUNT_TIME_SHIFT("__ACCOUNT_TIME_SHIFT__"),
        ACCOUNT_TIME_ZONE("__ACCOUNT_TIME_ZONE__"),
        ACCOUNT_REST_OF_DAY("__ACCOUNT_REST_OF_DAY__"),
        ACCOUNT_LANGUAGE("__ACCOUNT_LANGUAGE__"),

        API_PROVIDER_ID("__API_PROVIDER_ID__"),

        PLAYER_VOLUME("__PLAYER_VOLUME__"),
        PLAYER_FORWARD_MOVE("__PLAYER_FORWARD_MOVE__"),
        PLAYER_FAST_FORWARD_MOVE("__PLAYER_FAST_FORWARD_MOVE__"),
        PLAYER_BACKWARD_MOVE("__PLAYER_BACKWARD_MOVE__"),
        PLAYER_FAST_BACKWARD_MOVE("__PLAYER_FAST_BACKWARD_MOVE__"),

        APPLICATION_VERSION("__APPLICATION_VERSION__"),
        APPLICATION_UPLOAD_LOGS("__APPLICATION_UPLOAD_LOGS__"),
        APPLICATION_TIME_ZONE("__APPLICATION_TIME_ZONE__"),
        APPLICATION_FONT_SIZE("__APPLICATION_FONT_SIZE__"),
        APPLICATION_HIDDEN_CHANNELS("__APPLICATION_HIDDEN_CHANNELS__"),
        APPLICATION_SEARCH_CHANNELS("__APPLICATION_SEARCH_CHANNELS__"),
        APPLICATION_ALL_CHANNELS("__APPLICATION_ALL_CHANNELS__"),
        APPLICATION_USE_CUSTOM_TIME_AND_PARTIAL_TIME_ZONE_IN_EPGS("__APPLICATION_USE_CUSTOM_TIME_AND_PARTIAL_TIME_ZONE_IN_EPGS__"),
        APPLICATION_CUSTOM_TIME_TO_FIND_DEFAULT_POSITION_IN_EPGS("__APPLICATION_CUSTOM_TIME_TO_FIND_DEFAULT_POSITION_IN_EPGS__"),
        APPLICATION_SHOW_DEVICE_TIME("__APPLICATION_SHOW_DEVICE_TIME__"),
        APPLICATION_SHOW_VOLUME("__APPLICATION_SHOW_VOLUME__"),
        APPLICATION_SHOW_CONSOLE("__APPLICATION_SHOW_CONSOLE__"),
        APPLICATION_DEBUG_CONSOLE("__APPLICATION_DEBUG_CONSOLE__"),

        ;

        private String value;

        KEYS(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static KEYS fromString(String text) {
            for (KEYS key : KEYS.values())
                if (key.value.equalsIgnoreCase(text))
                    return key;

            return null;
        }
    }


    void save(KEYS key, String value);

    void save(KEYS key, boolean value);

    void save(KEYS key, int value);

    void save(KEYS key, float value);

    void save(KEYS key, LinkedHashMap<Integer, String> value);

    void save(KEYS key, HashSet<String> value);

    void clear(KEYS key);

    boolean contains(KEYS key);

    String get(KEYS key, String defaultValue);

    boolean get(KEYS key, boolean defaultValue);

    int get(KEYS key, int defaultValue);

    float get(KEYS key, float defaultValue);

    Map<Integer, String> get(KEYS key, LinkedHashMap<Integer, String> defaultValue);

    Set<String> get(KEYS key, HashSet<String> defaultValues);
}
