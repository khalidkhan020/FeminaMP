package com.linchpin.periodtracker.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.TimeLineAdapter;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.TimeLineModel;
import com.linchpin.periodtracker.partnersharing.PartnerTimeLineAdapter;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodttracker.database.DBManager;
import com.linchpin.periodttracker.database.DBProjections;
import com.linchpin.periodttracker.database.DashBoardDBHandler;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;

public class TimeLineFragment extends Fragment
{
	private PeriodLogDBHandler					periodLogDBHandler;
	List<TimeLineModel>							time_line_model_list	= null;
	
	/*ListView							timeLineList;
	TimeLineAdapter						adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.time_line, container, false);
	//		Tips.viewVisible(getActivity(), APP.TipsPath.TimeLine.id);		
			
		timeLineList = (ListView) view.findViewById(R.id.lst_time_line);
		timeLineList.setAdapter(adapter);		
		return view;
	}
	*/
	
	ListView									timeLineList;
	TimeLineAdapter								adapter;
	private TextView							t1, t33, t22, t2, t3;
	boolean										isFertile				= false, isOvulation = false, isReagularDay = false;
	private String								Days[]					=
																		{
			"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Satureday"
																		};
	PeriodLogModel								latestLogModel;
	int											periodlength			= 4;
	int											cyclelength				= 28;
	SimpleDateFormat							dateFormat;
	PeriodLogModel								periodLogModel;
	PeriodLogModel								periodLogModelPerdiction;
	private TextView							e_p_day, e_f_day;
	private List<PeriodTrackerModelInterface>	periodTrackerModes;
	DashBoardDBHandler							dashBoardDBHandler;
	Theme										t;
	
	private void applyTheme(View view)
	{
		t = Theme.getCurrentTheme(getActivity(), view);
		if (t != null)
		{
			t.applyBackgroundDrawable(R.id.toplayout, "timeline_top_img");
			t.applyTextColor(R.id.about_day, "text_color");
		}
	}
	
