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

package eu.trentorise.smartcampus.communicator.renderers;

import android.content.Context;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.NotificationProcessor;
import eu.trentorise.smartcampus.communicator.model.EntityObject;
import eu.trentorise.smartcampus.communicator.model.Notification;

/**
 * @author raman
 *
 */
public class CMProcessor implements NotificationProcessor {

	private static final String TYPE_EVENT = "event";
	private static final String TYPE_LOCATION = "location";
	private static final String TYPE_STORY = "story";

	@Override
	public void processMessage(Context ctx, Notification notification) {
		String desc = null;
		
		EntityObject event = null;
		EntityObject location = null;
		EntityObject story = null;

		for (EntityObject eb : notification.getEntities()) {
			String type = eb.getType();

			if (type.equalsIgnoreCase(TYPE_EVENT)) {
				event = eb;
			} else if (type.equalsIgnoreCase(TYPE_LOCATION)) {
				location = eb;
			} else if (type.equalsIgnoreCase(TYPE_STORY)) {
				story = eb;
			}
		}

		if (notification.getEntities().size() == 2) {
			// new
			if (event != null && location != null) {
				desc = ctx.getString(R.string.notifications_event_new, event.getTitle(), location.getTitle());
			} else if (location != null && story != null) {
				desc = ctx.getString(R.string.notifications_story_new, story.getTitle(), location.getTitle());
			}
		} else {
			if (event != null) {
				desc = ctx.getString(R.string.notifications_event_updated, event.getTitle());
			} else if (location != null) {
				desc = ctx.getString(R.string.notifications_location_updated, location.getTitle());
			} else if (story != null) {
				desc = ctx.getString(R.string.notifications_story_updated, story.getTitle());
			}
		}
		
		// missing custom data
		if (desc == null) {
			notification.setDescription(notification.getTitle() + "\n" + notification.getDescription());
		} else {
			notification.setDescription(desc);
		}


	}


}
