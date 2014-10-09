package com.linchpin.periodtracker.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.APP.PREF;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;

public class HelpUs extends Activity implements OnClickListener
{
	private void applyTheme()
	{
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			t.applyBackgroundColor(R.id.ll, "view_bg");
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundDrawable(R.id.suggestation_btn, "mpt_button2_sltr");
			t.applyBackgroundDrawable(R.id.localize_btn, "mpt_button2_sltr");
			
			t.applyTextColor(R.id.suggestation_btn, "co_btn2_fg");
			t.applyTextColor(R.id.localize_btn, "co_btn2_fg");
		}
		}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.helpus);
	//	Tips.viewVisible(this, APP.TipsPath.Trnslation.id);
		findViewById(R.id.back_pan).setOnClickListener(this);
		findViewById(R.id.suggestation_btn).setOnClickListener(this);
		findViewById(R.id.localize_btn).setOnClickListener(this);
		ListView listView=((ListView)findViewById(R.id.ack_lst));
		listView.setAdapter(new ArrayAdapter(this,R.layout.ack_list_item, getResources().getStringArray(R.array.ack_lst)));
		//((TextView)findViewById(R.id.txt_back)).setText(getString(R.string.helpus));
		applyTheme();
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	
	@Override
	protected void onResume()
	{
		if (!APP.GLOBAL().getPreferences().getBoolean(PREF.PURCHASED.key, false))			
			PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		super.onResume();
	}
	
	
	@Override
	public void onBackPressed()
	{
		finish();
		APP.GLOBAL().exicuteRIOAnim(this);
		super.onBackPressed();
	}
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.back_pan)
		{
			finish();
			APP.GLOBAL().exicuteRIOAnim(this);
		}
		else if (id == R.id.suggestation_btn)
		{
			Intent  intent=new Intent(HelpUs.this,GiveSuggestation.class);
			startActivity(intent);
			APP.GLOBAL().exicuteLIOAnim(this);
		}
		else if (id == R.id.localize_btn)
		{
			Intent email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_address)});
			email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.localize_mail_sub));
			email.putExtra(Intent.EXTRA_TEXT, getString(R.string.localize_mail_body));
			email.setType("message/rfc822");
			startActivity(Intent.createChooser(email, getString(R.string.email_chooser)));
			APP.GLOBAL().exicuteLIOAnim(this);
		}
		else
		{
		}
		
	}
}
