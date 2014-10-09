package com.linchpin.periodtracker.model;

import android.database.Cursor;

public class NotificattionModel implements PeriodTrackerModelInterface {

	public static final String NOTIFICATION = "Notificatons";
	public static final String NOTIFICATION_ID = "Notification_Id";
	public static final String NOTIFICATION_TYPE = "Notification_Type";

	int id;
	String type;

	public NotificattionModel(){
		
	}
	public NotificattionModel(int id, String type) {
		this.id = id;
		this.type = type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public PeriodTrackerModelInterface createModel(Cursor cursor) {
		// TODO Auto-generated method stub

		if (cursor != null) {
			this.id = cursor.getInt(0);
			this.type = cursor.getString(1);
		}

		return this;
	}
}
