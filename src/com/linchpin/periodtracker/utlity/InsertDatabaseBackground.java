package com.linchpin.periodtracker.utlity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodttracker.database.ImportDatabase;

public class InsertDatabaseBackground extends AsyncTask<Void, String, String> {

	Context context;

	public InsertDatabaseBackground(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		ImportDatabase.insertAndParsingArray();
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, true).commit();
		PeriodTrackerObjectLocator.getInstance().setPeriodTrackerObjectLocatorNull();
		Toast.makeText(context, context.getString(R.string.importdatasucessfully), Toast.LENGTH_LONG).show();
		Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
		((Activity) context).finish();
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);

	}

}
