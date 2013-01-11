package eu.trentorise.smartcampus.communicator.renderers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.FunnelFilterRenderer;

public class ExternalServicesFilterRenderer implements FunnelFilterRenderer {

	private static String[] sources = new String[]{
		"Opera Universitaria", "Cisca", "Ateneo", "Scienze", "Ingegneria", "Unisport"
	};
	
	private ListView listView = null;
	EditText edit = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public void setUpView(AlertDialog dlg, Activity ctx, View view, Map<String, Object> funnelFilterData) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.checked_list_view, sources);
		listView = (ListView)view.findViewById(R.id.services_filter_sources);
		listView.setAdapter(adapter);
		listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		
		List<String> selected = funnelFilterData != null && funnelFilterData.get("sources") != null ? (List<String>)funnelFilterData.get("sources") : new ArrayList<String>(0);  
		List<String> keywords = funnelFilterData != null && funnelFilterData.get("keywords") != null ? (List<String>)funnelFilterData.get("keywords") : new ArrayList<String>(0);
		edit = (EditText)view.findViewById(R.id.services_filter_keywords);
		if (keywords != null) {
			String s = "";
			for (String k : keywords) {
				s += (s.length() > 0 ? " ":"") + k;
			}
			edit.setText(s);
		}
		
		for (int i = 0; i < listView.getCount(); i++) {
			listView.setItemChecked(i, selected.contains(sources[i]));
		}

		
		dlg.show();
	}

	@Override
	public Map<String, Object> validate(View view) {
		List<String> sourceList = new ArrayList<String>();
		for (int i = 0; i < listView.getCount(); i++) {
			if (listView.isItemChecked(i)) {
				sourceList.add(sources[i]);
			}
		}
		Map<String,Object> res = new HashMap<String, Object>();
		res.put("sources", sourceList);
		if (edit.getText() != null && edit.getText().length() > 0) {
			res.put("keywords", Arrays.asList(edit.getText().toString().split(" ")));
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CharSequence getFilterDataDescription(Map<String, Object> funnelFilterData) {
		String res = "";
		if (funnelFilterData == null) return res;
		if (funnelFilterData.containsKey("keywords")) {
			String s = "";
			for (String k : (List<String>)funnelFilterData.get("keywords")) {
				s += (s.length() > 0 ? ", ":"") + k;
			}
			res += "Keywords: "+s;
		}
		if (funnelFilterData.containsKey("sources")) {
			String s = "";
			for (String k : (List<String>)funnelFilterData.get("sources")) {
				s += (s.length() > 0 ? ", ":"") + k;
			}
			res += (res.length() > 0 ? "\n":"") + "Sources: "+s;
		}	
		return res;	
	}
}
