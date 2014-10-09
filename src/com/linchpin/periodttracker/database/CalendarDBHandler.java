package com.linchpin.periodttracker.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.linchpin.periodtracker.model.CalendarDayDetailModel;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.MoodDataModel;
import com.linchpin.periodtracker.model.MoodSelected;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.Pills;
import com.linchpin.periodtracker.model.SymptomsModel;
import com.linchpin.periodtracker.model.SymtomsSelectedModel;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.APP.PARTNER;
import com.linchpin.periodtracker.utlity.ConstantsKey;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;

public class CalendarDBHandler extends PeriodTrackerDBHandler
{
	
	PeriodTrackerModelInterface			interfaceModelInterface;
	DayDetailModel						dayDetailModel;
	Pills								pills;
	List<PeriodTrackerModelInterface>	interfacesForPeriod, interfacesForNotes, interfaceFertileAndOvualtion, pregnancyList;
	SimpleDateFormat					dateFormat	= new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
	SimpleDateFormat					format		= new SimpleDateFormat("yyyyMMdd");
	
	public CalendarDBHandler(Context context)
	{
		super(context);
	}
	
	public List<CalendarDayDetailModel> getPartnerDetailForDayInCalendor(Date start, Date end)
	{
		List<CalendarDayDetailModel> calendarDayDetailModels = new ArrayList<CalendarDayDetailModel>();
		try
		{
			
			Set<String> periodSet = new HashSet<String>();
			Set<String> ovulationSet = new HashSet<String>();
			Set<String> fertileSet = new HashSet<String>();
			Set<String> noteSet = new HashSet<String>();
			Set<String> intimateSet = new HashSet<String>();
			
			start = Utility.setHourMinuteSecondZero(start);
			end = Utility.setHourMinuteSecondZero(end);
			List<DayDetailModel> detailModels =new ArrayList<DayDetailModel>();
			detailModels.addAll(APP.GLOBAL().getPartnerDDModels());
			List<PeriodLogModel> periodLogModels =new ArrayList<PeriodLogModel>();
			periodLogModels.addAll(APP.GLOBAL().getPartnerPLModels());
			
			Calendar startCalendar = new GregorianCalendar();
			PeriodLogModel latestLogModel = null;
			if(periodLogModels.size()>0)latestLogModel = periodLogModels.get(0);
			long cyclelength = APP.GLOBAL().getPreferences().getInt(PARTNER.FIELDS.CYCLE_LENGTH.key, 28);
			int periodlength = APP.GLOBAL().getPreferences().getInt(PARTNER.FIELDS.PERIOD_LENGTH.key, 4);
			
			
			for (int i = 0; i < periodLogModels.size(); i++)
			{
				if (i != 0)
				{
//					periodlength = (int) ((((float) (periodLogModels.get(i - 1).getPeriodLength() + periodLogModels.get(i - 1).getPeriodLength())) / 2.0) + .5);
//					if (i == 1)
//					{
//						cyclelength = (periodLogModels.get(i - 1).getStartDate().getTime() - periodLogModels.get(i).getStartDate().getTime()) / (24 * 60 * 60 * 1000);
//					}
//					else cyclelength = (cyclelength + (periodLogModels.get(i - 1).getStartDate().getTime() - periodLogModels.get(i).getStartDate().getTime()) / (24 * 60 * 60 * 1000)) / 2;
					if (periodLogModels.get(i).getStartDate().compareTo(latestLogModel.getStartDate()) > 0)
					{
						latestLogModel = periodLogModels.get(i - 1);
					}
				}
			}
			if (latestLogModel != null && null != latestLogModel.getStartDate()) startCalendar.setTime(latestLogModel.getStartDate());
			else startCalendar.setTime(new Date());
			
			Calendar endCalendar = new GregorianCalendar();
			endCalendar.setTime(end);
			
			int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
			int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
			
			if (null != latestLogModel && null != latestLogModel.getStartDate()) periodLogModels.addAll((Collection<? extends PeriodLogModel>) getPerdictionFertileDatesAndOvulationDates(latestLogModel, start, diffMonth, cyclelength, periodlength));
			
			for (DayDetailModel ddModel : detailModels)
			{
				/*if (ddModel.getNote()!=null&&!ddModel.getNote().toString().trim().equals(""))*/ noteSet.add(format.format(ddModel.getDate()));
				if (ddModel.isIntimate()) intimateSet.add(format.format(ddModel.getDate()));
				
			}
			
			for (PeriodLogModel ppModel : periodLogModels)
			{
				Date pStartDate = ppModel.getStartDate();
				Date pEndDate = ppModel.getEndDate();
				if((pEndDate.compareTo(new Date(2000, 1, 1)) <= 0))
					ppModel.setEndDate(Utility.addDays(pStartDate, periodlength-1));
				ppModel.setFertileStartDate(Utility.addDays(pStartDate, APP.GLOBAL().getPreferences().getInt(PARTNER.FIELDS.OVULATION_LENGTH.key, 14)-6));
				ppModel.setFertileEndDate(Utility.addDays(pStartDate, APP.GLOBAL().getPreferences().getInt(PARTNER.FIELDS.OVULATION_LENGTH.key, 14)+5));
				ppModel.setOvulationDate(Utility.addDays(pStartDate, APP.GLOBAL().getPreferences().getInt(PARTNER.FIELDS.OVULATION_LENGTH.key, 14)));
				if (!ppModel.isPregnancy())
				{
					for (Date loopDate = pStartDate; loopDate.compareTo(pEndDate) <= 0; loopDate = Utility.addDays(loopDate, 1))
					{
						periodSet.add(format.format(loopDate));
					}
					
					if (null != ppModel.getFertileStartDate())
					{
						pStartDate = ppModel.getFertileStartDate();
						pEndDate = ppModel.getFertileEndDate();
						for (Date loopDate = pStartDate; loopDate.compareTo(pEndDate) <= 0; loopDate = Utility.addDays(loopDate, 1))
						{
							if (!ppModel.isPregnancy()) fertileSet.add(format.format(loopDate));
						}
						if (!ppModel.isPregnancy())
						{
							ovulationSet.add(format.format(ppModel.getOvulationDate()));
						}
					}
				}
				
			}
			CalendarDayDetailModel calendarDayDetailModel;
			for (Date date = start; date.compareTo(end) <= 0; date = Utility.addDays(date, 1))
			{
				calendarDayDetailModel = new CalendarDayDetailModel(date, false, false, false, false, false, false);
				
				checkNoteIntimate(calendarDayDetailModel, date, noteSet, intimateSet);
				
				checkPeriodLogTableForCurrentAndPastDay(calendarDayDetailModel, date, periodSet, ovulationSet, fertileSet);
				
				calendarDayDetailModels.add(calendarDayDetailModel);
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		
		return calendarDayDetailModels;
		
	}
	
	public List<CalendarDayDetailModel> getDetailForDayInCalendor(Date start, Date end)
	{
		List<CalendarDayDetailModel> calendarDayDetailModels = new ArrayList<CalendarDayDetailModel>();
		
		Set<String> periodSet = new HashSet<String>();
		Set<String> ovulationSet = new HashSet<String>();
		Set<String> fertileSet = new HashSet<String>();
		Set<String> noteSet = new HashSet<String>();
		Set<String> intimateSet = new HashSet<String>();
		
		start = Utility.setHourMinuteSecondZero(start);
		end = Utility.setHourMinuteSecondZero(end);
		interfacesForPeriod = getLogsForMonth(start, end);
		interfacesForNotes = getDayDetailForAllDates(start, end);
		
		Calendar startCalendar = new GregorianCalendar();
		PeriodLogModel latestLogModel = (PeriodLogModel) getLatestLog();
		if (latestLogModel != null && null != latestLogModel.getStartDate()) startCalendar.setTime(latestLogModel.getStartDate());
		else startCalendar.setTime(new Date());
		
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(end);
		
		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		
		if (null != latestLogModel && null != latestLogModel.getStartDate()) interfacesForPeriod.addAll(getPerdictionFertileDatesAndOvulationDates(latestLogModel, start, diffMonth));
		
		for (PeriodTrackerModelInterface modelInterface : interfacesForNotes)
		{
			dayDetailModel = ((DayDetailModel) modelInterface);
			if (!dayDetailModel.getNote().toString().trim().equals("")) noteSet.add(format.format(dayDetailModel.getDate()));
			if (dayDetailModel.isIntimate()) intimateSet.add(format.format(dayDetailModel.getDate()));
			
		}
		
		for (PeriodTrackerModelInterface element : interfacesForPeriod)
		{
			Date pStartDate = ((PeriodLogModel) element).getStartDate();
			Date pEndDate = ((PeriodLogModel) element).getEndDate();
			
			if (!((PeriodLogModel) element).isPregnancy())
			{
				for (Date loopDate = pStartDate; loopDate.compareTo(pEndDate) <= 0; loopDate = Utility.addDays(loopDate, 1))
				{
					periodSet.add(format.format(loopDate));
				}
				
				if (null != ((PeriodLogModel) element).getFertileStartDate())
				{
					pStartDate = ((PeriodLogModel) element).getFertileStartDate();
					pEndDate = ((PeriodLogModel) element).getFertileEndDate();
					for (Date loopDate = pStartDate; loopDate.compareTo(pEndDate) <= 0; loopDate = Utility.addDays(loopDate, 1))
					{
						if (!((PeriodLogModel) element).isPregnancy()) fertileSet.add(format.format(loopDate));
					}
					if (!((PeriodLogModel) element).isPregnancy())
					{
						ovulationSet.add(format.format(((PeriodLogModel) element).getOvulationDate()));
					}
				}
			}
			
		}
		CalendarDayDetailModel calendarDayDetailModel;
		for (Date date = start; date.compareTo(end) <= 0; date = Utility.addDays(date, 1))
		{
			calendarDayDetailModel = new CalendarDayDetailModel(date, false, false, false, false, false, false);
			
			checkNoteIntimate(calendarDayDetailModel, date, noteSet, intimateSet);
			
			checkPeriodLogTableForCurrentAndPastDay(calendarDayDetailModel, date, periodSet, ovulationSet, fertileSet);
			
			calendarDayDetailModels.add(calendarDayDetailModel);
		}
		
		return calendarDayDetailModels;
		
	}
	
	private boolean checkPeriodLogTableForCurrentAndPastDay(CalendarDayDetailModel calendarDayDetailModel, Date date, Set<String> periodSet, Set<String> ovulationSet, Set<String> fertileSet)
	{
		
		boolean result = false;
		
		if (periodSet.contains(format.format(date)))
		{
			calendarDayDetailModel.setPeriodDay(true);
			result = true;
			
		}
		else
		{
			if (ovulationSet.contains(format.format(date)))
			{
				calendarDayDetailModel.setOvulationDay(true);
				calendarDayDetailModel.setFertileDay(false);
				result = true;
			}
			else if (fertileSet.contains(format.format(date)))
			{
				calendarDayDetailModel.setFertileDay(true);
				result = true;
			}
			
		}
		
		return result;
		
	}
	
	public void checkNoteIntimate(CalendarDayDetailModel calendarDayDetailModel, Date date, Set<String> noteSet, Set<String> intimateSet)
	{
		
		if (noteSet.contains(format.format(date)))
		{
			calendarDayDetailModel.setHavingNotes(true);
			
		}
		if (intimateSet.contains(format.format(date)))
		{
			calendarDayDetailModel.setIntimate(true);
		}
		
	}
	
	public List<PeriodTrackerModelInterface> getAllLogs()
	{
		
		String selectSQL = "select * from Period_Track order by Start_Date DESC";
		return this.selectMulipleRecord(PeriodLogModel.class, selectSQL);
		
	}
	
	public List<PeriodTrackerModelInterface> getLogsForMonth(Date startDate, Date endDate)
	{
		PeriodLogModel periodLogModel;
		PeriodLogModel previousPeriodLogModel = null;
		Date ovulationDate;
		int ovulationDay;
		
		List<PeriodTrackerModelInterface> periodTrackerModelInterfaces;
		
		String Sql = (" Select * From " + PeriodLogModel.PERIOD_TRACK + " Where " + PeriodLogModel.PERIOD_TRACK_START_DATE + " >=" + startDate.getTime() + " and " + PeriodLogModel.PERIOD_TRACK_START_DATE + "<=" + endDate.getTime()) + " UNION "
				+ (" Select * From " + PeriodLogModel.PERIOD_TRACK + " Where " + PeriodLogModel.PERIOD_TRACK_END_DATE + " >= " + startDate.getTime() + " and " + PeriodLogModel.PERIOD_TRACK_END_DATE + "<=" + endDate.getTime());
		
		periodTrackerModelInterfaces = (List<PeriodTrackerModelInterface>) this.selectMulipleRecord(PeriodLogModel.class, Sql);
		
		for (PeriodTrackerModelInterface modelInterface : periodTrackerModelInterfaces)
		{
			periodLogModel = (PeriodLogModel) modelInterface;
			periodLogModel.setStartDate(Utility.setHourMinuteSecondZero(periodLogModel.getStartDate()));
			if (periodLogModel.getEndDate().getTime() == (PeriodTrackerConstants.NULL_DATE))
			{
				periodLogModel.setEndDate(Utility.addDays(periodLogModel.getStartDate(), PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1));
			}
			else
			{
				periodLogModel.setEndDate(Utility.setHourMinuteSecondZero(periodLogModel.getEndDate()));
			}
			if (periodLogModel.getCycleLength() > 8 || periodLogModel.getCycleLength() == 0)
			{
				
				ovulationDay = periodLogModel.getCycleLength() > 32 || periodLogModel.getCycleLength() < 28 ? PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength() : PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength();
				ovulationDate = new Date(periodLogModel.getStartDate().getTime() + ((ovulationDay - 1) * PeriodTrackerConstants.MILLI_SECONDS));
				periodLogModel.setOvulationDate(ovulationDate);
				/*periodLogModel.setFertileStartDate(Utility.addDays(new Date(periodLogModel.getStartDate().getTime()), (PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_STARTS_DAYS)));
				periodLogModel.setFertileEndDate(Utility.addDays(new Date(periodLogModel.getStartDate().getTime()), (PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_END_DAYS)));
				*/periodLogModel.setFertileStartDate(Utility.addDays(ovulationDate, -6));
				periodLogModel.setFertileEndDate(Utility.addDays(ovulationDate, 5));
			}
			previousPeriodLogModel = periodLogModel;
		}
		return periodTrackerModelInterfaces;
	}
	
	public PeriodTrackerModelInterface getLatestLog()
	{
		String selectSQL = "select * from Period_Track order by Start_Date DESC LIMIT 1";
		return this.selectRecord(PeriodLogModel.class, selectSQL);
	}
	
	public List<PeriodTrackerModelInterface> getPerdictionFertileDatesAndOvulationDates(PeriodLogModel periodLogModel, Date start, int monthDiff)
	{
		int cycleLength = PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength();
		int periodLength = PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength();
		
		return getPerdictionFertileDatesAndOvulationDates(periodLogModel, start, monthDiff, cycleLength, periodLength);
		
	}
	
	public List<PeriodTrackerModelInterface> getPerdictionFertileDatesAndOvulationDates(PeriodLogModel periodLogModel, Date start, int monthDiff, long cycleLength, int periodLength)
	{
		
		//	PeriodLogModel periodLogModel = (PeriodLogModel) getLatestLog();
		List<PeriodTrackerModelInterface> predictionList = new ArrayList<PeriodTrackerModelInterface>();
		
		Date startDate = new Date();
		if(cycleLength<0)
			cycleLength=-cycleLength;
		int ovulationDay;
		Date ovulationDate;
		if (null != periodLogModel.getStartDate()) startDate = periodLogModel.getStartDate();
		
		if (null != periodLogModel && !((PeriodLogModel) periodLogModel).isPregnancy())
		{
			while (true)
			{
				if (startDate.getTime() > new Date().getTime())
				{
					Date checkDateinCurrentCyclLength = Utility.setHourMinuteSecondZero(new Date(startDate.getTime() - (PeriodTrackerConstants.MILLI_SECONDS * cycleLength)));
					if (periodLogModel.getStartDate() != null)
					{
						
						if (!periodLogModel.getStartDate().equals(checkDateinCurrentCyclLength) && !periodLogModel.getStartDate().before(checkDateinCurrentCyclLength)) startDate = checkDateinCurrentCyclLength;
					}
					else
					{
						startDate = checkDateinCurrentCyclLength;
					}
					break;
				}
				else
				{
					startDate = new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
				}
			}
		}
		else if (((PeriodLogModel) periodLogModel).isPregnancy())
		{
			if (((PeriodLogModel) periodLogModel).getEndDate().getTime() != (PeriodTrackerConstants.NULL_DATE))
			{
				startDate = ((PeriodLogModel) selectPreviousDateRecordForPregnancy(((PeriodLogModel) periodLogModel).getStartDate())).getStartDate();
				
				while (true)
				{
					if (startDate.getTime() > new Date().getTime())
					{
						Date checkDateinCurrentCyclLength = startDate;
						if (periodLogModel.getStartDate() != null)
						{
							
							if (!periodLogModel.getStartDate().equals(checkDateinCurrentCyclLength) && !periodLogModel.getStartDate().before(checkDateinCurrentCyclLength)) startDate = checkDateinCurrentCyclLength;
						}
						else
						{
							startDate = checkDateinCurrentCyclLength;
						}
						break;
					}
					else
					{
						startDate = new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
					}
				}
				
			}
		}
		
		if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
		{
			
			for (int i = 0; i < monthDiff; i++)
			{
				periodLogModel = new PeriodLogModel();
				periodLogModel.setStartDate(startDate);
				
				periodLogModel.setEndDate(Utility.setHourMinuteSecondZero(new Date(periodLogModel.getStartDate().getTime() + (PeriodTrackerConstants.MILLI_SECONDS * (periodLength - 1)))));
				
				periodLogModel.setCycleLength((int) cycleLength);
				periodLogModel.setPeriodLength(periodLength);
				
				ovulationDay = PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength();
				
				// Fertile Dates
				ovulationDate = new Date(periodLogModel.getStartDate().getTime() + ((ovulationDay - 1) * PeriodTrackerConstants.MILLI_SECONDS));
				periodLogModel.setOvulationDate(Utility.setHourMinuteSecondZero(ovulationDate));
				
				periodLogModel.setFertileStartDate(Utility.addDays(ovulationDate, -6));
				periodLogModel.setFertileEndDate(Utility.addDays(ovulationDate, 5));
				
				predictionList.add(periodLogModel);
				startDate = new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * (cycleLength)));
			}
			
		}
		return predictionList;
		
	}
	
