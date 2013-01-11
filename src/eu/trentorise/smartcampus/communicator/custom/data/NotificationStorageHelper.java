package eu.trentorise.smartcampus.communicator.custom.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import eu.trentorise.smartcampus.android.common.Utils;
import eu.trentorise.smartcampus.communicator.model.EntityObject;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.storage.db.BeanStorageHelper;

public class NotificationStorageHelper implements BeanStorageHelper<Notification> {

	@SuppressWarnings("unchecked")
	@Override
	public Notification toBean(Cursor cursor) {
		Notification n = new Notification();
		n.setId(cursor.getString(cursor.getColumnIndex("id")));
		n.setLabelIds(Utils.convertJSONToObjects(cursor.getString(cursor.getColumnIndex("labelIds")), String.class));
		n.setContent(Utils.convertJSONToObject(cursor.getString(cursor.getColumnIndex("content")), Map.class));
		n.setDescription(cursor.getString(cursor.getColumnIndex("description")));
		n.setEntities(Utils.convertJSONToObjects(cursor.getString(cursor.getColumnIndex("entities")), EntityObject.class));
		n.setFunnelId(cursor.getString(cursor.getColumnIndex("funnelId")));
		n.setReaded(cursor.getInt(cursor.getColumnIndex("readed")) > 0);
		n.setStarred(cursor.getInt(cursor.getColumnIndex("starred")) > 0);
		n.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
		n.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		n.setType(cursor.getString(cursor.getColumnIndex("type")));
		
		return n;
	}

	@Override
	public ContentValues toContent(Notification bean) {
		ContentValues values = new ContentValues();

		if (bean.getLabelIds() == null) bean.setLabelIds(new ArrayList<String>());
		if (bean.getContent() == null) bean.setContent(new HashMap<String, Object>());
		if (bean.getEntities() == null) bean.setEntities(new ArrayList<EntityObject>());

		values.put("id", bean.getId());
		values.put("labelIds", Utils.convertToJSON(bean.getLabelIds()));
		values.put("entities", Utils.convertToJSON(bean.getEntities()));
		values.put("content", Utils.convertToJSON(bean.getContent()));
		values.put("title", bean.getTitle());
		values.put("type", bean.getType());
		values.put("funnelId", bean.getFunnelId());
		values.put("description", bean.getDescription());
		values.put("timestamp", bean.getTimestamp());
		values.put("starred", bean.isStarred() ? 1 : 0);
		values.put("readed", bean.isReaded() ? 1 : 0);
		
		return values;
	}

	@Override
	public Map<String,String> getColumnDefinitions() {
		Map<String,String> defs = new HashMap<String, String>();

		defs.put("entities", "TEXT");
		defs.put("labelIds", "TEXT");
		defs.put("content", "TEXT");
		defs.put("title", "TEXT");
		defs.put("type", "TEXT");
		defs.put("description", "TEXT");
		defs.put("funnelId", "TEXT");
		defs.put("timestamp", "INTEGER");
		defs.put("starred", "INTEGER");
		defs.put("readed", "INTEGER");
		return defs;
	}

	@Override
	public boolean isSearchable() {
		return true;
	}
}
