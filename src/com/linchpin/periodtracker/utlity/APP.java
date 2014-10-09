package com.linchpin.periodtracker.utlity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.PeriodLogModel;
import com.linchpin.periodtracker.model.TimeLineModel;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.customcode.CustomCodeService;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.UserService;

public class APP extends Application
{
	static APP						app				= null;
	public static User				currentUser;
	
	public static User				myPartnerProfile;
	SharedPreferences				preferences;
	Editor							editor;
	static CustomCodeService		customCodeService;
	static UserService				userService;
	static String					parnerId;
	List<DayDetailModel>			detailModels	= new ArrayList<DayDetailModel>();
	List<TimeLineModel>				timeLineModels	= new ArrayList<TimeLineModel>();
	HashMap<Long, TimeLineModel>	timeLineMaps	= new HashMap<Long, TimeLineModel>();
	List<PeriodLogModel>			periodLogModels	= new ArrayList<PeriodLogModel>();
	private final String			API_KEY			= "_a2e5ae30e9dc16b3c5f9530af57bbce7ad26a44193cdc0dca423fa3abd1d7bfe";
	final String					SECRET_KEY		= "551fe0315dfa85b2092ca2d7162fc6f27569cb7552422d3103c0ac29f36f8337";
	public static WhoIAm			WHOIAM			= WhoIAm.SENDER;
	public static PartnerHome		PARTNER_HOME	= PartnerHome.NO_PARTNER;
	public static boolean			purchaseAppsMszShowOnltOnce;
	
	//
	public HashMap<Long, TimeLineModel> getTimeLineMaps()
	{
		return timeLineMaps;
	}
	
	public void setTimeLineMaps(HashMap<Long, TimeLineModel> timeLineMaps)
	{
		this.timeLineMaps = timeLineMaps;
	}
	
	public List<TimeLineModel> getTimeLineModels()
	{
		return timeLineModels;
	}
	
