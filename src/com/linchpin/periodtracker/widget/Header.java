package com.linchpin.periodtracker.widget;

import com.linchpin.periodtracker.theme.Theme;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class Header extends TextView
{
	
	
	public Header(Context context)
	{
		super(context);
		applyTheme();
	}
	
	public Header(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		applyTheme();
	}
	
	public Header(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		applyTheme();
	}
	private void applyTheme()
	{
		
		Theme t = Theme.getCurrentTheme(getContext());
		if (t != null)
		{			
			setTextSize(16);
			setPadding(12, 0, 0, 0);
			setTextColor(t.getColor("heading_fg"));
			setBackgroundColor(t.getColor("heading_bg"));
			
			
		}}
	
}
