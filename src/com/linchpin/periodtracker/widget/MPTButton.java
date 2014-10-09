package com.linchpin.periodtracker.widget;

import com.linchpin.periodtracker.theme.Theme;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class MPTButton extends Button
{
	
	public MPTButton(Context context)
	{
		super(context);
		applyTheme();
	}
	
	public MPTButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		applyTheme();
	}
	
	public MPTButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		applyTheme();
	}
	
	private void applyTheme()
	{
		
		Theme t = Theme.getCurrentTheme(getContext());
		if (t != null)
		{
			setBackgroundDrawable(t.getDrawableResource("mpt_button_sltr"));
			setTextColor(t.getColor("co_btn_fg"));
			
		}
	}
	
}
