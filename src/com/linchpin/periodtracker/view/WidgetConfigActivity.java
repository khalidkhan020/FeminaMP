package com.linchpin.periodtracker.view;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.receiver.MPTAppWidgetProvider;
import com.linchpin.periodtracker.utlity.APP;

public class WidgetConfigActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
//		Tips.viewVisible(this, APP.TipsPath.Wiidget.id);
	
		if (!getIntent().getBooleanExtra("lock", true))
		{setContentView(R.layout.widget_passcode);
			findViewById(R.id.unlock).setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View arg0)
				{					
					update(getIntent().getBooleanExtra("lock", true));
										
				}
			});
		}
		else
			update(getIntent().getBooleanExtra("lock", true));		
		super.onCreate(savedInstanceState);
	}
	private void update(boolean t)
	{
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.WIDGET_LOCK.key, t).commit();
		Intent intent = new Intent(WidgetConfigActivity.this,MPTAppWidgetProvider.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		int[] ids = getIntent().getIntArrayExtra("ids");
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
		sendBroadcast(intent);
		finish();
	}
}
