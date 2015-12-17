package com.momsdhaba;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.momsdhaba.notification.MyService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class AddMenu extends BaseActivity implements OnClickListener {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private final static String TAG = AddMenu.class.getSimpleName();
	protected Button selectColoursButton;
	TextView text_days, chef;
	String Food_type, Food_name, Item, Description, Price, Aailabledays, id,
			Numofitems, imgid, Chefname, firstdate, lastdate, Menuname;
	ImageView imgegg, imgnveg, imgveg, imgspinner, imgaddname, imgnext;
	ImageButton vegbtn, eggbtn, nveg;
	TextView type, selectdays, preview,text_logout;
	Button uploadbtn, Addname, AddButton;
	ImageView food_image;
	EditText eNumofitems, eDiscripton, ePrice, eMenuname;
	ArrayAdapter<String> ar;
	ArrayList<String> list = new ArrayList<String>();
	String message, title;
	AlertDialog alertDialog;
	protected CharSequence[] dayschar = { "All Days", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

	protected ArrayList<CharSequence> SelectedDays = new ArrayList<CharSequence>();
	int currentDayOfWeek;
	// int currentDayOfMonth;
	Calendar calendar;
	int weekNo;
	String[] availabledays, selecteddays;
	SimpleDateFormat format;
	private RadioGroup radioFoodGroup;
	private RadioButton radioFoodButton;
	int selectedId;
	public static final String MyPREFERENCES = "Prefs";
	SharedPreferences sharedpref;
	//boolean others =false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_menu);
		
		startService(new Intent(AddMenu.this,MyService.class));
		 Log.i(TAG, "Service started"); 
		 sharedpref = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
		 
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);// load icons from
															// strings.xml
		set(navMenuTitles, navMenuIcons);
		id = sharedpref.getString("ID", "");
		Log.d(TAG, "sharedpreferences " + id);
		/*Intent intent = getIntent();
		id = intent.getStringExtra("ID");
		Chefname = intent.getStringExtra("ChefName");
		Log.d(TAG, "ID" + id);
		Log.d(TAG, "ChefName " + Chefname);*/

		// eAdditem = (EditText) findViewById(R.id.edit_additem);
		eDiscripton = (EditText) findViewById(R.id.edit_description);
		ePrice = (EditText) findViewById(R.id.edit_price);
		eNumofitems = (EditText) findViewById(R.id.itemnumbers);
		eMenuname = (EditText) findViewById(R.id.edit_newitem);
		eMenuname.setVisibility(View.GONE);
		//type = (TextView) findViewById(R.id.type_text);
		
		radioFoodGroup = (RadioGroup) findViewById(R.id.radioFood);
		
		// final TextView t1 = (TextView) findViewById(R.id.food_item);
		selectdays = (TextView) findViewById(R.id.text_select_days);
		//chef = (TextView) findViewById(R.id.chefname);
//		text_logout = (TextView) findViewById(R.id.logout);
		//chef.setText("Welcome : " + Chefname);
		//chef.setText(Chefname);
		//imgegg = (ImageView) findViewById(R.id.eggit_btn);
		//imgveg = (ImageView) findViewById(R.id.veg_btn);
		//imgnveg = (ImageView) findViewById(R.id.non_veg_btn);
		//selectColoursButton = (Button) findViewById(R.id.select_days);
		preview = (TextView) findViewById(R.id.add_button);

		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
		format = new SimpleDateFormat("yyyy-MM-dd");
		calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		weekNo = calendar.get(Calendar.WEEK_OF_MONTH);
		Alldays();
		// findViewById(R.id.test).setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent i = new Intent(ProfileActivity.this, ChefListview.class);
		// i.putExtra("ID", id);
		// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(i);
		// }
		//
		// });

		selectdays.setOnClickListener(this);
//		imgnveg.setOnClickListener(this);
//		imgegg.setOnClickListener(this);
//		imgveg.setOnClickListener(this);

		text_days = (TextView) findViewById(R.id.textdays);
		preview.setOnClickListener(this);

		food_image = (ImageView) findViewById(R.id.imageView2);

		final String[] spstr = getResources().getStringArray(
				R.array.spinnervalue);
		final Spinner sp = (Spinner) findViewById(R.id.spinner1);

		ar = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, spstr);
		sp.setAdapter(ar);
		sp.setPrompt("Title");
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				//sp.setSelection(0,false);
				try {
					Food_name = ((TextView) arg1).getText().toString();
				} catch (Exception e) {					
					e.printStackTrace();
				}
				Log.d(TAG, "Food_name" + Food_name);
				if (Food_name.equals("Meals")) {
					food_image.setImageDrawable(getResources().getDrawable(
							R.drawable.meals));
					imgid = "meals";
				}
				if (Food_name.equals("ChickenBiryani")) {
					food_image.setImageDrawable(getResources().getDrawable(
							R.drawable.chickenbiryani));
					imgid = "chickenbiryani";
				}
				if (Food_name.equals("CurdRice")) {
					food_image.setImageDrawable(getResources().getDrawable(
							R.drawable.curdrice));
					imgid = "curd";
				}
				if (Food_name.equals("LemonRice")) {
					food_image.setImageDrawable(getResources().getDrawable(
							R.drawable.lemonrice1));
					imgid = "lemon";
				}
				if (Food_name.equals("TomatoRice")) {
					food_image.setImageDrawable(getResources().getDrawable(
							R.drawable.tomatorice));
					imgid = "tomoto";
				}
				if (Food_name.equals("SambarRice")) {
					food_image.setImageDrawable(getResources().getDrawable(
							R.drawable.sambarrice1));
					imgid = "sambar";
				}
				if (Food_name.equals("Others")) {
					food_image.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_launcher));
					imgid = "Others";
					//others =true;
					eMenuname.setVisibility(View.VISIBLE);
				}								
				
				// t1.setText(Food_name);
				//eMenuname.setText(Food_name);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
