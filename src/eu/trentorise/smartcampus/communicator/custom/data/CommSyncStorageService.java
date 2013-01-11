package eu.trentorise.smartcampus.communicator.custom.data;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import eu.trentorise.smartcampus.ac.Constants;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.storage.db.StorageConfiguration;
import eu.trentorise.smartcampus.storage.sync.SyncData;
import eu.trentorise.smartcampus.storage.sync.SyncStorageHelper;
import eu.trentorise.smartcampus.storage.sync.Utils;
import eu.trentorise.smartcampus.storage.sync.service.SyncStorageService;

public class CommSyncStorageService extends SyncStorageService {

	@Override
	public SyncStorageHelper getSyncStorageHelper(String appToken, StorageConfiguration config) {
		return new CommSyncStorageHelper(this, Utils.getDBName(this, appToken), Utils.getDBVersion(this, appToken), config);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDBUpdate(SyncData data, String appToken) {
        Intent i = new Intent("eu.trentorise.smartcampus.START");
        i.setPackage(this.getPackageName());

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        
        int icon = R.drawable.ic_n;
        CharSequence tickerText = getString(eu.trentorise.smartcampus.communicator.R.string.notification_title);
        long when = System.currentTimeMillis();
        CharSequence contentText = getString(eu.trentorise.smartcampus.communicator.R.string.notification_text);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);

        Notification notification = new Notification(icon, tickerText, when);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(this, tickerText, contentText, contentIntent);
        
        mNotificationManager.notify(Constants.ACCOUNT_NOTIFICATION_ID, notification);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void handleSecurityProblem(String appToken) {
        Intent i = new Intent("eu.trentorise.smartcampus.START");
        i.setPackage(this.getPackageName());

        CommunicatorHelper.getAccessProvider().invalidateToken(this, null);
        
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        
        int icon = R.drawable.stat_notify_error;
        CharSequence tickerText = getString(eu.trentorise.smartcampus.ac.R.string.token_expired);
        long when = System.currentTimeMillis();
        CharSequence contentText = getString(eu.trentorise.smartcampus.ac.R.string.token_required);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);

        Notification notification = new Notification(icon, tickerText, when);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(this, tickerText, contentText, contentIntent);
        
        mNotificationManager.notify(Constants.ACCOUNT_NOTIFICATION_ID, notification);
	}
}
