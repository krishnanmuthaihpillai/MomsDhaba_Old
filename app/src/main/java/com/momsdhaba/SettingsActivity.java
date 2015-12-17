package com.momsdhaba;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.momsdhaba.user.User_History_options;

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
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends BaseActivity {
	private final static String TAG = SettingsActivity.class.getSimpleName();
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	String accountname, accountnumber, bankname, branchname, ifsccode;

	EditText edit_accnumber, edit_accname, edit_bankname, edit_branch,
			edit_ifsc;
	Button submit;
	AlertDialog alertDialog;
	String message, title, result, id;
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	JSONObject jsonObj;
	public static final String MyPREFERENCES = "Prefs";
	SharedPreferences sharedpref;
	private static final String SEND_URL = "http://momsdhaba.com/mobileapp/chefbank/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		set(navMenuTitles, navMenuIcons);

		sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		id = sharedpref.getString("ID", "");
		Log.d(TAG, "sharedpreferences id" + id);
		edit_accnumber = (EditText) findViewById(R.id.etxt_bank_accountno);
		edit_accname = (EditText) findViewById(R.id.etxt_accountname);
		edit_bankname = (EditText) findViewById(R.id.etxt_bankname);
		edit_branch = (EditText) findViewById(R.id.etxt_brachname);
		edit_ifsc = (EditText) findViewById(R.id.etxt_ifsc_code);
		submit = (Button) findViewById(R.id.submit_button);
		
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				accountname = edit_accname.getText().toString().trim();
				accountnumber = edit_accnumber.getText().toString().trim();
				bankname = edit_bankname.getText().toString().trim();
				branchname = edit_branch.getText().toString().trim();
				ifsccode = edit_ifsc.getText().toString().trim();

				if ((!(accountname.trim().length() == 0
						|| accountnumber.trim().length() == 0
						|| bankname.trim().length() == 0
						|| branchname.trim().length() == 0 || ifsccode.trim()
						.length() == 0))) {
					new Updatestates().execute();
				} else {
					message = ("Please fill all fields");
					alertdialog();
					// Toast("Please fill all fields ");
				}

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
			pDialog = new ProgressDialog(SettingsActivity.this);
			pDialog.setMessage("Sending Details...");
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
				params.add(new BasicNameValuePair("account_number",
						accountnumber));
				params.add(new BasicNameValuePair("account_holder_name",
						accountname));
				params.add(new BasicNameValuePair("bank_name", bankname));
				params.add(new BasicNameValuePair("branch", branchname));
				params.add(new BasicNameValuePair("ifsc_code", ifsccode));
				params.add(new BasicNameValuePair("chef_id", id));

				Log.d(TAG, " id=" + id);
				Log.d(TAG, " ifsccode=" + ifsccode);
				Log.d(TAG, " branchname=" + branchname);
				Log.d(TAG, " bankname=" + bankname);
				Log.d(TAG, " accountname=" + accountname);
				Log.d(TAG, " accountnumber=" + accountnumber);

				// Log.d(TAG, "Ftype =" + );
				// Log.d(TAG, "Date =" + );

				JSONObject json = jsonParser.makeHttpRequest(SEND_URL, "POST",
						params);
				// check log cat fro response
				Log.d(TAG, "Create Response" + json.toString());
				// JSONObject obj=data.getJSONObject(i);
				result = json.getString("status");
				Log.d("Result: ", result);

				// check for success tag
				try {

					if (result.equals("success")) {

						Log.d(TAG, "Update " + result);
						
						/*
						 * Intent itab = new Intent(User_History_options.this,
						 * AddMenu.class);
						 * itab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						 * startActivity(itab);
						 */

					} else if (result.equals("keys_are_missing")) {

						Log.d(TAG, "keys_are_missing");
						// Toast("successfully_deleted ");

					} else if (result.equals("failed")) {
						Log.d(TAG, "failed");
						// Toast("failed");
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

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			String message = null;
			try {
				if (result.equals("success")) {
					message = "Successfully canceled your order";					
					/*
					 * else if (result.equals(" ")) { message = "Failed"; } else
					 * { message = "Unexpected Error "; }
					 */
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.i(TAG, message);
			try {
				if (result!=null) {
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
					Intent itab = new Intent(SettingsActivity.this,
							AddMenu.class);
					itab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(itab);

				}
			});
			return dialog;
		}

	}

	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				// Toast.makeText(getApplicationContext(), message,
				// Toast.LENGTH_LONG).show();
			}
		});
	}

	public void alertdialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(SettingsActivity.this)
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
