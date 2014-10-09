package com.linchpin.periodtracker.model;

import android.database.Cursor;

public class Pills implements PeriodTrackerModelInterface {

	public static final String MEDICINE = "Medicine";
	public static final String MEDICINE_ID = "Id";
	public static final String MEDICINE_NAME = "Medicine_Name";
	public static final String MEDICINE_DAY_DETAILS_ID = "Day_Details_Id";
	public static final String MEDICINE_QUANTITY = "Medicine_Quantity";

	private int id, dayDetailsId, quantity;
	private String medicineName;

	public Pills(int id, int dayDetailId, String medicineName, int quntity) {

		this.id = id;
		this.dayDetailsId = dayDetailId;
		this.medicineName = medicineName;
		this.quantity = quntity;
	}

	public Pills() {

	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *          the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *          the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the dayDetailsId
	 */
	public int getDayDetailsId() {
		return dayDetailsId;
	}

	/**
	 * @param dayDetailsId
	 *          the dayDetailsId to set
	 */
	public void setDayDetailsId(int dayDetailsId) {
		this.dayDetailsId = dayDetailsId;
	}

	/**
	 * @return the medicineName
	 */
	public String getMedicineName() {
		return medicineName;
	}

	/**
	 * @param medicineName
	 *          the medicineName to set
	 */
	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	@Override
	public PeriodTrackerModelInterface createModel(Cursor cursor) {
		// TODO Auto-generated method stub
		this.id = cursor.getInt(0);
		this.medicineName = cursor.getString(1);
		this.quantity = cursor.getInt(2);
		this.dayDetailsId = cursor.getInt(3);

		return this;
	}

}
