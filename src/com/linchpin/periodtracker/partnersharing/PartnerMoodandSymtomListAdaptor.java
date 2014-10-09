package com.linchpin.periodtracker.partnersharing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.MoodDataModel;
import com.linchpin.periodtracker.model.MoodSelected;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.Pills;
import com.linchpin.periodtracker.model.SymptomsModel;
import com.linchpin.periodtracker.model.SymtomsSelectedModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.view.AddNoteView;

public class PartnerMoodandSymtomListAdaptor extends BaseAdapter
{
	
	String									className;
	
	List<PeriodTrackerModelInterface>		interfaces		= new ArrayList<PeriodTrackerModelInterface>();
	Context									context;
	SymptomsModel							symptomModel;
	MoodDataModel							moodDataModel;
	//	public List<SymtomsSelectedModel>		symtomsSelectedModels	= new ArrayList<SymtomsSelectedModel>();
	
	//	public List<MoodSelected>				moodSelecteds			= new ArrayList<MoodSelected>();
	
	public static HashMap<Integer, Integer>	moodRatingMap	= null;
	public static HashMap<Integer, Integer>	symtomRatingMap	= null;
	
	View									row;
	ImageView								image			= null;
	TextView								textView		= null;
	RatingBar								ratingBar;
	boolean									add				= false, put = false;
	int										longposition;
	Theme									t;
	
	public PartnerMoodandSymtomListAdaptor(List<PeriodTrackerModelInterface> interfaces, List<PeriodTrackerModelInterface> trackerModelInterfaces, String className, Context context)
	{
		this.className = className;
		this.interfaces.addAll(interfaces);
		this.context = context;
		if (trackerModelInterfaces != null)
		{
			if (className.equals(PeriodTrackerConstants.MOOD_BASE_FRAGMENT))
			{
				moodRatingMap = new HashMap<Integer, Integer>(interfaces.size());
				MoodDataModel dataModel = null;
				for (int i = 0; i < interfaces.size(); i++)
				{
					dataModel = (MoodDataModel) interfaces.get(i);
					for (PeriodTrackerModelInterface modelInterface : trackerModelInterfaces)
					{
						MoodSelected moodSelected = (MoodSelected) modelInterface;
						
						if (dataModel.getId() == moodSelected.getMoodId())
						{
							if (moodSelected.getMoodWeight() != 0)
							{
								//	moodSelecteds.add(moodSelected);
								moodRatingMap.put(dataModel.getId(), moodSelected.getMoodWeight());
							}
							else this.interfaces.remove(dataModel);
							
							break;
						}
					}
				}
			}
			if (className.equals(PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT))
			{
				symtomRatingMap = new HashMap<Integer, Integer>(interfaces.size());
				SymptomsModel dataModel = null;
				for (int i = 0; i < interfaces.size(); i++)
				{
					dataModel = (SymptomsModel) interfaces.get(i);
					for (PeriodTrackerModelInterface modelInterface : trackerModelInterfaces)
					{
						SymtomsSelectedModel symtomSelected = (SymtomsSelectedModel) modelInterface;
						if (dataModel.getId() == symtomSelected.getSymptomId())
						{
							if (symtomSelected.getSymptomWeight() != 0)
							{
								symtomRatingMap.put(dataModel.getId(), symtomSelected.getSymptomWeight());
								//	symtomsSelectedModels.add(symtomSelected);
							}
							else this.interfaces.remove(dataModel);
							break;
						}
					}
					
				}
				
			}
		}
		
	}/*
		
		*/
	
	/**
	* @return the symtomsSelectedModels
	*/
	/*
	public final List<SymtomsSelectedModel> getSymtomsSelectedModels()
	{
	return symtomsSelectedModels;
	}
	
	*//**
		* @param symtomsSelectedModels the symtomsSelectedModels to set
		*/
	/*
	public final void setSymtomsSelectedModels(List<SymtomsSelectedModel> symtomsSelectedModels)
	{
	this.symtomsSelectedModels = symtomsSelectedModels;
	}
	*/
	/**
	 * @return the moodSelecteds
	 */
	/*
	public final List<MoodSelected> getMoodSelecteds()
	{
	return moodSelecteds;
	}
	
	*//**
		* @param moodSelecteds the moodSelecteds to set
		*/
	/*
	public final void setMoodSelecteds(List<MoodSelected> moodSelecteds)
	{
	this.moodSelecteds = moodSelecteds;
	}
	*/
	@Override
	public int getCount()
	{
		return interfaces.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		System.out.println("Position" + position);
		return interfaces.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}
	
	private void applyTheme(View view)
	{
		
		t = Theme.getCurrentTheme(context, view);
		if (t != null)
		{
			/*setTextSize(16);
			setPadding(8, 0, 0, 0);
			setTextColor(t.getColor("heading_fg"));
			setBackgroundColor(t.getColor("heading_bg"));*/
			
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		longposition = position;
		System.out.println("getview" + position);
		row = convertView;
		if (row == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.symtoms_row, parent, false);
			ratingBar = (RatingBar) row.findViewById(R.id.ratingbar);			
			applyTheme(row);
			if (t != null)
			{
				t.applyTextColor(R.id.symptomstext, "text_color");
				t.applyBackgroundColor(R.id.content, "view_bg");
				t.applyBackgroundColor(R.id.content, "view_bg");
				ratingBar.setNumStars(3);
				LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
				stars.getDrawable(2).setColorFilter(t.getColor("co_star_seected_color"), PorterDuff.Mode.SRC_ATOP);
				stars.getDrawable(1).setColorFilter(t.getColor("co_star_nor_color"), PorterDuff.Mode.SRC_ATOP);
				stars.getDrawable(0).setColorFilter(t.getColor("co_star_nor_color"), PorterDuff.Mode.SRC_ATOP);
			}
		}
		
		ratingBar = (RatingBar) row.findViewById(R.id.ratingbar);
		image = (ImageView) row.findViewById(R.id.symtomimageicon);
		textView = (TextView) row.findViewById(R.id.symptomstext);
		
		if (className.equals(PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT))
		{
			symptomModel = (SymptomsModel) interfaces.get(position);
			
			row.findViewById(R.id.header);
			Uri uri = Uri.parse("android.resource://com.linchpin.myperiodtracker/drawable/" + symptomModel.getImageUri());
			image.setImageURI(uri);
			String packageName = context.getPackageName();
			int resId = context.getResources().getIdentifier(symptomModel.getSymptomLabelKey(), "string", packageName);
			textView.setText(context.getString(resId));
			ratingBar.setTag(symptomModel.getId());
			ratingBar.setRating(symtomRatingMap.get(symptomModel.getId()));
			
		}
		else if (className.equals(PeriodTrackerConstants.MOOD_BASE_FRAGMENT))
		{
			moodDataModel = (MoodDataModel) interfaces.get(position);
			// moodSelecteds.clear();
			
			Uri uri = Uri.parse("android.resource://com.linchpin.myperiodtracker/drawable/" + moodDataModel.getImageUri());
			image.setImageURI(uri);
			String packageName = context.getPackageName();
			int resId = context.getResources().getIdentifier(moodDataModel.getMoodLabel(), "string", packageName);
			textView.setText(context.getString(resId));
			ratingBar.setTag(moodDataModel.getId());
			ratingBar.setRating(moodRatingMap.get(moodDataModel.getId()));
			
		}
		ratingBar.setEnabled(false);		
		return row;
	}
	
}
