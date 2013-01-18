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

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.model.Channel;
import eu.trentorise.smartcampus.communicator.model.CommunicatorConstants;

public class ChannelAdapter extends ArrayAdapter<Channel> {

	private Activity context;
	private int layoutResourceId;

	public ChannelAdapter(Activity context, int layoutResourceId, List<Channel> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Channel tag = getItem(position);


		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			row.setTag(tag);
		}

		((TextView)row.findViewById(R.id.channel_name)).setText(tag.getTitle());
		ImageView img = (ImageView)row.findViewById(R.id.channel_img);
		img.setImageDrawable(CommunicatorConstants.getChannelTypeImage(getContext(), tag.getSourceType()));
		return row;
	}
	
}
