/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linchpin.periodtracker.view;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.linchpin.myperiodtracker.R;

/**
 * A simple dialog containing an {@link android.widget.DatePicker}.
 * 
 * <p>
 * See the <a href="{@docRoot}
 * resources/tutorials/views/hello-datepicker.html">Date Picker tutorial</a>.
 * </p>
 */
public class DatePickerDialog extends AlertDialog implements OnClickListener,
		OnDateChangedListener {

	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";

	private final DatePicker mDatePicker;
	private final OnDateSetListener mCallBack;
	private final Calendar mCalendar;

	private boolean mTitleNeedsUpdate = true;

	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	public interface OnDateSetListener {

		/**
		 * @param view
		 *            The view associated with this listener.
		 * @param year
		 *            The year that was set.
		 * @param monthOfYear
		 *            The month that was set (0-11) for compatibility with
		 *            {@link java.util.Calendar}.
		 * @param dayOfMonth
		 *            The day of the month that was set.
		 */
		void onDateSet(DatePicker view, int id,int year, int monthOfYear,
				int dayOfMonth);
	}

	/**
	 * @param context
	 *            The context the dialog is to run in.
	 * @param callBack
	 *            How the parent is notified that the date is set.
	 * @param year
	 *            The initial year of the dialog.
	 * @param monthOfYear
	 *            The initial month of the dialog.
	 * @param dayOfMonth
	 *            The initial day of the dialog.
	 */
	private int id;
	public DatePickerDialog(Context context, OnDateSetListener callBack,int year, int monthOfYear, int dayOfMonth) 
	{
		this(context, 0, callBack, year, monthOfYear, dayOfMonth);
	}
	public DatePickerDialog(Context context, OnDateSetListener callBack,int year, int monthOfYear, int dayOfMonth,int id) 
	{
		
		this(context, 0, callBack, year, monthOfYear, dayOfMonth);
		this.id=id;
	}
	/**
	 * @param context
	 *            The context the dialog is to run in.
	 * @param theme
	 *            the theme to apply to this dialog
	 * @param callBack
	 *            How the parent is notified that the date is set.
	 * @param year
	 *            The initial year of the dialog.
	 * @param monthOfYear
	 *            The initial month of the dialog.
	 * @param dayOfMonth
	 *            The initial day of the dialog.
	 */
	public DatePickerDialog(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme);

		mCallBack = callBack;

		mCalendar = Calendar.getInstance();

		Context themeContext = getContext();
		setButton(BUTTON_POSITIVE, themeContext.getText(R.string.set), this);
		setButton(BUTTON_NEGATIVE, themeContext.getText(R.string.cancel), this);
		setIcon(0);

		LayoutInflater inflater = (LayoutInflater) themeContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.date_picker_dialog, null);
		setView(view);
		mDatePicker = (DatePicker) view.findViewById(R.id.date_picker);
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);
		//updateTitle(year, monthOfYear, dayOfMonth);
	}

	public void onClick(DialogInterface dialog, int which) {
		if (dialog.BUTTON_POSITIVE == which) {

			tryNotifyDateSet(true);
		} else {
			tryNotifyDateSet(false);
			mDatePicker.cancelLongPress();
		}

	}

	public void onDateChanged(DatePicker view, int year, int month, int day) {
		mDatePicker.init(year, month, day, this);
		//updateTitle(year, month, day);
	}

	/**
	 * Gets the {@link DatePicker} contained in this dialog.
	 * 
	 * @return The calendar view.
	 */
	public DatePicker getDatePicker() {
		return mDatePicker;
	}

	/**
	 * Sets the current date.
	 * 
	 * @param year
	 *            The date year.
	 * @param monthOfYear
	 *            The date month.
	 * @param dayOfMonth
	 *            The date day of month.
	 */
	public void updateDate(int year, int monthOfYear, int dayOfMonth) {
		mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
	}

	private void tryNotifyDateSet(boolean t)
	{
		if (mCallBack != null)
		{
			if(t){
			mDatePicker.clearFocus();
			mCallBack.onDateSet(mDatePicker, id, mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
		}else {
			mDatePicker.clearFocus();
			mCallBack.onDateSet(mDatePicker, id, 12345, 12345, 12345);
	
		}}
	}

	@Override
	protected void onStop() {
		if (mCallBack != null)
			mDatePicker.clearFocus();
		super.onStop();
	}

	/*private void updateTitle(int year, int month, int day) {
		 {
			mCalendar.set(Calendar.YEAR, year);
			mCalendar.set(Calendar.MONTH, month);
			mCalendar.set(Calendar.DAY_OF_MONTH, day);
			String title = DateUtils.formatDateTime(getContext(),
					mCalendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_SHOW_WEEKDAY
							| DateUtils.FORMAT_SHOW_YEAR
							| DateUtils.FORMAT_ABBREV_MONTH
							| DateUtils.FORMAT_ABBREV_WEEKDAY);
			setTitle(title);
			mTitleNeedsUpdate = true;
		} {
			if (mTitleNeedsUpdate) {
				mTitleNeedsUpdate = false;
				setTitle(R.string.about);
			}
		}
	}*/

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(YEAR, mDatePicker.getYear());
		state.putInt(MONTH, mDatePicker.getMonth());
		state.putInt(DAY, mDatePicker.getDayOfMonth());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int year = savedInstanceState.getInt(YEAR);
		int month = savedInstanceState.getInt(MONTH);
		int day = savedInstanceState.getInt(DAY);
		mDatePicker.init(year, month, day, this);
	}
}
