package com.linchpin.periodtracker.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.ListViewAdaptor;
import com.linchpin.periodttracker.database.ApplicationSettingDBHandler;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;
import com.linchpin.periodtracker.model.ApplicationSettingModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.view.DatePickerDialog;
import com.linchpin.periodtracker.view.PeriodLogPagerView;

public class PastPeriodRecordListFragment extends Fragment implements OnItemClickListener {

	ListView pastList;
	PeriodLogDBHandler periodLogDBHandler;
	List<PeriodTrackerModelInterface> pastRecordList;
	ListViewAdaptor adpator;
	private int day;
	private int month;
	View mView;
	private int year;
	PeriodLogModel periodLogModel/*, periodLogModel2, pregnancySuppotable*/;
	String DatefFormat = PeriodTrackerObjectLocator.getInstance().getDateFormat();
	Date startDate, endDate;
	Date dStartDate, dEndDate, dValidStartDate;
	int flag = 0;
	DatePickerDialog editStartDatePickerDialog, editEndDatePickerDialog;
	int Position;
	DialogInterface listDialog;
	AlertDialog.Builder builder, listBuilder;

	SimpleDateFormat dateFormat;

	// String sDatefFormat = PeriodTrackerConstants.DATE_FORMAT;

	List<PeriodTrackerModelInterface> perdictionPeriodTrackerModelInterfaces, pastFertileAndOvultutionList,
			perdictionFertileAndOvultutionList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

		/*Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(new Date());
		calendar1.get(Calendar.MILLISECOND);
		System.out.println("common past periodlist " + calendar1.getTime() + " " + calendar1.get(Calendar.MILLISECOND));
*/
		

		// System.out.println("common past periodlist "+new Date() +"" + new
		// Date().getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		month = calendar.get(Calendar.MONTH);
		year = calendar.get(Calendar.YEAR);
		day = calendar.get(calendar.DATE);

		mView = inflater.inflate(R.layout.past_list, container, false);
		pastList = (ListView) mView.findViewById(R.id.pastlist);
		pastRecordList = ((PeriodLogPagerView) getActivity()).periodTrackerModelInterfaces;
		adpator = new ListViewAdaptor(pastRecordList, getActivity(),PeriodTrackerConstants.PAST_PERIOD_RECORD_LIST_FRAGMENT);

		pastList.setAdapter(adpator);
		// setListViewHeightBasedOnChildren(pastList);
		pastList.setOnItemClickListener(this);

		dateFormat = new SimpleDateFormat(DatefFormat);

