package com.linchpin.periodtracker.view;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.SettingsAdaptor;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;

public class Welcome_Default_Value extends Activity implements OnClickListener, OnItemClickListener
{
	Theme t;
	Button btncontinue;
	private void applyTheme()
	{
		
		 t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{			
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundColor(R.id.h1, "heading_bg");
			t.applyBackgroundDrawable(R.id.personalsettingsback, "backbuttonselctor");
			
			t.applyTextColor(R.id.personalsettings, "text_color");
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
		setContentView(R.layout.welcome_default_value);
		btncontinue=(Button)findViewById(R.id.continue_welcome);
		btncontinue.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
			Intent homeScreen=new Intent(Welcome_Default_Value.this,HomeScreenView.class);
			startActivity(homeScreen);
			APP.GLOBAL().exicuteRIOAnim(Welcome_Default_Value.this);
			
				// TODO Auto-generated method stub
				
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
		APP.GLOBAL().exicuteLIOAnim(Welcome_Default_Value.this);
		
	}
	
	public void initLayout()
	{
		ListView listView;
		listView = (ListView) findViewById(R.id.personalsettingslistview);
		findViewById(R.id.personalsettingsback).setOnClickListener(this);
		listView.setAdapter(new SettingsAdaptor(getApplicationContext(), getResources().getStringArray(R.array.default_settings_wel)));
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
		if (PeriodTrackerObjectLocator.getInstance().isAveraged())			
			selectedPosition = 1;
		else
			selectedPosition = 0;		
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
	
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.personalsettingsback:
				finish();
				APP.GLOBAL().exicuteRIOAnim(this);
				break;
			
			default:
				break;
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
	{
		switch (arg2)
		{
			case 0:
				onClickSelectPeriodLength();
				break;
			case 1:
				onClickManualSettings();
				break;
			case 2:
				onClickSelectOvulationLength();
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
}