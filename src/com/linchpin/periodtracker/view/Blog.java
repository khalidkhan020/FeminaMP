package com.linchpin.periodtracker.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.utlity.Utility;

public class Blog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_app);
		Intent intent = getIntent();
		String string = intent.getStringExtra("classname");
		ProgressBar bar = new ProgressBar(this);
		final ProgressDialog progressDialog = new ProgressDialog(this);
		try {

			progressDialog.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
			progressDialog.setMessage(getResources().getString(R.string.loading));
			progressDialog.show();

		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
		}

		WebView view = (WebView) findViewById(R.id.webview);
		if (Utility.isNetworkConnected(getBaseContext())) {

			
			view.getSettings().setJavaScriptEnabled(true);

			view.setWebViewClient(new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return false;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * android.webkit.WebViewClient#onPageFinished(android.webkit
				 * .WebView, java.lang.String)
				 */
				@Override
				public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
					super.onPageFinished(view, url);
					progressDialog.dismiss();

				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * android.webkit.WebViewClient#onPageStarted(android.webkit
				 * .WebView, java.lang.String, android.graphics.Bitmap)
				 */
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					// TODO Auto-generated method stub
					super.onPageStarted(view, url, favicon);
					progressDialog.show();
				}
			});
		
			if (string.equals("blog")) {
				view.loadUrl("http://mptapp.com/blog/");
			} else {
				view.loadUrl("http://mptapp.com/user-guide/");
			}
		}

		else {
			Toast.makeText(this, getResources().getString(R.string.nointernetconnected), Toast.LENGTH_SHORT).show();
		}

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
		
	}



	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

}