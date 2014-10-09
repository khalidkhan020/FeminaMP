package com.linchpin.periodtracker.partnersharing;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidListener;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.CalendarDayDetailModel;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.view.CaldroidSampleCustomFragmentView;
import com.linchpin.periodttracker.database.CalendarDBHandler;
import com.viewpagerindicator.IconPagerAdapter;

public class SharingCalender
{
	
	private FragmentActivity					context;
/*	private LayoutInflater						mInflater;
	int											type;*/
	Handler										handler	= new Handler();
	int											status;
	private JSONObject							jsonObject;
	private CaldroidSampleCustomFragmentView	caldroidFragment;
	
	private View								previousDateView;
	private Date								currentDate;
	
	public SharingCalender(FragmentActivity context, LayoutInflater mInflater, int type)
	{
	//	this.context = context;
		
		/*this.mInflater = mInflater;
		this.type = type;*/
	}
	
	/*public void onResume()
	{
		try
		{
			selectDate(currentDate);
			CaldroidSampleCustomFragmentView.adapter.notifyDataSetChanged();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	*/
	public void selectDate(final Date date)
	{
		try
		{
			CalendarDBHandler calendarDBHandler = new CalendarDBHandler(context.getApplicationContext());
			DayDetailModel dayDetailModel = (DayDetailModel) calendarDBHandler.getDayDetailForDateInCalender(date);
			
			if (date.equals(currentDate))
			{
				if (previousDateView != null)
				{
					if (null != dayDetailModel.getNote() && !dayDetailModel.getNote().toString().trim().equals(""))
					{
						if (dayDetailModel.getNote().equals("")) previousDateView.findViewById(R.id.editnote).setVisibility(View.INVISIBLE);
						else previousDateView.findViewById(R.id.editnote).setVisibility(View.VISIBLE);
						if (!dayDetailModel.isIntimate()) previousDateView.findViewById(R.id.intimateimage).setVisibility(View.INVISIBLE);
						else previousDateView.findViewById(R.id.intimateimage).setVisibility(View.VISIBLE);
					}
					previousDateView.invalidate();
				}
				
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	final 
	class TestFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
	    protected static final String[] CONTENT = new String[] { "This", "Is", "A", "Test", };
	    protected static final int[] ICONS = new int[] {
	            R.drawable.perm_group_calendar,
	            R.drawable.perm_group_camera,
	            R.drawable.perm_group_device_alarms,
	            R.drawable.perm_group_location
	    };
	    FragmentManager fm;
	    FragmentActivity ctx;
	    private int mCount = CONTENT.length;

	    public TestFragmentAdapter(FragmentActivity ctx,FragmentManager fm) {
	    	
	        super(fm);
	        this.ctx=ctx;
	    }

	    @Override
	    public Fragment getItem(int position) {
	    	
	    
	        return ;
	    }

	    @Override
	    public int getCount() {
	        return mCount;
	    }

	    @Override
	    public CharSequence getPageTitle(int position) {
	      return TestFragmentAdapter.CONTENT[position % CONTENT.length];
	    }

	    @Override
	    public int getIconResId(int index) {
	      return ICONS[index % ICONS.length];
	    }

	    public void setCount(int count) {
	        if (count > 0 && count <= 10) {
	            mCount = count;
	            notifyDataSetChanged();
	        }
	    }
	}aldroidListener	listener	= new CaldroidListener()
									{
										SimpleDateFormat	format	= new SimpleDateFormat("yyyyMMdd");
										
										@Override
										public void onSelectDate(final Date date, View view)
										{
											try
											{
												LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
												if (view == null) view = inflater.inflate(R.layout.custom_cell, null);
												if (currentDate != null && previousDateView != null && currentDate.compareTo(Utility.setHourMinuteSecondZero(new Date())) != 0)
												
												previousDateView.findViewById(R.id.layout).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.custom_cell_graident_backgrouund));
												if (currentDate != null && date.compareTo(Utility.setHourMinuteSecondZero(new Date())) != 0)
												
												view.findViewById(R.id.layout).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.green_border));
												currentDate = date;
												previousDateView = view;
												selectDate(currentDate);
												CaldroidSampleCustomFragmentView.adapter.notifyDataSetChanged();
											}
											catch (Exception e)
											{
												e.printStackTrace();
											}
											
										}
										
										@Override
										public void onChangeMonth(int month, int year)
										{
											
											try
											{
												int currentmonth = new Date().getMonth();
												CalendarDBHandler calendarDBHandler = new CalendarDBHandler(context);
												Calendar calendarTemp = GregorianCalendar.getInstance();
												
												calendarTemp.set(Calendar.MONTH, month - 1);
												calendarTemp.set(Calendar.YEAR, year);
												calendarTemp.set(Calendar.DATE, 1);
												if (currentmonth < month) calendarTemp.add(Calendar.MONTH, -1);
												else calendarTemp.add(Calendar.MONTH, 0);
												if (currentmonth != month - 1) selectDate(Utility.setHourMinuteSecondZero(calendarTemp.getTime()));
												else selectDate(Utility.setHourMinuteSecondZero(new Date()));
												List<CalendarDayDetailModel> calendarDayDetailModels = calendarDBHandler.getDetailForDayInCalendor(Utility.addDays(new Date(calendarTemp.getTimeInMillis()), -23), Utility.addDays(new Date(calendarTemp.getTimeInMillis()), 67));
												for (CalendarDayDetailModel calendarDayDetailModel : calendarDayDetailModels)
													caldroidFragment.getExtraData().put(format.format(calendarDayDetailModel.getDate()), calendarDayDetailModel);
												CaldroidSampleCustomFragmentView.adapter.notifyDataSetChanged();
											}
											catch (Exception e)
											{
												e.printStackTrace();
											}
											
										}
										
										@Override
										public void onLongSelectDate(View view, Date date)
										{
										}
										
									};
	
	public Fragment getView()
	{
	//	View convertView = mInflater.inflate(R.layout.partner_calender, null);
		
		currentDate = Utility.setHourMinuteSecondZero(new Date());
		caldroidFragment = new CaldroidSampleCustomFragmentView();
		
		Bundle args = new Bundle();
		Calendar cal = Calendar.getInstance();
		args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH));
		args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
		args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
		args.putBoolean(CaldroidFragment.FIT_ALL_MONTHS, false);
		args.putInt(CaldroidFragment.FORWARD, 0);
		if (Integer.valueOf(PeriodTrackerObjectLocator.getInstance().getDayOfWeek()) != 0) args.putInt("startDayOfWeek", Integer.valueOf(PeriodTrackerObjectLocator.getInstance().getDayOfWeek()));
		caldroidFragment.setArguments(args);
		
	//	FragmentTransaction t = context.getSupportFragmentManager().beginTransaction();
	//	t.replace(R.id.partner_calender, caldroidFragment);
	//	t.commit();
		caldroidFragment.setCaldroidListener(listener);
		return caldroidFragment;
	}
	
}
