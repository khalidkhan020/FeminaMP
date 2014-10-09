package com.linchpin.periodtracker.utlity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.linchpin.periodtracker.view.CaldroidSampleView;

public class CalendarAsyncTask extends AsyncTask<Void, Void, String> {

	Context context;
	TransparentProgressDailog pd;

	public CalendarAsyncTask(Context context) {
		// TODO Auto-generated constructor stub

		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

		pd = new TransparentProgressDailog(context, 0);
		
		
		pd.setCancelable(false);
		pd.show();

	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Intent intent;
				// TODO Auto-generated method stub
				intent = new Intent((Activity) context, CaldroidSampleView.class);
				((Activity) context).startActivity(intent);

			}
		});

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (pd != null && pd.isShowing()) {
			pd.cancel();
		}

	}

}
