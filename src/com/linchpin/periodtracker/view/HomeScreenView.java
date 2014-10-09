package com.linchpin.periodtracker.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.controller.ClickListener;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.TimeLineModel;
import com.linchpin.periodtracker.partnersharing.LoginScreen;
import com.linchpin.periodtracker.partnersharing.PartnerHomeActivity;
import com.linchpin.periodtracker.receiver.MPTAppWidgetProvider;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.APP.PARTNER;
import com.linchpin.periodtracker.utlity.ConstantsKey;
import com.linchpin.periodtracker.utlity.ExportDataBase;
import com.linchpin.periodtracker.utlity.GetPurchaseItems;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.RestoreAndBackUp;
import com.linchpin.periodtracker.utlity.SetNotificationAlarmServices;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.utlity.ValidationFunction;
import com.linchpin.periodttracker.database.DashBoardDBHandler;
import com.linchpin.periodttracker.database.ImportDatabase;
import com.sbstrm.appirater.Appirater;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageResponseBuilder;

public class HomeScreenView extends Activity implements OnClickListener
{
	RelativeLayout						bCalender, bAddnotes, bPeriodlogs, bSettings, bPartnerSharing;
	LinearLayout						bDayleft;
	TextView							tDayLeftText;
	TextView							tAboutdays, tNextFertileDays, tNextPeriodDays, tPredicitionPeriod, tPredictionFertile;
	PeriodTrackerObjectLocator			Gobl;
	DatePickerDialog					startDatePickerDialog, endDatePickerDailog;
	int									year, month, day, days;
	AlertDialog.Builder					builder;
	IInAppBillingService				mService;
	boolean								isFertile	= false, isOvulation = false, isReagularDay = false;
	Date								startDate, endDate;
	List<PeriodTrackerModelInterface>	periodTrackerModelInterfaces;
	PeriodTrackerModelInterface			perdictionPeriodLog, perdictionFertileRecord;
	DashBoardDBHandler					dashBoardDBHandler;
	PeriodLogModel						periodLogModelPerdiction, periodLogModel, previousPeriodlog;
	SimpleDateFormat					dateFormat;
	Theme								t;
	
