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

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import it.smartcampuslab.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.data.CommunicatorHelper;
import eu.trentorise.smartcampus.communicator.model.CommunicatorConstants;
import eu.trentorise.smartcampus.communicator.model.LabelObject;
import eu.trentorise.smartcampus.communicator.model.Notification;

public class MessageAdapter extends ArrayAdapter<Notification> {

	private Activity context;
	private int layoutResourceId;

	public MessageAdapter(Activity context, int layoutResourceId, ArrayList<Notification> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Notification tag = null;

		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			row.setTag(getItem(position));
		}
		tag = getItem(position);

		((TextView)row.findViewById(R.id.message_title)).setText(tag.getTitle());
		LinearLayout ll = (LinearLayout) row.findViewById(R.id.message_labels);
		ll.removeAllViews();
		
		View sourceView = context.getLayoutInflater().inflate(R.layout.label_label, null);
		TextView source = (TextView) sourceView.findViewById(R.id.label_label_text);
		source.setText(CommunicatorConstants.getChannelTypeLabel(getContext(), tag.getType()));
		ll.addView(sourceView);
		

//		if (tag.getChannelIds() != null && tag.getChannelIds().size() > 0) {
//			for (int i = 0; i < tag.getChannelIds().size(); i++) {
//				Channel lo = CommunicatorHelper.getChannel(tag.getChannelIds().get(i));
//				if (lo == null) continue;
//				View v = context.getLayoutInflater().inflate(R.layout.label_label, null);
//				TextView tv = (TextView) v.findViewById(R.id.label_label_text);
//				tv.setTextColor(context.getResources().getColor(R.color.sc_dark_gray));
//				tv.setText(lo.getTitle());
//				ll.addView(v);
//			}
//		}
		if (tag.getLabelIds() != null && tag.getLabelIds().size() > 0) {
			for (int i = 0; i < tag.getLabelIds().size(); i++) {
				LabelObject lo = CommunicatorHelper.getLabel(tag.getLabelIds().get(i));
				if (lo == null) continue;
				View v = context.getLayoutInflater().inflate(R.layout.label_label, null);
				TextView tv = (TextView) v.findViewById(R.id.label_label_text);
				tv.setTextColor(context.getResources().getColor(R.color.sc_gray));
				tv.setText(lo.getName());
				tv.setBackgroundColor(Long.decode(lo.getColor()).intValue());
				ll.addView(v);
			}
		}
		((TextView)row.findViewById(R.id.message_date)).setText(CommunicatorConstants.DATE_TIME_FORMAT.format(new Date(tag.getTimestamp())));
		((ImageView)row.findViewById(R.id.message_starred)).setImageResource(
				tag.isStarred() ? R.drawable.star_s : R.drawable.star);
		row.findViewById(R.id.message_row).setBackgroundResource(
				tag.isReaded() ? R.color.message_read : R.color.message_notread);
		return row;
	}
	
}
