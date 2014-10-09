package com.linchpin.periodtracker.utlity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.NotificattionModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodttracker.database.NotificationDBHandler;

public class Utility
{
	// public static DecimalFormat decimalFormat = new DecimalFormat("#.#");
	NotificationDBHandler	notificationDBHandler;

	public static String getDefaultLanuageOFDeviece()
	{
		return Locale.getDefault().getLanguage();
	}

	public static void setAllNotiifications(Context context)
	{
		List<PeriodTrackerModelInterface> recordList;

		if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
		{
			try
			{
				NotificationDBHandler notificationDBHandler = new NotificationDBHandler(context);
				NotificattionModel notificattionModel = new NotificattionModel(0, context.getResources().getString(R.string.fertile_alert));

				
				recordList = notificationDBHandler.getPerdictionFertileDatesAndOvulationDates();
				Utility.createScheduledNotification(context, recordList, notificattionModel);

				notificattionModel.setType(context.getResources().getString(R.string.ovulation_alert));
				recordList = notificationDBHandler.getPerdictionFertileDatesAndOvulationDates();
				Utility.createScheduledNotification(context, recordList, notificattionModel);

				notificattionModel.setType(context.getResources().getString(R.string.period_alert));
				recordList = notificationDBHandler.getPredictionLogs();
				Utility.createScheduledNotification(context, recordList, notificattionModel);
				
				notificattionModel.setType(context.getResources().getString(R.string.pregnancy_alert));
				PeriodLogModel logModel = (PeriodLogModel) notificationDBHandler.getLatestLog();
				Date date=logModel.getStartDate();
				Date newdate=new Date();
				Date nextperiod=Utility.addDays(date, 28);
				if(Utility.isGreaterThan(newdate, nextperiod));
				{
					DayDetailModel dayDetailModel=(DayDetailModel) notificationDBHandler.getLatestInsecureIntimate(Utility.addDays(date, 7).getTime(), Utility.addDays(date, 19).getTime());
					
					Utility.createScheduledPregNotification(context, nextperiod, notificattionModel);
				}
				
				
			

			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
		}
		else
		{
			cancelAllNotification(context);
		}
	}

	public static long getNUllDate()
	{

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date("1/1/1900"));
		return calendar.getTimeInMillis();

	}
	
	public static float convertPixelsToDp(float px, Context context)
	{
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}
	public static float convertDpToPixel(float dp, Context context)
	{
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}
	public static void cancelAllNotification(Context Context)
	{
		List<PeriodTrackerModelInterface> modelInterfaces;
		NotificationDBHandler notificattionDbHandler = new NotificationDBHandler(Context);
		modelInterfaces = notificattionDbHandler.selectParticularNotificationType(new NotificattionModel(0, Context.getResources().getString(R.string.period_alert)));

		Utility.cancelScheduledNotification(Context, modelInterfaces);
		modelInterfaces = notificattionDbHandler.selectParticularNotificationType(new NotificattionModel(0, Context.getResources().getString(R.string.fertile_alert)));

		Utility.cancelScheduledNotification(Context, modelInterfaces);

		modelInterfaces = notificattionDbHandler.selectParticularNotificationType(new NotificattionModel(0, Context.getResources().getString(R.string.ovulation_alert)));

		Utility.cancelScheduledNotification(Context, modelInterfaces);
	}
	
