package eu.trentorise.smartcampus.communicator.custom;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

public interface FunnelFilterRenderer {

	void setUpView(AlertDialog dlg, Activity ctx, View view, Map<String,Object> funnelFilterData);
	Map<String,Object> validate(View view);
	
	CharSequence getFilterDataDescription(Map<String,Object> funnelFilterData);
}
