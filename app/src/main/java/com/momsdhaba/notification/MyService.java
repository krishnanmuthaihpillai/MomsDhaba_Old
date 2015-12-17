package com.momsdhaba.notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

import com.momsdhaba.AddMenu;
import com.momsdhaba.ConnectionDetector;
import com.momsdhaba.JSONParser;
import com.momsdhaba.MainActivity;
import com.momsdhaba.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class MyService extends Service {
	private static final String TAG = "MyService";
	public static int sInterval = 60000;
	private boolean mIsRunning = false;
	private Handler mHandler;
	private Timer mTimer;
	ConnectionDetector cd;
	private boolean mIsNetworkAvailable = false;
	private NotificationManager myNotificationManager;
	private int notificationIdOne = 111;
	private int numMessagesOne = 1;
	private String notfication_value;

	ArrayList<NotificationData> notificationList;
	JSONParser jsonParser = new JSONParser();
	ProgressDialog dialog;
	NotificationAdapter adapter;
	String result, id;
	public static final String MyPREFERENCES = "Prefs";
	SharedPreferences sharedpref, sharedprefdata;
	public static final String MyPREF = "Preference";
	Editor editordata;
	Context con;
	int adaptercount, sharecount;

	@Override
	public void onCreate() {
		Log.i(TAG, "Service onCreate");
		mHandler = new Handler();
		mTimer = new Timer();
		mIsRunning = true;
		cd = new ConnectionDetector(getApplicationContext());
		con = getApplicationContext();
		notificationList = new ArrayList<NotificationData>();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.e(TAG, "Service onStartCommand");
		// NotificationActivity notific = new NotificationActivity();
		// notific.callservice();
		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {
				mHandler.post(new Runnable() {
					public void run() {
						try {
							if (mIsRunning) {
								Log.e(TAG, "service.execute()");

								adapter = new NotificationAdapter(getApplicationContext(),
										R.layout.notification_items,notificationList);
								// NotificationActivity notific = new
								// NotificationActivity();
								// notific.callservice();
								// NotificationActivity.callservice();
								Log.e(TAG, "sharecount " + sharecount);
								Log.e(TAG, "adaptercount " + adaptercount);
								if (sharecount != adaptercount) {
									Log.d(TAG, "displayNotificationOne");
									displayNotificationOne();
								}
								new ProgressTask().execute("http://momsdhaba.com/mobileapp/chefnotification/");
								// Intent i = new Intent(MyService.this,
								// NotificationActivity.class);
								// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								// startActivity(i);

								mIsNetworkAvailable = cd
										.isConnectingToInternet();
							}

							if (!mIsNetworkAvailable) {
								/*
								 * Toast.makeText(getApplicationContext(),
								 * "Network is not Available",
								 * Toast.LENGTH_SHORT).show();
								 */
								// Log.d("pushpa", "" + mIsNetworkAvailable+
								// "");
							}

						} catch (Exception e) {

						}
					}
				});
			}
		};
		mTimer.schedule(doAsynchronousTask, 0, sInterval); // execute in every
															// 20000ms

		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// Log.i(TAG, "Service onBind");
		return null;
	}

	@Override
	public void onDestroy() {
		mIsRunning = false;
		Log.i(TAG, "Service onDestroy");
	}

	protected void displayNotificationOne() {

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this);

		mBuilder.setContentTitle("You got a New Message");
		mBuilder.setContentText("New Message Received!");
		mBuilder.setTicker("New Message Received!");
		mBuilder.setSmallIcon(R.drawable.momsdhabalogo);
		//mBuilder.setAutoCancel(true);
		// Increase notification number every time a new notification arrives
		mBuilder.setNumber(numMessagesOne);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, NotificationActivity.class);
		resultIntent.putExtra("notificationId", notificationIdOne);

		// This ensures that navigating backward from the Activity leads out of
		// the app to Home page
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		// Adds the back stack for the Intent
		stackBuilder.addParentStack(NotificationActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_ONE_SHOT // can only be used once
				);
		// start the activity when the user clicks the notification text
		mBuilder.setContentIntent(resultPendingIntent);

		myNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

		// pass the Notification object to the system
		myNotificationManager.notify(notificationIdOne, mBuilder.build());
		// }

	}

	class ProgressTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				Log.e(TAG, "onPreExecute");
				adapter.clear();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//
			// dialog = new ProgressDialog(getApplicationContext());
			// dialog.setMessage("Loading, please wait");
			// dialog.setTitle("Connecting server"); dialog.show();
			// dialog.setCancelable(false);

		}

		@Override
		protected Boolean doInBackground(String... urls) {
			try {

				sharedpref = getSharedPreferences(MyPREFERENCES,
						Context.MODE_PRIVATE);
				id = sharedpref.getString("ID", "");

				Log.d(TAG, "sharedpreferences service id" + id);
				// ------------------>>
				HttpGet httppost = new HttpGet(urls[0]);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httppost);

				// StatusLine stat = response.getStatusLine();
				int status = response.getStatusLine().getStatusCode();

				if (status == 200) {
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("chefid", id));
					Log.d(TAG, "BasicNameValuePair = " + id);
					JSONObject json = jsonParser.makeHttpRequest(
							"http://momsdhaba.com/mobileapp/chefnotification/",
							"POST", params);
					// Log.d(TAG, "Create Response" + json.toString());

					try {

						result = json.getString("status");

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JSONArray jarray = json.getJSONArray("order_history");

					for (int i = 0; i < jarray.length(); i++) {
						JSONObject object = jarray.getJSONObject(i);

						NotificationData user = new NotificationData();
						user.setFoodName(object.getString("food"));
						user.setFoodcount(object.getString("count"));
						user.setDate(object.getString("datetime"));
						user.setFoodId(object.getString("food_id"));
						user.setStatus(object.getString("chef_status"));
						user.setOrderId(object.getString("order_id"));
						// user.setTotal(object.getString("price"));
						// Log.i(TAG, "food = " + object.getString("food"));
						// Log.i(TAG, "count = " + object.getString("count"));
						// Log.i(TAG, "datetime = " +
						// object.getString("datetime"));
						// Log.i(TAG, "order_id = " +
						// object.getString("order_id"));
						// Log.i(TAG,
						// "chef_status = "
						// + object.getString("chef_status"));
						notificationList.add(user);

					}
					return true;
				}

				// ------------------>>

			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}

		protected void onPostExecute(Boolean file_url) {
			// dialog.dismiss();

			Thread t = new Thread(new Runnable() {

				@Override
				public void run() { // TODO Auto-generated method stub
					sharedprefdata = getSharedPreferences(MyPREF,
							Context.MODE_PRIVATE);
					sharecount = (sharedprefdata.getInt("ADAPCOUNT", 0));
					Log.d(TAG, "sharecount" + sharecount);

					adapter.notifyDataSetChanged();
					adaptercount = adapter.getCount();
					editordata = sharedprefdata.edit();
					editordata.putInt("ADAPCOUNT", adaptercount);
					editordata.clear();
					editordata.commit();

					Log.d(TAG, "adaptercount " + adaptercount);
					Log.d(TAG, "adapter.getCount()" + adapter.getCount());
					Log.d(TAG, "******adapter.notifyDataSetChanged()*****");
				}
			});
			t.start();

			
			/*
			 * String message = null;
			 * 
			 * try { if (result.equals("orders_are_not_available")) {
			 * 
			 * message = "Your orders basket is empty!";
			 * 
			 * } else if (result.equals("false")) { message =
			 * "Unable to fetch data from server";
			 * 
			 * } } catch (Exception e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 * 
			 * if (message != null) { Log.i(TAG, message);
			 * createAlertDialog(message).show(); }
			 * 
			 * }
			 * 
			 * private Dialog createAlertDialog(String message) {
			 * AlertDialog.Builder builder = new
			 * Builder(getApplicationContext()); AlertDialog dialog =
			 * builder.setMessage(message) .setCancelable(true).create();
			 * dialog.setButton("OK", new DialogInterface.OnClickListener() {
			 * public void onClick(DialogInterface dialog, int which) { } });
			 */
			return;
		}
	}
}
