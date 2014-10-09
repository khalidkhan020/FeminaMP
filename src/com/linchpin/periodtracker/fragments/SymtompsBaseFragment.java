package com.linchpin.periodtracker.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.MoodAndSymtomsAdaptor;
import com.linchpin.periodtracker.controller.ViewPageListener;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.SymptomsModel;
import com.linchpin.periodtracker.model.SymtomsSelectedModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.view.AddNoteView;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodttracker.database.AddNoteDBHandler;

public class SymtompsBaseFragment extends Fragment
{
	
	public ListView								mListView;
	public View									mView;
	public List<PeriodTrackerModelInterface>	periodTrackerModelInterfaces;
	public List<PeriodTrackerModelInterface>	modelInterfacesSymtomSelected;
	
	public List<SymtomsSelectedModel>			symtomSelectedsForadd		= new ArrayList<SymtomsSelectedModel>();
	public List<SymtomsSelectedModel>			symtomSelectedsForUpdate	= new ArrayList<SymtomsSelectedModel>();
	
	public AddNoteDBHandler						addNoteDBHandler;
	
	MoodAndSymtomsAdaptor						adaptor;
	
	public static SymtompsBaseFragment			symtompsBaseFragment;
	
	DayDetailModel								dayDetailModel;
	
	RatingBar									ratingBar;
	SymptomsModel								symptomModel;
	Button										save;
	boolean										add							= false;
	
	public static List<SymtomsSelectedModel>	symtomsSelectedModels		= new ArrayList<SymtomsSelectedModel>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		mView = inflater.inflate(R.layout.symtoms, container, false);
		Theme theme=Theme.getCurrentTheme(getActivity(),mView);
		if(theme!=null)
		theme.applyBackgroundColor(R.id.content, "view_bg");
		mListView = (ListView) mView.findViewById(R.id.symptomslist);
		/*AdView adView = (AdView) mView.findViewById(R.id.adView);
		if (Utility.addDays(
				new Date(getActivity().getSharedPreferences("com.linchpin.periodtracker", 0).getLong("InstallDate",
						PeriodTrackerConstants.NULL_DATE)), 10).after(PeriodTrackerConstants.CURRENT_DATE)
				|| getActivity().getSharedPreferences("com.linchpin.periodtracker", 0).getBoolean("purchased", false)) {
			adView.setVisibility(View.GONE);
		}*/
		intializeSymtomBaseFragment();
		mView.findViewById(R.id.symtominfobutton).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), HomeScreenHelp.class);
				intent.putExtra("classname", "symtom");
				startActivity(intent);
			}
		});
		
		return mView;
		
	}
	
	public void intializeSymtomBaseFragment()
	{
		
		int i = ((AddNoteView) getActivity()).mViewpager.getCurrentItem();
		ViewPageListener.activeFragment = symtompsBaseFragment;
		if (i == 2)
		{			
			if (ViewPageListener.forwordPerviousFragment == null) ViewPageListener.forwordPerviousFragment = symtompsBaseFragment;
		}
		periodTrackerModelInterfaces = ((AddNoteView) getActivity()).periodTrackerModelInterfaceGetAllSymptoms;
		modelInterfacesSymtomSelected = ((AddNoteView) getActivity()).periodTrackerModelInterfacesSymtomSelected;
		adaptor = new MoodAndSymtomsAdaptor(periodTrackerModelInterfaces, modelInterfacesSymtomSelected, PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT,
				getActivity());
		
		mListView.setAdapter(adaptor);
		
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				// TODO Auto-generated method stub
				// Toast.makeText(getActivity(), , Toast.LENGTH_SHORT).show();
			}
		});
		/*
		 * save = (Button) mView.findViewById(R.id.savesymtom);
		 * save.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { saveAndUpdate(); } });
		 */
		
	}
	
	public void saveAndUpdate()
	{
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
		addNoteDBHandler = new AddNoteDBHandler(symtompsBaseFragment.getActivity());		
		AddNoteView addNoteView = (AddNoteView) symtompsBaseFragment.getActivity();
		dayDetailModel = addNoteView.dayDetailModel;
		
		if (modelInterfacesSymtomSelected == null || modelInterfacesSymtomSelected.size() == 0)
		{
			addNoteDBHandler.addSymtomSelectedForDayDetail(symtompsBaseFragment.adaptor.getSymtomsSelectedModels());
			addNoteView.periodTrackerModelInterfacesSymtomSelected = addNoteDBHandler.getAllSelectedSymptom(dayDetailModel.getId());
			modelInterfacesSymtomSelected = addNoteView.periodTrackerModelInterfacesSymtomSelected;
		}
		else
		{
			addNoteDBHandler.deleteAndSymtomSelectedForDayDetail(dayDetailModel.getId(), adaptor.getSymtomsSelectedModels());
			
			addNoteView.periodTrackerModelInterfacesSymtomSelected = addNoteDBHandler.getAllSelectedSymptom(dayDetailModel.getId());
			
		}
		
	}
	
}
