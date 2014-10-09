package com.linchpin.periodtracker.utlity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.view.Gravity;
import android.widget.Toast;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodttracker.database.ImportDatabase;

public class RestoreAndBackUp extends AsyncTask<Void, String, String>
{
	
	public static ProgressDialog	progressDialog	= null;
	Context							context;
	JSONObject						jsonObject;
	String							what, response;
	boolean							problemInBackup	= false;
	
	public static RestoreAndBackUp	andBackUp;
	
	public RestoreAndBackUp(Context context, String what)
	{
		// TODO Auto-generated constructor stub
		this.context = context;
		this.what = what;
		andBackUp = this;
		
	}
	
	protected void onPreExecute()
	{
		
		progressDialog = new ProgressDialog(context);
		try
		{
			if (!what.contains("mail"))
			{
				progressDialog.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
				progressDialog.setMessage(context.getString(R.string.loading));
				progressDialog.show();
			}
		}
		catch (Exception e)
		{
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(String... values)
	{
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		
	}
	
	@Override
	protected String doInBackground(Void... params)
	{
		// TODO Auto-generated method stub
		
		String weburl = "http://lptpl.info/send_email/v1/send_email.php?request=";
		
		if (what.equals(context.getResources().getString(R.string.backup)))
		{
			ExportDataBase exportConstants = new ExportDataBase(context, PeriodTrackerObjectLocator.getInstance().getApplicationVersion());
			jsonObject = exportConstants.getFinalExportData();
			
		}
		else if (what.contains("mail"))
		{
			
			try
			{
				String webService[] = what.split("/");
				HttpClient httpclient = new DefaultHttpClient();
				
				HttpGet httpget = new HttpGet();
				
				URI uri = new URI(weburl + URLEncoder.encode(webService[1], "UTF-8"));
				httpget.setURI(uri);
				
				HttpResponse httpResponse = httpclient.execute(httpget);
				response = EntityUtils.toString(httpResponse.getEntity());
				
			}
			catch (Exception exception)
			{
				
				exception.printStackTrace();
			}
			
		}
		else
		{
			File file1 = new File(what);
			try
			{
				ImportDatabase importDatabase = new ImportDatabase(context, file1.getAbsolutePath());
				
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
				problemInBackup = true;
			}
			
		}
		
		return "";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result)
	{
		// TODO Auto-generated method stub
		if (null != progressDialog && progressDialog.isShowing())
		{
			progressDialog.cancel();
		}
		boolean boolean1;
		String root = Environment.getExternalStorageDirectory().getPath();
		if (null != progressDialog && progressDialog.isShowing())
		{
			progressDialog.cancel();
		}
		if (!problemInBackup && what.equals(context.getResources().getString(R.string.backup)))
		{
			
			try
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
				File file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/MYLPTPL/" + "BKP_MPT_" + dateFormat.format(new Date()) + ".mpt");
				
				if (!file1.exists())
				{
					file1.getParentFile().mkdirs();
					file1.createNewFile();
				}
				
				BufferedWriter buf = new BufferedWriter(new FileWriter(file1, true));
				buf.append(EncryptDecrypt.getInstance(ConstantsKey.sceretKey).newEncrypt(jsonObject.toString()));
				buf.close();
				Toast.makeText(context, context.getString(R.string.backupsucessfully), Toast.LENGTH_SHORT).show();
				if (!Utility.isNetworkConnected(context))
				{
					
					Toast.makeText(context, context.getString(R.string.nointernetconnected), Toast.LENGTH_SHORT).show();
					
				}
				
				InputStream is = context.getAssets().open("test.html");
				int size = is.available();
				
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				
				String str = new String(buffer);
				
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name) + "-" + context.getResources().getString(R.string.backup) + "-" + "Android -" + new SimpleDateFormat(PeriodTrackerObjectLocator.getInstance().getDateFormat()).format(new Date()));
				i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file1));
				
				i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(str));
				
				try
				{
					context.startActivity(Intent.createChooser(i, "Send mail..."));
					
				}
				catch (android.content.ActivityNotFoundException ex)
				{
					Toast.makeText(context, context.getString(R.string.noemailclient), Toast.LENGTH_SHORT).show();
				}
				
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if (problemInBackup)
		{
			Toast.makeText(context, context.getString(R.string.tryagain), Toast.LENGTH_LONG).show();
			
		}
		else if (!what.contains("mail") && !what.equals(context.getResources().getString(R.string.backup)))
		{
			
			if (null != ImportDatabase.passcode && !ImportDatabase.passcode.equals("")) ImportDatabase.askForPassword(ImportDatabase.passcode);
			else
			{
				ImportDatabase.insertAndParsingArray();
				
				PeriodTrackerObjectLocator.getInstance().setPeriodTrackerObjectLocatorNull();
				
				Toast.makeText(context, context.getString(R.string.importdatasucessfully), Toast.LENGTH_LONG).show();
				
				Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
				((Activity) context).finish();
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
				
			}
		}
		else if (what.contains("mail"))
		{
			
			try
			{
				if (null != response && !response.equals(""))
				{
					JSONObject jsonObject = new JSONObject(response);
					String Status = jsonObject.getString("status");
					if (Status.equals("error"))
					{
						
						Toast toast = Toast.makeText(context, context.getString(R.string.notabletosendmail), Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
						
						toast.show();
						
					}
					else
					{
						Toast toast = Toast.makeText(context, context.getString(R.string.passwordsentyourmail), Toast.LENGTH_LONG);
						
						toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
						toast.show();
					}
					
				}
				else
				{
					Toast toast = Toast.makeText(context, context.getString(R.string.notabletosendmail), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
					
				}
			}
			catch (Exception e)
			{
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}
}
