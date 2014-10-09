package com.linchpin.periodtracker.utlity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.ApplicationConstants;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.MoodDataModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.SymptomsModel;
import com.linchpin.periodtracker.view.DatePickerDialog;

public class ValidationFunction
{
	
	static boolean	updateCurrentStartDate	= false;
	
	public static ApplicationConstants getApplicationConstantsValue()
	{
		ApplicationConstants applicationConstants = new ApplicationConstants(PeriodTrackerConstants.APPLICATION_CONSTANT_VERSION_KEY,
				PeriodTrackerConstants.APPLICATION_CONSTANT_VERSION);
		
		return applicationConstants;
		
	}
	
	public static List<SymptomsModel> getBaseSymtoms()
	{
		List<SymptomsModel> models = new ArrayList<SymptomsModel>();
		models.add(new SymptomsModel(0, "acne", "symptoms.acne", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "anxiety", "symptoms.anxiety", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "backache", "symptoms.backache", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "bloating", "symptoms.bloating", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "breasttenderness", "symptoms.breasttenderness", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "chills", "symptoms.chills", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "constipation", "symptoms.constipation", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "cramps", "symptoms.cramps", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "carving", "symptoms.cravings", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "diarreah", "symptoms.diarrhea", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "dizziness", "symptoms.dizziness", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "fatigue", "symptoms.fatigue", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "headache", "symptoms.headaches", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "isomnia", "symptoms.insomnia", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "irritability", "symptoms.irritability", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "migrane", "symptoms.migraine", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "moody", "symptoms.moody", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "nausea", "symptoms.nausea", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		models.add(new SymptomsModel(0, "neckache", "symptoms.neckaches", PeriodTrackerConstants.SYSTEM_SYMTOM_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		
		return models;
		
	}
	
	public static List<MoodDataModel> getBaseMoods()
	{
		List<MoodDataModel> dataModels = new ArrayList<MoodDataModel>();
		dataModels.add(new MoodDataModel(0, "angery", "moods.angry", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "board", "moods.bored", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels
				.add(new MoodDataModel(0, "calm", "moods.clam", PeriodTrackerConstants.SYSTEM_MOOD_TYPE, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "confused", "moods.confused", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "confident", "moods.confident", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels
				.add(new MoodDataModel(0, "cool", "moods.cool", PeriodTrackerConstants.SYSTEM_MOOD_TYPE, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "depressed", "moods.depreessed", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "embarrased", "moods.embarrased", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "energized", "moods.energized", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels
				.add(new MoodDataModel(0, "evil", "moods.evil", PeriodTrackerConstants.SYSTEM_MOOD_TYPE, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "flirty", "moods.flirty", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "frustrated", "moods.frustrated", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "happy", "moods.happy", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "hungry", "moods.hungry", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "impatient", "moods.impatient", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "insecure", "moods.insecure", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "irritated", "moods.irritated", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "jealous", "moods.jealous", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "nervous", "moods.nervous", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "sad", "moods.sad", PeriodTrackerConstants.SYSTEM_MOOD_TYPE, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "satisfaction", "moods.satisfied", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "shy", "moods.shy", PeriodTrackerConstants.SYSTEM_MOOD_TYPE, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels
				.add(new MoodDataModel(0, "sick", "moods.sick", PeriodTrackerConstants.SYSTEM_MOOD_TYPE, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "stressed", "moods.stressed", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		dataModels.add(new MoodDataModel(0, "tired", "moods.tired", PeriodTrackerConstants.SYSTEM_MOOD_TYPE,
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		return dataModels;
	}
	
	public static List<ApplicationSettingModel> getDefaultApplicationSetting()
	{
		List<ApplicationSettingModel> applicationSettingModels = new ArrayList<ApplicationSettingModel>();
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTING_PREGNANT_MODE, String
				.valueOf(PeriodTrackerConstants.PREGNANCY_MODE_ON), PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_UNIT_KEY,
				PeriodTrackerConstants.DEFAULT_HIEGHT_UNIT, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_UNIT_KEY,
				PeriodTrackerConstants.DEFAULT_WEIGHT_UNIT, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMP_UNIT_KEY,
				PeriodTrackerConstants.DEFAULT_TEMPRATURE_UNIT, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_LENGTH_KEY, String
				.valueOf(PeriodTrackerConstants.PERIOD_LENGTH), PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_CYCLE_LENGTH_KEY, String
				.valueOf(PeriodTrackerConstants.CYCLE_LENGTH), PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_LENGTH_KEY, String
				.valueOf(PeriodTrackerConstants.OVULATION_DAY), PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_DATE_FORMAT_KEY,
				PeriodTrackerConstants.DATE_FORMAT, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_APP_LANGUAGE_KEY,
				PeriodTrackerConstants.DEVICE_LANUGAGE, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PASSWORD_STRING_KEY,
				PeriodTrackerConstants.APPLICATION_SETTINGS_DEFAULT_PASSWORD, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_VALUE_KEY, Utility
				.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_HIEGHT_VAULE)), PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_VALUE_KEY, Utility
				.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_WEIGHT_VALUE)), PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels
				.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMPERATURE_VALUE_KEY, Utility
						.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE)),
						PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PREGNANCY_MESSAGE_FORMAT_KEY,
				PeriodTrackerConstants.DEFAULT_PREGNANCY_MESSAGE_FORMAT, PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_KEY, String
				.valueOf(PeriodTrackerConstants.DEFAULT_NOTIFICATION_DAY), PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_KEY, String
				.valueOf(PeriodTrackerConstants.DEFAULT_NOTIFICATION_DAY), PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_KEY, String
				.valueOf(PeriodTrackerConstants.DEFAULT_NOTIFICATION_DAY), PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_MESSAGE_KEY,
				"periodstartnotificationmessage", PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_MESSAGE_KEY,
				"fertilestartnotificationmessage", PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_MESSAGE_KEY,
				"ovulationstartnotificationmessage", PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_DAY_OF_WEEK_KEY, "0",
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		
		applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_AVERAGE_LENGTHS, "true",
				PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		
		return applicationSettingModels;
	}
	
	public static int checkAllValidationForStartDate(Context context, Date startDate, final DatePickerDialog startDatePickerDialog,
			List<PeriodTrackerModelInterface> periodTrackerModelInterfaces, boolean updateCurrent)
	{
		int insereOrUpdate = 0;
		updateCurrentStartDate = updateCurrent;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		List<PeriodTrackerModelInterface> pastPeriodTrackerModelInterfaces;
		PeriodLogModel periodLogModel = null;
		// Condition 1
		if (!Utility.isDateLessThanCurrent(startDate))
		{
			
			// Show Message
			builder.setMessage(context.getResources().getString(R.string.lessthancallmessage));
			builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					startDatePickerDialog.show();
					updateCurrentStartDate = true;
				}
			});
			builder.setNegativeButton(context.getResources().getString(R.string.cancel), null);
			builder.show();
		}
		else
		{
			if (periodTrackerModelInterfaces.size() == 0)
			{
				insereOrUpdate = 2;
				
			}
			else
			{
				if (checkValidityOfDate(startDate, periodTrackerModelInterfaces))
				{
					
					pastPeriodTrackerModelInterfaces = periodTrackerModelInterfaces;
					if (pastPeriodTrackerModelInterfaces.size() != 0)
					{
						periodLogModel = (PeriodLogModel) pastPeriodTrackerModelInterfaces.get(0);
						
					}
					if (updateCurrentStartDate && periodLogModel.getEndDate().getTime() == PeriodTrackerConstants.NULL_DATE) insereOrUpdate = 1;
					else insereOrUpdate = 2;
					
				}
				else
				{
					
					builder.setTitle(context.getResources().getString(R.string.invaliddatetitle));
					builder.setMessage(R.string.invalidateRecordBetweenDates);
					builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method
							// stub
							
							dialog.dismiss();
							startDatePickerDialog.show();
						}
					});
					
					builder.setNegativeButton(context.getString(R.string.cancel), null);
					startDatePickerDialog.cancel();
					builder.show();
					
				}
				
			}
		}
		return insereOrUpdate;
	}
	
	public static boolean checkValidityOfDate(Date date, List<PeriodTrackerModelInterface> periodTrackerModelInterfaces)
	{
		boolean validity = false;
		List<PeriodTrackerModelInterface> pastPeriodTrackerModelInterfaces;
		pastPeriodTrackerModelInterfaces = periodTrackerModelInterfaces;
		if (pastPeriodTrackerModelInterfaces.size() > 0)
		{
			for (PeriodTrackerModelInterface modelInterface : pastPeriodTrackerModelInterfaces)
			{
				PeriodLogModel periodLogModel = (PeriodLogModel) modelInterface;
				if (!Utility.checkDateBetweenDates(periodLogModel.getStartDate(), periodLogModel.getEndDate(), date))
				{
					validity = false;
					break;
				}
				else
				{
					validity = true;
					periodLogModel.getEndDate();
					periodLogModel.getStartDate();
					
				}
			}
		}
		else
		{
			validity = true;
		}
		return validity;
	}
	
	public static int checkValidityForEndDate(final Context context, Date startDate, Date endDate, final DatePickerDialog endDatePickerDailog,
			List<PeriodTrackerModelInterface> periodTrackerModelInterfaces)
	{
		int UpdateEndate = 0;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength() - 1);
		
		/*
		 * if (!Utility.isDateLessThanCurrent(endDate)) {
		 * 
		 * builder.setTitle(context.getResources().getString(R.string.
		 * invaliddatetitle ));
		 * builder.setMessage(context.getResources().getString(R.string.
		 * enddatelessthancurrent));
		 * 
		 * builder.setPositiveButton(context.getResources().getString(R.string.ok
		 * ), new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) { //
		 * TODO Auto-generated method stub endDatePickerDailog.show(); } });
		 * 
		 * builder.setNegativeButton(context.getResources().getString(R.string.
		 * cancel ), null); builder.show(); }
		 */// Validation 2
		{
			if (!Utility.isEndDateGreaterThanStart(startDate, endDate))
			{
				
				builder.setTitle(context.getResources().getString(R.string.invaliddatetitle));
				builder.setMessage(context.getResources().getString(R.string.enddategreaterthanstart));
				
				builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						endDatePickerDailog.show();
					}
				});
				
				builder.setNegativeButton(context.getResources().getString(R.string.cancel), null);
				builder.show();
			}
			else if (endDate.after(date))
			{
				builder = new AlertDialog.Builder(context);
				builder.setTitle(context.getString(R.string.invaliddatetitle));
				builder.setMessage(context.getString(R.string.enddategreaterthancyclelength));
				
				builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						endDatePickerDailog.show();
					}
				});
				
				builder.setNegativeButton(context.getString(R.string.cancel), null);
				builder.show();
			}
			else
			{
				// / validation 3
				if (checkValidityOfDate(endDate, periodTrackerModelInterfaces))
				{
					UpdateEndate = 1;
					
				}
				else
				{
					
					builder.setTitle(R.string.invaliddatetitle);
					builder.setMessage(R.string.invaliddatemessage);
					builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
							dialog.dismiss();
							
							endDatePickerDailog.show();
							
						}
					});
					builder.setNegativeButton(context.getString(R.string.cancel), null);
					builder.show();
				}
			}
			
		}
		
		return UpdateEndate;
	}
	
}
