package com.linchpin.periodttracker.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.linchpin.periodtracker.interfaces.CalenderDialog;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;

public class PeriodLogDBHandler extends PeriodTrackerDBHandler
{
	SimpleDateFormat					dateFormat;
	Context	context;
	
	public PeriodLogDBHandler(Context context)
	{
		super(context);
		this.context = context;
		
		dateFormat = new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		
	}
	
	public boolean addPeriodLog(final PeriodLogModel periodTrackModel)
	{
		
		boolean rowCreate = false;
		int periodLength = 0;
		int cycleLength = 0;
		ContentValues createContentValues = null, updateContentValues = null;
		
		try
		{
			
			if (!periodTrackModel.isPregnancy())
			{
				PeriodLogModel logModellesser = (PeriodLogModel) selectPreviousDateRecord(periodTrackModel.getStartDate());
				PeriodLogModel logModelGreater = (PeriodLogModel) selectNextStartDateRecord(periodTrackModel.getStartDate());
				
				createContentValues = new ContentValues();
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_PROFILE_ID, periodTrackModel.getProfileId());
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_START_DATE, periodTrackModel.getStartDate().getTime());
				
				APP.GLOBAL().getEditor().putString(APP.PREF.START_DATE.key,dateFormat.format(periodTrackModel.getStartDate())).commit();
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY, periodTrackModel.isPregnancy());
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY, periodTrackModel.isPregnancy());
				
				if (null != periodTrackModel.getEndDate())
				{
					createContentValues.put(PeriodLogModel.PERIOD_TRACK_END_DATE, periodTrackModel.getEndDate().getTime());
					APP.GLOBAL().getEditor().putString(APP.PREF.END_DATE.key,dateFormat.format(periodTrackModel.getEndDate())).commit();
					createContentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY, periodTrackModel.isPregnancy());
					periodLength = Utility.calculatePeriodLength(periodTrackModel.getStartDate(), periodTrackModel.getEndDate());
					
				}
				else
				{
					createContentValues.put(PeriodLogModel.PERIOD_TRACK_END_DATE, PeriodTrackerConstants.NULL_DATE);
					APP.GLOBAL().getEditor().putString(APP.PREF.END_DATE.key,dateFormat.format(PeriodTrackerConstants.NULL_DATE)).commit();
					
				}
				
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_PERIOD_LENGTH, periodLength);
				if (logModelGreater.getStartDate() != null)
				{
					if (!logModelGreater.isPregnancy())
					{
						cycleLength = Utility.calculateCycleLength(logModelGreater.getStartDate(), periodTrackModel.getStartDate());
					}
					else
					{
						cycleLength = 0;
						periodTrackModel.setPregnancysupportable(true);
					}
				}
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_CYCLE_LENGTH, cycleLength);
				
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY_SUPPOTABLE, periodTrackModel.isPregnancysupportable());
				
				if (logModellesser.getStartDate() != null)
				{
					cycleLength = Utility.calculateCycleLength(periodTrackModel.getStartDate(), logModellesser.getStartDate());
					updateContentValues = new ContentValues();
					updateContentValues.put(periodTrackModel.PERIOD_TRACK_CYCLE_LENGTH, cycleLength);
					updateContentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY_SUPPOTABLE, false);
					
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
			}
			else
			{
				
				createContentValues = new ContentValues();
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_PROFILE_ID, periodTrackModel.getProfileId());
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_START_DATE, periodTrackModel.getStartDate().getTime());
				APP.GLOBAL().getEditor().putString(APP.PREF.START_DATE.key,dateFormat.format(periodTrackModel.getStartDate())).commit();
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY, periodTrackModel.isPregnancy());
				createContentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY_SUPPOTABLE, periodTrackModel.isPregnancysupportable());
				
				
				if (null != periodTrackModel.getEndDate())
				{
					createContentValues.put(PeriodLogModel.PERIOD_TRACK_END_DATE, periodTrackModel.getEndDate().getTime());
					periodLength = Utility.calculatePeriodLength(periodTrackModel.getStartDate(), periodTrackModel.getEndDate());
					APP.GLOBAL().getEditor().putString(APP.PREF.END_DATE.key,dateFormat.format(periodTrackModel.getEndDate())).commit();
				}
				else
				{
					APP.GLOBAL().getEditor().putString(APP.PREF.END_DATE.key,dateFormat.format(PeriodTrackerConstants.NULL_DATE)).commit();					
					createContentValues.put(PeriodLogModel.PERIOD_TRACK_END_DATE, PeriodTrackerConstants.NULL_DATE);
				}
				
				ContentValues updateContentVaules = null;
				String Where = "";
				PeriodLogModel previouslog = (PeriodLogModel) this.selectPreviousDateRecord(periodTrackModel.getStartDate());
				if (null != previouslog && previouslog.getStartDate() != null)
				{
					updateContentVaules = new ContentValues();
					updateContentVaules.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY_SUPPOTABLE, Boolean.valueOf("true"));
					Where = PeriodLogModel.PERIOD_TRACK_Id + " = " + previouslog.getId();
				}
				if (null != updateContentVaules)
				{
					if (this.createAndUpdateRecord(periodTrackModel.PERIOD_TRACK, null, createContentValues, PeriodLogModel.PERIOD_TRACK, updateContentVaules, Where, null) > 0)
					{
						rowCreate = true;
					}
				}
				else
				{
					this.createRecord(PeriodLogModel.PERIOD_TRACK, null, createContentValues);
				}
				rowCreate = true;
			}
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, false).commit();
			
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		if (PeriodTrackerObjectLocator.getInstance().isAveraged())
		{
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(context);
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_CYCLE_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAverageCycleLength()), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAveragePeriodLength()), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAverageCycleLength() / 2), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
		}
		
		return rowCreate;
	}
	
	public boolean updatePeriodLogWhenEditRecord(final PeriodLogModel periodTrackModel)
	{
		boolean rowUpdated = false;
		int cycleLength = 0;
		int periodLength = 0;
		ContentValues contentValues = null;
		try
		{
			
			contentValues = new ContentValues();
			contentValues.put(periodTrackModel.PERIOD_TRACK_START_DATE, periodTrackModel.getStartDate().getTime());
			APP.GLOBAL().getEditor().putString(APP.PREF.START_DATE.key,dateFormat.format(periodTrackModel.getStartDate())).commit();
			contentValues.put(periodTrackModel.PERIOD_TRACK_END_DATE, periodTrackModel.getEndDate().getTime());
			APP.GLOBAL().getEditor().putString(APP.PREF.END_DATE.key,dateFormat.format(periodTrackModel.getEndDate())).commit();
			periodLength = Utility.calculatePeriodLength(periodTrackModel.getStartDate(), periodTrackModel.getEndDate());
			contentValues.put(periodTrackModel.PERIOD_TRACK_PERIOD_LENGTH, periodLength);
			contentValues.put(periodTrackModel.PERIOD_TRACK_CYCLE_LENGTH, cycleLength);
			
			if (this.updateRecord(PeriodLogModel.PERIOD_TRACK, contentValues, PeriodLogModel.PERIOD_TRACK_Id + "=" + periodTrackModel.getId(), null) > 0)
			{
				rowUpdated = true;
			}
			
			PeriodLogModel logModelGreater = (PeriodLogModel) selectNextStartDateRecord(periodTrackModel.getStartDate());
			
			if (logModelGreater != null)
			{
				if (null != logModelGreater.getStartDate())
				{
					contentValues = new ContentValues();
					if (logModelGreater.getStartDate() != null && !logModelGreater.isPregnancy())
					{
						cycleLength = Utility.calculateCycleLength(logModelGreater.getStartDate(), periodTrackModel.getStartDate());
						contentValues.put(periodTrackModel.PERIOD_TRACK_PREGNANCY_SUPPOTABLE, periodTrackModel.isPregnancysupportable());
						
					}
					else
					{
						cycleLength = 0;
						contentValues.put(periodTrackModel.PERIOD_TRACK_PREGNANCY_SUPPOTABLE, true);
					}
					contentValues.put(periodTrackModel.PERIOD_TRACK_CYCLE_LENGTH, cycleLength);
					
					if (this.updateRecord(PeriodLogModel.PERIOD_TRACK, contentValues, PeriodLogModel.PERIOD_TRACK_Id + "=" + periodTrackModel.getId(), null) > 0)
					{
						rowUpdated = true;
					}
				}
			}
			
			if (null != logModelGreater.getStartDate())
			{
				PeriodLogModel greaterThanLogModelGreater = (PeriodLogModel) selectNextStartDateRecord(logModelGreater.getStartDate());
				
				if (null != greaterThanLogModelGreater)
				{
					if (greaterThanLogModelGreater.getStartDate() != null && !greaterThanLogModelGreater.isPregnancy())
					{
						cycleLength = Utility.calculateCycleLength(greaterThanLogModelGreater.getStartDate(), logModelGreater.getStartDate());
						if (cycleLength > 0) logModelGreater.setCycleLength(cycleLength);
						else
						{
							cycleLength = 0;
							logModelGreater.setCycleLength(cycleLength);
						}
						updatePeriodLog(logModelGreater);
					}
					else
					{
						logModelGreater.setCycleLength(0);
						updatePeriodLog(logModelGreater);
					}
				}
			}
			
			PeriodLogModel logModelleseer = (PeriodLogModel) selectPreviousDateRecord(periodTrackModel.getStartDate());
			if (null != logModelleseer)
			{
				if (logModelleseer.getStartDate() != null && !logModelleseer.isPregnancy())
				{
					cycleLength = Utility.calculateCycleLength(periodTrackModel.getStartDate(), logModelleseer.getStartDate());
					if (cycleLength > 0) logModelleseer.setCycleLength(cycleLength);
					else
					{
						cycleLength = 0;
						logModelleseer.setCycleLength(cycleLength);
					}
					if (logModelGreater.isPregnancy())
					{
						logModelleseer.setPregnancysupportable(false);
					}
					updatePeriodLog(logModelleseer);
				}
			}
			
			if (!periodTrackModel.isPregnancy() || !periodTrackModel.isPregnancysupportable())
			{
				APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, false).commit();
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		if (PeriodTrackerObjectLocator.getInstance().isAveraged())
		{
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(context);
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_CYCLE_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAverageCycleLength()), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAveragePeriodLength()), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAverageCycleLength() / 2), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
		}
		return rowUpdated;
	}
	
	public boolean updatePeriodLog(final PeriodLogModel periodTrackModel)
	{
		boolean rowUpdated = false;
		ContentValues contentValues = null;
		PeriodLogModel previousRecord;
		try
		{
			if (periodTrackModel.isPregnancy())
			{
				previousRecord = (PeriodLogModel) selectPreviousDateRecord(periodTrackModel.getStartDate());
				if (periodTrackModel.getEndDate().getTime() != PeriodTrackerConstants.NULL_DATE)
				{
					periodTrackModel.setCycleLength(Utility.calculateCycleLength(periodTrackModel.getEndDate(), previousRecord.getStartDate()) + 1);
				}
				contentValues = new ContentValues();
				contentValues.put(PeriodLogModel.PERIOD_TRACK_PROFILE_ID, periodTrackModel.getProfileId());
				contentValues.put(PeriodLogModel.PERIOD_TRACK_START_DATE, periodTrackModel.getStartDate().getTime());
				APP.GLOBAL().getEditor().putString(APP.PREF.START_DATE.key,dateFormat.format(periodTrackModel.getStartDate())).commit();
				
				contentValues.put(PeriodLogModel.PERIOD_TRACK_END_DATE, periodTrackModel.getEndDate().getTime());
				APP.GLOBAL().getEditor().putString(APP.PREF.END_DATE.key,dateFormat.format(periodTrackModel.getEndDate())).commit();
				
				contentValues.put(PeriodLogModel.PERIOD_TRACK_PERIOD_LENGTH, periodTrackModel.getPeriodLength());
				contentValues.put(PeriodLogModel.PERIOD_TRACK_CYCLE_LENGTH, periodTrackModel.getCycleLength());
				contentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY, periodTrackModel.isPregnancy());
				contentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY_SUPPOTABLE, periodTrackModel.isPregnancysupportable());
			}
			else
			{
				contentValues = new ContentValues();
				contentValues.put(PeriodLogModel.PERIOD_TRACK_PROFILE_ID, periodTrackModel.getProfileId());
				contentValues.put(PeriodLogModel.PERIOD_TRACK_START_DATE, periodTrackModel.getStartDate().getTime());
				APP.GLOBAL().getEditor().putString(APP.PREF.START_DATE.key,dateFormat.format(periodTrackModel.getStartDate())).commit();
				
				contentValues.put(PeriodLogModel.PERIOD_TRACK_END_DATE, periodTrackModel.getEndDate().getTime());
				APP.GLOBAL().getEditor().putString(APP.PREF.END_DATE.key,dateFormat.format(periodTrackModel.getEndDate())).commit();
				
				contentValues.put(PeriodLogModel.PERIOD_TRACK_PERIOD_LENGTH, periodTrackModel.getPeriodLength());
				contentValues.put(PeriodLogModel.PERIOD_TRACK_CYCLE_LENGTH, periodTrackModel.getCycleLength());
				contentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY, periodTrackModel.isPregnancy());
				contentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY_SUPPOTABLE, periodTrackModel.isPregnancysupportable());
			}
			if (this.updateRecord(PeriodLogModel.PERIOD_TRACK, contentValues, PeriodLogModel.PERIOD_TRACK_Id + "=" + periodTrackModel.getId(), null) > 0)
			{
				rowUpdated = true;
			}
			
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		if (PeriodTrackerObjectLocator.getInstance().isAveraged())
		{
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(context);
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_CYCLE_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAverageCycleLength()), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAveragePeriodLength()), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAverageCycleLength() / 2), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
		}
		return rowUpdated;
	}
	
	public List<PeriodTrackerModelInterface> selectDateForEnteringCycleLength(Date date)
	{
		
		String selectSQL = " select * from (select * from Period_Track Where Start_Date < " + date.getTime() + " order by Start_Date DESC LIMIT 1) " + " UNION " + " select * from (select * from Period_Track Where Start_Date > " + date.getTime() + " order by Start_Date ASC LIMIT 1) ";
		return this.selectMulipleRecord(PeriodLogModel.class, selectSQL);
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
	
	public List<PeriodTrackerModelInterface> checkValiadtionOfRecordBetweenRecordsWhileUpadte(Date startDate, Date endDate, int profileId)
	{
		
		String Sql = "select * from PERIOD_TRACK where (Start_Date > " + startDate.getTime() + " and Start_Date < " + endDate.getTime() + " and " + PeriodLogModel.PERIOD_TRACK_Id + " != " + profileId + ") UNION " + "select * from PERIOD_TRACK where (End_Date >" + startDate.getTime()
				+ " and End_Date < " + endDate.getTime() + " and " + PeriodLogModel.PERIOD_TRACK_Id + " != " + profileId + ") ";
		
		return this.selectMulipleRecord(PeriodLogModel.class, Sql);
		
	}
	
	public boolean deletePeriodRecord(PeriodLogModel periodLogModel)
	{
		boolean rowDeleted = false;
		int cycleLength = 0;
		ContentValues contentValues = null;
		
		PeriodLogModel logModelleseer = null;
		if (periodLogModel.isPregnancy())
		{
			logModelleseer = (PeriodLogModel) selectPreviousDateRecordForPregnancy(periodLogModel.getStartDate());
		}
		else
		{
			logModelleseer = (PeriodLogModel) selectPreviousDateRecord(periodLogModel.getStartDate());
			
		}
		PeriodLogModel logModelGreater = (PeriodLogModel) selectNextStartDateRecord(periodLogModel.getStartDate());
		
		if (logModelleseer.getStartDate() != null && !logModelleseer.isPregnancy())
		{
			contentValues = new ContentValues();
			if (logModelGreater.getStartDate() != null && !logModelGreater.isPregnancy())
			{
				cycleLength = Utility.calculateCycleLength(logModelGreater.getStartDate(), logModelleseer.getStartDate());
			}
			else
			{
				cycleLength = 0;
			}
			if (periodLogModel.isPregnancy())
			{
				contentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY_SUPPOTABLE, false);
			}
			contentValues.put(PeriodLogModel.PERIOD_TRACK_CYCLE_LENGTH, cycleLength);
		}
		
		if (null != contentValues)
		{
			if (this.updateAndDeleteRecord(PeriodLogModel.PERIOD_TRACK, contentValues, PeriodLogModel.PERIOD_TRACK_Id + "=" + logModelleseer.getId(), null, PeriodLogModel.PERIOD_TRACK, PeriodLogModel.PERIOD_TRACK_Id + "=" + periodLogModel.getId(), null) > 0) rowDeleted = true;
		}
		else
		{
			
			if (this.deleteRecord(PeriodLogModel.PERIOD_TRACK, PeriodLogModel.PERIOD_TRACK_Id + "=" + periodLogModel.getId(), null) > 0) rowDeleted = true;
		}
		if (PeriodTrackerObjectLocator.getInstance().isAveraged())
		{
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(context);
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_CYCLE_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAverageCycleLength()), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAveragePeriodLength()), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
			applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_LENGTH_KEY, String.valueOf(applicationSettingDBHandler.getAverageCycleLength() / 2), PeriodTrackerObjectLocator.getInstance().getProfileId()));
			
		}
		return rowDeleted;
		
	}
	
	public List<PeriodTrackerModelInterface> getAllLogs(int profileId)
	{
		
		String selectSQL = "select * from Period_Track Where " + PeriodLogModel.PERIOD_TRACK_PROFILE_ID + " = " + profileId + " order by Start_Date DESC ";
		return this.selectMulipleRecord(PeriodLogModel.class, selectSQL);
		
	}
	
	public List<PeriodTrackerModelInterface> getAllLogsforMonth(Date date, int profileId)
	{
		Utility.addDays(date, 30);
		String selectSQL = "select * from Period_Track Where " + PeriodLogModel.PERIOD_TRACK_START_DATE + " >=  " + Utility.addDays(date, -30).getTime() + " AND " + PeriodLogModel.PERIOD_TRACK_START_DATE + " <= " + Utility.addDays(date, 30).getTime() + " AND "
				+ PeriodLogModel.PERIOD_TRACK_PROFILE_ID + " = " + profileId + " order by Start_Date DESC ";
		return this.selectMulipleRecord(PeriodLogModel.class, selectSQL);
		
	}
	
	/***
	 * @author Khalid Khan*/
	public PeriodTrackerModelInterface getTopLogs(int profileId)
	{
		
		String selectSQL = "select * from Period_Track Where " + PeriodLogModel.PERIOD_TRACK_PROFILE_ID + " = " + profileId + " order by Start_Date DESC LIMIT 1";
		return this.selectRecord(PeriodLogModel.class, selectSQL);
		
	}
	
	public PeriodTrackerModelInterface getLatestLog()
	{
		String selectSQL = "select * from Period_Track order by Start_Date DESC LIMIT 1";
		return this.selectRecord(PeriodLogModel.class, selectSQL);
	}
	
	public List<PeriodTrackerModelInterface> getPredictionLogs()
	{
		
		PeriodLogModel periodLogModel = null;
		PeriodTrackerModelInterface logModel = getLatestLog();
		List<PeriodTrackerModelInterface> predictionList = new ArrayList<PeriodTrackerModelInterface>();
		
		Date startDate = new Date();
		int cycleLength = PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength();
		int periodLength = PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength();
		
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
					
					if (checkDateinCurrentCyclLength.compareTo(periodLogModel.getStartDate()) <= 0)
					{
						startDate = startDate;
					}
					else
					{
						if (checkDateinCurrentCyclLength.before(Utility.setHourMinuteSecondZero(new Date())))
						{
							startDate = startDate;
							
						}
						else
						{
							startDate = checkDateinCurrentCyclLength;
						}
					}
				}
				else
				{
					if (checkDateinCurrentCyclLength.before(Utility.setHourMinuteSecondZero(new Date())))
					{
						startDate = startDate;
					}
					else
					{
						
						startDate = checkDateinCurrentCyclLength;
					}
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
			periodLogModel.setEndDate(new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * (periodLength - 1))));
			periodLogModel.setCycleLength(cycleLength);
			periodLogModel.setPeriodLength(periodLength);
			predictionList.add(periodLogModel);
			startDate = new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
		}
		
		return predictionList;
	}
	
	public List<PeriodTrackerModelInterface> getPastFertileAndOvulationDates()
	{
		
		List<PeriodTrackerModelInterface> periodTrackerModelInterfaces = getAllLogs(PeriodTrackerObjectLocator.getInstance().getProfileId());
		Date ovulationDate;
		int ovulationDay;
		PeriodLogModel periodLogModel;
		PeriodLogModel baseForPregnancy = null;
		for (int i = 0; i < periodTrackerModelInterfaces.size(); i++)
		{
			periodLogModel = (PeriodLogModel) periodTrackerModelInterfaces.get(i);
			if (!periodLogModel.isPregnancy())
			{
				ovulationDay = periodLogModel.getCycleLength() > 32 || periodLogModel.getCycleLength() < 28 ? PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength() : PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength();
				ovulationDate = new Date(periodLogModel.getStartDate().getTime() + ((ovulationDay - 1) * PeriodTrackerConstants.MILLI_SECONDS));
				periodLogModel.setOvulationDate(ovulationDate);
				periodLogModel.setFertileStartDate(Utility.addDays(ovulationDate,-6));
				periodLogModel.setFertileEndDate(Utility.addDays(ovulationDate, 5));
			}
		}
		return periodTrackerModelInterfaces;
	}
	
	public List<PeriodTrackerModelInterface> getPerdictionFertileDatesAndOvulationDates()
	{
		
		PeriodLogModel periodLogModel = null;
		PeriodTrackerModelInterface logModel = getLatestLog();
		List<PeriodTrackerModelInterface> predictionList = new ArrayList<PeriodTrackerModelInterface>();
		
		Date startDate = new Date();
		int cycleLength = PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength();
		int periodLength = PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength();
		
		int ovulationDay;
		Date ovulationDate;
		
		if (null != logModel && !((PeriodLogModel) logModel).isPregnancy())
		{
			periodLogModel = (PeriodLogModel) logModel;
			if (null != periodLogModel.getStartDate()) startDate = periodLogModel.getStartDate();
		}
		if (null != logModel && !((PeriodLogModel) logModel).isPregnancy())
		{
			while (true)
			{
				if (startDate.getTime() > new Date().getTime())
				{
					Date checkDateinCurrentCyclLength = Utility.setHourMinuteSecondZero(new Date(startDate.getTime() - (PeriodTrackerConstants.MILLI_SECONDS * cycleLength)));
					if (periodLogModel.getStartDate() != null && checkDateinCurrentCyclLength.compareTo(periodLogModel.getStartDate()) <= 0 && Utility.addDays(checkDateinCurrentCyclLength, PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength() - 1).before(new Date()))
					{
						startDate = startDate;
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
		else if (((PeriodLogModel) logModel).isPregnancy())
		{
			if (((PeriodLogModel) logModel).getEndDate().getTime() != (PeriodTrackerConstants.NULL_DATE))
			{
				startDate = ((PeriodLogModel) selectPreviousDateRecordForPregnancy(((PeriodLogModel) logModel).getStartDate())).getStartDate();
				
			}
		}
		for (int i = 0; i <= 6; i++)
		{
			periodLogModel = new PeriodLogModel();
			periodLogModel.setStartDate(startDate);
			periodLogModel.setEndDate(new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * (periodLength - 1))));
			periodLogModel.setCycleLength(cycleLength);
			periodLogModel.setPeriodLength(periodLength);
			
			ovulationDay = PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength();
			
			// Fertile Dates
			ovulationDate = new Date(periodLogModel.getStartDate().getTime() + ((ovulationDay - 1) * PeriodTrackerConstants.MILLI_SECONDS));
			periodLogModel.setOvulationDate(ovulationDate);
			periodLogModel.setFertileStartDate(Utility.addDays(new Date(startDate.getTime()), (PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_STARTS_DAYS)));
			periodLogModel.setFertileEndDate(Utility.addDays(new Date(startDate.getTime()), (PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_END_DAYS)));
			
			predictionList.add(periodLogModel);
			startDate = new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
		}
		
		return predictionList;
		
	}
	
	public PeriodTrackerModelInterface selectPreviousDateRecordForPregnancy(Date date)
	{
		
		String SQL = "select * from Period_Track Where Start_Date < " + date.getTime() + " order by Start_Date DESC LIMIT 1 ";
		
		return this.selectRecord(PeriodLogModel.class, SQL);
		
	}
}
