package com.momsdhaba.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.momsdhaba.JSONParser;
import com.momsdhaba.MainActivity;
import com.momsdhaba.Payment;
import com.momsdhaba.R;
import com.momsdhaba.ServiceHandler;

public class OrderActivity extends Activity {
	private final static String TAG = OrderActivity.class.getSimpleName();

	UserlistAdapter useradapter;
	String shamount, shcount, orderstatus;
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	StringBuilder builder_id, builder_count, builder_amount, builder_foodname,
			builder_ordertype;
	JSONObject jsonObj;
	JSONArray contacts;
	JSONArray jsonarray;
	private static final String ORDER_URL = "http://momsdhaba.com/mobileapp/order/";
	String address, phone, uname, addressid;
	ArrayList<String> foodid = new ArrayList<String>();
	ArrayList<Integer> foodcount = new ArrayList<Integer>();
	ArrayList<Float> foodamount = new ArrayList<Float>();
	ArrayList<String> foodname = new ArrayList<String>();
	ArrayList<String> foodordertype = new ArrayList<String>();

	ImageView order, takeaway;
	String prefix1 = "";
	String prefix2 = "";
	String prefix3 = "";
	String prefix4 = "";
	String prefix5 = "";
	String Amount, Foodid, Count, FoodName, Ordertype, message, title = "";

