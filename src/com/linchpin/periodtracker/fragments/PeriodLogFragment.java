package com.linchpin.periodtracker.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.ListViewAdaptor;
import com.linchpin.periodtracker.interfaces.CalenderDialog;
import com.linchpin.periodtracker.interfaces.PeriodMessanger;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.CustomAlertDialog;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.utlity.CustomAlertDialog.onButoinClick;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodtracker.view.PeriodLogPagerView;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;

public class PeriodLogFragment extends LogFragment implements OnClickListener, PeriodMessanger, OnItemClickListener
{
	private List<PeriodTrackerModelInterface>	loglist;
	private PeriodMessanger						sendmessageToactivity;
	private CalenderDialog						calenderDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		mView.findViewById(R.id.periodloginfobutton).setOnClickListener(this);
		past.setOnClickListener(this);
		prediction.setOnClickListener(this);
		adpator = new ListViewAdaptor(loglist, getActivity(), PeriodTrackerConstants.PAST_OVULATION_RECORDS);
		listView.setAdapter(adpator);
		refreshAdapterList(true);
		return mView;
		
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		sendmessageToactivity = (PeriodMessanger) activity;
		sendmessageToactivity.bind(PeriodLogPagerView.PERIOD_LOG_FRAGMENT_ID, this);
		calenderDialog = (CalenderDialog) activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.past)
		{
			refreshAdapterList(true);
		}
		else if (id == R.id.prediction)
		{
			refreshAdapterList(false);
		}
		else if (id == R.id.periodloginfobutton)
		{
			Intent intent = new Intent(getActivity(), HomeScreenHelp.class);
			intent.putExtra("classname", "periodlog");
			startActivity(intent);
		}
		else
		{
		}
		
	}
	
	private void refreshAdapterList(boolean ispast)
	{
		List<PeriodTrackerModelInterface> temp;
		if (ispast||PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
		{
			listView.setOnItemClickListener(this);
			past.setSelected(true);
			prediction.setSelected(false);
			if (loglist != null) loglist.clear();
			loglist = periodLogDBHandler.getAllLogs(PeriodTrackerObjectLocator.getInstance().getProfileId());
			
			adpator.setClassName(PeriodTrackerConstants.PAST_PERIOD_RECORD_LIST_FRAGMENT);
		}
		else
		{
			listView.setOnItemClickListener(null);
			prediction.setSelected(true);
			past.setSelected(false);
			temp = periodLogDBHandler.getPredictionLogs();
			if (loglist == null) loglist = new ArrayList<PeriodTrackerModelInterface>();
			loglist.clear();
			
			for (PeriodTrackerModelInterface interfaces : temp)
			{
				PeriodLogModel logModel;
				logModel = (PeriodLogModel) interfaces;
				if (!logModel.isPregnancy() && logModel.getStartDate() != null
						&& logModel.getStartDate().compareTo(Utility.setHourMinuteSecondZero(new Date())) > 0)
				{
					loglist.add(interfaces);
				}
			}
			adpator.setClassName(PeriodTrackerConstants.PERDICTION_PERIOD_LIST_FRAGMNET);
			
		}
		adpator.setPeriodTrackerModel(loglist);
		adpator.notifyDataSetChanged();
		//	initStartDateAndEndDate(mView);
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		refreshLengthItems();
	}
	
	@Override
	public void sendMessage(int id, Bundle bundle)
	{
		switch (id)
		{
			case REFRESH_PAST_LIST:
				refreshAdapterList(true);
				break;
			
			default:
				break;
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, final int position, long arg3)
	{
		final CharSequence[] items;
		PeriodLogModel periodLogModel = (PeriodLogModel) loglist.get(position);
		if (periodLogModel.isPregnancysupportable())
		{
			items = new CharSequence[1];
			items[0] = getResources().getString(R.string.edit);
		}
		else if (!periodLogModel.isPregnancy())
		{
			items = new CharSequence[2];
			items[0] = getResources().getString(R.string.edit);
			items[1] = getResources().getString(R.string.delete);
		}
		else
		{
			items = new CharSequence[1];
			items[0] = getResources().getString(R.string.delete);
			
		}
		showListAlert(items, position, periodLogModel);
		
	}
	
	private void showListAlert(final CharSequence[] items, final int position, final PeriodLogModel periodLogModel)
	{
		AlertDialog.Builder listBuilder = new AlertDialog.Builder(getActivity());
		listBuilder.setTitle(getString(R.string.selectoperation));
		listBuilder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface listdialog, int item)
			{
				if (items[item] == getResources().getString(R.string.edit))
				{
					listdialog.dismiss();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(periodLogModel.getStartDate());
					calenderDialog.showDatePickerDialog(R.string.setstartdate, calendar, periodLogModel, PeriodLogPagerView.EDIT_START_DATE).show();
				}
				else
				{
					listdialog.dismiss();
					onRecordDelete(position, periodLogModel);
				}
				
			}
			
		});
		listBuilder.show();
		
	}
	
	public void onRecordDelete(final int position, final PeriodLogModel periodLogModel)
	{
		String msg = (periodLogModel.isPregnancy() && PeriodTrackerObjectLocator.getInstance().getPregnancyMode()) ? getString(R.string.deletepregnancyRecord)
				: getString(R.string.deletemessage);
		CustomAlertDialog.Dialog(getActivity()).getAlertDialog(getString(R.string.delete), msg, true, getString(R.string.yes), true,
				getString(R.string.no), false, new onButoinClick()
				{
					
					@Override
					public void onClickPositive(DialogInterface dialog, int which)
					{
						if (null == periodLogDBHandler)
						{
							periodLogDBHandler = new PeriodLogDBHandler(getActivity());
						}
						
						if (loglist.size() > 0)
						{
							if (periodLogDBHandler.deletePeriodRecord(periodLogModel))
							{
								if (periodLogModel.isPregnancy() && PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
								{
									ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(getActivity());
									applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(
											PeriodTrackerConstants.APPLICATION_SETTING_PREGNANT_MODE, "false", PeriodTrackerObjectLocator
													.getInstance().getProfileId()));
									
									applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(
											PeriodTrackerConstants.APPLICATION_SETTINGS_ESTIMATED_DATE_VALUE_KEY, null, PeriodTrackerObjectLocator
													.getInstance().getProfileId()));
									PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
									
								}
								if (position == 0)
									APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, true).commit();
								refreshAdapterList(true);
								// listDialog.cancel();
								
							}
						}
						APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
					}
					
					@Override
					public void onClickNegative(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
	}
}
