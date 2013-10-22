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
package eu.trentorise.smartcampus.communicator.custom;

import android.app.Service;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.data.CommunicatorHelper;
import eu.trentorise.smartcampus.communicator.custom.data.UpdateReadCountTask;
import eu.trentorise.smartcampus.communicator.fragments.channels.ChannelListFragment;
import eu.trentorise.smartcampus.communicator.fragments.channels.FeedListFragment;
import eu.trentorise.smartcampus.communicator.fragments.labels.LabelListFragment;
import eu.trentorise.smartcampus.communicator.fragments.messages.InboxFragment;
import eu.trentorise.smartcampus.communicator.fragments.messages.SearchFragment;
import eu.trentorise.smartcampus.communicator.fragments.messages.StarredFragment;

public class MainAdapter extends BaseAdapter {
	private Context context;
	private FragmentManager fragmentManager;

	public MainAdapter(Context c) {
		this.context = c;
	}

	public MainAdapter(Context applicationContext,
			FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
		this.context = applicationContext;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//View view = null;
        ViewHolder holder = new ViewHolder();

		if (convertView == null) {
			if (position == 0) {
		    	Integer unread = null;
				holder.view = ((LayoutInflater)context.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tumb_view, null);
		    	int resource = R.drawable.inbox;
		    	String txt = null;
		    	if ((unread = CommunicatorHelper.getUnreadCount()) != null) {
		    		if (unread > 0) {
			    		resource = R.drawable.inbox_alert;
			    		txt = ""+unread;
		    		}
		    	} else {
			    	new UpdateReadCountTask(holder.view).execute();
		    	}
		    	((TextView)holder.view.findViewById(R.id.numberView)).setText(txt);
		    	holder.view.findViewById(R.id.containerView).setBackgroundResource(resource);
			} else {
				TextView tmp = new TextView(context);
				tmp.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
				tmp.setCompoundDrawablesWithIntrinsicBounds(null, context.getResources().getDrawable(ACTIONS[position].thumbnail),
						null, null);
				tmp.setText(ACTIONS[position].description);
				tmp.setTextColor(context.getResources().getColor(
						R.color.sc_dark_gray));
				tmp.setGravity(Gravity.CENTER);
				holder.view = tmp;
			}
			holder.view.setOnClickListener(new CommunicatorOnClickListener(position));

		} else {
			holder.view = convertView;
			if (position == 0) {
		    	Integer unread = null;
				holder.view = ((LayoutInflater)context.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tumb_view, null);
		    	int resource = R.drawable.inbox;
		    	String txt = null;
		    	if ((unread = CommunicatorHelper.getUnreadCount()) != null) {
		    		if (unread > 0) {
			    		resource = R.drawable.inbox_alert;
			    		txt = ""+unread;
		    		}
		    	} else {
			    	new UpdateReadCountTask(holder.view).execute();
		    	}
		    	((TextView)holder.view.findViewById(R.id.numberView)).setText(txt);
		    	holder.view.findViewById(R.id.containerView).setBackgroundResource(resource);
			} else {
				TextView tmp = new TextView(context);
				tmp.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
				tmp.setCompoundDrawablesWithIntrinsicBounds(null, context.getResources().getDrawable(ACTIONS[position].thumbnail),
						null, null);
				tmp.setText(ACTIONS[position].description);
				tmp.setTextColor(context.getResources().getColor(
						R.color.sc_dark_gray));
				tmp.setGravity(Gravity.CENTER);
				holder.view = tmp;
			}
			holder.view.setOnClickListener(new CommunicatorOnClickListener(position));
		}

		return holder.view;
	}

	static class ViewHolder {
		View view;
	}
	
	public class CommunicatorOnClickListener implements OnClickListener {
		int position;
		
		public CommunicatorOnClickListener(int position) {
			this.position=position;
	}

		@Override
		public void onClick(View v) {
			// Starting transaction
			FragmentTransaction ft = fragmentManager.beginTransaction();
			Fragment fragment = (Fragment) Fragment.instantiate(
					context, ACTIONS[position].fragmentClass.getName());
			// Replacing old fragment with new one
			ft.replace(android.R.id.content, fragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(null);
			ft.commit();

		}
	}
		
	
	
	
	@Override
	public int getCount() {
		return ACTIONS.length;
	}

	@Override
	public Object getItem(int arg0) {
		return ACTIONS[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public static class MainActionDescriptor {
		public int description;
		public int thumbnail;
		public Class<? extends Fragment> fragmentClass;

		public MainActionDescriptor(int description, int thumbnail,
				Class<? extends Fragment> fragmentClass) {
			super();
			this.description = description;
			this.thumbnail = thumbnail;
			this.fragmentClass = fragmentClass;
		}
	}

	private static MainActionDescriptor[] ACTIONS = new MainActionDescriptor[] {
			new MainActionDescriptor(R.string.mainmenu_home, R.drawable.inbox, InboxFragment.class),
			new MainActionDescriptor(R.string.mainmenu_starred, R.drawable.starred, StarredFragment.class),
			//new MainActionDescriptor(R.string.mainmenu_channels, R.drawable.channels, ChannelListFragment.class),
			new MainActionDescriptor(R.string.mainmenu_feeds, R.drawable.sbscrptn, FeedListFragment.class),
			new MainActionDescriptor(R.string.mainmenu_labels, R.drawable.lables, LabelListFragment.class),
			new MainActionDescriptor(R.string.mainmenu_search, R.drawable.search, SearchFragment.class), 
	};
}
