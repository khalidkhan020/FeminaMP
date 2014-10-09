package com.linchpin.periodtracker.partnersharing;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.drawable.DrawableUtil;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.interfaces.MptClickableSpan;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.widget.MPTProgressDialog;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Exception;

public class Register extends Activity implements OnClickListener
{
	
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
	
	Button	register;
	APP		app;
	EditText	email, password, repassword, fname, lname;
	CheckBox	checkemail, repass, condition;
	TextView	condition_txt;
	RadioGroup	radioGender;
	Theme		t;
	
	private void findAllViews()
	{
		email = (EditText) findViewById(R.id.email);
		repassword = (EditText) findViewById(R.id.repassword);
		password = (EditText) findViewById(R.id.password);
		fname = (EditText) findViewById(R.id.fname);
		lname = (EditText) findViewById(R.id.lname);
		register = (Button) findViewById(R.id.register);
		condition_txt = (TextView) findViewById(R.id.conditions_txt);
		checkemail = (CheckBox) findViewById(R.id.checkemail);
		condition = (CheckBox) findViewById(R.id.condition);
		repass = (CheckBox) findViewById(R.id.checkpass);
		radioGender = (RadioGroup) findViewById(R.id.radioSex);
		DrawableUtil.roundedCorner = new float[]
		{
				5, 5, 5, 5, 5, 5, 5, 5
		};
		findViewById(R.id.parent).setBackgroundDrawable(DrawableUtil.getShapeDrawable(0xFFECD0EC));
	}
	
	private final Handler	myHandler	= new Handler();
	
	private void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
	{
		int start = strBuilder.getSpanStart(span);
		int end = strBuilder.getSpanEnd(span);
		int flags = strBuilder.getSpanFlags(span);
		MptClickableSpan clickable = new MptClickableSpan()
		{
			
			@Override
			public void onClick(View widget)
			{
				
				Intent intent = new Intent(Register.this, Policies.class);
				intent.putExtra("what", getUri());
				startActivity(intent);
				
			}
		};
		clickable.setUri(span.getURL());
		
		strBuilder.setSpan(clickable, start, end, flags);
		strBuilder.removeSpan(span);
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
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.partner_reg);
		findAllViews();
		applyTheme();
		register.setOnClickListener(this);
		
		email.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				checkemail.setVisibility(View.GONE);
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				checkemail.setVisibility(View.VISIBLE);
				checkemail.setChecked(APP.isValidEmail(email.getText().toString()));
				
			}
		});
		
		myDialog = new MPTProgressDialog(this, R.drawable.spinner);
		
		myDialog.setCancelable(false);
		TextWatcher tw = new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				repass.setVisibility(View.GONE);
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				repass.setVisibility(View.VISIBLE);
				repass.setChecked(!password.getText().toString().trim().equals("") && password.getText().toString().trim().equals(repassword.getText().toString().trim()));
				
			}
		};
		repassword.addTextChangedListener(tw);
		password.addTextChangedListener(tw);
		String string = getString(R.string.agreement_4) + "<a href='1'> " + getString(R.string.agreement_5) + " </a> " + getString(R.string.agreement_2) + " <a href='2'> " + getString(R.string.agreement_6) + " </a>";
		
		setTextViewHTML(condition_txt, string);
		
	}
	
	private void applyTheme()
	{
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{
			
		}
		
		// TODO Auto-generated method stub
		
	}
	
	String	msg;
	
	private boolean validate()
	{
		boolean b = checkemail.isChecked() && repass.isChecked() && !fname.getText().toString().trim().equals("") && !lname.getText().toString().trim().equals("");
		register.setEnabled(b);
		return b;
	}
	
	MPTProgressDialog	myDialog;
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.register)
		{
			if (APP.GLOBAL().isNetworkAvailable())
			{
				if (condition.isChecked())
				{
					register.setEnabled(false);
					
					if (validate())
					{
						myDialog.show();
						JSONObject jsonBody = new JSONObject();
						
						try
						{
							jsonBody.put("fname", fname.getText().toString());
							jsonBody.put("lname", lname.getText().toString());
							jsonBody.put("userid", email.getText().toString());
							jsonBody.put("passwd", password.getText().toString());
							if (R.id.radioFemale == radioGender.getCheckedRadioButtonId())
							{
								jsonBody.put("gender", "F");
								APP.GLOBAL().getEditor().putBoolean(APP.GENDER.FEMALE.key, true).commit();
							}
							else
							{
								jsonBody.put("gender", "M");
								APP.GLOBAL().getEditor().putBoolean(APP.GENDER.MALE.key, true).commit();
							}
						}
						catch (JSONException e)
						{
							
						}
						
						((APP) getApplication()).getCustomCodeService().runJavaCode("Register", jsonBody, new App42CallBack()
						{
							
							@Override
							public void onSuccess(Object arg0)
							{
								myHandler.post(new Runnable()
								{
									
									@Override
									public void run()
									{
										register.setEnabled(true);
										if (myDialog.isShowing()) myDialog.dismiss();
										Toast.makeText(Register.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
										Register.this.finish();
									}
								});
								
							}
							
							@Override
							public void onException(Exception e)
							{
								
								App42Exception exception = (App42Exception) e;
								try
								{
									JSONObject jsonObject = new JSONObject(exception.getMessage()).getJSONObject("app42Fault");
									try
									{
										jsonObject = jsonObject.getJSONObject("trace").getJSONObject("app42Fault");
										msg = jsonObject.getString("details");
									}
									catch (JSONException jpx)
									{
										msg = jsonObject.getString("trace");
									}
									
									myHandler.post(new Runnable()
									{
										
										@Override
										public void run()
										{
											register.setEnabled(true);
											if (myDialog.isShowing()) myDialog.dismiss();
											Toast.makeText(Register.this, msg, Toast.LENGTH_LONG).show();
											
										}
									});
								}
								catch (JSONException e1)
								{
									if (myDialog.isShowing()) myDialog.dismiss();
									e1.printStackTrace();
								}
							}
						});
						
					}
					else
					{
						register.setEnabled(true);
						Toast.makeText(Register.this, getString(R.string.provide_input), Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					register.setEnabled(true);
					Toast.makeText(Register.this, getString(R.string.accept_terms), Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(Register.this, getString(R.string.no_data_connection), 1000).show();
			}
		}
		else
		{
		}
		
	}
	
}
