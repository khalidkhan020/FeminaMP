package com.linchpin.periodtracker.view;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.NotificattionModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.widget.BackBar;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;
import com.linchpin.periodttracker.database.NotificationDBHandler;
import android.R.anim;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeSetting2 extends Activity implements OnClickListener
{
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		Intent setbackActivity = new Intent(WelcomeSetting2.this, PersonalSettingView.class);
		startActivity(setbackActivity);
		finish();
		APP.GLOBAL().exicuteLIOAnim(this);
		super.onBackPressed();
	}
	private void applyTheme()
	{
		
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			t.getDrawableResource("drop_down");
			t.getDrawableResource("up_arrow");
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
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundColor(R.id.h1, "heading_bg");
			t.applyTextColor(R.id.clearField, "co_btn_fg");
			t.applyBackgroundDrawable(R.id.clearField, "mpt_button_sltr");
			t.applyTextColor(R.id.continueWel, "co_btn_fg");
			t.applyBackgroundDrawable(R.id.continueWel, "mpt_button_sltr");
			t.applyTextColor(R.id.h1, "heading_fg");
			t.applyImageDrawable(R.id.arrow1, "next_info");
			t.applyImageDrawable(R.id.arrow2, "next_info");
			t.applyTextColor(R.id.txtasetting, "text_color");
			t.applyTextColor(R.id.txtnretting, "text_color");
			t.applyTextColor(R.id.txtnsetting, "text_color");
			t.applyTextColor(R.id.txtprsetting, "text_color");
			t.applyTextColor(R.id.txtpsetting, "text_color");
			t.applyBackgroundColor(R.id.llhasetting, "view_bg");
			t.applyBackgroundColor(R.id.llhnretting, "view_bg");
			t.applyBackgroundColor(R.id.llhnsetting, "view_bg");
			t.applyBackgroundColor(R.id.llhprsetting, "view_bg");
			t.applyBackgroundColor(R.id.llhpsetting, "view_bg");
			t.applyTextColor(R.id.showFertileAlert, "text_color");
			t.applyTextColor(R.id.showHeight, "text_color");
			t.applyTextColor(R.id.showHeightUnit, "text_color");
			t.applyTextColor(R.id.showOvalutionAlert, "text_color");
			t.applyTextColor(R.id.showPeriodAlert, "text_color");
			t.applyTextColor(R.id.showDateFormate, "text_color");
			t.applyTextColor(R.id.ShowStartDate, "text_color");
			t.applyTextColor(R.id.showLanguageSet, "text_color");
			t.applyTextColor(R.id.showTemp, "text_color");
			t.applyTextColor(R.id.showTempUnit, "text_color");
			t.applyTextColor(R.id.showWidth, "text_color");
			t.applyTextColor(R.id.showWidthUnit, "text_color");
			t.applyTextColor(R.id.txtDisAppNameAndIcon, "text_color");
			t.applyTextColor(R.id.txtDisDateFormate, "text_color");
			t.applyTextColor(R.id.txtDisFertilityAlert, "text_color");
			t.applyTextColor(R.id.txtDisFirstDayOfWeek, "text_color");
			t.applyTextColor(R.id.txtDisHeight, "text_color");
			t.applyTextColor(R.id.txtDisHeightUnit, "text_color");
			t.applyTextColor(R.id.txtDisLanguage, "text_color");
			t.applyTextColor(R.id.txtDisOvalutionAlert, "text_color");
			t.applyTextColor(R.id.txtDisPeriodAlert, "text_color");
			t.applyTextColor(R.id.txtDisTemp, "text_color");
			t.applyTextColor(R.id.txtDisTempUnit, "text_color");
			t.applyTextColor(R.id.txtDisWidthUnit, "text_color");
			t.applyTextColor(R.id.txtDisWidth, "text_color");
			t.applyTextColor(R.id.txtDisTheme, "text_color");
		}
	}
	
	private TextView	ttprsetting, ttnsetting, ttpesetting, ttrsetting, ttasetting;
	private LinearLayout	llhprsetting, llhnsetting, llhpesetting, llhrsetting, llhasetting;
	private RelativeLayout	rowHeightUnit, rowWidthUnit, rowTempUnit, rowLanguage, rowTheme;
	private RelativeLayout	rowHeight, rowWidth, rowTemp, rowsetDateFormate, rowSetFirstdateOfWeek;
	private RelativeLayout	rowPeriodAlert, rowFertileAlert, rowOvalutionAlert, rowAppNameAndIcon;
	private TextView		showHeightUnit, showWidthUnit, showTempUnit, showLanguage, showTheme;
	private TextView		showHeight, showWidth, showTemp, showsetDateFormate, showSetFirstdateOfWeek;
	private TextView		showPeriodAlert, showFertileAlert, showOvalutionAlert;
	
	EditText				passcode, confirmPasscode, /*email,*/securityAnswer;
	Button					save, securityQuestion, contButton;
	BackBar					back;
	Theme					t;
	private String[]		values;
	private String[]		entries;
	private int				childposition;
	
	private void findAllViews()
	{
		ttprsetting = (TextView) findViewById(R.id.txtprsetting);
		ttpesetting = (TextView) findViewById(R.id.txtpsetting);
		ttasetting = (TextView) findViewById(R.id.txtasetting);
		ttnsetting = (TextView) findViewById(R.id.txtnsetting);
		ttrsetting = (TextView) findViewById(R.id.txtnretting);
		
		llhprsetting = (LinearLayout) findViewById(R.id.llhprsetting);
		llhpesetting = (LinearLayout) findViewById(R.id.llhpsetting);
		llhasetting = (LinearLayout) findViewById(R.id.llhasetting);
		llhnsetting = (LinearLayout) findViewById(R.id.llhnsetting);
		llhrsetting = (LinearLayout) findViewById(R.id.llhnretting);
		
		rowHeight = (RelativeLayout) findViewById(R.id.rowHeight);
		rowWidth = (RelativeLayout) findViewById(R.id.rowWeight);
		rowTemp = (RelativeLayout) findViewById(R.id.rowTemp);
		rowHeightUnit = (RelativeLayout) findViewById(R.id.rowHeightUnit);
		rowWidthUnit = (RelativeLayout) findViewById(R.id.rowWidthUnit);
		rowTempUnit = (RelativeLayout) findViewById(R.id.rowTempUnit);
		rowLanguage = (RelativeLayout) findViewById(R.id.rowSetLanguage);
		rowTheme = (RelativeLayout) findViewById(R.id.rowSetTheme);
		rowsetDateFormate = (RelativeLayout) findViewById(R.id.rowSetDateFormate);
		rowSetFirstdateOfWeek = (RelativeLayout) findViewById(R.id.rowSetFirstDateOfWeek);
		rowPeriodAlert = (RelativeLayout) findViewById(R.id.rowPeriodAlert);
		rowFertileAlert = (RelativeLayout) findViewById(R.id.rowFertilityAlert);
		rowOvalutionAlert = (RelativeLayout) findViewById(R.id.rowOvalutionAlert);
		rowAppNameAndIcon = (RelativeLayout) findViewById(R.id.rowAppIconAndName);
		contButton = (Button) findViewById(R.id.continueWel);
		showHeight = (TextView) findViewById(R.id.showHeight);
		showWidth = (TextView) findViewById(R.id.showWidth);
		showTemp = (TextView) findViewById(R.id.showTemp);
		showHeightUnit = (TextView) findViewById(R.id.showHeightUnit);
		showWidthUnit = (TextView) findViewById(R.id.showWidthUnit);
		showTempUnit = (TextView) findViewById(R.id.showTempUnit);
		showLanguage = (TextView) findViewById(R.id.showLanguageSet);
		showsetDateFormate = (TextView) findViewById(R.id.showDateFormate);
		showSetFirstdateOfWeek = (TextView) findViewById(R.id.ShowStartDate);
		showPeriodAlert = (TextView) findViewById(R.id.showPeriodAlert);
		showOvalutionAlert = (TextView) findViewById(R.id.showOvalutionAlert);
		back = (BackBar) findViewById(R.id.welcomesettingback);
		
		contButton.setOnClickListener(this);
		back.setButtonClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				onBackPressed();
				// TODO Auto-generated method stub
				
			}
		});
		showFertileAlert = (TextView) findViewById(R.id.showFertileAlert);
	}
	
	private void setClickListners()
	{
		rowHeight.setOnClickListener(this);
		rowHeightUnit.setOnClickListener(this);
		rowLanguage.setOnClickListener(this);
		rowOvalutionAlert.setOnClickListener(this);
		rowPeriodAlert.setOnClickListener(this);
		rowFertileAlert.setOnClickListener(this);
		rowsetDateFormate.setOnClickListener(this);
		rowSetFirstdateOfWeek.setOnClickListener(this);
		rowTemp.setOnClickListener(this);
		rowTempUnit.setOnClickListener(this);
		rowTheme.setOnClickListener(this);
		rowWidth.setOnClickListener(this);
		rowWidthUnit.setOnClickListener(this);
		rowAppNameAndIcon.setOnClickListener(this);
		llhprsetting.setOnClickListener(this);
		llhpesetting.setOnClickListener(this);
		llhasetting.setOnClickListener(this);
		llhnsetting.setOnClickListener(this);
		llhrsetting.setOnClickListener(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_setting2);
		findAllViews();
		applyTheme();
		toggleVisibility(R.id.llprsetting, R.id.txtprsetting, !(findViewById(R.id.llprsetting).getVisibility() == View.VISIBLE));
		setClickListners();
		refreashViewValuew();
		
		/* set password code */
		t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			
			t.applyBackgroundColor(R.id.sv, "view_bg");
			t.applyBackgroundDrawable(R.id.passcode, "mpt_edit_text_sltr");
			t.applyBackgroundDrawable(R.id.confirmpasscode, "mpt_edit_text_sltr");
			t.applyBackgroundDrawable(R.id.securityanswer, "mpt_edit_text_sltr");
			t.applyTextColor(R.id.securityquestion, "text_color");
			t.applyTextColor(R.id.text1, "text_color");
			t.applyTextColor(R.id.passcode, "text_color");
			t.applyTextColor(R.id.confirmpasscode, "text_color");
			t.applyTextColor(R.id.securityanswer, "text_color");
			
			
			t.applyBackgroundDrawable(R.id.securityquestion, "drop_list");
		}
		
		initLayout();
		findViewById(R.id.clearField).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				passcode.setText("");
				confirmPasscode.setText("");
				securityAnswer.setText("");
				securityQuestion.setText(getResources().getString(R.string.select_security_question));
				}
		});
		securityQuestion.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				onClickSelectSecurityQuestiosn();
			}
			
			private void onClickSelectSecurityQuestiosn()
			{
				int selectedPosition = 1;
				AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeSetting2.this);
				final String[] items =
				{
						getResources().getString(R.string.securityquestion1), getResources().getString(R.string.securityquestion2), getResources().getString(R.string.securityquestion3), getResources().getString(R.string.securityquestion4), getResources().getString(R.string.securityquestion5),
						getResources().getString(R.string.securityquestion6), getResources().getString(R.string.securityquestion7), getResources().getString(R.string.securityquestion8), getResources().getString(R.string.securityquestion9), getResources().getString(R.string.securityquestion10),
				};
				
				if (securityQuestion.getText().equals(getResources().getString(R.string.select_security_question)))
				{
					selectedPosition = -1;
				}
				else
				{
					selectedPosition = Arrays.asList(items).indexOf(String.valueOf(securityQuestion.getText()));
				}
				
				builder.setTitle(getResources().getString(R.string.securityquestiontextonly));
				builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						securityQuestion.setText(items[which]);
						dialog.dismiss();
						
					}
				});
				builder.show();
				
				// TODO Auto-generated method stub
				
			}
		});
	}
	private boolean savePasswordMethod()
	{
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		if (passcode.getEditableText().toString().trim().equals(""))
		{
			Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.pleaseentervalidpassword), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
			passcode.setFocusable(true);
			toast.show();
			return false;
		}
		else if (passcode.getEditableText().toString().trim().length() > 10)
		{
			Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.passcodelessthan10), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			passcode.requestFocus();
			return false;
		}
		else if (!passcode.getEditableText().toString().equals(confirmPasscode.getText().toString()))
		{
			Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.mismatchpasscode), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			confirmPasscode.requestFocus();
			return false;
		}/* else if (!new EmailValidator().validate(email.getText().toString())) {
			Toast toast =Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalidemail), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			}*/
		else if (securityQuestion.getText().toString().equals(getResources().getString(R.string.securityquestion)) || securityQuestion.getText().toString().trim().equals(""))
		{
			Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.selectsecurityquestion), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			securityQuestion.requestFocus();
			return false;
		}
		else if (null == securityAnswer || securityAnswer.getText().toString().trim().equals(""))
		{
			Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalidanswer), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			securityAnswer.requestFocus();
			return false;
		}
		else
		{
			setPassword();
			return true;
		}
	
	}

	private void setPassword()
	{
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getApplicationContext());
		
		//	ApplicationSettingModel emailsettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APLLICATION_SETTING_EMAIL_KEY, email.getText()
		//			.toString(), PeriodTrackerObjectLocator.getInstance().getProfileId());
		//					if (((ApplicationSettingModel) applicationSettingDBHandler.getParticularApplicationSetting(PeriodTrackerConstants.APLLICATION_SETTING_EMAIL_KEY,
		//							PeriodTrackerObjectLocator.getInstance().getProfileId())).getValue() == null) {
		//						applicationSettingDBHandler.inseretApplicationSetting(emailsettingModel);
		//					} else {
		//						applicationSettingDBHandler.upadteApplicationSetting(emailsettingModel);
		//					}
		
		ApplicationSettingModel securityQuestionsetting = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION, securityQuestion.getText().toString(), PeriodTrackerObjectLocator.getInstance().getProfileId());
		
		if (((ApplicationSettingModel) applicationSettingDBHandler.getParticularApplicationSetting(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION, PeriodTrackerObjectLocator.getInstance().getProfileId())).getValue() == null)
		{
			applicationSettingDBHandler.inseretApplicationSetting(securityQuestionsetting);
		}
		else
		{
			applicationSettingDBHandler.upadteApplicationSetting(securityQuestionsetting);
		}
		
		ApplicationSettingModel securityAnswersetting = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_ANSWER, securityAnswer.getText().toString().trim(), PeriodTrackerObjectLocator.getInstance().getProfileId());
		
		if (((ApplicationSettingModel) applicationSettingDBHandler.getParticularApplicationSetting(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_ANSWER, PeriodTrackerObjectLocator.getInstance().getProfileId())).getValue() == null)
		{
			applicationSettingDBHandler.inseretApplicationSetting(securityAnswersetting);
		}
		else
		{
			applicationSettingDBHandler.upadteApplicationSetting(securityAnswersetting);
		}
		
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PASSWORD_STRING_KEY, passcode.getText().toString().trim(), PeriodTrackerObjectLocator.getInstance().getProfileId());
		
		if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
		{
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.passcodesetsucessfully), Toast.LENGTH_SHORT).show();
			PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(securityAnswer.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
			securityAnswer.setFocusable(false);
			
			//						if (Utility.isNetworkConnected(getApplicationContext())) {
			//							Utility.sendMail(SetPassword.this, emailsettingModel.getValue(), "set_password", PeriodTrackerObjectLocator.getInstance().getPasswordProtection(), "", "");
			//							
			//						}else {
			//							Toast.makeText(getApplicationContext(), getResources().getString(R.string.internetconnectioncheck), Toast.LENGTH_SHORT).show();
			//						}
		}
		
	
	}

	private void initLayout()
	{
		// TODO Auto-generated method stub
		securityQuestion = (Button) findViewById(R.id.securityquestion);
		securityAnswer = (EditText) findViewById(R.id.securityanswer);
		passcode = (EditText) findViewById(R.id.passcode);
		confirmPasscode = (EditText) findViewById(R.id.confirmpasscode);
		//	email = (EditText) findViewById(R.id.email);
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getApplicationContext());
		PeriodTrackerModelInterface modelInterface = applicationSettingDBHandler.getParticularApplicationSetting(PeriodTrackerConstants.APLLICATION_SETTING_EMAIL_KEY, PeriodTrackerObjectLocator.getInstance().getProfileId());
		/*if (modelInterface == null || null == ((ApplicationSettingModel) modelInterface).getValue()) {
			email.setText(getEmailid());
		} else {
			ApplicationSettingModel applicationSettingModel = (ApplicationSettingModel) modelInterface;
			email.setText(applicationSettingModel.getValue());
		}*/
		modelInterface = applicationSettingDBHandler.getParticularApplicationSetting(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION, PeriodTrackerObjectLocator.getInstance().getProfileId());
		if (modelInterface == null || null == ((ApplicationSettingModel) modelInterface).getValue() || ((ApplicationSettingModel) modelInterface).getValue().equals(""))
		{
			securityQuestion.setText(getResources().getString(R.string.securityquestion));
		}
		else
		{
			ApplicationSettingModel applicationSettingModel = (ApplicationSettingModel) modelInterface;
			securityQuestion.setText(applicationSettingModel.getValue());
		}
		if (PeriodTrackerObjectLocator.getInstance().getPasswordProtection() != null || !PeriodTrackerObjectLocator.getInstance().getPasswordProtection().equals(" "))
		{
			passcode.setText(PeriodTrackerObjectLocator.getInstance().getPasswordProtection());
			confirmPasscode.setText(PeriodTrackerObjectLocator.getInstance().getPasswordProtection());
		}
		
	}
	
	public void expand(final View v)
	{
		v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final int targtetHeight = v.getMeasuredHeight() - 100;
		
		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation()
		{
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t)
			{
				v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT : (int) (targtetHeight * interpolatedTime);
				//     v.getLayoutParams().height =  val;
				v.requestLayout();
				if (android.os.Build.VERSION.SDK_INT >= 11) v.setAlpha(interpolatedTime);
			}
			
			@Override
			public boolean willChangeBounds()
			{
				return true;
			}
		};
		
		// 1dp/ms
		a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
		
		//a.setDuration(300);
		//	a.setInterpolator(WelcomeSetting2.this, anim.accelerate_decelerate_interpolator);
		v.startAnimation(a);
	}
	
	public void collapse(final View v)
	{
		final int initialHeight = v.getMeasuredHeight();
		
		Animation a = new Animation()
		{
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t)
			{
				if (interpolatedTime == 1)
				{
					v.setVisibility(View.GONE);
				}
				else
				{
					int val = initialHeight - (int) (initialHeight * interpolatedTime);
					v.getLayoutParams().height = val;
					if (android.os.Build.VERSION.SDK_INT >= 11) v.setAlpha(1 - interpolatedTime);
					v.requestLayout();
				}
			}
			
			@Override
			public boolean willChangeBounds()
			{
				return true;
			}
		};
		
		// 1dp/ms
		a.setDuration(300);
		a.setInterpolator(WelcomeSetting2.this, anim.accelerate_decelerate_interpolator);
		v.startAnimation(a);
	}
	
	
	
	/**
	 * This method converts device specific pixels to density independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specif\ic display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	
	
	private void animateOut(final View v)
	{
		if (v.getVisibility() == View.VISIBLE)
		
		collapse(v);
		
	}
	
	private void resetChildes()
	{
		
		LinearLayout v = (LinearLayout) findViewById(R.id.llasetting);
		animateOut(v);
		v = (LinearLayout) findViewById(R.id.llrsetting);
		animateOut(v);
		v = (LinearLayout) findViewById(R.id.llpsetting);
		animateOut(v);
		
		v = (LinearLayout) findViewById(R.id.llnsetting);
		animateOut(v);
		v = (LinearLayout) findViewById(R.id.llprsetting);
		animateOut(v);
		Theme t = Theme.getCurrentTheme(this);
		Drawable d;
		if (t != null)
		{
		d=t.getDrawableResource("drop_down");
		}
		else
		d=getResources().getDrawable(R.drawable.drop_down);
		ttprsetting.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
		ttpesetting.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
		ttasetting.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
		ttnsetting.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
		ttrsetting.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
		
	}
	
	private void toggleVisibility(int id, int parentid, boolean visibility)
	{
		
		resetChildes();
		if (visibility)
		{
			expand(findViewById(id));
			Theme t = Theme.getCurrentTheme(this);
			Drawable d;
			if (t != null)
			d=t.getDrawableResource("up_arrow");
			else
			d=getResources().getDrawable(R.drawable.up_arrow);
			((TextView) findViewById(parentid)).setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
		}
		
	}
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		refreashViewValuew();
		if (id == R.id.llhprsetting)
		{
			toggleVisibility(R.id.llprsetting, R.id.txtprsetting, !(findViewById(R.id.llprsetting).getVisibility() == View.VISIBLE));
		}
		else if (id == R.id.llhpsetting)
		{
			toggleVisibility(R.id.llpsetting, R.id.txtpsetting, !(findViewById(R.id.llpsetting).getVisibility() == View.VISIBLE));
		}
		else if (id == R.id.llhasetting)
		{
			toggleVisibility(R.id.llasetting, R.id.txtasetting, !(findViewById(R.id.llasetting).getVisibility() == View.VISIBLE));
		}
		else if (id == R.id.llhnretting)
		{
			toggleVisibility(R.id.llrsetting, R.id.txtnretting, !(findViewById(R.id.llrsetting).getVisibility() == View.VISIBLE));
		}
		else if (id == R.id.llhnsetting)
		{
			toggleVisibility(R.id.llnsetting, R.id.txtnsetting, !(findViewById(R.id.llnsetting).getVisibility() == View.VISIBLE));
		}
		else if (id == R.id.rowAppIconAndName)
		{
			Intent intent = new Intent(WelcomeSetting2.this, ChangeAppIconView.class);
			startActivity(intent);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
		}
		else if (id == R.id.rowPeriodAlert)
		{
			childposition = 0;
			showOvaPerFerValue();
		}
		else if (id == R.id.rowFertilityAlert)
		{
			childposition = 1;
			showOvaPerFerValue();
		}
		else if (id == R.id.rowOvalutionAlert)
		{
			childposition = 2;
			showOvaPerFerValue();
		}
		else if (id == R.id.rowHeight)
		{
			onClickHeight();
		}
		else if (id == R.id.rowHeightUnit)
		{
			onClickSelectHieghtUnit();
		}
		else if (id == R.id.rowSetDateFormate)
		{
			OnClickDateFormat();
		}
		else if (id == R.id.rowSetFirstDateOfWeek)
		{
			onClickChangeDayOfWeek(getApplicationContext());
		}
		else if (id == R.id.rowSetLanguage)
		{
			onClickOfChangeApplicationLanuage();
		}
		else if (id == R.id.rowSetTheme)
		{
			onClickTheme(getApplicationContext());
		}
		else if (id == R.id.rowTempUnit)
		{
			onClickSelectTempUnit();
		}
		else if (id == R.id.rowWeight)
		{
			onClickWeight();
		}
		else if (id == R.id.rowWidthUnit)
		{
			onClickSelectWeightUnit();
		}
		else if (id == R.id.rowTemp)
		{
			onClickTemperature();
		}
		else if (id == R.id.continueWel)
		{
			if(!(passcode.getText().toString().trim().equals("")&&
					confirmPasscode.getText().toString().trim().equals("")
					&&securityAnswer.getText().toString().trim().equals("")))
			{
				if(savePasswordMethod())
				{
					Intent congrets = new Intent(WelcomeSetting2.this, CongretulationWizard.class);
					startActivity(congrets);
					finish();
					APP.GLOBAL().exicuteRIOAnim(WelcomeSetting2.this);
				}
				else
				{
					toggleVisibility(R.id.llprsetting, R.id.txtprsetting, !(findViewById(R.id.llprsetting).getVisibility() == View.VISIBLE));
				  findViewById(R.id.clearField).setVisibility(View.VISIBLE);
				}
			}
				
			else
			{
				Intent congrets = new Intent(WelcomeSetting2.this, CongretulationWizard.class);
				startActivity(congrets);
				finish();
				APP.GLOBAL().exicuteRIOAnim(WelcomeSetting2.this);
			}
		}
		else
		{
		}
		
	}
	
	private void refreashViewValuew()
	{
		// TODO Auto-generated method stub
		showFertilityValue();
		showHeightValue();
		showHeightUnit();
		showOvalutionValue();
		showPeriodValue();
		showsetDateFormate.setText(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		showDayOfWeek();
		showLanguage.setText(PeriodTrackerObjectLocator.getInstance().getAppLanguage());
		showTempUnit.setText(PeriodTrackerObjectLocator.getInstance().getTempUnit());
		showWeightValue();
		showWidthUnit.setText(PeriodTrackerObjectLocator.getInstance().getWeighUnit());
		showTempValue();
	}
	
	private void showTempValue()
	{
		// TODO Auto-generated method stub
		if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit())
		{
			if (PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(getApplicationContext().getString(R.string.celsius)))
			{
				
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule())) showTemp.setText(Utility.getStringFormatedNumber(String.valueOf((Utility
						.ConvertToCelsius(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))))) + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
				
			}
			else
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
				
				showTemp.setText(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
				
			}
		}
		else
		{
			if (null != PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
			
			showTemp.setText(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
			
		}
	}
	
	private void showWeightValue()
	{
		// TODO Auto-generated method stub
		if (null != PeriodTrackerObjectLocator.getInstance().getWeighUnit())
		{
			
			if (PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(getApplicationContext().getString(R.string.KG)))
			{
				
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
				{
					
					showWidth.setText(Utility.getStringFormatedNumber(String.valueOf((Utility.ConvertToKilogram(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))))) + " " + PeriodTrackerObjectLocator.getInstance().getWeighUnit());
					
				}
			}
			else
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
				{
					
					showWidth.setText(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() + " " + PeriodTrackerObjectLocator.getInstance().getWeighUnit());
				}
			}
		}
	}
	
	private void showDayOfWeek()
	{
		int selectedPosition = 0;
		final String[] items =
		{
				getApplicationContext().getString(R.string.sunday), getApplicationContext().getString(R.string.monday), getApplicationContext().getString(R.string.saturady)
		};
		
		if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("0"))
		{
			selectedPosition = 0;
		}
		else if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("1"))
		{
			selectedPosition = 1;
		}
		else if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("6"))
		{
			selectedPosition = 2;
		}
		
		showSetFirstdateOfWeek.setText(items[selectedPosition]);
		// TODO Auto-generated method stub
		
	}
	
	private void showPeriodValue()
	{
		// TODO Auto-generated method stub
		showPeriodAlert.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() != -1 && PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() != 1)
		{
			if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() == 0)
			{
				showPeriodAlert.setText(getApplicationContext().getString(R.string.ontheday));
			}
			else
			{
				showPeriodAlert.setText(String.valueOf(PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification()) + " " + getApplicationContext().getString(R.string.daysbefore));
				
			}
		}
		else if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() == 1)
		{
			showPeriodAlert.setText(String.valueOf(PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification()) + " " + getApplicationContext().getString(R.string.daybefore));
		}
		else if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() == -1)
		{
			showPeriodAlert.setText(getApplicationContext().getString(R.string.nonotificationrequired));
		}
		// TODO Auto-generated method stub
		
	}
	
	private void showFertilityValue()
	{
		showFertileAlert.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		if (PeriodTrackerObjectLocator.getInstance().getFertilityNotification() != -1 && PeriodTrackerObjectLocator.getInstance().getFertilityNotification() != 1)
		{
			if (PeriodTrackerObjectLocator.getInstance().getFertilityNotification() == 0)
			{
				showFertileAlert.setText(getApplicationContext().getString(R.string.ontheday));
			}
			else
			{
				showFertileAlert.setText(PeriodTrackerObjectLocator.getInstance().getFertilityNotification() + " " + getApplicationContext().getString(R.string.daysbefore));
			}
		}
		else if (PeriodTrackerObjectLocator.getInstance().getFertilityNotification() == 1)
		{
			showFertileAlert.setText(PeriodTrackerObjectLocator.getInstance().getFertilityNotification() + " " + getApplicationContext().getString(R.string.daysbefore));
			
		}
		else if (PeriodTrackerObjectLocator.getInstance().getFertilityNotification() == -1)
		{
			showFertileAlert.setText(getApplicationContext().getString(R.string.nonotificationrequired));
		}
		
	}
	
	private void showOvalutionValue()
	{
		if (PeriodTrackerObjectLocator.getInstance().getOvulationNotification() != -1 && PeriodTrackerObjectLocator.getInstance().getOvulationNotification() != 1)
		{
			if (PeriodTrackerObjectLocator.getInstance().getOvulationNotification() == 0)
			{
				showOvalutionAlert.setText(getApplicationContext().getString(R.string.ontheday));
			}
			else
			{
				showOvalutionAlert.setText(PeriodTrackerObjectLocator.getInstance().getOvulationNotification() + " " + getApplicationContext().getString(R.string.daysbefore));
			}
		}
		
		else if (PeriodTrackerObjectLocator.getInstance().getOvulationNotification() == 1)
		{
			showOvalutionAlert.setText(PeriodTrackerObjectLocator.getInstance().getOvulationNotification() + " " + getApplicationContext().getString(R.string.daybefore));
			
		}
		else if (PeriodTrackerObjectLocator.getInstance().getOvulationNotification() == -1)
		{
			showOvalutionAlert.setText(getApplicationContext().getString(R.string.nonotificationrequired));
		}
		// TODO Auto-generated method stub
		
	}
	
	private void showOvaPerFerValue()
	{
		int selectedPosition = -1;
		AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeSetting2.this);
		final String[] items =
		{
				getResources().getString(R.string.nonotificationrequired), getResources().getString(R.string.ontheday), " 1 " + getResources().getString(R.string.daybefore), " 2 " + getResources().getString(R.string.daysbefore), " 3 " + getResources().getString(R.string.daysbefore),
				" 4 " + getResources().getString(R.string.daysbefore)
		};
		if (childposition == 0)
		{
			selectedPosition = PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification();
			builder.setTitle(getResources().getString(R.string.setdaysforperiodnotifications));
		}
		else if (childposition == 1)
		{
			selectedPosition = PeriodTrackerObjectLocator.getInstance().getFertilityNotification();
			builder.setTitle(getResources().getString(R.string.setdaysforfetilenotifications));
		}
		else if (childposition == 2)
		{
			selectedPosition = PeriodTrackerObjectLocator.getInstance().getOvulationNotification();
			builder.setTitle(getResources().getString(R.string.setdaysforovualtionnotifications));
		}
		builder.setSingleChoiceItems(items, selectedPosition + 1, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				dialog.dismiss();
				if (childposition == 0)
				{
					setVaulesAlertDaysForPeriodStart(which);
					 showPeriodValue();
				}
				else if (childposition == 1)
				{
					setVaulesAlertDaysForFertileStart(which);
					showFertilityValue();
				}
				else if (childposition == 2)
				{
					setVaulesAlertDaysForOvulationStart(which);
					showOvalutionValue();
					
				}
			}
		});
		builder.show();
	}
	
	// TODO Auto-generated method stub
	
	private void showHeightUnit()
	{
		
		showHeightUnit.setText(PeriodTrackerObjectLocator.getInstance().getHeightUnit());
	}
	
	private void showHeightValue()
	{
		// TODO Auto-generated method stub
		showHeight.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		if (PeriodTrackerObjectLocator.getInstance().getHeightUnit() != null && PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() != null && !PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule().equals("0.0"))
		{
			if (PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(getApplicationContext().getString(R.string.inches)))
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))
				{
					showHeight.setText(Utility.getStringFormatedNumber(String.valueOf((Utility.convertCentiMeterTOInches(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))))) + " " + PeriodTrackerObjectLocator.getInstance().getHeightUnit());
				}
			}
			else
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule())) showHeight.setText(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() + " "
						+ PeriodTrackerObjectLocator.getInstance().getHeightUnit());
			}
			
			/*if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) && PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule().equals("0.0"))
			{
				txtChildUnit.setVisibility(View.INVISIBLE);
			}*/
		}
		else
		{
			if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) && PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule().equals("0.0"))
			{
				showHeight.setText(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() + PeriodTrackerObjectLocator.getInstance().getHeightUnit());
			}
			
		}
	}
	
	public void onClickSelectWeightUnit()
	{
		
		int selectedPosition;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items =
		{
				getResources().getString(R.string.KG), getResources().getString(R.string.lb)
		};
		if (String.valueOf(PeriodTrackerObjectLocator.getInstance().getWeighUnit()).equalsIgnoreCase(getResources().getString(R.string.KG)))
		{
			selectedPosition = 0;
		}
		else
		{
			selectedPosition = 1;
		}
		
		builder.setTitle(getResources().getString(R.string.changedefaultlweightunit));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				if (which == 0)
				{
					setWeightUnit(getResources().getString(R.string.KG));
				}
				else
				{
					setWeightUnit(getResources().getString(R.string.lb));
				}
				showWidthUnit.setText(PeriodTrackerObjectLocator.getInstance().getWeighUnit());
				dialog.dismiss();
				
			}
		});
		builder.show();
		
	}
	
	public void onClickSelectHieghtUnit()
	{
		
		int selectedPosition;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items =
		{
				getResources().getString(R.string.CM), getResources().getString(R.string.inches)
		};
		if (String.valueOf(PeriodTrackerObjectLocator.getInstance().getHeightUnit()).equalsIgnoreCase(getResources().getString(R.string.CM)))
		{
			selectedPosition = 0;
		}
		else
		{
			selectedPosition = 1;
		}
		
		builder.setTitle(getResources().getString(R.string.changedefaultlheightunit));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				if (which == 0)
				{
					setHieghUnit(getResources().getString(R.string.CM));
				}
				else
				{
					setHieghUnit(getResources().getString(R.string.inches));
				}
				showHeightUnit();
				dialog.dismiss();
				
			}
		});
		builder.show();
		
	}
	
	public void onClickSelectTempUnit()
	{
		
		int selectedPosition;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items =
		{
				getResources().getString(R.string.celsius), getResources().getString(R.string.Fehr)
		};
		if (String.valueOf(PeriodTrackerObjectLocator.getInstance().getTempUnit()).equalsIgnoreCase(getResources().getString(R.string.celsius)))
		{
			selectedPosition = 0;
		}
		else
		{
			selectedPosition = 1;
		}
		
		builder.setTitle(getResources().getString(R.string.bodytempratureunit));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				if (which == 0)
				{
					setTempUnit(getResources().getString(R.string.celsius));
				}
				else
				{
					setTempUnit(getResources().getString(R.string.Fehr));
				}
				showTempUnit.setText(PeriodTrackerObjectLocator.getInstance().getTempUnit());
				dialog.dismiss();
				
			}
		});
		builder.show();
		
	}
	
	private void onClickOfChangeApplicationLanuage()
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.changedefaultLanuage));
		final String[] items =
		{ // getResources().getString(R.string.croatian),
				getResources().getString(R.string.dutch), getResources().getString(R.string.english), getResources().getString(R.string.french), getResources().getString(R.string.german), getResources().getString(R.string.italian),
				// getResources().getString(R.string.japanese),
				getResources().getString(R.string.portuguese), getResources().getString(R.string.spanish)
		};
		
		int selectedPosition = Arrays.asList(items).indexOf(Utility.getLanguageFromLoacle(String.valueOf(PeriodTrackerObjectLocator.getInstance().getAppLanguage()), getApplicationContext()));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				Locale locale = null;
				String vaString = Utility.AppLanguageLocale(items[which], getApplicationContext());
				locale = new Locale(vaString);
				
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getApplicationContext());
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_APP_LANGUAGE_KEY, vaString, PeriodTrackerObjectLocator.getInstance().getProfileId());
				applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				
				Resources res = getApplicationContext().getResources();
				// Change locale settings in the app.
				DisplayMetrics dm = res.getDisplayMetrics();
				android.content.res.Configuration conf = res.getConfiguration();
				conf.locale = locale;
				
				res.updateConfiguration(conf, dm);
				showLanguage.setText(PeriodTrackerObjectLocator.getInstance().getAppLanguage());
				dialog.dismiss();
				restartActivity();
				
			}
		});
		builder.show();
	}
	
	private void restartActivity()
	{
		
		Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		WelcomeSetting2.this.finish();
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}
	
	public void OnClickDateFormat()
	{
		
		final AlertDialog.Builder builder;
		
		builder = new AlertDialog.Builder(this);
		
		builder.setTitle(getResources().getString(R.string.selectdateformat));
		
		final String[] patternItems =
		{
				"MM/dd/yyyy", "dd/MM/yyyy", "yyyy/dd/MM",
				
				"MM dd yyyy", "yyyy dd MM", "yyyy MM dd",
				
				"dd MMM, yyyy", "MMM dd, yyyy", "yyyy MMM dd"
		};
		
		final String[] items =
		{
				new SimpleDateFormat("MM/dd/yyyy").format(new Date()), new SimpleDateFormat("dd/MM/yyyy").format(new Date()), new SimpleDateFormat("yyyy/dd/MM").format(new Date()),
				
				new SimpleDateFormat("MM dd yyyy").format(new Date()), new SimpleDateFormat("yyyy dd MM").format(new Date()), new SimpleDateFormat("yyyy MM dd").format(new Date()),
				
				new SimpleDateFormat("dd MMM, yyyy").format(new Date()), new SimpleDateFormat("MMM dd, yyyy").format(new Date()), new SimpleDateFormat("yyyy MMM dd").format(new Date())
		};
		
		int selectedPosition = Arrays.asList(items).indexOf(new SimpleDateFormat(String.valueOf(PeriodTrackerObjectLocator.getInstance().getDateFormat())).format(new Date()));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getApplicationContext());
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_DATE_FORMAT_KEY, patternItems[which], PeriodTrackerObjectLocator.getInstance().getProfileId());
				applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				dialog.dismiss();
				showsetDateFormate.setText(PeriodTrackerObjectLocator.getInstance().getDateFormat());
				//initLayout();
			}
		});
		builder.show();
		
	}
	
	public void setWeightUnit(String Value)
	{
		try
		{
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_UNIT_KEY, Value, PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
			{
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				
			}
			//initLayout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setTempUnit(String Value)
	{
		
		try
		{
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMP_UNIT_KEY, Value, PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
			{
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				
			}
			//initLayout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void setHieghUnit(String value)
	{
		
		try
		{
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_UNIT_KEY, value, PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
			{
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				
			}
			//initLayout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void onClickHeight()
	{
		
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper(WelcomeSetting2.this, android.R.style.Theme_DeviceDefault_Light);
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from(this).inflate(R.layout.forheigthandweightvalue, null);
		t = Theme.getCurrentTheme(WelcomeSetting2.this, view);
		if (t != null)
		{
			t.applyBackgroundColor(R.id.heightandwightvalue, "view_bg");
			t.applyBackgroundColor(R.id.divider, "text_color");
			t.applyBackgroundColor(R.id.valuecancel, "text_color");
			t.applyBackgroundColor(R.id.setvalue, "text_color");
			t.applyBackgroundDrawable(R.id.heightweightvalue, "mpt_edit_text_sltr");
			
			t.applyTextColor(R.id.tileforhieghtandweight, "text_color");
			t.applyTextColor(R.id.heightweightvalue, "text_color");
			t.applyTextColor(R.id.text, "text_color");
			
		}
		builder.setView(view);
		final AlertDialog alertDialog = builder.create();
		TextView title = (TextView) view.findViewById(R.id.tileforhieghtandweight);
		title.setText(getResources().getString(R.string.hieght) + " in " + PeriodTrackerObjectLocator.getInstance().getHeightUnit());
		final EditText editText = (EditText) view.findViewById(R.id.heightweightvalue);
		
		if (PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(getResources().getString(R.string.CM)))
		{
			
			if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))
			{
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) != 0)
				{
					editText.setText(Utility.getStringFormatedNumber((PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule())));
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_HEIGHT_VALUE_IN_CM)));
					
				}
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_HEIGHT_VALUE_IN_CM)));
			}
		}
		else
		{
			
			if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))
			{
				
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) != 0)
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule())))));
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(PeriodTrackerConstants.DEFAULT_HEIGHT_VALUE_IN_CM))));
					
				}
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(PeriodTrackerConstants.DEFAULT_HEIGHT_VALUE_IN_CM))));
				
			}
		}
		Button set = (Button) view.findViewById(R.id.setvalue);
		Button cancel = (Button) view.findViewById(R.id.valuecancel);
		
		cancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		
		set.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (validateHieght(editText.getText().toString()))
				{
					setHieghtValue(editText.getText().toString());
					showHeightValue();
					alertDialog.dismiss();
					
				}
			}
		});
		
		alertDialog.show();
		
	}
	
	public boolean validateHieght(String hieghtVaule)
	{
		boolean validateHiegh = false;
		AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeSetting2.this);
		if (null != PeriodTrackerObjectLocator.getInstance().getHeightUnit() && PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(getResources().getString(R.string.CM)))
		{
			if (null != hieghtVaule && !hieghtVaule.equals(""))
			{
				if (Float.parseFloat(hieghtVaule) > PeriodTrackerConstants.MAX_HEIGHT_IN_CENTIMETER || (Float.parseFloat(hieghtVaule) < PeriodTrackerConstants.MIN_HEIGHT_IN_CENTIMETER))
				{
					validateHiegh = false;
					builder.setTitle(getResources().getString(R.string.invalidhieght));
					builder.setMessage(getResources().getString(R.string.invalidhieghtmessageincm));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				}
				else
				{
					validateHiegh = true;
				}
			}
		}
		else
		{
			if (null != hieghtVaule && !hieghtVaule.equals(""))
			{
				if (Float.parseFloat(hieghtVaule) > PeriodTrackerConstants.MAX_HEIGHT_IN_INCHES || (Float.parseFloat(hieghtVaule) < PeriodTrackerConstants.MIN_HEIGHT_IN_INCHES))
				{
					// Toast.makeText(getActivity(), "invalid hieght in inches",
					// Toast.LENGTH_SHORT).show();
					validateHiegh = false;
					builder.setTitle(getResources().getString(R.string.invalidhieght));
					builder.setMessage(getResources().getString(R.string.invalidhieghtmessageinInches));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					
					builder.show();
				}
				else
				{
					validateHiegh = true;
				}
			}
		}
		
		return validateHiegh;
	}
	
	public void onClickWeight()
	{
		
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper(WelcomeSetting2.this, android.R.style.Theme_DeviceDefault_Light);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from(this).inflate(R.layout.forheigthandweightvalue, null);
		t = Theme.getCurrentTheme(WelcomeSetting2.this, view);
		if (t != null)
		{
			t.applyBackgroundColor(R.id.heightandwightvalue, "view_bg");
			t.applyBackgroundColor(R.id.divider, "text_color");
			t.applyBackgroundColor(R.id.valuecancel, "text_color");
			t.applyBackgroundColor(R.id.setvalue, "text_color");
			t.applyBackgroundDrawable(R.id.heightweightvalue, "mpt_edit_text_sltr");
			
			t.applyTextColor(R.id.tileforhieghtandweight, "text_color");
			t.applyTextColor(R.id.heightweightvalue, "text_color");
			t.applyTextColor(R.id.text, "text_color");
			
		}
		builder.setView(view);
		
		final AlertDialog alertDialog = builder.create();
		
		TextView title = (TextView) view.findViewById(R.id.tileforhieghtandweight);
		
		title.setText(getResources().getString(R.string.weight) + " in " + PeriodTrackerObjectLocator.getInstance().getWeighUnit());
		
		final EditText editText = (EditText) view.findViewById(R.id.heightweightvalue);
		//	editText.setText(showWidth.getText().toString());
		
		if (PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(getResources().getString(R.string.lb)))
		{
			
			if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
			{
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) != 0)
				{
					editText.setText(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()));
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_WEIGHT_VAULE_IN_LB)));
				}
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerConstants.DEFAULT_WEIGHT_VAULE_IN_LB)));
				
			}
		}
		else
		{
			
			if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
			{
				
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) != 0)
				{
					
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule())))));
					
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram((PeriodTrackerConstants.DEFAULT_WEIGHT_VAULE_IN_LB)))));
					
				}
				
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram((PeriodTrackerConstants.DEFAULT_WEIGHT_VAULE_IN_LB)))));
				
			}
		}
		
		Button set = (Button) view.findViewById(R.id.setvalue);
		Button cancel = (Button) view.findViewById(R.id.valuecancel);
		
		cancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		
		set.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (checkValidityOfWeight(editText.getText().toString()))
				{
					setWeightValue(editText.getText().toString());
					alertDialog.dismiss();
					showWeightValue();
				}
			}
		});
		
		alertDialog.show();
		
	}
	
	public boolean checkValidityOfWeight(String weightValue)
	{
		boolean validateWeigh = false;
		AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeSetting2.this);
		if (PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(getResources().getString(R.string.lb)))
		{
			if (null != weightValue && !weightValue.equals(""))
			{
				
				if (Float.parseFloat(weightValue) > PeriodTrackerConstants.MAX_WEIGHT_IN_LB || Float.parseFloat(weightValue) < PeriodTrackerConstants.MIN_WEIGHT_IN_LB)
				{
					validateWeigh = false;
					// Toast.makeText(getActivity(), "invalid Weight in lb",
					// Toast.LENGTH_SHORT).show();
					builder.setTitle(getResources().getString(R.string.invalidwieght));
					builder.setMessage(getResources().getString(R.string.invalidwieghtmessageinlb));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				}
				else
				{
					validateWeigh = true;
				}
				
			}
			
		}
		else
		{
			if (null != weightValue && !weightValue.equals(""))
			{
				if (Float.parseFloat(weightValue) > PeriodTrackerConstants.MAX_WEIGHT_IN_KG || Float.parseFloat(weightValue) < PeriodTrackerConstants.MIN_WEIGHT_IN_KG)
				{
					validateWeigh = false;
					// Toast.makeText(getActivity(), "invalid wieght in kg",
					// Toast.LENGTH_SHORT).show();
					builder.setTitle(getResources().getString(R.string.invalidwieght));
					builder.setMessage(getResources().getString(R.string.invalidwieghtmessageinkg));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				}
				else
				{
					validateWeigh = true;
					
				}
			}
		}
		return validateWeigh;
		
	}
	
	public void setWeightValue(String value)
	{
		
		value = Utility.getStringFormatedNumber(value);
		if (Utility.isValidNumber(float.class, value))
		{
			try
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getWeighUnit() && !PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(getResources().getString(R.string.lb))) value = String.valueOf(Utility.ConvertToPounds(Float.parseFloat(value)));
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_VALUE_KEY, value, PeriodTrackerObjectLocator.getInstance().getProfileId());
				if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel)) PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "wrong value", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void setHieghtValue(String Value)
	{
		Value = Utility.getStringFormatedNumber(Value);
		
		if (Utility.isValidNumber(float.class, Value))
		{
			
			try
			{
				
				if (null != PeriodTrackerObjectLocator.getInstance().getHeightUnit() && !PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(getResources().getString(R.string.CM)))
				{
					Value = Utility.getStringFormatedNumber(String.valueOf(Utility.convertInchesTOCentiMeter(Float.parseFloat(Value))));
				}
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_VALUE_KEY, Value, PeriodTrackerObjectLocator.getInstance().getProfileId());
				
				if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
				{
					PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
					
				}
				//initLayout();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "wrong value", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onClickTemperature()
	{
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper(WelcomeSetting2.this, android.R.style.Theme_DeviceDefault_Light);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from(this).inflate(R.layout.forheigthandweightvalue, null);
		t = Theme.getCurrentTheme(WelcomeSetting2.this, view);
		if (t != null)
		{
			t.applyBackgroundColor(R.id.heightandwightvalue, "view_bg");
			t.applyBackgroundColor(R.id.divider, "text_color");
			t.applyBackgroundColor(R.id.valuecancel, "text_color");
			t.applyBackgroundColor(R.id.setvalue, "text_color");
			t.applyBackgroundDrawable(R.id.heightweightvalue, "mpt_edit_text_sltr");
			
			t.applyTextColor(R.id.tileforhieghtandweight, "text_color");
			t.applyTextColor(R.id.heightweightvalue, "text_color");
			t.applyTextColor(R.id.text, "text_color");
			
		}
		builder.setView(view);
		final AlertDialog alertDialog = builder.create();
		TextView title = (TextView) view.findViewById(R.id.tileforhieghtandweight);
		if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit())
		{
			title.setText(getResources().getString(R.string.temprature) + " in " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
		}
		final EditText editText = (EditText) view.findViewById(R.id.heightweightvalue);
		if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit() && PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(getResources().getString(R.string.Fehr)))
		{
			
			if (Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
			{
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()) != Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))
				{
					editText.setText(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()));
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))));
					
				}
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber(String.valueOf(Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))));
				
			}
		}
		else
		{
			
			if (Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
			{
				if (Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()) != Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))
				{
					editText.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule())))));
				}
				else
				{
					editText.setText(Utility.getStringFormatedNumber((String.valueOf(Utility.ConvertToCelsius(Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))))));
					
				}
			}
			else
			{
				editText.setText(Utility.getStringFormatedNumber((String.valueOf(Utility.ConvertToCelsius(Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))))));
				
			}
		}
		Button set = (Button) view.findViewById(R.id.setvalue);
		Button cancel = (Button) view.findViewById(R.id.valuecancel);
		
		cancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		
		set.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (checkValidityOfTemp(editText.getText().toString()))
				{
					setTemptValue(editText.getText().toString());
					alertDialog.dismiss();
					showTempValue();
				}
			}
		});
		
		alertDialog.show();
		
	}
	
	public boolean checkValidityOfTemp(String tempValue)
	{
		boolean validateTemp = false;
		AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeSetting2.this);
		if (PeriodTrackerObjectLocator.getInstance().getTempUnit().toString().equals(getResources().getString(R.string.celsius)))
		{
			
			if (null != tempValue && !tempValue.equals(""))
			{
				
				if (Float.parseFloat(tempValue) > PeriodTrackerConstants.MAX_TEMP_IN_CELSIUS || Float.parseFloat(tempValue) < PeriodTrackerConstants.MIN_TEMP_IN_CELSIUS)
				{
					validateTemp = false;
					builder.setTitle(getResources().getString(R.string.invalidtemp));
					builder.setMessage(getResources().getString(R.string.invalidtempmessageinC));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
					// Toast.makeText(PersonalSettingView.this,
					// "invalid temp in celsius",
					// Toast.LENGTH_SHORT).show();
				}
				else
				{
					validateTemp = true;
					
				}
				
			}
			
		}
		else
		{
			
			if (null != tempValue && !tempValue.equals(""))
			{
				if (Float.parseFloat(tempValue) > PeriodTrackerConstants.MAX_TEMP_IN_FEHRENHIET || Float.parseFloat((tempValue)) < PeriodTrackerConstants.MIN_TEMP_IN_FEHRENHIET)
				{
					validateTemp = false;
					builder.setTitle(getResources().getString(R.string.invalidtemp));
					builder.setMessage(getResources().getString(R.string.invalidtemptmessageinF));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
					
					// Toast.makeText(PersonalSettingView.this,
					// "invalid temp in Ferhinhiet ",
					// Toast.LENGTH_SHORT).show();
				}
				else
				{
					validateTemp = true;
					
				}
			}
		}
		
		return validateTemp;
		
	}
	
	public void setTemptValue(String Value)
	{
		Value = Utility.getStringFormatedNumber(Value);
		
		try
		{
			if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit() && !PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(getResources().getString(R.string.Fehr)))
			{
				Value = Utility.getStringFormatedNumber(String.valueOf((Utility.ConvertToFahrenheit(Float.parseFloat(Value)))));
			}
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_TEMPERATURE_VALUE_KEY, Value, PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
			{
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				
			}
			//initLayout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setVaulesAlertDaysForPeriodStart(int vaule)
	{
		
		List<PeriodTrackerModelInterface> recordList, deleteNotificationList;
		
		PeriodLogModel logModel;
		int days = 0;
		
		vaule = vaule - 1;
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_PERIOD_START_NOTIFICTION_KEY, String.valueOf(vaule), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
		
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		//initLayout();
		NotificationDBHandler notificationDBHandler = new NotificationDBHandler(getApplicationContext());
		NotificattionModel notificattionModel = new NotificattionModel(0, getResources().getString(R.string.period_alert));
		
		recordList = notificationDBHandler.getPredictionLogs();
		deleteNotificationList = notificationDBHandler.selectParticularNotificationType(notificattionModel);
		
		if (vaule == -1)
		{
			
			this.getSharedPreferences("com.linchpin.periodtracker", 0).edit().putBoolean("needtochange", true).commit();
			/*
			 * Utility.cancelScheduledNotification(getApplicationContext(),
			 * deleteNotificationList);
			 */}
		else
		{
			
			this.getSharedPreferences("com.linchpin.periodtracker", 0).edit().putBoolean("needtochange", true).commit();
			/*
			 * Utility.cancelScheduledNotification(getApplicationContext(),
			 * deleteNotificationList);
			 * 
			 * Utility.createScheduledNotification(getApplicationContext(),
			 * recordList, notificattionModel);
			 */
		}
		
	}
	
	public void setVaulesAlertDaysForFertileStart(int vaule)
	{
		List<PeriodTrackerModelInterface> recordList, deleteNotificationList;
		
		vaule = vaule - 1;
		
		PeriodLogModel logModel;
		
		int days = 0;
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_FERTILITY_NOTIFICTION_KEY, String.valueOf(vaule), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
		
		NotificationDBHandler notificationDBHandler = new NotificationDBHandler(getApplicationContext());
		NotificattionModel notificattionModel = new NotificattionModel(0, getResources().getString(R.string.fertile_alert));
		
		recordList = notificationDBHandler.getPerdictionFertileDatesAndOvulationDates();
		
		deleteNotificationList = notificationDBHandler.selectParticularNotificationType(notificattionModel);
		
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		
		//initLayout();
		
		if (vaule == -1)
		{
			this.getSharedPreferences("com.linchpin.periodtracker", 0).edit().putBoolean("needtochange", true).commit();
			/*
			 * Utility.cancelScheduledNotification(getApplicationContext(),
			 * deleteNotificationList);
			 */
		}
		else
		{
			
			this.getSharedPreferences("com.linchpin.periodtracker", 0).edit().putBoolean("needtochange", true).commit();
			/*
			 * Utility.cancelScheduledNotification(getApplicationContext(),
			 * deleteNotificationList);
			 * 
			 * Utility.createScheduledNotification(NotificationSettingsView.this,
			 * recordList, notificattionModel);
			 */}
		
	}
	
	public void setVaulesAlertDaysForOvulationStart(int vaule)
	{
		
		List<PeriodTrackerModelInterface> recordList, deleteNotificationList;
		
		PeriodLogModel logModel;
		vaule = vaule - 1;
		
		int days = 0;
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_OVULATION_NOTIFICTION_KEY, String.valueOf(vaule), PeriodTrackerObjectLocator.getInstance().getProfileId());
		applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
		
		NotificationDBHandler notificationDBHandler = new NotificationDBHandler(getApplicationContext());
		NotificattionModel notificattionModel = new NotificattionModel(0, getResources().getString(R.string.ovulation_alert));
		
		recordList = notificationDBHandler.getPerdictionFertileDatesAndOvulationDates();
		
		deleteNotificationList = notificationDBHandler.selectParticularNotificationType(notificattionModel);
		
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		//initLayout();
		
		if (vaule == -1)
		{
			this.getSharedPreferences("com.linchpin.periodtracker", 0).edit().putBoolean("needtochange", true).commit();
			/*
			 * Utility.cancelScheduledNotification(getApplicationContext(),
			 * deleteNotificationList);
			 */}
		else
		{
			
			this.getSharedPreferences("com.linchpin.periodtracker", 0).edit().putBoolean("needtochange", true).commit();
			/*
			 * Utility.cancelScheduledNotification(getApplicationContext(),
			 * deleteNotificationList);
			 * 
			 * Utility.createScheduledNotification(NotificationSettingsView.this,
			 * recordList, notificattionModel);
			 */}
		
	}
	
	public void onClickChangeDayOfWeek(Context context)
	{
		
		int selectedPosition = -1;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items =
		{
				getResources().getString(R.string.sunday), getResources().getString(R.string.monday), getResources().getString(R.string.saturady)
		};
		
		if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("0"))
		{
			selectedPosition = 0;
		}
		else if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("1"))
		{
			selectedPosition = 1;
		}
		else if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("6"))
		{
			selectedPosition = 2;
		}
		showDayOfWeek();
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
				if (which == 0)
				{
					setDayOfWeek("0");
					showSetFirstdateOfWeek.setText(items[which]);
				}
				else if (which == 1)
				{
					setDayOfWeek("1");
					showSetFirstdateOfWeek.setText(items[which]);
				}
				else if (which == 2)
				{
					setDayOfWeek("6");
					showSetFirstdateOfWeek.setText(items[which]);
				}
				dialog.dismiss();
				
			}
		});
		builder.show();
		
	}
	
	private void setupTheme()
	{
		HashMap<String, String> hm = Theme.getAvailableThemes(this);
		Set<String> key = hm.keySet();
		values = new String[hm.size()];
		entries = new String[hm.size()];
		Iterator iter = key.iterator();
		int i = 0;
		while (iter.hasNext())
		{
			String ss = (String) iter.next();
			values[i] = ss.equals("") ? getResources().getString(R.string.default_theme) : ss;
			entries[i] = hm.get(ss);
			i++;
		}
		
	}
	
	public void onClickTheme(Context context)
	{
		setupTheme();
		int selectedPosition = -1;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		selectedPosition = 0;
		for (int i = 0; i < entries.length; i++)
		{
			if (!APP.GLOBAL().getPreferences().getString(APP.PREF.THEME_NAME.key, "").equals("") && APP.GLOBAL().getPreferences().getString(APP.PREF.THEME_NAME.key, "").equals(entries[i])) selectedPosition = i;
		}
		builder.setSingleChoiceItems(entries, selectedPosition, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				APP.GLOBAL().getEditor().putString(APP.PREF.THEME_NAME.key, entries[which]).commit();
				APP.GLOBAL().getEditor().putString(APP.PREF.THEME_COMPONENT.key, values[which]).commit();
				dialog.dismiss();
				Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				
			}
		});
		builder.show();
		
	}
	
	public void setDayOfWeek(String Vaule)
	{
		
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
		ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(PeriodTrackerConstants.APPLICATION_SETTINGS_DAY_OF_WEEK_KEY, String.valueOf(Vaule), PeriodTrackerObjectLocator.getInstance().getProfileId());
		if (!applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel))
		{
			applicationSettingDBHandler.inseretApplicationSetting(applicationSettingModel);
		}
		
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		
		//initLayout();
	}
	
}
