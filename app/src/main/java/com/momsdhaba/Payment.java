package com.momsdhaba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.momsdhaba.user.CheckoutFragment;
import com.momsdhaba.user.CustomerActivity;
import com.momsdhaba.user.OrderActivity;
import com.momsdhaba.user.UserlistAdapter;

import org.json.JSONObject;

public class Payment extends BaseActivity {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	UserlistAdapter useradapter;
	final String public_key = "rzp_live_7RpwkHKfTXUsef";
	String cName, cPhone, cEmail, foodName, delAddress, foodCount;
	public static final String MyPREFERENCES = "Prefs";
	SharedPreferences sharedpref;
	OrderActivity orderactivity;
	float tAmount = 0.0f;
	int totAmount;
	TextView dName, dPhone, dFood, dAmount, dAddress, dCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);

		Button button = (Button) findViewById(R.id.pay_btn);
		dName = (TextView) findViewById(R.id.textView10);
		dPhone = (TextView) findViewById(R.id.textView11);
		dFood = (TextView) findViewById(R.id.textView12);
		dAmount = (TextView) findViewById(R.id.textView13);
		dAddress = (TextView) findViewById(R.id.textView14);
		dCount = (TextView) findViewById(R.id.textView16);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);// load icons from
															// strings.xml
		set(navMenuTitles, navMenuIcons);
		final Activity activity = this;
		final CheckoutFragment co = new CheckoutFragment();
		co.setPublicKey(public_key);

		sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		cName = sharedpref.getString("NAME", "");
		Log.d("Payment_NAME",cName);
		cPhone = sharedpref.getString("MOBILE"," ");
		Log.d("Payment_PHONE",cPhone);
		cEmail = sharedpref.getString("Email","");
		Log.d("Payment_EMAIL", cEmail);
		SharedPreferences fPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		foodName = fPrefs.getString("FoodName",null);
		delAddress = fPrefs.getString("DeliveryAddress",null);
		foodCount = fPrefs.getString("FoodCount", null);


		tAmount = (float) (useradapter.Amounttotal *100.0);
		Log.d("Payment_AMT_DOUBLE",""+tAmount);
		totAmount = (int)tAmount;
		Log.d("Payment_AMT_STRING",""+totAmount);

		dName.setText(cName);
		dPhone.setText(cPhone);
		dAmount.setText(String.valueOf(useradapter.Amounttotal));
		dFood.setText(foodName);
		dCount.setText(foodCount);
		dAddress.setText(delAddress);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					JSONObject options = new JSONObject("{" +
							"description: 'Credits towards Momsdhaba'," +
							"image: 'https://i.imgur.com/3g7nmJC.png'," +
							"currency: 'INR'}"
					);
					options.put("key", public_key);
					options.put("amount", totAmount);
					options.put("name", "Momsdhaba");
					options.put("prefill", new JSONObject("{"+
							"email:" +cEmail +","+
							"contact: "+cPhone +","+
							"name: "+cName+"}"));

					co.open(activity, options);
					useradapter.Amounttotal = 0.0f;
				} catch(Exception e){
					Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent intent = new Intent(Payment.this, CustomerActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		Payment.this.finish();
	}
}
