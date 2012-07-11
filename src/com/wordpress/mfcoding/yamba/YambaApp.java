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
		PreferenceManager.setDefaultValues(this, R.xml.prefs, true);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		prefs.registerOnSharedPreferenceChangeListener(this);

		getDelayFromPrefs();
		
		statusData = new StatusData(this);
	}

	public Twitter getTwitter() {
		if (twitter == null) {
			//username = prefs.getString("username", "student");
			//password = prefs.getString("password", "password");
			//url = prefs.getString("serverURL", "http:\\\\yamba.marakana.com\\api");
			username = prefs.getString("username", "");
			password = prefs.getString("password", "");
			url = prefs.getString("serverURL", "");
			Log.d(TAG, String.format("%s/%s@%s", username, password, url));

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
		//delay = prefs.getString("delaylist", "30");
		delay = prefs.getString("interval", "1");
	}

	public String getDelay() {
		return delay;
	}

}
