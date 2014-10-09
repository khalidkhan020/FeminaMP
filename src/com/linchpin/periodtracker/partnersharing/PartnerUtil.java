package com.linchpin.periodtracker.partnersharing;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Message;
import android.os.RemoteException;

import com.linchpin.periodtracker.model.TimeLineModel;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.ConstantsKey;
import com.linchpin.periodtracker.utlity.ExportDataBase;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.APP.PARTNER;
import com.linchpin.periodtracker.utlity.APP.PartnerHome;
import com.linchpin.periodtracker.utlity.APP.WhoIAm;
import com.linchpin.periodttracker.database.ImportDatabase;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageResponseBuilder;
import com.shephertz.app42.paas.sdk.android.storage.Storage.JSONDocument;

public class PartnerUtil
{
	public static void setPartnerDate() throws InterruptedException
	{
		
		JSONObject jsonBody = new JSONObject();
		try
		{
			
			jsonBody.put("userid", APP.currentUser.userName);
			jsonBody.put("sessionid", 	APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key,null));
			ExportDataBase exportConstants = new ExportDataBase(APP.GLOBAL(), PeriodTrackerObjectLocator.getInstance().getApplicationVersion());			
			jsonBody.put("appdata", exportConstants.getFinalExportData());
		}
		catch (JSONException e)
		{
			
		}
		Thread.sleep(500);
		APP.getCustomCodeService().runJavaCode("UploadData", jsonBody, new App42CallBack()
		{
			
			@Override
			public void onException(Exception arg0)
			{
				System.out.println("");
				
			}
			
			@Override
			public void onSuccess(Object arg0)
			{
				System.out.println("");
				
			}
		});
		
	}
	
	public static void getPartnerData() throws InterruptedException
	{
		try
		{
			getPartnerData(null,null);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public interface onPartnerData
	{
		public void onSuccess(Object arg0);
		public void onException(Exception arg0);
	}
	public static void getPartnerData(final Activity activity,final onPartnerData nitify) throws JSONException, InterruptedException
	{
	
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("userid", APP.currentUser.getUserName());
		jsonBody.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key,null));
		Thread.sleep(500);
		APP.getCustomCodeService().runJavaCode("GetPartnerData", jsonBody, new App42CallBack()
		{
			
			@Override
			public void onSuccess(Object arg0)
			{
				JSONObject res;
				try
				{
					Storage storage = (new StorageResponseBuilder()).buildResponse(((JSONObject) arg0).getString("data"));
					res = new JSONObject(storage.getJsonDocList().get(0).jsonDoc);
					APP.GLOBAL().getEditor().putString("partnerdata", res.toString()).commit();
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
					if (activity instanceof PartnerHomeActivity&&activity != null)
					{
						Message message = new Message();
						message.what = PartnerHomeActivity.REFRESH_PARTNER;
						try
						{
							((PartnerHomeActivity) activity).msg.send(message);
						}
						catch (RemoteException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
				if(nitify!=null)
				nitify.onSuccess(arg0);
			}
			
			@Override
			public void onException(Exception arg0)
			{
				if(nitify!=null)
				nitify.onException(arg0);
				
			}
		});
	}
	
	public static void resetStatus(Object arg0)
	{
		try
		{
			boolean t1, t2, t3, t4;
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
		}
		catch (App42Exception ex)
		{
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
