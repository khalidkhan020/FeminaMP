package com.linchpin.periodtracker.partnersharing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.CalendarDayDetailModel;
import com.linchpin.periodtracker.model.TimeLineModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.APP.PartnerHome;
import com.linchpin.periodtracker.utlity.APP.WhoIAm;
import com.linchpin.periodtracker.utlity.FragList;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.SwipFragmentAdapter;
import com.linchpin.periodtracker.utlity.Utility;
//import com.linchpin.periodtracker.view.CaldroidSampleCustomFragmentView;
import com.linchpin.periodtracker.view.HomeScreenView;
import com.linchpin.periodtracker.widget.MPTProgressDialog;
import com.linchpin.periodttracker.database.CalendarDBHandler;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.Storage.JSONDocument;
import com.shephertz.app42.paas.sdk.android.storage.StorageResponseBuilder;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class PartnerHomeActivity extends FragmentActivity
{
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	
	public static int				UPDATE_PROFILE	= 0x100;
	public static int				CHECK_STATUS	= 0x101;
	private SwipFragmentAdapter		mAdapter;
	private PartnerCalenderFragment	caldroidFragment;
	private ViewPager				mPager;
	private PageIndicator			mIndicator;
	
	private String					SAVE_STATE		= "CALDROID_SAVED_STATE";
	private WebHandler				handler			= new WebHandler();
	public Messenger				msg				= new Messenger(new WebHandler());
	public Message					message			= new Message();
	static final int				REFRESH_PARTNER	= 0x1;
	static final int				FINISH			= 0x2;
	Theme							t;
	@Override
	protected void onStart()
	{
		EasyTracker.getInstance(this).activityStart(this);
		super.onStart();
	}
	
	protected void onStop()
	{
		EasyTracker.getInstance(this).activityStop(this);
		
		super.onStop();
	}
	class WebHandler extends Handler
	{
		
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case REFRESH_PARTNER:
					if (progressDialog.isShowing()) progressDialog.dismiss();
					Intent intent = getIntent();
					PartnerHomeActivity.this.finish();
					startActivity(intent);
					//Toast.makeText(PartnerHomeActivity.this, "Success", Toast.LENGTH_LONG).show();
					break;
				case FINISH:
					PartnerHomeActivity.this.finish();
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	}
	
	private void applyTheme()
	{
		
		t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			//t.applyTextColor(R.id.indicator, "heading_fg");
			t.applyBackgroundColor(R.id.indicator, "heading_bg");
			//	t.applyBackgroundDrawable(R.id.personalsettingsback, "backbuttonselctor");
			//	t.applyTextColor(R.id.continue_welcome, "co_btn_fg");
			//t.applyBackgroundDrawable(R.id.continue_welcome, "mpt_button_sltr");
			//	t.applyTextColor(R.id.personalsettings, "text_color");
			//t.applyTextColor(R.id.h1, "heading_fg");
			
		}
		/*else
		{
			setContentView(R.layout.settings);
			initLayout();
		}*/
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK)
		{
			if (requestCode == UPDATE_PROFILE)
			{
				Intent intent = getIntent();
				PartnerHomeActivity.this.finish();
				startActivity(intent);
			}
			else
			{
				Toast.makeText(PartnerHomeActivity.this, getString(R.string.refreshing), Toast.LENGTH_LONG).show();
				progressDialog.show();
				JSONObject jsonBody = new JSONObject();
				try
				{
					jsonBody.put("userid", APP.currentUser.getUserName());
					jsonBody.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
				}
				catch (JSONException e)
				{
					
				}
				
				APP.getCustomCodeService().runJavaCode("CheckStatus", jsonBody, new App42CallBack()
				{
					
					@Override
					public void onSuccess(Object arg0)
					{
						try
						{
							boolean t1, t2, t3, t4;
							JSONObject request = new JSONObject();
							request.put("userid", APP.currentUser.getUserName());
							request.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
							StorageResponseBuilder storageResponseBuilder = new StorageResponseBuilder();
							JSONObject res = (JSONObject) arg0;
							Storage storage = storageResponseBuilder.buildResponse(res.getString("data"));
							JSONDocument document = storage.getJsonDocList().get(0);
							JSONObject jsonObject = new JSONObject(document.jsonDoc);
							
							t1 = jsonObject.getString("senderid").equals(APP.currentUser.getUserName());
							t2 = jsonObject.getString("receverid").equals(APP.currentUser.getUserName());
							t3 = jsonObject.getString("userid").equals(APP.currentUser.getUserName());
							t4 = jsonObject.getString("status").equals("0") ? false : true;
							if (!t1 && t2 && !t3 && !t4) APP.PARTNER_HOME = PartnerHome.RECEIVED_INVITATION;
							else if (t1 && !t2 && !t3 && !t4) APP.PARTNER_HOME = PartnerHome.RECEVED_REUEST;
							else if (!t1 && t2 && t3 && !t4) APP.PARTNER_HOME = PartnerHome.SEND_REQUEST;
							else if (t1 && !t2 && t3 && !t4) APP.PARTNER_HOME = PartnerHome.SEND_INVITATION;
							else if (!t1 && t2 && t4) APP.PARTNER_HOME = PartnerHome.RECEVER;
							else if (t1 && !t2 && t4) APP.PARTNER_HOME = PartnerHome.SENDER;
							
							if (t1)
							{
								APP.WHOIAM = WhoIAm.SENDER;
								APP.setParnerId(jsonObject.getString("receverid"));
							}
							else
							{
								APP.WHOIAM = WhoIAm.REVEVER;
								APP.setParnerId(jsonObject.getString("senderid"));
							}
							if (APP.PARTNER_HOME == APP.PartnerHome.SENDER) PartnerUtil.getPartnerData(PartnerHomeActivity.this, null);
							
							message.what = REFRESH_PARTNER;
							msg.send(message);
							
						}
						catch (JSONException exception)
						{
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					@Override
					public void onException(Exception ex)
					{
						message.what = REFRESH_PARTNER;
						try
						{
							msg.send(message);
						}
						catch (RemoteException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/*if (ex instanceof App42Exception)
						{
							
							try
							{
								JSONObject jsonObject = new JSONObject(ex.getMessage()).getJSONObject("app42Fault").getJSONObject("trace").getJSONObject("app42Fault");
								try
								{
									
									code = jsonObject.getString("appErrorCode");
								}
								catch (JSONException jpx)
								{
								}
								
							}
							catch (JSONException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}*/
						
					}
				});
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	ArrayList<FragList>			fragments	= new ArrayList<FragList>();
	private MPTProgressDialog	progressDialog;
	
	/*public void getTimeLineModel()
	{
		
		Collections.sort(APP.GLOBAL().getTimeLineModels(), new Comparator<TimeLineModel>()
		{
			@Override
			public int compare(TimeLineModel l, TimeLineModel r)
			{
				
				int ff = Utility.setHourMinuteSecondZero(new Date(r.date)).compareTo(Utility.setHourMinuteSecondZero(new Date(l.date)));
				return ff;
			}
		});
		int size = APP.GLOBAL().getTimeLineModels().size();
		for (int i = 0; i < size - 1; i++)
		{
			TimeLineModel l = APP.GLOBAL().getTimeLineModels().get(i);
			TimeLineModel r = APP.GLOBAL().getTimeLineModels().get(i + 1);
			if (l.date == (PeriodTrackerConstants.NULL_DATE))
			{
				APP.GLOBAL().getTimeLineModels().remove(i);
				APP.GLOBAL().getTimeLineMaps().remove(l.date);
				i--;
				size--;
				break;
			}
			if (Utility.setHourMinuteSecondZero(new Date(r.date)).compareTo(Utility.setHourMinuteSecondZero(new Date(l.date))) == 0)
			{
				if (l.id == -1)
				
				{
					r.fertility_end = l.fertility_end;
					r.fertility_start = l.fertility_start;
					r.period_end = l.period_end;
					r.period_start = l.period_start;
					r.ovulaton = l.ovulaton;
					r.pregnancy = l.pregnancy;
					r.period = l.period;
					APP.GLOBAL().getTimeLineMaps().put(r.date, r);
					APP.GLOBAL().getTimeLineModels().remove(i);
					size--;
				}
				else if (r.id == -1)
				{
					l.fertility_end = r.fertility_end;
					l.fertility_start = r.fertility_start;
					l.period_end = r.period_end;
					l.period_start = r.period_start;
					l.ovulaton = r.ovulaton;
					l.pregnancy = r.pregnancy;
					l.period = r.period;
					APP.GLOBAL().getTimeLineMaps().put(l.date, l);
					APP.GLOBAL().getTimeLineModels().remove(i + 1);
					size--;
				}
			}
		}
	}
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
	//	getTimeLineModel();
		progressDialog = new MPTProgressDialog(this, R.drawable.spinner);
		progressDialog.setCancelable(false);
		setContentView(R.layout.simple_titles);
		findViewById(R.id.back_pan).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				onbackpress();
			}
		});
		Bundle args;
		t = Theme.getCurrentTheme(this);
		applyTheme();
		
		/////////////////////////Invitation////////////////////////////////
		args = new Bundle();
		fragments.add(new FragList(new InvitatinFragment(), getString(R.string.partner_title), 0));
		if (APP.PARTNER_HOME == PartnerHome.RECEVER)
		{
			// sazid fragments.add(new FragList(new PartnerToday(), getString(R.string.today_title), 0));
			///////////////////////////Calender/////////////////////////////////////
			
			caldroidFragment = new PartnerCalenderFragment();
			args = new Bundle();
			Calendar cal = Calendar.getInstance();
			//args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH));
			//args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.FIT_ALL_MONTHS, false);
			args.putInt(CaldroidFragment.FORWARD, 2);
			args.putBoolean(CaldroidFragment.BACKBAR_NEEDED, false);
			if (Integer.valueOf(PeriodTrackerObjectLocator.getInstance().getDayOfWeek()) != 0) args.putInt("startDayOfWeek", Integer.valueOf(PeriodTrackerObjectLocator.getInstance().getDayOfWeek()));
			caldroidFragment.setArguments(args);
			
			fragments.add(new FragList(caldroidFragment, getString(R.string.calender_title), 0));
			///////////////////////////Time Line/////////////////////////////////////
			fragments.add(new FragList(new PartnerTimeLineFragment(), getString(R.string.timeline_title), 0));
			
		}
		/////////////////////////Settings////////////////////////////////		
		fragments.add(new FragList(new SharingSettingFragment(), getString(R.string.setting_title), 0));
		
		///////////////////////////////////////////////////////////////////////
		mAdapter = new SwipFragmentAdapter(fragments, getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		
		mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		
	}
	
	@Override
	public void onBackPressed()
	{
		onbackpress();
	}
	
	private void onbackpress()
	{
		
		APP.GLOBAL().getEditor().putString(APP.GOAL.SAVE_WHO_AM_I.key, APP.WHOIAM.toString()).commit();
		APP.GLOBAL().getEditor().putString(APP.GOAL.MY_PARTNERSTATUS.key, APP.PARTNER_HOME.toString()).commit();
		if (APP.currentUser.getProfile().getSex().equals("Female"))
		{
			
			Intent homeActivity = new Intent(PartnerHomeActivity.this, HomeScreenView.class);
			startActivity(homeActivity);
			finish();
			APP.GLOBAL().exicuteLIOAnim(PartnerHomeActivity.this);
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(PartnerHomeActivity.this);
			builder.setTitle(getResources().getString(R.string.notifiction));
			builder.setMessage(getResources().getString(R.string.exitmessage));
			builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					
					android.os.Process.killProcess(android.os.Process.myPid());
					
				}
			});
			
			builder.setNegativeButton(getResources().getString(R.string.no), null);
			builder.show();
		}
		
	}
	
	// TODO Auto-generated method stub
	
	@Override
	protected void onResume()
	{
		if (APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null) == null && APP.currentUser == null)
		{
			this.finish();
			startActivity(new Intent(PartnerHomeActivity.this, LoginScreen.class));
		}
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PURCHASED.key, false)) PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		
		super.onResume();
	}
	
}