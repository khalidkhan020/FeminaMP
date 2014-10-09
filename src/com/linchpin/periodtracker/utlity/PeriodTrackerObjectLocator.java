package com.linchpin.periodtracker.utlity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.ApplicationConstants;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.UserProfileModel;
import com.linchpin.periodttracker.database.ApplicationConstantDBHandeler;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;

public class PeriodTrackerObjectLocator
{
	private static PeriodTrackerObjectLocator	objectLocator;
	//private static Context						context;
	private static PeriodTrackerVault			periodTrackerVault;
	
	private PeriodTrackerObjectLocator()
	{
	}
	
	public  void intitializeApplicationParameters()
	{
		ApplicationSettingDBHandler dbHandler = new ApplicationSettingDBHandler(APP.GLOBAL().getApplicationContext()
				);
		Map<String, String> appSettings = new HashMap<String, String>();
		PeriodTrackerModelInterface interface1 = dbHandler.getUserProfile("priyanka");
		int profileId = ((UserProfileModel) interface1).getId();
		List<PeriodTrackerModelInterface> interfaces = dbHandler.getApplicationSettings(profileId);
		if (interfaces.size() == 0) interfaces = dbHandler.getApplicationSettings(1);
		ApplicationSettingModel applicationSettingModel = null;
		for (PeriodTrackerModelInterface periodTrackerModelInterface : interfaces)
		{
			applicationSettingModel = (ApplicationSettingModel) periodTrackerModelInterface;
			appSettings.put(applicationSettingModel.getId(), applicationSettingModel.getValue());
		}
		if (applicationSettingModel != null) periodTrackerVault.setProfileID(applicationSettingModel.getProfileId());
		else periodTrackerVault.setProfileID(PeriodTrackerConstants.DEFAULT_PROFILE_ID);
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_CYCLE_LENGTH_KEY)) periodTrackerVault.setCycleLength(Integer.valueOf(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_CYCLE_LENGTH_KEY)));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_LENGTH_KEY)) periodTrackerVault.setPeriodLength(Integer.valueOf(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_LENGTH_KEY)));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_UNIT_KEY)) periodTrackerVault.setHeightUnit(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_UNIT_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_UNIT_KEY)) periodTrackerVault.setWeightUnit(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_UNIT_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMP_UNIT_KEY)) periodTrackerVault.setTempuint(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMP_UNIT_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTING_SKIN_SELECTED)) periodTrackerVault.setThemeSelected(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTING_SKIN_SELECTED));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTING_PREGNANT_MODE)) periodTrackerVault.setPregnant(Boolean.valueOf(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTING_PREGNANT_MODE)));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_LENGTH_KEY)) periodTrackerVault.setOvulutionday(Integer.valueOf(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_LENGTH_KEY)));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_DATE_FORMAT_KEY)) periodTrackerVault.setDateFormat(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_DATE_FORMAT_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_APP_LANGUAGE_KEY)) periodTrackerVault.setAppLanguage(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_APP_LANGUAGE_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PASSWORD_STRING_KEY)) periodTrackerVault.setPasswordProtection(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PASSWORD_STRING_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_VALUE_KEY)) periodTrackerVault.setDefaultHeightVaule(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_VALUE_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_VALUE_KEY)) periodTrackerVault.setDefaultWeightVaule(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_VALUE_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_ESTIMATED_DATE_VALUE_KEY)) periodTrackerVault.setEstimatedDeliveryDate(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_ESTIMATED_DATE_VALUE_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PREGNANCY_MESSAGE_FORMAT_KEY)) periodTrackerVault.setPregnancyMessegeFormat(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PREGNANCY_MESSAGE_FORMAT_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMPERATURE_VALUE_KEY)) periodTrackerVault.setDefaultTempValue(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMPERATURE_VALUE_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_KEY)) periodTrackerVault.setPeriodStartNotification(Integer.valueOf(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_KEY)));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_KEY)) periodTrackerVault.setFeritilyNotification(Integer.valueOf(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_KEY)));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_KEY)) periodTrackerVault.setOvulationNotification(Integer.valueOf(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_KEY)));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_MESSAGE_KEY)) periodTrackerVault.setPeriodStartNotificationMessage(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_MESSAGE_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_MESSAGE_KEY)) periodTrackerVault.setFertilityNotificationMessage((appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_MESSAGE_KEY)));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PREGNANCY_NOTIFICTION_MESSAGE_KEY)) periodTrackerVault.setPregnancyNotificationMessage((appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_PREGNANCY_NOTIFICTION_MESSAGE_KEY)));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_MESSAGE_KEY)) periodTrackerVault.setOvulationNotificationMessage((appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_MESSAGE_KEY)));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_DAY_OF_WEEK_KEY)) periodTrackerVault.setDayOfWeek(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_DAY_OF_WEEK_KEY));
		if (null != appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_AVERAGE_LENGTHS))
			periodTrackerVault.setAveraged(Boolean.valueOf(appSettings.get(PeriodTrackerConstants.APPLICATION_SETTINGS_AVERAGE_LENGTHS)));
		else
		{	periodTrackerVault.setAveraged(false);
			ApplicationSettingDBHandler handler = new ApplicationSettingDBHandler(APP.GLOBAL().getApplicationContext());
			handler.inseretApplicationSetting(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_AVERAGE_LENGTHS, "true", PeriodTrackerConstants.DEFAULT_PROFILE_ID));
		}
	}
	
	public static Set<String> getTestIds()
	{
		Set<String> testIds = new HashSet<String>();
		testIds.add("A5706E051EC3016F9A0005521FFC590C"); // Karbon
		testIds.add("BD1130E01AFF60C19AFB3E6FA18D770F"); // Xperia
		testIds.add("3CE2E758B585E0994382AFA847C1B6A3"); // Ace
		testIds.add("417F89A06E2C77A15763172B2CD0DF53"); // S3
		testIds.add("F54E8E54ED161BD0B76A19A62152AF0E"); // Micromax
		testIds.add("D4BF940E3717A9662D7135580C20E654"); // Spice
		testIds.add("E65549D214B2DFFF468F2DF303C0F59E"); // Micromax Funbook
		testIds.add("D115C32BDC75D8B18D3906B12899FB09"); // S2
		//testIds.add("20F006057C36E5D7EFF33C0AA01A04BD"); // S4
		return testIds;		
	}
	
	public static PeriodTrackerObjectLocator getInstance()
	{
		if (null == objectLocator)
		{
			objectLocator = new PeriodTrackerObjectLocator();
			periodTrackerVault = new PeriodTrackerVault();
			objectLocator.intitializeApplicationParameters();
		}
		return objectLocator;
	}
	
	public static void setPeriodTrackerObjectLocatorNull()
	{
		objectLocator = null;
	}
	
	public static PeriodTrackerObjectLocator getInstance(Activity contextObj)
	{
		if (null == objectLocator)
		{
			objectLocator = new PeriodTrackerObjectLocator();
			periodTrackerVault = new PeriodTrackerVault();
			AdView adView = new AdView( contextObj, AdSize.SMART_BANNER, contextObj.getResources().getString(R.string.appaddid));
			AdRequest adRequest = new AdRequest();
			adRequest.setTestDevices(getTestIds());			
			adView.loadAd(adRequest);
			periodTrackerVault.setAdView(adView);
			objectLocator.intitializeApplicationParameters();
			ApplicationConstantDBHandeler constantDBHandeler = new ApplicationConstantDBHandeler(contextObj);
			PeriodTrackerModelInterface modelInterface = constantDBHandeler.getParticularApplicationconstant(PeriodTrackerConstants.APPLICATION_CONSTANT_VERSION_KEY);
			if (null != modelInterface)
			{
				ApplicationConstants applicationConstants = (ApplicationConstants) modelInterface;
				if (null != applicationConstants.getKey()) periodTrackerVault.setApplicationVersion(applicationConstants.getKey());
			}
		}		
		return objectLocator;
	}	
	public int getCurrentCycleLength()
	{
		if (periodTrackerVault.getCycleLength() == 0) periodTrackerVault.setCycleLength(PeriodTrackerConstants.CYCLE_LENGTH);
		return periodTrackerVault.getCycleLength();
	}	
	public int getCurrentPeriodLength()
	{
		if (periodTrackerVault.getPeriodLength() == 0) periodTrackerVault.setPeriodLength(PeriodTrackerConstants.PERIOD_LENGTH);
		return periodTrackerVault.getPeriodLength();
	}
	
	public String getApplicationVersion()
	{
		if (periodTrackerVault.getApplicationVersion() == null) periodTrackerVault.setApplicationVersion(PeriodTrackerConstants.APPLICATION_CONSTANT_VERSION);
		return periodTrackerVault.getApplicationVersion();
	}
	
	public int getCurrentOvualtionLength()
	{
		if (periodTrackerVault.getOvulutionday() == 0) 
			periodTrackerVault.setOvulutionday(PeriodTrackerConstants.OVULATION_DAY);
		return periodTrackerVault.getOvulutionday();
	}
	
	public boolean getPregnancyMode()
	{
		return periodTrackerVault.getPregnant();
	}
	
	public boolean isAveraged()
	{
		return periodTrackerVault.isAveraged();
	}
	
	public int getProfileId()
	{
		return periodTrackerVault.getProfileID();
	}
	
	public String getTempUnit()
	{
		if (periodTrackerVault.getTempuint() == null) periodTrackerVault.setTempuint(PeriodTrackerConstants.DEFAULT_TEMPRATURE_UNIT);
		return periodTrackerVault.getTempuint();
	}
	
	public String getWeighUnit()
	{
		if (periodTrackerVault.getWieghtUnit() == null) periodTrackerVault.setWeightUnit(PeriodTrackerConstants.DEFAULT_WEIGHT_UNIT);
		return periodTrackerVault.getWieghtUnit();
	}
	
	public String getThemeSelected()
	{
		return periodTrackerVault.getThemeSelected();
	}
	
	public String getHeightUnit()
	{
		if (periodTrackerVault.getHeightUnit() == null) periodTrackerVault.setHeightUnit(PeriodTrackerConstants.DEFAULT_HIEGHT_UNIT);
		return periodTrackerVault.getHeightUnit();
	}
	
	public String getDateFormat()
	{
		if (periodTrackerVault.getDateFormat() == null) periodTrackerVault.setDateFormat(PeriodTrackerConstants.DATE_FORMAT);
		return periodTrackerVault.getDateFormat();
	}
	
	public String getPasswordProtection()
	{
		if (periodTrackerVault.getPasswordProtection() == null) periodTrackerVault.setPasswordProtection("");
		return periodTrackerVault.getPasswordProtection();
	}
	
	public String getAppLanguage()
	{
		if (periodTrackerVault.getAppLanguage() == null) periodTrackerVault.setAppLanguage(PeriodTrackerConstants.DEVICE_LANUGAGE);
		return periodTrackerVault.getAppLanguage();
	}
	
	public String getDefaultWeightVaule()
	{
		if (periodTrackerVault.getDefaultWeightVaule() == null) periodTrackerVault.setDefaultWeightVaule(PeriodTrackerConstants.DEFAULT_WEIGHT_VALUE);
		return periodTrackerVault.getDefaultWeightVaule();
	}
	
	public String getDefaultHeightVaule()
	{
		if (periodTrackerVault.getHeightUnit() == null) periodTrackerVault.setDefaultHeightVaule(PeriodTrackerConstants.DEFAULT_HIEGHT_VAULE);
		return periodTrackerVault.getDefaultHeightVaule();
	}
	
	public String getDayOfWeek()
	{
		if (periodTrackerVault.getDayOfWeek() == null) periodTrackerVault.setDayOfWeek("0");
		return periodTrackerVault.getDayOfWeek();
	}
	
	public String getDefaultTemperatureVaule()
	{
		if (periodTrackerVault.getTempuint() == null) periodTrackerVault.setDefaultTempValue(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE);
		return periodTrackerVault.getDefaultTempValue();
	}
	
	public Date getEstimatedDeliveryDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try
		{
			if (null == periodTrackerVault.getEstimatedDeliveryDate() || periodTrackerVault.getEstimatedDeliveryDate().equals("")) date = new Date(PeriodTrackerConstants.NULL_DATE);
			else date = dateFormat.parse(periodTrackerVault.getEstimatedDeliveryDate());
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}
	
	public String PregnancyMessageFormat()
	{
		if (periodTrackerVault.getPregnancyMessegeFormat() == null) periodTrackerVault.setPregnancyMessegeFormat(PeriodTrackerConstants.DEFAULT_PREGNANCY_MESSAGE_FORMAT);
		return periodTrackerVault.getPregnancyMessegeFormat();
	}
	
	public int getOvulationNotification()
	{
		return periodTrackerVault.getOvulationNotification();
	}
	
	public int getPeriodStartNotification()
	{
		return periodTrackerVault.getPeriodStartNotification();
	}
	
	public int getFertilityNotification()
	{
		return periodTrackerVault.getFertilityNotification();
	}
	
	public String getOvulationNotificationMessage()
	{
		if (periodTrackerVault.getOvulationNotificationMessage() == null) periodTrackerVault.setFertilityNotificationMessage("ovulationstartnotificationmessage");
		return periodTrackerVault.getOvulationNotificationMessage();
	}
	
	public String getPregnancyNotificationMessage()
	{
		if (periodTrackerVault.getPregnancyNotificationMessage() == null) periodTrackerVault.setPregnancyNotificationMessage("PregnancyNotificationMessage");
		return periodTrackerVault.getOvulationNotificationMessage();
	}
	
	public String getPeriodStartNotificationMessage()
	{
		if (periodTrackerVault.getPeriodStartNotificationMessage() == null) periodTrackerVault.setFertilityNotificationMessage("periodstartnotificationmessage");
		return periodTrackerVault.getPeriodStartNotificationMessage();
	}
	
	public String getFertilityNotificationMessage()
	{
		if (periodTrackerVault.getFertilityNotificationMessage() == null) periodTrackerVault.setFertilityNotificationMessage("fertilestartnotificationmessage");
		return periodTrackerVault.getFertilityNotificationMessage();
	}
	
	public void setSinglaotonAdview(Activity activity)
	{
		if (null != periodTrackerVault.getAdView())
		{
			ViewGroup parent = (ViewGroup) ((ViewManager) periodTrackerVault.getAdView().getParent());
			if (parent != null) parent.removeView(periodTrackerVault.getAdView());
		}
		else
		{
			AdView adView = new AdView((Activity) activity, AdSize.SMART_BANNER, activity.getResources().getString(R.string.appaddid));
			AdRequest adRequest = new AdRequest();
			adRequest.setTestDevices(getTestIds());
			adView.loadAd(adRequest);
			periodTrackerVault.setAdView(adView);
		}
		((LinearLayout) activity.findViewById(R.id.rlbottom)).addView(periodTrackerVault.getAdView());
	}
}
