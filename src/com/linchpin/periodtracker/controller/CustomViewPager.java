package com.linchpin.periodtracker.controller;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
	private boolean swipeable = true;

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// Call this method in your motion events when you want to disable or enable
	// It should work as desired.
	public void setSwipeable(boolean swipeable) {
		this.swipeable = swipeable;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// return (this.swipeable) ? false : false;
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
