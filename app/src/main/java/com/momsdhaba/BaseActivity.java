package com.momsdhaba;

import java.util.ArrayList;

import com.momsdhaba.history.Chef_History_Activity;
import com.momsdhaba.notification.NotificationActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class BaseActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	protected RelativeLayout _completeLayout, _activityLayout;
	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer);
		// if (savedInstanceState == null) {
		// // on first time display view for first nav item
		// // displayView(0);
		// }
	}

	public void set(String[] navMenuTitles, TypedArray navMenuIcons) {
		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items
		if (navMenuIcons == null) {
			for (int i = 0; i < navMenuTitles.length; i++) {
				navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));
			}
		} else {
			for (int i = 0; i < navMenuTitles.length; i++) {
				navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],
						navMenuIcons.getResourceId(i, -1)));
			}
		}

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(0xffff8080));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_action_menu, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
				// accessibility
				R.string.app_name // nav drawer close - description for
		// accessibility
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_action_menu);
	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}
//		else if(item.getItemId() == R.id.action_settings){
//			Intent i = new Intent(getApplicationContext(), MainActivity.class);
//			i.putExtra("Logout", "Logout");
//			finish();
//			startActivity(i);
//			
//		}

		return super.onOptionsItemSelected(item);
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {

		switch (position) {
		case 0:
			Intent intent = new Intent(this, AddMenu.class);
			startActivity(intent);
			finish();// finishes the current activity
			break;
		case 1:
			Intent intent1 = new Intent(this, NotificationActivity.class);
			startActivity(intent1);
			finish();
			break;
		case 2:
			Intent intent2 = new Intent(this, FoodList.class);
			startActivity(intent2);
			finish();
			break;
		case 3:
			Intent intent3 = new Intent(this, ReferChef.class);
			startActivity(intent3);
			finish();
			break;
		case 4:
			Intent intent4 = new Intent(this, Chef_History_Activity.class);
			startActivity(intent4);
			finish();
			break;
		case 5:
			Intent intent5 = new Intent(this, Payment.class);
			startActivity(intent5);
			finish();
			break;
		case 6:
			Intent intent6 = new Intent(this, SettingsActivity.class);
			startActivity(intent6);
			finish();
			break;
		case 7:
			Intent intent7 = new Intent(this, HollerHere.class);
			startActivity(intent7);
			finish();
			break;
		case 8:
			Intent intent8 = new Intent(this, HelpDesk.class);
			startActivity(intent8);
			finish();
			break;
		case 9:
			Intent intent9 = new Intent(this, Chef_logout.class);
			startActivity(intent9);
			finish();
			break;
		default:
			break;
		}

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
