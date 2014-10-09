package com.linchpin.periodtracker.view;

import java.io.InputStream;
import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.APP.PREF;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;

public class MainInfo extends Activity implements OnClickListener
{
	
	LinearLayout	aboutApp, share, feedback, rateUs, /*moreApps,*/ blog, userGuide, faq;
	RelativeLayout helpUs;
	private void applyTheme()
	{
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			
			t.applyBackgroundDrawable(R.id.share, "mpt_setting_lst_sltr");
			t.applyBackgroundDrawable(R.id.rateus, "mpt_setting_lst_sltr");
			t.applyBackgroundDrawable(R.id.helpus, "mpt_setting_lst_sltr");
			t.applyBackgroundDrawable(R.id.aboutapp, "mpt_setting_lst_sltr");
			t.applyBackgroundDrawable(R.id.userguide, "mpt_setting_lst_sltr");
			t.applyBackgroundDrawable(R.id.faq, "mpt_setting_lst_sltr");
			t.applyBackgroundDrawable(R.id.blog, "mpt_setting_lst_sltr");
			t.applyBackgroundDrawable(R.id.feedback, "mpt_setting_lst_sltr");
			//t.applyBackgroundDrawable(R.id.moreapp, "mpt_setting_lst_sltr");
			t.applyBackgroundDrawable(R.id.version, "mpt_setting_lst_sltr");
			
			t.applyTextColor(R.id.t1, "text_color");
			t.applyTextColor(R.id.t11, "text_color");
			t.applyTextColor(R.id.t2, "text_color");
			t.applyTextColor(R.id.t3, "text_color");
			t.applyTextColor(R.id.t4, "text_color");
			t.applyTextColor(R.id.t5, "text_color");
			t.applyTextColor(R.id.t6, "text_color");
			t.applyTextColor(R.id.t7, "text_color");
		//	t.applyTextColor(R.id.t8, "text_color");
			t.applyTextColor(R.id.t9, "text_color");
			
		}
			
			
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maininfolayout);
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
		if (!APP.GLOBAL().getPreferences().getBoolean(PREF.PURCHASED.key, false))			
			PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(MainInfo.this);
		super.onResume();
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
		APP.GLOBAL().exicuteRIOAnim(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			finish();
			APP.GLOBAL().exicuteRIOAnim(MainInfo.this);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void initLayout()
	{
		
		blog = (LinearLayout) findViewById(R.id.blog);
		
		aboutApp = (LinearLayout) findViewById(R.id.aboutapp);
		share = (LinearLayout) findViewById(R.id.share);
		feedback = (LinearLayout) findViewById(R.id.feedback);
		rateUs = (LinearLayout) findViewById(R.id.rateus);
		((TextView) findViewById(R.id.versionnumberbelow)).setText(APP.GLOBAL().getAppVersion());
	//	moreApps = (LinearLayout) findViewById(R.id.moreapp);
		userGuide = (LinearLayout) findViewById(R.id.userguide);
		faq = (LinearLayout) findViewById(R.id.faq);
		helpUs = (RelativeLayout) findViewById(R.id.helpus);
		if(APP.GLOBAL().getPreferences().getBoolean(APP.PREF.NEW_FETURE.key+"Helpus", true));
		findViewById(R.id.includenewfesturer).setVisibility(View.VISIBLE);
		findViewById(R.id.pan_back).setOnClickListener(this);
		userGuide.setOnClickListener(this);
		faq.setOnClickListener(this);
		helpUs.setOnClickListener(this);
		//buyFullVersion.setOnClickListener(this);
		share.setOnClickListener(this);
		feedback.setOnClickListener(this);
		aboutApp.setOnClickListener(this);
		//moreApps.setOnClickListener(this);
		blog.setOnClickListener(this);
		rateUs.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v)
	{
		Intent intent = null;
		int vid=v.getId();
		if (vid == R.id.pan_back)
		{
			finish();
			APP.GLOBAL().exicuteRIOAnim(this);
		}
		else if (vid == R.id.userguide)
		{
			intent = new Intent(MainInfo.this, Blog.class);
			intent.putExtra("classname", "userGuide");
		}
		else if (vid == R.id.faq)
		{
			intent = new Intent(MainInfo.this, AboutApp.class);
			intent.putExtra("classname", "faq");
		}
		else if (vid == R.id.blog)
		{
			intent = new Intent(MainInfo.this, Blog.class);
			intent.putExtra("classname", "blog");
		}
		else if (vid == R.id.aboutapp)
		{
			intent = new Intent(MainInfo.this, AboutApp.class);
			intent.putExtra("classname", "aboutapp");
		}
		else if (vid == R.id.share)
		{
			try
			{
				InputStream is = getAssets().open("share.html");
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				String str = new String(buffer);
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
//					i.putExtra(Intent.EXTRA_EMAIL, new String[]
//					{
//						"myperiodtracker@lptpl.com"
//					});
				i.putExtra(Intent.EXTRA_SUBJECT, "Share MyPeriod Tracker with you");
				i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(str));
				startActivity(Intent.createChooser(i, "How do you want to share?"));
				APP.GLOBAL().exicuteLIOAnim(this);
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
		}
		else if (vid == R.id.feedback)
		{
			try
			{
				StringBuilder builder = new StringBuilder();
				builder.append(getResources().getString(R.string.osversion)).append(Build.VERSION.RELEASE);
				Field[] fields = Build.VERSION_CODES.class.getFields();
				for (Field field : fields)
				{
					String fieldName = field.getName();
					int fieldValue = -1;
					try
					{
						fieldValue = field.getInt(new Object());
					}
					catch (IllegalArgumentException e)
					{
						e.printStackTrace();
					}
					catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
					catch (NullPointerException e)
					{
						e.printStackTrace();
					}
					if (fieldValue == Build.VERSION.SDK_INT)
					{
						builder.append(" : ").append(fieldName).append(" : ");
						builder.append("sdk=").append(fieldValue);
					}
				}
				
				TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String id = (null == tm.getDeviceId() ? "not available" : tm.getDeviceId());
				String android_id = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
				
				InputStream is = getAssets().open("feedback.html");
				int size = is.available();
				
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				
				String str = new String(buffer);
				
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				str = str + "<li>App Version:" + APP.GLOBAL().getAppVersion() + "</li>" + "<br></br>" + "<li>Device id:" + id + "</li>" + "<br></br>" + "<li>Android OS:" + builder.toString() + "</li>)";
				
				i.putExtra(Intent.EXTRA_EMAIL, new String[]
				{
					"myperiodtracker@lptpl.com"
				});
				i.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
				i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(str));
				try
				{
					startActivity(Intent.createChooser(i, "Send mail..."));
					APP.GLOBAL().exicuteLIOAnim(this);
					
				}
				catch (android.content.ActivityNotFoundException ex)
				{
					Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (vid == R.id.rateus)
		{
			if (Utility.isNetworkConnected(this))
			{
				intent = new Intent();
				intent.setData(Uri.parse("market://details?id=com.linchpin.myperiodtracker"));
			}
			else Toast.makeText(getBaseContext(), getResources().getString(R.string.nointernetconnected), Toast.LENGTH_SHORT).show();
		}
		else if (vid == R.id.helpus)
		{
			intent = new Intent(MainInfo.this, HelpUs.class);
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEW_FETURE.key+"Helpus", false);
		}
		if (intent != null)
		{
			startActivity(intent);
			APP.GLOBAL().exicuteLIOAnim(this);
		}
	}
}
