package com.momsdhaba;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.momsdhaba.user.CustomerActivity;
import com.momsdhaba.user.User_SelectArea;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final static String TAG = MainActivity.class.getSimpleName();
	private static final String LOGIN_URL = "http://momsdhaba.com/mobileapp/login/";
	private EditText mobilelEditText;
	private EditText passEditText;
	ConnectionDetector cd;
	boolean isInternetPresent = false;
	boolean status = false;
	private ProgressDialog pDialog;
	String mobilenumber, password, id, address, phone, uname, logout;
	JSONParser jsonParser = new JSONParser();
	JSONObject json;
	String name, result, usertype, Phone, Password, Usertype;
	public static final String MyPREFERENCES = "Prefs";
	public static final String MyPREFERENCES1 = "Prefs1";
	SharedPreferences sharedpref, sharedpref1;
	String message, title;
	AlertDialog alertDialog;
	LocationManager lm;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	public static HashMap<String, String> Logindetails = new HashMap<String, String>();
	Editor editor1, editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// sharedpref = getSharedPreferences(MyPREF, Context.MODE_PRIVATE);
		// netactivecheck();
		sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		sharedpref1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();

		mobilelEditText = (EditText) findViewById(R.id.editText_mnumber);
		passEditText = (EditText) findViewById(R.id.editText_password);

		new Thread() {

			public void run() {
				Message msg = Message.obtain();

				try {
					HttpURLConnection urlc = (HttpURLConnection) (new URL(
							"http://www.google.com").openConnection());
					urlc.setRequestProperty("User-Agent", "Test");
					urlc.setRequestProperty("Connection", "close");
					urlc.setConnectTimeout(1500);
					urlc.connect();
					status = urlc.getResponseCode() == 200;
					Log.i(TAG, "*******Connection internet*****" + status + "");
					System.out.println("Sysetm out put " + status);
					Bundle b = new Bundle();
					b.putBoolean("status", status);
					msg.setData(b);
				} catch (IOException e) {

				}
				messageHandler.sendMessage(msg);
			}

		}.start();
		Intent intent = getIntent();
		logout = intent.getStringExtra("Logout");
		Log.i(TAG, "Logout " + sharedpref1 + "Logout");
		if (logout != null) {
			editor1 = sharedpref1.edit();
			editor1.clear();
			editor1.commit();
			editor = sharedpref.edit();
			editor.clear();
			editor.commit();
		}
		Phone = (sharedpref1.getString("NUMBER", ""));
		Password = (sharedpref1.getString("PASSWORD", ""));
		Usertype = (sharedpref.getString("USER", ""));

		try {
			Log.i(TAG, "Phone sharedpref1 " + Phone + "");
			Log.i(TAG, "Password sharedpref1 " + Password + "");
			Log.i(TAG, "Password sharedpref Usertype " + Usertype + "");
			if (isInternetPresent) {

				if (!Phone.isEmpty() && !Password.isEmpty()) {
					mobilenumber = Phone;
					password = Password;
					if (Usertype.equals("chef")) {
						Intent i = new Intent(MainActivity.this, AddMenu.class);
						finish();
						startActivity(i);
					} else if (Usertype.equals("customer")) {
						Intent i = new Intent(MainActivity.this,
								CustomerActivity.class);
						finish();
						startActivity(i);
					}
					// new AttemptLogin().execute();
				} else if (Phone.isEmpty() && Password.isEmpty()) {
					Log.i(TAG, "Phone else ");
				}

			} else {
				message = "Seems like you are not connected to internet" + "\n"
						+ "please try again later!";
				alertdialog();
			}

			// Toast("PASSWORD" + Phone);
			Log.i(TAG, "Phone ????? " + Phone + "");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Toast("PASSWORD" + Phone);

		try {

			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!gps_enabled && !network_enabled) {
				// Ask the user to enable GPS

				Log.d(TAG, "!gps_enabled && !network_enabled");
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("Location Manager");
				builder.setMessage("Please Enable Your GPS?");
				builder.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent i = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(i);
							}
						});
				builder.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// No location service, no Activity
								dialog.cancel();
							}
						});
				builder.create().show();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mobilenumber = mobilelEditText.getText().toString().trim();
				password = passEditText.getText().toString().trim();

				// isInternetPresent = cd.isConnectingToInternet();
				// if (!isValidEmail(mobilenumber)) {
				// //emailEditText.setError("Invalid Email");
				// } else if (!isValidPassword(password)) {
				Log.i(TAG, "isInternetPresent = " + isInternetPresent + "");
				if (isInternetPresent) {

					Log.i(TAG, "isInternetPresent = " + isInternetPresent + "");
					onRestart();
					Log.e("mobilenumber", ""+mobilenumber);
					Log.e("password", ""+password);
					if (!mobilenumber.isEmpty()) {
						if (!password.isEmpty()) {
							if (status) {
								editor1 = sharedpref1.edit();
								editor1.putString("NUMBER", mobilenumber);
								editor1.putString("PASSWORD", password);
								editor1.commit();
								Log.i(TAG, "status = " + status + "");
								new AttemptLogin().execute();
							} else {
								message = "Check your Internet Connection";
								alertdialog();
							}
						} else {
							message = "Please enter password";
							alertdialog();

							// message = "Please enter password";

						}
					} else {

						message = "Please enter mobile number";
						alertdialog();
						// message = "Please enter mobile number";

					}
				} else {

					message = "Seems like you are not connected to internet"
							+ "\n" + "please try again later!";
					alertdialog();
					// message = "Check Network Connection";

				}
			}

		});

		findViewById(R.id.user_registarion).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.i(TAG, "isInternetPresent = " + isInternetPresent
								+ "");
						if (isInternetPresent) {

							Log.i(TAG, "isInternetPresent = "
									+ isInternetPresent + "");
							if (status) {
								Log.i(TAG, "status = " + status + "");
								Intent itab = new Intent(MainActivity.this,
										CustomerRegistration.class);
								// Intent itab = new
								// Intent(MainActivity.this,ReferActivity.class);
								itab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(itab);
							} else {
								message = "Check your Internet Connection";
								alertdialog();
							}

						} else {
							message = "Seems like you are not connected to internet"
									+ "\n" + "please try again later!";
							alertdialog();
						}
					}

				});

		findViewById(R.id.chef_registarion).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (isInternetPresent) {
							if (status) {
								Log.i(TAG, "status = " + status + "");
								Log.i(TAG, "isInternetPresent = "
										+ isInternetPresent + "");

								Intent itab = new Intent(MainActivity.this,
										ChefRegistraion.class);
								itab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(itab);
								Log.i(TAG, "Stert AttemptLogin()");
							} else {
								message = "Check your Internet Connection";
								alertdialog();
							}

						} else {
							message = "Seems like you are not connected to internet"
									+ "\n" + "please try again later!";
							alertdialog();
						}
					}
				});

		findViewById(R.id.forgot_password).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (isInternetPresent) {
							if (status) {
								Log.i(TAG, "status = " + status + "");
								Log.i(TAG, "isInternetPresent = "
										+ isInternetPresent + "");

								Intent itab = new Intent(MainActivity.this,
										Forgotpassword.class);
								itab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(itab);
								Log.i(TAG, "Stert AttemptLogin()");
							} else {
								message = "Check your Internet Connection";
								alertdialog();
							}

						} else {
							message = "Seems like you are not connected to internet"
									+ "\n" + "please try again later!";
							alertdialog();
						}
					}
				});

	}

	/*
	 * public void netactivecheck() { new Thread() {
	 * 
	 * public void run() { Message msg = Message.obtain();
	 * 
	 * try { HttpURLConnection urlc = (HttpURLConnection) (new URL(
	 * "http://www.google.com").openConnection());
	 * urlc.setRequestProperty("User-Agent", "Test");
	 * urlc.setRequestProperty("Connection", "close");
	 * urlc.setConnectTimeout(1500); urlc.connect(); status =
	 * urlc.getResponseCode() == 200; Log.i(TAG,
	 * "*******Connection internet*****" + status + "");
	 * System.out.println("Sysetm out put " + status); Bundle b = new Bundle();
	 * b.putBoolean("status", status); msg.setData(b); } catch (IOException e) {
	 * 
	 * } messageHandler.sendMessage(msg); }
	 * 
	 * }.start();
	 * 
	 * }
	 */

	public final Handler messageHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			status = msg.getData().getBoolean("status");

		}
	};

	// validating email id
	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	// validating password with retype password
	private boolean isValidPassword(String pass) {
		if (pass != null && pass.length() > 6) {
			return true;
		}
		return false;
	}

	class AttemptLogin extends AsyncTask<String, String, String> {
		String uEmail, cEmail;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Attempting to login. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			pDialog.setCanceledOnTouchOutside(false);

		}

		@SuppressLint("NewApi")
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			// Building Parameters
			try {

				List<NameValuePair> paramter = new ArrayList<NameValuePair>();
				paramter.add(new BasicNameValuePair("mobile_number",
						mobilenumber));
				paramter.add(new BasicNameValuePair("password", password));
				Log.d("mobile_number!", mobilenumber);
				Log.d("password", password);

				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
						paramter);

				Log.d("Login attempt", json.toString());

				result = json.getString("status");
				Log.d("result", "" + result);

				if (result.equals("failed")) {
					runOnUiThread(new Runnable() {
						public void run() {
							// Toast("Please enter  vaild username and password");
							alertdialog();
							message = "Please enter  vaild username and password";
						}
					});
				}

				try {
					id = json.getString("id");
					name = json.getString("name");
					usertype = json.getString("usertype");

					editor = sharedpref.edit();
					editor.putString("ID", id);
					editor.putString("NAME", name);
					editor.putString("USER", usertype);
					editor.commit();
					if (result.equals("login_success")
							&& usertype.equals("chef")) {
						Log.d("result", "Login Successful!....");
						// Toast("Login Successful!....");

						Log.d("ID", "" + id);
						Intent i = new Intent(MainActivity.this, AddMenu.class);
						/*
						 * i.putExtra("ID", id); i.putExtra("Name", name);
						 */
						finish();
						startActivity(i);
					} else if (result.equals("login_success")
							&& usertype.equals("customer")) {

						address = json.getString("address");
						phone = json.getString("mobile_number");
						uname = json.getString("name");
						uEmail = json.getString("email");
						Log.d("address", "" + address);
						Log.d("result", "Login Successful!....");
						// Toast("Login Successful!....");
						editor.putString("MOBILE", phone);
						editor.putString("ADDRESS", address);
						editor.putString("NAME", uname);
						editor.putString("Email", uEmail);
						editor.putString("ID", id);
						editor.commit();

						Intent i = new Intent(MainActivity.this,
								CustomerActivity.class);
						finish();
						/*
						 * i.putExtra("ADDESS", address); i.putExtra("PHONE",
						 * phone); i.putExtra("UNAME", uname); i.putExtra("ID",
						 * id);
						 */
						startActivity(i);
					}

					else if (result.equals("mobile_number_does_not_exist")) {
						// Toast("Check your MobileNumber");

						runOnUiThread(new Runnable() {
							public void run() {
								// Toast("Check your MobileNumber");
								alertdialog();
								message = "Please enter mobile number and password";
							}
						});

					} else {

						alertdialog();
						message = "Please enter vaild mobile number and password";
						// Toast("Please enter vaild mobile number and password");

					}/*
					 * else if (result.equals("not_registered")) {
					 * alertdialog(); title = "Check your MobileNumber";
					 * 
					 * //Toast("Check your MobileNumber");
					 * 
					 * } else if (result.equals("enter_mobile_and_password")) {
					 * alertdialog(); title =
					 * "Please enter mobile number and password";
					 * 
					 * //Toast("Please enter mobile number and password"); }
					 */
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (Exception e) {
				// TODO: handle exception
				// Toast("Unexpected error.");
				alertdialog();
				message = "Unexpected error";
			}
			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted

			pDialog.dismiss();

			String message = null;
			try {
				if (!result.equals("login_success")) {
					// message = "Background succeeded.";
					if (result.equals("mobile_number_does_not_exist")) {
						message = "Please enter vaild mobile number and password";
					} else if (result.equals("not_registered")) {

						message = "Check your MobileNumber";
					} else if (result.equals("enter_mobile_and_password")) {

						message = "Please enter vaild mobile number and password";
					} else if (result.equals("incorrect_username_or_password")) {

						message = "Please enter vaild mobile number and password";
					} else {
						message = " Unexpected error ";
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.i(TAG, message);
			try {
				if (!result.equals("login_success")) {
					createAlertDialog(message).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private Dialog createAlertDialog(String message) {
			AlertDialog.Builder builder = new Builder(pDialog.getContext());
			AlertDialog dialog = builder.setMessage(message)
					.setCancelable(true).create();
			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			return dialog;
		}

	}

	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
		});

	}

	public void alertdialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(MainActivity.this)
						.create();
				alertDialog.setTitle(title);
				alertDialog.setMessage(message);
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				alertDialog.show();
				// Showing Alert Message
			}
		});

	}
}// The content of the adapter has changed but ListView did not receive a
// notification