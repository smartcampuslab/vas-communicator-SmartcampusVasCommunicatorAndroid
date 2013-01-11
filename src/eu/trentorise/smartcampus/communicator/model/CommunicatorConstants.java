package eu.trentorise.smartcampus.communicator.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.FunnelFilterRenderer;


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
	
	private static TreeMap<String,String> labels = new TreeMap<String, String>();
	private static TreeMap<String,Drawable> images = new TreeMap<String, Drawable>();
	private static TreeMap<String,FunnelFilterRenderer> renderers = new TreeMap<String, FunnelFilterRenderer>();
	private static TreeMap<String,Integer> layouts = new TreeMap<String, Integer>();

	private static List<Action> defaultActions = new ArrayList<Action>();
	
	@SuppressWarnings("unchecked")
	private static void init(Context ctx) {
		String[] typeArr = ctx.getResources().getStringArray(R.array.funnel_type_sourcetypes);
		String[] labelArr = ctx.getResources().getStringArray(R.array.funnel_type_labels);
		TypedArray icons = ctx.getResources().obtainTypedArray(R.array.funnel_type_images);
		String[] renderArr = ctx.getResources().getStringArray(R.array.funnel_type_renderers);
		TypedArray layoutArr = ctx.getResources().obtainTypedArray(R.array.funnel_type_layouts);
		for (int i = 0; i < typeArr.length; i++) {
			labels.put(typeArr[i], labelArr[i]);
			images.put(typeArr[i], icons.getDrawable(i));
			String rendererClass = renderArr[i];
			if (rendererClass != null) {
				try {
					Class<FunnelFilterRenderer> cls = (Class<FunnelFilterRenderer>)Class.forName(rendererClass);
					renderers.put(typeArr[i], cls.newInstance());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			layouts.put(typeArr[i], layoutArr.getResourceId(i, -1));
		}
		
		String[] defaultActionLabels = ctx.getResources().getStringArray(R.array.funnel_default_action_labels);
		String[] defaultActionValues = ctx.getResources().getStringArray(R.array.funnel_default_action_values);
		TypedArray defaultActionTypes = ctx.getResources().obtainTypedArray(R.array.funnel_default_action_types);
		for (int j = 0; j < defaultActionLabels.length; j++) {
			Action a = new Action();
			a.setLabel(defaultActionLabels[j]);
			a.setValue(defaultActionValues[j]);
			a.setType(defaultActionTypes.getInt(j, 0));
			defaultActions.add(a);
		}
	}
	public static String getFunnelTypeLabel(Context ctx, String sourceType) {
		if (labels.isEmpty()) init(ctx);
		return labels.get(sourceType);
	}
	public static Drawable getFunnelTypeImage(Context ctx, String sourceType) {
		if (labels.isEmpty()) init(ctx);
		return images.get(sourceType);
	}
	public static FunnelFilterRenderer getFunnelTypeRenderer(Context ctx, String sourceType) {
		if (labels.isEmpty()) init(ctx);
		return renderers.get(sourceType);
	}
	public static int getFunnelTypeLayout(Context ctx, String sourceType) {
		if (labels.isEmpty()) init(ctx);
		return layouts.get(sourceType);
	}
	public static TreeMap<String,String> getFunnelTypes(Context ctx) {
		if (labels.isEmpty()) init(ctx);
		return labels;
	}
	public static List<Action> getDefaultFunnelActions(Context ctx) {
		if (labels.isEmpty()) init(ctx);
		return defaultActions;
	}
}
