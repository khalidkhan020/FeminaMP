package com.linchpin.periodtracker.adpators;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.AppIconModel;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;

public class GridAppIconAdaptor extends BaseAdapter {

	List<AppIconModel> appIconModels;
	Context context;
	View row;

	public GridAppIconAdaptor(Context context, List<AppIconModel> appIconModels2) {
		this.context = context;
		this.appIconModels = appIconModels2;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appIconModels.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appIconModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
Theme t;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ImageView imageView;
		CheckBox checkBox;
		TextView textView;
		row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.grid_item, parent, false);
			t=Theme.getCurrentTheme(context,row);
if(t!=null)
{
	t.applyTextColor(R.id.appname, "text_color");
	}
		}

		imageView = (ImageView) row.findViewById(R.id.appicon);
		textView= (TextView) row.findViewById(R.id.appname);
		checkBox = (CheckBox) row.findViewById(R.id.gridcheckbox);
		Uri uri = Uri.parse("android.resource://com.linchpin.myperiodtracker/drawable/"
				+ appIconModels.get(position).getAppIcon());
		imageView.setImageURI(uri);
		String packageName = context.getPackageName();
		int resId = context.getResources().getIdentifier(appIconModels.get(position).getAppName(), "string",
				packageName);

		textView.setText(context.getResources().getString(resId));
		if(Integer.valueOf(APP.GLOBAL().getPreferences().getString("alias", "0"))==position)
		{
			if(t!=null)
				checkBox.setButtonDrawable(t.getDrawableResource("check2"));
				
			else
			checkBox.setButtonDrawable(context.getResources().getDrawable(R.drawable.check2));
		}
		else{
			if(t!=null)
				checkBox.setButtonDrawable(t.getDrawableResource("uncheck2"));
				
			else
			checkBox.setButtonDrawable(context.getResources().getDrawable(R.drawable.uncheck2));
		}
		
		return row;
	}

}
