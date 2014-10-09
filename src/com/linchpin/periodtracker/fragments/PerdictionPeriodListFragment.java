package com.linchpin.periodtracker.fragments;

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
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;

public class PerdictionPeriodListFragment extends Fragment {

	View mView;
	ListView predictionList;
	PeriodLogDBHandler dbHandler;
	List<PeriodTrackerModelInterface> predictionRecordList;
	ListViewAdaptor adaptor;
	TextView mTextStartDate, mTextEndDate, mTextCycleLength;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
			mView = inflater
					.inflate(R.layout.perdiction_list, container, false);

			// TODO Auto-generated method stub
		
		if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode()) {
			dbHandler = new PeriodLogDBHandler(getActivity());
			predictionRecordList = dbHandler.getPredictionLogs();

		}

		predictionList = (ListView) mView.findViewById(R.id.perdictionlist);
		mTextStartDate = (TextView) mView.findViewById(R.id.perdictionlisttextstartdate);
		mTextEndDate = (TextView) mView.findViewById(R.id.perdictionlisttextenddate);
		mTextCycleLength = (TextView) mView.findViewById(R.id.perdictionlisttextcyclelength);

		mTextCycleLength.setText(R.string.cyclelengthshort);
		return mView;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		{
		


			
			
			
			
			
			/*			PeriodLogModel logModel;
			ArrayList<PeriodTrackerModelInterface> arrayList = new ArrayList<PeriodTrackerModelInterface>();
			if (null != predictionRecordList) {
				for (PeriodTrackerModelInterface iterable : predictionRecordList) {
					logModel = (PeriodLogModel) iterable;
					if (!logModel.isPregnancy()&& logModel.getStartDate() != null
							&&logModel.getStartDate().compareTo(Utility.setHourMinuteSecondZero(new Date()))>0) {
						arrayList.add(iterable);
					}

				}
			}*/
			adaptor = new ListViewAdaptor(predictionRecordList, getActivity(),
					PeriodTrackerConstants.PERDICTION_PERIOD_LIST_FRAGMNET);
			predictionList.setAdapter(adaptor);
			adaptor.notifyDataSetChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

}
