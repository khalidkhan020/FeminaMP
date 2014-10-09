package com.linchpin.periodtracker.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.MoodAndSymtomsAdaptor;
import com.linchpin.periodtracker.adpators.PillArrayAdapter;
import com.linchpin.periodtracker.controller.ViewPageListener;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.Pills;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.view.AddNoteView;
import com.linchpin.periodtracker.view.HomeScreenHelp;
import com.linchpin.periodttracker.database.AddNoteDBHandler;

public class PillsFragmnet extends Fragment {

	View mView;
	Spinner PQSpinner;
	List<Integer> Items = new ArrayList<Integer>();
	EditText editText;
	ImageButton addButton;
	AddNoteDBHandler addNoteDBHandler;
	List<PeriodTrackerModelInterface> periodTrackerModelInterfaces;
	List<Pills> pillsList;
	Pills pills;
	ListView listView;
	String medicineName;
	DayDetailModel dayDetailModel;
	int quantity;
	public static PillsFragmnet pillsFragment;
	MoodAndSymtomsAdaptor adaptor;
	private void applyTheme(View view)
	{
		
		Theme t = Theme.getCurrentTheme(getActivity(),view);
		if (t != null)
		{			
			t.applyBackgroundColor(R.id.content, "view_bg");
			/*t.applyTextColor(R.id.textwieght, "text_color");
			t.applyTextColor(R.id.texthieght, "text_color");
			t.applyTextColor(R.id.texttemperature, "text_color");
			t.applyTextColor(R.id.textbmitext, "text_color");
			t.applyTextColor(R.id.textbmi, "text_color");
			*/
			
			t.applyTextColor(R.id.editPills, "text_color");
			t.applyTextColor(R.id.textuser, "text_color");
			t.applyTextColor(R.id.pillname, "text_color");
			t.applyTextColor(R.id.pill_quantity, "text_color");
			
			t.applyBackgroundDrawable(R.id.buttonadd, "mpt_note_txt_area_shp");
			t.applyImageDrawable(R.id.buttonadd, "mpt_plus");
			
//		
//			t.applyBackgroundDrawable(R.id.heightunit, "mpt_note_txt_area_shp");
//			t.applyBackgroundDrawable(R.id.temperatueunit, "mpt_note_txt_area_shp");
//			
			t.applyBackgroundDrawable(R.id.pil_area, "mpt_note_txt_area_shp");
			t.applyBackgroundDrawable(R.id.tempraturedecimalspinner, "mpt_note_txt_area_shp");
			//t.applyBackgroundDrawable(R.id.temperaturevalue, "mpt_note_txt_area_shp");
			
			
		}}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		mView = inflater.inflate(R.layout.pills, container, false);;
		applyTheme(mView);
		/*AdView adView = (AdView) mView.findViewById(R.id.adView);
		if (Utility.addDays(new Date(getActivity().getSharedPreferences("com.linchpin.periodtracker", 0).getLong("InstallDate", PeriodTrackerConstants.NULL_DATE)), 10).after(
				PeriodTrackerConstants.CURRENT_DATE)||getActivity().getSharedPreferences("com.linchpin.periodtracker", 0).getBoolean("purchased", false)) {
			adView.setVisibility(View.GONE);
		}*/
		initPillsFragment();
		mView.findViewById(R.id.pillsinfobutton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						HomeScreenHelp.class);
				intent.putExtra("classname", "pills");
				startActivity(intent);	
			}
		});
		return mView;

	}

	public void initPillsFragment() {
		// int i = ((AddNoteView) getActivity()).mViewpager.getCurrentItem();
		ViewPageListener.activeFragment = pillsFragment;

		int tab = ((AddNoteView) getActivity()).mViewpager.getCurrentItem();

		if (tab == 4) {
			if (ViewPageListener.forwordPerviousFragment == null)
				ViewPageListener.forwordPerviousFragment = pillsFragment;
		}

		PQSpinner = (Spinner) mView.findViewById(R.id.tempraturedecimalspinner);
		editText = (EditText) mView.findViewById(R.id.editPills);
		addButton = (ImageButton) mView.findViewById(R.id.buttonadd);
		listView = (ListView) mView.findViewById(R.id.pillslist);
		addNoteDBHandler = new AddNoteDBHandler(getActivity());
		periodTrackerModelInterfaces = ((AddNoteView) getActivity()).periodTrackerModelInterfacesPills;
		dayDetailModel = ((AddNoteView) getActivity()).dayDetailModel;
		adaptor = new MoodAndSymtomsAdaptor(periodTrackerModelInterfaces, null, PeriodTrackerConstants.PILLS_FRAGEMNT, getActivity());
		listView.setAdapter(adaptor);

		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				medicineName = editText.getText().toString();
				if (medicineName.equals("") || medicineName == null) {
					builder.setTitle(getResources().getString(R.string.blankmedicinename));
					builder.setMessage(getResources().getString(R.string.medicinename));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							editText.setFocusable(true);
							editText.setText("");
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();
				} else if (checkMedicineName(periodTrackerModelInterfaces, medicineName)) {

					builder.setTitle(getResources().getString(R.string.alreadymedicinetitle));
					builder.setMessage(getResources().getString(R.string.alreadymedicinename));
					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							editText.setFocusable(true);
							editText.setText("");
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.cancel), null);
					builder.show();

				} else {

					medicineName = editText.getText().toString();
					pills = new Pills(0, dayDetailModel.getId(), medicineName, quantity);
					addNoteDBHandler.addPillsDetail(pills);
					editText.setText("");
					periodTrackerModelInterfaces = addNoteDBHandler.getAllMedicineForDate(dayDetailModel.getId());
					listView.setAdapter(new MoodAndSymtomsAdaptor(periodTrackerModelInterfaces, null, PeriodTrackerConstants.PILLS_FRAGEMNT, getActivity()));
					adaptor.notifyDataSetChanged();
				}

			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(getResources().getString(R.string.delete));
				builder.setMessage(getResources().getString(R.string.deletemessageforpills));
				builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (addNoteDBHandler.deleteMedicineRecord(periodTrackerModelInterfaces.get(arg2))) {
							periodTrackerModelInterfaces.remove(arg2);
							periodTrackerModelInterfaces = addNoteDBHandler.getAllMedicineForDate(dayDetailModel.getId());
							listView.setAdapter(new MoodAndSymtomsAdaptor(periodTrackerModelInterfaces, null, PeriodTrackerConstants.PILLS_FRAGEMNT, getActivity()));
							adaptor.notifyDataSetChanged();
						}
					}
				});
				builder.setNegativeButton(getResources().getString(R.string.cancel), null);

				builder.show();

			}
		});

		if (Items.size() == 0) {
			for (int i = 1; i <= 10; i++) {
				Items.add(i);
			}
		}


		PillArrayAdapter adapter1 = new PillArrayAdapter(getActivity(), R.layout.custom_spinner, Items);
		PQSpinner.setAdapter(adapter1);
		PQSpinner.setSelection(0);
		PQSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				quantity = Items.get(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				quantity = Items.get(0);
			}
		});

	}

	public boolean checkMedicineName(List<PeriodTrackerModelInterface> modelInterface, String pillName) {
		boolean isPresent = false;
		Pills pills = null;
		for (PeriodTrackerModelInterface interface1 : modelInterface) {
			pills = (Pills) interface1;
			if (pillName.equals(pills.getMedicineName())) {
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}
}
