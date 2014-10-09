package com.linchpin.periodtracker.partnersharing;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.widget.MPTProgressDialog;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Exception;

public class ChangePasssword extends Activity implements TextWatcher
{
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart()
	{EasyTracker.getInstance(this).activityStart(this);
		// TODO Auto-generated method stub
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop()
	{EasyTracker.getInstance(this).activityStop(this);
		// TODO Auto-generated method stub
		super.onStop();
	}

	EditText					oldPass, newPass, confirmPass;
	Button						ok;
	Handler						handler	= new Handler();
	private MPTProgressDialog	myDialog;
	CheckBox valid;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.change_passsword);
		oldPass = (EditText) findViewById(R.id.old_pass);
		newPass = (EditText) findViewById(R.id.new_password);
		confirmPass = (EditText) findViewById(R.id.confirm_pass);ok = (Button) findViewById(R.id.done);
		valid=(CheckBox) findViewById(R.id.valid);
		valid.setChecked(false);
		oldPass.addTextChangedListener(this);
		newPass.addTextChangedListener(this);
		confirmPass.addTextChangedListener(this);
		ok.setEnabled(false);
			ok.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				myDialog.show();
				if (newPass.getText().toString().trim().equals(confirmPass.getText().toString().trim())) APP.getUserService().changeUserPassword(APP.currentUser.getUserName(), oldPass.getText().toString().trim(), newPass.getText().toString().trim(), new App42CallBack()
				{
					
					@Override
					public void onSuccess(Object arg0)
					{
						handler.post(new Runnable()
						{
							
							@Override
							public void run()
							{
								if (myDialog.isShowing()) myDialog.dismiss();
								Toast.makeText(ChangePasssword.this, "Password Changed Successfully.", 1000).show();
								ChangePasssword.this.finish();
							}
						});
						
					}
					
					@Override
					public void onException(final Exception arg0)
					{
						handler.post(new Runnable()
						{
							
							@Override
							public void run()
							{
								if (myDialog.isShowing()) myDialog.dismiss();
								
							}
						});
						if (arg0 instanceof App42Exception)
						{
							
								handler.post(new Runnable()
								{
									
									@Override
									public void run()
									{
										if (((App42Exception) arg0).getAppErrorCode() == 0)
										{
										Toast.makeText(ChangePasssword.this, "Current password and new password are same!", 1000).show();
										}else if(((App42Exception) arg0).getAppErrorCode() == 2003)
										{
											Toast.makeText(ChangePasssword.this, "Password do not match!", 1000).show();
											
										}
									}
								});
							
						}
						
						
					}
				});
				
			}
		});
		myDialog = new MPTProgressDialog(this, R.drawable.spinner);
		
		myDialog.setCancelable(false);
		super.onCreate(savedInstanceState);
	}
	
	private boolean validate()
	{
		boolean emptytest=!newPass.getText().toString().trim().equals("")&&!confirmPass.getText().toString().trim().equals("");
		boolean equality=newPass.getText().toString().trim().equals(confirmPass.getText().toString().trim());
		valid.setChecked(emptytest&&equality);
		emptytest=emptytest&&!oldPass.getText().toString().trim().equals("");
		if(oldPass.getText().toString().trim().equals(newPass.getText().toString().trim())&&emptytest)
			Toast.makeText(this, "New pasword should no be same as Current password!", 1000).show();
		return emptytest&&!oldPass.getText().toString().trim().equals(newPass.getText().toString().trim())&&equality;
	}
	
	@Override
	public void afterTextChanged(Editable arg0)
	{
		ok.setEnabled(validate());
		
	}
	
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
	{
		// TODO Auto-generated method stub
		
	}
	
}
