package com.linchpin.periodtracker.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.CustomArrayAdaptor;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.RestoreAndBackUp;

public class RestoreAndBackupActivity extends Activity implements OnClickListener
{
	
	//ListView				restoreAndBackUpList;
	public static Dialog	dialogInterface;
	private Button			signin, signup, restore, localbackup;
	
	private void applyTheme()
	{
		
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundColor(R.id.textrestoresetting, "heading_bg");
			t.applyBackgroundDrawable(R.id.restoreandbackupback, "backbuttonselctor");
			t.applyBackgroundDrawable(R.id.restorebackupinfobutton, "help");
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			
			t.applyTextColor(R.id.restoreandbackup, "text_color");
			t.applyTextColor(R.id.textrestoresetting, "heading_fg");
			
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restoreandbackup);
		applyTheme();
		findViewById(R.id.restoreandbackupback).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
			}
		});
		
		findViewById(R.id.restorebackupinfobutton).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(RestoreAndBackupActivity.this, HomeScreenHelp.class);
				intent.putExtra("classname", "restoreandbackup");
				startActivity(intent);
			}
		});
		
		signin = (Button) findViewById(R.id.signin);
		signup = (Button) findViewById(R.id.signup);
		restore = (Button) findViewById(R.id.restore_data);
		localbackup = (Button) findViewById(R.id.localBackup);
		signin.setOnClickListener(this);
		signup.setOnClickListener(this);
		restore.setOnClickListener(this);
		localbackup.setOnClickListener(this);
		
		//		restoreAndBackUpList = (ListView) findViewById(R.id.restoreandbackuplist);
		//		restoreAndBackUpList.setAdapter(new SettingsAdaptor(this, getResources().getStringArray(R.array.restore_settings)));
		//		
		//		restoreAndBackUpList.setOnItemClickListener(new OnItemClickListener()
		//		{
		//			
		//			@Override
		//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		//			{				
		//				if (arg2 == 0)
		//				{					
		//					final List<File> arrayList = walkdir(Environment.getExternalStorageDirectory());
		//					CharSequence[] items = new String[arrayList.size()];
		//					for (int i = 0; i < arrayList.size(); i++)
		//					{						
		//						items[i] = arrayList.get(i).getName();
		//					}
		//					
		//					ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(RestoreAndBackupActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		//					AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
		//					final AlertDialog alertDialog = builder.create();
		//					
		//					builder.setTitle(getResources().getString(R.string.selectfiletobackup));
		//					
		//					final CustomArrayAdaptor adaptor = new CustomArrayAdaptor(getApplicationContext(), arrayList, dialogInterface);
		//					builder.setSingleChoiceItems(adaptor, -1, new DialogInterface.OnClickListener()
		//					{
		//						@Override
		//						public void onClick(DialogInterface dialog, int which)
		//						{
		//							((RadioButton) adaptor.getView(which, adaptor.row, adaptor.viewGroup).findViewById(R.id.radio)).setChecked(true);
		//							dialogInterface = (Dialog) dialog;							
		//							((Dialog) dialog).dismiss();							
		//							alertDialog.dismiss();
		//							showMessageonImport(arrayList.get(which).getAbsolutePath());							
		//						}
		//					});
		//					
		//					if (arrayList.size() > 0)
		//					{						
		//						builder.show();
		//					}
		//					else
		//					{
		//						Toast.makeText(getApplicationContext(), getResources().getString(R.string.nofilefoundbackup), Toast.LENGTH_LONG).show();
		//					}
		
		//				}
		//				else if (arg2 == 1)
		//				{	
		//						
		//				}
		//			}
		//		});
	}
	
	@Override
	protected void onResume()
	{
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PURCHASED.key, false)) PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		super.onResume();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
		
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
	
	public ArrayList<File> walkdir(File dir)
	{
		String backupPattern = ".mpt";
		List<File> files = new ArrayList<File>();
		File listFile[] = dir.listFiles();
		
		if (listFile != null)
		{
			for (int i = 0; i < listFile.length; i++)
			{
				
				if (listFile[i].isDirectory())
				{
					files.addAll(walkdir(listFile[i]));
				}
				else
				{
					if (listFile[i].getName().endsWith(backupPattern))
					{
						// Do what ever u want
						files.add(listFile[i]);
						
					}
				}
			}
		}
		
		Comparator<File> comparator = Collections.reverseOrder();
		
		Collections.sort(files, comparator);/*
											 * {
											 * 
											 * @Override public int compare(File
											 * filel, File file2) { // TODO
											 * Auto-generated method stub int f
											 * =
											 * Long.valueOf(filel.lastModified(
											 * )).compareTo(Long.valueOf(file2.
											 * lastModified())); return (f != 0)
											 * ? f : -1; } });
											 */
		return (ArrayList<File>) files;
	}
	
	public void showMessageonImport(final String path)
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(RestoreAndBackupActivity.this);
		builder.setTitle(getResources().getString(R.string.warning));
		builder.setMessage(getResources().getString(R.string.messageonimport));
		builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				if (PeriodTrackerObjectLocator.getInstance().getPasswordProtection().equals("") || PeriodTrackerObjectLocator.getInstance().getPasswordProtection().equals(null))
				{
					new RestoreAndBackUp(RestoreAndBackupActivity.this, path).execute();
				}
				else
				{
					dialog.dismiss();
					askForPassword(path, RestoreAndBackupActivity.this);
				}
				
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.cancel), null);
		builder.show();
	}
	
	public static void askForPassword(final String path, final Context context)
	{
		
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper((RestoreAndBackupActivity) context, android.R.style.Theme_DeviceDefault_Light);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		// use a custom View defined in xml
		final View view = LayoutInflater.from((RestoreAndBackupActivity) context).inflate(R.layout.forheigthandweightvalue, null);
		builder.setView(view);
		final AlertDialog alertDialog = builder.create();
		
		TextView title = (TextView) view.findViewById(R.id.tileforhieghtandweight);
		title.setText(context.getResources().getString(R.string.enterpassword));
		final EditText editText = (EditText) view.findViewById(R.id.heightweightvalue);
		editText.setHint(context.getResources().getString(R.string.currentpasscode));
		editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		Button set = (Button) view.findViewById(R.id.setvalue);
		TextView textView = (TextView) view.findViewById(R.id.text);
		textView.setVisibility(View.INVISIBLE);
		
		set.setText(context.getResources().getString(R.string.ok));
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
				if (editText.getText().toString().equals(PeriodTrackerObjectLocator.getInstance().getPasswordProtection()))
				{
					alertDialog.cancel();
					new RestoreAndBackUp((RestoreAndBackupActivity) context, path).execute();
					
				}
				else
				{
					Toast.makeText(context, context.getResources().getString(R.string.mismatchpasscode), Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		alertDialog.show();
		
	}
	
	public void showAlertforpassword(final String path)
	{
		
		ContextThemeWrapper themeWrapper = new ContextThemeWrapper(RestoreAndBackupActivity.this, android.R.style.Theme_DeviceDefault_Light);
		AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
		final View view = LayoutInflater.from(this).inflate(R.layout.enter_password, null);
		builder.setView(view);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.mainlayout);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, 0);
		layout.setGravity(Gravity.TOP);
		layout.setLayoutParams(params);
		
		final AlertDialog alertDialog = builder.create();
		final EditText editText2 = (EditText) view.findViewById(R.id.checkpassword);
		TextView textView = (TextView) view.findViewById(R.id.enterpasswordtext);
		
		Button ok = (Button) view.findViewById(R.id.checkpasswordbutton);
		ok.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (editText2.getText().toString().equals(PeriodTrackerObjectLocator.getInstance().getPasswordProtection()))
				{
					alertDialog.cancel();
					new RestoreAndBackUp(RestoreAndBackupActivity.this, path).execute();
					
				}
				else
				{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.mismatchpasscode), Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		Button cancel = (Button) view.findViewById(R.id.forgotpassword);
		cancel.setText(getString(R.string.cancel));
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
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.signin)
		{
		}
		else if (id == R.id.signup)
		{
		}
		else if (id == R.id.localBackup)
		{
			new RestoreAndBackUp(RestoreAndBackupActivity.this, getResources().getString(R.string.backup)).execute();
		}
		else if (id == R.id.restore_data)
		{
			final List<File> arrayList = walkdir(Environment.getExternalStorageDirectory());
			CharSequence[] items = new String[arrayList.size()];
			for (int i = 0; i < arrayList.size(); i++)
			{
				items[i] = arrayList.get(i).getName();
			}
			ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(RestoreAndBackupActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
			final AlertDialog alertDialog = builder.create();
			builder.setTitle(getResources().getString(R.string.selectfiletobackup));
			final CustomArrayAdaptor adaptor = new CustomArrayAdaptor(getApplicationContext(), arrayList, dialogInterface);
			builder.setSingleChoiceItems(adaptor, -1, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					((RadioButton) adaptor.getView(which, adaptor.row, adaptor.viewGroup).findViewById(R.id.radio)).setChecked(true);
					dialogInterface = (Dialog) dialog;
					((Dialog) dialog).dismiss();
					alertDialog.dismiss();
					showMessageonImport(arrayList.get(which).getAbsolutePath());
				}
			});
			if (arrayList.size() > 0)
			{
				builder.show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.nofilefoundbackup), Toast.LENGTH_LONG).show();
			}
		}
		else
		{
		}
		
	}
	
}