	public void storePartnerLengths(JSONObject object)
	{
		try
		{
			getEditor().putInt(PARTNER.FIELDS.CYCLE_LENGTH.key, object.getInt(PARTNER.FIELDS.CYCLE_LENGTH.key)).commit();
			getEditor().putInt(PARTNER.FIELDS.PERIOD_LENGTH.key, object.getInt(PARTNER.FIELDS.PERIOD_LENGTH.key)).commit();
			getEditor().putInt(PARTNER.FIELDS.OVULATION_LENGTH.key, object.getInt(PARTNER.FIELDS.OVULATION_LENGTH.key)).commit();
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setTimeLineModels(List<TimeLineModel> timeLineModels)
	{
		this.timeLineModels = timeLineModels;
	}
	
	public void showDeviceSpecifcation(Activity activity)
	{
		DisplayMetrics dm = getResources().getDisplayMetrics();
		
		double x = dm.widthPixels / dm.xdpi;
		double y = dm.heightPixels / dm.ydpi;
		double screenInches = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		String dpi = "";
		if (dm.density == .75) dpi = "LDPI";
		else if (dm.density == 1.0) dpi = "MDPI";
		else if (dm.density == 1.5) dpi = "HDPI";
		else if (dm.density == 2.0) dpi = "XHDPI";
		else if (dm.density == 3.0) dpi = "XXHDPI";		
		else if (dm.density == 4.0) dpi = "XXXHDPI";
		
		String screeebcat = "";
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) screeebcat = "SMALL";
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) screeebcat = "NORMAL";
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) screeebcat = "LARGE";
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) screeebcat = "XLARGE";
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_UNDEFINED) screeebcat = "UNDEFINED";
		
		Toast.makeText(activity, "" + dm.densityDpi, Toast.LENGTH_LONG).show();
		CustomAlertDialog.Dialog(activity).getAlertDialog(
				"Device Specification",
				"Devide Screen Category\t" + screeebcat +
				"\nDevide Dencity Group\t" + dpi + 
				 "\nDensity\t" + dm.density + "\nWidth\t" + dm.widthPixels + "\nHeight\t" + dm.heightPixels + "\nScaledDensity\t" + dm.scaledDensity + "\nXdpi\t" + dm.xdpi + "\nYdpi\t" + dm.ydpi + "\n X Inches \t" + x + "\n Y Inches \t" + y + "\nScreen Size\t"
						+ screenInches, false, "", true, "OK", false, null);
		
	}
	
	public List<DayDetailModel> getPartnerDDModels()
	{
		return detailModels;
	}
	
	public void setPartnerDDModels(List<DayDetailModel> detailModels)
	{
		this.detailModels = detailModels;
	}
	
	public List<PeriodLogModel> getPartnerPLModels()
	{
		return periodLogModels;
	}
	
	public void setPartnerPLModels(List<PeriodLogModel> periodLogModels)
	{
		this.periodLogModels = periodLogModels;
	}
	
	public Editor getEditor()
	{
		return this.editor;
	}
	
	public final static boolean isValidEmail(CharSequence target)
	{
		if (target == null)
		{
			return false;
		}
		else
		{
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
	
	public static String getParnerId()
	{
		return APP.parnerId;
	}
	
	public static void setParnerId(String parnerId)
	{
		APP.parnerId = parnerId;
	}
	
	public static enum PARTNER
	{
		LENGTH_TABLE("F");
		public String	key;
		
		private PARTNER(String key)
		{
			this.key = key;
		}
		
		public enum FIELDS
		{
			PERIOD_LENGTH("E_A"), CYCLE_LENGTH("E_B"), OVULATION_LENGTH("E_C");
			public String	key;
			
			private FIELDS(String key)
			{
				this.key = key;
			}
			
		}
		
	}
	
	public static enum GENDER
	{
		MALE("male"), FEMALE("female");
		public String	key;
		
		private GENDER(String key)
		{
			this.key = key;
		}
	}
	
	public static enum GOAL
	{
		//MEN_CAL("menstrual_calendar"),
		SAVE_CURRENTUSER_INFO("save the user information"), SAVE_PARTNERUSER_INFORMATION("save the partner user information"),
		//	SAVE_CURRENTPART_INFO("save the partner user information"),
		MY_PARTNERSTATUS("save the partner user status"), SAVE_WHO_AM_I("save the about user data"), LOG_TO_START("login/signup_to_start_sharing_data"), IS_HOME_SCREEN_VISITED("check whether welcome is visit or not");
		public String	key;
		
		private GOAL(String key)
		{
			this.key = key;
		}
	}
	
	public static enum PREF
	{
		
		START_DATE("start_date"), /***/
		END_DATE("end_date"), /***/
		/***/
		PURCHASED("purchased"), /***/
		ENABLE_TIP("enable_tip"), /***/
		THEME_NAME("theme"), /***/
		THEME_COMPONENT("theme_comp"), /***/
		IS_FIRST_TIME("IsFirstTime"), /***/
		INSTALL_DATE("InstallDate"), /***/
		WIDGET_LOCK("widget_lock"), /***/
		WIDGET_ID("widget_id"), /** Boolean Key which provide whether the context has new feature or not*/
		NEW_FETURE("NewFeature_Apper"), /***/
		NEW_FETURE_CHANGE_ICO("NewFeature_Apper_ChangeIcon"), /**Boolean key which specify that user wants to share her Priod data or not*/
		SHARE_PERIODS("share_periods"), /**Boolean key which specify that user wants to share her Moods data or not*/
		SHARE_MOODS("share_moods"), /**Boolean key which specify that user wants to share her Symptoms data or not*/
		SHARE_SYMPTOMS("share_symp"), /**Boolean key which specify that user wants to share her Pills data or not*/
		SHARE_PILLS("share_pills"), /**Boolean key which specify that user wants to share her Notes or not*/
		SHARE_NOTES("share_notes"), CURRENT_SESSION("current_session"), PARTNER_CYCLE_LENGTH("partner_cycle_l"), PARTNER_PERIOD_LENGTH("partner_period_l"), PARTNER_OVULATION_LENGTH("partner_ovulation_l"), PARTNER_AUTO_SYNCH("partner_autosynch"), NEED_TO_CHANGE("need_to_change"), ALARM_NITIFICATION_ID(
				"alarm_notif_id"),
		
		SHARE_NOTIFY("notification");
		
		public String	key;
		
		private PREF(String key)
		{
			this.key = key;
		}
		
	}
	
	public static enum TipsPath
	{
		Wiidget("NULL", 1), RegionlSetting("com.linchpin.periodtracker.view.RegionalSettingsView", 2), Trnslation("com.linchpin.periodtracker.view.HelpUs", 3), Language("com.linchpin.periodtracker.view.RegionalSettingsView", 4), TimeLine("com.linchpin.periodtracker.view.TimeLine", 5), AdsFree(
				"com.linchpin.periodtracker.view.SettingsView", 6), AppIcon("com.linchpin.periodtracker.view.ChangeAppIconView", 7), Notification("com.linchpin.periodtracker.view.NotificationSettingsView", 8), Pill("com.linchpin.periodtracker.view.AddNoteView", 9), Passcode(
				"com.linchpin.periodtracker.view.PasswordView", 10);
		public String	path;
		public int		id;
		
		private TipsPath(String path, int id)
		{
			this.path = path;
			this.id = id;
		}
		
	}
	
	public String getAppVersion()
	{
		PackageInfo pInfo = null;
		try
		{
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			return pInfo.versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	public SharedPreferences getPreferences()
	{
		if (preferences == null)
		{
			preferences = PreferenceManager.getDefaultSharedPreferences(this);
			
		}
		return preferences;
	}
	
	public static UserService getUserService()
	{
		return userService;
	}
	
	public static void setUserService(UserService userService)
	{
		APP.userService = userService;
	}
	
	public static CustomCodeService getCustomCodeService()
	{
		return customCodeService;
	}
	
	public void setCustomCodeService(CustomCodeService customCodeService)
	{
		this.customCodeService = customCodeService;
	}
	
	public static APP GLOBAL()
	{
		if (app == null) app = new APP();
		return app;
	}
	
	public enum WhoIAm
	{
		NO_ONE, SENDER, REVEVER;
	}
	
	public enum PartnerHome
	{
		NO_PARTNER, RECEIVED_INVITATION, RECEVED_REUEST, SEND_REQUEST, SEND_INVITATION, SENDER, RECEVER;
	}
	
	@Override
	public void onCreate()
	{
		App42API.initialize(this.getApplicationContext(), API_KEY, SECRET_KEY);
		customCodeService = App42API.buildCustomCodeService();
		userService = App42API.buildUserService();
		
		app = this;
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		//getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
		
		this.editor = preferences.edit();
		super.onCreate();
	}
	
	public boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public void exicuteLIOAnim(Activity activity)
	{
		activity.overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
	
	public void exicuteRIOAnim(Activity activity)
	{
		
		activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}
	
}
