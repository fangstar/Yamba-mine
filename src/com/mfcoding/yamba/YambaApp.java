package com.mfcoding.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApp extends Application implements
		OnSharedPreferenceChangeListener {

	static final String TAG = "YambaApp";
	public static final String ACTION_NEW_STATUS = "com.mfcoding.yamba.NEW_STATUS";
	public static final String ACTION_REFRESH = "com.mfcoding.yamba.REFRESH";
	public static final String ACTION_REFRESH_ALARM = "com.mfcoding.yamba.REFRESHALARM";
	Twitter twitter;
	SharedPreferences prefs;
	String username, password, url;
	String delay;
	//StatusData statusData;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");

		// Pref
		PreferenceManager.setDefaultValues(this, R.xml.prefs, true);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		prefs.registerOnSharedPreferenceChangeListener(this);

		getDelayFromPrefs();

		//statusData = new StatusData(this);
	}

	public Twitter getTwitter() {
		if (twitter == null) {
			// username = prefs.getString("username", "student");
			// password = prefs.getString("password", "password");
			// url = prefs.getString("serverURL",
			// "http:\\\\yamba.marakana.com\\api");
			username = prefs.getString("username", "");
			password = prefs.getString("password", "");
			url = prefs.getString("serverURL", "");
			Log.d(TAG, String.format("Real Login info: %s/%s@%s", username,
					password, url));
			// url = "http:\\\\yamba.marakana.com\\api";
			// url = "http://yamba.marakana.com/api";
			// Log.d(TAG, String.format("%s/%s@%s", username, password, url));

			// Twitter
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(url);
		}
		return twitter;
	}

	static final Intent refreshIntent = new Intent("ACTION_REFRESH_ALARM");

	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		twitter = null;
		// getDelayFromPrefs();
		sendBroadcast(refreshIntent);
		Log.d(TAG, "onSharedPreferenceChanged for key:" + key);
	}

	void getDelayFromPrefs() {
		// delay = prefs.getString("delaytime", "30");
		// delay = prefs.getString("delaylist", "30");
		delay = prefs.getString("interval", "1");
		delay = "5";
	}

	public String getDelay() {
		return delay;
	}

	long lastTimestampSeen = -1;

	public int pullAndInsert() {
		int count = 0;
		long lastTimestampSeen = -1;
		try {
			List<Status> timeline = getTwitter().getPublicTimeline();
			for (Status status : timeline) {
				//statusData.insert(status);
				getContentResolver().insert(StatusProvider.CONTENT_URI,
						StatusProvider.statusToValues(status));
				
				Log.d(TAG, String.format("%s: %s", status.user.name,
						status.text));
				if (status.createdAt.getTime()>lastTimestampSeen) {
					count++;
					lastTimestampSeen = status.createdAt.getTime();
				}
			}
		} catch (TwitterException e) {
			//e.printStackTrace();
			Log.e(TAG, "Failed to pull timeline",e);
		}		
		if (count > 0) {
			sendBroadcast(new Intent(ACTION_NEW_STATUS).putExtra("count", count));
		}
		this.lastTimestampSeen = lastTimestampSeen;
		return count;
	}}
