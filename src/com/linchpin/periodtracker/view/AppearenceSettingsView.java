package com.linchpin.periodtracker.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.SettingsAdaptor;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;

public class AppearenceSettingsView extends Activity
{
	ListView			listView;
	SettingsAdaptor		adaptor;
	private String[]	values;
	private String[]	entries;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appearence_settings);
	
		findViewById(R.id.appearenceback).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
			}
		});
		
		findViewById(R.id.appearenceinfo).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(AppearenceSettingsView.this, HomeScreenHelp.class);
				intent.putExtra("classname", "changeAppIconView");
				startActivity(intent);
				
			}
		});
		
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
		initLayout();
		
		EasyTracker.getInstance(this).activityStart(this);
		
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		
		if (APP.GLOBAL().getPreferences().getBoolean("purchased", false))
		{
		}
		else
		{
			PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		}
		
	}
	
	public void initLayout()
	{
		
		listView = (ListView) findViewById(R.id.appereancesettingslistview);
		
		//list = new ArrayList<String>();
		
		/*list.add(getResources().getString(R.string.app_iconandname));
		list.add(getResources().getString(R.string.setdaysofweek));
		list.add(getResources().getString(R.string.theme));*/
		adaptor = new SettingsAdaptor(getApplicationContext(), getResources().getStringArray(R.array.appearence_settings));
		listView.setAdapter(adaptor);
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
			{
				TextView textView = (TextView) v.findViewById(R.id.settinglistitem);
				if (textView.getText().toString().trim().equals(getResources().getString(R.string.app_iconandname)))
				{
					
					Intent intent = new Intent(AppearenceSettingsView.this, ChangeAppIconView.class);
					startActivity(intent);
					overridePendingTransition(R.anim.left_in, R.anim.left_out);
					adaptor.notifyDataSetChanged();
					//Tips.viewVisible(AppearenceSettingsView.this, APP.TipsPath.AppIcon.id);
					
				}
				else if (textView.getText().toString().trim().equals(getResources().getString(R.string.setdaysofweek)))
				{
					
					APP.GLOBAL().getEditor().putBoolean("NewFeature_Apper_ChangeIcon", false).commit();
					
					onClickChangeDayOfWeek(getApplicationContext());
				}
				else if (textView.getText().toString().trim().equals(getResources().getString(R.string.theme)))
				{
					onClickTheme(getApplicationContext());
					APP.GLOBAL().getEditor().putBoolean("NewFeature_Apper_ChangeIcon", false).commit();
					
				}
			}
		});
		
	}
	
	// Button
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
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
			if (!APP.GLOBAL().getPreferences().getString(APP.PREF.THEME_NAME.key, "").equals("") && APP.GLOBAL().getPreferences().getString(APP.PREF.THEME_NAME.key, "").equals(entries[i])) 
				selectedPosition = i;
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
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
		initLayout();
	}
	
}
