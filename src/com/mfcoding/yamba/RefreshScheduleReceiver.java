package com.mfcoding.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class RefreshScheduleReceiver extends BroadcastReceiver {

	static final String TAG = "BootReceiver";
	static PendingIntent lastOp;

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Log.d(TAG, "onReceive received ACTION_BOOT_COMPLETED");
		}
		
		long interval = Long.parseLong(PreferenceManager
				.getDefaultSharedPreferences(context).getString("interval",
						"900000"));
		PendingIntent operation = PendingIntent.getService(context, -1,
				new Intent(YambaApp.ACTION_REFRESH),
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		
		if (lastOp != null)
			alarmManager.cancel(lastOp);
		
		if (interval > 0) {
			//alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
			alarmManager.setInexactRepeating(AlarmManager.RTC,
					System.currentTimeMillis(), interval, operation);
			// context.startService(new Intent(context, UpdaterService.class));
		}
		lastOp = operation;
		
		Log.d(TAG, "onReceive interval:" + interval);

	}
}
