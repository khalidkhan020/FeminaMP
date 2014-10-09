package com.linchpin.periodttracker.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.linchpin.periodtracker.model.ApplicationConstants;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.ApplicationSettingsModel;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.MoodDataModel;
import com.linchpin.periodtracker.model.MoodSelected;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.Pills;
import com.linchpin.periodtracker.model.SymptomsModel;
import com.linchpin.periodtracker.model.SymtomsSelectedModel;
import com.linchpin.periodtracker.model.UserProfileModel;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.utlity.ValidationFunction;

public class PeriodTrackerDBHandler extends SQLiteOpenHelper
{

	public static SQLiteDatabase		periodTrackerDB;

	public static final String	DATABASE_NAME						= "PeriodTracker.db";
	public static final int		DATABASE_VERSION					= 2;

	private final String		CREATE_TABLE_PERIOD_TRACK			= 
			"CREATE TABLE " 							+ 
			DBManager.TABLE_PERIOD_TRACKER 				+ 			
			" (" 										+ 
				DBProjections.ID 							+ 	" INTEGER PRIMARY KEY  AUTOINCREMENT  , "	+ 			
				DBProjections.PT_PROFILE_ID 				+ 	" INTEGER, " 								+ 
				DBProjections.PT_START_DATE 				+ 	" NUMERIC  NOT NULL , "						+
				DBProjections.PT_END_DATE 					+ 	" NUMERIC ,  " 								+ 
				DBProjections.PT_CYCLE_LENGTH 				+ 	" INTEGER, "								+ 
				DBProjections.PT_PERIOD_LENGTH 				+ 	" INTEGER , " 								+ 										
				DBProjections.PT_PREGENCY 					+ 	" BOOL ,"									+ 
				DBProjections.PT_PREGENCY_SUPPORT 			+ 	" BOOL" 									+ 
			")";

	private final String		CREATE_TABLE_DAY_DETALS				= 
			"CREATE TABLE " 								+ 
			DBManager.TABLE_PERIOD_DAY_DETAILS 				+ 
			" (" 											+ 
				DBProjections.ID							+ " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " 	+ 
				DBProjections.DD_DATE 						+ " NUMERIC NOT NULL , "								+ 
				DBProjections.DD_NOTE_DESCRIPTION 			+ " VARCHAR, " 											+ 
				DBProjections.DD_INTIMATE 					+ " BOOL, " 											+ 
				DBProjections.DD_PROCTION					+ " INTEGER, " 											+
				DBProjections.DD_WEIGHT						+ " NUMERIC , " 										+ 
				DBProjections.DD_TEMPRATURE 				+ " NUMERIC, " 											+ 
				DBProjections.DD_HEIGHT 					+ " NUMERIC ,"											+
				DBProjections.PT_PROFILE_ID 				+ " INTEGER" 											+ 
			")";

	private final String		CREATE_TABLE_MOOD	= 
			"CREATE TABLE " + 
			DBManager.TABLE_PERIOD_MOOD 			+ 
			" (" 									+ 
				DBProjections.ID 					+ " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "	+ 
				DBProjections.MOOD_IMAGE_URI 		+ " VARCHAR NOT NULL , " 								+ 
				DBProjections.MOOD_LABLE 			+ " VARCHAR NOT NULL , "								+ 
				DBProjections.MOOD_TYPE 			+ " VARCHAR NOT NULL , " 								+ 
				DBProjections.PT_PROFILE_ID 		+ " INTEGER" 											+ 
			")";

	private final String		CREATE_TABLE_MOOD_SELECTED			= 
			"CREATE TABLE " + 
			DBManager.TABLE_PERIOD_MOOD_SELECTED + 
			" (" + 
				DBProjections.ID			+ " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " + 
				DBProjections.DD_ID 		+ " INTEGER NOT NULL , " + 
				DBProjections.MOOD_ID		+ " INTEGER NOT NULL , " + 
				DBProjections.MOOD_WEIGHT 	+ " INTEGER" + 
			")";

	private final String		CREATE_TABLE_SYMPTOMS				= 
			"CREATE TABLE " + 
			DBManager.TABLE_PERIOD_SYMPTOMS + 
			" (" + 
				DBProjections.ID 			+ " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "	+ 
				DBProjections.SY_IMAGE_URI 	+ " VARCHAR NOT NULL , " + 
				DBProjections.SY_LABLE_KEY 	+ " VARCHAR NOT NULL , "		+ 
				DBProjections.SY_TYPE 		+ " VARCHAR NOT NULL , " + 
				DBProjections.PT_PROFILE_ID + " INTEGER" + 
			")";

	private final String		CREATE_TABLE_SYMPTOMS_SELECTED		= 
			"CREATE TABLE " + 
			DBManager.TABLE_PERIOD_SYMPTOMS_SELECTED + 
			" (" + 
				DBProjections.ID		+ " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " + 
				DBProjections.SY_ID 	+ " INTEGER NOT NULL , " + 
				DBProjections.DD_ID		+ " INTEGER NOT NULL , " + 
				DBProjections.SY_WEIGHT + " INTEGER NOT NULL " + 
			")";

