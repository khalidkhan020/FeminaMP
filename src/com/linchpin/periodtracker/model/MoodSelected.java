package com.linchpin.periodtracker.model;

import android.database.Cursor;

public class MoodSelected implements PeriodTrackerModelInterface{

private	int id, DayDetailId, MoodId, MoodWeight;
	
	public static final String MOOD_SELECETED ="Mood_Selected"; 
	public static final String MOOD_SELECETED_ID ="Id"; 
	public static final String DAY_DETAIL_ID ="Day_Details_Id";
	public static final String MOOD_ID ="Mood_Id";
	public static final String MOOD_WIEGHT ="Mood_Weight";
	
	public MoodSelected(int Id , int Day_Details_Id ,  int Mood_Id , int Mood_Weight){
		this.id=Id;
		this.DayDetailId=Day_Details_Id;
		this.MoodId=Mood_Id;
		this.MoodWeight=Mood_Weight;
	}
	
	public MoodSelected(){
		
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
	 * @return the dayDetailId
	 */
	public int getDayDetailId() {
		return DayDetailId;
	}
	/**
	 * @param dayDetailId the dayDetailId to set
	 */
	public void setDayDetailId(int dayDetailId) {
		DayDetailId = dayDetailId;
	}
	/**
	 * @return the moodId
	 */
	public int getMoodId() {
		return MoodId;
	}
	/**
	 * @param moodId the moodId to set
	 */
	public void setMoodId(int moodId) {
		MoodId = moodId;
	}
	/**
	 * @return the moodWeight
	 */
	public int getMoodWeight() {
		return MoodWeight;
	}
	/**
	 * @param moodWeight the moodWeight to set
	 */
	public void setMoodWeight(int moodWeight) {
		MoodWeight = moodWeight;
	}
	
	@Override
	public PeriodTrackerModelInterface createModel(Cursor cursor) {
		// TODO Auto-generated method stub
		if(cursor!=null){
			this.id=cursor.getInt(0);
			this.DayDetailId= cursor.getInt(1);
			this.MoodId=cursor.getInt(2);
			this.MoodWeight= cursor.getInt(3);
		}
		return this;
	}
}