	/*
	 * public List<PeriodTrackerModelInterface>
	 * getPastFertileAndOvulationDates() {
	 * 
	 * List<PeriodTrackerModelInterface> periodTrackerModelInterfaces =
	 * getAllLogs(); Date ovulationDate; int ovulationDay; PeriodLogModel
	 * periodLogModel; PeriodLogModel baseForPregnancy = null; for (int i = 0; i
	 * < periodTrackerModelInterfaces.size(); i++) { periodLogModel =
	 * (PeriodLogModel) periodTrackerModelInterfaces .get(i); if
	 * (!periodLogModel.isPregnancy()) { ovulationDay =
	 * periodLogModel.getCycleLength() > 32 || periodLogModel.getCycleLength() <
	 * 28 ? PeriodTrackerObjectLocator
	 * .getInstance().getCurrentOvualtionLength() :
	 * periodLogModel.getCycleLength() / 2; ovulationDate = new Date(
	 * periodLogModel.getStartDate().getTime() + ((ovulationDay - 1) *
	 * PeriodTrackerConstants.MILLI_SECONDS));
	 * periodLogModel.setOvulationDate(ovulationDate); periodLogModel
	 * .setFertileStartDate(Utility .addDays( new
	 * Date(periodLogModel.getStartDate() .getTime()),
	 * (PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_STARTS_DAYS)));
	 * periodLogModel .setFertileEndDate(Utility .addDays( new
	 * Date(periodLogModel.getStartDate() .getTime()),
	 * (PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_END_DAYS))); } else {
	 * baseForPregnancy = (PeriodLogModel) selectPreviousDateRecordForPregnancy
	 * (periodLogModel.getStartDate()); } if (baseForPregnancy != null) { if
	 * (periodLogModel.getStartDate().equals (baseForPregnancy.getStartDate()))
	 * { periodLogModel.setFertileEndDate(null);
	 * periodLogModel.setFertileStartDate(null);
	 * periodLogModel.setOvulationDate(null); } }
	 * 
	 * 
	 * } return periodTrackerModelInterfaces; }
	 */public PeriodTrackerModelInterface getDayDetailForDateInCalender(Date date)
	{
		
		String SQl = " SELECT * From " + DayDetailModel.DAY_DETAIL + " Where " + DayDetailModel.DAY_DETAIL_DATE + " = " + date.getTime();
		
		String SelcetSQl = "select * from " + DayDetailModel.DAY_DETAIL + " Where " + DayDetailModel.DAY_DETAIL_DATE + " = " + date.getTime();
		
		return this.selectRecord(DayDetailModel.class, SQl);
		
	}
	
