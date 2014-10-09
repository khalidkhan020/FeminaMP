package com.linchpin.periodtracker.partnersharing;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidGridAdapter;
import com.caldroid.CaldroidListener;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.CaldroidSampleCustomAdapter;
import com.linchpin.periodtracker.model.CalendarDayDetailModel;
import com.linchpin.periodtracker.model.MoodDataModel;
import com.linchpin.periodtracker.model.MoodSelected;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.Pills;
import com.linchpin.periodtracker.model.SymptomsModel;
import com.linchpin.periodtracker.model.SymtomsSelectedModel;
import com.linchpin.periodtracker.model.TimeLineModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodtracker.widget.HelpButton;
import com.linchpin.periodttracker.database.CalendarDBHandler;

public class PartnerCalenderFragment extends CaldroidFragment
{
	TextView							date, notes, pills, moods, syms, notesLbl, pillsLbl, moodsLbl, symsLbl;
	
	public CaldroidSampleCustomAdapter	adapter;
	CalendarDBHandler					calendarDBHandler;														// = new CalendarDBHandler(context);
	View								view;
	HelpButton							helpbutton;
	Theme								t;
	private View						previousDateView;
	private Date						currentDate;
	
	private void applyTheme(View v)
	{
		
		t = Theme.getCurrentTheme(getActivity(), v);
		if (t != null)
		{
			t.applyBackgroundColor(R.id.current_date, "heading_bg");
			t.applyTextColor(R.id.notes, "text_color");
			t.applyTextColor(R.id.moods, "text_color");
			t.applyTextColor(R.id.symptoms, "text_color");
			t.applyTextColor(R.id.pills, "text_color");
		}
	}
	
