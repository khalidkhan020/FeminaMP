package com.linchpin.periodtracker.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.Expendable_Welcome_Setting_Adapter;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.NotificattionModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.widget.BackBar;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;
import com.linchpin.periodttracker.database.DBManager;
import com.linchpin.periodttracker.database.NotificationDBHandler;

public class Welcome_Setting extends Activity
{
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	Button	btncontinue;
	
	private void applyTheme()
	{
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			t.applyTextColor(R.id.h1, "heading_fg");
			t.applyBackgroundColor(R.id.h1, "heading_bg");
			t.applyBackgroundColor(R.id.continue1, "heading_fg");
			t.applyTextColor(R.id.continue1, "text_color");
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			
		}
		
	}
	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		Intent setbackActivity = new Intent(Welcome_Setting.this, PersonalSettingView.class);
		startActivity(setbackActivity);
		finish();
		APP.GLOBAL().exicuteLIOAnim(this);
		super.onBackPressed();
	}
	
	private String[]	values;
	private String[]	entries;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPostResume()
	 */
	@Override
	protected void onPostResume()
	{
		
		// TODO Auto-generated method stub
		super.onPostResume();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume()
	{
		
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	Theme	t;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart()
	{
		EasyTracker.getInstance(this).activityStart(this);
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop()
	{
		EasyTracker.getInstance(this).activityStop(this);
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	ExpandableListAdapter			listAdapter;
	ExpandableListView				expListView;
	List<String>					listDataHeader;
	HashMap<String, List<String>>	listDataChildDetails;
	HashMap<String, List<String>>	listDataChild;
	HashMap<String, List<String>>	listDataChildUnit;
	BackBar							backbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcom_settings);
		backbar = (BackBar) findViewById(R.id.welcomesettingback);
		btncontinue = (Button) findViewById(R.id.continue1);
		btncontinue.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent congrets = new Intent(Welcome_Setting.this, CongretulationWizard.class);
				startActivity(congrets);
				finish();
				APP.GLOBAL().exicuteRIOAnim(Welcome_Setting.this);
			}
		});
		backbar.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				
				onBackPressed();
				// TODO Auto-generated method stub
				
			}
		});
		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		
		// preparing list data
		prepareListData();
		
		listAdapter = new Expendable_Welcome_Setting_Adapter(this, listDataHeader, listDataChild, listDataChildDetails);
		
		// setting list adapter
		expListView.setAdapter(listAdapter);
		expListView.expandGroup(0);
		expListView.setOnGroupExpandListener(new OnGroupExpandListener()
		{
			int	priviousgroup	= 0;
			
			@Override
			public void onGroupExpand(int arg0)
			{
				if (arg0 != priviousgroup) expListView.collapseGroup(priviousgroup);
				priviousgroup = arg0;
				// TODO Auto-generated method stub
				
			}
		});
		expListView.setOnChildClickListener(new OnChildClickListener()
		{
			
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int groupposition, final int childposition, long id)
			{
				// TODO Auto-generated method stub
				switch (groupposition)
				{
					case 0:
						if (childposition == 0) onClickHeight();
						else if (childposition == 1) onClickWeight();
						else if (childposition == 2) onClickTemperature();
						break;
					case 1:
					{
						int selectedPosition = -1;
						AlertDialog.Builder builder = new AlertDialog.Builder(Welcome_Setting.this);
						final String[] items =
						{
								getResources().getString(R.string.nonotificationrequired), getResources().getString(R.string.ontheday), " 1 " + getResources().getString(R.string.daybefore), " 2 " + getResources().getString(R.string.daysbefore), " 3 " + getResources().getString(R.string.daysbefore),
								" 4 " + getResources().getString(R.string.daysbefore)
						};
						if (childposition == 0)
						{
							selectedPosition = PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification();
							builder.setTitle(getResources().getString(R.string.setdaysforperiodnotifications));
						}
						else if (childposition == 1)
						{
							selectedPosition = PeriodTrackerObjectLocator.getInstance().getFertilityNotification();
							builder.setTitle(getResources().getString(R.string.setdaysforfetilenotifications));
						}
						else if (childposition == 2)
						{
							selectedPosition = PeriodTrackerObjectLocator.getInstance().getOvulationNotification();
							builder.setTitle(getResources().getString(R.string.setdaysforovualtionnotifications));
						}
						builder.setSingleChoiceItems(items, selectedPosition + 1, new DialogInterface.OnClickListener()
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								// TODO Auto-generated method stub
								
								dialog.dismiss();
								if (childposition == 0)
								{
									setVaulesAlertDaysForPeriodStart(which);
								}
								else if (childposition == 1)
								{
									setVaulesAlertDaysForFertileStart(which);
								}
								else if (childposition == 2)
								{
									setVaulesAlertDaysForOvulationStart(which);
								}
								((BaseExpandableListAdapter) listAdapter).notifyDataSetChanged();
							}
						});
						builder.show();
					}
						break;
					case 2:
						if (childposition == 0) onClickSelectHieghtUnit();
						else if (childposition == 1) onClickSelectWeightUnit();
						else if (childposition == 2) onClickSelectTempUnit();
						else if (childposition == 3) onClickOfChangeApplicationLanuage();
						else if (childposition == 4) OnClickDateFormat();
						break;
					case 3:
						if (childposition == 0)
						{
							Intent intent = new Intent(Welcome_Setting.this, ChangeAppIconView.class);
							startActivity(intent);
							overridePendingTransition(R.anim.left_in, R.anim.left_out);
							//							Tips.viewVisible(Welcome_Setting.this, APP.TipsPath.AppIcon.id);
						}
						if (childposition == 1)
						{
							onClickChangeDayOfWeek(getApplicationContext());
						}
						if (childposition == 2)
						{
							onClickTheme(getApplicationContext());
							APP.GLOBAL().getEditor().putBoolean("NewFeature_Apper_ChangeIcon", false).commit();
						}
						break;
					case 4:
						if (childposition == 0)
						{
							Intent intent = new Intent(Welcome_Setting.this, SetPassword.class);
							//							Tips.viewVisible(Welcome_Setting.this, APP.TipsPath.Passcode.id);
							if (intent != null) startActivity(intent);
							APP.GLOBAL().exicuteLIOAnim(Welcome_Setting.this);
						}
						break;
				
				}
				return true;
			}
		});
		applyTheme();
	}
	
	/*
	 * Preparing the list data
	 */
	private void prepareListData()
	{
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		listDataChildDetails = new HashMap<String, List<String>>();
		// Adding child data
		listDataHeader.add(getResources().getString(R.string.personalsettings));
		listDataHeader.add(getResources().getString(R.string.notificationsettings));
		listDataHeader.add(getResources().getString(R.string.regionalsettings));
		listDataHeader.add(getResources().getString(R.string.apperence));
		listDataHeader.add(getResources().getString(R.string.setpasscode));
		// Adding child data
		List<String> personalSetting = new ArrayList<String>();
		personalSetting.add(getResources().getString(R.string.hieght));
		personalSetting.add(getResources().getString(R.string.weight));
		personalSetting.add(getResources().getString(R.string.temprature));
		
		List<String> notificationSetting = new ArrayList<String>();
		notificationSetting.add(getResources().getString(R.string.period_alert));
		notificationSetting.add(getResources().getString(R.string.fertile_alert));
		notificationSetting.add(getResources().getString(R.string.ovulation_alert));
		
		List<String> rigionalSetting = new ArrayList<String>();
		rigionalSetting.add(getResources().getString(R.string.hieghtunit));
		rigionalSetting.add(getResources().getString(R.string.weightunit));
		rigionalSetting.add(getResources().getString(R.string.selecttempunit));
		rigionalSetting.add(getResources().getString(R.string.changelanugae));
		rigionalSetting.add(getResources().getString(R.string.changedateformat));
		
		List<String> appearanceSetting = new ArrayList<String>();
		appearanceSetting.add(getResources().getString(R.string.app_iconandname));
		appearanceSetting.add(getResources().getString(R.string.setdaysofweek));
		appearanceSetting.add(getResources().getString(R.string.theme));
		
		List<String> privacySetting = new ArrayList<String>();
		privacySetting.add(getResources().getString(R.string.setpasscode));
		
		List<String> personalSettingDetails = new ArrayList<String>();
		personalSettingDetails.add(getResources().getString(R.string.enterheight));
		personalSettingDetails.add(getResources().getString(R.string.enterweight));
		personalSettingDetails.add(getResources().getString(R.string.entertemprature));
		
		List<String> notificationSettingDetails = new ArrayList<String>();
		notificationSettingDetails.add(getResources().getString(R.string.setnotificationsperiodalert));
		notificationSettingDetails.add(getResources().getString(R.string.setnotificationsfertle_alert));
		notificationSettingDetails.add(getResources().getString(R.string.setnotificationsovulation_alert));
		
		List<String> rigionalSettingDetails = new ArrayList<String>();
		rigionalSettingDetails.add(getResources().getString(R.string.changedefaultlheightunit));
		rigionalSettingDetails.add(getResources().getString(R.string.changedefaultlweightunit));
		rigionalSettingDetails.add(getResources().getString(R.string.changedefaultlTempunit));
		rigionalSettingDetails.add(getResources().getString(R.string.changedefaultLanuage));
		rigionalSettingDetails.add(getResources().getString(R.string.changedefaultldateformat));
		
		List<String> appearanceSettingDetails = new ArrayList<String>();
		appearanceSettingDetails.add(getResources().getString(R.string.changeappiconandname));
		appearanceSettingDetails.add(getResources().getString(R.string.changedayofweekbelow));
		appearanceSettingDetails.add(getResources().getString(R.string.set_theme));
		
		List<String> privacySettingDetails = new ArrayList<String>();
		privacySettingDetails.add(getResources().getString(R.string.changepasscode));
		
		listDataChildDetails.put(listDataHeader.get(0), personalSettingDetails); // Header, Child data
		listDataChildDetails.put(listDataHeader.get(1), notificationSettingDetails);
		listDataChildDetails.put(listDataHeader.get(2), rigionalSettingDetails);
		listDataChildDetails.put(listDataHeader.get(3), appearanceSettingDetails);
		listDataChildDetails.put(listDataHeader.get(4), privacySettingDetails);
		
		listDataChild.put(listDataHeader.get(0), personalSetting); // Header, Child data
		listDataChild.put(listDataHeader.get(1), notificationSetting);
		listDataChild.put(listDataHeader.get(2), rigionalSetting);
		listDataChild.put(listDataHeader.get(3), appearanceSetting);
		listDataChild.put(listDataHeader.get(4), privacySetting);
	}
	
	public static void viewVisible(Activity context, int id)
	{
		//		ContentValues values = null;
		//		Cursor c = context.getContentResolver().query(DBManager.TIPS_ID_URI_BASE, null, DBManager.TIPS_FIELD_CONTEXT + "=?", new String[]
		//				{ id+"" }, null);
		//		if (c != null && c.getCount() > 0)
		//		{
		//			c.moveToFirst();
		//			values = new ContentValues();
		//			values.put(DBManager.TIPS_FIELD_FRIQUENCY, c.getInt(c.getColumnIndex(DBManager.TIPS_FIELD_FRIQUENCY)) + 1);
		//			int rowcount = context.getContentResolver().update(DBManager.TIPS_ID_URI_BASE, values, DBManager.TIPS_FIELD_CONTEXT + "=?", new String[]
		//					{ id+"" });
		//		}
		
	}
	
	public void onClickSelectWeightUnit()
	{
		
		int selectedPosition;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items =
		{
				getResources().getString(R.string.KG), getResources().getString(R.string.lb)
		};
		if (String.valueOf(PeriodTrackerObjectLocator.getInstance().getWeighUnit()).equalsIgnoreCase(getResources().getString(R.string.KG)))
		{
			selectedPosition = 0;
		}
		else
		{
			selectedPosition = 1;
		}
		
		builder.setTitle(getResources().getString(R.string.changedefaultlweightunit));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				if (which == 0)
				{
					setWeightUnit(getResources().getString(R.string.KG));
				}
				else
				{
					setWeightUnit(getResources().getString(R.string.lb));
				}
				((BaseExpandableListAdapter) listAdapter).notifyDataSetChanged();
				dialog.dismiss();
				
			}
		});
		builder.show();
		
	}
	
	public void onClickSelectHieghtUnit()
	{
		
		int selectedPosition;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items =
		{
				getResources().getString(R.string.CM), getResources().getString(R.string.inches)
		};
		if (String.valueOf(PeriodTrackerObjectLocator.getInstance().getHeightUnit()).equalsIgnoreCase(getResources().getString(R.string.CM)))
		{
			selectedPosition = 0;
		}
		else
		{
			selectedPosition = 1;
		}
		
		builder.setTitle(getResources().getString(R.string.changedefaultlheightunit));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				if (which == 0)
				{
					setHieghUnit(getResources().getString(R.string.CM));
				}
				else
				{
					setHieghUnit(getResources().getString(R.string.inches));
				}
				((BaseExpandableListAdapter) listAdapter).notifyDataSetChanged();
				dialog.dismiss();
				
			}
		});
		builder.show();
		
	}
	
	public void onClickSelectTempUnit()
	{
		
		int selectedPosition;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items =
		{
				getResources().getString(R.string.celsius), getResources().getString(R.string.Fehr)
		};
		if (String.valueOf(PeriodTrackerObjectLocator.getInstance().getTempUnit()).equalsIgnoreCase(getResources().getString(R.string.celsius)))
		{
			selectedPosition = 0;
		}
		else
		{
			selectedPosition = 1;
		}
		
		builder.setTitle(getResources().getString(R.string.bodytempratureunit));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				if (which == 0)
				{
					setTempUnit(getResources().getString(R.string.celsius));
				}
				else
				{
					setTempUnit(getResources().getString(R.string.Fehr));
				}
				((BaseExpandableListAdapter) listAdapter).notifyDataSetChanged();
				dialog.dismiss();
				
			}
		});
		builder.show();
		
	}
	
	private void onClickOfChangeApplicationLanuage()
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.changedefaultLanuage));
		final String[] items =
		{ // getResources().getString(R.string.croatian),
				getResources().getString(R.string.dutch), getResources().getString(R.string.english), getResources().getString(R.string.french), getResources().getString(R.string.german), getResources().getString(R.string.italian),
				// getResources().getString(R.string.japanese),
				getResources().getString(R.string.portuguese), getResources().getString(R.string.spanish)
		};
		
		int selectedPosition = Arrays.asList(items).indexOf(Utility.getLanguageFromLoacle(String.valueOf(PeriodTrackerObjectLocator.getInstance().getAppLanguage()), getApplicationContext()));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				Locale locale = null;
				String vaString = Utility.AppLanguageLocale(items[which], getApplicationContext());
				locale = new Locale(vaString);
				
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getApplicationContext());
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_APP_LANGUAGE_KEY, vaString, PeriodTrackerObjectLocator.getInstance().getProfileId());
				applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				
				Resources res = getApplicationContext().getResources();
				// Change locale settings in the app.
				DisplayMetrics dm = res.getDisplayMetrics();
				android.content.res.Configuration conf = res.getConfiguration();
				conf.locale = locale;
				res.updateConfiguration(conf, dm);
				dialog.dismiss();
				restartActivity();
				
			}
		});
		builder.show();
	}
	
	private void restartActivity()
	{
		
		Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		Welcome_Setting.this.finish();
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}
	
	public void OnClickDateFormat()
	{
		
		final AlertDialog.Builder builder;
		
		builder = new AlertDialog.Builder(this);
		
		builder.setTitle(getResources().getString(R.string.selectdateformat));
		
		final String[] patternItems =
		{
				"MM/dd/yyyy", "dd/MM/yyyy", "yyyy/dd/MM",
				
				"MM dd yyyy", "yyyy dd MM", "yyyy MM dd",
				
				"dd MMM, yyyy", "MMM dd, yyyy", "yyyy MMM dd"
		};
		
		final String[] items =
		{
				new SimpleDateFormat("MM/dd/yyyy").format(new Date()), new SimpleDateFormat("dd/MM/yyyy").format(new Date()), new SimpleDateFormat("yyyy/dd/MM").format(new Date()),
				
				new SimpleDateFormat("MM dd yyyy").format(new Date()), new SimpleDateFormat("yyyy dd MM").format(new Date()), new SimpleDateFormat("yyyy MM dd").format(new Date()),
				
				new SimpleDateFormat("dd MMM, yyyy").format(new Date()), new SimpleDateFormat("MMM dd, yyyy").format(new Date()), new SimpleDateFormat("yyyy MMM dd").format(new Date())
		};
		
		int selectedPosition = Arrays.asList(items).indexOf(new SimpleDateFormat(String.valueOf(PeriodTrackerObjectLocator.getInstance().getDateFormat())).format(new Date()));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getApplicationContext());
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_DATE_FORMAT_KEY, patternItems[which], PeriodTrackerObjectLocator.getInstance().getProfileId());
				applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				dialog.dismiss();
				((BaseExpandableListAdapter) listAdapter).notifyDataSetChanged();
				//initLayout();
			}
		});
		builder.show();
		
	}
	
	public void setWeightUnit(String Value)
	{
		try
		{
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_UNIT_KEY, Value, PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
			{
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				
			}
			//initLayout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setTempUnit(String Value)
	{
		
		try
		{
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMP_UNIT_KEY, Value, PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
			{
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				
			}
			//initLayout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void setHieghUnit(String value)
	{
		
		try
		{
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_UNIT_KEY, value, PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
			{
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				
			}
			//initLayout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void onClickHeight()
	{
		
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper(Welcome_Setting.this, android.R.style.Theme_DeviceDefault_Light);
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from(this).inflate(R.layout.forheigthandweightvalue, null);
		t = Theme.getCurrentTheme(Welcome_Setting.this, view);
		if (t != null)
		{
			t.applyBackgroundColor(R.id.heightandwightvalue, "view_bg");
			t.applyBackgroundColor(R.id.divider, "text_color");
			t.applyBackgroundColor(R.id.valuecancel, "text_color");
			t.applyBackgroundColor(R.id.setvalue, "text_color");
			t.applyBackgroundDrawable(R.id.heightweightvalue, "mpt_edit_text_sltr");
			
			t.applyTextColor(R.id.tileforhieghtandweight, "text_color");
			t.applyTextColor(R.id.heightweightvalue, "text_color");
			t.applyTextColor(R.id.text, "text_color");
			
		}
		builder.setView(view);
		final AlertDialog alertDialog = builder.create();
		TextView title = (TextView) view.findViewById(R.id.tileforhieghtandweight);
		title.setText(getResources().getString(R.string.hieght) + " in " + PeriodTrackerObjectLocator.getInstance().getHeightUnit());
		final EditText editText = (EditText) view.findViewById(R.id.heightweightvalue);
		
		if (PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(getResources().getString(R.string.CM)))
		{
			
			if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))
			{
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) != 0)
				{
					editText.setText(Utility.getStringFormatedNumber((PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule())));
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_HEIGHT_VALUE_IN_CM)));
					
				}
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_HEIGHT_VALUE_IN_CM)));
			}
		}
		else
		{
			
			if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))
			{
				
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) != 0)
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule())))));
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(PeriodTrackerConstants.DEFAULT_HEIGHT_VALUE_IN_CM))));
					
				}
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(PeriodTrackerConstants.DEFAULT_HEIGHT_VALUE_IN_CM))));
				
			}
		}
		Button set = (Button) view.findViewById(R.id.setvalue);
		Button cancel = (Button) view.findViewById(R.id.valuecancel);
		
		cancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		
		set.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (validateHieght(editText.getText().toString()))
				{
					setHieghtValue(editText.getText().toString());
					alertDialog.dismiss();
					((BaseExpandableListAdapter) listAdapter).notifyDataSetChanged();
				}
			}
		});
		
		alertDialog.show();
		
	}
	
	public boolean validateHieght(String hieghtVaule)
	{
		boolean validateHiegh = false;
		AlertDialog.Builder builder = new AlertDialog.Builder(Welcome_Setting.this);
		if (null != PeriodTrackerObjectLocator.getInstance().getHeightUnit() && PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(getResources().getString(R.string.CM)))
		{
			if (null != hieghtVaule && !hieghtVaule.equals(""))
			{
				if (Float.parseFloat(hieghtVaule) > PeriodTrackerConstants.MAX_HEIGHT_IN_CENTIMETER || (Float.parseFloat(hieghtVaule) < PeriodTrackerConstants.MIN_HEIGHT_IN_CENTIMETER))
				{
					validateHiegh = false;
					builder.setTitle(getResources().getString(R.string.invalidhieght));
					builder.setMessage(getResources().getString(R.string.invalidhieghtmessageincm));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				}
				else
				{
					validateHiegh = true;
				}
			}
		}
		else
		{
			if (null != hieghtVaule && !hieghtVaule.equals(""))
			{
				if (Float.parseFloat(hieghtVaule) > PeriodTrackerConstants.MAX_HEIGHT_IN_INCHES || (Float.parseFloat(hieghtVaule) < PeriodTrackerConstants.MIN_HEIGHT_IN_INCHES))
				{
					// Toast.makeText(getActivity(), "invalid hieght in inches",
					// Toast.LENGTH_SHORT).show();
					validateHiegh = false;
					builder.setTitle(getResources().getString(R.string.invalidhieght));
					builder.setMessage(getResources().getString(R.string.invalidhieghtmessageinInches));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					
					builder.show();
				}
				else
				{
					validateHiegh = true;
				}
			}
		}
		
		return validateHiegh;
	}
	
	public void onClickWeight()
	{
		
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper(Welcome_Setting.this, android.R.style.Theme_DeviceDefault_Light);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from(this).inflate(R.layout.forheigthandweightvalue, null);
		t = Theme.getCurrentTheme(Welcome_Setting.this, view);
		if (t != null)
		{
			t.applyBackgroundColor(R.id.heightandwightvalue, "view_bg");
			t.applyBackgroundColor(R.id.divider, "text_color");
			t.applyBackgroundColor(R.id.valuecancel, "text_color");
			t.applyBackgroundColor(R.id.setvalue, "text_color");
			t.applyBackgroundDrawable(R.id.heightweightvalue, "mpt_edit_text_sltr");
			
			t.applyTextColor(R.id.tileforhieghtandweight, "text_color");
			t.applyTextColor(R.id.heightweightvalue, "text_color");
			t.applyTextColor(R.id.text, "text_color");
			
		}
		builder.setView(view);
		
		final AlertDialog alertDialog = builder.create();
		
		TextView title = (TextView) view.findViewById(R.id.tileforhieghtandweight);
		
		title.setText(getResources().getString(R.string.weight) + " in " + PeriodTrackerObjectLocator.getInstance().getWeighUnit());
		
		final EditText editText = (EditText) view.findViewById(R.id.heightweightvalue);
		
		if (PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(getResources().getString(R.string.lb)))
		{
			
			if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
			{
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) != 0)
				{
					editText.setText(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()));
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_WEIGHT_VAULE_IN_LB)));
				}
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_WEIGHT_VAULE_IN_LB)));
				
			}
		}
		else
		{
			
			if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
			{
				
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) != 0)
				{
					
					editText.setText(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()));
					
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram((PeriodTrackerConstants.DEFAULT_WEIGHT_VAULE_IN_LB)))));
					
				}
				
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram((PeriodTrackerConstants.DEFAULT_WEIGHT_VAULE_IN_LB)))));
				
			}
		}
		Button set = (Button) view.findViewById(R.id.setvalue);
		Button cancel = (Button) view.findViewById(R.id.valuecancel);
		
		cancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		
		set.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (checkValidityOfWeight(editText.getText().toString()))
				{
					setWeightValue(editText.getText().toString());
					alertDialog.dismiss();
					((BaseExpandableListAdapter) listAdapter).notifyDataSetChanged();
				}
			}
		});
		
		alertDialog.show();
		
	}
	
	public boolean checkValidityOfWeight(String weightValue)
	{
		boolean validateWeigh = false;
		AlertDialog.Builder builder = new AlertDialog.Builder(Welcome_Setting.this);
		if (PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(getResources().getString(R.string.lb)))
		{
			if (null != weightValue && !weightValue.equals(""))
			{
				
				if (Float.parseFloat(weightValue) > PeriodTrackerConstants.MAX_WEIGHT_IN_LB || Float.parseFloat(weightValue) < PeriodTrackerConstants.MIN_WEIGHT_IN_LB)
				{
					validateWeigh = false;
					// Toast.makeText(getActivity(), "invalid Weight in lb",
					// Toast.LENGTH_SHORT).show();
					builder.setTitle(getResources().getString(R.string.invalidwieght));
					builder.setMessage(getResources().getString(R.string.invalidwieghtmessageinlb));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				}
				else
				{
					validateWeigh = true;
				}
				
			}
			
		}
		else
		{
			if (null != weightValue && !weightValue.equals(""))
			{
				if (Float.parseFloat(weightValue) > PeriodTrackerConstants.MAX_WEIGHT_IN_KG || Float.parseFloat(weightValue) < PeriodTrackerConstants.MIN_WEIGHT_IN_KG)
				{
					validateWeigh = false;
					// Toast.makeText(getActivity(), "invalid wieght in kg",
					// Toast.LENGTH_SHORT).show();
					builder.setTitle(getResources().getString(R.string.invalidwieght));
					builder.setMessage(getResources().getString(R.string.invalidwieghtmessageinkg));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				}
				else
				{
					validateWeigh = true;
					
				}
			}
		}
		return validateWeigh;
		
	}
	
	public void setWeightValue(String Value)
	{
		
		Value = Utility.getStringFormatedNumber(Value);
		if (Utility.isValidNumber(float.class, Value))
		{
			
			try
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getWeighUnit() && !PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(getResources().getString(R.string.lb)))
				{
					
					Value = String.valueOf(Utility.ConvertToPounds(Float.parseFloat(Value)));
				}
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_VALUE_KEY, Value, PeriodTrackerObjectLocator.getInstance().getProfileId());
				
				if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
				{
					PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
					//initLayout();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "wrong value", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void setHieghtValue(String Value)
	{
		Value = Utility.getStringFormatedNumber(Value);
		
		if (Utility.isValidNumber(float.class, Value))
		{
			
			try
			{
				
				if (null != PeriodTrackerObjectLocator.getInstance().getHeightUnit() && !PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(getResources().getString(R.string.CM)))
				{
					Value = Utility.getStringFormatedNumber(String.valueOf(Utility.convertInchesTOCentiMeter(Float.parseFloat(Value))));
				}
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_VALUE_KEY, Value, PeriodTrackerObjectLocator.getInstance().getProfileId());
				
				if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
				{
					PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
					
				}
				//initLayout();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "wrong value", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onClickTemperature()
	{
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper(Welcome_Setting.this, android.R.style.Theme_DeviceDefault_Light);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from(this).inflate(R.layout.forheigthandweightvalue, null);
		t = Theme.getCurrentTheme(Welcome_Setting.this, view);
		if (t != null)
		{
			t.applyBackgroundColor(R.id.heightandwightvalue, "view_bg");
			t.applyBackgroundColor(R.id.divider, "text_color");
			t.applyBackgroundColor(R.id.valuecancel, "text_color");
			t.applyBackgroundColor(R.id.setvalue, "text_color");
			t.applyBackgroundDrawable(R.id.heightweightvalue, "mpt_edit_text_sltr");
			
			t.applyTextColor(R.id.tileforhieghtandweight, "text_color");
			t.applyTextColor(R.id.heightweightvalue, "text_color");
			t.applyTextColor(R.id.text, "text_color");
			
		}
		builder.setView(view);
		final AlertDialog alertDialog = builder.create();
		TextView title = (TextView) view.findViewById(R.id.tileforhieghtandweight);
		if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit())
		{
			title.setText(getResources().getString(R.string.temprature) + " in " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
		}
		final EditText editText = (EditText) view.findViewById(R.id.heightweightvalue);
		if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit() && PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(getResources().getString(R.string.Fehr)))
		{
			
			if (Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
			{
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()) != Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))
				{
					editText.setText(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()));
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))));
					
				}
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber(String.valueOf(Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))));
				
			}
		}
		else
		{
			
			if (Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
			{
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()) != Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule())))));
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber((String.valueOf(Utility.ConvertToCelsius(Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))))));
					
				}
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber((String.valueOf(Utility.ConvertToCelsius(Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))))));
				
			}
		}
		Button set = (Button) view.findViewById(R.id.setvalue);
		Button cancel = (Button) view.findViewById(R.id.valuecancel);
		
		cancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		
		set.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (checkValidityOfTemp(editText.getText().toString()))
				{
					setTemptValue(editText.getText().toString());
					alertDialog.dismiss();
					((BaseExpandableListAdapter) listAdapter).notifyDataSetChanged();
				}
			}
		});
		
		alertDialog.show();
		
	}
	
	public boolean checkValidityOfTemp(String tempValue)
	{
		boolean validateTemp = false;
		AlertDialog.Builder builder = new AlertDialog.Builder(Welcome_Setting.this);
		if (PeriodTrackerObjectLocator.getInstance().getTempUnit().toString().equals(getResources().getString(R.string.celsius)))
		{
			
			if (null != tempValue && !tempValue.equals(""))
			{
				
				if (Float.parseFloat(tempValue) > PeriodTrackerConstants.MAX_TEMP_IN_CELSIUS || Float.parseFloat(tempValue) < PeriodTrackerConstants.MIN_TEMP_IN_CELSIUS)
				{
					validateTemp = false;
					builder.setTitle(getResources().getString(R.string.invalidtemp));
					builder.setMessage(getResources().getString(R.string.invalidtempmessageinC));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
					// Toast.makeText(PersonalSettingView.this,
					// "invalid temp in celsius",
					// Toast.LENGTH_SHORT).show();
				}
				else
				{
					validateTemp = true;
					
				}
				
			}
			
		}
		else
		{
			
			if (null != tempValue && !tempValue.equals(""))
			{
				if (Float.parseFloat(tempValue) > PeriodTrackerConstants.MAX_TEMP_IN_FEHRENHIET || Float.parseFloat((tempValue)) < PeriodTrackerConstants.MIN_TEMP_IN_FEHRENHIET)
				{
					validateTemp = false;
					builder.setTitle(getResources().getString(R.string.invalidtemp));
					builder.setMessage(getResources().getString(R.string.invalidtemptmessageinF));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
					
					// Toast.makeText(PersonalSettingView.this,
					// "invalid temp in Ferhinhiet ",
					// Toast.LENGTH_SHORT).show();
				}
				else
				{
					validateTemp = true;
					
				}
			}
		}
		
		return validateTemp;
		
	}
	
	public void setTemptValue(String Value)
	{
		Value = Utility.getStringFormatedNumber(Value);
		
		try
		{
			if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit() && !PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(getResources().getString(R.string.Fehr)))
			{
				Value = Utility.getStringFormatedNumber(String.valueOf((Utility.ConvertToFahrenheit(Float.parseFloat(Value)))));
			}
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMPERATURE_VALUE_KEY, Value, PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
			{
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				
			}
			//initLayout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
		//initLayout();
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
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, true).commit();
		
	}
	
	public void onClickChangeDayOfWeek(Context context)
	{
		
		int selectedPosition = -1;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items =
		{
				getResources().getString(R.string.sunday), getResources().getString(R.string.monday), getResources().getString(R.string.saturady)
		};
		
		if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("0"))
		{
			selectedPosition = 0;
		}
		else if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("1"))
		{
			selectedPosition = 1;
		}
		else if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("6"))
		{
			selectedPosition = 2;
		}
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				if (which == 0)
				{
					setDayOfWeek("0");
				}
				else if (which == 1)
				{
					setDayOfWeek("1");
				}
				else if (which == 2)
				{
					setDayOfWeek("6");
				}
				((BaseExpandableListAdapter) listAdapter).notifyDataSetChanged();
				dialog.dismiss();
				
			}
		});
		builder.show();
		
	}
	
	private void setupTheme()
	{
		HashMap<String, String> hm = Theme.getAvailableThemes(this);
		Set<String> key = hm.keySet();
		values = new String[hm.size()];
		entries = new String[hm.size()];
		Iterator iter = key.iterator();
		int i = 0;
		while (iter.hasNext())
		{
			String ss = (String) iter.next();
			values[i] = ss.equals("") ? getResources().getString(R.string.default_theme) : ss;
			entries[i] = hm.get(ss);
			i++;
		}
		
	}
	
	public void onClickTheme(Context context)
	{
		setupTheme();
		int selectedPosition = -1;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		selectedPosition = 0;
		for (int i = 0; i < entries.length; i++)
		{
			if (!APP.GLOBAL().getPreferences().getString(APP.PREF.THEME_NAME.key, "").equals("") && APP.GLOBAL().getPreferences().getString(APP.PREF.THEME_NAME.key, "").equals(entries[i])) selectedPosition = i;
		}
		builder.setSingleChoiceItems(entries, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				APP.GLOBAL().getEditor().putString(APP.PREF.THEME_NAME.key, entries[which]).commit();
				APP.GLOBAL().getEditor().putString(APP.PREF.THEME_COMPONENT.key, values[which]).commit();
				dialog.dismiss();
				Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				
			}
		});
		builder.show();
		
	}
	
	public void setDayOfWeek(String Vaule)
	{
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_DAY_OF_WEEK_KEY, String.valueOf(Vaule), PeriodTrackerObjectLocator.getInstance().getProfileId());
		if (!applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
		{
			applicationSettingDBHandler.inseretApplicationSetting(applicationSettingModel);
		}
		
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		//initLayout();
	}
	
}
