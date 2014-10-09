package com.linchpin.periodttracker.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.MoodSelected;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.Pills;
import com.linchpin.periodtracker.model.SymtomsSelectedModel;
import com.linchpin.periodtracker.model.UserProfileModel;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.ConstantsKey;
import com.linchpin.periodtracker.utlity.EncryptDecrypt;
import com.linchpin.periodtracker.utlity.InsertDatabaseBackground;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.view.RestoreAndBackupActivity;

public class ImportDatabase
{
	
	static Context									context;
	static ConstantsKey								constantsKey;
	String											version;
	static Map<Date, List<MoodSelected>>			moodMap		= new HashMap<Date, List<MoodSelected>>();
	static Map<Date, List<SymtomsSelectedModel>>	symptomMap	= new HashMap<Date, List<SymtomsSelectedModel>>();
	static Map<Date, List<Pills>>					medicineMap	= new HashMap<Date, List<Pills>>();
	static JSONArray								periodTrackArray;
	static JSONArray								dayDetailArray;
	static JSONArray								applicationSettingsArray;
	static JSONArray								userProileArray;
	JSONArray										moodSelectedArray;
	JSONArray										symtomSelectedArray;
	JSONArray										medicineArray;
	static SimpleDateFormat							dateFormat	= new SimpleDateFormat("yyyyMMdd");
	static SimpleDateFormat							pregnancyDateFormat;
	public static String							passcode	= "";
	
