package com.linchpin.periodtracker.controller;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.fragments.AddNoteFragments;
import com.linchpin.periodtracker.fragments.LogFragment;
import com.linchpin.periodtracker.fragments.MoodBaseFragment;
import com.linchpin.periodtracker.fragments.PillsFragmnet;
import com.linchpin.periodtracker.fragments.SymtompsBaseFragment;
import com.linchpin.periodtracker.fragments.WeightAndTemperatureFragment;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.view.AddNoteView;
import com.linchpin.periodtracker.view.PeriodLogPagerView;

public class ViewPageListener implements OnPageChangeListener {

	public static Fragment activeFragment;
	// public static Fragment currentActiveFragment;
	public static Fragment forwordPerviousFragment;
	public static Fragment backwordPreviousFragment;

	TabHost mTabHost;
	String mClassName;
	Context mcontext;
	int previousPosition = 0;
	public int getPreviousPosition()
	{
		return previousPosition;
	}
	public void setPreviousPosition(int previousPosition)
	{
		this.previousPosition = previousPosition;
	}
	HorizontalScrollView horizontalScrollView;

	public ViewPageListener(TabHost mTabHost, String className, Context context,
			HorizontalScrollView horizontalScrollView) {
		this.mTabHost = mTabHost;
		this.mClassName = className;
		this.mcontext = context;
		this.horizontalScrollView = horizontalScrollView;
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		System.out.println("pagescrolled");
		if (mClassName.equals(PeriodTrackerConstants.ADD_NOTE_VIEW)) {
			int i = ((AddNoteView) mcontext).mViewpager.getCurrentItem();
			previousPosition = i;
			((AddNoteView) mcontext).mViewpager.invalidate();

		}

		if (mClassName.equals(PeriodTrackerConstants.PERIOD_LOG_PAGER_VIEW)) {

			int i = ((PeriodLogPagerView) mcontext).mViewpager.getCurrentItem();
			previousPosition = i;
		}
		View tabView = mTabHost.getTabWidget().getChildAt(arg0);
		if (tabView != null) {
			final int width = horizontalScrollView.getWidth();
			final int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2;
			horizontalScrollView.scrollTo(scrollPos, 0);
		} else {
			horizontalScrollView.scrollBy(arg2, 0);
		}
	}

