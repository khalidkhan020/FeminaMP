package com.linchpin.periodtracker.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.ListViewAdaptor;
import com.linchpin.periodtracker.interfaces.CalenderDialog;
import com.linchpin.periodtracker.interfaces.PeriodMessanger;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.view.PeriodLogPagerView;
import com.linchpin.periodtracker.widget.MPTButton;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;

public class LogFragment extends Fragment implements PeriodMessanger
{
	public static final int			GET_END_DATE		= 1;
	public static final int			REFRESH_PAST_LIST	= 2;
	protected View					mView;
	//protected ImageButton			help;
	protected MPTButton				past, prediction;
	protected ListViewAdaptor		adpator;
	protected ListView				listView;
	protected PeriodLogDBHandler	periodLogDBHandler;
	protected TextView				tAverageCycleLength, tAveragePeriodLength, lAverageCycleLength, lAveragePeriodLength;
	private LinearLayout			bStartDate, bEndDate;
	private TextView				tStartDate, tEndDate;
	private CalenderDialog			calenderDialog;
	////////////////////////////////////////////////////////////////////////////////////////////
	
	public static LogFragment		periodLogFragment;	
	private SimpleDateFormat		dateFormat;
	Theme t;
	private void applyTheme(View v)
	{
		
		t = Theme.getCurrentTheme(getActivity(),v);
		if (t != null)
		{		
			
			t.applyBackgroundColor(R.id.linear, "heading_bg");
			
			t.applyBackgroundDrawable(R.id.startdate, "mpt_button_date_sltr");
			t.applyBackgroundDrawable(R.id.enddate, "mpt_button_date_sltr");
			
			t.applyTextColor(R.id.textviewcyclelength, "heading_fg");
			t.applyTextColor(R.id.textviewenddate, "heading_fg");
			t.applyTextColor(R.id.textviewstartdate, "heading_fg");
			
			t.applyTextColor(R.id.t1, "text_color");
			t.applyTextColor(R.id.t2, "text_color");
			t.applyTextColor(R.id.t3, "text_color");
			t.applyTextColor(R.id.t4, "text_color");
			t.applyTextColor(R.id.startdatetext, "text_color");
			tEndDate.setTextColor(getResources().getColor(R.color.disabledatecolor));
			t.applyTextColor(R.id.simpleperiodlogtextforperiodlength, "text_color");
			t.applyTextColor(R.id.periodlogtextforperiodlength, "text_color");
			t.applyTextColor(R.id.simpleperiodlogtextforcyclelength, "text_color");
			t.applyTextColor(R.id.periodlogtextforcyclelength, "text_color");
			
			
			
		}}
	
	@Override
	public void onAttach(Activity activity)
	{
		calenderDialog = (CalenderDialog) activity;
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		mView = inflater.inflate(R.layout.period_log, container, false);
		initLayout();
		applyTheme(mView);
		return mView;
	}
	
