package com.linchpin.periodtracker.model;

import java.util.Date;

public class CalendarDayDetailModel
{
	
	private boolean	isPeriodDay, isFertileDay, isOvulationDay, havingNotes, isIntimate, havingPills;
	
	private Date	date;
	
	public CalendarDayDetailModel(Date date, boolean isPeriod, boolean isFertile, boolean isOvulation, boolean havingNotes, boolean isIntimate, boolean havingPill)
	{
		this.date = date;
		this.isPeriodDay = isPeriod;
		this.isFertileDay = isFertile;
		this.isOvulationDay = isOvulation;
		this.havingNotes = havingNotes;
		this.isIntimate = isIntimate;
		this.havingPills = havingPill;
		
	}
	
	/**
	* @return the date
	*/
	public final Date getDate()
	{
		return date;
	}
	
	/**
	 * @param date the date to set
	 */
	public final void setDate(Date date)
	{
		this.date = date;
	}
	
	/**
	 * @return the isFertileDay
	 */
	public final boolean isFertileDay()
	{
		return isFertileDay;
	}
	
	/**
	 * @param isFertileDay the isFertileDay to set
	 */
	public final void setFertileDay(boolean isFertileDay)
	{
		this.isFertileDay = isFertileDay;
	}
	
	/**
	 * @return the isOvulationDay
	 */
	public final boolean isOvulationDay()
	{
		return isOvulationDay;
	}
	
	/**
	 * @param isOvulationDay the isOvulationDay to set
	 */
	public final void setOvulationDay(boolean isOvulationDay)
	{
		this.isOvulationDay = isOvulationDay;
	}
	
	/**
	 * @return the havingNotes
	 */
	public final boolean isHavingNotes()
	{
		return havingNotes;
	}
	
	/**
	 * @param havingNotes the havingNotes to set
	 */
	public final void setHavingNotes(boolean havingNotes)
	{
		this.havingNotes = havingNotes;
	}
	
	/**
	 * @return the isIntimate
	 */
	public final boolean isIntimate()
	{
		return isIntimate;
	}
	
	/**
	 * @param isIntimate the isIntimate to set
	 */
	public final void setIntimate(boolean isIntimate)
	{
		this.isIntimate = isIntimate;
	}
	
	/**
	 * @return the havingPills
	 */
	public final boolean isHavingPills()
	{
		return havingPills;
	}
	
	/**
	 * @param havingPills the havingPills to set
	 */
	public final void setHavingPills(boolean havingPills)
	{
		this.havingPills = havingPills;
	}
	
	/**
	 * @return the isPeriodDay
	 */
	public final boolean isPeriodDay()
	{
		return isPeriodDay;
	}
	
	/**
	 * @param isPeriodDay the isPeriodDay to set
	 */
	public final void setPeriodDay(boolean isPeriodDay)
	{
		this.isPeriodDay = isPeriodDay;
	}
	
}
