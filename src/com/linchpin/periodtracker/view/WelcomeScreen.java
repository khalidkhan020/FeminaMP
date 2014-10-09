package com.linchpin.periodtracker.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.interfaces.MptClickableSpan;
import com.linchpin.periodtracker.partnersharing.Policies;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;

public class WelcomeScreen extends Activity
{
	
	Button		btnAccept, btnCancel;
	CheckBox	chkAccept;
	TextView	termsandcondition;
	
	@Override
	public void onBackPressed()
	{
		askForExit();
	}
	
	private void applyTheme()
	{
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyTextColor(R.id.welcomemessage, "text_color");
			t.applyTextColor(R.id.terms_condition, "text_color");
			t.applyTextColor(R.id.accept_welcome, "text_color");
			t.applyTextColor(R.id.cancel_welcome, "text_color");
			t.applyBackgroundColor(R.id.accept_welcome, "view_bg");
		}
		
	}
	
	@Override
	protected void onStart()
	{
		EasyTracker.getInstance(this).activityStart(this);
		super.onStart();
	}
	
	@Override
	protected void onStop()
	{
		EasyTracker.getInstance(this).activityStop(this);
		super.onStop();
	}
	
	protected void onCreate(android.os.Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcomescreen);
		APP.GLOBAL().showDeviceSpecifcation(this);
		btnAccept = (Button) findViewById(R.id.accept_welcome);
		btnCancel = (Button) findViewById(R.id.cancel_welcome);
		chkAccept = (CheckBox) findViewById(R.id.checkcondition);
		termsandcondition = (TextView) findViewById(R.id.terms_condition);
		String string = getString(R.string.agreement_4) + "<a href='1'> " + getString(R.string.agreement_5) + " </a> " + getString(R.string.agreement_2) + " <a href='2'> " + getString(R.string.agreement_6) + " </a>";
		setTextViewHTML(termsandcondition, string);
		btnAccept.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				if (chkAccept.isChecked())
				{					
					Intent welcomeGoal = new Intent(WelcomeScreen.this, SelectGoal.class);
					startActivity(welcomeGoal);
					finish();
					APP.GLOBAL().exicuteRIOAnim(WelcomeScreen.this);
					
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please accept term of service and privacy policy.", Toast.LENGTH_LONG).show();
				}
				// TODO Auto-generated method stub
				
			}
		});
		btnCancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				askForExit();
				
			}
		});
		
		applyTheme();
	}
	
	private void setTextViewHTML(TextView text, String html)
	{
		text.setLinksClickable(true);
		text.setMovementMethod(LinkMovementMethod.getInstance());
		CharSequence sequence = Html.fromHtml(html);
		SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
		URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
		for (URLSpan span : urls)
		{
			makeLinkClickable(strBuilder, span);
		}
		text.setText(strBuilder);
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onResume()
	{
		
		//super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		
		//super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	private void askForExit()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getResources().getString(R.string.notifiction));
		alert.setMessage(getResources().getString(R.string.exitmessage));
		alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
				
			}
		});
		alert.setNegativeButton(getResources().getString(R.string.no), null);
		alert.show();
		// TODO Auto-generated method stub
		
	}
	
	private void makeLinkClickable(SpannableStringBuilder strBuilder, URLSpan span)
	{
		int start = strBuilder.getSpanStart(span);
		int end = strBuilder.getSpanEnd(span);
		int flags = strBuilder.getSpanFlags(span);
		MptClickableSpan clickable = new MptClickableSpan()
		{
			
			@Override
			public void onClick(View widget)
			{
				
				Intent intent = new Intent(WelcomeScreen.this, Policies.class);
				intent.putExtra("what", getUri());
				startActivity(intent);
				
			}
		};
		clickable.setUri(span.getURL());
		strBuilder.setSpan(clickable, start, end, flags);
		strBuilder.removeSpan(span);
		// TODO Auto-generated method stub
		
	}
	
}
