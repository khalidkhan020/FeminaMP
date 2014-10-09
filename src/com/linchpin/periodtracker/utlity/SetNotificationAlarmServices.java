package com.linchpin.periodtracker.utlity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SetNotificationAlarmServices extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		int id = APP.GLOBAL().getPreferences().getInt(APP.PREF.ALARM_NITIFICATION_ID.key, 0);

		if (APP.GLOBAL().getPreferences().getBoolean(APP.PREF.NEED_TO_CHANGE.key,false)) {
			if (PeriodTrackerObjectLocator.getInstance().getPregnancyMode()) {
				Utility.cancelAllNotification(context);
			} else {
				Utility.cancelAllNotification(context);
				Utility.setAllNotiifications(context);
			}
		} else {
			if (id != 0) {
				PendingIntent pendingIntent = PendingIntent.getActivity(context, id, new Intent(),
						PendingIntent.FLAG_CANCEL_CURRENT);
				AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
				alarmManager.cancel(pendingIntent);
			}
		}

	}

}
