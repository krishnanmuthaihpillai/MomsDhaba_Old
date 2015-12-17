package com.momsdhaba.user;

import com.momsdhaba.R;
import com.momsdhaba.user.BaseActivity2;

import android.support.v7.app.ActionBarActivity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class User_SelectArea extends BaseActivity2 {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private final static String TAG = User_SelectArea.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user__select_area);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_user); // load
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons_user);// load icons from
															// strings.xml
		set(navMenuTitles, navMenuIcons);
	}

	
}
