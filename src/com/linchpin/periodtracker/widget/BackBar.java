package com.linchpin.periodtracker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.theme.Theme;

public class BackBar extends RelativeLayout
{
	OnClickListener	clickListener;
	
	public void setButtonClickListener(OnClickListener clickListener)
	{
		this.clickListener = clickListener;
		//title.setOnClickListener(clickListener);
		img.setOnClickListener(clickListener);
	}
	
	public BackBar(Context context)
	{
		super(context);
		applyTheme(null);
	}
	
	public BackBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		applyTheme(attrs);
	}
	
	public BackBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		applyTheme(attrs);
	}
	
	public void setText(String text)
	{
		title.setText(text);
	}
	
	TextView	title;
	ImageView	img;
	
	@SuppressWarnings("deprecation")
	private void applyTheme(AttributeSet attrs)
	{
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BackBar);
		//setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.back_bar, this, true);
		
		title = (TextView) getChildAt(1);
		title.setText(a.getString(R.styleable.BackBar_android_text));
		title.setTextColor(a.getColor(R.styleable.BackBar_android_textColor, 0xFF820787));
		img = (ImageView) getChildAt(0);
		img.setBackgroundDrawable(a.getDrawable(R.styleable.BackBar_backbarbackground));
		Theme t = Theme.getCurrentTheme(getContext());
		if (t != null)
		{
		t.applyBackgroundDrawable(img, "backbuttonselctor");
			
			t.applyTextColor(title, "text_color");
			//setPadding(0, 0, 0, 0);
			
		}
		a.recycle();
	}
}
