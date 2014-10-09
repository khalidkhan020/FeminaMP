/** 
 * @author Khalid Khan
 * 
 */
package com.linchpin.periodtracker.theme;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Environment;
import android.util.Log;

public final class Compatibility
{

	private Compatibility()
	{
	}

	private static final String	THIS_FILE	= "Compat";

	public static int getApiLevel()
	{
		return android.os.Build.VERSION.SDK_INT;
	}

	public static boolean isCompatible(int apiLevel)
	{
		return android.os.Build.VERSION.SDK_INT >= apiLevel;
	}

	public static String getCpuAbi()
	{
		if (isCompatible(4))
		{
			Field field;
			try
			{
				field = android.os.Build.class.getField("CPU_ABI");
				return field.get(null).toString();
			}
			catch (Exception e)
			{
				Log.w(THIS_FILE, "Announce to be android 1.6 but no CPU ABI field", e);
			}

		}
		return "armeabi";
	}

	public final static int getNumCores()
	{
		// Private Class to display only CPU devices in the directory listing
		class CpuFilter implements FileFilter
		{
			@Override
			public boolean accept(File pathname)
			{
				// Check if filename is "cpu", followed by a single digit number
				if (Pattern.matches("cpu[0-9]", pathname.getName())) { return true; }
				return false;
			}
		}
		try
		{
			// Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			// Return the number of cores (virtual CPU devices)
			return files.length;
		}
		catch (Exception e)
		{
			return Runtime.getRuntime().availableProcessors();
		}
	}

	public static boolean useFlipAnimation()
	{
		if (android.os.Build.BRAND.equalsIgnoreCase("archos") && android.os.Build.DEVICE.equalsIgnoreCase("g7a")) { return false; }
		return true;
	}

	public static boolean isTabletScreen(Context ctxt)
	{
		boolean isTablet = false;
		if (!isCompatible(4)) { return false; }
		Configuration cfg = ctxt.getResources().getConfiguration();
		int screenLayoutVal = 0;
		try
		{
			Field f = Configuration.class.getDeclaredField("screenLayout");
			screenLayoutVal = (Integer) f.get(cfg);
		}
		catch (Exception e)
		{
			return false;
		}
		int screenLayout = (screenLayoutVal & 0xF);
		// 0xF = SCREENLAYOUT_SIZE_MASK but avoid 1.5 incompat doing that
		if (screenLayout == 0x3 || screenLayout == 0x4)
		{
			// 0x3 = SCREENLAYOUT_SIZE_LARGE but avoid 1.5 incompat doing that
			// 0x4 = SCREENLAYOUT_SIZE_XLARGE but avoid 1.5 incompat doing that
			isTablet = true;
		}

		return isTablet;
	}

	public static boolean isInstalledOnSdCard(Context context)
	{
		// check for API level 8 and higher
		if (Compatibility.isCompatible(8))
		{
			PackageManager pm = context.getPackageManager();
			try
			{
				PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
				ApplicationInfo ai = pi.applicationInfo;
				return (ai.flags & 0x00040000 /*
											 * ApplicationInfo.
											 * FLAG_EXTERNAL_STORAGE
											 */) == 0x00040000 /*
																 * ApplicationInfo.
																 * FLAG_EXTERNAL_STORAGE
																 */;
			}
			catch (NameNotFoundException e)
			{
				// ignore
			}
		}

		// check for API level 7 - check files dir
		try
		{
			String filesDir = context.getFilesDir().getAbsolutePath();
			if (filesDir.startsWith("/data/"))
			{
				return false;
			}
			else if (filesDir.contains(Environment.getExternalStorageDirectory().getPath())) { return true; }
		}
		catch (Throwable e)
		{
			// ignore
		}

		return false;
	}

}
