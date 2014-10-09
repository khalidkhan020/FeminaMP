package com.linchpin.periodtracker.controller;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class PeriodLogTabListener implements OnTabChangeListener {

	TabHost mTabHost;
	ViewPager mViewPager;
View view;
	public PeriodLogTabListener(TabHost mTabHost, ViewPager mViewPager,View v) {
		this.mTabHost = mTabHost;
		this.mViewPager = mViewPager;
		this.view=v;
	}
	public void setTabPosition(int position) {
		mTabHost.setCurrentTab(position);
		mViewPager.setCurrentItem(position);
	}
	
	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		int position = mTabHost.getCurrentTab();
		mTabHost.setCurrentTab(position);
		mViewPager.setCurrentItem(position);
		if(view!=null)
		if(this.view instanceof  ImageView&&position>0)
		view.setVisibility(View.GONE);
		else
			view.setVisibility(View.VISIBLE);
		// mTabHost.getTabWidget().getChildTabViewAt(mTabHost.getCurrentTab()).refreshDrawableState();
		/*
		 * for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
		 * System.out.println(mTabHost.getChildAt(i)); TextView textView =
		 * (TextView)
		 * mTabHost.getTabWidget().getChildTabViewAt(i).findViewById(R
		 * .id.tabsText); if (textView != null)
		 * textView.setTextColor(Color.WHITE);
		 * 
		 * }
		 * 
		 * 
		 * TextView textView1 = (TextView)
		 * mTabHost.getTabWidget().getChildAt(mTabHost
		 * .getCurrentTab()).findViewById(R.id.tabsText); if (textView1 != null)
		 * textView1.setTextColor(Color.parseColor("#f3dc8c"));
		 */
	}
}
