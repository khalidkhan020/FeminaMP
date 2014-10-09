package com.linchpin.periodtracker.partnersharing;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.ListViewAdaptor;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.APP.PartnerHome;
import com.linchpin.periodtracker.utlity.APP.WhoIAm;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodtracker.widget.HelpButton;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.Storage.JSONDocument;
import com.shephertz.app42.paas.sdk.android.storage.StorageResponseBuilder;

public class InvitatinFragment extends Fragment implements OnClickListener
{
	FrameLayout									framelayout;
	HelpButton									helpbutton;
	Button										myButton1, partnerButton1, partnerButton2;
	ImageView									myPic, partnerPic, relation;
	LinearLayout								myPan, partnerPan, heder;
	TextView									myName, myEmail, partnerName, partnerEmail;
	TextView									inviteRequestMsz;
	ListView									listView;
	ListViewAdaptor								adaptor;
	Handler										handler	= new Handler();
	TextView									awaiting_reminder_pro;
	int											status;
	private List<PeriodTrackerModelInterface>	loglist;
	Theme										t;
	
	private void applyTheme(View v)
	{
		
		t = Theme.getCurrentTheme(getActivity(), v);
		if (t != null)
		{
			t.applyBackgroundDrawable(R.id.linear, "mpt_button_sltr");
			t.applyBackgroundDrawable(R.id.my_button1, "mpt_button_sltr");
			t.applyBackgroundDrawable(R.id.partner_button1, "mpt_button_sltr");
			t.applyBackgroundDrawable(R.id.partner_button2, "mpt_button_sltr");
			t.applyTextColor(R.id.my_name, "text_color");
			t.applyTextColor(R.id.partner_name, "text_color");
			t.applyTextColor(R.id.my_email, "co_prediction_perood");
			t.applyTextColor(R.id.partner_email, "co_prediction_perood");
			t.applyTextColor(R.id.awaiting_reminder, "co_prediction_perood");
			t.applyBackgroundDrawable(R.id.linearLayout, "shp_partner_pan_bg");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.partner_invite, container, false);
		findAllviews(v);
		applyTheme(v);
		setMaleAndFemaleImage();
		myName.setOnClickListener(this);
		myEmail.setOnClickListener(this);
		adaptor = new ListViewAdaptor(loglist, getActivity(), PeriodTrackerConstants.PAST_PERIOD_RECORD_LIST_FRAGMENT);
		listView.setAdapter(adaptor);
		helpbutton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(getActivity(), HomeScreenHelp.class);
				if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
				{
					if (APP.PARTNER_HOME == PartnerHome.NO_PARTNER)
					{
						intent.putExtra("classname", "invite_no_partner");
					}
					if (APP.PARTNER_HOME != PartnerHome.NO_PARTNER)
					{
						intent.putExtra("classname", "invite_recieved_partner");
					}
					else if (APP.PARTNER_HOME == PartnerHome.RECEIVED_INVITATION || APP.PARTNER_HOME == PartnerHome.RECEVED_REUEST)
					{
						intent.putExtra("classname", "invite_with_partner");
					}
				}
				startActivity(intent);
				// TODO Auto-generated method stub
				
			}
		});
		
		if (APP.PARTNER_HOME != PartnerHome.NO_PARTNER)
		{
			awaiting_reminder_pro.setVisibility(View.GONE);
			//welcomeTxt.setVisibility(View.GONE);
			if (APP.myPartnerProfile != null)
			{
				partnerName.setText(APP.myPartnerProfile.getProfile().getFirstName() + " " + APP.myPartnerProfile.getProfile().getLastName());
				partnerEmail.setText(APP.myPartnerProfile.getUserName());
				APP.setParnerId(APP.myPartnerProfile.getUserName());
			}
			else
			{
				partnerName.setText(APP.getParnerId());
				partnerEmail.setText(APP.getParnerId());
			}
		}
		
		if (APP.PARTNER_HOME == PartnerHome.NO_PARTNER)
		{
			framelayout.setVisibility(View.GONE);
			awaiting_reminder_pro.setVisibility(View.GONE);
			//	welcomeTxt.setVisibility(View.VISIBLE);
			partnerPan.setVisibility(View.GONE);
			partnerName.setVisibility(View.GONE);
			partnerEmail.setVisibility(View.GONE);
			partnerPic.setVisibility(View.GONE);
			if (APP.currentUser.getProfile().sex.equals("Male"))
			{
				myButton1.setText(getResources().getString(R.string.Requestdetails));
				inviteRequestMsz.setText(getResources().getString(R.string.Requestmessage));
			}
			else
			{
				myButton1.setText(getResources().getString(R.string.Invitedetails));
				inviteRequestMsz.setText(getResources().getString(R.string.Invitemessage));
			}
			listView.setVisibility(View.GONE);
			heder.setVisibility(View.GONE);
			relation.setVisibility(View.GONE);
			myButton1.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View arg0)
				{
					Intent intent = new Intent(getActivity(), DialogActivity.class);
					if (APP.currentUser.getProfile().sex.equals("Male")) intent.putExtra("request_type", 1);
					
					else intent.putExtra("request_type", 0);
					getActivity().startActivityForResult(intent, PartnerHomeActivity.CHECK_STATUS);
				}
			});
			/*	myButton2.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View arg0)
					{
						Intent intent = new Intent(getActivity(), DialogActivity.class);
						
						getActivity().startActivityForResult(intent, PartnerHomeActivity.CHECK_STATUS);
					}
				});*/
			
		}
		else if (APP.PARTNER_HOME == PartnerHome.RECEIVED_INVITATION || APP.PARTNER_HOME == PartnerHome.RECEVED_REUEST)
		{
			framelayout.setVisibility(View.VISIBLE);
			partnerEmail.setText(APP.getParnerId());
			myPan.setVisibility(View.GONE);
			partnerButton1.setText("Accept");
			partnerButton2.setText("Decline");
			listView.setVisibility(View.GONE);
			heder.setVisibility(View.GONE);
			relation.setVisibility(View.VISIBLE);
			relation.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_awaiting));
			partnerButton1.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					JSONObject jsonBody = new JSONObject();
					try
					{
						
						jsonBody.put("userid", APP.currentUser.userName);
						jsonBody.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
						jsonBody.put("status", "1");
					}
					catch (JSONException e)
					{
						
					}
					
					APP.getCustomCodeService().runJavaCode("UpdateStatus", jsonBody, new App42CallBack()
					{
						
						@Override
						public void onException(Exception arg0)
						{
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onSuccess(Object arg0)
						{
							invalidate(arg0);
						}
					});
					
				}
			});
			partnerButton2.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					JSONObject jsonBody = new JSONObject();
					try
					{
						
						jsonBody.put("userid", APP.currentUser.userName);
						jsonBody.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
						
					}
					catch (JSONException e)
					{
						
					}
					
					APP.getCustomCodeService().runJavaCode("RemovePartner", jsonBody, new App42CallBack()
					{
						
						@Override
						public void onException(Exception arg0)
						{
							Toast.makeText(getActivity(), "wdafd", Toast.LENGTH_SHORT).show();
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onSuccess(Object arg0)
						{
							APP.PARTNER_HOME = APP.PARTNER_HOME.NO_PARTNER;
							APP.WHOIAM = WhoIAm.NO_ONE;
							APP.setParnerId("");
							APP.myPartnerProfile=null;
							
							try
							{
								if (APP.PARTNER_HOME == APP.PartnerHome.SENDER) PartnerUtil.getPartnerData(((PartnerHomeActivity) getActivity()),null);
								Message message = new Message();
								message.what = PartnerHomeActivity.REFRESH_PARTNER;
								((PartnerHomeActivity) getActivity()).msg.send(message);
								APP.myPartnerProfile=null;
							}
							catch (RemoteException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//						invalidate(arg0);
							catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							catch (InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					
				}
			});
		}
		else if (APP.PARTNER_HOME == PartnerHome.SEND_INVITATION || APP.PARTNER_HOME == PartnerHome.SEND_REQUEST)
		{
			framelayout.setVisibility(View.VISIBLE);
			awaiting_reminder_pro.setVisibility(View.VISIBLE);
			partnerEmail.setText(APP.getParnerId());
			myPan.setVisibility(View.GONE);
			partnerButton1.setText("Awaiting");
			partnerButton1.setEnabled(false);
			partnerButton2.setVisibility(View.GONE);
			listView.setVisibility(View.GONE);
			heder.setVisibility(View.GONE);
			relation.setVisibility(View.VISIBLE);
			relation.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_awaiting));
			
		}
		else if (APP.PARTNER_HOME == PartnerHome.SENDER || APP.PARTNER_HOME == PartnerHome.RECEVER)
		{
			framelayout.setVisibility(View.VISIBLE);
			partnerEmail.setText(APP.getParnerId());
			myPan.setVisibility(View.GONE);
			partnerButton1.setVisibility(View.GONE);
			partnerButton2.setVisibility(View.GONE);
			relation.setVisibility(View.VISIBLE);
			
			relation.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_accepted));
			
			if (APP.PARTNER_HOME == PartnerHome.RECEVER)
			{
				
				if (loglist != null) loglist.clear();
				else loglist = new ArrayList<PeriodTrackerModelInterface>();
				
				List<PeriodLogModel> logModels = APP.GLOBAL().getPartnerPLModels();
				if (logModels != null) for (PeriodLogModel logModel : logModels)
				{
					heder.setVisibility(View.VISIBLE);
					loglist.add(logModel);
				}
				if (loglist.isEmpty()) heder.setVisibility(View.GONE);
				else listView.setVisibility(View.VISIBLE);
				adaptor = new ListViewAdaptor(loglist, getActivity(), PeriodTrackerConstants.PAST_PERIOD_RECORD_LIST_FRAGMENT);
				listView.setAdapter(adaptor);
				adaptor.notifyDataSetChanged();
			}
			else
			{
				listView.setVisibility(View.GONE);
				heder.setVisibility(View.GONE);
			}
		}
		return v;
	}
	
	private void setMaleAndFemaleImage()
	{
		// TODO Auto-generated method stub
		if (APP.currentUser.getProfile().getSex().equals("Male"))
		{
			APP.GLOBAL().getEditor().putBoolean(APP.GENDER.MALE.key, true).commit();
			APP.GLOBAL().getEditor().putBoolean(APP.GENDER.FEMALE.key, false).commit();
		}
		else
		{
			APP.GLOBAL().getEditor().putBoolean(APP.GENDER.FEMALE.key, true).commit();
			APP.GLOBAL().getEditor().putBoolean(APP.GENDER.MALE.key, false).commit();
		}
		myEmail.setText(APP.currentUser.getUserName());
		myName.setText(APP.currentUser.getProfile().getFirstName() + " " + APP.currentUser.getProfile().getLastName());
		try
		{
				if (APP.currentUser.getProfile().sex.equals("Male")) myPic.setImageResource(R.drawable.cool_boy);
				else myPic.setImageResource(R.drawable.cool_girl);
						
			if (APP.myPartnerProfile == null)
			{
				if (APP.currentUser.getProfile().sex.equals("Male"))
					{
						partnerPic.setImageResource(R.drawable.cool_girl);
						
					}
					else
					{
						partnerPic.setImageResource(R.drawable.cool_boy);
					}
			}
			else
			{
				if (APP.myPartnerProfile.getProfile().sex.equals("Male")) partnerPic.setImageResource(R.drawable.cool_boy);
				else partnerPic.setImageResource(R.drawable.cool_girl);
			}
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void findAllviews(View v)
	{
		myButton1 = (Button) v.findViewById(R.id.my_button1);
		//	myButton2 = (Button) v.findViewById(R.id.my_button2);
		partnerButton1 = (Button) v.findViewById(R.id.partner_button1);
		partnerButton2 = (Button) v.findViewById(R.id.partner_button2);
		myPic = (ImageView) v.findViewById(R.id.my_pic);
		partnerPic = (ImageView) v.findViewById(R.id.partner_pic);
		myName = (TextView) v.findViewById(R.id.my_name);
		myEmail = (TextView) v.findViewById(R.id.my_email);
		partnerName = (TextView) v.findViewById(R.id.partner_name);
		partnerEmail = (TextView) v.findViewById(R.id.partner_email);
		myPan = (LinearLayout) v.findViewById(R.id.my_button_pan);
		partnerPan = (LinearLayout) v.findViewById(R.id.partner_button_pan);
		heder = (LinearLayout) v.findViewById(R.id.linear);
		listView = (ListView) v.findViewById(R.id.pastlist);
		inviteRequestMsz = (TextView) v.findViewById(R.id.requestmessage);
		relation = (ImageView) v.findViewById(R.id.relation);
		awaiting_reminder_pro = (TextView) v.findViewById(R.id.awaiting_reminder);
		helpbutton = (HelpButton) v.findViewById(R.id.partnerinvitehelpbutton);
		framelayout = (FrameLayout) v.findViewById(R.id.linearLayout2);
	}
	
	private void invalidate(Object arg0)
	{
		
		try
		{
			boolean t1, t2, t3, t4;
			//JSONObject request = new JSONObject();
			//request.put("userid", APP.currentUser.getUserName());
			//request.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
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
			if (APP.PARTNER_HOME == APP.PartnerHome.SENDER) PartnerUtil.getPartnerData(((PartnerHomeActivity) getActivity()),null);
			Message message = new Message();
			message.what = PartnerHomeActivity.REFRESH_PARTNER;
			((PartnerHomeActivity) getActivity()).msg.send(message);
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
	public void onClick(View v)
	{
		Intent intent = new Intent(getActivity(), UpdateUserProfile.class);
		startActivityForResult(intent, PartnerHomeActivity.UPDATE_PROFILE);
		
	}
	
}
