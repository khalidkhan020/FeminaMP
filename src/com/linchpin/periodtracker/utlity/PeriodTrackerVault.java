package com.linchpin.periodtracker.utlity;

import android.app.Activity;

import com.google.ads.AdView;


public class PeriodTrackerVault {
	private int cycleLength;
	private int periodLength;
	private int ovulutionday;
	private String weightUnit;
	private String tempuint;
	private boolean pregnant;
	private String themeSelected;
	private String heightUnit;
	private String dateFormat;
	private String appLanguage;
	private String passwordProtection;
	private int profileID;
	private String applicationVersion;
	private String defaultWeightVaule;
	private String defaultHeightVaule;
	private String estimatedDeliveryDate;
	private String pregnancyMessegeFormat;
	private String defaultTempValue;
	private int periodStartNotification;
	private int fertilityNotification;
	private int OvulationNotification;
	private String periodStartNotificationMessage;
	private String dayOfWeek;
	private AdView adView;
	private  Activity activity;
	private  boolean  isAveraged;
	
	/**
	 * @return the isAveraged
	 */
	protected boolean isAveraged() {
		return isAveraged;
	}

	/**
	 * @param isAveraged the isAveraged to set
	 */
	protected void setAveraged(boolean isAveraged) {
		this.isAveraged = isAveraged;
	}

	/**
	 * @return the activity
	 */
	protected Activity getActivity() {
		return activity;
	}

	/**
	 * @param activity the activity to set
	 */
	protected void setActivity(Activity activity) {
		this.activity = activity;
	}

	/**
	 * @return the adView
	 */
	protected AdView getAdView() {
		return adView;
	}

	/**
	 * @param adView the adView to set
	 */
	protected void setAdView(AdView adView) {
		this.adView = adView;
	}

	/**
	 * @return the dayOfWeek
	 */
	protected final String getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * @param dayOfWeek the dayOfWeek to set
	 */
	protected  final void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	/**
	 * @return the periodStartNotificationMessage
	 */
	protected final String getPeriodStartNotificationMessage() {
		return periodStartNotificationMessage;
	}

	/**
	 * @param periodStartNotificationMessage the periodStartNotificationMessage to set
	 */
	protected final void setPeriodStartNotificationMessage(
			String periodStartNotificationMessage) {
		this.periodStartNotificationMessage = periodStartNotificationMessage;
	}

	/**
	 * @return the fertilityNotificationMessage
	 */
	protected final String getFertilityNotificationMessage() {
		return fertilityNotificationMessage;
	}

	/**
	 * @param fertilityNotificationMessage the fertilityNotificationMessage to set
	 */
	protected  final void setFertilityNotificationMessage(
			String fertilityNotificationMessage) {
		this.fertilityNotificationMessage = fertilityNotificationMessage;
	}

	/**
	 * @return the ovulationNotificationMessage
	 */
	protected final String getOvulationNotificationMessage() {
		return OvulationNotificationMessage;
	}
	
	/**
	 * @param ovulationNotificationMessage the ovulationNotificationMessage to set
	 */
	protected final void setOvulationNotificationMessage(
			String ovulationNotificationMessage) {
		OvulationNotificationMessage = ovulationNotificationMessage;
	}

	private String fertilityNotificationMessage;
	private String OvulationNotificationMessage;
	private String PregnancyNotificationMessage;
	
	public String getPregnancyNotificationMessage()
	{
		return PregnancyNotificationMessage;
	}

	public void setPregnancyNotificationMessage(String pregnancyNotificationMessage)
	{
		PregnancyNotificationMessage = pregnancyNotificationMessage;
	}

	protected final String getDefaultTempValue() {
		return defaultTempValue;
	}

	protected final void setDefaultTempValue(String defaultTempValue) {
		this.defaultTempValue = defaultTempValue;
	}

	/**
	 * @return the pregnancyMessegeFormat
	 */
	protected final String getPregnancyMessegeFormat() {
		return pregnancyMessegeFormat;
	}

	/**
	 * @param pregnancyMessegeFormat the pregnancyMessegeFormat to set
	 */
	protected final void setPregnancyMessegeFormat(String pregnancyMessegeFormat) {
		this.pregnancyMessegeFormat = pregnancyMessegeFormat;
	}

