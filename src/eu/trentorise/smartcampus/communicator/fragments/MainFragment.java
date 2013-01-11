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

import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.MainAdapter;
import eu.trentorise.smartcampus.communicator.preferences.SettingsActivity;

public class MainFragment extends SherlockFragment {

	private GridView gridview;
	private FragmentManager fragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getSherlockActivity().getSupportFragmentManager();
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Showing/hiding back button
		getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(false);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSherlockActivity().getSupportActionBar().setDisplayShowTitleEnabled(true);
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
		gridview = (GridView) getView().findViewById(R.id.gridview);
		gridview.setAdapter(new MainAdapter(getSherlockActivity().getApplicationContext(), fragmentManager));
//		
//		View inbox =  getView().findViewById(R.id.btn_inbox);
//    	int unread = CommunicatorHelper.getUnreadCount();
//    	int resource = R.drawable.inbox;
//    	String txt = "";
//    	if (unread != 0) {
//    		resource = R.drawable.inbox_alert;
//    		txt = ""+unread;
//    	}
//    	((TextView)inbox.findViewById(R.id.numberView)).setText(txt);
//    	inbox.findViewById(R.id.containerView).setBackgroundResource(resource);
//
//		inbox.setOnClickListener(buttonListener);
//		getView().findViewById(R.id.btn_funnels).setOnClickListener(buttonListener);
//		getView().findViewById(R.id.btn_labels).setOnClickListener(buttonListener);
//		getView().findViewById(R.id.btn_starred).setOnClickListener(buttonListener);
//		getView().findViewById(R.id.btn_search).setOnClickListener(buttonListener);
	}

//	private View.OnClickListener buttonListener = new View.OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.btn_inbox:
//				openFragment(InboxFragment.class.getName());
//				break;
//			case R.id.btn_starred:
//				openFragment(StarredFragment.class.getName());
//				break;
//			case R.id.btn_labels:
//				openFragment(LabelListFragment.class.getName());
//				break;
//			case R.id.btn_funnels:
//				openFragment(FunnelListFragment.class.getName());
//				break;
//			case R.id.btn_search:
//				openFragment(SearchFragment.class.getName());
//				break;
//			default:
//				break;
//			}
//		}
//	};
//	
//	private void openFragment(String cName) {
//		FragmentTransaction ft  = fragmentManager.beginTransaction();
//		Fragment fragment = (Fragment) Fragment.instantiate(getSherlockActivity(), cName);
//		// Replacing old fragment with new one
//		ft.replace(android.R.id.content, fragment);
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//		ft.addToBackStack(null);
//		ft.commit();
//	}
}
