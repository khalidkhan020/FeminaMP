package com.linchpin.periodtracker.utlity;

import android.support.v4.app.Fragment;

public class FragList
{
	private String	titles;
	
	public String getTitles()
	{
		return titles;
	}
	
	public void setTitles(String titles)
	{
		this.titles = titles;
	}
	
	public int getIcon()
	{
		return icon;
	}
	
	public void setIcon(int icon)
	{
		this.icon = icon;
	}
	
	public Fragment getFragments()
	{
		return fragments;
	}
	
	public void setFragments(Fragment fragments)
	{
		this.fragments = fragments;
	}
	
	private int			icon;
	private Fragment	fragments;
	
	public FragList(Fragment fragment, String title, int icon)
	{
		this.fragments = fragment;
		this.icon = icon;
		this.titles = title;
		
	}
	
}
