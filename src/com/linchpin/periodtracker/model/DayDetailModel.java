package com.linchpin.periodtracker.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;

import android.database.Cursor;

public class DayDetailModel implements PeriodTrackerModelInterface
{
	
	private int								Id, ProfileID, Protection;
	private Date							date;
	private boolean							Intimate;
	private String							note;
	private Float							weight, temp, height;
	private int period=-1;
	public List<PeriodTrackerModelInterface>			moodSelectedsMap			= new ArrayList<PeriodTrackerModelInterface>();
	public ArrayList<PeriodTrackerModelInterface>	symtomsSelectedModelsMap	= new ArrayList<PeriodTrackerModelInterface>();
	
	public ArrayList<PeriodTrackerModelInterface>				medicineMap					= new ArrayList<PeriodTrackerModelInterface>();
	
	public static final String				DAY_DETAIL					= "Day_Details";
	public static final String				DAY_DETAIL_Id				= "Id";
	public static final String				DAY_DETAIL_DATE				= "Date";
	public static final String				DAY_DETAIL_NOTE				= "Note_Description";
	public static final String				DAY_DETAIL_INTIMATE			= "Intimate";
	public static final String				DAY_DETAIL_PROTECTION		= "Protection";
	public static final String				DAY_DETAIL_WEIGHT			= "Weight";
	public static final String				DAY_DETAIL_TEMPERATURE		= "Temperature";
	public static final String				DAY_DETAIL_HEIGHT			= "Height";
	public static final String				DAY_DETAIL_PROFILLE_ID		= "Profile_Id";
	
	public DayDetailModel(int id, Date date, String Note, boolean Intimate, int Protection, Float weight, Float temp, Float height, int profile_id,int period)
	{
		this.Id = id;
		this.date = date;
		this.note = Note;
		this.Intimate = Intimate;
		this.Protection = Protection;
		this.weight = weight;
		this.temp = temp;
		this.ProfileID = profile_id;
		this.height = height;
		this.period=period;
		
	}
	
	public int getPeriod()
	{
		return period;
	}

	public void setPeriod(int period)
	{
		this.period = period;
	}

	public DayDetailModel(int id, Date date, String Note, boolean Intimate, int Protection, Float weight, Float temp, Float height, int profile_id,int period,  List<PeriodTrackerModelInterface> moodSelectedsMap, ArrayList<PeriodTrackerModelInterface> symtomsSelectedModelsMap,ArrayList<PeriodTrackerModelInterface> medicineMap)
	{
		this(id, date, Note, Intimate, Protection, weight, temp, height, profile_id,period);
		this.medicineMap = medicineMap;
		this.symtomsSelectedModelsMap = symtomsSelectedModelsMap;
		this.moodSelectedsMap = moodSelectedsMap;
		
	}
	
	public DayDetailModel()
	{
	}
	/**
	 * @return the heightUnit
	 */
	public final Float getHeight()
	{
		return height;
	}
	
	/**
	 * @param heightUnit
	 *          the heightUnit to set
	 */
	public final void setHeight(Float height)
	{
		this.height = height;
	}
	
	/**
	 * @return the id
	 */
	public int getId()
	{
		return Id;
	}
	
	/**
	 * @param id
	 *          the id to set
	 */
	public void setId(int id)
	{
		Id = id;
	}
	
	/**
	 * @return the profileID
	 */
	public int getProfileID()
	{
		return ProfileID;
	}
	
	/**
	 * @param profileID
	 *          the profileID to set
	 */
	public void setProfileID(int profileID)
	{
		ProfileID = profileID;
	}
	
	/**
	 * @return the protection
	 */
	public int getProtection()
	{
		return Protection;
	}
	
	/**
	 * @param protection
	 *          the protection to set
	 */
	public void setProtection(int protection)
	{
		Protection = protection;
	}
	
	/**
	 * @return the note
	 */
	public String getNote()
	{
		return this.note;
	}
	
	/**
	 * @param note
	 *          the note to set
	 */
	public void setNote(String note)
	{
		this.note = note;
	}
	
	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}
	
	/**
	 * @param date
	 *          the date to set
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	/**
	 * @return the intimate
	 */
	public boolean isIntimate()
	{
		return Intimate;
	}
	
	/**
	 * @param intimate
	 *          the intimate to set
	 */
	public void setIntimate(boolean intimate)
	{
		Intimate = intimate;
	}
	
	/**
	 * @return the weight
	 */
	/**
	 * @return the weightUnit
	 */
	public final Float getWeight()
	{
		return weight;
	}
	
	/**
	 * @param weightUnit
	 *          the weightUnit to set
	 */
	public final void setWeight(Float weight)
	{
		this.weight = weight;
	}
	
	/**
	 * @return the tempUnit
	 */
	public final Float getTemp()
	{
		return temp;
	}
	
	/**
	 * @param tempUnit
	 *          the tempUnit to set
	 */
	public final void setTemp(Float tempUnit)
	{
		this.temp = tempUnit;
	}
	private PeriodLogDBHandler						periodLogDBHandler;
	@Override
	public PeriodTrackerModelInterface createModel(Cursor cursor)
	{

		this.periodLogDBHandler = new PeriodLogDBHandler(APP.GLOBAL().getApplicationContext());
		if (cursor != null)
		{
			this.Id = cursor.getInt(0);
			this.date = new Date(cursor.getLong(1));
			this.note = cursor.getString(2);
			this.Intimate = cursor.getInt(3) > 0;
			this.Protection = cursor.getInt(4);
			this.weight = cursor.getFloat(5);
			this.temp = cursor.getFloat(6);
			this.height = cursor.getFloat(7);
			this.ProfileID = cursor.getInt(8);
			int temp = periodLogDBHandler.isDateBetweenPeriods(cursor.getLong(1));
			
			this.period = temp;
		}
		return this;
	}
	
}
