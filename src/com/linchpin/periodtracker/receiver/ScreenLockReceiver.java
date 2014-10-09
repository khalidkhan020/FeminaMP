package com.linchpin.periodtracker.receiver;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;

public class ScreenLockReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) || intent.getAction().equals(Intent.ACTION_SCREEN_ON) || intent.getAction().equals(Intent.ACTION_USER_PRESENT))
		{
			try{
			
			 if(!PeriodTrackerObjectLocator.getInstance().getPasswordProtection().trim().equals("")){
			String g = APP.GLOBAL().getPreferences().getString(APP.PREF.WIDGET_ID.key, "");			
			String ss[] = g.split("#");
			int widgetids[] = new int[ss.length];
			for (int i = 0; i < ss.length; i++)
				widgetids[i] = Integer.valueOf(ss[i]);			
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.WIDGET_LOCK.key, true).commit();
			Intent i = new Intent(context, MPTAppWidgetProvider.class);
			i.setAction("android.appwidget.action.APPWIDGET_UPDATE");
			i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetids);
			context.sendBroadcast(i);}
			}catch(Exception e)
			{}
			
		}
	}
}