		editStartDatePickerDialog = new DatePickerDialog(getActivity(), startDatePickerListener, year, month, day);
		editStartDatePickerDialog.setTitle(getResources().getString(R.string.setstartdate));
		return mView;
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View view, final int position, long arg3) {
		final CharSequence[] items;
		int itemPosition = position;

		periodLogModel = (PeriodLogModel) pastRecordList.get(position);

		if (periodLogModel.isPregnancysupportable()) {
			items = new CharSequence[1];
			items[0] = getResources().getString(R.string.edit);

		} else if (!periodLogModel.isPregnancy()) {
			items = new CharSequence[2];
			items[0] = getResources().getString(R.string.edit);
			items[1] = getResources().getString(R.string.delete);
		} else {
			items = new CharSequence[1];
			items[0] = getResources().getString(R.string.delete);

		}
		/*
		 * } else { if (periodLogModel.isPregnancy() ) { items = new
		 * CharSequence[1]; items[0] =
		 * getResources().getString(R.string.delete);
		 * 
		 * } else { items = new CharSequence[2]; items[0] =
		 * getResources().getString(R.string.edit); items[1] =
		 * getResources().getString(R.string.delete);
		 * 
		 * } }
		 */
		listBuilder = new AlertDialog.Builder(getActivity());
		listBuilder.setTitle(getString(R.string.selectoperation));
		listBuilder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface listdialog, int item) {
				listDialog = listdialog;
				if (items[item] == getResources().getString(R.string.edit)) {
					listdialog.dismiss();
					Position = position;
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(periodLogModel.getStartDate());
					editStartDatePickerDialog = new DatePickerDialog(getActivity(), startDatePickerListener, calendar
							.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
					editStartDatePickerDialog.setTitle(R.string.setstartdate);
					editStartDatePickerDialog.show();

				} else {
					listdialog.dismiss();
					onRecordDelete(position);
				}

			}

		});
		listBuilder.show();

	}

	// For Deleting Periodlog from DataBase

	public void onRecordDelete(final int position) {
		builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getResources().getString(R.string.delete));
		periodLogModel = (PeriodLogModel) pastRecordList.get(position);
		if (periodLogModel.isPregnancy() && PeriodTrackerObjectLocator.getInstance().getPregnancyMode()) {
			builder.setMessage(getResources().getString(R.string.deletepregnancyRecord));
		} else {
			builder.setMessage(getResources().getString(R.string.deletemessage));
		}
		builder.setPositiveButton(getResources().getString(R.string.yes), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (null == periodLogDBHandler) {
					periodLogDBHandler = new PeriodLogDBHandler(getActivity());
				}

				if (pastRecordList.size() > 0) {
					periodLogModel = (PeriodLogModel) pastRecordList.get(position);
					if (periodLogDBHandler.deletePeriodRecord(periodLogModel)) {
						if (periodLogModel.isPregnancy() && PeriodTrackerObjectLocator.getInstance().getPregnancyMode()) {
							ApplicationSettingDBHandler applicationSettingDBHandler = new ApplicationSettingDBHandler(
									getActivity());
							applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(
									PeriodTrackerConstants.APPLICATION_SETTING_PREGNANT_MODE, "false",
									PeriodTrackerObjectLocator.getInstance().getProfileId()));

							applicationSettingDBHandler.upadteApplicationSetting(new ApplicationSettingModel(
									PeriodTrackerConstants.APPLICATION_SETTINGS_ESTIMATED_DATE_VALUE_KEY, null,
									PeriodTrackerObjectLocator.getInstance().getProfileId()));
							PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();

						}
						if (position == 0) {
							APP.GLOBAL().getEditor().putBoolean(APP.PREF.NEED_TO_CHANGE.key, true).commit();
						refreshlist();
						listDialog.cancel();

					}
				}
			}
		});

		builder.setNegativeButton(getResources().getString(R.string.no), null);
		builder.show();

	}

	// Set Start Date Listener

	private DatePickerDialog.OnDateSetListener startDatePickerListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view,int id, int _year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			year = _year;
			month = monthOfYear;
			day = dayOfMonth;
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getResources().getString(R.string.invaliddatetitle));
			startDate = Utility.createDate(year, month, day);

			if (((PeriodLogPagerView) getActivity()).periodTrackerModelInterfaces.size() > 0) {
				periodLogModel = (PeriodLogModel) ((PeriodLogPagerView) getActivity()).periodTrackerModelInterfaces
						.get(0);
				if (!Utility.isDateLessThanCurrent(startDate)) {

					// Show Message
					builder.setMessage(getResources().getString(R.string.lessthancallmessage));
					builder.setPositiveButton(getResources().getString(R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									editStartDatePickerDialog.show();

								}
							});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				} else if (Utility.isGreaterThan(periodLogModel.getStartDate(), startDate) && Position != 0) {

					builder.setMessage(R.string.lessthanLastestRecord);
					builder.setPositiveButton(getResources().getString(R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									editStartDatePickerDialog.show();

								}
							});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();

				} else if (checkValidityOfDate(startDate)) {

					Calendar calendar = new GregorianCalendar();
					Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance()
							.getCurrentPeriodLength() - 1);
					if (date.getTime() <= startDate.getTime()) {
						calendar.setTime(new Date());
					} else {
						calendar.setTime(date);
					}
					updateStartDate(startDate, Position);

				} else {

					builder.setTitle(getResources().getString(R.string.invaliddatetitle));
					builder.setMessage(R.string.invalidateRecordBetweenDates);
					builder.setPositiveButton(getResources().getString(R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									editStartDatePickerDialog.show();
								}
							});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();

				}
			}
		}
	};

	// Set End Date Listener

	private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view,int id, final int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			endDate = Utility.createDate(year, monthOfYear, dayOfMonth);
			if (((PeriodLogPagerView) getActivity()).periodTrackerModelInterfaces.size() > 0) {
				periodLogModel = (PeriodLogModel) ((PeriodLogPagerView) getActivity()).periodTrackerModelInterfaces
						.get(0);

				if (!Utility.isDateLessThanCurrent(endDate)) {
					builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(getResources().getString(R.string.invaliddatetitle));
					builder.setMessage(getResources().getString(R.string.lessthancallmessage));
					builder.setPositiveButton(getResources().getString(R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									periodLogModel = (PeriodLogModel) ((PeriodLogPagerView) getActivity()).periodTrackerModelInterfaces
											.get(Position);
									Calendar calendar = GregorianCalendar.getInstance();
									calendar.setTime(periodLogModel.getEndDate());
									editEndDatePickerDialog = new DatePickerDialog(getActivity(), endDateSetListener,
											calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
													.get(Calendar.DATE));
									editEndDatePickerDialog.show();
								}
							});

					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				} else if (!Utility.isEndDateGreaterThanStart(startDate, endDate)) {

					builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(getResources().getString(R.string.invaliddatetitle));
					builder.setMessage(getResources().getString(R.string.enddategreaterthanstart));

					builder.setPositiveButton(getResources().getString(R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub

									Calendar calendar = new GregorianCalendar();
									Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance()
											.getCurrentPeriodLength() - 1);
									if (date.getTime() <= startDate.getTime()) {
										calendar.setTime(new Date());
									} else {
										calendar.setTime(date);
									}
									editEndDatePickerDialog = new DatePickerDialog(getActivity(), endDateSetListener,
											calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
													.get(Calendar.DATE));
									editEndDatePickerDialog.setTitle(getResources().getString(R.string.setenddate));
									/*
									 * if (Utility.checkAndroidApiVersion()) {
									 * editEndDatePickerDialog
									 * .getDatePicker().setMaxDate(new
									 * Date().getTime()); }
									 */
									editEndDatePickerDialog.show();
								}
							});

					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				} else if (checkValidityOfDate(endDate)) {

					if (checkRecordBetweenDates(startDate, endDate, Position)) {
						updateEndDate(endDate, Position);
					} else {

						builder.setMessage(getResources().getString(R.string.invalidateRecordBetweenDates));
						builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub

								Calendar calendar = new GregorianCalendar();
								Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance()
										.getCurrentPeriodLength());
								if (date.getTime() <= startDate.getTime()) {
									calendar.setTime(new Date());
								} else {
									calendar.setTime(date);
								}

								editEndDatePickerDialog = new DatePickerDialog(getActivity(), endDateSetListener,
										calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
												.get(Calendar.DATE));
								editEndDatePickerDialog.setTitle(getResources().getString(R.string.setenddate));
								/*
								 * if (Utility.checkAndroidApiVersion()) {
								 * editEndDatePickerDialog.getDatePicker
								 * ().setMaxDate(new Date().getTime()); }
								 */
								editEndDatePickerDialog.show();
							}
						});
						builder.setNegativeButton(getResources().getString(R.string.cancel), null);
						builder.show();

					}
				} else {

					builder.setMessage(getResources().getString(R.string.invaliddatemessage));
					builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							Calendar calendar = new GregorianCalendar();
							Date date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance()
									.getCurrentPeriodLength() - 1);
							if (date.getTime() <= startDate.getTime()) {
								calendar.setTime(new Date());
							} else {
								calendar.setTime(date);
							}

							editEndDatePickerDialog = new DatePickerDialog(getActivity(), endDateSetListener, calendar
									.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
							editEndDatePickerDialog.setTitle(getResources().getString(R.string.setenddate));
							/*
							 * if (Utility.checkAndroidApiVersion()) {
							 * editEndDatePickerDialog
							 * .getDatePicker().setMaxDate(new
							 * Date().getTime()); }
							 */
							editEndDatePickerDialog.show();
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();

				}

			}

		}
	};

	public void refreshlist() {

		periodLogDBHandler = new PeriodLogDBHandler(getActivity());
		if (pastRecordList != null) {
			pastRecordList.clear();
			adpator.notifyDataSetChanged();
		}
		PeriodTrackerObjectLocator.getInstance().intitializeApplicationParameters();
		pastRecordList = periodLogDBHandler.getAllLogs(PeriodTrackerObjectLocator.getInstance().getProfileId());
		((PeriodLogPagerView) getActivity()).periodTrackerModelInterfaces = pastRecordList;
		adpator = new ListViewAdaptor(pastRecordList, getActivity(),
				PeriodTrackerConstants.PAST_PERIOD_RECORD_LIST_FRAGMENT);
		pastList.setAdapter(adpator);

		perdictionPeriodTrackerModelInterfaces = periodLogDBHandler.getPredictionLogs();
		/*if (PeriodLogFragment.perdictionPeriodListFragment.predictionRecordList != null) {
			PeriodLogFragment.perdictionPeriodListFragment.predictionRecordList.clear();
		}
		{

			PeriodLogFragment.perdictionPeriodListFragment.predictionRecordList = perdictionPeriodTrackerModelInterfaces;
			PeriodLogFragment.perdictionPeriodListFragment.adaptor = new ListViewAdaptor(
					perdictionPeriodTrackerModelInterfaces, getActivity(),
					PeriodTrackerConstants.PERDICTION_PERIOD_LIST_FRAGMNET);
			PeriodLogFragment.perdictionPeriodListFragment.predictionList
					.setAdapter(PeriodLogFragment.perdictionPeriodListFragment.adaptor);
			// perdictionPeriodListFragment.adaptor.notifyDataSetChanged();
		}
*/
		perdictionFertileAndOvultutionList = periodLogDBHandler.getPerdictionFertileDatesAndOvulationDates();
		if (PeriodLogFragment.perdictionFertileRecordsFragments.predictionRecordList != null) {
			PeriodLogFragment.perdictionFertileRecordsFragments.predictionRecordList.clear();
		}
		{

			PeriodLogModel logModel;
			ArrayList<PeriodTrackerModelInterface> arrayList = new ArrayList<PeriodTrackerModelInterface>();
			if (null != perdictionFertileAndOvultutionList) {
				for (int i = 0; i < perdictionFertileAndOvultutionList.size(); i++) {
					if (arrayList.size() < 6) {
						logModel = (PeriodLogModel) perdictionFertileAndOvultutionList.get(i);
						if (!logModel.isPregnancy() && logModel.getFertileStartDate() != null
								&& !logModel.getFertileStartDate().before(Utility.setHourMinuteSecondZero(new Date()))) {
							arrayList.add(perdictionFertileAndOvultutionList.get(i));
						}
					} else {
						break;
					}
				}
			}

			PeriodLogFragment.perdictionFertileRecordsFragments.predictionRecordList = arrayList;
			PeriodLogFragment.perdictionFertileRecordsFragments.adaptor = new ListViewAdaptor(arrayList, getActivity(),
					PeriodTrackerConstants.PERDICTION_FERTILE_RECORDS);
			PeriodLogFragment.perdictionFertileRecordsFragments.predictionList
					.setAdapter(PeriodLogFragment.perdictionFertileRecordsFragments.adaptor);
			PeriodLogFragment.perdictionFertileRecordsFragments.adaptor.notifyDataSetChanged();
		}
		if (PeriodLogFragment.perdictionOvulationRecordsFragments != null) {
			if (PeriodLogFragment.perdictionOvulationRecordsFragments.predictionRecordList != null) {
				PeriodLogFragment.perdictionOvulationRecordsFragments.predictionRecordList.clear();
			}

			PeriodLogModel logModel;
			ArrayList<PeriodTrackerModelInterface> arrayList = new ArrayList<PeriodTrackerModelInterface>();
			if (null != perdictionFertileAndOvultutionList) {
				for (int i = 0; i < perdictionFertileAndOvultutionList.size(); i++) {
					if (arrayList.size() < 6) {
						logModel = (PeriodLogModel) perdictionFertileAndOvultutionList.get(i);
						if (!logModel.isPregnancy() && logModel.getOvulationDate() != null
								&& !logModel.getOvulationDate().before(Utility.setHourMinuteSecondZero(new Date()))) {
							arrayList.add(perdictionFertileAndOvultutionList.get(i));
						}
					} else {
						break;
					}

				}
			}

			PeriodLogFragment.perdictionOvulationRecordsFragments.predictionRecordList = arrayList;
			PeriodLogFragment.perdictionOvulationRecordsFragments.adaptor = new ListViewAdaptor(arrayList,
					getActivity(), PeriodTrackerConstants.PERDICTION_OVULATION_RECORDS);
			PeriodLogFragment.perdictionOvulationRecordsFragments.predictionList
					.setAdapter(PeriodLogFragment.perdictionOvulationRecordsFragments.adaptor);
			PeriodLogFragment.perdictionOvulationRecordsFragments.adaptor.notifyDataSetChanged();
		}

		pastFertileAndOvultutionList = periodLogDBHandler.getPastFertileAndOvulationDates();
		if (PeriodLogFragment.pastfertileRecordsFragments.pastRecordList != null) {
			PeriodLogFragment.pastfertileRecordsFragments.pastRecordList.clear();
		}
		{

			PeriodLogModel logModel;
			ArrayList<PeriodTrackerModelInterface> arrayList = new ArrayList<PeriodTrackerModelInterface>();
			for (PeriodTrackerModelInterface iterable : pastFertileAndOvultutionList) {
				logModel = (PeriodLogModel) iterable;
				if (!logModel.isPregnancy() && logModel.getFertileStartDate() != null
						&& logModel.getFertileStartDate().before(new Date())) {
					arrayList.add(iterable);
				}

			}
			PeriodLogFragment.pastfertileRecordsFragments.pastRecordList = arrayList;
			PeriodLogFragment.pastfertileRecordsFragments.adpator = new ListViewAdaptor(arrayList, getActivity(),
					PeriodTrackerConstants.PAST_FERTILE_RECORDS);
			PeriodLogFragment.pastfertileRecordsFragments.pastList
					.setAdapter(PeriodLogFragment.pastfertileRecordsFragments.adpator);
			PeriodLogFragment.pastfertileRecordsFragments.adpator.notifyDataSetChanged();
		}
		if (PeriodLogFragment.pastovulationRecordsFragments != null) {
			if (PeriodLogFragment.pastovulationRecordsFragments.pastRecordList != null) {
				PeriodLogFragment.pastovulationRecordsFragments.pastRecordList.clear();
			}
			{

				PeriodLogModel logModel;
				ArrayList<PeriodTrackerModelInterface> arrayList = new ArrayList<PeriodTrackerModelInterface>();
				for (PeriodTrackerModelInterface iterable : pastFertileAndOvultutionList) {
					logModel = (PeriodLogModel) iterable;
					if (!logModel.isPregnancy() && null != logModel.getOvulationDate()
							&& logModel.getOvulationDate().before(new Date())) {
						arrayList.add(iterable);
					}

				}
				PeriodLogFragment.pastovulationRecordsFragments.pastRecordList = arrayList;
				PeriodLogFragment.pastovulationRecordsFragments.adpator = new ListViewAdaptor(arrayList, getActivity(),
						PeriodTrackerConstants.PAST_OVULATION_RECORDS);
				PeriodLogFragment.pastovulationRecordsFragments.pastList
						.setAdapter(PeriodLogFragment.pastovulationRecordsFragments.adpator);
				PeriodLogFragment.pastovulationRecordsFragments.adpator.notifyDataSetChanged();
			}
		}
		if (null != PeriodLogFragment.periodLogFragment) {
			PeriodLogFragment.periodLogFragment.initStartDateAndEndDate(PeriodLogFragment.periodLogFragment.mView);
		}
		/*if (null != FertileDaysFragments.fertileDaysFragments) {
			FertileDaysFragments.fertileDaysFragments
					.initStartAndEndDateButton(FertileDaysFragments.fertileDaysFragments.mView);
		}*/

		if (null != PeriodLogFragment.periodLogFragment)
			PeriodLogFragment.periodLogFragment.RefreshLengthItems();
		/*if (null != FertileDaysFragments.fertileDaysFragments)
			FertileDaysFragments.fertileDaysFragments.RefreshLengthItems();
		*//*if (null != OvulationDaysFragments.ovulationDaysFragments)
			OvulationDaysFragments.ovulationDaysFragments.RefreshItems();
*/
		/*
		 * PeriodLogFragment.periodLogFragment.tAverageCycleLength.setText(
		 * PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength());
		 * PeriodLogFragment.periodLogFragment.tAveragePeriodLength.setText(
		 * PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength());
		 * FertileDaysFragments
		 * .fertileDaysFragments.tAverageCycleLength.setText(
		 * PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength());
		 * FertileDaysFragments
		 * .fertileDaysFragments.tAveragePeriodLength.setText
		 * (PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength());
		 * OvulationDaysFragments
		 * .ovulationDaysFragments.textView.setText(PeriodTrackerObjectLocator
		 * .getInstance().getCurrentOvualtionLength());
		 */

	}

	public void updateEndDate(Date EndDate, int positon) {
		periodLogDBHandler = new PeriodLogDBHandler(getActivity());
		periodLogModel = (PeriodLogModel) pastRecordList.get(positon);
		EndDate = Utility.setHourMinuteSecondZero(EndDate);
		periodLogModel.setEndDate(EndDate);
		if (periodLogDBHandler.updatePeriodLogWhenEditRecord(periodLogModel)) {
			refreshlist();
		} else {
			Toast.makeText(getActivity(), "Not able to update", Toast.LENGTH_LONG).show();
		}

	}

	public void updateStartDate(Date startDate, int position) {

		periodLogDBHandler = new PeriodLogDBHandler(getActivity());
		periodLogModel = (PeriodLogModel) pastRecordList.get(position);
		periodLogModel.setStartDate(startDate);
		Date date;
		Calendar calendar = new GregorianCalendar();
		date = Utility.addDays(startDate, PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() - 1);
		calendar.setTime(date);
		editEndDatePickerDialog = new DatePickerDialog(getActivity(), endDateSetListener, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		editEndDatePickerDialog.setTitle(getResources().getString(R.string.setenddate));
		editEndDatePickerDialog.show();

	}

	public boolean checkValidityOfDate(Date date) {
		boolean validity = false;
		periodLogModel = (PeriodLogModel) pastRecordList.get(Position);
		for (int i = 0; i < pastRecordList.size(); i++) {
			if (i != Position) {
				PeriodLogModel periodLogModel = (PeriodLogModel) pastRecordList.get(i);
				if (!Utility.checkDateBetweenDates(periodLogModel.getStartDate(), periodLogModel.getEndDate(), date)) {
					validity = false;
					Toast.makeText(getActivity(), "In Valid date", Toast.LENGTH_LONG).show();
					break;
				} else {
					validity = true;
					periodLogModel.getEndDate();
					periodLogModel.getStartDate();

				}

			}
		}
		if (pastRecordList.size() == 1) {
			validity = true;
		}
		return validity;
	}

	public boolean checkRecordBetweenDates(Date startDate, Date endDate, int postion) {
		boolean validity = false;
		startDate = Utility.setHourMinuteSecondZero(startDate);
		endDate = Utility.setHourMinuteSecondZero(endDate);
		periodLogDBHandler = new PeriodLogDBHandler(getActivity());
		periodLogModel = (PeriodLogModel) pastRecordList.get(postion);
		List<PeriodTrackerModelInterface> PeriodTrackerModelInterface = periodLogDBHandler
				.checkValiadtionOfRecordBetweenRecordsWhileUpadte(startDate, endDate, periodLogModel.getId());
		if (PeriodTrackerModelInterface.size() > 0) {
			return validity = false;
		} else {
			return validity = true;
		}
	}


}
