package com.momsdhaba.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.momsdhaba.AddMenu;
import com.momsdhaba.JSONParser;
import com.momsdhaba.MainActivity;
import com.momsdhaba.R;

public class User_History_options extends Activity {
	TextView text_food, text_count, text_id;
	String message, title;
	AlertDialog alertDialog;
	private final static String TAG = User_History_options.class
			.getSimpleName();
	private static final String SEND_URL = "http://momsdhaba.com/mobileapp/cancelorder/";
	String fname, fcount, fdate, ok, cancel, ready, delivery, orderstates,
			usercancel, foodid, orderid, orderitemid, id;
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	JSONObject jsonObj;
	JSONArray contacts;
	public static final String MyPREFERENCES = "Prefs";
	SharedPreferences sharedpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user__history_options);
		ImageView i_back = (ImageView) findViewById(R.id.back);
		text_food = (TextView) findViewById(R.id.foodname);
		text_count = (TextView) findViewById(R.id.foodcount);
		text_id = (TextView) findViewById(R.id.orderid);

		sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		id = sharedpref.getString("ID", "");
		Log.d(TAG, "sharedpreferences id" + id);

		Intent intent = getIntent();
		fname = intent.getStringExtra("FoodName");
		fcount = intent.getStringExtra("Count");
		fdate = intent.getStringExtra("Amount");
		orderid = intent.getStringExtra("OrderId");
		orderitemid = intent.getStringExtra("OrderItemId");
		foodid = intent.getStringExtra("FoodId");
		fdate = intent.getStringExtra("Date");

		text_food.setText(fname);
		text_count.setText(fcount);
		text_id.setText(orderid);
		findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Updatestates().execute();
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
			pDialog = new ProgressDialog(User_History_options.this);
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
				params.add(new BasicNameValuePair("user_id", id));
				params.add(new BasicNameValuePair("order_id", orderid));
				params.add(new BasicNameValuePair("food_id", foodid));
				params.add(new BasicNameValuePair("order_item_id", orderitemid));

				Log.d(TAG, " id=" + id);
				Log.d(TAG, " orderid=" + orderid);
				Log.d(TAG, " foodid=" + foodid);
				Log.d(TAG, " orderitemid=" + orderitemid);

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
					Intent itab = new Intent(User_History_options.this,
							MainActivity.class);
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
				alertDialog = new AlertDialog.Builder(User_History_options.this)
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
