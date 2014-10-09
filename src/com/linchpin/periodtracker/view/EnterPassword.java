package com.linchpin.periodtracker.view;

import java.lang.ref.SoftReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.receiver.MPTAppWidgetProvider;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.CustomAlertDialog;
import com.linchpin.periodtracker.utlity.CustomAlertDialog.onButoinClick;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;

public class EnterPassword extends Activity
{
	
	EditText		checkPassword;
	int				frogotcount	= 0;
	Theme           t;
	static boolean	backpressed	= false;
	
	private void applyTheme()
	{
		t = Theme.getCurrentTheme(this, findViewById(R.id.content));
		if (t != null)
		{
			t.applyBackgroundColor(R.id.mainlayout, "view_bg");
	    	t.applyTextColor(R.id.enterpasswordtext, "text_color");
			t.applyTextColor(R.id.forgotpassword, "co_btn_fg");
			t.applyTextColor(R.id.checkpassword, "text_color");
			t.applyBackgroundDrawable(R.id.forgotpassword, "mpt_button_sltr");
			t.applyTextColor(R.id.checkpasswordbutton, "co_btn_fg");
			t.applyBackgroundDrawable(R.id.checkpasswordbutton, "mpt_button_sltr");
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (getIntent().getBooleanExtra("fromwidget", false) && getIntent().getBooleanExtra("lock", true)) update(getIntent().getBooleanExtra("lock", true));
		else if (PeriodTrackerObjectLocator.getInstance().getPasswordProtection().trim().equals("")) update(getIntent().getBooleanExtra("lock", true));
		else
		{
			setContentView(R.layout.enter_password);
			initLayout();
			Button buttonOk = (Button) findViewById(R.id.checkpasswordbutton);
			buttonOk.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					if (checkPassword.getText().toString().equals(PeriodTrackerObjectLocator.getInstance().getPasswordProtection()))
					{
						if (!getIntent().getBooleanExtra("fromwidget", false))
						{
							InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(checkPassword.getWindowToken(), 0);
						//	Intent intent = new Intent(getApplicationContext(), HomeScreenView.class);
						//	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							//startActivity(intent);
							setResult(Activity.RESULT_OK);
							finish();
							;
							APP.GLOBAL().exicuteRIOAnim(EnterPassword.this);
							
						}
						else
						{
							update(getIntent().getBooleanExtra("lock", true));
						}
					}
					else
					{
						Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrongpasscode), Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
						toast.show();
					}
				}
			});
			
			findViewById(R.id.forgotpassword).setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getApplicationContext());
					ApplicationSettingModel model = (ApplicationSettingModel) applicationSettingDBHandler.getParticularApplicationSetting(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_QUESTION, PeriodTrackerObjectLocator.getInstance().getProfileId());
					
					ApplicationSettingModel model2 = (ApplicationSettingModel) applicationSettingDBHandler.getParticularApplicationSetting(PeriodTrackerConstants.APPLICATION_SETTING_SECURITY_ANSWER, PeriodTrackerObjectLocator.getInstance().getProfileId());
					
					displayAlert(model.getValue(), model2.getValue());
					
				}
			});
			
		}
		applyTheme();
		
	}
	
	private void update(boolean t)
	{
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.WIDGET_LOCK.key, t).commit();
		Intent intent = new Intent(EnterPassword.this, MPTAppWidgetProvider.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		int[] ids = getIntent().getIntArrayExtra("ids");
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
		sendBroadcast(intent);
		finish();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		initLayout();
		EasyTracker.getInstance(this).activityStart(this);
		
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
		
	}
	
	protected void displayAlert(final String question, final String answer)
	{
		
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper(EnterPassword.this, android.R.style.Theme_DeviceDefault_Light);
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from(this).inflate(R.layout.forgot_password, null);
		builder.setView(view);
		final AlertDialog alertDialog = builder.create();
		EditText editText = (EditText) view.findViewById(R.id.forgotpasswordsecurity);
		final EditText editText2 = (EditText) view.findViewById(R.id.fortgotpasswordanswer);
		editText.setText(question);
		final TextView textView = (TextView) view.findViewById(R.id.apppassword);
		editText.setTextColor(getResources().getColor(R.color.datecolor));
		Button ok = (Button) view.findViewById(R.id.popok);
		applyThemeAlert(view);
		ok.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				frogotcount++;
				
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
				
				ApplicationSettingModel applicationSettingModel = (ApplicationSettingModel) applicationSettingDBHandler.getParticularApplicationSetting(PeriodTrackerConstants.APLLICATION_SETTING_EMAIL_KEY, PeriodTrackerObjectLocator.getInstance().getProfileId());
				
				if (editText2.getText().toString().equals(answer))
				{
					
					textView.setText(getResources().getString(R.string.passcode) + " : " + PeriodTrackerObjectLocator.getInstance().getPasswordProtection());
					textView.setVisibility(view.VISIBLE);
					CustomAlertDialog.Dialog(EnterPassword.this).getAlertDialog("Passcode", getResources().getString(R.string.passcode) + " : " + PeriodTrackerObjectLocator.getInstance().getPasswordProtection(), true, getString(R.string.ok), false, "", true, new onButoinClick()
					{
						
						@Override
						public void onClickPositive(DialogInterface dialog, int which)
						{
							dialog.dismiss();
						//	Intent intent = new Intent(EnterPassword.this, HomeScreenView.class);
							//setResult(Activity.RESULT_OK);
							//startActivity(intent);
							alertDialog.dismiss();
							EnterPassword.this.finish();
							
						}
						
						@Override
						public void onClickNegative(DialogInterface dialog, int which)
						{
							
						}
					});
					
					/*if (Utility.isNetworkConnected(getBaseContext())) {

						Utility.sendMail(EnterPassword.this, applicationSettingModel.getValue(), "forgot_password",
								PeriodTrackerObjectLocator.getInstance().getPasswordProtection(), "", "");

					} else {
						Toast toast1 = Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.internetconnectioncheck), Toast.LENGTH_SHORT);
						toast1.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
						toast1.show();

					}*/
					
				}
				else
				{
					Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.wronganswer), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
					
					if (frogotcount >= 3)
					{
						if (Utility.isNetworkConnected(getBaseContext()))
						{
							
							Utility.sendMail(EnterPassword.this, applicationSettingModel.getValue(), "forgot_password", PeriodTrackerObjectLocator.getInstance().getPasswordProtection(), question, answer);
							frogotcount = 0;
						}
						else
						{
							Toast toast1 = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internetconnectioncheck), Toast.LENGTH_SHORT);
							toast1.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
							toast1.show();
							
						}
					}
				}
				
			}
		});
		
		Button cancel = (Button) view.findViewById(R.id.popcancel);
		cancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}
	
	private void applyThemeAlert(View view)
	{
		// TODO Auto-generated method stub
		t=Theme.getCurrentTheme(getApplicationContext(),view);
		if(t!=null)
		{
			t.applyBackgroundColor(R.id.popupforgotpassword, "view_bg");
			t.applyTextColor(R.id.forgotpasswordtiltle, "text_color");
			t.applyTextColor(R.id.forgotpasswordsecurity, "text_color");
			t.applyTextColor(R.id.fortgotpasswordanswer, "text_color");
			t.applyTextColor(R.id.text, "text_color");
			t.applyTextColor(R.id.text3, "text_color");
	    	t.applyTextColor(R.id.popok, "co_btn_fg");
			t.applyBackgroundDrawable(R.id.popok, "mpt_button_sltr");
			t.applyTextColor(R.id.popcancel, "co_btn_fg");
			t.applyBackgroundDrawable(R.id.popcancel, "mpt_button_sltr");
		}
		
	}
	public void initLayout()
	{
		checkPassword = (EditText) findViewById(R.id.checkpassword);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	/*
	 * @Override public void onBackPressed() { // TODO Auto-generated method
	 * stub super.onBackPressed();
	 * 
	 * }
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		backpressed = true;
		finish();
		
	}
	
}
