package com.momsdhaba.user;

import com.momsdhaba.R;

import android.content.res.TypedArray;
import android.os.Bundle;

public class User_HollerHere extends BaseActivity2 {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_hollerhere);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_user); // load
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons_user);// load icons from
															// strings.xml
		set(navMenuTitles, navMenuIcons);
	}

}
