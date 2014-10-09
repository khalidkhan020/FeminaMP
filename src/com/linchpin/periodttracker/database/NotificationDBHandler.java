package com.linchpin.periodttracker.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.NotificattionModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;

public class NotificationDBHandler extends PeriodTrackerDBHandler {

	public NotificationDBHandler(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public List<PeriodTrackerModelInterface> getPredictionLogs() {

		List<PeriodTrackerModelInterface> predictionList = new ArrayList<PeriodTrackerModelInterface>();
		PeriodLogModel periodLogModel = null;
		PeriodTrackerModelInterface logModel = getLatestLog();

		Date startDate = new Date();
		int cycleLength = PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength();
		int periodLength = PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength();

		if (null != logModel) {
			periodLogModel = (PeriodLogModel) logModel;
			if (null != periodLogModel.getStartDate())
				startDate = periodLogModel.getStartDate();
		}

		while (true) {
			if (startDate.getTime() > new Date().getTime()) {
				Date checkDateinCurrentCyclLength = new Date(startDate.getTime()
						- (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
				if (periodLogModel.getStartDate() != null) {

					if (!periodLogModel.getStartDate().equals(checkDateinCurrentCyclLength)
							&& !periodLogModel.getStartDate().before(checkDateinCurrentCyclLength))
						startDate = checkDateinCurrentCyclLength;
				} else {
					startDate = checkDateinCurrentCyclLength;
				}
				break;
			} else {
				startDate = new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
			}
		}

		if (!periodLogModel.isPregnancy()) {
			for (int i = 0; i <= 3; i++) {
				periodLogModel = new PeriodLogModel();
				periodLogModel.setStartDate(startDate);
				periodLogModel.setEndDate(new Date(startDate.getTime()
						+ (PeriodTrackerConstants.MILLI_SECONDS * periodLength)));
				periodLogModel.setCycleLength(cycleLength);
				periodLogModel.setPeriodLength(periodLength);
				predictionList.add(periodLogModel);
				startDate = new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
			}
		}
		return predictionList;
	}

	public List<PeriodTrackerModelInterface> getPerdictionFertileDatesAndOvulationDates() {

		PeriodLogModel periodLogModel = null;
		PeriodTrackerModelInterface logModel = getLatestLog();
		List<PeriodTrackerModelInterface> predictionList = new ArrayList<PeriodTrackerModelInterface>();

		Date startDate = Utility.setHourMinuteSecondZero(new Date());
		int cycleLength = PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength();
		int periodLength = PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength();

		int ovulationDay;
		Date ovulationDate;

		if (null != logModel && !((PeriodLogModel) logModel).isPregnancy()) {
			periodLogModel = (PeriodLogModel) logModel;
			if (null != periodLogModel.getStartDate())
				startDate = periodLogModel.getStartDate();
		}
		if (null != logModel && !((PeriodLogModel) logModel).isPregnancy()) {
			while (true) {
				if (startDate.getTime() > new Date().getTime()) {
					Date checkDateinCurrentCyclLength = Utility.setHourMinuteSecondZero(new Date(startDate.getTime()
							- (PeriodTrackerConstants.MILLI_SECONDS * cycleLength)));
					if (periodLogModel.getStartDate() != null) {

						/*
						 * if (!periodLogModel.getStartDate().equals(
						 * checkDateinCurrentCyclLength) &&
						 * !periodLogModel.getStartDate
						 * ().before(checkDateinCurrentCyclLength))
						 */startDate = checkDateinCurrentCyclLength;
					} else {
						startDate = checkDateinCurrentCyclLength;
					}
					break;
				} else {
					startDate = new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
				}
			}
		} else if (((PeriodLogModel) logModel).isPregnancy()) {
			if (((PeriodLogModel) logModel).getEndDate().getTime() != (PeriodTrackerConstants.NULL_DATE)) {
				startDate = ((PeriodLogModel) selectPreviousDateRecordForPregnancy(((PeriodLogModel) logModel)
						.getStartDate())).getStartDate();

			}
		}

		ovulationDay = PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength();

		if (!((PeriodLogModel)logModel).isPregnancy()) {
			for (int i = 0; i <= 3; i++) {
				periodLogModel = new PeriodLogModel();
				periodLogModel.setStartDate(startDate);
				periodLogModel.setEndDate(new Date(startDate.getTime()
						+ (PeriodTrackerConstants.MILLI_SECONDS * (periodLength - 1))));
				periodLogModel.setCycleLength(cycleLength);
				periodLogModel.setPeriodLength(periodLength);

				// Fertile Dates
				ovulationDate = Utility.setHourMinuteSecondZero(new Date(periodLogModel.getStartDate().getTime()
						+ ((ovulationDay - 1) * PeriodTrackerConstants.MILLI_SECONDS)));
				periodLogModel.setOvulationDate(ovulationDate);
				periodLogModel
						.setFertileStartDate(Utility.setHourMinuteSecondZero(Utility.addDays(
								new Date(startDate.getTime()),
								(PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_STARTS_DAYS))));
				periodLogModel.setFertileEndDate(Utility.setHourMinuteSecondZero(Utility.addDays(
						new Date(startDate.getTime()), (PeriodTrackerConstants.NUMBER_OF_DAYS_IN_FERTILE_END_DAYS))));

				predictionList.add(periodLogModel);
				startDate = new Date(startDate.getTime() + (PeriodTrackerConstants.MILLI_SECONDS * cycleLength));
			}
		}
		return predictionList;

	}

	public PeriodTrackerModelInterface selectPreviousDateRecordForPregnancy(Date date) {

		String SQL = "select * from Period_Track Where Start_Date < " + date.getTime()
				+ " order by Start_Date DESC LIMIT 1 ";

		return this.selectRecord(PeriodLogModel.class, SQL);

	}

	public PeriodTrackerModelInterface getLatestLog() {
		String selectSQL = "select * from Period_Track order by Start_Date DESC LIMIT 1";
		return this.selectRecord(PeriodLogModel.class, selectSQL);
	}
	public PeriodTrackerModelInterface getLatestInsecureIntimate(long fertilitystart,long fertilitiend) {
		String selectSQL = "select * from "+DBManager.TABLE_PERIOD_DAY_DETAILS+" where "+DBProjections.DD_INTIMATE+">0 AND  "+DBProjections.DD_DATE+">"+fertilitystart+" AND  "+DBProjections.DD_DATE+"<"+fertilitiend+" order by "+DBProjections.DD_DATE+"  DESC LIMIT 1";
		return this.selectRecord(DayDetailModel.class, selectSQL);
	}

	public boolean addRecord(NotificattionModel notificattionModel) {

		ContentValues createContentValues;

		try {

			createContentValues = new ContentValues();
			createContentValues.put(NotificattionModel.NOTIFICATION_ID, notificattionModel.getId());
			createContentValues.put(NotificattionModel.NOTIFICATION_TYPE, notificattionModel.getType());
			this.createRecord(NotificattionModel.NOTIFICATION, null, createContentValues);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return true;

	}

	public void deleteAllNotifications() {
		try {

			this.deleteRecord(NotificattionModel.NOTIFICATION, null, null);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void deleteNotificationForParticularType(NotificattionModel notificattionModel) {
		try {

			this.deleteRecord(NotificattionModel.NOTIFICATION, NotificattionModel.NOTIFICATION_TYPE + " = " + "'"
					+ notificattionModel.getType() + "'", null);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public List<PeriodTrackerModelInterface> selectParticularNotificationType(NotificattionModel model) {
		List<PeriodTrackerModelInterface> modelInterface = null;
		try {
			String Sql = " Select " + NotificattionModel.NOTIFICATION_ID + "," + NotificattionModel.NOTIFICATION_TYPE
					+ " From " + NotificattionModel.NOTIFICATION + " WHERE " + NotificattionModel.NOTIFICATION_TYPE
					+ " = " + "'" + model.getType() + "'";
			modelInterface = this.selectMulipleRecord(NotificattionModel.class, Sql);

		} catch (Exception exception) {

		}

		return modelInterface;
	}
}
