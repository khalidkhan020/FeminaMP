package com.linchpin.periodtracker.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.GridAppIconAdaptor;
import com.linchpin.periodtracker.model.AppIconModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.widget.BackBar;

public class ChangeAppIconView extends Activity
{
	
	GridView			gridView;
	List<AppIconModel>	appIconModels	= new ArrayList<AppIconModel>();
	private String      prev;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.appicon_list);	
		Theme t=Theme.getCurrentTheme(this,findViewById(android.R.id.content));
		if(t!=null)
			t.applyBackgroundColor(R.id.appicongrid, "view_bg");
		((BackBar)findViewById(R.id.changeiconsettingsheaderlayout)).setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				onBackPressed();
				
			}
		});
		
		findViewById(R.id.changeiconsettingsinfobutton).setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(ChangeAppIconView.this, HomeScreenHelp.class);
				intent.putExtra("classname", "changeAppIconView");
				startActivity(intent);
				
			}
		});
		gridView = (GridView) findViewById(R.id.appicongrid);
		
		appIconModels.add(new AppIconModel("0", "app_name", "icon"));
		appIconModels.add(new AppIconModel("1", "app_newname", "icon"));
		appIconModels.add(new AppIconModel("2", "app_nameMPT", "icon"));
		appIconModels.add(new AppIconModel("3", "app_name", "icon_flower"));
		appIconModels.add(new AppIconModel("4", "app_newname", "icon_flower"));
		appIconModels.add(new AppIconModel("5", "app_nameMPT", "icon_flower"));
		appIconModels.add(new AppIconModel("6", "app_name", "icon_bee"));
		appIconModels.add(new AppIconModel("7", "app_newname", "icon_bee"));
		appIconModels.add(new AppIconModel("8", "app_nameMPT", "icon_bee"));
		appIconModels.add(new AppIconModel("9", "app_name", "icon_heart"));
		appIconModels.add(new AppIconModel("10", "app_newname", "icon_heart"));
		appIconModels.add(new AppIconModel("11", "app_nameMPT", "icon_heart"));
		appIconModels.add(new AppIconModel("12", "app_name", "icon_teddy"));
		appIconModels.add(new AppIconModel("13", "app_newname", "icon_teddy"));
		appIconModels.add(new AppIconModel("14", "app_nameMPT", "icon_teddy"));
		prev=APP.GLOBAL().getPreferences().getString("alias", "0");
		final GridAppIconAdaptor adaptor = new GridAppIconAdaptor(getApplicationContext(), appIconModels);
		gridView.setAdapter(adaptor);
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				// TODO Auto-generated method stub
				
				String previous = APP.GLOBAL().getPreferences().getString("alias", "0");
				
				APP.GLOBAL().getEditor().putString("alias", String.valueOf(arg2)).commit();
				
				if (arg2 != 0)
				{
					getPackageManager().setComponentEnabledSetting(new ComponentName("com.linchpin.myperiodtracker", "com.linchpin.periodtracker.view.SplashScreen" + arg2), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
				}
				else if (arg2 == 0)
				{
					getPackageManager().setComponentEnabledSetting(new ComponentName("com.linchpin.myperiodtracker", "com.linchpin.periodtracker.view.SplashScreen"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
					
				}
				
				if (!previous.equals("0"))
				{
					getPackageManager().setComponentEnabledSetting(new ComponentName("com.linchpin.myperiodtracker", "com.linchpin.periodtracker.view.SplashScreen" + previous), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
				}
				else if (previous.equals("0"))
				{
					getPackageManager().setComponentEnabledSetting(new ComponentName("com.linchpin.myperiodtracker", "com.linchpin.periodtracker.view.SplashScreen"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
					
				}
				adaptor.notifyDataSetChanged();
				
			}
		});
		
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();		
		if (!APP.GLOBAL().getPreferences().getBoolean("purchased", false)) PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
	}
	@Override
	protected void onStop()
	{
		super.onStop();		
		EasyTracker.getInstance(this).activityStop(this);
		
	}	
	@Override
	public void onBackPressed()
	{
		if(!(APP.GLOBAL().getPreferences().getString("alias", "0").equals(prev)))
		{
			Toast.makeText(getApplicationContext(), "Please wait Your apps is refreshing...", Toast.LENGTH_SHORT).show();
			Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}
		else
		{
			super.onBackPressed();
			APP.GLOBAL().exicuteLIOAnim(this);
		}
	}
	
}
