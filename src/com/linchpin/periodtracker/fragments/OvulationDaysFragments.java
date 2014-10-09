package com.linchpin.periodtracker.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.PeriodLogPagerAdpator;
import com.linchpin.periodtracker.controller.CustomViewPager;
import com.linchpin.periodtracker.controller.PeriodLogTabListener;
import com.linchpin.periodtracker.controller.ViewPageListener;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodtracker.view.PeriodLogPagerView;
import com.linchpin.periodtracker.view.TabFactory;
import com.linchpin.periodtracker.view.TabInfo;

public class OvulationDaysFragments extends Fragment {

	public View mView;
	public TabHost mTabHost;
	CustomViewPager mViewPager;
	List<TabInfo> mTabs = new ArrayList<TabInfo>();
	PeriodLogPagerAdpator mPeriodLogPagerAdpator;
	public ImageButton bAddPastRecord;
	public TextView textView;
	public static OvulationDaysFragments ovulationDaysFragments;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		mView = inflater.inflate(R.layout.ovulation_details, container, false);
		mView.findViewById(R.id.ovulationinfobutton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), HomeScreenHelp.class);
				intent.putExtra("classname", "ovulationlog");
				startActivity(intent);

			}
		});
		
		mTabHost = (TabHost) mView.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mViewPager = (CustomViewPager) mView.findViewById(R.id.listviewpager);
		mViewPager.setSwipeable(false);
		bAddPastRecord = ((PeriodLogPagerView) getActivity()).bAddPast;
		textView = (TextView) mView.findViewById(R.id.averageovulationlogs);
		
		RefreshItems();
		PeriodLogTabListener listener = new PeriodLogTabListener(mTabHost, mViewPager);
		mTabHost.setOnTabChangedListener((OnTabChangeListener) listener);

		if (FertileDaysFragments.fertileDaysFragments != null)
			FertileDaysFragments.fertileDaysFragments.initStartAndEndDateButton(mView);

		int i = ((PeriodLogPagerView) getActivity()).mViewpager.getCurrentItem();
		if (i == 2) {
			ViewPageListener.activeFragment = ovulationDaysFragments;
		}

		/******************* Adding tabs in TabHost *************/
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.past)).setIndicator(
				createTabView(getActivity(), getResources().getString(R.string.past))),
				PastOvulationRecordsFragments.class, null);
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.prediction)).setIndicator(
				createPredictionTabView(getActivity(), getResources().getString(R.string.prediction))),
				PerdictionOvulationRecordsFragments.class, null);

		/***************** Setting Adpator **************/
		// Create the adapter that will return a fragment for each of the
		// primary sections
		// of the app.
		mPeriodLogPagerAdpator = new PeriodLogPagerAdpator(getChildFragmentManager(), mTabs, getActivity());
		mViewPager.setAdapter(mPeriodLogPagerAdpator);
		/* mViewPager.setCurrentItem(1); */
		mViewPager.setCurrentItem(0);

		return mView;
	}

	
	
	public void RefreshItems(){
		
		textView.setText("" + PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength());
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mTabHost.getCurrentTab() == 0)
			mViewPager.setCurrentItem(0);
		else
			mViewPager.setCurrentItem(1);

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

	public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
		tabSpec.setContent(new TabFactory(getActivity()));
		String tag = tabSpec.getTag();
		TabInfo info = new TabInfo(tag, clss, args);
		mTabs.add(info);
		mTabHost.addTab(tabSpec);

	}

}
