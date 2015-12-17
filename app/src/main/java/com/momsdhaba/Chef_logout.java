package com.momsdhaba;

import com.momsdhaba.notification.MyService;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Chef_logout extends BaseActivity {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chef_logout);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);// load icons from
															// strings.xml
		set(navMenuTitles, navMenuIcons);
		stopService(new Intent(Chef_logout.this,MyService.class));
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		i.putExtra("Logout", "Logout");
		finish();
		startActivity(i);
	}

}
