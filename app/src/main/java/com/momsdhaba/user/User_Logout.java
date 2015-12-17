package com.momsdhaba.user;

import com.momsdhaba.MainActivity;
import com.momsdhaba.R;
import com.momsdhaba.user.BaseActivity2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class User_Logout extends BaseActivity2 {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user__logout);
		
		
		navMenuTitles = getResources().getStringArray(
				R.array.nav_drawer_items_user); // load
		navMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons_user);// load icons from
		// strings.xml
		set(navMenuTitles, navMenuIcons);
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		i.putExtra("Logout", "Logout");
		finish();
		startActivity(i);
		
	}

	
}
