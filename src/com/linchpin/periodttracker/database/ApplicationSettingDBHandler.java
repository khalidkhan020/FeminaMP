package com.linchpin.periodttracker.database;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.UserProfileModel;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;

public class ApplicationSettingDBHandler extends PeriodTrackerDBHandler {

	public ApplicationSettingDBHandler(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public int getAverageCycleLength() {

		int averageCycleLength;

		String query = "SELECT AVG (" + PeriodLogModel.PERIOD_TRACK_CYCLE_LENGTH + ") " + " FROM "
				+ PeriodLogModel.PERIOD_TRACK + " WHERE " + PeriodLogModel.PERIOD_TRACK_START_DATE + " < "
				+ Utility.setHourMinuteSecondZero(new Date()).getTime() + " AND "
				+ PeriodLogModel.PERIOD_TRACK_CYCLE_LENGTH + " != 0 " + " order by Start_Date DESC LIMIT 6 ";

		averageCycleLength = this.getAverageCycleLength(query);
		if (averageCycleLength == 0) {

			averageCycleLength = PeriodTrackerConstants.CYCLE_LENGTH;
		} else if (averageCycleLength < 10) {
			averageCycleLength = PeriodTrackerConstants.CYCLE_LENGTH;
		} else if (averageCycleLength > 60) {
			averageCycleLength = PeriodTrackerConstants.CYCLE_LENGTH;
		}

		return averageCycleLength;

	}

	public int getAveragePeriodLength() {

		int periodLength;
		String query = "SELECT AVG (" + PeriodLogModel.PERIOD_TRACK_PERIOD_LENGTH + ") " + " FROM "
				+ PeriodLogModel.PERIOD_TRACK + " WHERE " + PeriodLogModel.PERIOD_TRACK_START_DATE + " < "
				+ Utility.setHourMinuteSecondZero(new Date()).getTime() + " AND "
				+ PeriodLogModel.PERIOD_TRACK_PERIOD_LENGTH + " != 0 " + " order by Start_Date DESC LIMIT 6 ";
		periodLength = this.getAverageCycleLength(query);

		if (periodLength == 0 ) {
			periodLength = PeriodTrackerConstants.PERIOD_LENGTH;
		}else if(periodLength < 2){
			periodLength = PeriodTrackerConstants.PERIOD_LENGTH;
		}else if( periodLength > 10){
			periodLength = PeriodTrackerConstants.PERIOD_LENGTH;
		}
		return periodLength;

	}

	public PeriodTrackerModelInterface getUserProfile(String firstName) {
		String query = "SELECT * FROM " + UserProfileModel.USER_PROFILE + " WHERE "
				+ UserProfileModel.USER_PROFILE_FIRST_NAME + " = " + "'" + firstName + "'";
		return this.selectRecord(UserProfileModel.class, query);
	}

	public List<PeriodTrackerModelInterface> getApplicationSettings(int profileId) {
		String query = "SELECT * FROM APPLICATION_SETTINGS WHERE "
				+ ApplicationSettingModel.APPLICATION_SETTING_PROFILE_ID + " = " + profileId;
		return this.selectMulipleRecord(ApplicationSettingModel.class, query);
	}

	public PeriodTrackerModelInterface getParticularApplicationSetting(String key, int profileId) {
		String query = "SELECT * FROM APPLICATION_SETTINGS WHERE "
				+ ApplicationSettingModel.APPLICATION_SETTING_PROFILE_ID + " = " + profileId + " AND "
				+ ApplicationSettingModel.APPLICATION_SETTING_ID + " = " + "'" + key + "'";
		return selectRecord(ApplicationSettingModel.class, query);
	}

	public boolean upadteApplicationSetting(ApplicationSettingModel applicationSettingModel) {
		ContentValues contentValues = null;
		boolean rowupdated = false;

		try {
			contentValues = new ContentValues();
			contentValues.put(ApplicationSettingModel.APPLICATION_SETTING_VALUE, applicationSettingModel.getValue());

			String whereClause = ApplicationSettingModel.APPLICATION_SETTING_ID + " = " + "'"
					+ applicationSettingModel.getId() + "'" + " AND "
					+ ApplicationSettingModel.APPLICATION_SETTING_PROFILE_ID + " = "
					+ applicationSettingModel.getProfileId();

			if (this.updateRecord(ApplicationSettingModel.APPLICATION_SETTING, contentValues, whereClause, null) > 0)
				rowupdated = true;

		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return rowupdated;

	}

	public boolean inseretApplicationSetting(ApplicationSettingModel applicationSettingModel) {
		ContentValues contentValues = null;
		boolean rowupdated = false;

		contentValues = new ContentValues();
		contentValues.put(ApplicationSettingModel.APPLICATION_SETTING_ID, applicationSettingModel.getId());
		contentValues.put(ApplicationSettingModel.APPLICATION_SETTING_VALUE, applicationSettingModel.getValue());
		contentValues.put(ApplicationSettingModel.APPLICATION_SETTING_PROFILE_ID,
				applicationSettingModel.getProfileId());

		if (this.createRecord(ApplicationSettingModel.APPLICATION_SETTING, null, contentValues) > 0)
			rowupdated = true;

		return rowupdated;

	}

	public boolean deleteApplicationSetting(ApplicationSettingModel applicationSettingModel) {

		return (this.deleteRecord(ApplicationSettingModel.APPLICATION_SETTING,
				ApplicationSettingModel.APPLICATION_SETTING_PROFILE_ID + " = "
						+ PeriodTrackerObjectLocator.getInstance().getProfileId() + " and "
						+ ApplicationSettingModel.APPLICATION_SETTING_ID + " = '" + applicationSettingModel.getId()
						+ "'", null) > 0);
	}

}
