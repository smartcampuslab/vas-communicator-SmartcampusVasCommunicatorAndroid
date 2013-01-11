package eu.trentorise.smartcampus.communicator.custom.data;

import eu.trentorise.smartcampus.communicator.R;
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
