package com.linchpin.periodtracker.view;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.SettingsAdaptor;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;

public class RegionalSettingsView extends Activity {

	ListView listView;
//	List<String> list;
	private void applyTheme()
	{
		
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{			
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundColor(R.id.textregionalsettings, "heading_bg");
			t.applyBackgroundDrawable(R.id.regionalsettingsback, "backbuttonselctor");
	
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			
			t.applyTextColor(R.id.text1, "text_color");
			t.applyTextColor(R.id.textregionalsettings, "heading_fg");
			
		}}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regional_settings_view); applyTheme();
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

	}	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initLayout();

		/*
		 * findViewById(R.id.regionalsettinginfobutton).setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * Intent intent = new Intent(RegionalSettingsView.this,
		 * HomeSceenHelp.class); intent.putExtra("classname",
		 * "regionalsettings"); startActivity(intent); } });
		 */
		findViewById(R.id.regionalsettingsback).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
			}
		});
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

		listView = (ListView) findViewById(R.id.regionalsettingslistview);
		/*list = new ArrayList<String>();
		list.add(getResources().getString(R.string.hieghtunit));
		list.add(getResources().getString(R.string.weightunit));
		list.add(getResources().getString(R.string.bodytempratureunit));
		list.add(getResources().getString(R.string.changelanugae));
		list.add(getResources().getString(R.string.changedateformat));*/

		listView.setAdapter(new SettingsAdaptor(getApplicationContext(), getResources().getStringArray(R.array.regional_settings)));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
				TextView textView = (TextView) v.findViewById(R.id.settinglistitem);
				if (textView.getText().toString().trim().equals(getResources().getString(R.string.weightunit))) {
					onClickSelectWeightUnit();
				} else if (textView.getText().toString().trim().equals(getResources().getString(R.string.bodytempratureunit))) {
					onClickSelectTempUnit();
				} else if (textView.getText().toString().trim().equals(getResources().getString(R.string.hieghtunit))) {
					onClickSelectHieghtUnit();
				} else if (textView.getText().toString().trim().equals(getResources().getString(R.string.changedateformat))) {
					OnClickDateFormat();
				} else if (textView.getText().toString().trim().equals(getResources().getString(R.string.changelanugae))) {
					onClickOfChangeApplicationLanuage();
					//Tips.viewVisible(RegionalSettingsView.this, APP.TipsPath.Language.id);
				}
			}
		});
	}

	private void onClickOfChangeApplicationLanuage() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.changedefaultLanuage));
		final String[] items = { // getResources().getString(R.string.croatian),
		getResources().getString(R.string.dutch), getResources().getString(R.string.english),
				getResources().getString(R.string.french), getResources().getString(R.string.german),
				getResources().getString(R.string.italian),
				// getResources().getString(R.string.japanese),
				getResources().getString(R.string.portuguese), getResources().getString(R.string.spanish) };

		int selectedPosition = Arrays.asList(items).indexOf(
				Utility.getLanguageFromLoacle(
						String.valueOf(PeriodTrackerObjectLocator.getInstance().getAppLanguage()),
						getApplicationContext()));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Locale locale = null;
				String vaString = Utility.AppLanguageLocale(items[which], getApplicationContext());
				locale = new Locale(vaString);

				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(
						getApplicationContext());
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(
						PeriodTrackerConstants.APPLICATION_SETTINGS_APP_LANGUAGE_KEY, vaString,
						PeriodTrackerObjectLocator.getInstance().getProfileId());
				applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();

				Resources res = getApplicationContext().getResources();
				// Change locale settings in the app.
				DisplayMetrics dm = res.getDisplayMetrics();
				android.content.res.Configuration conf = res.getConfiguration();
				conf.locale = locale;
				res.updateConfiguration(conf, dm);
				dialog.dismiss();
				restartActivity();

			}
		});
		builder.show();
	}

	public void OnClickDateFormat() {

		final AlertDialog.Builder builder;

		builder = new AlertDialog.Builder(this);

		builder.setTitle(getResources().getString(R.string.selectdateformat));

		final String[] patternItems = { "MM/dd/yyyy", "dd/MM/yyyy", "yyyy/dd/MM",

		"MM dd yyyy", "yyyy dd MM", "yyyy MM dd",

		"dd MMM, yyyy", "MMM dd, yyyy", "yyyy MMM dd" };

		final String[] items = { new SimpleDateFormat("MM/dd/yyyy").format(new Date()),
				new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
				new SimpleDateFormat("yyyy/dd/MM").format(new Date()),

				new SimpleDateFormat("MM dd yyyy").format(new Date()),
				new SimpleDateFormat("yyyy dd MM").format(new Date()),
				new SimpleDateFormat("yyyy MM dd").format(new Date()),

				new SimpleDateFormat("dd MMM, yyyy").format(new Date()),
				new SimpleDateFormat("MMM dd, yyyy").format(new Date()),
				new SimpleDateFormat("yyyy MMM dd").format(new Date()) };

		int selectedPosition = Arrays.asList(items).indexOf(
				new SimpleDateFormat(String.valueOf(PeriodTrackerObjectLocator.getInstance().getDateFormat()))
						.format(new Date()));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(
						getApplicationContext());
				ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(
						PeriodTrackerConstants.APPLICATION_SETTINGS_DATE_FORMAT_KEY, patternItems[which],
						PeriodTrackerObjectLocator.getInstance().getProfileId());
				applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel);
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
				dialog.dismiss();
				initLayout();
			}
		});
		builder.show();

	}

	private void restartActivity() {

		Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		RegionalSettingsView.this.finish();
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}

	public void onClickSelectWeightUnit() {

		int selectedPosition;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items = { getResources().getString(R.string.KG), getResources().getString(R.string.lb) };
		if (String.valueOf(PeriodTrackerObjectLocator.getInstance().getWeighUnit()).equalsIgnoreCase(
				getResources().getString(R.string.KG))) {
			selectedPosition = 0;
		} else {
			selectedPosition = 1;
		}

		builder.setTitle(getResources().getString(R.string.changedefaultlweightunit));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				if (which == 0) {
					setWeightUnit(getResources().getString(R.string.KG));
				} else {
					setWeightUnit(getResources().getString(R.string.lb));
				}

				dialog.dismiss();

			}
		});
		builder.show();

	}

	public void onClickSelectHieghtUnit() {

		int selectedPosition;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items = { getResources().getString(R.string.CM), getResources().getString(R.string.inches) };
		if (String.valueOf(PeriodTrackerObjectLocator.getInstance().getHeightUnit()).equalsIgnoreCase(
				getResources().getString(R.string.CM))) {
			selectedPosition = 0;
		} else {
			selectedPosition = 1;
		}

		builder.setTitle(getResources().getString(R.string.changedefaultlheightunit));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				if (which == 0) {
					setHieghUnit(getResources().getString(R.string.CM));
				} else {
					setHieghUnit(getResources().getString(R.string.inches));
				}

				dialog.dismiss();

			}
		});
		builder.show();

	}

	public void onClickSelectTempUnit() {

		int selectedPosition;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] items = { getResources().getString(R.string.celsius), getResources().getString(R.string.Fehr) };
		if (String.valueOf(PeriodTrackerObjectLocator.getInstance().getTempUnit()).equalsIgnoreCase(
				getResources().getString(R.string.celsius))) {
			selectedPosition = 0;
		} else {
			selectedPosition = 1;
		}

		builder.setTitle(getResources().getString(R.string.bodytempratureunit));
		builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				if (which == 0) {
					setTempUnit(getResources().getString(R.string.celsius));
				} else {
					setTempUnit(getResources().getString(R.string.Fehr));
				}

				dialog.dismiss();

			}
		});
		builder.show();

	}

	public void setWeightUnit(String Value) {
		try {
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(
					PeriodTrackerConstants.APPLICATION_SETTINGS_WEIGH_UNIT_KEY, Value, PeriodTrackerObjectLocator
							.getInstance().getProfileId());

			if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel)) {
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();

			}
			initLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTempUnit(String Value) {

		try {
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(
					PeriodTrackerConstants.APPLICATION_SETTINGS_TEMP_UNIT_KEY, Value, PeriodTrackerObjectLocator
							.getInstance().getProfileId());

			if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel)) {
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();

			}
			initLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setHieghUnit(String value) {

		try {
			ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getBaseContext());
			ApplicationSettingModel applicationSettingModel = new ApplicationSettingModel(
					PeriodTrackerConstants.APPLICATION_SETTINGS_HEIGH_UNIT_KEY, value, PeriodTrackerObjectLocator
							.getInstance().getProfileId());

			if (applicationSettingDBHandler.upadteApplicationSetting(applicationSettingModel)) {
				PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();

			}
			initLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
