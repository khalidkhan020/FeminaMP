package com.linchpin.periodtracker.partnersharing;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.APP.PartnerHome;
import com.linchpin.periodtracker.utlity.APP.WhoIAm;
import com.linchpin.periodtracker.widget.MPTProgressDialog;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Exception;

public class SharingSettingFragment extends Fragment implements OnCheckedChangeListener, OnClickListener
{
	
	//Button				sharedata;
	RelativeLayout				periods, moods, symptoms, pills, notes, notification, removePartner, signout, updateProfile, changePassword, syncData;
	CheckBox					cb_periods, cb_moods, cb_symptoms, cb_pills, cb_notes, cb_notification;
	TextView					appsetting, sharing_setting, txtRemovePartner;
	LinearLayout				appsettingPan, sharingSettingPan;
	Handler						handler	= new Handler();
	int							status;
	TextView					txt_remove_partner_pro;
	/**Description in the remove partner option*/
	private JSONObject			jsonObject;
	private MPTProgressDialog	myDialog;
	/*HelpButton                  helpbutton;*/
	LinearLayout				show_more_settings;
	Theme                       t;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
	}
	private void applyTheme(View v)
	{   
		
		t=Theme.getCurrentTheme(getActivity(),v);
		if(t!=null)
		{
			t.applyBackgroundColor(R.id.content, "newsettingsbackground");
			t.applyBackgroundDrawable(R.id.shar_setting, "mpt_button_sltr");
			t.applyBackgroundDrawable(R.id.app_setting, "mpt_button_sltr");
			t.applyTextColor(R.id.pt, "text_color");
			t.applyTextColor(R.id.mt, "text_color");
			t.applyTextColor(R.id.pit, "text_color");
			t.applyTextColor(R.id.nt, "text_color");
			t.applyTextColor(R.id.ntt, "text_color");
			t.applyTextColor(R.id.st, "text_color");
			t.applyTextColor(R.id.sy, "text_color");
			t.applyTextColor(R.id.up, "text_color");
			t.applyTextColor(R.id.txt_remove_partner, "text_color");
			t.applyTextColor(R.id.change_password_t, "text_color");
			t.applyTextColor(R.id.signout_t, "text_color");
			t.applyButtonCheckBox(R.id.cb_moods,"checkboxselector_partner");
			t.applyButtonCheckBox(R.id.cb_notes,"checkboxselector_partner");
			t.applyButtonCheckBox(R.id.cb_notify,"checkboxselector_partner");
			t.applyButtonCheckBox(R.id.cb_periods,"checkboxselector_partner");
			t.applyButtonCheckBox(R.id.cb_pills,"checkboxselector_partner");
			t.applyButtonCheckBox(R.id.cb_symptoms,"checkboxselector_partner");
		}
	}
	private void findViws(View v)
	{
		sharing_setting = (TextView) v.findViewById(R.id.shar_setting);
		sharingSettingPan = (LinearLayout) v.findViewById(R.id.shar_setting_pan);
		periods = (RelativeLayout) v.findViewById(R.id.periods);
		moods = (RelativeLayout) v.findViewById(R.id.moods);
		symptoms = (RelativeLayout) v.findViewById(R.id.symptoms);
		pills = (RelativeLayout) v.findViewById(R.id.pills);
		notes = (RelativeLayout) v.findViewById(R.id.notes);
		notification = (RelativeLayout) v.findViewById(R.id.notifications);
		syncData = (RelativeLayout) v.findViewById(R.id.sync_data);
		cb_periods = (CheckBox) v.findViewById(R.id.cb_periods);
		cb_moods = (CheckBox) v.findViewById(R.id.cb_moods);
		cb_symptoms = (CheckBox) v.findViewById(R.id.cb_symptoms);
		cb_pills = (CheckBox) v.findViewById(R.id.cb_pills);
		cb_notes = (CheckBox) v.findViewById(R.id.cb_notes);
		cb_notification = (CheckBox) v.findViewById(R.id.cb_notify);
		/*	helpbutton=(HelpButton)v.findViewById(R.id.partnersettinghelpbutton);*/
		//sharedata = (Button) v.findViewById(R.id.share_data);
		show_more_settings = (LinearLayout) v.findViewById(R.id.show_more);
		appsetting = (TextView) v.findViewById(R.id.app_setting);
		appsettingPan = (LinearLayout) v.findViewById(R.id.app_setting_pan);
		txtRemovePartner = (TextView) v.findViewById(R.id.txt_remove_partner);
		updateProfile = (RelativeLayout) v.findViewById(R.id.update_profile);
		removePartner = (RelativeLayout) v.findViewById(R.id.remove_partner);
		changePassword = (RelativeLayout) v.findViewById(R.id.change_password);
		signout = (RelativeLayout) v.findViewById(R.id.signout);
		txt_remove_partner_pro = (TextView) v.findViewById(R.id.txt_remove_partner_d);
	}
	
	private void setListners()
	{
		signout.setOnClickListener(this);
		removePartner.setOnClickListener(this);
		updateProfile.setOnClickListener(this);
		changePassword.setOnClickListener(this);
		syncData.setOnClickListener(this);
		periods.setOnClickListener(this);
		moods.setOnClickListener(this);
		symptoms.setOnClickListener(this);
		pills.setOnClickListener(this);
		notes.setOnClickListener(this);
		notification.setOnClickListener(this);
		//	sharedata.setOnClickListener(this);
		
		cb_periods.setOnCheckedChangeListener(this);
		cb_moods.setOnCheckedChangeListener(this);
		cb_symptoms.setOnCheckedChangeListener(this);
		cb_pills.setOnCheckedChangeListener(this);
		cb_notes.setOnCheckedChangeListener(this);
		cb_notification.setOnCheckedChangeListener(this);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.partner_sharing_setting, container, false);
		findViws(v);
		applyTheme(v);
		setListners();
		cb_periods.setChecked(((APP) getActivity().getApplicationContext()).getPreferences().getBoolean(APP.PREF.SHARE_PERIODS.key, true));
		cb_moods.setChecked(((APP) getActivity().getApplicationContext()).getPreferences().getBoolean(APP.PREF.SHARE_MOODS.key, true));
		cb_symptoms.setChecked(((APP) getActivity().getApplicationContext()).getPreferences().getBoolean(APP.PREF.SHARE_SYMPTOMS.key, true));
		cb_pills.setChecked(((APP) getActivity().getApplicationContext()).getPreferences().getBoolean(APP.PREF.SHARE_PILLS.key, false));
		cb_notes.setChecked(((APP) getActivity().getApplicationContext()).getPreferences().getBoolean(APP.PREF.SHARE_NOTES.key, false));
		cb_notification.setChecked(((APP) getActivity().getApplicationContext()).getPreferences().getBoolean(APP.PREF.SHARE_NOTIFY.key, true));
		
		if (APP.PARTNER_HOME == PartnerHome.SENDER || APP.PARTNER_HOME == PartnerHome.RECEVED_REUEST || APP.PARTNER_HOME == PartnerHome.SEND_INVITATION)
		{
			sharing_setting.setVisibility(View.VISIBLE);
			sharingSettingPan.setVisibility(View.VISIBLE);
		}
		else
		{
			sharing_setting.setVisibility(View.GONE);
			sharingSettingPan.setVisibility(View.GONE);
		}
		if (APP.PARTNER_HOME == PartnerHome.SENDER || APP.PARTNER_HOME == PartnerHome.RECEVER || APP.PARTNER_HOME == PartnerHome.SEND_INVITATION || APP.PARTNER_HOME == PartnerHome.SEND_REQUEST)
		{
			if (APP.PARTNER_HOME == PartnerHome.SENDER || APP.PARTNER_HOME == PartnerHome.RECEVER) txtRemovePartner.setText("Remove Partner");
			else
			{
				txtRemovePartner.setText("Cancel Invitation/Request");
				txt_remove_partner_pro.setText("Tap to cancel the Invitation/Request");
			}
			
			removePartner.setVisibility(View.VISIBLE);
		}
		else removePartner.setVisibility(View.GONE);
		myDialog = new MPTProgressDialog(getActivity(), R.drawable.spinner);
		
		myDialog.setCancelable(false);
		return v;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton b, boolean isChecked)
	{
		int id = b.getId();
		if (id == R.id.cb_periods)
		{
			((APP) getActivity().getApplication()).getEditor().putBoolean(APP.PREF.SHARE_PERIODS.key, isChecked).commit();
		}
		else if (id == R.id.cb_moods)
		{
			((APP) getActivity().getApplication()).getEditor().putBoolean(APP.PREF.SHARE_MOODS.key, isChecked).commit();
		}
		else if (id == R.id.cb_symptoms)
		{
			((APP) getActivity().getApplication()).getEditor().putBoolean(APP.PREF.SHARE_SYMPTOMS.key, isChecked).commit();
		}
		else if (id == R.id.cb_pills)
		{
			((APP) getActivity().getApplication()).getEditor().putBoolean(APP.PREF.SHARE_PILLS.key, isChecked).commit();
		}
		else if (id == R.id.cb_notes)
		{
			((APP) getActivity().getApplication()).getEditor().putBoolean(APP.PREF.SHARE_NOTES.key, isChecked).commit();
		}
		else if (id == R.id.cb_notify)
		{
			((APP) getActivity().getApplication()).getEditor().putBoolean(APP.PREF.SHARE_NOTIFY.key, isChecked).commit();
		}
		else
		{
		}
		
	}
	
	@Override
	public void onClick(View v)
	{
		//sazid
		final JSONObject jsonBody = new JSONObject();
		try
		{
			
			jsonBody.put("userid", APP.currentUser.userName);
			jsonBody.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
		}
		catch (JSONException e)
		{
			
		}
		Intent intent;
		int id = v.getId();
		if (id == R.id.periods)
		{
			cb_periods.setChecked(!cb_periods.isChecked());
		}
		else if (id == R.id.moods)
		{
			cb_moods.setChecked(!cb_moods.isChecked());
		}
		else if (id == R.id.symptoms)
		{
			cb_symptoms.setChecked(!cb_symptoms.isChecked());
		}
		else if (id == R.id.pills)
		{
			cb_pills.setChecked(!cb_pills.isChecked());
		}
		else if (id == R.id.notes)
		{
			cb_notes.setChecked(!cb_notes.isChecked());
		}
		else if (id == R.id.notifications)
		{
			cb_notification.setChecked(!cb_notification.isChecked());
		}
		else if (id == R.id.sync_data)
		{
			if (APP.GLOBAL().isNetworkAvailable())
			{
				myDialog.show();
				if (APP.WHOIAM == APP.WhoIAm.REVEVER) try
				{
					PartnerUtil.getPartnerData();
				}
				catch (InterruptedException e3)
				{
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				else try
				{
					PartnerUtil.setPartnerDate();
				}
				catch (InterruptedException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				handler.postDelayed(new Runnable()
				{
					
					@Override
					public void run()
					{
						myDialog.dismiss();
						
					}
				}, 3000);
			}
			else
				Toast.makeText(getActivity(), getResources().getString(R.string.no_data_connection), Toast.LENGTH_SHORT).show();
			
		}
		else if (id == R.id.signout)
		{
			if (APP.GLOBAL().isNetworkAvailable())
			{
				new AlertDialog.Builder(getActivity()).setTitle("Confiramation")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage("Are you sure you want to Signout?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						myDialog.show();
						try
						{
							Session.getActiveSession().closeAndClearTokenInformation();
						}
						catch (Exception e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						APP.getCustomCodeService().runJavaCode("Logout", jsonBody, new App42CallBack()
						{
							
							@Override
							public void onSuccess(Object arg0)
							{
								APP.GLOBAL().getEditor().putString(APP.PREF.CURRENT_SESSION.key, null).commit();
								APP.currentUser = null;
								Message message = new Message();
								message.what = PartnerHomeActivity.REFRESH_PARTNER;
								try
								{
									((PartnerHomeActivity) getActivity()).msg.send(message);
								}
								catch (RemoteException e)
								{
									
									e.printStackTrace();
								}
								handler.post(new Runnable()
								{
									
									@Override
									public void run()
									{
										if (myDialog.isShowing()) myDialog.dismiss();
										
									}
								});
								
							}
							
							@Override
							public void onException(Exception arg0)
							{
								if (arg0 instanceof App42Exception)
								{
									if (((App42Exception) arg0).getAppErrorCode() == 2201)
									{
										APP.GLOBAL().getEditor().putString(APP.PREF.CURRENT_SESSION.key, null).commit();
										APP.currentUser = null;
										Message message = new Message();
										message.what = PartnerHomeActivity.FINISH;
										try
										{
											((PartnerHomeActivity) getActivity()).msg.send(message);
										}
										catch (RemoteException e)
										{
											
											e.printStackTrace();
										}
									}
									else if(((App42Exception) arg0).getAppErrorCode() == 1903)
									{
										APP.GLOBAL().getEditor().putString(APP.PREF.CURRENT_SESSION.key, null).commit();
										APP.currentUser = null;
										Message message = new Message();
										message.what = PartnerHomeActivity.REFRESH_PARTNER;
										try
										{
											((PartnerHomeActivity) getActivity()).msg.send(message);
										}
										catch (RemoteException e)
										{
											
											e.printStackTrace();
										}
									}
								}
								handler.post(new Runnable()
								{
									
									@Override
									public void run()
									{
										if (myDialog.isShowing()) myDialog.dismiss();
										
									}
								});
							}
						});
					}
				})
				.setNegativeButton("No", null).show();		   
			}
			else
				Toast.makeText(getActivity(), getResources().getString(R.string.no_data_connection), Toast.LENGTH_SHORT).show();
		}
		else if (id == R.id.update_profile)
		{
			if (APP.GLOBAL().isNetworkAvailable())
		{
			intent = new Intent(getActivity(), UpdateUserProfile.class);
			startActivityForResult(intent, PartnerHomeActivity.UPDATE_PROFILE);
		}
		else
			Toast.makeText(getActivity(), getResources().getString(R.string.no_data_connection), Toast.LENGTH_SHORT).show();
		
		}
		else if (id == R.id.change_password)
		{
			if (APP.GLOBAL().isNetworkAvailable())
			{
			intent = new Intent(getActivity(), ChangePasssword.class);
			startActivity(intent);
			}
			else
				Toast.makeText(getActivity(), getResources().getString(R.string.no_data_connection), Toast.LENGTH_SHORT).show();
		}
		else if (id == R.id.remove_partner)
		{

			if (APP.GLOBAL().isNetworkAvailable())
			{
			new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Confiramation").setMessage("Are you sure you want to " + txtRemovePartner.getText() + "..?").setPositiveButton("Yes", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					myDialog.show();
					APP.getCustomCodeService().runJavaCode("RemovePartner", jsonBody, new App42CallBack()
					{
						
						@Override
						public void onException(Exception arg0)
						{
							handler.post(new Runnable()
							{
								
								@Override
								public void run()
								{
									if (myDialog.isShowing()) myDialog.dismiss();
									
								}
							});
							
						}
						
						@Override
						public void onSuccess(Object arg0)
						{
							try
							{
								APP.PARTNER_HOME = PartnerHome.NO_PARTNER;
								
								APP.WHOIAM = WhoIAm.NO_ONE;
								//	APP.currentSession = null;
								//	APP.currentUser = null;
								Message message = new Message();
								message.what = PartnerHomeActivity.REFRESH_PARTNER;
								((PartnerHomeActivity) getActivity()).msg.send(message);
								handler.post(new Runnable()
								{
									
									@Override
									public void run()
									{
										if (myDialog.isShowing()) myDialog.dismiss();
										
									}
								});
							}
							catch (Exception e)
							{
							}
							
						}
					});
				}
				
			}).setNegativeButton("No", null).show();
		}
		else
			Toast.makeText(getActivity(), getResources().getString(R.string.no_data_connection), Toast.LENGTH_SHORT).show();
		}
	}
}
