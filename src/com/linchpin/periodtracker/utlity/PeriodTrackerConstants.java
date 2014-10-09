package com.linchpin.periodtracker.utlity;

import java.util.Date;

public class PeriodTrackerConstants {

	// Application constant key

	public static final String APPLICATION_CONSTANT_VERSION = "1.0";
	public static final String APPLICATION_CONSTANT_VERSION_KEY = "A_C";

	// Application settings key

	public static final String APPLICATION_SETTINGS_TEMP_UNIT_KEY = "D_TU";
	public static final String APPLICATION_SETTINGS_WEIGH_UNIT_KEY = "D_WU";
	public static final String APPLICATION_SETTINGS_HEIGH_UNIT_KEY = "D_HU";
	public static final String APPLICATION_SETTINGS_PASSWORD_STRING_KEY = "D_PP";
	public static final String APPLICATION_SETTINGS_PERIOD_LENGTH_KEY = "D_PL";
	public static final String APPLICATION_SETTINGS_CYCLE_LENGTH_KEY = "D_CL";
	public static final String APPLICATION_SETTINGS_OVULATION_LENGTH_KEY = "D_OL";
	public static final String APPLICATION_SETTINGS_DATE_FORMAT_KEY = "D_DF";
	public static final String APPLICATION_SETTINGS_APP_LANGUAGE_KEY = "D_AL";
	public static final String APPLICATION_SETTING_PREGNANT_MODE = "D_PM";
	public static final String APPLICATION_SETTING_SKIN_SELECTED = "D_SS";
	public static final String APLLICATION_SETTING_EMAIL_KEY = "E_ID";
	public static final String APPLICATION_SETTING_SECURITY_QUESTION = "A_SQ";
	public static final String APPLICATION_SETTING_SECURITY_ANSWER = "A_SA";
	public static final String APPLICATION_SETTINGS_WEIGH_VALUE_KEY = "D_WV";
	public static final String APPLICATION_SETTINGS_HEIGH_VALUE_KEY = "D_HV";
	public static final String APPLICATION_SETTINGS_TEMPERATURE_VALUE_KEY = "D_TV";
	public static final String APPLICATION_SETTINGS_ESTIMATED_DATE_VALUE_KEY = "E_DV";
	public static final String APPLICATION_SETTINGS_PREGNANCY_MESSAGE_FORMAT_KEY = "PM_MF";
	public static final String APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_KEY="PS_N";
	public static final String APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_KEY="FS_N";
	public static final String APPLICATION_SETTINGS_OVULATION_NOTIFICTION_KEY="OS_N";
	public static final String APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_MESSAGE_KEY="PSNM";
	public static final String APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_MESSAGE_KEY="FSNM";
	public static final String APPLICATION_SETTINGS_PREGNANCY_NOTIFICTION_MESSAGE_KEY="PNM";
	public static final String APPLICATION_SETTINGS_OVULATION_NOTIFICTION_MESSAGE_KEY="OSNM";
	public static final String APPLICATION_SETTINGS_DAY_OF_WEEK_KEY="DOW";
	public static final String APPLICATION_SETTINGS_AVERAGE_LENGTHS="DAVGL";
	
	
	// Default value of Application settings

