package com.linchpin.periodtracker.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.PeriodLogPagerAdpator;
import com.linchpin.periodtracker.controller.PeriodLogTabListener;
import com.linchpin.periodtracker.controller.ViewPageListener;
import com.linchpin.periodtracker.fragments.FertileLogFragment;
import com.linchpin.periodtracker.fragments.LogFragment;
import com.linchpin.periodtracker.fragments.OvulationLogFragment;
import com.linchpin.periodtracker.fragments.PeriodLogFragment;
import com.linchpin.periodtracker.interfaces.CalenderDialog;
import com.linchpin.periodtracker.interfaces.PeriodMessanger;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.CustomAlertDialog;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.utlity.CustomAlertDialog.onButoinClick;
import com.linchpin.periodtracker.view.DatePickerDialog.OnDateSetListener;
import com.linchpin.periodtracker.widget.BackBar;
import com.linchpin.periodtracker.widget.Header;
import com.linchpin.periodttracker.database.DBManager;
import com.linchpin.periodttracker.database.DBProjections;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;

public class PeriodLogPagerView extends FragmentActivity implements PeriodMessanger, OnClickListener, OnDateSetListener, CalenderDialog
{
	public static final int			PERIOD_LOG_FRAGMENT_ID		= 0;
	public static final int			FERTILITY_LOG_FRAGMENT_ID	= 1;
	public static final int			OVULATION_LOG_FRAGMENT_ID	= 2;
	public static final int			GET_END_DATE				= 1;
	public static final int			EDIT_START_DATE				= 2;
	public static final int			EDIT_END_DATE				= 3;
	public static final int			NEW_START_DATE				= 4;
	public static final int			NEW_END_DATE				= 5;
	public static final int			UPDATE_END_DATE				= 6;
	ArrayList<PeriodMessanger>		messagsToFrag				= new ArrayList<PeriodMessanger>();
	private List<TabInfo>			mTabs						= new ArrayList<TabInfo>();
	public ImageButton				bAddPast;
	private PeriodLogPagerAdpator	mPeriodLogPagerAdapter;
	public TabHost					mTabHost;
	private PeriodLogDBHandler		periodLogDBHandler;
	public BackBar					textView;
	public ViewPager				mViewpager;
	private Date					startDate;
	private Date					endDate;
	Calendar						oldcalCalendar;
	PeriodLogModel					logModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.periodlog_pager_tab);
		applyTheme();
		if (t != null)
		{
			findViewById(R.id.content).setBackgroundDrawable(t.getDrawableResource("mpt_background"));
			findViewById(R.id.l1).setBackgroundDrawable(t.getDrawableResource("view_bg"));
			t.applyBackgroundColor(findViewById(android.R.id.tabs), "co_btn_fg");
		}
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		bAddPast = (ImageButton) findViewById(R.id.addpastrecord);
		bAddPast.setOnClickListener(this);
		textView = (BackBar) findViewById(R.id.backbutton);
		mViewpager = (ViewPager) findViewById(R.id.viewpager);
		findViewById(R.id.backbutton).setOnClickListener(this);
		mTabHost.setup();
		mTabHost.setOnTabChangedListener((OnTabChangeListener) new PeriodLogTabListener(mTabHost, mViewpager, bAddPast));
		ViewPageListener PageListener = new ViewPageListener(mTabHost, PeriodTrackerConstants.PERIOD_LOG_PAGER_VIEW, this, (HorizontalScrollView) findViewById(R.id.periologhorizontalscroll));
		mViewpager.setOnPageChangeListener(PageListener);
		
		/*************** Adding Tabs in tabHost ***************/
		
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.period_logs)).setIndicator(createTabView(this, getResources().getString(R.string.period_logs))), PeriodLogFragment.class, null);
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.fertile)).setIndicator(createTabView(this, getResources().getString(R.string.fertile))), FertileLogFragment.class, null);
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.ovulation)).setIndicator(createTabView(this, getResources().getString(R.string.ovulation))), OvulationLogFragment.class, null);
		
		/***************** Setting Adpator **************/
		
		mPeriodLogPagerAdapter = new PeriodLogPagerAdpator(getSupportFragmentManager(), mTabs, getApplicationContext());
		mViewpager.setAdapter(mPeriodLogPagerAdapter);
		periodLogDBHandler = new PeriodLogDBHandler(this);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		EasyTracker.getInstance(this).activityStop(this);
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PURCHASED.key, false)) PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
	}
	
	static Theme	t;
	
	private void applyTheme()
	{
		
		t = Theme.getCurrentTheme(this);
		if (t != null)
		{
			t.applyBackgroundDrawable(findViewById(R.id.addpastrecord), "mpt_more");
			
			/*setTextSize(16);
			setPadding(8, 0, 0, 0);
			setTextColor(t.getColor("heading_fg"));
			setBackgroundColor(t.getColor("heading_bg"));*/
			
		}
	}
	
	private static View createTabView(final Context context, final String text)
	{
		final View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		if (t != null)
		{
			t.applyBackgroundColor(view, "heading_bg");
			
		}
		TextView tv = ((TextView) view.findViewById(R.id.tabsText));
		tv.setText(text);
		if (t != null) t.applyBackgroundDrawable(tv, "mpt_tab_sltr");
		
		return view;
	}
	
	/************************** Method to adding tabs into tab host *************************/
	
	public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args)
	{
		tabSpec.setContent(new TabFactory(getApplicationContext()));
		TabInfo info = new TabInfo(tabSpec.getTag(), clss, args);
		mTabs.add(info);
		mTabHost.addTab(tabSpec);
		// mPeriodLogPagerAdapter.notifyDataSetChanged();
		
	}
	
	/*public void getAllLogsFromDatebase()
	{
		periodLogDBHandler = new PeriodLogDBHandler(getApplicationContext());
		periodTrackerModelInterfaces = periodLogDBHandler.getPastFertileAndOvulationDates();
		// (PeriodTrackerObjectLocator.getInstance().getProfileId());
	}*/
	
	@Override
	public void sendMessage(int id, Bundle bundle)
	{
		
	}
	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		APP.GLOBAL().exicuteLIOAnim(PeriodLogPagerView.this);
	}
	
	@Override
	public DatePickerDialog showDatePickerDialog(int titleid, Calendar calendar, Object object, int id)
	{
		this.oldcalCalendar = calendar;
		if (object != null) logModel = (PeriodLogModel) object;
		
		DatePickerDialog datePickerDailog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), id);
		
		datePickerDailog.setTitle(getResources().getString(titleid));
		
		if (Utility.checkAndroidApiVersion())
		{
			datePickerDailog.getDatePicker().setMinDate(new Date("1/1/2003").getTime());
		}
		return datePickerDailog;
	}
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.addpastrecord)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 3);
			showDatePickerDialog(R.string.setstartdate, calendar, null, R.id.addpastrecord).show();
		}
		else if (id == R.id.backbutton)
		{
			finish();
			APP.GLOBAL().exicuteLIOAnim(PeriodLogPagerView.this);
		}
		else
		{
		}
	}
	
	@Override
	public void onDateSet(DatePicker view, int id, int year, int month, int day)
	{
		if (year == month && month == day && day == 12345 && id == NEW_END_DATE)
		{
			insertPastRecordInDatabase(startDate, new Date("1/1/1900"));
		}
		else if (year != 12345 && month != 12345 && day != 12345)
		{
			if(id==R.id.addpastrecord)
			{
				startDate = Utility.createDate(year, month, day);
				addpastRecordStartDateValidation(startDate);
			}
			else
			switch (id)
			{
				case GET_END_DATE:
					endDate = Utility.createDate(year, month, day);
					addPastRecordEndDateValidation(endDate);
					
					break;
				case EDIT_START_DATE:
					startDate = Utility.createDate(year, month, day);
					updateStartDate();
					break;
				case EDIT_END_DATE:
					endDate = Utility.createDate(year, month, day);
					updateEndDate();
				case UPDATE_END_DATE:
					startDate = logModel.getStartDate();
					endDate = Utility.createDate(year, month, day);
					updateEndDate();
					break;
				case NEW_START_DATE:
					startDate = Utility.createDate(year, month, day);
					newStartDateValidation(startDate);
					
					break;
				case NEW_END_DATE:
					endDate = Utility.createDate(year, month, day);
					newEndDateValidation(endDate);
					break;
				default:
					break;
			}
		}
	}
	
	/*
	public void updateEndDates(Date endDate)
	{
		periodLogDBHandler = new PeriodLogDBHandler(this);
		;
		logModel = (PeriodLogModel) periodTrackerModelInterfaces.get(0);
		endDate = Utility.setHourMinuteSecondZero(endDate);
		logModel.setEndDate(endDate);
		if (periodLogDBHandler.updatePeriodLogWhenEditRecord(logModel))
		{
			// refreshPastListRecord();
			
			 * bStartDate.setEnabled(true);
			 * tStartDate.setTextColor(Color.parseColor
			 * (getString(R.color.newdatecolor)));
			 * FertileDaysFragments.fertileDaysFragments
			 * .bStartDate.setEnabled(true);
			 * tStartDate.setText(getResources().getString
			 * (R.string.period_start_date));
			 * tEndDate.setText(getResources().getString
			 * (R.string.period_end_date)); tEndDate.setEnabled(false);
			 * tEndDate.
			 * setTextColor(Color.parseColor(getResources().getString(R.
			 * color.disabledatecolor)));
			 if (FertileDaysFragments.fertileDaysFragments != null)
			{
				FertileDaysFragments.fertileDaysFragments.initStartAndEndDateButton(FertileDaysFragments.fertileDaysFragments.mView);
			}
			if (OvulationDaysFragments.ovulationDaysFragments != null)
			{
				FertileDaysFragments.fertileDaysFragments.initStartAndEndDateButton(OvulationDaysFragments.ovulationDaysFragments.mView);
			}
		}
		// RefreshLengthItems();
		
	}
	*/
	public void addpastRecordStartDateValidation(Date startDate)
	{
		boolean valid = true;
		String invalidmsg = "";
		if (!Utility.isDateLessThanCurrent(startDate))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthancallmessage);
		}
		else if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()) || periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		}
		else if (!periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthanLastestRecord);
		}
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				bAddPast.callOnClick();
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		else
		{
			Calendar calendar = new GregorianCalendar();
			Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
			if (date.getTime() >= startDate.getTime()) calendar.setTime(date);
			else calendar.setTime(new Date());
			showDatePickerDialog(R.string.setenddate, calendar, null, GET_END_DATE).show();
			
		}
		
	}
	
	public void newStartDateValidation(final Date startDate)
	{
		boolean valid = true;
		String invalidmsg = "";
		if (!Utility.isDateLessThanCurrent(startDate))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthancallmessage);
		}
		else if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()) || periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		}
		else if (periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.graterthanLastestRecord);
		}
		final boolean valid1 = valid;
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(startDate);
				showDatePickerDialog(R.string.setenddate, calendar, null, NEW_START_DATE).show();
				if (valid1)
				{
					APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
					//APP.PREF.SHARE_NOTIFY.=true;
				}
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		else
		{
			Calendar calendar = new GregorianCalendar();
			Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
			if (date.getTime() >= startDate.getTime()) calendar.setTime(date);
			else calendar.setTime(new Date());
			showDatePickerDialog(R.string.setenddate, calendar, null, NEW_END_DATE).show();
			if (valid1)
			{
				APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
				//APP.PREF.SHARE_NOTIFY.=true;
			}
		}
		
	}
	
	private void newEndDateValidation(Date endDate)
	{
		boolean valid = true;
		String invalidmsg = "";
		if (!Utility.isEndDateGreaterThanStart(startDate, endDate))
		{
			valid = false;
			invalidmsg = getString(R.string.enddategreaterthanstart);
		}
		else if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()) || periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		}
		else if (periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthanLastestRecord);
		}
		final boolean valid1 = valid;
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				Calendar calendar = new GregorianCalendar();
				Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
				if (date.getTime() >= startDate.getTime())
				{
					calendar.setTime(date);
				}
				else
				{
					calendar.setTime(new Date());
				}
				showDatePickerDialog(R.string.setenddate, calendar, null, GET_END_DATE).show();
				if (valid1)
				{
					APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
					//APP.PREF.SHARE_NOTIFY.=true;
				}
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				
				dialog.dismiss();
				
			}
		});
		
		else
		{
			insertPastRecordInDatabase(startDate, endDate);
			if (valid1)
			{
				APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
				//APP.PREF.SHARE_NOTIFY.=true;
			}
		}
	}
	
	private void addPastRecordEndDateValidation(Date endDate)
	{
		boolean valid = true;
		String invalidmsg = "";
		
		if (!Utility.isEndDateGreaterThanStart(startDate, endDate))
		{
			valid = false;
			invalidmsg = getString(R.string.enddategreaterthanstart);
		}
		else if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()) || periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		}
		else if (!periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthanLastestRecord);
		}
		
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				Calendar calendar = new GregorianCalendar();
				Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
				if (date.getTime() >= startDate.getTime())
				{
					calendar.setTime(date);
				}
				else
				{
					calendar.setTime(new Date());
				}
				showDatePickerDialog(R.string.setenddate, calendar, null, GET_END_DATE).show();
				
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				
			}
		});
		
		else
		{
			insertPastRecordInDatabase(startDate, endDate);
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
			
		}
	}
	
	public PeriodLogModel insertPastRecordInDatabase(Date startDate, Date endDate)
	{
		periodLogDBHandler = new PeriodLogDBHandler(this);
		startDate = Utility.setHourMinuteSecondZero(startDate);
		endDate = Utility.setHourMinuteSecondZero(endDate);
		PeriodLogModel model = new PeriodLogModel(0, PeriodTrackerObjectLocator.getInstance().getProfileId(), startDate, endDate, 0, 0, false, false);
		if (periodLogDBHandler.addPeriodLog(model))
		{
			refresh();
			
			//	messagsToFrag.get(PERIOD_LOG_FRAGMENT_ID).sendMessage(LogFragment.REFRESH_PAST_LIST, null);
		}
		return model;
	}
	
	private void refresh()
	{
		for (PeriodMessanger messanger : messagsToFrag)
			messanger.sendMessage(LogFragment.REFRESH_PAST_LIST, null);
	}
	
	@Override
	public void bind(int id, PeriodMessanger messanger)
	{
		messagsToFrag.add(id, messanger);
		
	}
	
	/*
	 * public void updateStartDate(Date startDate, int position) {
	 * 
	 * periodLogDBHandler = new PeriodLogDBHandler(getActivity());
	 * periodLogModel = (PeriodLogModel) pastRecordList.get(position);
	 * periodLogModel.setStartDate(startDate); Date date; Calendar calendar =
	 * new GregorianCalendar(); date = Utility.addDays(startDate,
	 * PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
	 * calendar.setTime(date); editEndDatePickerDialog = new
	 * DatePickerDialog(getActivity(), endDateSetListener,
	 * calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
	 * calendar.get(Calendar.DATE));
	 * editEndDatePickerDialog.setTitle(getResources
	 * ().getString(R.string.setenddate)); editEndDatePickerDialog.show();
	 * 
	 * }
	 */
	public void updateStartDate()
	{
		
		boolean valid = true;
		String invalidmsg = "";
		/*
		 * if (!Utility.isDateLessThanCurrent(startDate)) { valid = false;
		 * invalidmsg = getString(R.string.lessthancallmessage); } else if
		 * (Utility.isGreaterThan(new Date(oldcalCalendar.getTimeInMillis()),
		 * startDate)) { valid = false; invalidmsg =
		 * getString(R.string.lessthanLastestRecord);
		 * 
		 * }
		 */
		if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, oldcalCalendar.getTimeInMillis(), startDate.getTime())
				|| periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE, oldcalCalendar.getTimeInMillis(), startDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		}
		/*
		 * else if
		 * (!periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER
		 * , DBProjections.PT_START_DATE, startDate.getTime())) { valid = false;
		 * invalidmsg = getString(R.string.lessthanLastestRecord); }
		 */
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				// showDatePickerDialog(R.string.setstartdate,
				// oldcalCalendar,null,
				// PeriodLogPagerView.EDIT_START_DATE).show();
				dialog.dismiss();
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				
			}
		});
		else
		{
			
			Calendar calendar = new GregorianCalendar();
			Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
			if (date.getTime() <= startDate.getTime())
			{
				calendar.setTime(new Date());
			}
			else
			{
				calendar.setTime(date);
			}
			
			showDatePickerDialog(R.string.setenddate, calendar, null, EDIT_END_DATE).show();
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
			
		}
		
	}
	
	public void updateEndDate()
	{
		boolean valid = true;
		String invalidmsg = "";
		if (!Utility.isDateLessThanCurrent(endDate))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthancallmessage);
		}
		else if (!Utility.isGreaterThan(startDate, endDate))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthanLastestRecord);
			
		}
		
		else if (periodLogDBHandler.checkFieldExistance(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, oldcalCalendar.getTimeInMillis(), endDate.getTime())
		/*|| periodLogDBHandler.checkDateLiesBetween(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE,
				oldcalCalendar.getTimeInMillis(), endDate.getTime()*/)
		{
			valid = false;
			invalidmsg = getString(R.string.invalidateRecordBetweenDates);
		}
		/*else if (periodLogDBHandler.compareWithLatest(DBManager.TABLE_PERIOD_TRACKER, DBProjections.PT_START_DATE, endDate.getTime()))
		{
			valid = false;
			invalidmsg = getString(R.string.lessthanLastestRecord);
		}*/
		
		if (!valid) CustomAlertDialog.Dialog(this).getAlertDialog(getString(R.string.invaliddatetitle), invalidmsg, true, getString(R.string.ok), true, getString(R.string.cancel), true, new onButoinClick()
		{
			@Override
			public void onClickPositive(DialogInterface dialog, int which)
			{
				showDatePickerDialog(R.string.setenddate, oldcalCalendar, null, EDIT_END_DATE).show();
			}
			
			@Override
			public void onClickNegative(DialogInterface dialog, int which)
			{
				
			}
		});
		else
		{
			if (logModel != null)
			{
				logModel.setStartDate(startDate);
				logModel.setEndDate(endDate);
				if (periodLogDBHandler.updatePeriodLogWhenEditRecord(logModel))
				{
					refresh();
					APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();
					//sazid
					
					//messagsToFrag.get(PERIOD_LOG_FRAGMENT_ID).sendMessage(LogFragment.REFRESH_PAST_LIST, null);
				}
				else
				{
					Toast.makeText(this, "Not able to update", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				PeriodLogModel logModel = new PeriodLogModel();
				
			}
		}
		
	}
}
