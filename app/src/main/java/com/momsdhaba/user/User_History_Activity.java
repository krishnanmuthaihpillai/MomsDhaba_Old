package com.momsdhaba.user;

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

import com.momsdhaba.JSONParser;
import com.momsdhaba.R;
import com.momsdhaba.notification.NotificationActivity;
import com.momsdhaba.notification.NotificationOptions;

public class User_History_Activity extends BaseActivity2 {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private final static String TAG = User_History_Activity.class
			.getSimpleName();
	ArrayList<User_History_Data> uhistoryList;
	JSONParser jsonParser = new JSONParser();
	ProgressDialog dialog;
	UserHistoryAdapter adapter;
	ListView listview;
	String result, id;
	public static final String MyPREFERENCES = "Prefs";
	SharedPreferences sharedpref;
	String l_fname, l_fcount, l_amount, l_date, l_orderid, l_foodid,
			l_orderitemid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user__history);

		navMenuTitles = getResources().getStringArray(
				R.array.nav_drawer_items_user); // load
		navMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons_user);// load icons from
		// strings.xml
		set(navMenuTitles, navMenuIcons);

		timer();

		sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		id = sharedpref.getString("ID", "");
		Log.d(TAG, "sharedpreferences id" + id);

		uhistoryList = new ArrayList<User_History_Data>();
		listview = (ListView) findViewById(R.id.user_historylist);
		adapter = new UserHistoryAdapter(getApplicationContext(),
				R.layout.history_items_user, uhistoryList);

		// new
		// ProgressTask().execute("http://192.168.0.192:8080/mobileapp/customerhistory/");

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				adapter.notifyDataSetChanged();
				l_fname = uhistoryList.get(position).getFoodName();
				l_fcount = uhistoryList.get(position).getFoodcount();
				l_amount = uhistoryList.get(position).getTotal();
				l_date = uhistoryList.get(position).getDate();
				l_orderid = uhistoryList.get(position).getOrderid();
				l_foodid = uhistoryList.get(position).getFoodid();
				l_orderitemid = uhistoryList.get(position).getOrderItemid();

				Log.d(TAG, "l_fname " + l_fname);
				Log.d(TAG, "l_fcount " + l_fcount);
				Log.d(TAG, "l_amount " + l_amount);
				Log.d(TAG, "l_date " + l_date);
				Log.d(TAG, "l_orderid " + l_orderid);
				Log.d(TAG, "l_foodid " + l_foodid);
				Log.d(TAG, "l_orderitemid" + l_orderitemid);

				Intent intent = new Intent(User_History_Activity.this,
						User_History_options.class);
				intent.putExtra("FoodName", l_fname);
				intent.putExtra("Count", l_fcount);
				intent.putExtra("Amount", l_amount);
				intent.putExtra("Date", l_date);
				intent.putExtra("OrderId", l_orderid);
				intent.putExtra("OrderItemId", l_orderitemid);
				intent.putExtra("FoodId", l_foodid);
				startActivity(intent);
			}
		});
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
							new ProgressTask()
									.execute("http://momsdhaba.com/mobileapp/customerhistory/");
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
			adapter.clear();
			/*
			 * dialog = new ProgressDialog(User_History_Activity.this);
			 * dialog.setMessage("Loading, please wait");
			 * dialog.setTitle("Connecting server"); dialog.show();
			 * dialog.setCancelable(false);
			 */
		}

		@Override
		protected Boolean doInBackground(String... urls) {
			try {

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
					params.add(new BasicNameValuePair("userid", id));
					Log.d(TAG, "BasicNameValuePair = " + id);

					JSONObject json = jsonParser.makeHttpRequest(
							"http://momsdhaba.com/mobileapp/customerhistory/",
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

						User_History_Data user = new User_History_Data();

						user.setFoodName(object.getString("food"));
						user.setFoodcount(object.getString("count"));
						user.setDate(object.getString("datetime"));
						user.setTotal(object.getString("price"));
						user.setFoodid(object.getString("food_id"));
						user.setOrderid(object.getString("order_id"));
						user.setOrderItemid(object.getString("order_item_id"));
						Log.i(TAG, "food = " + object.getString("food"));
						Log.i(TAG, "count = " + object.getString("count"));
						Log.i(TAG, "datetime = " + object.getString("datetime"));
						Log.i(TAG, "food_id = " + object.getString("food_id"));
						Log.i(TAG, "order_id = " + object.getString("order_id"));
						Log.i(TAG,
								"order_item_id = "
										+ object.getString("order_item_id"));
						uhistoryList.add(user);
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
			runOnUiThread(new Runnable() {
				public void run() {
					adapter.notifyDataSetChanged();
				}
			});
			listview.setAdapter(adapter);
			String message = null;

			try {
				if (result.equals("menu_not_available")) {

					message = "Today 's Menu Not Available";

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
			AlertDialog.Builder builder = new Builder(
					User_History_Activity.this);
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