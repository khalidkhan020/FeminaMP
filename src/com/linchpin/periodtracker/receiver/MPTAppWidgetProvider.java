package com.linchpin.periodtracker.receiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Html;
import android.view.View;
import android.widget.RemoteViews;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.view.EnterPassword;
import com.linchpin.periodtracker.view.HomeScreenView;
import com.linchpin.periodttracker.database.DashBoardDBHandler;

public class MPTAppWidgetProvider extends AppWidgetProvider
{
	RemoteViews							remoteViews;
	Context								context;
	DashBoardDBHandler					dashBoardDBHandler;
	PeriodTrackerModelInterface			perdictionPeriodLog, perdictionFertileRecord;
	PeriodLogModel						periodLogModelPerdiction, periodLogModel, previousPeriodlog;
	List<PeriodTrackerModelInterface>	periodTrackerModelInterfaces;
	boolean								isFertile	= false, isOvulation = false, isReagularDay = false;
	SimpleDateFormat					dateFormat;
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		Tracker tracker = GoogleAnalytics.getInstance(context).getTracker("UA-54563519-3");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "AppWidget Deleted");
		hitParameters.put(Fields.SCREEN_NAME, "AppWidget");
		tracker.send(hitParameters);
		super.onDeleted(context, appWidgetIds);
	}
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		
		Tracker tracker = GoogleAnalytics.getInstance(context).getTracker("UA-54563519-3");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "AppWidget Receved");
		hitParameters.put(Fields.SCREEN_NAME, "AppWidget");
		tracker.send(hitParameters);
		super.onReceive(context, intent);
	}
	
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{	
		this.context = context;
		Tracker tracker = GoogleAnalytics.getInstance(context.getApplicationContext()).getTracker("UA-54563519-3");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "AppWidget Updated");
		hitParameters.put(Fields.SCREEN_NAME, "AppWidget");
		tracker.send(hitParameters);
		dateFormat = new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		StringBuilder sb= new StringBuilder();
		for(int i:appWidgetIds)
			sb.append(i+"#");
		final IntentFilter recever = new IntentFilter();
		recever.addAction(Intent.ACTION_SCREEN_ON);
		recever.addAction(Intent.ACTION_SCREEN_OFF);		
		ScreenLockReceiver mPowerKeyReceiver = new ScreenLockReceiver();		
		context.getApplicationContext().registerReceiver(mPowerKeyReceiver, recever);
		APP.GLOBAL().getEditor().putString(APP.PREF.WIDGET_ID.key, sb.toString()).commit();
		for (int i = 0; i < appWidgetIds.length; i++)
		{
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.mpt_appwidget);
			
			if (APP.GLOBAL().getPreferences().getBoolean(APP.PREF.WIDGET_LOCK.key, true))
			{
				Intent configIntent;
				PendingIntent configPendingIntent;
				remoteViews.setViewVisibility(R.id.frame, View.GONE);
				remoteViews.setViewVisibility(R.id.lock, View.VISIBLE);
				remoteViews.setOnClickPendingIntent(R.id.frame, null);
				configIntent = new Intent(context, EnterPassword.class);
				configIntent.putExtra("lock", false);
				configIntent.putExtra("ids", appWidgetIds);
				configIntent.putExtra("fromwidget", true);
				configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.lock, configPendingIntent);
				
			}
			else
			{
				Intent configIntent0,configIntent;
				PendingIntent configPendingIntent0,configPendingIntent;
				configIntent0 = new Intent(context, HomeScreenView.class);
				configIntent0.putExtra("fromwidget", true);
				configPendingIntent0 = PendingIntent.getActivity(context, 0, configIntent0, PendingIntent.FLAG_UPDATE_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.frame, configPendingIntent0);
				
				remoteViews.setViewVisibility(R.id.frame, View.VISIBLE);
				remoteViews.setViewVisibility(R.id.lock, View.GONE);
				
				configIntent = new Intent(context, EnterPassword.class);
				configIntent.putExtra("lock", true);
				configIntent.putExtra("ids", appWidgetIds);
				configIntent.putExtra("fromwidget", true);
				configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.unlock, configPendingIntent);
				
				dashBoardDBHandler = new DashBoardDBHandler(context);
				periodTrackerModelInterfaces = dashBoardDBHandler.getAllLogs();
				if (periodTrackerModelInterfaces.size() > 0)
				{
					periodLogModel = (PeriodLogModel) periodTrackerModelInterfaces.get(0);
					if (PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
					{
						if (periodLogModel != null)
						{
							if (PeriodTrackerObjectLocator.getInstance().getEstimatedDeliveryDate().getTime() == PeriodTrackerConstants.NULL_DATE)
							{
								if (!periodLogModel.isPregnancy()) setAboutText(periodLogModel);
								else
								{
									previousPeriodlog = (PeriodLogModel) dashBoardDBHandler.selectPreviousDateRecord(periodLogModel.getStartDate());
									if (null != previousPeriodlog && null != previousPeriodlog.getStartDate()) setAboutText(previousPeriodlog);
								}
							}
							else
							{
								if (PeriodTrackerObjectLocator.getInstance().PregnancyMessageFormat().equals(context.getResources().getString(R.string.daystobaby))) remoteViews.setTextViewText(R.id.t1,
										"" + Utility.dueDaysInPregnancyWhenKnownEstimateddate((PeriodTrackerObjectLocator.getInstance().getEstimatedDeliveryDate()), new Date()) + " " + context.getResources().getString(R.string.daysLeftinbirth));
								else remoteViews.setTextViewText(R.id.t1, "" + Utility.getdaysfromstartofPregnancyIntheformWeaks(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + context.getResources().getString(R.string.sincepregnancy));
								
							}
						}
						remoteViews.setTextViewText(R.id.t2, "");
						remoteViews.setTextViewText(R.id.t3, context.getResources().getString(R.string.congratulations) + "\n" + context.getResources().getString(R.string.youarepregnant));
					}
					else whichdaysIsToday(periodTrackerModelInterfaces);
				}
				else
				{
					remoteViews.setTextViewText(R.id.t1, "");
					remoteViews.setTextViewText(R.id.t2, "");
					remoteViews.setTextViewText(R.id.t3, context.getResources().getString(R.string.welcome) + "\n" + context.getResources().getString(R.string.pleasetapperiodlogbutton));
					
				}
			}
			appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	private void setAboutText(PeriodLogModel periodLogModel)
	{
		if (PeriodTrackerObjectLocator.getInstance().PregnancyMessageFormat().equals(context.getResources().getString(R.string.daystobaby)))
		{
			if (Utility.dueDaysInPregnancy(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) > 0)
			{
				
				remoteViews.setTextViewText(R.id.t1, "" + Utility.dueDaysInPregnancy(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + "\n" + context.getResources().getString(R.string.daysLeftinbirth));
			}
			else
			{
				remoteViews.setTextViewText(R.id.t1, "" + Utility.lateDaysInPregnancy(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + context.getResources().getString(R.string.daysLateinbirth));
			}
		}
		else
		{
			remoteViews.setTextViewText(R.id.t1, "" + Utility.getdaysfromstartofPregnancyIntheformWeaks(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + context.getResources().getString(R.string.sincepregnancy));
			
		}
	}
	
	public void whichdaysIsToday(List<PeriodTrackerModelInterface> modelInterface)
	{
		
		if (modelInterface.size() > 0 && !((PeriodLogModel) modelInterface.get(0)).isPregnancy())
		{
			PeriodTrackerModelInterface perdictionPeriodLog = dashBoardDBHandler.getPredictionLogs();
			perdictionFertileRecord = dashBoardDBHandler.getPerdictionFertileDatesAndOvulationDates();
			periodLogModelPerdiction = (PeriodLogModel) perdictionPeriodLog;
			remoteViews.setTextViewText(R.id.t3, context.getResources().getString(R.string.nextperiod) + "\n" + dateFormat.format(periodLogModelPerdiction.getStartDate()));
			remoteViews.setTextViewText(R.id.t2, context.getResources().getString(R.string.nextFertiledays) + "\n" + dateFormat.format(((PeriodLogModel) perdictionFertileRecord).getFertileStartDate()));
			
			periodLogModel = (PeriodLogModel) dashBoardDBHandler.getPastFertileAndOvulationDates();
			printTextMessage(periodLogModel, periodLogModelPerdiction, Utility.calculateDayofperiod(periodLogModel.getStartDate()));
			
		}
		else
		{
			if (modelInterface.size() == 0)
			{
				remoteViews.setTextViewText(R.id.t1, "");
				remoteViews.setTextViewText(R.id.t2, "");
				remoteViews.setTextViewText(R.id.t3, context.getResources().getString(R.string.welcome) + "\n" + context.getResources().getString(R.string.pleasetapperiodlogbutton));
			}
			else
			{
				
				remoteViews.setTextViewText(R.id.t1, "");
				remoteViews.setTextViewText(R.id.t2, "");
				remoteViews.setTextViewText(R.id.t3, "");
			}
		}
		
	}
	
	public void printTextMessage(PeriodLogModel periodLogModel, PeriodLogModel periodLogModelPerdiction, int days)
	{
		
		if (Utility.setHourMinuteSecondZero(new Date()).before(periodLogModel.getFertileStartDate()))
		{
			remoteViews.setTextViewText(R.id.t2, context.getResources().getString(R.string.nextFertiledays) + "\n" + dateFormat.format(periodLogModel.getFertileStartDate()));
			dayLeftInNextPeriod(false);
		}
		else if (Utility.setHourMinuteSecondZero(periodLogModel.getOvulationDate()).equals(Utility.setHourMinuteSecondZero(new Date())))
		{
			remoteViews.setTextViewText(R.id.t2, context.getResources().getString(R.string.ovulation_day));
			isOvulation = true;
			dayLeftInNextPeriod(false);
		}
		else if (!Utility.checkDateBetweenDates(periodLogModel.getFertileStartDate(), periodLogModel.getFertileEndDate(), Utility.setHourMinuteSecondZero(new Date())))
		{
			isFertile = true;
			remoteViews.setTextViewText(R.id.t2, context.getResources().getString(R.string.fertileday));
			dayLeftInNextPeriod(false);
		}
		else if (days > 7 && days < PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength())
		{
			dayLeftInNextPeriod(isReagularDay);
		}
		else if (days > PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength())
		{
			lateDayMessage();
		}
		
		if (Utility.setHourMinuteSecondZero(periodLogModel.getStartDate()).after(Utility.setHourMinuteSecondZero(new Date())))
		{
			remoteViews.setTextViewText(R.id.t1, Utility.leftdaysInPeriod(new Date(), periodLogModel.getStartDate()) + " " + context.getResources().getString(R.string.leftdays));
		}
		else
		{
			if (days < 7)
			{
				if (days <= PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength())
				{
					addSuffixOnPeriodDayMessage(days);
				}
				else
				{
					if (days <= 7 && days > PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength())
					{
						isReagularDay = true;
						dayLeftInNextPeriod(isReagularDay);
					}
				}
			}
			else if (days == PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength())
			{
				
				dayLeftInNextPeriod(false);
				
			}
			
			else
			{
				if (days <= PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength())
				{
					isReagularDay = true;
					dayLeftInNextPeriod(isReagularDay);
				}
			}
		}
	}
	
	public void dayLeftInNextPeriod(boolean isRegularDay)
	{
		int days = Utility.dueDaysInNextPeriod(periodLogModelPerdiction.getStartDate(), Utility.setHourMinuteSecondZero(new Date()));
		
		int d = Integer.valueOf(PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength());
		if (days == d)
		{
			days = d;
		}
		
		switch (days)
		{
		
			case 1:
				if (isRegularDay)
				{
					remoteViews.setTextViewText(R.id.t1, days + " " + context.getResources().getString(R.string.dayleftinnextperiod));
					remoteViews.setTextViewText(R.id.t2, context.getResources().getString(R.string.regularday));
				}
				else remoteViews.setTextViewText(R.id.t1, days + " " + context.getResources().getString(R.string.daysleftinnextperiod));
				break;
			default:
				if (isRegularDay)
				{
					if (days != d)
					{
						remoteViews.setTextViewText(R.id.t1, days + " " + context.getResources().getString(R.string.daysleftinnextperiod));
					}
					else
					{
						remoteViews.setTextViewText(R.id.t1, "1" + Html.fromHtml("<sub><small>" + context.getResources().getString(R.string.stdayofperiod) + "</small></sub>") + " " + context.getResources().getString(R.string.dayofperiod));
						
					}
					remoteViews.setTextViewText(R.id.t2, context.getResources().getString(R.string.regularday));
				}
				else
				{
					if (days != d)
					{
						remoteViews.setTextViewText(R.id.t1, days + " " + context.getResources().getString(R.string.daysleftinnextperiod));
					}
					else
					{
						remoteViews.setTextViewText(R.id.t1, "1" + Html.fromHtml("<sub><small>" + context.getResources().getString(R.string.stdayofperiod) + "</small></sub>") + " " + context.getResources().getString(R.string.dayofperiod));
						
					}
				}
				break;
		}
	}
	
	public void addSuffixOnPeriodDayMessage(int days)
	{
		
		switch (days)
		{
			case 1:
				remoteViews.setTextViewText(R.id.t1, "" + days + Html.fromHtml("<sub><small>" + context.getResources().getString(R.string.stdayofperiod) + "</small></sub>") + " " + context.getResources().getString(R.string.dayofperiod));
				break;
			
			case 2:
				remoteViews.setTextViewText(R.id.t1, "" + days + Html.fromHtml("<sub><small>" + context.getResources().getString(R.string.nddayofperiod) + "</small></sub>") + " " + context.getResources().getString(R.string.dayofperiod));
				break;
			
			case 3:
				remoteViews.setTextViewText(R.id.t1, "" + days + Html.fromHtml("<sub><small>" + context.getResources().getString(R.string.rddayofperiod) + "</small></sub>") + " " + context.getResources().getString(R.string.dayofperiod));
				break;
			
			default:
				remoteViews.setTextViewText(R.id.t1, "" + days + Html.fromHtml("<sub><small>" + context.getResources().getString(R.string.thdayofperiod) + "</small></sub>") + " " + context.getResources().getString(R.string.dayofperiod));
				break;
		}
		
	}
	
	public void lateDayMessage()
	{
		int days = Utility.lateDaysInNextPeriod(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date()));
		switch (days)
		{
			case 0:
				remoteViews.setTextViewText(R.id.t1, context.getResources().getString(R.string.periodstarttoday));
				
				break;
			case 1:
				remoteViews.setTextViewText(R.id.t1, days + " " + context.getResources().getString(R.string.daylate));
				break;
			default:
				remoteViews.setTextViewText(R.id.t1, days + " " + context.getResources().getString(R.string.dayslate));
				break;
		}
		
	}
	
}