	public List<PeriodTrackerModelInterface> getDayDetailForAllDates(Date startDate, Date endDate)
	{
		
		String SQl = " SELECT * From " + DayDetailModel.DAY_DETAIL + " Where " + DayDetailModel.DAY_DETAIL_DATE + " >= " + startDate.getTime() + " AND " + DayDetailModel.DAY_DETAIL_DATE + " <= " + endDate.getTime();
		
		return this.selectMulipleRecord(DayDetailModel.class, SQl);
		
	}
	
	public PeriodTrackerModelInterface getMedicineDetialForDay(int dayDetailId)
	{
		
		String SQl = "SELECT * FROM " + Pills.MEDICINE + " Where " + Pills.MEDICINE_DAY_DETAILS_ID + " = " + dayDetailId;
		
		return this.selectRecord(Pills.class, SQl);
		
	}
	
	public List<PeriodTrackerModelInterface> getMoodListforDayDetailId(int dayDetailId)
	{
		
		String SQL = " Select * FROM " + MoodDataModel.MOOD + " Where " + MoodDataModel.MOOD_ID + " IN " + "(" + " Select " + MoodSelected.MOOD_ID + " From " + MoodSelected.MOOD_SELECETED + " Where " + MoodSelected.DAY_DETAIL_ID + " = " + dayDetailId + " and " + MoodSelected.MOOD_WIEGHT
				+ " NOT IN (0) " + " order by " + MoodSelected.MOOD_WIEGHT + ")";
		
		return this.selectMulipleRecord(MoodDataModel.class, SQL);
		
	}
	
