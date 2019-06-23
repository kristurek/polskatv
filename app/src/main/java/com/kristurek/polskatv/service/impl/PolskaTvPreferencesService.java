package com.kristurek.polskatv.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kristurek.polskatv.service.PreferencesService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class PolskaTvPreferencesService implements PreferencesService {

    private SharedPreferences prefs;

    public PolskaTvPreferencesService(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void save(KEYS key, String value) {
        final SharedPreferences.Editor editor = prefs.edit();

        editor.putString(key.getValue(), value);
        editor.apply();
    }

    @Override
    public void save(KEYS key, boolean value) {
        final SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(key.getValue(), value);
        editor.apply();
    }

    @Override
    public void save(KEYS key, int value) {
        final SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(key.getValue(), value);
        editor.apply();
    }

    @Override
    public void save(KEYS key, float value) {
        final SharedPreferences.Editor editor = prefs.edit();

        editor.putFloat(key.getValue(), value);
        editor.apply();
    }

    @Override
    public void save(KEYS key, LinkedHashMap<Integer, String> value) {
        try {
            JSONArray arr = new JSONArray();
            for (Integer index : value.keySet()) {
                JSONObject json = new JSONObject();

                json.put("id", index);
                json.put("name", value.get(index));
                arr.put(json);
            }
            final SharedPreferences.Editor editor = prefs.edit();

            editor.putString(key.getValue(), arr.toString());
            editor.apply();
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void save(KEYS key, HashSet<String> value) {
        final SharedPreferences.Editor editor = prefs.edit();

        editor.putStringSet(key.getValue(), value);
        editor.apply();
    }

    @Override
    public void clear(KEYS key) {
        final SharedPreferences.Editor editor = prefs.edit();

        editor.remove(key.getValue());
        editor.apply();
    }

    @Override
    public boolean contains(KEYS key) {
        return prefs.contains(key.getValue());
    }

    @Override
    public String get(KEYS key, String defaultValue) {
        return prefs.getString(key.getValue(), defaultValue);
    }

    @Override
    public boolean get(KEYS key, boolean defaultValue) {
        return prefs.getBoolean(key.getValue(), defaultValue);
    }

    @Override
    public int get(KEYS key, int defaultValue) {
        return prefs.getInt(key.getValue(), defaultValue);
    }

    @Override
    public float get(KEYS key, float defaultValue) {
        return prefs.getFloat(key.getValue(), defaultValue);
    }

    @Override
    public Map<Integer, String> get(KEYS key, LinkedHashMap<Integer, String> defaultValue) {
        try {
            String data = prefs.getString(key.getValue(), null);
            if (data == null)
                return defaultValue;

            LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
            JSONArray arr = new JSONArray(data);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject json = arr.getJSONObject(i);
                map.put(json.getInt("id"), json.getString("name"));
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Set<String> get(KEYS key, HashSet<String> defaultValue) {
        return prefs.getStringSet(key.getValue(), defaultValue);
    }
}
