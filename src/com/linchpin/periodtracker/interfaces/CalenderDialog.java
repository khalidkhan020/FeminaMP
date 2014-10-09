package com.linchpin.periodtracker.interfaces;

import java.util.Calendar;

import com.linchpin.periodtracker.view.DatePickerDialog;

public interface CalenderDialog
{
	public DatePickerDialog showDatePickerDialog(int titleid, Calendar calendar, Object obj,int id);
}
