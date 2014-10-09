package com.linchpin.periodtracker.view;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.SettingsAdaptor;
import com.linchpin.periodtracker.fragments.LogFragment;
import com.linchpin.periodtracker.interfaces.CalenderDialog;
import com.linchpin.periodtracker.interfaces.PeriodMessanger;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.partnersharing.LoginScreen;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.CustomAlertDialog;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.utlity.ValidationFunction;
import com.linchpin.periodtracker.utlity.CustomAlertDialog.onButoinClick;
import com.linchpin.periodtracker.view.DatePickerDialog.OnDateSetListener;
import com.linchpin.periodtracker.widget.BackBar;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;
import com.linchpin.periodttracker.database.DBManager;
import com.linchpin.periodttracker.database.DBProjections;
import com.linchpin.periodttracker.database.DashBoardDBHandler;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;

public class PersonalSettingView extends Activity implements OnClickListener, OnItemClickListener, CalenderDialog, OnDateSetListener
{
	Theme								t;
	Button								btncontinue;
	//TextView							statustext;
	TextView							header;
	LinearLayout						rlbottom1;
	List<PeriodTrackerModelInterface>	periodTrackerModelInterfaces;
	DashBoardDBHandler					dashBoardDBHandler;
	Date								startDate, endDate;
	PeriodLogModel						periodLogModel;
	SimpleDateFormat					dateFormat;
	Calendar							oldcalCalendar;
	DatePickerDialog					startDatePickerDialog, endDatePickerDailog;
	PeriodLogModel						logModel;
	private PeriodLogDBHandler			periodLogDBHandler;
	private CalenderDialog				calenderDialog;
	public static final int				GET_END_DATE	= 1;
	public static final int				EDIT_START_DATE	= 2;
	public static final int				EDIT_END_DATE	= 3;
	public static final int				NEW_START_DATE	= 4;
	public static final int				NEW_END_DATE	= 5;
	public static final int				UPDATE_END_DATE	= 6;
	
