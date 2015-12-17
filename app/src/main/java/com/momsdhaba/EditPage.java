package com.momsdhaba;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditPage extends Activity {
	private final static String TAG = EditPage.class.getSimpleName();
	private static final String SEND_URL = "http://momsdhaba.com/mobileapp/chefeditfood/";
	String Cname, Fname, Ftype, Date, Description, Id, Price, Quantity,
			Process;
	EditText eCname, eFname, eFtype, eDate, eDescription, ePrice, eQuantity;
	Button Delete, Update, Cancel;

	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	JSONObject jsonObj;
	JSONArray contacts;
	private RadioGroup radioFoodGroup;
	private RadioButton radioFoodButton;
	int selectedId;
	String message, title,chefname;
	AlertDialog alertDialog;
	 public static final String MyPREFERENCES = "MyPrefs";
	 SharedPreferences sharedpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_page);
		sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		Id = sharedpref.getString("ID", "");
		chefname = sharedpref.getString("NAME", "");
		
        ImageView i_back=(ImageView)findViewById(R.id.back);
		radioFoodGroup = (RadioGroup) findViewById(R.id.radioFood);

		eCname = (EditText) findViewById(R.id.editchef);
		eFname = (EditText) findViewById(R.id.editfood);
		eFtype = (EditText) findViewById(R.id.edittype);
		eDate = (EditText) findViewById(R.id.editdate);
		eDescription = (EditText) findViewById(R.id.editdescription);
		ePrice = (EditText) findViewById(R.id.editprice);
		eQuantity = (EditText) findViewById(R.id.editquantity);

		// Id = sharedpref.getString("ID", "");
		// Log.d(TAG, "sharedpreferences ???" + Id);

		Intent intent = getIntent();
		Cname = intent.getStringExtra("Chef");
		Fname = intent.getStringExtra("Food");
		Ftype = intent.getStringExtra("Type");
		Date = intent.getStringExtra("Date");
		Description = intent.getStringExtra("Description");
		Price = intent.getStringExtra("Price");
		Id = intent.getStringExtra("ID");
		Quantity = intent.getStringExtra("Quantity");

		eCname.setText(Cname);
		eFname.setText(Fname);
		eFtype.setText(Ftype);
		eDate.setText(Date);
		eDescription.setText(Description);
		ePrice.setText(Price);
		eQuantity.setText(Quantity);

		Log.d(TAG, "Cname" + Cname);
		Log.d(TAG, "Fname" + Fname);
		Log.d(TAG, "Ftype" + Ftype);
		Log.d(TAG, "Date" + Date);
		Log.d(TAG, "Description" + Description);
		Log.d(TAG, "Price" + Price);
		Log.d(TAG, "Id" + Id);
		Log.d(TAG, "Quantity" + Quantity);

		findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i_cancel = new Intent(EditPage.this, FoodList.class);
				i_cancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i_cancel);
			}

		});

		findViewById(R.id.btn_delete).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SendData();
				Process = "delete";

				new UpDateprocess().execute();
			}

		});

		findViewById(R.id.btn_update).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SendData();
				Process = "update";

				new UpDateprocess().execute();
			}
		});
		i_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditPage.this, FoodList.class);
				startActivity(intent);
				finish(); 
				
			}
		});
		
		
	}

	public void SendData() {
		// TODO Auto-generated method stub
		Cname = eCname.getText().toString().trim();
		Fname = eFname.getText().toString().trim();
		Date = eDate.getText().toString().trim();
		Description = eDescription.getText().toString().trim();
		Price = ePrice.getText().toString().trim();
		Quantity = eQuantity.getText().toString().trim();
		Ftype = eFtype.getText().toString().trim();
		eFtype.setText(Ftype);

		selectedId = radioFoodGroup.getCheckedRadioButtonId();
		// Toast.makeText(EditPage.this,radioFoodButton.getText(),
		// Toast.LENGTH_SHORT).show();
		radioFoodButton = (RadioButton) findViewById(selectedId);

		try {
			Ftype = radioFoodButton.getText().toString().trim();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Ftype = Ftype;
	}

	class UpDateprocess extends AsyncTask<String, String, String> {
		String result;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditPage.this);
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
				params.add(new BasicNameValuePair("food_name", Fname));
				params.add(new BasicNameValuePair("food_type", Ftype));
				params.add(new BasicNameValuePair("food_price", Price));
				params.add(new BasicNameValuePair("description", Description));
				params.add(new BasicNameValuePair("food_id", Id));
				params.add(new BasicNameValuePair("available_date", Date));
				params.add(new BasicNameValuePair("food_quantity", Quantity));
				params.add(new BasicNameValuePair("operation", Process));

				Log.d(TAG, "Cname =" + Cname);
				Log.d(TAG, "Fname =" + Fname);
				Log.d(TAG, "Ftype =" + Ftype);
				Log.d(TAG, "Date =" + Date);
				Log.d(TAG, "Description =" + Description);
				Log.d(TAG, "Price =" + Price);
				Log.d(TAG, "Id =" + Id);
				Log.d(TAG, "Quantity =" + Quantity);
				Log.d(TAG, "Process =" + Process);

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
						finish();

						Intent i = new Intent(EditPage.this, FoodList.class);
						// i.putExtra("ID", id);
						finish();
						startActivity(i);

					} else if (result.equals("successfully_deleted")) {

						Log.d(TAG, "successfully_deleted");
						finish();

					} else if (result.equals("invalid_food_object")) {

						Log.d(TAG, "Please check ");
						//Toast("Please check ");
						message = "Please check";
						alertdialog();
					}

					else if (result.equals("invalid_food_available_object")) {
						//Toast("invalid_food_available_object");
						Log.d(TAG, "invalid_food_available_object");
						message = "invalid_food_available_object";
						alertdialog();

					} else if (result.equals("failed")) {
						Log.d(TAG, "failed");
					//	Toast("failed");
						message = "failed";
						alertdialog();
					}
				} catch (Exception e) {
					e.printStackTrace();
					//Toast("e");
					message = "Error";
					alertdialog();
				}

			}

			catch (Exception e) {
				// TODO: handle exception
				//Toast("Unexpected error.");
				message = "Unexpected error";
				alertdialog();
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
					if (result.equals("successfully_deleted")) {
						message = "Successfully Deleted";
					} else if (result.equals("invalid_food_object")) {
						message = "Please check The Details";
					} else if (result.equals("invalid_food_available_object")) {
						message = "invalid_food_available_object";
					} else if (result.equals("failed")) {
						message = "Failed";
					} else {
						message = "Unexpected Error ";
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.i(TAG, message);
			createAlertDialog(message).show();
		}

		private Dialog createAlertDialog(String message) {
			AlertDialog.Builder builder = new Builder(pDialog.getContext());
			AlertDialog dialog = builder.setMessage(message)
					.setCancelable(true).create();
			dialog.setButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
						}
					});
			return dialog;
		}

	}

	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
//				Toast.makeText(getApplicationContext(), message,
//						Toast.LENGTH_LONG).show();
			}
		});
	}
	public void alertdialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(EditPage.this)
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