	public static void createScheduledPregNotification(Context ctx,Date nextperiod, NotificattionModel notificattionModel)
	{

		Intent intent = new Intent(ctx, TimeAlarmReciever.class);
		intent.putExtra("type", notificattionModel.getType());
		NotificationDBHandler dbHandler = new NotificationDBHandler(ctx);
		dbHandler.deleteNotificationForParticularType(notificattionModel);
		int days = 0;
		

			
				if (notificattionModel.getType().equals(ctx.getResources().getString(R.string.pregnancy_alert)))
				{

					if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() != -1)
					{
						days = Utility.leftdaysInPeriod(new Date(), nextperiod);

						//days = days - (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification());

						intent.putExtra("date", Utility.setHourMinuteSecondZero(nextperiod).getTime());

						Utility.scheduleNotification(days, ctx, intent, notificattionModel, dbHandler);

					}
				}
				}

	public static void createScheduledNotification(Context ctx, List<PeriodTrackerModelInterface> recordList, NotificattionModel notificattionModel)
	{

		// Prepare the intent which should be launched at the date
		Intent intent = new Intent(ctx, TimeAlarmReciever.class);

		intent.putExtra("type", notificattionModel.getType());

		NotificationDBHandler dbHandler = new NotificationDBHandler(ctx);

		dbHandler.deleteNotificationForParticularType(notificattionModel);

		PeriodLogModel logModel;
		int days = 0;
		for (int i = 0; i < recordList.size(); i++)
		{

			if (recordList.size() > 0)
			{
				logModel = (PeriodLogModel) recordList.get(i);

				if (notificattionModel.getType().equals(ctx.getResources().getString(R.string.period_alert)))
				{

					if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() != -1)
					{
						days = Utility.leftdaysInPeriod(new Date(), logModel.getStartDate());

						days = days - (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification());

						intent.putExtra("date", Utility.setHourMinuteSecondZero(logModel.getStartDate()).getTime());

						Utility.scheduleNotification(days, ctx, intent, notificattionModel, dbHandler);

					}

				}
				else if (notificattionModel.getType().equals(ctx.getResources().getString(R.string.fertile_alert)))
				{
					days = Utility.leftdaysInPeriod(new Date(), logModel.getFertileStartDate());

					if (PeriodTrackerObjectLocator.getInstance().getFertilityNotification() != -1)
					{

						days = days - (PeriodTrackerObjectLocator.getInstance().getFertilityNotification());

						intent.putExtra("date", Utility.setHourMinuteSecondZero(logModel.getFertileStartDate()).getTime());

						scheduleNotification(days, ctx, intent, notificattionModel, dbHandler);

					}

				}

				else if (notificattionModel.getType().equals(ctx.getResources().getString(R.string.ovulation_alert)))
				{
					days = Utility.leftdaysInPeriod(new Date(), logModel.getOvulationDate());

					days = days - (PeriodTrackerObjectLocator.getInstance().getOvulationNotification());

					intent.putExtra("date", Utility.setHourMinuteSecondZero(logModel.getOvulationDate()).getTime());
					scheduleNotification(days, ctx, intent, notificattionModel, dbHandler);

				}

			}
		}
	}

	public static void scheduleNotification(int days, Context context, Intent intent, NotificattionModel notificattionModel, NotificationDBHandler dbHandler)
	{

		try
		{
			// Get new calendar object and set the date to now
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DATE, Utility.setHourMinuteSecondZero(new Date()).getDate());
			calendar.set(Calendar.HOUR_OF_DAY, 8);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			// Add defined amount of days to the date
			calendar.add(Calendar.DATE, days);

			// Retrieve alarm manager from the system
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
			// Every scheduled intent needs a different ID, else it is just
			// executed
			// once
			int id = (int) System.currentTimeMillis();

			intent.putExtra("id", id);
			// Prepare the pending intent
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

			// Register the alert in the system. You have the option to
			// define
			// if
			// the device has to wake up on the alert or not

			System.out.println(calendar.getTime());

			// alarmManager.set(, calendar.getTimeInMillis(), pendingIntent);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
			/*
			 * alarmManager.set(AlarmManager.RTC_WAKEUP,
			 * calendar.getTimeInMillis(), pendingIntent);
			 */

			notificattionModel.setId(id);

			dbHandler.addRecord(notificattionModel);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

	}

	public static void cancelScheduledNotification(Context ctx, List<PeriodTrackerModelInterface> notificattionModel)
	{
		if (notificattionModel.size() > 0)
		{
			for (int i = 0; i < notificattionModel.size(); i++)
			{

				try
				{
					// Retrieve alarm manager from the system
					AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);
					// Prepare the intent which should be launched at the date
					Intent intent = new Intent(ctx, TimeAlarmReciever.class);

					// Prepare the pending intent
					PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, ((NotificattionModel) notificattionModel.get(i)).getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

					alarmManager.cancel(pendingIntent);

				}
				catch (Exception exception)
				{
					exception.printStackTrace();
				}
			}

			NotificationDBHandler dbHandler = new NotificationDBHandler(ctx);
			dbHandler.deleteNotificationForParticularType((NotificattionModel) notificattionModel.get(0));
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView, Context ctx, String name)
	{
		BaseAdapter listAdapter = (BaseAdapter) listView.getAdapter();
		if (listAdapter == null)
		{
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();

		for (int i = 0; i < listAdapter.getCount(); i++)
		{
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		if (name.equals("personal"))
		{
			params.height = totalHeight + 40 + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		}
		else
		{
			params.height = totalHeight + 5 + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		}
		listView.setPadding(0, 5, 0, 5);
		listView.setLayoutParams(params);
		listView.requestLayout();

	}

	public static String AppLanguageLocale(String language, Context context)
	{

		Map<String, String> mapLanguage = new HashMap<String, String>();

		mapLanguage.put(context.getString(R.string.english), "en");
		mapLanguage.put(context.getString(R.string.croatian), "hr");
		mapLanguage.put(context.getString(R.string.dutch), "nl");
		mapLanguage.put(context.getString(R.string.french), "fr");
		mapLanguage.put(context.getString(R.string.german), "de");
		mapLanguage.put(context.getString(R.string.italian), "it");
		mapLanguage.put(context.getString(R.string.japanese), "ja");
		mapLanguage.put(context.getString(R.string.portuguese), "pt");
		mapLanguage.put(context.getString(R.string.spanish), "es");

		return mapLanguage.get(language);
	}

	public static String getLanguageFromLoacle(String Locale, Context context)
	{

		Map<String, String> mapLanguage = new HashMap<String, String>();

		mapLanguage.put("en", context.getString(R.string.english));
		mapLanguage.put("hr", context.getString(R.string.croatian));
		mapLanguage.put("nl", context.getString(R.string.dutch));
		mapLanguage.put("fr", context.getString(R.string.french));
		mapLanguage.put("de", context.getString(R.string.german));
		mapLanguage.put("it", context.getString(R.string.italian));
		mapLanguage.put("ja", context.getString(R.string.japanese));
		mapLanguage.put("pt", context.getString(R.string.portuguese));
		mapLanguage.put("es", context.getString(R.string.spanish));

		return mapLanguage.get(Locale);

	}

	public static void sendMail(Context context, String userName, String action, String content1, String content2, String content3)
	{
		try
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("app_name", "my_period_tracker");
			jsonObject.put("user_email", userName);
			jsonObject.put("action", action);
			jsonObject.put("content1", content1);
			jsonObject.put("content2", content2);
			jsonObject.put("content3", content3);
			jsonObject.put("platform", "android");
			new RestoreAndBackUp(context, "mail/" + jsonObject.toString().trim()).execute();

		}
		catch (JSONException json)
		{
			json.printStackTrace();
		}

	}

	public static int leftdaysInPeriod(Date currentDate, Date periodLogStartDate)
	{

		Calendar calendar = GregorianCalendar.getInstance();

		currentDate = Utility.setHourMinuteSecondZero(currentDate);
		periodLogStartDate = Utility.setHourMinuteSecondZero(periodLogStartDate);
		long l = periodLogStartDate.getTime() - currentDate.getTime();
		int days = (int) (l / PeriodTrackerConstants.MILLI_SECONDS);
		return days;

	}

	public static String getdaysfromstartofPregnancyIntheformWeaks(Date lastPeriodstartDate, Date currentDate)
	{
		lastPeriodstartDate = Utility.setHourMinuteSecondZero(lastPeriodstartDate);
		currentDate = Utility.setHourMinuteSecondZero(currentDate);
		int days = (int) ((currentDate.getTime() - lastPeriodstartDate.getTime()) / PeriodTrackerConstants.MILLI_SECONDS);

		int weaks = days / 7;
		int day = (days % 7) + 1;
		return weaks + "W" + day + "D";

	}

	public static String getLatedaysfromstartofPregnancyIntheformWeaks(Date lastPeriodstartDate, Date currentDate)
	{
		lastPeriodstartDate = Utility.setHourMinuteSecondZero(lastPeriodstartDate);
		currentDate = Utility.setHourMinuteSecondZero(currentDate);
		int days = (int) ((currentDate.getTime() - lastPeriodstartDate.getTime()) / PeriodTrackerConstants.MILLI_SECONDS);

		int weaks = days / 7;
		int day = (days % 7) + 1;

		return weaks + "W" + day + "D";

	}

	public static String getDefaultDateFormatOFDeviece(Context context)
	{
		java.text.DateFormat dateFormat;
		ContentResolver contentResolver = new ContentResolver(context)
		{
		};
		final String format = Settings.System.getString(contentResolver, Settings.System.DATE_FORMAT);
		if (TextUtils.isEmpty(format))
		{
			dateFormat = android.text.format.DateFormat.getMediumDateFormat(context);
		}
		else
		{
			dateFormat = new SimpleDateFormat(format);
		}
		return dateFormat.toString();
	}

	public static int calculateDayofperiod(Date periodStartDate)
	{

		long days = setHourMinuteSecondZero(Utility.setHourMinuteSecondZero(new Date())).getTime() - setHourMinuteSecondZero(periodStartDate).getTime();

		return (int) (days / PeriodTrackerConstants.MILLI_SECONDS) + 1;
	}

	public static int dueDaysInNextPeriod(Date predictionDate, Date currentDate)
	{

		predictionDate = setHourMinuteSecondZero(predictionDate);
		currentDate = setHourMinuteSecondZero(currentDate);

		long days = predictionDate.getTime() - currentDate.getTime();
		return (int) (days / PeriodTrackerConstants.MILLI_SECONDS);
	}

	public static int lateDaysInNextPeriod(Date lastPeriodStart, Date currentDate)
	{
		lastPeriodStart = setHourMinuteSecondZero(lastPeriodStart);
		currentDate = setHourMinuteSecondZero(currentDate);
		lastPeriodStart = addDays(lastPeriodStart, PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength());
		long days = currentDate.getTime() - lastPeriodStart.getTime();

		return (int) (days / PeriodTrackerConstants.MILLI_SECONDS);
	}

	public static Date calculatePregnancyDate(Date lastPeriodStartDate)
	{
		Date pregnancyDate;
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(lastPeriodStartDate);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 12);
		lastPeriodStartDate = calendar.getTime();
		calendar.setTime(lastPeriodStartDate);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 3);
		lastPeriodStartDate = calendar.getTime();
		pregnancyDate = Utility.addDays(lastPeriodStartDate, 7);
		return pregnancyDate;
	}

	public static int dueDaysInPregnancy(Date lastPeriodStartDate, Date currentDate)
	{
		Date pregnancyDate;
		pregnancyDate = calculatePregnancyDate(lastPeriodStartDate);

		pregnancyDate = Utility.setHourMinuteSecondZero(pregnancyDate);

		currentDate = Utility.setHourMinuteSecondZero(currentDate);

		long days = pregnancyDate.getTime() - currentDate.getTime();

		return ((int) (days / PeriodTrackerConstants.MILLI_SECONDS) - 1);
	}

	public static int lateDaysInPregnancy(Date lastPeriodStartDate, Date currentDate)
	{
		Date pregnancyDate;
		pregnancyDate = calculatePregnancyDate(lastPeriodStartDate);
		pregnancyDate = Utility.setHourMinuteSecondZero(pregnancyDate);
		currentDate = Utility.setHourMinuteSecondZero(currentDate);

		long days = currentDate.getTime() - pregnancyDate.getTime();

		return ((int) (days / PeriodTrackerConstants.MILLI_SECONDS) + 1);
	}

	public static int dueDaysInPregnancyWhenKnownEstimateddate(Date pregnancyDate, Date currentDate)
	{

		pregnancyDate = Utility.setHourMinuteSecondZero(pregnancyDate);
		currentDate = Utility.setHourMinuteSecondZero(currentDate);

		long days = pregnancyDate.getTime() - currentDate.getTime();

		return ((int) (days / PeriodTrackerConstants.MILLI_SECONDS) - 1);
	}

	public static int lateDaysInPregnancyWhenKnownEstimateddate(Date pregnancyDate, Date currentDate)
	{

		pregnancyDate = Utility.setHourMinuteSecondZero(pregnancyDate);
		currentDate = Utility.setHourMinuteSecondZero(currentDate);

		long days = currentDate.getTime() - pregnancyDate.getTime();

		return ((int) (days / PeriodTrackerConstants.MILLI_SECONDS) - 1);
	}

	public static double ConvertToCelsius(double temp)
	{
		try
		{
			temp = (temp - 32) * 5 / 9;

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return temp;
	}

	public static float ConvertToFahrenheit(float temp)
	{
		try
		{
			temp = (temp * 9 / 5) + 32;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return temp;
	}

	public static float ConvertToKilogram(float weigh)
	{
		try
		{
			weigh = (float) (weigh / 2.2046);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return weigh;
	}

	public static float ConvertToPounds(float weigh)
	{
		try
		{
			weigh = (float) (weigh * 2.2046);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return weigh;
	}

	public static int calculateCycleLength(Date currentStartDate, Date previousStartDate)
	{

		currentStartDate = setHourMinuteSecondZero(currentStartDate);

		previousStartDate = setHourMinuteSecondZero(previousStartDate);

		Long days = currentStartDate.getTime() - previousStartDate.getTime();

		return (int) (days / (PeriodTrackerConstants.MILLI_SECONDS));
	}

	public static int calculatePeriodLength(Date StartDate, Date EndDate)
	{

		StartDate = setHourMinuteSecondZero(StartDate);

		EndDate = setHourMinuteSecondZero(EndDate);

		long days = (EndDate.getTime() - StartDate.getTime()) + PeriodTrackerConstants.MILLI_SECONDS;

		return (int) (days / (PeriodTrackerConstants.MILLI_SECONDS));

	}

	public static Date setHourMinuteSecondZero(Date date)
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date addDays(Date d, int days)
	{

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		d = setHourMinuteSecondZero(calendar.getTime());

		return d;
	}

	public static Date subtractDays(Date d, int days)
	{

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		d = setHourMinuteSecondZero(calendar.getTime());

		return d;

		/*
		 * d = setHourMinuteSecondZero(d);
		 * 
		 * return new Date(d.getTime() - days *
		 * PeriodTrackerConstants.MILLI_SECONDS);
		 */}

	public static Date stringToDate(String date, String format)
	{
		Date returndate = null;
		try
		{
			SimpleDateFormat dateformat = new SimpleDateFormat(format);
			returndate = dateformat.parse(date);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return returndate;

	}

	public static Date createDate(int year, int month, int date)
	{
		Calendar cal = Calendar.getInstance();
		try
		{
			cal.set(Calendar.DATE, date);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.YEAR, year);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return cal.getTime();
	}

	public static boolean isDateLessThanCurrent(Date dateObj)
	{
		boolean flag = false;
		Date currentDate = new Date();

		currentDate = setHourMinuteSecondZero(currentDate);

		dateObj = setHourMinuteSecondZero(dateObj);

		if (null != dateObj && dateObj.compareTo(currentDate) <= 0) flag = true;
		return flag;
	}

	public static boolean isDateLessThanLatestRecord(Date newStartDate, Date latestDate)
	{
		boolean flag = false;

		newStartDate = setHourMinuteSecondZero(newStartDate);

		latestDate = setHourMinuteSecondZero(latestDate);

		if (null != newStartDate && newStartDate.compareTo(latestDate) < 0) 
			flag = true;
		return flag;
	}

	public static boolean isEndDateGreaterThanStart(Date startDateObj, Date endDateObj)
	{
		boolean flag = false;

		endDateObj = setHourMinuteSecondZero(endDateObj);

		startDateObj = setHourMinuteSecondZero(startDateObj);

		if (null != startDateObj && null != endDateObj)
		{

			if (endDateObj.after(startDateObj) || endDateObj.equals(startDateObj))
			{
				flag = true;

			}
		}
		return flag;
	}

	public static boolean isGreaterThan(Date startDateObj, Date endDateObj)
	{
		boolean flag = false;

		endDateObj = setHourMinuteSecondZero(endDateObj);

		startDateObj = setHourMinuteSecondZero(startDateObj);

		if (null != startDateObj && null != endDateObj)
		{

			if (endDateObj.after(startDateObj) || endDateObj.compareTo(startDateObj) == 0)
			{
				flag = true;

			}
		}
		return flag;
	}

	public static boolean checkDateBetweenDates(Date comparefrom1, Date comparefrom2, Date compareToBe)
	{

		comparefrom1 = setHourMinuteSecondZero(comparefrom1);

		comparefrom2 = setHourMinuteSecondZero(comparefrom2);

		compareToBe = setHourMinuteSecondZero(compareToBe);

		return compareToBe.before(comparefrom1) || compareToBe.after(comparefrom2);

	}
	public static int checkDateBetweenDates2(Date comparefrom1, Date comparefrom2, Date compareToBe)
	{
		int type=0;
		comparefrom1 = setHourMinuteSecondZero(comparefrom1);

		comparefrom2 = setHourMinuteSecondZero(comparefrom2);

		compareToBe = setHourMinuteSecondZero(compareToBe);
		if(compareToBe.compareTo(comparefrom1)==0)
			type=1;
		else if(compareToBe.compareTo(comparefrom2)==0)
			type=2;
		else if((compareToBe.before(comparefrom1) && compareToBe.after(comparefrom2)))
			type=3;
		return type;

	}

	public static float convertInchesTOCentiMeter(float inches)
	{
		float centimeter = (float) (inches * 2.54);
		return centimeter;
	}

	public static float convertCentiMeterTOInches(float centimeter)
	{
		float inches = (float) (centimeter * 0.393701);
		return inches;
	}

	public static boolean isNetworkConnected(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null)
		{
			// There are no active networks.
			return false;
		}
		else return true;
	}

	public static float calculateBMI(float weight, float height)
	{
		float bmi = 0;
		height = height / 100;
		if (height != 0.0)
		{
			bmi = ((weight / (height * height)));

		}
		return bmi;
	}

	public static boolean checkAndroidApiVersion()
	{
		boolean statemet = false;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			statemet = true;
		}
		return statemet;
	}

	public static boolean isValidNumber(Class c, String numString)
	{
		try
		{
			if (c == double.class || c == Double.class)
			{
				Double.parseDouble(numString);
			}
			else if (c == int.class || c == Integer.class)
			{
				Integer.parseInt(numString);
			}
			else if (c == float.class || c == Float.class)
			{
				Float.parseFloat(numString);
			}
			else if (c == long.class || c == Long.class)
			{
				Long.parseLong(numString);
			}
		}
		catch (Exception ex)
		{
			return false;
		}
		return true;
	}

	public static String getStringFormatedNumber(String Snumber)
	{
		double d = 0;
		try
		{
			DecimalFormat decimalFormat = new DecimalFormat("#.#");
			Snumber = decimalFormat.format(Float.parseFloat(Snumber));
			NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
			Number number = format.parse(Snumber);
			d = number.doubleValue();

		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return (String.valueOf(d));

	}
}
