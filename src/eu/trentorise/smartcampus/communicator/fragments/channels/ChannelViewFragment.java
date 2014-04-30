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
package eu.trentorise.smartcampus.communicator.fragments.channels;

import android.os.Bundle;
import it.smartcampuslab.communicator.R;
import eu.trentorise.smartcampus.communicator.fragments.messages.AbstractMessageListFragment;
import eu.trentorise.smartcampus.communicator.model.Channel;
import eu.trentorise.smartcampus.communicator.model.NotificationFilter;

public class ChannelViewFragment extends AbstractMessageListFragment {

	private Channel channel = null;
	public static final String ARG_CHANNEL = "channel";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		channel = (Channel)getArguments().getSerializable(ARG_CHANNEL);
	}

	@Override
	protected CharSequence getTitle() {
		return getResources().getString(channel.isFeed() ? R.string.feed : R.string.channel) +": "+(channel == null ? "" : channel.getTitle());
	}

	@Override
	protected NotificationFilter initFilter() {
		NotificationFilter filter = new NotificationFilter();
		filter.setChannelId(channel == null? null : channel.getId());
		return filter;
	}
	@Override
	protected boolean hasLabelSelector() {
		return true;
	}
	@Override
	protected boolean isLabelSelectorEnabled() {
		return true;
	}
}
