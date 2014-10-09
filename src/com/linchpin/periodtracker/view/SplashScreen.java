package com.linchpin.periodtracker.view;

import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.DisplayMetrics;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.TimeLineModel;
import com.linchpin.periodtracker.partnersharing.LoginScreen;
import com.linchpin.periodtracker.partnersharing.PartnerHomeActivity;
import com.linchpin.periodtracker.partnersharing.PartnerUtil;
import com.linchpin.periodtracker.partnersharing.PartnerUtil.onPartnerData;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.APP.PARTNER;
import com.linchpin.periodtracker.utlity.APP.PartnerHome;
import com.linchpin.periodtracker.utlity.ConstantsKey;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodttracker.database.ImportDatabase;
import com.shephertz.app42.paas.sdk.android.user.UserResponseBuilder;

public class SplashScreen extends Activity
{
	Handler	handler	= new Handler();
	
	@Override
	protected void onResume()
	{
		super.onResume();
		/*nextbuttonselector.xml	try
			{
				Field f=com.lptpl.tipoftheday.R.string.class.getField("app_name");nextbuttonselector.xml		f.setAccessible(true);
				int dd=(Integer) f.get(com.lptpl.tipoftheday.R.string.class);
				 String icodn = getResources().getString(dd);
				int resId = getResources().getIdentifier("app_name", "string",this.getPackageName()); 
		        String icon = getResources().getString(resId);
		      System.out.println(""); 
		        
			}
			catch (NoSuchFieldException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		
		//		Tips.viewforTips(this, APP.TipsPath.AppIcon.id, APP.TipsPath.AppIcon.path, "tip_appico_heading", "tip_appico_msg");
		//		
		//		Tips.viewforTips(this, APP.TipsPath.RegionlSetting.id, APP.TipsPath.RegionlSetting.path, "tip_regional_setting_heading", "tip_regional_setting_msg");
		//		Tips.viewforTips(this, APP.TipsPath.TimeLine.id, APP.TipsPath.TimeLine.path, "tip_translation_heading", "tip_translation_msg");
		//		Tips.viewforTips(this, APP.TipsPath.Language.id, APP.TipsPath.Language.path, "tip_language_heading", "tip_language_msg");
		//		
		//		Tips.viewforTips(this, APP.TipsPath.AdsFree.id, APP.TipsPath.AdsFree.path, "tip_buy_heading", "tip_buy_msg");
		//		Tips.viewforTips(this, APP.TipsPath.AppIcon.id, APP.TipsPath.AppIcon.path, "tip_appico_heading", "tip_appico_msg");
		//		Tips.viewforTips(this, APP.TipsPath.Notification.id, APP.TipsPath.Notification.path, "tip_notification_heading", "tip_notification_msg");
		//		Tips.viewforTips(this, APP.TipsPath.Pill.id, APP.TipsPath.Pill.path, "tip_pils_heading", "tip_pils_msg");
		//		Tips.viewforTips(this, APP.TipsPath.Passcode.id, APP.TipsPath.Passcode.path, "tip_passcode_heading", "tip_passcode_msg");
		//		Tips.viewforTips(this, APP.TipsPath.Wiidget.id, APP.TipsPath.Wiidget.path, "tip_widget_heading", "tip_widget_msg");
		
	}
	
	public void selectStartScreen()
	{
		if (APP.GLOBAL().getPreferences().getBoolean(APP.GENDER.MALE.key, false))
		{
			
			if (APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null) == null)
			{
				SplashScreen.this.startActivity(new Intent(SplashScreen.this, LoginScreen.class));
				SplashScreen.this.finish();
			}
			else
			{
				if (APP.currentUser == null)
				{
					
					//Object usr=(Object)APP.GLOBAL().getPreferences().getString(APP.GOAL.SAVE_USER_INFO.key, null);
					try
					{
						JSONObject jsonuser = new JSONObject(APP.GLOBAL().getPreferences().getString(APP.GOAL.SAVE_CURRENTUSER_INFO.key, null));
						
						APP.currentUser = (new UserResponseBuilder()).buildResponse((jsonuser).toString());
						APP.PARTNER_HOME = APP.PartnerHome.valueOf(APP.GLOBAL().getPreferences().getString(APP.GOAL.MY_PARTNERSTATUS.key, null));
						if (APP.PARTNER_HOME != PartnerHome.NO_PARTNER)
						{
							JSONObject jsonpartneruser = new JSONObject(APP.GLOBAL().getPreferences().getString(APP.GOAL.SAVE_PARTNERUSER_INFORMATION.key, null));
							APP.myPartnerProfile = (new UserResponseBuilder()).buildResponse((jsonpartneruser).toString());
							APP.WHOIAM = APP.WhoIAm.valueOf(APP.GLOBAL().getPreferences().getString(APP.GOAL.SAVE_WHO_AM_I.key, null));
							if (APP.WHOIAM == APP.WhoIAm.REVEVER) PartnerUtil.getPartnerData(SplashScreen.this, new onPartnerData()
							{
								
								@Override
								public void onSuccess(Object arg0)
								{
									SplashScreen.this.startActivity(new Intent(SplashScreen.this, PartnerHomeActivity.class));
									SplashScreen.this.finish();
									
								}
								
								@Override
								public void onException(Exception arg0)
								{
									JSONObject res;
									try
									{
										res = new JSONObject(APP.GLOBAL().getPreferences().getString("partnerdata", null));
										if (res != null)
										{
											ConstantsKey constantsKey = new ConstantsKey("1.0");
											APP.GLOBAL().getPartnerDDModels().clear();
											try
											{
												APP.GLOBAL().getPartnerDDModels().addAll(ImportDatabase.getDayDetailforPartner(res.getJSONObject("appdata").getJSONArray(constantsKey.getKey(ConstantsKey.Day_Detail))));
											}
											catch (JSONException exception)
											{
											}
											try
											{
												APP.GLOBAL().storePartnerLengths(res.getJSONObject("appdata").getJSONObject(PARTNER.LENGTH_TABLE.key));
											}
											catch (JSONException e)
											{
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											APP.GLOBAL().getPartnerPLModels().clear();
											try
											{
												APP.GLOBAL().getPartnerPLModels().addAll(ImportDatabase.getPeriodlogTableBackup(res.getJSONObject("appdata").getJSONArray(constantsKey.getKey(ConstantsKey.PERIOD_TRACK))));
											}
											catch (JSONException exception)
											{
											}
											TimeLineModel.createModel();
										}
										
									}
									catch (JSONException exception)
									{
									}
									catch (Exception e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									SplashScreen.this.startActivity(new Intent(SplashScreen.this, PartnerHomeActivity.class));
									SplashScreen.this.finish();
									
								}
							});
							
							else PartnerUtil.setPartnerDate();
							
							handler.postDelayed(new Runnable()
							{
								
								@Override
								public void run()
								{
									//myDialog.dismiss();
									
								}
							}, 3000);
						}
						
						//APP.GLOBAL().getEditor().putString(APP.PREF.CURRENT_SESSION.key, (jsonuser).getString("sessionid")).commit();
					}
					catch (Exception e)
					{
						SplashScreen.this.startActivity(new Intent(SplashScreen.this, LoginScreen.class));
						SplashScreen.this.finish();
					}
				}
			}
		}
		else
		{
			/*if(APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null)!=null)
			{	
				try
				{
					JSONObject jsonuser=new JSONObject(APP.GLOBAL().getPreferences().getString(APP.GOAL.SAVE_USER_INFO.key, null));
					APP.currentUser = (new UserResponseBuilder()).buildResponse((jsonuser).toString());
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			SplashScreen.this.startActivity(new Intent(SplashScreen.this, HomeScreenView.class));
			SplashScreen.this.finish();
		}
		// TODO Auto-generated method stub
		
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try
		{
			
			setContentView(R.layout.splash);
			//	Utility.setAllNotiifications(this.getApplicationContext());
			PeriodTrackerObjectLocator.getInstance(this);
			
			// ((AdView)findViewById(R.id.includeadview)).loadAd(adRequest)(PeriodTrackerObjectLocator.getInstance().get)
			Locale locale = new Locale(PeriodTrackerObjectLocator.getInstance().getAppLanguage());
			Resources res = this.getResources();
			// Change locale settings in the app.
			DisplayMetrics dm = res.getDisplayMetrics();
			android.content.res.Configuration conf = res.getConfiguration();
			conf.locale = locale;
			res.updateConfiguration(conf, dm);
			//final 		
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					
					if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.IS_FIRST_TIME.key, false))
					{
						Intent showWelcomeScreen = null;
						if (!APP.GLOBAL().getPreferences().getBoolean(APP.GOAL.IS_HOME_SCREEN_VISITED.key, false))
						{
							APP.GLOBAL().getEditor().putLong(APP.PREF.INSTALL_DATE.key, Utility.setHourMinuteSecondZero(new Date()).getTime()).commit();
							showWelcomeScreen = new Intent(SplashScreen.this, WelcomeScreen.class);
							
						}
						else
						{
							showWelcomeScreen = new Intent(SplashScreen.this, SelectGoal.class);
						}
						startActivity(showWelcomeScreen);
						finish();
					}
					else
					{
						if (PeriodTrackerObjectLocator.getInstance().getPasswordProtection().equals(""))
						{
							selectStartScreen();
						}
						else
						{
							startActivityForResult(new Intent(SplashScreen.this, EnterPassword.class), 1001);
							
						}
						
					}
					
				}
				
			}, 1000);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		
		EasyTracker.getInstance(this).activityStart(this);
		
	}
	
	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
		
	}
	
	private void createFirstScreen()
	{
		startActivity(new Intent(this, HomeScreenView.class));
		finish();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 1001 && resultCode == RESULT_OK)
		{
			//finish();
			selectStartScreen();
			
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	private void creatingSplashScreen()
	{
		new CountDownTimer(2500, 1000)
		{
			public void onTick(long millisUntilFinished)
			{
			}
			
			public void onFinish()
			{
				createFirstScreen();
			}
		}.start();
	}
}
