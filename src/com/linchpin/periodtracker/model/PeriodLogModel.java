package com.linchpin.periodtracker.model;

import java.util.Date;

import android.database.Cursor;

public class PeriodLogModel implements PeriodTrackerModelInterface{
	
	public static final String PERIOD_TRACK = "PERIOD_TRACK";
	public static final String PERIOD_TRACK_Id = "Id";
	public static final String PERIOD_TRACK_PROFILE_ID = "PROFILE_ID";
	public static final String PERIOD_TRACK_START_DATE="Start_Date";
	public static final String PERIOD_TRACK_END_DATE="End_Date";
	public static final String PERIOD_TRACK_CYCLE_LENGTH="Cycle_Length";
	public static final String PERIOD_TRACK_PERIOD_LENGTH="Period_Length";
	public static final String PERIOD_TRACK_PREGNANCY="Pregnancy";
	public static final String PERIOD_TRACK_PREGNANCY_SUPPOTABLE="Pregnancy_Supportable";
	
	
	private int id;
	private int profileId;
	private int cycleLength;
	private int periodLength;
	private Date startDate;
	private Date endDate;
	private Date fertileStartDate;
	private Date fertileEndDate;
	private Date ovulationDate;
	private boolean pregnancy;
	private boolean pregnancysupportable;
	

	/**
	 * @return the pregnancysupportable
	 */
	public final boolean isPregnancysupportable() {
		return pregnancysupportable;
	}

	/**
	 * @param pregnancysupportable the pregnancysupportable to set
	 */
	public final void setPregnancysupportable(boolean pregnancysupportable) {
		this.pregnancysupportable = pregnancysupportable;
	}

	/**
	 * @return the pregnancy
	 */
	public final boolean isPregnancy() {
		return pregnancy;
	}

	/**
	 * @param pregnancy the pregnancy to set
	 */
	public final void setPregnancy(boolean pregnancy) {
		this.pregnancy = pregnancy;
	}

	/**
	 * @return the fertileStartDate
	 */
	public final Date getFertileStartDate() {
		return fertileStartDate;
	}

	/**
	 * @param fertileStartDate the fertileStartDate to set
	 */
	public final void setFertileStartDate(Date fertileStartDate) {
		this.fertileStartDate = fertileStartDate;
	}

	/**
	 * @return the fertileEndDate
	 */
	public final Date getFertileEndDate() {
		return fertileEndDate;
	}

	/**
	 * @param fertileEndDate the fertileEndDate to set
	 */
	public final void setFertileEndDate(Date fertileEndDate) {
		this.fertileEndDate = fertileEndDate;
	}

	/**
	 * @return the ovulationDate
	 */
	public final Date getOvulationDate() {
		return ovulationDate;
	}

	/**
	 * @param ovulationDate the ovulationDate to set
	 */
	public final void setOvulationDate(Date ovulationDate) {
		this.ovulationDate = ovulationDate;
	}

	public PeriodLogModel(){
		
	}
	
	public PeriodLogModel(int id, int profileId, Date startDate,
			Date endDate, int cycleLength, int periodLength, boolean pregnancy,boolean pregnancySupportable) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.profileId = profileId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.cycleLength = cycleLength;
		this.periodLength = periodLength;
		this.pregnancy =pregnancy;
		this.pregnancysupportable=pregnancySupportable;
	}

	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the profileId
	 */
	public final int getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId the profileId to set
	 */
	public final void setProfileId(int profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the cycleLength
	 */
	public final int getCycleLength() {
		return cycleLength;
	}

	/**
	 * @param cycleLength the cycleLength to set
	 */
	public final void setCycleLength(int cycleLength) {
		this.cycleLength = cycleLength;
	}

	/**
	 * @return the periodLength
	 */
	public final int getPeriodLength() {
		return periodLength;
	}

	/**
	 * @param periodLength the periodLength to set
	 */
	public final void setPeriodLength(int periodLength) {
		this.periodLength = periodLength;
	}

	/**
	 * @return the startDate
	 */
	public final Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public final void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public final Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public final void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public PeriodLogModel createModel(Cursor cursor) {
		// TODO Auto-generated method stub
		this.id = cursor.getInt(0);
		this.profileId = cursor.getInt(1);
		this.startDate = new Date(cursor.getLong(2));
		this.endDate = new Date(cursor.getLong(3));
		this.cycleLength = cursor.getInt(4);
		this.periodLength = cursor.getInt(5);		
		this.pregnancy= cursor.getInt(6)>0;
		this.pregnancysupportable=cursor.getInt(7)>0;
		return this;
	}

}
