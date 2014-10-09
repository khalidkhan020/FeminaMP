package com.linchpin.periodtracker.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.PeriodLogPagerAdpator;
import com.linchpin.periodtracker.controller.CustomViewPager;
import com.linchpin.periodtracker.controller.PeriodLogTabListener;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.view.DatePickerDialog;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodtracker.view.PeriodLogPagerView;
import com.linchpin.periodtracker.view.TabFactory;
import com.linchpin.periodtracker.view.TabInfo;

public class FertileDaysFragments extends Fragment {
	public View mView;
	public TabHost mTabHost;
	CustomViewPager mViewPager;

	PeriodLogModel periodLogModel;

	public Button bAddPastRecord;
	TextView tStartDate, tEndDate;
	public LinearLayout bStartDate, bEndDate;

	List<TabInfo> mTabs = new ArrayList<TabInfo>();
	PeriodLogPagerAdpator mPeriodLogPagerAdpator;

	SimpleDateFormat dateFormat;
	String sDatefFormat = PeriodTrackerObjectLocator.getInstance().getDateFormat();

	boolean addPastFlag = false;
	public TextView tAverageCycleLength, tAveragePeriodLength;

	public static FertileDaysFragments fertileDaysFragments;

	private int day;
	private int month;
	private int year;

	DatePickerDialog startDatePickerDialog, endDatePickerDailog;

	List<PeriodTrackerModelInterface> pastPeriodTrackerModelInterfaces, perdictionPeriodTrackerModelInterfaces,
			pastFertileAndOvultutionList, perdictionFertileAndOvultutionList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