	/**
	 * @return the estimatedDeliveryDate
	 */
	protected final String getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	/**
	 * @param estimatedDeliveryDate the estimatedDeliveryDate to set
	 */
	protected final void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}

	/**
	 * @return the defaultHeightVaule
	 */
	protected final String getDefaultHeightVaule() {
		return defaultHeightVaule;
	}

	/**
	 * @param defaultHeightVaule the defaultHeightVaule to set
	 */
	protected final void setDefaultHeightVaule(String defaultHeightVaule) {
		this.defaultHeightVaule = defaultHeightVaule;
	}

	/**
	 * @return the defaultWeightVaule
	 */
	protected final String getDefaultWeightVaule() {
		return defaultWeightVaule;
	}

	/**
	 * @param defaultWeightVaule the defaultWeightVaule to set
	 */
	protected final void setDefaultWeightVaule(String defaultWeightVaule) {
		this.defaultWeightVaule = defaultWeightVaule;
	}

	

	/**
	 * @return the version
	 */
	protected final String getApplicationVersion() {
		return applicationVersion;
	}

	/**
	 * @param version the version to set
	 */
	protected final void setApplicationVersion(String version) {
		this.applicationVersion = version;
	}

	/**
	 * @return the profileID
	 */
	protected final int getProfileID() {
		return profileID;
	}

	/**
	 * @param profileID
	 *          the profileID to set
	 */
	protected final void setProfileID(int profileID) {
		this.profileID = profileID;
	}

	/**
	 * @return the dateFormat
	 */
	protected final String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat
	 *          the dateFormat to set
	 */
	protected final void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @return the appLanguage
	 */
	protected final String getAppLanguage() {
		return appLanguage;
	}

	/**
	 * @param appLanguage
	 *          the appLanguage to set
	 */
	protected final void setAppLanguage(String appLanguage) {
		this.appLanguage = appLanguage;
	}

	/**
	 * @return the passwordProtection
	 */
	protected final String getPasswordProtection() {
		return passwordProtection;
	}

	/**
	 * @param passwordProtection
	 *          the passwordProtection to set
	 */
	protected final void setPasswordProtection(String passwordProtection) {
		this.passwordProtection = passwordProtection;
	}

	/**
	 * @return the heightUnit
	 */
	protected final String getHeightUnit() {
		return heightUnit;
	}

	/**
	 * @param heightUnit
	 *          the heightUnit to set
	 */
	protected final void setHeightUnit(String heightUnit) {
		this.heightUnit = heightUnit;
	}

	/**
	 * @return the ovulutionday
	 */
	protected final int getOvulutionday() {
		return ovulutionday;
	}

	/**
	 * @param ovulutionday
	 *          the ovulutionday to set
	 */
	protected final void setOvulutionday(int ovulutionday) {
		this.ovulutionday = ovulutionday;
	}

	/**
	 * @return the wieghtUnit
	 */
	protected final String getWieghtUnit() {
		return weightUnit;
	}

	/**
	 * @param wieghtUnit
	 *          the wieghtUnit to set
	 */
	protected final void setWeightUnit(String wieghtUnit) {
		this.weightUnit = wieghtUnit;
	}

	/**
	 * @return the tempuint
	 */
	protected final String getTempuint() {
		return tempuint;
	}

	/**
	 * @param tempuint
	 *          the tempuint to set
	 */
	protected final void setTempuint(String tempuint) {
		this.tempuint = tempuint;
	}

	/**
	 * @return the pregnant
	 */
	protected final boolean getPregnant() {
		return pregnant;
	}

	/**
	 * @param pregnant
	 *          the pregnant to set
	 */
	protected final void setPregnant(boolean pregnant) {
		this.pregnant = pregnant;
	}

	/**
	 * @return the themeSelected
	 */
	protected final String getThemeSelected() {
		return themeSelected;
	}

	/**
	 * @param themeSelected
	 *          the themeSelected to set
	 */
	protected final void setThemeSelected(String themeSelected) {
		this.themeSelected = themeSelected;
	}

	/**
	 * @return the cycleLength
	 */
	protected final int getCycleLength() {
		return cycleLength;
	}

	/**
	 * @param cycleLength
	 *          the cycleLength to set
	 */
	protected final void setCycleLength(int cycleLength) {
		this.cycleLength = cycleLength;
	}

	/**
	 * @return the periodLength
	 */
	protected final int getPeriodLength() {
		return periodLength;
	}

	/**
	 * @param periodLength
	 *          the periodLength to set
	 */
	protected final void setPeriodLength(int periodLength) {

		this.periodLength = periodLength;
	}
	protected final void setPeriodStartNotification(int days) {

		this.periodStartNotification = days;
	}
	protected final void setFeritilyNotification(int days) {

		this.fertilityNotification = days;
	}
	protected final void setOvulationNotification(int days) {

		this.OvulationNotification = days;
	}
	protected final int getPeriodStartNotification() {
		return periodStartNotification;
	}
	protected final int getFertilityNotification() {
		return fertilityNotification;
	}
	
	protected final int getOvulationNotification() {
		return OvulationNotification;
	}
}
