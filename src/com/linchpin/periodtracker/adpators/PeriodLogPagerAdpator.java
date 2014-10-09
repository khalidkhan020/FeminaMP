package com.linchpin.periodtracker.adpators;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.linchpin.periodtracker.fragments.AddNoteFragments;
import com.linchpin.periodtracker.fragments.LogFragment;
import com.linchpin.periodtracker.fragments.MoodBaseFragment;
import com.linchpin.periodtracker.fragments.PillsFragmnet;
import com.linchpin.periodtracker.fragments.SymtompsBaseFragment;
import com.linchpin.periodtracker.fragments.WeightAndTemperatureFragment;
import com.linchpin.periodtracker.view.TabInfo;

public class PeriodLogPagerAdpator extends FragmentPagerAdapter {
	ArrayList<TabInfo> mTabs;
	Context mContext;

	public PeriodLogPagerAdpator(FragmentManager supportFragmentManager, List<TabInfo> mTabs2, Context mcontext) {
		// TODO Auto-generated constructor stub
		super(supportFragmentManager);
		this.mTabs = (ArrayList<TabInfo>) mTabs2;
		this.mContext = mcontext;
	}

	@Override
	public Fragment getItem(int i) {
		TabInfo info = mTabs.get(i);
				Fragment fragment = Fragment.instantiate(mContext, info.clss.getName(), null);

		/*if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.PastPeriodRecordListFragment")) {
			PeriodLogFragment.pastPeriodRecordListFragment = (PastPeriodRecordListFragment) fragment;
		}

		if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.PerdictionPeriodListFragment")) {
			PeriodLogFragment.perdictionPeriodListFragment = (PerdictionPeriodListFragment) fragment;
		}*/

		/*if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.PerdictionOvulationRecordsFragments")) {
			LogFragment.perdictionOvulationRecordsFragments = (PerdictionOvulationRecordsFragments) fragment;
		}

		if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.PastOvulationRecordsFragments")) {
			LogFragment.pastovulationRecordsFragments = (PastOvulationRecordsFragments) fragment;
		}*/

		/*if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.PerdictionFertileRecordsFragments")) {
			PeriodLogFragment.perdictionFertileRecordsFragments = (PerdictionFertileRecordsFragments) fragment;
		}

		if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.PastFertileRecordsFragments")) {
			PeriodLogFragment.pastfertileRecordsFragments = (PastFertileRecordsFragments) fragment;
		}*/

		if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.AddNoteFragments")) {
			AddNoteFragments.addnoteFragment = (AddNoteFragments) fragment;
		}

		if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.MoodBaseFragment")) {
			MoodBaseFragment.moodBaseFragment = (MoodBaseFragment) fragment;
		}

		if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.SymtompsBaseFragment")) {
			SymtompsBaseFragment.symtompsBaseFragment = (SymtompsBaseFragment) fragment;
		}

		if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.WeightAndTemperatureFragment")) {
			WeightAndTemperatureFragment.weightFragment = (WeightAndTemperatureFragment) fragment;
		}

		if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.PeriodLogFragment")) {
			LogFragment.periodLogFragment = (LogFragment) fragment;
		}

		/*if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.OvulationDaysFragments")) {
			OvulationDaysFragments.ovulationDaysFragments = (OvulationDaysFragments) fragment;
		}

		if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.FertileDaysFragments")) {
			FertileDaysFragments.fertileDaysFragments = (FertileDaysFragments) fragment;
		}*/
		/*if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.FertileDaysFragments")) {
			FertileDaysFragments.fertileDaysFragments = (FertileDaysFragments) fragment;
		}*/
		if (info.clss.getName().equals("com.linchpin.periodtracker.fragments.PillsFragmnet")) {
			PillsFragmnet.pillsFragment = (PillsFragmnet) fragment;
		}
		return fragment;

	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "Section " + (position + 1);
	}

}
