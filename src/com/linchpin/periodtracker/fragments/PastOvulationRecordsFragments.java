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
import com.linchpin.periodtracker.utlity.Utility;

public class PastOvulationRecordsFragments extends Fragment {

	View mView;
	ListView pastList;
	PeriodLogDBHandler periodLogDBHandler;
	List<PeriodTrackerModelInterface> pastRecordList;
	ListViewAdaptor adpator;
	TextView mTextStartDate, mTextEndDate, mTextCycleLength;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		System.out.println("common past periodlist "+new Date());
		mView = inflater.inflate(R.layout.past_list, container, false);
		pastList = (ListView) mView.findViewById(R.id.pastlist);
		mTextStartDate = (TextView) mView.findViewById(R.id.textviewstartdate);
		mTextEndDate = (TextView) mView.findViewById(R.id.textviewenddate);
		mTextCycleLength = (TextView) mView.findViewById(R.id.textviewcyclelength);
		mTextStartDate.setText(R.string.ovulationdate);
		mTextEndDate.setText("");
		mTextCycleLength.setText("");
		return mView;
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
		periodLogDBHandler = new PeriodLogDBHandler(getActivity());
		pastRecordList = periodLogDBHandler.getPastFertileAndOvulationDates();
		PeriodLogModel logModel;
		ArrayList<PeriodTrackerModelInterface> arrayList = new ArrayList<PeriodTrackerModelInterface>();
		for (PeriodTrackerModelInterface iterable : pastRecordList) {
			logModel = (PeriodLogModel) iterable;
			if (!logModel.isPregnancy()&& null!=logModel.getOvulationDate()&&logModel.getOvulationDate().before(Utility.setHourMinuteSecondZero(new Date()))) {
				arrayList.add(iterable);
			}

		}
		adpator = new ListViewAdaptor(arrayList, getActivity(), PeriodTrackerConstants.PAST_OVULATION_RECORDS);
		pastList.setAdapter(adpator);

	}

}
