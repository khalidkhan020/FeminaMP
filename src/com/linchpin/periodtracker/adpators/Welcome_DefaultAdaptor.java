package com.linchpin.periodtracker.adpators;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;

public class Welcome_DefaultAdaptor extends BaseAdapter
{
	
	String[]	strings;
	Context		context;
	String		className;
	
	public Welcome_DefaultAdaptor(Context context, String[] strings2)
	{
		this.context = context;
		this.strings = strings2;
	}
	
	@Override
	public int getCount()
	{
		return strings.length;
	}
	
	@Override
	public Object getItem(int arg0)
	{
		return strings[arg0];
	}
	
	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}
	
	Theme	t;
	
	private void applyTheme(View v)
	{
		
		t = Theme.getCurrentTheme(context, v);
		if (t != null)
		{
			t.applyBackgroundColor(v, "view_bg");
			
			t.applyBackgroundDrawable(R.id.settingsback, "backbuttonselctor");
			
			t.applyTextColor(R.id.settinglistitem, "list_heading_fg");
			t.applyTextColor(R.id.settingsecondlistitem, "list_des_fg");
			t.applyTextColor(R.id.nextimage, "list_heading_fg");
			
		}
		
	}
	
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2)
	{
		TextView textView, textView2;
		
		final TextView imageviView;
		View row = convertView;
		if (row == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.settings_list_row, arg2, false);
			applyTheme(row);
		}
		
		textView = (TextView) row.findViewById(R.id.settinglistitem);
		textView.setText(strings[arg0]);
		textView2 = (TextView) row.findViewById(R.id.settingsecondlistitem);
		imageviView = (TextView) row.findViewById(R.id.nextimage);
		boolean haveNewapperance = APP.GLOBAL().getPreferences().getBoolean(APP.PREF.NEW_FETURE.key, true);
		boolean haveNewapperanceChangeIcon = APP.GLOBAL().getPreferences().getBoolean(APP.PREF.NEW_FETURE_CHANGE_ICO.key, true);
		
		if (strings[arg0].equals(context.getString(R.string.defaultVaules)))
		{
			textView2.setText(context.getString(R.string.changedefaultlengths));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
			
		}
		else if (strings[arg0].equals(context.getString(R.string.pregnancymode)))
		{
			textView2.setText(context.getString(R.string.areyoupregnant));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
			
		}
		else if (strings[arg0].equals(context.getString(R.string.apperence)))
		{
			textView2.setText(context.getString(R.string.changeapperence));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
			
			if (haveNewapperance)
			{
				row.findViewById(R.id.includenewfesturer).setVisibility(View.VISIBLE);
			}
			
		}
		else if (strings[arg0].equals(context.getString(R.string.show_tip)))
		{
			textView2.setText(context.getString(R.string.des_tip));
			if (APP.GLOBAL().getPreferences().getBoolean(APP.PREF.ENABLE_TIP.key, true))
			{
				imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.check2)) : t.getDrawableResource("check2"), null);
			}
			else
			{
				imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.uncheck2)) : t.getDrawableResource("uncheck2"), null);
			}
			imageviView.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View arg0)
				{
					APP.GLOBAL().getEditor().putBoolean(APP.PREF.ENABLE_TIP.key, !APP.GLOBAL().getPreferences().getBoolean(APP.PREF.ENABLE_TIP.key, true)).commit();
					if (APP.GLOBAL().getPreferences().getBoolean(APP.PREF.ENABLE_TIP.key, true))
					{
						imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.check2)) : t.getDrawableResource("check2"), null);
					}
					else
					{
						imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.uncheck2)) : t.getDrawableResource("uncheck2"), null);
					}
				}
			});
			if (haveNewapperance)
			{
				row.findViewById(R.id.includenewfesturer).setVisibility(View.VISIBLE);
			}
			
		}
		
		else if (strings[arg0].equals(context.getString(R.string.info)))
		{
			textView2.setText(context.getString(R.string.seeinfo));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
			
			if (haveNewapperance) row.findViewById(R.id.includenewfesturer).setVisibility(View.VISIBLE);
			
		}
		else if (strings[arg0].equals(context.getString(R.string.butfullversion)))
		{
			textView2.setText(context.getString(R.string.belowbutfullversion));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
			
			if (haveNewapperance) row.findViewById(R.id.includenewfesturer).setVisibility(View.VISIBLE);
		}
		else if (strings[arg0].equals(context.getString(R.string.userguide)))
		{
			textView2.setText(context.getString(R.string.userguide));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
		}
		else if (strings[arg0].equals(context.getString(R.string.regionalsettings)))
		{
			textView2.setText(context.getString(R.string.changedefaultRegionalsettings));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
		}
		else if (strings[arg0].equals(context.getString(R.string.passwordprotection)))
		{
			textView2.setText(context.getString(R.string.changedefaulpassword));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
		}
		else if (strings[arg0].equals(context.getString(R.string.theme)))
		{
			textView2.setText(context.getString(R.string.set_theme));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
		}
		else if (strings[arg0].equals(context.getString(R.string.hieghtunit)))
		{
			textView2.setText(context.getString(R.string.changedefaultlheightunit));
			imageviView.setText(PeriodTrackerObjectLocator.getInstance().getHeightUnit());
		}
		else if (strings[arg0].equals(context.getString(R.string.weightunit)))
		{
			textView2.setText(context.getString(R.string.changedefaultlweightunit));
			imageviView.setText(PeriodTrackerObjectLocator.getInstance().getWeighUnit());
		}
		else if (strings[arg0].equals(context.getString(R.string.bodytempratureunit)))
		{
			textView2.setText(context.getString(R.string.changedefaultlTempunit));
			imageviView.setText(PeriodTrackerObjectLocator.getInstance().getTempUnit());
		}
		else if (strings[arg0].equals(context.getString(R.string.cyclelength)))
		{
			textView2.setText(context.getString(R.string.changedefaultlcyclelegths));
			
			imageviView.setText(PeriodTrackerObjectLocator.getInstance().getCurrentCycleLength() + " " + context.getString(R.string.days));
		}
		else if (strings[arg0].equals(context.getString(R.string.Periodlength)))
		{
			textView2.setText(context.getString(R.string.changedefaultlperiodlengths));
			imageviView.setVisibility(View.VISIBLE);
			imageviView.setText(PeriodTrackerObjectLocator.getInstance().getCurrentPeriodLength() + " " + context.getString(R.string.days));
		}
		else if (strings[arg0].equals(context.getString(R.string.averagelength)))
		{
			textView2.setText(context.getString(R.string.averagelengthbelow));
			imageviView.setVisibility(View.VISIBLE);
			if (PeriodTrackerObjectLocator.getInstance().isAveraged()) imageviView.setCompoundDrawablesWithIntrinsicBounds(t == null ? context.getResources().getDrawable(R.drawable.check) : t.getDrawableResource("check2"), null, null, null);
			else imageviView.setCompoundDrawablesWithIntrinsicBounds(t == null ? context.getResources().getDrawable(R.drawable.uncheck) : t.getDrawableResource("uncheck2"), null, null, null);
			
		}
		else if (strings[arg0].equals(context.getString(R.string.ovulationdaylength)))
		{
			
			textView2.setText(context.getString(R.string.changedefaultlOvulationlengths));
			Spanned txt ;
			if (PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength() == 23)
			{
				txt = Html.fromHtml(PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength() + "<sup>" + context.getString(R.string.rddayofperiod).trim() + "</sup> " + context.getString(R.string.notificationday));
			}
			else if (PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength() == 21)
			{
				txt = Html.fromHtml(PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength() +  "<sup>" + context.getString(R.string.stdayofperiod).trim() + "</sup> " + context.getString(R.string.notificationday));
				
			}
			else if (PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength() == 22)
			{
				txt = Html.fromHtml(PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength() +  "<sup>" + context.getString(R.string.nddayofperiod).trim() + "</sup> " + context.getString(R.string.notificationday));
				
			}
			else
			{
				txt = Html.fromHtml(PeriodTrackerObjectLocator.getInstance().getCurrentOvualtionLength() +  "<sup>" + context.getString(R.string.thdayofperiod).trim() + "</sup> " + context.getString(R.string.notificationday));
				
			}
			imageviView.setText(txt);
		}
		else if (strings[arg0].equals(context.getString(R.string.changedateformat)))
		{
			textView2.setText(context.getString(R.string.changedefaultldateformat));
			
			imageviView.setText(new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat()).format(new Date()));
		}
		else if (strings[arg0].equals(context.getString(R.string.changelanugae)))
		{
			textView2.setText(context.getString(R.string.changedefaultLanuage));
			
			imageviView.setText(Utility.getLanguageFromLoacle(PeriodTrackerObjectLocator.getInstance().getAppLanguage(), context));
		}
		else if (strings[arg0].equals(context.getString(R.string.restoreandbackup)))
		{
			textView2.setText(context.getString(R.string.restoreandbackup));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
			
		}
		else if (strings[arg0].equals(context.getString(R.string.hieght)))
		{
			textView2.setText(context.getString(R.string.setdefaultheight));
			
			if (PeriodTrackerObjectLocator.getInstance().getHeightUnit() != null && PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() != null && !PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule().equals("0.0"))
			{
				if (PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(context.getString(R.string.inches)))
				{
					if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))
					{
						imageviView.setText(Utility.getStringFormatedNumber(String.valueOf((Utility.convertCentiMeterTOInches(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))))) + " " + PeriodTrackerObjectLocator.getInstance().getHeightUnit());
					}
				}
				else
				{
					if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule())) imageviView.setText(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() + " "
							+ PeriodTrackerObjectLocator.getInstance().getHeightUnit());
				}
				
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) && PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule().equals("0.0"))
				{
					imageviView.setVisibility(View.INVISIBLE);
				}
			}
			else
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) && PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule().equals("0.0"))
				{
					imageviView.setVisibility(View.INVISIBLE);
				}
				
			}
		}
		else if (strings[arg0].equals(context.getString(R.string.weight)))
		{
			textView2.setText(context.getString(R.string.setdefaultweight));
			
			if (null != PeriodTrackerObjectLocator.getInstance().getWeighUnit())
			{
				
				if (PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(context.getString(R.string.KG)))
				{
					
					if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
					{
						
						imageviView.setText(Utility.getStringFormatedNumber(String.valueOf((Utility.ConvertToKilogram(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))))) + " " + PeriodTrackerObjectLocator.getInstance().getWeighUnit());
						
					}
				}
				else
				{
					if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))
					{
						
						imageviView.setText(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() + " " + PeriodTrackerObjectLocator.getInstance().getWeighUnit());
					}
				}
				
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) && PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule().equals("0.0"))
				{
					imageviView.setVisibility(View.INVISIBLE);
				}
			}
			else
			{
				
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) && PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule().equals("0.0"))
				{
					imageviView.setVisibility(View.INVISIBLE);
				}
				
			}
			
		}
		else if (strings[arg0].equals(context.getString(R.string.temprature)))
		{
			textView2.setText(context.getString(R.string.setdefaulttemperature));
			
			if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit())
			{
				if (PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(context.getString(R.string.celsius)))
				{
					
					if (null != PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule())) imageviView.setText(Utility.getStringFormatedNumber(String.valueOf((Utility
							.ConvertToCelsius(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))))) + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
					
				}
				else
				{
					if (null != PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
					
					imageviView.setText(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
					
				}
			}
			else
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()))
				
				imageviView.setText(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule() + " " + PeriodTrackerObjectLocator.getInstance().getTempUnit());
				
			}
			
		}
		else if (strings[arg0].equals(context.getString(R.string.setpasscode)))
		{
			textView2.setText(context.getString(R.string.setpasscodebelow));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
			
		}
		else if (strings[arg0].equals(context.getString(R.string.changepasscode)))
		{
			textView2.setText(context.getString(R.string.changepasscodebelow));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
			
		}
		else if (strings[arg0].equals(context.getString(R.string.clearpasscode)))
		{
			textView2.setText(context.getString(R.string.clearpasscodebelow));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
			
		}
		else if (strings[arg0].equals(context.getString(R.string.restore)))
		{
			textView2.setText(context.getString(R.string.restorebelow));
			imageviView.setVisibility(View.INVISIBLE);
		}
		else if (strings[arg0].equals(context.getString(R.string.backup)))
		{
			textView2.setText(context.getString(R.string.backupbelow));
			imageviView.setVisibility(View.INVISIBLE);
		}
		else if (strings[arg0].equals(context.getString(R.string.notifications)))
		{
			textView2.setText(context.getString(R.string.setnotifications));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
			
		}
		else if (strings[arg0].equals(context.getString(R.string.period_alert)))
		{
			textView2.setText(context.getString(R.string.setnotificationsperiodalert));
			if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() != -1 && PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() != 1)
			{
				if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() == 0)
				{
					imageviView.setText(context.getString(R.string.ontheday));
				}
				else
				{
					imageviView.setText(String.valueOf(PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification()) + " " + context.getString(R.string.daysbefore));
					
				}
			}
			else if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() == 1)
			{
				imageviView.setText(String.valueOf(PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification()) + " " + context.getString(R.string.daybefore));
			}
			else if (PeriodTrackerObjectLocator.getInstance().getPeriodStartNotification() == -1)
			{
				imageviView.setVisibility(View.INVISIBLE);
			}
			
		}
		
		else if (strings[arg0].equals(context.getString(R.string.fertile_alert)))
		{
			textView2.setText(context.getString(R.string.setnotificationsfertle_alert));
			if (PeriodTrackerObjectLocator.getInstance().getFertilityNotification() != -1 && PeriodTrackerObjectLocator.getInstance().getFertilityNotification() != 1)
			{
				if (PeriodTrackerObjectLocator.getInstance().getFertilityNotification() == 0)
				{
					imageviView.setText(context.getString(R.string.ontheday));
				}
				else
				{
					imageviView.setText(PeriodTrackerObjectLocator.getInstance().getFertilityNotification() + " " + context.getString(R.string.daysbefore));
				}
			}
			else if (PeriodTrackerObjectLocator.getInstance().getFertilityNotification() == 1)
			{
				imageviView.setText(PeriodTrackerObjectLocator.getInstance().getFertilityNotification() + " " + context.getString(R.string.daysbefore));
				
			}
		}
		
		else if (strings[arg0].equals(context.getString(R.string.ovulation_alert)))
		{
			textView2.setText(context.getString(R.string.setnotificationsovulation_alert));
			if (PeriodTrackerObjectLocator.getInstance().getOvulationNotification() != -1 && PeriodTrackerObjectLocator.getInstance().getOvulationNotification() != 1)
			{
				if (PeriodTrackerObjectLocator.getInstance().getOvulationNotification() == 0)
				{
					imageviView.setText(context.getString(R.string.ontheday));
				}
				else
				{
					imageviView.setText(PeriodTrackerObjectLocator.getInstance().getOvulationNotification() + " " + context.getString(R.string.daysbefore));
				}
			}
			
			else if (PeriodTrackerObjectLocator.getInstance().getOvulationNotification() == 1)
			{
				imageviView.setText(PeriodTrackerObjectLocator.getInstance().getOvulationNotification() + " " + context.getString(R.string.daybefore));
				
			}/*
				* else
				* if(PeriodTrackerObjectLocator.getInstance().getOvulationNotification
				* ()==-1){ imageviView.setVisibility(View.INVISIBLE); }
				*/
			
		}
		else if (strings[arg0].equals(context.getString(R.string.app_iconandname)))
		{
			textView2.setText(context.getString(R.string.changeappiconandname));
			imageviView.setCompoundDrawablesWithIntrinsicBounds(null, null, t == null ? context.getResources().getDrawable((R.drawable.next_info)) : t.getDrawableResource("next_info"), null);
			imageviView.setText("");
			row.findViewById(R.id.includenewfesturer).setVisibility(View.GONE);
			
		}
		else if (strings[arg0].equals(context.getString(R.string.setdaysofweek)))
		{
			textView2.setText(context.getString(R.string.changedayofweekbelow));
			if (haveNewapperanceChangeIcon)
			{
				row.findViewById(R.id.includenewfesturer).setVisibility(View.VISIBLE);
			}
			
			int selectedPosition = 0;
			final String[] items =
			{
					context.getString(R.string.sunday), context.getString(R.string.monday), context.getString(R.string.saturady)
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
			
			imageviView.setText(items[selectedPosition]);
		}
		else
		{
			imageviView.setVisibility(View.INVISIBLE);
			textView.setVisibility(View.GONE);
			row.findViewById(R.id.includenewfesturer).setVisibility(View.GONE);
		}
		return row;
	}
	
}
