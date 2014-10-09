package com.linchpin.periodtracker.receiver;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GlobalTouchService extends Service implements OnTouchListener
{
	
	private String			TAG	= this.getClass().getSimpleName();
	// window manager 
	private WindowManager	mWindowManager;
	// linear layout will use to detect touch event
	private LinearLayout	touchLayout;
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, 
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, 
				PixelFormat.TRANSLUCENT);
		View bb = new View(this);
		//bb.setText("Button");
		bb.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Toast.makeText(getBaseContext(), "hello Button clicked !!!!!!!!!", Toast.LENGTH_LONG).show();
				System.out.println("Clicked----><<<<<<<");
			}
		});
		
		params.gravity = Gravity.RIGHT | Gravity.TOP;
		//params.setTitle("Load Average");
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.addView(bb, params);
		bb.setOnTouchListener(new OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				Toast.makeText(getBaseContext(), "hello Homee clicked !!!!!!!!!", Toast.LENGTH_LONG).show();
				System.out.println("Touched =----- > ");
				return false;
			}
		});
	}
	
	@Override
	public void onDestroy()
	{
		if (mWindowManager != null)
		{
			if (touchLayout != null) mWindowManager.removeView(touchLayout);
		}
		
		super.onDestroy();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) Log.i(TAG, "Action :" + event.getAction() + "\t X :" + event.getRawX() + "\t Y :" + event.getRawY());
		
		return true;
	}
	
}
