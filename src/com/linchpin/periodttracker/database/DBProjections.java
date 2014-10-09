package com.linchpin.periodttracker.database;

public class DBProjections
{
	
	// Common Fields
	public static String	ID								= "Id";

	// Period Tracker Fields
	public static String	PT_PROFILE_ID					= "Profile_Id";
	public static String	PT_START_DATE					= "Start_Date";
	public static String	PT_END_DATE						= "End_Date";
	public static String	PT_CYCLE_LENGTH					= "Cycle_Length";
	public static String	PT_PERIOD_LENGTH				= "Period_Length";
	public static String	PT_PREGENCY						= "Pregnancy";
	public static String	PT_PREGENCY_SUPPORT				= "Pregnancy_Supportable";

	// Table Day Details
	public static String	DD_DATE							= "Date";
	public static String	DD_NOTE_DESCRIPTION				= "Note_Description";
	public static String	DD_INTIMATE						= "Intimate";
	public static String	DD_PROCTION						= "Protection";
	public static String	DD_WEIGHT						= "Weight";
	public static String	DD_TEMPRATURE					= "Temperature";
	public static String	DD_HEIGHT						= "Height";
	// Table Mood
	public static String	MOOD_IMAGE_URI						= "Image_Uri";
	public static String	MOOD_LABLE						= "Mood_Label_Key";
	public static String	MOOD_TYPE						= "Mood_Type";
	//Mood Selected

	public static String	DD_ID				= "Day_Details_Id";
	public static String	MOOD_ID				= "Mood_Id";
	public static String	MOOD_WEIGHT			= "Mood_Weight";
	
	// symptoms
	public static String	SY_IMAGE_URI		= "Image_Uri";
	public static String	SY_LABLE_KEY		= "Symptom_Label_Key";
	public static String	SY_TYPE				= "Symptom_Type";
	
	// Sym,ptoms selected
	public static String	SY_ID				= "Symptom_Id";
	public static String	SY_WEIGHT			= "Symptom_Weight";
	
	// Medicine
	public static String	MED_NAME			= "Medicine_Name";
	public static String	MED_QUANTITY		= "Medicine_Quantity";
	//User profile
	public static String	UP_FIRST_NAME		= "First_Name";
	public static String	UP_LAST_NAME		= "Last_Name";
	public static String	UP_NAME				= "User_Name";
	public static String	UP_PASSWORD			= "Password";
	//Application settings
	public static String AS_KEY_VALUE="KeyValues";
	//Application constants
	public static String AC_KEY="Key";
	//Skins
	public static String SK_URI="Uri" ;
	//notifications
	public static String NOTY_ID="Notification_Id" ;
	public static String NOTY_TYPE="Notification_Type";
	
	//projections
	
	public final static String[]	PERIOD_TRACKER_FULL_PROJECTION	=
																		{ DBProjections.ID, DBProjections.PT_PROFILE_ID, DBProjections.PT_START_DATE, DBProjections.PT_END_DATE,
			DBProjections.PT_CYCLE_LENGTH, DBProjections.PT_PERIOD_LENGTH, DBProjections.PT_PREGENCY, DBProjections.PT_PREGENCY_SUPPORT };
	
	public final static String[]	DAY_DETAILS_FULL_PROJECTION		=
																		{ DBProjections.ID, DBProjections.DD_DATE, DBProjections.DD_NOTE_DESCRIPTION, DBProjections.DD_INTIMATE,
			DBProjections.DD_PROCTION, DBProjections.DD_TEMPRATURE, DBProjections.DD_HEIGHT, DBProjections.PT_PROFILE_ID };
}
