package com.linchpin.periodttracker.database;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.MoodDataModel;
import com.linchpin.periodtracker.model.MoodSelected;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.Pills;
import com.linchpin.periodtracker.model.SymptomsModel;
import com.linchpin.periodtracker.model.SymtomsSelectedModel;
import com.linchpin.periodtracker.model.TimeLineModel;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;

public class AddNoteDBHandler extends PeriodTrackerDBHandler {

	public AddNoteDBHandler(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public boolean addRecordInDayDetail(DayDetailModel dayDetailModel) {
		boolean rowCreated = false;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(DayDetailModel.DAY_DETAIL_DATE, dayDetailModel
					.getDate().getTime());
			contentValues.put(DayDetailModel.DAY_DETAIL_NOTE,
					dayDetailModel.getNote());
			contentValues.put(DayDetailModel.DAY_DETAIL_INTIMATE,
					dayDetailModel.isIntimate());
			contentValues.put(DayDetailModel.DAY_DETAIL_PROTECTION,
					dayDetailModel.getProtection());
			contentValues.put(DayDetailModel.DAY_DETAIL_HEIGHT,
					dayDetailModel.getHeight());
			contentValues.put(DayDetailModel.DAY_DETAIL_WEIGHT,
					dayDetailModel.getWeight());
			contentValues.put(DayDetailModel.DAY_DETAIL_TEMPERATURE,
					dayDetailModel.getTemp());
			contentValues.put(DayDetailModel.DAY_DETAIL_PROFILLE_ID,
					dayDetailModel.getProfileID());

			if (this.createRecord(DayDetailModel.DAY_DETAIL, null,
					contentValues) > 0) {
				rowCreated = true;

			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return rowCreated;
	}

	public boolean updateDayDetail(DayDetailModel dayDetailModel) {
		boolean rowUpdated = false;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(DayDetailModel.DAY_DETAIL_DATE, dayDetailModel
					.getDate().getTime());
			contentValues.put(DayDetailModel.DAY_DETAIL_NOTE,
					dayDetailModel.getNote());
			contentValues.put(DayDetailModel.DAY_DETAIL_INTIMATE,
					dayDetailModel.isIntimate());
			contentValues.put(DayDetailModel.DAY_DETAIL_PROTECTION,
					dayDetailModel.getProtection());
			contentValues.put(DayDetailModel.DAY_DETAIL_HEIGHT,
					dayDetailModel.getHeight());
			contentValues.put(DayDetailModel.DAY_DETAIL_WEIGHT,
					dayDetailModel.getWeight());
			contentValues.put(DayDetailModel.DAY_DETAIL_TEMPERATURE,
					dayDetailModel.getTemp());
			contentValues.put(DayDetailModel.DAY_DETAIL_PROFILLE_ID,
					dayDetailModel.getProfileID());
			if (this.updateRecord(
					DayDetailModel.DAY_DETAIL,
					contentValues,
					DayDetailModel.DAY_DETAIL_Id + "=" + dayDetailModel.getId(),
					null) > 0) {
				rowUpdated = true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return rowUpdated;
	}

	public boolean updateWeightAndTemp(DayDetailModel dayDetailModel) {
		boolean rowUpdated = false;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(DayDetailModel.DAY_DETAIL_TEMPERATURE,
					dayDetailModel.getTemp());
			contentValues.put(DayDetailModel.DAY_DETAIL_WEIGHT,
					dayDetailModel.getWeight());
			contentValues.put(DayDetailModel.DAY_DETAIL_HEIGHT,
					dayDetailModel.getHeight());

			if (this.updateRecord(
					DayDetailModel.DAY_DETAIL,
					contentValues,
					DayDetailModel.DAY_DETAIL_Id + " = "
							+ dayDetailModel.getId(), null) > 0)
				rowUpdated = true;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return rowUpdated;

	}

	public boolean addMoodSelectedForDayDetail(List<MoodSelected> interfaces) {
		boolean rowcreated = false;

		if (this.createMultipleRecordMoodSelected(MoodSelected.MOOD_SELECETED,
				null, interfaces) > 0)
			rowcreated = true;

		return rowcreated;
	}

	public boolean deleteAndAddMoodSelectedForDayDetail(int dayDetailId,
			List<MoodSelected> interfaces) {
		boolean rowcreated = false;
		if (this.deleteAndCreateMultipleRecordMoodSelected(
				MoodSelected.MOOD_SELECETED, MoodSelected.DAY_DETAIL_ID + " = "
						+ dayDetailId, null, MoodSelected.MOOD_SELECETED, null,
				interfaces) > 0)
			rowcreated = true;
		return rowcreated;
	}

	public boolean addSymtomSelectedForDayDetail(
			List<SymtomsSelectedModel> interfaces) {
		boolean rowcreated = false;

		if (this.createMultipleRecordSymptomSelected(
				SymtomsSelectedModel.SYMTOM_SELECTED, null, interfaces) > 0)
			rowcreated = true;

		return rowcreated;
	}

	public boolean deleteAndSymtomSelectedForDayDetail(int dayDetailId,
			List<SymtomsSelectedModel> interfaces) {
		boolean rowcreated = false;
		if (this.deleteAndCreateMultipleRecordSymptomSelected(
				SymtomsSelectedModel.SYMTOM_SELECTED,
				SymtomsSelectedModel.SYMTOM_SELECTED_DAY_DETAIL_ID + " = "
						+ dayDetailId, null,
				SymtomsSelectedModel.SYMTOM_SELECTED, null, interfaces) > 0)
			rowcreated = true;
		return rowcreated;
	}

	public List<PeriodTrackerModelInterface> getAllMedicineForDate(
			int dayDetailId) {

		String Sql = "SELECT * FROM " + Pills.MEDICINE + " WHERE "
				+ Pills.MEDICINE_DAY_DETAILS_ID + " = " + dayDetailId;

		return this.selectMulipleRecord(Pills.class, Sql);

	}
	public List<PeriodTrackerModelInterface> getTopMedicineForDate(
			int dayDetailId) {

		String Sql = "SELECT * FROM " + Pills.MEDICINE + " WHERE "
				+ Pills.MEDICINE_DAY_DETAILS_ID + " = " + dayDetailId+" LIMIT 1";

		return this.selectMulipleRecord(Pills.class, Sql);

	}

	public boolean addPillsDetail(Pills pills) {
		boolean rowCreated = false;
		ContentValues contentValues = new ContentValues();
		contentValues.put(Pills.MEDICINE_NAME, pills.getMedicineName());
		contentValues.put(Pills.MEDICINE_QUANTITY, pills.getQuantity());
		contentValues.put(Pills.MEDICINE_DAY_DETAILS_ID,
				pills.getDayDetailsId());
		if (this.createRecord(Pills.MEDICINE, null, contentValues) > 0)
			rowCreated = true;
		return rowCreated;

	}

	public void selectSymptomWeight(int symtomId, int dayDetailID) {

		String SQL = " select "
				+ SymtomsSelectedModel.SYMTOM_SELECTED_SYMTOM_WEIGHT + " from "
				+ SymtomsSelectedModel.SYMTOM_SELECTED + " Where "
				+ SymtomsSelectedModel.SYMTOM_SELECTED_ID + " = " + symtomId
				+ " AND " + SymtomsSelectedModel.SYMTOM_SELECTED_DAY_DETAIL_ID
				+ " = " + dayDetailID;

		this.selectRecord(SymptomsModel.class, SQL);
	}

	public List<PeriodTrackerModelInterface> getAllMood() {
		String Sql = "Select * From " + MoodDataModel.MOOD;

		return this.selectMulipleRecord(MoodDataModel.class, Sql);
	}

	public List<PeriodTrackerModelInterface> getAllSymtoms() {

		String Sql = "Select * From " + SymptomsModel.SYMTOMS;

		return this.selectMulipleRecord(SymptomsModel.class, Sql);

	}

	public List<PeriodTrackerModelInterface> getAllSelectedMood(int dayDetailId) {
		String Sql = "Select * From " + MoodSelected.MOOD_SELECETED + " where "
				+ MoodSelected.DAY_DETAIL_ID + "=" + dayDetailId;
		return this.selectMulipleRecord(MoodSelected.class, Sql);

	}

	public List<PeriodTrackerModelInterface> getAllSelectedMoodForProfileID(
			int profileId) {
		List<PeriodTrackerModelInterface> interfaces;

		String Sql = " Select  MS.* From  " + MoodSelected.MOOD_SELECETED
				+ " MS " + " INNER JOIN " + DayDetailModel.DAY_DETAIL + " DD "
				+ " ON " + " MS.Day_Details_Id " + " = " + " DD.Id "
				+ " where " + "DD.Profile_Id" + " = " + profileId;

		interfaces = this.selectMulipleRecord(MoodSelected.class, Sql);

		return interfaces;

	}

	public List<PeriodTrackerModelInterface> getAllSelectedSymptom(
			int dayDetailId) {
		String Sql = "Select * From " + SymtomsSelectedModel.SYMTOM_SELECTED
				+ " where "
				+ SymtomsSelectedModel.SYMTOM_SELECTED_DAY_DETAIL_ID + " ="
				+ dayDetailId;
		return this.selectMulipleRecord(SymtomsSelectedModel.class, Sql);

	}

	public List<PeriodTrackerModelInterface> getAllSelectedSymtomForProfileID(
			int profileId) {
		List<PeriodTrackerModelInterface> interfaces;

		String Sql = " Select  SS.* From  "
				+ SymtomsSelectedModel.SYMTOM_SELECTED + " SS "
				+ " INNER JOIN " + DayDetailModel.DAY_DETAIL + " DD " + " ON "
				+ " SS.Day_Details_Id " + " = " + " DD.Id " + " where "
				+ "DD.Profile_Id" + " = " + profileId;

		interfaces = this.selectMulipleRecord(SymtomsSelectedModel.class, Sql);

		return interfaces;

	}

	public List<PeriodTrackerModelInterface> getAllMedicineForProfileID(
			int profileId) {
		List<PeriodTrackerModelInterface> interfaces;

		String Sql = " Select  PS.* From  " + Pills.MEDICINE + " PS "
				+ " INNER JOIN " + DayDetailModel.DAY_DETAIL + " DD " + " ON "
				+ " PS.Day_Details_Id " + " = " + " DD.Id "
				+ " where " + "DD.Profile_Id" + " = " + profileId;

		interfaces = this.selectMulipleRecord(Pills.class, Sql);

		return interfaces;

	}

	public boolean deleteMoodForDay(int dayDetailId) {
		boolean deleted = false;
		if (this.deleteRecord(MoodSelected.MOOD_SELECETED,
				MoodSelected.DAY_DETAIL_ID + " = " + dayDetailId, null) > 0) {
			deleted = true;
		}
		return deleted;
	}

	public boolean deleteSymtomForDay(int dayDetailId) {
		boolean deleted = false;

		if (this.deleteRecord(SymtomsSelectedModel.SYMTOM_SELECTED,
				SymtomsSelectedModel.SYMTOM_SELECTED_DAY_DETAIL_ID + " = "
						+ dayDetailId, null) > 0) {
			deleted = true;
		}
		return deleted;
	}

	public PeriodTrackerModelInterface getDayDetail(Date date) {

		String SelcetSQl = "select * from " + DayDetailModel.DAY_DETAIL
				+ " Where " + DayDetailModel.DAY_DETAIL_DATE + " = "
				+ date.getTime()+" AND "+DBProjections.PT_PROFILE_ID+"="+PeriodTrackerObjectLocator.getInstance().getProfileId();
		return this.selectRecord(DayDetailModel.class, SelcetSQl);

	}

	public List<PeriodTrackerModelInterface> getDayDetailsForProfileId(
			int profileId) {

		String SelcetSQl = "select * from " + DayDetailModel.DAY_DETAIL
				+ " Where " + DayDetailModel.DAY_DETAIL_PROFILLE_ID + " = "
				+ profileId+ " order by " + DBProjections.DD_DATE + " DESC";
		return this.selectMulipleRecord(DayDetailModel.class, SelcetSQl);

	}

	public boolean deleteMedicineRecord(
			PeriodTrackerModelInterface modelInterface) {
		boolean deleted = false;
		Pills pill = (Pills) modelInterface;
		if (this.deleteRecord(Pills.MEDICINE,
				Pills.MEDICINE_ID + " = " + pill.getId(), null) > 0) {
			deleted = true;
		}
		return deleted;
	}
}
