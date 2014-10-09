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
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;

public class ChangePasswordView extends Activity {
	Theme t;
	EditText currentPasscode, confirmCurrentPasscode, newPasscode;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
		t=Theme.getCurrentTheme(this,findViewById(android.R.id.content));
		if(t!=null)
		{
			
			t.applyBackgroundColor(R.id.ll, "view_bg");
			t.applyBackgroundDrawable(R.id.currentpasscode, "mpt_edit_text_sltr");
			t.applyBackgroundDrawable(R.id.newpasscode, "mpt_edit_text_sltr");
			t.applyBackgroundDrawable(R.id.changeconfirmpasscode, "mpt_edit_text_sltr");
			
			t.applyTextColor(R.id.savenewpassword, "co_btn_fg");
			t.applyTextColor(R.id.currentpasscode, "text_color");
			t.applyTextColor(R.id.newpasscode, "text_color");
			t.applyTextColor(R.id.changeconfirmpasscode, "text_color");
			
			t.applyBackgroundDrawable(R.id.savenewpassword, "mpt_button_sltr");
		}
		initLayout();

		findViewById(R.id.changepasscodeinfobutton).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ChangePasswordView.this,
								HomeScreenHelp.class);

						intent.putExtra("classname", "changepasscode");
						startActivity(intent);
					}
				});

		findViewById(R.id.changepasscodesettingsback).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
						overridePendingTransition(R.anim.right_in, R.anim.right_out);
					}
				});
		findViewById(R.id.savenewpassword).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!currentPasscode
								.getText()
								.toString()
								.equals(PeriodTrackerObjectLocator
										.getInstance().getPasswordProtection())) {
						Toast toast=	Toast.makeText(
									getBaseContext(),
									getResources().getString(
											R.string.wrongpasscode),
									Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
						toast.show();
							currentPasscode.setText("");
						} else if (newPasscode.getEditableText().toString()
								.equals("")) {
						Toast toast =	Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.entervaildpasscode),
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
							toast.show();
							newPasscode.setFocusable(true);
						} else if (newPasscode
								.getEditableText()
								.toString()
								.equals(PeriodTrackerObjectLocator
										.getInstance().getPasswordProtection())) {
						Toast toast=	Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.invaildpasscodesame),
									Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
						toast.show();
						} else if (!confirmCurrentPasscode.getEditableText()
								.toString()
								.equals(newPasscode.getText().toString())) {
						Toast toast=	Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.mismatchpasscode),
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
							toast.show();
						} else {
							changePassword();
						}

					}
				});
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initLayout();

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

		currentPasscode = (EditText) findViewById(R.id.currentpasscode);
		confirmCurrentPasscode = (EditText) findViewById(R.id.changeconfirmpasscode);
		newPasscode = (EditText) findViewById(R.id.newpasscode);

	}

	public void changePassword() {

		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(
				getApplicationContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(
				PeriodTrackerConstants.APPLICATION_SETTINGS_PASSWORD_STRING_KEY,
				newPasscode.getText().toString(), PeriodTrackerObjectLocator
						.getInstance().getProfileId());
		if (applicationSettingDBHandler
				.upadteApplicationSetting(applicationSettingModel)) {
		Toast toast=	Toast.makeText(
					getApplicationContext(),
					getResources()
							.getString(R.string.changedpasscodesucessfuly),
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			PeriodTrackerObjectLocator.getInstance()
					.intitializeApplicationParameters();
			ApplicationSettingModel model = (ApplicationSettingModel) applicationSettingDBHandler
					.getParticularApplicationSetting(
							PeriodTrackerConstants.APLLICATION_SETTING_EMAIL_KEY,
							PeriodTrackerObjectLocator.getInstance()
									.getProfileId());
			if (Utility.isNetworkConnected(getApplicationContext())) {
				Utility.sendMail(ChangePasswordView.this, model.getValue(), "change_password", PeriodTrackerObjectLocator.getInstance().getPasswordProtection(), "", "");
							} else {
			Toast toast1=	Toast.makeText(
						getApplicationContext(),
						getResources().getString(
								R.string.internetconnectioncheck),
						Toast.LENGTH_SHORT);
			toast1.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast1.show();
			}
		}

		finish();

	}

}
