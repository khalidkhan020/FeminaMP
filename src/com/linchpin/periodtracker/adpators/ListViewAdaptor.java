package com.linchpin.periodtracker.adpators;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;

public class ListViewAdaptor extends BaseAdapter {
	TextView startDate, endDate, cycleLength;
	Context context;
	List<PeriodTrackerModelInterface> periodTrackerModel;
	public List<PeriodTrackerModelInterface> getPeriodTrackerModel()
	{
		return periodTrackerModel;
	}


	public void setPeriodTrackerModel(List<PeriodTrackerModelInterface> periodTrackerModel)
	{
		this.periodTrackerModel = periodTrackerModel;
	}

	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat(PeriodTrackerObjectLocator
			.getInstance().getDateFormat());
	String className;

	public String getClassName()
	{
		return className;
	}


	public void setClassName(String className)
	{
		this.className = className;
	}


	public ListViewAdaptor(
			List<PeriodTrackerModelInterface> periodTrackerModel,
			Context context, String className) {
		this.periodTrackerModel = periodTrackerModel;
		this.context = context;
		this.className = className;

	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (periodTrackerModel != null)
			return periodTrackerModel.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return periodTrackerModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	private void applyTheme(View view)
	{
		
			Theme t = Theme.getCurrentTheme(context,view);
		if (t != null)
		{
			
			
			
			t.applyTextColor(R.id.starttextdate, "text_color");
			t.applyTextColor(R.id.endtextdate, "text_color");
			t.applyTextColor(R.id.cyclelengthlistrow, "text_color");
			
		}
		/*else
		
		setContentView(R.layout.homescreen);
		*/
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		if (null == row) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.listrow, parent, false);
			applyTheme(row);
		}
		startDate = (TextView) row.findViewById(R.id.starttextdate);
		endDate = (TextView) row.findViewById(R.id.endtextdate);
		cycleLength = (TextView) row.findViewById(R.id.cyclelengthlistrow);

		PeriodLogModel tempLogModel = (PeriodLogModel) periodTrackerModel
				.get(position);
		if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode()) 
		{
			if (className
					.equals(PeriodTrackerConstants.PAST_PERIOD_RECORD_LIST_FRAGMENT)
					|| className
							.equals(PeriodTrackerConstants.PERDICTION_PERIOD_LIST_FRAGMNET)) {
				if (!tempLogModel.isPregnancy()) {
					startDate
							.setText(format.format(tempLogModel.getStartDate()));
				} else {
					startDate.setText(context.getResources().getString(
							R.string.pregnant));
				}
				if (tempLogModel.getEndDate().getTime() == PeriodTrackerConstants.NULL_DATE)
					endDate.setText(context.getResources().getString(R.string.stillcounting));
				else
					endDate.setText(format.format(tempLogModel.getEndDate()));
				if (tempLogModel.getCycleLength() == 0)
					cycleLength.setText("/");
				else
					cycleLength.setText("" + tempLogModel.getCycleLength());
			}

			else if (className
					.equals(PeriodTrackerConstants.PAST_FERTILE_RECORDS)
					|| className
							.equals(PeriodTrackerConstants.PERDICTION_FERTILE_RECORDS)) {
				if (!tempLogModel.isPregnancy()) {
					startDate.setText(format.format(tempLogModel
							.getFertileStartDate()));
					endDate.setText(format.format(tempLogModel
							.getFertileEndDate()));
					cycleLength.setText("12");

				}
			} else if (className
					.equals(PeriodTrackerConstants.PAST_OVULATION_RECORDS)
					|| className
							.equals(PeriodTrackerConstants.PERDICTION_OVULATION_RECORDS)) {
				if (!tempLogModel.isPregnancy()) {
					startDate.setText(format.format(tempLogModel
							.getOvulationDate()));

				}
			}
		} else {
			if (className
					.endsWith(PeriodTrackerConstants.PAST_PERIOD_RECORD_LIST_FRAGMENT)) {

				if (tempLogModel.isPregnancy())
					startDate.setText(context.getResources().getString(
							R.string.pregnant));
				else
					startDate
							.setText(format.format(tempLogModel.getStartDate()));
				if (tempLogModel.getEndDate().getTime() == PeriodTrackerConstants.NULL_DATE)
					endDate.setText("/");
				else
					endDate.setText(format.format(tempLogModel.getEndDate()));
				if (tempLogModel.getCycleLength() == 0)
					cycleLength.setText("/");
				else
					cycleLength.setText("" + tempLogModel.getCycleLength());

			} else if (className
					.equals(PeriodTrackerConstants.PAST_FERTILE_RECORDS)) {
				if (!tempLogModel.isPregnancy()) {
					if (null != tempLogModel.getFertileStartDate()) {
						startDate.setText(format.format(tempLogModel
								.getFertileStartDate()));
						endDate.setText(format.format(tempLogModel
								.getFertileEndDate()));
						cycleLength.setText("12");
					}
				}
			} else if (className
					.equals(PeriodTrackerConstants.PAST_OVULATION_RECORDS)) {
				if (!tempLogModel.isPregnancy()
						&& null != tempLogModel.getFertileStartDate()) {
					startDate.setText(format.format(tempLogModel
							.getOvulationDate()));
				}
			}
		}

		return row;
	}
}