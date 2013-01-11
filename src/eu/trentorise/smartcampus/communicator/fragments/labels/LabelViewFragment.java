package eu.trentorise.smartcampus.communicator.fragments.labels;

import android.os.Bundle;
import eu.trentorise.smartcampus.communicator.fragments.messages.AbstractMessageListFragment;
import eu.trentorise.smartcampus.communicator.model.LabelObject;
import eu.trentorise.smartcampus.communicator.model.NotificationFilter;

public class LabelViewFragment extends AbstractMessageListFragment {

	LabelObject label = null;
	public static final String ARG_LABEL = "label";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		label = (LabelObject)getArguments().getSerializable(ARG_LABEL);
	}

	@Override
	protected CharSequence getTitle() {
		return "Label: "+label.getName();
	}

	@Override
	protected NotificationFilter initFilter() {
		NotificationFilter filter = new NotificationFilter();
		filter.setLabelId(label.getId());
		return filter;
	}
	@Override
	protected boolean hasFunnelSelector() {
		return true;
	}
	@Override
	protected boolean hasLabelSelector() {
		return false;
	}
	@Override
	protected boolean isFunnelSelectorEnabled() {
		return true;
	}
	@Override
	protected boolean isLabelSelectorEnabled() {
		return false;
	}
}