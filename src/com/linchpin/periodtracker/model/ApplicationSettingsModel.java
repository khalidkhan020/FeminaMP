package com.linchpin.periodtracker.model;

import android.database.Cursor;

public class ApplicationSettingsModel implements PeriodTrackerModelInterface {

	public static final String APPLICATION_SETTINGS = "Application_Settings";
	public static final String APPLICATION_SETTINGS_PREGNANT = "Pregnant";
	public static final String APPLICATION_SETTINGS_DEFAULT_CYCLE_LENGTH = "Default_Cycle_Length";
	public static final String APPLICATION_SETTINGS_DEFAULT_PERIOD_LENGTH = "Default_Period_Length";
	public static final String APPLICATION_SETTINGS_OVULATION_DAY = "Default_Ovulation_Day";
	public static final String APPLICATION_SETTINGS_WEIGHT = "Default_Weight_Unit";
	public static final String APPLICATION_SETTINGS_TEMPERATURE = "Default_Temperature_Unit";
	public static final String APPLICATION_SETTINGS_SKIN_SELECTED = "Skin_Selected";
	public static final String APPLICATION_SETTINGS_DEFAULT_LANGUAGE = "Default_App_Language";
	public static final String APPLICATION_SETTINGS_DEFAULT_DATE_FORMAT = "Default_Date_Format";
	public static final  String APPLICATION_SETTINGS_DEFAULT_HIEGHT="Default_Height_Unit";

	private Boolean pregnant;
	private int defaultCycleLength, defaultPeriodLength, defaultOvulationDay;
	private String weight, temperature;
	private String skinSelected;
	private String heightUnit , defaultApplicationLanuage, defaultDateFormat;
	


	
	public ApplicationSettingsModel () {
		
	}



	public ApplicationSettingsModel(Boolean pregnant, int defaultCycleLength,
			int defaultPeriodLength, int defaultOvulationDay,
			String defaultWeight, String defaultTemperature,String defaultHieghtUnit,String defaultLanguage, String defaultDateFormat,
			String skinSelected) {

		this.pregnant = pregnant;
		this.defaultCycleLength = defaultCycleLength;
		this.defaultPeriodLength = defaultPeriodLength;
		this.defaultOvulationDay = defaultOvulationDay;
		this.weight = defaultWeight;
		this.temperature = defaultTemperature;
		this.skinSelected = skinSelected;
		this.defaultApplicationLanuage= defaultLanguage;
		this.defaultDateFormat= defaultDateFormat;
		this.heightUnit= defaultHieghtUnit;
		
	}

	
	
	/**
	 * @return the heightUnit
	 */
	public final String getHeightUnit() {
		return heightUnit;
	}
	/**
	 * @param heightUnit the heightUnit to set
	 */
	public final void setHeightUnit(String heightUnit) {
		this.heightUnit = heightUnit;
	}
	
	/**
	 * @return the pregnant
	 */
	public final Boolean getPregnant() {
		return pregnant;
	}

	/**
	 * @param pregnant
	 *            the pregnant to set
	 */
	public final void setPregnant(Boolean pregnant) {
		this.pregnant = pregnant;
	}

	/**
	 * @return the defaultCycleLength
	 */
	public final int getDefaultCycleLength() {
		return defaultCycleLength;
	}

	/**
	 * @param defaultCycleLength
	 *            the defaultCycleLength to set
	 */
	public final void setDefaultCycleLength(int defaultCycleLength) {
		this.defaultCycleLength = defaultCycleLength;
	}

	/**
	 * @return the defaultPeriodLength
	 */
	public final int getDefaultPeriodLength() {
		return defaultPeriodLength;
	}

	/**
	 * @param defaultPeriodLength
	 *            the defaultPeriodLength to set
	 */
	public final void setDefaultPeriodLength(int defaultPeriodLength) {
		this.defaultPeriodLength = defaultPeriodLength;
	}

	/**
	 * @return the defaultOvulationDay
	 */
	public final int getDefaultOvulationDay() {
		return defaultOvulationDay;
	}

	/**
	 * @param defaultOvulationDay
	 *            the defaultOvulationDay to set
	 */
	public final void setDefaultOvulationDay(int defaultOvulationDay) {
		this.defaultOvulationDay = defaultOvulationDay;
	}

	/**
	 * @return the weight
	 */
	public final String getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public final void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * @return the temperature
	 */
	public final String getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature
	 *            the temperature to set
	 */
	public final void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the skinSelected
	 */
	public final String getSkinSelected() {
		return skinSelected;
	}

	/**
	 * @param skinSelected
	 *            the skinSelected to set
	 */
	public final void setSkinSelected(String skinSelected) {
		this.skinSelected = skinSelected;
	}

	@Override
	public PeriodTrackerModelInterface createModel(Cursor cursor) {
		// TODO Auto-generated method stub
	
					
		return this;
	}
	/**
	 * @return the defaultApplicationLanuage
	 */
	public final String getDefaultApplicationLanuage() {
		return defaultApplicationLanuage;
	}



	/**
	 * @param defaultApplicationLanuage the defaultApplicationLanuage to set
	 */
	public final void setDefaultApplicationLanuage(String defaultApplicationLanuage) {
		this.defaultApplicationLanuage = defaultApplicationLanuage;
	}



	/**
	 * @return the defaultDateFormat
	 */
	public final String getDefaultDateFormat() {
		return defaultDateFormat;
	}



	/**
	 * @param defaultDateFormat the defaultDateFormat to set
	 */
	public final void setDefaultDateFormat(String defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}
}
