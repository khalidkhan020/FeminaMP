package com.linchpin.periodtracker.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.PeriodLogPagerAdpator;
import com.linchpin.periodtracker.controller.ClickListener;
import com.linchpin.periodtracker.controller.PeriodLogTabListener;
import com.linchpin.periodtracker.controller.ViewPageListener;
import com.linchpin.periodtracker.fragments.AddNoteFragments;
import com.linchpin.periodtracker.fragments.MoodBaseFragment;
import com.linchpin.periodtracker.fragments.PillsFragmnet;
import com.linchpin.periodtracker.fragments.SymtompsBaseFragment;
import com.linchpin.periodtracker.fragments.WeightAndTemperatureFragment;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.widget.BackBar;
import com.linchpin.periodttracker.database.AddNoteDBHandler;

public class AddNoteView extends FragmentActivity
{
	
	public ImageButton							bnext, bprev;
	public BackBar bBack;
	PeriodLogPagerAdpator						mPeriodLogPagerAdapter;
	public TabHost								mTabHost;
	List<TabInfo>								mTabs	= new ArrayList<TabInfo>();
	PeriodTrackerModelInterface					modelInterfaces;
	public DayDetailModel						dayDetailModel;
	public ViewPager							mViewpager;
	AddNoteDBHandler							addNoteDBHandler;
	TextView									textView;
	Intent										intent;
	Date										noteDate;
	public HorizontalScrollView					horizontalScrollView;
	public List<PeriodTrackerModelInterface>	periodTrackerModelInterfacesSymtomSelected;
	public List<PeriodTrackerModelInterface>	periodTrackerModelInterfacesMoodSelected;
	public List<PeriodTrackerModelInterface>	periodTrackerModelInterfacesPills;
	public List<PeriodTrackerModelInterface>	periodTrackerModelInterfacesGetAllMoods;
	public List<PeriodTrackerModelInterface>	periodTrackerModelInterfaceGetAllSymptoms;
	SimpleDateFormat							sfd;
	ViewPageListener							PageListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
	//	Tips.viewVisible(this, APP.TipsPath.Pill.id);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnote_pager_tab_activity);
		// getActionBar().hide();
		applyTheme();
		ClickListener click = new ClickListener(AddNoteView.this);
		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		bBack = (BackBar) findViewById(R.id.addbackbutton);
		textView = (TextView) findViewById(R.id.addheadertext);
		bBack.setOnClickListener(click);
		mViewpager = (ViewPager) findViewById(R.id.addnoteviewpager);
		bnext = (ImageButton) findViewById(R.id.next);
		bprev = (ImageButton) findViewById(R.id.pre);
		
		intent = getIntent();
		try
		{
			noteDate = Utility.setHourMinuteSecondZero(new Date((long) intent.getExtras().getLong("date")));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mTabHost.setup();
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.addnotehorizontalscroll);
		
		nullifyFragments();
		// Intializing Listener.
		PeriodLogTabListener listener = new PeriodLogTabListener(mTabHost, mViewpager, null);
		
		mTabHost.setOnTabChangedListener((OnTabChangeListener) listener);
		PageListener = new ViewPageListener(mTabHost, PeriodTrackerConstants.ADD_NOTE_VIEW, this, horizontalScrollView);
		mViewpager.setOnPageChangeListener(PageListener);
		mViewpager.setOffscreenPageLimit(2);
		
