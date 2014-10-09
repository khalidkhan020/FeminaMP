package com.linchpin.periodtracker.adpators;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.linchpin.periodtracker.theme.Theme;

public class AdapterForCountry extends ArrayAdapter
{
	
	Activity	activity;
	
	private void applyTheme(View v)
	{
		Theme t = Theme.getCurrentTheme(activity);
		if (t != null)
		{
			t.applyTextColor((TextView) v, "text_color");
		}
	}
	
	public AdapterForCountry(Activity activity, int resource, ArrayList<String> country)
	{
		super(activity, resource, country);
		this.activity = activity;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = super.getView(position, convertView, parent);
		if (v != null) applyTheme(v);
		return v;
		
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		View v = super.getDropDownView(position, convertView, parent);
		if (v != null) applyTheme(v);
		return v;
	}
}
