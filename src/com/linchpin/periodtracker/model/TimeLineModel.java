package com.linchpin.periodtracker.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.ConstantsKey;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.utlity.APP.PARTNER;
import com.linchpin.periodttracker.database.AddNoteDBHandler;
import com.linchpin.periodttracker.database.CalendarDBHandler;
import com.linchpin.periodttracker.database.ImportDatabase;
import com.linchpin.periodttracker.database.PeriodLogDBHandler;

public class TimeLineModel implements PeriodTrackerModelInterface
{
	public int										id							= -1;
	public boolean									period						= false;
	public boolean									period_start				= false;
	public boolean									period_end					= false;
	
	public boolean									ovulaton					= false;
	public boolean									fertility_start				= false;
	public boolean									fertility_end				= false;
	public boolean									fertility					= false;
	public boolean									pills						= false;
	public boolean									pregnancy;
	public long										date;
	public String									note;
	public boolean									intimate;
	public int										protection;
	public float									weight;
	public float									temprature;
	public float									height;
	Context											context;
	private PeriodLogDBHandler						periodLogDBHandler;
	private AddNoteDBHandler						addNoteDBHandler;
	public List<PeriodTrackerModelInterface>		moodSelectedsMap			= new ArrayList<PeriodTrackerModelInterface>();
	public ArrayList<PeriodTrackerModelInterface>	symtomsSelectedModelsMap	= new ArrayList<PeriodTrackerModelInterface>();
	
	public ArrayList<PeriodTrackerModelInterface>	medicineMap					= new ArrayList<PeriodTrackerModelInterface>();
	
	public TimeLineModel(Context ctx)
	{
		this.context = ctx;
		this.periodLogDBHandler = new PeriodLogDBHandler(ctx);
		this.addNoteDBHandler = new AddNoteDBHandler(ctx);
		this.note = ctx.getString(R.string.addnote);
	}
	
	public TimeLineModel()
	{
	}
	
	@Override
	public TimeLineModel createModel(Cursor cursor)
	{
		this.id = cursor.getInt(0);
		this.date = cursor.getLong(1);
		Date dakte = new Date(this.date);
		this.note = cursor.getString(2);
		this.intimate = cursor.getInt(3) > 0;
		this.protection = cursor.getInt(4);
		this.weight = cursor.getFloat(5);
		this.temprature = cursor.getFloat(6);
		this.height = cursor.getFloat(7);
		int temp = periodLogDBHandler.isDateBetweenPeriods(this.date);
		
		this.period = temp > 0;
		this.period_end = temp == 1;
		this.period_start = temp == 2;
		this.pills = false;
		
		List<PeriodTrackerModelInterface> tm = addNoteDBHandler.getTopMedicineForDate(this.id);
		if (tm.size() > 0) this.pills = true;
		return this;
	}
	
	public static void createModel()
	{
		APP.GLOBAL().getTimeLineModels().clear();
		APP.GLOBAL().getTimeLineMaps().clear();
		for (DayDetailModel model : APP.GLOBAL().getPartnerDDModels())
		{
			TimeLineModel timeLineModel = new TimeLineModel();
			timeLineModel.id = model.getId();
			timeLineModel.date = model.getDate().getTime();
			timeLineModel.note = model.getNote();
			timeLineModel.intimate = model.isIntimate();
			timeLineModel.protection = model.getProtection();
			timeLineModel.weight = model.getWeight();
			timeLineModel.temprature = model.getTemp();
			
			timeLineModel.height = model.getHeight();
			timeLineModel.period_start = model.getPeriod()==1;
			timeLineModel.period = model.getPeriod()>0;			timeLineModel.period_end = model.getPeriod()==2;
			
			timeLineModel.medicineMap = model.medicineMap;
			timeLineModel.symtomsSelectedModelsMap = model.symtomsSelectedModelsMap;
			timeLineModel.moodSelectedsMap = model.moodSelectedsMap;
			if (timeLineModel.medicineMap != null && timeLineModel.medicineMap.size() > 0) timeLineModel.pills = true;
			APP.GLOBAL().getTimeLineModels().add(timeLineModel);
			APP.GLOBAL().getTimeLineMaps().put(timeLineModel.date, timeLineModel);
		}
		PeriodLogModel logModel;
		for (PeriodLogModel modl : APP.GLOBAL().getPartnerPLModels())
		{
			logModel = (PeriodLogModel) modl;
			if (!logModel.isPregnancy() && logModel.getEndDate() != null && logModel.getEndDate().getTime() > (new Date("1/1/1990")).getTime())
			{
				TimeLineModel lineModel = new TimeLineModel();
				lineModel.date = logModel.getStartDate().getTime();
				lineModel.period_start = true;
				lineModel.period = true;
				lineModel.pregnancy = logModel.isPregnancy();
				
				APP.GLOBAL().getTimeLineModels().add(lineModel);
				APP.GLOBAL().getTimeLineMaps().put(lineModel.date, lineModel);
				
				lineModel = new TimeLineModel();
				lineModel.date = logModel.getEndDate().getTime();
				lineModel.period_end = true;
				lineModel.period = true;
				lineModel.pregnancy = logModel.isPregnancy();
				APP.GLOBAL().getTimeLineModels().add(lineModel);
				APP.GLOBAL().getTimeLineMaps().put(lineModel.date, lineModel);
				
				lineModel = new TimeLineModel();
				
				long cyclelength = APP.GLOBAL().getPreferences().getInt(PARTNER.FIELDS.CYCLE_LENGTH.key, 28);
				int periodlength = APP.GLOBAL().getPreferences().getInt(PARTNER.FIELDS.PERIOD_LENGTH.key, 4);
				
				lineModel.date = Utility.addDays(logModel.getStartDate(), APP.GLOBAL().getPreferences().getInt(PARTNER.FIELDS.OVULATION_LENGTH.key, 14)).getTime();
				lineModel.pregnancy = logModel.isPregnancy();
				lineModel.ovulaton = true;
				APP.GLOBAL().getTimeLineModels().add(lineModel);
				APP.GLOBAL().getTimeLineMaps().put(lineModel.date, lineModel);
				
				lineModel = new TimeLineModel();
				lineModel.date = Utility.addDays(logModel.getStartDate(), APP.GLOBAL().getPreferences().getInt(PARTNER.FIELDS.OVULATION_LENGTH.key, 14)-6).getTime();
				lineModel.pregnancy = logModel.isPregnancy();
				lineModel.fertility_start = true;
				APP.GLOBAL().getTimeLineModels().add(lineModel);
				APP.GLOBAL().getTimeLineMaps().put(lineModel.date, lineModel);
				
				lineModel = new TimeLineModel();
				lineModel.date = Utility.addDays(logModel.getStartDate(), APP.GLOBAL().getPreferences().getInt(PARTNER.FIELDS.OVULATION_LENGTH.key, 14)+5).getTime();
				lineModel.pregnancy = logModel.isPregnancy();
				lineModel.fertility_end = true;
				APP.GLOBAL().getTimeLineModels().add(lineModel);				
				APP.GLOBAL().getTimeLineMaps().put(lineModel.date, lineModel);
				
			}
		}
	}
	
}
