package com.linchpin.periodtracker.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;

public class PregnancyOptionView extends Activity implements OnClickListener {

	Button pregnancyOn, pregnancyOff, mistakelyOnPregnancy, estimatieddate, displayHomeScreen;
	SimpleDateFormat dateFormat;
	DatePickerDialog estimatedDateDailog;
	Date pregnancyDate, estimatedPregnancyDate, lastPeriodStartDate;
	int days;
	private void applyTheme()
	{
		
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{			
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundColor(R.id.pregnancysettings, "heading_bg");
			t.applyBackgroundColor(R.id.pregnancymodeon, "heading_bg");
			t.applyBackgroundColor(R.id.estimiteddate, "heading_bg");
			t.applyBackgroundColor(R.id.homescreendisplaymessage, "heading_bg");
			t.applyBackgroundColor(R.id.mistakelyonpregnancymode, "heading_bg");
			t.applyBackgroundColor(R.id.pregnancymodeoff, "heading_bg");
			
			t.applyBackgroundDrawable(R.id.pregnancyback, "backbuttonselctor");
			t.applyBackgroundColor(R.id.scrollview, "view_bg");
			
			t.applyTextColor(R.id.personalsettings, "text_color");
			t.applyTextColor(R.id.pregnancymodeon, "heading_fg");
			t.applyTextColor(R.id.estimiteddate, "heading_fg");	
			t.applyTextColor(R.id.homescreendisplaymessage, "heading_fg");
			t.applyTextColor(R.id.mistakelyonpregnancymode, "heading_fg");	
			t.applyTextColor(R.id.pregnancymodeoff, "heading_fg");
			
			
			
			t.applyTextColor(R.id.pregnancysettings, "heading_fg");
			t.applyBackgroundDrawable(R.id.pregnancyinfobutton, "help");
			
			
		}
		/*else
		{
			setContentView(R.layout.settings);
			initLayout();
		}*/
		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pregnancy_option_view);
		applyTheme();
		dateFormat = new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());

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
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
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
		initLayout();
		if ( this.getSharedPreferences("com.linchpin.periodtracker", 0).getBoolean("purchased", false)) {

		} else {
			PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		}

	}

	public void initLayout() {

		findViewById(R.id.pregnancyinfobutton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub

				Intent intent = new Intent(PregnancyOptionView.this, HomeScreenHelp.class);
				if (PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
					intent.putExtra("classname", "pregnancyon");
				else
					intent.putExtra("classname", "pregnancyoff");
				startActivity(intent);

			}
		});
		pregnancyOn = (Button) findViewById(R.id.pregnancymodeon);
		estimatieddate = (Button) findViewById(R.id.estimiteddate);
		mistakelyOnPregnancy = (Button) findViewById(R.id.mistakelyonpregnancymode);
		displayHomeScreen = (Button) findViewById(R.id.homescreendisplaymessage);
		pregnancyOff = (Button) findViewById(R.id.pregnancymodeoff);
		findViewById(R.id.pregnancyback).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
			}
		});

		pregnancyOn.setText(getResources().getString(R.string.turendonpregnancy));
		if (PeriodTrackerObjectLocator.getInstance().getPregnancyMode()) {
			pregnancyOn.setVisibility(View.GONE);
			estimatieddate.setVisibility(View.VISIBLE);
			mistakelyOnPregnancy.setVisibility(View.VISIBLE);
			displayHomeScreen.setVisibility(View.VISIBLE);
			pregnancyOff.setVisibility(View.VISIBLE);
			PeriodLogDBHandler dbHandler = new PeriodLogDBHandler(getBaseContext());
			PeriodLogModel logModel = (PeriodLogModel) dbHandler.getLatestLog();
			PeriodLogModel previouslogmodel = null;
			if (null != logModel) {
				if (logModel.isPregnancy() && null != logModel.getStartDate())
					previouslogmodel = (PeriodLogModel) dbHandler.selectPreviousDateRecord(logModel.getStartDate());
				if (null != previouslogmodel && null != previouslogmodel.getStartDate())
					lastPeriodStartDate = previouslogmodel.getStartDate();
				else
					lastPeriodStartDate = logModel.getStartDate();

				if (PeriodTrackerObjectLocator.getInstance().getEstimatedDeliveryDate().getTime() == PeriodTrackerConstants.NULL_DATE) {

					pregnancyDate = Utility.calculatePregnancyDate(lastPeriodStartDate);

				} else {
					try {
						pregnancyDate = PeriodTrackerObjectLocator.getInstance().getEstimatedDeliveryDate();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				estimatieddate.setText(getResources().getString(R.string.estimateddate) + "\n"
						+ dateFormat.format(pregnancyDate));

				mistakelyOnPregnancy.setText(getResources().getString(R.string.mistakelyonpregnancymode));
				if (!PeriodTrackerObjectLocator.getInstance().PregnancyMessageFormat()
						.contains(getResources().getString(R.string.sincepregnancy))) {

					if (PeriodTrackerObjectLocator.getInstance().getEstimatedDeliveryDate().getTime() == PeriodTrackerConstants.NULL_DATE) {

						if (Utility.dueDaysInPregnancy(lastPeriodStartDate, PeriodTrackerConstants.CURRENT_DATE) > 0) {
							displayHomeScreen.setText(getResources().getString(R.string.setformatforpregnancydates)
									+ "\n"
									+ Utility.dueDaysInPregnancy(lastPeriodStartDate,
											PeriodTrackerConstants.CURRENT_DATE) + " "
									+ getResources().getString(R.string.daysLeftinbirth));
						} else {
							displayHomeScreen.setText(getResources().getString(R.string.setformatforpregnancydates)
									+ "\n"
									+ Utility.lateDaysInPregnancy(lastPeriodStartDate,
											PeriodTrackerConstants.CURRENT_DATE) + " "
									+ getResources().getString(R.string.daysLateinbirth));
						}

					} else {

						if (Utility.dueDaysInPregnancyWhenKnownEstimateddate(pregnancyDate,
								PeriodTrackerConstants.CURRENT_DATE) > 0) {
							displayHomeScreen.setText(getResources().getString(R.string.setformatforpregnancydates)
									+ "\n"
									+ Utility.dueDaysInPregnancyWhenKnownEstimateddate(pregnancyDate,
											PeriodTrackerConstants.CURRENT_DATE) + " "
									+ getResources().getString(R.string.daysLeftinbirth));
						} else {
							displayHomeScreen.setText(getResources().getString(R.string.setformatforpregnancydates)
									+ "\n"
									+ Utility.lateDaysInPregnancyWhenKnownEstimateddate(lastPeriodStartDate,
											PeriodTrackerConstants.CURRENT_DATE) + " "
									+ getResources().getString(R.string.daysLateinbirth));
						}

					}

				} else {
					displayHomeScreen.setText(getResources().getString(R.string.setformatforpregnancydates) + "\n"
							+ Utility.getdaysfromstartofPregnancyIntheformWeaks(lastPeriodStartDate, new Date()));

				}
				pregnancyOff.setText(getResources().getString(R.string.nolongerpregnant));

			}
		} else {
			pregnancyOn.setVisibility(View.VISIBLE);
			estimatieddate.setVisibility(View.GONE);
			mistakelyOnPregnancy.setVisibility(View.GONE);
			displayHomeScreen.setVisibility(View.GONE);
			pregnancyOff.setVisibility(View.GONE);

		}
		pregnancyOn.setOnClickListener(this);
		estimatieddate.setOnClickListener(this);
		mistakelyOnPregnancy.setOnClickListener(this);
		displayHomeScreen.setOnClickListener(this);
		pregnancyOff.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Calendar calendar = GregorianCalendar.getInstance();

		if (v == pregnancyOn) {

			PeriodLogDBHandler periodLogDBHandler = new PeriodLogDBHandler(getApplicationContext());
			PeriodLogModel logModel = (PeriodLogModel) periodLogDBHandler.getLatestLog();
			if (null != logModel.getStartDate() && null != logModel.getEndDate()
					&& logModel.getEndDate().getTime() == (PeriodTrackerConstants.NULL_DATE)) {
				showMessageOnDelevieryEndDate();

			} else {

				AlertDialog.Builder builder = new AlertDialog.Builder(PregnancyOptionView.this);
				builder.setMessage(getResources().getString(R.string.pregnancymodeon));
				builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						setPregnancyMode("true");

					}
				});
				builder.show();
			}

		} else if (v == pregnancyOff) {

			AlertDialog.Builder builder = new AlertDialog.Builder(PregnancyOptionView.this);
			builder.setTitle(R.string.pregnancymode);
			builder.setMessage(getResources().getString(R.string.pregnancymodeoff));
			builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					setPregnancyMode("false");

				}
			});
			builder.show();
		} else if (v == estimatieddate) {
			calendar.setTime(pregnancyDate);
			estimatedDateDailog = new DatePickerDialog(PregnancyOptionView.this, estimatedDateListener,
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
			estimatedDateDailog.setTitle(R.string.setedd);
			estimatedDateDailog.show();
			if (Utility.checkAndroidApiVersion()) {
				estimatedDateDailog.getDatePicker().setMinDate(new Date().getTime());
			}
		} else if (v == displayHomeScreen) {
			onDisplayHomeScreen();
		} else if (v == mistakelyOnPregnancy) {
			onClickMistakelyOnPregnancyMode();
		}
	}

	public void onDisplayHomeScreen() {

		int selectedPosition = 0;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String days = null;
		/*
		 * if
		 * ((Utility.dueDaysInPregnancyWhenKnownEstimateddate(lastPeriodStartDate
		 * , new Date())) < 0) { days =
		 * Utility.lateDaysInPregnancy(lastPeriodStartDate, new Date()) +
		 * getResources().getString(R.string.daysLateinbirth); } else { days =
		 * Utility.dueDaysInPregnancy(lastPeriodStartDate, new Date()) +
		 * getResources().getString(R.string.daystobaby);
		 * 
		 * }
		 */

		if (PeriodTrackerObjectLocator.getInstance().getEstimatedDeliveryDate().getTime() == PeriodTrackerConstants.NULL_DATE) {

			if (Utility.dueDaysInPregnancy(lastPeriodStartDate, PeriodTrackerConstants.CURRENT_DATE) > 0) {

				days = Utility.dueDaysInPregnancy(lastPeriodStartDate, PeriodTrackerConstants.CURRENT_DATE) + " "
						+ getResources().getString(R.string.daysLeftinbirth);
			} else {
				days = Utility.lateDaysInPregnancy(lastPeriodStartDate, PeriodTrackerConstants.CURRENT_DATE) + " "
						+ getResources().getString(R.string.daysLateinbirth);
			}

		} else {

			if (Utility.dueDaysInPregnancyWhenKnownEstimateddate(pregnancyDate, PeriodTrackerConstants.CURRENT_DATE) > 0) {
				days = Utility.dueDaysInPregnancyWhenKnownEstimateddate(pregnancyDate,
						PeriodTrackerConstants.CURRENT_DATE) + " " + getResources().getString(R.string.daysLeftinbirth);
			} else {
				days = Utility.lateDaysInPregnancyWhenKnownEstimateddate(lastPeriodStartDate,
						PeriodTrackerConstants.CURRENT_DATE) + " " + getResources().getString(R.string.daysLateinbirth);
			}

		}

		final String[] items = {
				days,
				Utility.getdaysfromstartofPregnancyIntheformWeaks(lastPeriodStartDate, new Date()) + " "
						+ getResources().getString(R.string.sincepregnancy) };
		if (String.valueOf(PeriodTrackerObjectLocator.getInstance().PregnancyMessageFormat()).contains(
				getResources().getString(R.string.sincepregnancy))) {
			selectedPosition = 1;
		} else {
			selectedPosition = 0;
		}

		builder.setTitle(getResources().getString(R.string.selectpregnancymode));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				if (which == 0) {
					setMessageFormat(items[0]);
				} else {
					setMessageFormat(items[1]);
				}

				dialog.dismiss();

			}
		});
		builder.show();

	}

	public void onClickMistakelyOnPregnancyMode() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.trunoff));
		builder.setMessage(getResources().getString(R.string.trunoffmistakely));
		builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				PeriodLogDBHandler dbHandler = new PeriodLogDBHandler(getBaseContext());
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(
						getBaseContext());
				PeriodLogModel logModel = (PeriodLogModel) dbHandler.getLatestLog();
				if (null != logModel && logModel.isPregnancy()) {
					dbHandler.deletePeriodRecord(logModel);
				}
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(
						PeriodTrackerConstants.APPLICATION_SETTING_PREGNANT_MODE, "false", PeriodTrackerObjectLocator
								.getInstance().getProfileId());
				if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel)) {
					PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();

				}
				initLayout();
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.cancel), null);
		builder.show();
	}

	public void onClickSelectPregnancyMode() {

		int selectedPosition = 1;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items = { "On", "Off" };
		if (String.valueOf(PeriodTrackerObjectLocator.getInstance().getPregnancyMode()).equals("true")) {
			selectedPosition = 0;
		} else {
			selectedPosition = 1;
		}

		builder.setTitle(getResources().getString(R.string.selectpregnancymode));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				if (which == 0) {
					setPregnancyMode("true");
				} else {
					setPregnancyMode("false");
				}

				dialog.dismiss();

			}
		});
		builder.show();

	}

	public void setPregnancyMode(String Vaule) {
		try {
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			PeriodLogDBHandler periodLogDBHandler = new PeriodLogDBHandler(getApplicationContext());
			if (Vaule.equals("true")) {
				PeriodLogModel logModel = (PeriodLogModel) periodLogDBHandler.getLatestLog();
				if (null != logModel && null != logModel.getStartDate()) {
					if (logModel.getEndDate().getTime() == (PeriodTrackerConstants.NULL_DATE)) {

						showMessageOnDelevieryEndDate();

					} else if (logModel.isPregnancy()) {

						periodLogDBHandler.deletePeriodRecord(logModel);

					}

				} else {

					logModel = new PeriodLogModel(0, PeriodTrackerObjectLocator.getInstance().getProfileId(),
							Utility.setHourMinuteSecondZero(new Date()), null, 0, 0, false, true);
					/*
					 * if (Utility.addDays( logModel.getStartDate(),
					 * (PeriodTrackerObjectLocator.getInstance()
					 * .getCurrentPeriodLength()) - 1).before(
					 * PeriodTrackerConstants.CURRENT_DATE))
					 * logModel.setEndDate(Utility.addDays(logModel
					 * .getStartDate(), (PeriodTrackerObjectLocator
					 * .getInstance().getCurrentPeriodLength()) - 1)); else
					 */logModel.setEndDate(PeriodTrackerConstants.CURRENT_DATE);
					periodLogDBHandler.addPeriodLog(logModel);
				}
				if (logModel.getEndDate().getTime() != (PeriodTrackerConstants.NULL_DATE)) {
					logModel = new PeriodLogModel(0, PeriodTrackerObjectLocator.getInstance().getProfileId(),
							new Date(), null, 0, 0, true, false);
					periodLogDBHandler.addPeriodLog(logModel);

					ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(
							PeriodTrackerConstants.APPLICATION_SETTING_PREGNANT_MODE, Vaule, PeriodTrackerObjectLocator
									.getInstance().getProfileId());
					if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel)) {
						PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
					}
				}
				APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, true).commit();
			} else {
				PeriodLogModel logModel = (PeriodLogModel) periodLogDBHandler.getLatestLog();
				if (logModel.getEndDate().getTime() == PeriodTrackerConstants.NULL_DATE && logModel.isPregnancy()) {
					if (logModel.getStartDate().before(PeriodTrackerConstants.CURRENT_DATE)
							|| Utility.setHourMinuteSecondZero(logModel.getStartDate()).equals(
									PeriodTrackerConstants.CURRENT_DATE)) {
						logModel.setEndDate(PeriodTrackerConstants.CURRENT_DATE);
					} else {

						showMessageOnInvalidEndDate();
					}

				}
				periodLogDBHandler.updatePeriodLog(logModel);
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(
						PeriodTrackerConstants.APPLICATION_SETTING_PREGNANT_MODE, Vaule, PeriodTrackerObjectLocator
								.getInstance().getProfileId());
				if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel)) {
					applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(
							PeriodTrackerConstants.APPLICATION_SETTINGS_ESTIMATED_DATE_VALUE_KEY, null,
							PeriodTrackerObjectLocator.getInstance().getProfileId()));

					PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();

				}
			}
			initLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setMessageFormat(String Value) {
		ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(
				getApplicationContext());
		ApplicationSettingModel applicationSettingModel;
		if (Value.contains(getResources().getString(R.string.sincepregnancy))) {
			applicationSettingModel = new ApplicationSettingModel(
					PeriodTrackerConstants.APPLICATION_SETTINGS_PREGNANCY_MESSAGE_FORMAT_KEY, getResources().getString(
							R.string.sincepregnancy), PeriodTrackerObjectLocator.getInstance().getProfileId());
		} else {
			applicationSettingModel = new ApplicationSettingModel(
					PeriodTrackerConstants.APPLICATION_SETTINGS_PREGNANCY_MESSAGE_FORMAT_KEY, getResources().getString(
							R.string.daystobaby), PeriodTrackerObjectLocator.getInstance().getProfileId());

		}
		if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel)) {
			PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();

		}
		initLayout();
	}

	public DatePickerDialog.OnDateSetListener estimatedDateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			estimatedPregnancyDate = Utility.setHourMinuteSecondZero(Utility.createDate(year, monthOfYear, dayOfMonth));

			if (estimatedPregnancyDate != null && !estimatedPregnancyDate.before(PeriodTrackerConstants.CURRENT_DATE)) {
				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(
						getBaseContext());
				applicationSettingDBHandler.inseretApplicationSetting(new ApplicationSettingModel(
						PeriodTrackerConstants.APPLICATION_SETTINGS_ESTIMATED_DATE_VALUE_KEY, dateFormat
								.format(estimatedPregnancyDate), PeriodTrackerObjectLocator.getInstance()
								.getProfileId()));
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				initLayout();
			} else {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.invaliddatetitle),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	public void showMessageOnDelevieryEndDate() {

		AlertDialog.Builder builder = new AlertDialog.Builder(PregnancyOptionView.this);
		builder.setTitle(getResources().getString(R.string.end_date));
		builder.setMessage(getResources().getString(R.string.enddatebeforepregnancy));

		builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PregnancyOptionView.this, PeriodLogPagerView.class);
				startActivity(intent);
			}
		});

		builder.setNegativeButton(getResources().getString(R.string.cancel), null);
		builder.show();

	}

	public void showMessageOnInvalidEndDate() {

		AlertDialog.Builder builder = new AlertDialog.Builder(PregnancyOptionView.this);
		builder.setTitle(getResources().getString(R.string.end_date));
		builder.setMessage(getResources().getString(R.string.enddategreaterthanstart));
		builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		builder.setNegativeButton(getResources().getString(R.string.cancel), null);
		builder.show();

	}
}