	EditText eUname, ePhone, eAddress;
	public TextView tvFname, tvAmount, tvCount, tvTotal, submit;
	Boolean checkaddress, terms = false;
	public static float amount = 0.0f;
	int countvalue, id;
	AlertDialog alertDialog;
	String userid;
	HashMap<String, Float> map_price;
	String idvalue, s_meals, s_id, o_meals, o_id;
	public static final String MyPREFERENCES = "Prefs";
	SharedPreferences sharedpref;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);

		sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		phone = sharedpref.getString("MOBILE", "");
		uname = sharedpref.getString("NAME", "");
		addressid = sharedpref.getString("ADDRESS", "");
		userid = sharedpref.getString("ID", "");

		Log.d(TAG, "sharedpreferences phone" + phone);
		Log.d(TAG, "sharedpreferences uname" + uname);
		Log.d(TAG, "sharedpreferences addressid" + addressid);
		Log.d(TAG, "sharedpreferences userid" + userid);

		ImageView i_back = (ImageView) findViewById(R.id.back);
		// submit =(TextView)findViewById(R.id.submit);

		map_price = useradapter.hashMap;
		Log.d(TAG, "useradapter.hashMap " + useradapter.hashMap);
		Log.d(TAG, "useradapter.hashMapcount " + useradapter.hashMapcount);

		for (String s : map_price.keySet()) {
			foodamount.add(map_price.get(s));
			// Log.e(TAG, ".... price amount .." + map_price);
		}

		try {
			HashMap<String, Integer> map_count = useradapter.hashMapcount;
			for (String s : map_count.keySet()) {
				foodcount.add(map_count.get(s));
				foodid.add(s);

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		// List remList = Arrays.asList(foodid);

		HashMap<String, String> map_food = useradapter.hashMapFood;
		for (String s : map_food.keySet()) {
			idvalue = map_food.get(s);
			foodname.add(map_food.get(s));
		}
		HashMap<String, String> map_ordertype = useradapter.hashMapordertype;
		for (String s : map_ordertype.keySet()) {

			foodordertype.add(map_ordertype.get(s));
			Log.e(TAG, ".... map_ordertype .." + map_ordertype);
		}

		amount = useradapter.Amounttotal;
		/*
		 * Intent intent = getIntent(); addressid =
		 * intent.getStringExtra("ADDESS"); uname =
		 * intent.getStringExtra("UNAME"); phone =
		 * intent.getStringExtra("PHONE"); userid = intent.getStringExtra("ID");
		 */
		Log.d(TAG, "Address " + addressid);
		Log.d(TAG, "UNAME " + uname);
		Log.d(TAG, "PHONE " + phone);

		Log.d(TAG, "Amounttotal " + amount);

		eUname = (EditText) findViewById(R.id.e_name);
		ePhone = (EditText) findViewById(R.id.e_phone);
		eAddress = (EditText) findViewById(R.id.e_address);
		tvFname = (TextView) findViewById(R.id.t_foodname);
		tvCount = (TextView) findViewById(R.id.t_count);
		tvAmount = (TextView) findViewById(R.id.t_amount);
		tvTotal = (TextView) findViewById(R.id.t_totalamount);
		address = eAddress.getText().toString().trim();
		eUname.setText(uname);
		ePhone.setText(phone);
		tvTotal.setText("" + amount);

		try {
			builder_id = new StringBuilder(); // Amount , Foodid ,Count;
			for (String s : foodid) {
				builder_id.append(prefix1);
				prefix1 = ",";
				builder_id.append(s);
			}
			Foodid = builder_id.toString();
			Log.d(TAG, " builder " + builder_id);

			builder_count = new StringBuilder();
			for (Integer s : foodcount) {
				builder_count.append(prefix2);
				prefix2 = ",";
				builder_count.append(s);
				builder_count.append("\n");
			}
			Log.d(TAG, " builder " + builder_count);
			Count = builder_count.toString();

			builder_amount = new StringBuilder();
			for (Float s : foodamount) {
				builder_amount.append(prefix3);
				prefix3 = ",";
				builder_amount.append(s);
				builder_amount.append("\n");

			}
			Log.d(TAG, " builder " + builder_amount);
			Amount = builder_amount.toString();

			builder_foodname = new StringBuilder();
			for (String s : foodname) {
				builder_foodname.append(prefix4);
				prefix3 = ",";
				builder_foodname.append(s);
				builder_foodname.append("\n");
			}
			Log.d(TAG, " builder " + builder_foodname);
			FoodName = builder_foodname.toString();
			// tvFname.setText(builder_foodname.toString());
			tvFname.setText(FoodName);
			tvAmount.setText(Amount);
			tvCount.setText(Count);

			SharedPreferences fPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor fEdit = fPrefs.edit();
			fEdit.putString("FoodName", FoodName);
			fEdit.putString("FoodCount", Count);
			fEdit.commit();

			builder_ordertype = new StringBuilder();
			for (String s : foodordertype) {
				builder_ordertype.append(prefix5);
				prefix5 = ",";
				builder_ordertype.append(s);
				builder_ordertype.append("\n");
			}
			Log.d(TAG, " builder " + builder_ordertype);
			Ordertype = builder_ordertype.toString();

			/*
			 * int Counttype = foodordertype.size(); int Countname =
			 * foodname.size(); int Countcount = foodcount.size(); int Countid =
			 * foodid.size();
			 * 
			 * Log.d("Counttype", ""+Counttype); Log.d("Countname",
			 * ""+Countname); Log.d("Countcount", ""+Countcount);
			 * Log.d("Countid", ""+Countid);
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		findViewById(R.id.checkBox1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				terms = true;
			}
		});
		findViewById(R.id.checkaddress).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						checkaddress = true;

						if (checkaddress == true) {
							eAddress.setText(addressid);
							address = eAddress.getText().toString().trim();
                                                        SharedPreferences aPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
							SharedPreferences.Editor aEdit = aPrefs.edit();
							aEdit.putString("DeliveryAddress", address);
							aEdit.commit();
						}
						// eAddress.setText(" ");

						Log.d(TAG, "address" + address);
					}
				});
		findViewById(R.id.submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				address = eAddress.getText().toString().trim();
				if (terms == true) {
					try {
						if (!address.isEmpty()) {
							Log.d(TAG, "Address" + address);
							if (amount == 0.0) {
								Log.d(TAG, "Amount" + amount);
								alertdialog();
								message = "Your orders basket is empty!";

							} else {							

									 new OrderProcess().execute();
							}

						} else {
							alertdialog();
							message = "Address Field is empty";
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					message = "Accept Terms and Conditions";
					alertdialog();

				}
			}
		});

		i_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(OrderActivity.this,
						CustomerActivity.class);
				startActivity(intent);
				finish();

			}
		});

	}

	class OrderProcess extends AsyncTask<String, String, String> {
		String result;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OrderActivity.this);
			pDialog.setMessage("Sending Food  Details...");
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
				params.add(new BasicNameValuePair("userid", userid));
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("address", address));
				params.add(new BasicNameValuePair("delivery_type", "DELIVERY")); // change
				params.add(new BasicNameValuePair("foodid", Foodid));
				params.add(new BasicNameValuePair("foodprice", Amount));
				params.add(new BasicNameValuePair("foodcount", Count));

				Log.d(TAG, "userid = " + userid);
				Log.d(TAG, "phone= " + phone);
				Log.d(TAG, "address = " + address);
				Log.d(TAG, "delivery_type = " + Ordertype);
				Log.d(TAG, "foodid = " + foodid);
				Log.d(TAG, "foodprice = " + Amount);
				Log.d(TAG, "foodcount = " + Count);

				// Amount , Foodid ,Count;
				JSONObject json = jsonParser.makeHttpRequest(ORDER_URL, "POST",
						params);
				// check log cat fro response
				Log.d(TAG, "Create Response" + json.toString());

				try {
					result = json.getString("status");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {

					JSONArray jarray = json.getJSONArray("successfull_orders");
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject object = jarray.getJSONObject(i);

						s_meals = object.getString("food_name");
						s_id = object.getString("food_id");
						Log.d(TAG, "successfull_orders " + s_meals);
						Log.d(TAG, "successfull_orders " + s_id);
					}
					JSONArray jarray1 = json.getJSONArray("out_of_stock");
					for (int i = 0; i < jarray1.length(); i++) {
						JSONObject object = jarray1.getJSONObject(i);
						o_meals = object.getString("food_name");
						o_id = object.getString("food_id");
						Log.d(TAG, "out_of_stock " + o_meals);
						Log.d(TAG, "out_of_stock " + o_id);

					}
					/*
					 * if (result.equals("success")) {
					 * 
					 * // Intent itab = new Intent(OrderActivity.this, //
					 * MainActivity.class); //
					 * itab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //
					 * startActivity(itab); // Log.d(TAG, "Send Data " +
					 * result);
					 * 
					 * } else if (result.equals("failed")) { Log.d(TAG,
					 * "failed"); // Toast("failed"); }
					 */
				} catch (Exception e) {
					e.printStackTrace();
					// Toast("e");
					alertdialog();
					message = "Unexpected error";
				}
			}

			catch (Exception e) {
				// TODO: handle exception
				// Toast("Unexpected error.");
				alertdialog();
				message = "Unexpected error";
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
			boolean sFlag = false;
			try {

				if (s_meals != null && o_meals == null) {
					/*
					 * message = "FoodName :" + FoodName + "\n" +
					 * "Toatl Amount :" + amount + "\n" +
					 * " Your Oder is Successfully completed";
					 */
					// message =
					// "Thankyou for place an order at MOMS DHABA"++"\n"+""+""+""+"";
					message = "Thankyou for place an order at MOMS DHABA We will deliveryour delicious "
							+ "food from your mom between 1.00pm to 2.00pm	"
							+ "Share your order with your friends on Facebook and earn a free meals"
							+ " once you reach 100 points";
					sFlag = true;
				} else if (s_meals != null && o_meals != null) {
					message = "Thankyou for place an order at MOMS DHABA"
							+ "\n" + "Successfull Orders : " + s_meals + ""
							+ "\n" + "OUT OF STOCK : " + o_meals + "";
					sFlag = false;
				} else if (s_meals == null && o_meals != null) {

					message = "Thankyou for place an order at MOMS DHABA"
							+ "\n" + "Your Order" + o_meals + "is OUT OF STOCK";
					sFlag = false;
				}

				if (result.equals("time_is_over")) {
					message = "Today's food ordering time is closed.." + "\n"
							+ " Please come to us by tomorrow at 10.00am. "
							+ "\n" + " Our closing time for Lunch is 11.30am";
					sFlag = false;
				} else {
					message = "Unexpected Error ";

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.i(TAG, message);
			try {
				if (message != null) {
					createAlertDialog(message,sFlag).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				useradapter.hashMap.clear();
				useradapter.hashMapcount.clear();
				useradapter.hashMapFood.clear();
				useradapter.hashMapordertype.clear();
				useradapter.hashMaptotal.clear();
				amount=0.0f;
				//useradapter.Amounttotal = 0.0f;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private Dialog createAlertDialog(String message, final boolean sFlag) {
			AlertDialog.Builder builder = new Builder(pDialog.getContext());
			AlertDialog dialog = builder.setMessage(message)
					.setCancelable(true).create();
			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if(sFlag){
						Intent itab = new Intent(OrderActivity.this,
								Payment.class);
						itab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(itab);
						OrderActivity.this.finish();
					}else{
						Intent itab = new Intent(OrderActivity.this,
								CustomerActivity.class);
						itab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(itab);
						OrderActivity.this.finish();
					}
				}
			});
			return dialog;
		}


	}

	public void alertdialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(OrderActivity.this)
						.create();
				alertDialog.setTitle(title);
				alertDialog.setCanceledOnTouchOutside(false);
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

	// public void Toast(final String message) {
	// runOnUiThread(new Runnable() {
	// public void run() {
	// // If there are stories, add them to the table
	// Toast.makeText(getApplicationContext(),
	// message,Toast.LENGTH_LONG).show();
	// }
	// });
	// }

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	// if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	// try {
	//
	// // showSettingsAlert();
	// AlertDialog.Builder altDialog = new AlertDialog.Builder(this);
	// altDialog.setMessage("Are you sure want to exit Application"); // here
	// // add
	// altDialog.setPositiveButton("Yes",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// // TODO Auto-generated method stub
	// // MainActivity.this.finish();
	// OrderActivity.this.finish();
	// }
	// });
	//
	// altDialog.setNegativeButton("No",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// // TODO Auto-generated method stub
	// dialog.cancel();
	// }
	// });
	// altDialog.show();
	//
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
	// return super.onKeyDown(keyCode, event);
	// }
}