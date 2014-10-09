package com.linchpin.periodtracker.adpators;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.view.GiveSuggestation.CorrectionItemCollection;

public class CorrectionListAdapter extends BaseAdapter {
	public View row;	
	ArrayList<CorrectionItemCollection>	arrayList;
	Context context;

	public CorrectionListAdapter(Context context, ArrayList<CorrectionItemCollection>	arrayList) {
		this.context = context;
		this.arrayList = arrayList;
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	private void applyTheme(View view)
	{
		Theme t ;
			 t = Theme.getCurrentTheme(context, view);
		if (t != null)
		{
			t.applyBackgroundColor(R.id.content, "content_lyr");
			t.applyBackgroundDrawable(R.id.remove, "mpt_remove");
			t.applyTextColor(R.id.incorrect, "text_color");
			t.applyTextColor(R.id.correct, "text_color");
			
		}}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		
		row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.suggestion_item, parent, false);
			applyTheme(row);
		}
		CorrectionItemCollection collection=arrayList.get(position);
		TextView tv=(TextView)row.findViewById(R.id.correct);
		tv.setText(collection.correct);
		tv=(TextView)row.findViewById(R.id.incorrect);
		tv.setText(collection.incorrect);
		ImageButton button=(ImageButton) row.findViewById(R.id.remove);
		button.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				arrayList.remove(position);
				
				CorrectionListAdapter.this.notifyDataSetChanged();
				CorrectionListAdapter.this.notifyDataSetInvalidated();
			}
		});
		return row;
	}

}
