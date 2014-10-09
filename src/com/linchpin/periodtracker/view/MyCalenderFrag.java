package com.linchpin.periodtracker.view;

import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidGridAdapter;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.CaldroidSampleCustomAdapter;
import com.linchpin.periodtracker.adpators.CalendarMoodandSymtomListAdaptor;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodttracker.database.CalendarDBHandler;
import com.meetme.android.horizontallistview.HorizontalListView;

public class MyCalenderFrag extends CaldroidFragment implements OnClickListener
{
	
	public static CaldroidSampleCustomAdapter	adapter;
	
	private void applyTheme(View v)
	{
		t = Theme.getCurrentTheme(getActivity(), v);
		if (t != null)
		{
			t.applyBackgroundColor(R.id.content, "heading_bg");
			t.applyBackgroundDrawable(R.id.calendornotes, "mpt_note_bg");
			t.applyTextColor(R.id.namemoods, "text_color");
			t.applyTextColor(R.id.namesymtoms, "text_color");
			t.applyTextColor(R.id.weightoncalendar, "text_color");
			t.applyTextColor(R.id.temperatureoncalendar, "text_color");
			t.applyBackgroundDrawable(R.id.bottominfobar, "bottom_img");
		}
		
	}
	
	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year)
	{
		return adapter = new CaldroidSampleCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData);
	}
	
	View	view;
	private TextView	noteView, tempTextvaule, weghTextvalue;
	private HorizontalListView	listViewforMoods, listViewforSymtoms;
	View						previousDateView;
	Date						currentDate;
	Theme						t;
	
	public void selectDate(final Date date)
	{
		try
		{
			CalendarDBHandler calendarDBHandler = new CalendarDBHandler(getActivity());
			DayDetailModel dayDetailModel = (DayDetailModel) calendarDBHandler.getDayDetailForDateInCalender(date);
			
			if (null != dayDetailModel.getNote() && !dayDetailModel.getNote().toString().trim().equals("")) noteView.setText(getResources().getString(R.string.notes) + "\n" + dayDetailModel.getNote());
			else noteView.setText("");
			if (null != dayDetailModel.getTemp() && !dayDetailModel.getTemp().toString().trim().equals("") && dayDetailModel.getTemp() != Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))
			{
				if (PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(getResources().getString(R.string.celsius))) tempTextvaule.setText(Utility.getStringFormatedNumber(String.valueOf((Utility.ConvertToCelsius(dayDetailModel.getTemp())))) + " "
						+ PeriodTrackerObjectLocator.getInstance().getTempUnit());
				else tempTextvaule.setText(Utility.getStringFormatedNumber(String.valueOf(dayDetailModel.getTemp())) + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
			}
			else tempTextvaule.setText("");
			if (null != dayDetailModel.getWeight() && !dayDetailModel.getWeight().toString().trim().equals("") && dayDetailModel.getWeight() > 0)
			{
				if (PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(getResources().getString(R.string.KG))) weghTextvalue.setText((Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram(dayDetailModel.getWeight()))) + " " + PeriodTrackerObjectLocator.getInstance()
						.getWeighUnit()));
				else weghTextvalue.setText((Utility.getStringFormatedNumber(String.valueOf((dayDetailModel.getWeight()))) + " " + PeriodTrackerObjectLocator.getInstance().getWeighUnit()));
			}
			else weghTextvalue.setText("");
			List<PeriodTrackerModelInterface> interfaces = calendarDBHandler.getMoodListforDayDetailId(dayDetailModel.getId());
			if (null != interfaces) listViewforMoods.setAdapter(new CalendarMoodandSymtomListAdaptor(interfaces, PeriodTrackerConstants.MOOD_BASE_FRAGMENT, getActivity()));
			interfaces = calendarDBHandler.getSymtomListforDayDetailId(dayDetailModel.getId());
			if (null != interfaces) listViewforSymtoms.setAdapter(new CalendarMoodandSymtomListAdaptor(interfaces, PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT, getActivity()));
			if (date.equals(currentDate))
			{
				if (previousDateView != null)
				{
					if (null != dayDetailModel.getNote() && !dayDetailModel.getNote().toString().trim().equals(""))
					{
						if (dayDetailModel.getNote().equals("")) previousDateView.findViewById(R.id.editnote).setVisibility(View.INVISIBLE);
						else previousDateView.findViewById(R.id.editnote).setVisibility(View.VISIBLE);
						if (!dayDetailModel.isIntimate()) previousDateView.findViewById(R.id.intimateimage).setVisibility(View.INVISIBLE);
						else previousDateView.findViewById(R.id.intimateimage).setVisibility(View.VISIBLE);
					}
					previousDateView.invalidate();
				}
				
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.calendar, (ViewGroup) root.findViewById(R.id.day_detail_holder), true);
		noteView = (TextView) view.findViewById(R.id.calendornotes);
		applyTheme(view);
		listViewforMoods = (HorizontalListView) view.findViewById(R.id.moodsoncalendarlist);
		listViewforSymtoms = (HorizontalListView) view.findViewById(R.id.Symtomsoncalendarlist);
		tempTextvaule = (TextView) view.findViewById(R.id.temperatureoncalendarvaule);
		weghTextvalue = (TextView) view.findViewById(R.id.weightoncalendarvaule);
		noteView.setOnClickListener(this);
		view.findViewById(R.id.calendarinfobutton).setOnClickListener(this);
		currentDate = Utility.setHourMinuteSecondZero(new Date());
		return root;
	}
	
	@Override
	public void onClick(View v)
	{
		
		Intent intent;
		int id = v.getId();
		
		if (id == R.id.calendarinfobutton)
		{
			intent = new Intent(getActivity(), HomeScreenHelp.class);
			intent.putExtra("classname", "calender");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
		}
		else if (id == R.id.calendornotes)
		{
			intent = new Intent(getActivity(), AddNoteView.class);
			intent.putExtra("date", currentDate.getTime());
			startActivityForResult(intent,10006);
			getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
			
		}
		
	}
}
