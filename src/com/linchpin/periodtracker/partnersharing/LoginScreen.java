package com.linchpin.periodtracker.partnersharing;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gcm.GCMRegistrar;
import com.linchpin.drawable.DrawableUtil;
import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.gcm.Util;
import com.linchpin.periodtracker.interfaces.MptClickableSpan;
import com.linchpin.periodtracker.theme.Theme;
import com.linchpin.periodtracker.utlity.APP;
import com.linchpin.periodtracker.utlity.APP.PartnerHome;
import com.linchpin.periodtracker.utlity.APP.WhoIAm;
import com.linchpin.periodtracker.utlity.ExportDataBase;
import com.linchpin.periodtracker.utlity.PeriodTrackerObjectLocator;
import com.linchpin.periodtracker.view.HomeScreenView;
import com.linchpin.periodtracker.view.SelectGoal;
import com.linchpin.periodtracker.widget.MPTProgressDialog;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.push.PushNotification;
import com.shephertz.app42.paas.sdk.android.push.PushNotificationResponseBuilder;
import com.shephertz.app42.paas.sdk.android.storage.StorageResponseBuilder;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.UserResponseBuilder;

public class LoginScreen extends Activity implements OnClickListener, TextWatcher
{
	private void applyTheme()
	{
		
		Theme t = Theme.getCurrentTheme(this, findViewById(android.R.id.content));
		if (t != null)
		{			
			t.applyBackgroundDrawable(R.id.content, "mpt_background");
			t.applyTextColor(R.id.fbmessages, "text_color");
			t.applyTextColor(R.id.info, "text_color");
			t.applyBackgroundDrawable(R.id.view4, "mpt_button_sltr");
			t.applyBackgroundDrawable(R.id.view2, "mpt_button_sltr");
			t.applyBackgroundDrawable(R.id.view3, "shp_circle");
			t.applyTextColor(R.id.user_id, "text_color");
			t.applyTextColor(R.id.password, "text_color");
			t.applyTextColor(R.id.login, "co_btn_fg");
			t.applyBackgroundDrawable(R.id.login, "mpt_button_sltr");
			t.applyImageDrawable(R.id.imageView1,"ic_user_id");
			t.applyImageDrawable(R.id.imageView2,"ic_password");
		}
	}
	@Override
	protected void onStart()
	{
		EasyTracker.getInstance(this).activityStart(this);
		super.onStart();
	}
	
	protected void onStop()
	{
		EasyTracker.getInstance(this).activityStop(this);
		
		super.onStop();
	}
	
	private EditText			userId, password;
	private Button				login;
	int							status					= 0;
	private final int			ENABLE_LOGIN_BUTTON		= 0x1;
	private final int			GO_TO_PARTNER_HOME		= 0x2;
	private final int			AUTHENTICATION_FAILED	= 0x3;
	private WebHandler			handler					= new WebHandler();
	private Messenger			msg						= new Messenger(new WebHandler());
	private Message				message					= new Message();
	private Button			register, forgotPas;
	private boolean				isResumed				= false;
	LoginButton					facebook;
	private UiLifecycleHelper	uiHelper;
	MPTProgressDialog			myDialog;
	
	class WebHandler extends Handler
	{
		
		@Override
		public void handleMessage(Message msg)
		{
			
			switch (msg.what)
			{
				case ENABLE_LOGIN_BUTTON:
					login.setEnabled(msg.what == 1);
					break;
				case GO_TO_PARTNER_HOME:
					if (myDialog.isShowing()) myDialog.dismiss();
					login.setEnabled(msg.what != 1);
					LoginScreen.this.finish();
					APP.GLOBAL().getEditor().putBoolean(APP.PREF.IS_FIRST_TIME.key, true).commit();
					APP.GLOBAL().getEditor().putString(APP.GOAL.MY_PARTNERSTATUS.key, APP.PARTNER_HOME.toString()).commit();
					APP.GLOBAL().getEditor().putString(APP.GOAL.SAVE_WHO_AM_I.key, APP.WHOIAM.toString()).commit();
					Intent intent = new Intent(LoginScreen.this, PartnerHomeActivity.class);
					startActivity(intent);
					//	Toast.makeText(LoginScren.this, "Success", Toast.LENGTH_LONG).show();
					break;
				case AUTHENTICATION_FAILED:
					if (myDialog.isShowing()) myDialog.dismiss();
					login.setEnabled(msg.what != 1);
					Toast.makeText(LoginScreen.this, getString(R.string.auth_fail), Toast.LENGTH_LONG).show();
					break;
				
				default:
					break;
			}
			super.handleMessage(msg);
		}
	}
	
