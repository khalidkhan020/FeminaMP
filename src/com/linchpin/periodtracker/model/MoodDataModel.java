package com.linchpin.periodtracker.model;

import android.database.Cursor;

public class MoodDataModel implements PeriodTrackerModelInterface {

	private int Id, profileId;
	private String imageUri, moodLabel;

	private String moodType;

	public static final String MOOD = "Mood";
	public static final String MOOD_ID ="Id";
	public static final String MOOD_IMAGE_URI = "Image_Uri";
	public static final String MOOD_LABEL_KEY = "Mood_Label_Key";
	public static final String MOOD_TYPE = "Mood_Type";
	public static final String MOOD_PROFILE_ID = "Profile_Id";

	public MoodDataModel(int Id, String imageUri, String Mood_Label, String Mood_Type, int Profile_id) {
		// TODO Auto-generated constructor stub
		this.Id = Id;
		this.imageUri = imageUri;
		this.moodLabel = Mood_Label;
		this.moodType = Mood_Type;
		this.profileId = Profile_id;

	}


	public MoodDataModel() {

	}

	/**
	 * @return the id
	 */
	public int getId() {
		return Id;
	}

	/**
	 * @param id
	 *          the id to set
	 */
	public void setId(int id) {
		Id = id;
	}

	/**
	 * @return the profileId
	 */
	public int getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId
	 *          the profileId to set
	 */
	public void setProfileId(int profileId) {
		profileId = profileId;
	}

	/**
	 * @return the imageUri
	 */
	public String getImageUri() {
		return imageUri;
	}

	/**
	 * @param imageUri
	 *          the imageUri to set
	 */
	public void setImageUri(String imageUri) {
		imageUri = imageUri;
	}

	/**
	 * @return the moodLabel
	 */
	public String getMoodLabel() {
		return moodLabel;
	}

	/**
	 * @param moodLabel
	 *          the moodLabel to set
	 */
	public void setMoodLabel(String moodLabel) {
		moodLabel = moodLabel;
	}

	/**
	 * @return the moodType
	 */
	public String getMoodType() {
		return moodType;
	}

	/**
	 * @param moodType
	 *          the moodType to set
	 */
	public void setMoodType(String moodType) {
		moodType = moodType;
	}

	@Override
	public PeriodTrackerModelInterface createModel(Cursor cursor) {
		// TODO Auto-generated method stub
		if (null != cursor) {
			this.Id = cursor.getInt(0);
			this.imageUri = cursor.getString(1);
			this.moodLabel = cursor.getString(2);
			this.moodType = cursor.getString(3);
			this.profileId = cursor.getInt(4);
		}
		return this;
	}

}