	public ImportDatabase(final Context context, String data) throws Exception
	{
		
		this.context = context;
		try
		{
			dateFormat = new SimpleDateFormat("yyyyMMdd");
			pregnancyDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, false).commit();
			
			/* Utility.cancelAllNotification(context); */
			JSONObject jsonObject = importData(data);
			String version = jsonObject.getString(ConstantsKey.VersionKey);
			if (null != version)
			{
				constantsKey = new ConstantsKey(version);
				jsonParsing(jsonObject);
			}
		}
		catch (final Exception exception)
		{
			exception.printStackTrace();
			
		}
		
	}
	
	public JSONObject importData(String data) throws Exception
	{
		
		final String scheme = Uri.parse(data).getScheme();
		JSONObject json = null;
		if (ContentResolver.SCHEME_CONTENT.equals(scheme))
		{
			try
			{
				ContentResolver cr = context.getContentResolver();
				InputStream is = cr.openInputStream(Uri.parse(data));
				if (is == null) return json;
				StringBuffer buf = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String str;
				if (is != null)
				{
					while ((str = reader.readLine()) != null)
					{
						buf.append(str + "\n");
					}
				}
				is.close();
				json = new JSONObject(EncryptDecrypt.getInstance(ConstantsKey.sceretKey).newDcrypt(buf.toString()));
				System.out.println(json.toString());
			}
			catch (Exception exception)
			{
				
			}
		}
		else
		{
			try
			{
				FileReader fileReader = new FileReader(data);
				StringBuffer buf = new StringBuffer();
				
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				
				String str;
				if (bufferedReader != null)
				{
					while ((str = bufferedReader.readLine()) != null)
					{
						buf.append(str + "\n");
					}
				}
				bufferedReader.close();
				json = new JSONObject(EncryptDecrypt.getInstance(ConstantsKey.sceretKey).newDcrypt(buf.toString()));
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json;
		
	}
	
	public void jsonParsing(JSONObject jsonObject) throws Exception
	{
		
		try
		{
			version = jsonObject.getString(ConstantsKey.VersionKey);
			periodTrackArray = jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.PERIOD_TRACK));
			
			dayDetailArray = jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.Day_Detail));
			
			userProileArray = jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.User_Profile));
			
			applicationSettingsArray = jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.Application_Settings));
			
			for (ApplicationSettingModel applicationSettingModel : getApplicationSettingModels(applicationSettingsArray))
			{
				if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_PASSWORD_STRING_KEY))
				{
					passcode = applicationSettingModel.getValue();
					break;
				}
			}
			
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			
		}
	}
	
	public static void askForPassword(final String passcode)
	{
		
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper((RestoreAndBackupActivity) context, android.R.style.Theme_DeviceDefault_Light);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from((RestoreAndBackupActivity) context).inflate(R.layout.forheigthandweightvalue, null);
		builder.setView(view);
		final AlertDialog alertDialog = builder.create();
		
		TextView title = (TextView) view.findViewById(R.id.tileforhieghtandweight);
		title.setText(context.getResources().getString(R.string.enterpassword));
		final EditText editText = (EditText) view.findViewById(R.id.heightweightvalue);
		editText.setHint(context.getResources().getString(R.string.passcode));
		editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		Button set = (Button) view.findViewById(R.id.setvalue);
		TextView textView = (TextView) view.findViewById(R.id.text);
		textView.setText(context.getString(R.string.importpassword));
		
		set.setText(context.getResources().getString(R.string.ok));
		Button cancel = (Button) view.findViewById(R.id.valuecancel);
		
		cancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		
		set.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (editText.getText().toString().equals(passcode))
				{
					
					new InsertDatabaseBackground(context).execute();
					alertDialog.dismiss();
					
				}
				else
				{
					
					((Activity) context).runOnUiThread(new Runnable()
					{
						
						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							alertDialog.dismiss();
							Toast.makeText(context, context.getString(R.string.wrongpasscode), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		});
		
		alertDialog.show();
		
	}
	
	public static void insertAndParsingArray()
	{
		
		try
		{
			ImportDatabaseHandler databaseHandler = new ImportDatabaseHandler(context);
			databaseHandler.createTempTablePeriodTrack();
			databaseHandler.inseretDataTOTempTablePeriodTrack();
			ApplicationSettingDBHandler dbHandler = new ApplicationSettingDBHandler(context);
			
			String question = ((ApplicationSettingModel) dbHandler.getParticularApplicationSetting(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION, PeriodTrackerObjectLocator.getInstance().getProfileId())).getValue();
			String answer = ((ApplicationSettingModel) dbHandler.getParticularApplicationSetting(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_ANSWER, PeriodTrackerObjectLocator.getInstance().getProfileId())).getValue();
			
			String email = ((ApplicationSettingModel) dbHandler.getParticularApplicationSetting(PeriodTrackerConstants.APLLICATION_SETTING_EMAIL_KEY, PeriodTrackerObjectLocator.getInstance().getProfileId())).getValue();
			
			List<ApplicationSettingModel> applicationSettingModels = getApplicationSettingModels(applicationSettingsArray);
			HashMap<String, ApplicationSettingModel> hashMap = new HashMap<String, ApplicationSettingModel>();
			for (ApplicationSettingModel applicationSettingModel : applicationSettingModels)
			{
				
				hashMap.put(applicationSettingModel.getId(), applicationSettingModel);
				
				if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTINGS_PASSWORD_STRING_KEY))
				{
					applicationSettingModel.setValue(PeriodTrackerObjectLocator.getInstance().getPasswordProtection());
					
				}
				else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION))
				{
					applicationSettingModel.setValue(question);
					
				}
				else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_ANSWER))
				{
					applicationSettingModel.setValue(answer);
					
				}
				else if (applicationSettingModel.getId().equals(PeriodTrackerConstants.APLLICATION_SETTING_EMAIL_KEY))
				{
					
					applicationSettingModel.setValue(email);
					
				}
				
			}
			
			if (!hashMap.containsKey(PeriodTrackerConstants.APPLICATION_SETTINGS_DAY_OF_WEEK_KEY))
			{
				applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_DAY_OF_WEEK_KEY, "0", PeriodTrackerObjectLocator.getInstance().getProfileId()));
			}
			
			databaseHandler.inseretDataFromBackup(getDayDetailBackup(dayDetailArray), moodMap, symptomMap, medicineMap, applicationSettingModels, getBackupOFUserProfile(userProileArray).get(0), getPeriodlogTableBackup(periodTrackArray));
			
			((Activity) context).runOnUiThread(new Runnable()
			{
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					Toast.makeText(context, context.getString(R.string.importdatasucessfully), Toast.LENGTH_LONG).show();
					
				}
			});
		}
		catch (Exception exception)
		{
			
			ImportDatabaseHandler database = new ImportDatabaseHandler(context);
			database.close();
			try
			{
				
				database.rollBackDataFromTempTable();
				((Activity) context).runOnUiThread(new Runnable()
				{
					
					@Override
					public void run()
					{
						// TODO Auto-generated method stub
						Toast.makeText(context, context.getString(R.string.tryagain), Toast.LENGTH_LONG).show();
						
					}
				});
				
			}
			catch (final Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				((Activity) context).runOnUiThread(new Runnable()
				{
					
					@Override
					public void run()
					{
						// TODO Auto-generated method stub
						Toast.makeText(context, "Sorry Lost Data" + e.toString(), Toast.LENGTH_LONG).show();
					}
				});
			}
			
		}
		
	}
	
	public static List<PeriodLogModel> getPeriodlogTableBackup(JSONArray jsonArray) throws Exception
	{
		JSONObject jsonObject;
		constantsKey = new ConstantsKey("1.0");
		List<PeriodLogModel> logModels = new ArrayList<PeriodLogModel>();
		PeriodLogModel logModel;
		String startDate = null, endDate = null, periodLength = null, cycleLength = null, pregnancyMode = null, pregnancySupportable = null;
		for (int i = 0; i < jsonArray.length(); i++)
		{
			try
			{
				jsonObject = (JSONObject) jsonArray.get(i);
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.ST_D)))
				{
					startDate = jsonObject.getString(constantsKey.getKey(ConstantsKey.ST_D));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.EN_D)))
				{
					endDate = jsonObject.getString(constantsKey.getKey(ConstantsKey.EN_D));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.PT_PL)))
				{
					periodLength = jsonObject.getString(constantsKey.getKey(ConstantsKey.PT_PL));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.PT_CL)))
				{
					cycleLength = jsonObject.getString(constantsKey.getKey(ConstantsKey.PT_CL));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.PT_PM)))
				{
					pregnancyMode = jsonObject.getString(constantsKey.getKey(ConstantsKey.PT_PM));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.PT_PSM)))
				{
					pregnancySupportable = jsonObject.getString(constantsKey.getKey(ConstantsKey.PT_PSM));
				}
				
				if (Boolean.valueOf(pregnancyMode))
				{
					
					logModels.add(new PeriodLogModel(0, PeriodTrackerObjectLocator.getInstance().getProfileId(), pregnancyDateFormat.parse(startDate), pregnancyDateFormat.parse(endDate), Integer.valueOf(cycleLength), Integer.valueOf(periodLength), Boolean.valueOf(pregnancyMode), Boolean
							.valueOf(pregnancySupportable)));
					
				}
				else
				{
					
					logModels
							.add(new PeriodLogModel(0, PeriodTrackerObjectLocator.getInstance().getProfileId(), dateFormat.parse(startDate), dateFormat.parse(endDate), Integer.valueOf(cycleLength), Integer.valueOf(periodLength), Boolean.valueOf(pregnancyMode), Boolean.valueOf(pregnancySupportable)));
					
				}
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e)
			{
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		
		return logModels;
		
	}
	
	public static List<DayDetailModel> getDayDetailBackup(JSONArray jsonArray) throws Exception
	{
		constantsKey = new ConstantsKey("1.0");
		JSONObject jsonObject;
		List<DayDetailModel> dayDetailModels = new ArrayList<DayDetailModel>();
		DayDetailModel dayDetailModel;
		String date = null, note = null, height = null, weight = null, temp = null, intimate = null, protection = null;
		int period=3;
		try
		{
			for (int i = 0; i < jsonArray.length(); i++)
			{
				jsonObject = (JSONObject) jsonArray.get(i);
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_DT)))
				{
					date = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_DT));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_NT)))
				{
					note = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_NT));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_HV)))
				{
					height = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_HV));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_WV)))
				{
					weight = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_WV));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_PR)))
				{
					weight = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_PR));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_TV)))
				{
					temp = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_TV));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_IV)))
				{
					intimate = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_IV));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_PV)))
				{
					protection = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_PV));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.Mood_Selected)))
				{
					if (null != jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.Mood_Selected))) moodMap.put(dateFormat.parse(date), getBackupOFMoodSelected(jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.Mood_Selected))));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.SYMTOM_SELECTED)))
				{
					
					if (null != jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.SYMTOM_SELECTED))) symptomMap.put(dateFormat.parse(date), getBackupOFSymtomSelected(jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.SYMTOM_SELECTED))));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.Medicine)))
				{
					if (null != jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.Medicine))) medicineMap.put(dateFormat.parse(date), getBackupOFMedicine(jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.Medicine))));
				}
				
				dayDetailModels.add(new DayDetailModel(0, dateFormat.parse(date), note, Boolean.valueOf(intimate), Integer.parseInt(protection), Float.parseFloat(weight), Float.parseFloat(temp), Float.parseFloat(height), PeriodTrackerObjectLocator.getInstance().getProfileId(),period));
				
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return dayDetailModels;
		
	}
	
	public static List<DayDetailModel> getDayDetailforPartner(JSONArray jsonArray) throws Exception
	{
		constantsKey = new ConstantsKey("1.0");
		List<DayDetailModel> dayDetailModels = new ArrayList<DayDetailModel>();
		
		try
		{
			for (int i = 0; i < jsonArray.length(); i++)
			{
				String date = null, note = null, height = null, weight = null, temp = null, intimate = null, protection = "0";
				int period = 3;
				ArrayList<PeriodTrackerModelInterface> moodMap = new ArrayList<PeriodTrackerModelInterface>();
				ArrayList<PeriodTrackerModelInterface> symtomsMap = new ArrayList<PeriodTrackerModelInterface>();
				ArrayList<PeriodTrackerModelInterface> medicineMap = new ArrayList<PeriodTrackerModelInterface>();
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				//	DayDetailModel detailModel = new DayDetailModel();
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_DT)))
				{
					date = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_DT));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_NT)))
				{
					note = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_NT));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_HV)))
				{
					height = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_HV));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_PR)))
				{
					period = jsonObject.getInt(constantsKey.getKey(ConstantsKey.DD_PR));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_WV)))
				{
					weight = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_WV));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_TV)))
				{
					temp = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_TV));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_IV)))
				{
					intimate = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_IV));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.DD_PV)))
				{
					protection = jsonObject.getString(constantsKey.getKey(ConstantsKey.DD_PV));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.Mood_Selected)))
				{
					if (null != jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.Mood_Selected)))
					{
						
						moodMap.addAll(getBackupOFMoodSelected(jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.Mood_Selected))));
					}
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.SYMTOM_SELECTED)))
				{
					
					if (null != jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.SYMTOM_SELECTED)))
					{
						
						symtomsMap.addAll(getBackupOFSymtomSelected(jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.SYMTOM_SELECTED))));
					}
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.Medicine)))
				{
					if (null != jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.Medicine)))
					{
						
						medicineMap.addAll(getBackupOFMedicine(jsonObject.getJSONArray(constantsKey.getKey(ConstantsKey.Medicine))));
					}
				}
				
				dayDetailModels.add(new DayDetailModel(0, dateFormat.parse(date), note, Boolean.valueOf(intimate), Integer.parseInt(protection), Float.parseFloat(weight), Float.parseFloat(temp), Float.parseFloat(height), PeriodTrackerObjectLocator.getInstance().getProfileId(),period, moodMap, symtomsMap,
						medicineMap));
				
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return dayDetailModels;
		
	}
	
	public static List<ApplicationSettingModel> getApplicationSettingModels(JSONArray jsonArray) throws Exception
	{
		
		JSONObject jsonObject;
		ApplicationSettingModel applicationSettingModel;
		List<ApplicationSettingModel> applicationSettingModels = new ArrayList<ApplicationSettingModel>();
		Object dPL = null, dCL = null, dOL = null, dWU = null, dHU = null, dTU = null, dPM = null, dSQ = null, dSA = null, dAP = null, dAL = null, dDF = null, dWV = null, dHV = null, pED = null, dPMF = null, dTV = null, pSN = null, oSN = null, fSN = null, dPSNM = null, dFSNM = null, dOSNM = null, dOW = null, dAVGL = null;
		
		String Vaule = String.valueOf(PeriodTrackerObjectLocator.getInstance().isAveraged());
		try
		{
			
			for (int i = 0; i < jsonArray.length(); i++)
			{
				jsonObject = (JSONObject) jsonArray.get(i);
				if (jsonObject.has(constantsKey.getKey(constantsKey.DPl)))
				{
					dPL = jsonObject.getString(constantsKey.getKey(constantsKey.DPl));
					if (null != dPL)
					{
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_LENGTH_KEY, dPL.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
					}
				}
				if (jsonObject.has(constantsKey.getKey(constantsKey.DCL)))
				{
					dCL = jsonObject.getString(constantsKey.getKey(constantsKey.DCL));
					if (null != dCL)
					{
						
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_CYCLE_LENGTH_KEY, dCL.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
					}
				}
				if (jsonObject.has(constantsKey.getKey(constantsKey.DOL)))
				{
					dOL = jsonObject.getString(constantsKey.getKey(constantsKey.DOL));
					if (null != dOL)
					{
						
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_LENGTH_KEY, dOL.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
					}
				}
				if (jsonObject.has(constantsKey.getKey(constantsKey.DWU)))
				{
					dWU = jsonObject.getString(constantsKey.getKey(constantsKey.DWU));
					if (null != dWU)
					{
						
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_UNIT_KEY, dWU.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
					}
				}
				if (jsonObject.has(constantsKey.getKey(constantsKey.DHU)))
				{
					dHU = jsonObject.getString(constantsKey.getKey(constantsKey.DHU));
					if (null != dHU)
					{
						
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_UNIT_KEY, dHU.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
					}
				}
				if (jsonObject.has(constantsKey.getKey(constantsKey.DTU)))
				{
					dTU = jsonObject.getString(constantsKey.getKey(constantsKey.DTU));
					
					if (null != dTU)
					{
						
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMP_UNIT_KEY, dTU.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
					}
				}
				if (jsonObject.has(constantsKey.getKey(constantsKey.DPM)))
				{
					dPM = jsonObject.getString(constantsKey.getKey(constantsKey.DPM));
					if (null != dPM)
					{
						
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTING_PREGNANT_MODE, dPM.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
					}
				}
				if (jsonObject.has(constantsKey.getKey(constantsKey.PSQ)))
				{
					dSQ = jsonObject.getString(constantsKey.getKey(constantsKey.PSQ));
					if (null != dSQ)
					{
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION, dSQ.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						
					}
				}
				if (jsonObject.has(constantsKey.getKey(constantsKey.PSA)))
				{
					dSA = jsonObject.getString(constantsKey.getKey(constantsKey.PSA));
					if (null != dSA)
					{
						
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_ANSWER, dSA.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
					}
				}
				if (jsonObject.has(constantsKey.getKey(constantsKey.DAP)))
				{
					dAP = jsonObject.getString(constantsKey.getKey(constantsKey.DAP));
					if (dAP != null)
					{
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PASSWORD_STRING_KEY, dAP.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
					}
				}
				if (jsonObject.has(constantsKey.getKey(constantsKey.DAL)))
				{
					dAL = jsonObject.getString(constantsKey.getKey(constantsKey.DAL));
					if (null != dAL)
					{
						
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_APP_LANGUAGE_KEY, dAL.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
					}
				}
				if (jsonObject.has(constantsKey.getKey(constantsKey.DDF)))
				{
					dDF = jsonObject.getString(constantsKey.getKey(constantsKey.DDF));
					if (null != dDF)
					{
						
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_DATE_FORMAT_KEY, dDF.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
					}
					if (jsonObject.has(constantsKey.getKey(constantsKey.DHV)))
					{
						dHV = jsonObject.getString(constantsKey.getKey(constantsKey.DHV));
						if (null != dHV)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_VALUE_KEY, dHV.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					if (jsonObject.has(constantsKey.getKey(constantsKey.DWV)))
					{
						dWV = jsonObject.getString(constantsKey.getKey(constantsKey.DWV));
						if (null != dWV)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_VALUE_KEY, dWV.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					
					if (jsonObject.has(constantsKey.getKey(constantsKey.DTV)))
					{
						dTV = jsonObject.getString(constantsKey.getKey(constantsKey.DTV));
						if (null != dWV)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMPERATURE_VALUE_KEY, dTV.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					if (jsonObject.has(constantsKey.getKey(constantsKey.PED)))
					{
						pED = jsonObject.getString(constantsKey.getKey(constantsKey.PED));
						if (null != pED)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_ESTIMATED_DATE_VALUE_KEY, pED.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					
					if (jsonObject.has(constantsKey.getKey(constantsKey.DPMF)))
					{
						dPMF = jsonObject.getString(constantsKey.getKey(constantsKey.DPMF));
						if (null != dPMF)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PREGNANCY_MESSAGE_FORMAT_KEY, dPMF.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					
					if (jsonObject.has(constantsKey.getKey(constantsKey.PSN)))
					{
						pSN = jsonObject.getString(constantsKey.getKey(constantsKey.PSN));
						if (null != pSN)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_KEY, pSN.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					
					if (jsonObject.has(constantsKey.getKey(constantsKey.FSN)))
					{
						fSN = jsonObject.getString(constantsKey.getKey(constantsKey.FSN));
						if (null != fSN)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_KEY, fSN.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					
					if (jsonObject.has(constantsKey.getKey(constantsKey.OSN)))
					{
						oSN = jsonObject.getString(constantsKey.getKey(constantsKey.OSN));
						if (null != oSN)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_KEY, oSN.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					
					if (jsonObject.has(constantsKey.getKey(constantsKey.OSNM)))
					{
						dOSNM = jsonObject.getString(constantsKey.getKey(constantsKey.OSNM));
						if (null != dOSNM)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_MESSAGE_KEY, dOSNM.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					
					if (jsonObject.has(constantsKey.getKey(constantsKey.PSNM)))
					{
						dPSNM = jsonObject.getString(constantsKey.getKey(constantsKey.PSNM));
						if (null != dPSNM)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_MESSAGE_KEY, dPSNM.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					
					if (jsonObject.has(constantsKey.getKey(constantsKey.FSNM)))
					{
						dFSNM = jsonObject.getString(constantsKey.getKey(constantsKey.FSNM));
						if (null != dFSNM)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_MESSAGE_KEY, dFSNM.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					
					if (jsonObject.has(constantsKey.getKey(constantsKey.DOW)))
					{
						dOW = jsonObject.getString(constantsKey.getKey(constantsKey.DOW));
						if (null != dOW)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_DAY_OF_WEEK_KEY, dOW.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					
					if (jsonObject.has(constantsKey.getKey(constantsKey.DAVGL)))
					{
						dAVGL = jsonObject.getString(constantsKey.getKey(constantsKey.DAVGL));
						if (null != dAVGL)
						{
							
							applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_AVERAGE_LENGTHS, dAVGL.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						}
					}
					else
					{
						
						applicationSettingModels.add(new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_AVERAGE_LENGTHS, Vaule.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId()));
						
					}
					
				}
			}
			
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		return applicationSettingModels;
		
	}
	
	public static List<MoodSelected> getBackupOFMoodSelected(JSONArray jsonArray) throws Exception
	{
		JSONObject jsonObject;
		List<MoodSelected> moodSelecteds = new ArrayList<MoodSelected>();
		String moodId = null, moodWeight = null, moodDayDetail;
		try
		{
			for (int i = 0; i < jsonArray.length(); i++)
			{
				jsonObject = (JSONObject) jsonArray.get(i);
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.MS_MID)))
				{
					moodId = jsonObject.getString(constantsKey.getKey(ConstantsKey.MS_MID));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.MS_MW)))
				{
					moodWeight = jsonObject.getString(constantsKey.getKey(ConstantsKey.MS_MW));
				}
				moodSelecteds.add(new MoodSelected(0, 0, Integer.valueOf(moodId), Integer.valueOf(moodWeight)));
				
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return moodSelecteds;
	}
	
	public static List<SymtomsSelectedModel> getBackupOFSymtomSelected(JSONArray jsonArray) throws Exception
	{
		JSONObject jsonObject;
		List<SymtomsSelectedModel> symtomsSelectedModels = new ArrayList<SymtomsSelectedModel>();
		String symptomId = null, symptomWeight = null, symptomDayDetail;
		try
		{
			for (int i = 0; i < jsonArray.length(); i++)
			{
				jsonObject = (JSONObject) jsonArray.get(i);
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.SS_SID)))
				{
					symptomId = jsonObject.getString(constantsKey.getKey(ConstantsKey.SS_SID));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.SS_SW)))
				{
					symptomWeight = jsonObject.getString(constantsKey.getKey(ConstantsKey.SS_SW));
				}
				symtomsSelectedModels.add(new SymtomsSelectedModel(0, Integer.valueOf(symptomId), 0, Integer.valueOf(symptomWeight)));
				
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return symtomsSelectedModels;
	}
	
	public static List<UserProfileModel> getBackupOFUserProfile(JSONArray jsonArray) throws Exception
	{
		JSONObject jsonObject;
		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();
		
		String firstName = null, lastName = null, userName = null, passwordProtection = null;
		try
		{
			for (int i = 0; i < jsonArray.length(); i++)
			{
				jsonObject = (JSONObject) jsonArray.get(i);
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.FN)))
				{
					firstName = jsonObject.getString(constantsKey.getKey(ConstantsKey.FN));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.LN)))
				{
					lastName = jsonObject.getString(constantsKey.getKey(ConstantsKey.LN));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.UN)))
				{
					userName = jsonObject.getString(constantsKey.getKey(ConstantsKey.UN));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.US_PP)))
				{
					passwordProtection = jsonObject.getString(constantsKey.getKey(ConstantsKey.US_PP));
				}
				userProfileModels.add(new UserProfileModel(0, firstName, lastName, userName, passwordProtection));
				
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return userProfileModels;
	}
	
	public static List<Pills> getBackupOFMedicine(JSONArray jsonArray) throws Exception
	{
		JSONObject jsonObject;
		List<Pills> pillsDetails = new ArrayList<Pills>();
		
		String dayDetailId, medicineName = null, quantity = null;
		try
		{
			for (int i = 0; i < jsonArray.length(); i++)
			{
				jsonObject = (JSONObject) jsonArray.get(i);
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.MDN)))
				{
					medicineName = jsonObject.getString(constantsKey.getKey(ConstantsKey.MDN));
				}
				if (jsonObject.has(constantsKey.getKey(ConstantsKey.MDQ)))
				{
					quantity = jsonObject.getString(constantsKey.getKey(ConstantsKey.MDQ));
				}
				pillsDetails.add(new Pills(0, 0, medicineName, Integer.valueOf(quantity)));
				
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return pillsDetails;
	}
	
}