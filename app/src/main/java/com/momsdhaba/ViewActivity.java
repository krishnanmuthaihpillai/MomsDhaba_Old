package com.momsdhaba;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.momsdhaba.user.CustomerActivity;
import com.momsdhaba.user.OrderActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewActivity extends Activity implements OnClickListener {
	private final static String TAG = ViewActivity.class.getSimpleName();
	String type, name, description, price, days, id, imgid, numofitems,
			chefname;
	TextView txtname, txttype, txtdescription, txtprice, txtdays, txtquantity,
			txtchefname;
	ImageView img;
	TextView send, Edit, Cancel;
	private static final String SEND_URL = "http://momsdhaba.com/mobileapp/chefaddfood/";
	String prefix = "";
	String[] availabledays, selecteddays;
	SimpleDateFormat format;
	// Calendar localCalendar;
	// Date currentTime;
	// int currentDay;
	int currentDayOfWeek;
	// int currentDayOfMonth;
	Calendar calendar;
	int weekNo;
	ArrayList<String> ar = new ArrayList<String>();
	ArrayList<String> ar2 = new ArrayList<String>();
	String[] array;
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	StringBuilder builder;
	JSONObject jsonObj;
	JSONArray contacts;
	int i;
	private static final int PICK_IMAGE = 1;
	private static final int PICK_Camera_IMAGE = 2;
	String jsonStr;
	// private Button upload, cancel;
	ImageButton gallery, camera;
	private ProgressDialog dialog;
	Uri imageUri;
	private int serverResponseCode = 0;
	private String upLoadServerUri = null;
	MediaPlayer mp = new MediaPlayer();
	private String imagepath = null;
	private ImageView imgView;
	Boolean imageoption = false;
	String[] temp;
	String fName;
	String message, title;
	AlertDialog alertDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view2);
		ImageView i_back = (ImageView) findViewById(R.id.btn_back);
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
		format = new SimpleDateFormat("yyyy-MM-dd");
		calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		weekNo = calendar.get(Calendar.WEEK_OF_MONTH);
		upLoadServerUri = "http://momsdhaba.com/mobileapp/uploadfile/";

		// upload = (Button) findViewById(R.id.imguploadbtn);
		// cancel = (Button) findViewById(R.id.imgcancelbtn);
		gallery = (ImageButton) findViewById(R.id.gallery);
		camera = (ImageButton) findViewById(R.id.camera);
		gallery.setOnClickListener(this);
		camera.setOnClickListener(this);
		i_back.setOnClickListener(this);
		Intent intent = getIntent();

		txtname = (TextView) findViewById(R.id.itemname);
		txttype = (TextView) findViewById(R.id.itemtype);
		txtdescription = (TextView) findViewById(R.id.description);
		txtprice = (TextView) findViewById(R.id.itemprice);
		txtdays = (TextView) findViewById(R.id.availabledays);
		//txtchefname = (TextView) findViewById(R.id.chefname);
		txtquantity = (TextView) findViewById(R.id.numberofitem);

		img = (ImageView) findViewById(R.id.foodimage);
		send = (TextView) findViewById(R.id.send_button);
		Edit = (TextView) findViewById(R.id.edit_button);
		Cancel = (TextView) findViewById(R.id.cancel_button);

		send.setOnClickListener(this);
		Edit.setOnClickListener(this);
		Cancel.setOnClickListener(this);

		type = intent.getStringExtra("FOODTYPE");
		name = intent.getStringExtra("FOODNAME");
		description = intent.getStringExtra("DISCRIPTION");
		price = intent.getStringExtra("PRICE");
		days = intent.getStringExtra("DAYS");
		id = intent.getStringExtra("ID");
		imgid = intent.getStringExtra("IMAGEID");
		numofitems = intent.getStringExtra("NUMBER");
		chefname = intent.getStringExtra("CHEFNAME");
		Log.d(TAG, "ID " + id);
		Log.d(TAG, "chefname " + chefname);
		Log.d(TAG, "numofitems " + numofitems);
		Log.d(TAG, "imgid " + imgid);
		Log.d(TAG, "name " + name);

		txtname.setText(name);
		txttype.setText("Food type : "+ type);
		txtdescription.setText(description);
		txtprice.setText("₹ :"+price +" (inclusive pacakage charges)");
		txtdays.setText(days);
		txtquantity.setText(numofitems);
		//txtchefname.setText(chefname);

		if(imgid.equals("Others")){
			message = "Please Upload Image for your "+ name +"";
			alertdialog();
		}
		
		if (name.equals("Meals"))
			img.setImageDrawable(getResources().getDrawable(R.drawable.meals));
		if (name.equals("ChickenBiryani"))
			img.setImageDrawable(getResources().getDrawable(
					R.drawable.chickenbiryani));
		if (name.equals("CurdRice"))
			img.setImageDrawable(getResources()
					.getDrawable(R.drawable.curdrice));
		if (name.equals("LemonRice"))
			img.setImageDrawable(getResources().getDrawable(
					R.drawable.lemonrice1));
		if (name.equals("TomatoRice"))
			img.setImageDrawable(getResources().getDrawable(
					R.drawable.tomatorice));
		if (name.equals("SambarRice"))
			img.setImageDrawable(getResources().getDrawable(
					R.drawable.sambarrice1));
				
		if (days.equals("All")) {
			Alldays();
		} else {
			selectdays();
		}
	}

	private void Alldays() {
		// TODO Auto-generated method stub
		Log.d("week of month =", "" + weekNo);
		Log.d("currentDayOfWeek =", "" + currentDayOfWeek);
		availabledays = new String[7];
		for (i = 0; i < 7; i++) {

			availabledays[i] = format.format(calendar.getTime());
			Log.d(TAG, " Days : " + availabledays[i]);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			if (i >= currentDayOfWeek - 2) {
				Log.i(TAG, "Selected Days : " + availabledays[i]);
				ar.add(availabledays[i]);
			}
			// newString = String.valueOf(ar);
		}
		builder = new StringBuilder();
		for (String s : ar) {
			// builder.append(""+s+""+n);
			builder.append(prefix);
			prefix = ",";
			builder.append(s);
		}
		Log.d(TAG, " builder " + builder);
	}

	private void selectdays() {
		// TODO Auto-generated method stub

		availabledays = new String[7];
		for (i = 0; i < 7; i++) {

			availabledays[i] = format.format(calendar.getTime());
			Log.i(TAG, "Week Days : " + availabledays[i]);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			if (i >= currentDayOfWeek - 2) {
				Log.i(TAG, "Selected Days : " + availabledays[i]);
				ar.add(availabledays[i]);
			}
		}

		Log.i(TAG, "Days : " + days);
		String[] parts = days.split(",");
		System.out.println("Using , as a delimiter " + Arrays.toString(parts));
		if (Arrays.asList(parts).contains("Monday")
				&& (ar.contains(availabledays[0]))) {
			ar2.add(availabledays[0]);
		}
		if (Arrays.asList(parts).contains("Tuesday")
				&& (ar.contains(availabledays[1]))) {
			ar2.add(availabledays[1]);
		}
		if (Arrays.asList(parts).contains("Wednesday")
				&& (ar.contains(availabledays[2]))) {
			ar2.add(availabledays[2]);
		}
		if (Arrays.asList(parts).contains("Thursday")
				&& (ar.contains(availabledays[3]))) {
			ar2.add(availabledays[3]);
		}
		if (Arrays.asList(parts).contains("Friday")
				&& (ar.contains(availabledays[4]))) {
			ar2.add(availabledays[4]);
		}
		if (Arrays.asList(parts).contains("Saturday")
				&& (ar.contains(availabledays[5]))) {
			ar2.add(availabledays[5]);
		}
		if (Arrays.asList(parts).contains("Sunday")
				&& (ar.contains(availabledays[6]))) {
			ar2.add(availabledays[6]);
		}
		Log.d(TAG, " Array " + ar2);

		builder = new StringBuilder();
		for (String s : ar2) {
			// builder.append(""+s+""+n);
			builder.append(prefix);
			prefix = ",";
			builder.append(s);
		}
		Log.d(TAG, " builder " + builder);
		//
		// if (builder.equals("days")) {
		// Log.d(TAG, "check days");
		// } else {
		// Log.d(TAG, "contain vales");
		// }
		// Log.i(TAG, "Send Days array " + ar);
		// newString = String.valueOf(ar);
		// for(int i=0;i<ar.size();i++){
		// newString = ar.get(i);
		// }

		// builder = new StringBuilder();
		// for (String s : ar) {
		// // builder.append(""+s+""+n);
		// builder.append(prefix);
		// prefix = ",";
		// builder.append(s);
		// }
		// Log.d(TAG, " builder " + builder);
		// newString = String.valueOf(ar);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.send_button:
			// if (imagepath == null) {
			// // Toast.makeText(getApplicationContext(),
			// // "please select image", Toast.LENGTH_SHORT).show();
			// } else {
			// dialog = ProgressDialog.show(ViewActivity.this,
			// "Uploading", "Please wait...", true);
			if (imageoption) {
				new Thread(new Runnable() {
					public void run() {
						uploadFile(imagepath);
					}
				}).start();
			}

			new SendDetails().execute();

			break;
		case R.id.gallery:
			try {
				imageoption=true;
				Log.d(TAG, "imageoption gallery"+imageoption);
				// Intent gintent = new Intent();
				// gintent.setType("image/*");
				// gintent.setAction(Intent.ACTION_GET_CONTENT);
				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(
						Intent.createChooser(galleryIntent, "Select Picture"),
						PICK_IMAGE);
			} catch (Exception e) {

				// message = e.getMessage(); //
				// alertdialog(); //
				// Toast.makeText(getApplicationContext(),
				// e.getMessage(),Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}

			break;

		case R.id.camera:
			imageoption=true;
			Log.d(TAG, "imageoption camera"+imageoption);
			String fileName = "new-photo-name.jpg";
			// create parameters for Intent with filename
			ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.TITLE, fileName);
			values.put(MediaStore.Images.Media.DESCRIPTION,
					"Image captured by camera");
			// imageUri is the current activity attribute, define and save it
			// for later usage (also in onSaveInstanceState)
			imageUri = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			// create new Intent
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, PICK_Camera_IMAGE);
			break;

		case R.id.cancel_button:
			Intent i_cancel = new Intent(ViewActivity.this,
					AddMenu.class);
			i_cancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i_cancel);
			break;
		case R.id.edit_button:
			super.onBackPressed();
			break;
			
		case R.id.btn_back:
			Intent i = new Intent(ViewActivity.this,
					AddMenu.class);
			startActivity(i);
			finish();
			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri selectedImageUri = null;
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				selectedImageUri = data.getData();
			}
			break;
		case PICK_Camera_IMAGE:
			if (resultCode == RESULT_OK) {
				// use imageUri here to access the image
				selectedImageUri = imageUri;
			} else if (resultCode == RESULT_CANCELED) {
				/*Toast.makeText(this, "Picture was not taken",
						Toast.LENGTH_SHORT).show();*/
			} else {
				/*Toast.makeText(this, "Picture was not taken",
						Toast.LENGTH_SHORT).show();*/
			}
			break;
		}
		if (selectedImageUri != null) {
			
			try {
				imagepath = getPath(selectedImageUri);
				Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
				img.setImageBitmap(bitmap);
				// int w = imgView.getWidth();
				// int h = imgView.getHeight();
				// Log.e("img width and height", "width" + w + "height" + h);
				// Log.i("Uploading file path", "Uploading file path:" + imagepath);
				temp = imagepath.split("/");
				fName = temp[temp.length - 1];
				Log.i("Uploading file path", "Uploading file path:" + fName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	public int uploadFile(String sourceFileUri) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = null;
		try {
			sourceFile = new File(sourceFileUri);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!sourceFile.isFile()) {
			dialog.dismiss();
			Log.e("uploadFile", "Source File not exist :" + imagepath);
			return 0;
		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("food_image_by_chef", fileName);

				conn.connect();
				dos = new DataOutputStream(conn.getOutputStream());

				// image upload
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"food_image_by_chef\";filename=\""
						+ fileName + "\"" + lineEnd);
				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						public void run() {
							// new Chefregister().execute();
							// Toast.makeText(ChefRegistraion.this,
							// "File Upload Complete.", Toast.LENGTH_SHORT)
							// .show();
						}
					});

				}
				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (final MalformedURLException ex) {
				dialog.dismiss();
				ex.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(ViewActivity.this,
								"MalformedURLException", Toast.LENGTH_SHORT)
								.show();
						Log.e("Upload file to server",
								"error: " + ex.getMessage(), ex);
					}
				});
			} catch (final Exception e) {
				dialog.dismiss();
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(ViewActivity.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
						Log.e("Upload file to server Exception", "Exception : "
								+ e.getMessage(), e);
					}
				});
			}
		}
		try {
			dialog.dismiss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverResponseCode;

	}

	class SendDetails extends AsyncTask<String, String, String> {
		String result;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ViewActivity.this);
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
				Log.d(TAG, "imageoption onPreExecute"+imageoption);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("food_name", name));
				params.add(new BasicNameValuePair("food_type", type));
				params.add(new BasicNameValuePair("food_price", price));				
				params.add(new BasicNameValuePair("description", description));
				params.add(new BasicNameValuePair("available_date", ""+ builder));
				params.add(new BasicNameValuePair("id", id));
				params.add(new BasicNameValuePair("food_quantity", numofitems));
				
				if(imageoption==false){
					Log.d(TAG, "imageoption doInBackground"+imageoption);
					Log.d(TAG, "Image id  = " + imgid);
					params.add(new BasicNameValuePair("food_image", imgid));
				}else{
					Log.d(TAG, "Image id  = " + fName);
					Log.d(TAG, "imageoption doInBackground else ="+imageoption);
					params.add(new BasicNameValuePair("food_image_by_chef", fName));
				}

				Log.d(TAG, "ID = " + id);
				Log.d(TAG, "name = " + name);
				Log.d(TAG, "type = " + type);
				Log.d(TAG, "price = " + price);
				Log.d(TAG, "description = " + description);
				Log.d(TAG, "days = " + builder);
				
				Log.d(TAG, "food_quantity  = " + numofitems);

				JSONObject json = jsonParser.makeHttpRequest(SEND_URL, "POST",params);
				// check log cat fro response
				Log.d(TAG, "Create Response" + json.toString());
				// JSONObject obj=data.getJSONObject(i);
				result = json.getString("status");
				Log.d("Result: ", result);

				// check for success tag
				try {

					if (result.equals("success")) {

						Log.d(TAG, "Send Data " + result);

						Intent i = new Intent(ViewActivity.this,
								FoodList.class);
						i.putExtra("ID", id);
						finish();
						startActivity(i);

					} else if (result.equals("days_are_zero")) {

						Log.d(TAG, "your elected date is expired");
						// Toast("Please check selected dates");

					}

					else if (result.equals("UserDoesNotExist")) {
						// Toast("UserDoesNotExist");
						Log.d(TAG, "UserDoesNotExist");

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
					if (result.equals("days_are_zero")) {
						message = " Your seleced expired date";
					} else if (result.equals("UserDoesNotExist")) {

						message = "UserDoesNotExist";
					} else if (result.equals("time_is_over")) {

						message = "Time is Over";
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
	public void alertdialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(ViewActivity.this)
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
	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				// Toast.makeText(getApplicationContext(), message,
				// Toast.LENGTH_LONG).show();
			}
		});
	}
}