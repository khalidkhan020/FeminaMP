package com.linchpin.periodttracker.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.MoodSelected;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.Pills;
import com.linchpin.periodtracker.model.SymtomsSelectedModel;
import com.linchpin.periodtracker.model.UserProfileModel;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;

public class ImportDatabaseHandler extends PeriodTrackerDBHandler
{
	Context	context;
	
	public ImportDatabaseHandler(Context context)
	{
		
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public void createTempTablePeriodTrack() throws Exception
	{
		try
		{
			List<String> strings = new ArrayList<String>();
			String SqlP = "CREATE TABLE TEMP_Period_Track (Id INTEGER PRIMARY KEY  AUTOINCREMENT  , Profile_Id INTEGER, Start_Date NUMERIC  NOT NULL , End_Date NUMERIC ,  Cycle_Length INTEGER, Period_Length INTEGER , Pregnancy BOOL ,Pregnancy_Supportable BOOL )";
			strings.add(SqlP);
			String SqlD = "CREATE TABLE TEMP_Day_Details (Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , Date NUMERIC NOT NULL , Note_Description VARCHAR, Intimate BOOL, Protection INTEGER, Weight NUMERIC , Temperature NUMERIC, Height NUMERIC ,Profile_Id INTEGER)";
			strings.add(SqlD);
			String SqlMS = "CREATE TABLE TEMP_Mood_Selected (Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , Day_Details_Id INTEGER NOT NULL , Mood_Id INTEGER NOT NULL , Mood_Weight INTEGER)";
			strings.add(SqlMS);
			String SqlSS = "CREATE TABLE TEMP_Symptom_Selected (Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , Symptom_Id INTEGER NOT NULL , Day_Details_Id INTEGER NOT NULL , Symptom_Weight INTEGER NOT NULL )";
			strings.add(SqlSS);
			String SqlM = "CREATE TABLE TEMP_Medicine (Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , Medicine_Name VARCHAR NOT NULL ,Medicine_Quantity INTEGER , Day_Details_Id INTEGER NOT NULL )";
			strings.add(SqlM);
			String SqlUp = "CREATE TABLE TEMP_User_Profile (Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , First_Name VARCHAR NOT NULL , Last_Name VARCHAR NOT NULL , User_Name VARCHAR NOT NULL, Password VARCHAR NOT NULL)";
			strings.add(SqlUp);
			String SqlAS = "CREATE TABLE TEMP_Application_Settings(Id VACHAR NOT NULL ,KeyValues VARCHAR, Profile_id INTEGER NOT NULL, PRIMARY KEY (Id,Profile_id))";
			strings.add(SqlAS);
			
			this.executeQueryForCreateTemTable(strings);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	public void inseretDataTOTempTablePeriodTrack() throws Exception
	{
		try
		{
			List<String> strings = new ArrayList<String>();
			String SqlP = "INSERT INTO TEMP_Period_Track SELECT * FROM " + PeriodLogModel.PERIOD_TRACK;
			strings.add(SqlP);
			String SqlD = "INSERT INTO TEMP_Day_Details SELECT * FROM " + DayDetailModel.DAY_DETAIL;
			strings.add(SqlD);
			String SqlMS = "INSERT INTO TEMP_User_Profile SELECT * FROM " + UserProfileModel.USER_PROFILE;
			strings.add(SqlMS);
			String SqlSS = "INSERT INTO TEMP_Mood_Selected SELECT * FROM " + MoodSelected.MOOD_SELECETED;
			strings.add(SqlSS);
			String SqlM = "INSERT INTO TEMP_Symptom_Selected SELECT * FROM " + SymtomsSelectedModel.SYMTOM_SELECTED;
			strings.add(SqlM);
			String SqlUp = "INSERT INTO TEMP_Medicine SELECT * FROM " + Pills.MEDICINE;
			strings.add(SqlUp);
			String SqlAS = "INSERT INTO TEMP_Application_Settings SELECT * FROM " + ApplicationSettingModel.APPLICATION_SETTING;
			strings.add(SqlAS);
			
			this.executeQueryForCreateTemTable(strings);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	public void inseretDataFromBackup(List<DayDetailModel> dayDetailModels, Map<Date, List<MoodSelected>> moodSelectedsMap, Map<Date, List<SymtomsSelectedModel>> symtomsSelectedModelsMap, Map<Date, List<Pills>> medicineMap, List<ApplicationSettingModel> applicationSettingModels,
			UserProfileModel profileModel, List<PeriodLogModel> logModels) throws Exception
	{
		
		List<String> tables = new ArrayList<String>();
		Map<String, String> whereCaluse = new HashMap<String, String>();
		tables.add(UserProfileModel.USER_PROFILE);
		tables.add(PeriodLogModel.PERIOD_TRACK);
		tables.add(DayDetailModel.DAY_DETAIL);
		tables.add(MoodSelected.MOOD_SELECETED);
		tables.add(SymtomsSelectedModel.SYMTOM_SELECTED);
		tables.add(Pills.MEDICINE);
		tables.add(ApplicationSettingModel.APPLICATION_SETTING);
		
		whereCaluse.put(UserProfileModel.USER_PROFILE, UserProfileModel.USER_PROFILE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId());
		
		whereCaluse.put(PeriodLogModel.PERIOD_TRACK, PeriodLogModel.PERIOD_TRACK_PROFILE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId());
		
		String MOODSWHERE = MoodSelected.DAY_DETAIL_ID + " in ( SELECT " + DayDetailModel.DAY_DETAIL_Id + " FROM " + DayDetailModel.DAY_DETAIL + " Where " + DayDetailModel.DAY_DETAIL_PROFILLE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId() + ")";
		
		whereCaluse.put(MoodSelected.MOOD_SELECETED, MOODSWHERE);
		
		String SYMPTOMWHERE = SymtomsSelectedModel.SYMTOM_SELECTED_DAY_DETAIL_ID + " in ( SELECT " + DayDetailModel.DAY_DETAIL_Id + " FROM " + DayDetailModel.DAY_DETAIL + " Where " + DayDetailModel.DAY_DETAIL_PROFILLE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId() + ")";
		
		whereCaluse.put(SymtomsSelectedModel.SYMTOM_SELECTED, SYMPTOMWHERE);
		
		String MEDICINEWHERE = Pills.MEDICINE_DAY_DETAILS_ID + " in ( SELECT " + DayDetailModel.DAY_DETAIL_Id + " FROM " + DayDetailModel.DAY_DETAIL + " Where " + DayDetailModel.DAY_DETAIL_PROFILLE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId() + ")";
		
		whereCaluse.put(Pills.MEDICINE, MEDICINEWHERE);
		
		whereCaluse.put(DayDetailModel.DAY_DETAIL, DayDetailModel.DAY_DETAIL_PROFILLE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId());
		
		whereCaluse.put(ApplicationSettingModel.APPLICATION_SETTING, ApplicationSettingModel.APPLICATION_SETTING_PROFILE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId());
		
		if (this.inseretDataBackupToTable(dayDetailModels, moodSelectedsMap, symtomsSelectedModelsMap, medicineMap, applicationSettingModels, profileModel, logModels, tables, whereCaluse))
		{
			List<String> deleteTables = new ArrayList<String>();
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_User_Profile");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Period_Track ");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Day_Details ");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Mood_Selected ");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Symptom_Selected ");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Medicine ");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Application_Settings ");
			this.executeQueryForDeleteTemTable(deleteTables);
			
		}
		
		/*		} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
		*/
	}
	
	public void rollBackDataFromTempTable() throws Exception
	{
		try
		{
			List<String> tables = new ArrayList<String>();
			List<String> deleteTables = new ArrayList<String>();
			Map<String, String> whereCaluse = new HashMap<String, String>();
			tables.add(UserProfileModel.USER_PROFILE);
			tables.add(PeriodLogModel.PERIOD_TRACK);
			tables.add(DayDetailModel.DAY_DETAIL);
			tables.add(MoodSelected.MOOD_SELECETED);
			tables.add(SymtomsSelectedModel.SYMTOM_SELECTED);
			tables.add(Pills.MEDICINE);
			tables.add(ApplicationSettingModel.APPLICATION_SETTING);
			
			whereCaluse.put(UserProfileModel.USER_PROFILE, UserProfileModel.USER_PROFILE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			whereCaluse.put(PeriodLogModel.PERIOD_TRACK, PeriodLogModel.PERIOD_TRACK_PROFILE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			String MOODSWHERE = MoodSelected.DAY_DETAIL_ID + " in ( SELECT " + DayDetailModel.DAY_DETAIL_Id + " FROM " + DayDetailModel.DAY_DETAIL + " Where " + DayDetailModel.DAY_DETAIL_PROFILLE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId() + ")";
			
			whereCaluse.put(MoodSelected.MOOD_SELECETED, MOODSWHERE);
			
			String SYMPTOMWHERE = SymtomsSelectedModel.SYMTOM_SELECTED_DAY_DETAIL_ID + " in ( SELECT " + DayDetailModel.DAY_DETAIL_Id + " FROM " + DayDetailModel.DAY_DETAIL + " Where " + DayDetailModel.DAY_DETAIL_PROFILLE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId() + ")";
			
			whereCaluse.put(SymtomsSelectedModel.SYMTOM_SELECTED, SYMPTOMWHERE);
			
			String MEDICINEWHERE = Pills.MEDICINE_DAY_DETAILS_ID + " in ( SELECT " + DayDetailModel.DAY_DETAIL_Id + " FROM " + DayDetailModel.DAY_DETAIL + " Where " + DayDetailModel.DAY_DETAIL_PROFILLE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId() + ")";
			
			whereCaluse.put(Pills.MEDICINE, MEDICINEWHERE);
			
			whereCaluse.put(DayDetailModel.DAY_DETAIL, DayDetailModel.DAY_DETAIL_PROFILLE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			whereCaluse.put(ApplicationSettingModel.APPLICATION_SETTING, ApplicationSettingModel.APPLICATION_SETTING_PROFILE_ID + " = " + PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			List<String> strings = new ArrayList<String>();
			String SqlP = "INSERT INTO " + PeriodLogModel.PERIOD_TRACK + " SELECT * FROM TEMP_Period_Track ";
			strings.add(SqlP);
			String SqlD = "INSERT INTO " + DayDetailModel.DAY_DETAIL + " SELECT * FROM TEMP_Day_Details ";
			strings.add(SqlD);
			String SqlMS = "INSERT INTO " + UserProfileModel.USER_PROFILE + " SELECT * FROM TEMP_User_Profile ";
			strings.add(SqlMS);
			String SqlSS = "INSERT INTO " + MoodSelected.MOOD_SELECETED + " SELECT * FROM TEMP_Mood_Selected ";
			strings.add(SqlSS);
			String SqlM = "INSERT INTO " + SymtomsSelectedModel.SYMTOM_SELECTED + " SELECT * FROM TEMP_Symptom_Selected ";
			strings.add(SqlM);
			String SqlUp = "INSERT INTO " + Pills.MEDICINE + " SELECT * FROM TEMP_Medicine ";
			strings.add(SqlUp);
			String SqlAS = "INSERT INTO " + ApplicationSettingModel.APPLICATION_SETTING + " SELECT * FROM TEMP_Application_Settings ";
			strings.add(SqlAS);
			
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_User_Profile");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Period_Track");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Day_Details");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Mood_Selected");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Symptom_Selected");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Medicine");
			deleteTables.add("DROP TABLE IF EXISTS" + " TEMP_Application_Settings");
			
			this.rollBackData(strings, tables, whereCaluse, deleteTables);
			
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
}
