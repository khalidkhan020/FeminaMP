package com.linchpin.periodtracker.adpators;

import java.util.List;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.MoodDataModel;
import com.linchpin.periodtracker.model.MoodSelected;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.SymptomsModel;
import com.linchpin.periodtracker.model.SymtomsSelectedModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.view.AddNoteView;
import com.linchpin.periodttracker.database.AddNoteDBHandler;

public class CalendarMoodandSymtomListAdaptor extends BaseAdapter
{
	
	List<PeriodTrackerModelInterface>	interfaces;
	List<PeriodTrackerModelInterface>	selected;
	String								className;
	Context								context;
	Theme                                t;
	boolean isfronTimeLine=false;
	public CalendarMoodandSymtomListAdaptor(List<PeriodTrackerModelInterface> interfaces, String className, Context context)
	{
		// TODO Auto-generated constructor stub
		this.interfaces = interfaces;
		this.className = className;
		this.context = context;
	}
	public CalendarMoodandSymtomListAdaptor(List<PeriodTrackerModelInterface> interfaces,List<PeriodTrackerModelInterface> selected, String className, Context context)
	{
		// TODO Auto-generated constructor stub
		this.interfaces = interfaces;
		this.className = className;
		this.context = context;
		this.selected=selected;
		isfronTimeLine=true;
	}
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return interfaces.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return interfaces.get(position);
		
	}
	
	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		View rowView = convertView;
		ImageView imageView = null;
		RatingBar bar = null;
		TextView lable = null;
		if (rowView == null)
		{
			//AddNoteView addNoteView = (AddNoteView) context;
			//dayDetailModel = addNoteView.dayDetailModel;
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(isfronTimeLine)
			{
			rowView = inflater.inflate(R.layout.partnersharing_mood_sympton_list_row, null);
			
			}
			else
			{
				rowView = inflater.inflate(R.layout.symtom_mood_calendarlist, null);
			}
		}
		rowView.setOnTouchListener(new View.OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// TODO Auto-generated method stub
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		imageView = (ImageView) rowView.findViewById(R.id.listitemonCalendar);
		if(isfronTimeLine)
		{
		bar = (RatingBar) rowView.findViewById(R.id.ratingbarmoodsymptons);
		t=Theme.getCurrentTheme(context, rowView);
		if(t!=null)
		{
			t.applyTextColor(R.id.lable, "text_color");
			t.applyBackgroundColor(R.id.content, "view_bg");
		    bar.setNumStars(3);
			LayerDrawable stars = (LayerDrawable) bar.getProgressDrawable();
			stars.getDrawable(2).setColorFilter(t.getColor("co_star_seected_color"), PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(1).setColorFilter(t.getColor("co_star_nor_color"), PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(0).setColorFilter(t.getColor("co_star_nor_color"), PorterDuff.Mode.SRC_ATOP);
		}
		lable = (TextView) rowView.findViewById(R.id.lable);
		}
		if (className.equals(PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT))
		{
			SymptomsModel symptomsModel = (SymptomsModel) interfaces.get(position);
			if (null != symptomsModel.getImageUri())
			{
				Uri uri = Uri.parse("android.resource://com.linchpin.myperiodtracker/drawable/" + symptomsModel.getImageUri());
				imageView.setImageURI(uri);
				
			}
			if(isfronTimeLine)
			{
				bar.setProgress(((SymtomsSelectedModel)selected.get(position)).getSymptomWeight());
				int resId = context.getResources().getIdentifier(symptomsModel.getSymptomLabelKey(), "string", APP.GLOBAL().getPackageName());
				lable.setText(context.getString(resId));
			}
			
			
			
		}
		else
		{
			MoodDataModel moodModel = (MoodDataModel) interfaces.get(position);
			if (null != moodModel.getImageUri())
			{
			Uri uri = Uri.parse("android.resource://com.linchpin.myperiodtracker/drawable/" + moodModel.getImageUri());
			imageView.setImageURI(uri);
			}
			
			if(isfronTimeLine)
			{
				
				bar.setProgress(((MoodSelected)selected.get(position)).getMoodWeight());
				int resId = context.getResources().getIdentifier(moodModel.getMoodLabel(), "string", APP.GLOBAL().getPackageName());
				lable.setText(context.getString(resId));
			}
			/*MoodSelected moodSelected = (MoodSelected) item;
			MoodDataModel moodModel = (MoodDataModel) calendarDBHandler.getMoodListforMoodId(moodSelected.getMoodId());
			if(moodSelected.getMoodWeight()!=0)
			sbMoods.append(moodModel.getImageUri() + " (" + moodSelected.getMoodWeight() + ") ");*/
			
		}
		return rowView;
	}
	
}