	private void applyTheme()
	{
		
		t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			t.applyBackgroundDrawable(R.id.add_note, "mpt_circle_item_bg_sltr");
			t.applyBackgroundDrawable(R.id.calendar, "mpt_circle_item_bg_sltr");
			t.applyBackgroundDrawable(R.id.timeline_btn, "mpt_circle_item_bg_sltr");
			t.applyBackgroundDrawable(R.id.settings, "mpt_circle_item_bg_sltr");
			t.applyBackgroundDrawable(R.id.period_logs, "mpt_circle_item_bg_sltr");
			t.applyBackgroundDrawable(R.id.actuallayout, "hollimg");
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundDrawable(R.id.dayleft, "start_end");
			
			t.applyBackgroundDrawable(R.id.toplayout, "top_bg_calendar");
			
			t.applyTextColor(R.id.lbl_addnot, "co_circle_item_text");
			t.applyTextColor(R.id.lbl_calender, "co_circle_item_text");
			t.applyTextColor(R.id.lbl_periodlog, "co_circle_item_text");
			t.applyTextColor(R.id.lbl_setting, "co_circle_item_text");
			t.applyTextColor(R.id.lbl_timeline, "co_circle_item_text");
			t.applyTextColor(R.id.about_days, "co_hollimage_fg");
			t.applyTextColor(R.id.next_fertile_Date, "co_fertile_window");
			t.applyTextColor(R.id.prediction_fertile_date, "co_fertile_window_bot");
			t.applyTextColor(R.id.next_period_date, "co_next_perood");
			t.applyTextColor(R.id.prediction_period_date, "co_prediction_perood");
			
		}
	}
	
	private void findAllViews()
	{
		dateFormat = new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		bAddnotes = (RelativeLayout) findViewById(R.id.myNotesRelativeLayout);
		bCalender = (RelativeLayout) findViewById(R.id.calendarrealativelayout);
		bPartnerSharing = (RelativeLayout) findViewById(R.id.myPartnerRelativeLayout);
		bDayleft = (LinearLayout) findViewById(R.id.dayleft);
		tDayLeftText = (TextView) findViewById(R.id.daylefttext);
		bPeriodlogs = (RelativeLayout) findViewById(R.id.peroiodlogrelative);
		bSettings = (RelativeLayout) findViewById(R.id.settingsrelativelayout);
		tAboutdays = (TextView) findViewById(R.id.about_days);
		tNextFertileDays = (TextView) findViewById(R.id.next_fertile_Date);
		tNextPeriodDays = (TextView) findViewById(R.id.next_period_date);
		tPredicitionPeriod = (TextView) findViewById(R.id.prediction_period_date);
		tPredictionFertile = (TextView) findViewById(R.id.prediction_fertile_date);
	}
	
	public void setClickListner()
	{
		bAddnotes.setOnClickListener(this);
		bPeriodlogs.setOnClickListener(this);
		bCalender.setOnClickListener(this);
		bSettings.setOnClickListener(this);
		bDayleft.setOnClickListener(this);
		findViewById(R.id.homescreenhelpbutton).setOnClickListener(this);
		bPartnerSharing.setOnClickListener(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);
		findAllViews();
		setClickListner();
		applyTheme();
		Appirater.appLaunched(this);
		if ((Utility.setHourMinuteSecondZero(new Date()).after(Utility.addDays(new Date(this.getSharedPreferences("com.linchpin.periodtracker", 0).getLong("InstallDate", PeriodTrackerConstants.NULL_DATE)), 30))
				&& !this.getSharedPreferences("com.linchpin.periodtracker", 0).getBoolean("purchased", false) && !APP.purchaseAppsMszShowOnltOnce)
				&& ((APP.GLOBAL().getPreferences().getInt("frequency", 0)+1) % 5 == 0))
		{
			showMessageAlertForPurchase();
		}
		APP.GLOBAL().getEditor().putInt("frequency", (APP.GLOBAL().getPreferences().getInt("frequency", 0) + 1)).commit();
		if (APP.GLOBAL().getPreferences().getBoolean(APP.PREF.NEW_FETURE.key + "Timeline", true)) findViewById(R.id.myPartnerRelativeLayout).findViewById(R.id.includenewfesturer).setVisibility(View.VISIBLE);
		if (APP.GLOBAL().getPreferences().getBoolean(APP.PREF.NEW_FETURE.key + "Setting", true)) findViewById(R.id.settingsrelativelayout).findViewById(R.id.includenewfesturer).setVisibility(View.VISIBLE);
		int id = APP.GLOBAL().getPreferences().getInt(APP.PREF.ALARM_NITIFICATION_ID.key, 0);
		if (id != 0)
		{
			PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);
			startAlarmServices();
		}
		else startAlarmServices();
		resolveIntent(getIntent());
		Locale locale = new Locale(PeriodTrackerObjectLocator.getInstance().getAppLanguage());
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = locale;
		res.updateConfiguration(conf, dm);
		bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"), mServiceConn, Context.BIND_AUTO_CREATE);
		initLayout();
		
		if (getIntent().getBooleanExtra("EXIT", false))
		{
			int process = Process.myPid();
			Process.killProcess(process);
			System.exit(0);
		}
		
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		resolveIntent(getIntent());
	}
	
	private void resolveIntent(Intent intent)
	{
		if (intent != null)
		{
			String callAction = intent.getAction();
			if (!TextUtils.isEmpty(callAction))
			{
				if (callAction.equalsIgnoreCase("android.intent.action.VIEW"))
				{
					String action = getIntent().getAction();
					String type = getIntent().getType();
					if (Intent.ACTION_VIEW.equals(action) && type != null)
					{
						if (getIntent().getDataString().contains(".mpt")) showMessageonImport();
						else
						{
							Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.unsupportedfile), Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
							toast.show();
						}
						
					}
				}
			}
		}
	}
	
	public void startAlarmServices()
	{
		
		Intent intent = new Intent(getApplicationContext(), SetNotificationAlarmServices.class);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, 30);
		AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
		int id = (int) System.currentTimeMillis();		
		intent.putExtra("id", id);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);		
		APP.GLOBAL().getEditor().putInt(APP.PREF.ALARM_NITIFICATION_ID.key, id).commit();
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, false).commit();
	}
	
	@Override
	public void onBackPressed()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenView.this);
		builder.setTitle(getResources().getString(R.string.notifiction));
		builder.setMessage(getResources().getString(R.string.exitmessage));
		builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				finish();
				
			}
		});
		
		builder.setNegativeButton(getResources().getString(R.string.no), null);
		builder.show();
	}
	
	public void showMessageAlertForPurchase()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenView.this);
		builder.setTitle(R.string.butfullversion);
		builder.setMessage(getString(R.string.purchasemessage));
		APP.purchaseAppsMszShowOnltOnce = true;
		builder.setPositiveButton(getResources().getString(R.string.buy), new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				new GetPurchaseItems(HomeScreenView.this, mService).execute();
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.cancel), null);
		builder.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1001)
		{
			
			{
				
				if (resultCode == RESULT_OK)
				{
					if (null != data)
					{
						try
						{
							String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
							JSONObject jo = new JSONObject(purchaseData);
							String sku = jo.getString("productId");
							Toast.makeText(HomeScreenView.this, getResources().getString(R.string.youhavebought) + " ", Toast.LENGTH_LONG).show();
							
							HomeScreenView.this.getSharedPreferences("com.linchpin.periodtracker", 0).edit().putBoolean("purchased", true).commit();
							HomeScreenView.this.getSharedPreferences("com.linchpin.periodtracker", 0).edit().putString("purchased_item_json", purchaseData).commit();
							// AdView ad_view = (AdView)
							// findViewById(R.id.adView);
							findViewById(R.id.rlbottom).setVisibility(View.GONE);
							APP.GLOBAL().getEditor().putBoolean(APP.PREF.PURCHASED.key, true).commit();
							findViewById(R.id.rlbottom).setVisibility(View.GONE);
							Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
							HomeScreenView.this.finish();
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(i);
						}
						catch (JSONException e)
						{
							Toast.makeText(HomeScreenView.this, getResources().getString(R.string.failedinpurchase), Toast.LENGTH_LONG).show();
							
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	public void showMessageonImport()
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenView.this);
		builder.setTitle(getResources().getString(R.string.warning));
		builder.setMessage(getResources().getString(R.string.messageonimport));
		builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				if (PeriodTrackerObjectLocator.getInstance().getPasswordProtection().equals("") || PeriodTrackerObjectLocator.getInstance().getPasswordProtection().equals(null))
				{
					new RestoreAndBackUp(HomeScreenView.this, getIntent().getData().getPath()).execute();
				}
				else
				{
					showAlertforpassword();
				}
				
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.cancel), null);
		builder.show();
	}
	
	/*	private boolean isApplicationBroughtToBackground()
		{
			ActivityManager am = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> tasks = am.getRunningTasks(1);
			if (!tasks.isEmpty())
			{
				ComponentName baseActivity = tasks.get(0).baseActivity;
				if (!baseActivity.getPackageName().equals(getBaseContext().getPackageName())) { return true; }
			}
			
			return false;
		}*/
	
	@Override
	protected void onStart()
	{
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
		if (getIntent().getBooleanExtra("fromwidget", false))
		{
			Tracker tracker = GoogleAnalytics.getInstance(this).getTracker("UA-54563519-3");
			HashMap<String, String> hitParameters = new HashMap<String, String>();
			hitParameters.put(Fields.HIT_TYPE, "AppWidget Clicked");
			hitParameters.put(Fields.SCREEN_NAME, "AppWidget");
			tracker.send(hitParameters);
		}
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	
	private void setAboutText(PeriodLogModel periodLogModel)
	{
		if (PeriodTrackerObjectLocator.getInstance().PregnancyMessageFormat().equals(getResources().getString(R.string.daystobaby)))
		{
			if (Utility.dueDaysInPregnancy(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) > 0)
			{
				tAboutdays.setText("" + Utility.dueDaysInPregnancy(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + "\n" + getResources().getString(R.string.daysLeftinbirth));
			}
			else
			{
				tAboutdays.setText("" + Utility.lateDaysInPregnancy(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + getResources().getString(R.string.daysLateinbirth));
			}
		}
		else
		{
			tAboutdays.setText("" + Utility.getdaysfromstartofPregnancyIntheformWeaks(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + getResources().getString(R.string.sincepregnancy));
			
		}
	}
	
	public void initLayout()
	{
		try
		{
			AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
			ComponentName widgetComponent = new ComponentName(this, MPTAppWidgetProvider.class);
			int[] widgetIds = widgetManager.getAppWidgetIds(widgetComponent);
			Intent update = new Intent();
			update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
			update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			sendBroadcast(update);
			isReagularDay = false;
			tPredictionFertile.setText("");
			tDayLeftText.setText(getResources().getString(R.string.period_start_date));
			dashBoardDBHandler = new DashBoardDBHandler(getApplicationContext());
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
							if (PeriodTrackerObjectLocator.getInstance().PregnancyMessageFormat().equals(getResources().getString(R.string.daystobaby))) tAboutdays.setText(""
									+ Utility.dueDaysInPregnancyWhenKnownEstimateddate((PeriodTrackerObjectLocator.getInstance().getEstimatedDeliveryDate()), new Date()) + " " + getResources().getString(R.string.daysLeftinbirth));
							else tAboutdays.setText("" + Utility.getdaysfromstartofPregnancyIntheformWeaks(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + getResources().getString(R.string.sincepregnancy));
							
						}
					}
					tNextFertileDays.setText("");
					tPredictionFertile.setText("");
					tNextPeriodDays.setText(getResources().getString(R.string.congratulations));
					tPredicitionPeriod.setText(getResources().getString(R.string.youarepregnant));
					bDayleft.setVisibility(View.GONE);
				}
				else
				{
					if (periodLogModel.getEndDate().getTime() == PeriodTrackerConstants.NULL_DATE)
					{
						tDayLeftText.setText(getResources().getString(R.string.period_end_date));
						whichdaysIsToday(periodTrackerModelInterfaces);
					}
					else
					{
						bDayleft.setVisibility(View.VISIBLE);
						whichdaysIsToday(periodTrackerModelInterfaces);
						
					}
					
				}
			}
			else
			{
				tAboutdays.setText("");
				tNextFertileDays.setText("");
				tNextFertileDays.setText("");
				tNextPeriodDays.setText(getResources().getString(R.string.welcome));
				tPredicitionPeriod.setText(getResources().getString(R.string.pleasetapperiodlogbutton));
				tPredicitionPeriod.setTextSize(16);
				tPredicitionPeriod.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				bDayleft.setVisibility(View.VISIBLE);
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		try
		{
			if (APP.PARTNER_HOME == APP.PartnerHome.SENDER)
			{
				if (APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true))
				{
					JSONObject jsonObject = new JSONObject();
					ExportDataBase exportConstants = new ExportDataBase(HomeScreenView.this, PeriodTrackerObjectLocator.getInstance().getApplicationVersion());
					jsonObject = exportConstants.getFinalExportData();
					
					JSONObject jsonBody0 = new JSONObject();
					
					jsonBody0.put("userid", APP.currentUser.userName);
					jsonBody0.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
					jsonBody0.put("appdata", jsonObject);
					
					APP.getCustomCodeService().runJavaCode("UploadData", jsonBody0, new App42CallBack()
					{
						
						@Override
						public void onException(Exception arg0)
						{
							System.out.println("");
							
						}
						
						@Override
						public void onSuccess(Object arg0)
						{
							System.out.println("");
							
						}
					});
					APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, false).commit();
				}
				
			}
		}
		catch (JSONException e)
		{
		}
		catch (Exception e)
		{
		}
		initLayout();
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PURCHASED.key, false)) PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		
	}
	
	public void whichdaysIsToday(List<PeriodTrackerModelInterface> modelInterface)
	{
		
		if (modelInterface.size() > 0 && !((PeriodLogModel) modelInterface.get(0)).isPregnancy())
		{
			
			perdictionPeriodLog = dashBoardDBHandler.getPredictionLogs();
			perdictionFertileRecord = dashBoardDBHandler.getPerdictionFertileDatesAndOvulationDates();
			periodLogModelPerdiction = (PeriodLogModel) perdictionPeriodLog;
			tNextPeriodDays.setText(getResources().getString(R.string.nextperiod));
			
			tPredicitionPeriod.setText(dateFormat.format(periodLogModelPerdiction.getStartDate()));
			
			tNextFertileDays.setTextSize(15f);
			tNextFertileDays.setPadding(3, 3, 3, 3);
			tPredictionFertile.setVisibility(View.VISIBLE);
			tNextFertileDays.setText(getResources().getString(R.string.nextFertiledays));
			tPredictionFertile.setText(dateFormat.format(((PeriodLogModel) perdictionFertileRecord).getFertileStartDate()));
			
			periodLogModel = (PeriodLogModel) dashBoardDBHandler.getPastFertileAndOvulationDates();
			days = Utility.calculateDayofperiod(periodLogModel.getStartDate());
			
			printTextMessage(periodLogModel, periodLogModelPerdiction, days);
			
		}
		else
		{
			if (modelInterface.size() == 0)
			{
				tAboutdays.setText("");
				tNextFertileDays.setText("");
				tNextPeriodDays.setText(getResources().getString(R.string.welcome));
				
				tPredicitionPeriod.setText(getResources().getString(R.string.pleasetapperiodlogbutton));
				tPredicitionPeriod.setTextSize(16);
				tPredicitionPeriod.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				
				tPredictionFertile.setText("");
			}
			else
			{
				
				tAboutdays.setText("");
				tNextFertileDays.setText("");
				tNextPeriodDays.setText("");
				tPredicitionPeriod.setText("");
				tPredictionFertile.setText("");
				
			}
		}
		
	}
	
	public void printTextMessage(PeriodLogModel periodLogModel, PeriodLogModel periodLogModelPerdiction, int days)
	{
		
		if (Utility.setHourMinuteSecondZero(new Date()).before(periodLogModel.getFertileStartDate()))
		{
			tNextFertileDays.setTextSize(15f);
			tNextFertileDays.setPadding(3, 3, 3, 3);
			tPredictionFertile.setVisibility(View.VISIBLE);
			tNextFertileDays.setText(getResources().getString(R.string.nextFertiledays));
			tPredictionFertile.setText(dateFormat.format(periodLogModel.getFertileStartDate()));
			dayLeftInNextPeriod(false);
		}
		else if (Utility.setHourMinuteSecondZero(periodLogModel.getOvulationDate()).equals(Utility.setHourMinuteSecondZero(new Date())))
		{
			tNextFertileDays.setTextSize(22f);
			tNextFertileDays.setPadding(30, 20, 20, 10);
			tNextFertileDays.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
			tNextFertileDays.setText(getResources().getString(R.string.ovulation_day));
			
			tPredictionFertile.setText("");
			isOvulation = true;
			dayLeftInNextPeriod(false);
		}
		else if (!Utility.checkDateBetweenDates(periodLogModel.getFertileStartDate(), periodLogModel.getFertileEndDate(), Utility.setHourMinuteSecondZero(new Date())))
		{
			isFertile = true;
			tNextFertileDays.setTextSize(22f);
			tNextFertileDays.setPadding(30, 20, 20, 10);
			tNextFertileDays.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
			tNextFertileDays.setText(getResources().getString(R.string.fertileday));
			
			tPredictionFertile.setVisibility(View.GONE);
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
			tAboutdays.setText(Utility.leftdaysInPeriod(new Date(), periodLogModel.getStartDate()) + " " + getResources().getString(R.string.leftdays));
		}
		else
		{
			if (days < 7)
			{
				if (days <= PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength()) addSuffixOnPeriodDayMessage(days);
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
	
	public void lateDayMessage()
	{
		int days = Utility.lateDaysInNextPeriod(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date()));
		switch (days)
		{
			case 0:
				tAboutdays.setText(getResources().getString(R.string.periodstarttoday));
				break;
			case 1:
				tAboutdays.setText(days + " " + getResources().getString(R.string.daylate));
				break;
			default:
				tAboutdays.setText(days + " " + getResources().getString(R.string.dayslate));
				break;
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
					tAboutdays.setText(days + " " + getResources().getString(R.string.dayleftinnextperiod));
					tNextFertileDays.setTextSize(22f);
					tNextFertileDays.setPadding(30, 20, 20, 10);
					tNextFertileDays.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
					tNextFertileDays.setText(getResources().getString(R.string.regularday));
					tPredictionFertile.setText("");
				}
				else tAboutdays.setText(days + " " + getResources().getString(R.string.daysleftinnextperiod));
				break;
			default:
				if (isRegularDay)
				{
					if (days != d)
					{
						tAboutdays.setText(days + " " + getResources().getString(R.string.daysleftinnextperiod));
					}
					else
					{
						tAboutdays.setText("1" + Html.fromHtml("<sub><small>" + getResources().getString(R.string.stdayofperiod) + "</small></sub>") + " " + getResources().getString(R.string.dayofperiod));
						
					}
					tNextFertileDays.setTextSize(22f);
					tNextFertileDays.setPadding(30, 20, 20, 10);
					tNextFertileDays.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
					tNextFertileDays.setText(getResources().getString(R.string.regularday));
					tPredictionFertile.setVisibility(View.GONE);
				}
				else
				{
					if (days != d)
					{
						tAboutdays.setText(days + " " + getResources().getString(R.string.daysleftinnextperiod));
					}
					else
					{
						tAboutdays.setText("1" + Html.fromHtml("<sub><small>" + getResources().getString(R.string.stdayofperiod) + "</small></sub>") + " " + getResources().getString(R.string.dayofperiod));
						
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
				tAboutdays.setText("" + days + Html.fromHtml("<sub><small>" + getResources().getString(R.string.stdayofperiod) + "</small></sub>") + " " + getResources().getString(R.string.dayofperiod));
				break;
			
			case 2:
				tAboutdays.setText("" + days + Html.fromHtml("<sub><small>" + getResources().getString(R.string.nddayofperiod) + "</small></sub>") + " " + getResources().getString(R.string.dayofperiod));
				break;
			
			case 3:
				tAboutdays.setText("" + days + Html.fromHtml("<sub><small>" + getResources().getString(R.string.rddayofperiod) + "</small></sub>") + " " + getResources().getString(R.string.dayofperiod));
				break;
			
			default:
				tAboutdays.setText("" + days + Html.fromHtml("<sub><small>" + getResources().getString(R.string.thdayofperiod) + "</small></sub>") + " " + getResources().getString(R.string.dayofperiod));
				break;
		}
		
	}
	
	public DatePickerDialog.OnDateSetListener	startdatePickerListener	= new DatePickerDialog.OnDateSetListener()
																		{
																			// while dialog box is closed, below method is called.
																			
																			@Override
																			public void onDateSet(DatePicker view, int id, int year, int monthOfYear, int dayOfMonth)
																			{
																				if (year != 12345 && monthOfYear != 12345)
																				{
																					startDate = Utility.createDate(year, monthOfYear, dayOfMonth);
																					if (periodTrackerModelInterfaces.size() > 0)
																					{
																						periodLogModel = (PeriodLogModel) periodTrackerModelInterfaces.get(0);
																						if (!Utility.isDateLessThanLatestRecord(startDate, periodLogModel.getStartDate()))
																						{
																							int value = ValidationFunction.checkAllValidationForStartDate(HomeScreenView.this, startDate, startDatePickerDialog, periodTrackerModelInterfaces, false);
																							if (value == 2)
																							{
																								insertStartDateInDatabase(startDate);
																							}
																						}
																						else
																						{
																							Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.addpastrecord), Toast.LENGTH_LONG);
																							toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
																							toast.show();
																						}
																					}
																					else
																					{
																						insertStartDateInDatabase(startDate);
																					}
																				}
																			}
																		};
	DatePickerDialog.OnDateSetListener			endDatePickerListener	= new DatePickerDialog.OnDateSetListener()
																		{
																			
																			@Override
																			public void onDateSet(DatePicker view, int id, final int year, int monthOfYear, int dayOfMonth)
																			{
																				if (year != 12345 && monthOfYear != 12345)
																				{
																					endDate = Utility.createDate(year, monthOfYear, dayOfMonth);
																					periodLogModel = (PeriodLogModel) periodTrackerModelInterfaces.get(0);
																					int value = ValidationFunction.checkValidityForEndDate(HomeScreenView.this, periodLogModel.getStartDate(), endDate, endDatePickerDailog, periodTrackerModelInterfaces);
																					if (value == 1)
																					{
																						UpdateEndDateForStartDate(endDate);
																					}
																				}
																			}
																		};
	
	public void insertStartDateInDatabase(Date date)
	{
		dashBoardDBHandler = new DashBoardDBHandler(getApplicationContext());
		date = Utility.setHourMinuteSecondZero(date);
		periodLogModel = new PeriodLogModel(0, PeriodTrackerObjectLocator.getInstance().getProfileId(), date, null, 0, 0, false, false);
		dashBoardDBHandler.addPeriodLog(periodLogModel);
		tDayLeftText.setText(getResources().getString(R.string.period_end_date));
		//tAboutdays.setText(tAboutdays.getText().toString());
		initLayout();
	}
	
	public void UpdateEndDateForStartDate(Date endDate)
	{
		dashBoardDBHandler = new DashBoardDBHandler(getApplicationContext());
		periodTrackerModelInterfaces = dashBoardDBHandler.getAllLogs();
		endDate = Utility.setHourMinuteSecondZero(endDate);
		if (periodTrackerModelInterfaces.size() > 0)
		{
			periodLogModel = (PeriodLogModel) periodTrackerModelInterfaces.get(0);
			periodLogModel.setEndDate(endDate);
		}
		if (dashBoardDBHandler.updatePeriodLog(periodLogModel))
		{
			initLayout();
		}
	}
	
	public void showAlertforpassword()
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenView.this);
		final View view = LayoutInflater.from(this).inflate(R.layout.enter_password, null);
		builder.setView(view);
		final AlertDialog alertDialog = builder.create();
		EditText editText = (EditText) view.findViewById(R.id.forgotpasswordsecurity);
		final EditText editText2 = (EditText) view.findViewById(R.id.fortgotpasswordanswer);
		editText.setVisibility(view.GONE);
		view.findViewById(R.id.text).setVisibility(View.GONE);
		view.findViewById(R.id.text1).setVisibility(View.GONE);
		view.findViewById(R.id.forgotpasswordtiltle).setVisibility(View.GONE);
		editText2.setHint(getResources().getString(R.string.passcode));
		
		Button ok = (Button) view.findViewById(R.id.popok);
		ok.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (editText2.getText().toString().equals(PeriodTrackerObjectLocator.getInstance().getPasswordProtection()))
				{
					alertDialog.cancel();
					new RestoreAndBackUp(getApplicationContext(), "mail").execute();
					
				}
				else
				{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.mismatchpasscode), Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		Button cancel = (Button) view.findViewById(R.id.popcancel);
		cancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}
	
	ServiceConnection	mServiceConn	= new ServiceConnection()
										{
											@Override
											public void onServiceConnected(ComponentName name, IBinder service)
											{
												mService = IInAppBillingService.Stub.asInterface(service);
											}
											
											@Override
											public void onServiceDisconnected(ComponentName name)
											{
												mService = null;
											}
										};
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		Intent intent;
		if (id == R.id.dayleft)
		{
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
			{
				
				if (periodTrackerModelInterfaces.size() > 0)
				{
					periodLogModel = (PeriodLogModel) periodTrackerModelInterfaces.get(0);
					Date expectedendDate = Utility.addDays(periodLogModel.getStartDate(), PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
					if (!expectedendDate.after(Utility.setHourMinuteSecondZero(new Date()))) calendar.setTime(expectedendDate);
					if (periodLogModel.getEndDate().getTime() == PeriodTrackerConstants.NULL_DATE)
					{
						
						endDatePickerDailog = new DatePickerDialog(HomeScreenView.this, endDatePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
						if (Utility.checkAndroidApiVersion())
						{
							endDatePickerDailog.getDatePicker().setMinDate(Utility.setHourMinuteSecondZero(new Date()).getTime() - 3640 * PeriodTrackerConstants.MILLI_SECONDS);
							endDatePickerDailog.getDatePicker().setMaxDate(Utility.setHourMinuteSecondZero(Utility.setHourMinuteSecondZero(new Date())).getTime());
						}
						endDatePickerDailog.setTitle(getResources().getString(R.string.setenddate) + "\n" + getResources().getString(R.string.start_date) + "-" + dateFormat.format(periodLogModel.getStartDate()));
						endDatePickerDailog.show();
						
					}
					else
					{
						
						startDatePickerDialog = new DatePickerDialog(HomeScreenView.this, startdatePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
						
						if (Utility.checkAndroidApiVersion())
						{
							
							startDatePickerDialog.getDatePicker().setMaxDate(Utility.setHourMinuteSecondZero(Utility.setHourMinuteSecondZero(new Date())).getTime());
						}
						startDatePickerDialog.setTitle(getResources().getString(R.string.setstartdate));
						startDatePickerDialog.show();
					}
				}
				else
				{
					startDatePickerDialog = new DatePickerDialog(HomeScreenView.this, startdatePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
					if (Utility.checkAndroidApiVersion())
					{
						startDatePickerDialog.getDatePicker().setMinDate(Utility.setHourMinuteSecondZero(new Date()).getTime() - 3640 * PeriodTrackerConstants.MILLI_SECONDS);
						startDatePickerDialog.getDatePicker().setMaxDate(Utility.setHourMinuteSecondZero(new Date()).getTime());
					}
					startDatePickerDialog.setTitle(getResources().getString(R.string.setstartdate));
					startDatePickerDialog.show();
				}
			}
			else
			{
				
				Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.pregnancy), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			}
		}
		else if (id == R.id.myPartnerRelativeLayout)
		{
			if (APP.currentUser != null && APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null) != null)
			{
				if (APP.PARTNER_HOME == APP.PartnerHome.RECEVER)
				{
					JSONObject request = new JSONObject();
					try
					{
						request.put("userid", APP.currentUser.getUserName());
						request.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
						JSONObject res = null;
						APP.getCustomCodeService().runJavaCode("GetPartnerData", request, new App42CallBack()
						{
							
							@Override
							public void onSuccess(Object arg0)
							{
								JSONObject res = (JSONObject) arg0;
								Storage storage;
								try
								{
									storage = (new StorageResponseBuilder()).buildResponse(res.getString("data"));
									res = new JSONObject(storage.getJsonDocList().get(0).jsonDoc);
									APP.GLOBAL().getEditor().putString("partnerdata", res.toString()).commit();
									ConstantsKey constantsKey = new ConstantsKey("1.0");
									APP.GLOBAL().getPartnerDDModels().clear();
									try
									{
										APP.GLOBAL().getPartnerDDModels().addAll(ImportDatabase.getDayDetailforPartner(res.getJSONObject("appdata").getJSONArray(constantsKey.getKey(ConstantsKey.Day_Detail))));
									}
									catch (JSONException e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									try
									{
										APP.GLOBAL().storePartnerLengths(res.getJSONObject("appdata").getJSONObject(PARTNER.LENGTH_TABLE.key));
									}
									catch (JSONException e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									APP.GLOBAL().getPartnerPLModels().clear();
									try
									{
										APP.GLOBAL().getPartnerPLModels().addAll(ImportDatabase.getPeriodlogTableBackup(res.getJSONObject("appdata").getJSONArray(constantsKey.getKey(ConstantsKey.PERIOD_TRACK))));
									}
									catch (JSONException e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									TimeLineModel.createModel();
									startActivity(new Intent(HomeScreenView.this, PartnerHomeActivity.class));
									//	if(APP.currentUser.getProfile().getSex().equals("Male"))
									finish();
									APP.GLOBAL().exicuteRIOAnim(HomeScreenView.this);
								}
								
								catch (Exception e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							
							@Override
							public void onException(Exception arg0)
							{
								startActivity(new Intent(HomeScreenView.this, PartnerHomeActivity.class));
								//if(APP.currentUser.getProfile().getSex().equals("Male"))
								finish();
								APP.GLOBAL().exicuteRIOAnim(HomeScreenView.this);
								
							}
						});
						
					}
					catch (JSONException e)
					{
						
					}
				}
				else
				{
					startActivity(new Intent(HomeScreenView.this, PartnerHomeActivity.class));
					//if(APP.currentUser.getProfile().getSex().equals("Male"))
					finish();
					
					APP.GLOBAL().exicuteRIOAnim(this);
				}
				
			}
			else
			{
				startActivity(new Intent(HomeScreenView.this, LoginScreen.class));
				finish();
				APP.GLOBAL().exicuteRIOAnim(this);
			}
		}
		else if (id == R.id.settingsrelativelayout)
		{
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEW_FETURE.key + "Setting", false).commit();
			 intent = new Intent(HomeScreenView.this, SettingsView.class);
			startActivity(intent);
			APP.GLOBAL().exicuteRIOAnim(HomeScreenView.this);
		}
		else if (id == R.id.homescreenhelpbutton)
		{
			intent = new Intent(HomeScreenView.this, HomeScreenHelp.class);
			if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode()) intent.putExtra("classname", "dashboard");
			else intent.putExtra("classname", "dashbordpregnancy");
			startActivity(intent);
		}
		else if (id == R.id.myNotesRelativeLayout)
		{
			intent = new Intent(this, AddNoteView.class);
			intent.putExtra("date", PeriodTrackerConstants.CURRENT_DATE.getTime());
			startActivity(intent);
			APP.GLOBAL().exicuteRIOAnim(this);
		}
		else if (id == R.id.calendarrealativelayout)
		{
			intent = new Intent(this, CaldroidActivity.class);
			startActivity(intent);
			APP.GLOBAL().exicuteRIOAnim(this);
		}
		else if (id == R.id.peroiodlogrelative)
		{
			intent = new Intent(this, PeriodLogPagerView.class);
			startActivity(intent);
			APP.GLOBAL().exicuteRIOAnim(this);
		}
		else
		{
		}
		
	}
	
}