	private final String		CREATE_TABLE_MEDICINE				= 
			"CREATE TABLE " 					+ 
			DBManager.TABLE_PERIOD_MEDICINE 	+ 
			" (" 								+ 
				DBProjections.ID 				+ " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "+ 
				DBProjections.MED_NAME 			+ " VARCHAR NOT NULL ," 							+ 
				DBProjections.MED_QUANTITY 		+ " INTEGER , " 									+ 
				DBProjections.DD_ID				+ " INTEGER NOT NULL " 								+ 
			")";

	private final String		CREATE_TABLE_USER_PROFILE			= 
			"CREATE TABLE " + 
			DBManager.TABLE_PERIOD_USER_PROFILE + 
			" (" + 
				DBProjections.ID+ " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " + 
				DBProjections.UP_FIRST_NAME + " VARCHAR NOT NULL , "+ 
				DBProjections.UP_LAST_NAME + " VARCHAR NOT NULL , " + 
				DBProjections.UP_NAME + " VARCHAR NOT NULL, "	+ 
				DBProjections.UP_PASSWORD + " VARCHAR NOT NULL" + 
			")";

	private final String		CREATE_TABLE_APPLICATION_SETTINGS	= 
			"CREATE TABLE " + 
			DBManager.TABLE_PERIOD_APPLICATION_SETTINGS + 
			" (" + 
			DBProjections.ID + " VACHAR NOT NULL ,"+ 
				DBProjections.AS_KEY_VALUE + " VARCHAR, " + 
				DBProjections.PT_PROFILE_ID + " INTEGER NOT NULL, PRIMARY KEY " +
				"("+		
					DBProjections.ID + "," + 
					DBProjections.PT_PROFILE_ID + 
				")" + 
			")";

	private final String		CREATE_TABLE_APPLICATION_CONSTANTS	= "CREATE TABLE " + DBManager.TABLE_PERIOD_APPLICATION_CONSTANTS + " (" + DBProjections.ID + " VARCHAR PRIMARY KEY   , "
																			+ DBProjections.AC_KEY + " VARCHAR NOT NULL " + ")";

	private final String		CREATE_TABLE_SKINS					= "CREATE TABLE " + DBManager.TABLE_PERIOD_SKINS + " (" + DBProjections.ID + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
																			+ DBProjections.SK_URI + " VARCHAR NOT NULL " + ")";

	private final String		CREATE_TABLE_NOTIFICATION			= "CREATE TABLE " + DBManager.TABLE_PERIOD_NOTIFICATIONS + " (" + DBProjections.ID + " INTEGER PRIMARY KEY  AUTOINCREMENT , "
																			+ DBProjections.NOTY_ID + " NUMERIC , " + DBProjections.NOTY_TYPE + " String " + ")";