	public ArrayList<TimeLineModel> getTimeLineModel()
	{
		String SQL = "SELECT * FROM " + DBManager.TABLE_PERIOD_DAY_DETAILS + " WHERE " + DBProjections.PT_PROFILE_ID + "=" + PeriodTrackerObjectLocator.getInstance().getProfileId() + " order by " + DBProjections.DD_DATE + " DESC";
		Cursor cursor = null;
		
		try
		{
			time_line_model_list = new ArrayList<TimeLineModel>();
			periodLogDBHandler.openPeriodTrackerDB();
			cursor = periodLogDBHandler.queryOnDb(SQL);
			int count = cursor.getCount();
			if (count > 0)
			{
				cursor.moveToFirst();
				do
				{
					time_line_model_list.add(new TimeLineModel(getActivity()).createModel(cursor));
					
				}
				while (cursor.moveToNext());
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		ArrayList<PeriodTrackerModelInterface> period = (ArrayList<PeriodTrackerModelInterface>) periodLogDBHandler.getPastFertileAndOvulationDates();//AllLogs(PeriodTrackerObjectLocator.getInstance().getProfileId());
		PeriodLogModel logModel;
		for (PeriodTrackerModelInterface modl : period)
		{
			logModel = (PeriodLogModel) modl;
			if (!logModel.isPregnancy() && logModel.getEndDate() != null && logModel.getEndDate().getTime() > (new Date("1/1/1990")).getTime())
			{
				TimeLineModel lineModel = new TimeLineModel();
				lineModel.date = logModel.getStartDate().getTime();
				lineModel.period_start = true;
				lineModel.period = true;
				lineModel.pregnancy = logModel.isPregnancy();
				//Date ld=new Date(lineModel.date);
				
				time_line_model_list.add(lineModel);
				lineModel = new TimeLineModel();
				lineModel.date = logModel.getEndDate().getTime();
				lineModel.period_end = true;
				lineModel.period = true;
				lineModel.pregnancy = logModel.isPregnancy();
				time_line_model_list.add(lineModel);
				
				lineModel = new TimeLineModel();
				if (logModel.getOvulationDate() != null) lineModel.date = logModel.getOvulationDate().getTime();
				lineModel.pregnancy = logModel.isPregnancy();
				lineModel.ovulaton = true;
				time_line_model_list.add(lineModel);
				//			if(!lineModel.ovulaton)
				//			{
				
				lineModel = new TimeLineModel();
				if (logModel.getFertileStartDate() != null) lineModel.date = logModel.getFertileStartDate().getTime();
				lineModel.pregnancy = logModel.isPregnancy();
				lineModel.fertility_start = true;
				time_line_model_list.add(lineModel);
				
				lineModel = new TimeLineModel();
				if (logModel.getFertileEndDate() != null) lineModel.date = logModel.getFertileEndDate().getTime();
				lineModel.pregnancy = logModel.isPregnancy();
				lineModel.fertility_end = true;
				time_line_model_list.add(lineModel);
				//			}
			}
		}
		//time_line_model_temp = new ArrayList<PeriodTrackerModelInterface>();
		Collections.sort(time_line_model_list, new Comparator<PeriodTrackerModelInterface>()
		{
			@Override
			public int compare(PeriodTrackerModelInterface lhs, PeriodTrackerModelInterface rhs)
			{
				TimeLineModel l = ((TimeLineModel) lhs);
				TimeLineModel r = ((TimeLineModel) rhs);
				int ff = Utility.setHourMinuteSecondZero(new Date(r.date)).compareTo(Utility.setHourMinuteSecondZero(new Date(l.date)));
				/*
					ff=	Utility.setHourMinuteSecondZero(new Date(r.date)).compareTo(Utility.setHourMinuteSecondZero(new Date(l.date)));
				}*/
				return ff;
			}
		});
		int size = time_line_model_list.size();
		for (int i = 0; i < size - 1; i++)
		{
			
			TimeLineModel l = ((TimeLineModel) time_line_model_list.get(i));
			TimeLineModel r = ((TimeLineModel) time_line_model_list.get(i + 1));
			//	Date ld=new Date(l.date);
			//			Date rd=new Date(r.date);
			if (l.date == (PeriodTrackerConstants.NULL_DATE))
			{
				time_line_model_list.remove(i);
				i--;
				break;
			}
			//			
			if (Utility.setHourMinuteSecondZero(new Date(r.date)).compareTo(Utility.setHourMinuteSecondZero(new Date(l.date))) == 0) if (l.id == -1)
			{
				r.fertility_end = l.fertility_end;
				r.fertility_start = l.fertility_start;
				r.period_end = l.period_end;
				r.period_start = l.period_start;
				r.ovulaton = l.ovulaton;
				r.pregnancy = l.pregnancy;
				r.period = l.period;
				time_line_model_list.remove(i);
				size--;
			}
			else if (r.id == -1)
			{
				l.fertility_end = r.fertility_end;
				l.fertility_start = r.fertility_start;
				l.period_end = r.period_end;
				l.period_start = r.period_start;
				l.ovulaton = r.ovulaton;
				l.pregnancy = r.pregnancy;
				l.period = r.period;
				time_line_model_list.remove(i + 1);
				size--;
				
			}
		}
		return (ArrayList<TimeLineModel>) time_line_model_list;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.partner_timeline, container, false);
		t1 = (TextView) view.findViewById(R.id.about_day);
		t3 = (TextView) view.findViewById(R.id.next_period_date);
		t33 = (TextView) view.findViewById(R.id.prediction_period_date);
		t2 = (TextView) view.findViewById(R.id.next_fertile_Date);
		t22 = (TextView) view.findViewById(R.id.prediction_fertile_date);
		e_p_day = (TextView) view.findViewById(R.id.next_period_day_deatails);
		e_f_day = (TextView) view.findViewById(R.id.prediction_period_day_deatails);
		//d_o_period_day=(TextView)view.findViewById(R.id.prediction_period_day_deatails);
		timeLineList = (ListView) view.findViewById(R.id.partner_main_list);
		applyTheme(view);
		periodLogDBHandler = new PeriodLogDBHandler(getActivity());
		dashBoardDBHandler = new DashBoardDBHandler(getActivity());
		periodTrackerModes = dashBoardDBHandler.getAllLogs();
		
		dateFormat = new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		if (periodTrackerModes.size() > 0)
		{
			PeriodLogModel periodLogModel = (PeriodLogModel) periodTrackerModes.get(0);
			if (PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
			{
				if (periodLogModel != null)
				{
					if (PeriodTrackerObjectLocator.getInstance().getEstimatedDeliveryDate().getTime() == PeriodTrackerConstants.NULL_DATE)
					{
						if (!periodLogModel.isPregnancy()) setAboutText(periodLogModel);
						else
						{
							PeriodLogModel previousPeriodlog = (PeriodLogModel) periodTrackerModes.get(1);
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
				e_f_day.setText("");
				e_p_day.setText("");
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
			e_f_day.setText("");
			e_p_day.setText("");
			//t3.setText(getString(R.string.welcome) + "\n" + getString(R.string.pleasetapperiodlogbutton));
			
		}
		//view.findViewById(R.id.settingsback).setVisibility(View.GONE);
		//	timeLineList.setAdapter(adapter);
		return view;
	}
	
	public void printTextMessage(PeriodLogModel periodLogModel, PeriodLogModel periodLogModelPerdiction, int days)
	{
		
		if (Utility.setHourMinuteSecondZero(new Date()).before(periodLogModel.getFertileStartDate()))
		{
			t2.setText(getString(R.string.nextFertiledays));
			t22.setText(dateFormat.format(periodLogModel.getFertileStartDate()));
			e_f_day.setText(Days[periodLogModel.getFertileStartDate().getDay()]);
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
						t1.setText(days + "\n" + getString(R.string.daysleftinnextperiod));
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
				t1.setText("" + days + Html.fromHtml("<sub><small>" + getString(R.string.stdayofperiod) + "</small></sub>") + "\n " + getString(R.string.dayofperiod));
				break;
			
			case 2:
				t1.setText("" + days + Html.fromHtml("<sub><small>" + getString(R.string.nddayofperiod) + "</small></sub>") + "\n " + getString(R.string.dayofperiod));
				break;
			
			case 3:
				t1.setText("" + days + Html.fromHtml("<sub><small>" + getString(R.string.rddayofperiod) + "</small></sub>") + "\n " + getString(R.string.dayofperiod));
				break;
			
			default:
				t1.setText("" + days + Html.fromHtml("<sub><small>" + getString(R.string.thdayofperiod) + "</small></sub>") + "\n " + getString(R.string.dayofperiod));
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
	
	public void whichdaysIsToday()
	{
		latestLogModel = (PeriodLogModel) periodTrackerModes.get(0);
		if (periodTrackerModes.size() > 0 && !latestLogModel.isPregnancy())
		{
			for (int i = 0; i < periodTrackerModes.size(); i++)
			{
				if (i != 0)
				{
					periodlength = (int) ((((float) (((PeriodLogModel) periodTrackerModes.get(i - 1)).getPeriodLength() + ((PeriodLogModel) periodTrackerModes.get(i - 1)).getPeriodLength())) / 2.0) + .5);
					if (i == 1)
					{
						cyclelength = (int) ((((PeriodLogModel) periodTrackerModes.get(i - 1)).getStartDate().getTime() - ((PeriodLogModel) periodTrackerModes.get(i)).getStartDate().getTime()) / (24 * 60 * 60 * 1000));
					}
					else cyclelength = (int) ((cyclelength + (((PeriodLogModel) periodTrackerModes.get(i - 1)).getStartDate().getTime() - ((PeriodLogModel) periodTrackerModes.get(i)).getStartDate().getTime()) / (24 * 60 * 60 * 1000)) / 2);
					if (((PeriodLogModel) periodTrackerModes.get(i)).getStartDate().compareTo(((PeriodLogModel) periodTrackerModes.get(0)).getStartDate()) > 0)
					{
						latestLogModel = (PeriodLogModel) periodTrackerModes.get(i - 1);
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
			e_p_day.setText(Days[((PeriodLogModel) perdictionFertileRecord).getFertileStartDate().getDay()]);
			e_f_day.setText(Days[((PeriodLogModel) perdictionFertileRecord).getFertileStartDate().getDay()]);
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
				e_f_day.setText("");
				e_p_day.setText("");
				//t3.setText(getString(R.string.welcome) + "\n" + getString(R.string.pleasetapperiodlogbutton));
			}
			else
			{
				
				t1.setText("");
				t2.setText("");
				t3.setText("");
				t22.setText("");
				t33.setText("");
				e_f_day.setText("");
				e_p_day.setText("");
			}
		}
		
	}
	
	@Override
	public void onResume()
	{
		time_line_model_list = getTimeLineModel();
		adapter = new TimeLineAdapter(time_line_model_list, getActivity(), "time_line");
		adapter.notifyDataSetChanged();
		adapter.notifyDataSetInvalidated();
		timeLineList.setAdapter(adapter);
		timeLineList.invalidate();
		
		super.onResume();
	}
	
}
