package com.mfcoding.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	static final String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		long interval = Long.parseLong(PreferenceManager.getDefaultSharedPreferences(context)
				.getString("interval", "900000"));
		PendingIntent operation = PendingIntent.getService(context, -1,
				new Intent(YambaApp.ACTION_REFRESH),
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				System.currentTimeMillis(), interval, operation);
		// context.startService(new Intent(context, UpdaterService.class));
		Log.d(TAG, "onReceive interval:"+interval);

	}
}
