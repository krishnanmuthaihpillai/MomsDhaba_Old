package com.momsdhaba;

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
import android.widget.TextView;

public class FoodList extends BaseActivity{
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private final static String TAG = FoodList.class.getSimpleName();
	String id, address;
	ArrayList<Chefdata> actorsList;
	ChefListAdapter adapter;
	JSONParser jsonParser = new JSONParser();
	public static final String MyPREFERENCES = "Prefs";
	SharedPreferences sharedpref;
	String l_cname, l_fname, l_ftype, l_fdescription, l_date, l_fprice, l_id,
			l_quantity ,l_profile;
	String[] arrayfood, arraycount;
	String Orderfood, Ordercount, result;
	TextView Ofood, Ocount;
	StringBuilder builder;
	ArrayList<String> ar;
	String prefix = "";
	StringBuilder builder1;
	ArrayList<String> ar1;
	String prefix1 = "";
	ListView listview;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_list);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		// strings.xml
		set(navMenuTitles, navMenuIcons);

		sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		id = sharedpref.getString("ID", "");
		address = sharedpref.getString("ADDESS", "");
		Log.d(TAG, "sharedpreferences" + id);
		 timer();
		actorsList = new ArrayList<Chefdata>();
		
		// new
		// JSONAsyncTask().execute("http://192.168.0.106:8000/mobileapp/chefviewfood/");
		// new Handler().postDelayed(new Runnable() {
		//
		// public void run() {
		//
		//new ProgressTask().execute("http://192.168.0.192:8080/mobileapp/chefviewfood/");
		// Log.d(TAG, "Handler" + id);
		// }
		// }, 2 * 1000);

		listview = (ListView) findViewById(R.id.cheflist);
		ar = new ArrayList<String>();
		ar1 = new ArrayList<String>();
		adapter = new ChefListAdapter(getApplicationContext(),
				R.layout.listietem, actorsList);

		// listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				adapter.notifyDataSetChanged();
				l_cname = actorsList.get(position).getName();
				l_fname = actorsList.get(position).getFoodName();
				l_ftype = actorsList.get(position).getFoodtype();
				l_fdescription = actorsList.get(position).getDescription();
				l_date = actorsList.get(position).getDate();
				l_fprice = actorsList.get(position).getPrice();
				l_id = actorsList.get(position).getFoodId();
				l_quantity = actorsList.get(position).getFoodQuantity();
			
				Log.d(TAG, "l_cname " + l_cname);
				Log.d(TAG, "l_fname " + l_fname);
				Log.d(TAG, "l_ftype " + l_ftype);
				Log.d(TAG, "l_fdescription " + l_fdescription);
				Log.d(TAG, "l_date " + l_date);
				Log.d(TAG, "l_fprice " + l_fprice);
				Log.d(TAG, "l_id " + l_id);
				Log.d(TAG, "l_quantity " + l_quantity);
				//Log.d(TAG, "l_profile " + l_profile);

				Intent intent = new Intent(FoodList.this, EditPage.class);
				intent.putExtra("Chef", l_cname);
				intent.putExtra("Food", l_fname);
				intent.putExtra("Type", l_ftype);
				intent.putExtra("Description", l_fdescription);
				intent.putExtra("Date", l_date);
				intent.putExtra("Price", l_fprice);
				intent.putExtra("Quantity", l_quantity);
				intent.putExtra("ID", l_id);
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
									.execute("http://momsdhaba.com/mobileapp/chefviewfood/");
						}

						Log.d(TAG, "timer");
					}
				});
			}
		};
		t.schedule(ttask, 0, 200000);
	}

	class ProgressTask extends AsyncTask<String, Void, Boolean> {

		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			adapter.clear();
			/*dialog = new ProgressDialog(ChefListview.this);
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
					params.add(new BasicNameValuePair("id", id));
					Log.d(TAG, "BasicNameValuePair = " + id);

					JSONObject json = jsonParser
							.makeHttpRequest(
									"http://momsdhaba.com/mobileapp/chefviewfood/",
									"POST", params);
					Log.d(TAG, "Create Response" + json.toString());
					try {
						result = json.getString("status");
						Log.d("Result: ", result);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// JSONObject jsono = new JSONObject(data);
					JSONArray jarray = json.getJSONArray("chef_food_today");

					for (int i = 0; i < jarray.length(); i++) {
						JSONObject object = jarray.getJSONObject(i);

						Chefdata actor = new Chefdata();

						actor.setName(object.getString("chef_name"));
						actor.setDescription(object.getString("description"));
						actor.setDate(object.getString("available_date"));
						actor.setFoodtype(object.getString("food_type"));
						actor.setPrice(object.getString("food_price"));
						actor.setFoodName(object.getString("food_name"));
						actor.setImage(object.getString("food_image"));						
						actor.setFoodQuantity(object.getString("food_quantity"));
						actor.setFoodId(object.getString("food_id"));
						//actorsList.clear();
						actorsList.add(actor);
						
						Log.d(TAG, "actorsList.add");
					}
					
					//actorsList.clear();
					//Log.d(TAG, "actorsList.clear");

					//actorsList.add(actor);
					

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
		//	dialog.dismiss();
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

				}else if (result.equals("no_foods")) {
					message = "Foodmenu is empty";

				}
				
				else if (result.equals("false")) {
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
			AlertDialog.Builder builder = new Builder(dialog.getContext());
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