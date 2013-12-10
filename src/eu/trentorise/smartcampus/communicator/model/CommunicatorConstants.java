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
package eu.trentorise.smartcampus.communicator.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import eu.trentorise.smartcampus.communicator.R;


public class CommunicatorConstants {

	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MMM/yyyy '-' HH:mm");

	public enum ORDERING {
		ORDER_BY_ARRIVAL("arrival"), 
		ORDER_BY_REL_TIME("relevance now"), 
		ORDER_BY_REL_PLACE("relevance here"), 
		ORDER_BY_PRIORITY("priority"),
		ORDER_BY_TITLE("title");
		
		public String text;

		private ORDERING(String text) {
			this.text = text;
		}
	}
	
	private static String[][] FEED_SOURCES = null;
	
	private static List<Action> defaultActions = null;
	
	private static void init(Context ctx) {
		defaultActions = new ArrayList<Action>();
		String[] defaultActionLabels = ctx.getResources().getStringArray(R.array.channel_default_action_labels);
		String[] defaultActionValues = ctx.getResources().getStringArray(R.array.channel_default_action_values);
		TypedArray defaultActionTypes = ctx.getResources().obtainTypedArray(R.array.channel_default_action_types);
		for (int j = 0; j < defaultActionLabels.length; j++) {
			Action a = new Action();
			a.setLabel(defaultActionLabels[j]);
			a.setValue(defaultActionValues[j]);
			a.setType(defaultActionTypes.getInt(j, 0));
			defaultActions.add(a);
		}
		
		String[] feedSources = ctx.getResources().getStringArray(R.array.feed_sources);
		String[] feedLabels = ctx.getResources().getStringArray(R.array.feed_labels);
		
		FEED_SOURCES = new String[feedSources.length][2];
		for (int i = 0; i < feedSources.length; i++) {
			FEED_SOURCES[i] = new String[]{feedSources[i],feedLabels[i]};
		}
	}
	public static String getChannelTypeLabel(Context ctx, String sourceType) {
		return sourceType;
	}
	public static Drawable getChannelTypeImage(Context ctx, String sourceType) {
		Drawable d = ctx.getResources().getDrawable(R.drawable.ic_channel);
		return d;
	}
	
	public static String[][] getFeedLabels(Context ctx) {
		if (defaultActions == null) init(ctx);
		return FEED_SOURCES;
	}
	
	public static List<Action> getDefaultChannelActions(Context ctx) {
		if (defaultActions == null) init(ctx);
		return defaultActions;
	}
}
