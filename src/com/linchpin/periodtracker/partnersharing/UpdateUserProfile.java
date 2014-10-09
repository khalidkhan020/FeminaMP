package com.linchpin.periodtracker.partnersharing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.AdapterForCountry;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.view.DatePickerDialog;
import com.linchpin.periodtracker.view.DatePickerDialog.OnDateSetListener;
import com.linchpin.periodtracker.widget.MPTProgressDialog;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.User.Profile;
import com.shephertz.app42.paas.sdk.android.user.User.UserGender;
import com.shephertz.app42.paas.sdk.android.user.UserResponseBuilder;

public class UpdateUserProfile extends Activity implements OnClickListener
{
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart()
	{
		EasyTracker.getInstance(this).activityStart(this);
		// TODO Auto-generated method stub
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop()
	{
		EasyTracker.getInstance(this).activityStop(this);
		// TODO Auto-generated method stub
		super.onStop();
	}

	EditText					fName, lName/*, phone*/;
	TextView					dob;
	Spinner						countrySPn;
	RadioGroup					sex;
	RadioButton					male, female;
	Button						update;
	SimpleDateFormat			format;//	= new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
	int							defaultciuntry;
	Date						dateOfBorth;
	Handler						handler	= new Handler();
	private MPTProgressDialog	myDialog;
	
	private void applyTheme()
	{
		Theme t = Theme.getCurrentTheme(this, findViewById(R.id.content));
		if (t != null)
		{
			t.applyBackgroundColor(R.id.content, "newsettingsbackground");
			t.applyTextColor(R.id.header, "text_color");
			t.applyTextColor(R.id.male, "text_color");
			t.applyTextColor(R.id.female, "text_color");
			t.applyTextColor(R.id.fname, "text_color");
			t.applyTextColor(R.id.lname, "text_color");
			t.applyTextColor(R.id.dob, "text_color");
		//	t.applyTextColor(R.id.text1, "text_color");
			t.applyButtonCheckBox(R.id.male, "checkboxselector_partner");
			t.applyButtonCheckBox(R.id.female, "checkboxselector_partner");
			t.applyBackgroundDrawable(R.id.update, "mpt_button_sltr");
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_user_profile);
		Calendar calendar=Calendar.getInstance();
		calendar.set(1990, Calendar.JANUARY, 1);
		format	= new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
		dateOfBorth = calendar.getTime();
		
		fName = (EditText) findViewById(R.id.fname);
		lName = (EditText) findViewById(R.id.lname);
		//phone = (EditText) findViewById(R.id.phone);
		dob = (TextView) findViewById(R.id.dob);
		update = (Button) findViewById(R.id.update);
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);
		update.setOnClickListener(this);
		dob.setOnClickListener(this);
	
		Locale[] locale = Locale.getAvailableLocales();
		ArrayList<String> countries = new ArrayList<String>();
		
		String country;
		for (Locale loc : locale)
		{
			country = loc.getDisplayCountry();
			if (country.length() > 0 && !countries.contains(country))
			{
				countries.add(country);
			}
		}
		Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
		countries.add(0,"Select Country");
		AdapterForCountry adapter = new AdapterForCountry(this, R.layout.country_spinner, countries);
		countrySPn = (Spinner) findViewById(R.id.country);
		
		countrySPn.setAdapter(adapter);
		applyTheme();
		if (null != APP.currentUser.getProfile())
		{
			fName.setText(APP.currentUser.getProfile().firstName == null ? "" : APP.currentUser.getProfile().firstName);
			lName.setText(APP.currentUser.getProfile().lastName == null ? "" : APP.currentUser.getProfile().lastName);
		//	phone.setText(APP.currentUser.getProfile().mobile == null ? "" : APP.currentUser.getProfile().mobile);
			dateOfBorth = APP.currentUser.getProfile().dateOfBirth == null ? dateOfBorth : APP.currentUser.getProfile().dateOfBirth;
			 if(APP.currentUser.getProfile().dateOfBirth != null)
			dob.setText(format.format(APP.currentUser.getProfile().dateOfBirth));
			if ("Female".equals(APP.currentUser.getProfile().getSex()))
			{
				male.setChecked(false);
				female.setChecked(true);
			}
			else
			{
				female.setChecked(false);
				male.setChecked(true);
			}
			if (APP.currentUser.getProfile().country != null) defaultciuntry = APP.currentUser.getProfile().country.equals("") ? adapter.getPosition(Locale.getDefault().getDisplayCountry()) : adapter.getPosition(APP.currentUser.getProfile().country);
		}
		countrySPn.setSelection(defaultciuntry, true);
		myDialog = new MPTProgressDialog(this, R.drawable.spinner);
		
		myDialog.setCancelable(false);
	}
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.dob)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateOfBorth);
			DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateUserProfile.this, new OnDateSetListener()
			{
				
				@Override
				public void onDateSet(DatePicker view, int id, int year, int month, int day)
				{
					if (year != 12345 && month != 12345)
					{
						Calendar calendar = Calendar.getInstance();
						calendar.set(year, month, day);
						calendar.getTime();
						
						String dateFormat = format.format(calendar.getTime());
						dob.setText(dateFormat);
					}
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			datePickerDialog.show();
		}
		else if (id == R.id.update)
		{
			myDialog.show();
			Profile profile = APP.currentUser.getProfile();
			profile.setFirstName(fName.getText().toString());
			profile.setLastName(lName.getText().toString());
			//	profile.setMobile(phone.getText().toString());
			profile.setSex(male.isChecked() ? UserGender.MALE : UserGender.FEMALE);
			profile.setCountry(countrySPn.getSelectedItem().toString());
			try
			{
				profile.setDateOfBirth(format.parse(dob.getText().toString()));
			}
			catch (ParseException e)
			{
			}
			APP.currentUser.setProfile(profile);
			APP.getUserService().createOrUpdateProfile(APP.currentUser, new App42CallBack()
			{
				
				@Override
				public void onException(Exception arg0)
				{
					handler.post(new Runnable()
					{
						
						@Override
						public void run()
						{
							if (myDialog.isShowing()) myDialog.dismiss();
							Toast.makeText(UpdateUserProfile.this, getString(R.string.update_fail), 1000).show();
							
						}
					});
					
				}
				
				@Override
				public void onSuccess(Object arg0)
				{
					
					APP.currentUser=(User) arg0;
					APP.GLOBAL().getEditor().putString(APP.GOAL.SAVE_CURRENTUSER_INFO.key,(arg0.toString())).commit();
					handler.post(new Runnable()
					{
						
						@Override
						public void run()
						{
							if (myDialog.isShowing()) myDialog.dismiss();
							Toast.makeText(UpdateUserProfile.this, getString(R.string.update_successfull), 1000).show();
							setResult(Activity.RESULT_OK);
							if(APP.currentUser.getProfile().getSex().equals("Male"))
							{
							APP.GLOBAL().getEditor().putBoolean(APP.GENDER.MALE.key, true).commit();
							APP.GLOBAL().getEditor().putBoolean(APP.GENDER.FEMALE.key, false).commit();
							}
							else
							{
							APP.GLOBAL().getEditor().putBoolean(APP.GENDER.FEMALE.key, true).commit();
							APP.GLOBAL().getEditor().putBoolean(APP.GENDER.MALE.key, false).commit();
							}
							
							UpdateUserProfile.this.finish();
							
						}
					});
				
				}
			});
		}
		
	}
	
}
