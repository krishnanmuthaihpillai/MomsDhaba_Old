package com.momsdhaba.notification;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.momsdhaba.BaseActivity;
import com.momsdhaba.JSONParser;
import com.momsdhaba.MainActivity;
import com.momsdhaba.R;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationActivity extends BaseActivity {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private final static String TAG = NotificationActivity.class
			.getSimpleName();
	ArrayList<NotificationData> notificationList;
	JSONParser jsonParser = new JSONParser();
	ProgressDialog dialog;
	NotificationAdapter adapter;
	ListView listview;
	String result, id;
	public static final String MyPREFERENCES = "Prefs";
	SharedPreferences sharedpref;
	String l_fname, l_fcount, l_date, l_fid;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.momsdhaba.R.layout.notification);
		String ns = Context.NOTIFICATION_SERVICE;
//		NotificationManager nMgr = (NotificationManager) getApplicationContext()
//				.getSystemService(ns);
//		nMgr.cancel(ns, 0);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		set(navMenuTitles, navMenuIcons);

		context = this;
		sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		id = sharedpref.getString("ID", "");
		Log.d(TAG, "sharedpreferences id" + id);
		
		if(id.isEmpty()){
			Intent i = new Intent(NotificationActivity.this, MainActivity.class);
			finish();
			startActivity(i);
		}

		notificationList = new ArrayList<NotificationData>();
		listview = (ListView) findViewById(R.id.notification_list);

		adapter = new NotificationAdapter(getApplicationContext(),
				R.layout.notification_items, notificationList);
		listview.setAdapter(adapter);

		Log.d(TAG, "callservice();");
		timer();
		// callservice();

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// adapter.notifyDataSetChanged();
				l_fname = notificationList.get(position).getFoodName();
				l_fcount = notificationList.get(position).getFoodcount();
				l_date = notificationList.get(position).getDate();
				l_fid = notificationList.get(position).getFoodId();
				Log.d(TAG, "l_fname " + l_fname);

				Intent intent = new Intent(NotificationActivity.this,
						NotificationOptions.class);
				intent.putExtra("Food", l_fname);
				intent.putExtra("Count", l_fcount);
				intent.putExtra("Date", l_date);
				intent.putExtra("FoodId", l_fid);
				startActivity(intent);
			}
		});
	}

	/*
	 * public void callservice() { Log.d(TAG, " void callservice()"); //
	 * listview.setAdapter(adapter); // if (!isFinishing()) { new ProgressTask()
	 * .execute("http://momsdhaba.com/mobileapp/chefnotification/"); // } }
	 */
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
							new ProgressTask()
									.execute("http://momsdhaba.com/mobileapp/chefnotification/");
						}
						Log.d(TAG, "timer");
					}
				});
			}
		};
		t.schedule(ttask, 0, 200000);
	}

	class ProgressTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d(TAG, " onPreExecute()");
			adapter.clear();
			dialog = new ProgressDialog(NotificationActivity.this);
			dialog.setMessage("Loading, please wait");
			dialog.setTitle("Connecting server");
			dialog.show();
			dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(String... urls) {
			try {
				Log.d(TAG, " doInBackground");
				// ------------------>>
				HttpGet httppost = new HttpGet(urls[0]);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httppost);

				// StatusLine stat = response.getStatusLine();
				int status = response.getStatusLine().getStatusCode();
				Log.d(TAG, "  response.getStatusLine()");
				if (status == 200) {
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("chefid", id));
					Log.d(TAG, "BasicNameValuePair = " + id);
					JSONObject json = jsonParser.makeHttpRequest(
							"http://momsdhaba.com/mobileapp/chefnotification/",
							"POST", params);
					Log.d(TAG, "Create Response" + json.toString());
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
						Log.i(TAG, "food = " + object.getString("food"));
						Log.i(TAG, "count = " + object.getString("count"));
						Log.i(TAG, "datetime = " + object.getString("datetime"));
						Log.i(TAG, "order_id = " + object.getString("order_id"));
						Log.i(TAG, "chef_status = " + object.getString("chef_status"));
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

			dialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					adapter.getCount();
					adapter.notifyDataSetChanged();
					Log.d(TAG, "******adapter.notifyDataSetChanged()*****");
					Log.d(TAG, "adapter.getCount()" + adapter.getCount());
				}
			});

			listview.setAdapter(adapter);

			String message = null;

			try {
				if (result.equals("orders_are_not_available")) {

					message = "Your orders basket is empty!";

				} else if (result.equals("false")) {
					message = "Unable to fetch data from server";

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (message != null) {
				Log.i(TAG, message);
				createAlertDialog(message).show();
			}

		}

		private Dialog createAlertDialog(String message) {
			AlertDialog.Builder builder = new Builder(NotificationActivity.this);
			AlertDialog dialog = builder.setMessage(message)
					.setCancelable(true).create();
			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			return dialog;
		}
	}
}