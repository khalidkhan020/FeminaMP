package com.linchpin.periodtracker.theme;

import java.util.HashMap;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.internal.utils.UtilityWrapper;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.utlity.APP;

/**
 * This class contains All the utility methods which will be used to Apply theme
 * on a Appplication.
 * 
 * @author Khalid Khan **
 */
public class Theme
{
	
	public static String			GET_THEME	= "com.linchpin.periodtracker.theme.THEME_ACTION";
	public static String			THEME		= "theme";
	public static String			THEME_FILE	= "theme_file";
	private static final String		THIS_FILE	= "Theme";
	private View					view;
	private final PackageManager	pm;
	private Resources				remoteRes	= null;
	private PackageInfo				pInfos		= null;
	private Context					context;
	String							prefix		= "";
	boolean isinapp=false;
	
	/**
	 * Constructor to instansiate Theme class. 
	 * @param ctxt <a>Context</b>  Taks Current Context not Remote context. eg <b>this</b>
	 * @param compName  ComponentName
	 * @return {@link Theme} Instance of Theme Class
	 * @author Khalid Khan*/
	public Theme(Context ctxt, String compName, View view)
	{
		this(ctxt, compName);
		this.view = view;
		
	}
	
	public Theme(Context ctxt, String compName)
	{
		pm = ctxt.getPackageManager();
		ComponentName cn = ComponentName.unflattenFromString(compName);
		this.context = ctxt;
		try{
			Bundle inf=pm.getReceiverInfo(cn, PackageManager.GET_META_DATA).metaData;
			
			prefix=	inf.getString("prefix");
			isinapp=inf.getBoolean("isInApp");
			
		}catch(Exception exception)
		{
			
		}
		//if (compName.equals("com.linchpin.myperiodtracker/com.linchpin.periodtracker.theme.Green_NewTheme"))
		if(isinapp){
			//prefix = "green_";
			remoteRes = ctxt.getResources();
			
		}
		/*else if (compName.equals("com.linchpin.myperiodtracker/com.linchpin.periodtracker.theme.Blue_NewTheme"))
		{
			prefix = "blue_";
			remoteRes = ctxt.getResources();
			
		}*/
		else
		{
			
			if (cn != null)
			{
				try
				{
					pInfos = pm.getPackageInfo(cn.getPackageName(), 0);
					remoteRes = pm.getResourcesForApplication(cn.getPackageName());
				}
				catch (NameNotFoundException e)
				{
					pInfos = null;
					remoteRes = null;
				}
			}
		}
	}
	
	/**
	 * Method Returns Current Theme. Yoy have to modify this method according to your need to provide theme name to instanciate
	 * Theme class which takes Flatern Component Name of theme
	 * 
	 * @param context <b>Context</b>  Taks Current Context not Remote context. eg <b>this</b>
	 * @param view <b>View</b>
	 * @return {@link Theme} Instance of Theme Class
	 * @author Khalid Khan*/
	public static Theme getCurrentTheme(Context context, View view)
	{
		
		String themecompName = APP.GLOBAL().getPreferences().getString(APP.PREF.THEME_COMPONENT.key, "");
		
		if (!TextUtils.isEmpty(themecompName) && !themecompName.equals(context.getResources().getString(R.string.default_theme))) { return new Theme(context, themecompName, view); }
		return null;
	}
	
	public static Theme getCurrentTheme(Context context)
	{
		
		String themecompName = APP.GLOBAL().getPreferences().getString(APP.PREF.THEME_COMPONENT.key, "");
		
		if (!TextUtils.isEmpty(themecompName) && !themecompName.equals(APP.GLOBAL().getResources().getString(R.string.default_theme))) 
		{ 
			return new Theme(APP.GLOBAL(), themecompName); 
			}
		return null;
	}
	
	/**
	 * Method To find all the theme either these installed in the app on out of the Application. you can call tis method from 
	 * your preference activity.To make select it to apply.
	 * @param ctxt <a>Context</b>  Taks Current Context not Remote context. eg <b>this</b>
	 * @return {@link HashMap} Map of the themes
	 * @author Khalid Khan*/
	public static HashMap<String, String> getAvailableThemes(Context ctxt)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("", ctxt.getResources().getString(R.string.default_theme));
		
		PackageManager packageManager = ctxt.getPackageManager();
		Intent it = new Intent(GET_THEME);
		
