package com.linchpin.periodtracker.utlity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.view.HomeScreenView;

public class TimeAlarmReciever extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent paramIntent)
	{

		int days = 0;

		try
		{
			//PeriodTrackerObjectLocator.getInstance(context);
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String type = paramIntent.getStringExtra("type");
		int id = paramIntent.getIntExtra("Id", 0);
		long scheduletime = paramIntent.getLongExtra("date", 2);

		Date scheduledate = null, startnotificationdate = null;

		CharSequence charSequence = "dddddddddddd";
		if (type.equals(context.getResources().getString(R.string.pregnancy_alert)))
		{
			
			charSequence = context.getString(R.string.pregnancy);

			if (scheduletime != 2)
			{
				Calendar calendar = new GregorianCalendar().getInstance();
				calendar.setTimeInMillis(scheduletime);

				calendar.add(Calendar.DAY_OF_MONTH, +2);
				calendar.set(Calendar.HOUR_OF_DAY, 8);
				startnotificationdate = calendar.getTime();

				calendar.setTimeInMillis(scheduletime);
				calendar.set(Calendar.HOUR_OF_DAY, 9);
				scheduledate = calendar.getTime();
				int i=new Date().compareTo(startnotificationdate);
				i=new Date().compareTo(scheduledate);
				if (new Date().compareTo(startnotificationdate) <= 0 && new Date().compareTo(scheduledate) >= 0)
				{
					days = Utility.leftdaysInPeriod(Utility.setHourMinuteSecondZero(new Date()), scheduledate);
					showNotification(context, type, charSequence, days);
				}
				else
				{
					
					PendingIntent pendingIntent = PendingIntent.getActivity(context, id, paramIntent, PendingIntent.FLAG_CANCEL_CURRENT);
					AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
					alarmManager.cancel(pendingIntent);

				}

			}

		}else
		if (type.equals(context.getResources().getString(R.string.period_alert)))
		{
			charSequence = context.getString(R.string.period);
		

			if (scheduletime != 2)
			{
				Calendar calendar = new GregorianCalendar().getInstance();
				calendar.setTimeInMillis(scheduletime);

				calendar.add(Calendar.DAY_OF_MONTH, -PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification());
				calendar.set(Calendar.HOUR_OF_DAY, 8);
				startnotificationdate = calendar.getTime();

				calendar.setTimeInMillis(scheduletime);
				calendar.set(Calendar.HOUR_OF_DAY, 9);
				scheduledate = calendar.getTime();

				if (new Date().compareTo(startnotificationdate) >= 0 && new Date().compareTo(scheduledate) <= 0)
				{
					days = Utility.leftdaysInPeriod(Utility.setHourMinuteSecondZero(new Date()), scheduledate);
					showNotification(context, type, charSequence, days);
				}
				else
				{
						
				PendingIntent pendingIntent = PendingIntent.getActivity(context, id, paramIntent, PendingIntent.FLAG_CANCEL_CURRENT);
					AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
					alarmManager.cancel(pendingIntent);

				}

			}

		}
		else if (type.equals(context.getResources().getString(R.string.fertile_alert)))
		{
			charSequence = context.getString(R.string.fertile);

			if (scheduletime != 2)
			{
				Calendar calendar = new GregorianCalendar().getInstance();
				calendar.setTimeInMillis(scheduletime);

				calendar.add(Calendar.DAY_OF_MONTH, -PeriodTrackerObjectLocator.getInstance().getFertilityNotification());
				calendar.set(Calendar.HOUR_OF_DAY, 8);
				startnotificationdate = calendar.getTime();

				calendar.setTimeInMillis(scheduletime);
				calendar.set(Calendar.HOUR_OF_DAY, 9);
				scheduledate = calendar.getTime();

				if (new Date().compareTo(startnotificationdate) >= 0 && new Date().compareTo(scheduledate) <= 0)
				{
					try
					{
						days = Utility.leftdaysInPeriod(Utility.setHourMinuteSecondZero(new Date()), scheduledate);
						showNotification(context, type, charSequence, days);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					PendingIntent pendingIntent = PendingIntent.getActivity(context, id, paramIntent, PendingIntent.FLAG_CANCEL_CURRENT);
					AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
					alarmManager.cancel(pendingIntent);

				}

			}
		}
		else if (type.equals(context.getResources().getString(R.string.ovulation_alert)))
		{
			charSequence = context.getString(R.string.ovulation);

			if (scheduletime != 2)
			{
				Calendar calendar = new GregorianCalendar().getInstance();
				calendar.setTimeInMillis(scheduletime);

				calendar.add(Calendar.DAY_OF_MONTH, -PeriodTrackerObjectLocator.getInstance().getOvulationNotification());
				calendar.set(Calendar.HOUR_OF_DAY, 8);
				startnotificationdate = calendar.getTime();

				calendar.setTimeInMillis(scheduletime);
				calendar.set(Calendar.HOUR_OF_DAY, 9);
				scheduledate = calendar.getTime();

				if (new Date().compareTo(startnotificationdate) >= 0 && new Date().compareTo(scheduledate) <= 0)
				{
					days = Utility.leftdaysInPeriod(Utility.setHourMinuteSecondZero(new Date()), scheduledate);
					showNotification(context, type, charSequence, days);
				}
				else
				{
					PendingIntent pendingIntent = PendingIntent.getActivity(context, id, paramIntent, PendingIntent.FLAG_CANCEL_CURRENT);
					AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
					alarmManager.cancel(pendingIntent);

				}

			}

		}

		// Create the notification

	}

	public void showNotification(Context context, String type, CharSequence charSequence, int days)
	{

		// Request the notification manager
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		// Create a new intent which will be fired if you click on the
		// notification
		Intent intent = new Intent(context, HomeScreenView.class);

		// Attach the intent to a pending intent
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

		String packageName = context.getPackageName();

		int iconAndName = Integer.valueOf(context.getSharedPreferences("com.linchpin.periodtracker", 0).getString("alias", "0"));
		Notification notification = null;
		if (iconAndName == 0 || iconAndName == 1 || iconAndName == 2)
		{
			notification = new Notification(R.drawable.icon, charSequence, System.currentTimeMillis());

		}
		else if (iconAndName == 3 || iconAndName == 4 || iconAndName == 5)
		{
			notification = new Notification(R.drawable.icon_flower, charSequence, System.currentTimeMillis());
		}
		else if (iconAndName == 6 || iconAndName == 7 || iconAndName == 8)
		{
			notification = new Notification(R.drawable.icon_bee, charSequence, System.currentTimeMillis());
		}
		else if (iconAndName == 9 || iconAndName == 10 || iconAndName == 11)
		{
			notification = new Notification(R.drawable.icon_heart, charSequence, System.currentTimeMillis());
		}
		else if (iconAndName == 12 || iconAndName == 13 || iconAndName == 14)
		{
			notification = new Notification(R.drawable.icon_teddy, charSequence, System.currentTimeMillis());
		}

		if (type.equals(context.getResources().getString(R.string.period_alert)))
		{
			int resId = context.getResources().getIdentifier(PeriodTrackerObjectLocator.getInstance().getPeriodStartNotificationMessage(), "string", packageName);

			if (days != 0 && days != 1)
			{

				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_name), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationdays),
							pendingIntent);

				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_newname), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationdays),
							pendingIntent);
				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationdays),
							pendingIntent);
				}

			}
			else if (days == 1)
			{

				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_name), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);
				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_newname), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);

				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);

				}

			}
			else
			{

				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_name),
							context.getString(R.string.periodstartnotificationmessagefrom) + " " + context.getString(R.string.today), pendingIntent);

				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_newname),
							context.getString(R.string.periodstartnotificationmessagefrom) + " " + context.getString(R.string.today), pendingIntent);

				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT),
							context.getString(R.string.periodstartnotificationmessagefrom) + " " + context.getString(R.string.today), pendingIntent);

				}
			}

		}
		else if (type.equals(context.getResources().getString(R.string.fertile_alert)))
		{
			int resId = context.getResources().getIdentifier(PeriodTrackerObjectLocator.getInstance().getFertilityNotificationMessage(), "string", packageName);

			if (days != 0 && days != 1)
			{
				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_name), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationdays),
							pendingIntent);
				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_newname), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationdays),
							pendingIntent);

				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationdays),
							pendingIntent);

				}
			}
			else if (days == 1)
			{

				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_name), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);

				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_newname), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);

				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);

				}
			}
			else
			{
				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_name),
							context.getString(R.string.fertilestartnotificationmessagefrom) + " " + context.getString(R.string.today), pendingIntent);

				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_newname),
							context.getString(R.string.fertilestartnotificationmessagefrom) + " " + context.getString(R.string.today), pendingIntent);

				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT),
							context.getString(R.string.fertilestartnotificationmessagefrom) + " " + context.getString(R.string.today), pendingIntent);

				}
			}

		}
		else if (type.equals(context.getResources().getString(R.string.ovulation_alert)))
		{
			int resId = context.getResources().getIdentifier(PeriodTrackerObjectLocator.getInstance().getOvulationNotificationMessage(), "string", packageName);
			if (days != 0 && days != 1)
			{

				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_name),

					context.getString(resId) + " " + days + " " + context.getString(R.string.notificationdays), pendingIntent);
				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_newname),

					context.getString(resId) + " " + days + " " + context.getString(R.string.notificationdays), pendingIntent);

				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT),

					context.getString(resId) + " " + days + " " + context.getString(R.string.notificationdays), pendingIntent);

				}
			}
			else if (days == 1)
			{

				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_name), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);
				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_newname), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);

				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT), context.getString(resId) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);

				}
			}

			else
			{

				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_name),
							context.getString(R.string.ovulationstartnotificationmessagefrom) + " " + context.getString(R.string.today), pendingIntent);
				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_newname),
							context.getString(R.string.ovulationstartnotificationmessagefrom) + " " + context.getString(R.string.today), pendingIntent);

				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT),
							context.getString(R.string.ovulationstartnotificationmessagefrom) + " " + context.getString(R.string.today), pendingIntent);

				}
			}
		}
		
		
		
		
	/*	else  if (type.equals(context.getResources().getString(R.string.pregnancy_alert)))
		{
			//int resId = context.getResources().getIdentifier(PeriodTrackerObjectLocator.getInstance().getPregnancyNotificationMessage(), "string", packageName);
			if (days != 0 && days != 1)
			{
if(days<0)
	days=-days;
				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_name),

					context.getString(R.string.PregnancyNotificationMessage) + " " + days + " " + context.getString(R.string.notificationdays), pendingIntent);
				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_newname),

					context.getString(R.string.PregnancyNotificationMessage) + " " + days + " " + context.getString(R.string.notificationdays), pendingIntent);

				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT),

					context.getString(R.string.PregnancyNotificationMessage) + " " + days + " " + context.getString(R.string.notificationdays), pendingIntent);

				}
			}
			else if (days == 1)
			{

				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_name), context.getString(R.string.PregnancyNotificationMessage) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);
				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_newname), context.getString(R.string.PregnancyNotificationMessage) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);

				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT), context.getString(R.string.PregnancyNotificationMessage) + " " + days + " " + context.getString(R.string.notificationday),
							pendingIntent);

				}
			}

			else
			{

				if (iconAndName == 0 || iconAndName == 3 || iconAndName == 6 || iconAndName == 9 || iconAndName == 12)
				{
					notification.setLatestEventInfo(context, context.getString(R.string.app_name),
							context.getString(R.string.PregnancyNotificationMessage) + " " + context.getString(R.string.today), pendingIntent);
				}
				else if (iconAndName == 1 || iconAndName == 4 || iconAndName == 7 || iconAndName == 10 || iconAndName == 13)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_newname),
							context.getString(R.string.PregnancyNotificationMessage) + " " + context.getString(R.string.today), pendingIntent);

				}
				else if (iconAndName == 2 || iconAndName == 5 || iconAndName == 8 || iconAndName == 11 || iconAndName == 14)
				{

					notification.setLatestEventInfo(context, context.getString(R.string.app_nameMPT),
							context.getString(R.string.PregnancyNotificationMessage) + " " + context.getString(R.string.today), pendingIntent);

				}
			}
		}*/

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		// Fire the notification
		notificationManager.notify(1, notification);

	}

}