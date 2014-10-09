package com.linchpin.periodtracker.model;

import android.database.Cursor;

public class SymptomsModel implements PeriodTrackerModelInterface{

	
	public static final String SYMTOMS="Symptoms";
	public static final String  SYMTOMS_IMAGEURI="Image_Uri";
	public static final String  SYMTOMS_LABEL_KEY="Symptom_Label_Key";
	public static final String SYMTOMS_PROFILE_ID="Profile_Id";
	public static final String SYMTOMS_ID="Id";
	public static final String SYMTOMS_TYPE="Symptom_Type";
		
	private String imageUri,symptomLabelKey;
	private int id,profileId ;
	private String symptomType ;
	
	public SymptomsModel(){
		
	}
	
	public SymptomsModel(int id , String imageUri , String symptomLabelKey , String symptomType , int profileId ){
		this.id =id;
		this.imageUri = imageUri;
		this.symptomLabelKey = symptomLabelKey;
		this.symptomType=symptomType;
		this.profileId =profileId;
		
		
	}
	/**
	 * @return the imageUri
	 */
	public String getImageUri() {
		return imageUri;
	}
	/**
	 * @param imageUri the imageUri to set
	 */
	public void setImageUri(String imageUri) {
		imageUri = imageUri;
	}
	/**
	 * @return the symptomLabelKey
	 */
	public String getSymptomLabelKey() {
		return symptomLabelKey;
	}
	/**
	 * @param symptomLabelKey the symptomLabelKey to set
	 */
	public void setSymptomLabelKey(String symptomLabelKey) {
		symptomLabelKey = symptomLabelKey;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		id = id;
	}
	/**
	 * @return the profileId
	 */
	public int getProfileId() {
		return profileId;
	}
	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(int profileId) {
		profileId = profileId;
	}
	
	/**
	 * @return the symptom
	 */
	public String getSymptomType() {
		return symptomType;
	}
	/**
	 * @param symptom the symptom to set
	 */
	public void setSymptomType(char symptom) {
		symptom = symptom;
	}
	@Override
	public PeriodTrackerModelInterface createModel(Cursor cursor) {
		// TODO Auto-generated method stub
		this.id = cursor.getInt(0);
		this.imageUri = cursor.getString(1);
		this.symptomLabelKey= cursor.getString(2);
		this.symptomType=cursor.getString(3);
		this.profileId=cursor.getInt(4);
		return this;
	}
}
