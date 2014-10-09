package com.linchpin.periodtracker.utlity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;



public class CustomAlertDialog 
{
	public interface onButoinClick
	{
		public void onClickPositive(DialogInterface dialog, int which);
		public void onClickNegative(DialogInterface dialog, int which);
	}
	Context	context;

	private CustomAlertDialog(Context context)
	{
		this.context = context;
	}

	onButoinClick	buttinClick;

	public void getAlertDialog(String title, String msg, boolean ispositive, String postext, boolean isnegative, String negText, boolean iscancilable, final onButoinClick click)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setCancelable(iscancilable);
		buttinClick = click;
		if (ispositive) builder.setPositiveButton(postext, new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (buttinClick != null) buttinClick.onClickPositive(dialog, which);
				
			}
		});
		builder.setNegativeButton(negText, new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (buttinClick != null) buttinClick.onClickNegative(dialog, which);
				
			}
		});
		 builder.show();

	}

	static CustomAlertDialog	alertDialog;

	public static CustomAlertDialog Dialog(Context context)
	{
		//if (alertDialog == null)
		 alertDialog = new CustomAlertDialog(context);
		return alertDialog;

	}

}
