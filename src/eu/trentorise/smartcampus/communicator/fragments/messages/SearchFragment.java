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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import it.smartcampuslab.communicator.R;
import eu.trentorise.smartcampus.communicator.model.NotificationFilter;

public class SearchFragment extends AbstractMessageListFragment {

	
	@Override
	protected boolean loadOnStart() {
		return false;
	}

	@Override
	public void onStart() {
		super.onStart();
		ImageButton img = (ImageButton)getView().findViewById(R.id.message_search_img);
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText txt = (EditText) getView().findViewById(R.id.messages_search);
				if (txt != null && txt.getText() != null) {
					filter.setSearchText(txt.getText().toString().trim());
				}
				position = 0;
				load();
			}
		});
	}

	@Override
	protected CharSequence getTitle() {
		return getString(R.string.title_search);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.messages_search, container, false);
	}

	@Override
	protected NotificationFilter initFilter() {
		NotificationFilter filter = new NotificationFilter();
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
