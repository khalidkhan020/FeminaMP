package com.linchpin.periodtracker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.linchpin.periodtracker.theme.Theme;

public class HelpButton extends ImageView
{

	public HelpButton(Context context)
	{
		super(context);
		applyTheme();
	}

	public HelpButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		applyTheme();
	}

	public HelpButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		applyTheme();
	}
	
	
	private void applyTheme()
	{
		
		Theme t = Theme.getCurrentTheme(getContext());
		if (t != null)
		{			
			setImageDrawable(t.getDrawableResource("help"));
			
		}}
}
