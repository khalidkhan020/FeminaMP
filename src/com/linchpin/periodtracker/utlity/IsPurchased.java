package com.linchpin.periodtracker.utlity;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;

public class IsPurchased extends AsyncTask<Void, Void, String> {
	IInAppBillingService mService;
	Bundle skuDetails;
	boolean purchased = false;
	Context context;

	public IsPurchased(Context context, IInAppBillingService mService) {
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

		// TODO Auto-generated method stub

		try {

			Bundle ownedItems = mService.getPurchases(3, context.getPackageName(), "inapp", null);

			int response = ownedItems.getInt("RESPONSE_CODE");
			if (response == 0) {
				ArrayList ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
				ArrayList purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
				ArrayList signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE");
				String continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");

				for (int i = 0; i < purchaseDataList.size(); ++i) {
					String purchaseData = (String) purchaseDataList.get(i);
					Log.d("", "" + purchaseData);
					// String signature = (String) signatureList.get(i);
					// Log.d("",""+signature);
					String sku = (String) ownedSkus.get(i);

					if (sku.equals(ConstantsKey.IN_APP_ID)) {

						purchased = true;
						context.getSharedPreferences("com.linchpin.periodtracker", 0).edit().putBoolean("purchased", true).commit();
						context.getSharedPreferences("com.linchpin.periodtracker", 0).edit().putString("purchased_item_json", purchaseData).commit();
					}
					Log.d("", "" + sku);
					// do something with this purchase information
					// e.g. display the updated list of products owned by user
				}

				// if continuationToken != null, call getPurchases again
				// and pass in the token to retrieve more items
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		/*if (purchased) {
			Toast.makeText(context, context.getResources().getString(R.string.adfree_restore), Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context,context.getResources().getString(R.string.havnt_purchased), Toast.LENGTH_LONG).show();
		}*/
	}

}