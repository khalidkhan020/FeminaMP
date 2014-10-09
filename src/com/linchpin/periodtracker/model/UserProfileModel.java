package com.linchpin.periodtracker.model;

import android.database.Cursor;

public class UserProfileModel implements PeriodTrackerModelInterface {

	private int id;
	private String firstName, lastName, userName, password;

	public static final String USER_PROFILE = "User_Profile";
	public static final String USER_PROFILE_FIRST_NAME = "First_Name";
	public static final String USER_PROFILE_LAST_NAME = "Last_Name";
	public static final String USER_PROFILE_USER_NAME = "User_Name";
	public static final String USER_PROFILE_PASSWORD = "Password";
	public static final String USER_PROFILE_ID = "Id";

	public UserProfileModel(int Id, String First_Name, String Last_Name, String User_Name, String Password) {

		this.id = Id;
		this.firstName = First_Name;
		this.lastName = Last_Name;
		this.userName = User_Name;
		this.password = Password;
	}

	public UserProfileModel() {

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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *          the firstName to set
	 */
	public void setFirstName(String firstName) {
		firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *          the lastName to set
	 */
	public void setLastName(String lastName) {
		lastName = lastName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *          the userName to set
	 */
	public void setUserName(String userName) {
		userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *          the password to set
	 */
	public void setPassword(String password) {
		password = password;
	}

	@Override
	public PeriodTrackerModelInterface createModel(Cursor cursor) {
		// TODO Auto-generated method stub
		id = cursor.getInt(0);
		firstName = cursor.getString(1);
		lastName = cursor.getString(2);
		userName = cursor.getString(3);
		password = cursor.getString(4);
		return this;
	}

}