		List<ResolveInfo> availables = packageManager.queryBroadcastReceivers(it, 0);
		Log.d(THIS_FILE, "We found " + availables.size() + "themes");
		for (ResolveInfo resInfo : availables)
		{
			Log.d(THIS_FILE, "We have -- " + resInfo);
			ActivityInfo actInfos = resInfo.activityInfo;
			
			//String ff=actInfos.metaData.getString("prefix");
			ComponentName cmp = new ComponentName(actInfos.packageName, actInfos.name);
			
			String label = (String) actInfos.loadLabel(packageManager);
			if (TextUtils.isEmpty(label))
			{
				label = (String) resInfo.loadLabel(packageManager);
			}
			result.put(cmp.flattenToString(), label);
		}
		
		return result;
	}
	
	/**
	 * @param name <b>String</b> Remote {@link Drawable} Name
	 * @return {@link Drawable}
	 * @author Khalid Khan
	 */
	public Drawable getDrawableResource(String name)
	{
		if (remoteRes != null && pInfos != null )
		{
			int id = remoteRes.getIdentifier(prefix +name, "drawable", pInfos.packageName);
			return pm.getDrawable(pInfos.packageName, id, pInfos.applicationInfo);
		}
		else
		{
			int id = remoteRes.getIdentifier(prefix + name, "drawable", context.getPackageName());
			if (id > 0) return remoteRes.getDrawable(id);
		}
		return null;
	}
	
	/**
	 * @param name <b>String</b> Remote Dimention Resource  Name
	 * @return {@link Integer} Dimention
	 * @author Khalid Khan
	 */
	public Integer getDimension(String name)
	{
		if (remoteRes != null && pInfos != null)
		{
			int id = remoteRes.getIdentifier(name, "dimen", pInfos.packageName);
			if (id > 0) { return remoteRes.getDimensionPixelSize(id); }
		}
		else
		{
			
			Log.d(THIS_FILE, "No results yet !! ");
		}
		return null;
	}
	
	public Integer getColor(String name)
	{
		
		if (remoteRes != null && pInfos != null)
		{
			int id = remoteRes.getIdentifier(prefix +name, "color", pInfos.packageName);
			if (id > 0) { return remoteRes.getColor(id); }
		}
		else
		{
			int id = remoteRes.getIdentifier(prefix + name, "color", context.getPackageName());
			if (id > 0) { return remoteRes.getColor(id); }
		}
		return null;
	}
	
	public void applyBackgroundDrawable(int id, String res)
	{
		applyBackgroundDrawable(view.findViewById(id), res);
	}
	
	public void applyBackgroundColor(int id, String res)
	{
		applyBackgroundColor(view.findViewById(id), res);
	}
	
	public void applyButtonCheckBox(int id, String res)
	{
		applyButtonCheckBox(view.findViewById(id), res);
	}
	
	private void applyButtonCheckBox(View v, String res)
	{
		Drawable d=getDrawableResource(res);
		if (d != null && v != null)
		{
			((CompoundButton) v).setButtonDrawable(d);
		}
	}

	public void applyBackgroundDrawable(View v, String res)
	{
		
		Drawable d = getDrawableResource(res);
		if (d != null && v != null)
		{
			UtilityWrapper.getInstance().setBackgroundDrawable(v, d);
		}
	}
	
	public void applyBackgroundColor(View v, String res)
	{
		
		Integer d = getColor(res);
		if (d != null && v != null)
		{
			v.setBackgroundColor(d);
		}
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, String respath, int reqWidth, int reqHeight)
	{
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(respath, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(respath, options);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
	{
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth)
		{
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth)
			{
				inSampleSize *= 2;
			}
		}
		
		return inSampleSize;
	}
	
	public void applyImageDrawable(int id, String res)
	{
		ImageView subV = (ImageView) view.findViewById(id);
		if (subV != null) applyImageDrawable(subV, res);
	}
	
	public void applyImageDrawable(ImageView subV, String res)
	{
		Drawable d = getDrawableResource(res);
		if (d != null)
		{
			subV.setImageDrawable(d);
		}
	}
	
	public void applyTextColor(int id, String name)
	{
		
		TextView button = (TextView) view.findViewById(id);
		if(button!=null)
		applyTextColor(button, name);
		
	}
	
	public void applyTextColor(TextView v, String name)
	{
		Integer color = getColor(name);
		
		if (color != null)
		{
			v.setTextColor(color);
		}
	}
	
	public void applyBackgroundStateListDrawable(View v, String prefix)
	{
		Drawable pressed = getDrawableResource(prefix + "_press");
		Drawable focused = getDrawableResource(prefix + "_focus");
		Drawable normal = getDrawableResource(prefix + "_normal");
		if (focused == null)
		{
			focused = pressed;
		}
		StateListDrawable std = null;
		if (pressed != null && focused != null && normal != null)
		{
			std = new StateListDrawable();
			std.addState(new int[]
			{
				android.R.attr.state_pressed
			}, pressed);
			std.addState(new int[]
			{
				android.R.attr.state_focused
			}, focused);
			std.addState(new int[] {}, normal);
		}
		
		if (std != null)
		{
			UtilityWrapper.getInstance().setBackgroundDrawable(v, std);
		}
	}
	
	public void applyBackgroundStateListSelectableDrawable(View v, String prefix)
	{
		Drawable pressed = getDrawableResource(prefix + "_press");
		Drawable focused = getDrawableResource(prefix + "_focus");
		Drawable selected = getDrawableResource(prefix + "_selected");
		Drawable unselected = getDrawableResource(prefix + "_unselected");
		if (focused == null)
		{
			focused = pressed;
		}
		StateListDrawable std = null;
		if (pressed != null && focused != null && selected != null && unselected != null)
		{
			std = new StateListDrawable();
			std.addState(new int[]
			{
				android.R.attr.state_pressed
			}, pressed);
			std.addState(new int[]
			{
				android.R.attr.state_focused
			}, focused);
			std.addState(new int[]
			{
				android.R.attr.state_selected
			}, selected);
			std.addState(new int[] {}, unselected);
		}
		
		if (std != null)
		{
			UtilityWrapper.getInstance().setBackgroundDrawable(v, std);
		}
	}
	
	public void applyLayoutMargin(View v, String prefix)
	{
		ViewGroup.MarginLayoutParams lp = null;
		try
		{
			lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
		}
		catch (ClassCastException e)
		{
			Log.e(THIS_FILE, "Trying to apply layout params to invalid layout " + v.getLayoutParams());
		}
		Integer marginTop = getDimension(prefix + "_top");
		Integer marginBottom = getDimension(prefix + "_bottom");
		Integer marginRight = getDimension(prefix + "_right");
		Integer marginLeft = getDimension(prefix + "_left");
		if (marginTop != null)
		{
			lp.topMargin = marginTop;
		}
		if (marginBottom != null)
		{
			lp.bottomMargin = marginBottom;
		}
		if (marginRight != null)
		{
			lp.rightMargin = marginRight;
		}
		if (marginLeft != null)
		{
			lp.leftMargin = marginLeft;
		}
		v.setLayoutParams(lp);
		
	}
	
	public void applyLayoutSize(View v, String prefix)
	{
		LayoutParams lp = v.getLayoutParams();
		Integer width = getDimension(prefix + "_width");
		Integer height = getDimension(prefix + "_height");
		if (width != null)
		{
			lp.width = width;
		}
		if (height != null)
		{
			lp.height = height;
		}
		v.setLayoutParams(lp);
	}
	
	private static boolean needRepeatableFix()
	{
		// In ICS and upper the problem is fixed, so no need to apply by code
		return (!Compatibility.isCompatible(14));
	}
	
	/**
	 * @param v
	 *            The view to fix background of.
	 * @see #fixRepeatableDrawable(Drawable)
	 */
	public static void fixRepeatableBackground(View v)
	{
		if (!needRepeatableFix()) { return; }
		fixRepeatableDrawable(v.getBackground());
	}
	
	/**
	 * Fix the repeatable background of a drawable. This support both bitmap and
	 * layer drawables
	 * 
	 * @param d
	 *            the drawable to fix.
	 */
	public static void fixRepeatableDrawable(Drawable d)
	{
		if (!needRepeatableFix()) { return; }
		if (d instanceof LayerDrawable)
		{
			LayerDrawable layer = (LayerDrawable) d;
			for (int i = 0; i < layer.getNumberOfLayers(); i++)
			{
				fixRepeatableDrawable(layer.getDrawable(i));
			}
		}
		else if (d instanceof BitmapDrawable)
		{
			fixRepeatableBitmapDrawable((BitmapDrawable) d);
		}
		
	}
	
	/**
	 * Fix the repeatable background of a bitmap drawable. This only support a
	 * BitmapDrawable
	 * 
	 * @param d
	 *            the BitmapDrawable to set repeatable.
	 */
	public static void fixRepeatableBitmapDrawable(BitmapDrawable d)
	{
		if (!needRepeatableFix()) { return; }
		// I don't want to mutate because it's better to share the drawable fix
		// for all that share this constant state
		// d.mutate();
		// Log.d(THIS_FILE, "Exisiting tile mode : " + d.getTileModeX() + ", "+
		// d.getTileModeY());
		d.setTileModeXY(d.getTileModeX(), d.getTileModeY());
		
	}
	
}
