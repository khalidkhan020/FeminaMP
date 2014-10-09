package com.linchpin.periodtracker.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.analytics.tracking.android.EasyTracker;
import com.linchpin.myperiodtracker.R;

public class HomeScreenHelp extends Activity{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.periodlog_xml);
		
		Intent intent = getIntent();
		String string =intent.getStringExtra("classname");
		
		if(string.equals("periodlog")){
			setContentView(R.layout.periodlog_xml);
		}
		else if(string.equals("invite_no_partner")){
			setContentView(R.layout.partner_invite_xml);
		}
		else if(string.equals("partner_setting_help")){
			setContentView(R.layout.sharing_setting_xml);
		}
		else if(string.equals("partner_Calender")){
			setContentView(R.layout.partner_calender_xml);
		}
		else if(string.equals("invite_with_partner")){
			setContentView(R.layout.partner_with_invite_xml);
		}
		else if(string.equals("partner_today")){
			setContentView(R.layout.partner_today_xml);
		}
		else if(string.equals("invite_recieved_partner")){
			setContentView(R.layout.partner_invite_received_xml);
		}else if(string.equals("invitepartnerhelp")){
			setContentView(R.layout.invitepartnerhelp_xml);
		}else if(string.equals("fertilelog")){
			setContentView(R.layout.fertilelog_xml);
		}else if(string.equals("ovulationlog")){
			setContentView(R.layout.ovulation_xml);
		}else if(string.equals("addnote")){
			setContentView(R.layout.notes_notes_xml);
		}else if(string.equals("calender")){
			setContentView(R.layout.calander_xml);
		}else if(string.equals("pills")){
			setContentView(R.layout.notes_pills_xml);
		}else if(string.equals("mood")){
			setContentView(R.layout.notes_mood_xml);
		}else if(string.equals("symtom")){
			setContentView(R.layout.notes_symptoms_xml);
		}else if(string.equals("weight")){
			setContentView(R.layout.notes_weight_xml);
		}else if(string.equals("pregnancyon")){
			setContentView(R.layout.pregnancy_on);
		}else if(string.equals("pregnancyoff")){
			setContentView(R.layout.pregnancy_off);
		}else if(string.equals("nopassword")){
			setContentView(R.layout.setpasscode_xml);
		}else if(string.equals("passwordset")){
			setContentView(R.layout.change_clear_passcode);
		}else if(string.equals("setpasscode")){
			setContentView(R.layout.set_passcode_inner);
		}else if(string.equals("clearpasscode")){
			setContentView(R.layout.clear_passcode_xml);
		}else if(string.equals("changepasscode")){
			setContentView(R.layout.change_passcode_xml);
		}else if(string.equals("dashboard")){
			setContentView(R.layout.dashboard_xml);
		}else if(string.equals("dashbordpregnancy")){
			setContentView(R.layout.dashboard2_xml);
		}else if(string.equals("restoreandbackup")){
			setContentView(R.layout.store_backup_xml);
		}else if(string.equals("NotificationSettingsView")){
			setContentView(R.layout.schedule_notification_help);
		}else if(string.equals("changeAppIconView")){
			setContentView(R.layout.changeapp_icon);
		}
		
		
        findViewById(R.id.imageClose).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	
        findViewById(R.id.imageLayout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		

		EasyTracker.getInstance(this).activityStart(this);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);

	}
}
