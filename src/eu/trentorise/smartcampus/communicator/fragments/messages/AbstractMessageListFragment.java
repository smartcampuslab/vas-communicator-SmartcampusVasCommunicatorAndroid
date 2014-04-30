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
package eu.trentorise.smartcampus.communicator.fragments.messages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import eu.trentorise.smartcampus.android.common.SCAsyncTask;
import eu.trentorise.smartcampus.android.common.SCAsyncTask.SCAsyncTaskProcessor;
import eu.trentorise.smartcampus.android.common.listing.AbstractLstingFragment;
import it.smartcampuslab.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.AbstractAsyncTaskProcessor;
import eu.trentorise.smartcampus.communicator.custom.MessageAdapter;
import eu.trentorise.smartcampus.communicator.custom.data.CommunicatorHelper;
import eu.trentorise.smartcampus.communicator.model.CommunicatorConstants.ORDERING;
import eu.trentorise.smartcampus.communicator.model.LabelObject;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.communicator.model.NotificationFilter;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.SecurityException;

public abstract class AbstractMessageListFragment extends
		AbstractLstingFragment<Notification> {

	private static final String ARG_MESSAGES = "messages";
	private static final String ARG_POSITION = "pos";
	private static final String ARG_LAST = "last";

	protected NotificationFilter filter = null;

	protected abstract NotificationFilter initFilter();

	protected abstract boolean hasLabelSelector();

	protected abstract boolean isLabelSelectorEnabled();

	protected abstract CharSequence getTitle();

	protected ArrayList<Notification> list = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putSerializable(ARG_MESSAGES, list);
		savedInstanceState.putInt(ARG_POSITION, position);
		savedInstanceState.putInt(ARG_LAST, lastSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			list = (ArrayList<Notification>) savedInstanceState
					.get(ARG_MESSAGES);
			position = savedInstanceState.getInt(ARG_POSITION);
			lastSize = savedInstanceState.getInt(ARG_LAST);
		}
		return inflater.inflate(R.layout.messages, container, false);
	}

	@Override
	public void onStart() {
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {

				registerForContextMenu(getListView());
				
				if (filter == null) {
					filter = initFilter();
				}
				
				getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						viewMessage(getAdapter().getItem(position));
					}
				});
				
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				
				super.onPostExecute(result);
				setupSelectors();
			}
		}.execute();
		
		super.onStart();
	}

	@Override
	protected void initData() {
		if (list == null) {
			list = new ArrayList<Notification>();
			setAdapter(new MessageAdapter(getActivity(), R.layout.message, list));
			super.initData();
		} else {
			setAdapter(new MessageAdapter(getActivity(), R.layout.message, list));
			for (Iterator<Notification> iterator = list.iterator(); iterator
					.hasNext();) {
				Notification n = iterator.next();
				if (n.markedDeleted())
					iterator.remove();
			}
			getListView().setAdapter(adapter);
			getListView().setOnScrollListener(this);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(
				true);
		getSherlockActivity().getSupportActionBar().setDisplayShowTitleEnabled(
				true);
		getSherlockActivity().setTitle(getTitle());

	}

	@SuppressWarnings("unchecked")
	protected void setupSelectors() {
		if (hasLabelSelector()) {
			if (isLabelSelectorEnabled()) {
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						getActivity(), android.R.layout.simple_spinner_item);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				final Spinner labelFilter = (Spinner) getView().findViewById(
						R.id.messages_spinner_labels);
				labelFilter.setAdapter(dataAdapter);
				for (String n : CommunicatorHelper.getLabelsForSelector()) {
					((ArrayAdapter<String>) labelFilter.getAdapter()).add(n);
				}

				labelFilter
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								if (position == 0
										&& filter.getLabelId() == null)
									return;
								LabelObject selected = null;
								try {
									String selectedString = (String) labelFilter
											.getAdapter().getItem(position);
									selected = CommunicatorHelper
											.getLabelByName(selectedString);

									if (selected != null) {
										if (selected.getId().equals(
												filter.getLabelId()))
											return;
										filter.setLabelId(selected.getId());
									} else
										filter.setLabelId(null);
								} catch (Exception e) {
									labelFilter.setSelection(0);
								}
								updateMessageList();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});
			} else {
				getView().findViewById(R.id.messages_spinner_labels)
						.setEnabled(false);
			}
		} else {
			((LinearLayout) getView().findViewById(R.id.messages_filters))
					.removeView(getView().findViewById(
							R.id.messages_spinner_labels));

		}

	}

	protected void updateMessageList() {
		position = 0;
		load();
	}

	@Override
	protected ListView getListView() {
		return (ListView) getView().findViewById(android.R.id.list);
	}

	@Override
	protected SCAsyncTaskProcessor<ListingRequest, List<Notification>> getLoader() {
		return new MessageLoader(getActivity());
	}

	private class MessageLoader
			extends
			AbstractAsyncTaskProcessor<AbstractLstingFragment.ListingRequest, List<Notification>> {

		public MessageLoader(Activity activity) {
			super(activity);
		}

		@Override
		public List<Notification> performAction(
				AbstractLstingFragment.ListingRequest... params)
				throws SecurityException, Exception {
			return CommunicatorHelper.getNotifications(filter,
					params[0].position, params[0].size, 0);
		}

		@Override
		public void handleResult(List<Notification> result) {
			updateListFooter(result == null || result.size() == 0);
		}

	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Notification content = adapter.getItem(info.position);
		return handleMenuItem(content, item.getItemId());
	}

	protected boolean handleMenuItem(Notification content, int itemId) {
		switch (itemId) {
		case R.id.notice_option_toggle_read:
			toggleRead(content);
			return true;
		case R.id.notice_option_toggle_starred:
			toggleStar(content);
			return true;
		case R.id.notice_option_remove:
			remove(content);
			return true;
		case R.id.notice_option_assign_labels:
			createLabelsDialog(content);
			return true;
		case R.id.notice_option_view:
			viewMessage(content);
			return true;
			// case R.id.notice_option_view_funnel:
			// viewFunnel(content);
			// return true;
		default:
			return false;
		}
	}

	protected void viewMessage(Notification content) {
		FragmentTransaction ft = getSherlockActivity()
				.getSupportFragmentManager().beginTransaction();
		Fragment fragment = new MessageDetailsFragment();
		Bundle args = new Bundle();
		args.putSerializable(MessageDetailsFragment.ARG_MSG, content);
		fragment.setArguments(args);
		// Replacing old fragment with new one
		ft.replace(R.id.fragment_container, fragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}

	// protected void viewFunnel(Notification content) {
	// FragmentTransaction ft =
	// getSherlockActivity().getSupportFragmentManager().beginTransaction();
	// Fragment fragment = new ChannelViewFragment();
	// Bundle args = new Bundle();
	// args.putSerializable(ChannelViewFragment.ARG_FUNNEL,
	// CommunicatorHelper.getFunnel(content.getFunnelId()));
	// fragment.setArguments(args);
	// // Replacing old fragment with new one
	// ft.replace(android.R.id.content, fragment);
	// ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	// ft.addToBackStack(null);
	// ft.commit();
	// }

	private void createLabelsDialog(final Notification content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				getSherlockActivity());
		final CharSequence[] items = CommunicatorHelper.getLabelNames();

		builder.setTitle(R.string.notice_option_assign_labels);
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new SCAsyncTask<Object, Void, Void>(getActivity(),
								new AssignLabelProcessor(getActivity()))
								.execute(content, items[which]);
						dialog.dismiss();
					}

				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.setPositiveButton(R.string.btn_new_label,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						LabelDialog d = new LabelDialog(getActivity(),
								new LabelDialog.OnLabelCreatedListener() {

									@Override
									public void OnLabelCreated(LabelObject label) {
										new SCAsyncTask<Object, Void, Void>(
												getActivity(),
												new AssignLabelProcessor(
														getActivity()))
												.execute(content,
														label.getName());
									}
								});
						d.setOwnerActivity(getActivity());
						d.show();
					}
				});
		builder.show();
	}

	private void remove(Notification content) {
		new SCAsyncTask<Notification, Void, Notification>(getActivity(),
				new RemoveNotificationProcessor(getActivity()))
				.execute(content);
	}

	private void toggleRead(Notification content) {
		new SCAsyncTask<Notification, Void, Void>(getActivity(),
				new ToggleReadProcessor(getActivity())).execute(content);
	}

	private void toggleStar(Notification content) {
		new SCAsyncTask<Notification, Void, Void>(getActivity(),
				new ToggleStarProcessor(getActivity())).execute(content);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(R.string.notice_options_header);
		android.view.MenuInflater inflater = getSherlockActivity()
				.getMenuInflater();
		inflater.inflate(R.menu.notice_menu, menu);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getSherlockActivity().getSupportMenuInflater().inflate(
				R.menu.notice_list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mainmenu_refresh:
			try {
				// CommunicatorHelper.synchronizeInBG();
				// load();
				new SCAsyncTask<Void, Void, List<Notification>>(getActivity(),
						new SyncProcessor(getActivity())).execute();
			} catch (Exception e) {
				Log.e(getClass().getName(), "" + e.getMessage());
			}
			return true;
		case R.id.noticelist_menu_mark_all_read:
			new SCAsyncTask<Void, Void, Void>(getActivity(),
					new MarkAllReadProcessor(getActivity())).execute();
			return true;
		case R.id.noticelist_menu_delete_all:
			new SCAsyncTask<Void, Void, Void>(getActivity(),
					new DeleteAllProcessor(getActivity())).execute();
			return true;
		case R.id.noticelist_menu_mark_order_arrival:
			filter.setOrdering(ORDERING.ORDER_BY_ARRIVAL);
			position = 0;
			load();
			return true;
			// case R.id.noticelist_menu_mark_order_rel_now:
			// filter.setOrdering(ORDERING.ORDER_BY_REL_TIME);
			// position = 0;
			// load();
			// return true;
			// case R.id.noticelist_menu_mark_order_rel_place:
			// filter.setOrdering(ORDERING.ORDER_BY_REL_PLACE);
			// position = 0;
			// load();
			// return true;
			// case R.id.noticelist_menu_mark_order_priority:
			// filter.setOrdering(ORDERING.ORDER_BY_PRIORITY);
			// position = 0;
			// load();
			// return true;
		case R.id.noticelist_menu_mark_order_title:
			filter.setOrdering(ORDERING.ORDER_BY_TITLE);
			position = 0;
			load();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class ToggleStarProcessor extends
			AbstractAsyncTaskProcessor<Notification, Void> {
		public ToggleStarProcessor(Activity activity) {
			super(activity);
		}

		@Override
		public Void performAction(Notification... params)
				throws SecurityException, Exception {
			CommunicatorHelper.toggleStar(params[0]);
			return null;
		}

		@Override
		public void handleResult(Void result) {
			getAdapter().notifyDataSetChanged();
			updateListFooter(getAdapter().getCount() == 0);
		}
	}

	private class ToggleReadProcessor extends
			AbstractAsyncTaskProcessor<Notification, Void> {
		public ToggleReadProcessor(Activity activity) {
			super(activity);
		}

		@Override
		public Void performAction(Notification... params)
				throws SecurityException, Exception {
			CommunicatorHelper.toggleRead(params[0]);
			return null;
		}

		@Override
		public void handleResult(Void result) {
			getAdapter().notifyDataSetChanged();
			updateListFooter(getAdapter().getCount() == 0);
		}
	}

	private class RemoveNotificationProcessor extends
			AbstractAsyncTaskProcessor<Notification, Notification> {
		public RemoveNotificationProcessor(Activity activity) {
			super(activity);
		}

		@Override
		public Notification performAction(Notification... params)
				throws SecurityException, Exception {
			CommunicatorHelper.removeNotification(params[0]);
			return params[0];
		}

		@Override
		public void handleResult(Notification result) {
			getAdapter().remove(result);
		}
	}

	private class AssignLabelProcessor extends
			AbstractAsyncTaskProcessor<Object, Void> {
		public AssignLabelProcessor(Activity activity) {
			super(activity);
		}

		@Override
		public Void performAction(Object... params) throws SecurityException,
				Exception {
			CommunicatorHelper.assignLabel((Notification) params[0],
					(String) params[1]);
			return null;
		}

		@Override
		public void handleResult(Void result) {
			getAdapter().notifyDataSetChanged();
			updateListFooter(getAdapter().getCount() == 0);
		}
	}

	private class MarkAllReadProcessor extends
			AbstractAsyncTaskProcessor<Void, Void> {
		public MarkAllReadProcessor(Activity activity) {
			super(activity);
		}

		@Override
		public Void performAction(Void... params) throws SecurityException,
				Exception {
			CommunicatorHelper.markAllAsRead(filter);
			return null;
		}

		@Override
		public void handleResult(Void result) {
			for (int i = 0; i < getAdapter().getCount(); i++) {
				getAdapter().getItem(i).setReaded(true);
			}
			getAdapter().notifyDataSetChanged();
			updateListFooter(getAdapter().getCount() == 0);
		}
	}

	private class DeleteAllProcessor extends
			AbstractAsyncTaskProcessor<Void, Void> {
		public DeleteAllProcessor(Activity activity) {
			super(activity);
		}

		@Override
		public Void performAction(Void... params) throws SecurityException,
				Exception {
			CommunicatorHelper.deleteAll(filter);
			return null;
		}

		@Override
		public void handleResult(Void result) {
			getAdapter().clear();
			getAdapter().notifyDataSetChanged();
			updateListFooter(true);
		}
	}

	private void updateListFooter(boolean empty) {
		TextView view = (TextView) getView().findViewById(android.R.id.empty);
		if (empty) {
			if (view == null) {
				view = new TextView(getSherlockActivity());
				view.setPadding(5, 5, 5, 5);
				view.setText(R.string.empty_messages);
				view.setId(android.R.id.empty);
				((ViewGroup) getView()).addView(view);
			} else {
				view.setVisibility(View.VISIBLE);
			}

		} else if (view != null) {
			view.setVisibility(View.GONE);
		}
	}

	private class SyncProcessor extends
			AbstractAsyncTaskProcessor<Void, List<Notification>> {
		public SyncProcessor(Activity activity) {
			super(activity);
		}

		@Override
		public List<Notification> performAction(Void... params)
				throws SecurityException, Exception {
			long since = 0;
			if (getAdapter().getCount() > 0)
				since = getAdapter().getItem(0).getTimestamp();
			CommunicatorHelper.synchronize();
			return CommunicatorHelper.getNotifications(filter, position, size,
					since);
		}

		@Override
		public void handleResult(List<Notification> result) {
			if (result != null) {
				int i = 0;
				for (Notification o : result) {
					getAdapter().insert(o, i++);
				}
				position += i;
			}
			getAdapter().notifyDataSetChanged();
			updateListFooter(getAdapter().getCount() == 0);
		}
	}

}
