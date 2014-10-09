package com.linchpin.periodtracker.view;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.partnersharing.LoginScreen;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SelectGoal extends Activity implements OnClickListener
{
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	
	private void applyTheme()
	{
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			t.applyTextColor(R.id.log_to_start, "text_color");
			t.applyTextColor(R.id.try_to_con, "text_color");
			t.applyTextColor(R.id.men_cal, "text_color");
			t.applyTextColor(R.id.h1, "heading_fg");
			t.applyBackgroundColor(R.id.h1, "heading_bg");
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundColor(R.id.log_to_start_li, "view_bg");
			t.applyBackgroundColor(R.id.try_to_con_li, "view_bg");
			t.applyBackgroundColor(R.id.men_cal_li, "view_bg");
			t.applyImageDrawable(R.id.arrow1, "next_info");
			t.applyImageDrawable(R.id.arrow2, "next_info");
			t.applyImageDrawable(R.id.arrow3, "next_info");
		}
		
	}
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
	
	FrameLayout ll_try_to_con,ll_log_to_start;
	FrameLayout llmenCal;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectgoal);
		ll_log_to_start=(FrameLayout)findViewById(R.id.log_to_start_li);
		ll_try_to_con=(FrameLayout)findViewById(R.id.try_to_con_li);
		llmenCal=(FrameLayout)findViewById(R.id.men_cal_li);
		APP.GLOBAL().getEditor().putBoolean(APP.GOAL.IS_HOME_SCREEN_VISITED.key, true).commit();
		//Toast.makeText(getApplicationContext(),getString(R.string.devicetype), Toast.LENGTH_LONG).show();
		llmenCal.setOnClickListener(this);
		ll_log_to_start.setOnClickListener(this);
		ll_try_to_con.setOnClickListener(this);
		findViewById(R.id.selectGoalbackbutton).setOnClickListener(this);
		applyTheme();
	}
	@Override
	public void onClick(View v)
	{
		Intent setDefalutValue = null;
		int id = v.getId();
		if (id == R.id.men_cal_li)
		{
			//APP.GLOBAL().getEditor().putBoolean(APP.GOAL.MEN_CAL.key, true).commit();
			setDefalutValue=new Intent(SelectGoal.this,PersonalSettingView.class);
			APP.GLOBAL().getEditor().putBoolean(APP.GOAL.LOG_TO_START.key, false).commit();
			startActivity(setDefalutValue);
			finish();
			APP.GLOBAL().exicuteRIOAnim(SelectGoal.this);
		}
		else if (id == R.id.try_to_con_li)
		{
			//APP.GLOBAL().getEditor().putBoolean(APP.GOAL.TRY_TO_CON.key, true).commit();
			setDefalutValue=new Intent(SelectGoal.this,PersonalSettingView.class);
			APP.GLOBAL().getEditor().putBoolean(APP.GOAL.LOG_TO_START.key, false).commit();
			startActivity(setDefalutValue);
			finish();
			APP.GLOBAL().exicuteRIOAnim(SelectGoal.this);
		}
		else if (id == R.id.log_to_start_li)
		{
			APP.GLOBAL().getEditor().putBoolean(APP.GOAL.LOG_TO_START.key, true).commit();
			/*	APP.GLOBAL().getEditor().putBoolean(APP.GOAL.MEN_CAL.key, false).commit();
			APP.GLOBAL().getEditor().putBoolean(APP.GOAL.TRY_TO_CON.key, false).commit();*/
			setDefalutValue=new Intent(SelectGoal.this,LoginScreen.class);
			startActivity(setDefalutValue);
			finish();
			APP.GLOBAL().exicuteRIOAnim(SelectGoal.this);
		}
		
		
	}
	private void askForExit()
	{
		AlertDialog.Builder alert=new AlertDialog.Builder(this);
		alert.setTitle(getResources().getString(R.string.notifiction));
		alert.setMessage(getResources().getString(R.string.exitmessage));
		alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
				
			}
		});
		alert.setNegativeButton(getResources().getString(R.string.no),null);
		alert.show();
		// TODO Auto-generated method stub
		
	}
	// TODO Auto-generated method stub
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		//	if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PURCHASED.key, false)) PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		
	}
	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		
		askForExit();
		
		
	}
}
