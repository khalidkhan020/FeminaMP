package com.linchpin.periodtracker.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.SettingsAdaptor;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.NotificattionModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;
import com.linchpin.periodttracker.database.NotificationDBHandler;

public class NotificationSettingsView extends Activity
{
	
	//ArrayList<String>	notificationTypeList;
	ListView	listView;
	private void applyTheme()
	{
		
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{			
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundColor(R.id.notificationsettings, "heading_bg");
			t.applyBackgroundDrawable(R.id.notificationsettingsback, "backbuttonselctor");
			t.applyBackgroundDrawable(R.id.notificationsettinginfobutton, "help");
			
			t.applyTextColor(R.id.notification, "text_color");
			t.applyTextColor(R.id.notificationsettings, "heading_fg");
		
			
		}
		/*else
		{
			setContentView(R.layout.settings);
			initLayout();
		}*/
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setnotificationlayout);
		applyTheme();
		findViewById(R.id.notificationsettinginfobutton).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(NotificationSettingsView.this, HomeScreenHelp.class);
				
				intent.putExtra("classname", "NotificationSettingsView");
				startActivity(intent);
				
			}
		});
		
		findViewById(R.id.notificationsettingsback).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
			}
		});
		
		/*notificationTypeList = new ArrayList<String>();
		notificationTypeList.add(getResources().getString(R.string.period_alert));
		notificationTypeList.add(getResources().getString(R.string.fertile_alert));
		notificationTypeList.add(getResources().getString(R.string.ovulation_alert));*/
		initLayout();
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
		if (this.getSharedPreferences("com.linchpin.periodtracker", 0).getBoolean("purchased", false))
		{
			
		}
		else
		{
			PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
		
		EasyTracker.getInstance(this).activityStop(this);
		
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
	
	public void initLayout()
	{
		
		listView = (ListView) findViewById(R.id.notificationlist);
		listView.setAdapter(new SettingsAdaptor(NotificationSettingsView.this, getResources().getStringArray(R.array.notification_settings)));
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
			{
				onClickListItem(((TextView) v.findViewById(R.id.settinglistitem)).getText().toString().trim());
			}
		});
		
	}
	
	private void onClickListItem(final String type)
	{
		
		int selectedPosition = -1;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items =
		{
				getResources().getString(R.string.nonotificationrequired), getResources().getString(R.string.ontheday), " 1 " + getResources().getString(R.string.daybefore), " 2 " + getResources().getString(R.string.daysbefore), " 3 " + getResources().getString(R.string.daysbefore),
				" 4 " + getResources().getString(R.string.daysbefore)
		};
		if (type.equals((getResources().getString(R.string.fertile_alert))))
		{
			
			selectedPosition = PeriodTrackerObjectLocator.getInstance().getFertilityNotification();
		}
		else if ((type.equals(getResources().getString(R.string.ovulation_alert))))
		{
			selectedPosition = PeriodTrackerObjectLocator.getInstance().getOvulationNotification();
			
		}
		else if ((type.equals(getResources().getString(R.string.period_alert))))
		{
			selectedPosition = PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification();
		}
		
		if (type.equals(getResources().getString(R.string.period_alert)))
		{
			builder.setTitle(getResources().getString(R.string.setdaysforperiodnotifications));
		}
		else if (type.equals(getResources().getString(R.string.fertile_alert)))
		{
			builder.setTitle(getResources().getString(R.string.setdaysforfetilenotifications));
		}
		else if (type.equals(getResources().getString(R.string.ovulation_alert)))
		{
			builder.setTitle(getResources().getString(R.string.setdaysforovualtionnotifications));
		}
		
		builder.setSingleChoiceItems(items, selectedPosition + 1, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				dialog.dismiss();
				if (type.equals(getResources().getString(R.string.period_alert)))
				{
					setVaulesAlertDaysForPeriodStart(which);
				}
				else if (type.equals(getResources().getString(R.string.fertile_alert)))
				{
					setVaulesAlertDaysForFertileStart(which);
				}
				else if (type.equals(getResources().getString(R.string.ovulation_alert)))
				{
					setVaulesAlertDaysForOvulationStart(which);
				}
				
			}
		});
		builder.show();
		
	}
	
	public void setVaulesAlertDaysForPeriodStart(int vaule)
	{
		
		List<PeriodTrackerModelInterface> recordList, deleteNotificationList;
		
		PeriodLogModel logModel;
		int days = 0;
		
		vaule = vaule - 1;
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_KEY, String.valueOf(vaule), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
		
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		initLayout();
		NotificationDBHandler notificationDBHandler = new NotificationDBHandler(getApplicationContext());
		NotificattionModel notificattionModel = new NotificattionModel(0, getResources().getString(R.string.period_alert));
		
		recordList = notificationDBHandler.getPredictionLogs();
		deleteNotificationList = notificationDBHandler.selectParticularNotificationType(notificattionModel);
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, true).commit();
		
	}
	
	public void setVaulesAlertDaysForFertileStart(int vaule)
	{
		List<PeriodTrackerModelInterface> recordList, deleteNotificationList;
		
		vaule = vaule - 1;
		
		PeriodLogModel logModel;
		
		int days = 0;
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_KEY, String.valueOf(vaule), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
		
		NotificationDBHandler notificationDBHandler = new NotificationDBHandler(getApplicationContext());
		NotificattionModel notificattionModel = new NotificattionModel(0, getResources().getString(R.string.fertile_alert));
		
		recordList = notificationDBHandler.getPerdictionFertileDatesAndOvulationDates();
		
		deleteNotificationList = notificationDBHandler.selectParticularNotificationType(notificattionModel);
		
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		
		initLayout();
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, true).commit();
		
	}
	
	public void setVaulesAlertDaysForOvulationStart(int vaule)
	{
		
		List<PeriodTrackerModelInterface> recordList, deleteNotificationList;
		
		PeriodLogModel logModel;
		vaule = vaule - 1;
		
		int days = 0;
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_KEY, String.valueOf(vaule), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
		
		NotificationDBHandler notificationDBHandler = new NotificationDBHandler(getApplicationContext());
		NotificattionModel notificattionModel = new NotificattionModel(0, getResources().getString(R.string.ovulation_alert));
		
		recordList = notificationDBHandler.getPerdictionFertileDatesAndOvulationDates();
		
		deleteNotificationList = notificationDBHandler.selectParticularNotificationType(notificattionModel);
		
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		initLayout();
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, true).commit();
	}
	
}
