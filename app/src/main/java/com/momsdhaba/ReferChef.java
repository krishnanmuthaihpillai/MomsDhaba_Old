package com.momsdhaba;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ReferChef extends BaseActivity{
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private final static String TAG = ReferChef.class.getSimpleName();
	String mobile, name, Id ,usertype;
	private EditText Emobile, Ename;
	public static final String MyPREFERENCES = "Prefs";
	SharedPreferences sharedpref;
	AlertDialog alertDialog;
	String message, title, result;
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	JSONObject json;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refer_chef);
		
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		set(navMenuTitles, navMenuIcons);
		
		sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		Id = sharedpref.getString("ID", "");
		usertype =sharedpref.getString("UserType", "");
		Log.d(TAG, "sharedpreferences id" + Id);
		Log.d(TAG, "sharedpreferences usertype" + usertype);
		
		Ename =(EditText)findViewById(R.id.edit_name);
		Emobile =(EditText)findViewById(R.id.edit_mobile);
		findViewById(R.id.btn_refer).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				mobile = Emobile.getText().toString().trim();
				name = Ename.getText().toString().trim();
				if (mobile != null) {
					if (name != null) {
						new AttemptLogin().execute();
					} else {
						message = "Please enter Name";
						alertdialog();
					}
				} else {
					message = "Please enter Mobile number";
					alertdialog();
				}

			}

		});
	}

	class AttemptLogin extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			pDialog = new ProgressDialog(ReferChef.this);
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			pDialog.setCanceledOnTouchOutside(false);

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			// Building Parameters
			try {

				List<NameValuePair> paramter = new ArrayList<NameValuePair>();
				paramter.add(new BasicNameValuePair("chefname", name));
				paramter.add(new BasicNameValuePair("chefmobileno",mobile ));
				paramter.add(new BasicNameValuePair("userid", Id));
				paramter.add(new BasicNameValuePair("usertype",usertype));
				Log.d("mobile_number!", mobile);
				Log.d("name", name);
				Log.d("Id", Id);

				JSONObject json = jsonParser.makeHttpRequest(
						"http://momsdhaba.com/mobileapp/referchef/",
						"POST", paramter);

				Log.d("Login attempt", json.toString());

				try {
					result = json.getString("status");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
				}
				Log.d("result", "" + result);

				try {

					if (result.equals("success")) {
						Log.d("result", "Successful!....");
						// Toast("Login Successful!....");

						Intent i = new Intent(ReferChef.this,ReferChef.class);
						finish();
						startActivity(i);
					}

					else {
						// Toast("Check your MobileNumber");

						runOnUiThread(new Runnable() {
							public void run() {
								// Toast("Check your MobileNumber");
								alertdialog();
								message = "Activity Failed";
							}
						});

					}

				} catch (Exception e) {
					// TODO: handle exception
					// Toast("Unexpected error.");
					alertdialog();
					message = "Unexpected error";
				}

				return "";

			} finally {
				// ... cleanup that will execute whether or not an error
				// occurred ...
			}
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted

			pDialog.dismiss();

			String message = null;
			try {
				if (!result.equals("success")) {
					// message = "Background succeeded.";
					if (result.equals("keys_are_missing")) {
						message = "Enter Name and Mobile Number";
					}   else {
						message = "Unexpected Error ";
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.i(TAG, message);
			if (!result.equals("success")) {
				createAlertDialog(message).show();
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
				// Toast.makeText(getApplicationContext(),
				// message,Toast.LENGTH_LONG).show();
			}
		});

	}

	public void alertdialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(ReferChef.this)
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

