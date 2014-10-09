package com.caldroid;

import java.util.List;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;

public class WeekdayBaseAdptor extends BaseAdapter {
 Context context;
 List<String> list;
	
	public WeekdayBaseAdptor(Context context, List<String>list){
		this.context= context;
		this.list=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		if(arg1==null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view= inflater.inflate(R.layout.weekday_text_view, null);		
			
		}
			TextView text= (TextView) view.findViewById(R.id.weekdaytextview);
			if (list.get(arg0).length() <= 3) {
				text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			} else {
				text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
			}
			text.setText(list.get(arg0));
			
		return view;
	}

}
