package com.linchpin.periodtracker.utlity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.linchpin.myperiodtracker.R;

public class GetPurchaseItems extends AsyncTask<Void, Void, String> {
	
	Bundle skuDetails;
	int resonse;
	private String sku;
	IInAppBillingService mService;
	boolean error = false;
	Bundle querySkus;
	ArrayList<String> skuList;
	Context context;
	
	public GetPurchaseItems(Context context, IInAppBillingService mService) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mService = mService;
	}
	
	@Override
	protected void onPreExecute() {
		
		try {
			
		} catch (Exception e) {
			
		}
	}
	
	@Override
	protected String doInBackground(Void... params) {
		if (null != mService) {
			
			// TODO Auto-generated method stub
			skuList = new ArrayList<String>();
			skuList.add(ConstantsKey.IN_APP_ID);
			
			querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			
			try {
				
				skuDetails = mService.getSkuDetails(3, context.getPackageName(), "inapp", querySkus);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int response = skuDetails.getInt("RESPONSE_CODE");
			
			if (response == 0) {
				ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
				
				for (String thisResponse : responseList) {
					JSONObject object = null;
					try {
						object = new JSONObject(thisResponse);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						sku = object.getString("productId");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String price = null, title;
					try {
						price = object.getString("price");
						title = object.getString("description");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (ConstantsKey.IN_APP_ID.equals(sku)) {
						String mPremiumUpgradePrice = price;
					} else {
						// Show that toast and Exit to Home screen;
						error = true;
						
					}
					
				}
			}
			try {
				// String devloperPayload =
				// "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi3rgM0nTMnN8n+u7xCMAFtLxR09G79nM4tteU12tcYGxHlKUs/uZbjQgP0RNw6xTOamV6OcsuBLFBAKzlHYqJhSiTVMWnxJ97QxoV1sXKuuoJqGxJyScG67uGwTRG0ynsi1ayM/SzUB1pKJ2Lm7yz/g+0G9DIX6q+CGtEXHN8VgS6oL2fmsA0eZ6vHUUM1lgVlD+jPDXA/S9dQrvfErlTzf4cXMCS8auInzs8p+Vv+Rte+r4BDKr0CLiA27BEkYZ9gSG9NKu1ng1o6ekhf6H0ARUAG8KZZZrlk0L860KRLP/tmWS2bJ3z01gV44McML3vmdzjOpLuVUKUwI65zzhtwIDAQAB";
				if (null != mService) {
					Bundle buyIntentBundle = mService.getBuyIntent(3, context.getPackageName(), sku, "inapp", null);
					int responseForBuySku = buyIntentBundle.getInt("RESPONSE_CODE");
					
					resonse = responseForBuySku;
					if (responseForBuySku == 0) {
						
						PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
						try {
							((Activity) context).startIntentSenderForResult(pendingIntent.getIntentSender(), 1001,
									new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
						} catch (SendIntentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (responseForBuySku == 7) {
						resonse = 7;
					}
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			
			((Activity) context).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					Toast.makeText(context, context.getString(R.string.purcheasenullmessage), Toast.LENGTH_LONG).show();
				}
			});
			
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		// Toast.makeText(AppSettings.this,"resonse"+resonse,Toast.LENGTH_LONG).show();
		if (resonse == 7) {
			Toast.makeText(context, context.getResources().getString(R.string.alreadypurchased), Toast.LENGTH_LONG)
			.show();
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.PURCHASED.key, true).commit();
			context.getSharedPreferences("com.linchpin.periodtracker", 0).edit().putBoolean("purchased", true)
			.commit();
			Intent i = APP.GLOBAL().getPackageManager().getLaunchIntentForPackage(APP.GLOBAL().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			APP.GLOBAL().startActivity(i);
			
		}
	}
}
