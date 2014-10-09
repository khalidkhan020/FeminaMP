package com.linchpin.periodtracker.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.SettingsAdaptor;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;

public class PasswordView extends Activity
{
	
	RelativeLayout	setPasscode, changePasscode, clearPasscode;
	ListView		passCodeOptionList;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passcode_view);
		
		findViewById(R.id.passcodesettinginfobutton).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(PasswordView.this, HomeScreenHelp.class);
				if (!PeriodTrackerObjectLocator.getInstance().getPasswordProtection().equals("")) intent.putExtra("classname", "passwordset");
				else intent.putExtra("classname", "nopassword");
				startActivity(intent);
			}
		});
		findViewById(R.id.passcodesheaderlayout).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
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
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		initLayout();
		if (this.getSharedPreferences("com.linchpin.periodtracker", 0).getBoolean("purchased", false))
		{
			
		}
		else
		{
			PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		}
		
	}
	
	public void initLayout()
	{
		
		passCodeOptionList = (ListView) findViewById(R.id.passcodelist);
		
		String[] arrayList;
		if (PeriodTrackerObjectLocator.getInstance().getPasswordProtection() != null && !PeriodTrackerObjectLocator.getInstance().getPasswordProtection().equals(""))
		{
			arrayList = getResources().getStringArray(R.array.passcode_settings);
			
		}
		else
		{
			arrayList = getResources().getStringArray(R.array.setpasscode_settings);
		}
		
		passCodeOptionList.setAdapter(new SettingsAdaptor(this, arrayList));
		
		/*
		 * ViewTreeObserver vto = passCodeOptionList.getViewTreeObserver();
		 * vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		 * 
		 * @Override public void onGlobalLayout() {
		 * 
		 * if (PeriodTrackerObjectLocator.getInstance() .getPasswordProtection()
		 * != null && !PeriodTrackerObjectLocator.getInstance()
		 * .getPasswordProtection().equals("")) {
		 * 
		 * passCodeOptionList.getChildAt(0).setEnabled(false);
		 * passCodeOptionList.getChildAt(2).setClickable(true);
		 * passCodeOptionList.getChildAt(1).setClickable(true);
		 * passCodeOptionList.getChildAt(0).setClickable(false);
		 * 
		 * } else { passCodeOptionList.getChildAt(0).setEnabled(true);
		 * passCodeOptionList.getChildAt(0).setClickable(true);
		 * passCodeOptionList.getChildAt(2).setClickable(false);
		 * passCodeOptionList.getChildAt(1).setClickable(false);
		 * 
		 * }
		 */passCodeOptionList.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				// TODO Auto-generated method stub
				Intent intent = null;
				
				if (PeriodTrackerObjectLocator.getInstance().getPasswordProtection() != null && !PeriodTrackerObjectLocator.getInstance().getPasswordProtection().equals(""))
				{
					
					if (position == 0)
					{
						intent = new Intent(PasswordView.this, ChangePasswordView.class);
						startActivity(intent);
						overridePendingTransition(R.anim.left_in, R.anim.left_out);
					}
					else if (position == 1)
					{
						
						intent = new Intent(PasswordView.this, ClearPassword.class);
						startActivity(intent);
						overridePendingTransition(R.anim.left_in, R.anim.left_out);
					}
					
				}
				else
				{
					
					if (position == 0)
					{
						
						intent = new Intent(PasswordView.this, SetPassword.class);
						startActivity(intent);
						overridePendingTransition(R.anim.left_in, R.anim.left_out);
						
					}
				}
				
			}
		});
		
	}
}