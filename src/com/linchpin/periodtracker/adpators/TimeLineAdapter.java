package com.linchpin.periodtracker.adpators;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.TimeLineModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodttracker.database.AddNoteDBHandler;
import com.linchpin.periodttracker.database.CalendarDBHandler;

public class TimeLineAdapter extends BaseAdapter
{
	Context				context;
	List<TimeLineModel>	periodTrackerModel;
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat	format		= new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat());
	SimpleDateFormat	sformate	= new SimpleDateFormat("MMM dd");
	Theme				t;
	AddNoteDBHandler							addNoteDBHandler;
	public List<PeriodTrackerModelInterface>	periodTrackerModelInterfacesSymtomSelected;
	public List<PeriodTrackerModelInterface>	periodTrackerModelInterfacesMoodSelected;

	private void applyTheme(View view)
	{
		t = Theme.getCurrentTheme(context, view);
		if (t != null)
		{
			t.applyTextColor(R.id.date, "text_color");
		}
	}
	
	public List<TimeLineModel> getPeriodTrackerModel()
	{
		return periodTrackerModel;
	}
	
	public void setPeriodTrackerModel(List<TimeLineModel> periodTrackerModel)
	{
		this.periodTrackerModel = periodTrackerModel;
	}
	
	public TimeLineAdapter(List<TimeLineModel> list, Context context, String className)
	{
		this.periodTrackerModel = list;
		this.context = context;
		addNoteDBHandler = new AddNoteDBHandler(context);
	}
	
	@Override
	public int getCount()
	{
		if (periodTrackerModel != null) return periodTrackerModel.size();
		else return 0;
	}
	
	@Override
	public Object getItem(int position)
	{
		return periodTrackerModel.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return 0;
	}
	
	/*private void toggleColor(ViewHohder v, boolean statpink)
	{
		if (statpink)
		{ //Theme theme=Theme.getCurrentTheme(context);
			//v.datePan.setBackgroundColor(context.getResources().getColor(R.color.tl_box_color2));
			//v.arrow.setImageDrawable(context.getResources().getDrawable(R.drawable.box_arrow_pink));
		}
		else
		{
			//v.datePan.setBackgroundColor(context.getResources().getColor(R.color.tl_box_color1));
			//v.arrow.setImageDrawable(context.getResources().getDrawable(R.drawable.box_arrow));
		}
	}*/
	
	static class ViewHohder
	{
		protected ListView	listViewforMoods, listViewforSymtoms;
		protected TextView	tempTextView, wTextView, note, /*header,*/date, day, /*empty_mood, empty_sym,*/datType;
		protected ImageView	intimate/*, pills, arrow*/, daylogo;
		protected LinearLayout	datePan, imgPan;
		
		public ViewHohder(View convertView)
		{
			this.imgPan = (LinearLayout) convertView.findViewById(R.id.img_pan);
			this.listViewforMoods = (ListView) convertView.findViewById(R.id.moods);
			this.listViewforSymtoms = (ListView) convertView.findViewById(R.id.symptoms);
			this.tempTextView = ((TextView) convertView.findViewById(R.id.temp));
			this.wTextView = ((TextView) convertView.findViewById(R.id.weight));
			this.note = ((TextView) convertView.findViewById(R.id.note));
			//	this.header = (TextView) convertView.findViewById(R.id.section);
			this.intimate = ((ImageView) convertView.findViewById(R.id.intimate));
			//	this.pills = ((ImageView) convertView.findViewById(R.id.pill));
			this.day = (TextView) convertView.findViewById(R.id.day);
			this.daylogo = (ImageView) convertView.findViewById(R.id.logo);
			//	this.arrow = (ImageView) convertView.findViewById(R.id.arrow);
			this.datePan = (LinearLayout) convertView.findViewById(R.id.date_pan);
			this.date = (TextView) convertView.findViewById(R.id.date);
			//			this.empty_mood = (TextView) convertView.findViewById(R.id.empty_mood);
			//			this.empty_sym = (TextView) convertView.findViewById(R.id.empty_sym);
			this.datType = (TextView) convertView.findViewById(R.id.day_type);
		}
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHohder hohder;
		TimeLineModel prev = null;
		TimeLineModel next = null;
		
		TimeLineModel tempLogModel = (TimeLineModel) periodTrackerModel.get(position);
		if (position != 0) prev = (TimeLineModel) periodTrackerModel.get(position - 1);
		if (position != periodTrackerModel.size() - 1) next = (TimeLineModel) periodTrackerModel.get(position + 1);
		if (prev != null && next != null && (prev.fertility_end || prev.ovulaton) && (next.fertility_start || next.ovulaton)) tempLogModel.fertility = true;
		
		CalendarDBHandler calendarDBHandler = new CalendarDBHandler(context);
		
		final Date date = new Date(tempLogModel.date);
		String label = (String) DateFormat.format("MMM, yyyy", date);
		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.partner_timeline_item, parent, false);
			applyTheme(convertView);
			hohder = new ViewHohder(convertView);
			convertView.setTag(hohder);
		}
		else hohder = (ViewHohder) convertView.getTag();
		//toggleColor(hohder, position % 2 == 0);
		//Setting header
		//		if (position == 0)
		//		{
		//			hohder.header.setText(label);
		//			hohder.header.setVisibility(View.VISIBLE);
		//		}
		//		else if (!label.equals((String) DateFormat.format("MMM, yyyy", new Date(((TimeLineModel) periodTrackerModel.get(position - 1)).date))))
		//		{
		//			hohder.header.setText(label);
		//			hohder.header.setVisibility(View.VISIBLE);
		//		}
		//		else hohder.header.setVisibility(View.GONE);
		
		if (tempLogModel.note != null && !tempLogModel.note.trim().equals(""))
		{
			hohder.note.setText(tempLogModel.note.trim());
			hohder.note.setTextColor(context.getResources().getColor(R.color.default_timeline_fg));
		}
		else
		{
			hohder.note.setVisibility(View.GONE);//sazid
			hohder.note.setTextColor(context.getResources().getColor(R.color.default_timeline_fg_no));
			hohder.note.setText(context.getString(R.string.no_notes));
		}
		if (tempLogModel.temprature != 0f && tempLogModel.temprature > 80) if (PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(context.getResources().getString(R.string.celsius))) hohder.tempTextView.setText("Temp: "
				+ (Utility.getStringFormatedNumber(String.valueOf((Utility.ConvertToCelsius(tempLogModel.temprature)))) + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit()));
		else hohder.tempTextView.setText("Temp: " + Utility.getStringFormatedNumber(String.valueOf(tempLogModel.temprature)) + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
		else if (PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(context.getResources().getString(R.string.celsius))) hohder.tempTextView.setText("Temp: "
				+ (Utility.getStringFormatedNumber(String.valueOf((Utility.ConvertToCelsius(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))))) + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit()));
		
		else hohder.tempTextView.setText("Temp: " + (Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()) + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit()));
		
		if (PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(context.getResources().getString(R.string.KG))) if (tempLogModel.weight > PeriodTrackerConstants.MIN_WEIGHT_IN_KG) hohder.wTextView.setText((Utility.getStringFormatedNumber(String.valueOf(Utility
				.ConvertToKilogram(tempLogModel.weight))) + " " + PeriodTrackerObjectLocator.getInstance().getWeighUnit()));
		else hohder.wTextView.setText((Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule())))) + " " + PeriodTrackerObjectLocator.getInstance().getWeighUnit()));
		else if (tempLogModel.weight > PeriodTrackerConstants.MIN_WEIGHT_IN_LB) hohder.wTextView.setText((Utility.getStringFormatedNumber(String.valueOf((tempLogModel.weight))) + " " + PeriodTrackerObjectLocator.getInstance().getWeighUnit()));
		else hohder.wTextView.setText((Utility.getStringFormatedNumber(String.valueOf(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule())) + " " + PeriodTrackerObjectLocator.getInstance().getWeighUnit()));
		if (tempLogModel.intimate) hohder.intimate.setVisibility(View.VISIBLE);
		else hohder.intimate.setVisibility(View.GONE);
		//		if (tempLogModel.pills) hohder.pills.setVisibility(View.VISIBLE);
		//		else hohder.pills.setVisibility(View.GONE);
		
		if (tempLogModel.period_end)
		{
			hohder.datType.setText(context.getText(R.string.period_end));
			hohder.daylogo.setImageResource(R.drawable.period1);
		}
		else if (tempLogModel.period_start)
		{
			hohder.datType.setText(context.getText(R.string.period_start));
			hohder.daylogo.setImageResource(R.drawable.period1);
		}
		else if (tempLogModel.period)
		{
			hohder.datType.setText(context.getText(R.string.period));
			hohder.daylogo.setImageResource(R.drawable.period1);
		}
		else if (tempLogModel.fertility_start)
		{
			hohder.datType.setText(context.getText(R.string.fertility_start));
			hohder.daylogo.setImageResource(R.drawable.firtilty_logo);
		}
		else if (tempLogModel.fertility_end)
		{
			hohder.datType.setText(context.getText(R.string.fertility_end));
			hohder.daylogo.setImageResource(R.drawable.firtilty_logo);
		}
		else if (tempLogModel.ovulaton)
		{
			hohder.datType.setText(context.getText(R.string.ovulation));
			hohder.daylogo.setImageResource(R.drawable.ovalution_logo);
		}
		else if (tempLogModel.fertility)
		{
			hohder.datType.setText(context.getText(R.string.fertileday));
			hohder.daylogo.setImageResource(R.drawable.firtilty_logo);
		}
		else
		{
			hohder.datType.setText(context.getText(R.string.regularday));
			hohder.daylogo.setImageResource(R.drawable.done);
		}
		hohder.date.setText(sformate.format(date) + "");
		hohder.day.setText(DateFormat.format("EEEE", date) + "");
		//		hohder.empty_mood.setVisibility(View.GONE);
		//		hohder.empty_sym.setVisibility(View.GONE);//sazid
		//hohder.empty_mood.setText(context.getString(R.string.no_mood));
		//hohder.empty_sym.setText(context.getString(R.string.no_sym));
		if (tempLogModel.id != -1)
		{
			periodTrackerModelInterfacesSymtomSelected = addNoteDBHandler.getAllSelectedSymptom(tempLogModel.id);
			periodTrackerModelInterfacesMoodSelected = addNoteDBHandler.getAllSelectedMood(tempLogModel.id);
			//List<PeriodTrackerModelInterface> interfaces = calendarDBHandler.getMoodListforMoodId(tempLogModel.moodSelectedsMap);
			List<PeriodTrackerModelInterface> interfaces = calendarDBHandler.getMoodListforDayDetailId(tempLogModel.id);
			if (null != interfaces && interfaces.size() > 0)
			{
				hohder.listViewforMoods.setAdapter(new CalendarMoodandSymtomListAdaptor(interfaces, periodTrackerModelInterfacesMoodSelected, PeriodTrackerConstants.MOOD_BASE_FRAGMENT, context));
				
				//				hohder.empty_mood.setVisibility(View.GONE);
				hohder.imgPan.setVisibility(View.VISIBLE);
			}
			else
			{
				//				hohder.empty_mood.setVisibility(View.VISIBLE);
				hohder.imgPan.setVisibility(View.GONE);
			}
			//interfaces = calendarDBHandler.getSymtomListforSymptomID(tempLogModel.symtomsSelectedModelsMap);
			List<PeriodTrackerModelInterface> interfaces1 = calendarDBHandler.getSymtomListforDayDetailId(tempLogModel.id);
			if (null != interfaces1 && interfaces1.size() > 0)
			{
				hohder.listViewforSymtoms.setAdapter(new CalendarMoodandSymtomListAdaptor(interfaces1,periodTrackerModelInterfacesSymtomSelected , PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT, context));
				
				//				convertView.findViewById(R.id.empty_sym).setVisibility(View.GONE);
				hohder.imgPan.setVisibility(View.VISIBLE);
			}
			else
			{
				//				hohder.empty_sym.setVisibility(View.VISIBLE);
				hohder.imgPan.setVisibility(View.GONE);
			}
		}
		else
		{
			//			hohder.empty_mood.setVisibility(View.VISIBLE);
			hohder.imgPan.setVisibility(View.GONE);
			//			hohder.empty_sym.setVisibility(View.VISIBLE);
			hohder.imgPan.setVisibility(View.GONE);
		}
		return convertView;
	}
	
}