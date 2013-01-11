package eu.trentorise.smartcampus.communicator.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.FunnelFilterRenderer;

public class JPFilterRenderer implements FunnelFilterRenderer {

	private static String[] sourceTypes = new String[]{
		"USER", "SERVICE"
	};

	private static String[] sources = new String[]{
		"User-defined", "Certified"
	};
	private static String[] types = new String[]{
		"Delays", "Strikes", "Parkings", "Road blocks"
	};

	
	private ListView sourceListView = null;
	private ListView typeListView = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public void setUpView(AlertDialog dlg, Activity ctx, View view, Map<String, Object> funnelFilterData) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.checked_list_view, sources);
		sourceListView = (ListView)view.findViewById(R.id.jp_filter_source_list);
		sourceListView.setAdapter(adapter);
		sourceListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

		adapter = new ArrayAdapter<String>(ctx, R.layout.checked_list_view, types);
		typeListView = (ListView)view.findViewById(R.id.jp_filter_type_list);
		typeListView.setAdapter(adapter);
		typeListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		
		List<String> selectedSources = funnelFilterData != null && funnelFilterData.get("sources") != null ? (List<String>)funnelFilterData.get("sources") : new ArrayList<String>(0);  
		List<String> selectedTypes = funnelFilterData != null && funnelFilterData.get("types") != null ? (List<String>)funnelFilterData.get("types") : new ArrayList<String>(0);  
		
		for (int i = 0; i < sourceListView.getCount(); i++) {
			sourceListView.setItemChecked(i, selectedSources.contains(sourceTypes[i]));
		}
		for (int i = 0; i < typeListView.getCount(); i++) {
			typeListView.setItemChecked(i, selectedTypes.contains(types[i].toLowerCase()));
		}
		
		dlg.show();
	}

	@Override
	public Map<String, Object> validate(View view) {
		List<String> sourceList = new ArrayList<String>();
		for (int i = 0; i < sourceListView.getCount(); i++) {
			if (sourceListView.isItemChecked(i)) {
				sourceList.add(sourceTypes[i]);
			}
		}
		List<String> typeList = new ArrayList<String>();
		for (int i = 0; i < typeListView.getCount(); i++) {
			if (typeListView.isItemChecked(i)) {
				typeList.add(types[i].toLowerCase());
			}
		}
		Map<String,Object> res = new HashMap<String, Object>();
		res.put("sources", sourceList);
		res.put("types", typeList);
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CharSequence getFilterDataDescription(Map<String, Object> funnelFilterData) {
		String res = "";
		if (funnelFilterData == null) return res;
		if (funnelFilterData.containsKey("types")) {
			String s = "";
			for (String k : (List<String>)funnelFilterData.get("types")) {
				s += (s.length() > 0 ? ", ":"") + k;
			}
			res += "Types: "+s;
		}	
		if (funnelFilterData.containsKey("sources")) {
			String s = "";
			for (String k : (List<String>)funnelFilterData.get("sources")) {
				s += (s.length() > 0 ? ", ":"") + k;
			}
			res += (res.length() > 0 ? "\n":"") + "Source: "+s;
		}	
		return res;	
	}
}
