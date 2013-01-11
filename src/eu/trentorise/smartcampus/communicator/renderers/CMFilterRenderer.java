package eu.trentorise.smartcampus.communicator.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import eu.trentorise.smartcampus.android.common.GlobalConfig;
import eu.trentorise.smartcampus.android.common.SCAsyncTask;
import eu.trentorise.smartcampus.android.common.Utils;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.AbstractAsyncTaskProcessor;
import eu.trentorise.smartcampus.communicator.custom.FunnelFilterRenderer;
import eu.trentorise.smartcampus.communicator.custom.data.CommunicatorHelper;
import eu.trentorise.smartcampus.communicator.custom.data.Constants;
import eu.trentorise.smartcampus.protocolcarrier.ProtocolCarrier;
import eu.trentorise.smartcampus.protocolcarrier.common.Constants.Method;
import eu.trentorise.smartcampus.protocolcarrier.custom.MessageRequest;
import eu.trentorise.smartcampus.protocolcarrier.custom.MessageResponse;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.SecurityException;

public class CMFilterRenderer implements FunnelFilterRenderer {

	private static final String CM_TOPIC_ADDRESS = "/smartcampus.vas.community-manager.web/eu.trentorise.smartcampus.cm.model.Topic";
	
	private Activity ctx;
	private ArrayAdapter<String> adapter = null;
//	private TopicAdapter adapter = null;
	private List<Topic> topicList = null;
	private List<Object> selected = null;
	private ListView listView = null;
	private AlertDialog dlg = null;
	
	@Override
	public void setUpView(AlertDialog dlg, Activity ctx, View view, Map<String, Object> funnelFilterData) {
		this.ctx = ctx;
		this.dlg = dlg;
//		this.adapter = new ArrayAdapter<String>(ctx, R.layout.checklist_row, R.id.checklist_checkBox);
		listView = (ListView)view.findViewById(R.id.cmfilter_list);
//		listView.setAdapter(adapter);
		selected = funnelFilterData != null && funnelFilterData.get("topics") != null ? (List<Object>)funnelFilterData.get("topics") : null;  
		if (selected != null)
			for (int i = 0; i < selected.size(); i++) {
				selected.set(i, selected.get(i).toString());
			}

		new SCAsyncTask<Void, Void, List<Topic>>(ctx, new TopicListLoader(ctx)).execute();
	}

	@Override
	public Map<String, Object> validate(View view) {
		List<Long> idList = new ArrayList<Long>();
		String description = "";
		for (int i = 0; i < listView.getCount(); i++) {
//			CheckedTextView child = (CheckedTextView)listView.getChildAt(i);
//			CheckBox chk = (CheckBox)child.findViewById(R.id.checklist_checkBox);
			if (listView.isItemChecked(i)) {
				idList.add(topicList.get(i).getSocialId());
				description += (description.length() > 0 ? ", ":"")+topicList.get(i).getName();
			}
		}
		Map<String,Object> res = new HashMap<String, Object>();
		res.put("topics", idList);
		res.put("description", description);
		return res;
	}

	@Override
	public CharSequence getFilterDataDescription(Map<String, Object> funnelFilterData) {
		if (funnelFilterData == null || !funnelFilterData.containsKey("description")) {
			return "";
		}  
		return funnelFilterData.get("description").toString();
	}

	private class TopicListLoader extends AbstractAsyncTaskProcessor<Void, List<Topic>> {

		public TopicListLoader(Activity activity) {
			super(activity);
		}

		@Override
		public List<Topic> performAction(Void... params) throws SecurityException, Exception {
			MessageRequest req = new MessageRequest(GlobalConfig.getAppUrl(ctx), CM_TOPIC_ADDRESS);
			req.setMethod(Method.GET);
			MessageResponse res = new ProtocolCarrier(ctx, Constants.APP_TOKEN).invokeSync(req, Constants.APP_TOKEN, CommunicatorHelper.getAuthToken());
			return Utils.convertJSONToObjects(res.getBody(), Topic.class);
		}

		@Override
		public void handleResult(List<Topic> result) {
			if (result != null) {
				topicList = result;
				Collections.sort(topicList, new Comparator<Topic>() {
					@Override
					public int compare(Topic lhs, Topic rhs) {
						return lhs.getName().compareTo(rhs.getName());
					}
				});
//				Map<Long,Integer> map = new HashMap<Long, Integer>();
//				int idx = 0;
//				for (Topic t : result) {
//					adapter.add(t.getName());
//					map.put(t.getSocialId(), idx++);
//				}
//				adapter.notifyDataSetChanged();
//				
//				if (selected != null) {
//					for (Long l : selected) {
//						View child = listView.getChildAt(map.get(l));
//						CheckBox chk = (CheckBox)child.findViewById(R.id.checklist_checkBox);
//						chk.setChecked(true);
//					}
//				}
				List<String> names = new ArrayList<String>(topicList.size());
				for (int i = 0; i < result.size(); i++) {
					names.add(result.get(i).getName());
				}
				
				adapter = new ArrayAdapter<String>(ctx, R.layout.checked_list_view, names);
				listView.setAdapter(adapter);
				listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


				if (selected != null) {
					for (int i = 0; i < listView.getCount(); i++) {
						listView.setItemChecked(i, selected.contains(result.get(i).socialId.toString()));
					}
				}

//				adapter = new TopicAdapter(result);
//				listView.setAdapter(adapter);
				dlg.show();
			}
		}
		
	}
	
	public static class Topic {
		private Long socialId;
		private String name;

		public Topic() {
			super();
		}

		public Long getSocialId() {
			return socialId;
		}
		public void setSocialId(Long socialId) {
			this.socialId = socialId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
//	private class TopicAdapter extends ArrayAdapter<Topic> {
//
//		public TopicAdapter(List<Topic> objects) {
//			super(ctx,  R.layout.checked_list_view, objects);
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			CheckedTextView row = (CheckedTextView)convertView;
//			Topic tag = getItem(position);
//
//			if (row == null) {
//				LayoutInflater inflater = ctx.getLayoutInflater();
//				row = (CheckedTextView)inflater.inflate(R.layout.checked_list_view, parent, false);
//				row.setTag(tag);
//			}
//
//			boolean checked = false;
//			if (selected != null) {
//				for (int i = 0; i < selected.size(); i++) {
//					if (tag.socialId.toString().equals(selected.get(i).toString())) {
//						checked = true;
//						break;
//					}
//				}
//			}
////			((CheckBox)row.findViewById(R.id.checklist_checkBox)).setChecked(checked);
////			((TextView)row.findViewById(R.id.checklist_textView)).setText(tag.getName());
//			row.setChecked(checked);
//			row.setText(tag.getName());
//			return row;
//		}
//		
//	}
}
