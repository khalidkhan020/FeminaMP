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
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.MoodAndSymtomsAdaptor;
import com.linchpin.periodtracker.controller.ViewPageListener;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.MoodSelected;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.view.AddNoteView;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodttracker.database.AddNoteDBHandler;

public class MoodBaseFragment extends Fragment {

	ArrayList<Integer> Items = new ArrayList<Integer>();
	View mView;
	static ListView mListView;
	public static MoodBaseFragment moodBaseFragment;
	AddNoteDBHandler addNoteDBHandler;
	List<PeriodTrackerModelInterface> periodTrackerModelInterface;
	List<PeriodTrackerModelInterface> modelInterfacesSelceted;
	List<MoodSelected> moodSelectedsForUpdate = new ArrayList<MoodSelected>();
	List<MoodSelected> moodSelectedsForAdd = new ArrayList<MoodSelected>();
	List<MoodSelected> moodSelectedsForDelete = new ArrayList<MoodSelected>();
	MoodAndSymtomsAdaptor adaptor;
	//Button save;
	DayDetailModel dayDetailModel;

	Boolean add = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);

		
		mView = inflater.inflate(R.layout.mood, container, false);
		Theme theme=Theme.getCurrentTheme(getActivity(),mView);
		if(theme!=null)
		theme.applyBackgroundColor(R.id.content, "view_bg");		
		mView.findViewById(R.id.moodsinfobutton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						HomeScreenHelp.class);
				intent.putExtra("classname", "mood");
				startActivity(intent);
				
			}
		});
		setmListView((ListView) mView.findViewById(R.id.moodListView));
		initailizeMoodBaseFragment();
		return mView;

	}

	public void initailizeMoodBaseFragment(){
		
			int i = ((AddNoteView) getActivity()).mViewpager.getCurrentItem();
			ViewPageListener.activeFragment = moodBaseFragment;
			if (i == 1) {
				
				if (ViewPageListener.forwordPerviousFragment == null)
					ViewPageListener.forwordPerviousFragment = moodBaseFragment;
			}
		addNoteDBHandler = new AddNoteDBHandler(getActivity());
		periodTrackerModelInterface = ((AddNoteView) getActivity()).periodTrackerModelInterfacesGetAllMoods;
		modelInterfacesSelceted = ((AddNoteView) getActivity()).periodTrackerModelInterfacesMoodSelected;
		adaptor = new MoodAndSymtomsAdaptor(periodTrackerModelInterface, modelInterfacesSelceted, PeriodTrackerConstants.MOOD_BASE_FRAGMENT,
				getActivity());

		getmListView().setAdapter(adaptor);
		getmListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
			}
		});
		
		
		
	}
	
	public void saveAndUpdate() {

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
		addNoteDBHandler = new AddNoteDBHandler(getActivity());
		dayDetailModel = ((AddNoteView) getActivity()).dayDetailModel;
		
		if (modelInterfacesSelceted == null || modelInterfacesSelceted.size() == 0) {
			addNoteDBHandler.addMoodSelectedForDayDetail(adaptor.getMoodSelecteds());
			((AddNoteView) getActivity()).periodTrackerModelInterfacesMoodSelected = addNoteDBHandler.getAllSelectedMood(dayDetailModel.getId());
			modelInterfacesSelceted = ((AddNoteView) getActivity()).periodTrackerModelInterfacesMoodSelected;
		} else {
			addNoteDBHandler.deleteAndAddMoodSelectedForDayDetail(dayDetailModel.getId(), adaptor.getMoodSelecteds());
			((AddNoteView)getActivity()).periodTrackerModelInterfacesMoodSelected= addNoteDBHandler.getAllSelectedMood(dayDetailModel.getId());
		}

	}

	public static ListView getmListView() {
		return mListView;
	}

	public static void setmListView(ListView mListView) {
		MoodBaseFragment.mListView = mListView;
	}
}
