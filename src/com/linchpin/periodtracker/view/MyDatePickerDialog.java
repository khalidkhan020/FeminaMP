package com.linchpin.periodtracker.view;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;

public class MyDatePickerDialog extends DatePickerDialog {
	ContentResolver contentResolver=null;
	public MyDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);
		// TODO Auto-generated constructor stub
	}

}
