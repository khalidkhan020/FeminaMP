package com.linchpin.periodtracker.utlity;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

public class SwipFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter
{
	ArrayList<FragList>	fragList;
	
	private int			mCount;
	
	public SwipFragmentAdapter(ArrayList<FragList> fragList, FragmentManager fm)
	{
		
		super(fm);
		this.fragList = fragList;
		mCount = fragList.size();
	}
	
	@Override
	public Fragment getItem(int position)
	{
		
		return fragList.get(position).getFragments();
	}
	
	@Override
	public int getCount()
	{
		return mCount;
	}
	
	@Override
	public CharSequence getPageTitle(int position)
	{
		return fragList.get(position).getTitles();//CONTENT[position % CONTENT.length];
	}
	
	@Override
	public int getIconResId(int index)
	{
		return fragList.get(index).getIcon();//ICONS[index % ICONS.length];
	}
	
	public void setCount(int count)
	{
		
		mCount = count;
		notifyDataSetChanged();
		
	}
}