package com.linchpin.periodtracker.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.ListViewAdaptor;
import com.linchpin.periodtracker.interfaces.PeriodMessanger;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodtracker.view.PeriodLogPagerView;

public class FertileLogFragment extends LogFragment implements OnClickListener
{
	private List<PeriodTrackerModelInterface> loglist;
	@Override
	public void onAttach(Activity activity)
	{
		 ((PeriodMessanger) activity).
		bind(PeriodLogPagerView.PERIOD_LOG_FRAGMENT_ID, this);
		super.onAttach(activity);
	}
	@Override
	public void sendMessage(int id, Bundle bundle)
	{
		switch (id)
		{
			case REFRESH_PAST_LIST:
				refreshAdapterList(true);
				break;
			
			default:
				break;
		}
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		mView.findViewById(R.id.periodloginfobutton).setOnClickListener(this);
		past.setOnClickListener(this);
		prediction.setOnClickListener(this);		
		adpator = new ListViewAdaptor(loglist, getActivity(), PeriodTrackerConstants.PAST_FERTILE_RECORDS);
		listView.setAdapter(adpator);
		refreshAdapterList(true);
			return mView;
	}		
	private void refreshAdapterList(boolean ispast)
	{	
		List<PeriodTrackerModelInterface> temp;		
		if (loglist == null) loglist = new ArrayList<PeriodTrackerModelInterface>();
		loglist.clear();
		if (ispast||PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
		{
			past.setSelected(true);		
			prediction.setSelected(false);
			
			temp = periodLogDBHandler.getPastFertileAndOvulationDates();					
			for (PeriodTrackerModelInterface interfaces : temp)
			{
				PeriodLogModel logModel;
				logModel = (PeriodLogModel) interfaces;
				if (!logModel.isPregnancy())
				{
					loglist.add(interfaces);
				}
			}
			adpator.setClassName(PeriodTrackerConstants.PAST_FERTILE_RECORDS);
			
		}
		else
		{
			prediction.setSelected(true);
			past.setSelected(false);
			temp = periodLogDBHandler.getPerdictionFertileDatesAndOvulationDates();
		
			
			for (PeriodTrackerModelInterface interfaces : temp)
			{
				PeriodLogModel logModel;
				logModel = (PeriodLogModel) interfaces;
				if (!logModel.isPregnancy() && logModel.getStartDate() != null
						&& logModel.getStartDate().compareTo(Utility.setHourMinuteSecondZero(new Date())) > 0)
				{
					loglist.add(interfaces);
				}
			}
			
			adpator.setClassName(PeriodTrackerConstants.PERDICTION_FERTILE_RECORDS);
		}
		adpator.setPeriodTrackerModel(loglist);
		adpator.notifyDataSetChanged();
		
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		refreshLengthItems();
	}
		@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.past)
		{
			refreshAdapterList(true);
		}
		else if (id == R.id.prediction)
		{
			refreshAdapterList(false);
		}
		else if (id == R.id.periodloginfobutton)
		{
			Intent intent = new Intent(getActivity(), HomeScreenHelp.class);
			intent.putExtra("classname", "fertilelog");
			startActivity(intent);
		}
		else
		{
		}
		
	}
	
}