package com.linchpin.periodtracker.view;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

public class TabFactory implements TabContentFactory {
	private final Context mContext;

	public TabFactory(Context c) {
		this.mContext = c;
	}

	@Override
	public View createTabContent(String arg0) {
		// TODO Auto-generated method stub
		View v = new View(mContext);
		v.setMinimumWidth(0);
		v.setMinimumHeight(0);
		return v;

	}

}