	public PeriodTrackerDBHandler(Context context)
	{

		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	/**
	 * @return the periodTrackerDBO
	 */
	public final SQLiteDatabase getPeriodTrackerDB()
	{
		return periodTrackerDB;
	}

	/**
	 * @param periodTrackerDB
	 *            the periodTrackerDB to set
	 */
	public void setPeriodTrackerDB(SQLiteDatabase periodTrackerDB)
	{
		this.periodTrackerDB = periodTrackerDB;
	}

	public void openPeriodTrackerDB()
	{
		try
		{
			if (null == periodTrackerDB || !periodTrackerDB.isOpen()) 
			{
				
				periodTrackerDB = getWritableDatabase();
			}
		}
		catch (SQLiteException exception)
		{
			exception.printStackTrace();
		}
	}

	public void closePeriodTrackerDB()
	{
		if (null != periodTrackerDB && periodTrackerDB.isOpen())
		{
			periodTrackerDB.close();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase database)
	{
		try
		{
			 setPeriodTrackerDB(database);
			database.execSQL(CREATE_TABLE_PERIOD_TRACK);
			database.execSQL(CREATE_TABLE_DAY_DETALS);
			database.execSQL(CREATE_TABLE_MOOD);
			database.execSQL(CREATE_TABLE_MOOD_SELECTED);
			database.execSQL(CREATE_TABLE_SYMPTOMS);
			database.execSQL(CREATE_TABLE_SYMPTOMS_SELECTED);
			database.execSQL(CREATE_TABLE_USER_PROFILE);
			database.execSQL(CREATE_TABLE_MEDICINE);
			database.execSQL(CREATE_TABLE_SKINS);
			database.execSQL(CREATE_TABLE_APPLICATION_SETTINGS);
			database.execSQL(CREATE_TABLE_APPLICATION_CONSTANTS);
			database.execSQL(CREATE_TABLE_NOTIFICATION);
			createUserProfile(UserProfileModel.USER_PROFILE, null, database);
			createPeriodLogAppliactionSettings(ApplicationSettingsModel.APPLICATION_SETTINGS, null, database);
			createMultipleRecordMoodBase(MoodDataModel.MOOD, null, database);
			createApplicationConstants(ApplicationConstants.APPLICATION_CONSTANTS, null, database);
			createMultipleRecordSymptomBase(SymptomsModel.SYMTOMS, null, database);

		}
		catch (Exception e)
		{
			Log.d("", "" + e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub

		Log.w(PeriodTrackerDBHandler.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

		/*
		 * if (newVersion == 2) {
		 * 
		 * try { ContentValues contentValues = new ContentValues();
		 * contentValues
		 * .put(PeriodTrackerConstants.APPLICATION_SETTINGS_DAY_OF_WEEK_KEY,
		 * "0"); createRecord(ApplicationSettingModel.APPLICATION_SETTING, null,
		 * contentValues); } catch (Exception e) { Log.d("", "" + e); }
		 * 
		 * }
		 */
		// database.execSQL("DROP TABLE IF EXISTS " + TABLE_Details);

		onCreate(database);

	}
	public Cursor queryOnDb(String Query)
	{
		
		return periodTrackerDB.rawQuery(Query, null);
		
		
	}
	public boolean checkFieldExistance(String table, String field, long l)
	{
		Cursor cursor = null;
		try
		{
			long day=24*3600000;
			long min=l-day;
			l+=day;
			this.openPeriodTrackerDB();
			cursor = periodTrackerDB.rawQuery("SELECT " + field + " FROM " + table + "  WHERE " + field + " >" + min+" AND " + field + " <" + l, null);
			int c=cursor.getCount();
			
			return c > 0;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{	cursor.close();
			this.closePeriodTrackerDB();
		}
		return false;
	}
	public boolean checkFieldExistance(String table, String field,long exclude, long l)
	{
		Cursor cursor = null;
		try
		{
			long day=24*3600000;
			long min=l-day;
			l+=day;
			this.openPeriodTrackerDB();
			cursor = periodTrackerDB.rawQuery("SELECT " + field + " FROM " + table + "  WHERE " + field + "!= " + exclude+" AND " + field + " >" + min+" AND " + field + " <" + l, null);
			int c=cursor.getCount();
			
			return c > 0;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{	cursor.close();
			this.closePeriodTrackerDB();
		}
		return false;
	}
	public boolean checkDateLiesBetween(String table, String field1, String field2,long l)
	{
		Cursor cursor = null;
		try
		{			
			this.openPeriodTrackerDB();
			cursor = periodTrackerDB.rawQuery("SELECT  COUNT( * )  FROM " + table + "  WHERE " + field1 + " <" + l+" AND " + field2 + " >" + l, null);
			int c=cursor.getCount();
			cursor.moveToFirst();
			if(c>0)
				c=cursor.getInt(0);
			return c > 0;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{	cursor.close();
			this.closePeriodTrackerDB();
		}
		return false;
	}
	public boolean checkDateLiesBetween(String table, String field1, String field2,long exclude,long l)
	{
		Cursor cursor = null;
		try
		{			
			this.openPeriodTrackerDB();
			cursor = periodTrackerDB.rawQuery("SELECT  COUNT( * )  FROM " + table + "  WHERE " + field1 + " !=" + exclude+" AND "+ field1 + " <" + l+" AND " + field2 + " >" + l, null);
			int c=cursor.getCount();
			cursor.moveToFirst();
			if(c>0)
				c=cursor.getInt(0);
			return c > 0;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{	cursor.close();
			this.closePeriodTrackerDB();
		}
		return false;
	}
	/**
	 * true=less
	 * false=grater*/
	public boolean compareWithLatest(String table, String field,long l)
	{
		Cursor cursor = null;
		try
		{			
			this.openPeriodTrackerDB();
			cursor = periodTrackerDB.rawQuery("SELECT " + field + "  FROM " + table + " order by " + field + " DESC LIMIT 1 ", null);
			
			if(cursor.getCount()>0)
			{
				cursor.moveToFirst();
				
				return 	Utility.isDateLessThanLatestRecord(new Date(l), new Date(cursor.getLong(0)));
			}
			return false;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{	cursor.close();
			this.closePeriodTrackerDB();
		}
		return false;
	}
	public int isDateBetweenPeriods(long date)
	{
		Date nn=new Date(date);
		Cursor cursor = null;
		int betwen=0;
		try
		{			
			this.openPeriodTrackerDB();
			cursor = periodTrackerDB.rawQuery("SELECT "+ DBProjections.PT_START_DATE+","+DBProjections.PT_END_DATE +"  FROM " + DBManager.TABLE_PERIOD_TRACKER + " Where "+DBProjections.PT_PROFILE_ID+"=" +PeriodTrackerObjectLocator.getInstance().getProfileId()+" order by "  +DBProjections.PT_START_DATE+ " DESC", null);
			
			if(cursor.getCount()>0)			
				cursor.moveToFirst();
			do{
				betwen=Utility.checkDateBetweenDates2(new Date(cursor.getLong(1)), new Date(cursor.getLong(0)), new Date(date));
				if(betwen>0)
					return betwen;
				
			}while(cursor.moveToNext());			
			
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{	cursor.close();
			this.closePeriodTrackerDB();
		}
		return 0;
	}
	
	public long createApplicationConstants(String table, String nullColumnHack, SQLiteDatabase sqLiteDatabase) throws SQLiteException
	{
		ContentValues contentValues = null;
		long rowCreated = -1;
		try
		{
			periodTrackerDB = sqLiteDatabase;
			periodTrackerDB.beginTransaction();
			ApplicationConstants applicationConstants = ValidationFunction.getApplicationConstantsValue();

			contentValues = new ContentValues();
			contentValues.put(ApplicationConstants.APPLICATION_CONSTANT_ID, applicationConstants.getId());
			contentValues.put(ApplicationConstants.APPLICATION_CONSTANT_KEY, applicationConstants.getKey());

			rowCreated = periodTrackerDB.insert(table, nullColumnHack, contentValues);

			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		return rowCreated;

	}

	public long createUserProfile(String table, String nullColumnHack, SQLiteDatabase sqLiteDatabase) throws SQLiteException
	{
		ContentValues contentValues = null;
		long rowCreated = -1;
		try
		{
			periodTrackerDB = sqLiteDatabase;
			periodTrackerDB.beginTransaction();
			UserProfileModel userProfileModel = new UserProfileModel(PeriodTrackerConstants.DEFAULT_PROFILE_ID, "priyanka", "priyanka", "priyanka", "priyanka");
			contentValues = new ContentValues();
			contentValues.put(UserProfileModel.USER_PROFILE_FIRST_NAME, userProfileModel.getFirstName());
			contentValues.put(UserProfileModel.USER_PROFILE_LAST_NAME, userProfileModel.getLastName());
			contentValues.put(UserProfileModel.USER_PROFILE_USER_NAME, userProfileModel.getUserName());
			contentValues.put(UserProfileModel.USER_PROFILE_PASSWORD, userProfileModel.getPassword());
			rowCreated = periodTrackerDB.insert(table, nullColumnHack, contentValues);

			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		return rowCreated;

	}

	public PeriodTrackerModelInterface selectRecord(Class clazz, String query) throws SQLiteException
	{

		PeriodTrackerModelInterface modelObject = null;
		try
		{
			modelObject = (PeriodTrackerModelInterface) clazz.newInstance();
			this.openPeriodTrackerDB();
			if(periodTrackerDB==null)
				periodTrackerDB=this.getWritableDatabase();
			Cursor cursor = periodTrackerDB.rawQuery(query, null);
			if (null != cursor && cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				modelObject.createModel(cursor);
			}
			this.closePeriodTrackerDB();
		}
		catch (Exception exception)
		{
			this.close();
			exception.printStackTrace();
		}

		return modelObject;
	}

	public int getAverageCycleLength(String query) throws SQLiteException
	{

		int i = 0;
		try
		{
			this.openPeriodTrackerDB();
			Cursor cursor = periodTrackerDB.rawQuery(query, null);
			if (null != cursor && cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				i = cursor.getInt(0);
			}
			this.closePeriodTrackerDB();
		}
		catch (Exception exception)
		{
			this.close();
			exception.printStackTrace();
		}

		return i;
	}

	public int getAveragePeriodLength(String query) throws SQLiteException
	{

		int i = 0;
		try
		{
			this.openPeriodTrackerDB();
			Cursor cursor = periodTrackerDB.rawQuery(query, null);
			if (null != cursor && cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				i = cursor.getInt(0);
			}
			this.closePeriodTrackerDB();
		}
		catch (Exception exception)
		{
			this.close();
			exception.printStackTrace();
		}

		return i;
	}

	public PeriodTrackerModelInterface selectRecordForBackup(Class clazz, String query) throws SQLiteException
	{

		PeriodTrackerModelInterface modelObject = null;
		try
		{
			modelObject = (PeriodTrackerModelInterface) clazz.newInstance();
			Cursor cursor = periodTrackerDB.rawQuery(query, null);
			if (null != cursor && cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				modelObject.createModel(cursor);
			}
		}
		catch (Exception exception)
		{
			this.close();
			exception.printStackTrace();
		}

		return modelObject;
	}

	public void executeQueryForCreateTemTable(List<String> strings) throws SQLiteException
	{

		try
		{
			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			for (String string : strings)
			{
				periodTrackerDB.execSQL(string);
			}
			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();

		}
		catch (Exception exception)
		{
			this.close();
			exception.printStackTrace();
		}

	}

	public void executeQueryForDeleteTemTable(List<String> strings) throws SQLiteException
	{

		try
		{
			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			for (String string : strings)
			{
				periodTrackerDB.execSQL(string);
			}
			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
			periodTrackerDB.close();
		}
		catch (Exception exception)
		{
			this.close();
			exception.printStackTrace();
		}

	}

	public boolean executeQueryForInseretDataInTemTable(List<String> tables) throws SQLiteException
	{

		boolean b = false;
		try
		{
			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			for (String string : tables)
			{
				periodTrackerDB.execSQL(string);
				b = true;
			}
			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
		}
		catch (Exception exception)
		{
			this.close();
			exception.printStackTrace();
		}
		return b;
	}

	public List<PeriodTrackerModelInterface> selectMulipleRecord(Class clazz, String query) throws SQLiteException
	{

		List<PeriodTrackerModelInterface> array = new ArrayList<PeriodTrackerModelInterface>();

		PeriodTrackerModelInterface modelObject = null;
		try
		{
			openPeriodTrackerDB();

			Cursor cursor = periodTrackerDB.rawQuery(query, null);
			if (null != cursor && cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					modelObject = (PeriodTrackerModelInterface) clazz.newInstance();
					array.add(modelObject.createModel(cursor));
				}
				while (cursor.moveToNext());
			}

			this.closePeriodTrackerDB();
		}
		catch (Exception exception)
		{
			this.close();
			exception.printStackTrace();
		}

		return array;
	}

	public long createRecord(String table, String nullColumnHack, ContentValues values) throws SQLiteException
	{

		long rowCreated = -1;
		try
		{
			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			rowCreated = periodTrackerDB.insert(table, nullColumnHack, values);
			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
			this.closePeriodTrackerDB();
		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return rowCreated;
	}

	public long createMultipleRecordMoodBase(String table, String nullColumnHack, SQLiteDatabase sqLiteDatabase) throws SQLiteException
	{
		ContentValues contentValues = null;
		long rowCreated = -1;
		try
		{
			periodTrackerDB = sqLiteDatabase;
			periodTrackerDB.beginTransaction();
			List<MoodDataModel> moodDataModels = ValidationFunction.getBaseMoods();
			for (MoodDataModel model : moodDataModels)
			{
				contentValues = new ContentValues();
				contentValues.put(MoodDataModel.MOOD_IMAGE_URI, model.getImageUri());
				contentValues.put(MoodDataModel.MOOD_LABEL_KEY, model.getMoodLabel());
				contentValues.put(MoodDataModel.MOOD_PROFILE_ID, model.getProfileId());
				contentValues.put(MoodDataModel.MOOD_TYPE, model.getMoodType());
				rowCreated = periodTrackerDB.insert(table, nullColumnHack, contentValues);
			}
			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		return rowCreated;

	}

	public long createPeriodLogAppliactionSettings(String table, String nullColumnHack, SQLiteDatabase sqLiteDatabase) throws SQLiteException
	{
		long rowCreated = -1;
		ContentValues contentValues;
		try
		{
			periodTrackerDB = sqLiteDatabase;
			periodTrackerDB.beginTransaction();
			try
			{

				List<ApplicationSettingModel> applicationSettingModels = ValidationFunction.getDefaultApplicationSetting();
				for (ApplicationSettingModel applicationSettingModel : applicationSettingModels)
				{
					contentValues = new ContentValues();
					contentValues.put(ApplicationSettingModel.APPLICATION_SETTING_ID, applicationSettingModel.getId());
					contentValues.put(ApplicationSettingModel.APPLICATION_SETTING_VALUE, applicationSettingModel.getValue());
					contentValues.put(ApplicationSettingModel.APPLICATION_SETTING_PROFILE_ID, applicationSettingModel.getProfileId());
					rowCreated = periodTrackerDB.insert(table, nullColumnHack, contentValues);
				}
			}
			catch (SQLiteException exception)
			{
				this.close();
			}

			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();

		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		return rowCreated;

	}

	public long createMultipleRecordSymptomBase(String table, String nullColumnHack, SQLiteDatabase sqLiteDatabase) throws SQLiteException
	{
		ContentValues contentValues;

		long rowCreated = -1;
		try
		{
			periodTrackerDB = sqLiteDatabase;
			periodTrackerDB.beginTransaction();
			List<SymptomsModel> models = ValidationFunction.getBaseSymtoms();
			for (SymptomsModel model : models)
			{
				contentValues = new ContentValues();
				contentValues.put(SymptomsModel.SYMTOMS_IMAGEURI, model.getImageUri());
				contentValues.put(SymptomsModel.SYMTOMS_LABEL_KEY, model.getSymptomLabelKey());
				contentValues.put(SymptomsModel.SYMTOMS_PROFILE_ID, model.getProfileId());
				contentValues.put(SymptomsModel.SYMTOMS_TYPE, model.getSymptomType());
				rowCreated = periodTrackerDB.insert(table, nullColumnHack, contentValues);
			}

			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();

		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		return rowCreated;
	}

	public long createMultipleRecordSymptomSelected(String table, String nullColumnHack, List<SymtomsSelectedModel> interfaces) throws SQLiteException
	{
		ContentValues contentValues;

		long rowCreated = -1;
		try
		{

			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			for (SymtomsSelectedModel model : interfaces)
			{
				contentValues = new ContentValues();
				contentValues.put(SymtomsSelectedModel.SYMTOM_SELECTED_DAY_DETAIL_ID, model.getDayDetailsID());
				contentValues.put(SymtomsSelectedModel.SYMTOM_SELECTED_SYMTOM_ID, model.getSymptomId());
				contentValues.put(SymtomsSelectedModel.SYMTOM_SELECTED_SYMTOM_WEIGHT, model.getSymptomWeight());
				rowCreated = periodTrackerDB.insert(table, nullColumnHack, contentValues);
			}

			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
			this.closePeriodTrackerDB();
		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		return rowCreated;
	}

	public long deleteAndCreateMultipleRecordSymptomSelected(String deleteTable, String whereClause, String whereArgs[], String addTable, String nullColumnHack, List<SymtomsSelectedModel> models)
			throws SQLiteException
	{
		ContentValues contentValues;
		int rowdeleted = -1;
		long rowCreated = -1;
		try
		{

			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			rowdeleted = periodTrackerDB.delete(deleteTable, whereClause, whereArgs);
			if (rowdeleted > 0)
			{
				for (SymtomsSelectedModel model : models)
				{
					contentValues = new ContentValues();
					contentValues.put(SymtomsSelectedModel.SYMTOM_SELECTED_DAY_DETAIL_ID, model.getDayDetailsID());
					contentValues.put(SymtomsSelectedModel.SYMTOM_SELECTED_SYMTOM_ID, model.getSymptomId());
					contentValues.put(SymtomsSelectedModel.SYMTOM_SELECTED_SYMTOM_WEIGHT, model.getSymptomWeight());
					rowCreated = periodTrackerDB.insert(addTable, nullColumnHack, contentValues);
				}
			}
			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
			this.closePeriodTrackerDB();
		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		return rowCreated;
	}

	public long createMultipleRecordMoodSelected(String table, String nullColumnHack, List<MoodSelected> models) throws SQLiteException
	{
		ContentValues contentValues;

		long rowCreated = -1;
		try
		{

			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			for (MoodSelected model : models)
			{
				contentValues = new ContentValues();
				contentValues.put(MoodSelected.DAY_DETAIL_ID, model.getDayDetailId());
				contentValues.put(MoodSelected.MOOD_ID, model.getMoodId());
				contentValues.put(MoodSelected.MOOD_WIEGHT, model.getMoodWeight());
				rowCreated = periodTrackerDB.insert(table, nullColumnHack, contentValues);
			}

			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
			this.closePeriodTrackerDB();
		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		return rowCreated;
	}

	public long deleteAndCreateMultipleRecordMoodSelected(String deleteTable, String whereClause, String whereArgs[], String addTable, String nullColumnHack, List<MoodSelected> models)
			throws SQLiteException
	{
		ContentValues contentValues;
		int rowdeleted = -1;
		long rowCreated = -1;
		try
		{

			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();

			rowdeleted = periodTrackerDB.delete(deleteTable, whereClause, whereArgs);
			if (rowdeleted > 0)
			{
				for (MoodSelected model : models)
				{
					contentValues = new ContentValues();
					contentValues.put(MoodSelected.DAY_DETAIL_ID, model.getDayDetailId());
					contentValues.put(MoodSelected.MOOD_ID, model.getMoodId());
					contentValues.put(MoodSelected.MOOD_WIEGHT, model.getMoodWeight());
					rowCreated = periodTrackerDB.insert(addTable, nullColumnHack, contentValues);
				}
			}
			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
			this.closePeriodTrackerDB();
		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		return rowCreated;
	}

	public long createAndUpdateRecord(String createTable, String createNullColumnHack, ContentValues createValues, String updateTable, ContentValues updateValues, String updatewhereClause,
			String[] updateWhereArgs) throws SQLiteException
	{

		long rowCreated = -1;
		try
		{
			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			if (null != createTable) rowCreated = periodTrackerDB.insert(createTable, createNullColumnHack, createValues);

			if (null != updateTable) periodTrackerDB.update(updateTable, updateValues, updatewhereClause, updateWhereArgs);
			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
			this.closePeriodTrackerDB();

		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return rowCreated;

	}

	public long updateRecord(String table, ContentValues values, String whereClause, String[] whereArgs) throws SQLiteException
	{
		long rowsUpdated = -1;
		try
		{
			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			rowsUpdated = periodTrackerDB.update(table, values, whereClause, whereArgs);
			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
			this.closePeriodTrackerDB();
		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
			sqLiteException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return rowsUpdated;
	}

	public int deleteRecord(String table, String whereClause, String whereArgs[]) throws SQLiteException
	{
		int rowdeleted = -1;
		try
		{
			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			rowdeleted = periodTrackerDB.delete(table, whereClause, whereArgs);

			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
			this.closePeriodTrackerDB();

		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
		}
		catch (Exception exception)
		{

		}
		return rowdeleted;
	}

	public int deleteRecordFromMutipleTable(List<String> tables, Map<String, String> whereCaluseMap) throws SQLiteException
	{
		int rowdeleted = -1;
		try
		{
			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			for (String string : tables)
			{
				String whereClause = whereCaluseMap.get(string);
				rowdeleted = periodTrackerDB.delete(string, whereClause, null);
			}
			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();

		}
		catch (SQLiteException exception)
		{
			this.close();
			exception.printStackTrace();
		}

		return rowdeleted;

	}

	public int updateAndDeleteRecord(String updateTable, ContentValues values, String updateWhereClause, String updateWhereArgs[], String deleteTable, String deleteWhereClause,
			String deleteWhereArgs[]) throws SQLiteException
	{
		int rowdeleted = -1;
		try
		{
			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			if (null != deleteTable) rowdeleted = periodTrackerDB.delete(deleteTable, deleteWhereClause, deleteWhereArgs);

			if (null != updateTable) periodTrackerDB.update(updateTable, values, updateWhereClause, updateWhereArgs);
			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();
			this.closePeriodTrackerDB();
		}
		catch (SQLiteException sqLiteException)
		{
			this.close();
		}
		catch (Exception exception)
		{

		}
		return rowdeleted;
	}

	public boolean inseretDataBackupToTable(List<DayDetailModel> dayDetailModels, Map<Date, List<MoodSelected>> moodSelectedsMap, Map<Date, List<SymtomsSelectedModel>> symtomsSelectedModelsMap,
			Map<Date, List<Pills>> medicineMap, List<ApplicationSettingModel> applicationSettingModels, UserProfileModel profileModel, List<PeriodLogModel> logModels, List<String> deleteTables,
			Map<String, String> whereCaluseMap) throws SQLiteException
	{
		boolean noOfRowBackup = false;
	
		int rowdeleted = -1;
		int profileId;
		ContentValues contentValues = null;
		try
		{
			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();

			for (String string : deleteTables)
			{
				String whereClause = whereCaluseMap.get(string);
				rowdeleted = periodTrackerDB.delete(string, whereClause, null);
			}

			contentValues = new ContentValues();
			contentValues.put(UserProfileModel.USER_PROFILE_FIRST_NAME, profileModel.getFirstName());
			contentValues.put(UserProfileModel.USER_PROFILE_LAST_NAME, profileModel.getLastName());
			contentValues.put(UserProfileModel.USER_PROFILE_USER_NAME, profileModel.getUserName());
			contentValues.put(UserProfileModel.USER_PROFILE_PASSWORD, profileModel.getPassword());
			if (periodTrackerDB.insert(UserProfileModel.USER_PROFILE, null, contentValues) > 0) noOfRowBackup = true;
			if (noOfRowBackup)
			{

				profileId = ((UserProfileModel) this.selectRecordForBackup(UserProfileModel.class, " SELECT * FROM " + UserProfileModel.USER_PROFILE + " WHERE "
						+ UserProfileModel.USER_PROFILE_FIRST_NAME + " = " + "'" + profileModel.getFirstName() + "'")).getId();

				for (ApplicationSettingModel applicationSettingModel : applicationSettingModels)
				{
					applicationSettingModel.setProfileId(profileId);
					contentValues = new ContentValues();
					contentValues.put(ApplicationSettingModel.APPLICATION_SETTING_VALUE, applicationSettingModel.getValue());
					contentValues.put(ApplicationSettingModel.APPLICATION_SETTING_ID, applicationSettingModel.getId());
					contentValues.put(applicationSettingModel.APPLICATION_SETTING_PROFILE_ID, applicationSettingModel.getProfileId());

					if (periodTrackerDB.insert(ApplicationSettingModel.APPLICATION_SETTING, null, contentValues) > 0)
					{
						noOfRowBackup = true;
					}
					else
					{
						noOfRowBackup = false;
						break;
					}

				}

				for (PeriodLogModel periodLogModel : logModels)
				{
					contentValues = new ContentValues();
					contentValues.put(PeriodLogModel.PERIOD_TRACK_PROFILE_ID, profileId);
					contentValues.put(PeriodLogModel.PERIOD_TRACK_START_DATE, periodLogModel.getStartDate().getTime());
					contentValues.put(PeriodLogModel.PERIOD_TRACK_END_DATE, periodLogModel.getEndDate().getTime());
					contentValues.put(PeriodLogModel.PERIOD_TRACK_PERIOD_LENGTH, periodLogModel.getPeriodLength());
					contentValues.put(PeriodLogModel.PERIOD_TRACK_CYCLE_LENGTH, periodLogModel.getCycleLength());
					contentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY, periodLogModel.isPregnancy());
					contentValues.put(PeriodLogModel.PERIOD_TRACK_PREGNANCY_SUPPOTABLE, periodLogModel.isPregnancysupportable());

					if (periodTrackerDB.insert(PeriodLogModel.PERIOD_TRACK, null, contentValues) > 0)
					{
						noOfRowBackup = true;
					}
					else
					{
						noOfRowBackup = false;
						break;
					}
				}

				for (DayDetailModel dayDetailModel : dayDetailModels)
				{
					contentValues = new ContentValues();
					contentValues.put(DayDetailModel.DAY_DETAIL_DATE, dayDetailModel.getDate().getTime());
					contentValues.put(DayDetailModel.DAY_DETAIL_NOTE, dayDetailModel.getNote());
					contentValues.put(DayDetailModel.DAY_DETAIL_INTIMATE, dayDetailModel.isIntimate());
					contentValues.put(DayDetailModel.DAY_DETAIL_PROTECTION, dayDetailModel.getProtection());
					contentValues.put(DayDetailModel.DAY_DETAIL_HEIGHT, dayDetailModel.getHeight());
					contentValues.put(DayDetailModel.DAY_DETAIL_WEIGHT, dayDetailModel.getWeight());
					contentValues.put(DayDetailModel.DAY_DETAIL_TEMPERATURE, dayDetailModel.getTemp());
					contentValues.put(DayDetailModel.DAY_DETAIL_PROFILLE_ID, profileId);
					if (periodTrackerDB.insert(DayDetailModel.DAY_DETAIL, null, contentValues) > 0)
					{
						noOfRowBackup = true;
					}
					else
					{
						noOfRowBackup = false;
						break;
					}

					int dayDetailId = ((DayDetailModel) this.selectRecordForBackup(DayDetailModel.class, " SELECT * FROM " + DayDetailModel.DAY_DETAIL + " WHERE " + DayDetailModel.DAY_DETAIL_DATE
							+ " = " + dayDetailModel.getDate().getTime() + " AND " + DayDetailModel.DAY_DETAIL_PROFILLE_ID + " = " + profileId)).getId();

					List<MoodSelected> moodSelecteds = moodSelectedsMap.get(dayDetailModel.getDate());
					if (null != moodSelecteds)
					{
						for (MoodSelected moodSelected : moodSelecteds)
						{
							moodSelected.setDayDetailId(dayDetailId);
							contentValues = new ContentValues();
							contentValues.put(MoodSelected.MOOD_ID, moodSelected.getMoodId());
							contentValues.put(MoodSelected.MOOD_WIEGHT, moodSelected.getMoodWeight());
							contentValues.put(MoodSelected.DAY_DETAIL_ID, moodSelected.getDayDetailId());
							if (periodTrackerDB.insert(MoodSelected.MOOD_SELECETED, null, contentValues) > 0)
							{

								noOfRowBackup = true;
							}
							else
							{
								noOfRowBackup = false;
								break;
							}

						}

					}
					List<SymtomsSelectedModel> selectedModels = symtomsSelectedModelsMap.get(dayDetailModel.getDate());
					if (null != selectedModels)
					{
						for (SymtomsSelectedModel symtomsSelectedModel : selectedModels)
						{
							symtomsSelectedModel.setDayDetailsID(dayDetailId);
							contentValues = new ContentValues();
							contentValues.put(SymtomsSelectedModel.SYMTOM_SELECTED_SYMTOM_ID, symtomsSelectedModel.getSymptomId());
							contentValues.put(SymtomsSelectedModel.SYMTOM_SELECTED_SYMTOM_WEIGHT, symtomsSelectedModel.getSymptomWeight());
							contentValues.put(SymtomsSelectedModel.SYMTOM_SELECTED_DAY_DETAIL_ID, symtomsSelectedModel.getDayDetailsID());
							if (periodTrackerDB.insert(SymtomsSelectedModel.SYMTOM_SELECTED, null, contentValues) > 0)
							{
								noOfRowBackup = true;
							}
							else
							{
								noOfRowBackup = false;
								break;
							}

						}
					}

					List<Pills> pills = medicineMap.get(dayDetailModel.getDate());
					if (null != pills)
					{
						for (Pills pills2 : pills)
						{
							pills2.setDayDetailsId(dayDetailId);

							contentValues = new ContentValues();
							contentValues.put(Pills.MEDICINE_NAME, pills2.getMedicineName());
							contentValues.put(Pills.MEDICINE_QUANTITY, pills2.getQuantity());
							contentValues.put(Pills.MEDICINE_DAY_DETAILS_ID, pills2.getDayDetailsId());
							if (periodTrackerDB.insert(Pills.MEDICINE, null, contentValues) > 0)
							{
								noOfRowBackup = true;
							}
							else
							{
								noOfRowBackup = false;
								break;
							}

						}

					}

				}
				periodTrackerDB.setTransactionSuccessful();
				periodTrackerDB.endTransaction();
			}
		}
		catch (SQLiteException exception)
		{
			this.close();
			exception.printStackTrace();
			noOfRowBackup = false;
		}

		return noOfRowBackup;

	}

	public boolean rollBackData(List<String> strings, List<String> tables, Map<String, String> whereCaluseMap, List<String> deleteList) throws SQLiteException
	{

		int rowdeleted = -1;
		boolean finalResult = false;
		try
		{
			this.openPeriodTrackerDB();
			periodTrackerDB.beginTransaction();
			for (String string : tables)
			{
				String whereClause = whereCaluseMap.get(string);
				rowdeleted = periodTrackerDB.delete(string, whereClause, null);
				if (rowdeleted < 0)
				{
					finalResult = true;
				}
				else
				{
					finalResult = false;
					break;
				}
			}

			for (String string : strings)
			{
				periodTrackerDB.execSQL(string);
			}

			for (String string : deleteList)
			{
				periodTrackerDB.execSQL(string);
			}

			periodTrackerDB.setTransactionSuccessful();
			periodTrackerDB.endTransaction();

		}
		catch (SQLiteException exception)
		{
			this.close();
			exception.printStackTrace();
		}

		return finalResult;

	}

}