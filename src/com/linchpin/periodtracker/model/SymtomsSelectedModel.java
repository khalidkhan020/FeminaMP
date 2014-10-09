package com.linchpin.periodtracker.model;

import android.database.Cursor;

public class SymtomsSelectedModel implements PeriodTrackerModelInterface {

	
	public static final String SYMTOM_SELECTED ="Symptom_Selected";
	public static final String SYMTOM_SELECTED_ID ="Id";
	public static final String SYMTOM_SELECTED_SYMTOM_ID="Symptom_Id";
	public static final String SYMTOM_SELECTED_DAY_DETAIL_ID =	"Day_Details_Id";
	public static final String SYMTOM_SELECTED_SYMTOM_WEIGHT = "Symptom_Weight";
	
	
	private int id,symptomId,dayDetailsID,symptomWeight;
	
	
	
	
	public SymtomsSelectedModel(int Id, int Symptom_Id , int Day_Details_Id ,int Symptom_Weight ){
		this.id =Id;
		this.symptomId =Symptom_Id;
		this.dayDetailsID=Day_Details_Id;
		this.symptomWeight=Symptom_Weight;
		
	}
	
	public SymtomsSelectedModel(){
		
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
		this.id = id;
	}

	/**
	 * @return the symptomId
	 */
	public int getSymptomId() {
		return symptomId;
	}

	/**
	 * @param symptomId the symptomId to set
	 */
	public void setSymptomId(int symptomId) {
	this.symptomId = symptomId;
	}

	/**
	 * @return the dayDetailsID
	 */
	public int getDayDetailsID() {
		return dayDetailsID;
	}

	/**
	 * @param dayDetailsID the dayDetailsID to set
	 */
	public void setDayDetailsID(int dayDetailsID) {
		this.dayDetailsID = dayDetailsID;
	}

	/**
	 * @return the symptomWeight
	 */
	public int getSymptomWeight() {
		return symptomWeight;
	}

	/**
	 * @param symptomWeight the symptomWeight to set
	 */
	public void setSymptomWeight(int symptomWeight) {
		this.symptomWeight = symptomWeight;
	}
	
	@Override
	public PeriodTrackerModelInterface createModel(Cursor cursor) {
		// TODO Auto-generated method stub
		if(cursor!=null){
		this.id = cursor.getInt(0);
		this.symptomId= cursor.getInt(1);
		this.dayDetailsID= cursor.getInt(2);
		this.symptomWeight = cursor.getInt(3);
		}
		return this;
	}
	
	
}