	private void makeMeRequest(final Session session)
	{
		
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback()
		{
			@SuppressWarnings("static-access")
			@Override
			public void onCompleted(GraphUser user, Response response)
			{
				
				if (session == Session.getActiveSession())
				{
					if (user != null)
					{
						
						if (!"".equals(userId.getText().toString().trim()) && !"".equals(password.getText().toString().trim()))
						{
							myDialog.show();
							JSONObject jsonBody = new JSONObject();
							try
							{
								
								jsonBody.put("userid", user.getId());
								jsonBody.put("userid", user.asMap().get("email"));
								jsonBody.put("gender", user.asMap().get("gender").toString().equals("male") ? "M" : "F");
								jsonBody.put("fbtoken", session.getAccessToken());
								jsonBody.put("fname", user.getFirstName());
								jsonBody.put("lname", user.getLastName());
								jsonBody.put("name", user.getName());
							}
							catch (JSONException e)
							{
								
							}
							((APP) getApplication()).getCustomCodeService().runJavaCode("FBLogin", jsonBody, new App42CallBack()
							/*{
								((APP) getApplication()).getCustomCodeService().runJavaCode("FBLogin", jsonBody, new App42CallBack()
							 */
							{
								
								@Override
								public void onException(Exception ex)
								{
									try
									{
										if (ex instanceof App42Exception && ((App42Exception) ex).getAppErrorCode() == 1903)
										{
											message.what = AUTHENTICATION_FAILED;
											msg.send(message);
											
										}
										if (ex instanceof App42Exception && ((App42Exception) ex).getAppErrorCode() == 1401)
										{
											message.what = AUTHENTICATION_FAILED;
											msg.send(message);
											
										}
									}
									catch (RemoteException e)
									{
										e.printStackTrace();
									}
									
								}
								
								@SuppressWarnings("static-access")
								@Override
								public void onSuccess(Object usr)
								{
									try
									{
										boolean t1, t2, t3, t4;
										APP.currentUser = (new UserResponseBuilder()).buildResponse(((JSONObject) usr).getString("data"));
										APP.GLOBAL().getEditor().putString(APP.PREF.CURRENT_SESSION.key, ((JSONObject) usr).getString("sessionid")).commit();
										APP.GLOBAL().getEditor().putString(APP.GOAL.SAVE_CURRENTUSER_INFO.key, ((JSONObject) usr).getString("data")).commit();
										JSONObject request = new JSONObject();
										JSONObject response = new JSONObject();
										request.put("userid", APP.currentUser.getUserName());
										request.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
										request.put("devicetype", "Android");
										Util.registerWithApp42("810990387766");
										App42API.setLoggedInUser(APP.currentUser.getUserName());
										String deviceRegId = GCMRegistrar.getRegistrationId(App42API.appContext);
										request.put("devicetoken", deviceRegId);
										APP.getCustomCodeService().runJavaCode("RegisterDevice", request, new App42CallBack()
										{
											@Override
											public void onSuccess(Object arg0)
											{
												
												PushNotificationResponseBuilder builder2 = new PushNotificationResponseBuilder();
												try
												{
													PushNotification notification = builder2.buildResponse(((JSONObject) arg0).getString("data"));
												}
												catch (JSONException e)
												{
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												catch (Exception e)
												{
													
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												
											}
											
											@Override
											public void onException(Exception arg0)
											{
												System.out.println("");
											}
										});
										
										//request.remove("devicetype");
										//request.remove("devicetoken");
										APP.PARTNER_HOME = PartnerHome.NO_PARTNER;
										try
										{
											response = (JSONObject) ((APP) getApplication()).getCustomCodeService().runJavaCode("CheckStatus", request);
											response = new JSONObject((new StorageResponseBuilder()).buildResponse(response.getString("data")).getJsonDocList().get(0).jsonDoc);
											
											t1 = response.getString("senderid").equals(APP.currentUser.getUserName());
											t2 = response.getString("receverid").equals(APP.currentUser.getUserName());
											t3 = response.getString("userid").equals(APP.currentUser.getUserName());
											t4 = response.getString("status").equals("0") ? false : true;
											if (!t1 && t2 && !t3 && !t4) APP.PARTNER_HOME = PartnerHome.RECEIVED_INVITATION;
											else if (t1 && !t2 && !t3 && !t4) APP.PARTNER_HOME = PartnerHome.RECEVED_REUEST;
											else if (!t1 && t2 && t3 && !t4) APP.PARTNER_HOME = PartnerHome.SEND_REQUEST;
											else if (t1 && !t2 && t3 && !t4) APP.PARTNER_HOME = PartnerHome.SEND_INVITATION;
											else if (!t1 && t2 && t4) APP.PARTNER_HOME = PartnerHome.RECEVER;
											else if (t1 && !t2 && t4) APP.PARTNER_HOME = PartnerHome.SENDER;
											
											if (t1)
											{
												APP.WHOIAM = WhoIAm.SENDER;
												APP.setParnerId(response.getString("receverid"));
											}
											else
											{
												APP.WHOIAM = WhoIAm.REVEVER;
												APP.setParnerId(response.getString("senderid"));
											}
											if (APP.PARTNER_HOME == APP.PartnerHome.RECEVER) PartnerUtil.getPartnerData();
											if (APP.PARTNER_HOME != APP.PartnerHome.NO_PARTNER)
											{
												JSONObject partner = (JSONObject) ((APP) getApplication()).getCustomCodeService().runJavaCode("GetPartnerProfile", request);
												APP.myPartnerProfile = (new UserResponseBuilder()).buildResponse(((JSONObject) partner).getString("data"));
												APP.GLOBAL().getEditor().putString(APP.GOAL.SAVE_PARTNERUSER_INFORMATION.key,((JSONObject) partner).getString("data")).commit();
											}
											if (APP.PARTNER_HOME == APP.PartnerHome.SENDER)
											{
												JSONObject jsonObject = new JSONObject();
												ExportDataBase exportConstants = new ExportDataBase(LoginScreen.this, PeriodTrackerObjectLocator.getInstance().getApplicationVersion());
												jsonObject = exportConstants.getFinalExportData();
												
												JSONObject jsonBody0 = new JSONObject();
												try
												{
													jsonBody0.put("userid", APP.currentUser.userName);
													jsonBody0.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
													jsonBody0.put("appdata", jsonObject);
												}
												catch (JSONException e)
												{
													
												}
												APP.getCustomCodeService().runJavaCode("UploadData", jsonBody0, new App42CallBack()
												{
													
													@Override
													public void onException(Exception arg0)
													{
														System.out.println("");
														
													}
													
													@Override
													public void onSuccess(Object arg0)
													{
														System.out.println("");
														
													}
												});
												
											}
											
										}
										catch (App42Exception ex)
										{
										}
										
										message.what = GO_TO_PARTNER_HOME;
										msg.send(message);
										
									}
									catch (App42Exception e)
									{
										e.printStackTrace();
									}
									catch (JSONException e2)
									{
										e2.printStackTrace();
									}
									catch (Exception e2)
									{
										e2.printStackTrace();
									}
									
								}
							});
							
						}
						
					}
				}
				if (response.getError() != null)
				{
					//	handleError(response.getError());
				}
			}
		});
		request.executeAsync();
		
	}
	
	private Session.StatusCallback	callback	= new Session.StatusCallback()
												{
													@Override
													public void call(Session session, SessionState state, Exception exception)
													{
														
														Session session2 = Session.getActiveSession();
														login.setEnabled(false);
														if (session != null && session.isOpened())
														{
															makeMeRequest(session);
														}
														else login.setEnabled(true);
													}
												};
	
	@Override
	public void onResume()
	{
		super.onResume();
		if (!APP.GLOBAL().getPreferences().getBoolean(APP.PREF.PURCHASED.key, false)) PeriodTrackerObjectLocator.getInstance().setSinglaotonAdview(this);
		uiHelper.onResume();
		isResumed = true;
		// Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
		// the onResume methods of the primary Activities that an app may be launched into.
		AppEventsLogger.activateApp(this);
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}
	
	@Override
	public void onBackPressed()
	{
		if (APP.GLOBAL().getPreferences().getBoolean(APP.GENDER.MALE.key, false))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(LoginScreen.this);
			builder.setTitle(getResources().getString(R.string.notifiction));
			builder.setMessage(getResources().getString(R.string.exitmessage));
			builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					android.os.Process.killProcess(android.os.Process.myPid());
					
				}
			});
			
			builder.setNegativeButton(getResources().getString(R.string.no), null);
			builder.show();
		}
		
		else if (APP.GLOBAL().getPreferences().getBoolean(APP.GENDER.FEMALE.key, false))
		{
			Intent homeActivity = new Intent(LoginScreen.this, HomeScreenView.class);
			startActivity(homeActivity);
			finish();
			
			APP.GLOBAL().exicuteLIOAnim(LoginScreen.this);
		}
		
		else if (APP.GLOBAL().getPreferences().getBoolean(APP.GOAL.LOG_TO_START.key, false))
		{
			Intent homeActivity = new Intent(LoginScreen.this, SelectGoal.class);
			startActivity(homeActivity);
			finish();
			APP.GLOBAL().exicuteLIOAnim(LoginScreen.this);
		}
		else
		{
			Intent homeActivity = new Intent(LoginScreen.this, HomeScreenView.class);
			startActivity(homeActivity);
			finish();
			APP.GLOBAL().exicuteLIOAnim(LoginScreen.this);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
		
	}
	protected void onCreate(Bundle savedInstanceState)
	{
		/*if(APP.GLOBAL().getPreferences().getBoolean(APP.GOAL.LOG_TO_START.key, false))
		{
			setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		}*/
		/*else
		{
			setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		}*/
		if (android.os.Build.VERSION.SDK_INT >= 9)
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}	
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.partnerlogin);
		userId = (EditText) findViewById(R.id.user_id);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		login.setEnabled(false);
		facebook = (LoginButton) findViewById(R.id.facebook);
		facebook.setReadPermissions(Arrays.asList("email"));
		userId.addTextChangedListener(this);
		password.addTextChangedListener(this);
		userId.setText("");//ok
		password.setText("");
		register = (Button) findViewById(R.id.register);
		forgotPas = (Button) findViewById(R.id.forget_pass);
		String str_links = "<a href='1'>" + getString(R.string.create_acc) + "</a>";
		setTextViewHTML(register, str_links);
		String str_links0 = "<a href='2'>" + getString(R.string.forgot_pass) + "</a>";
		setTextViewHTML(forgotPas, str_links0);
		applyTheme();
		DrawableUtil.roundedCorner = new float[]
		{
				5, 5, 5, 5, 5, 5, 5, 5
		};
		//findViewById(R.id.content).setBackgroundDrawable(DrawableUtil.getShapeDrawable(0xFFECD0EC));
		
		myDialog = new MPTProgressDialog(this, R.drawable.spinner);
		
		myDialog.setCancelable(false);
	}
	
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
				if (APP.GLOBAL().isNetworkAvailable())
				{
					if (getUri().equals("1"))
					{
						Intent intent = new Intent(LoginScreen.this, Register.class);
						startActivity(intent);
					}
					else if (getUri().equals("2"))
					{
						Intent intent = new Intent(LoginScreen.this, DialogActivity.class);
						intent.putExtra("request_type", 2);
						startActivity(intent);
					}
				}
				else
				{
					Toast.makeText(LoginScreen.this, getString(R.string.no_data_connection), 1000).show();
				}
				
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
	
	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.login)
		{
			if (APP.GLOBAL().isNetworkAvailable())
			{
				login.setEnabled(false);
				myDialog.show();
				if (!"".equals(userId.getText().toString().trim()) && !"".equals(password.getText().toString().trim()))
				{
					JSONObject jsonBody = new JSONObject();
					try
					{
						jsonBody.put("userid", userId.getText().toString());
						jsonBody.put("passwd", password.getText().toString());
						
					}
					catch (JSONException e)
					{
						
					}
					
					((APP) getApplication()).getCustomCodeService().runJavaCode("Login", jsonBody, new App42CallBack()
					{
						
						@Override
						public void onException(Exception ex)
						{
							try
							{
								if (ex instanceof App42Exception && ((App42Exception) ex).getAppErrorCode() == 1903)
								{
									message = new Message();
									message.what = AUTHENTICATION_FAILED;
									msg.send(message);
									
								}
								if (ex instanceof App42Exception && ((App42Exception) ex).getAppErrorCode() == 1401)
								{
									message = new Message();
									message.what = AUTHENTICATION_FAILED;
									msg.send(message);
									
								}
								
							}
							catch (RemoteException e)
							{
								e.printStackTrace();
							}
							
						}
						
						@Override
						public void onSuccess(Object usr)
						{
							try
							{
								Thread.sleep(500);
								boolean t1, t2, t3, t4;
								APP.currentUser = (new UserResponseBuilder()).buildResponse(((JSONObject) usr).getString("data"));
								APP.GLOBAL().getEditor().putString(APP.GOAL.SAVE_CURRENTUSER_INFO.key, ((JSONObject) usr).getString("data")).commit();									
								APP.GLOBAL().getEditor().putString(APP.PREF.CURRENT_SESSION.key, ((JSONObject) usr).getString("sessionid")).commit();
								
								JSONObject request = new JSONObject();
								JSONObject response = new JSONObject();
								request.put("userid", APP.currentUser.getUserName());
								request.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
								request.put("devicetype", "Android");
								Util.registerWithApp42("810990387766");
								App42API.setLoggedInUser(APP.currentUser.getUserName());
								String deviceRegId = GCMRegistrar.getRegistrationId(App42API.appContext);
								request.put("devicetoken", deviceRegId);
								APP.getCustomCodeService().runJavaCode("RegisterDevice", request, new App42CallBack()
								{
									@Override
									public void onSuccess(Object arg0)
									{
										
										PushNotificationResponseBuilder builder2 = new PushNotificationResponseBuilder();
										try
										{
											PushNotification notification = builder2.buildResponse(((JSONObject) arg0).getString("data"));
										}
										catch (JSONException e)
										{
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										catch (Exception e)
										{
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
									}
									
									@Override
									public void onException(Exception arg0)
									{
										System.out.println("");
									}
								});
								
								APP.PARTNER_HOME = PartnerHome.NO_PARTNER;
								try
								{
									Thread.sleep(500);
									response = (JSONObject) ((APP) getApplication()).getCustomCodeService().runJavaCode("CheckStatus", request);
									response = new JSONObject((new StorageResponseBuilder()).buildResponse(response.getString("data")).getJsonDocList().get(0).jsonDoc);
									t1 = response.getString("senderid").equals(APP.currentUser.getUserName());
									t2 = response.getString("receverid").equals(APP.currentUser.getUserName());
									t3 = response.getString("userid").equals(APP.currentUser.getUserName());
									t4 = response.getString("status").equals("0") ? false : true;
									if (!t1 && t2 && !t3 && !t4) APP.PARTNER_HOME = PartnerHome.RECEIVED_INVITATION;
									else if (t1 && !t2 && !t3 && !t4) APP.PARTNER_HOME = PartnerHome.RECEVED_REUEST;
									else if (!t1 && t2 && t3 && !t4) APP.PARTNER_HOME = PartnerHome.SEND_REQUEST;
									else if (t1 && !t2 && t3 && !t4) APP.PARTNER_HOME = PartnerHome.SEND_INVITATION;
									else if (!t1 && t2 && t4) APP.PARTNER_HOME = PartnerHome.RECEVER;
									else if (t1 && !t2 && t4) APP.PARTNER_HOME = PartnerHome.SENDER;
									if (t1)
									{
										APP.WHOIAM = WhoIAm.SENDER;
										APP.setParnerId(response.getString("receverid"));
									}
									else
									{
										APP.WHOIAM = WhoIAm.REVEVER;
										APP.setParnerId(response.getString("senderid"));
									}
									if (APP.PARTNER_HOME == APP.PartnerHome.RECEVER) PartnerUtil.getPartnerData();
									if (APP.PARTNER_HOME != APP.PartnerHome.NO_PARTNER)
									{
										Thread.sleep(500);
										JSONObject partner = (JSONObject) ((APP) getApplication()).getCustomCodeService().runJavaCode("GetPartnerProfile", request);
										APP.myPartnerProfile = (new UserResponseBuilder()).buildResponse(((JSONObject) partner).getString("data"));
										APP.GLOBAL().getEditor().putString(APP.GOAL.SAVE_PARTNERUSER_INFORMATION.key,((JSONObject) partner).getString("data")).commit();
										
									}
									if (APP.PARTNER_HOME == APP.PartnerHome.SENDER)
									{
										JSONObject jsonObject = new JSONObject();
										ExportDataBase exportConstants = new ExportDataBase(LoginScreen.this, PeriodTrackerObjectLocator.getInstance().getApplicationVersion());
										jsonObject = exportConstants.getFinalExportData();
										JSONObject jsonBody0 = new JSONObject();
										try
										{
											jsonBody0.put("userid", APP.currentUser.userName);
											jsonBody0.put("sessionid", APP.GLOBAL().getPreferences().getString(APP.PREF.CURRENT_SESSION.key, null));
											jsonBody0.put("appdata", jsonObject);
										}
										catch (JSONException e)
										{
											System.out.println("");
										}
										Thread.sleep(500);
										APP.getCustomCodeService().runJavaCode("UploadData", jsonBody0, new App42CallBack()
										{
											
											@Override
											public void onException(Exception arg0)
											{
												System.out.println("");		
											}
											@Override
											public void onSuccess(Object arg0)
											{
												System.out.println("");
												
											}
										});
										
									}
									
								}
								catch (App42Exception ex)
								{
									System.out.print("Find Error" + ex.getMessage());
								}
								
								message = new Message();
								message.what = GO_TO_PARTNER_HOME;
								msg.send(message);
								
							}
							catch (App42Exception e)
							{
								e.printStackTrace();
							}
							catch (JSONException e2)
							{
								e2.printStackTrace();
							}
							catch (Exception e2)
							{
								e2.printStackTrace();
							}
							
						}
					});
					
				}
			}
			else
			{
				Toast.makeText(LoginScreen.this, getString(R.string.no_data_connection), 1000).show();
			}
		}
		else
		{
		}
		
	}
	
	@Override
	public void afterTextChanged(Editable s)
	{
		boolean b = APP.isValidEmail(userId.getText().toString());
		boolean b2 = !"".equals(password.getText().toString().trim());
		login.setEnabled(b && b2);
		//		if(!b)
		//		{
		//			Toast.makeText(LoginScreen.this, "Invalid User ID.", Toast.LENGTH_LONG).show();
		//		}
		//		if(!b2)
		//			Toast.makeText(LoginScreen.this, "Password should not be empty.", Toast.LENGTH_LONG).show();
		
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
