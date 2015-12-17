package com.momsdhaba;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class CustomerRegistration extends Activity {
	private final static String TAG = CustomerRegistration.class
			.getSimpleName();
	Button Btn_register;
	EditText edit_username, edit_mobilenumber, edit_emailid, edit_password,
			edit_repassword, edit_address;
	String uname, mobile, email, password, repassword, address, result;
	JSONParser jsonParser = new JSONParser();
	private static final String REGIST_URL = "http://momsdhaba.com/mobileapp/customerregister/";
	private ProgressDialog pDialog;
	ConnectionDetector cd;
	boolean isInternetPresent = false;
	public CheckBox checkbox;
	boolean check = false;
	String message, title;
	AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_registration);
		edit_username = (EditText) findViewById(R.id.name);
		edit_mobilenumber = (EditText) findViewById(R.id.mobile);
		edit_emailid = (EditText) findViewById(R.id.email);
		edit_password = (EditText) findViewById(R.id.password);
		edit_repassword = (EditText) findViewById(R.id.confrompassword);
		edit_address = (EditText) findViewById(R.id.address);
		Btn_register = (Button) findViewById(R.id.register_btn);
		checkbox = (CheckBox) findViewById(R.id.checkbox);

		checkbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (((CheckBox) v).isChecked()) {
					Intent i = new Intent(CustomerRegistration.this,
							Terms.class);
					startActivity(i);
					check = true;
				}
			}
		});
		Btn_register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				uname = edit_username.getText().toString().trim();
				mobile = edit_mobilenumber.getText().toString().trim();
				email = edit_emailid.getText().toString().trim();
				password = edit_password.getText().toString().trim();
				repassword = edit_repassword.getText().toString().trim();
				address = edit_address.getText().toString().trim();
				if ((!(uname.trim().length() == 0
						|| mobile.trim().length() == 0
						|| password.trim().length() == 0
						|| repassword.trim().length() == 0 || address.trim()
						.length() == 0))) {

					if (isValidName(uname)) {
						if (password.equals(repassword)) {

							if (validEmail(email)) {

								if (check == true) {
									new RegisterCustomer().execute();
								} else {
									message = "Accept Terms & Conditions";
									alertdialog();
									//Toast("Accept Terms & Conditions");
								}
							} else {
								message = "Invalid Email Format";
								alertdialog();
								//Toast("Invalid Email Format");
								edit_emailid.setError("Invalid Email");
							}
						} else {
							edit_repassword.setError("Password Not Match");
							//Toast("Password Not Match");
							message = "Password Not Match";
							alertdialog();
						}
					} else {
						message = "special characters not allowed";
						alertdialog();
						//Toast("special characters not allowed");
					}

				} else {
					message = "Please fill all fields";
					alertdialog();
					//Toast("Please fill all fields");
				}
			}
		});
	}

	class RegisterCustomer extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CustomerRegistration.this);
			pDialog.setMessage("Registering Details...");
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
				params.add(new BasicNameValuePair("name", uname));
				params.add(new BasicNameValuePair("mobile_number", mobile));
				params.add(new BasicNameValuePair("password", password));
				params.add(new BasicNameValuePair("email_address", email));
				params.add(new BasicNameValuePair("address", address));
				params.add(new BasicNameValuePair("usertype", "CUSTOMER"));
				Log.d(TAG, "uname  " + uname);
				Log.d(TAG, "mobile  " + mobile);
				Log.d(TAG, "email_address  " + email);
				Log.d(TAG, "password  " + password);
				Log.d(TAG, "address  " + address);

				JSONObject json = jsonParser.makeHttpRequest(REGIST_URL,
						"POST", params);
				// check log cat fro response
				Log.d("Create Response", json.toString());
				result = json.getString("status");
				String id = json.getString("id");
				String name = json.getString("name");

				Log.d(TAG, "id" + id);
				Log.d(TAG, "name" + name);

				// check for success tag
				try {

					if (result.equals("success")) {

						Log.d(TAG, "Success" + result);
						Intent i = new Intent(getApplicationContext(),MainActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(i);
						finish();
					} else if (result.equals("mobile_number_already_exist")) {

						//Toast("Mobile Number Already Exist");
					} else {
						Log.d(TAG, "else" + result);
						//Toast("Error");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			catch (Exception e) {
				// TODO: handle exception
				Toast("Unexpected error.");
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
				if (!result.equals("success")) {
					// message = "Background succeeded.";
					if (result.equals("mobile_number_already_exist")) {
						message = "Mobile Number Already Exist";
					} else {
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
			dialog.setButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
						}
					});
			return dialog;
		}
	}

	private boolean isValidName(String name) {
		String NAME_PATTERN = "^[a-zA-Z ]+$";

		Pattern pattern = Pattern.compile(NAME_PATTERN);
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}

	private boolean validEmail(String email) {
		Pattern pattern = Patterns.EMAIL_ADDRESS;
		return pattern.matcher(email).matches();
	}

	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
//				Toast.makeText(getApplicationContext(), message,
//						Toast.LENGTH_LONG).show();
			}
		});
	}

	public void alertdialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(CustomerRegistration.this)
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