	private void initStartDateAndEndDate()
	{
		tStartDate.setText(getResources().getString(R.string.period_start_date));
		tEndDate.setTextColor(getResources().getColor(R.color.disabledatecolor));
		
		dateFormat = new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		//pastPeriodTrackerModelInterfaces = ((PeriodLogPagerView) getActivity()).periodTrackerModelInterfaces;
		if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
		{
			bStartDate.setEnabled(true);
			
			
			if(t!=null)
				
				tStartDate.setTextColor(t.getColor("text_color"));
			
			else tStartDate.setTextColor(Color.parseColor(getString(R.color.newdatecolor)));
			final PeriodLogModel periodLogModel = (PeriodLogModel) periodLogDBHandler.getTopLogs(PeriodTrackerObjectLocator.getInstance()
					.getProfileId());
			
			if (periodLogModel != null)
			{
				if (periodLogModel.getEndDate()!=null&&periodLogModel.getEndDate().getTime() == (PeriodTrackerConstants.NULL_DATE))
				{
					tStartDate.setText("" + dateFormat.format(periodLogModel.getStartDate()));
					bEndDate.setEnabled(true);
					bStartDate.setEnabled(false);
					
					if(t!=null)
					{
						
						tEndDate.setTextColor(t.getColor("text_color"));
					}else tEndDate.setTextColor(getResources().getColor(R.color.newdatecolor));
					tEndDate.setText(getResources().getString(R.string.period_end_date));
				}
				else
				{
					bEndDate.setEnabled(false);
					tEndDate.setTextColor(getResources().getColor(R.color.disabledatecolor));}
			}
			
			bStartDate.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(new Date());
					if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
					{
						if (periodLogModel != null)
						{
							if (periodLogModel.getEndDate() != null&&periodLogModel.getEndDate().getTime() == (PeriodTrackerConstants.NULL_DATE))
							{
								calendar.setTime(periodLogModel.getStartDate());
							}
						}
						calenderDialog.showDatePickerDialog(R.string.setstartdate, calendar, null, PeriodLogPagerView.NEW_START_DATE).show();
					}
					else
					{
						Toast.makeText(getActivity(), getResources().getString(R.string.pregnancy), Toast.LENGTH_LONG).show();
					}
				}
				
			});
			
			bEndDate.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					
					if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
					{
						Date startdate = periodLogModel.getStartDate();
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(Utility.addDays(startdate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1));
						calenderDialog.showDatePickerDialog(R.string.setenddate, calendar, periodLogModel, PeriodLogPagerView.UPDATE_END_DATE).show();
						
					}
					else
					{
						Toast.makeText(getActivity(), getResources().getString(R.string.pregnancy), Toast.LENGTH_LONG).show();
					}
				}
			});
		}
		else
		{
			bStartDate.setEnabled(false);
			bEndDate.setEnabled(false);
			tStartDate.setTextColor(getResources().getColor(R.color.disabledatecolor));
			tEndDate.setTextColor(getResources().getColor(R.color.disabledatecolor));
		}
		
		
	}
	
	private void initLayout()
	{
		periodLogDBHandler = new PeriodLogDBHandler(getActivity());
		///help = (ImageButton) mView.findViewById(R.id.periodloginfobutton);
		past = (MPTButton) mView.findViewById(R.id.past);
		prediction = (MPTButton) mView.findViewById(R.id.prediction);
		listView = (ListView) mView.findViewById(R.id.pastlist);
		
		tAverageCycleLength = (TextView) mView.findViewById(R.id.periodlogtextforcyclelength);
		tAveragePeriodLength = (TextView) mView.findViewById(R.id.periodlogtextforperiodlength);
		lAverageCycleLength = (TextView) mView.findViewById(R.id.simpleperiodlogtextforcyclelength);
		lAveragePeriodLength = (TextView) mView.findViewById(R.id.simpleperiodlogtextforperiodlength);
		
		bStartDate = (LinearLayout) mView.findViewById(R.id.startdate);
		bEndDate = (LinearLayout) mView.findViewById(R.id.enddate);
		tEndDate = (TextView) mView.findViewById(R.id.enddatetext);
		tStartDate = (TextView) mView.findViewById(R.id.startdatetext);
		if(t!=null)
		{
			tStartDate.setTextColor(t.getColor("text_color"));
			tEndDate.setTextColor(t.getColor("text_color"));
		}
		refreshLengthItems();
		
	}
	
	protected void refreshLengthItems()
	{
		if (PeriodTrackerObjectLocator.getInstance().isAveraged())
		{
			lAveragePeriodLength.setText("Average Period Length");
			lAverageCycleLength.setText("Average Cycle Length");
		}
		else
		{
			lAveragePeriodLength.setText(getString(R.string.defaultperiodlength));
			lAverageCycleLength.setText(getString(R.string.defaultcyclelength));
			
		}
		tAverageCycleLength.setText(+PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength() + " " + getString(R.string.days));
		tAveragePeriodLength.setText(+PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() + " " + getString(R.string.days));
		initStartDateAndEndDate();
	}
	
	public void bind(int id, PeriodMessanger messanger)
	{
	}
	
	@Override
	public void sendMessage(int id, Bundle bundle)
	{
	}
}