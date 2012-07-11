package com.wordpress.mfcoding.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RefreshService extends IntentService {

	static final String TAG = "RefreshService";

	public RefreshService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
	}

	
	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.d(TAG, "onHandleIntent");
		StatusData statusData = ((YambaApp) getApplication()).statusData;
		try {
			List<Status> timeline = ((YambaApp) getApplication()).getTwitter().getPublicTimeline();
			for (Status status : timeline) {
				statusData.insert(status);
				Log.d(TAG, String.format("%s: %s", status.user.name,
						status.text));
			}
		} catch (TwitterException e) {
			//e.printStackTrace();
			Log.e(TAG, "TwitterException", e);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}
	
	
}