	@Override
	public void onPageSelected(int position) {
		TabWidget widget = mTabHost.getTabWidget();
		int oldFocusability = widget.getDescendantFocusability();
		widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		mTabHost.setCurrentTab(position);
		widget.setDescendantFocusability(oldFocusability);

		if (mClassName.equals(PeriodTrackerConstants.ADD_NOTE_VIEW)) {
			switch (previousPosition) {
			case 0:
				AddNoteFragments addNoteFragments = null;

				if (position < previousPosition) {
					if (null == backwordPreviousFragment) {
						forwordPerviousFragment = AddNoteFragments.addnoteFragment;
					}
					if (!backwordPreviousFragment.getClass().getName()
							.contains(PeriodTrackerConstants.ADD_NOTE_FRAGMENT))
						backwordPreviousFragment = AddNoteFragments.addnoteFragment;

					addNoteFragments = (AddNoteFragments) backwordPreviousFragment;

					backwordPreviousFragment = null;
					backwordPreviousFragment = AddNoteFragments.addnoteFragment;
					previousPosition = position;
				} else {
					if (!forwordPerviousFragment.getClass().getName().contains(PeriodTrackerConstants.ADD_NOTE_FRAGMENT))
						forwordPerviousFragment = AddNoteFragments.addnoteFragment;
					addNoteFragments = (AddNoteFragments) forwordPerviousFragment;
					backwordPreviousFragment = forwordPerviousFragment;
					forwordPerviousFragment = null;
					forwordPerviousFragment = activeFragment;
				}
				addNoteFragments.validateAndSaveNote();

				break;

			case 1:
				SymtompsBaseFragment symtompsBaseFragment = null;
				if (position < previousPosition) {
					if (null == backwordPreviousFragment) {
						forwordPerviousFragment = SymtompsBaseFragment.symtompsBaseFragment;
						backwordPreviousFragment = SymtompsBaseFragment.symtompsBaseFragment;
					}
					if (!backwordPreviousFragment.getClass().getName()
							.contains(PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT))
						backwordPreviousFragment = SymtompsBaseFragment.symtompsBaseFragment;

					symtompsBaseFragment = (SymtompsBaseFragment) backwordPreviousFragment;
					backwordPreviousFragment = null;
					backwordPreviousFragment = MoodBaseFragment.moodBaseFragment;
					previousPosition = position;
				} else {

					if (!forwordPerviousFragment.getClass().getName().contains(PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT))
						forwordPerviousFragment = SymtompsBaseFragment.symtompsBaseFragment;
					symtompsBaseFragment = (SymtompsBaseFragment) forwordPerviousFragment;
					backwordPreviousFragment = forwordPerviousFragment;
					forwordPerviousFragment = null;
					forwordPerviousFragment = activeFragment;
				}

				symtompsBaseFragment.saveAndUpdate();

				break;

			case 2:
				MoodBaseFragment moodBaseFragment = null;
				if (position < previousPosition) {
					if (null == backwordPreviousFragment) {
						forwordPerviousFragment = MoodBaseFragment.moodBaseFragment;
						backwordPreviousFragment = forwordPerviousFragment;
					}
					if (!backwordPreviousFragment.getClass().getName()
							.contains(PeriodTrackerConstants.MOOD_BASE_FRAGMENT))
						backwordPreviousFragment = MoodBaseFragment.moodBaseFragment;
					moodBaseFragment = (MoodBaseFragment) backwordPreviousFragment;
					backwordPreviousFragment = null;
					backwordPreviousFragment = AddNoteFragments.addnoteFragment;
					previousPosition = position;
				} else {

					///if (!forwordPerviousFragment.getClass().getName().contains(PeriodTrackerConstants.MOOD_BASE_FRAGMENT))
						forwordPerviousFragment = MoodBaseFragment.moodBaseFragment;
					moodBaseFragment = (MoodBaseFragment) forwordPerviousFragment;
					backwordPreviousFragment = forwordPerviousFragment;
					forwordPerviousFragment = null;
					forwordPerviousFragment = activeFragment;
				}

				moodBaseFragment.saveAndUpdate();

				break;

			case 3:
				WeightAndTemperatureFragment weightAndTemperatureFragment = null;
				if (position < previousPosition) {
					if (null == backwordPreviousFragment) {
						forwordPerviousFragment = weightAndTemperatureFragment.weightFragment;
						backwordPreviousFragment = forwordPerviousFragment;
					}
					if (!backwordPreviousFragment.getClass().getName()
							.contains(PeriodTrackerConstants.WEIGHT_TEMPERATURE_FRAGEMENT))
						backwordPreviousFragment = WeightAndTemperatureFragment.weightFragment;
					weightAndTemperatureFragment = (WeightAndTemperatureFragment) backwordPreviousFragment;
					backwordPreviousFragment = null;
					backwordPreviousFragment = SymtompsBaseFragment.symtompsBaseFragment;
					previousPosition = position;
				} else {

					if (!forwordPerviousFragment.getClass().getName().contains(PeriodTrackerConstants.WEIGHT_TEMPERATURE_FRAGEMENT))
						forwordPerviousFragment = WeightAndTemperatureFragment.weightFragment;
					weightAndTemperatureFragment = (WeightAndTemperatureFragment) forwordPerviousFragment;
					backwordPreviousFragment = forwordPerviousFragment;
					forwordPerviousFragment = null;
					forwordPerviousFragment = activeFragment;
				}

				if (weightAndTemperatureFragment.validateTempWeightAndHieght())
					weightAndTemperatureFragment.saveAndUpdate();
				else
					position = previousPosition;
				break;
			case 4:
				PillsFragmnet pillsFragmnet = null;
				if (position < previousPosition) {
					if (null == backwordPreviousFragment) {
						forwordPerviousFragment = PillsFragmnet.pillsFragment;
						backwordPreviousFragment = forwordPerviousFragment;
					}
					if (!backwordPreviousFragment.getClass().getName().contains(PeriodTrackerConstants.PILLS_FRAGEMNT))
						backwordPreviousFragment = pillsFragmnet.pillsFragment;
					pillsFragmnet = (PillsFragmnet) backwordPreviousFragment;
					backwordPreviousFragment = null;
					backwordPreviousFragment = WeightAndTemperatureFragment.weightFragment;
					previousPosition = position;
				} else {

				if (!forwordPerviousFragment.getClass().getName().contains(PeriodTrackerConstants.PILLS_FRAGEMNT))
						forwordPerviousFragment = PillsFragmnet.pillsFragment;
					pillsFragmnet = (PillsFragmnet) forwordPerviousFragment;
					forwordPerviousFragment = null;
					forwordPerviousFragment = activeFragment;

				}

			default:
				break;
			}

			previousPosition = position;
		}
		if (mClassName.equals(PeriodTrackerConstants.PERIOD_LOG_PAGER_VIEW)) {
			PeriodLogPagerView logPagerView = new PeriodLogPagerView();

			switch (position) {
			case 0:
				if (null != LogFragment.periodLogFragment) {
					((PeriodLogPagerView) LogFragment.periodLogFragment.getActivity()).bAddPast
							.setVisibility(View.VISIBLE);
					((PeriodLogPagerView) LogFragment.periodLogFragment.getActivity()).textView.setText(mcontext
							.getString(R.string.period_logs));
				}
				break;
			/*case 1:
				if (null != FertileDaysFragments.fertileDaysFragments) {
					((PeriodLogPagerView) FertileDaysFragments.fertileDaysFragments.getActivity()).bAddPast
							.setVisibility(View.INVISIBLE);
					((PeriodLogPagerView) FertileDaysFragments.fertileDaysFragments.getActivity()).textView
							.setText(mcontext.getString(R.string.fertile));
				}
				break;
			case 2:
				if (null != FertileDaysFragments.fertileDaysFragments) {
					((PeriodLogPagerView) FertileDaysFragments.fertileDaysFragments.getActivity()).bAddPast
							.setVisibility(View.INVISIBLE);
					((PeriodLogPagerView) FertileDaysFragments.fertileDaysFragments.getActivity()).textView
							.setText(mcontext.getString(R.string.ovulation));
				}
				break;*/
			default:
				if (null != logPagerView.bAddPast)
					logPagerView.bAddPast.setVisibility(View.INVISIBLE);
				break;
			}

		}
		previousPosition = position;
	}
}
