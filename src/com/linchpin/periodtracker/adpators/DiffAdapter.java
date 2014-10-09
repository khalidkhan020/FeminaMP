package com.linchpin.periodtracker.adpators;

import org.taptwo.android.widget.TitleProvider;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.partnersharing.Invitatin;
import com.linchpin.periodtracker.partnersharing.SharingCalender;
import com.linchpin.periodtracker.partnersharing.SharingSetting;
import com.linchpin.periodtracker.view.CaldroidSampleCustomFragmentView;

public class DiffAdapter extends BaseAdapter implements TitleProvider
{
	
	private final int		VIEW1			= 0;
	private final int		VIEW2			= 1;
	private final int		VIEW3			= 2;
	int						VIEW_MAX_COUNT	= VIEW3 + 1;
	private final String[]	names			=
											{
			"View1", "View2", "View3", "View4"
											};
	FragmentActivity		context;
	private LayoutInflater	mInflater;
	SharingCalender			sharingcalender;
	
	View CalV;
	public DiffAdapter(FragmentActivity context)
	{
		this.context = context;
		
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		
		
	}
	
	public void onResume()
	{
		if (sharingcalender != null) sharingcalender.onResume();
	}
	
	@Override
	public int getItemViewType(int position)
	{
		return position;
	}
	
	int	type	= 0;
	
	public void setType(int type)
	{
		this.type = type;
		
		if (type == 0 || type == 1 || type == 2 || type == 3 || type == 4) VIEW_MAX_COUNT = 2;
		else if (type == 6 || type == 6) VIEW_MAX_COUNT = 2;
		else if (type == 5 || type == 7) VIEW_MAX_COUNT = 2;
		sharingcalender = new SharingCalender(context,  mInflater, type);
	}
	
	@Override
	public int getViewTypeCount()
	{
		return VIEW_MAX_COUNT;
	}
	
	@Override
	public int getCount()
	{
		return VIEW_MAX_COUNT;
	}
	
	@Override
	public Object getItem(int position)
	{
		return position;
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		int view = getItemViewType(position);
		if (convertView == null)
		{
			TextView id;
			switch (view)
			{
				case VIEW1:
					Invitatin invitatin = new Invitatin(context, convertView, mInflater, type);
					convertView = invitatin.getView();
					break;
				case VIEW2:
					if (type == 6 | type == 8)
					{
						SharingSetting sharingstting = new SharingSetting(context, new View(context), mInflater, type);
						convertView = sharingstting.getView();
					}
					else
					{
						
						convertView = sharingcalender.getView();
					}
					
					break;
				case VIEW3:
					convertView = mInflater.inflate(R.layout.partner_timeline, null);
					break;
			}
		}
		
		return convertView;
	}
	
	public String getTitle(int position)
	{
		return names[position];
	}
	
}
