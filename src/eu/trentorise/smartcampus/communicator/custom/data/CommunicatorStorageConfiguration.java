package eu.trentorise.smartcampus.communicator.custom.data;

import eu.trentorise.smartcampus.communicator.model.Funnel;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.communicator.model.Preference;
import eu.trentorise.smartcampus.storage.BasicObject;
import eu.trentorise.smartcampus.storage.StorageConfigurationException;
import eu.trentorise.smartcampus.storage.db.BeanStorageHelper;
import eu.trentorise.smartcampus.storage.db.StorageConfiguration;

public class CommunicatorStorageConfiguration implements StorageConfiguration {
	private static final long serialVersionUID = 906503482979452854L;

	@SuppressWarnings("unchecked")
	private static Class<? extends BasicObject>[] classes = (Class<? extends BasicObject>[])new Class<?>[]{Funnel.class, Preference.class, Notification.class};
	private static BeanStorageHelper<Funnel> funnelHelper = new FunnelStorageHelper();
	private static BeanStorageHelper<Notification> notificationHelper = new NotificationStorageHelper();
	private static BeanStorageHelper<Preference> prefHelper = new PreferenceStorageHelper();
	
	@Override
	public Class<? extends BasicObject>[] getClasses() {
		return classes;
	}

	@Override
	public String getTableName(Class<? extends BasicObject> cls) throws StorageConfigurationException {
		if (cls.equals(Notification.class)) {
			return "notifications";
		}
		if (cls.equals(Preference.class)) {
			return "preferences";
		}
		if (cls.equals(Funnel.class)) {
			return "funnels";
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BasicObject> BeanStorageHelper<T> getStorageHelper(Class<T> cls) throws StorageConfigurationException {
		if (cls.equals(Notification.class)) {
			return (BeanStorageHelper<T>) notificationHelper;
		}
		if (cls.equals(Funnel.class)) {
			return (BeanStorageHelper<T>) funnelHelper;
		}
		if (cls.equals(Preference.class)) {
			return (BeanStorageHelper<T>) prefHelper;
		}
		return null;
	}

}
