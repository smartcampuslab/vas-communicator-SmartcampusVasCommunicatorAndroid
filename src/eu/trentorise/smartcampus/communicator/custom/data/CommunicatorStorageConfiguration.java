/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either   express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.communicator.custom.data;

import eu.trentorise.smartcampus.communicator.model.Channel;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.communicator.model.Preference;
import eu.trentorise.smartcampus.storage.BasicObject;
import eu.trentorise.smartcampus.storage.StorageConfigurationException;
import eu.trentorise.smartcampus.storage.db.BeanStorageHelper;
import eu.trentorise.smartcampus.storage.db.StorageConfiguration;

public class CommunicatorStorageConfiguration implements StorageConfiguration {
	private static final long serialVersionUID = 906503482979452854L;

	@SuppressWarnings("unchecked")
	private static Class<? extends BasicObject>[] classes = (Class<? extends BasicObject>[])new Class<?>[]{Channel.class, Preference.class, Notification.class};
	private static BeanStorageHelper<Channel> channelHelper = new ChannelStorageHelper();
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
		if (cls.equals(Channel.class)) {
			return "channels";
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BasicObject> BeanStorageHelper<T> getStorageHelper(Class<T> cls) throws StorageConfigurationException {
		if (cls.equals(Notification.class)) {
			return (BeanStorageHelper<T>) notificationHelper;
		}
		if (cls.equals(Channel.class)) {
			return (BeanStorageHelper<T>) channelHelper;
		}
		if (cls.equals(Preference.class)) {
			return (BeanStorageHelper<T>) prefHelper;
		}
		return null;
	}

}