	SimpleDateFormat	format;
	
	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year)
	{
		return adapter = new CaldroidSampleCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData);
	}
	
	View	root;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (root == null)
		{
			root = super.onCreateView(inflater, container, savedInstanceState);
			format = new SimpleDateFormat("yyyyMMdd");
			currentDate = Utility.setHourMinuteSecondZero(new Date());
			calendarDBHandler = new CalendarDBHandler(getActivity());
			view = inflater.inflate(R.layout.partner_day_details, (ViewGroup) root.findViewById(R.id.day_detail_holder), true);
			date = (TextView) view.findViewById(R.id.current_date);
			notes = (TextView) view.findViewById(R.id.notes_data);
			pills = (TextView) view.findViewById(R.id.pills_data);
			moods = (TextView) view.findViewById(R.id.moods_data);
			syms = (TextView) view.findViewById(R.id.symptoms_data);
			helpbutton = (HelpButton) view.findViewById(R.id.partnercalenderhelpbutton);
			notesLbl = (TextView) view.findViewById(R.id.notes);
			pillsLbl = (TextView) view.findViewById(R.id.pills);
			moodsLbl = (TextView) view.findViewById(R.id.moods);
			symsLbl = (TextView) view.findViewById(R.id.symptoms);
			clear(new Date().getTime());
			helpbutton.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), HomeScreenHelp.class);
					intent.putExtra("classname", "partner_Calender");
					startActivity(intent);
				}
			});
			setCaldroidListener(listener);
		}
		else ((ViewGroup) root.getParent()).removeAllViews();
		return root;
	}
	
	public void selectDate(final Date date)
	{
		try
		{
			if (APP.GLOBAL().getTimeLineMaps().containsKey(date.getTime()))
			{
				TimeLineModel lineModel = APP.GLOBAL().getTimeLineMaps().get(date.getTime());
				refreshView(lineModel);
			}
			else clear(date.getTime());
			//SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			CalendarDayDetailModel calendarDayDetailModel = (CalendarDayDetailModel) adapter.getExtraData().get(format.format(Utility.setHourMinuteSecondZero(date)));
			
			if (date.equals(currentDate))
			{
				if (previousDateView != null)
				{
					if (calendarDayDetailModel.isHavingNotes()) previousDateView.findViewById(R.id.editnote).setVisibility(View.VISIBLE);
					else previousDateView.findViewById(R.id.editnote).setVisibility(View.GONE);
					if (!calendarDayDetailModel.isIntimate()) previousDateView.findViewById(R.id.intimateimage).setVisibility(View.GONE);
					else previousDateView.findViewById(R.id.intimateimage).setVisibility(View.VISIBLE);
					previousDateView.invalidate();
				}
				
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	final CaldroidListener	listener	= new CaldroidListener()
										{
											
											@Override
											public void onSelectDate(final Date date, View view)
											{
												try
												{
													LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
													if (view == null) view = inflater.inflate(R.layout.custom_cell, null);
													if (currentDate != null && previousDateView != null && currentDate.compareTo(Utility.setHourMinuteSecondZero(new Date())) != 0) if (t != null) previousDateView.findViewById(R.id.layout).setBackgroundDrawable(
															t.getDrawableResource("mpt_cal_itm_nor_shp"));
													else previousDateView.findViewById(R.id.layout).setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_cell_graident_backgrouund));
													if (currentDate != null && date.compareTo(Utility.setHourMinuteSecondZero(new Date())) != 0) if (t != null) view.findViewById(R.id.layout).setBackgroundDrawable(t.getDrawableResource("mpt_cal_itm_pressed_shp"));
													else view.findViewById(R.id.layout).setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border));
													currentDate = date;
													previousDateView = view;
													selectDate(currentDate);
													
													System.out.println("");
													
												}
												catch (Exception e)
												{
													e.printStackTrace();
												}
											}
											
											@Override
											public void onChangeMonth(int month, int year)
											{
												try
												{
													
													Calendar currentcal = GregorianCalendar.getInstance();
													currentcal.setTime(new Date());
													int currentmonth = currentcal.get(Calendar.MONTH);
													CalendarDBHandler calendarDBHandler = new CalendarDBHandler(getActivity());
													Calendar calendarTemp = GregorianCalendar.getInstance();
													
													calendarTemp.set(Calendar.MONTH, month - 1);
													calendarTemp.set(Calendar.YEAR, year);
													calendarTemp.set(Calendar.DATE, 1);
													
													List<CalendarDayDetailModel> calendarDayDetailModels = calendarDBHandler.getPartnerDetailForDayInCalendor(Utility.addDays(new Date(calendarTemp.getTimeInMillis()), -23), Utility.addDays(new Date(calendarTemp.getTimeInMillis()), 67));
													for (CalendarDayDetailModel calendarDayDetailModel : calendarDayDetailModels)
														getExtraData().put(format.format(calendarDayDetailModel.getDate()), calendarDayDetailModel);
													if (currentmonth != month - 1) selectDate(Utility.setHourMinuteSecondZero(calendarTemp.getTime()));
													else selectDate(Utility.setHourMinuteSecondZero(new Date()));
													
												}
												
												catch (Exception e)
												{
													e.printStackTrace();
												}
											}
											
											@Override
											public void onLongSelectDate(View view, Date date)
											{
												//					Intent intent = new Intent(CaldroidSampleView.this, AddNoteView.class);
												//					intent.putExtra("date", date.getTime());
												//					startActivity(intent);
											}
											
										};
	
	public void clear(long nodate)
	{
		SimpleDateFormat format = new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		
		moods.setVisibility(View.GONE);
		pills.setVisibility(View.GONE);
		syms.setVisibility(View.GONE);
		notesLbl.setVisibility(View.GONE);
		notes.setVisibility(View.GONE);
		moodsLbl.setVisibility(View.GONE);
		pillsLbl.setVisibility(View.GONE);
		symsLbl.setVisibility(View.GONE);
		date.setText(format.format(nodate));
	}
	
	public void refreshView(TimeLineModel lineModel)
	{
		clear(lineModel.date);
		SimpleDateFormat format = new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		date.setText(format.format(lineModel.date));
		
		StringBuilder sbMoods = new StringBuilder(), sbSymptoms = new StringBuilder(), sbPills = new StringBuilder();
		for (PeriodTrackerModelInterface item : lineModel.moodSelectedsMap)
		{
			MoodSelected moodSelected = (MoodSelected) item;
			MoodDataModel moodModel = (MoodDataModel) calendarDBHandler.getMoodListforMoodId(moodSelected.getMoodId());
			if (moodSelected.getMoodWeight() != 0) sbMoods.append(moodModel.getImageUri() + " (" + moodSelected.getMoodWeight() + ") ");
		}
		for (PeriodTrackerModelInterface item : lineModel.symtomsSelectedModelsMap)
		{
			SymtomsSelectedModel symselected = (SymtomsSelectedModel) item;
			SymptomsModel dataModel = (SymptomsModel) calendarDBHandler.getSymtomListforSymId(symselected.getSymptomId());
			if (symselected.getSymptomWeight() != 0) sbSymptoms.append(dataModel.getImageUri() + "(" + symselected.getSymptomWeight() + ") ");
		}
		for (PeriodTrackerModelInterface item : lineModel.medicineMap)
		{
			System.out.println("");
			Pills pillsModel = (Pills) item;
			sbPills.append(pillsModel.getMedicineName() + " (" + pillsModel.getQuantity() + ") ");
			
		}
		if (lineModel.note != null && !lineModel.note.trim().equals(""))
		{
			notesLbl.setVisibility(View.VISIBLE);
			notes.setVisibility(View.VISIBLE);
			notes.setText(lineModel.note);
		}
		else
		{
			notesLbl.setVisibility(View.GONE);
			notes.setVisibility(View.GONE);
		}
		if (!sbMoods.toString().trim().equals(""))
		{
			moodsLbl.setVisibility(View.VISIBLE);
			moods.setVisibility(View.VISIBLE);
			moods.setText(sbMoods);
		}
		else
		{
			moodsLbl.setVisibility(View.GONE);
			moods.setVisibility(View.GONE);
		}
		if (!sbSymptoms.toString().trim().equals(""))
		{
			symsLbl.setVisibility(View.VISIBLE);
			syms.setVisibility(View.VISIBLE);
			syms.setText(sbSymptoms);
		}
		else
		{
			symsLbl.setVisibility(View.GONE);
			syms.setVisibility(View.GONE);
		}
		if (!sbPills.toString().trim().equals(""))
		{
			pillsLbl.setVisibility(View.VISIBLE);
			pills.setVisibility(View.VISIBLE);
			pills.setText(sbPills);
		}
		else
		{
			pillsLbl.setVisibility(View.GONE);
			pills.setVisibility(View.GONE);
		}
		
	}
}