	public List<PeriodTrackerModelInterface> getMoodListforMoodId(List<PeriodTrackerModelInterface> moodSelectedsMap)
	{
		List<PeriodTrackerModelInterface> interfaces = new ArrayList<PeriodTrackerModelInterface>();
		for (PeriodTrackerModelInterface interface1 : moodSelectedsMap)
		{
			String SQL = " Select * FROM " + MoodDataModel.MOOD + " Where " + MoodDataModel.MOOD_ID + " = " + ((MoodSelected) interface1).getMoodId();
			
			interfaces.addAll(this.selectMulipleRecord(MoodDataModel.class, SQL));
		}
		return interfaces;
		
	}
	public PeriodTrackerModelInterface getMoodListforMoodId(int id)
	{
		String SQL = " Select * FROM " + MoodDataModel.MOOD + " Where " + MoodDataModel.MOOD_ID + " = " + id;
		
		return this.selectMulipleRecord(MoodDataModel.class, SQL).get(0);
	
		
	}
	public PeriodTrackerModelInterface getSymtomListforSymId(int id)
	{
		String SQL = " Select * FROM " + SymptomsModel.SYMTOMS + " Where " + SymptomsModel.SYMTOMS_ID + " = " + id;
		
		return this.selectMulipleRecord(SymptomsModel.class, SQL).get(0);

	}
	public List<PeriodTrackerModelInterface> getSymtomListforDayDetailId(int dayDetailId)
	{
		
		String SQL = " Select * FROM " + SymptomsModel.SYMTOMS + " Where " + SymptomsModel.SYMTOMS_ID + " IN " + "(" + " Select " + SymtomsSelectedModel.SYMTOM_SELECTED_SYMTOM_ID + " From " + SymtomsSelectedModel.SYMTOM_SELECTED + " Where " + SymtomsSelectedModel.SYMTOM_SELECTED_DAY_DETAIL_ID
				+ " = " + dayDetailId + " and " + SymtomsSelectedModel.SYMTOM_SELECTED_SYMTOM_WEIGHT + " NOT IN (0) " + " Order by " + SymtomsSelectedModel.SYMTOM_SELECTED_SYMTOM_WEIGHT + ")";
		
		return this.selectMulipleRecord(SymptomsModel.class, SQL);
	}
	
