package com.momsdhaba;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ChefRegistraion extends Activity implements OnClickListener {
	private final static String TAG = ChefRegistraion.class.getSimpleName();
	private static final String REGIST_URL = "http://momsdhaba.com/mobileapp/chefregister/";
	private ProgressDialog pDialog;
	Button Btn_register;
	EditText edit_username, edit_mobilenumber, edit_emailid, edit_password,
			edit_repassword, edit_location, edit_accnumber, edit_accname,
			edit_bankname, edit_branch, edit_ifsc;

	String uname, mobile, email, password, retypepassword, location,
			accountname, accountnumber, bankname, branchname, ifsccode ,strLattitude ,strLongitude ,result ,message ,title;
	JSONParser jsonParser = new JSONParser();
	AppLocationService appLocationService;
	Location nwLocation, gpsLocation;
	double lattitude, longitude;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	LocationManager lm;
	JSONObject jsonObj;
	JSONArray jsonarray;
	AlertDialog alertDialog;
	private static final int PICK_IMAGE = 1;
	private static final int PICK_Camera_IMAGE = 2;
	String jsonStr;
//	private Button upload, cancel;
	ImageButton gallery, camera;
	private ProgressDialog dialog;
	Uri imageUri;
	private int serverResponseCode = 0;
	private String upLoadServerUri = null;
	MediaPlayer mp = new MediaPlayer();
	private String imagepath = null;
	private ImageView imgView;
	
	String[] temp;
	String fName;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chef_registration);
		upLoadServerUri = "http://momsdhaba.com/mobileapp/uploadfile/";
		imgView = (ImageView) findViewById(R.id.imageView1);
		// upload = (Button) findViewById(R.id.imguploadbtn);
		// cancel = (Button) findViewById(R.id.imgcancelbtn);
		gallery = (ImageButton) findViewById(R.id.gallery_btn);
		camera = (ImageButton) findViewById(R.id.camera_btn);
		gallery.setOnClickListener(this);
		camera.setOnClickListener(this);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// imgepath =imagegallery.imagepath;
		// imagefile=imagegallery.fileName;
		appLocationService = new AppLocationService(ChefRegistraion.this);
		edit_username = (EditText) findViewById(R.id.etxt_name);
		edit_mobilenumber = (EditText) findViewById(R.id.etxt_mobileno);
		edit_password = (EditText) findViewById(R.id.etxt_password);
		edit_repassword = (EditText) findViewById(R.id.etxt_retypepassword);
		edit_emailid = (EditText) findViewById(R.id.etxt_email);
	    edit_location = (EditText) findViewById(R.id.etxt_location);
		/*edit_accnumber = (EditText) findViewById(R.id.etxt_bank_accountno);
		edit_accname = (EditText) findViewById(R.id.etxt_accountname);
		edit_bankname = (EditText) findViewById(R.id.etxt_bankname);
		edit_branch = (EditText) findViewById(R.id.etxt_brachname);
		edit_ifsc = (EditText) findViewById(R.id.etxt_ifsc_code);*/
		Btn_register = (Button) findViewById(R.id.register_button);

		// public void onClick(View v) {
		//
		// if (imagepath == null) {
		// Toast.makeText(getApplicationContext(),
		// "please select image", Toast.LENGTH_SHORT).show();
		// } else {
		// dialog = ProgressDialog.show(ChefRegistraion.this,
		// "Uploading", "Please wait...", true);
		//
		// new Thread(new Runnable() {
		// public void run() {
		//
		// uploadFile(imagepath);
		//
		// }
		// }).start();
		// }
		//
		// }
		// });

		// cancel.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// ChefRegistraion.this.finish();
		// }
		// });

		Btn_register.setOnClickListener(new View.OnClickListener() {
			LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			@Override
			public void onClick(View v) {

				uname = edit_username.getText().toString().trim();
				mobile = edit_mobilenumber.getText().toString().trim();
				password = edit_password.getText().toString().trim();
				retypepassword = edit_repassword.getText().toString().trim();
				email = edit_emailid.getText().toString().trim(); 
				location= edit_location.getText().toString().trim();
			/*	accountname = edit_accname.getText().toString().trim();
				accountnumber = edit_accnumber.getText().toString().trim();
				bankname = edit_bankname.getText().toString().trim();
				branchname = edit_branch.getText().toString().trim();
				ifsccode = edit_ifsc.getText().toString().trim();*/
				try {

					gps_enabled = lm
							.isProviderEnabled(LocationManager.GPS_PROVIDER);
					network_enabled = lm
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

					nwLocation = appLocationService
							.getLocation(LocationManager.NETWORK_PROVIDER);
					gpsLocation = appLocationService
							.getLocation(LocationManager.GPS_PROVIDER);
					
					if (gpsLocation != null) {
						Log.d(TAG, "gps_enabled");
						lattitude = gpsLocation.getLatitude();
						longitude = gpsLocation.getLongitude();
						Log.d(TAG, "gpsLocation " + lattitude + " : " + longitude);

					} if (nwLocation != null) {
						Log.d(TAG, "network_enabled");
						lattitude = nwLocation.getLatitude();
						longitude = nwLocation.getLongitude();
						Log.d(TAG, "nwLocation " + lattitude + " : " + longitude);
					}

					if (!gps_enabled && !network_enabled) {
						// Ask the user to enable GPS

						Log.d(TAG, "!gps_enabled && !network_enabled");
						AlertDialog.Builder builder = new AlertDialog.Builder(
								ChefRegistraion.this);
						builder.setTitle("Location Manager");
						builder.setMessage("Please Enable Your GPS?");
						builder.setPositiveButton("Settings",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent i = new Intent(
												Settings.ACTION_LOCATION_SOURCE_SETTINGS);
										startActivity(i);
									}
								});
						builder.setNegativeButton("Close",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// No location service, no Activity
										dialog.cancel();
									}
								});
						builder.create().show();
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				

				strLattitude = String.valueOf(lattitude);
				strLongitude = String.valueOf(longitude);
				Log.d(TAG, "strLattitude" + strLattitude);
				Log.d(TAG, "strLongitude" + strLongitude);


				if ((!(uname.trim().length() == 0
						|| mobile.trim().length() == 0
						|| password.trim().length() == 0
						|| retypepassword.trim().length() == 0 
						||location.trim().length() == 0
					/*	|| accountname.trim().length() == 0
						|| accountnumber.trim().length() == 0
						|| bankname.trim().length() == 0
						|| branchname.trim().length() == 
						|| ifsccode.trim().length() == 00*/ 
						|| email.trim().length() == 0))) {

					if (isValidName(uname)) {

						if (mobile.trim().length() == 10) {

							if (password.equals(retypepassword)) {

								if (validEmail(email)) {

									// new Chefregister().execute();
									// *********

									if (imagepath == null) {
										// Toast.makeText(getApplicationContext(),
										// "please select image",
										// Toast.LENGTH_SHORT).show();
										message = "Please select image";
										alertdialog();
									} else {
										// dialog =
										// ProgressDialog.show(ChefRegistraion.this,
										// "Uploading", "Please wait...", true);

										new Thread(new Runnable() {
											public void run() {
												Log.d(TAG,
														"uploadFile(imagepath)");
												Log.d("", "upLoadServerUri");
												uploadFile(imagepath);

											}

										}).start();
										new Chefregister().execute();
									}
									// **********

								} else {
									edit_emailid.setError("Invalid Email");
									alertdialog();
									message = "Invalid Email";
									// Toast("Invalid Email");
								}
							} else {
								message = ("Password Not Matching");
								alertdialog();
								// Toast("Password Not Matching");
							}

						} else {
							message = ("Please enter a valid 10 Digit Mobile No");
							alertdialog();
							// Toast("Please enter a valid 10 Digit Mobile No");
						}
					} else {
						message = ("special characters not allowed");
						alertdialog();
						// Toast("special characters not allowed");
					}

				} else {
					message = ("Please fill all fields");
					alertdialog();
					// Toast("Please fill all fields ");
				}

			}
		});
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
				Toast.makeText(this, "Picture was not taken",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Picture was not taken",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
		if (selectedImageUri != null) {
			try {
				imagepath = getPath(selectedImageUri);
				Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
				imgView.setImageBitmap(bitmap);
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.gallery_btn) {
			try {
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

				message = e.getMessage();
				alertdialog();
				// Toast.makeText(getApplicationContext(),
				// e.getMessage(),Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		} else if (v.getId() == R.id.camera_btn) {

			// define the file-name to save photo taken by Camera activity
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
		File sourceFile = new File(sourceFileUri);
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
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("profile_image", fileName);

				conn.connect();
				dos = new DataOutputStream(conn.getOutputStream());

				// image upload
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"profile_image\";filename=\""
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
						Toast.makeText(ChefRegistraion.this,
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
						Toast.makeText(ChefRegistraion.this,
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

	class Chefregister extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			 pDialog = new ProgressDialog(ChefRegistraion.this);
			 pDialog.setMessage("Registering...");
			 pDialog.setIndeterminate(false);
			 pDialog.setCancelable(true);
			 pDialog.show();
			 pDialog.setCanceledOnTouchOutside(false);
		}

		protected String doInBackground(String... args) {

			try { // Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name", uname));
				params.add(new BasicNameValuePair("mobile_number", mobile));
				params.add(new BasicNameValuePair("password", password));
				params.add(new BasicNameValuePair("email", email));
				params.add(new BasicNameValuePair("latitude", strLattitude));
				params.add(new BasicNameValuePair("longitude", strLongitude));
				params.add(new BasicNameValuePair("address", location));
				params.add(new BasicNameValuePair("chef_image", fName));
				params.add(new BasicNameValuePair("usertype", "CHEF"));
/*				params.add(new BasicNameValuePair("account_number",accountnumber));
				params.add(new BasicNameValuePair("account_holder_name",accountname));
				params.add(new BasicNameValuePair("bank_name", bankname));
				params.add(new BasicNameValuePair("branch", branchname));
				params.add(new BasicNameValuePair("ifsc_code", ifsccode));
				params.add(new BasicNameValuePair("usertype", "CHEF"));
				params.add(new BasicNameValuePair("chef_image", fName));*/

				Log.d(TAG, "imagepath" + imagepath);
				Log.d(TAG, "strLattitude" + strLattitude);
				Log.d(TAG, "strLongitude" + strLongitude);
				Log.d("uname", "" + uname);
				Log.d("mobile", "" + mobile);
				Log.d("password", "" + password);
				Log.d("retypepassword", "" + retypepassword);
				Log.d("email", "" + email);
				Log.d("location", "" + location);
				/*Log.d("accountname", "" + accountname);
				Log.d("accountnumber", "" + accountnumber);
				Log.d("bankname", "" + bankname);
				Log.d("branchname", "" + branchname);
				Log.d("ifsccode", "" + ifsccode);*/

				JSONObject json = jsonParser.makeHttpRequest(REGIST_URL,
						"POST", params);
				Log.d("Create Response", json.toString());

				result = json.getString("status");
				Log.d("Registration States...", "" + result);

				// check for success tag
				try {

					if (result.equals("success")) {
						// Toast("success");
						message = ("success");
						Intent i = new Intent(getApplicationContext(),
								MainActivity.class);
						startActivity(i);
						finish();
					} else if (result.equals("mobile_number_exist")) {
						// Toast("Mobile number Exist");
						message = ("Mobile number Exist");
						alertdialog();
					} else if (result.equals("failed")) {
						// Toast("Registraion Failed");
						message = ("Registraion Failed");
						alertdialog();
					} else {
						// Toast("Unexpected error");
						message = ("Unexpected Error");
						alertdialog();
					}
				} catch (Exception e) {
					e.printStackTrace();

				}

			}

			catch (Exception e) {
				// TODO: handle exception
				message = ("Unexpected Error");
				alertdialog();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			try {
				pDialog.dismiss();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String message = null;
			// new Chefregister().execute();
			try {
				if (!result.equals("success")) {
					// message = "Background succeeded.";
					if (result.equals("mobile_number_exist")) {
						message = "Mobile number Exist";
					} else if (result.equals("failed")) {

						message = "Registraion Failed";
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

	private boolean isValidName(String name) {
		String NAME_PATTERN = "^[a-zA-Z ]+$";

		Pattern pattern = Pattern.compile(NAME_PATTERN);
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}

	private boolean validEmail(String email) {
		Pattern pattern = Patterns.EMAIL_ADDRESS;
		return pattern.matcher(email).matches();
	}

	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
		});

	}

	public void alertdialog() {

		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(ChefRegistraion.this)
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