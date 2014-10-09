package com.linchpin.periodtracker.adpators;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;

public class CustomArrayAdaptor extends BaseAdapter {
	public View row;
	public ViewGroup viewGroup;
	List<File> arrayList;
	Context context;
	Dialog alertDialog;

	public CustomArrayAdaptor(Context context, List<File> files, Dialog alertDialog) {
		this.context = context;
		this.arrayList = files;
		this.alertDialog = alertDialog;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		TextView textView, textView2;
		RadioButton imageviView;
		row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.restorelistrow, parent, false);

		}
		viewGroup = parent;
		textView = (TextView) row.findViewById(R.id.listitem1);
		textView.setText(arrayList.get(position).getName());
		textView2 = (TextView) row.findViewById(R.id.listitem2);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTimeInMillis(arrayList.get(position).lastModified());
		textView2.setText(new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat() + " hh:mm")
				.format(calendar.getTime()));
		imageviView = (RadioButton) row.findViewById(R.id.radio);
		imageviView.setVisibility(View.GONE);

		textView.setTextColor(Color.parseColor("#000000"));

		/*
		 * row.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub ((RadioButton) v.findViewById(R.id.radio)).setChecked(true);
		 * if(RestoreAndBackupActivity.dialogInterface!=null){
		 * RestoreAndBackupActivity.dialogInterface.dismiss(); Res }
		 * 
		 * } });
		 */return row;
	}

}
