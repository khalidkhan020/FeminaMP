package com.linchpin.periodtracker.partnersharing;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.TimeLineModel;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodtracker.widget.HelpButton;
import com.linchpin.periodttracker.database.CalendarDBHandler;
import com.linchpin.periodttracker.database.DashBoardDBHandler;

public class PartnerToday extends Fragment
{
	
	private List<PeriodLogModel>	periodTrackerModes;
	private TextView				/*t1, t33, t22, t2, t3,*/ todayNotes, todayNotesHeading, moodHeading, symptomHeading;
	DashBoardDBHandler				dashBoardDBHandler;
	ListView						lstMoods, lstSym;
	List<PeriodTrackerModelInterface>	moodInterfaces, symInterfaces;
	PartnerMoodandSymtomListAdaptor		moodAdapter, symptomAdapter;
	HelpButton                          helpbutton;
	boolean	isFertile	= false, isOvulation = false, isReagularDay = false;
	
	PeriodLogModel		latestLogModel;
	int					periodlength	= 4;
	int					cyclelength		= 28;
	SimpleDateFormat	dateFormat;
	PeriodLogModel		periodLogModel;
	PeriodLogModel		periodLogModelPerdiction;
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.partner_today, container, false);
		t1 = (TextView) view.findViewById(R.id.about_day);
		t3 = (TextView) view.findViewById(R.id.next_period_date);
		t33 = (TextView) view.findViewById(R.id.prediction_period_date);
		t2 = (TextView) view.findViewById(R.id.next_fertile_Date);
		t22 = (TextView) view.findViewById(R.id.prediction_fertile_date);
		helpbutton=(HelpButton)view.findViewById(R.id.partner_today_helpbutton);
		todayNotes = (TextView) view.findViewById(R.id.today_notes);
		lstMoods = (ListView) view.findViewById(R.id.lst_moods);
		lstSym = (ListView) view.findViewById(R.id.lst_symptoms);
		todayNotesHeading = (TextView) view.findViewById(R.id.notes_heading);
		moodHeading = (TextView) view.findViewById(R.id.heading1);
		symptomHeading = (TextView) view.findViewById(R.id.heading2);
		TimeLineModel lineModel = null;
		CalendarDBHandler calendarDBHandler = new CalendarDBHandler(getActivity());
		helpbutton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(getActivity(), HomeScreenHelp.class);
				intent.putExtra("classname", "partner_today");
				startActivity(intent);
			}
		});
		if (APP.GLOBAL().getTimeLineMaps().containsKey(Utility.setHourMinuteSecondZero(new Date()).getTime()))
		{
			lineModel = APP.GLOBAL().getTimeLineMaps().get(Utility.setHourMinuteSecondZero(new Date()).getTime());
			moodInterfaces = calendarDBHandler.getMoodListforMoodId(lineModel.moodSelectedsMap);
			symInterfaces = calendarDBHandler.getSymtomListforSymptomID(lineModel.symtomsSelectedModelsMap);
			moodAdapter = new PartnerMoodandSymtomListAdaptor(moodInterfaces, lineModel.moodSelectedsMap, PeriodTrackerConstants.MOOD_BASE_FRAGMENT, getActivity());
			symptomAdapter = new PartnerMoodandSymtomListAdaptor(symInterfaces, lineModel.symtomsSelectedModelsMap, PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT, getActivity());
			lstMoods.setAdapter(moodAdapter);
			lstSym.setAdapter(symptomAdapter);
		}
		if (symInterfaces == null || symInterfaces.size() <= 0)
		{
			symptomHeading.setVisibility(View.GONE);
			lstSym.setVisibility(View.GONE);
		}
		else
		{
			symptomHeading.setVisibility(View.VISIBLE);
			lstSym.setVisibility(View.VISIBLE);
		}
		if (moodInterfaces == null || moodInterfaces.size() <= 0)
		{
			moodHeading.setVisibility(View.GONE);
			lstMoods.setVisibility(View.GONE);
		}
		else
		{
			moodHeading.setVisibility(View.VISIBLE);
			lstMoods.setVisibility(View.VISIBLE);
		}
		if (lineModel != null && lineModel.note != null && !lineModel.note.equals(""))
		{
			todayNotes.setText(lineModel.note);
			todayNotes.setVisibility(View.VISIBLE);
			todayNotesHeading.setVisibility(View.VISIBLE);
		}
		else
		{
			todayNotesHeading.setVisibility(View.GONE);
			todayNotes.setVisibility(View.GONE);
		}
		
		periodTrackerModes = APP.GLOBAL().getPartnerPLModels();
		dashBoardDBHandler = new DashBoardDBHandler(getActivity());
		dateFormat = new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		if (periodTrackerModes.size() > 0)
		{
			PeriodLogModel periodLogModel = periodTrackerModes.get(0);
			if (PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
			{
				if (periodLogModel != null)
				{
					if (PeriodTrackerObjectLocator.getInstance().getEstimatedDeliveryDate().getTime() == PeriodTrackerConstants.NULL_DATE)
					{
						if (!periodLogModel.isPregnancy()) setAboutText(periodLogModel);
						else
						{
							PeriodLogModel previousPeriodlog = periodTrackerModes.get(1);
							if (null != previousPeriodlog && null != previousPeriodlog.getStartDate()) setAboutText(previousPeriodlog);
						}
					}
					else
					{
						if (PeriodTrackerObjectLocator.getInstance().PregnancyMessageFormat().equals(getString(R.string.daystobaby))) t1.setText(Utility.dueDaysInPregnancyWhenKnownEstimateddate((PeriodTrackerObjectLocator.getInstance().getEstimatedDeliveryDate()), new Date()) + " "
								+ getString(R.string.daysLeftinbirth));
						else t1.setText(Utility.getdaysfromstartofPregnancyIntheformWeaks(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + getString(R.string.sincepregnancy));
						
					}
				}
				t2.setText("");
				t22.setText("");
				t3.setText(getString(R.string.congratulations));
				t33.setText(getString(R.string.youarepregnant));
			}
			else whichdaysIsToday();
		}
		else
		{
			t1.setText("");
			t2.setText("");
			t22.setText("");
			t3.setText("");
			t33.setText("");
			//t3.setText(getString(R.string.welcome) + "\n" + getString(R.string.pleasetapperiodlogbutton));
			
		}
		return view;
	}
	

	
	public void whichdaysIsToday()
	{
		latestLogModel = periodTrackerModes.get(0);
		if (periodTrackerModes.size() > 0 && !periodTrackerModes.get(0).isPregnancy())
		{
			for (int i = 0; i < periodTrackerModes.size(); i++)
			{
				if (i != 0)
				{
					periodlength = (int) ((((float) (periodTrackerModes.get(i - 1).getPeriodLength() + periodTrackerModes.get(i - 1).getPeriodLength())) / 2.0) + .5);
					if (i == 1)
					{
						cyclelength = (int) ((periodTrackerModes.get(i - 1).getStartDate().getTime() - periodTrackerModes.get(i).getStartDate().getTime()) / (24 * 60 * 60 * 1000));
					}
					else cyclelength = (int) ((cyclelength + (periodTrackerModes.get(i - 1).getStartDate().getTime() - periodTrackerModes.get(i).getStartDate().getTime()) / (24 * 60 * 60 * 1000)) / 2);
					if (periodTrackerModes.get(i).getStartDate().compareTo(periodTrackerModes.get(0).getStartDate()) > 0)
					{
						latestLogModel = periodTrackerModes.get(i - 1);
					}
				}
			}
			PeriodTrackerModelInterface perdictionPeriodLog = dashBoardDBHandler.getPredictionLogs(latestLogModel, cyclelength, periodlength);
			PeriodTrackerModelInterface perdictionFertileRecord = dashBoardDBHandler.getPerdictionFertileDatesAndOvulationDates(latestLogModel, cyclelength, periodlength);
			periodLogModelPerdiction = (PeriodLogModel) perdictionPeriodLog;
			t3.setText(getString(R.string.nextperiod));
			t2.setText(getString(R.string.nextFertiledays));
			t33.setText(dateFormat.format(periodLogModelPerdiction.getStartDate()));
			t22.setText(dateFormat.format(((PeriodLogModel) perdictionFertileRecord).getFertileStartDate()));
			
			periodLogModel = (PeriodLogModel) dashBoardDBHandler.getPastFertileAndOvulationDates(latestLogModel, 14);
			printTextMessage(periodLogModel, periodLogModelPerdiction, Utility.calculateDayofperiod(periodLogModel.getStartDate()));
			
		}
		else
		{
			if (periodTrackerModes.size() == 0)
			{
				t1.setText("");
				t2.setText("");
				t22.setText("");
				t3.setText("");
				t33.setText("");
				//t3.setText(getString(R.string.welcome) + "\n" + getString(R.string.pleasetapperiodlogbutton));
			}
			else
			{
				
				t1.setText("");
				t2.setText("");
				t3.setText("");
				t22.setText("");
				t33.setText("");
			}
		}
		
	}
	
	
	
	public void printTextMessage(PeriodLogModel periodLogModel, PeriodLogModel periodLogModelPerdiction, int days)
	{
		
		if (Utility.setHourMinuteSecondZero(new Date()).before(periodLogModel.getFertileStartDate()))
		{
			t2.setText(getString(R.string.nextFertiledays));
			t22.setText(dateFormat.format(periodLogModel.getFertileStartDate()));
			dayLeftInNextPeriod(false);
		}
		else if (Utility.setHourMinuteSecondZero(periodLogModel.getOvulationDate()).equals(Utility.setHourMinuteSecondZero(new Date())))
		{
			t2.setText(getString(R.string.ovulation_day));
			isOvulation = true;
			dayLeftInNextPeriod(false);
		}
		else if (!Utility.checkDateBetweenDates(periodLogModel.getFertileStartDate(), periodLogModel.getFertileEndDate(), Utility.setHourMinuteSecondZero(new Date())))
		{
			isFertile = true;
			t2.setText(getString(R.string.fertileday));
			dayLeftInNextPeriod(false);
		}
		else if (days > 7 && days < PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength())
		{
			dayLeftInNextPeriod(isReagularDay);
		}
		else if (days > PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength())
		{
			lateDayMessage();
		}
		
		if (Utility.setHourMinuteSecondZero(periodLogModel.getStartDate()).after(Utility.setHourMinuteSecondZero(new Date())))
		{
			t1.setText(Utility.leftdaysInPeriod(new Date(), periodLogModel.getStartDate()) + " " + getString(R.string.leftdays));
		}
		else
		{
			if (days < 7)
			{
				if (days <= periodlength)
				{
					addSuffixOnPeriodDayMessage(days);
				}
				else
				{
					if (days <= 7 && days > periodlength)
					{
						isReagularDay = true;
						dayLeftInNextPeriod(isReagularDay);
					}
				}
			}
			else if (days == cyclelength)
			{
				
				dayLeftInNextPeriod(false);
				
			}
			
			else
			{
				if (days <= periodlength)
				{
					isReagularDay = true;
					dayLeftInNextPeriod(isReagularDay);
				}
			}
		}
	}
	
	public void dayLeftInNextPeriod(boolean isRegularDay)
	{
		int days = Utility.dueDaysInNextPeriod(periodLogModelPerdiction.getStartDate(), Utility.setHourMinuteSecondZero(new Date()));
		
		int d = Integer.valueOf(PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength());
		if (days == d)
		{
			days = d;
		}
		
		switch (days)
		{
		
			case 1:
				if (isRegularDay)
				{
					t1.setText(days + " " + getString(R.string.dayleftinnextperiod));
					t2.setText(getString(R.string.regularday));
				}
				else t1.setText(days + " " + getString(R.string.daysleftinnextperiod));
				break;
			default:
				if (isRegularDay)
				{
					if (days != d)
					{
						t1.setText(days + " " + getString(R.string.daysleftinnextperiod));
					}
					else
					{
						t1.setText("1" + Html.fromHtml("<sub><small>" + getString(R.string.stdayofperiod) + "</small></sub>") + " " + getString(R.string.dayofperiod));
						
					}
					t2.setText(getString(R.string.regularday));
				}
				else
				{
					if (days != d)
					{
						t1.setText(days + " " + getString(R.string.daysleftinnextperiod));
					}
					else
					{
						t1.setText("1" + Html.fromHtml("<sub><small>" + getString(R.string.stdayofperiod) + "</small></sub>") + " " + getString(R.string.dayofperiod));
						
					}
				}
				break;
		}
	}
	
	public void addSuffixOnPeriodDayMessage(int days)
	{
		
		switch (days)
		{
			case 1:
				t1.setText("" + days + Html.fromHtml("<sub><small>" + getString(R.string.stdayofperiod) + "</small></sub>") + " " + getString(R.string.dayofperiod));
				break;
			
			case 2:
				t1.setText("" + days + Html.fromHtml("<sub><small>" + getString(R.string.nddayofperiod) + "</small></sub>") + " " + getString(R.string.dayofperiod));
				break;
			
			case 3:
				t1.setText("" + days + Html.fromHtml("<sub><small>" + getString(R.string.rddayofperiod) + "</small></sub>") + " " + getString(R.string.dayofperiod));
				break;
			
			default:
				t1.setText("" + days + Html.fromHtml("<sub><small>" + getString(R.string.thdayofperiod) + "</small></sub>") + " " + getString(R.string.dayofperiod));
				break;
		}
		
	}
	
	public void lateDayMessage()
	{
		int days = Utility.lateDaysInNextPeriod(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date()));
		switch (days)
		{
			case 0:
				t1.setText(getString(R.string.periodstarttoday));
				
				break;
			case 1:
				t1.setText(days + " " + getString(R.string.daylate));
				break;
			default:
				t1.setText(days + " " + getString(R.string.dayslate));
				break;
		}
		
	}
	
	private void setAboutText(PeriodLogModel periodLogModel)
	{
		if (PeriodTrackerObjectLocator.getInstance().PregnancyMessageFormat().equals(getString(R.string.daystobaby)))
		{
			if (Utility.dueDaysInPregnancy(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) > 0)
			{
				
				t1.setText(Utility.dueDaysInPregnancy(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + "\n" + getString(R.string.daysLeftinbirth));
			}
			else
			{
				t1.setText(Utility.lateDaysInPregnancy(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + getString(R.string.daysLateinbirth));
			}
		}
		else
		{
			t1.setText(Utility.getdaysfromstartofPregnancyIntheformWeaks(periodLogModel.getStartDate(), Utility.setHourMinuteSecondZero(new Date())) + " " + getString(R.string.sincepregnancy));
			
		}
	}
}
