package com.linchpin.periodtracker.partnersharing;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.webkit.WebView;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;

public class Policies extends Activity
{
	@Override
	protected void onStart()
	{
		EasyTracker.getInstance(this).activityStart(this);
		super.onStart();
	}
	
	@Override
	protected void onStop()
	{
		EasyTracker.getInstance(this).activityStop(this);
		super.onStop();
	}
	
	WebView	view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
	/*	Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		WindowManager.LayoutParams params = getWindow().getAttributes();
		//params.x = -20;
		params.alpha = 0.85f;
		params.gravity = Gravity.CENTER;
		params.height = size.y - 50;
		params.width = size.x - 50;*/
		//	params.y = -10;
		
		//	this.getWindow().setAttributes(params);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.policies);
		view = (WebView) findViewById(R.id.webView);
		view.getSettings().setJavaScriptEnabled(true);
		if(android.os.Build.VERSION.SDK_INT>11)
		{
			if (getIntent().getExtras().getString("what", "").equals("2")) view.loadUrl("file:///android_asset/privacy.html");
			else if (getIntent().getExtras().getString("what", "").equals("1")) view.loadUrl("file:///android_asset/terms_ous.html");
		}
		else
		{
			if (getIntent().getExtras().getString("what").equals("2")) view.loadUrl("file:///android_asset/privacy.html");
			else if (getIntent().getExtras().getString("what").equals("1")) view.loadUrl("file:///android_asset/terms_ous.html");
		}
	}
	
}
