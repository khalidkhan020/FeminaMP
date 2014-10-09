package com.linchpin.periodtracker.utlity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.MoodSelected;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.Pills;
import com.linchpin.periodtracker.model.SymtomsSelectedModel;
import com.linchpin.periodtracker.model.UserProfileModel;
import com.linchpin.periodttracker.database.AddNoteDBHandler;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;

public class ExportDataBase
{
	
	JSONObject			mainJsonObject	= new JSONObject();
	
	JSONArray			fieldsJsonArray;
	
	JSONObject			internalFieldJsonObject;
	
	Context				context;
	ConstantsKey		constantsKey;
	
	SimpleDateFormat	dateFormat;
	SimpleDateFormat	pregnancyDateFormat;
	
	public ExportDataBase(Context context, String version)
	{
		this.context = context;
		try
		{
			constantsKey = new ConstantsKey(version);
			mainJsonObject.put("V", version);
			dateFormat = new SimpleDateFormat("yyyyMMdd");
			pregnancyDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JSONObject getFinalExportData()
	{
		PeriodLogModel logModel;
		MoodSelected moodSelected;
		SymtomsSelectedModel symtomsSelectedModel;
		Pills pills;
		List<PeriodTrackerModelInterface> interfaces;
		Map<Integer, ArrayList<JSONObject>> moodMap = null, symtomMap = null, medicineMap = null;
		
		try
		{
			
			PeriodLogDBHandler dbHandler = new PeriodLogDBHandler(context);
			interfaces = dbHandler.getAllLogs(PeriodTrackerObjectLocator.getInstance().getProfileId());
			fieldsJsonArray = new JSONArray();
			int index = 0;
			if (((APP) context.getApplicationContext()).getPreferences().getBoolean(APP.PREF.SHARE_PERIODS.key, true))
			{
				for (PeriodTrackerModelInterface modelInterface : interfaces)
				{
					index++;
					if (index > 3) break;
					logModel = (PeriodLogModel) modelInterface;
					internalFieldJsonObject = new JSONObject();
					if (logModel.isPregnancy())
					{
						
						internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.ST_D), pregnancyDateFormat.format(logModel.getStartDate()));
						internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.EN_D), pregnancyDateFormat.format(logModel.getEndDate()));
					}
					else
					{
						internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.ST_D), dateFormat.format(logModel.getStartDate()));
						internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.EN_D), dateFormat.format(logModel.getEndDate()));
						
					}
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PT_PL), logModel.getPeriodLength());
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PT_CL), logModel.getCycleLength());
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PT_PM), logModel.isPregnancy());
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PT_PSM), logModel.isPregnancysupportable());
					fieldsJsonArray.put(internalFieldJsonObject);
				}
				mainJsonObject.put(constantsKey.getKey(ConstantsKey.PERIOD_TRACK), fieldsJsonArray);
			}
			/**
			 * Backup OF Mood Selcted
			 */
			
			AddNoteDBHandler addNoteDBHandler = new AddNoteDBHandler(context);
			ArrayList<JSONObject> arrayList;
			if (((APP) context.getApplicationContext()).getPreferences().getBoolean(APP.PREF.SHARE_MOODS.key, true))
			{
				
				interfaces = addNoteDBHandler.getAllSelectedMoodForProfileID(PeriodTrackerObjectLocator.getInstance().getProfileId());
				moodMap = new HashMap<Integer, ArrayList<JSONObject>>();
				for (PeriodTrackerModelInterface modelInterface : interfaces)
				{
					moodSelected = (MoodSelected) modelInterface;
					internalFieldJsonObject = new JSONObject();
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.MS_MID), moodSelected.getMoodId());
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.MS_MW), moodSelected.getMoodWeight());
					if (!moodMap.containsKey(moodSelected.getDayDetailId()))
					{
						arrayList = new ArrayList<JSONObject>();
						arrayList.add(internalFieldJsonObject);
						moodMap.put(moodSelected.getDayDetailId(), arrayList);
					}
					else
					{
						if (moodMap.containsKey(moodSelected.getDayDetailId()))
						{
							arrayList = moodMap.get(moodSelected.getDayDetailId());
							arrayList.add(internalFieldJsonObject);
						}
						else
						{
							arrayList = new ArrayList<JSONObject>();
						}
					}
					
				}
			}
			/**
			 * Backup OF Symptom Selected
			 */
			if (((APP) context.getApplicationContext()).getPreferences().getBoolean(APP.PREF.SHARE_SYMPTOMS.key, true))
			{
				interfaces = addNoteDBHandler.getAllSelectedSymtomForProfileID(PeriodTrackerObjectLocator.getInstance().getProfileId());
				ArrayList<JSONObject> arrayListSysmtom;
				symtomMap = new HashMap<Integer, ArrayList<JSONObject>>();
				for (PeriodTrackerModelInterface modelInterface : interfaces)
				{
					symtomsSelectedModel = (SymtomsSelectedModel) modelInterface;
					internalFieldJsonObject = new JSONObject();
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.SS_SID), symtomsSelectedModel.getSymptomId());
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.SS_SW), symtomsSelectedModel.getSymptomWeight());
					if (!symtomMap.containsKey(symtomsSelectedModel.getDayDetailsID()))
					{
						arrayListSysmtom = new ArrayList<JSONObject>();
						arrayListSysmtom.add(internalFieldJsonObject);
						symtomMap.put(symtomsSelectedModel.getDayDetailsID(), arrayListSysmtom);
					}
					else
					{
						if (symtomMap.containsKey(symtomsSelectedModel.getDayDetailsID()))
						{
							arrayListSysmtom = symtomMap.get(symtomsSelectedModel.getDayDetailsID());
							arrayListSysmtom.add(internalFieldJsonObject);
						}
						else
						{
							arrayList = new ArrayList<JSONObject>();
						}
					}
				}
			}
			/**
			 * Backup OF Medicine
			 */
			if (((APP) context.getApplicationContext()).getPreferences().getBoolean(APP.PREF.SHARE_PILLS.key, false))
			{
				interfaces = addNoteDBHandler.getAllMedicineForProfileID(PeriodTrackerObjectLocator.getInstance().getProfileId());
				ArrayList<JSONObject> arrayListMedicine;
				medicineMap = new HashMap<Integer, ArrayList<JSONObject>>();
				for (PeriodTrackerModelInterface modelInterface1 : interfaces)
				{
					pills = (Pills) modelInterface1;
					internalFieldJsonObject = new JSONObject();
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.MDN), pills.getMedicineName());
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.MDQ), pills.getQuantity());
					if (!medicineMap.containsKey(pills.getDayDetailsId()))
					{
						arrayListMedicine = new ArrayList<JSONObject>();
						arrayListMedicine.add(internalFieldJsonObject);
						medicineMap.put(pills.getDayDetailsId(), arrayListMedicine);
						arrayListMedicine = new ArrayList<JSONObject>();
					}
					else
					{
						if (medicineMap.containsKey(pills.getDayDetailsId()))
						{
							arrayListMedicine = medicineMap.get(pills.getDayDetailsId());
							arrayListMedicine.add(internalFieldJsonObject);
						}
						else
						{
							arrayList = new ArrayList<JSONObject>();
						}
					}
					
				}
			}
			/**
			 * Backup OF DayDetail
			 */
			
			// tableJsonArray.put(internalTableJasonObject);int temp = periodLogDBHandler.isDateBetweenPeriods(this.date);
			
			interfaces = addNoteDBHandler.getDayDetailsForProfileId(PeriodTrackerObjectLocator.getInstance().getProfileId());
			fieldsJsonArray = new JSONArray();
			DayDetailModel dayDetailModel;
			 PeriodLogDBHandler						periodLogDBHandler;
			periodLogDBHandler = new PeriodLogDBHandler(context);
			for (int i = 0; i < interfaces.size(); i++)
			{
				
				internalFieldJsonObject = new JSONObject();
				dayDetailModel = (DayDetailModel) interfaces.get(i);
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_DT), dateFormat.format(dayDetailModel.getDate()));
				if(((APP) context.getApplicationContext()).getPreferences().getBoolean(APP.PREF.SHARE_NOTES.key, false))
				{
					internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_NT), dayDetailModel.getNote());
					internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_IV), dayDetailModel.isIntimate());
					internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_PV), dayDetailModel.getProtection());
				}
			
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_PR),dayDetailModel.getPeriod());
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_WV), dayDetailModel.getWeight());
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_HV), dayDetailModel.getHeight());
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_TV), dayDetailModel.getTemp());
				if (null != dayDetailModel)
				{
					if (moodMap != null && moodMap.size() != 0)
					{
						if (moodMap.get(dayDetailModel.getId()) != null) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.Mood_Selected), new JSONArray(moodMap.get(dayDetailModel.getId())));
						
					}
					if (null != symtomMap && symtomMap.size() != 0)
					{
						if (symtomMap.get(dayDetailModel.getId()) != null) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.SYMTOM_SELECTED), new JSONArray(symtomMap.get(dayDetailModel.getId())));
						
					}
					if (null != medicineMap && medicineMap.size() != 0 )
					{
						if (medicineMap.get(dayDetailModel.getId()) != null) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.Medicine), new JSONArray(medicineMap.get(dayDetailModel.getId())));
						
					}
				}
				fieldsJsonArray.put(internalFieldJsonObject);
			}
			mainJsonObject.put(constantsKey.getKey(ConstantsKey.Day_Detail), fieldsJsonArray);
			// tableJsonArray.put(internalTableJasonObject);
			JSONObject lenths=new JSONObject();
			lenths.put(APP.PARTNER.FIELDS.CYCLE_LENGTH.key, PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength());
			lenths.put(APP.PARTNER.FIELDS.PERIOD_LENGTH.key, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength());
			lenths.put(APP.PARTNER.FIELDS.OVULATION_LENGTH.key, PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength()-1);
			mainJsonObject.put(APP.PARTNER.LENGTH_TABLE.key, lenths);
			
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		return mainJsonObject;
		
	}
	
	public JSONObject getFinalSharingData()
	{
		PeriodLogModel logModel;
		MoodSelected moodSelected;
		SymtomsSelectedModel symtomsSelectedModel;
		Pills pills;
		ApplicationSettingModel applicationSettingModel;
		List<PeriodTrackerModelInterface> interfaces;
		Map<Integer, ArrayList<JSONObject>> moodMap, symtomMap, medicineMap;
		
		try
		{
			
			/**
			 * Backup OF USER PRFILE
			 */
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(context);
			PeriodTrackerModelInterface interface1 = applicationSettingDBHandler.getUserProfile("priyanka");
			UserProfileModel userProfileModel = (UserProfileModel) interface1;
			fieldsJsonArray = new JSONArray();
			internalFieldJsonObject = new JSONObject();
			internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.FN), userProfileModel.getFirstName());
			internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.LN), userProfileModel.getLastName());
			internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.UN), userProfileModel.getUserName());
			internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.US_PP), userProfileModel.getPassword());
			fieldsJsonArray.put(internalFieldJsonObject);
			mainJsonObject.put(constantsKey.getKey(ConstantsKey.User_Profile), fieldsJsonArray);
			
			/**
			 * Backup OF APPLICATION SETTING
			 */
			interfaces = applicationSettingDBHandler.getApplicationSettings(PeriodTrackerObjectLocator.getInstance().getProfileId());
			fieldsJsonArray = new JSONArray();
			internalFieldJsonObject = new JSONObject();
			try
			{
				for (PeriodTrackerModelInterface modelInterface : interfaces)
				{
					applicationSettingModel = (ApplicationSettingModel) modelInterface;
					
					if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_LENGTH_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DPl), applicationSettingModel.getValue());
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_CYCLE_LENGTH_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DCL), applicationSettingModel.getValue());
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_LENGTH_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DOL), applicationSettingModel.getValue());
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTING_PREGNANT_MODE)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DPM), applicationSettingModel.getValue());
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_DATE_FORMAT_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DDF), applicationSettingModel.getValue());
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_APP_LANGUAGE_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DAL), applicationSettingModel.getValue());
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_PASSWORD_STRING_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DAP), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_UNIT_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DHU), applicationSettingModel.getValue());
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_UNIT_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DWU), applicationSettingModel.getValue());
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMP_UNIT_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DTU), applicationSettingModel.getValue());
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PSQ), applicationSettingModel.getValue());
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_ANSWER)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PSA), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_VALUE_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DHV), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_VALUE_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DWV), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMPERATURE_VALUE_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DTV), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_ESTIMATED_DATE_VALUE_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PED), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_PREGNANCY_MESSAGE_FORMAT_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DPMF), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PSN), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.FSN), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.OSN), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_MESSAGE_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PSNM), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_MESSAGE_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.FSNM), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_MESSAGE_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.OSNM), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_DAY_OF_WEEK_KEY)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DOW), applicationSettingModel.getValue());
					
					else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_AVERAGE_LENGTHS)) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.DAVGL), applicationSettingModel.getValue());
					
				}
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
			fieldsJsonArray.put(internalFieldJsonObject);
			mainJsonObject.put(constantsKey.getKey(ConstantsKey.Application_Settings), fieldsJsonArray);
			
			/**
			 * Backup OF PERIOD TRACK TABLE
			 */
			
			PeriodLogDBHandler dbHandler = new PeriodLogDBHandler(context);
			interfaces = dbHandler.getAllLogs(PeriodTrackerObjectLocator.getInstance().getProfileId());
			fieldsJsonArray = new JSONArray();
			for (PeriodTrackerModelInterface modelInterface : interfaces)
			{
				logModel = (PeriodLogModel) modelInterface;
				internalFieldJsonObject = new JSONObject();
				if (logModel.isPregnancy())
				{
					
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.ST_D), pregnancyDateFormat.format(logModel.getStartDate()));
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.EN_D), pregnancyDateFormat.format(logModel.getEndDate()));
				}
				else
				{
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.ST_D), dateFormat.format(logModel.getStartDate()));
					internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.EN_D), dateFormat.format(logModel.getEndDate()));
					
				}
				internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PT_PL), logModel.getPeriodLength());
				internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PT_CL), logModel.getCycleLength());
				internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PT_PM), logModel.isPregnancy());
				internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.PT_PSM), logModel.isPregnancysupportable());
				fieldsJsonArray.put(internalFieldJsonObject);
			}
			mainJsonObject.put(constantsKey.getKey(ConstantsKey.PERIOD_TRACK), fieldsJsonArray);
			
			/**
			 * Backup OF Mood Selcted
			 */
			
			AddNoteDBHandler addNoteDBHandler = new AddNoteDBHandler(context);
			ArrayList<JSONObject> arrayList;
			interfaces = addNoteDBHandler.getAllSelectedMoodForProfileID(PeriodTrackerObjectLocator.getInstance().getProfileId());
			moodMap = new HashMap<Integer, ArrayList<JSONObject>>();
			for (PeriodTrackerModelInterface modelInterface : interfaces)
			{
				moodSelected = (MoodSelected) modelInterface;
				internalFieldJsonObject = new JSONObject();
				internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.MS_MID), moodSelected.getMoodId());
				internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.MS_MW), moodSelected.getMoodWeight());
				if (!moodMap.containsKey(moodSelected.getDayDetailId()))
				{
					arrayList = new ArrayList<JSONObject>();
					arrayList.add(internalFieldJsonObject);
					moodMap.put(moodSelected.getDayDetailId(), arrayList);
				}
				else
				{
					if (moodMap.containsKey(moodSelected.getDayDetailId()))
					{
						arrayList = moodMap.get(moodSelected.getDayDetailId());
						arrayList.add(internalFieldJsonObject);
					}
					else
					{
						arrayList = new ArrayList<JSONObject>();
					}
				}
				
			}
			/**
			 * Backup OF Symptom Selected
			 */
			interfaces = addNoteDBHandler.getAllSelectedSymtomForProfileID(PeriodTrackerObjectLocator.getInstance().getProfileId());
			ArrayList<JSONObject> arrayListSysmtom;
			symtomMap = new HashMap<Integer, ArrayList<JSONObject>>();
			for (PeriodTrackerModelInterface modelInterface : interfaces)
			{
				symtomsSelectedModel = (SymtomsSelectedModel) modelInterface;
				internalFieldJsonObject = new JSONObject();
				internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.SS_SID), symtomsSelectedModel.getSymptomId());
				internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.SS_SW), symtomsSelectedModel.getSymptomWeight());
				if (!symtomMap.containsKey(symtomsSelectedModel.getDayDetailsID()))
				{
					arrayListSysmtom = new ArrayList<JSONObject>();
					arrayListSysmtom.add(internalFieldJsonObject);
					symtomMap.put(symtomsSelectedModel.getDayDetailsID(), arrayListSysmtom);
				}
				else
				{
					if (symtomMap.containsKey(symtomsSelectedModel.getDayDetailsID()))
					{
						arrayListSysmtom = symtomMap.get(symtomsSelectedModel.getDayDetailsID());
						arrayListSysmtom.add(internalFieldJsonObject);
					}
					else
					{
						arrayList = new ArrayList<JSONObject>();
					}
				}
			}
			/**
			 * Backup OF Medicine
			 */
			
			interfaces = addNoteDBHandler.getAllMedicineForProfileID(PeriodTrackerObjectLocator.getInstance().getProfileId());
			ArrayList<JSONObject> arrayListMedicine;
			medicineMap = new HashMap<Integer, ArrayList<JSONObject>>();
			for (PeriodTrackerModelInterface modelInterface1 : interfaces)
			{
				pills = (Pills) modelInterface1;
				internalFieldJsonObject = new JSONObject();
				internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.MDN), pills.getMedicineName());
				internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.MDQ), pills.getQuantity());
				if (!medicineMap.containsKey(pills.getDayDetailsId()))
				{
					arrayListMedicine = new ArrayList<JSONObject>();
					arrayListMedicine.add(internalFieldJsonObject);
					medicineMap.put(pills.getDayDetailsId(), arrayListMedicine);
					arrayListMedicine = new ArrayList<JSONObject>();
				}
				else
				{
					if (medicineMap.containsKey(pills.getDayDetailsId()))
					{
						arrayListMedicine = medicineMap.get(pills.getDayDetailsId());
						arrayListMedicine.add(internalFieldJsonObject);
					}
					else
					{
						arrayList = new ArrayList<JSONObject>();
					}
				}
				
			}
			
			/**
			 * Backup OF DayDetail
			 */
			
			// tableJsonArray.put(internalTableJasonObject);
			
			interfaces = addNoteDBHandler.getDayDetailsForProfileId(PeriodTrackerObjectLocator.getInstance().getProfileId());
			fieldsJsonArray = new JSONArray();
			DayDetailModel dayDetailModel;
			
			for (int i = 0; i < interfaces.size(); i++)
			{
				
				internalFieldJsonObject = new JSONObject();
				dayDetailModel = (DayDetailModel) interfaces.get(i);
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_DT), dateFormat.format(dayDetailModel.getDate()));
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_NT), dayDetailModel.getNote());
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_IV), dayDetailModel.isIntimate());
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_PV), dayDetailModel.getProtection());
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_WV), dayDetailModel.getWeight());
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_HV), dayDetailModel.getHeight());
				internalFieldJsonObject.put(constantsKey.getKey(constantsKey.DD_TV), dayDetailModel.getTemp());
				if (null != dayDetailModel && moodMap.size() != 0)
				{
					if (moodMap.get(dayDetailModel.getId()) != null) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.Mood_Selected), new JSONArray(moodMap.get(dayDetailModel.getId())));
					
				}
				if (null != symtomMap && symtomMap.size() != 0)
				{
					if (symtomMap.get(dayDetailModel.getId()) != null) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.SYMTOM_SELECTED), new JSONArray(symtomMap.get(dayDetailModel.getId())));
					
				}
				if (null != medicineMap && medicineMap.size() != 0 && i < moodMap.size())
				{
					if (medicineMap.get(dayDetailModel.getId()) != null) internalFieldJsonObject.put(constantsKey.getKey(ConstantsKey.Medicine), new JSONArray(medicineMap.get(dayDetailModel.getId())));
					
				}
				fieldsJsonArray.put(internalFieldJsonObject);
			}
			mainJsonObject.put(constantsKey.getKey(ConstantsKey.Day_Detail), fieldsJsonArray);
			// tableJsonArray.put(internalTableJasonObject);
			
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		return mainJsonObject;
		
	}
	
}
