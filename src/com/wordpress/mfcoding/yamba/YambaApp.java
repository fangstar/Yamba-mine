package com.wordpress.mfcoding.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApp extends Application implements
		OnSharedPreferenceChangeListener {

	static final String TAG = "YambaApp";
	Twitter twitter;
	SharedPreferences prefs;
	String username, password, url;
	String delay;
	StatusData statusData;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");

		// Pref
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		prefs.registerOnSharedPreferenceChangeListener(this);

		getDelayFromPrefs();
		
		statusData = new StatusData(this);
	}

	public Twitter getTwitter() {
		if (twitter == null) {
			username = prefs.getString("username", "student");
			password = prefs.getString("password", "password");
			url = prefs.getString("serverURL", "http:\\\\yamba.marakana.com\\api");
			// Twitter
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(url);
		}
		return twitter;
	}

	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		Log.d(TAG, "onSharedPreferenceChanged");
		twitter = null;
		getDelayFromPrefs();
	}

	void getDelayFromPrefs() {
		//delay = prefs.getString("delaytime", "30");
		delay = prefs.getString("delaylist", "30");
	}

	public String getDelay() {
		return delay;
	}

}