		/*************** Adding Tabs in tabHost ***************/
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.notes)).setIndicator(createTabView(this, getResources().getString(R.string.notes))), AddNoteFragments.class, null);
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.symptoms)).setIndicator(createTabView(this, getResources().getString(R.string.symptoms))), SymtompsBaseFragment.class, null);
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.moods)).setIndicator(createTabView(this, getResources().getString(R.string.moods))), MoodBaseFragment.class, null);
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.weight)).setIndicator(createTabView(this, getResources().getString(R.string.weight))), WeightAndTemperatureFragment.class, null);
		addTab(mTabHost.newTabSpec(getResources().getString(R.string.pills)).setIndicator(createTabView(this, getResources().getString(R.string.pills))), PillsFragmnet.class, null);
		/**/
		/***************** Setting Adpator **************/
		// Create the adapter that will return a fragment for each of the
		// primary sectionss
		// of the app.
		mPeriodLogPagerAdapter = new PeriodLogPagerAdpator(getSupportFragmentManager(), mTabs, this);
		mViewpager.setAdapter(mPeriodLogPagerAdapter);
		
		intializeFragments(noteDate);
		
		bBack.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				saveDataOfCurretFragment();
				finish();
				APP.GLOBAL().exicuteRIOAnim(AddNoteView.this);
			}
		});
		
		bnext.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				saveDataOfCurretFragment();
				intializeFragments(Utility.addDays(dayDetailModel.getDate(), 1));
				
			}
		});
		bprev.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Avuto-generated method stub
				saveDataOfCurretFragment();
				intializeFragments(Utility.subtractDays(dayDetailModel.getDate(), 1));
			}
		});
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume()
	{
		
		super.onResume();
		if (intent != null)
		{
			int position = intent.getExtras().getInt("positon");
			if (position != 0)
			{
				ViewPageListener.forwordPerviousFragment = mPeriodLogPagerAdapter.getItem(position + 1);
				ViewPageListener.backwordPreviousFragment = mPeriodLogPagerAdapter.getItem(0);
				ViewPageListener.activeFragment = mPeriodLogPagerAdapter.getItem(position);
				PageListener.setPreviousPosition(0);
				
				mTabHost.setCurrentTab(position);
				mViewpager.setCurrentItem(position);
				intent = null;
				//	PageListener.onPageScrolled(position, 0, 0);
			}
			
		}
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PURCHASED.key, false)) 
			PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		APP.GLOBAL().exicuteLIOAnim(AddNoteView.this);
		super.onBackPressed();
				
	}
	
	static Theme t;
	private void applyTheme()
	{
		
		t = Theme.getCurrentTheme(this);
		if (t != null)
		{	
			findViewById(R.id.nav).setBackgroundDrawable(t.getDrawableResource("mpt_cal_nav"));
			findViewById(android.R.id.tabs).setBackgroundDrawable(t.getDrawableResource("view_bg"));
			((TextView)findViewById(R.id.addheadertext)).setTextColor(t.getColor("heading_fg"));
			/*setTextSize(16);
			 * 
			setPadding(8, 0, 0, 0);
			setTextColor(t.getColor("heading_fg"));
			setBackgroundColor(t.getColor("heading_bg"));*/
			
			
		}}
	private static View createTabView(final Context context, final String text)
	{
		final View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		final TextView tv = (TextView) view.findViewById(R.id.tabsText);
		//tv.setGravity(Gravity.CENTER);
		if(t!=null){
			t.applyBackgroundColor(view, "heading_bg");
			
			t.applyBackgroundDrawable(tv, "mpt_tab_sltr");
		}
		tv.setText(text);
		return view;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
	 * android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			saveDataOfCurretFragment();
			finish();
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/************************** Method to adding tabs into tab host *************************/
	
	public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args)
	{
		tabSpec.setContent(new TabFactory(this));
		String tag = tabSpec.getTag();
		TabInfo info = new TabInfo(tag, clss, args);
		mTabs.add(info);
		
		mTabHost.addTab(tabSpec);
		
	}
	
	protected void nullifyFragments()
	{
		if (AddNoteFragments.addnoteFragment != null) AddNoteFragments.addnoteFragment = null;
		if (MoodBaseFragment.moodBaseFragment != null) MoodBaseFragment.moodBaseFragment = null;
		if (SymtompsBaseFragment.symtompsBaseFragment != null) SymtompsBaseFragment.symtompsBaseFragment = null;
		if (WeightAndTemperatureFragment.weightFragment != null) WeightAndTemperatureFragment.weightFragment = null;
		if (PillsFragmnet.pillsFragment != null) PillsFragmnet.pillsFragment = null;
	}
	
	public void intializeFragments(Date date)
	{
		
		date = Utility.setHourMinuteSecondZero(date);
		bnext.setEnabled(true);
		addNoteDBHandler = new AddNoteDBHandler(this);
		modelInterfaces = addNoteDBHandler.getDayDetail(date);
		dayDetailModel = (DayDetailModel) modelInterfaces;
		if (dayDetailModel == null || dayDetailModel.getDate() == null)
		{
			Float h, w, t;
			if (null != PeriodTrackerObjectLocator.getInstance().getHeightUnit() && PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(PeriodTrackerConstants.DEFAULT_HIEGHT_UNIT))
			{
				if (null != Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))
				{
					if (Float.parseFloat(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule())) != 0)
					{
						h = Float.parseFloat(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()));
					}
					else
					{
						h = PeriodTrackerConstants.MIN_HEIGHT_IN_CENTIMETER;
					}
				}
				else
				{
					h = PeriodTrackerConstants.MIN_HEIGHT_IN_CENTIMETER;
				}
			}
			else
			{
				if (null != Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))
				{
					
					if (Float.parseFloat(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule())) != 0)
					{
						h = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule())))));
					}
					else
					{
						h = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.MIN_HEIGHT_IN_INCHES)));
						
					}
				}
				else
				{
					h = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.MIN_HEIGHT_IN_INCHES)));
					
				}
			}
			
			if (null != PeriodTrackerObjectLocator.getInstance().getWeighUnit() && PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(PeriodTrackerConstants.DEFAULT_WEIGHT_UNIT))
			{
				
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
				{
					if (Float.parseFloat(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule())) != 0)
					{
						w = Float.parseFloat(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()));
					}
					else
					{
						w = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.MIN_WEIGHT_IN_LB)));
					}
				}
				else
				{
					w = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.MIN_WEIGHT_IN_LB)));
				}
			}
			else
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
				{
					
					if (Float.parseFloat(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule())) != 0)
					{
						w = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule())))));
					}
					else
					{
						w = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.MIN_WEIGHT_IN_KG)));
					}
				}
				else
				{
					w = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.MIN_WEIGHT_IN_KG)));
				}
			}
			
			if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit() && PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(PeriodTrackerConstants.DEFAULT_TEMPRATURE_UNIT))
			{
				
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() && Utility.isValidNumber(Float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
				{
					
					t = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule())));
				}
				else
				{
					t = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE)));
					
				}
				
			}
			else
			{
				
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() && Utility.isValidNumber(Float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
				{
					t = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToCelsius(PeriodTrackerConstants.MIN_TEMP_IN_FEHRENHIET))));
				}
				else
				{
					
					t = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToCelsius(Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE)))));
					
				}
			}
			
			dayDetailModel = new DayDetailModel(0, date, "", false, PeriodTrackerConstants.PROTECTION, 0f, Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE), 0f, PeriodTrackerObjectLocator.getInstance().getProfileId(),3);
			addNoteDBHandler.addRecordInDayDetail(dayDetailModel);
			modelInterfaces = addNoteDBHandler.getDayDetail(dayDetailModel.getDate());
			dayDetailModel = (DayDetailModel) modelInterfaces;
			
		}
		System.out.println(dayDetailModel.getDate().toString());
		sfd = new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		textView.setText(sfd.format(dayDetailModel.getDate()));
		periodTrackerModelInterfaceGetAllSymptoms = addNoteDBHandler.getAllSymtoms();
		periodTrackerModelInterfacesGetAllMoods = addNoteDBHandler.getAllMood();
		periodTrackerModelInterfacesSymtomSelected = addNoteDBHandler.getAllSelectedSymptom(dayDetailModel.getId());
		periodTrackerModelInterfacesMoodSelected = addNoteDBHandler.getAllSelectedMood(dayDetailModel.getId());
		periodTrackerModelInterfacesPills = addNoteDBHandler.getAllMedicineForDate(dayDetailModel.getId());
		// refreshFragments(mTabHost.getCurrentTab());
		
		if (null != AddNoteFragments.addnoteFragment)
		{
			AddNoteFragments.addnoteFragment.intializeAddNoteFragment();
			
			if (null != AddNoteFragments.addnoteFragment.mView)
			{
				// mTabHost.getCurrentTabView().equals(AddNoteFragments.addnoteFragment.mView);
				AddNoteFragments.addnoteFragment.mView.refreshDrawableState();
				AddNoteFragments.addnoteFragment.mView.invalidate();
			}
		}
		if (null != MoodBaseFragment.moodBaseFragment)
		{
			MoodBaseFragment.moodBaseFragment.initailizeMoodBaseFragment();
			/*
			 * if (null != MoodBaseFragment.moodBaseFragment.getView()) {
			 * MoodBaseFragment
			 * .moodBaseFragment.getView().refreshDrawableState();
			 * MoodBaseFragment.moodBaseFragment.getView().invalidate(); }
			 */
		}
		if (null != SymtompsBaseFragment.symtompsBaseFragment)
		{
			SymtompsBaseFragment.symtompsBaseFragment.intializeSymtomBaseFragment();
			/*
			 * if (null != SymtompsBaseFragment.symtompsBaseFragment.getView())
			 * { SymtompsBaseFragment
			 * .symtompsBaseFragment.getView().refreshDrawableState();
			 * SymtompsBaseFragment.symtompsBaseFragment.getView().invalidate();
			 * }
			 */
		}
		if (null != WeightAndTemperatureFragment.weightFragment)
		{
			WeightAndTemperatureFragment.weightFragment.initWeightAndTempFragment();
			/*
			 * if (null !=
			 * WeightAndTemperatureFragment.weightFragment.getView()) {
			 * WeightAndTemperatureFragment
			 * .weightFragment.getView().refreshDrawableState();
			 * WeightAndTemperatureFragment
			 * .weightFragment.getView().invalidate(); }
			 */
		}
		if (null != PillsFragmnet.pillsFragment)
		{
			PillsFragmnet.pillsFragment.initPillsFragment();
			/*
			 * if (null != PillsFragmnet.pillsFragment.getView()) {
			 * PillsFragmnet.pillsFragment.getView().refreshDrawableState();
			 * PillsFragmnet.pillsFragment.getView().invalidate(); }
			 */
		}
		
		mPeriodLogPagerAdapter.notifyDataSetChanged();
		mViewpager.invalidate();
		/*
		 * if (date.compareTo(PeriodTrackerConstants.CURRENT_DATE) == 0) {
		 * bnext.setEnabled(false); }
		 */
	}
	
	public void refreshFragments(int i)
	{
		switch (i)
		{
			case 0:
				if (null != AddNoteFragments.addnoteFragment) AddNoteFragments.addnoteFragment.intializeAddNoteFragment();
				break;
			case 1:
				if (null != MoodBaseFragment.moodBaseFragment) MoodBaseFragment.moodBaseFragment.initailizeMoodBaseFragment();
				break;
			case 2:
				if (null != SymtompsBaseFragment.symtompsBaseFragment) SymtompsBaseFragment.symtompsBaseFragment.intializeSymtomBaseFragment();
				break;
				
			case 3:
				if (null != WeightAndTemperatureFragment.weightFragment)
				{
					
					WeightAndTemperatureFragment.weightFragment.initWeightAndTempFragment();
				}
				break;
			case 4:
				if (null != PillsFragmnet.pillsFragment)
				{
					
					PillsFragmnet.pillsFragment.initPillsFragment();
				}
				break;
			default:
				break;
		}
		
	}
	
	public void saveDataOfCurretFragment()
	{
		switch (mTabHost.getCurrentTab())
		{
			case 0:
				if (null != AddNoteFragments.addnoteFragment) 
					AddNoteFragments.addnoteFragment.validateAndSaveNote();
				break;
			case 1:
				if (null != SymtompsBaseFragment.symtompsBaseFragment)
					SymtompsBaseFragment.symtompsBaseFragment.saveAndUpdate();
				break;
				
			case 2:
				if (null != MoodBaseFragment.moodBaseFragment) 
					MoodBaseFragment.moodBaseFragment.saveAndUpdate();
				break;
				
			case 3:
				if (null != WeightAndTemperatureFragment.weightFragment)
				{
					if (WeightAndTemperatureFragment.weightFragment.validateTempWeightAndHieght())
						WeightAndTemperatureFragment.weightFragment.saveAndUpdate();
				}
				break;
				
			default:
				break;
		}
		
	}
	
}
