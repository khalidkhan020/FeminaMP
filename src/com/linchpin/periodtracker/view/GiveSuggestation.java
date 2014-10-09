package com.linchpin.periodtracker.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.adpators.CorrectionListAdapter;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.utlity.APP.PREF;

public class GiveSuggestation extends Activity implements OnClickListener
{
	EditText							correct, incorrect;
	StringBuilder						sb;
	ListView							correctionLst;
	ArrayList<CorrectionItemCollection>	arrayList;
	Theme t ;
	private void applyTheme()
	{
		
			 t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyBackgroundColor(R.id.l_layout, "content_lyr");
			t.applyBackgroundDrawable(R.id.done_btn, "mpt_done");
			t.applyBackgroundDrawable(R.id.more, "mpt_more");
			t.applyTextColor(R.id.text_incurect, "text_color");
			t.applyTextColor(R.id.text_correct, "text_color");
			
			t.applyBackgroundDrawable(R.id.text_incurect, "mpt_edit_text_sltr");
			t.applyBackgroundDrawable(R.id.text_correct, "mpt_edit_text_sltr");
			
		}}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.give_suggestion);
		/*sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<root>");*/
		applyTheme();
		findViewById(R.id.more).setOnClickListener(this);
		findViewById(R.id.done_btn).setOnClickListener(this);
		findViewById(R.id.back_pan).setOnClickListener(this);
		correctionLst = (ListView) findViewById(R.id.correction_lst);
		correct = (EditText) findViewById(R.id.text_correct);
		incorrect = (EditText) findViewById(R.id.text_incurect);
		//((TextView) findViewById(R.id.txt_back)).setText(getString(R.string.give_suggestion));
		arrayList=new ArrayList<CorrectionItemCollection>();
		correctionLst.setAdapter(new CorrectionListAdapter(this, arrayList));
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
	
	public class CorrectionItemCollection
	{
		public String	correct, incorrect;
	}
	private void generateDoc()
	{
		sb = new StringBuilder();
		sb.append("Language :\n"+PeriodTrackerObjectLocator.getInstance().getAppLanguage()+"\n");
//		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
//		sb.append("\n<root>");
		for(CorrectionItemCollection collection:arrayList)
		{
		sb.append("Correct :\n");
		sb.append("\t"+collection.correct+"\n");
		sb.append("Incorrect :");
		sb.append("\t"+collection.incorrect+"\n");
		
	}
//		sb.append("\n</root>");
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
		else if (id == R.id.more)
		{
			if(!correct.getText().toString().trim().equals("")&&!incorrect.getText().toString().trim().equals(""))
			{CorrectionItemCollection collection=new CorrectionItemCollection();
			collection.correct=correct.getText().toString().trim();
			collection.incorrect=incorrect.getText().toString().trim();
			correct.getText().clear();
			incorrect.getText().clear();
			arrayList.add(collection);
			correctionLst.setAdapter(new CorrectionListAdapter(this, arrayList));
			correctionLst.invalidate();	
			}else Toast.makeText(GiveSuggestation.this, "Input fields must not be empty.", Toast.LENGTH_LONG).show();
		}
		else if (id == R.id.done_btn)
		{
			generateDoc();
			Intent email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_address)});
			email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.correctioon_sub));
			email.putExtra(Intent.EXTRA_TEXT, sb.toString());
			email.setType("message/rfc822");
			startActivity(Intent.createChooser(email,getString(R.string.email_chooser)));
			APP.GLOBAL().exicuteLIOAnim(this);
		}
		else
		{
		}
		
	}
}
