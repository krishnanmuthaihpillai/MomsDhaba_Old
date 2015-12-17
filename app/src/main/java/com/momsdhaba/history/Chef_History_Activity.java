package com.momsdhaba.history;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class Chef_History_Activity extends BaseActivity {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private final static String TAG = Chef_History_Activity.class.getSimpleName();
	ArrayList<Chef_History_Data> uhistoryList;
	JSONParser jsonParser = new JSONParser();
	ProgressDialog dialog;
	ChefAdapter adapter;
	ListView listview;
	String Id ,result, id,Url = "http://momsdhaba.com/mobileapp/chefhistory/";
	public static final String MyPREFERENCES = "Prefs";
	SharedPreferences sharedpref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chef_history_);
		
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		set(navMenuTitles, navMenuIcons);
		sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		
		 Id = sharedpref.getString("ID", "");
		 Log.d(TAG, "sharedpreferences id" + Id);
		
		uhistoryList = new ArrayList<Chef_History_Data>();
		listview = (ListView) findViewById(R.id.chef_historylist);
		adapter = new ChefAdapter(getApplicationContext(),
				R.layout.history_items_chef, uhistoryList);
		 timer();
		//new ProgressTask().execute(Url);

		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				// String l_cname = actorsList.get(position).getName();

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
							 new ProgressTask().execute(Url);
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
			/*dialog = new ProgressDialog(Chef_History_Activity.this);
			dialog.setMessage("Loading, please wait");
			dialog.setTitle("Connecting server");
			dialog.show();
			dialog.setCancelable(false);*/
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
					params.add(new BasicNameValuePair("chefid", Id));
					Log.d(TAG, "BasicNameValuePair = " + id);

					JSONObject json = jsonParser.makeHttpRequest(Url, "POST", params);
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
						Chef_History_Data user = new Chef_History_Data();

						user.setFoodName(object.getString("food"));
						user.setFoodcount(object.getString("count"));
						user.setDate(object.getString("datetime"));
						// user.setTotal(object.getString("price"));
						Log.i(TAG, "status = " + object.getString("food"));
						Log.i(TAG, "status = " + object.getString("count"));
						Log.i(TAG, "status = " + object.getString("datetime"));
						// Log.i(TAG, "status = " +object.getString("price"));
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
			//dialog.dismiss();
			runOnUiThread(new Runnable() {
	            public void run() {
	            	adapter.notifyDataSetChanged();
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
			AlertDialog.Builder builder = new Builder(Chef_History_Activity.this);
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
