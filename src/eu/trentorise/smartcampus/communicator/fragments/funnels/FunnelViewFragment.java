package eu.trentorise.smartcampus.communicator.fragments.funnels;

import android.os.Bundle;
import eu.trentorise.smartcampus.communicator.fragments.messages.AbstractMessageListFragment;
import eu.trentorise.smartcampus.communicator.model.Funnel;
import eu.trentorise.smartcampus.communicator.model.NotificationFilter;

public class FunnelViewFragment extends AbstractMessageListFragment {

	private Funnel funnel = null;
	public static final String ARG_FUNNEL = "funnel";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		funnel = (Funnel)getArguments().getSerializable(ARG_FUNNEL);
	}

	@Override
	protected CharSequence getTitle() {
		return "Channel: "+(funnel == null ? "" : funnel.getTitle());
	}

	@Override
	protected NotificationFilter initFilter() {
		NotificationFilter filter = new NotificationFilter();
		filter.setFunnelId(funnel == null? null : funnel.getId());
		return filter;
	}
	@Override
	protected boolean hasFunnelSelector() {
		return false;
	}
	@Override
	protected boolean hasLabelSelector() {
		return true;
	}
	@Override
	protected boolean isFunnelSelectorEnabled() {
		return false;
	}
	@Override
	protected boolean isLabelSelectorEnabled() {
		return true;
	}
}