	private void applyTheme()
	{
		
		t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundColor(R.id.h1, "heading_bg");
			//	t.applyBackgroundDrawable(R.id.personalsettingsback, "backbuttonselctor");
			t.applyTextColor(R.id.continue_welcome, "co_btn_fg");
			t.applyBackgroundDrawable(R.id.continue_welcome, "mpt_button_sltr");
			//	t.applyTextColor(R.id.personalsettings, "text_color");
			t.applyTextColor(R.id.h1, "heading_fg");
			
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.persnol_setting_view);
		btncontinue = (Button) findViewById(R.id.continue_welcome);
		//	statustext = (TextView) findViewById(R.id.personalsettings);
		((BackBar) findViewById(R.id.personalsettingsheaderlayout)).setButtonClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});
		header = (TextView) findViewById(R.id.h1);
		rlbottom1 = (LinearLayout) findViewById(R.id.rlbottom);
		calenderDialog = (CalenderDialog) PersonalSettingView.this;
		periodLogDBHandler = new PeriodLogDBHandler(getApplication());
		dateFormat = new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		final PeriodLogModel periodLogModel = (PeriodLogModel) periodLogDBHandler.getTopLogs(PeriodTrackerObjectLocator.getInstance().getProfileId());
		
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.IS_FIRST_TIME.key, false))
		{
			rlbottom1.setVisibility(View.GONE);
			btncontinue.setVisibility(View.VISIBLE);
			//	statustext.setText(getResources().getString(R.string.my_home));
			header.setText(getResources().getString(R.string.welcomePersonalSettingHeading));
		}
		dashBoardDBHandler = new DashBoardDBHandler(getApplicationContext());
		periodTrackerModelInterfaces = dashBoardDBHandler.getAllLogs();
		btncontinue.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (!(APP.GLOBAL().getPreferences().getString(APP.PREF.END_DATE.key, "").equals("")))
				{
					Intent homeScreen = null;
					homeScreen = new Intent(PersonalSettingView.this, WelcomeSetting2.class);
					finish();
					startActivity(homeScreen);
					APP.GLOBAL().exicuteRIOAnim(PersonalSettingView.this);
				}
				else Toast.makeText(getApplicationContext(), "Please set Period Start Date..", Toast.LENGTH_LONG).show();
				
			}
		});
		initLayout();
		applyTheme();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PURCHASED.key, false)) PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.IS_FIRST_TIME.key, false))
		{
			Intent setbackActivity = new Intent(PersonalSettingView.this, SelectGoal.class);
			startActivity(setbackActivity);
			finish();
			APP.GLOBAL().exicuteLIOAnim(this);
		}
		else APP.GLOBAL().exicuteLIOAnim(this);
	}
	
	ListView	listView;
	
	public void initLayout()
	{
		
		listView = (ListView) findViewById(R.id.personalsettingslistview);
		//findViewById(R.id.personalsettingsback).setOnClickListener(this);
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.IS_FIRST_TIME.key, false))
		{
			listView.setAdapter(new SettingsAdaptor(getApplicationContext(), getResources().getStringArray(R.array.default_settings_wel)));
		}
		else listView.setAdapter(new SettingsAdaptor(getApplicationContext(), getResources().getStringArray(R.array.default_settings)));
		listView.setOnItemClickListener(this);
	}
	
	private void onClickSelectPeriodLength()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.setperiodperiodlength));
		final String[] items =
		{
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
		};
		int selectedPosition = Arrays.asList(items).indexOf(String.valueOf(PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength()));
		
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				setDefaultPeriodLength(which + 1);
				
				dialog.dismiss();
				
			}
		});
		builder.show();
		
	}
	
	private void onClickSelectOvulationLength()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle(getResources().getString(R.string.setperiodovulationlength));
		final String[] items =
		{
				"10", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"
		};
		int selectedPosition = Arrays.asList(items).indexOf(String.valueOf(PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength()));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				setDefaultOvulationLength(Integer.valueOf(items[which]));
				
				dialog.dismiss();
				
			}
		});
		builder.show();
		
	}
	
	private void onClickAverageLength()
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		int selectedPosition = 0;
		if (PeriodTrackerObjectLocator.getInstance().isAveraged()) selectedPosition = 1;
		else selectedPosition = 0;
		builder.setTitle(getResources().getString(R.string.setperiodcyclelength));
		builder.setSingleChoiceItems(getResources().getStringArray(R.array.length_settings), selectedPosition, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
				if (which == 1)
				{
					applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_AVERAGE_LENGTHS, "true", PeriodTrackerObjectLocator.getInstance().getProfileId()));
					setDefaultCycleLengthOnAverage(applicationSettingDBHandler.getAverageCycleLength());
					setDefaultPeriodLengthOnAverage(applicationSettingDBHandler.getAveragePeriodLength());
					setDefaultOvulationLengthOnAverage(applicationSettingDBHandler.getAverageCycleLength() / 2);
					
				}
				else
				{
					applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_AVERAGE_LENGTHS, "false", PeriodTrackerObjectLocator.getInstance().getProfileId()));
					setDefaultCycleLengthOnAverage(PeriodTrackerConstants.CYCLE_LENGTH);
					setDefaultPeriodLengthOnAverage(PeriodTrackerConstants.PERIOD_LENGTH);
					setDefaultOvulationLengthOnAverage(PeriodTrackerConstants.OVULATION_DAY);
				}
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				initLayout();
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	public void onClickManualSettings()
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items =
		{
				"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", ""
		};
		int selectedPosition = Arrays.asList(items).indexOf(String.valueOf(PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength()));
		
		builder.setTitle(getResources().getString(R.string.setperiodcyclelength));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				setDefaultCycleLength(Integer.valueOf(items[which]));
				
				dialog.dismiss();
				
			}
		});
		builder.show();
		
	}
	
	public void setDefaultPeriodLength(int length)
	{
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_LENGTH_KEY, String.valueOf(length), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		initLayout();
	}
	
	public void setDefaultCycleLength(int length)
	{
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_CYCLE_LENGTH_KEY, String.valueOf(length), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		initLayout();
		showAlertForOvulationSuggestion(length);
		
	}
	
	public void setDefaultCycleLengthOnAverage(int length)
	{
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_CYCLE_LENGTH_KEY, String.valueOf(length), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
		
	}
	
	public void setDefaultPeriodLengthOnAverage(int length)
	{
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_LENGTH_KEY, String.valueOf(length), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
		
	}
	
	public void setDefaultOvulationLengthOnAverage(int length)
	{
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_LENGTH_KEY, String.valueOf(length), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
	}
	
	public void showAlertForOvulationSuggestion(int periodlength)
	{
		
		final int ovualtionLength;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		ovualtionLength = periodlength / 2;
		
		builder.setTitle(getResources().getString(R.string.setperiodovulationlength));
		
		if (ovualtionLength == 21)
		{
			builder.setMessage(getResources().getString(R.string.whencyclengthgetchangeovulationlength) + " " + ovualtionLength + getResources().getString(R.string.stdayofperiod) + getResources().getString(R.string.notificationday));
		}
		else if (ovualtionLength == 22)
		{
			builder.setMessage(getResources().getString(R.string.whencyclengthgetchangeovulationlength) + " " + ovualtionLength + getResources().getString(R.string.nddayofperiod) + getResources().getString(R.string.notificationday));
			
		}
		else if (ovualtionLength == 23)
		{
			builder.setMessage(getResources().getString(R.string.whencyclengthgetchangeovulationlength) + " " + ovualtionLength + getResources().getString(R.string.rddayofperiod) + getResources().getString(R.string.notificationday));
			
		}
		else
		{
			builder.setMessage(getResources().getString(R.string.whencyclengthgetchangeovulationlength) + " " + ovualtionLength + getResources().getString(R.string.thdayofperiod) + getResources().getString(R.string.notificationday));
			
		}
		
		builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				setDefaultOvulationLength(ovualtionLength);
				
			}
		});
		
		builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		builder.show();
		
	}
	
	public void setDefaultOvulationLength(int length)
	{
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_LENGTH_KEY, String.valueOf(length), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		initLayout();
	}
	
	public void onClickWeight()
	{
		
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper(PersonalSettingView.this, android.R.style.Theme_DeviceDefault_Light);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from(this).inflate(R.layout.forheigthandweightvalue, null);
		t = Theme.getCurrentTheme(PersonalSettingView.this, view);
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
				}
			}
		});
		
		alertDialog.show();
		
	}
	
	public boolean checkValidityOfWeight(String weightValue)
	{
		boolean validateWeigh = false;
		AlertDialog.Builder builder = new AlertDialog.Builder(PersonalSettingView.this);
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
	
	public void onClickHeight()
	{
		
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper(PersonalSettingView.this, android.R.style.Theme_DeviceDefault_Light);
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from(this).inflate(R.layout.forheigthandweightvalue, null);
		t = Theme.getCurrentTheme(PersonalSettingView.this, view);
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
				}
			}
		});
		
		alertDialog.show();
		
	}
	
	public boolean validateHieght(String hieghtVaule)
	{
		boolean validateHiegh = false;
		AlertDialog.Builder builder = new AlertDialog.Builder(PersonalSettingView.this);
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
					initLayout();
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
				initLayout();
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
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper(PersonalSettingView.this, android.R.style.Theme_DeviceDefault_Light);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from(this).inflate(R.layout.forheigthandweightvalue, null);
		t = Theme.getCurrentTheme(PersonalSettingView.this, view);
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
				}
			}
		});
		
		alertDialog.show();
		
	}
	
	public boolean checkValidityOfTemp(String tempValue)
	{
		boolean validateTemp = false;
		AlertDialog.Builder builder = new AlertDialog.Builder(PersonalSettingView.this);
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
			initLayout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v)
	{/*
		switch (v.getId())
		{
			case R.id.personalsettingsback:
				if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.IS_FIRST_TIME.key, false))
				{
					Intent setGoBack = new Intent(PersonalSettingView.this, SelectGoal.class);
					startActivity(setGoBack);
					finish();
					APP.GLOBAL().exicuteLIOAnim(this);
				}
				else
				{
					finish();
					APP.GLOBAL().exicuteRIOAnim(this);
				}
				break;
			
			default:
				break;
		}
		
		*/
	}
	
	/*void setDate(int position)
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
				
				endDatePickerDailog = new DatePickerDialog(PersonalSettingView.this, endDatePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
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
				
				startDatePickerDialog = new DatePickerDialog(PersonalSettingView.this, startdatePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
				
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
			startDatePickerDialog = new DatePickerDialog(PersonalSettingView.this, startdatePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
			if (Utility.checkAndroidApiVersion())
			{
				startDatePickerDialog.getDatePicker().setMinDate(Utility.setHourMinuteSecondZero(new Date()).getTime() - 3640 * PeriodTrackerConstants.MILLI_SECONDS);
				startDatePickerDialog.getDatePicker().setMaxDate(Utility.setHourMinuteSecondZero(new Date()).getTime());
			}
			startDatePickerDialog.setTitle(getResources().getString(R.string.setstartdate));
			startDatePickerDialog.show();
		}
	//}
	else
	{
		
		Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.pregnancy), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
	}
	 */public DatePickerDialog.OnDateSetListener	startdatePickerListener	= new DatePickerDialog.OnDateSetListener()
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
																								int value = ValidationFunction.checkAllValidationForStartDate(PersonalSettingView.this, startDate, startDatePickerDialog, periodTrackerModelInterfaces, false);
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
	DatePickerDialog.OnDateSetListener				endDatePickerListener	= new DatePickerDialog.OnDateSetListener()
																			{
																				
																				@Override
																				public void onDateSet(DatePicker view, int id, final int year, int monthOfYear, int dayOfMonth)
																				{
																					if (year != 12345 && monthOfYear != 12345)
																					{
																						endDate = Utility.createDate(year, monthOfYear, dayOfMonth);
																						periodLogModel = (PeriodLogModel) periodTrackerModelInterfaces.get(0);
																						int value = ValidationFunction.checkValidityForEndDate(PersonalSettingView.this, periodLogModel.getStartDate(), endDate, endDatePickerDailog, periodTrackerModelInterfaces);
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
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
	{
		switch (arg2)
		{
			case 0:
				if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.IS_FIRST_TIME.key, false))
				{
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(new Date());
					if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
					{
						if (periodLogModel != null)
						{
							if (periodLogModel.getEndDate() != null && periodLogModel.getEndDate().getTime() == (PeriodTrackerConstants.NULL_DATE))
							{
								calendar.setTime(periodLogModel.getStartDate());
							}
						}
						calenderDialog.showDatePickerDialog(R.string.setstartdate, calendar, null, PeriodLogPagerView.NEW_START_DATE).show();
					}
				}
				else onClickSelectPeriodLength();
				break;
			case 1:
				if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.IS_FIRST_TIME.key, false))
				{
					periodTrackerModelInterfaces = dashBoardDBHandler.getAllLogs();
					if (periodTrackerModelInterfaces.size() > 0)
					{
						
						periodLogModel = (PeriodLogModel) periodTrackerModelInterfaces.get(0);
						if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
						{
							if (periodLogModel != null)
							{
								Date startdate = periodLogModel.getStartDate();
								Calendar calendar = new GregorianCalendar();
								calendar.setTime(Utility.addDays(startdate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1));
								calenderDialog.showDatePickerDialog(R.string.setenddate, calendar, periodLogModel, PeriodLogPagerView.UPDATE_END_DATE).show();
							}
							else
							{
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.start_datenotset), Toast.LENGTH_LONG).show();
							}
						}
						else
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.pregnancy), Toast.LENGTH_LONG).show();
						}
					}
					else
					{
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.start_datenotset), Toast.LENGTH_LONG).show();
					}
					
				}
				else onClickManualSettings();
				break;
			case 2:
				if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.IS_FIRST_TIME.key, false)) onClickSelectPeriodLength();
				else onClickSelectOvulationLength();
				break;
			case 3:
				if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.IS_FIRST_TIME.key, false))
				{
					onClickManualSettings();
				}
				else onClickAverageLength();
				break;
			case 4:
				if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.IS_FIRST_TIME.key, false))
				{
					onClickSelectOvulationLength();
					
				}
				else onClickHeight();
				break;
			case 5:
				
				onClickWeight();
				break;
			case 6:
				onClickTemperature();
				break;
			default:
				break;
		}
		//		if (textView.getText().toString().trim().equals(getResources().getString(R.string.cyclelength))) onClickManualSettings();
		//		if (textView.getText().toString().trim().equals(getResources().getString(R.string.Periodlength))) onClickSelectPeriodLength();
		//		if (textView.getText().toString().trim().equals(getResources().getString(R.string.averagelength))) onClickAverageLength();
		//		if (textView.getText().toString().trim().equals(getResources().getString(R.string.ovulationdaylength))) onClickSelectOvulationLength();
		//		if (textView.getText().toString().trim().equals(getResources().getString(R.string.hieght))) onClickHeight();
		//		if (textView.getText().toString().trim().equals(getResources().getString(R.string.weight))) onClickWeight();
		//		if (textView.getText().toString().trim().equals(getResources().getString(R.string.temprature))) onClickTemperature();
	}
	
	@Override
	public DatePickerDialog showDatePickerDialog(int titleid, Calendar calendar, Object object, int id)
	{
		this.oldcalCalendar = calendar;
		if (object != null) logModel = (PeriodLogModel) object;
		
		DatePickerDialog datePickerDailog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), id);
		
		datePickerDailog.setTitle(getResources().getString(titleid));
		
		if (Utility.checkAndroidApiVersion())
		{
			datePickerDailog.getDatePicker().setMinDate(new Date("1/1/2003").getTime());
		}
		return datePickerDailog;
	}
	
	@Override
	public void onDateSet(DatePicker view, int id, int year, int month, int day)
	{
		// TODO Auto-generated method stub
		if (year == month && month == day && day == 12345 && id == NEW_END_DATE)
		{
			insertPastRecordInDatabase(startDate, new Date("1/1/1900"));
		}
		else if (year != 12345 && month != 12345 && day != 12345)
		{
			if (id == R.id.addpastrecord)
			{
				startDate = Utility.createDate(year, month, day);
				addpastRecordStartDateValidation(startDate);
				
			}
			else switch (id)
			{
			
				case GET_END_DATE:
					endDate = Utility.createDate(year, month, day);
					addPastRecordEndDateValidation(endDate);
					
					break;
				case EDIT_START_DATE:
					startDate = Utility.createDate(year, month, day);
					updateStartDate();
					
					break;
				case EDIT_END_DATE:
					endDate = Utility.createDate(year, month, day);
					updateEndDate();
				case UPDATE_END_DATE:
					startDate = logModel.getStartDate();
					endDate = Utility.createDate(year, month, day);
					updateEndDate();
					break;
				case NEW_START_DATE:
					startDate = Utility.createDate(year, month, day);
					newStartDateValidation(startDate);
					
					break;
				case NEW_END_DATE:
					endDate = Utility.createDate(year, month, day);
					newEndDateValidation(endDate);
					break;
				default:
					break;
			}
		}
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.IS_FIRST_TIME.key, false))
		{
			listView.setAdapter(new SettingsAdaptor(getApplicationContext(), getResources().getStringArray(R.array.default_settings_wel)));
		}
		else listView.setAdapter(new SettingsAdaptor(getApplicationContext(), getResources().getStringArray(R.array.default_settings)));
		
	}
	
	public void addpastRecordStartDateValidation(Date startDate)
	{
		boolean valid = true;
		String invalidmsg = "";
		if (!Utility.isDateLessThanCurrent(startDate))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthancallmessage);
		}
		else if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()) || periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		}
		else if (!periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthanLastestRecord);
		}
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				//bAddPast.callOnClick();
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		else
		{
			Calendar calendar = new GregorianCalendar();
			Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
			if (date.getTime() >= startDate.getTime()) calendar.setTime(date);
			else calendar.setTime(new Date());
			showDatePickerDialog(R.string.setenddate, calendar, null, GET_END_DATE).show();
			
		}
		
	}
	
	public void newStartDateValidation(final Date startDate)
	{
		boolean valid = true;
		String invalidmsg = "";
		if (!Utility.isDateLessThanCurrent(startDate))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthancallmessage);
		}
		else if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()) || periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		}
		else if (periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.graterthanLastestRecord);
		}
		final boolean valid1 = valid;
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(startDate);
				showDatePickerDialog(R.string.setenddate, calendar, null, NEW_START_DATE).show();
				if (valid1)
				{
					APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
					//APP.PREF.SHARE_NOTIFY.=true;
				}
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		else
		{
			Calendar calendar = new GregorianCalendar();
			Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
			if (date.getTime() >= startDate.getTime()) calendar.setTime(date);
			else calendar.setTime(new Date());
			showDatePickerDialog(R.string.setenddate, calendar, null, NEW_END_DATE).show();
			if (valid1)
			{
				APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
				//APP.PREF.SHARE_NOTIFY.=true;
			}
		}
		
	}
	
	private void newEndDateValidation(Date endDate)
	{
		boolean valid = true;
		String invalidmsg = "";
		if (!Utility.isEndDateGreaterThanStart(startDate, endDate))
		{
			valid = false;
			invalidmsg = getString(R.string.enddategreaterthanstart);
		}
		/* else if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()) || periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE, startDate.getTime()))
		 {
			 valid = false;
			 invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		 }
		 else if (periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()))
		 {
			 valid = false;
			 invalidmsg = getString(R.string.lessthanLastestRecord);
		 }*/
		final boolean valid1 = valid;
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				Calendar calendar = new GregorianCalendar();
				Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
				if (date.getTime() >= startDate.getTime())
				{
					calendar.setTime(date);
				}
				else
				{
					calendar.setTime(new Date());
				}
				showDatePickerDialog(R.string.setenddate, calendar, null, GET_END_DATE).show();
				if (valid1)
				{
					APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
					//APP.PREF.SHARE_NOTIFY.=true;
				}
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				
				dialog.dismiss();
				
			}
		});
		
		else
		{
			insertPastRecordInDatabase(startDate, endDate);
			if (valid1)
			{
				APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
				//APP.PREF.SHARE_NOTIFY.=true;
			}
		}
	}
	
	private void addPastRecordEndDateValidation(Date endDate)
	{
		boolean valid = true;
		String invalidmsg = "";
		
		if (!Utility.isEndDateGreaterThanStart(startDate, endDate))
		{
			valid = false;
			invalidmsg = getString(R.string.enddategreaterthanstart);
		}
		else if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()) || periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		}
		else if (!periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthanLastestRecord);
		}
		
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				Calendar calendar = new GregorianCalendar();
				Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
				if (date.getTime() >= startDate.getTime())
				{
					calendar.setTime(date);
				}
				else
				{
					calendar.setTime(new Date());
				}
				showDatePickerDialog(R.string.setenddate, calendar, null, GET_END_DATE).show();
				
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				
			}
		});
		
		else
		{
			insertPastRecordInDatabase(startDate, endDate);
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
			
		}
	}
	
	public PeriodLogModel insertPastRecordInDatabase(Date startDate, Date endDate)
	{
		periodLogDBHandler = new PeriodLogDBHandler(this);
		startDate = Utility.setHourMinuteSecondZero(startDate);
		endDate = Utility.setHourMinuteSecondZero(endDate);
		PeriodLogModel model = new PeriodLogModel(0, PeriodTrackerObjectLocator.getInstance().getProfileId(), startDate, endDate, 0, 0, false, false);
		if (periodLogDBHandler.addPeriodLog(model))
		{
			//refresh();
			
			//	messagsToFrag.get(PERIOD_LOG_FRAGMENT_ID).sendMessage(LogFragment.REFRESH_PAST_LIST, null);
		}
		return model;
	}
	
	/*
	 * public void updateStartDate(Date startDate, int position) {
	 * 
	 * periodLogDBHandler = new PeriodLogDBHandler(getActivity());
	 * periodLogModel = (PeriodLogModel) pastRecordList.get(position);
	 * periodLogModel.setStartDate(startDate); Date date; Calendar calendar =
	 * new GregorianCalendar(); date = Utility.addDays(startDate,
	 * PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
	 * calendar.setTime(date); editEndDatePickerDialog = new
	 * DatePickerDialog(getActivity(), endDateSetListener,
	 * calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
	 * calendar.get(Calendar.DATE));
	 * editEndDatePickerDialog.setTitle(getResources
	 * ().getString(R.string.setenddate)); editEndDatePickerDialog.show();
	 * 
	 * }
	 */
	public void updateStartDate()
	{
		
		boolean valid = true;
		String invalidmsg = "";
		/*
		 * if (!Utility.isDateLessThanCurrent(startDate)) { valid = false;
		 * invalidmsg = getString(R.string.lessthancallmessage); } else if
		 * (Utility.isGreaterThan(new Date(oldcalCalendar.getTimeInMillis()),
		 * startDate)) { valid = false; invalidmsg =
		 * getString(R.string.lessthanLastestRecord);
		 * 
		 * }
		 */
		if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, oldcalCalendar.getTimeInMillis(), startDate.getTime())
				|| periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE, oldcalCalendar.getTimeInMillis(), startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		}
		/*
		 * else if
		 * (!periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER
		 * , DBProjections.PT_START_DATE, startDate.getTime())) { valid = false;
		 * invalidmsg = getString(R.string.lessthanLastestRecord); }
		 */
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				// showDatePickerDialog(R.string.setstartdate,
				// oldcalCalendar,null,
				// PeriodLogPagerView.EDIT_START_DATE).show();
				dialog.dismiss();
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				
			}
		});
		else
		{
			
			Calendar calendar = new GregorianCalendar();
			Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
			if (date.getTime() <= startDate.getTime())
			{
				calendar.setTime(new Date());
			}
			else
			{
				calendar.setTime(date);
			}
			
			showDatePickerDialog(R.string.setenddate, calendar, null, EDIT_END_DATE).show();
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
			
		}
		
	}
	
	public void updateEndDate()
	{
		boolean valid = true;
		String invalidmsg = "";
		if (!Utility.isDateLessThanCurrent(endDate))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthancallmessage);
		}
		else if (!Utility.isGreaterThan(startDate, endDate))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthanLastestRecord);
			
		}
		
		else if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, oldcalCalendar.getTimeInMillis(), endDate.getTime())
		/*|| periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE,
		oldcalCalendar.getTimeInMillis(), endDate.getTime()*/)
		{
			valid = false;
			invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		}
		/*else if (periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, endDate.getTime()))
		{
		valid = false;
		invalidmsg = getString(R.string.lessthanLastestRecord);
		}*/
		
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				showDatePickerDialog(R.string.setenddate, oldcalCalendar, null, EDIT_END_DATE).show();
				
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		else
		{
			if (logModel != null)
			{
				logModel.setStartDate(startDate);
				logModel.setEndDate(endDate);
				if (periodLogDBHandler.updatePeriodLogWhenEditRecord(logModel))
				{
					//refresh();
					APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();
					//sazid
					
					//messagsToFrag.get(PERIOD_LOG_FRAGMENT_ID).sendMessage(LogFragment.REFRESH_PAST_LIST, null);
				}
				else
				{
					Toast.makeText(this, "Not able to update", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				PeriodLogModel logModel = new PeriodLogModel();
				
			}
		}
		
	}
}
