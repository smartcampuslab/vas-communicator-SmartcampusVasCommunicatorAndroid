package eu.trentorise.smartcampus.communicator.fragments.funnels;

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
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import eu.trentorise.smartcampus.android.common.SCAsyncTask;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.AbstractAsyncTaskProcessor;
import eu.trentorise.smartcampus.communicator.custom.FunnelAdapter;
import eu.trentorise.smartcampus.communicator.custom.data.CommunicatorHelper;
import eu.trentorise.smartcampus.communicator.model.Funnel;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.SecurityException;

public class FunnelListFragment extends SherlockListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		return arg0.inflate(R.layout.funnels, arg1, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSherlockActivity().setTitle(R.string.title_funnels);
		
		FunnelAdapter adapter = new FunnelAdapter(getActivity(), R.layout.funnel, CommunicatorHelper.getFunnels());
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				viewFunnelMessages(((FunnelAdapter)getListAdapter()).getItem(position));
			}
		});
		
		registerForContextMenu(getListView());
	}

	
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		MenuItem item = menu.add(Menu.CATEGORY_SYSTEM, R.id.funnel_add, 1, R.string.funnel_add);
		item.setIcon(R.drawable.add);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.funnel_add:
			openFunnelForm(null);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(R.string.funnel_options_header);
		android.view.MenuInflater inflater = getSherlockActivity().getMenuInflater();
	    inflater.inflate(R.menu.funnel_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Funnel content = ((FunnelAdapter)getListAdapter()).getItem(info.position);
		return handleMenuItem(content, item.getItemId());
	}

	protected boolean handleMenuItem(final Funnel content, int itemId) {
		switch (itemId) {
		case R.id.funnel_option_edit:
			openFunnelForm(content);
			return true;
		case R.id.funnel_option_remove:
			AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
			builder.setMessage(R.string.funnel_remove_text)
			       .setCancelable(false)
			       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   new SCAsyncTask<Funnel, Void, Funnel>(getActivity(), new RemoveFunnelProcessor(getActivity())).execute(content);
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
		case R.id.funnel_option_view:
			viewFunnelMessages(content);
			return true;
		default:
			return false;
		}
	}

	protected void viewFunnelMessages(Funnel content) {
		FragmentTransaction ft  = getSherlockActivity().getSupportFragmentManager().beginTransaction();
		Fragment fragment = new FunnelViewFragment();
		Bundle args = new Bundle();
		args.putSerializable(FunnelViewFragment.ARG_FUNNEL, content);
		fragment.setArguments(args);
		// Replacing old fragment with new one
		ft.replace(android.R.id.content, fragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}

	protected void openFunnelForm(Funnel content) {
		FragmentTransaction ft  = getSherlockActivity().getSupportFragmentManager().beginTransaction();
		Fragment fragment = new FunnelFormFragment();
		if (content != null) {
			Bundle args = new Bundle();
			args.putSerializable(FunnelFormFragment.ARG_FUNNEL, CommunicatorHelper.getFunnel(content.getId()));
			fragment.setArguments(args);
		}
		// Replacing old fragment with new one
		ft.replace(android.R.id.content, fragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}

	private class RemoveFunnelProcessor extends AbstractAsyncTaskProcessor<Funnel, Funnel> {

		public RemoveFunnelProcessor(Activity activity) {
			super(activity);
		}

		@Override
		public Funnel performAction(Funnel... params) throws SecurityException, Exception {
			if (CommunicatorHelper.removeFunnel(params[0])) return params[0];
			return null;
		}

		@Override
		public void handleResult(Funnel result) {
			if (result != null) {
				   ((FunnelAdapter)getListAdapter()).remove(result);
				   ((FunnelAdapter)getListAdapter()).notifyDataSetChanged();
			}
		}
		
	} 
	
}
