package com.linchpin.periodtracker.adpators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
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

public class MoodAndSymtomsAdaptor extends BaseAdapter
{
	
	String											className;
	HashMap<Integer, PeriodTrackerModelInterface>	interfacesMap;
	List<PeriodTrackerModelInterface>				interfaces;
	Context											context;
	SymptomsModel									symptomModel;
	MoodDataModel									moodDataModel;
	public List<SymtomsSelectedModel>				symtomsSelectedModels	= new ArrayList<SymtomsSelectedModel>();
	
	public List<MoodSelected>						moodSelecteds			= new ArrayList<MoodSelected>();
	
	public List<PeriodTrackerModelInterface>		trackerModelInterfaces;
	public static HashMap<Integer, Integer>			moodRatingMap			= null;
	public static HashMap<Integer, Integer>			symtomRatingMap			= null;
	DayDetailModel									dayDetailModel;
	View											row;
	ImageView										image					= null;
	TextView										textView				= null;
	RatingBar										ratingBar;
	boolean											add						= false, put = false;
	int												longposition;
	
	public MoodAndSymtomsAdaptor(List<PeriodTrackerModelInterface> modelInterfaces, List<PeriodTrackerModelInterface> trackerModelInterfaces, String className, Context context)
	{
		this.className = className;
		this.interfaces = modelInterfaces;
		interfacesMap = new HashMap<Integer, PeriodTrackerModelInterface>(modelInterfaces.size());
		if (className.equals(PeriodTrackerConstants.MOOD_BASE_FRAGMENT))
		{
			for (PeriodTrackerModelInterface modelInterface : modelInterfaces)
			{
				MoodDataModel dataModel = (MoodDataModel) modelInterface;
				interfacesMap.put(dataModel.getId(), modelInterface);
			}
		}
		if (className.equals(PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT))
		{
			for (PeriodTrackerModelInterface modelInterface : modelInterfaces)
			{
				SymptomsModel dataModel = (SymptomsModel) modelInterface;
				interfacesMap.put(dataModel.getId(), modelInterface);
			}
		}
		
		this.context = context;
		this.trackerModelInterfaces = trackerModelInterfaces;
		
		if (trackerModelInterfaces != null)
		{
			if (className.equals(PeriodTrackerConstants.MOOD_BASE_FRAGMENT))
			{
				moodRatingMap = new HashMap<Integer, Integer>(modelInterfaces.size());
				MoodDataModel dataModel = null;
				for (int i = 0; i < modelInterfaces.size(); i++)
				{
					dataModel = (MoodDataModel) modelInterfaces.get(i);
					for (PeriodTrackerModelInterface modelInterface : trackerModelInterfaces)
					{
						MoodSelected moodSelected = (MoodSelected) modelInterface;
						if (dataModel.getId() == moodSelected.getMoodId())
						{
							moodRatingMap.put(dataModel.getId(), moodSelected.getMoodWeight());
							moodSelecteds.add(moodSelected);
							put = true;
							break;
						}
						else
						{
							put = false;
							continue;
						}
					}
					if (!put)
					{
						moodRatingMap.put(dataModel.getId(), 0);
					}
					else
					{
						put = false;
					}
				}
			}
			if (className.equals(PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT))
			{
				symtomRatingMap = new HashMap<Integer, Integer>(modelInterfaces.size());
				SymptomsModel dataModel = null;
				for (int i = 0; i < modelInterfaces.size(); i++)
				{
					dataModel = (SymptomsModel) modelInterfaces.get(i);
					for (PeriodTrackerModelInterface modelInterface : trackerModelInterfaces)
					{
						SymtomsSelectedModel symtomSelected = (SymtomsSelectedModel) modelInterface;
						if (dataModel.getId() == symtomSelected.getSymptomId())
						{
							symtomRatingMap.put(dataModel.getId(), symtomSelected.getSymptomWeight());
							symtomsSelectedModels.add(symtomSelected);
							put = true;
							break;
						}
						else
						{
							continue;
						}
					}
					if (!put)
					{
						symtomRatingMap.put(dataModel.getId(), 0);
					}
					else
					{
						put = false;
					}
				}
				
			}
		}
		
	}
	
	/**
	 * @return the symtomsSelectedModels
	 */
	public final List<SymtomsSelectedModel> getSymtomsSelectedModels()
	{
		return symtomsSelectedModels;
	}
	
	/**
	 * @param symtomsSelectedModels the symtomsSelectedModels to set
	 */
	public final void setSymtomsSelectedModels(List<SymtomsSelectedModel> symtomsSelectedModels)
	{
		this.symtomsSelectedModels = symtomsSelectedModels;
	}
	
