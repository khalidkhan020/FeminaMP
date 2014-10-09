package com.linchpin.periodtracker.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.controller.ViewPageListener;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.view.AddNoteView;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodttracker.database.AddNoteDBHandler;

public class AddNoteFragments extends Fragment {

	public EditText eAddNote;
	Button add, save;
	public Checkable check;
	public boolean intimate;
	public String note;

	public AddNoteDBHandler addNoteDBHandler;

	public DayDetailModel dayDetailModel;

	public static AddNoteFragments addnoteFragment;
	Theme t;
	private void applyTheme(View view)
	{
		
		 t = Theme.getCurrentTheme(getActivity(),view);
		if (t != null)
		{			
			t.applyTextColor(R.id.intimate_txt, "text_color");
			t.applyBackgroundColor(R.id.inforelative, "heading_bg");
			t.applyBackgroundColor(R.id.checkboxlayout, "heading_bg");
			t.applyBackgroundColor(R.id.content, "co_note_bg_color");
			t.applyBackgroundDrawable(R.id.intimate_layout, "mpt_note_page_bottom");
			t.applyBackgroundDrawable(R.id.addnote, "mpt_note_txt_area_shp");
			CheckBox cc=(CheckBox)view.findViewById(R.id.intimate);
			cc.setButtonDrawable(t.getDrawableResource("mpt_intimate_sltr"));
			
		}}
	AlertDialog.Builder builder;

	public PeriodTrackerModelInterface periodTrackerModelInterface;

	public View mView;
	public static View rootview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		if (addnoteFragment.getView() == null) {
			mView = inflater.inflate(R.layout.add_note, container, false);
			applyTheme(mView);
		}
		
		/*
		 * AdView adView = (AdView) mView.findViewById(R.id.adView); if
		 * (Utility.addDays( new
		 * Date(getActivity().getSharedPreferences("com.linchpin.periodtracker"
		 * , 0).getLong("InstallDate", PeriodTrackerConstants.NULL_DATE)),
		 * 10).after(PeriodTrackerConstants.CURRENT_DATE) ||
		 * getActivity().getSharedPreferences("com.linchpin.periodtracker",
		 * 0).getBoolean("purchased", false)) { adView.setVisibility(View.GONE);
		 * }
		 */mView.findViewById(R.id.addnoteinfobutton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), HomeScreenHelp.class);
				intent.putExtra("classname", "addnote");
				startActivity(intent);

			}
		});
		return mView;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (null != addnoteFragment && null != addnoteFragment.getView()) {
			eAddNote = (EditText) addnoteFragment.getView().findViewById(R.id.addnote);
			check = (Checkable) addnoteFragment.getView().findViewById(R.id.intimate);
			intializeAddNoteFragment();
		}
	}

	public void intializeAddNoteFragment() {

		int i = ((AddNoteView) getActivity()).mViewpager.getCurrentItem();

		ViewPageListener.activeFragment = addnoteFragment;

		if (i == 0) {

			if (ViewPageListener.forwordPerviousFragment == null)
				ViewPageListener.forwordPerviousFragment = addnoteFragment;

		}
		/* save = (Button) mView.findViewById(R.id.savenote); */

		dayDetailModel = ((AddNoteView) getActivity()).dayDetailModel;
		if (dayDetailModel != null) {
			eAddNote.setText(dayDetailModel.getNote());
			check.setChecked(dayDetailModel.isIntimate());

		}
	}

	public void validateAndSaveNote() {
		
		APP.GLOBAL().getEditor().putBoolean(APP.PREF.PARTNER_AUTO_SYNCH.key, true).commit();//sazid
		if (null != getActivity()) {
			InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
					getActivity().INPUT_METHOD_SERVICE);
			if (inputManager != null)
				inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

			if (eAddNote.getText().toString().length() > 255) {
				builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(getResources().getString(R.string.invaliddatetitle));
				builder.setMessage(getResources().getString(R.string.invalidatenotemessage));
				builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub=
						eAddNote.setFocusable(true);
					}
				});
				builder.setNegativeButton(getResources().getString(R.string.cancel), null);
				builder.show();
			} else {
				if (check.isChecked()) {
					intimate = true;
				} else {
					intimate = false;
				}
				note = eAddNote.getText().toString();
				addNoteDBHandler = new AddNoteDBHandler(getActivity());
				if (dayDetailModel != null) {
					dayDetailModel = new DayDetailModel(dayDetailModel.getId(), dayDetailModel.getDate(), note,
							intimate, dayDetailModel.getProtection(), dayDetailModel.getWeight(),
							dayDetailModel.getTemp(), dayDetailModel.getHeight(), dayDetailModel.getProfileID(),3);
					addNoteDBHandler.updateDayDetail(dayDetailModel);
					((AddNoteView) getActivity()).dayDetailModel = (DayDetailModel) addNoteDBHandler
							.getDayDetail(dayDetailModel.getDate());
					dayDetailModel = ((AddNoteView) getActivity()).dayDetailModel;
				} else {

					Float h, w, t;
					if (PeriodTrackerObjectLocator.getInstance().getHeightUnit()
							.equals(PeriodTrackerConstants.DEFAULT_HIEGHT_UNIT)) {
						h = PeriodTrackerConstants.MIN_HEIGHT_IN_CENTIMETER;
					} else {
						h = PeriodTrackerConstants.MIN_HEIGHT_IN_INCHES;
					}

					if (PeriodTrackerObjectLocator.getInstance().getWeighUnit()
							.equals(PeriodTrackerConstants.DEFAULT_WEIGHT_UNIT)) {
						w = PeriodTrackerConstants.MIN_WEIGHT_IN_LB;
					} else {
						w = PeriodTrackerConstants.MIN_WEIGHT_IN_KG;
					}

					if (PeriodTrackerObjectLocator.getInstance().getTempUnit()
							.equals(PeriodTrackerConstants.DEFAULT_TEMPRATURE_UNIT)) {
						t = PeriodTrackerConstants.MIN_TEMP_IN_FEHRENHIET;
					} else {
						t = PeriodTrackerConstants.MIN_TEMP_IN_CELSIUS;
					}
					dayDetailModel = new DayDetailModel(0, PeriodTrackerConstants.CURRENT_DATE, note, intimate,
							PeriodTrackerConstants.PROTECTION, w, t, h, PeriodTrackerConstants.DEFAULT_PROFILE_ID,3);
					addNoteDBHandler.addRecordInDayDetail(dayDetailModel);
					((AddNoteView) getActivity()).dayDetailModel = (DayDetailModel) addNoteDBHandler
							.getDayDetail(dayDetailModel.getDate());
					dayDetailModel = ((AddNoteView) getActivity()).dayDetailModel;
				}

			}
		}
	}
}
