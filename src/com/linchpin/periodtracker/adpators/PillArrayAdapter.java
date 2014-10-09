package com.linchpin.periodtracker.adpators;

import java.util.List;

import com.linchpin.periodtracker.theme.Theme;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PillArrayAdapter extends ArrayAdapter<Integer>
{
	Theme t;
	public PillArrayAdapter(Context context, int resource, List<Integer> objects)
	{
		
		super(context, resource,  objects);
		 t=Theme.getCurrentTheme(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		TextView tv=(TextView) super.getView(position, convertView, parent);
		if(tv!=null&&t!=null)
		{
			tv.setTextColor(t.getColor("text_color"));
			//tv.setBackgroundColor(t.getColor("view_bg"));
		}
		return  tv;
	}
	
}
