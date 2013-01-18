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

import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.model.NotificationFilter;

public class StarredFragment extends AbstractMessageListFragment {

	@Override
	protected CharSequence getTitle() {
		return getString(R.string.title_starred);
	}

	@Override
	protected NotificationFilter initFilter() {
		NotificationFilter filter = new NotificationFilter();
		filter.setStarred(true);
		return filter;
	}
	@Override
	protected boolean hasLabelSelector() {
		return false;
	}
	@Override
	protected boolean isLabelSelectorEnabled() {
		return false;
	}
}
