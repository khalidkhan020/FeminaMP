package com.linchpin.periodtracker.model;

import android.database.Cursor;

public class ApplicationSettingModel implements PeriodTrackerModelInterface {

	String id, value;
	int profileId;

public static final String APPLICATION_SETTING="Application_Settings";
public static final String APPLICATION_SETTING_ID="Id";
public static final String APPLICATION_SETTING_VALUE="KeyValues";
public static final String APPLICATION_SETTING_PROFILE_ID="Profile_id";

	
	
	
	
	
	
	public ApplicationSettingModel(){
		
	}
	
	
	
	
	public ApplicationSettingModel(String id, String value, int profileId) {
		this.id = id;
		this.value = value;
		this.profileId = profileId;

	}
	
	
	
	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @param id
	 *          the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}

	/**
	 * @param value
	 *          the value to set
	 */
	public final void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the profileId
	 */
	public final int getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId
	 *          the profileId to set
	 */
	public final void setProfileId(int profileId) {
		this.profileId = profileId;
	}

	
	

	@Override
	public PeriodTrackerModelInterface createModel(Cursor cursor) {
		// TODO Auto-generated method stub
	this.id=	cursor.getString(0);
	this.value=cursor.getString(1);
	this.profileId= cursor.getInt(2);
		
		return this;
	}

}
