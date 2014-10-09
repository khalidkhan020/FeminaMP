package com.linchpin.periodtracker.fragments;

import java.util.Arrays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.controller.ViewPageListener;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.Utility;
import com.linchpin.periodtracker.view.AddNoteView;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodttracker.database.AddNoteDBHandler;

public class WeightAndTemperatureFragment extends Fragment implements OnClickListener
{
	View										mView;
	float										currentwiegh	= 0, currenthiegh = 0, currenttemp = 0;
	
	public static WeightAndTemperatureFragment	weightFragment;
	
	public EditText								wTextWieght, wTextHiehgt, wTextTemperature;
	
	Button										uWieghtUnit, uHiehgtUnit, uTempUnit, save;
	
	TextView									wTextWeightBmi, wTextBmiMessage;
	ImageView										temPlusButton, tempMinusButton, weighPlusButton, weighMinusButton, heighPlusButton, heighMinusButton;
	
	//NumberFormat format = NumberFormat.getNumberInstance();
	
	AlertDialog.Builder							builder;
	
	//	DecimalFormat floatformat = new DecimalFormat("#.#");
	
	public DayDetailModel						dayDetailModel, pdayDayDetailModel;
	
	public AddNoteDBHandler						addNoteDBHandler;
	
	public PeriodTrackerModelInterface			periodTrackerModelInterface;
	String										as2;
	private void applyTheme(View view)
	{
		
		Theme t = Theme.getCurrentTheme(getActivity(),view);
		if (t != null)
		{			
			t.applyBackgroundColor(R.id.content, "view_bg");
			t.applyTextColor(R.id.textwieght, "text_color");
			t.applyTextColor(R.id.texthieght, "text_color");
			t.applyTextColor(R.id.texttemperature, "text_color");
			t.applyTextColor(R.id.textbmitext, "text_color");
			t.applyTextColor(R.id.textbmi, "text_color");
			t.applyTextColor(R.id.textbmimesg, "text_color");
			
		
			t.applyTextColor(R.id.temperaturevalue, "text_color");
			t.applyTextColor(R.id.editheightvalue, "text_color");
			t.applyTextColor(R.id.weightvalue, "text_color");
			
			t.applyBackgroundDrawable(R.id.weightunit, "mpt_note_txt_area_shp");
			t.applyBackgroundDrawable(R.id.heightunit, "mpt_note_txt_area_shp");
			t.applyBackgroundDrawable(R.id.temperatueunit, "mpt_note_txt_area_shp");
			
			t.applyBackgroundDrawable(R.id.heightvalueminus, "mpt_note_txt_area_shp");
			t.applyBackgroundDrawable(R.id.heightvalueplus, "mpt_note_txt_area_shp");
			t.applyBackgroundDrawable(R.id.weightvalueplus, "mpt_note_txt_area_shp");
			t.applyBackgroundDrawable(R.id.weightvalueminus, "mpt_note_txt_area_shp");	
			t.applyBackgroundDrawable(R.id.temperaturevalueplus, "mpt_note_txt_area_shp");
			t.applyBackgroundDrawable(R.id.temperaturevalueminus, "mpt_note_txt_area_shp");
	
			
			//temperaturevalueminus
			t.applyImageDrawable(R.id.weightvalueminus, "mpt_minus");
			t.applyImageDrawable(R.id.heightvalueminus, "mpt_minus");
			t.applyImageDrawable(R.id.weightvalueplus, "mpt_plus");
			t.applyImageDrawable(R.id.heightvalueplus, "mpt_plus");			
		t.applyImageDrawable(R.id.temperaturevalueminus, "mpt_minus");
		t.applyImageDrawable(R.id.temperaturevalueplus, "mpt_plus");
			
			t.applyBackgroundDrawable(R.id.weightvalue, "mpt_note_txt_area_shp");
			t.applyBackgroundDrawable(R.id.editheightvalue, "mpt_note_txt_area_shp");
			t.applyBackgroundDrawable(R.id.temperaturevalue, "mpt_note_txt_area_shp");
			
			
		}}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		if (null != weightFragment)
		{
			if (weightFragment.getView() == null)
			{
				mView = inflater.inflate(R.layout.weight, container, false);
			}
		}
		if (mView == null)
		{
			mView = inflater.inflate(R.layout.weight, container, false);
			
		}
		applyTheme(mView);
		int i = ((AddNoteView) getActivity()).mViewpager.getCurrentItem();
		
