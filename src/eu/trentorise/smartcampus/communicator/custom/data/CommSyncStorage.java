package eu.trentorise.smartcampus.communicator.custom.data;

import android.content.Context;
import eu.trentorise.smartcampus.storage.db.StorageConfiguration;
import eu.trentorise.smartcampus.storage.sync.SyncStorageHelper;
import eu.trentorise.smartcampus.storage.sync.SyncStorageWithPaging;

public class CommSyncStorage extends SyncStorageWithPaging {

	public CommSyncStorage(Context context, String appToken, String dbName, int dbVersion, StorageConfiguration config) {
		super(context, appToken, dbName, dbVersion, config);
	}

	@Override
	protected SyncStorageHelper createHelper(Context context, String dbName, int dbVersion, StorageConfiguration config) {
		return new CommSyncStorageHelper(context, dbName, dbVersion, config);
	}


	
}
