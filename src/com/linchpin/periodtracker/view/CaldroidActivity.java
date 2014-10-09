package com.linchpin.periodtracker.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.CalendarDayDetailModel;
import com.linchpin.periodtracker.partnersharing.LoginScreen;
import com.linchpin.periodtracker.partnersharing.PartnerHomeActivity;
import com.linchpin.periodtracker.partnersharing.PartnerTimeLineFragment;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.FragList;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.SwipFragmentAdapter;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.widget.BackBar;
import com.linchpin.periodttracker.database.CalendarDBHandler;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

@SuppressLint("SimpleDateFormat")
public class CaldroidActivity extends FragmentActivity
{
	@Override
	public void onBackPressed()
	{
		CaldroidActivity.this.finish();
		APP.GLOBAL().exicuteLIOAnim(CaldroidActivity.this);
	}
	
	private MyCalenderFrag	caldroidFragment;
	private View			view		= null;
	private Date			date;
	private String			SAVE_STATE	= "CALDROID_SAVED_STATE";
	
	private void applyTheme()
	{
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			t.applyTextColor(R.id.calendornotes, "text_color");
			t.applyTextColor(R.id.namemoods, "text_color");
			t.applyTextColor(R.id.namesymtoms, "text_color");
			t.applyTextColor(R.id.weightoncalendar, "text_color");
			t.applyTextColor(R.id.temperatureoncalendar, "text_color");
			t.applyTextColor(R.id.temperatureoncalendarvaule, "text_color");
			t.applyTextColor(R.id.weightoncalendarvaule, "text_color");
			t.applyBackgroundColor(R.id.indicator, "heading_bg");
			//			t.applyTextColor(R.id.t1, "text_color");
			//			t.applyTextColor(R.id.t2, "text_color");
			//			t.applyTextColor(R.id.t3, "text_color");
			//			t.applyTextColor(R.id.t4, "text_color");
			//			
			t.applyBackgroundColor(R.id.l1, "view_bg");
			t.applyBackgroundColor(R.id.moodsoncalendarlist, "view_bg");
			t.applyBackgroundColor(R.id.Symtomsoncalendarlist, "view_bg");
			//			t.applyBackgroundColor(R.id.temperatureoncalendarvaule, "view_bg");
			//			t.applyBackgroundColor(R.id.temperatureoncalendarvaule, "view_bg");
			//			t.applyBackgroundColor(R.id.temperatureoncalendarvaule, "view_bg");
			//			
			t.applyBackgroundDrawable(R.id.calendornotes, "mpt_note_bg");
			t.applyBackgroundDrawable(R.id.bottominfobar, "mpt_bottom_img");
		}
		
	}
	
	ArrayList<FragList>			fragments	= new ArrayList<FragList>();
	private SwipFragmentAdapter	mAdapter;
	private ViewPager			mPager;
	private PageIndicator		mIndicator;
	BackBar						backBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_titles);
		backBar = (BackBar) findViewById(R.id.back_pan);
		backBar.setText(getString(R.string.my_home));
		backBar.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				onBackPressed();
				
			}
		});
		
		caldroidFragment = new MyCalenderFrag();
		if (savedInstanceState != null) caldroidFragment.restoreStatesFromKey(savedInstanceState, SAVE_STATE);
		else
		{
			Bundle args = new Bundle();
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.FIT_ALL_MONTHS, false);
			args.putInt(CaldroidFragment.FORWARD, 2);
			args.putBoolean(CaldroidFragment.BACKBAR_NEEDED, false);
			if (Integer.valueOf(PeriodTrackerObjectLocator.getInstance().getDayOfWeek()) != 0) args.putInt("startDayOfWeek", Integer.valueOf(PeriodTrackerObjectLocator.getInstance().getDayOfWeek()));
			caldroidFragment.setArguments(args);
			caldroidFragment.setCaldroidListener(listener);
		}
		fragments.add(new FragList(caldroidFragment, getString(R.string.calender_title), 0));
		fragments.add(new FragList(new TimeLineFragment(), getString(R.string.timeline_title), 0));
		mAdapter = new SwipFragmentAdapter(fragments, getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		applyTheme();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
		//	if (!APP.GLOBAL().getPreferences().getBoolean("purchased", false)) PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	
	@Override
	protected void onResume()
	{
		
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PURCHASED.key, false)) PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		
		super.onResume();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2)
	{
		listener.onSelectDate(date, view);
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	CaldroidListener	listener	= new CaldroidListener()
										{
											SimpleDateFormat	format	= new SimpleDateFormat("yyyyMMdd");
											Theme				t		= Theme.getCurrentTheme(CaldroidActivity.this);
											
											@Override
											public void onSelectDate(final Date date, View view)
											{
												try
												{
													LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
													if (view == null) view = inflater.inflate(R.layout.custom_cell, null);
													if (caldroidFragment.currentDate != null && caldroidFragment.previousDateView != null && caldroidFragment.currentDate.compareTo(Utility.setHourMinuteSecondZero(new Date())) != 0) if (t != null) caldroidFragment.previousDateView.findViewById(
															R.id.layout).setBackgroundDrawable(t.getDrawableResource("mpt_cal_itm_nor_shp"));
													else caldroidFragment.previousDateView.findViewById(R.id.layout).setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_cell_graident_backgrouund));
													if (caldroidFragment.currentDate != null && date.compareTo(Utility.setHourMinuteSecondZero(new Date())) != 0) if (t != null) view.findViewById(R.id.layout).setBackgroundDrawable(t.getDrawableResource("mpt_cal_itm_pressed_shp"));
													else view.findViewById(R.id.layout).setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border));
													caldroidFragment.currentDate = date;
													caldroidFragment.previousDateView = view;
													caldroidFragment.selectDate(caldroidFragment.currentDate);
													CaldroidActivity.this.view = view;
													CaldroidActivity.this.date = date;
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
													CalendarDBHandler calendarDBHandler = new CalendarDBHandler(getApplicationContext());
													Calendar calendarTemp = GregorianCalendar.getInstance();
													
													calendarTemp.set(Calendar.MONTH, month - 1);
													calendarTemp.set(Calendar.YEAR, year);
													calendarTemp.set(Calendar.DATE, 1);
													if (currentmonth < month) calendarTemp.add(Calendar.MONTH, -1);
													else calendarTemp.add(Calendar.MONTH, 0);
													if (currentmonth != month - 1) caldroidFragment.selectDate(Utility.setHourMinuteSecondZero(calendarTemp.getTime()));
													else caldroidFragment.selectDate(Utility.setHourMinuteSecondZero(new Date()));
													List<CalendarDayDetailModel> calendarDayDetailModels = calendarDBHandler.getDetailForDayInCalendor(Utility.addDays(new Date(calendarTemp.getTimeInMillis()), -23), Utility.addDays(new Date(calendarTemp.getTimeInMillis()), 67));
													for (CalendarDayDetailModel calendarDayDetailModel : calendarDayDetailModels)
														caldroidFragment.getExtraData().put(format.format(calendarDayDetailModel.getDate()), calendarDayDetailModel);
												}
												catch (Exception e)
												{
													e.printStackTrace();
												}
											}
											
											@Override
											public void onLongSelectDate(View view, Date date)
											{
												CaldroidActivity.this.view = view;
												CaldroidActivity.this.date = date;
												Intent intent = new Intent(CaldroidActivity.this, AddNoteView.class);
												intent.putExtra("date", date.getTime());
												startActivityForResult(intent, 1001);
												this.onSelectDate(date, view);
												view.invalidate();
											}
											
										};
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		if (caldroidFragment != null) caldroidFragment.saveStatesToKey(outState, SAVE_STATE);
		
	}
	
	/*	@Override
		public void onClick(View v)
		{
			Intent intent;
			switch (v.getId())
			{
				case R.id.calendarinfobutton:
					intent = new Intent(CaldroidActivity.this, HomeScreenHelp.class);
					intent.putExtra("classname", "calender");
					startActivity(intent);
					overridePendingTransition(R.anim.left_in, R.anim.left_out);
					break;
				case R.id.calendornotes:
					intent = new Intent(CaldroidActivity.this, AddNoteView.class);
					intent.putExtra("date", caldroidFragment.currentDate.getTime());
					startActivity(intent);
					overridePendingTransition(R.anim.left_in, R.anim.left_out);
				default:
					break;
			}
		}*/
}
