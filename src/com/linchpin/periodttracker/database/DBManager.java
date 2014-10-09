package com.linchpin.periodttracker.database;

import android.content.ContentResolver;
import android.net.Uri;

public class DBManager
{
	public static String		AUTHORITY							= "com.linchpin.periodttracker.database";
	public static final String	BASE_DIR_TYPE						= "vnd.android.cursor.dir/vnd.periodtracker";
	public static final String	BASE_ITEM_TYPE						= "vnd.android.cursor.item/vnd.periodtracker";
	
	
	//Table Names
		public static String		TABLE_PERIOD_TRACKER				= "Period_Track";
		public static String		TABLE_PERIOD_DAY_DETAILS			= "Day_Details";
		public static String		TABLE_PERIOD_MOOD					= "Mood";
		public static String		TABLE_PERIOD_MOOD_SELECTED			= "Mood_Selected";
		public static String		TABLE_PERIOD_SYMPTOMS				= "Symptoms";
		public static String		TABLE_PERIOD_SYMPTOMS_SELECTED		= "Symptom_Selected";
		public static String		TABLE_PERIOD_MEDICINE				= "Medicine";
		public static String		TABLE_PERIOD_USER_PROFILE			= "User_Profile";
		public static String		TABLE_PERIOD_APPLICATION_SETTINGS	= "Application_Settings";
		public static String		TABLE_PERIOD_APPLICATION_CONSTANTS	= "Application_Constants";
		public static String		TABLE_PERIOD_SKINS					= "Skins";
		public static String		TABLE_PERIOD_NOTIFICATIONS			= "Notificatons";
	///URIS
	 public final static Uri URI_PERIOD_TRACKER = Uri.parse(ContentResolver.SCHEME_CONTENT + "://"
	            + AUTHORITY + "/" + TABLE_PERIOD_TRACKER );
	 public final static Uri URI_PERIOD_TRACKER_ID = Uri.parse(ContentResolver.SCHEME_CONTENT + "://"
	            + AUTHORITY + "/" + TABLE_PERIOD_TRACKER + "/");
	
	 public final static Uri URI_DAY_DETAILS = Uri.parse(ContentResolver.SCHEME_CONTENT + "://"
	            + AUTHORITY + "/" + TABLE_PERIOD_DAY_DETAILS );
	 public final static Uri URI_DAY_DETAILS_ID = Uri.parse(ContentResolver.SCHEME_CONTENT + "://"
	            + AUTHORITY + "/" + TABLE_PERIOD_DAY_DETAILS + "/");
	 
	 public final static String PERIOD_TRACKER_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".period_tracker";	 
	 public final static String PERIOD_TRACKER_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".period_tracker";
	 public final static String DAY_DETAILS_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".day_details";	 
	 public final static String DAY_DETAILS_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".day_details";
	 public final static String MOOD_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".mood";	 
	 public final static String MOOD_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".mood";
	 public final static String MOOD_SELECTED_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".mood_selected";	 
	 public final static String MOOD_SELECTED_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".mood_selected";
	 public final static String SYMPTIOMS_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".symptioms";	 
	 public final static String SYMPTIOMS_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".symptioms";
	 public final static String SYMPTIOMS_SELECTED_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".symptioms_selected";	 
	 public final static String SYMPTIOMS_SELECTED_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".symptioms_selected";
	 public final static String MEDICINE_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".medicine";	 
	 public final static String MEDICINE_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".medicine";
	 public final static String USER_PROFILE_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".profile";	 
	 public final static String USER_PROFILE_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".profile";
	 public final static String SKINS_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".skin";	 
	 public final static String SKINS_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".skin";
	 public final static String NOTIFICATIONS_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".notification";	 
	 public final static String NOTIFICATIONS_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".notification";
	 public final static String APPLICATION_SETTINGS_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".setting";	 
	 public final static String APPLICATION_SETTINGS_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".setting";
	 public final static String APPLICATION_CONSTANTS_CONTENT_TYPE = DBManager.BASE_DIR_TYPE + ".setting_cons";	 
	 public final static String APPLICATION_CONSTANTS_CONTENT_ITEM_TYPE = DBManager.BASE_ITEM_TYPE + ".setting_cons"; 
	
	

	
	

}
