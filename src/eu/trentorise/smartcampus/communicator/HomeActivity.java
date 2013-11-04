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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import eu.trentorise.smartcampus.ac.AACException;
import eu.trentorise.smartcampus.ac.SCAccessProvider;
import eu.trentorise.smartcampus.android.common.SCAsyncTask;
import eu.trentorise.smartcampus.communicator.custom.AbstractAsyncTaskProcessor;
import eu.trentorise.smartcampus.communicator.custom.data.CommunicatorHelper;
import eu.trentorise.smartcampus.communicator.fragments.BackListener;
import eu.trentorise.smartcampus.communicator.fragments.MainFragment;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.SecurityException;

public class HomeActivity extends SherlockFragmentActivity {

	protected final int mainlayout = android.R.id.content;
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
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
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		CommunicatorHelper.init(getApplicationContext());
        try {
                if (!CommunicatorHelper.getAccessProvider().login(this, null)) {
                        initData();

                }
        } catch (AACException e) {
                e.printStackTrace();
        }
		setUpContent();
	}
///TEST//
	@Override
	protected void onResume() {

			try {
				Log.i("TOKEN", CommunicatorHelper.getAuthToken());
			} catch (AACException e) {
				
				e.printStackTrace();
			}
		super.onResume();
	}
//TEST///
	
	
	@Override
	public void onNewIntent(Intent arg0) {
		try {
			CommunicatorHelper.resetUnread();
			CommunicatorHelper.getAccessProvider().login(this, null);
		} catch (Exception e) {
			CommunicatorHelper.endAppFailure(this, R.string.app_failure_setup);
		}
	}

	private void setUpContent() {
		if (getSupportFragmentManager().getBackStackEntryCount() > 0)
			getSupportFragmentManager().popBackStack();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment frag = null;
		frag = new MainFragment();
		ft.replace(android.R.id.content, frag).commitAllowingStateLoss();

	}

	@Override
	public void onBackPressed() {
		Fragment currentFragment = getSupportFragmentManager()
				.findFragmentById(android.R.id.content);
		// Checking if there is a fragment that it's listening for back button
		if (currentFragment != null && currentFragment instanceof BackListener) {
			((BackListener) currentFragment).onBack();
		}

		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SCAccessProvider.SC_AUTH_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String token = data.getExtras().getString(
						AccountManager.KEY_AUTHTOKEN);
				try {
					CommunicatorHelper.getAuthToken();
				} catch (AACException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
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
			setUpContent();
		}

	}

}
