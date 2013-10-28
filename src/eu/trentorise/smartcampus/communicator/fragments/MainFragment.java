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
package eu.trentorise.smartcampus.communicator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import eu.trentorise.smartcampus.communicator.HomeActivity;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.preferences.SettingsActivity;

//@Override
//public View onCreateView(LayoutInflater inflater, ViewGroup container,
//		Bundle savedInstanceState) {
//	// View rootView = inflater.inflate(R.layout.main, container, false);
//	int i = getArguments().getInt(ARG_FRAGMENT);
//	String frgm = getResources().getStringArray(R.array.fragment_array)[i];
//
//	// int imageId =
//	// getResources().getIdentifier(frgm.toLowerCase(Locale.getDefault()),
//	// "drawable", getActivity().getPackageName());
//	// ((ImageView)
//	// rootView.findViewById(R.id.image)).setImageResource(imageId);
//	getActivity().setTitle(frgm);
//	// return rootView;
//	return inflater.inflate(R.layout.main, container, false);
//}
public class MainFragment extends SherlockFragment {

	private FragmentManager fragmentManager;
	public static String ARG_FRAGMENT = "fragment_n";

	public MainFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getSherlockActivity().getSupportFragmentManager();
		
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int i = 1;//getArguments().getInt(ARG_FRAGMENT);
		String frgm = getResources().getStringArray(R.array.fragment_array)[i];
		getSherlockActivity().setTitle(frgm);
		return inflater.inflate(R.layout.main, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Showing/hiding back button
		// getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(false);
		// getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(
		// false);
		// getSherlockActivity().getSupportActionBar().setDisplayShowTitleEnabled(
		// true);
		getSherlockActivity().setTitle(R.string.app_name);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.main_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mainmenu_settings:
			startActivity(new Intent(getActivity(), SettingsActivity.class));
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}
}
