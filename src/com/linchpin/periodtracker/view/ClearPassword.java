package com.linchpin.periodtracker.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;

public class ClearPassword extends Activity {

	EditText clearcurrentpasscode;
Theme t;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clear_password);
		initLayout();
		t=Theme.getCurrentTheme(this,findViewById(android.R.id.content));
		if(t!=null)
		{
			
			t.applyBackgroundColor(R.id.ll, "view_bg");
			t.applyBackgroundDrawable(R.id.clearcurrentpasscode, "mpt_edit_text_sltr");
		
			
		
			t.applyTextColor(R.id.clearcurrentpasscode, "text_color");
			t.applyTextColor(R.id.t1, "text_color");
			t.applyTextColor(R.id.clear, "co_btn_fg");
			
			t.applyBackgroundDrawable(R.id.clear, "mpt_button_sltr");
		}
	
		
		findViewById(R.id.clearpasscodeinfobutton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				Intent intent = new Intent(ClearPassword.this, HomeScreenHelp.class);

				intent.putExtra("classname", "clearpasscode");
				startActivity(intent);

			}
		});

		findViewById(R.id.clearpasscodesettingsback).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
			}
		});
		findViewById(R.id.clear).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (clearcurrentpasscode.getText().toString()
						.equals(PeriodTrackerObjectLocator.getInstance().getPasswordProtection())) {

					clearPasscode();
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.wrongpasscode), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
				}

				/*
				 * // TODO Auto-generated method stub AlertDialog.Builder
				 * builder = new AlertDialog.Builder( ClearPassword.this);
				 * builder.setTitle(getString(R.string.clearpasscode));
				 * builder.setMessage(getResources().getString(
				 * R.string.alertonclearpassoce));
				 * builder.setPositiveButton(getResources()
				 * .getString(R.string.yes), new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) { // TODO Auto-generated method stub if
				 * (clearcurrentpasscode .getText() .toString()
				 * .equals(PeriodTrackerObjectLocator .getInstance()
				 * .getPasswordProtection())) { clearPasscode(); } else { Toast
				 * toast= Toast.makeText( getApplicationContext(),
				 * getResources().getString( R.string.mismatchpasscode),
				 * Toast.LENGTH_SHORT);
				 * toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0,
				 * 0); toast.show(); } } }); builder.setNegativeButton(
				 * getResources().getString(R.string.no), null); builder.show();
				 */}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		

		EasyTracker.getInstance(this).activityStart(this);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if ( this.getSharedPreferences("com.linchpin.periodtracker", 0).getBoolean("purchased", false)) {
			
		} else {
			PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		}

	}
	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

	public void initLayout() {

		clearcurrentpasscode = (EditText) findViewById(R.id.clearcurrentpasscode);

	}

	public void clearPasscode() {

		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(
				getApplicationContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(
				PeriodTrackerConstants.APPLICATION_SETTINGS_PASSWORD_STRING_KEY, "", PeriodTrackerObjectLocator
						.getInstance().getProfileId());
		ApplicationSettingModel applicationSettingModel2 = new ApplicationSettingModel(
				PeriodTrackerConstants.APLLICATION_SETTING_EMAIL_KEY, "", PeriodTrackerObjectLocator.getInstance()
						.getProfileId());
		ApplicationSettingModel applicationSettingModel3 = new ApplicationSettingModel(
				PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION, "", PeriodTrackerObjectLocator
						.getInstance().getProfileId());
		ApplicationSettingModel applicationSettingModel4 = new ApplicationSettingModel(
				PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_ANSWER, "", PeriodTrackerObjectLocator
						.getInstance().getProfileId());
		if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel)) {
			applicationSettingDBHandler.deleteApplicationSetting(applicationSettingModel2);
			applicationSettingDBHandler.deleteApplicationSetting(applicationSettingModel3);
			applicationSettingDBHandler.deleteApplicationSetting(applicationSettingModel4);
			Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.passcodecleared),
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		}
		finish();
	}

}