		mView = inflater.inflate(R.layout.fertile_details, container, false);
		/*
		 * AdView adView = (AdView) mView.findViewById(R.id.adView); if
		 * (Utility.addDays( new
		 * Date(getActivity().getSharedPreferences("com.linchpin.periodtracker"
		 * , 0).getLong("InstallDate", PeriodTrackerConstants.NULL_DATE)),
		 * 10).after(PeriodTrackerConstants.CURRENT_DATE) ||
		 * getActivity().getSharedPreferences("com.linchpin.periodtracker",
		 * 0).getBoolean("purchased", false)) { adView.setVisibility(View.GONE);
		 * }
		 */
		mView.findViewById(R.id.periodloginfobutton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), HomeScreenHelp.class);
				intent.putExtra("classname", "fertilelog");
				startActivity(intent);

			}
		});
		mTabHost = (TabHost) mView.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mViewPager = (CustomViewPager) mView.findViewById(R.id.listviewpager);
		mViewPager.setSwipeable(false);
		PeriodLogTabListener listener = new PeriodLogTabListener(mTabHost, mViewPager);
		tAverageCycleLength = (TextView) mView.findViewById(R.id.periodlogtextforcyclelength);
		tAveragePeriodLength = (TextView) mView.findViewById(R.id.periodlogtextforperiodlength);

		RefreshLengthItems();

		initStartAndEndDateButton(mView);
		mTabHost.setOnTabChangedListener((OnTabChangeListener) listener);

		/*
		 * ViewPageListener PageListener = new ViewPageListener(mTabHost);
		 * mViewPager.setOnPageChangeListener(PageListener);
		 */
		/******************* Adding tabs in TabHost *************/
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.past)).setIndicator(
				createTabView(getActivity(), getResources().getString(R.string.past))),
				PastFertileRecordsFragments.class, null);
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.prediction)).setIndicator(
				createPredictionTabView(getActivity(), getResources().getString(R.string.prediction))),
				PerdictionFertileRecordsFragments.class, null);

		/***************** Setting Adpator **************/
		// Create the adapter that will return a fragment for each of the
		// primary sections
		// of the app.
		mPeriodLogPagerAdpator = new PeriodLogPagerAdpator(getChildFragmentManager(), mTabs, getActivity());
		mViewPager.setAdapter(mPeriodLogPagerAdpator);
		// mViewPager.setCurrentItem(1);
		mViewPager.setCurrentItem(0);

		return mView;
	}

	public void RefreshLengthItems() {
		
		tAverageCycleLength.setText(+PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength() + " "
				+ getString(R.string.days));
		tAveragePeriodLength.setText(+PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() + " "
				+ getString(R.string.days));
	}

	private static View createTabView(final Context context, final String text) {
		final View view = LayoutInflater.from(context).inflate(R.layout.past_tab_bg, null);
		final TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setGravity(Gravity.CENTER);
		tv.setText(text);
		return view;
	}

	private static View createPredictionTabView(final Context context, final String text) {
		final View view = LayoutInflater.from(context).inflate(R.layout.perdiciton_tab_bg, null);
		final TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setGravity(Gravity.CENTER);
		tv.setText(text);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mTabHost.getCurrentTab() == 0)
			mViewPager.setCurrentItem(0);
		else
			mViewPager.setCurrentItem(1);

	}

	public void initStartAndEndDateButton(View mView) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DATE);

		bEndDate = (LinearLayout) mView.findViewById(R.id.enddate);
		bStartDate = (LinearLayout) mView.findViewById(R.id.startdate);
		tEndDate = (TextView) mView.findViewById(R.id.enddatetext);
		tStartDate = (TextView) mView.findViewById(R.id.startdatetext);
		if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode()) {
			bEndDate.setEnabled(false);
			tEndDate.setTextColor(Color.parseColor(getResources().getString(R.color.disabledatecolor)));
			bStartDate.setEnabled(true);
			dateFormat = new SimpleDateFormat(sDatefFormat);
			tStartDate.setText(R.string.period_start_date);
			pastPeriodTrackerModelInterfaces = ((PeriodLogPagerView) getActivity()).periodTrackerModelInterfaces;
			if (pastPeriodTrackerModelInterfaces.size() != 0) {
				periodLogModel = (PeriodLogModel) pastPeriodTrackerModelInterfaces.get(0);
				if (periodLogModel.getEndDate().getTime() == (PeriodTrackerConstants.NULL_DATE)) {

					tStartDate.setText("" + dateFormat.format(periodLogModel.getStartDate()));
					bEndDate.setEnabled(true);
					tEndDate.setTextColor(Color.parseColor(getResources().getString(R.color.newdatecolor)));
					tEndDate.setText(getResources().getString(R.string.period_end_date));

				}
			}

			/*kkif (PeriodLogFragment.periodLogFragment != null) {
				startDatePickerDialog = new DatePickerDialog(getActivity(),
						PeriodLogFragment.periodLogFragment.startdatePickerListener, year, month, day);
				if (Utility.checkAndroidApiVersion()) {
					startDatePickerDialog.getDatePicker().setMaxDate(
							new Date().getTime() + 3640 * PeriodTrackerConstants.MILLI_SECONDS);
					startDatePickerDialog.getDatePicker().setMinDate(
							new Date().getTime() - 3640 * PeriodTrackerConstants.MILLI_SECONDS);
				}
			}*/
			bStartDate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(new Date());
					if (!PeriodTrackerConstants.PREGNANCY_MODE_ON) {
						pastPeriodTrackerModelInterfaces = ((PeriodLogPagerView) getActivity()).periodTrackerModelInterfaces;
						// Calendar

					/*kk	startDatePickerDialog = new DatePickerDialog(getActivity(),
								PeriodLogFragment.periodLogFragment.startdatePickerListener, calendar
										.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
				*/		if (Utility.checkAndroidApiVersion()) {
							startDatePickerDialog.getDatePicker().setMinDate(
									new Date().getTime() - 3640 * PeriodTrackerConstants.MILLI_SECONDS);
							startDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
						}
						startDatePickerDialog.setTitle(getResources().getString(R.string.setstartdate));
						startDatePickerDialog.show();

					} else {
						Toast.makeText(getActivity(), getResources().getString(R.string.pregnancy), Toast.LENGTH_LONG)
								.show();
					}
				}
			});

			bEndDate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (!PeriodTrackerConstants.PREGNANCY_MODE_ON) {
						Date startdate = periodLogModel.getStartDate();

						startdate = Utility.addDays(startdate, (PeriodTrackerObjectLocator.getInstance()
								.getCurrentPeriodLength()-1));
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(startdate);
						/*kkk endDatePickerDailog = new DatePickerDialog(getActivity(),
								PeriodLogFragment.periodLogFragment.endDatePickerListener, calendar.get(Calendar.YEAR),
								calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
					*/	endDatePickerDailog.setTitle(getResources().getString(R.string.setenddate));

						endDatePickerDailog.show();
					} else {
						Toast.makeText(getActivity(), getResources().getString(R.string.pregnancy), Toast.LENGTH_LONG)
								.show();
					}
				}
			});
		} else {
			bEndDate.setEnabled(false);
			tEndDate.setTextColor(Color.parseColor(getResources().getString(R.color.disabledatecolor)));
			bStartDate.setEnabled(false);
			tStartDate.setTextColor(Color.parseColor(getResources().getString(R.color.disabledatecolor)));
		}
	}

	public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
		tabSpec.setContent(new TabFactory(getActivity()));
		String tag = tabSpec.getTag();
		TabInfo info = new TabInfo(tag, clss, args);
		mTabs.add(info);
		mTabHost.addTab(tabSpec);

	}

}
