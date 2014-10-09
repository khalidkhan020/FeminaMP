package com.linchpin.periodttracker.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;

public class DashBoardDBHandler extends PeriodTrackerDBHandler
{
	
	Context	context;
	
	public DashBoardDBHandler(Context context)
	{
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
		
	}
	
	public boolean addPeriodLog(final PeriodLogModel periodTrackModel)
	{
		boolean rowCreate = false;
		int periodLength = 0;
		int cycleLength = 0;
		ContentValues createContentValues = null, updateContentValues = null;
		
		try
		{
			
			PeriodLogModel logModellesser = (PeriodLogModel) selectPreviousDateRecord(periodTrackModel.getStartDate());
			PeriodLogModel logModelGreater = (PeriodLogModel) selectNextStartDateRecord(periodTrackModel.getStartDate());
			
			createContentValues = new ContentValues();
			createContentValues.put(PeriodLogModel.PERIOD_TRACK_PROFILE_ID, periodTrackModel.getProfileId());
			createContentValues.put(PeriodLogModel.PERIOD_TRACK_START_DATE, periodTrackModel.getStartDate().getTime());
			
			if (null != periodTrackModel.getEndDate())
			{
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_END_DATE, periodTrackModel.getEndDate().getTime());
				periodLength = Utility.calculatePeriodLength(periodTrackModel.getStartDate(), periodTrackModel.getEndDate());
				
			}
			else
			{
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_END_DATE, PeriodTrackerConstants.NULL_DATE);
			}
			
			createContentValues.put(PeriodLogModel.PERIOD_TRACK_PERIOD_LENGTH, periodLength);
			if (logModelGreater.getStartDate() != null)
			{
				cycleLength = Utility.calculateCycleLength(logModelGreater.getStartDate(), periodTrackModel.getStartDate());
			}
			createContentValues.put(PeriodLogModel.PERIOD_TRACK_CYCLE_LENGTH, cycleLength);
			
			if (logModellesser.getStartDate() != null)
			{
				cycleLength = Utility.calculateCycleLength(periodTrackModel.getStartDate(), logModellesser.getStartDate());
				updateContentValues = new ContentValues();
				updateContentValues.put(periodTrackModel.PERIOD_TRACK_CYCLE_LENGTH, cycleLength);
				// updatePeriodLog(logModellesser);
			}
			