	public List<PeriodTrackerModelInterface> getSymtomListforSymptomID(List<PeriodTrackerModelInterface> simId)
	{
		List<PeriodTrackerModelInterface> interfaces = new ArrayList<PeriodTrackerModelInterface>();
		for (PeriodTrackerModelInterface interface1 : simId)
		{
			String SQL = " Select * FROM " + SymptomsModel.SYMTOMS + " Where " + SymptomsModel.SYMTOMS_ID + " = " + ((SymtomsSelectedModel) interface1).getSymptomId();
			
			interfaces.addAll(this.selectMulipleRecord(SymptomsModel.class, SQL));
		}
		return interfaces;
	}
	
	public PeriodTrackerModelInterface selectPreviousDateRecordForPregnancy(Date date)
	{
		
		String SQL = "select * from Period_Track Where Start_Date < " + date.getTime() + " order by Start_Date DESC LIMIT 1 ";
		
		return this.selectRecord(PeriodLogModel.class, SQL);
		
	}
	
	/*
	 * public List<PeriodTrackerModelInterface> getAllPregnancyLogs() { Boolean
	 * boolean1 = false;
	 * 
	 * String SQL = "select Start_Date from Period_Track Where " +
	 * PeriodLogModel.PERIOD_TRACK_PREGNANCY + " = " + "'" + "1" + "'"; String
	 * nSQL = "select * from Period_Track Where Start_Date <" +
	 * 
	 * PeriodLogModel.PERIOD_TRACK_PREGNANCY + " = " + "'" + "0" + "'";
	 * 
	 * return this.selectMulipleRecord(PeriodLogModel.class, SQL);
	 * 
	 * }
	 */
}