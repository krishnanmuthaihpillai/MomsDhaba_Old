package com.momsdhaba.user;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.momsdhaba.AppLocationService;
import com.momsdhaba.JSONParser;
import com.momsdhaba.R;
import com.momsdhaba.ServiceHandler;

public class CustomerActivity extends BaseActivity2 implements OnClickListener {

	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private final static String TAG = CustomerActivity.class.getSimpleName();
	String id;
	ArrayList<Userdata> usersList;
	UserlistAdapter adapter;
	JSONParser jsonParser = new JSONParser();
	String l_cname, l_fname, l_ftype, l_fdescription, l_date, l_fprice, l_id,
			l_quantity;
	private static String URL = "http://momsdhaba.com/mobileapp/customerfoodview/";
	JSONObject jsonObj;
	JSONArray jsonarray;
	private static final float THRESHOLD_DIST = 6000;
	AppLocationService appLocationService;
	double user_latitude = 0;
	double user_longitude = 0;
	double[] Lattitude, Longitude;
	LocationManager manager;
	Location nwLocation, gpsLocation;
	String longitude, lattitude, Address, uname, phone, result, time;
	float Total, state = 0.0f;
	TextView txtTotal, checkout;
	ProgressDialog dialog;
	String jsonStr;
	BroadcastReceiver receiver;
	boolean location = false;
	AlertDialog alertDialog;
	String message, title;
	String D_count;
	int myNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer);
		navMenuTitles = getResources().getStringArray(
				R.array.nav_drawer_items_user); // load
		navMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons_user);// load icons from
		// strings.xml
		set(navMenuTitles, navMenuIcons);
		txtTotal = (TextView) findViewById(R.id.totla_amt);
		getValue();
		/*
		 * Intent intent = getIntent(); Address =
		 * intent.getStringExtra("ADDESS"); uname =
		 * intent.getStringExtra("UNAME"); phone =
		 * intent.getStringExtra("PHONE"); id = intent.getStringExtra("ID");
		 */

		Log.d(TAG, "Address " + Address);
		Log.d(TAG, "UNAME " + uname);
		Log.d(TAG, "PHONE " + phone);
		Log.d(TAG, "User ID " + id);
		Total = adapter.Amounttotal;

		Log.d(TAG, "Amounttotal " + Total);
		Log.i(TAG, "Amounttotal " + Total);

		appLocationService = new AppLocationService(CustomerActivity.this);
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		usersList = new ArrayList<Userdata>();
		checkout = (TextView) findViewById(R.id.checkout);
		checkout.setOnClickListener(this);

		try {
			nwLocation = appLocationService
					.getLocation(LocationManager.NETWORK_PROVIDER);
			gpsLocation = appLocationService
					.getLocation(LocationManager.GPS_PROVIDER);

			if (gpsLocation != null) {

				user_latitude = gpsLocation.getLatitude();
				user_longitude = gpsLocation.getLongitude();
				Log.d(TAG, "gpsLocation " + user_latitude + " : "
						+ user_longitude);
			}/*
			 * else if (!manager
			 * .isProviderEnabled(LocationManager.GPS_PROVIDER)) { // Ask the
			 * user to enable GPS AlertDialog.Builder builder = new
			 * AlertDialog.Builder( CustomerActivity.this);
			 * builder.setTitle("Location Manager");
			 * builder.setMessage("Please Enable Your GPS?");
			 * builder.setPositiveButton("Settings", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { Intent i = new Intent(
			 * Settings.ACTION_LOCATION_SOURCE_SETTINGS); startActivity(i); }
			 * }); builder.setNegativeButton("Close", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { // No location service, no Activity dialog.cancel(); } });
			 * builder.create().show(); }
			 */
			else if (nwLocation != null) {

				user_latitude = nwLocation.getLatitude();
				user_longitude = nwLocation.getLongitude();
				Log.d(TAG, "nwLocation " + user_latitude + " : "
						+ user_longitude);
			} else if (nwLocation == null && gpsLocation == null) {

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(TAG, "Location " + user_latitude + " : " + user_longitude);

		// new
		// JSONAsyncTask().execute("http://192.168.0.106:8000/mobileapp/chefviewfood/");

		new ProgressTask().execute();

		// timer();
		ListView listview = (ListView) findViewById(R.id.userlistview);
		adapter = new UserlistAdapter(getApplicationContext(),
				R.layout.userlistitems, usersList);

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				l_cname = usersList.get(position).getName();
				l_fname = usersList.get(position).getFoodName();

				Log.d(TAG, "l_cname " + l_cname);
				Log.d(TAG, "l_fname " + l_fname);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.e(TAG, "state ??????" + state);
		if (state == 0.0) {
			// alertdialog();
			// message = "Your orders basket is empty!";
		} else {
			Intent i = new Intent(this, OrderActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(i, 1);
			CustomerActivity.this.finish();
		}
	}

	public void timer() {
		final Handler h = new Handler();
		Timer t = new Timer();
		TimerTask ttask = new TimerTask() {

			@Override
			public void run() {

				h.post(new Runnable() {

					@Override
					public void run() {
						if (!isFinishing()) {
							new ProgressTask().execute();
						}

						Log.d(TAG, "timer");
					}
				});
			}
		};
		t.schedule(ttask, 0, 200000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	public void getValue() {

		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				Log.i("Receiver", "Broadcast received: " + action);

				if (action.equals("my.action.string")) {
					// String state = intent.getExtras().getString("TOTAL");

					state = intent.getFloatExtra("TOTAL", 0.0f);
					Log.d(TAG, "Broadcast received " + state);
					Log.d(TAG, "Broadcast received " + state);
					// do your stuff
					txtTotal.setText("₹ :" + state);
					Log.d(TAG, "state " + state);
				}
			}
		};
		IntentFilter filter = new IntentFilter("my.action.string");
		registerReceiver(receiver, filter);

	}

	class ProgressTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				adapter.clear();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d(TAG, "onPreExecute clear");
			/*
			 * dialog = new ProgressDialog(CustomerActivity.this);
			 * dialog.setMessage("Loading, please wait");
			 * dialog.setTitle("Connecting server"); dialog.show();
			 * dialog.setCancelable(false);
			 */
		}

		@Override
		protected Boolean doInBackground(String... urls) {

			try {

				// ------------------>>
				Log.d(TAG, "doInBackground ");
				ServiceHandler sh = new ServiceHandler();
				jsonStr = sh.makeServiceCall(URL, ServiceHandler.POST);
				Log.d("Response: ", jsonStr);
				if (!jsonStr.matches("") && jsonStr != null) {

					jsonObj = new JSONObject(jsonStr);

					try {
						result = jsonObj.getString("status");
						Log.d("Result: ", result);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					jsonarray = jsonObj.getJSONArray("time_is_over");
					for (int i = 0; i < jsonarray.length(); i++) {
						JSONObject object = jsonarray.getJSONObject(i);
						time = object.getString("time_is_over");
						Log.d("Time : ", time);
					}

					jsonarray = jsonObj.getJSONArray("today_menu");
					for (int i = 0; i < jsonarray.length(); i++) {
						JSONObject object = jsonarray.getJSONObject(i);

						Userdata dataitems = new Userdata();

						dataitems.setName(object.getString("chef_name"));
						dataitems.setDescription(object
								.getString("description"));
						dataitems.setDate(object.getString("available_date"));
						dataitems.setFoodtype(object.getString("food_type"));
						dataitems.setPrice(object.getString("food_price"));
						dataitems.setFoodName(object.getString("food_name"));
						dataitems.setImage(object.getString("food_image"));
						dataitems.setFoodQuantity(object
								.getString("food_quantity"));
						dataitems.setProfile(object
								.getString("chef_profile_image"));
						dataitems.setFoodId(object.getString("food_id"));
						lattitude = object.getString("chef_latitude");
						longitude = object.getString("chef_longitude");
						D_count = object.getString("food_quantity");
						Log.d("D_count", "" + D_count);
						Log.d("lattitude", "" + lattitude);
						Log.d("longitude", "" + longitude);
						try {
							myNum = Integer.parseInt(D_count);
							Log.d(TAG, "myNum"+myNum);
						} catch (NumberFormatException nfe) {
							
						}

						Log.d("food_id", "" + object.getString("food_id"));

						float distance = distFrom((float) user_latitude,
								(float) user_longitude,
								Float.valueOf(lattitude),
								Float.valueOf(longitude));

						if (distance < THRESHOLD_DIST) {

							Log.d("THRESHOLD_DIST",
									"true" + object.getString("food_name"));
							// usersList.add(dataitems);
							if (myNum > 0) {
								usersList.add(dataitems);
							}
						} else {
							Log.d("THRESHOLD_DIST",
									"false" + object.getString("food_name"));
						}
					}
					return true;
				}

				// ------------------>>

			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}

		// /////////////////////**********************////////////////
		public float distFrom(float lat1, float lng1, float lat2, float lng2) {
			double earthRadius = 6371000; // meters
			double dLat = Math.toRadians(lat2 - lat1);
			double dLng = Math.toRadians(lng2 - lng1);
			double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
					+ Math.cos(Math.toRadians(lat1))
					* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
					* Math.sin(dLng / 2);
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			float dist = (float) (earthRadius * c);

			return dist;
		}

		protected void onPostExecute(Boolean file_url) {
			// dialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					adapter.notifyDataSetChanged();
					Log.d(TAG, "onPostExecute ");
				}
			});

			String message = null;
			try {

				if (!time.equals("True")) {
					message = "Today's food ordering time is closed.." + "\n"
							+ " Please come to us by tomorrow at 10.00am. "
							+ "\n" + " Our closing time for Lunch is 11.30am";
				}

				if (result.equals("menu_not_available")) {

					message = "Today's Menu Not Available";

				} else if (result.equals("time_is_over")) {
					message = "Today's food ordering time is closed.." + "\n"
							+ " Please come to us by tomorrow at 10.00am. "
							+ "\n" + " Our closing time for Lunch is 11.30am";

				} else if (result.equals("false")) {
					message = "Unable to fetch data from server";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (message != null) {
				createAlertDialog(message).show();
			}

		}

		private Dialog createAlertDialog(String message) {
			AlertDialog.Builder builder = new Builder(CustomerActivity.this);
			AlertDialog dialog = builder.setMessage(message)
					.setCancelable(true).create();
			/*
			 * dialog.setButton("Exit", new DialogInterface.OnClickListener() {
			 * public void onClick(DialogInterface dialog, int which) { } });
			 */
			dialog.setButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			return dialog;
		}
	}

	public void alertdialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(CustomerActivity.this)
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