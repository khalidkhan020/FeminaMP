package com.linchpin.periodtracker.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.view.AddNoteView;
import com.linchpin.periodtracker.view.CaldroidActivity;
import com.linchpin.periodtracker.view.HomeScreenView;
import com.linchpin.periodtracker.view.PeriodLogPagerView;
import com.linchpin.periodtracker.view.SettingsView;
import com.linchpin.periodtracker.view.TimeLineFragment;

public class ClickListener implements OnClickListener
{
	Intent		intent;
	Activity	activity;
	
	//Activity	act;
	
	public ClickListener(Activity activity)
	{
		this.activity = activity;
	}
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		
		if (id == R.id.backbutton)
		{
			((Activity) activity).finish();
			APP.GLOBAL().exicuteRIOAnim(((Activity) activity));
		}
		
		else if (id == R.id.addbackbutton)
		{
			((Activity) activity).finish();
			APP.GLOBAL().exicuteRIOAnim(((Activity) activity));
		}
		else if (id == R.id.next)
		{
			Toast.makeText(activity, "ok", Toast.LENGTH_SHORT).show();
		}
		else if (id == R.id.pre)
		{
			Toast.makeText(activity, "ok", Toast.LENGTH_SHORT).show();
		}
		
	}
	
}
