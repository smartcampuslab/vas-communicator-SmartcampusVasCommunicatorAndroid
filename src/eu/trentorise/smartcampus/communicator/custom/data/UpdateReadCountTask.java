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

import it.smartcampuslab.communicator.R;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

public class UpdateReadCountTask extends AsyncTask<Void, Void, Integer> {

	private View view;
	
	public UpdateReadCountTask(View view) {
		super();
		this.view = view;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		return CommunicatorHelper.readUnreadCount();
	}

	@Override
	protected void onPostExecute(Integer unread) {
    	int resource = R.drawable.inbox;
    	String txt = "";
    	if (unread != 0) {
    		resource = R.drawable.inbox_alert;
    		txt = ""+unread;
    	}
    	((TextView)view.findViewById(R.id.numberView)).setText(txt);
    	view.findViewById(R.id.containerView).setBackgroundResource(resource);
	}
	
}