	public static final int DEFAULT_PROFILE_ID = 1;
	public static final String DEFAULT_PROFILE_FIRST_NAME = "priyanka";
	public static final String DEFAULT_HIEGHT_VAULE = "0";
	public static final String DEFAULT_WEIGHT_VALUE = "0";
	public static final String DEFAULT_TEMP_STRING_VALUE = "98.6";
	public static final String APPLICATION_SETTINGS_DEFAULT_PASSWORD = "";
	public static final String SYSTEM_SYMTOM_TYPE = "system";
	public static final String SYSTEM_MOOD_TYPE = "system";
	public static final int CYCLE_LENGTH = 28;
	public static final int DEFAULT_NOTIFICATION_DAY = 2;
	public static final int PERIOD_LENGTH = 4;
	public static final boolean PREGNANCY_MODE_ON = false;
	public static final boolean IS_AVERAGE_LENGTHS= true;
	public static final String DEFAULT_TEMPRATURE_UNIT = "°F";
	public static final String DEFAULT_WEIGHT_UNIT = "lb";
	public static final String DEFAULT_HIEGHT_UNIT = "cm";
	public static final int OVULATION_DAY = 14;
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	// Utility.getDefaultDateFormatOFDeviece(PeriodTrackerObjectLocator.getInstance().getContext());
	//
	public static final String DEVICE_LANUGAGE = Utility.getDefaultLanuageOFDeviece();
	public static final int PREDICTION_COUNT = 6;
	public static final int NUMBER_OF_DAYS_IN_FERTILE_STARTS_DAYS = 7;
	public static final int NUMBER_OF_DAYS_IN_FERTILE_END_DAYS = 18;

	public static final long MILLI_SECONDS = 86400000;
	public static final String SKIN_SELECTED = "";
	
	public static final long NULL_DATE = Utility.getNUllDate();
	public static final Date CURRENT_DATE = Utility.setHourMinuteSecondZero(new Date());
	public static final int PROTECTION = 0;

	public static final String PAST_PERIOD_RECORD_LIST_FRAGMENT = "PastPeriodRecordListFragment";
	public static final String PERDICTION_PERIOD_LIST_FRAGMNET = "PerdictionPeriodListFragment";
	public static final String PAST_FERTILE_RECORDS = "PastFertileRecords";
	public static final String PERDICTION_FERTILE_RECORDS = "PerdictionFertileRecords";
	public static final String PAST_OVULATION_RECORDS = "PastOvulationRecords";
	public static final String PERDICTION_OVULATION_RECORDS = "PerdictionOvulationRecords";

	public static final String PERIOD_LOG_PAGER_VIEW = "PeriodLogPagerView";
	public static final String ADD_NOTE_VIEW = "AddNoteView";
	public static final String PERIOD_LOG_FRAGMENT = "periodLogFragment";
	public static final String PILLS_FRAGEMNT = "PillsFragment";
	public static final String SYMTOM_BASE_FRAGMENT = "SymtompsBaseFragment";
	public static final String MOOD_BASE_FRAGMENT = "MoodBaseFragment";
	public static final String ADD_NOTE_FRAGMENT = "AddNoteFragments";
	public static final String WEIGHT_TEMPERATURE_FRAGEMENT = "WeightAndTemperatureFragment";

	public static final Float MAX_TEMP_IN_CELSIUS = (float) 43.3;
	public static final Float MAX_HEIGHT_IN_INCHES = (float) 96;
	public static final Float MAX_HEIGHT_IN_CENTIMETER = 243.8f;
	public static final Float MAX_WEIGHT_IN_KG = 250f;
	public static final Float MAX_WEIGHT_IN_LB = 552f;
	public static final Float MAX_TEMP_IN_FEHRENHIET = 110f;
	public static final Float DEFAULT_WEIGHT_VAULE_IN_LB =88.1f;
	public static final Float DEFAULT_HEIGHT_VALUE_IN_CM=121.9f;
	public static final Float DEFAULT_TEMP_VALUE_IN_F=98.6f;
	public static final String DEFAULT_PREGNANCY_MESSAGE_FORMAT="Days To Baby";
	public static final Float MIN_TEMP_IN_FEHRENHIET = 90f;
	public static final Float MIN_TEMP_IN_CELSIUS = 32.2f;
	public static final Float MIN_HEIGHT_IN_INCHES = 36f;
	public static final Float MIN_HEIGHT_IN_CENTIMETER = 91.4f;
	public static final Float MIN_WEIGHT_IN_KG = 20f;
	public static final Float MIN_WEIGHT_IN_LB = 44.1f;

}
