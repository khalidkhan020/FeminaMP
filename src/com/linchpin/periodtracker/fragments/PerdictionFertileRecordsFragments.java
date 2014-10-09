package com.linchpin.periodtracker.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.ListViewAdaptor;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;

public class PerdictionFertileRecordsFragments extends Fragment {

	View mView;
	ListView predictionList;
	PeriodLogDBHandler dbHandler  ;
	List<PeriodTrackerModelInterface> predictionRecordList;
	ListViewAdaptor adaptor;
	TextView mTextStartDate, mTextEndDate, mTextCycleLength;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
			
	//	System.out.println("common perdiciton fertilelist "+new Date() +"" + new Date().getTime());
		mView = inflater.inflate(R.layout.perdiction_list, container, false);
		// TODO Auto-generated method stub
		predictionList = (ListView) mView.findViewById(R.id.perdictionlist);
		
		mTextStartDate = (TextView) mView.findViewById(R.id.perdictionlisttextstartdate);
		mTextEndDate = (TextView) mView.findViewById(R.id.perdictionlisttextenddate);
		mTextCycleLength = (TextView) mView
				.findViewById(R.id.perdictionlisttextcyclelength);
		
		mTextCycleLength.setText(R.string.duration);
		return mView;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if(!PeriodTrackerObjectLocator.getInstance().getPregnancyMode()){
		dbHandler = new PeriodLogDBHandler(getActivity());
		predictionRecordList = dbHandler.getPerdictionFertileDatesAndOvulationDates(); 
		
		PeriodLogModel logModel;
		ArrayList<PeriodTrackerModelInterface> arrayList = new ArrayList<PeriodTrackerModelInterface>();
		if (null != predictionRecordList) {
			for (int i =0; i<predictionRecordList.size();i++) {
				if(arrayList.size()<6){
				logModel = (PeriodLogModel) predictionRecordList.get(i);
				if (!logModel.isPregnancy()
						&& logModel.getFertileStartDate() != null
						&& !logModel.getFertileStartDate().before(Utility
										.setHourMinuteSecondZero(new Date()))) {
					arrayList.add(predictionRecordList.get(i));
				}
				}else{
					break;
				}
			}
		}

		
		adaptor=new ListViewAdaptor(arrayList, getActivity(),PeriodTrackerConstants.PERDICTION_FERTILE_RECORDS);
		predictionList.setAdapter(adaptor);
		
		
	
		}
	}

	
}