		mView.findViewById(R.id.weightinfobutton).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), HomeScreenHelp.class);
				intent.putExtra("classname", "weight");
				startActivity(intent);
			}
		});
		initWeightAndTempFragment();
		return mView;
		
	}
	
	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	public void SetBMIMsg(double bmi)
	{
		if (bmi >= 16 && bmi < 18.5)
		{
			wTextBmiMessage.setText(getResources().getString(R.string.underweight));// Underweight
		}
		else if (bmi >= 18.5 && bmi < 25)
		{
			wTextBmiMessage.setText(getResources().getString(R.string.normal)); // Normal
		}
		else if (bmi >= 25)
		{
			wTextBmiMessage.setText(getResources().getString(R.string.overweight));// Overweight
		}
		else
		{
			wTextBmiMessage.setText(getResources().getString(R.string.underweight));
		}
	}
	
	public void initWeightAndTempFragment()
	{
		
		if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) && Float.valueOf(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) != 0)
		{
			currenthiegh = Float.valueOf(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule());
		}
		else
		{
			if (null != PeriodTrackerObjectLocator.getInstance().getHeightUnit() && PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(getResources().getString(R.string.CM))) currenthiegh = PeriodTrackerConstants.DEFAULT_HEIGHT_VALUE_IN_CM;
			else currenthiegh = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf((Utility.convertCentiMeterTOInches(PeriodTrackerConstants.DEFAULT_HEIGHT_VALUE_IN_CM)))));
		}
		if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) && Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) != 0)
		{
			currentwiegh = Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule());
		}
		else
		{
			
			if (null != PeriodTrackerObjectLocator.getInstance().getWeighUnit() && PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(getResources().getString(R.string.lb))) 
				currentwiegh = PeriodTrackerConstants.DEFAULT_WEIGHT_VAULE_IN_LB;
			else currentwiegh = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram(PeriodTrackerConstants.DEFAULT_WEIGHT_VAULE_IN_LB))));
			
		}
		
		if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit() && PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(getResources().getString(R.string.Fehr)))
		{
			
			currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule()));
		}
		else
		{
			currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToCelsius(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule())))));
		}
		
		/*AdView adView = (AdView) mView.findViewById(R.id.adView);
		if (Utility.addDays(
				new Date(getActivity().getSharedPreferences("com.linchpin.periodtracker", 0).getLong("InstallDate",
						PeriodTrackerConstants.NULL_DATE)), 10).after(PeriodTrackerConstants.CURRENT_DATE)
				|| getActivity().getSharedPreferences("com.linchpin.periodtracker", 0).getBoolean("purchased", false)) {
			adView.setVisibility(View.GONE);
		}*/
		int i = ((AddNoteView) getActivity()).mViewpager.getCurrentItem();
		ViewPageListener.activeFragment = weightFragment;
		if (i == 3)
		{
			if (ViewPageListener.forwordPerviousFragment == null) ViewPageListener.forwordPerviousFragment = weightFragment;
		}
		
		wTextWieght = (EditText) mView.findViewById(R.id.weightvalue);
		wTextWieght.setText("");
		wTextHiehgt = (EditText) mView.findViewById(R.id.editheightvalue);
		wTextHiehgt.setText("");
		wTextTemperature = (EditText) mView.findViewById(R.id.temperaturevalue);
		wTextTemperature.setText("");
		wTextWeightBmi = (TextView) mView.findViewById(R.id.textbmi);
		wTextBmiMessage = (TextView) mView.findViewById(R.id.textbmimesg);
		
		uWieghtUnit = (Button) mView.findViewById(R.id.weightunit);
		uWieghtUnit.setText(PeriodTrackerObjectLocator.getInstance().getWeighUnit());
		uHiehgtUnit = (Button) mView.findViewById(R.id.heightunit);
		
		uTempUnit = (Button) mView.findViewById(R.id.temperatueunit);
		uTempUnit.setText(PeriodTrackerObjectLocator.getInstance().getTempUnit());
		weighPlusButton = (ImageView) mView.findViewById(R.id.weightvalueplus);
		weighMinusButton = (ImageView) mView.findViewById(R.id.weightvalueminus);
		weighMinusButton.setOnClickListener(this);
		weighPlusButton.setOnClickListener(this);
		
		uWieghtUnit.setOnClickListener(this);
		weighPlusButton.setOnClickListener(this);
		weighMinusButton.setOnClickListener(this);
		
		heighPlusButton = (ImageView) mView.findViewById(R.id.heightvalueplus);
		heighMinusButton = (ImageView) mView.findViewById(R.id.heightvalueminus);
		
		uHiehgtUnit.setOnClickListener(this);
		heighPlusButton.setOnClickListener(this);
		heighMinusButton.setOnClickListener(this);
		
		temPlusButton = (ImageView) mView.findViewById(R.id.temperaturevalueplus);
		tempMinusButton = (ImageView) mView.findViewById(R.id.temperaturevalueminus);
		
		uTempUnit.setOnClickListener(this);
		temPlusButton.setOnClickListener(this);
		tempMinusButton.setOnClickListener(this);
		
		uHiehgtUnit.setText(PeriodTrackerObjectLocator.getInstance().getHeightUnit());
		uTempUnit.setText(PeriodTrackerObjectLocator.getInstance().getTempUnit());
		uWieghtUnit.setText(PeriodTrackerObjectLocator.getInstance().getWeighUnit());
		
		addNoteDBHandler = new AddNoteDBHandler(getActivity());
		dayDetailModel = ((AddNoteView) getActivity()).dayDetailModel;
		
		if (null == dayDetailModel)
		{
			
			wTextTemperature.setHint(String.valueOf(currenttemp));
			
			if (null != PeriodTrackerObjectLocator.getInstance().getWeighUnit() && PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(getResources().getString(R.string.KG)))
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) && Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) != 0) wTextWieght
						.setHint(String.valueOf(Utility.ConvertToKilogram(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()))));
				else wTextWieght.setHint(String.valueOf(String.valueOf(PeriodTrackerConstants.MIN_WEIGHT_IN_KG)));
			}
			else
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) && Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule()) != 0) wTextWieght
						.setHint(String.valueOf(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultWeightVaule())));
				else wTextWieght.setHint(String.valueOf(String.valueOf(PeriodTrackerConstants.MIN_WEIGHT_IN_LB)));
			}
			if (null != PeriodTrackerObjectLocator.getInstance().getHeightUnit() && PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(getResources().getString(R.string.CM)))
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) && Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) != 0) wTextHiehgt
						.setHint(String.valueOf(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule())));
				else wTextHiehgt.setHint(String.valueOf(String.valueOf(PeriodTrackerConstants.MIN_HEIGHT_IN_CENTIMETER)));
				
			}
			else
			{
				if (null != PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule() && Utility.isValidNumber(float.class, PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) && Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()) != 0) wTextHiehgt
						.setHint(String.valueOf(Utility.convertCentiMeterTOInches(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultHeightVaule()))));
				else wTextHiehgt.setHint(String.valueOf(String.valueOf(PeriodTrackerConstants.MIN_HEIGHT_IN_INCHES)));
				
			}
			
		}
		else
		{
			if (null != PeriodTrackerObjectLocator.getInstance().getWeighUnit() && PeriodTrackerObjectLocator.getInstance().getWeighUnit().equals(getResources().getString(R.string.lb)))
			{
				if (dayDetailModel.getWeight() != 0) wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf(dayDetailModel.getWeight())));
				else wTextWieght.setHint(Utility.getStringFormatedNumber(String.valueOf(currentwiegh)));
			}
			else
			{
				if (dayDetailModel.getWeight() != 0) wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram(dayDetailModel.getWeight()))));
				else wTextWieght.setHint(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram(currentwiegh))));
			}
			if (null != PeriodTrackerObjectLocator.getInstance().getHeightUnit() && PeriodTrackerObjectLocator.getInstance().getHeightUnit().equals(getResources().getString(R.string.CM)))
			{
				if (dayDetailModel.getHeight() != 0) wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf(dayDetailModel.getHeight())));
				else wTextHiehgt.setHint(Utility.getStringFormatedNumber(String.valueOf(currenthiegh)));
			}
			else
			{
				if (dayDetailModel.getHeight() != 0) wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(dayDetailModel.getHeight()))));
				else wTextHiehgt.setHint(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(currenthiegh))));
				
			}
			if (null != PeriodTrackerObjectLocator.getInstance().getTempUnit() && PeriodTrackerObjectLocator.getInstance().getTempUnit().equals(PeriodTrackerConstants.DEFAULT_TEMPRATURE_UNIT))
			{
				if (!dayDetailModel.getTemp().equals(Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))) wTextTemperature.setText(Utility.getStringFormatedNumber(String.valueOf(dayDetailModel.getTemp())));
				else wTextTemperature.setHint(Utility.getStringFormatedNumber(String.valueOf(currenttemp)));
			}
			else
			{
				if (!dayDetailModel.getTemp().equals(Float.parseFloat(PeriodTrackerConstants.DEFAULT_TEMP_STRING_VALUE))) wTextTemperature.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToCelsius(dayDetailModel.getTemp()))));
				else wTextTemperature.setHint(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToCelsius(Float.parseFloat(PeriodTrackerObjectLocator.getInstance().getDefaultTemperatureVaule())))));
			}
			Float bmi = Utility.calculateBMI(Utility.ConvertToKilogram(currentwiegh), currenthiegh);
			
			wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf(bmi)) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
			SetBMIMsg(bmi);
			
		}
		
		wTextHiehgt.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				// TODO Auto-generated method stub
				
				if (!wTextWieght.getText().toString().equals(""))
				{
					currentwiegh = Float.parseFloat(wTextWieght.getText().toString());
					wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf(currentwiegh)));
					if (!uWieghtUnit.getText().toString().equals(getResources().getString(R.string.KG)))
					{
						currentwiegh = Utility.ConvertToKilogram(currentwiegh);
					}
				}
				
				if (!wTextHiehgt.getText().toString().equals(""))
				{
					
					currenthiegh = Float.parseFloat(wTextHiehgt.getText().toString());
					wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf(currenthiegh)));
					if (!uHiehgtUnit.getText().toString().equals(getResources().getString(R.string.CM)))
					{
						currenthiegh = Utility.convertInchesTOCentiMeter(currenthiegh);
					}
				}
				Float bmi = Utility.calculateBMI(currentwiegh, currenthiegh);
				
				wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf(bmi)) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
				SetBMIMsg(bmi);
				if (!hasFocus)
				{
					if (!validateHieght())
					{
						wTextHiehgt.setFocusable(true);
						
					}
					
				}
				
			}
		});
		wTextTemperature.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				// TODO Auto-generated method stub
				
				if (!wTextTemperature.getText().toString().equals(""))
				{
					currenttemp = Float.parseFloat(wTextTemperature.getText().toString());
					wTextTemperature.setText(Utility.getStringFormatedNumber(String.valueOf(Float.valueOf(currenttemp))));
					
				}
				
				if (!wTextWieght.getText().toString().equals(""))
				{
					currentwiegh = Float.parseFloat(wTextWieght.getText().toString());
					wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf(Float.valueOf(currentwiegh))));
					if (!uWieghtUnit.getText().toString().equals(getResources().getString(R.string.KG)))
					{
						currentwiegh = Utility.ConvertToKilogram(currentwiegh);
					}
				}
				
				if (!wTextHiehgt.getText().toString().equals(""))
				{
					currenthiegh = Float.parseFloat(wTextHiehgt.getText().toString());
					wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf(Float.valueOf(currenthiegh))));
					if (!uHiehgtUnit.getText().toString().equals(getResources().getString(R.string.CM)))
					{
						currenthiegh = Utility.convertInchesTOCentiMeter(currenthiegh);
					}
					
				}
				Float bmi = Utility.calculateBMI(currentwiegh, currenthiegh);
				
				wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf(bmi)) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
				SetBMIMsg(bmi);
				if (!hasFocus)
				{
					if (!checkValidityOfTemp())
					{
						wTextTemperature.setFocusable(true);
					}
				}
				
			}
		});
		wTextWieght.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				// TODO Auto-generated method stub
				if (!wTextWieght.getText().toString().equals(""))
				{
					currentwiegh = Float.parseFloat(wTextWieght.getText().toString());
					wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf(Float.valueOf(currentwiegh))));
					if (!uWieghtUnit.getText().toString().equals(getResources().getString(R.string.KG)))
					{
						currentwiegh = Utility.ConvertToKilogram(currentwiegh);
					}
				}
				
				if (!wTextHiehgt.getText().toString().equals(""))
				{
					currenthiegh = Float.parseFloat(wTextHiehgt.getText().toString());
					wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf(Float.valueOf(currenthiegh))));
					if (!uHiehgtUnit.getText().toString().equals(getResources().getString(R.string.CM)))
					{
						currenthiegh = Utility.convertInchesTOCentiMeter(currenthiegh);
					}
					
				}
				Float bmi = Utility.calculateBMI(currentwiegh, currenthiegh);
				
				wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf(bmi)) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
				SetBMIMsg(bmi);
				if (!hasFocus)
				{
					if (!checkValidityOfWeight())
					{
						wTextWieght.setFocusable(true);
					}
				}
			}
		});
	}
	
	@Override
	public void onClick(View view)
	{
		int id = view.getId();
		if (id == R.id.weightvalueplus)
		{
			if (uWieghtUnit.getText().toString().equals(getResources().getString(R.string.lb)))
			{
				
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currentwiegh))) < PeriodTrackerConstants.MAX_WEIGHT_IN_LB || Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currentwiegh))) >= PeriodTrackerConstants.MIN_WEIGHT_IN_LB)
				{
					if (!String.valueOf(wTextWieght.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextWieght.getText())))
						{
							currentwiegh = Float.parseFloat(String.valueOf(wTextWieght.getText()));
							currentwiegh = (float) (currentwiegh + 0.1);
							wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf((currentwiegh))));
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextWieght.getHint().toString())))
						{
							
							currentwiegh = Float.parseFloat(String.valueOf(wTextWieght.getHint().toString()));
							currentwiegh = (float) (currentwiegh + 0.1);
							wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf((currentwiegh))));
							
						}
						
					}
					if (uHiehgtUnit.getText().equals(getResources().getString(R.string.inches)))
					{
						currenthiegh = Utility.convertInchesTOCentiMeter(currenthiegh);
					}
					Float bmi = Utility.calculateBMI(Utility.ConvertToKilogram(currentwiegh), currenthiegh);
					wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf((bmi))) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
					SetBMIMsg(bmi);
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidweightinlb), Toast.LENGTH_LONG).show();
				}
				
			}
			else
			{
				if (wTextWieght.getText().toString().equals(""))
				{
					
					currentwiegh = (float) (Utility.ConvertToKilogram(currentwiegh));
				}
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currentwiegh))) < PeriodTrackerConstants.MAX_WEIGHT_IN_KG || Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currentwiegh))) >= PeriodTrackerConstants.MIN_WEIGHT_IN_KG)
				{
					
					if (!String.valueOf(wTextWieght.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextWieght.getText())))
						{
							
							currentwiegh = Float.parseFloat(String.valueOf(wTextWieght.getText()));
							currentwiegh = (float) (currentwiegh + 0.1);
							wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf((currentwiegh))));
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextWieght.getHint())))
						{
							
							currentwiegh = Float.parseFloat(String.valueOf(wTextWieght.getHint()));
							currentwiegh = (float) (currentwiegh + 0.1);
							wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf((currentwiegh))));
						}
					}
					if (uHiehgtUnit.getText().equals(getResources().getString(R.string.inches)))
					{
						currenthiegh = Utility.convertInchesTOCentiMeter(currenthiegh);
					}
					Float bmi = Utility.calculateBMI(Utility.ConvertToKilogram(currentwiegh), currenthiegh);
					wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf(bmi)) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
					SetBMIMsg(bmi);
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidweightinkg), Toast.LENGTH_LONG).show();
				}
				
			}
		}
		else if (id == R.id.weightvalueminus)
		{
			if (uWieghtUnit.getText().toString().equals(getResources().getString(R.string.lb)))
			{
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currentwiegh))) > PeriodTrackerConstants.MIN_WEIGHT_IN_LB)
				{
					if (!String.valueOf(wTextWieght.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextWieght.getText())))
						{
							
							currentwiegh = Float.parseFloat(String.valueOf(wTextWieght.getText()));
							currentwiegh = (float) (currentwiegh - 0.1);
							wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf(currentwiegh)));
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextWieght.getHint())))
						{
							
							currentwiegh = Float.parseFloat(String.valueOf(wTextWieght.getHint()));
							currentwiegh = (float) (currentwiegh - 0.1);
							wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf(currentwiegh)));
						}
					}
					if (uHiehgtUnit.getText().equals(getResources().getString(R.string.inches)))
					{
						currenthiegh = Utility.convertInchesTOCentiMeter(currenthiegh);
					}
					Float bmi = Utility.calculateBMI(Utility.ConvertToKilogram(currentwiegh), currenthiegh);
					wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf(bmi)) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
					SetBMIMsg(bmi);
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidweightinlb), Toast.LENGTH_LONG).show();
				}
				
			}
			else
			{
				if (wTextWieght.getText().toString().equals(""))
				{
					
					currentwiegh = (float) (Utility.ConvertToKilogram(currentwiegh));
				}
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currentwiegh))) > PeriodTrackerConstants.MIN_WEIGHT_IN_KG)
				{
					if (!String.valueOf(wTextWieght.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextWieght.getText())))
						{
							
							currentwiegh = Float.parseFloat(String.valueOf(wTextWieght.getText()));
							currentwiegh = (float) (currentwiegh - 0.1);
							wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf(currentwiegh)));
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextWieght.getHint())))
						{
							
							currentwiegh = Float.parseFloat(String.valueOf(wTextWieght.getHint()));
							currentwiegh = (float) (currentwiegh - 0.1);
							wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf(currentwiegh)));
						}
					}
					if (uHiehgtUnit.getText().equals(getResources().getString(R.string.inches)))
					{
						currenthiegh = Utility.convertInchesTOCentiMeter(currenthiegh);
					}
					Float bmi = (Utility.calculateBMI(currentwiegh, currenthiegh));
					wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf(bmi)) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
					SetBMIMsg(bmi);
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidweightinkg), Toast.LENGTH_LONG).show();
				}
				
			}
		}
		else if (id == R.id.heightvalueplus)
		{
			if (uHiehgtUnit.getText().toString().equals(getResources().getString(R.string.CM)))
			{
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currenthiegh))) < PeriodTrackerConstants.MAX_HEIGHT_IN_CENTIMETER)
				{
					if (!String.valueOf(wTextHiehgt.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextHiehgt.getText())))
						{
							
							currenthiegh = Float.parseFloat(wTextHiehgt.getText().toString());
							currenthiegh = (float) (currenthiegh + 0.1);
							wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf(currenthiegh)));
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextHiehgt.getHint())))
						{
							
							currenthiegh = Float.parseFloat(wTextHiehgt.getHint().toString());
							currenthiegh = (float) (currenthiegh + 0.1);
							wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf(currenthiegh)));
						}
					}
					if (uWieghtUnit.getText().equals(getResources().getString(R.string.lb)))
					{
						currentwiegh = Utility.ConvertToKilogram(currentwiegh);
					}
					Float bmi = (Utility.calculateBMI(currentwiegh, currenthiegh));
					wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf(bmi)) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
					SetBMIMsg(bmi);
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidhieghtmessageincm), Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				if (wTextHiehgt.getText().toString().equals(""))
				{
					
					currenthiegh = Utility.convertCentiMeterTOInches(currenthiegh);
				}
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currenthiegh))) < PeriodTrackerConstants.MAX_HEIGHT_IN_INCHES)
				{
					if (!String.valueOf(wTextHiehgt.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextHiehgt.getText())))
						{
							
							currenthiegh = Float.parseFloat(wTextHiehgt.getText().toString());
							currenthiegh = (float) (currenthiegh + 0.1);
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextHiehgt.getHint())))
						{
							
							currenthiegh = Float.parseFloat(wTextHiehgt.getHint().toString());
							currenthiegh = (float) (currenthiegh + 0.1);
						}
					}
					wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf((currenthiegh))));
					if (uWieghtUnit.getText().equals(getResources().getString(R.string.lb)))
					{
						currentwiegh = Utility.ConvertToKilogram(currentwiegh);
					}
					Float bmi = Utility.calculateBMI(currentwiegh, Utility.convertInchesTOCentiMeter(currenthiegh));
					wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf(bmi)) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
					SetBMIMsg(bmi);
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidheightininches), Toast.LENGTH_LONG).show();
				}
			}
		}
		else if (id == R.id.heightvalueminus)
		{
			if (uHiehgtUnit.getText().toString().equals(getResources().getString(R.string.CM)))
			{
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currenthiegh))) > PeriodTrackerConstants.MIN_HEIGHT_IN_CENTIMETER)
				{
					if (!String.valueOf(wTextHiehgt.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextHiehgt.getText())))
						{
							
							currenthiegh = Float.parseFloat(wTextHiehgt.getText().toString());
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextHiehgt.getHint())))
						{
							
							currenthiegh = Float.parseFloat(wTextHiehgt.getHint().toString());
						}
					}
					currenthiegh = (float) (currenthiegh - 0.1);
					if (uWieghtUnit.getText().equals(getResources().getString(R.string.lb)))
					{
						currentwiegh = Utility.ConvertToKilogram(currentwiegh);
					}
					Float bmi = Utility.calculateBMI(currentwiegh, currenthiegh);
					wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf((bmi))) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
					SetBMIMsg(bmi);
					wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf((currenthiegh))));
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidheightincm), Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				if (wTextHiehgt.getText().toString().equals(""))
				{
					
					currenthiegh = Utility.convertCentiMeterTOInches(currenthiegh);
				}
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currenthiegh))) > PeriodTrackerConstants.MIN_HEIGHT_IN_INCHES)
				{
					if (!String.valueOf(wTextHiehgt.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextHiehgt.getText())))
						{
							
							currenthiegh = Float.parseFloat(wTextHiehgt.getText().toString());
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextHiehgt.getHint())))
						{
							
							currenthiegh = Float.parseFloat(wTextHiehgt.getHint().toString());
						}
					}
					currenthiegh = (float) (currenthiegh - 0.1);
					if (uWieghtUnit.getText().equals(getResources().getString(R.string.lb)))
					{
						currentwiegh = Utility.ConvertToKilogram(currentwiegh);
					}
					Float bmi = (Utility.calculateBMI(currentwiegh, Utility.convertInchesTOCentiMeter(currenthiegh)));
					wTextWeightBmi.setText(Utility.getStringFormatedNumber(String.valueOf(bmi)) + " " + getResources().getString(R.string.kgm2) + Html.fromHtml("<sup><small>" + "2" + "</small></sup>"));
					SetBMIMsg(bmi);
					wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf((currenthiegh))));
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidheightininches), Toast.LENGTH_LONG).show();
				}
			}
		}
		else if (id == R.id.temperaturevalueplus)
		{
			if (uTempUnit.getText().toString().equals(getResources().getString(R.string.Fehr)))
			{
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currenttemp))) < PeriodTrackerConstants.MAX_TEMP_IN_FEHRENHIET)
				{
					if (!String.valueOf(wTextTemperature.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextTemperature.getText())))
						{
							
							currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(wTextTemperature.getText().toString()));
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextTemperature.getHint())))
						{
							
							currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(wTextTemperature.getHint().toString()));
						}
					}
					
					currenttemp = (float) (currenttemp + 0.1);
					wTextTemperature.setText(Utility.getStringFormatedNumber(String.valueOf((currenttemp))));
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidtempinF), Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				if (wTextTemperature.getText().toString().equals(""))
				{
					
					currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf((Utility.ConvertToCelsius(currenttemp)))));
				}
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currenttemp))) < PeriodTrackerConstants.MAX_TEMP_IN_CELSIUS)
				{
					if (!String.valueOf(wTextTemperature.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextTemperature.getText())))
						{
							
							currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(wTextTemperature.getText().toString()));
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextTemperature.getHint())))
						{
							
							currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(wTextTemperature.getHint().toString()));
						}
					}
					currenttemp = (float) (currenttemp + 0.1);
					wTextTemperature.setText(Utility.getStringFormatedNumber(String.valueOf((currenttemp))));
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidtempinC), Toast.LENGTH_LONG).show();
				}
			}
		}
		else if (id == R.id.temperaturevalueminus)
		{
			if (uTempUnit.getText().toString().equals(getResources().getString(R.string.Fehr)))
			{
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currenttemp))) > PeriodTrackerConstants.MIN_TEMP_IN_FEHRENHIET)
				{
					if (!String.valueOf(wTextTemperature.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextTemperature.getText())))
						{
							{
								
								currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(wTextTemperature.getText().toString()));
							}
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextTemperature.getHint())))
						{
							
							currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(wTextTemperature.getHint().toString()));
						}
						
					}
					currenttemp = (float) (currenttemp - 0.1);
					wTextTemperature.setText(Utility.getStringFormatedNumber(String.valueOf(currenttemp)));
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidtempinF), Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				if (wTextTemperature.getText().toString().equals(""))
				{
					
					currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf((Utility.ConvertToCelsius(currenttemp)))));
				}
				if (Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(currenttemp))) > PeriodTrackerConstants.MIN_TEMP_IN_CELSIUS)
				{
					if (!String.valueOf(wTextTemperature.getText()).equals(""))
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextTemperature.getText())))
						{
							{
								currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(wTextTemperature.getText().toString())));
							}
						}
					}
					else
					{
						if (Utility.isValidNumber(float.class, String.valueOf(wTextTemperature.getHint())))
						{
							
							currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(wTextTemperature.getHint().toString())));
						}
					}
					currenttemp = (float) (currenttemp - 0.1);
					wTextTemperature.setText(Utility.getStringFormatedNumber(String.valueOf(currenttemp)));
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.invalidtempinC), Toast.LENGTH_LONG).show();
				}
			}
		}
		else if (id == R.id.temperatueunit)
		{
			builder = new AlertDialog.Builder(getActivity());
			final String tempUnit = uTempUnit.getText().toString();
			builder.setTitle(getResources().getString(R.string.selecttempunit));
			final String[] tempitems =
			{
					getResources().getString(R.string.Fehr), getResources().getString(R.string.celsius)
			};
			int selectedPosition = Arrays.asList(tempitems).indexOf(tempUnit);
			builder.setSingleChoiceItems(tempitems, selectedPosition, new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					
					uTempUnit.setText(tempitems[which]);
					if (tempUnit.equals(getResources().getString(R.string.Fehr)))
					{
						if (!uTempUnit.getText().toString().equals(getResources().getString(R.string.Fehr)))
						{
							if (!wTextTemperature.getText().toString().equals("")) wTextTemperature.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToCelsius(Float.parseFloat(wTextTemperature.getText().toString()))).toString()));
							else wTextTemperature.setHint(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToCelsius(Float.parseFloat(wTextTemperature.getHint().toString()))).toString()));
							
						}
						
					}
					else
					{
						if (!uTempUnit.getText().toString().equals(getResources().getString(R.string.celsius)))
						{
							if (!wTextTemperature.getText().toString().equals(""))
							{
								wTextTemperature.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToFahrenheit((Float.parseFloat(wTextTemperature.getText().toString())))).toString()));
							}
							else
							{
								wTextTemperature.setHint(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToFahrenheit((Float.parseFloat(wTextTemperature.getHint().toString())))).toString()));
								
							}
						}
						
					}
					dialog.cancel();
				}
			});
			builder.show();
		}
		else if (id == R.id.weightunit)
		{
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getResources().getString(R.string.selectweightunit));
			final String[] weightitems =
			{
					getResources().getString(R.string.KG), getResources().getString(R.string.lb)
			};
			int selectedPosition = Arrays.asList(weightitems).indexOf(uWieghtUnit.getText().toString());
			builder.setSingleChoiceItems(weightitems, selectedPosition, new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					
					final String weiString = uWieghtUnit.getText().toString();
					uWieghtUnit.setText(weightitems[which]);
					if (weightitems[which].equals(getResources().getString(R.string.KG)))
					{
						if (!weiString.equals(getResources().getString(R.string.KG)))
						{
							if (!wTextWieght.getText().toString().equals(""))
							{
								
								wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram(Float.parseFloat(wTextWieght.getText().toString())))));
							}
							else
							{
								
								wTextWieght.setHint(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToKilogram(Float.parseFloat(wTextWieght.getHint().toString())))));
							}
							
						}
						
					}
					else
					{
						if (!weiString.equals(getResources().getString(R.string.lb)))
						{
							
							if (!wTextWieght.getText().toString().equals(""))
							{
								
								wTextWieght.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToPounds((Float.parseFloat(wTextWieght.getText().toString()))))));
							}
							else
							{
								
								wTextWieght.setHint(Utility.getStringFormatedNumber(String.valueOf(Utility.ConvertToPounds(Float.parseFloat(wTextWieght.getHint().toString())))));
							}
							
						}
					}
					dialog.dismiss();
				}
			});
			builder.show();
		}
		else if (id == R.id.heightunit)
		{
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getResources().getString(R.string.selecthieghtunit));
			final String[] heightitems =
			{
					getResources().getString(R.string.CM), getResources().getString(R.string.inches)
			};
			int selectedPosition = Arrays.asList(heightitems).indexOf(uHiehgtUnit.getText().toString());
			builder.setSingleChoiceItems(heightitems, selectedPosition, new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					
					// TODO Auto-generated method stub
					final String heString = uHiehgtUnit.getText().toString();
					uHiehgtUnit.setText(heightitems[which]);
					if (heightitems[which].equals(getResources().getString(R.string.inches)))
					{
						if (!heString.equals(getResources().getString(R.string.inches)))
						{
							if (!wTextHiehgt.getText().toString().equals(""))
							{
								
								wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(Float.parseFloat(wTextHiehgt.getText().toString())))));
							}
							else
							{
								
								wTextHiehgt.setHint(Utility.getStringFormatedNumber(String.valueOf(Utility.convertCentiMeterTOInches(Float.parseFloat(wTextHiehgt.getHint().toString())))));
							}
						}
					}
					else
					{
						
						if (!heString.equals(getResources().getString(R.string.CM)))
						{
							if (!wTextHiehgt.getText().toString().equals(""))
							{
								wTextHiehgt.setText(Utility.getStringFormatedNumber(String.valueOf(Utility.convertInchesTOCentiMeter((Float.parseFloat(wTextHiehgt.getText().toString()))))));
							}
							else
							{
								
								wTextHiehgt.setHint(Utility.getStringFormatedNumber(String.valueOf(Utility.convertInchesTOCentiMeter((Float.parseFloat(wTextHiehgt.getHint().toString()))))));
							}
						}
					}
					
					dialog.dismiss();
				}
			});
			builder.show();
		}
		else
		{
		}
		
	}
	
	public boolean validateTempWeightAndHieght()
	{
		boolean validity = false;
		if (checkValidityOfWeight() || validateHieght() || checkValidityOfTemp())
		{
			
			validity = true;
			
		}
		
		return validity;
	}
	
	public boolean checkValidityOfWeight()
	{
		boolean validateWeigh = false;
		builder = new AlertDialog.Builder(getActivity());
		if (uWieghtUnit.getText().toString().equalsIgnoreCase(getResources().getString(R.string.lb)))
		{
			
			if (null != wTextWieght.getText().toString() && !wTextWieght.getText().toString().equals(""))
			{
				
				if (Float.parseFloat(wTextWieght.getText().toString()) > PeriodTrackerConstants.MAX_WEIGHT_IN_LB || Float.parseFloat(wTextWieght.getText().toString()) < PeriodTrackerConstants.MIN_WEIGHT_IN_LB)
				{
					validateWeigh = false;
					// Toast.makeText(getActivity(), "invalid Weight in lb",
					// Toast.LENGTH_SHORT).show();
					builder.setTitle(getResources().getString(R.string.invalidwieght));
					builder.setMessage(getResources().getString(R.string.invalidwieghtmessageinlb));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							if (((AddNoteView) getActivity()).mTabHost.getCurrentTab() != 3) ((AddNoteView) getActivity()).mTabHost.setCurrentTab(3);
							wTextWieght.setFocusable(true);
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				}
				else
				{
					validateWeigh = true;
					currentwiegh = Float.parseFloat(wTextWieght.getText().toString());
				}
				
			}
			
		}
		else
		{
			
			if (null != wTextWieght.getText().toString() && !wTextWieght.getText().toString().equals(""))
			{
				if (Float.parseFloat(wTextWieght.getText().toString()) > PeriodTrackerConstants.MAX_WEIGHT_IN_KG || Float.parseFloat(wTextWieght.getText().toString()) < PeriodTrackerConstants.MIN_WEIGHT_IN_KG)
				{
					validateWeigh = false;
					// Toast.makeText(getActivity(), "invalid wieght in kg",
					// Toast.LENGTH_SHORT).show();
					builder.setTitle(getResources().getString(R.string.invalidwieght));
					builder.setMessage(getResources().getString(R.string.invalidwieghtmessageinkg));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
							((AddNoteView) getActivity()).mTabHost.setCurrentTab(3);
							wTextWieght.setFocusable(true);
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				}
				else
				{
					validateWeigh = true;
					currentwiegh = Float.parseFloat(wTextWieght.getText().toString());
					currentwiegh = Utility.ConvertToPounds(currentwiegh);
				}
			}
		}
		return validateWeigh;
		
	}
	
	public boolean checkValidityOfTemp()
	{
		boolean validateTemp = false;
		builder = new AlertDialog.Builder(getActivity());
		if (uTempUnit.getText().toString().equalsIgnoreCase(getResources().getString(R.string.celsius)))
		{
			
			if (null != wTextTemperature.getText().toString() && !wTextTemperature.getText().toString().equals(""))
			{
				
				if (Float.parseFloat(wTextTemperature.getText().toString()) > PeriodTrackerConstants.MAX_TEMP_IN_CELSIUS || Float.parseFloat(wTextTemperature.getText().toString()) < PeriodTrackerConstants.MIN_TEMP_IN_CELSIUS)
				{
					validateTemp = false;
					builder.setTitle(getResources().getString(R.string.invalidtemp));
					builder.setMessage(getResources().getString(R.string.invalidtempmessageinC));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							if (((AddNoteView) getActivity()).mTabHost.getCurrentTab() != 3) ((AddNoteView) getActivity()).mTabHost.setCurrentTab(3);
							wTextTemperature.setFocusable(true);
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
					// Toast.makeText(getActivity(), "invalid temp in celsius",
					// Toast.LENGTH_SHORT).show();
				}
				else
				{
					validateTemp = true;
					currenttemp = Float.parseFloat(wTextTemperature.getText().toString());
					currenttemp = (float) Utility.ConvertToFahrenheit(currenttemp);
				}
				
			}
			
		}
		else
		{
			
			if (null != wTextTemperature.getText().toString() && !wTextTemperature.getText().toString().equals(""))
			{
				if (Float.parseFloat(wTextTemperature.getText().toString()) > PeriodTrackerConstants.MAX_TEMP_IN_FEHRENHIET || Float.parseFloat((wTextTemperature.getText().toString())) < PeriodTrackerConstants.MIN_TEMP_IN_FEHRENHIET)
				{
					validateTemp = false;
					builder.setTitle(getResources().getString(R.string.invalidtemp));
					builder.setMessage(getResources().getString(R.string.invalidtemptmessageinF));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
							((AddNoteView) getActivity()).mTabHost.setCurrentTab(3);
							wTextTemperature.setFocusable(true);
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
					
					// Toast.makeText(getActivity(),
					// "invalid temp in Ferhinhiet ",
					// Toast.LENGTH_SHORT).show();
				}
				else
				{
					validateTemp = true;
					currenttemp = Float.parseFloat(wTextTemperature.getText().toString());
					
				}
			}
		}
		
		return validateTemp;
		
	}
	
	public boolean validateHieght()
	{
		boolean validateHiegh = false;
		builder = new AlertDialog.Builder(getActivity());
		if (uHiehgtUnit.getText().toString().equalsIgnoreCase(getResources().getString(R.string.CM)))
		{
			
			if (null != wTextHiehgt.getText().toString() && !wTextHiehgt.getText().toString().equals(""))
			{
				if (Float.parseFloat(wTextHiehgt.getText().toString()) > PeriodTrackerConstants.MAX_HEIGHT_IN_CENTIMETER || (Float.parseFloat(wTextHiehgt.getText().toString()) < PeriodTrackerConstants.MIN_HEIGHT_IN_CENTIMETER))
				{
					validateHiegh = false;
					builder.setTitle(getResources().getString(R.string.invalidhieght));
					builder.setMessage(getResources().getString(R.string.invalidhieghtmessageincm));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
							if (((AddNoteView) getActivity()).mTabHost.getCurrentTab() != 3) ((AddNoteView) getActivity()).mTabHost.setCurrentTab(3);
							wTextHiehgt.setFocusable(true);
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				}
				else
				{
					currenthiegh = Float.parseFloat(wTextHiehgt.getText().toString());
					validateHiegh = true;
				}
			}
		}
		else
		{
			
			if (null != wTextHiehgt.getText().toString() && !wTextHiehgt.getText().toString().equals(""))
			{
				if (Float.parseFloat(wTextHiehgt.getText().toString()) > PeriodTrackerConstants.MAX_HEIGHT_IN_INCHES || (Float.parseFloat(wTextHiehgt.getText().toString()) < PeriodTrackerConstants.MIN_HEIGHT_IN_INCHES))
				{
					// Toast.makeText(getActivity(), "invalid hieght in inches",
					// Toast.LENGTH_SHORT).show();
					validateHiegh = false;
					builder.setTitle(getResources().getString(R.string.invalidhieght));
					builder.setMessage(getResources().getString(R.string.invalidhieghtmessageinInches));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
							((AddNoteView) getActivity()).mTabHost.setCurrentTab(3);
							wTextHiehgt.setFocusable(true);
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					
					builder.show();
				}
				else
				{
					validateHiegh = true;
					currenthiegh = Float.parseFloat(wTextHiehgt.getText().toString());
					currenthiegh = Utility.convertInchesTOCentiMeter(currenthiegh);
				}
			}
		}
		
		return validateHiegh;
	}
	
	public void saveAndUpdate()
	{
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
		if (wTextTemperature.getText() != null && !wTextTemperature.getText().toString().equals(""))
		{
			currenttemp = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(Float.parseFloat(wTextTemperature.getText().toString()))));
			if (uTempUnit.getText().equals(getResources().getString(R.string.celsius)))
			{
				currenttemp = Utility.ConvertToFahrenheit(currenttemp);
			}
			
		}
		
		if (wTextHiehgt.getText() != null && !wTextHiehgt.getText().toString().equals(""))
		{
			currenthiegh = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(Float.parseFloat(wTextHiehgt.getText().toString()))));
			if (uHiehgtUnit.getText().equals(getResources().getString(R.string.inches)))
			{
				currenthiegh = Utility.convertInchesTOCentiMeter(currenthiegh);
			}
		}
		if (wTextWieght.getText() != null && !wTextWieght.getText().toString().equals(""))
		{
			currentwiegh = Float.parseFloat(Utility.getStringFormatedNumber(String.valueOf(Float.parseFloat(wTextWieght.getText().toString()))));
			if (uWieghtUnit.getText().equals(getResources().getString(R.string.KG)))
			{
				currentwiegh = Utility.ConvertToPounds(currentwiegh);
				
			}
			
		}
		
		dayDetailModel = ((AddNoteView) getActivity()).dayDetailModel;
		dayDetailModel = new DayDetailModel(dayDetailModel.getId(), dayDetailModel.getDate(), dayDetailModel.getNote(), dayDetailModel.isIntimate(), dayDetailModel.getProtection(), currentwiegh, currenttemp, currenthiegh, dayDetailModel.getProfileID(),3);
		
		addNoteDBHandler.updateDayDetail(dayDetailModel);
		
	}
	
}
