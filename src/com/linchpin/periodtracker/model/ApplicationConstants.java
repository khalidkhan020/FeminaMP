package com.linchpin.periodtracker.model;

import android.database.Cursor;

public class ApplicationConstants implements PeriodTrackerModelInterface {

	public static final String APPLICATION_CONSTANTS = "Application_Constants";
	public static final String APPLICATION_CONSTANT_ID = "Id";
	public static final String APPLICATION_CONSTANT_KEY = "Key";
	
	private String id;
	private String key;

	public ApplicationConstants(String id, String key) {
		this.id = id;
		this.key = key;
	}
	public ApplicationConstants(){
		
	}
	
	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the key
	 */
	public final String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public final void setKey(String key) {
		this.key = key;
	}

	@Override
	public PeriodTrackerModelInterface createModel(Cursor cursor) {
		// TODO Auto-generated method stub

		this.id = cursor.getString(0);
		this.key = cursor.getString(1);
		return this;
	}

}
