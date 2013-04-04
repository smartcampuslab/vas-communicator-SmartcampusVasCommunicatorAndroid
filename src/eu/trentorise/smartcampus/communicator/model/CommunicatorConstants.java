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
import java.util.TreeMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.NotificationProcessor;


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
	
	private static String[][] FEED_SOURCES = new String[][]{
		new String[]{"Opera Universitaria","Opera Universitaria"}, 
		new String[]{"Cisca","Cisca"}, 
		new String[]{"Ateneo","Ateneo"}, 
		new String[]{"Scienze", "Scienze"},
		new String[]{"Ingegneria", "Ingegneria"},
		new String[]{"Unisport","Unisport"}, 
		new String[]{"DISI","DISI"}
	};
	
	private static TreeMap<String,String> labels = new TreeMap<String, String>();
	private static TreeMap<String,Drawable> images = new TreeMap<String, Drawable>();
	private static TreeMap<String,NotificationProcessor> processors = new TreeMap<String, NotificationProcessor>();
	private static TreeMap<String,Integer> layouts = new TreeMap<String, Integer>();
	private static TreeMap<String,String> colors = new TreeMap<String, String>();

	private static List<Action> defaultActions = new ArrayList<Action>();
	
	@SuppressWarnings("unchecked")
	private static void init(Context ctx) {
		String[] typeArr = ctx.getResources().getStringArray(R.array.channel_type_sourcetypes);
		String[] labelArr = ctx.getResources().getStringArray(R.array.channel_type_labels);
		TypedArray icons = ctx.getResources().obtainTypedArray(R.array.channel_type_images);
		String[] processorArr = ctx.getResources().getStringArray(R.array.message_processors);
		TypedArray layoutArr = ctx.getResources().obtainTypedArray(R.array.channel_type_layouts);
		String[] colorArr =  ctx.getResources().getStringArray(R.array.channel_type_colors);
		for (int i = 0; i < typeArr.length; i++) {
			labels.put(typeArr[i], labelArr[i]);
			images.put(typeArr[i], icons.getDrawable(i));
			colors.put(typeArr[i], colorArr[i]);
			String rendererClass = processorArr[i];
			if (rendererClass != null) {
				try {
					Class<NotificationProcessor> cls = (Class<NotificationProcessor>)Class.forName(rendererClass);
					processors.put(typeArr[i], cls.newInstance());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			layouts.put(typeArr[i], layoutArr.getResourceId(i, -1));
		}
		
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
	}
	public static String getChannelTypeLabel(Context ctx, String sourceType) {
		if (labels.isEmpty()) init(ctx);
		if (sourceType == null) return null;
		String label = labels.get(sourceType);
		if (label == null) label = sourceType;
		return label;
	}
	public static Drawable getChannelTypeImage(Context ctx, String sourceType) {
		if (labels.isEmpty()) init(ctx);
		Drawable d = null;
		
		if (sourceType == null || (d = images.get(sourceType)) == null) d = ctx.getResources().getDrawable(R.drawable.ic_channel);
		return d;
	}
	public static String getChannelTypeColor(Context ctx, String sourceType) {
		if (labels.isEmpty()) init(ctx);
		String color = null;
		if (sourceType == null || (color = colors.get(sourceType)) == null)color = "#333333"; 
		return color;
	}
//	public static ChannelFilterRenderer getChannelTypeRenderer(Context ctx, String sourceType) {
//		if (labels.isEmpty()) init(ctx);
//		return processors.get(sourceType);
//	}
//	public static int getChannelTypeLayout(Context ctx, String sourceType) {
//		if (labels.isEmpty()) init(ctx);
//		return layouts.get(sourceType);
//	}
	public static NotificationProcessor getNotificationProcessor(Context ctx, String sourceType) {
		if (labels.isEmpty()) init(ctx);
		return processors.get(sourceType);
	}
	
	private static String[][] labelsArray = null;
	public static String[][] getChannelLabels(Context ctx) {
		if (labels.isEmpty()) init(ctx);
		if (labelsArray == null) {
			labelsArray = new String[labels.size()][];
			int i = 0;
			for (String key : labels.keySet()) {
				labelsArray[i] = new String[]{key, labels.get(key)};
				i++;
			}
		}
		return labelsArray;
	}
	
	public static String[][] getFeedLabels() {
		return FEED_SOURCES;
	}
	
	public static List<Action> getDefaultChannelActions(Context ctx) {
		if (labels.isEmpty()) init(ctx);
		return defaultActions;
	}
}
