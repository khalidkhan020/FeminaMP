package com.linchpin.periodtracker.adpators;

import java.util.HashMap;
import java.util.List;

import android.R.anim;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;

public class Expendable_Welcome_Setting_Adapter extends BaseExpandableListAdapter
{
	private Context _context;
	Animation fadein,fade_out;
	private Boolean isexBoolean;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;
	private HashMap<String, List<String>> _listDataChildDetails;
	public Expendable_Welcome_Setting_Adapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData,HashMap<String, List<String>> listChildDataDetails) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
		this._listDataChildDetails=listChildDataDetails;
	}
	Theme	t;
	
	private void applyTheme(View v)
	{
		
		t = Theme.getCurrentTheme(_context, v);
		if (t != null)
		{
			t.applyTextColor(R.id.lblListItem, "text_color");
		}
		
	}
	private void applyTheme1(View v)
	{
		t = Theme.getCurrentTheme(_context, v);
		if (t != null)
		{
			t.applyTextColor(R.id.lblListHeader, "text_color");
		}
		
	}
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}
	
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TextView txtListChild = null,txtChildDetails,txtChildUnit;
		int child=childPosition;
		final String childText = (String) getChild(groupPosition, childPosition);
		final String childTextDetails=_listDataChildDetails.get(this._listDataHeader.get(groupPosition)).get(child);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = infalInflater.inflate(R.layout.welcome_setting_row, null);
			fadein=AnimationUtils.loadAnimation(_context, R.anim.fade_in);
		}
		txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);
		txtChildDetails=(TextView)convertView
				.findViewById(R.id.lblListItemDetails);
		txtChildUnit=(TextView)convertView
				.findViewById(R.id.lblListItemUnit);
		txtListChild.setText(childText);
		txtChildDetails.setText(childTextDetails);
		convertView.startAnimation(fadein);
		applyTheme(convertView);
		int selectedPosition;
		switch(groupPosition)
		{
			case 0:
				if(childPosition==0)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
					if (PeriodTrackerObjectLocator.getInstance().getHeightUnit() != null && PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() != null && !PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule().equals("0.0"))
					{
						if (PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(_context.getString(R.string.inches)))
						{
							if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))
							{
								txtChildUnit.setText(Utility.getStringFormatedNumber(String.valueOf((Utility.convertCentiMeterTOInches(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))))) + " " + PeriodTrackerObjectLocator.getInstance().getHeightUnit());
							}
						}
						else
						{
							if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))
								txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() + " "
										+ PeriodTrackerObjectLocator.getInstance().getHeightUnit());
						}
						
						/*if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) && PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule().equals("0.0"))
						{
							txtChildUnit.setVisibility(View.INVISIBLE);
						}*/
					}
					else
					{
						if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) && PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule().equals("0.0"))
						{
							txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()+PeriodTrackerObjectLocator.getInstance().getHeightUnit());
						}
						
					}
				}
				else if(childPosition==1)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
					if (null != PeriodTrackerObjectLocator.getInstance().getWeighUnit())
					{
						
						if (PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(_context.getString(R.string.KG)))
						{
							
							if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
							{
								
								txtChildUnit.setText(Utility.getStringFormatedNumber(String.valueOf((Utility.ConvertToKilogram(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))))) + " " + PeriodTrackerObjectLocator.getInstance().getWeighUnit());
								
							}
						}
						else
						{
							if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
							{
								
								txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() + " " + PeriodTrackerObjectLocator.getInstance().getWeighUnit());
							}
						}
						
						/*if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) && PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule().equals("0.0"))
						{
							txtChildUnit.setVisibility(View.INVISIBLE);
						}*/
					}
					else
					{
						
						if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) && PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule().equals("0.0"))
						{
							txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()+PeriodTrackerObjectLocator.getInstance().getWeighUnit());
							
						}
					}
				}
				else if(childPosition==2)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
					if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit())
					{
						if (PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(_context.getString(R.string.celsius)))
						{
							
							if (null != PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule())) txtChildUnit.setText(Utility.getStringFormatedNumber(String.valueOf((Utility
									.ConvertToCelsius(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))))) + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
							
						}
						else
						{
							if (null != PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
								
								txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
							
						}
					}
					else
					{
						if (null != PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
							
							txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
						
					}
				}
				break;
			case 1:
				if(childPosition==0)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
					if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() != -1 && PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() != 1)
					{
						if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() == 0)
						{
							txtChildUnit.setText(_context.getString(R.string.ontheday));
						}
						else
						{
							txtChildUnit.setText(String.valueOf(PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification()) + " " + _context.getString(R.string.daysbefore));
							
						}
					}
					else if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() == 1)
					{
						txtChildUnit.setText(String.valueOf(PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification()) + " " + _context.getString(R.string.daybefore));
					}
					else if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() == -1)
					{
						txtChildUnit.setText( _context.getString(R.string.nonotificationrequired));
					}
				}
				else if(childPosition==1)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
					if (PeriodTrackerObjectLocator.getInstance().getFertilityNotification() != -1 && PeriodTrackerObjectLocator.getInstance().getFertilityNotification() != 1)
					{
						if (PeriodTrackerObjectLocator.getInstance().getFertilityNotification() == 0)
						{
							txtChildUnit.setText(_context.getString(R.string.ontheday));
						}
						else
						{
							txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getFertilityNotification() + " " + _context.getString(R.string.daysbefore));
						}
					}
					else if (PeriodTrackerObjectLocator.getInstance().getFertilityNotification() == 1)
					{
						txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getFertilityNotification() + " " + _context.getString(R.string.daysbefore));
						
					}
					else if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() == -1)
					{
						txtChildUnit.setText( _context.getString(R.string.nonotificationrequired));
					}
				}
				else if(childPosition==2)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
					if (PeriodTrackerObjectLocator.getInstance().getOvulationNotification() != -1 && PeriodTrackerObjectLocator.getInstance().getOvulationNotification() != 1)
					{
						if (PeriodTrackerObjectLocator.getInstance().getOvulationNotification() == 0)
						{
							txtChildUnit.setText(_context.getString(R.string.ontheday));
						}
						else
						{
							txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getOvulationNotification() + " " + _context.getString(R.string.daysbefore));
						}
					}
					
					else if (PeriodTrackerObjectLocator.getInstance().getOvulationNotification() == 1)
					{
						txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getOvulationNotification() + " " + _context.getString(R.string.daybefore));
						
					}
					else if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() == -1)
					{
						txtChildUnit.setText( _context.getString(R.string.nonotificationrequired));
					}
					
				}
				break;
			case 2:
				if(childPosition==0)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
					txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getHeightUnit());
				}
				else if(childPosition==1)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
					txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getWeighUnit());
				}
				else if(childPosition==2)
				{
					txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getTempUnit());
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
				}
				else if(childPosition==3)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
					txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getAppLanguage());
				}
				else if(childPosition==4)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
					txtChildUnit.setText(PeriodTrackerObjectLocator.getInstance().getDateFormat());
				}
				break;
			case 3:
				if(childPosition==0)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? _context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
					txtChildUnit.setText("");
				}
				else if(childPosition==1)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
					selectedPosition = 0;
					final String[] items =
						{
							_context.getString(R.string.sunday), _context.getString(R.string.monday), _context.getString(R.string.saturady)
						};
					
					if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("0"))
					{
						selectedPosition = 0;
					}
					else if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("1"))
					{
						selectedPosition = 1;
					}
					else if (PeriodTrackerObjectLocator.getInstance().getDayOfWeek().equals("6"))
					{
						selectedPosition = 2;
					}
					
					txtChildUnit.setText(items[selectedPosition]);
				}
				else if(childPosition==2)
					
				{
					
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? _context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
					txtChildUnit.setText("");
				}
				
				break;
			case 4:
				if(childPosition==0)
				{
					txtChildUnit.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? _context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
					txtChildUnit.setText("");
				}
				break;
		}
		// TODO Auto-generated method stub
		
		return convertView;
		
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}
	
	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}
	
	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.welcome_setting_group_item, null);
		}
		
		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);
		applyTheme1(convertView);
		return convertView;
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	
}
