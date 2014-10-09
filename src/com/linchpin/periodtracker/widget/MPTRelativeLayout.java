package com.linchpin.periodtracker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.linchpin.periodtracker.theme.Theme;

public class MPTRelativeLayout extends RelativeLayout
{
	
	public MPTRelativeLayout(Context context)
	{
		super(context);
		applyTheme(null);
	}
	
	public MPTRelativeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		applyTheme(attrs);
	}
	
	public MPTRelativeLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		applyTheme(attrs);
	}
	
	private void applyTheme(AttributeSet attrs)
	{
		/*
		Theme t = Theme.getCurrentTheme(getContext());
		if (t != null)
		{
			setBackgroundDrawable(t.getDrawableResource("mpt_background"));
			
		}*/
	}
	
}
