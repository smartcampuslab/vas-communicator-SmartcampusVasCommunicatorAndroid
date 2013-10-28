/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either   express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.communicator;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import eu.trentorise.smartcampus.ac.AACException;
import eu.trentorise.smartcampus.ac.SCAccessProvider;
import eu.trentorise.smartcampus.android.common.SCAsyncTask;
import eu.trentorise.smartcampus.communicator.custom.AbstractAsyncTaskProcessor;
import eu.trentorise.smartcampus.communicator.custom.data.CommunicatorHelper;
import eu.trentorise.smartcampus.communicator.fragments.channels.FeedListFragment;
import eu.trentorise.smartcampus.communicator.fragments.labels.LabelListFragment;
import eu.trentorise.smartcampus.communicator.fragments.messages.InboxFragment;
import eu.trentorise.smartcampus.communicator.fragments.messages.SearchFragment;
import eu.trentorise.smartcampus.communicator.fragments.messages.StarredFragment;
import eu.trentorise.smartcampus.communicator.preferences.SettingsActivity;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.SecurityException;

public class HomeActivity extends SherlockFragmentActivity {

	protected final int mainlayout = android.R.id.content;

	public static DrawerLayout mDrawerLayout;
	public static ListView mDrawerList;
	public static ActionBarDrawerToggle mDrawerToggle;
	public static String drawerState = "on";
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mFragmentTitles;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private boolean initData() {
		try {
			new SCAsyncTask<Void, Void, Void>(this, new StartProcessor(this))
					.execute();
		} catch (Exception e1) {
			CommunicatorHelper.endAppFailure(this, R.string.app_failure_setup);
			return false;
		}
		return true;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		CommunicatorHelper.init(getApplicationContext());
		int i = 1;//getArguments().getInt(ARG_FRAGMENT);
		String frgm = getResources().getStringArray(R.array.fragment_array)[i];
		try {
			if (!CommunicatorHelper.getAccessProvider().login(this, null)) {
				initData();

			}
		} catch (AACException e) {
			e.printStackTrace();
		}

		mFragmentTitles = getResources().getStringArray(R.array.fragment_array);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new MenuDrawerAdapter(this, getResources()
				.getStringArray(R.array.fragment_array)));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		//
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu();
			}

			/** Fix the slide and map bug. **/
			public void onDrawerSlide(View drawerView, float slideOffset) {
				getSupportActionBar().setTitle(mDrawerTitle);
				mDrawerLayout.bringChildToFront(drawerView);
				supportInvalidateOptionsMenu();
				super.onDrawerSlide(drawerView, slideOffset);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		if (savedInstanceState == null) {
			startHomeFragment();
		}
	}

	private void startHomeFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		StarredFragment fragment = new StarredFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.replace(R.id.fragment_container, fragment);
		// ft.addToBackStack(fragment.getTag());
		ft.commit();
		
	}
	

	// /TEST//
	@Override
	protected void onResume() {

		try {
			Log.i("TOKEN", CommunicatorHelper.getAuthToken());
		} catch (AACException e) {

			e.printStackTrace();
		}
		super.onResume();
	}

	// TEST///

	@Override
	public void onNewIntent(Intent arg0) {
		try {
			CommunicatorHelper.resetUnread();
			CommunicatorHelper.getAccessProvider().login(this, null);
		} catch (Exception e) {
			CommunicatorHelper.endAppFailure(this, R.string.app_failure_setup);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mDrawerToggle.setDrawerIndicatorEnabled(true);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SCAccessProvider.SC_AUTH_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String token = data.getExtras().getString(
						AccountManager.KEY_AUTHTOKEN);
				if (token == null) {
					CommunicatorHelper.endAppFailure(this,
							R.string.app_failure_security);
				} else {
					initData();
				}
			} else if (resultCode == RESULT_CANCELED
					&& requestCode == SCAccessProvider.SC_AUTH_ACTIVITY_REQUEST_CODE) {
				CommunicatorHelper.endAppFailure(this, R.string.token_required);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			if (drawerState.equals("on")) {
				if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
					mDrawerLayout.closeDrawer(mDrawerList);
				} else {
					mDrawerLayout.openDrawer(mDrawerList);
				}
			}
			else{
				drawerState = "on";
				onBackPressed();
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class StartProcessor extends AbstractAsyncTaskProcessor<Void, Void> {

		public StartProcessor(Activity activity) {
			super(activity);
		}

		@Override
		public Void performAction(Void... params) throws SecurityException,
				Exception {
			CommunicatorHelper.start(false);
			return null;
		}

		@Override
		public void handleResult(Void result) {
			CommunicatorHelper.resetUnread();
		}

	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		String fragmentString = mFragmentTitles[position];
		// // update the main content by replacing fragments
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		if (fragmentString.equals(mFragmentTitles[0])) {
			InboxFragment fragment = new InboxFragment();
			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.replace(R.id.fragment_container, fragment,
					"inbox");
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
			mDrawerLayout.closeDrawer(mDrawerList);
		} else if (fragmentString.equals(mFragmentTitles[1])) {
			StarredFragment fragment = new StarredFragment();
			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.replace(R.id.fragment_container, fragment,
					"star");
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
			mDrawerLayout.closeDrawer(mDrawerList);
		} else if (fragmentString.equals(mFragmentTitles[2])) {
			FeedListFragment fragment = new FeedListFragment();
			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.replace(R.id.fragment_container, fragment,
					"extsbs");
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
			mDrawerLayout.closeDrawer(mDrawerList);
		} else if (fragmentString.equals(mFragmentTitles[3])) {
			LabelListFragment fragment = new LabelListFragment();
			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.replace(R.id.fragment_container, fragment,
					"Labels");
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
			mDrawerLayout.closeDrawer(mDrawerList);
		} else if (fragmentString.equals(mFragmentTitles[4])) {
			SearchFragment fragment = new SearchFragment();
			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.replace(R.id.fragment_container, fragment,
					"search");
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		else if (fragmentString.equals(mFragmentTitles[5])) {
			Intent i = (new Intent(HomeActivity.this, SettingsActivity.class));
			startActivity(i);
			mDrawerLayout.closeDrawer(mDrawerList);
		}

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

}
