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
import android.widget.TextView;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.model.LabelObject;

public class LabelAdapter extends ArrayAdapter<LabelObject> {

	private Activity context;
	private int layoutResourceId;

	public LabelAdapter(Activity context, int layoutResourceId, List<LabelObject> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LabelObject tag = null;

		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			row.setTag(getItem(position));
		}
		tag = getItem(position);

		((TextView)row.findViewById(R.id.label_name)).setText(tag.getName());
		View color = row.findViewById(R.id.label_color);
		color.setBackgroundColor(Long.decode(tag.getColor()).intValue());
		return row;
	}
	
}
