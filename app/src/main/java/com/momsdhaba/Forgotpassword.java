package com.momsdhaba;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.momsdhaba.MainActivity.AttemptLogin;
import com.momsdhaba.notification.NotificationActivity;
import com.momsdhaba.notification.NotificationData;
import com.momsdhaba.user.CustomerActivity;
import com.momsdhaba.user.User_History_options;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Forgotpassword extends Activity {
	private final static String TAG = CustomerActivity.class.getSimpleName();
	TextView Btn_submit;
	EditText edit_mobile, edit_otp, edit_password, edit_conpwsd;
	String mobile, opt, password, retypepassword, result;
	ProgressDialog dialog;
	String jsonStr;
	BroadcastReceiver receiver;
	boolean location = false;
	AlertDialog alertDialog;
	String message, title;
	boolean check_opt;
	JSONParser jsonParser = new JSONParser();
	private static final String SEND_URL = "http://www.momsdhaba.com/mobileapp/forgetpassword/";
	private ProgressDialog pDialog;
	boolean step1, step2, step3 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgotpassword);
		edit_mobile = (EditText) findViewById(R.id.mobile);
		edit_otp = (EditText) findViewById(R.id.opt);
		edit_password = (EditText) findViewById(R.id.password);
		// edit_conpwsd = (EditText) findViewById(R.id.conformpassword);
		Btn_submit = (TextView) findViewById(R.id.submit);

		edit_mobile.setVisibility(View.VISIBLE);
		edit_otp.setVisibility(View.GONE);
		edit_password.setVisibility(View.GONE);
		
	
		//edit_password.setEnabled(true);
		// edit_conpwsd.setVisibility(View.INVISIBLE);
		Btn_submit.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				mobile = edit_mobile.getText().toString().trim();
				opt = edit_otp.getText().toString().trim();
				password = edit_password.getText().toString().trim();
				
				if(!mobile.isEmpty()&& opt.isEmpty()){
					//edit_mobile.setVisibility(View.GONE);
					edit_otp.setVisibility(View.VISIBLE);
					step1 = true;
					step2 = false;
					step3 = false;
					new Updatestates().execute();
				}
				if (!mobile.isEmpty() && !opt.isEmpty()) {
					edit_password.setVisibility(View.VISIBLE);
					//edit_otp.setVisibility(View.GONE);
					//edit_mobile.setVisibility(View.GONE);
					step1 = false;
					step2 = true;
					step3 = false;
					new Updatestates().execute();
				}
				if (!mobile.isEmpty() && !opt.isEmpty() && !password.isEmpty()) {
					edit_password.setVisibility(View.VISIBLE);
					step1 = false;
					step2 = false;
					step3 = true;
					new Updatestates().execute();
					
				}
			}
		});
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i_cancel = new Intent(Forgotpassword.this,
						MainActivity.class);
				i_cancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i_cancel);

			}
		});
	}

	class Updatestates extends AsyncTask<String, String, String> {
		String result;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Forgotpassword.this);
			pDialog.setMessage("Sending Order States...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			pDialog.setCanceledOnTouchOutside(false);
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			try { // Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();

				if (step1 == true) {
					Log.d("step: ", "step_1");
					params.add(new BasicNameValuePair("step_1", "step_1"));
					params.add(new BasicNameValuePair("mobile_number", mobile));
					Log.d("step: ", "step_1");

				}
				if (step2 == true) {
					Log.d("step: ", "step_2");
					params.add(new BasicNameValuePair("step_2", "step_2"));
					params.add(new BasicNameValuePair("mobile_number", mobile));
					params.add(new BasicNameValuePair("otp", opt));
					Log.d("mobile: ", "mobile");
					Log.d("password: ", "password");
				}
				if (step3 == true) {
					Log.d("step: ", "step_3");
					params.add(new BasicNameValuePair("step_3", "step_3"));
					params.add(new BasicNameValuePair("mobile_number", mobile));
					params.add(new BasicNameValuePair("password", password));
					params.add(new BasicNameValuePair("otp", opt));
					Log.d("step: ", "step_1");
					Log.d("mobile: ", "mobile");
					Log.d("password: ", "password");
				}

				JSONObject json = jsonParser.makeHttpRequest(SEND_URL, "POST",
						params);
				// check log cat fro response
				Log.d(TAG, "Create Response" + json.toString());
				// JSONObject obj=data.getJSONObject(i);
				result = json.getString("status");
				Log.d("Result: ", result);

				// check for success tag
				try {

					if (result.equals("OTP_Generated")) {
						optprocess();

					} else if (result.equals("OTP_is_correct")) {

						passwordprocess();

					} else if (result.equals("password_changed")) {

						changepassword();

					} else if (result
							.equals("request_is_not_POST_or_mobile_number_not_exist")) {

					}
				} catch (Exception e) {
					e.printStackTrace();
					// Toast("e");
				}

			}

			catch (Exception e) {
				// TODO: handle exception
				// Toast("Unexpected error.");
			}
			return null;
		}

		public void changepassword() {
			// TODO Auto-generated method stub
			Log.i(TAG, "passwordprocess()");
		}

		public void passwordprocess() {
			Log.i(TAG, "passwordprocess()");
			//edit_password.setVisibility(View.VISIBLE);
			//edit_conpwsd.setVisibility(View.VISIBLE);

		}

		public void optprocess() {
			// TODO Auto-generated method stub
			Log.i(TAG, "optprocess()");
			//edit_otp.setVisibility(View.VISIBLE);
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			String message = null;
			try {
				if (result.equals("OTP_Generated")) {

					message = "OTP will Recive to Your Mobile";
				} else if (result.equals("OTP_is_correct")) {

					message = " OTP is Correct";

				} else if (result.equals("password_changed")) {

					message = "Password Successfully Change";

				} else if (result
						.equals("mobile_number_not_exist")) {
					message = "Mobile Number Not Exist";

				} else if (result.equals("OTP_time_out")) {
					message = "Mobile Number Not Exist";
				}

				else if (result.equals("invalid_OTP")) {
					message = "Invalid OPT";
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.i(TAG, message);
			try {
				if (result != null) {
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
					.setCancelable(false).create();
			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					if (result.equals("invalid_OTP")
							|| result.equals("OTP_time_out")
							|| result.equals("password_changed")) {
						Intent itab = new Intent(Forgotpassword.this,
								MainActivity.class);
						itab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(itab);
					}
				}
			});
			return dialog;
		}
	}

	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				// Toast.makeText(getApplicationContext(), message,
			}
		});
	}

	public void alertdialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(Forgotpassword.this)
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
}
