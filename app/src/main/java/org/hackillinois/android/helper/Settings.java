package org.hackillinois.android.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.annimon.stream.Optional;

import org.joda.time.DateTime;


public class Settings {
	public static final DateTime EVENT_START_TIME = new DateTime("2018-02-23T16:00:00-0600"); //  = 2018-02-23T22:00:00+0000
	public static final DateTime HACKING_START_TIME = new DateTime("2018-02-23T23:00:00-0600"); //  = 2018-02-24T07:00:00+0000
	public static final DateTime HACKING_END_TIME = new DateTime("2018-02-25T11:00:00-0600"); //  = 2018-02-25T17:00:00+0000
	public static final DateTime EVENT_END_TIME = new DateTime("2018-02-25T17:00:00-0600"); //  = 2018-02-25T23:00:00+0000
	private static final String PREFS_NAME = "AppPrefs";
	private static final String AUTH_PREF = "AUTH";
	private static final String LAST_TIME_AUTH_PREF = "LAST_TIME_AUTH";
	private static final String HACKER_PREF = "HACKER";
	private static Settings INSTANCE;

	private final SharedPreferences prefs;

	private Settings(Context context) {
		prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	public static void initialize(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new Settings(context);
		}
	}

	public static Settings get() {
		return INSTANCE;
	}

	public void saveAuthKey(String auth) {
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putString(AUTH_PREF, auth);
		saveLastAuth();
		prefsEditor.apply();
	}

	public Optional<String> getAuthKey() {
		return Optional.ofNullable(prefs.getString(AUTH_PREF, null));
	}

	public Optional<DateTime> getLastAuth() {
		if (!prefs.contains(LAST_TIME_AUTH_PREF)) {
			return Optional.empty();
		}

		return Optional.of(new DateTime(prefs.getString(LAST_TIME_AUTH_PREF, "")));
	}

	private void saveLastAuth() {
		DateTime now = DateTime.now();
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(LAST_TIME_AUTH_PREF, now.toString()).apply();
		edit.apply();
	}

	public String getAuthString() {
		String authKey = prefs.getString(AUTH_PREF, null);
		if (getIsHacker()) {
			return "Bearer " + authKey;
		} else {
			return "Basic " + authKey;
		}
	}

	public void saveIsHacker(boolean isHacker) {
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putBoolean(HACKER_PREF, isHacker);
		prefsEditor.apply();
	}

	public boolean getIsHacker() {
		return prefs.getBoolean(HACKER_PREF, false);
	}

	public void clear() {
		prefs.edit().clear().apply();
	}
}
