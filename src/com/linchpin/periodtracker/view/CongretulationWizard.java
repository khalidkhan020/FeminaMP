package com.linchpin.periodtracker.view;


import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.partnersharing.LoginScreen;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CongretulationWizard extends Activity implements OnClickListener
{
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	TextView congrets;
	Theme t;
	private void applyTheme()
	{
		
		t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{			
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyTextColor(R.id.congrets, "text_color");
			t.applyTextColor(R.id.btnyes, "co_btn_fg");
			t.applyBackgroundDrawable(R.id.btnyes, "mpt_button_sltr");
			t.applyTextColor(R.id.btnno, "text_color");
			
		}			
	}
	@Override
	public void onBackPressed()
	{
		Intent setbackActivity=new Intent(CongretulationWizard.this,WelcomeSetting2.class);
		startActivity(setbackActivity);
		finish();
		APP.GLOBAL().exicuteLIOAnim(this);
		super.onBackPressed();
	}
	Button yes,no;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.congretulationwizard);
		yes=(Button)findViewById(R.id.btnyes);
		no=(Button)findViewById(R.id.btnno);
		congrets=(TextView)findViewById(R.id.congrets);
		Typeface custonfont=Typeface.createFromAsset(getAssets(), "fonts/festus!.ttf");
		congrets.setTypeface(custonfont);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
		applyTheme();
		
	}
	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.btnyes)
		{
			Intent setDefalutValue=new Intent(CongretulationWizard.this,LoginScreen.class);
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.IS_FIRST_TIME.key, true).commit();
			startActivity(setDefalutValue);
			finish();
			APP.GLOBAL().exicuteRIOAnim(this);	
		}
		if(v.getId()==R.id.btnno)
		{
			Intent homeScreen = null;
			homeScreen=new Intent(CongretulationWizard.this,HomeScreenView.class);
			APP.GLOBAL().getEditor().putBoolean(APP.PREF.IS_FIRST_TIME.key, true).commit();
			startActivity(homeScreen);
			finish();
			APP.GLOBAL().exicuteRIOAnim(CongretulationWizard.this);
		}
		
	}
}
