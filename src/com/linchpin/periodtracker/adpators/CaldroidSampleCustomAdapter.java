package com.linchpin.periodtracker.adpators;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.joda.time.DateTime;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidGridAdapter;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.CalendarDayDetailModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.Utility;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter
{
	
	SimpleDateFormat	format	= new SimpleDateFormat("yyyyMMdd");
	LayoutInflater		inflater;
	
	public CaldroidSampleCustomAdapter(final Context context, final int month, final int year, HashMap<String, Object> caldroidData, HashMap<String, Object> extraData)
	{
		super(context, month, year, caldroidData, extraData);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	class ViewHolder
	{
		TextView	tv1;
		ImageView	intimateImageView, editNoteImageView, fertileOvulationView;
		
	}
	
	Theme	t;
	
	private void applyTheme(View v)
	{
		
		t = Theme.getCurrentTheme(context, v);
		if (t != null)
		{
			
			t.applyBackgroundDrawable(R.id.layout, "mpt_cal_itm_sltr");
			
		}
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		try
		{
			if (convertView == null)
			{
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.custom_cell, null);
				holder.tv1 = (TextView) convertView.findViewById(R.id.textdate);
				holder.intimateImageView = (ImageView) convertView.findViewById(R.id.intimateimage);
				holder.fertileOvulationView = (ImageView) convertView.findViewById(R.id.fetileorOvulationimage);
				holder.editNoteImageView = (ImageView) convertView.findViewById(R.id.editnote);
				convertView.setTag(holder);
				
			}
			else holder = (ViewHolder) convertView.getTag();
			applyTheme(convertView);
			CalendarDayDetailModel calendarDayDetailModel = (CalendarDayDetailModel) this.getExtraData().get(format.format(Utility.setHourMinuteSecondZero(new Date(getDatetimeList().get(position).getMillis()))));
			//	convertView.setTag(1000,calendarDayDetailModel);
			holder.intimateImageView.setVisibility(View.GONE);
			holder.fertileOvulationView.setVisibility(View.GONE);
			holder.editNoteImageView.setVisibility(View.GONE);
			
			if (null != calendarDayDetailModel)
			{
				
				if (calendarDayDetailModel.isHavingNotes())
				{
					holder.editNoteImageView.setVisibility(View.VISIBLE);
				}
				
				if (calendarDayDetailModel.isPeriodDay())
				{
					holder.fertileOvulationView.setVisibility(View.VISIBLE);
					holder.fertileOvulationView.setImageDrawable(context.getResources().getDrawable(R.drawable.period));
				}
				else if (calendarDayDetailModel.isFertileDay())
				{
					holder.fertileOvulationView.setVisibility(View.VISIBLE);
					holder.fertileOvulationView.setImageDrawable(context.getResources().getDrawable(R.drawable.fertile));
					
				}
				else if (calendarDayDetailModel.isOvulationDay())
				{
					holder.fertileOvulationView.setImageDrawable(context.getResources().getDrawable(R.drawable.ovulation));
					holder.fertileOvulationView.setVisibility(View.VISIBLE);
				}
				else
				{
					holder.fertileOvulationView.setVisibility(View.GONE);
				}
				
				if (calendarDayDetailModel.isIntimate())
				{
					holder.intimateImageView.setVisibility(View.VISIBLE);
				}
			}
			
			holder.tv1.setTextColor(Color.BLACK);
			
			// Get dateTime of this cell
			DateTime dateTime = getDatetimeList().get(position);
			Resources resources = context.getResources();
			
			// Set color of the dates in previous / next month
			if (dateTime.getMonthOfYear() != month)
			{
				holder.tv1.setTextColor(resources.getColor(R.color.caldroid_darker_gray));
			}
			
			boolean shouldResetDiabledView = false;
			boolean shouldResetSelectedView = false;
			
			// Customize for disabled dates and date outside min/max dates
			if ((minDateTime != null && dateTime.isBefore(minDateTime)) || (maxDateTime != null && dateTime.isAfter(maxDateTime)) || (disableDates != null && disableDates.indexOf(dateTime) != -1))
			{
				
				holder.tv1.setTextColor(CaldroidFragment.disabledTextColor);
				if (CaldroidFragment.disabledBackgroundDrawable == -1)
				{
					convertView.setBackgroundResource(R.drawable.disable_cell);
				}
				else
				{
					convertView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
				}
				
			}
			else
			{
				shouldResetDiabledView = true;
			}
			
			if (selectedDates != null && selectedDates.indexOf(dateTime) != -1)
			{
				if (CaldroidFragment.selectedBackgroundDrawable != -1)
				{
					convertView.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
				}
				else
				{
					if (t == null) convertView.setBackgroundColor(resources.getColor(R.color.caldroid_sky_blue));
					
					else convertView.setBackgroundDrawable(t.getDrawableResource("mpt_cal_itm_pressed_shp"));
				}
				
				holder.tv1.setTextColor(CaldroidFragment.selectedTextColor);
				
			}
			else
			{
				shouldResetSelectedView = true;
			}
			
			if (selectedDates.contains(dateTime))
			{
				if (t == null) convertView.setBackgroundResource(R.drawable.red_border_gray_bg);
				else convertView.setBackgroundDrawable(t.getDrawableResource("mpt_cal_itm_today_shp"));
				
			}
			
			if (shouldResetDiabledView && shouldResetSelectedView)
			{
				// Customize for today
				if (dateTime.equals(getToday()))
				{
					//ff	convertView.setBackgroundResource(R.drawable.green_border);
				}
				else
				{
					if (t == null) convertView.setBackgroundResource(R.drawable.custom_cell_graident_backgrouund);
					else convertView.setBackgroundDrawable(t.getDrawableResource("mpt_cal_itm_nor_shp"));
					//	convertView.setBackgroundResource(R.drawable.custom_cell_graident_backgrouund);
				}
			}
			
			holder.tv1.setText("" + dateTime.getDayOfMonth());
			
			if (dateTime.equals(getToday()))
			{
				if (t == null) convertView.setBackgroundResource(R.drawable.red_border_gray_bg);
				else convertView.setBackgroundDrawable(t.getDrawableResource("mpt_cal_itm_today_shp"));
			}
		}
		catch (Exception exception)
		{
		}
		
		return convertView;
	}
	
}
