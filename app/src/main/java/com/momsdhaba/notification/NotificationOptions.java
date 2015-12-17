package com.momsdhaba.notification;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.momsdhaba.AddMenu;
import com.momsdhaba.JSONParser;
import com.momsdhaba.R;

public class NotificationOptions extends Activity {

	private final static String TAG = NotificationOptions.class.getSimpleName();
	private static final String SEND_URL = "http://www.momsdhaba.com/mobileapp/chefstatusonorder/";
	String fname, fcount, fid, fdate, ok, cancel, ready, delivery, orderstates,
			usercancel;
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	JSONObject jsonObj;
	JSONArray contacts;
	Button Ok, Ready, Delivery, Cancel;
	String message, title;
	AlertDialog alertDialog;
	TextView text_food, text_count, text_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_options);
		
		ImageView i_back = (ImageView) findViewById(R.id.back);
		text_food= (TextView)findViewById(R.id.foodname);
		text_count= (TextView)findViewById(R.id.foodcount);
		text_cancel = (TextView)findViewById(R.id.user_cancel);
		text_cancel.setVisibility(View.GONE);
		// if(option != null){
		// new Updatestates().execute();
		// }else{
		//
		// }
		Intent intent = getIntent();
		fname = intent.getStringExtra("Food");
		fcount = intent.getStringExtra("Count");
		fid = intent.getStringExtra("FoodId");
		fdate = intent.getStringExtra("Date");
	//	usercancel = intent.getStringExtra("Date");
		text_food.setText(fname);
		text_count.setText(fcount);
		
		try {
			if(!usercancel.isEmpty()){
				text_cancel.setVisibility(View.VISIBLE);				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				orderstates = "CONFIRMED";
				new Updatestates().execute();
			}

		});
		findViewById(R.id.btn_ready).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				orderstates = "READY_FOR_DELIVERY";
				new Updatestates().execute();
			}

		});
		findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				orderstates = "DECLINED";
				new Updatestates().execute();
			}

		});
		findViewById(R.id.btn_delivery).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						orderstates = "DELIVERED";
						new Updatestates().execute();
					}

				});
		i_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent itab = new Intent(NotificationOptions.this,NotificationActivity.class);
				startActivity(itab);
				finish();
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
			pDialog = new ProgressDialog(NotificationOptions.this);
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
				params.add(new BasicNameValuePair("food_id", fid));
				params.add(new BasicNameValuePair("chef_status", orderstates));
				// params.add(new BasicNameValuePair("food_type", ));
				// params.add(new BasicNameValuePair("food_price", ));
				// params.add(new BasicNameValuePair("description", ));

				Log.d(TAG, " States=" + orderstates);
				Log.d(TAG, "Foodid =" + fid);
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
						Intent itab = new Intent(NotificationOptions.this,
								AddMenu.class);
						itab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(itab);

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
				if (!result.equals("success")) {
					// message = "Background succeeded.";

					message = "Failed";
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
				if (!result.equals("success")) {
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
				// Toast.makeText(getApplicationContext(), message,
				// Toast.LENGTH_LONG).show();
			}
		});
	}

	public void alertdialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(NotificationOptions.this)
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