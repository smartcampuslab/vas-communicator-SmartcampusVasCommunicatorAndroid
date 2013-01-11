package eu.trentorise.smartcampus.communicator.custom;

import android.app.Activity;
import android.util.Log;
import eu.trentorise.smartcampus.ac.SCAccessProvider;
import eu.trentorise.smartcampus.android.common.HandleExceptionHelper;
import eu.trentorise.smartcampus.android.common.SCAsyncTask.SCAsyncTaskProcessor;
import eu.trentorise.smartcampus.communicator.HomeActivity;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.data.CommunicatorHelper;

public abstract class AbstractAsyncTaskProcessor<Params, Result> implements SCAsyncTaskProcessor<Params, Result>{

	private Activity activity;
	
	public AbstractAsyncTaskProcessor(Activity activity) {
		super();
		this.activity = activity;
	}

	
	@Override
	public void handleConnectionError() {
		// TODO Auto-generated method stub
		
		HandleExceptionHelper.showDialogConnectivity(activity);
	}


	@Override
	public void handleFailure(Exception e) {
		Log.e(activity.getClass().getName(), ""+e.getMessage());
		CommunicatorHelper.showFailure(activity, R.string.app_failure_operation);
	}

	@Override
	public void handleSecurityError() {
		SCAccessProvider accessProvider =  CommunicatorHelper.getAccessProvider();
		try {
			accessProvider.invalidateToken(activity, null);
			accessProvider.getAuthToken(activity, null);
		} catch (Exception e) {
			Log.e(HomeActivity.class.getName(), ""+e.getMessage());
			CommunicatorHelper.showFailure(activity, R.string.app_failure_security);
		}
	}

}
