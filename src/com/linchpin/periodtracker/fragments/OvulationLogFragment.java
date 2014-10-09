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
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.ListViewAdaptor;
import com.linchpin.periodtracker.interfaces.PeriodMessanger;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodtracker.view.PeriodLogPagerView;

public class OvulationLogFragment extends LogFragment implements OnClickListener
{
	private List<PeriodTrackerModelInterface>	loglist;
	
	@Override
	public void onAttach(Activity activity)
	{
		((PeriodMessanger) activity).bind(PeriodLogPagerView.PERIOD_LOG_FRAGMENT_ID, this);
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
		adpator = new ListViewAdaptor(loglist, getActivity(), PeriodTrackerConstants.PAST_OVULATION_RECORDS);
		listView.setAdapter(adpator);
		refreshAdapterList(true);lAveragePeriodLength.measure(lAveragePeriodLength.getWidth(), lAveragePeriodLength.getHeight());
		int height = lAveragePeriodLength.getMeasuredHeight() - 7;
		((TextView)mView.findViewById(R.id.textviewcyclelength)).setVisibility(View.GONE);
		((TextView)mView.findViewById(R.id.textviewenddate)).setVisibility(View.GONE);
		((TextView)mView.findViewById(R.id.textviewstartdate)).setText(getString(R.string.ovulation));
		lAveragePeriodLength.setPadding(0, height, 0, height);
		tAveragePeriodLength.setPadding(0, height, 0, height);
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
				if (!logModel.isPregnancy() )
				{
					loglist.add(interfaces);
				}
			}
			adpator.setClassName(PeriodTrackerConstants.PAST_OVULATION_RECORDS);
			
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
			
			adpator.setClassName(PeriodTrackerConstants.PERDICTION_OVULATION_RECORDS);
		}
		adpator.setPeriodTrackerModel(loglist);
		adpator.notifyDataSetChanged();
		
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		refreshOvulationText();
	}
	
	private void refreshOvulationText()
	{
		
		lAverageCycleLength.setVisibility(View.GONE);
		tAverageCycleLength.setVisibility(View.GONE);
		if (PeriodTrackerObjectLocator.getInstance().isAveraged())		
		lAveragePeriodLength.setText(R.string.average_ovulationdaylength);		
		else		
		lAveragePeriodLength.setText(R.string.ovulationdaylength);		
		tAveragePeriodLength.setText("" + PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength());
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
			intent.putExtra("classname", "ovulationlog");
			startActivity(intent);
		}
		else
		{
		}
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
	}
	
}
