package com.linchpin.periodtracker.partnersharing;

import org.json.JSONException;
import org.json.JSONObject;
import org.taptwo.android.widget.TitleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.DiffAdapter;
import com.linchpin.periodtracker.utlity.APP;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.Storage.JSONDocument;
import com.shephertz.app42.paas.sdk.android.storage.StorageResponseBuilder;

public class PartnerSharing extends FragmentActivity
{
	private ViewFlow	viewFlow;
	Handler				handler	= new Handler();
	int					status	= 0;			
	String				code;
	DiffAdapter			adapter;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
		JSONObject jsonBody = new JSONObject();
		try
		{
			jsonBody.put("userid", APP.currentUser.getUserName());
			jsonBody.put("sessionid", APP.currentSession);
		}
		catch (JSONException e)
		{
			
		}
		
		((APP) getApplication()).getCustomCodeService().runJavaCode("CheckStatus", jsonBody, new App42CallBack()
		{
			
			@Override
			public void onSuccess(Object arg0)
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
					if (!t1 && t2 && !t3 && !t4) status = 1;
					else if (t1 && !t2 && !t3 && !t4) status = 2;
					else if (!t1 && t2 && t3 && !t4) status = 3;
					else if (t1 && !t2 && t3 && !t4) status = 4;
					else if (!t1 && t2 && t4) status = 5;
					else if (t1 && !t2 && t4) status = 6;
					if (t1) APP.setParnerId(jsonObject.getString("receverid"));
					else APP.setParnerId(jsonObject.getString("senderid"));
					handler.post(new Runnable()
					{
						
						@Override
						public void run()
						{
							
							PartnerSharing.this.finish();
							Intent intent = new Intent(PartnerSharing.this, PartnerSharing.class);
							intent.putExtra("type", status);
							startActivity(intent);
							PartnerSharing.this.finish();
							Toast.makeText(PartnerSharing.this, "Success", Toast.LENGTH_LONG).show();
							
						}
					});
					
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
				
				if (ex instanceof App42Exception)
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
				}
				
			}
		});
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		/*LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		SharingCalender sharingcalender = new SharingCalender(this, new View(this), mInflater, 0);
		setContentView(sharingcalender.getView());
		;*/
		setContentView(R.layout.partnersharing);
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		
		 adapter = new DiffAdapter(this);
		
		adapter.setType(getIntent().getIntExtra("type", 0));
		viewFlow.setAdapter(adapter, 3);
		
		TitleFlowIndicator indicator = (TitleFlowIndicator) findViewById(R.id.viewflowindic);
		indicator.setTitleProvider(adapter);
		viewFlow.setFlowIndicator(indicator);
		
	}
	
	@Override
	protected void onResume()
	{
		if(adapter!=null)
					adapter.onResume();
		super.onResume();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
				viewFlow.onConfigurationChanged(newConfig);
	}
}
