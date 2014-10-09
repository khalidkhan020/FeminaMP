package com.linchpin.periodtracker.view;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.widget.BackBar;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;

public class SetPassword extends Activity {

	EditText passcode, confirmPasscode, /*email,*/ securityAnswer;
	Button save, securityQuestion;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	Theme t;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_password_view);
	t=Theme.getCurrentTheme(this,findViewById(android.R.id.content));
		if(t!=null)
		{
			
			t.applyBackgroundColor(R.id.sv, "view_bg");
			t.applyBackgroundDrawable(R.id.passcode, "mpt_edit_text_sltr");
			t.applyBackgroundDrawable(R.id.confirmpasscode, "mpt_edit_text_sltr");
			t.applyBackgroundDrawable(R.id.securityanswer, "mpt_edit_text_sltr");
			t.applyTextColor(R.id.securityquestion, "text_color");
			t.applyTextColor(R.id.text1, "text_color");
			t.applyTextColor(R.id.savepassword, "co_btn_fg");
			t.applyTextColor(R.id.passcode, "text_color");
			t.applyTextColor(R.id.confirmpasscode, "text_color");
			t.applyTextColor(R.id.securityanswer, "text_color");
			
			t.applyBackgroundDrawable(R.id.savepassword, "mpt_button_sltr");    
			t.applyBackgroundDrawable(R.id.securityquestion, "drop_list");  
		}
		findViewById(R.id.setpasswordinfobutton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
			
				Intent intent = new Intent(SetPassword.this,
						HomeScreenHelp.class);
				intent.putExtra("classname", "setpasscode");
				startActivity(intent);
				
			}
		});
		initLayout();

		securityQuestion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickSelectSecurityQuestiosn();
			}
		});

		((BackBar)findViewById(R.id.setpasscodesheaderlayout)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
				
			}
		});
		findViewById(R.id.savepassword).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (passcode.getEditableText().toString().trim().equals("")) {
				Toast toast=	Toast.makeText(getApplicationContext(), getResources().getString(R.string.pleaseentervalidpassword), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
				} else if (passcode.getEditableText().toString().trim().length() > 10) {
					Toast toast =Toast.makeText(getApplicationContext(), getResources().getString(R.string.passcodelessthan10), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
				} else if (!passcode.getEditableText().toString().equals(confirmPasscode.getText().toString())) {
					Toast toast=Toast.makeText(getApplicationContext(), getResources().getString(R.string.mismatchpasscode), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
				}/* else if (!new EmailValidator().validate(email.getText().toString())) {
					Toast toast =Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalidemail), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
				}*/ else if (securityQuestion.getText().toString().equals(getResources().getString(R.string.securityquestion))
						|| securityQuestion.getText().toString().trim().equals("")) {
					Toast toast=Toast.makeText(getApplicationContext(), getResources().getString(R.string.selectsecurityquestion), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
				} else if (null == securityAnswer || securityAnswer.getText().toString().trim().equals("")) {
					Toast toast=Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalidanswer), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
				} else {
					setPassword();
					finish();
				}
			}
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
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
		securityQuestion = (Button) findViewById(R.id.securityquestion);
		securityAnswer = (EditText) findViewById(R.id.securityanswer);
		passcode = (EditText) findViewById(R.id.passcode);
		confirmPasscode = (EditText) findViewById(R.id.confirmpasscode);
	//	email = (EditText) findViewById(R.id.email);
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getApplicationContext());
		PeriodTrackerModelInterface modelInterface = applicationSettingDBHandler.getParticularApplicationSetting(
				PeriodTrackerConstants.APLLICATION_SETTING_EMAIL_KEY, PeriodTrackerObjectLocator.getInstance().getProfileId());
		/*if (modelInterface == null || null == ((ApplicationSettingModel) modelInterface).getValue()) {
			email.setText(getEmailid());
		} else {
			ApplicationSettingModel applicationSettingModel = (ApplicationSettingModel) modelInterface;
			email.setText(applicationSettingModel.getValue());
		}*/
		modelInterface = applicationSettingDBHandler.getParticularApplicationSetting(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION,
				PeriodTrackerObjectLocator.getInstance().getProfileId());
		if (modelInterface == null || null == ((ApplicationSettingModel) modelInterface).getValue()
				|| ((ApplicationSettingModel) modelInterface).getValue().equals("")) {
			securityQuestion.setText(getResources().getString(R.string.securityquestion));
		} else {
			ApplicationSettingModel applicationSettingModel = (ApplicationSettingModel) modelInterface;
			securityQuestion.setText(applicationSettingModel.getValue());
		}
		if (PeriodTrackerObjectLocator.getInstance().getPasswordProtection() != null
				|| !PeriodTrackerObjectLocator.getInstance().getPasswordProtection().equals(" ")) {
			passcode.setText(PeriodTrackerObjectLocator.getInstance().getPasswordProtection());
			confirmPasscode.setText(PeriodTrackerObjectLocator.getInstance().getPasswordProtection());
		}
	}

	public void onClickSelectSecurityQuestiosn() {

		int selectedPosition = 1;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items = { getResources().getString(R.string.securityquestion1), getResources().getString(R.string.securityquestion2),
				getResources().getString(R.string.securityquestion3), getResources().getString(R.string.securityquestion4),
				getResources().getString(R.string.securityquestion5), getResources().getString(R.string.securityquestion6),
				getResources().getString(R.string.securityquestion7), getResources().getString(R.string.securityquestion8),
				getResources().getString(R.string.securityquestion9), getResources().getString(R.string.securityquestion10), };

		if (securityQuestion.getText().equals(getResources().getString(R.string.select_security_question))) {
			selectedPosition = -1;
		} else {
			selectedPosition = Arrays.asList(items).indexOf(String.valueOf(securityQuestion.getText()));
		}

		builder.setTitle(getResources().getString(R.string.securityquestiontextonly));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				securityQuestion.setText(items[which]);
				dialog.dismiss();

			}
		});
		builder.show();

	}

	public void setPassword() {
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getApplicationContext());

	//	ApplicationSettingModel emailsettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APLLICATION_SETTING_EMAIL_KEY, email.getText()
	//			.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId());