//		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent i = new Intent(AddMenu.this, MainActivity.class);
//				i.putExtra("Logout", "Logout");
//				finish();
//				startActivity(i);
//			}
//		});

	}

	public void Alldays() {
		// TODO Auto-generated method stub
		Log.d("week of month =", "" + weekNo);
		Log.d("currentDayOfWeek =", "" + currentDayOfWeek);
		availabledays = new String[7];
		for (int i = 0; i < 7; i++) {

			availabledays[i] = format.format(calendar.getTime());
			Log.d(TAG, " Days : " + availabledays[i]);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			// if (i >= currentDayOfWeek - 2) {
			// Log.i(TAG, "Selected Days : " + availabledays[i]);
			// ar.add(availabledays[i]);
			// }
			// newString = String.valueOf(ar);
		}
		firstdate = availabledays[0];
		lastdate = availabledays[6];
		Log.i(TAG, "first day : " + availabledays[0]);
		Log.i(TAG, "last day : " + availabledays[6]);
	}

	@Override
	public void onClick(View view) {

		
		selectedId = radioFoodGroup.getCheckedRadioButtonId();
		// Toast.makeText(EditPage.this,radioFoodButton.getText(),
		// Toast.LENGTH_SHORT).show();
		radioFoodButton = (RadioButton) findViewById(selectedId);
		
		switch (view.getId()) {
		case R.id.text_select_days:
			showSelectDayDialog();
			break;
		/*case R.id.radioveg:
			Food_type = "VEG";
			Log.d(TAG, "Food_type "+Food_type);
			//type.setText(Food_type);
			break;
		case R.id.radioegg:
			Food_type = "EGG";
			Log.d(TAG, "Food_type "+Food_type);
			//type.setText(Food_type);
			break;
		case R.id.radiononveg:
			Food_type = "NON-VEG";
			Log.d(TAG, "Food_type "+Food_type);
			//type.setText(Food_type);
			break;*/

		case R.id.add_button:
			passdata();
			break;

		default:
			break;
		}
	}

	// private void selectImage() {
	//
	// final CharSequence[] options = { "Take Photo", "Choose from Gallery",
	// "Cancel" };
	//
	// AlertDialog.Builder builder = new AlertDialog.Builder(
	// ProfileActivity.this);
	// builder.setTitle("Add Photo!");
	// builder.setItems(options, new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int item) {
	//
	// if (options[item].equals("Take Photo"))
	//
	// {
	// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	// File f = new File(android.os.Environment
	// .getExternalStorageDirectory(), "temp.jpg");
	// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
	// startActivityForResult(intent, 1);
	// } else if (options[item].equals("Choose from Gallery")) {
	// Intent intent = new Intent(
	// Intent.ACTION_PICK,
	// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	//
	// startActivityForResult(intent, 2);
	// } else if (options[item].equals("Cancel")) {
	// dialog.dismiss();
	// }
	// }
	// });
	// builder.show();
	// }

	protected void onChangeSelectedDays() {
		StringBuilder stringBuilder = new StringBuilder();

		for (CharSequence day : SelectedDays)
			stringBuilder.append(day + ",");
		Log.d(TAG, ""+SelectedDays);
		
		
		text_days.setText(stringBuilder.toString());
		Aailabledays = text_days.getText().toString();
		
		Log.d("Aailabledays", "= " + Aailabledays);
		if (Aailabledays.contains("All Days")) {
			text_days.setText("All Days");
			Aailabledays = "All";
		} else {
			text_days.setText(stringBuilder.toString());

		}

		// Log.d("???????????????", ""+sharedFact);
	}

	private void passdata() {
		// TODO Auto-generated method stub
		// Item = eAdditem.getText().toString();
		try {
			Description = eDiscripton.getText().toString();
			Price = ePrice.getText().toString();
			Numofitems = eNumofitems.getText().toString();
			Menuname = eMenuname.getText().toString();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (!Menuname.isEmpty()) {
			Log.d(TAG, "Menuname "+Menuname);
			Food_name = Menuname;
		}
		try {
			Food_type = radioFoodButton.getText().toString().trim();
			Log.d(TAG, "Food_type " + Food_type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!Description.isEmpty()) {
			if (!Price.isEmpty()) {
				if (Food_type != null) {
					if (Aailabledays != null) {
						Intent intent = new Intent(AddMenu.this,
								ViewActivity.class);
						intent.putExtra("FOODTYPE", Food_type);
						intent.putExtra("FOODNAME", Food_name);
						intent.putExtra("DISCRIPTION", Description);
						intent.putExtra("PRICE", Price);
						intent.putExtra("DAYS", Aailabledays);
						intent.putExtra("ID", id);
						intent.putExtra("IMAGEID", imgid);
						intent.putExtra("NUMBER", Numofitems);
						intent.putExtra("CHEFNAME", Chefname);
						startActivity(intent);
					} else {
						message = "Please Select The Aailabledays Days";
						alertdialog();
						//Toast("Please Select The Aailabledays Days");
					}
				} else {
					message = "Please Select The Type Of Food";
					alertdialog();
					//Toast("Please Select The Type Of Food");
				}
			} else {
				message = "Please Enter The Food Price";
				alertdialog();
				
				//Toast("Please Enter The Food Price");
			}
		} else {
			message = "Please Enter Description About Food";
			alertdialog();
			
			//Toast("Please Enter Description About Food");
		}
		
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

	protected void showSelectDayDialog() {
		

		boolean[] checkedColours = new boolean[dayschar.length];
		int count = dayschar.length;

		for (int i = 0; i < count; i++)
			checkedColours[i] = SelectedDays.contains(dayschar[i]);

		DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {

				if (isChecked)
					SelectedDays.add(dayschar[which]);
				else
					SelectedDays.remove(dayschar[which]);
				onChangeSelectedDays();
				
				if (which == 0) {
					for (int i = 1; i <= 7; i++) {
						((AlertDialog) dialog).getListView().setItemChecked(i,true);
					}
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setTitle("Select Availabile Days");
		builder.setTitle("Select Dates " + " (" + firstdate + ")" + " - " + "("
				+ lastdate + ")");
		builder.setPositiveButton("Close",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// stuff you want the button to do
					}
				});

		builder.setMultiChoiceItems(dayschar, checkedColours,
				coloursDialogListener);

		AlertDialog dialog = builder.create();
		dialog.show();
	}
	public void alertdialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				// If there are stories, add them to the table
				alertDialog = new AlertDialog.Builder(AddMenu.this)
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
