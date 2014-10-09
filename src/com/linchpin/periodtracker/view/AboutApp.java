package com.linchpin.periodtracker.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;

public class AboutApp extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		WebView view = null;
		final Activity activity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_app);
		
		view = (WebView) findViewById(R.id.webview);
		
		if (getIntent().getExtras().get("classname").equals("faq"))
		{
			
			view.getSettings().setJavaScriptEnabled(true);
			view.loadUrl("file:///android_asset/userguide.html");
		}
		else
		{
			view.getSettings().setJavaScriptEnabled(true);
			view.loadUrl("file:///android_asset/aboutus.html");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
		
	}
	
	/* (non-Javadoc)
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
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
	
}