			if (null != createContentValues && null != updateContentValues)
			{
				
				if (this.createAndUpdateRecord(periodTrackModel.PERIOD_TRACK, null, createContentValues, periodTrackModel.PERIOD_TRACK, updateContentValues, periodTrackModel.PERIOD_TRACK_Id + " = " + logModellesser.getId(), null) > 0) rowCreate = true;
			}
			else
			{
				
				if (this.createRecord(periodTrackModel.PERIOD_TRACK, null, createContentValues) > 0) rowCreate = true;
			}
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, true).commit();
			
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return rowCreate;
	}
	
	public PeriodTrackerModelInterface getPredictionLogs()
	{
		PeriodTrackerModelInterface logModel = getLatestLog();
		int cycleLength = PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength();
		int periodLength = PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength();
		
		return getPredictionLogs((PeriodLogModel) logModel, cycleLength, periodLength);
	}
	
	public PeriodTrackerModelInterface getPredictionLogs(PeriodLogModel logModel, int cycleLength, int periodLength)
	{
		PeriodLogModel periodLogModel = null;
		Date startDate = new Date();
		if (null != logModel)
		{
			periodLogModel = (PeriodLogModel) logModel;
			if (null != periodLogModel.getStartDate()) startDate = periodLogModel.getStartDate();
		}
		
		while (true)
		{
			if (startDate.getTime() > new Date().getTime())
			{
				Date checkDateinCurrentCyclLength = new Date(startDate.getTime() - (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
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
		
		periodLogModel = new PeriodLogModel();
		periodLogModel.setStartDate(startDate);
		periodLogModel.setEndDate(new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * periodLength)));
		periodLogModel.setCycleLength(cycleLength);
		periodLogModel.setPeriodLength(periodLength);
		
		return periodLogModel;
	}
	
	public PeriodTrackerModelInterface getPastFertileAndOvulationDates()
	{
		int ovulationDay;
		ovulationDay = PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength();
		
		PeriodTrackerModelInterface periodTrackerModelInterfaces = getLatestLog();
		return getPastFertileAndOvulationDates(periodTrackerModelInterfaces, ovulationDay);
	}
	
	public PeriodTrackerModelInterface getPastFertileAndOvulationDates(PeriodTrackerModelInterface periodTrackerModelInterfaces, int ovulationDay)
	{
		
		Date ovulationDate;
		
		PeriodLogModel periodLogModel;
		
		periodLogModel = (PeriodLogModel) periodTrackerModelInterfaces;
		
		ovulationDate = new Date(periodLogModel.getStartDate().getTime() + ((ovulationDay) * PeriodTrackerConstants.MILLI_SECONDS));
		periodLogModel.setOvulationDate(ovulationDate);
		/*periodLogModel.setFertileStartDate(new Date(periodLogModel
				.getStartDate().getTime()
				+ PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_STARTS_DAYS
				* PeriodTrackerConstants.MILLI_SECONDS));
		periodLogModel.setFertileEndDate(new Date(periodLogModel.getStartDate()
				.getTime()
				+ PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_END_DAYS
				* PeriodTrackerConstants.MILLI_SECONDS));
		*/
		periodLogModel.setFertileStartDate(new Date(periodLogModel.getOvulationDate().getTime() - 6 * PeriodTrackerConstants.MILLI_SECONDS));
		periodLogModel.setFertileEndDate(new Date(periodLogModel.getOvulationDate().getTime() + 5 * PeriodTrackerConstants.MILLI_SECONDS));
		
		return periodTrackerModelInterfaces;
	}
	
	public List<PeriodTrackerModelInterface> getAllPerdictionFertileDatesAndOvulationDates()
	{
		
		PeriodLogModel periodLogModel = null;
		PeriodTrackerModelInterface logModel = getLatestLog();
		List<PeriodTrackerModelInterface> predictionList = new ArrayList<PeriodTrackerModelInterface>();
		
		Date startDate = new Date();
		int cycleLength = PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength();
		int periodLength = PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength();
		
		int ovulationDay;
		Date ovulationDate;
		
		if (null != logModel)
		{
			periodLogModel = (PeriodLogModel) logModel;
			if (null != periodLogModel.getStartDate()) startDate = periodLogModel.getStartDate();
		}
		
		while (true)
		{
			if (startDate.getTime() > new Date().getTime())
			{
				Date checkDateinCurrentCyclLength = new Date(startDate.getTime() - (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
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
		
		for (int i = 0; i < PeriodTrackerConstants.PREDICTION_COUNT; i++)
		{
			periodLogModel = new PeriodLogModel();
			periodLogModel.setStartDate(startDate);
			periodLogModel.setEndDate(new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * periodLength)));
			periodLogModel.setCycleLength(cycleLength);
			periodLogModel.setPeriodLength(periodLength);
			
			ovulationDay = cycleLength / 2;
			
			// Fertile Dates
			ovulationDate = new Date(periodLogModel.getStartDate().getTime() + (ovulationDay * PeriodTrackerConstants.MILLI_SECONDS));
			periodLogModel.setOvulationDate(ovulationDate);
			/*periodLogModel
					.setFertileStartDate(new Date(
							startDate.getTime()
									+ PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_STARTS_DAYS
									* PeriodTrackerConstants.MILLI_SECONDS));
			periodLogModel.setFertileEndDate(new Date(startDate.getTime()
					+ PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_END_DAYS
					* PeriodTrackerConstants.MILLI_SECONDS));*/
			periodLogModel.setFertileStartDate(Utility.addDays(ovulationDate, -6));
			periodLogModel.setFertileEndDate(Utility.addDays(ovulationDate, 5));
			
			predictionList.add(periodLogModel);
			startDate = new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
		}
		
		return predictionList;
		
	}
	
	public PeriodTrackerModelInterface getLatestLog()
	{
		String selectSQL = "select * from Period_Track order by Start_Date DESC LIMIT 1";
		return this.selectRecord(PeriodLogModel.class, selectSQL);
	}
	
	public PeriodTrackerModelInterface getPerdictionFertileDatesAndOvulationDates()
	{
		PeriodTrackerModelInterface logModel = getLatestLog();
		int cycleLength = PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength();
		int periodLength = PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength();
		return getPerdictionFertileDatesAndOvulationDates((PeriodLogModel) logModel, cycleLength, periodLength);
	}
	
	public PeriodTrackerModelInterface getPerdictionFertileDatesAndOvulationDates(PeriodLogModel periodLogModel, int cycleLength, int periodLength)
	{
		
		List<PeriodTrackerModelInterface> predictionList = new ArrayList<PeriodTrackerModelInterface>();
		
		Date startDate = Utility.setHourMinuteSecondZero(new Date());
		
		int ovulationDay;
		Date ovulationDate;
		
		if (null != periodLogModel && !periodLogModel.isPregnancy())
		{
			if (null != periodLogModel.getStartDate()) startDate = periodLogModel.getStartDate();
		}
		if (null != periodLogModel && !periodLogModel.isPregnancy())
		{
			while (true)
			{
				
				if (startDate.getTime() > new Date().getTime())
				{
					Date checkDateinCurrentCyclLength = Utility.setHourMinuteSecondZero(new Date(startDate.getTime() - (PeriodTrackerConstants.MILLI_SECONDS * cycleLength)));
					if (periodLogModel.getStartDate() != null && checkDateinCurrentCyclLength.compareTo(periodLogModel.getStartDate()) <= 0)
					{
						
						startDate = startDate;
					}
					else if (Utility.addDays(checkDateinCurrentCyclLength, PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_STARTS_DAYS).before(new Date()))
					{
						startDate = startDate;
					}
					else
					{
						
						startDate = checkDateinCurrentCyclLength;
						
						/*if(checkDateinCurrentCyclLength.after(Utility.setHourMinuteSecondZero(new Date()))){
							startDate = checkDateinCurrentCyclLength;
							
						}else{
							startDate = startDate;
						}*/
					}
					break;
				}
				else
				{
					startDate = new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
				}
			}
		}
		else if (periodLogModel.isPregnancy())
		{
			if (periodLogModel.getEndDate().getTime() != (PeriodTrackerConstants.NULL_DATE))
			{
				startDate = ((PeriodLogModel) selectPreviousDateRecordForPregnancy(periodLogModel.getStartDate())).getStartDate();
				
			}
		}
		
		periodLogModel = new PeriodLogModel();
		periodLogModel.setStartDate(startDate);
		periodLogModel.setEndDate(new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * (periodLength - 1))));
		periodLogModel.setCycleLength(cycleLength);
		periodLogModel.setPeriodLength(periodLength);
		
		ovulationDay = PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength();
		
		// Fertile Dates
		ovulationDate = Utility.setHourMinuteSecondZero(new Date(periodLogModel.getStartDate().getTime() + ((ovulationDay - 1) * PeriodTrackerConstants.MILLI_SECONDS)));
		periodLogModel.setOvulationDate(ovulationDate);
		periodLogModel.setFertileStartDate(Utility.setHourMinuteSecondZero(Utility.addDays(new Date(startDate.getTime()), (PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_STARTS_DAYS))));
		periodLogModel.setFertileEndDate(Utility.setHourMinuteSecondZero(Utility.addDays(new Date(startDate.getTime()), (PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_END_DAYS))));
		
		return periodLogModel;
		
	}
	
	public PeriodTrackerModelInterface selectPreviousDateRecordForPregnancy(Date date)
	{
		
		String SQL = "select * from Period_Track Where Start_Date < " + date.getTime() + " order by Start_Date DESC LIMIT 1 ";
		
		return this.selectRecord(PeriodLogModel.class, SQL);
		
	}
	
	public boolean updatePeriodLog(final PeriodLogModel periodTrackModel)
	{
		boolean rowUpdated = false;
		try
		{
			ContentValues contentValues = new ContentValues();
			contentValues.put(PeriodLogModel.PERIOD_TRACK_PROFILE_ID, periodTrackModel.getProfileId());
			contentValues.put(PeriodLogModel.PERIOD_TRACK_START_DATE, periodTrackModel.getStartDate().getTime());
			contentValues.put(PeriodLogModel.PERIOD_TRACK_END_DATE, periodTrackModel.getEndDate().getTime());
			contentValues.put(PeriodLogModel.PERIOD_TRACK_PERIOD_LENGTH, periodTrackModel.getPeriodLength());
			contentValues.put(PeriodLogModel.PERIOD_TRACK_CYCLE_LENGTH, periodTrackModel.getCycleLength());
			
			if (this.updateRecord(PeriodLogModel.PERIOD_TRACK, contentValues, PeriodLogModel.PERIOD_TRACK_Id + "=" + periodTrackModel.getId(), null) > 0)
			{
				rowUpdated = true;
			}
			
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return rowUpdated;
	}
	
	public PeriodTrackerModelInterface getPerdictionFertileDatesAndOvulationDatesForDate(Date date)
	{
		
		PeriodLogModel periodLogModel = null;
		PeriodTrackerModelInterface logModel = getLatestLog();
		
		Date startDate = new Date();
		int cycleLength = PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength();
		int periodLength = PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength();
		
		int ovulationDay;
		Date ovulationDate;
		
		if (null != logModel)
		{
			periodLogModel = (PeriodLogModel) logModel;
			if (null != periodLogModel.getStartDate()) startDate = periodLogModel.getStartDate();
		}
		
		while (true)
		{
			if (startDate.getTime() > new Date().getTime())
			{
				Date checkDateinCurrentCyclLength = new Date(startDate.getTime() - (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
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
		
		periodLogModel = new PeriodLogModel();
		periodLogModel.setStartDate(startDate);
		periodLogModel.setEndDate(new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * periodLength)));
		periodLogModel.setCycleLength(cycleLength);
		periodLogModel.setPeriodLength(periodLength);
		
		ovulationDay = cycleLength / 2;
		
		// Fertile Dates
		ovulationDate = new Date(periodLogModel.getStartDate().getTime() + (ovulationDay * PeriodTrackerConstants.MILLI_SECONDS));
		periodLogModel.setOvulationDate(ovulationDate);
		/*periodLogModel.setFertileStartDate(new Date(startDate.getTime()
				+ PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_STARTS_DAYS
				* PeriodTrackerConstants.MILLI_SECONDS));
		periodLogModel.setFertileEndDate(new Date(startDate.getTime()
				+ PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_END_DAYS
				* PeriodTrackerConstants.MILLI_SECONDS));
		*/
		periodLogModel.setFertileStartDate(Utility.addDays(ovulationDate, -6));
		periodLogModel.setFertileEndDate(Utility.addDays(ovulationDate, 5));
		return periodLogModel;
		
	}
	
	public PeriodTrackerModelInterface selectPreviousDateRecord(Date date)
	{
		
		String SQL = "select * from Period_Track Where Start_Date < " + date.getTime() + " order by Start_Date DESC LIMIT 1 ";
		
		return this.selectRecord(PeriodLogModel.class, SQL);
		
	}
	
	public PeriodTrackerModelInterface selectNextStartDateRecord(Date date)
	{
		
		String SQL = "select * from Period_Track Where Start_Date > " + date.getTime() + " order by Start_Date ASC LIMIT 1";
		
		return this.selectRecord(PeriodLogModel.class, SQL);
		
	}
	
	public List<PeriodTrackerModelInterface> checkValiadtionOfRecordBetweenRecords(Date startDate, Date endDate)
	{
		
		String Sql = "select * from PERIOD_TRACK where (Start_Date > " + startDate.getTime() + " and Start_Date < " + endDate.getTime() + ") UNION " + "select * from PERIOD_TRACK where (End_Date >" + startDate.getTime() + " and End_Date < " + endDate.getTime() + ") ";
		
		return this.selectMulipleRecord(PeriodLogModel.class, Sql);
		
	}
	
	public List<PeriodTrackerModelInterface> getAllLogs()
	{
		
		String selectSQL = "select * from Period_Track order by Start_Date DESC";
		return this.selectMulipleRecord(PeriodLogModel.class, selectSQL);
		
	}
}
