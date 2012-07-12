package com.mfcoding.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	static final String TAG = "UpdaterService";
	boolean running = false;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		running = false;
		Log.d(TAG, "onDestroy");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		running = true;
		new Thread() {
			public void run() {
				try {
					while (running) {
						((YambaApp) getApplication()).pullAndInsert();
						int delay = Integer.parseInt(((YambaApp) getApplication()).getDelay());
						Thread.sleep(delay * 1000);
					}
				} catch (NumberFormatException e) {
					Log.d(TAG, "Updater NumberFormatException", e);
				} catch (InterruptedException e) {
					Log.d(TAG, "Updater InterruptedException", e);
				}

			}
		}.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
