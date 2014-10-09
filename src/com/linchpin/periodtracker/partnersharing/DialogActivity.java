package com.linchpin.periodtracker.partnersharing;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.widget.MPTProgressDialog;
import com.shephertz.app42.paas.sdk.android.App42CallBack;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogActivity extends Activity implements OnClickListener, TextWatcher
{
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	private void applyTheme()
	{
		Theme t = Theme.getCurrentTheme(this, findViewById(R.id.parent));
		if (t != null)
		{
			t.applyBackgroundColor(R.id.parent, "newsettingsbackground");
			t.applyTextColor(R.id.info, "text_color");
			t.applyTextColor(R.id.partner_name, "text_color");
			t.applyTextColor(R.id.partner_id, "text_color");
			t.applyBackgroundDrawable(R.id.request, "mpt_button_sltr");
			t.applyImageDrawable(R.id.imageView1, "ic_user");
			t.applyImageDrawable(R.id.imageView2, "ic_user_id");
		}
	}
	
	@Override
	protected void onStart()
	{
		EasyTracker.getInstance(this).activityStart(this);
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop()
	{
		EasyTracker.getInstance(this).activityStop(this);
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	EditText			partnerName, partnerId;
	Button				request;
	TextView			info;
	LinearLayout		user_id_pan;
	int					requesttype	= 0;
	MPTProgressDialog	progressDialog;
	
	//	HelpButton help;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.partner_invite_dialog);
		info = (TextView) findViewById(R.id.info);
		request = (Button) findViewById(R.id.request);
		//cancel = (Button) findViewById(R.id.cancel);
		partnerId = (EditText) findViewById(R.id.partner_id);
		partnerName = (EditText) findViewById(R.id.partner_name);
		requesttype = getIntent().getIntExtra("request_type", 0);
		user_id_pan = (LinearLayout) findViewById(R.id.user_id_pan);
		applyTheme();
		/*	help=(HelpButton)findViewById(R.id.partnerinvitehelpbutton);
			help.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(DialogActivity.this, HomeScreenHelp.class);
					if (!PeriodTrackerObjectLocator.getInstance().getPregnancyMode())
					{
						intent.putExtra("classname", "invitepartnerhelp");
					}
					startActivity(intent);
					
				}
					// TODO Auto-generated method stub
			});*/
		if (requesttype == 0)
		{
			info.setText(getString(R.string.req_to_invite));
			request.setText(getString(R.string.invite));
		}
		else if (requesttype == 1)
		{
			info.setText(getString(R.string.req_for_sharing));
			request.setText(getString(R.string.request));
		}
		else if (requesttype == 2)
		{
			user_id_pan.setVisibility(View.GONE);
			info.setText(getString(R.string.forgot_pass));
			request.setText(getString(R.string.ok));
		}
		request.setEnabled(false);
		partnerName.addTextChangedListener(this);
		partnerId.addTextChangedListener(this);
		progressDialog = new MPTProgressDialog(this, R.drawable.spinner);
		
		progressDialog.setCancelable(false);
	}
	
	Handler	handler	= new Handler();
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.request)
		{
			if (APP.GLOBAL().isNetworkAvailable())
			{
				request.setEnabled(false);
				progressDialog.show();
				if (requesttype == 2)
				{
					if (APP.GLOBAL().isNetworkAvailable())
					{
						Toast.makeText(this, getString(R.string.request_reset_pass), Toast.LENGTH_LONG).show();
						
						APP.getUserService().resetUserPassword(partnerId.getText().toString().trim(), new App42CallBack()
						{
							
							@Override
							public void onSuccess(Object arg0)
							{
								handler.post(new Runnable()
								{
									
									@Override
									public void run()
									{
										if (progressDialog.isShowing()) progressDialog.dismiss();
										Toast.makeText(DialogActivity.this, getString(R.string.success_reset_pass), Toast.LENGTH_LONG).show();
										
										DialogActivity.this.finish();
										
									}
								});
								
							}
							
							@Override
							public void onException(Exception arg0)
							{
								handler.post(new Runnable()
								{
									
									@Override
									public void run()
									{
										if (progressDialog.isShowing()) progressDialog.dismiss();
										DialogActivity.this.finish();
										Toast.makeText(DialogActivity.this, getString(R.string.failed_reset_pass), Toast.LENGTH_LONG).show();
										
									}
								});
								
							}
						});
						
					}
					else Toast.makeText(this, getString(R.string.internetconnectioncheck), Toast.LENGTH_LONG).show();
				}
				else
				{
					if (!APP.currentUser.userName.equals(partnerId.getText().toString()))
					{
						JSONObject jsonBody = new JSONObject();
						try
						{
							
							jsonBody.put("userid", APP.currentUser.userName);
							if (requesttype == 0)
							{
								Toast.makeText(this, getString(R.string.sending_invtsn), Toast.LENGTH_LONG).show();
								jsonBody.put("receverid", partnerId.getText().toString());
								jsonBody.put("senderid", APP.currentUser.userName);
							}
							else
							{
								Toast.makeText(this, getString(R.string.sending_rqst), Toast.LENGTH_LONG).show();
								jsonBody.put("receverid", APP.currentUser.userName);
								jsonBody.put("senderid", partnerId.getText().toString());
							}
							jsonBody.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
						}
						catch (JSONException e)
						{
							
						}
						
						((APP) getApplication()).getCustomCodeService().runJavaCode("Invite", jsonBody, new App42CallBack()
						{
							
							@Override
							public void onException(Exception arg0)
							{
								handler.post(new Runnable()
								{
									
									@Override
									public void run()
									{
										
										if (progressDialog.isShowing()) progressDialog.dismiss();
										request.setEnabled(true);
										Toast.makeText(DialogActivity.this, getString(R.string.failed_sending_rqst), Toast.LENGTH_LONG).show();
										
									}
								});
								
							}
							
							@Override
							public void onSuccess(Object arg0)
							{
								handler.post(new Runnable()
								{
									
									@Override
									public void run()
									{
										if (progressDialog.isShowing()) progressDialog.dismiss();
										request.setEnabled(true);
										setResult(Activity.RESULT_OK);
										Toast.makeText(DialogActivity.this, getString(R.string.success_sending_rqst), Toast.LENGTH_LONG).show();
										
										finish();
										
									}
								});
								
							}
						});
						
					}
					else
					{
						Toast.makeText(DialogActivity.this, getString(R.string.same_email), 1000).show();
					}
				}
			}
			else
			{
				Toast.makeText(DialogActivity.this, getString(R.string.no_data_connection), 1000).show();
			}
		}
		
	}
	
	@Override
	public void afterTextChanged(Editable s)
	{
		request.setEnabled(APP.isValidEmail(partnerId.getText().toString().trim()));
		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		// TODO Auto-generated method stub
		
	}
	
}
