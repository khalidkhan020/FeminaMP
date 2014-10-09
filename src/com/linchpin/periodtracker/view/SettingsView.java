package com.linchpin.periodtracker.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.SettingsAdaptor;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.GetPurchaseItems;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;

public class SettingsView extends Activity implements OnClickListener, OnItemClickListener
{
	
	ListView				applicationSettingListView, personalSettingListView;
	IInAppBillingService	mService;
	ServiceConnection		mServiceConn	= new ServiceConnection()
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
	public void onBackPressed()
	{
		finish();
		super.onBackPressed();
		APP.GLOBAL().exicuteLIOAnim(SettingsView.this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (data != null && requestCode == 1001 && resultCode == RESULT_OK)
		{
			try
			{
				String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
				JSONObject jo = new JSONObject(purchaseData);
				String sku = jo.getString("productId");
				Toast.makeText(SettingsView.this, getResources().getString(R.string.youhavebought) + " ", Toast.LENGTH_LONG).show();
				APP.GLOBAL().getEditor().putBoolean("purchased", true).commit();
				APP.GLOBAL().getEditor().putString("purchased_item_json", purchaseData).commit();
				findViewById(R.id.rlbottom).setVisibility(View.GONE);
				APP.GLOBAL().getEditor().putBoolean(APP.PREF.PURCHASED.key, true).commit();
				Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
				SettingsView.this.finish();
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
			}
			catch (JSONException e)
			{
				Toast.makeText(SettingsView.this, getResources().getString(R.string.failedinpurchase), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		applyTheme();
		findViewById(R.id.settingsheaderlayout).setOnClickListener(this);
		initLayout();
		try
		{
			bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"), mServiceConn, Context.BIND_AUTO_CREATE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
	
	public void initLayout()
	{
		findViewById(R.id.settingsheaderlayout).setOnClickListener(this);
		personalSettingListView = (ListView) findViewById(R.id.defaultsettinglist);
		applicationSettingListView = (ListView) findViewById(R.id.notificationsettinglist);
		personalSettingListView.setAdapter(new SettingsAdaptor(getBaseContext(), getResources().getStringArray(R.array.personal_settings)));
		ViewTreeObserver vto = personalSettingListView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener()
		{
			@Override
			public void onGlobalLayout()
			{
				Utility.setListViewHeightBasedOnChildren(personalSettingListView, SettingsView.this, "personal");
			}
		});
		
		applicationSettingListView.setAdapter(new SettingsAdaptor(getBaseContext(), getResources().getStringArray(R.array.application_settings)));
		Utility.setListViewHeightBasedOnChildren(applicationSettingListView, SettingsView.this, "");
		personalSettingListView.setOnItemClickListener(this);
		applicationSettingListView.setOnItemClickListener(this);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PURCHASED.key, false)) PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
	}
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.settingsheaderlayout)
		{
			finish();
			APP.GLOBAL().exicuteLIOAnim(this);
		}
		else
		{
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
	{
		Intent intent = null;
		TextView textView = (TextView) v.findViewById(R.id.settinglistitem);
		if (textView.getText().toString().trim().equals(getResources().getString(R.string.defaultVaules))) intent = new Intent(SettingsView.this, PersonalSettingView.class);
		else if (textView.getText().toString().trim().equals(getResources().getString(R.string.pregnancymode))) intent = new Intent(SettingsView.this, PregnancyOptionView.class);
		else if (textView.getText().toString().trim().equals(getResources().getString(R.string.notifications)))
		{
			intent = new Intent(SettingsView.this, NotificationSettingsView.class);
//			Tips.viewVisible(this, APP.TipsPath.Notification.id);
		}
		else if (textView.getText().toString().trim().equals(getResources().getString(R.string.apperence)))
		{
			APP.GLOBAL().getEditor().putBoolean("NewFeature_Apper", false).commit();
			intent = new Intent(SettingsView.this, AppearenceSettingsView.class);
		}
		/*else if (textView.getText().toString().trim().equals(getResources().getString(R.string.show_tip)))
		{
			APP.GLOBAL().getEditor().putBoolean("NewFeature_Apper", false).commit();
			((BaseAdapter) applicationSettingListView.getAdapter()).notifyDataSetChanged(); 
		}
		*/
		else if (textView.getText().toString().trim().equals(getResources().getString(R.string.restoreandbackup))) intent = new Intent(SettingsView.this, RestoreAndBackupActivity.class);
		else if (textView.getText().toString().trim().equals(getResources().getString(R.string.userguide)))
		{
			intent = new Intent(SettingsView.this, AboutApp.class);
			intent.putExtra("classname", "userguide");
		}
		else if (textView.getText().toString().trim().equals(getResources().getString(R.string.regionalsettings)))
		{
			intent = new Intent(SettingsView.this, RegionalSettingsView.class);
//			Tips.viewVisible(this, APP.TipsPath.RegionlSetting.id);
		}
		else if (textView.getText().toString().trim().equals(getResources().getString(R.string.passwordprotection)))
		{
			intent = new Intent(SettingsView.this, PasswordView.class);
//			Tips.viewVisible(this, APP.TipsPath.Passcode.id);
		}
		else if (textView.getText().toString().trim().equals(getResources().getString(R.string.info))) intent = new Intent(SettingsView.this, MainInfo.class);
		else if (textView.getText().toString().trim().equals(getResources().getString(R.string.butfullversion)))
		{
			if (APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PURCHASED.key, false))
		        Toast.makeText(getApplicationContext(),getResources().getString(R.string.alreadypurchased), Toast.LENGTH_SHORT).show();
				else
				{
			new GetPurchaseItems(SettingsView.this, mService).execute();
				}
//			Tips.viewVisible(this, APP.TipsPath.AdsFree.id);
		}
		
		if (intent != null) startActivity(intent);
		APP.GLOBAL().exicuteRIOAnim(SettingsView.this);
		
	}
	
	private void applyTheme()
	{
		
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{			
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundColor(R.id.h1, "heading_bg");
			t.applyBackgroundColor(R.id.h2, "heading_bg");
			//t.applyBackgroundDrawable(R.id.settingsback, "backbuttonselctor");
			
			t.applyTextColor(R.id.settings, "text_color");
			t.applyTextColor(R.id.h1, "heading_fg");
			t.applyTextColor(R.id.h2, "heading_fg");
			
		}
		/*else
		{
			setContentView(R.layout.settings);
			initLayout();
		}*/
		
	}
}
