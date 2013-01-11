package eu.trentorise.smartcampus.communicator.custom.data;

import java.util.Collection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import eu.trentorise.smartcampus.communicator.model.Preference;
import eu.trentorise.smartcampus.protocolcarrier.ProtocolCarrier;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.ConnectionException;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.ProtocolException;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.SecurityException;
import eu.trentorise.smartcampus.storage.DataException;
import eu.trentorise.smartcampus.storage.StorageConfigurationException;
import eu.trentorise.smartcampus.storage.db.StorageConfiguration;
import eu.trentorise.smartcampus.storage.sync.SyncData;
import eu.trentorise.smartcampus.storage.sync.SyncStorageHelperWithPaging;

public class CommSyncStorageHelper extends SyncStorageHelperWithPaging {

	public CommSyncStorageHelper(Context context, String dbName, int version, StorageConfiguration config) {
		super(context, dbName, version, config);
	}

	protected void removeOld(int num) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		try {
			Cursor c = db.rawQuery("SELECT COUNT(*) FROM notifications WHERE starred = 0", null);
			c.moveToNext();
			int total = c.getInt(0);
			if (total > num) {
				int toDelete = total - num;
				c = db.rawQuery("SELECT id FROM notifications WHERE starred = 0 ORDER BY timestamp ASC", null);
				c.moveToFirst();
				for (int i = 0; i < toDelete; i++) {
					db.delete("notifications", "id = '"+c.getString(0)+"'", null);
					c.moveToNext();
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public SyncData synchronize(Context ctx, ProtocolCarrier mProtocolCarrier,
			String authToken, String appToken, String host, String service)
			throws SecurityException, ConnectionException, DataException,
			ProtocolException, StorageConfigurationException {
		SyncData data = super.synchronize(ctx, mProtocolCarrier, authToken, appToken, host, service);
		Collection<Preference> prefs = getObjects(Preference.class);
		int max = Constants.MAX_MESSAGE_NUM;
		if (prefs != null && !prefs.isEmpty()) {
			Preference userPrefs = prefs.iterator().next();
			Integer userMax = userPrefs.getMaxMessageNumber();
			if (userMax != null && userMax > 0) max = Math.min(userMax, max);
		}
		removeOld(max);
		return data;
	}
	
	
}
