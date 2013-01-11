package eu.trentorise.smartcampus.communicator.custom.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import eu.trentorise.smartcampus.android.common.Utils;
import eu.trentorise.smartcampus.communicator.model.Action;
import eu.trentorise.smartcampus.communicator.model.Funnel;
import eu.trentorise.smartcampus.storage.db.BeanStorageHelper;

public class FunnelStorageHelper implements BeanStorageHelper<Funnel> {

	@SuppressWarnings("unchecked")
	@Override
	public Funnel toBean(Cursor cursor) {
		Funnel f = new Funnel();
		f.setId(cursor.getString(cursor.getColumnIndex("id")));
		f.setLabelIds(Utils.convertJSONToObjects(cursor.getString(cursor.getColumnIndex("labelIds")), String.class));
		f.setActions(Utils.convertJSONToObjects(cursor.getString(cursor.getColumnIndex("actions")), Action.class));
		f.setFunnelFilterData(Utils.convertJSONToObject(cursor.getString(cursor.getColumnIndex("funnelFilterData")), Map.class));
		f.setSourceType(cursor.getString(cursor.getColumnIndex("sourceType")));
		f.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		return f;
	}

	@Override
	public ContentValues toContent(Funnel bean) {
		ContentValues values = new ContentValues();

		if (bean.getLabelIds() == null) bean.setLabelIds(new ArrayList<String>());
		if (bean.getActions() == null) bean.setActions(new ArrayList<Action>());
		if (bean.getFunnelFilterData() == null) bean.setFunnelFilterData(new HashMap<String, Object>());

		values.put("id", bean.getId());
		values.put("labelIds", Utils.convertToJSON(bean.getLabelIds()));
		values.put("actions", Utils.convertToJSON(bean.getActions()));
		values.put("funnelFilterData", Utils.convertToJSON(bean.getFunnelFilterData()));
		values.put("title", bean.getTitle());
		values.put("sourceType", bean.getSourceType());
		
		return values;
	}

	@Override
	public Map<String,String> getColumnDefinitions() {
		Map<String,String> defs = new HashMap<String, String>();

		defs.put("actions", "TEXT");
		defs.put("labelIds", "TEXT");
		defs.put("funnelFilterData", "TEXT");
		defs.put("title", "TEXT");
		defs.put("sourceType", "TEXT");
		return defs;
	}

	@Override
	public boolean isSearchable() {
		return false;
	}

	
}