//		if (((ApplicationSettingModel) applicationSettingDBHandler.getParticularApplicationSetting(PeriodTrackerConstants.APLLICATION_SETTING_EMAIL_KEY,
//				PeriodTrackerObjectLocator.getInstance().getProfileId())).getValue() == null) {
//			applicationSettingDBHandler.inseretApplicationSetting(emailsettingModel);
//		} else {
//			applicationSettingDBHandler.upadteApplicationSetting(emailsettingModel);
//		}

		ApplicationSettingModel securityQuestionsetting = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION,
				securityQuestion.getText().toString(), PeriodTrackerObjectLocator.getInstance().getProfileId());

		if (((ApplicationSettingModel) applicationSettingDBHandler.getParticularApplicationSetting(
				PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION, PeriodTrackerObjectLocator.getInstance().getProfileId())).getValue() == null) {
			applicationSettingDBHandler.inseretApplicationSetting(securityQuestionsetting);
		} else {
			applicationSettingDBHandler.upadteApplicationSetting(securityQuestionsetting);
		}

		
		ApplicationSettingModel securityAnswersetting = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_ANSWER,
				securityAnswer.getText().toString().trim(), PeriodTrackerObjectLocator.getInstance().getProfileId());

		if (((ApplicationSettingModel) applicationSettingDBHandler.getParticularApplicationSetting(
				PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_ANSWER, PeriodTrackerObjectLocator.getInstance().getProfileId())).getValue() == null) {
			applicationSettingDBHandler.inseretApplicationSetting(securityAnswersetting);
		} else {
			applicationSettingDBHandler.upadteApplicationSetting(securityAnswersetting);
		}

		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PASSWORD_STRING_KEY,
				passcode.getText().toString().trim(), PeriodTrackerObjectLocator.getInstance().getProfileId());

		if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel)) {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.passcodesetsucessfully), Toast.LENGTH_SHORT).show();
			PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
//			if (Utility.isNetworkConnected(getApplicationContext())) {
//				Utility.sendMail(SetPassword.this, emailsettingModel.getValue(), "set_password", PeriodTrackerObjectLocator.getInstance().getPasswordProtection(), "", "");
//				
//			}else {
//				Toast.makeText(getApplicationContext(), getResources().getString(R.string.internetconnectioncheck), Toast.LENGTH_SHORT).show();
//			}
		}

	}

	public String getEmailid() {
		String possibleEmail = null;
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				possibleEmail = account.name;
				break;
			}
		}
		return possibleEmail;
	}
}

class EmailValidator {

	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *          hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validate(final String hex) {

		matcher = pattern.matcher(hex);
		return matcher.matches();

	}
}