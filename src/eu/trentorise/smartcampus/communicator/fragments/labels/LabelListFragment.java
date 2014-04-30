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
package eu.trentorise.smartcampus.communicator.fragments.labels;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import eu.trentorise.smartcampus.android.common.SCAsyncTask;
import eu.trentorise.smartcampus.communicator.HomeActivity;
import it.smartcampuslab.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.AbstractAsyncTaskProcessor;
import eu.trentorise.smartcampus.communicator.custom.LabelAdapter;
import eu.trentorise.smartcampus.communicator.custom.data.CommunicatorHelper;
import eu.trentorise.smartcampus.communicator.fragments.messages.LabelDialog;
import eu.trentorise.smartcampus.communicator.fragments.messages.LabelDialog.OnLabelCreatedListener;
import eu.trentorise.smartcampus.communicator.model.LabelObject;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.SecurityException;

public class LabelListFragment extends SherlockListFragment implements OnLabelCreatedListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		return arg0.inflate(R.layout.labels, arg1, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSherlockActivity().setTitle(R.string.title_labels);
		
		LabelAdapter adapter = new LabelAdapter(getActivity(), R.layout.label, CommunicatorHelper.getLabels());
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				viewLabelMessages(((LabelAdapter)getListAdapter()).getItem(position));
			}
		});
		
		registerForContextMenu(getListView());
	}

	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		MenuItem item = menu.add(Menu.CATEGORY_SYSTEM, R.id.label_add, 1, R.string.add_label_dialog_title);
		item.setIcon(R.drawable.add);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.label_add:
			LabelDialog d = new LabelDialog(getActivity(), this);
			d.setOwnerActivity(getActivity());
			d.show();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void OnLabelCreated(LabelObject label) {
		LabelAdapter labelAdapter = (LabelAdapter)getListAdapter();
		boolean found = false;
		for (int i = 0; i < labelAdapter.getCount(); i++) {
			LabelObject oldLabel = labelAdapter.getItem(i);
			if (label.getId().equals(oldLabel.getId())) {
				oldLabel.setColor(label.getColor());
				oldLabel.setId(label.getId());
				oldLabel.setName(label.getName());
				found = true;
				break;
			}
		} 
		if (!found) {
			labelAdapter.add(label);
		}
		labelAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(R.string.label_options_header);
		android.view.MenuInflater inflater = getSherlockActivity().getMenuInflater();
	    inflater.inflate(R.menu.label_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		LabelObject content = ((LabelAdapter)getListAdapter()).getItem(info.position);
		return handleMenuItem(content, item.getItemId());
	}

	protected boolean handleMenuItem(final LabelObject content, int itemId) {
		switch (itemId) {
		case R.id.label_option_edit:
			LabelDialog d = new LabelDialog(getActivity(), this, CommunicatorHelper.getLabel(content.getId()));
			d.setOwnerActivity(getActivity());
			d.show();
			return true;
		case R.id.label_option_remove:
			AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
			builder.setMessage(R.string.label_remove_text)
			       .setCancelable(false)
			       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   new SCAsyncTask<LabelObject, Void, LabelObject>(getActivity(), new RemoveLabelProcessor(getActivity())).execute(content);
			               dialog.dismiss();
			           }
			       })
			       .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		case R.id.label_option_view:
			viewLabelMessages(content);
			return true;
		default:
			return false;
		}
	}

	protected void viewLabelMessages(LabelObject content) {
		FragmentTransaction ft  = getSherlockActivity().getSupportFragmentManager().beginTransaction();
		Fragment fragment = new LabelViewFragment();
		Bundle args = new Bundle();
		args.putSerializable(LabelViewFragment.ARG_LABEL, content);
		fragment.setArguments(args);
		// Replacing old fragment with new one
		ft.replace(R.id.fragment_container, fragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}

	private class RemoveLabelProcessor extends AbstractAsyncTaskProcessor<LabelObject, LabelObject> {

		public RemoveLabelProcessor(Activity activity) {
			super(activity);
		}

		@Override
		public LabelObject performAction(LabelObject ... params) throws SecurityException, Exception {
			if (CommunicatorHelper.removeLabel(params[0])) return params[0];
			return null;
		}

		@Override
		public void handleResult(LabelObject result) {
			if (result != null) {
				   ((LabelAdapter)getListAdapter()).remove(result);
				   ((LabelAdapter)getListAdapter()).notifyDataSetChanged();
			}
		}
		
	} 

}