	/**
	 * @return the moodSelecteds
	 */
	public final List<MoodSelected> getMoodSelecteds()
	{
		return moodSelecteds;
	}
	
	/**
	 * @param moodSelecteds the moodSelecteds to set
	 */
	public final void setMoodSelecteds(List<MoodSelected> moodSelecteds)
	{
		this.moodSelecteds = moodSelecteds;
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method st
		System.out.println("interfacesize" + interfaces.size());
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
	
	Theme	t;
	
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
		// TODO Auto-generated method stub
		
		longposition = position;
		System.out.println("getview" + position);
		row = convertView;
		dayDetailModel = ((AddNoteView) context).dayDetailModel;
		if (className.equals(PeriodTrackerConstants.PILLS_FRAGEMNT))
		{
			if (row == null)
			{
				
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.pillsrow, parent, false);
				textView = (TextView) row.findViewById(R.id.pillstextInlist);
				TextView tView = (TextView) row.findViewById(R.id.pillstextquntity);
				Pills pills = (Pills) interfaces.get(position);
				textView.setText(pills.getMedicineName());
				tView.setText(String.valueOf(pills.getQuantity()));
		}
	}
		else
		{
			
			if (row == null)
			{
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.symtoms_row, parent, false);
				 ratingBar = (RatingBar) row.findViewById(R.id.ratingbar);
				applyTheme(row);
				if(t!=null)
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
				//
			}
			
			ratingBar = (RatingBar) row.findViewById(R.id.ratingbar);
			image = (ImageView) row.findViewById(R.id.symtomimageicon);
			textView = (TextView) row.findViewById(R.id.symptomstext);
			
			if (className.equals(PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT))
			{
				symptomModel = (SymptomsModel) interfaces.get(position);
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
			
			image.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					View s = (View) v.getParent();
					ratingBar = (RatingBar) s.findViewById(R.id.ratingbar);
					ratingBar.setRating(0f);
				}
			});
			textView.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					textView.getText();
					View s = (View) v.getParent();
					ratingBar = (RatingBar) s.findViewById(R.id.ratingbar);
					ratingBar.setRating(0f);
				}
			});
			ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener()
			{
				
				@Override
				public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
				{
					// TODO Auto-generated method stub
					
					if (className.equals(PeriodTrackerConstants.MOOD_BASE_FRAGMENT))
					{
						moodDataModel = (MoodDataModel) interfacesMap.get(Integer.parseInt(ratingBar.getTag().toString()));
						if (moodSelecteds.size() > 0)
						{
							for (MoodSelected moodSelected : moodSelecteds)
							{
								if (moodSelected.getMoodId() == moodDataModel.getId())
								{
									moodSelected.setMoodWeight((int) rating);
									moodRatingMap.put(moodDataModel.getId(), (int) rating);
									add = true;
									break;
								}
								else
								{
									// add = false;
									continue;
								}
								
							}
							if (!add)
							{
								moodSelecteds.add(new MoodSelected(0, dayDetailModel.getId(), moodDataModel.getId(), (int) rating));
								moodRatingMap.put(moodDataModel.getId(), (int) rating);
							}
							else
							{
								add = false;
								
							}
						}
						else
						{
							
							moodSelecteds.add(new MoodSelected(0, dayDetailModel.getId(), moodDataModel.getId(), (int) rating));
							moodRatingMap.put(moodDataModel.getId(), (int) rating);
						}
					}
					else if (className.equals(PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT))
					{
						symptomModel = (SymptomsModel) interfacesMap.get(Integer.parseInt((ratingBar.getTag().toString())));
						
						if (symtomsSelectedModels.size() > 0)
						{
							for (SymtomsSelectedModel symtomSelected : symtomsSelectedModels)
							{
								if (symtomSelected.getSymptomId() == symptomModel.getId())
								{
									symtomSelected.setSymptomWeight((int) rating);
									symtomRatingMap.put(symptomModel.getId(), (int) rating);
									add = true;
									break;
								}
								else
								{
									add = false;
									continue;
								}
								
							}
							if (!add)
							{
								symtomsSelectedModels.add(new SymtomsSelectedModel(0, symptomModel.getId(), dayDetailModel.getId(), (int) rating));
								symtomRatingMap.put(symptomModel.getId(), (int) rating);
							}
							else
							{
								add = false;
							}
						}
						else
						{
							symtomsSelectedModels.add(new SymtomsSelectedModel(0, symptomModel.getId(), dayDetailModel.getId(), (int) rating));
							symtomRatingMap.put(symptomModel.getId(), (int) rating);
						}
						
					}
					
				}
			});
			
		}
		
		return row;
	}
	
}
